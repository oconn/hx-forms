### hx-forms

Form library built for [hx](https://github.com/Lokeh/hx)

[Demo Site](https://d39iaf8kr29b5t.cloudfront.net)

## Install

[![Clojars Project](https://img.shields.io/clojars/v/oconn/hx-forms.svg)](https://clojars.org/oconn/hx-forms)

**Note** This project is currently in alpha and the API may change in future releases.

## Project Goals

1. Unopinionated when it comes to how form markup is structured
1. Extendable
1. Form state management only (bring your own styles)
1. User friendly (Accessible & Intuitive)

## Basic Usage

Turning an HTML form into an hx-form requires wrapping the form in `HXForm` and adding the hx-form keys to each form element you would like to track.

```clojure
(ns app.core
  (require [hx-forms.core :refer [HXForm]])

;; HTML Form
[:form {:on-submit #()}
  [:input {:type "text"}]
  [:input {:type "submit"
           :value "Submit"]]

;; HXForm
[HXForm
  {:body
   [:form {:hx/form {:on-submit #()}}
    [:input {:type "text"
             :hx/input {:field-key :sample-field}}]

    [:input
      {:hx/submit-button {}
       :type "submit"
       :value "Submit"}]]]}]
```

### Validators

**TODO** Add documentation

```clojure
(ns app.core
  (require [hx-forms.core :refer [HXForm]]
           [hx-forms.validators :as v])

[:input {:type "text"
         :hx/input {:field-key :sample-field
                    :validators [{:validator v/required-input
                                  :error "Please enter a value."}]}}]
```

### Formatters / Masks

**TODO** Add documentation

### Transformers (Data transformations)

**TODO** Add documentation

### Dynamic Fields

**TODO** Add documentation

## Creating Custom Components

**TODO** Add documentation

## Styling your forms

hx-forms does not take a strong stance on how you choose to style your forms. There is some sample styles (same styles applied to the demo site) in `hx-forms.styles.core`.

## Code Samples

The following code samples are the pulled from the demo site.

```clojure
(ns hx-forms-demo.core
  (:require
   [hx.react :refer [defnc]]
   [hx-comp.base.text :as text]
   [hx-forms.core :refer [HXForm]]
   [hx-forms.utils :as u]
   [hx-forms.validators :as v]
   [hx-forms.transformers :as t]
   [hx-forms.formatters :as f]))

(defnc SimpleForm
  []
  [:<>
   [DemoTitle
    {:title "Simple Form"
     :description
     (str "None of the bells and whistles. Just a plain-old form.")}]

   [HXForm
    {:is-submitting false
     :body
     [:form {:id "formOne"
             :hx/form {:on-submit #(js/console.log %)}}
      [:div {:class ["hx-forms--row-2-2-1"]}
       [:input
        {:hx/input
         {:field-key :email-one
          :label "Email"}
         :type "text"
         :placeholder "Email"}]]

      [:input
       {:hx/submit-button {}
        :type "submit"
        :value "Submit"}]]}]])

(defnc SimpleFormWithSimpleValidation
  []
  [:<>
   [DemoTitle
    {:title "Enforcing requiredness"
     :description
     (str "Doing more validation clientside can shorten the feedback loop and "
          "improve the overall user experience. You can test requiredness by "
          "either tabbing out of a requied field (blurring the field) or "
          "submitting the form with the required field empty.")}]

   [HXForm
    {:is-submitting false
     :body
     [:form {:id "formTwo"
             :hx/form {:on-submit #(js/console.log %)}}
      [:div {:class ["hx-forms--row-2-2-1"]}
       [:input
        {:hx/input
         {:field-key :email-two
          :label "Email"
          :validators [{:validator v/required-input
                        :error "Please enter your email address."}]}
         :type "text"
         :placeholder "Email"}]]

      [:input
       {:hx/submit-button {}
        :type "submit"
        :value "Submit"}]]}]])

(defnc SimpleFormWithCustomValidation
  []
  [:<>
   [DemoTitle
    {:title "Adding custom validation"
     :description
     (str "Checking requiedness is a great first step, validating data is the "
          "level. While the server should always validate data, the is no "
          "reason the client can not also do the same to improve the feeback "
          "loop and minimize frustration that is common in online forms. "
          "Try entering an invalidly formatted email address in this "
          "example.")}]

   [HXForm
    {:is-submitting false
     :body
     [:form {:id "formThree"
             :hx/form {:on-submit #(js/console.log %)}}
      [:div {:class ["hx-forms--row-2-2-1"]}
       [:input
        {:hx/input
         {:field-key :email
          :label "Email"
          :validators [{:validator v/required-input
                        :error "Please enter your email address."}
                       {:validator v/simple-email
                        :error "Please enter a valid email address."}]}
         :type "text"
         :placeholder "Email"}]]

      [:input
       {:hx/submit-button {}
        :type "submit"
        :value "Submit"}]]}]])

(defnc SimpleFormWithDefaults
  []
  [:<>
   [DemoTitle
    {:title "Defaults"
     :description
     (str "Defaults are of course a must, but to take them a step further, "
          "they can be persisted across form resets or submission events.")}]

   [HXForm
    {:is-submitting false
     :body
     [:form {:id "formFour"
             :hx/form {:on-submit #(js/console.log %)}}
      [:div {:class ["hx-forms--row-2-2-1"]}
       [:input
        {:hx/input
         {:field-key :email
          :label "Email"
          :default-value "johndoe@gmail.com"
          :validators [{:validator v/required-input
                        :error "Please enter your email address."}
                       {:validator v/simple-email
                        :error "Please enter a valid email address."}]}
         :type "text"
         :placeholder "Email"}]]

      [:input
       {:hx/submit-button {}
        :type "submit"
        :value "Submit"}]]}]])

(defnc SimpleFormatter
  []
  [:<>
   [DemoTitle
    {:title "Masks & Formatters"
     :description
     (str "Providing immediate error feedback is important, but preventing "
          "users from ever entering invalid data is the next step.")}]

   [HXForm
    {:is-submitting false
     :body
     [:form {:id "formFive"
             :hx/form {:on-submit #(js/console.log %)}}
      [:div {:class ["hx-forms--row-2-2-1"]}
       [:input
        {:hx/input
         {:field-key :color
          :label "HEX Color"
          :validators v/hex-validators
          :formatters f/hex-formatters}
         :type "text"
         :placeholder "HEX Color"}]]

      [:input
       {:hx/submit-button {}
        :type "submit"
        :value "Submit"}]]}]])

(defnc DynamicForms
  []
  [:<>
   [DemoTitle
    {:title "Dynamic Forms"
     :description
     (str "There are times when users may be presented with a form and not all "
         "fields are applicable depending on a subset of the filled out form. "
         "This can add to clutter and confusion and this is where dynamic "
         "forms can help. Showing only data that pertains under certain "
         "circumstances will reduce clutter and simplify the form.")}]

   [HXForm
    {:is-submitting false
     :body
     [:form {:id "formSix"
             :hx/form {:on-submit #(js/console.log %)}}
      [:div
       {:hx/checkbox
        {:field-key :under-eighteen
         :label "Under 18?"}}]

      [:div {:class ["hx-forms--row-2-2-1"]}
       [:input
        {:hx/input
         {:field-key :gurdian-email
          :label "Gurdian Email Address"
          :visibility #(u/get-field-value % :under-eighteen)
          :validators [{:validator v/required-input
                        :error "Please enter your email address."}
                       {:validator v/simple-email
                        :error "Please enter a valid email address."}]}
         :type "text"
         :placeholder "Email Address"}]]

      [:input
       {:hx/submit-button {}
        :type "submit"
        :value "Submit"}]]}]])

(defnc ResponsiveForms
  []
  [:<>
   [DemoTitle
    {:title "Custom Styles & Markup"
     :description
     (str "HX Forms doesn't take a strong stance on how your structure "
          "your markup or style your forms. It take a minimalist approach "
          "to hooking into and managing your form's state. This demo shows "
          "off how arbitrary markup can be applied to your forms")}]

   [HXForm
    {:is-submitting false
     :body
     [:form {:id "formSeven"
             :hx/form {:on-submit #(js/console.log %)}}
      [:div {:class ["hx-forms--row-2-1-1"]}
       [:input
        {:hx/input
         {:field-key :first-name
          :label "First Name"
          :validators [{:validator v/required-input
                        :error "Please enter your first name."}]}
         :type "text"
         :placeholder "First Name"}]

       [:input
        {:hx/input
         {:field-key :last-name
          :label "Last Name"
          :validators [{:validator v/required-input
                        :error "Please enter your last name."}]}
         :type "text"
         :placeholder "Last Name"}]

       [:input
        {:hx/input
         {:field-key :email
          :label "Email Address"
          :validators [{:validator v/required-input
                        :error "Please enter your email address."}
                       {:validator v/simple-email
                        :error "Please enter a valid email address."}]}
         :type "text"
         :placeholder "Email Address"}]]

      [:input
       {:hx/submit-button {}
        :type "submit"
        :value "Submit"}]]}]])

(defnc DataTransformation
  []
  [:<>
   [DemoTitle
    {:title "Data Transformation"
     :description
     (str "There are times you may need to transform the data before passing "
          "it along. You may want to coerce values to other types or possibly "
          "augment specific fields. Depending on the values of other fields. "
          "This can be done by using transformers.")}]

   [HXForm
    {:is-submitting false
     :body
     [:form {:id "formEight"
             :hx/form {:on-submit #(js/console.log %)}}
      [:div {:class ["hx-forms--row-2-1-1"]}
       [:input
        {:hx/input
         {:field-key :age-string
          :label "Age (String)"
          :formatters [f/numeric-only]
          :validators [{:validator v/required-input
                        :error "Please enter your age."}]}
         :type "text"
         :placeholder "Age"}]

       [:input
        {:hx/input
         {:field-key :age-number
          :label "Age (Number)"
          :formatters [f/numeric-only]
          :transformers [t/str->int]
          :validators [{:validator v/required-input
                        :error "Please enter your age."}]}
         :type "text"
         :placeholder "Age"}]]

      [:input
       {:hx/submit-button {}
        :type "submit"
        :value "Submit"}]]}]])

(defnc AdditionalComponentTypes
  []
  [:<>
   [DemoTitle
    {:title "Form Components"
     :description
     (str "")}]

   [HXForm
    {:is-submitting false
     :body
     [:form {:id "formNine"
             :hx/form {:on-submit #(js/console.log %)}}
      [text/Title10
       {:style {:margin-bottom "20px}}
       (str "Inputs are standard inputs and support all valid types")]

      [:div {:class ["hx-forms--row-2-1-1"]}
       [:input
        {:hx/input
         {:field-key :text-input
          :label "Text Input"}
         :type "text"
         :placeholder "Text Input"}]

       [:input
        {:hx/input
         {:field-key :standard-input
          :label "Password Input"}
         :type "password"
         :placeholder "Password Input"}]

       [:input
        {:hx/input
         {:field-key :calendar-input
          :label "Calendar Input"}
         :type "date"
         :placeholder "Calendar Input"}]

       [:input
        {:hx/input
         {:field-key :search-input
          :label "Search Input"}
         :type "search"
         :placeholder "Search Input"}]]

      [text/Title10
       {:style {:margin-bottom "20px}}
       (str "Binary Input Fields")]

      [:div {:class ["hx-forms--row-2-1-1"]}
       [:input
        {:hx/checkbox
         {:field-key :checkbox
          :label "Checkbox"}}]

       [:input
        {:hx/toggle
         {:field-key :toggle
          :label "Toggle"}}]]

      [text/Title10
       {:style {:margin-bottom "20px}}
       (str "Multi-Option Fields")]

      [:div {:class ["hx-forms--row-2-1-1"]}
       [:div
        {:hx/radio-group
         {:field-key :radio-group-type
          :label "Radio Group Type"
          :options [{:display "Option One"
                     :value :one}
                    {:display "Option Two"
                     :value :two}
                    {:display "Option Three"
                     :value :three}]}}]

       [:div
        {:hx/checkbox-group
         {:field-key :checkbox-group-type
          :label "Checkbox Group Type"
          :options [{:display "Option One"
                     :value :one}
                    {:display "Option Two"
                     :value :two}
                    {:display "Option Three"
                     :value :three}]}}]

       [:div
        {:hx/select
         {:field-key :select-type
          :label "Select Type"
          :options [{:display "Option One"
                     :value "one"}
                    {:display "Option Two"
                     :value "two"}
                    {:display "Option Three"
                     :value "three"}]}}]]

      [:input
       {:hx/submit-button {}
        :type "submit"
        :value "Submit"}]]}]])
```
