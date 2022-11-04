(ns projeto-final.db
  (:require [qbits.alia :as alia])
  (:gen-class))


(def session (alia/session {:session-keyspace "alia_test"
                            :contact-points ["host.docker.internal:9042"]
                            :load-balancing-local-datacenter "datacenter1"}))

;(alia/execute session "CREATE KEYSPACE testegrupo WITH replication = {:'class' 'SimpleStrategy', 'replication_factor': 3};")

;; (def prepara (alia/prepare session "INSERT INTO users
;;                          (user_name, first_name, last_name)
;;                          VALUES(?, ?, ?);"))

;; (defn cria [nome primeiro ultimo]
;;   (alia/execute session prepara {:values [nome, primeiro, ultimo]}))

;; (defn le [nome] (alia/execute session "select * from users where user_name=?" {:values nome}))

;; (def prepara-le (alia/prepare session "select * from projetoteste.users where user_name= :nome limit :lmt;"))

;; (defn le [nome] (alia/execute session prepara-le {:values {:nome nome :lmt (int 1)}}))

(defn cria-tabela-registro-tipo
  []
  (alia/execute session "CREATE TABLE IF NOT EXISTS registro_por_tipo (tipo varchar,
                                              valor double,
                                              data_vencimento varchar,
                                              id_gerado varchar,
                                              quantidade int,
                                              id_ativo_participante varchar,
                                              data_emissao varchar,
                                              local_emissao varchar,
                                              local_pagamento varchar,
                                              forma_pagamento varchar,
                                              conta_emissao varchar,
                                              status varchar,
                                              cnpj_cpf varchar,
                                              PRIMARY KEY ((tipo), valor) );"))

(defn cria-tabela-registro-id-ativo
  []
  (alia/execute session "CREATE TABLE IF NOT EXISTS registro_por_id_ativo (id_ativo_participante varchar,
                                              valor varchar,
                                              data_vencimento varchar,
                                              id_gerado varchar,
                                              quantidade int,
                                              tipo varchar,
                                              data_emissao varchar,
                                              local_emissao varchar,
                                              local_pagamento varchar,
                                              forma_pagamento varchar,
                                              conta_emissao varchar,
                                              status varchar,
                                              cnpj_cpf varchar,
                                              PRIMARY KEY ((id_ativo_participante), valor) );"))

(defn cria-tabela-registro-cadastro
  []
  (alia/execute session "CREATE TABLE IF NOT EXISTS registro_por_cnpj_cpf (cnpj_cpf varchar,
                                              valor varchar,
                                              data_vencimento varchar,
                                              id_gerado varchar,
                                              quantidade int,
                                              tipo varchar,
                                              data_emissao varchar,
                                              local_emissao varchar,
                                              local_pagamento varchar,
                                              forma_pagamento varchar,
                                              conta_emissao varchar,
                                              status varchar,
                                              id_ativo_participante varchar,
                                              PRIMARY KEY ((cnpj_cpf), valor) );"))


(defn popula-registro-tipo
  [tipo, valor, data_vencimento, id_gerado, quantidade, id_ativo_participante, data_emissao, forma_pagamento, conta_emissao, status, cnpj_cpf]
  (let [query-prep (alia/prepare session "INSERT INTO alia_test.registro_por_tipo
                         (tipo, valor, data_vencimento, id_gerado, quantidade, id_ativo_participante, data_emissao, forma_pagamento, conta_emissao, status, cnpj_cpf)
                         VALUES(:tipo, :valor, :data_vencimento, :id_gerado, :quantidade, :id_ativo_participante, :data_emissao, :forma_pagamento, :conta_emissao, :status, :cnpj_cpf);")]
    (alia/execute session query-prep {:values {:tipo tipo :valor valor :data_vencimento data_vencimento :id_gerado id_gerado :quantidade quantidade :id_ativo_participante id_ativo_participante :data_emissao data_emissao :forma_pagamento forma_pagamento :conta_emissao conta_emissao :status status :cnpj_cpf cnpj_cpf}}))
  )

(defn -main []
  (cria-tabela-registro-tipo)
  (cria-tabela-registro-id-ativo)
  (cria-tabela-registro-cadastro)
  (popula-registro-tipo "cdb" 80.00 "20-03-2023" "ASB900" (int 23) "100201" "20-03-2022" "a vista" "2002a" "pendente" "08125792621")
  )