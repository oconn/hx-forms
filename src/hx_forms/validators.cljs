(ns hx-forms.validators)

(defn required-input
  [field-value _]
  (not (empty? field-value)))
