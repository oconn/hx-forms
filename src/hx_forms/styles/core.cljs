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
   {:padding [(gs [:spacing :p8]) (gs [:spacing :p12])]
    :cursor :pointer
    :outline :none
    :transition (str "color 0.2s ease, "
                     "border-color 0.2s ease")
    :border (str "1px solid " (gs [:colors :primary-200]))
    :background-color (gs [:colors :true-white])
    :color (gs [:colors :gray-scale-500])
    :width "100%"
    :border-radius (gs [:radius :r4])

    "&::placeholder"
    {:color (gs [:colors :gray-scale-200])}

    "&:focus"
    {:border-color (gs [:colors :primary-500])}

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
   {:width "40px"
    :height "25px"
    :border-radius "40px"
    :border (str "1px solid " (gs [:colors :gray-scale-200]))
    :position :relative
    :background-color (gs [:colors :true-white])
    :transition "background-color 0.3s ease"
    :outline :none}

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
  {".hx-forms--select-container"
   {:width "100%"
    :position :relative}

   ".hx-forms--select-option-list-container"
   {:margin (gs [:spacing :p0])
    :box-shadow (gs [:shadows :shadow-20])
    :display :none
    :overflow :hidden
    :overflow-y :scroll
    :position :absolute
    :width "100%"
    :max-height (str (+ (* 5 select-button-height)
                        (/ select-button-height 2)) "px")
    :z-index select-menu-z-index

    "& button"
    {:border-bottom "none !important"

     "&:last-child"
     {:border-bottom (gs [:borders :border-200-1])}

     "&:hover"
     {:background-color (gs [:colors :primary-500])
      :color (gs [:colors :true-white])}}}

   ".hx-forms--select-option-list-container-open"
   {:display "block !important"}

   ".hx-forms--select-option-container"
   {"& > *"
    {:margin (gs [:spacing :p0])}}

   ".hx-forms--select-option-button"
   (merge
    (create-font-styles {:style :body-20
                         :color :gray-scale-500
                         :family :primary})
    {:width "100%"
     :border (gs [:borders :border-primary-200-1])
     :outline :none
     :cursor :pointer
     :text-align :left
     :height (str select-button-height "px")
     :white-space :nowrap
     :overflow :hidden
     :text-overflow :ellipsis
     :transition (str "background-color 0.1s ease-in-out, "
                      "color 0.1s ease-in-out")})

   ".hx-forms--select-option-button-selected"
   {:background-color (gs [:colors :primary-300])
    :color (str (gs [:colors :true-white]) " !important")}

   ".hx-forms--select-selected-option-no-value"
   {:color (str (gs [:colors :gray-scale-400]) " !important")}

   ".hx-forms--select-option-button-no-selection"
   {:color (gs [:colors :gray-scale-400])
    :background-color (gs [:colors :gray-scale-25])}

   ".hx-forms--select-selected-option-container"
   {"&:before"
    {:display :block
     :position :absolute
     :content "''"
     :right (gs [:spacing :p8])
     :top "calc(50% - 2.5px)"
     :width "0"
     :height "0"
     :border-left "5px solid transparent"
     :border-right "5px solid transparent"}

    "& > button"
    {:border-radius (gs [:radius :r4])
     ;; Does not allow overflow to enter icon
     :padding-right (gs [:spacing :p20])}}

   ".hx-forms--select-selected-option-container-open"
   {"&:before"
    {:border-bottom (str "5px solid " (gs [:colors :gray-scale-100]))}}

   ".hx-forms--select-selected-option-container-closed"
   {"&:before"
    {:border-top (str "5px solid " (gs [:colors :gray-scale-100]))}}

   ".hx-forms--select-option-button-disabled"
   {:background-color (gs [:colors :gray-scale-25])
    :border (str (gs [:borders :border-50-1]) " !important")
    :color (str (gs [:colors :gray-scale-300]) " !important")}})

(def checkbox-styles
  {".hx-forms--checkbox-container"
   {:display :flex
    :align-items :center}

   ".hx-forms--checkbox-button-container"
   {:position :relative
    :width (gs [:spacing :p20])
    :height (gs [:spacing :p20])
    :border (gs [:borders :border-100-1])
    :background (gs [:colors :true-white])
    :outline :none
    :cursor :pointer
    :border-radius (gs [:radius :r4])}

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
    (create-font-styles {:style :caption-30
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
    .hx-forms--field-container-error > textarea"
   {:border-color (str (gs [:colors :error-500]) " !important")
    "&::placeholder"
    {:color (gs [:colors :error-100])}}

   ".hx-forms--field-container-error > label"
   {:color (gs [:colors :error-500])}

   ".hx-forms--field-container-hidden"
   {:display :none}

   ".hx-forms--toggle-field-container"
   {:display :flex
    :align-items :center

    "& > label"
    {:margin-right (gs [:spacing :p12])}}

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
                         :family :primary}))})

(def hx-form-styles
  (merge
   select-styles
   toggle-button-styles
   checkbox-styles
   field-styles
   {"input[type=text]" input-field-styles
    "input[type=email]" input-field-styles
    "input[type=number]" input-field-styles
    "input[type=search]" input-field-styles
    "input[type=url]" input-field-styles
    "input[type=password]" input-field-styles
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

     "& > button, & > input[type=submit]"
     (merge primary-button-styles)}}))
