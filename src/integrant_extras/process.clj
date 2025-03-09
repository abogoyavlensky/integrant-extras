(ns integrant-extras.process
  (:require [clojure.java.process :as process]
            [clojure.tools.logging :as log]
            [integrant-extras.core :as ig-extras]
            [integrant.core :as ig]))

(defmethod ig/assert-key ::process
  [_ params]
  (ig-extras/validate-schema!
    {:component ::process
     :data params
     :schema [:map
              [:cmd [:vector {:min 1} string?]]]}))

(defmethod ig/init-key ::process
  [_ options]
  (log/info (format "[PROCESS] Starting process %s..." (:cmd options)))
  {:options options
   :process (apply process/start (merge {:out :inherit} (:opts options)) (:cmd options))})

(defmethod ig/halt-key! ::process
  [_ {:keys [options process]}]
  (log/info (format "[PROCESS] Stopping process %s..." (:cmd options)))
  (when (some? process)
    (.destroyForcibly process)))
