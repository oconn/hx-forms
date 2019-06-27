(ns hx-forms.components.toggle
  (:require
   [hx.react :refer [defnc]]
   [hx.hooks :as hooks]

   [hx-forms.components.field :refer [Field]]
   [hx-forms.utils :as u]))

(def node-key :hx/toggle)

(defnc ToggleComponent
  [{:keys [on-change default-value disabled]
    :or {on-change identity
         default-value true
         disabled false}}]
  (let [[is-on update-toggle-state] (hooks/useState default-value)]
    [:button (cond-> (-> {:disabled disabled}
                         (assoc :class ["hx-forms--toggle-container"])
                         (assoc :on-click
                                #(do
                                   (.preventDefault %)
                                   (on-change (not is-on))
                                   (update-toggle-state (not is-on)))))
               (true? is-on)
               (update :class conj "hx-forms--toggle-container-on"))
     [:span (cond-> {:class ["hx-forms--toggle-position"]}
              (true? is-on)
              (update :class conj "hx-forms--toggle-position-on"))]]))

(defnc Toggle
  [{:keys [node update-state form-state is-submitting]}]
  (let [field-key
        (u/get-field-key node node-key)

        errors
        (u/get-field-errors form-state field-key)

        [{:keys [on-change disabled default-value]
          :as hx-props
          :or {on-change identity}} _]
        (u/get-field-props node node-key)]

    (hooks/useEffect
     (fn []
       (u/initialize-field! {:node node
                             :node-key node-key
                             :update-state update-state
                             :defaults {:default-value false}}))
     ["on-mount"])

    [Field {:errors errors
            :label (:label hx-props)
            :field-key field-key}
     [ToggleComponent {:on-change (partial u/on-change!
                                           {:update-state update-state
                                            :field-key field-key
                                            :get-value identity
                                            :callback on-change})
                       :disabled (or disabled is-submitting)
                       :default-value default-value}]]))
