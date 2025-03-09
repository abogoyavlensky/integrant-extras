(ns integrant-extras.tests
  (:require [integrant-extras.core :as ig-extras]
            [integrant.core :as ig]))

(def ^:dynamic *test-system* nil)

(defn with-system
  "Run the test system before tests."
  ([]
   (with-system nil))
  ([config-path]
   (fn
     [test-fn]
     (let [test-config (ig-extras/get-config :test config-path)]
       (ig/load-namespaces test-config)
       (binding [*test-system* (ig/init test-config)]
         (try
           (test-fn)
           (finally
             (ig/halt! *test-system*))))))))
