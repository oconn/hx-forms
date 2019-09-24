(ns hx-forms.reducer
  (:require [hx-forms.utils :as u]))

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
  (let [is-visible (u/get-field-visibility state field-key)
        field-value (u/get-field-value state field-key)
        field-validators (u/get-field-validators state field-key)
        errors (if (false? is-visible)
                 []
                 (u/process-validators field-validators field-value state))]
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

(defn- apply-visibility-checks
  [state]
  (reduce-kv
   (fn [updated-state field-key {:keys [visibility] :as field}]
     (let [{:keys [visibility-validator is-visible]} visibility
           updated-visibility (visibility-validator state)
           updated-field (cond-> (assoc-in field
                                           [:visibility :is-visible]
                                           updated-visibility)
                           (and (true? is-visible)
                                (false? updated-visibility))
                           (assoc :errors []))]
       (assoc updated-state field-key updated-field)))
   {}
   state))

(defn form-state-reducer
  [on-change]
  (fn [state {:keys [action payload]}]
    (let [new-state
          (case action
            :change-field (-> state
                              (apply-field-change payload)
                              (apply-visibility-checks))
            :initialize-field (apply-field-initialization state payload)
            :set-form-state payload
            :validate-field (apply-field-validation state payload)
            :reset-form-state (apply-form-reset state)
            :calculate-visibility (apply-visibility-checks state)

            (do
              (js/console.error "Invalid state-reducer action '" action "'")
              state))]

      (on-change new-state)

      new-state)))
