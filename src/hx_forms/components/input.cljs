(ns hx-forms.components.input
  (:require
   [goog.object :as gobj]
   [hx.react :refer [defnc]]
   [hx.hooks :as hooks]

   [hx-forms.components.field :refer [Field]]
   [hx-forms.utils :as u]))

(def node-key :hx/input)

(defn- get-value
  [e]
  (gobj/getValueByKeys e "target" "value"))

(def ^{:private true} default-value "")

(defnc Input
  [{:keys [node update-state form-state is-submitting]}]

  (hooks/useEffect
   (fn []
     (u/initialize-field! {:node node
                           :node-key node-key
                           :update-state update-state
                           :defaults {:default-value default-value}}))
   ["on-mount"])

  (let [field-key
        (u/get-field-key node node-key)

        field-value
        (u/get-field-value form-state field-key)

        errors
        (u/get-field-errors form-state field-key)

        is-visible
        (u/get-field-visibility form-state field-key)

        [hx-props {:keys [on-change on-blur]
                   :as node-props
                   :or {on-change identity
                        on-blur identity}}]
        (u/get-field-props node node-key)

        input
        (hooks/useMemo
         (fn []
           (-> node
               (u/remove-hx-props node-key)
               (u/merge-with-props
                {:on-change (partial u/on-change! {:update-state update-state
                                                   :field-key field-key
                                                   :get-value get-value
                                                   :callback on-change})
                 :on-blur (partial u/validate-field! {:update-state update-state
                                                      :field-key field-key
                                                      :callback on-blur})
                 :id (or (:id node-props)
                         (name field-key))})))
         ["no-update"])]

    [Field {:errors errors
            :label (:label hx-props)
            :field-key field-key
            :visible is-visible}
     ;; Controlled input fields allow for formatters and
     ;; masks to work properly
     (-> input
         (update-in [1] #(dissoc % :default-value))
         (assoc-in [1 :value] (or field-value default-value))
         (assoc-in [1 :disabled] is-submitting))]))
