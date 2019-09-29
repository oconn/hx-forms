(ns hx-forms.components.radio-group
  (:require
   [hx.react :refer [defnc]]
   [hx.hooks :as hooks]

   [hx-forms.components.field :refer [Field]]
   [hx-forms.utils :as u]))

(def node-key :hx/radio-group)

(defnc Option
  [{:keys [option
           current-value
           update-current-value
           on-change
           field-key]}]
  (let [{:keys [value display]} option
        on-select-option #(do
                            (.preventDefault %)
                            (update-current-value value)
                            (on-change value))]
    [:li {:class ["hx-forms--radio-group-option-container"]}
     [:button (cond-> {:class ["hx-forms--radio-group-option-button"]
                       :on-click on-select-option}
                (= current-value value)
                (update :class
                        conj
                        "hx-forms--radio-group-option-button-selected"))]
     [:label {:on-click on-select-option} display]]))

(defnc RadioGroupComponent
  [{:keys [on-change options disabled default-value field-key]
    :or {disabled false
         on-change identity
         options []}}]
  (let [[current-value update-current-value] (hooks/useState default-value)]
    [:ul {:class ["hx-forms--radio-group-container"]}
     (for [option options]
       ^{:key (:value option)}
       [Option {:option option
                :current-value current-value
                :update-current-value update-current-value
                :on-change on-change
                :field-key field-key}])]))

(defnc RadioGroup
  [{:keys [node update-state form-state is-submitting]}]
  (let [field-key
        (u/get-field-key node node-key)

        errors
        (u/get-field-errors form-state field-key)

        is-visible
        (u/get-field-visibility form-state field-key)

        [{:keys [on-change options disabled default-value]
          :as hx-props
          :or {on-change identity}} _]
        (u/get-field-props node node-key)]

    (when (nil? options)
      (js/console.error
       (str "No options were provided to radio group component " field-key)))

    (hooks/useEffect
     (fn []
       (u/initialize-field! {:node node
                             :node-key node-key
                             :update-state update-state
                             :defaults {:default-value nil}}))
     ["on-mount"])

    [Field {:errors errors
            :label (:label hx-props)
            :field-key field-key
            :visible is-visible}
     [RadioGroupComponent {:on-change (partial u/on-change!
                                               {:update-state update-state
                                                :field-key field-key
                                                :get-value identity
                                                :callback on-change})
                           :disabled (or disabled is-submitting)
                           :options options
                           :default-value default-value
                           :field-key field-key}]]))
