(ns hx-forms.components.submit-button
  (:require
   [hx.react :refer [defnc]]

   [hx-forms.utils :as u]))

(def node-key :hx/submit-button)

(defnc SubmitButton
  [{:keys [node]}]
  (let [hx-params (u/get-hx-params node node-key)]
    (u/remove-node-params node node-key)))
