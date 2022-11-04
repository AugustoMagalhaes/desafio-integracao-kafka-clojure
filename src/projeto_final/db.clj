(ns projeto-final.db
  (:require [qbits.alia :as alia])
  (:gen-class))

(def session (alia/session {:session-keyspace "alia-test"
                            :contact-points ["host.docker.internal:7000"]}))

(defn remove-list [data]
  ;; função remove list do retorno da query e retorna só o mapa
  (peek (into [] data)))

(defn list-vector [data]
  ;;função troca list por vector do retorno da query
  (into [] data))

(defn cria [nome primeiro ultimo]
  (alia/execute session "insert into users (user_name, first_name, last_name) values (?, ?, ?)" {:values [nome primeiro ultimo]}))

(defn le [nome]
  (alia/execute session "select * from users where user_name=?" {:values [nome]}))

;; (defn -main
;;   "I don't do a whole lot ... yet."
;;   [& args]
;;   (cria "" "" "")
;;   (println "começooooooooooo")
;;   (let [teste (remove-list (le "kung fu panda"))]
;;     (println teste))
;;   (println "acaboooooooo"))