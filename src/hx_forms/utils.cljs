(ns hx-forms.utils)

(defn spy [x] (js/console.log x) x)

(defn get-raw-params
  [node node-key]
  (-> node second (dissoc node-key)))

(defn get-hx-params
  [node node-key]
  (-> node second node-key))

(defn remove-node-params
  [node node-key]
  (assoc node 1 (dissoc (get node 1) node-key)))

(defn merge-with-params
  [node value]
  (update-in node [1] merge value))

(defn initialize-field!
  [{:keys [field-key] :as hx-params} update-state]
  (update-state {:acition :initialize-field
                 :field-key field-key
                 :payload hx-params}))
