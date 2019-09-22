(ns hx-forms.components.custom-field
  (:require
   [hx.react :refer [defnc]]

   [hx-forms.utils :as u]
   [hx-forms.components.field :refer [Field]]))

(def node-key :hx/custom-field)

(defnc CustomField
  [{:keys [node form-state] :as hx-field-props}]
  (let [field-key
        (u/get-field-key node node-key)

        errors
        (u/get-field-errors form-state field-key)

        is-visible
        (u/get-field-visibility form-state field-key)

        [{:keys [label render]} _]
        (u/get-field-props node node-key)]
    [Field {:errors errors
            :label label
            :field-key field-key
            :visible is-visible}
     [render hx-field-props]]))
