(ns hx-forms.components.checkbox
  (:require
   [hx.react :refer [defnc]]
   [hx.hooks :as hooks]

   [hx-forms.components.field :refer [Field]]
   [hx-forms.utils :as u]))

(def node-key :hx/checkbox)

(defnc CheckboxComponent
  [{:keys [default-value label on-change]
    :or {default-value false
         on-change identity}}]
  (let [[checked toggle-checked-state] (hooks/useState default-value)
        !ref (hooks/useIRef nil)]
    [:div {:class ["hx-forms--checkbox-container"]}
     [:button {:class ["hx-forms--checkbox-button-container"]
               :on-click #(do
                            (.preventDefault %)
                            (on-change (not checked))
                            (toggle-checked-state (not checked)))
               :id label
               :type :button
               :ref #(reset! !ref %)}

      (when (true? checked)
        [:span {:class ["hx-forms--checkbox-checkmark"]} "✓"])]

     (when (some? label)
       [:label {:on-click #(js-invoke @!ref "click")
                :class ["hx-forms--checkbox-label"]}
        label])]))

(defnc Checkbox
  [{:keys [node update-state form-state is-submitting]}]
  (let [field-key
        (u/get-field-key node node-key)

        errors
        (u/get-field-errors form-state field-key)

        is-visible
        (u/get-field-visibility form-state field-key)

        [{:keys [on-change disabled default-value]
          :as hx-props
          :or {on-change identity}} _]
        (u/get-field-props node node-key)]

    (hooks/useEffect
     (fn []
       (u/initialize-field! {:node node
                             :node-key node-key
                             :update-state update-state
                             :defaults {:default-value false}}))
     ["on-mount"])

    [Field {:errors errors
            :label (:label hx-props)
            :field-key field-key
            :classname ["hx-forms--checkbox-field-container"]
            :visible is-visible}
     [CheckboxComponent {:on-change (partial u/on-change!
                                             {:update-state update-state
                                              :field-key field-key
                                              :get-value identity
                                              :callback on-change})
                         :disabled (or disabled is-submitting)
                         :default-value default-value}]]))
