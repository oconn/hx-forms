(ns hx-forms.components.input
  (:require
   [hx.react :refer [defnc]]
   [hx.hooks :as hooks]

   [hx-forms.utils :as u]))

(def node-key :hx/input)

(defnc Input
  [{:keys [node update-state]}]
  (let [hx-params (u/get-hx-params node node-key)]

    (hooks/useEffect
     (fn []
       (u/initialize-field! hx-params update-state))
     [:on-mount])

    (u/remove-node-params node node-key)))
