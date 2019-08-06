(ns hx-forms.formatters
  (:require [clojure.string]))

(defn ->string-formatter
  [pattern]
  (fn [field-value _]
    (if (string? field-value)
      (apply str (re-seq (re-pattern pattern) field-value))
      (do
        (js/console.error
         (str "Invalid type " (type field-value) " passed to string formatter"))
        field-value))))

(defn ->max-length-formatter
  [max-length]
  (fn [field-value _]
    (if (string? field-value)
      (do
        (if (<= (count field-value) max-length)
          field-value
          (apply str (drop-last field-value))))
      (do
        (js/console.error
         (str "Invalid type " (type field-value) " passed to string formatter"))
        field-value))))

(def alpha-only
  "Strips out non alpha characters"
  (->string-formatter "[a-zA-Z]"))

(def numeric-only
  "Strips out non numeric characters"
  (->string-formatter "[0-9]"))

(def alphanumeric-only
  "Strips out non alphanumeric characters"
  (->string-formatter "[a-zA-Z0-9]"))

(defn trim
  "Trims whitespace"
  [field-value _]
  (if (string? field-value)
    (clojure.string/trim field-value)
    (do
      (js/console.error
       (str "Invalid type " (type field-value) " passed to trim"))
      field-value)))

(defn lower-case
  "Forces lower-case"
  [field-value _]
  (if (string? field-value)
    (clojure.string/lower-case field-value)
    (do
      (js/console.error
       (str "Invalid type " (type field-value) " passed to lower-case"))
      field-value)))

(defn upper-case
  "Forces upper-case"
  [field-value _]
  (if (string? field-value)
    (clojure.string/upper-case field-value)
    (do
      (js/console.error
       (str "Invalid type " (type field-value) " passed to upper-case"))
      field-value)))
