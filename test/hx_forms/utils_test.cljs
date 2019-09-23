(ns hx-forms.utils-test
  (:require
   [cljs.test :refer-macros [deftest is testing]]
   [clojure.spec.alpha :as s]

   [hx-forms.reducer :as r]
   [hx-forms.utils :as u]))

(def ^:private reducer
  (#'r/form-state-reducer identity))

(deftest contains-errors?
  (testing "Returns true when the form contains errors"
    (let [form-state-without-errors
          (-> {:test (#'u/format-field-state
                      {:hx-props {}})}
              (reducer {:action :validate-field
                        :payload {:field-key :test}}))

          form-state-with-errors
          (-> {:test (#'u/format-field-state
                      {:hx-props
                       {:validators
                        [{:validator #(-> % count pos?)
                          :error "Required"}]}})}
              (reducer {:action :validate-field
                        :payload {:field-key :test}}))]

      (is (s/valid? ::u/form-state form-state-with-errors))
      (is (s/valid? ::u/form-state form-state-without-errors))
      (is (false? (u/contains-errors? (u/process-all-validators
                                       form-state-without-errors))))
      (is (true? (u/contains-errors? (u/process-all-validators
                                      form-state-with-errors)))))))

(deftest get-node-props
  (testing "Returns just the node props from a given node"
    (is (= {:placeholder "Test"}
           (u/get-node-props [:input {:placeholder "Test"
                                      :hx/input {:default-value true
                                                 :field-key :test}}]
                             :hx/input)))))

(deftest get-hx-props
  (testing "Returns just the node props from a given node"
    (is (= {:default-value true
            :field-key :test}
           (u/get-hx-props [:input {:placeholder "Test"
                                    :hx/input {:default-value true
                                               :field-key :test}}]
                           :hx/input)))))

(deftest get-field-props
  (testing "Returns both the node and hx props as a tuple"
    (is (= [{:default-value true
             :field-key :test}
            {:placeholder "Test"}]
           (u/get-field-props [:input {:placeholder "Test"
                                       :hx/input {:default-value true
                                                  :field-key :test}}]
                              :hx/input)))))

(deftest get-field-key
  (testing "Returns the field-key of a given field"
    (is (= :test (u/get-field-key [:input {:placeholder "Test"
                                           :hx/input {:default-value true
                                                      :field-key :test}}]
                                  :hx/input)))))

(deftest remove-hx-props
  (testing "Properly strips hx-props from a node"
    (is (= (u/remove-hx-props [:input {:hx/input {:any :value}}]
                              :hx/input)
           [:input {}]))))

(deftest merge-with-props
  (testing "Merges options into an form element"
    (is (= (u/merge-with-props [:input {:disabled false}]
                               {:hx/input {:default-value true}})
           [:input {:disabled false
                    :hx/input {:default-value true}}]))))

(deftest process-validators
  (let [form-state
        {:field-one (#'u/format-field-state
                     {:hx-props {:default-value "one"
                                 :validators [{:validator #(-> % count pos?)
                                               :error "Required"}]}})
         :field-two (#'u/format-field-state
                     {:hx-props {:default-value ""
                                 :validators [{:validator #(-> % count pos?)
                                               :error "Required"}]}})}]

    (testing "Is valid form-state"
      (is (s/valid? ::u/form-state form-state)))

    (testing "Returns no errors if all validators evaluate to false"
      (->> form-state
           (u/process-validators (u/get-field-validators form-state :field-one)
                                 (u/get-field-value form-state :field-one))
           (empty?)))

    (testing "Returns no errors if all validators evaluate to false"
      (->> form-state
           (u/process-validators (u/get-field-validators form-state :field-two)
                                 (u/get-field-value form-state :field-two))
           (empty?)
           (false?)))))

(deftest process-all-validators
  (let [form-state
        (u/process-all-validators
         {:field-one (#'u/format-field-state
                      {:hx-props {:default-value "one"
                                  :validators [{:validator #(-> % count pos?)
                                                :error "Required"}]}})
          :field-two (#'u/format-field-state
                      {:hx-props {:default-value ""
                                  :validators [{:validator #(-> % count pos?)
                                                :error "Required"}]}})})]

    (testing "Is valid form-state"
      (is (s/valid? ::u/form-state form-state)))

    (testing "Sets errors to fields with errors"
      (is (-> form-state
              (u/get-field-errors :field-one)
              (count)
              (zero?)))

      (is (-> form-state
              (u/get-field-errors :field-two)
              (count)
              (pos?))))))

(deftest form-state->values
  (testing "Returns all the values in a form"
    (let [form-state
          {:field-one (#'u/format-field-state
                       {:hx-props {:default-value "one"}})
           :field-two (#'u/format-field-state
                       {:hx-props {:default-value "two"}})}]
      (is (s/valid? ::u/form-state form-state))
      (is (= {:field-one "one"
              :field-two "two"}
             (u/form-state->values form-state))))))
