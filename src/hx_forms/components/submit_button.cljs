(ns hx-forms.components.submit-button
  (:require
   [hx.react :refer [defnc]]

   [hx-forms.utils :as u]))

(def node-key :hx/submit-button)

(defnc SubmitButton
  [{:keys [node is-submitting]}]
  [:div {:class ["hx-forms--submit-button-container"]}
   (assoc-in (u/remove-hx-props node node-key)
             [1 :disabled] is-submitting)])
