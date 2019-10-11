(ns hx-forms.styles.core
  "Styles are inteded to be used with JSS via hx-comp"
  (:require
   [hx-comp.core :refer [gs create-font-styles]]
   [hx-comp.base.button :refer [primary-button-styles]]))

(def input-field-styles
  (merge
   (create-font-styles {:style :body-20
                        :color :primary-500
                        :family :primary})
   (gs [:shadows :outline])
   {:padding [(gs [:spacing :p8]) (gs [:spacing :p12])]
    :cursor :pointer
    :outline :none
    :transition (str "color 0.2s ease, "
                     "border-color 0.2s ease")
    :border (str "1px solid " (gs [:colors :primary-200]))
    :background-color (gs [:colors :true-white])
    :color (gs [:colors :gray-scale-500])
    :width "100%"


    "&::placeholder"
    {:color (gs [:colors :gray-scale-200])}

    "&:disabled"
    {:background-color (gs [:colors :gray-scale-25])
     :color (gs [:colors :gray-scale-300])
     :border (gs [:borders :border-50-1])
     :cursor :default}}))

(def textarea-field-styles
  {:min-width "100%"
   :max-width "100%"
   :min-height "100px"})

(defonce toggle-btn-width 23)

(def toggle-button-styles
  {".hx-forms--toggle-container"
   (merge
    (gs [:shadows :outline])
    {:width "40px"
     :height "25px"
     :border-radius "40px"
     :border (str "1px solid " (gs [:colors :gray-scale-200]))
     :position :relative
     :background-color (gs [:colors :true-white])
     :transition "background-color 0.3s ease"
     :outline :none})

   ".hx-forms--toggle-container-on"
   {:background-color (str (gs [:colors :primary-500]) " !important")}

   ".hx-forms--toggle-position"
   {:position :absolute
    :left 0
    :top 0
    :background-color (gs [:colors :true-white])
    :width (str toggle-btn-width "px")
    :height (str toggle-btn-width "px")
    :box-shadow (gs [:shadows :shadow-20])
    :border-radius "50%"
    :outline :none
    :cursor :pointer
    :transition "left 0.3s ease"}

   ".hx-forms--toggle-position-on"
   {:left (str "calc(100% - " toggle-btn-width "px) !important")}})

(defonce select-button-height 50)
(defonce select-menu-z-index 100)

(def select-styles
  {".hx-forms--select-element"
   (merge
    input-field-styles
    {:height "50px"})})

(def checkbox-styles
  {".hx-forms--checkbox-container"
   {:display :flex
    :align-items :center}

   ".hx-forms--checkbox-button-container"
   (merge
    (gs [:shadows :outline])
    {:position :relative
     :padding (gs [:spacing :p0])
     :width (gs [:spacing :p20])
     :height (gs [:spacing :p20])
     :border (gs [:borders :border-100-1])
     :background (gs [:colors :true-white])
     :outline :none
     :cursor :pointer
     :border-radius (gs [:radius :r4])})

   ".hx-forms--checkbox-checkmark"
   (merge
    (create-font-styles {:style :body-30
                         :color :gray-scale-600
                         :family :primary})
    {:position :absolute
     :top (gs [:spacing :p8])
     :left (gs [:spacing :p2])
     :line-height 0
     :cursor :pointer})

   ".hx-forms--checkbox-label"
   (merge
    (create-font-styles {:style :caption-20
                         :color :gray-scale-600
                         :family :primary})
    {:margin (gs [:spacing :p0])
     :margin-left (gs [:spacing :p8])
     :cursor :pointer})})

