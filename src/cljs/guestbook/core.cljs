(ns guestbook.core
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST]]))

(defn send-message! [fields errors messages]
  (POST "/message"
        {:headers
         {"Accept" "application/transit+json"
          "x-csrf-token" (.-value (.getElementById js/document "token"))}
         :params @fields
         :handler #(do
                     (reset! errors nil)
                     (swap! messages conj (assoc @fields :timestamp (js/Date.))))
         :error-handler #(do
                           (.log js/console (str %))
                           (reset! errors (get-in % [:response :errors])))}))

(defn errors-component [errors id]
  (when-let [error (id @errors)]
    [:div.alert.alert-danger (clojure.string/join error)]))

(defn message-form []
  (let [fields (atom {})
        errors (atom nil)]
    (fn []
      [:div.content
       [:div.form-group
        [errors-component errors :name]
        [:p "Name:"
         [:input.form-control
          {:type :text
           :name :name
           :on-change #(swap! fields assoc :name (-> % .-target .-value))
           :value (:name @fields)}]]
        [errors-component errors :message]
        [:p "Message:"
         [:textarea.form-control
          {:rows 4
           :cols 50
           :name :message
           :on-change #(swap! fields assoc :message (-> % .-target .-value))}
          (:message @fields)]]
        [:input.btn.btn-primary
         {:type :submit
          :on-click #(send-message! fields errors) :value "comment"}]]])))


;; (defn message-form []
;;   (let [fields (atom {})
;;         errors (atom nil)]
;;     (fn [] [:div.content
;;             [:div.form-group
;;              [:p "name:" (:name @fields)]
;;              [:p "message:" (:message @fields)]
;;              [errors-component errors :name]
;;              [:p "Name:"
;;               [:input.form-control
;;                {:type :text
;;                 :name :name
;;                 :on-change #(swap!
;;                               fields
;;                               assoc
;;                               :name
;;                               (-> % .-target .-value))
;;                 :value (:name @fields)}]]]
;;             [errors-component errors :message]
;;             [:p "Message:"
;;              [:textarea.form-control
;;               {:rows 4
;;                :cols 50
;;                :name :message
;;                :on-change #(swap!
;;                              fields
;;                              assoc
;;                              :message
;;                              (-> % .-target .-value))}
;;               (:message @fields)]]
;;             [:input.btn.btn-primary
;;              {:type :submit
;;               :on-click #(send-message! fields errors)
;;               :value "comment"}]])))

(defn home []
  [:div.row
   [:div.span12
    [message-form]]])

(reagent/render
  [home]
(.getElementById js/document "clojurescript-content"))
