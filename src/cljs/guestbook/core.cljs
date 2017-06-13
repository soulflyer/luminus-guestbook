(ns guestbook.core)
(-> (.getElementById js/document "clojurescript-content")
    (.-innerHTML)
    (set! "Hello World!"))
