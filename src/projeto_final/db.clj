(ns projeto-final.db
  (:require [qbits.alia :as alia])
  (:gen-class))

(def session (alia/session {:session-keyspace "alia-test"
                            :contact-points ["host.docker.internal:7000"]}))

(alia/execute session "CREATE KEYSPACE testegrupo WITH replication = {:'class' 'SimpleStrategy', 'replication_factor': 3};")

(def prepara (alia/prepare session "INSERT INTO users
                         (user_name, first_name, last_name)
                         VALUES(?, ?, ?);"))

(defn cria [nome primeiro ultimo]
  (alia/execute session prepara {:values [nome, primeiro, ultimo]}))

(defn le [nome] (alia/execute session "select * from users where user_name=?" {:values nome}))

(def prepara-le (alia/prepare session "select * from projetoteste.users where user_name= :nome limit :lmt;"))

(defn le [nome] (alia/execute session prepara-le {:values {:nome nome :lmt (int 1)}}))

;; (defn -main
;;   "I don't do a whole lot ... yet."
;;   [& args]
;;   (cria "" "" "")
;;   (println "começooooooooooo")
;;   (let [teste (le "kung fu panda")]
;;     (println teste))
;;   (println "acaboooooooo"))