(def field-styles
  {".hx-forms--field-container"
   {:position :relative
    :width "100%"
    :margin-bottom (gs [:spacing :p16])
    :padding-bottom (gs [:spacing :p16])}

   ".hx-forms--field-container-error > input,
    .hx-forms--field-container-error > textarea,
    .hx-forms--field-container-error > select"
   {:border-color (str (gs [:colors :error-500]) " !important")
    "&::placeholder"
    {:color (gs [:colors :error-100])}}

   ".hx-forms--field-container-error > label"
   {:color (gs [:colors :error-500])}

   ".hx-forms--field-container-hidden"
   {:display :none}

   ".hx-forms--radio-group-container,
    .hx-forms--checkbox-group-container"
   {:padding (gs [:spacing :p8])}

   ".hx-forms--radio-group-option-container,
    .hx-forms--checkbox-group-option-container"
   {:display :flex
    :align-items :center
    :margin-bottom (gs [:spacing :p8])

    "& > .hx-forms--radio-group-option-button"
    (merge
     (gs [:shadows :outline])
     {:margin-right (gs [:spacing :p12])
      :border-radius "50%"
      :width (gs [:spacing :p20])
      :height (gs [:spacing :p20])
      :outline :none
      :cursor :pointer
      :padding (gs [:spacing :p0])
      :border (gs [:borders :border-50-2])})

    "& > .hx-forms--radio-group-option-button-selected"
    {:background-color (gs [:colors :primary-500])}

    "& > label"
    (merge
     (create-font-styles {:style :caption-20
                          :color :gray-scale-600
                          :family :primary})
     {:cursor :pointer})}

   ".hx-forms--toggle-field-container,
    .hx-forms--checkbox-field-container"
   {:display :flex
    :align-items :center

    "& > label"
    {:margin-right (gs [:spacing :p12])
     :display :inline-block
     :width :auto}}

   ".hx-forms--field-active-error"
   (merge
    (create-font-styles {:style :caption-20
                         :color :error-500
                         :family :primary})
    {:position :absolute
     :bottom (str "-" (gs [:spacing :p4]))})

   ".hx-forms--field-label"
   (merge
    (create-font-styles {:style :caption-30
                         :color :gray-scale-600
                         :family :primary})
    {:display :block
     :width "100%"})})

(def responsive-styles
  {".hx-forms--row-4-2-1"
   {:display :flex
    :flex-wrap :wrap
    :justify-content :space-between

    "& > .hx-forms--field-container"
    {:width "calc(25% - 10px)"

     (gs [:queries :tablet])
     {:width "calc(50% - 10px)"}

     (gs [:queries :phone])
     {:width "100%)"}}}

   ".hx-forms--row-2-2-1"
   {:display :flex
    :flex-wrap :wrap
    :justify-content :space-between

    "& > .hx-forms--field-container"
    {:width "calc(50% - 10px)"

     (gs [:queries :phone])
     {:width "100%"}}}

   ".hx-forms--row-2-1-1"
   {:display :flex
    :flex-wrap :wrap
    :justify-content :space-between

    "& > .hx-forms--field-container"
    {:width "calc(50% - 10px)"

     (gs [:queries :tablet-below])
     {:width "100%"}}}})

(def hx-form-styles
  (merge
   select-styles
   toggle-button-styles
   checkbox-styles
   responsive-styles
   field-styles
   {"input[type=text]" input-field-styles
    "input[type=email]" input-field-styles
    "input[type=number]" input-field-styles
    "input[type=search]" input-field-styles
    "input[type=url]" input-field-styles
    "input[type=password]" input-field-styles
    "input[type=date]" input-field-styles
    "textarea" (merge input-field-styles
                      textarea-field-styles)

    ".hx-forms--submit-button-outer-container"
    {:position :relative
     :padding-top (gs [:spacing :p24])

     "& .hx-forms--submit-button-form-error-message"
     (merge
      (create-font-styles {:style :caption-30
                           :color :error-500
                           :family :primary})
      {:position :absolute
       :top (gs [:spacing :p0])})}

    ".hx-forms--submit-button-container"
    {:position :relative
     :border-radius (gs [:spacing :p0])

     "& > button, & > input[type=submit]"
     (merge primary-button-styles)}}))
