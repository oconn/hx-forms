(ns hx-forms.components.checkbox
  (:require
   [hx.react :refer [defnc]]
   [hx.hooks :as hooks]))

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
               :ref !ref}

      (when (true? checked)
        [:span {:class ["hx-forms--checkbox-checkmark"]} "✓"])]

     (when (some? label)
       [:label {:on-click #(js-invoke @!ref "click")
                :class ["hx-forms--checkbox-label"]}
        label])]))

;; TODO Write hx-forms filed component
