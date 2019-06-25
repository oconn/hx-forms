(ns hx-forms.components.input
  (:require
   [goog.object :as gobj]
   [hx.react :refer [defnc]]
   [hx.hooks :as hooks]

   [hx-forms.utils :as u]))

(def node-key :hx/input)

(defn- get-value
  [e]
  (gobj/getValueByKeys e "target" "value"))

(defnc Input
  [{:keys [node update-state form-state]}]

  (hooks/useEffect
   (fn []
     (u/initialize-field! {:node node
                           :node-key node-key
                           :update-state update-state
                           :defaults {:default-value ""}}))
   ["on-mount"])

  (let [field-key
        (u/get-field-key node node-key)

        errors
        (u/get-field-errors form-state field-key)

        input
        (hooks/useMemo
         (fn []
           (-> node
               (u/remove-hx-props node-key)
               (u/merge-with-props
                {:on-change (partial u/on-change! {:update-state update-state
                                                   :field-key field-key
                                                   :get-value get-value})
                 :on-blur (partial u/validate-field! {:update-state update-state
                                                      :field-key field-key})})))
         ["no-update"])]
    [:div
     (when (seq errors)
       [:h1 (first errors)])
     input]))
