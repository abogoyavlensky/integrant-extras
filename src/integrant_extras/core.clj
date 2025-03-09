(ns integrant-extras.core
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [integrant.core :as ig]
            [malli.core :as m]
            [malli.error :as me]
            [malli.util :as mu])
  (:import (clojure.lang IFn)
           (java.net ServerSocket)))

; Config

(def SYSTEM-CONFIG-PATH-DEFAULT "config.edn")

; Add #ig/ref tag for reading integrant config from aero.
(defmethod aero/reader 'ig/ref
  [_ _ value]
  (ig/ref value))

(defmethod aero/reader 'free-port
  [_ _ _value]
  (with-open [socket (ServerSocket. 0)]
    (.getLocalPort socket)))

(defn get-config
  "Return edn config with all variables set."
  ([profile]
   (get-config profile nil))
  ([profile config-path]
   (-> (or config-path SYSTEM-CONFIG-PATH-DEFAULT)
       (io/resource)
       (aero/read-config {:profile profile
                          :resolver aero/resource-resolver}))))

(defn validate-schema!
  "Validate data against schema and throw a humanized error if data is not valid."
  [{:keys [component schema data]}]
  (some-> schema
          (mu/closed-schema)
          (m/explain data)
          (me/with-spell-checking)
          (me/humanize)
          (#(throw (Exception. (format "Invalid %s component config: %s" component %))))))

; System

(defn at-shutdown
  "Add hook for shutdown system on sigterm."
  [system]
  (-> (Runtime/getRuntime)
      (.addShutdownHook
        (Thread. ^IFn (bound-fn []
                        (log/info "[SYSTEM] System is stopping...")
                        (ig/halt! system)
                        (shutdown-agents)
                        (log/info "[SYSTEM] System has been stopped."))))))

(defn run-system
  "Run application system."
  [{:keys [profile config-path]}]
  (let [config (get-config profile config-path)]
    (log/info "[SYSTEM] System is starting with profile:" profile)
    (ig/load-namespaces config)
    (-> config
        (ig/init)
        (at-shutdown))
    (log/info "[SYSTEM] System has been started successfully.")))
