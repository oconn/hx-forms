(ns hx-forms.components.select
  (:require
   [goog.object :as gobj]
   [hx.react :refer [defnc]]
   [hx.hooks :as hooks]

   [hx-forms.components.field :refer [Field]]
   [hx-forms.utils :as u]))

(def node-key :hx/select)

(defonce no-selection-value "hx-forms--select-no-selection")

(defnc Select
  [{:keys [node update-state form-state is-submitting]}]
  (let [field-key
        (u/get-field-key node node-key)

        errors
        (u/get-field-errors form-state field-key)

        is-visible
        (u/get-field-visibility form-state field-key)

        [{:keys [on-change options disabled default-value select-option-text]
          :as hx-props
          :or {on-change identity
               select-option-text "Select an option"}} _]
        (u/get-field-props node node-key)]


    (when (nil? options)
      (js/console.error
       (str "No options were provided to select component " field-key)))

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
     [:select {:class ["hx-forms--select-element"]
               :on-change #(let [value (gobj/getValueByKeys % "target" "value")]
                             (u/on-change!
                              {:update-state update-state
                               :field-key field-key
                               :get-value identity
                               :callback on-change}
                              (if (= no-selection-value value)
                                nil value)))
               :default-value default-value
               :disabled (or disabled is-submitting)}
      (for [{:keys [value display]}
            (into [{:value no-selection-value :display select-option-text}]
                  options)]
        ^{:key value}
        [:option {:value value} display])]]))
