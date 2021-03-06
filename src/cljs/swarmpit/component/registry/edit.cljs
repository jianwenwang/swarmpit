(ns swarmpit.component.registry.edit
  (:require [material.components :as comp]
            [material.component.composite :as composite]
            [swarmpit.component.mixin :as mixin]
            [swarmpit.component.state :as state]
            [swarmpit.component.message :as message]
            [swarmpit.component.progress :as progress]
            [swarmpit.url :refer [dispatch!]]
            [swarmpit.ajax :as ajax]
            [swarmpit.routes :as routes]
            [sablono.core :refer-macros [html]]
            [rum.core :as rum]))

(enable-console-print!)

(defn- form-name [value]
  (comp/text-field
    {:label           "Name"
     :fullWidth       true
     :name            "name"
     :key             "name"
     :variant         "outlined"
     :value           value
     :required        true
     :disabled        true
     :margin          "normal"
     :InputLabelProps {:shrink true}}))

(defn- form-url [value]
  (comp/text-field
    {:label           "Url"
     :fullWidth       true
     :name            "url"
     :key             "url"
     :variant         "outlined"
     :value           value
     :required        true
     :disabled        true
     :margin          "normal"
     :InputLabelProps {:shrink true}}))

(defn- form-public [value]
  (comp/checkbox
    {:checked  value
     :value    (str value)
     :onChange #(state/update-value [:public] (-> % .-target .-checked) state/form-value-cursor)}))

(defn- registry-handler
  [registry-id]
  (ajax/get
    (routes/path-for-backend :registry {:id registry-id})
    {:state      [:loading?]
     :on-success (fn [{:keys [response]}]
                   (state/set-value response state/form-value-cursor))}))

(defn- update-registry-handler
  [registry-id]
  (ajax/post
    (routes/path-for-backend :registry-update {:id registry-id})
    {:params     (state/get-value state/form-value-cursor)
     :state      [:processing?]
     :on-success (fn [{:keys [origin?]}]
                   (when origin?
                     (dispatch!
                       (routes/path-for-frontend :registry-info {:id registry-id})))
                   (message/info
                     (str "Registry " registry-id " has been updated.")))
     :on-error   (fn [{:keys [response]}]
                   (message/error
                     (str "Registry update failed. " (:error response))))}))

(defn- init-form-state
  []
  (state/set-value {:loading?    true
                    :processing? false} state/form-state-cursor))

(def mixin-init-form
  (mixin/init-form
    (fn [{{:keys [id]} :params}]
      (init-form-state)
      (registry-handler id))))

(rum/defc form-edit < rum/static [{:keys [_id name url public]}
                                  {:keys [processing?]}]
  (comp/mui
    (html
      [:div.Swarmpit-form
       [:div.Swarmpit-form-context
        (comp/grid
          {:item true
           :xs   12
           :sm   6
           :md   4}
          (comp/card
            {:className "Swarmpit-form-card"
             :key       "rec"}
            (comp/card-header
              {:className "Swarmpit-form-card-header"
               :key       "rech"
               :title     "Edit Registry"})
            (comp/card-content
              {:key "recc"}
              (comp/grid
                {:container true
                 :key       "reccc"
                 :spacing   40}
                (comp/grid
                  {:item true
                   :key  "recccig"
                   :xs   12}
                  (form-name name)
                  (form-url url)
                  (comp/form-control
                    {:component "fieldset"
                     :key       "recccigc"}
                    (comp/form-group
                      {:key "recccigcg"}
                      (comp/form-control-label
                        {:control (form-public public)
                         :key     "recccigcgp"
                         :label   "Public"})))))
              (html
                [:div {:class "Swarmpit-form-buttons"
                       :key   "reccbtn"}
                 (composite/progress-button
                   "Save"
                   #(update-registry-handler _id)
                   processing?)]))))]])))

(rum/defc form < rum/reactive
                 mixin-init-form [_]
  (let [state (state/react state/form-state-cursor)
        registry (state/react state/form-value-cursor)]
    (progress/form
      (:loading? state)
      (form-edit registry state))))
