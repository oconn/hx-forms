(ns hx-forms.utils
  (:require [clojure.spec.alpha :as s]))

(s/def ::value any?)
(s/def ::initial-value any?)
(s/def ::error string?)
(s/def ::errors (s/coll-of ::error))
(s/def ::is-visible boolean?)
(s/def ::visibility-validator ifn?)
(s/def ::visibility (s/keys :req-un [::is-visible
                                     ::visibility-validator]))
(s/def ::formatter ifn?)
(s/def ::formatters (s/coll-of ::formatter))
(s/def ::validator ifn?)
(s/def ::validator (s/keys :req-un [::validator
                                    ::error]))
(s/def ::transformer ifn?)
(s/def ::transformers (s/coll-of ::transformer))

(s/def ::field-state (s/keys :req-un [::value
                                      ::initial-value
                                      ::errors
                                      ::visibility
                                      ::formatters
                                      ::transformers
                                      ::validators]))

(s/def ::form-state (s/map-of keyword ::field-state))

(defn- format-field-state
  [{:keys [hx-props node-props defaults]
    :or {defaults {}}}]
  (let [initial-value (or (:default-value node-props)
                          (:default-value hx-props)
                          (:default-value defaults))]

    {:value initial-value
     :initial-value initial-value
     :errors []
     :visibility {:is-visible true
                  :visibility-validator (or (:visibility hx-props)
                                            (:visibility defaults)
                                            (constantly true))}
     :formatters (or (:formatters hx-props)
                     (:formatters defaults)
                     [])
     :validators (or (:validators hx-props)
                     (:validators defaults)
                     [])
     :transformers (or (:transformers hx-props)
                       (:transformers defaults)
                       [identity])}))

(defn contains-errors?
  [form-state]
  (->> form-state
       vals
       (map :errors)
       (flatten)
       (count)
       (pos?)))

(defn get-node-props
  [node node-key]
  (-> node second (dissoc node-key)))

(defn get-hx-props
  [node node-key]
  (-> node second node-key))

(defn get-field-props
  [node node-key]
  [(get-hx-props node node-key) (get-node-props node node-key)])

(defn get-field-key
  [node node-key]
  (-> node
      (get-hx-props node-key)
      (:field-key)))

(defn get-field
  [state field-key]
  (get state field-key))

(defn get-field-value
  [state field-key]
  (get-in state [field-key :value]))

(defn get-field-formatters
  [state field-key]
  (get-in state [field-key :formatters]))

(defn get-field-validators
  [state field-key]
  (get-in state [field-key :validators]))

(defn get-field-errors
  [state field-key]
  (get-in state [field-key :errors]))

(defn get-field-visibility
  [state field-key]
  (get-in state [field-key :visibility :is-visible]))

(defn- process-visibility
  [form-state]
  form-state)

(defn- process-validator
  [field-value form-state]
  (fn [errors {:keys [validator error]}]
    (if (false? (validator field-value form-state))
      (conj errors error)
      errors)))

(defn process-validators
  [field-validators field-value form-state]
  (reduce (process-validator field-value form-state)
          []
          field-validators))

(defn process-all-validators
  [form-state]
  (reduce-kv
   (fn [updated-state field-key field]
     (let [{:keys [value validators visibility]}
           field

           {:keys [is-visible]}
           visibility

           updated-field
           (if (false? is-visible)
             (assoc field :errors [])
             (assoc field :errors (process-validators validators
                                                      value
                                                      form-state)))]
       (assoc updated-state field-key updated-field)))
   {}
   form-state))

(defn- process-formatter
  [form-state]
  (fn [field-value formatter]
    (formatter field-value form-state)))

(defn process-formatters
  [field-formatters field-value form-state]
  (reduce (process-formatter form-state)
          field-value
          field-formatters))

(defn remove-hx-props
  [node node-key]
  (assoc node 1 (dissoc (get node 1) node-key)))

(defn merge-with-props
  [node value]
  (update-in node [1] merge value))

(defn form-state->values
  [form-state]
  (reduce
   (fn [acc [field-key {:keys [value visibility transformers]}]]
     (let [transform-with (apply comp transformers)]
       (if (true? (:is-visible visibility))
         (assoc acc field-key (transform-with value))
         acc)))
   {}
   form-state))

(defn on-change!
  [{:keys [field-key update-state get-value callback]} e]
  (update-state
   {:action :change-field
    :payload {:field-key field-key
              :value (get-value e)}})

  (when (some? callback)
    (callback e)))

(defn validate-field!
  [{:keys [update-state field-key callback]} e]
  (update-state
   {:action :validate-field
    :payload {:field-key field-key}})

  (when (some? callback)
    (callback e)))

(defn initialize-field!
  [{:keys [node node-key update-state defaults]}]
  (let [[hx-props node-props] (get-field-props node node-key)]
    (update-state
     {:action :initialize-field
      :payload {:field-key (get-field-key node node-key)
                :field-state (format-field-state {:hx-props hx-props
                                                  :node-props node-props
                                                  :defaults defaults})}})))
