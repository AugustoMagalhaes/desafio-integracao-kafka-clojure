(ns projeto-final.db
  (:require [qbits.alia :as alia])
  (:gen-class))

(def session (alia/session {:session-keyspace "alia-test"
                            :contact-points ["host.docker.internal:7000"]}))

(alia/execute session "CREATE KEYSPACE testegrupo WITH replication = {:'class' 'SimpleStrategy', 'replication_factor': 3};")

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println session))