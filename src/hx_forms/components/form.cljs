(ns hx-forms.components.form
  (:require
   [hx.react :refer [defnc]]
   [hx.hooks :as hooks]

   [hx-forms.utils :as u]))

(def node-key :hx/form)

(defnc Form
  [{:keys [node form-state update-state]}]
  (let [[hx-props node-props] (u/get-field-props node node-key)
        on-submit (or (:on-submit node-props)
                      (:on-submit hx-props)
                      identity)]
    (-> node
        (u/remove-hx-props node-key)
        (u/merge-with-props
         {:on-submit
          (fn [e]
            (js-invoke e "preventDefault")

            (let [validated-form-state (u/process-all-validators form-state)]
              (if (u/contains-errors? validated-form-state)
                (update-state {:action :set-form-state
                               :payload validated-form-state})
                (on-submit form-state e))))}))))
