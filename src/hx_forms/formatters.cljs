(ns hx-forms.formatters
  (:require [clojure.string]))

(defn trim
  "Trims whitespace"
  [field-value _]
  (if (string? field-value)
    (clojure.string/trim field-value)
    (do
      (js/console.error
       (str "Invalid type " (type field-value) " passed to trim"))
      field-value)))
