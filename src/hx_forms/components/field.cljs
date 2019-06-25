(ns hx-forms.components.field
  (:require
   [hx.react :refer [defnc]]))

(defnc Field
  [{:keys [errors children]}]
  [:div {:class (cond-> ["hx-forms--field-container"]
                  (pos? (count errors))
                  (conj "hx-forms--field-container-error"))}
   children
   [:p {:class ["hx-forms--field-active-error"]}
    (first errors)]])
