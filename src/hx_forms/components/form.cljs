(ns hx-forms.components.form
  (:require
   [hx.react :refer [defnc]]

   [hx-forms.utils :as u]))

(def node-key :hx/form)

(defn wrap-on-submit
  [{raw-on-submit :on-submit} {hx-on-submit :on-submit}]
  (fn [e]
    (js-invoke e "preventDefault")

    ((or raw-on-submit hx-on-submit) {:test true})))

(defnc Form
  [{:keys [node]}]
  (let [raw-params (u/get-raw-params node node-key)
        hx-params (u/get-hx-params node node-key)
        on-submit (wrap-on-submit raw-params hx-params)]

    (-> node
        (u/remove-node-params node-key)
        (u/merge-with-params {:on-submit on-submit}))))
