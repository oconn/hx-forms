(ns hx-forms.validators)

(defn required-input
  [field-value _]
  (not (empty? field-value)))

(defn min-input-length
  [min-length]
  (fn [field-value _]
    (>= (count field-value) min-length)))

(defn max-input-length
  [max-length]
  (fn [field-value _]
    (<= (count field-value) max-length)))

(defn simple-email
  [field-value _]
  (some? (re-matches #"(.+)@(.+)\.(.+){2,}" field-value)))

(defn hex-validator
  [field-value _]
  (some? (re-matches #"#[a-f0-9]{6}" field-value)))

(def hex-validators
  [{:validator required-input
    :error "Please enter a hex value"}
   {:validator hex-validator
    :error "Please enter a valid hex value"}])
