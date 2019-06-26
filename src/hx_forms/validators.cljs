(ns hx-forms.validators)

(defn required-input
  "Input field is not an empty string"
  [field-value _]
  (not (empty? field-value)))

(defn min-input-length
  "Input field contains min-length or more characters"
  [min-length]
  (fn [field-value _]
    (>= (count field-value) min-length)))

(defn max-input-length
  "Input field contains max-length or less characters"
  [max-length]
  (fn [field-value _]
    (<= (count field-value) max-length)))

(defn simple-email
  "Input field matchs a simple email regular expression"
  [field-value _]
  (some? (re-matches #"(.+)@(.+)\.(.+){2,}" field-value)))
