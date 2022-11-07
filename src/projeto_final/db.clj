(ns projeto-final.db
  (:require [qbits.alia :as alia])
  (:gen-class))


(def session (alia/session {:session-keyspace "alia_test"
                            :contact-points ["host.docker.internal:9042"]
                            :load-balancing-local-datacenter "datacenter1"}))

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

(defn cria-tabela-registro-tipo
  []
  (alia/execute session "CREATE TABLE IF NOT EXISTS registro_por_tipo (tipo varchar,
                                              id_gerado varchar,
                                              data_vencimento varchar,
                                              valor double,
                                              quantidade int,
                                              id_ativo_participante varchar,
                                              data_emissao varchar,
                                              local_emissao varchar,
                                              local_pagamento varchar,
                                              forma_pagamento varchar,
                                              conta_emissao varchar,
                                              status varchar,
                                              cnpj_cpf varchar,
                                              PRIMARY KEY ((tipo) id_gerado) );"))

(defn popula-registro-tipo
  [tipo, id_gerado, data_vencimento, valor, quantidade, id_ativo_participante, data_emissao, forma_pagamento, conta_emissao, status, cnpj_cpf]
  (let [query-prep (alia/prepare session "INSERT INTO alia_test.registro_por_tipo
                         (tipo, id_gerado, data_vencimento, valor, quantidade, id_ativo_participante, data_emissao, forma_pagamento, conta_emissao, status, cnpj_cpf)
                         VALUES(:tipo, :valor, :data_vencimento, :id_gerado, :quantidade, :id_ativo_participante, :data_emissao, :forma_pagamento, :conta_emissao, :status, :cnpj_cpf);")]
    (alia/execute session query-prep {:values {:tipo tipo :id_gerado id_gerado :data_vencimento data_vencimento :valor (double valor) :quantidade (int quantidade) :id_ativo_participante id_ativo_participante :data_emissao data_emissao :forma_pagamento forma_pagamento :conta_emissao conta_emissao :status status :cnpj_cpf cnpj_cpf}})))

(defn cria-tabela-registro-id-gerado
  []
  (alia/execute session "CREATE TABLE IF NOT EXISTS registro_por_id_gerado (id_gerado varchar,
                                              tipo varchar,
                                              valor varchar,
                                              id_ativo_participante varchar,
                                              data_vencimento varchar,
                                              quantidade int,
                                              data_emissao varchar,
                                              local_emissao varchar,
                                              local_pagamento varchar,
                                              forma_pagamento varchar,
                                              conta_emissao varchar,
                                              status varchar,
                                              cnpj_cpf varchar,
                                              PRIMARY KEY ((id_gerado)) );"))

(defn popula-registro-id
  [id_gerado, tipo, valor, id_ativo_participante, data_vencimento, quantidade, data_emissao, forma_pagamento, conta_emissao, status, cnpj_cpf]
  (let [query-prep (alia/prepare session "INSERT INTO alia_test.registro_por_id
                         (id_gerado, tipo, valor, id_ativo_participante, data_vencimento, quantidade, data_emissao, forma_pagamento, conta_emissao, status, cnpj_cpf)
                         VALUES(:id_gerado, :tipo, :valor, :id_ativo_participante, :data_vencimento, :quantidade, :data_emissao, :forma_pagamento, :conta_emissao, :status, :cnpj_cpf);")]
    (alia/execute session query-prep {:values {:id_gerado id_gerado :tipo tipo :valor (double valor) :id_ativo_participante id_ativo_participante :data_vencimento data_vencimento :quantidade (int quantidade) :data_emissao data_emissao :forma_pagamento forma_pagamento :conta_emissao conta_emissao :status status :cnpj_cpf cnpj_cpf}})))

(defn cria-tabela-registro-cadastro
  []
  (alia/execute session "CREATE TABLE IF NOT EXISTS registro_por_cnpj_cpf (cnpj_cpf varchar,
                                              tipo varchar,
                                              valor varchar,
                                              id_gerado varchar,
                                              data_vencimento varchar,
                                              quantidade int,
                                              data_emissao varchar,
                                              local_emissao varchar,
                                              local_pagamento varchar,
                                              forma_pagamento varchar,
                                              conta_emissao varchar,
                                              status varchar,
                                              id_ativo_participante varchar,
                                              PRIMARY KEY ((cnpj_cpf), tipo, valor, id_gerado) );"))


(defn popula-registro-cadastro
  [cnpj_cpf, tipo, valor, id_gerado, data_vencimento, quantidade, data_emissao, forma_pagamento, conta_emissao, status, id_ativo_participante]
  (let [query-prep (alia/prepare session "INSERT INTO alia_test.registro_por_cnpj_cpf
                         (cnpj_cpf, tipo, valor, id_gerado, data_vencimento, quantidade, data_emissao, forma_pagamento, conta_emissao, status, id_ativo_participante)
                         VALUES(:cnpj_cpf, :tipo, :valor, :id_gerado, :data_vencimento, :quantidade, :data_emissao, :forma_pagamento, :conta_emissao, :status, :id_ativo_participante);")]
    (alia/execute session query-prep {:values {:cnpj_cpf cnpj_cpf :tipo tipo :valor (double valor) :id_gerado id_gerado :data_vencimento data_vencimento :quantidade (int quantidade) :data_emissao data_emissao :forma_pagamento forma_pagamento :conta_emissao conta_emissao :status status :id_ativo_participante id_ativo_participante}})))


(defn gera-id
  [tipo offset]
  (let [tipo-maiusculo (.toUpperCase tipo)
        offset-texto (str offset)
        zeros (reduce str (repeat (- 8 3 (count offset-texto)) "0"))]
    (str tipo-maiusculo zeros offset-texto)))

(defn -main []
  (cria-tabela-registro-tipo)
  (cria-tabela-registro-id-gerado)
  (cria-tabela-registro-cadastro)
  (popula-registro-tipo "rdb" 71.00 "20-03-2023" "ASB900" 33 "100201" "20-03-2022" "a vista" "2002a" "pendente" "08125792621"))
