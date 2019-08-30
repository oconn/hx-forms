(ns hx-forms.core
  (:require
   [clojure.walk :refer [postwalk]]
   [hx.react :as hx :refer [defnc]]
   [hx.hooks :as hooks]

   [hx-forms.utils :as u]
   [hx-forms.components.custom-field :as custom-field]
   [hx-forms.components.form :as form]
   [hx-forms.components.input :as input]
   [hx-forms.components.select :as select]
   [hx-forms.components.toggle :as toggle]
   [hx-forms.components.submit-button :as submit-button]))

(def ^{:private true} hx-field-params #{:field-key})

(defn- hx-node-type?
  "Checks to see if any given node is an hx field of a specific type"
  [node key]
  (and (coll? node)
       (contains? (second node) key)))

(defn- walk-node
  "Walks the form replacing rf fields with form aware components"
  [{:keys [form-spec form-state update-state]}]
  (fn [node]
    (let [{:keys [is-submitting]} form-spec
          hx-comp-props {:form-state form-state
                         :is-submitting is-submitting
                         :update-state update-state
                         :node node}]
      (cond
        (hx-node-type? node form/node-key)
        [form/Form hx-comp-props]

        (hx-node-type? node input/node-key)
        [input/Input hx-comp-props]

        (hx-node-type? node select/node-key)
        [select/Select hx-comp-props]

        (hx-node-type? node submit-button/node-key)
        [submit-button/SubmitButton hx-comp-props]

        (hx-node-type? node toggle/node-key)
        [toggle/Toggle hx-comp-props]

        (hx-node-type? node :hx/custom-field)
        [custom-field/CustomField hx-comp-props]

        ;; (rf-node? node :rf/select-input)
        ;; [select-input/mount-select-input {:node node
        ;;                                   :form-state form-state
        ;;                                   :is-submitting is-submitting}]

        ;; (rf-node? node :rf/field-error)
        ;; [field-error/mount-field-error {:node node
        ;;                                 :form-state form-state}]

        ;; (rf-node? node :rf/field-hint)
        ;; [field-hint/mount-field-hint {:node node
        ;;                               :form-state form-state}]

        ;; (rf-node? node :rf/form-errors)
        ;; [form-errors/mount-form-errors {:node node
        ;;                                 :form-state form-state}]

        ;; (rf-node? node :rf/submit-button)
        ;; [submit-button/mount-submit-button {:node node
        ;;                                     :form-state form-state
        ;;                                     :is-submitting is-submitting}]

        ;; (rf-node? node :rf/hidden-field)
        ;; [hidden-field/mount-hidden-field {:node node
        ;;                                   :form-state form-state}]

        :else
        node))))

(defn- apply-field-initialization
  "Registers the field with form-state"
  [state {:keys [field-key field-state]}]
  (if (keyword? field-key)
    (assoc state field-key field-state)
    (do
      (js/console.error
       "Initialized field must contain the the keyword 'field-key'")
      state)))

(defn- apply-field-change
  "Applies changes to a field"
  [state {:keys [field-key value]}]
  (let [{:keys [errors validators formatters]} (u/get-field state field-key)
        new-value (u/process-formatters formatters value state)
        new-errors (if (pos? (count errors))
                     (u/process-validators validators new-value state)
                     errors)]
    (update state field-key merge {:value new-value
                                   :errors new-errors})))

(defn- apply-field-validation
  "Applies validation to a field updating it's errors if any."
  [state {:keys [field-key]}]
  (let [field-value (u/get-field-value state field-key)
        field-validators (u/get-field-validators state field-key)
        errors (u/process-validators field-validators field-value state)]
    (assoc-in state [field-key :errors] errors)))

(defn- apply-form-reset
  "Sets the value of each field in the form back to its initial value
  on initialization."
  [state]
  (reduce-kv
   (fn [updated-state field-key {:keys [initial-value] :as field}]
     (assoc updated-state field-key (-> field
                                        (assoc :value initial-value)
                                        (assoc :errors []))))
   {}
   state))

(defn- form-state-reducer
  [state {:keys [action payload]}]
  (let [new-state
        (case action
          :change-field (apply-field-change state payload)
          :initialize-field (apply-field-initialization state payload)
          :set-form-state payload
          :validate-field (apply-field-validation state payload)
          :reset-form-state (apply-form-reset state)

          (do
            (js/console.error "Invalid state-reducer action '" action "'")
            state))]
    new-state))

(defnc Form
  [{:keys [on-mount on-unmount is-submitting body]
    :or {on-mount identity
         on-unmount identity
         is-submitting false}
    :as form-spec}]
  (let [[form-state update-state] (hooks/useReducer form-state-reducer {})]

    (hooks/useEffect
     (fn []
       (on-mount {:form-state form-state
                  :reset-form #()})
       (fn []
         (on-unmount)))
     ["on-mount"])

    (postwalk (walk-node {:form-spec form-spec
                          :form-state form-state
                          :update-state update-state
                          :is-submitting is-submitting})
              body)))

(def HXForm Form)
