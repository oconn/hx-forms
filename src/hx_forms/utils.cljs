(ns hx-forms.utils)

;; TODO Remove
(defn spy [x] (js/console.log x) x)

(defn- format-field-state
  [{:keys [hx-props node-props defaults]
    :or {defaults {}}}]
  {:value (or (:default-value node-props)
              (:default-value hx-props)
              (:default-value defaults))})

(defn get-node-props
  [node node-key]
  (-> node second (dissoc node-key)))

(defn get-hx-props
  [node node-key]
  (-> node second node-key))

(defn get-field-props
  [node node-key]
  [(get-hx-props node node-key) (get-node-props node node-key)])

(defn get-field-key
  [node node-key]
  (-> node
      (get-hx-props node-key)
      (:field-key)))

(defn remove-hx-props
  [node node-key]
  (assoc node 1 (dissoc (get node 1) node-key)))

(defn merge-with-props
  [node value]
  (update-in node [1] merge value))

(defn on-change!
  [{:keys [field-key update-state get-value]} e]
  (update-state
   {:action :change-field
    :payload {:field-key field-key
              :value (get-value e)}}))

(defn initialize-field!
  [{:keys [node node-key update-state defaults]}]
  (let [[hx-props node-props] (get-field-props node node-key)]
    (update-state
     {:action :initialize-field
      :payload {:field-key (get-field-key node node-key)
                :field-state (format-field-state {:hx-props hx-props
                                                  :node-props node-props
                                                  :defaults defaults})}})))
