(ns projeto-final.validacoes
  (:gen-class))

(defn valida-json [msg]
  (instance? clojure.lang.PersistentHashMap msg))

(defn valida-number [valor quantidade]
  (and (number? valor) (number? quantidade)))

(defn valida-strings
  [& args]
  (every? string? args))
