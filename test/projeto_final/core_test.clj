(ns projeto-final.core-test
  (:require [clojure.test :refer :all]
            [projeto-final.core :refer :all]
            [projeto-final.db :refer :all]
            [projeto-final.validacoes :refer :all]))

(def quantidade-em-string {:tipo "CDB" :valor 22 :data_emissao "22-09-2021" :data_vencimento "22-09-2022" :quantidade "22" :id_ativo_participante "123" :forma_pagamento "a prazo" :conta_emissao "123123" :cnpj_cpf "123123123" })

(def msg-sem-cnpj-cpf {:tipo "RDB" :valor 22 :data_emissao "22-09-2021" :data_vencimento "22-09-2022" :quantidade "22" :id_ativo_participante "123" :forma_pagamento "a prazo" :conta_emissao "123123"})

(def mapa-vazio {})

(deftest validacao-json
  (testing "Campo quantidade em string retorna erro"
    (is (instance? java.lang.String (dados-com-erros? quantidade-em-string "CDB"))))
  (testing "Campo cnpj_cpf ausente retorna erro"
    (is (instance? java.lang.String (dados-com-erros? msg-sem-cnpj-cpf "RDB"))))
  (testing "Objeto vazio retorna erro"
    (is (instance? java.lang.String (dados-com-erros? mapa-vazio "LAM")))))


(deftest validacao-gerador-id
  (testing "Tipo CDB com offset 12"
    (is (= (gera-id "CDB" 12) "CDB00012")))
  (testing "Tipo RDB com offset 9"
    (is (= (gera-id "RDB" 9) "RDB00009")))
  (testing "Tipo LAM com offset 500 tem 8 digitos"
    (is (= (count (gera-id "RDB" 9)) 8))))