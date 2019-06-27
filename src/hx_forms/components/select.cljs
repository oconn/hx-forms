(ns hx-forms.components.select
  (:require
   [hx.react :refer [defnc]]
   [hx.hooks :as hooks]

   [hx-forms.components.field :refer [Field]]
   [hx-forms.utils :as u]))

(def node-key :hx/select)

(defonce no-selection-value "hx-forms--select-no-selection")

(defnc Option
  [{:keys [option
           current-value
           update-current-value
           is-open
           toggle-open-state
           on-change]}]
  (let [{:keys [value display]} option]
    [:li {:class ["hx-forms--select-option-container"]}
     [:button (cond-> {:on-click #(do
                                      (.preventDefault %)
                                      (toggle-open-state false)
                                      (update-current-value value)
                                      (on-change value))
                       :class ["hx-forms--select-option-button"]}
                (nil? value)
                (update :class
                        conj
                        "hx-forms--select-option-button-no-selection")

                (= current-value value)
                (update :class
                        conj
                        "hx-forms--select-option-button-selected"))
      display]]))

(defnc SelectComponent
  [{:keys [on-change no-value-copy options disabled default-value]
    :or {disabled false
         on-change identity
         no-value-copy "Select an option"
         options []}}]
  (let [[current-value update-current-value] (hooks/useState default-value)
        [is-open toggle-open-state] (hooks/useState false)
        toggle-open #(when-not disabled
                       (toggle-open-state (not is-open)))
        options* (into [{:display no-value-copy :value nil}] options)
        selected-option (->> options*
                             (filter #(= current-value (:value %)))
                             (first))]
    [:div {:class ["hx-forms--select-container"]}
     [:div (cond-> {:on-click toggle-open
                    :class ["hx-forms--select-option-container"
                            "hx-forms--select-selected-option-container"]}
             (true? is-open)
             (update :class
                     conj
                     "hx-forms--select-selected-option-container-open")

             (false? is-open)
             (update :class
                     conj
                     "hx-forms--select-selected-option-container-closed"))
      [:button (cond-> {:on-click #(.preventDefault %)
                        :disabled disabled
                        :class ["hx-forms--select-option-button"]}
                 (nil? current-value)
                 (update :class
                         conj
                         "hx-forms--select-selected-option-no-value")

                 (true? disabled)
                 (update :class
                         conj
                         "hx-forms--select-option-button-disabled"))
       (:display selected-option)]]
     [:ul (cond-> {:class ["hx-forms--select-option-list-container"]}
            (true? is-open)
            (update :class
                    conj
                    "hx-forms--select-option-list-container-open"))
      (for [option options*]
        ^{:key (or (:value option) no-selection-value)}
        [Option {:option option
                 :current-value current-value
                 :update-current-value update-current-value
                 :is-open is-open
                 :toggle-open-state toggle-open-state
                 :on-change on-change}])]]))

(defnc Select
  [{:keys [node update-state form-state is-submitting]}]
  (let [field-key
        (u/get-field-key node node-key)

        errors
        (u/get-field-errors form-state field-key)

        [{:keys [on-change on-blur options disabled default-value]
          :as hx-props
          :or {on-change identity
               on-blur identity}} _]
        (u/get-field-props node node-key)]

    (hooks/useEffect
     (fn []
       (u/initialize-field! {:node node
                             :node-key node-key
                             :update-state update-state
                             :defaults {:default-value nil}}))
     ["on-mount"])

    [Field {:errors errors
            :label (:label hx-props)
            :field-key field-key}
     [SelectComponent {:on-change (partial u/on-change!
                                           {:update-state update-state
                                            :field-key field-key
                                            :get-value identity
                                            :callback on-change})
                       :disabled (or disabled is-submitting)
                       :options options
                       :default-value default-value}]]))
