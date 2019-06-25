(ns hx-forms.core
  (:require
   [clojure.walk :refer [postwalk]]
   [hx.react :as hx :refer [defnc]]
   [hx.hooks :as hooks]

   [hx-forms.utils :as u]
   [hx-forms.components.form :as form]
   [hx-forms.components.input :as input]
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

        (hx-node-type? node submit-button/node-key)
        [submit-button/SubmitButton hx-comp-props]

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

        ;; (rf-node? node :rf/custom-field)
        ;; (render-custom-field {:node node
        ;;                       :form-state form-state
        ;;                       :is-submitting is-submitting})

        :else
        node))))

(defn- apply-field-initialization
  [state {:keys [field-key field-state]}]
  (if (keyword? field-key)
    (assoc state field-key field-state)
    (do
      (js/console.error
       "Initialized field must contain the the keyword 'field-key'")
      state)))

(defn- apply-field-change
  [state {:keys [field-key value]}]
  (assoc-in state [field-key :value] value))

(defn- process-validator
  [field-value form-state]
  (fn [errors {:keys [validator error]}]
    (if (false? (validator field-value form-state))
      (conj errors error)
      errors)))

(defn- apply-field-validation
  [state {:keys [field-key]}]
  (let [field-value (u/get-field-value state field-key)
        field-validators (u/get-field-validators state field-key)
        errors (reduce (process-validator field-value state)
                       []
                       field-validators)]
    (assoc-in state [field-key :errors] errors)))

(defn- form-state-reducer
  [state {:keys [action payload]}]
  (let [new-state
        (case action
          :initialize-field (apply-field-initialization state payload)
          :change-field (apply-field-change state payload)
          :validate-field (apply-field-validation state payload)

          (do
            (js/console.error "Invalid state-reducer action '" action "'")
            state))]
    new-state))

(defnc Form
  [{:keys [on-mount on-unmount body]
    :or {on-mount identity
         on-unmount identity}
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
                          :update-state update-state})
              body)))

(def HXForm Form)
