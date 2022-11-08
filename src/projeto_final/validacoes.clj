(ns projeto-final.validacoes
  (:require [schema.core :as s])
  (:gen-class))

(s/defschema
  CDB_RDB
  {:tipo s/Str
   :valor s/Num
   :data_vencimento s/Str
   :quantidade s/Int
   :id_ativo_participante s/Str
   :data_emissao s/Str
   :forma_pagamento s/Str
   :conta_emissao s/Str
   :cnpj_cpf s/Str})

(s/defschema
  LAM
  {:tipo s/Str
   :valor s/Num
   :data_vencimento s/Str
   :quantidade s/Int
   :id_ativo_participante s/Str
   :local_emissao s/Str
   :local_pagamento s/Str
   :data_emissao s/Str
   :forma_pagamento s/Str
   :conta_emissao s/Str
   :cnpj_cpf s/Str})

(defn dados-com-erros [msg tipo]
  (if (= tipo "LAM")
    (let [erro (try (s/validate LAM msg) (catch Exception e "Erro de validação: " (.getMessage e)))]
      (if (string? erro)
        erro
        false))
    (let [erro (try (s/validate CDB_RDB msg) (catch Exception e "Erro de validação: " (.getMessage e)))]
      (if (string? erro)
        erro
        false))))

(defn gera-data []
  (.format (java.text.SimpleDateFormat. "dd/MM/yyyy") (new java.util.Date)))

(defn valida-json [msg]
  (instance? clojure.lang.PersistentHashMap msg))

(defn valida-number [valor quantidade]
  (and (number? valor) (number? quantidade)))

(defn valida-strings
  [& args]
  (every? string? args))
