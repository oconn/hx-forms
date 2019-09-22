(ns hx-forms.utils-test
  (:require
   [cljs.test :refer-macros [deftest is testing]]
   [clojure.spec.alpha :as s]

   [hx-forms.reducer :as r]
   [hx-forms.utils :as u]))

(def ^:private reducer
  (#'r/form-state-reducer identity))

#_(deftest contains-errors?
  (testing "Returns true when the form contains errors"
    (let [form-state-without-errors
          (-> {:test (#'u/format-field-state {})}
              (reducer ))

          form-state-with-errors
          (-> {:test (#'u/format-field-state {:validators
                                              [{:validator #(-> % count pos?)
                                                :error "Required"}]})})]
      (is (s/valid? :hx-forms.utils/form-state form-state-with-errors))
      (is (s/valid? :hx-forms.utils/form-state form-state-without-errors))
      (is (false? (u/contains-errors? (u/process-all-validators
                                       form-state-without-errors))))
      (is (true? (u/contains-errors? (u/process-all-validators
                                      form-state-with-errors)))))))

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

(deftest form-state->values
  (testing "Returns all the values in a form"
    (let [form-state
          (-> {:field-one (#'u/format-field-state {})
               :field-two (#'u/format-field-state {})}
              (reducer
               {:action :change-field
                :payload {:field-key :field-one
                          :value "one"}})
              (reducer
               {:action :change-field
                :payload {:field-key :field-two
                          :value "two"}}))]
      (is (s/valid? :hx-forms.utils/form-state form-state))
      (is (= {:field-one "one"
              :field-two "two"}
             (u/form-state->values form-state))))))
