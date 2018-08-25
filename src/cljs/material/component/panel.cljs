(ns material.component.panel
  (:require [material.component :as cmp]
            [material.icon :as icon]
            [sablono.core :refer-macros [html]]))

(defn search [placeholder on-change-fn]
  (html
    [:div {:className "Swarmpit-form-panel-search"}
     [:div
      (cmp/input
        {:placeholder      placeholder
         :onChange         on-change-fn
         :fullWidth        true
         :className        "Swarmpit-form-panel-search-input"
         :id               "Swarmpit-form-panel-filter"
         :disableUnderline true})]
     (cmp/icon-button
       {:className "Swarnpit-form-panel-search-icon"
        :disabled  true} icon/search)]))






;(defn text-field
;  [props]
;  (cmp/mui
;    (cmp/text-field
;      (merge props
;             {:underlineStyle {:borderColor "rgba(0, 0, 0, 0.2)"}
;              :style          {:height     "44px"
;                               :lineHeight "15px"}}))))
;
;(defn checkbox
;  [props]
;  (cmp/mui
;    (cmp/checkbox
;      (merge props
;             {:style      {:width     "200px"
;                           :marginTop "12px"}
;              :labelStyle {:left -10}}))))
;
;(defn info
;  ([icon text]
;   [:div.form-panel-info
;    [:span.form-panel-info-icon
;     (cmp/mui (cmp/svg icon))]
;    [:span.form-panel-info-text text]])
;  ([icon text state]
;   [:div.form-panel-info
;    [:span.form-panel-info-icon
;     (cmp/mui (cmp/svg icon))]
;    [:span.form-panel-info-text text]
;    [:span.form-panel-info-label state]]))
