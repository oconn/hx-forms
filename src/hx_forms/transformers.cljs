(ns hx-forms.transformers)

(defn str->int
  [value]
  (if (= (type value) js/String)
    (try
      (let [integer (js/parseInt value 10)]
        (if (js/isNaN integer)
          (js/console.error
           (js/Error. (str "Failed to convert '" value "' into an integer.")))
          integer))
      (catch js/Error e
        (js/console.error
         (js/Error. (str "Failed to convert '" value "' into an integer.")))
        value))
    (do
      (js/console.error
       (js/Error. (str "Failed to convert " value " into an integer.")))
      value)))
