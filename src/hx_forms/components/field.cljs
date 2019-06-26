(ns hx-forms.components.field
  (:require
   [hx.react :refer [defnc]]))

(defnc Field
  [{:keys [errors label field-key children]}]
  [:div {:class (cond-> ["hx-forms--field-container"]
                  (pos? (count errors))
                  (conj "hx-forms--field-container-error"))}
   (when (some? label)
     [:label {:class ["hx-forms--field-label"]
              :for (name field-key)} label])
   children

   [:p {:class ["hx-forms--field-active-error"]}
    (first errors)]])
