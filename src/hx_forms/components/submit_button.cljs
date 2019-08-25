(ns hx-forms.components.submit-button
  (:require
   [hx.react :refer [defnc]]

   [hx-forms.utils :as u]))

(def node-key :hx/submit-button)

(defnc SubmitButton
  [{:keys [node is-submitting form-state]}]
  (let [has-form-errors (u/contains-errors? form-state)]
    [:div {:class ["hx-forms--submit-button-outer-container"]}
     (when (true? has-form-errors)
       [:p {:class ["hx-forms--submit-button-form-error-message"]}
        "Please address the errors above before continuing"])

     [:div {:class ["hx-forms--submit-button-container"]}
      (cond-> (assoc-in (u/remove-hx-props node node-key)
                        [1 :disabled] is-submitting)

        (true? has-form-errors)
        (assoc-in [1 :disabled] true))]]))
