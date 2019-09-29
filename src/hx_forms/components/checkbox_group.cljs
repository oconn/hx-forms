(ns hx-forms.components.checkbox-group
  (:require
   [hx.react :refer [defnc]]
   [hx.hooks :as hooks]

   [hx-forms.components.field :refer [Field]]
   [hx-forms.utils :as u]))

(def node-key :hx/checkbox-group)

(defnc CheckboxComponent
  [{:keys [selected label on-change]
    :or {on-change identity}}]
  (let [!ref (hooks/useIRef nil)]
    [:div {:class ["hx-forms--checkbox-container"]}
     [:button {:class ["hx-forms--checkbox-button-container"]
               :on-click #(do
                            (.preventDefault %)
                            (on-change (not selected)))
               :id label
               :type :button
               :ref #(reset! !ref %)}

      (when (true? selected)
        [:span {:class ["hx-forms--checkbox-checkmark"]} "✓"])]

     [:label {:on-click #(js-invoke @!ref "click")
              :class ["hx-forms--checkbox-label"]}
      label]]))

(defnc Option
  [{:keys [option
           current-value
           on-change
           field-key]}]
  (let [{:keys [value display]} option]
    [:li {:class ["hx-forms--checkbox-group-option-container"]}
     [CheckboxComponent
      {:on-change (fn [selected]
                    (on-change (if (true? selected)
                                 (conj current-value value)
                                 (disj current-value value))))
       :label display
       :selected (contains? current-value value)}]]))

(defnc CheckboxGroup
  [{:keys [node update-state form-state is-submitting]}]
  (let [field-key
        (u/get-field-key node node-key)

        field-value
        (u/get-field-value form-state field-key)

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
       (str "No options were provided to checkbox group component " field-key)))

    (hooks/useEffect
     (fn []
       (u/initialize-field! {:node node
                             :node-key node-key
                             :update-state update-state
                             :defaults {:default-value #{}}}))
     ["on-mount"])

    [Field {:errors errors
            :label (:label hx-props)
            :field-key field-key
            :visible is-visible}
     [:ul {:class ["hx-forms--checkbox-group-container"]}
      (for [option options]
        ^{:key (:value option)}
        [Option {:option option
                 :current-value field-value
                 :on-change (partial u/on-change!
                                     {:update-state update-state
                                      :field-key field-key
                                      :get-value identity
                                      :callback on-change})
                 :field-key field-key}])]]))
