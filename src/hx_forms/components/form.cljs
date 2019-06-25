(ns hx-forms.components.form
  (:require
   [hx.react :refer [defnc]]
   [hx.hooks :as hooks]

   [hx-forms.utils :as u]))

(def node-key :hx/form)

(defn wrap-on-submit
  [on-submit]
  (fn [e]
    (js-invoke e "preventDefault")

    (on-submit {:test true})))

(defnc Form
  [{:keys [node]}]
  (let [[hx-props node-props] (u/get-field-props node node-key)
        on-submit (wrap-on-submit (or (:on-submit node-props)
                                      (:on-submit hx-props)))]
    (let [form
          (hooks/useMemo
           (fn []
             (-> node
                 (u/remove-hx-props node-key)
                 (u/merge-with-props {:on-submit on-submit})))
           ["no-update"])]
      form)))
