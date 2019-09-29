(ns hx-forms.core
  (:require
   [clojure.walk :refer [postwalk]]
   [hx.react :as hx :refer [defnc]]
   [hx.hooks :as hooks]

   [hx-forms.reducer :as r]
   [hx-forms.components.custom-field :as custom-field]
   [hx-forms.components.form :as form]
   [hx-forms.components.input :as input]
   [hx-forms.components.select :as select]
   [hx-forms.components.toggle :as toggle]
   [hx-forms.components.checkbox :as checkbox]
   [hx-forms.components.radio-group :as radio-group]
   [hx-forms.components.submit-button :as submit-button]))

(defn- hx-node-type?
  "Checks to see if any given node is an hx field of a specific type"
  [node key]
  (and (coll? node)
       (contains? (second node) key)))

(defn- walk-node
  "Walks the form replacing rf fields with form aware components"
  [{:keys [form-spec form-state update-state]}]
  (fn [node]
    (let [{:keys [is-submitting]} form-spec
          hx-comp-props {:form-state form-state
                         :is-submitting is-submitting
                         :update-state update-state
                         :node node}]
      (cond
        (hx-node-type? node form/node-key)
        [form/Form hx-comp-props]

        (hx-node-type? node input/node-key)
        [input/Input hx-comp-props]

        (hx-node-type? node select/node-key)
        [select/Select hx-comp-props]

        (hx-node-type? node radio-group/node-key)
        [radio-group/RadioGroup hx-comp-props]

        (hx-node-type? node toggle/node-key)
        [toggle/Toggle hx-comp-props]

        (hx-node-type? node checkbox/node-key)
        [checkbox/Checkbox hx-comp-props]

        (hx-node-type? node :hx/custom-field)
        [custom-field/CustomField hx-comp-props]

        (hx-node-type? node submit-button/node-key)
        [submit-button/SubmitButton hx-comp-props]

        :else
        node))))

(defnc Form
  [{:keys [on-change on-mount on-unmount is-submitting body]
    :or {on-change identity
         on-mount identity
         on-unmount identity
         is-submitting false}
    :as form-spec}]
  (let [[form-state update-state]
        (hooks/useReducer (r/form-state-reducer on-change) {})]

    (hooks/useEffect
     (fn []
       (update-state {:action :calculate-visibility})
       (on-mount {:form-state form-state})

       (fn []
         (on-unmount)))
     ["on-mount"])

    (postwalk (walk-node {:form-spec form-spec
                          :form-state form-state
                          :update-state update-state
                          :is-submitting is-submitting})
              body)))

(def HXForm Form)
