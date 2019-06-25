(ns hx-forms.components.submit-button
  (:require
   [hx.react :refer [defnc]]

   [hx-forms.utils :as u]))

(def node-key :hx/submit-button)

(defnc SubmitButton
  [{:keys [node]}]
  (let [[hx-props node-props] (u/get-field-props node node-key)]
    (u/remove-hx-props node node-key)))
