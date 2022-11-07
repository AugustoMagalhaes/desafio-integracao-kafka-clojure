(ns projeto-final.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [projeto-final.db :as db]
            [taoensso.timbre :as log])
  (:import [java.util Properties]
           [org.apache.kafka.common.serialization
            Deserializer
            Serde
            Serdes
            Serializer
            StringDeserializer
            StringSerializer]
           [org.apache.kafka.streams KafkaStreams StreamsConfig Topology]
           [org.apache.kafka.streams.processor Processor ProcessorSupplier To]))
;; (defn -main
;;   "I don't do a whole lot ... yet."
;;   [& args]
;;   (println Properties Serdes Serde Serializer Deserializer StringSerializer StringDeserializer KafkaStreams StreamsConfig Topology Processor ProcessorSupplier To
;;            log/info logt/info .json AdminClientConfig NewTopic KafkaAdminClient KafkaConsumer KafkaProducer ProducerRecord TopicPartition Duration))


(deftype Desserializador []
  Deserializer
  (close [_])
  (configure [_ configs isKey])
  (deserialize [_ topic data]
    (json/read-str (.deserialize (StringDeserializer.) topic data) :key-fn keyword)))



(deftype Serializador []
  Serializer
  (close [_])
  (configure [_ configs isKey])
  (serialize [_ topic data]
    (.serialize (StringSerializer.) topic (json/write-str data))))



(deftype Serde-personalizado []
  Serde
  (close [_])
  (configure [_ configs isKey])
  (deserializer [_] (Desserializador.))
  (serializer [_] (Serializador.)))



(def props
  (doto (Properties.)
    (.putAll
     {StreamsConfig/APPLICATION_ID_CONFIG  "trt-topology"
      StreamsConfig/BOOTSTRAP_SERVERS_CONFIG "host.docker.internal:29092"
      StreamsConfig/DEFAULT_KEY_SERDE_CLASS_CONFIG (.getClass (Serdes/String))
      StreamsConfig/DEFAULT_VALUE_SERDE_CLASS_CONFIG (.getClass (Serde-personalizado.))})))

(deftype  Processador [^:volatile-mutable context]
  Processor
  (close [_])
  (init [_ c]
    (set! context c))
  (process [_ k msg]
    (case (.topic context)
      "registro.cdb"
      (when (= (:status msg) "pendente")
        (log/info "Mensagem recebida, iniciando processo...")

        (.forward context (:tipo msg) (assoc msg :status "executado") (To/child "cmd-cdb"))
        (log/info "mensagem: " msg)
        (log/info "a mensagem eh.................... : " k)
        (log/info "tipo de valor: " (type (:valor msg)))
        (log/info "tipo de quantidade: " (type (:quantidade msg)))
        (let [tipo (:tipo msg) id_gerado (:id_gerado msg) data_vencimento (:data_vencimento msg) valor (:valor msg) quantidade (int (:quantidade msg)) id_ativo_participante (:id_ativo_participante msg) data_emissao (:data_emissao msg) forma_pagamento (:forma_pagamento msg) conta_emissao (:conta_emissao msg) status (:status msg) cnpj_cpf (:cnpj_cpf msg)]

          (db/popula-registro-tipo tipo id_gerado data_vencimento valor quantidade id_ativo_participante data_emissao forma_pagamento conta_emissao status cnpj_cpf))
        (log/info "CHEGOU ATE AQUI..........................................")
        (spit "relatorio.txt" (str k ": " msg "\n") :append true))
             ; checa se existe participante antes....
             ; gera o id antes
      )))

(deftype Supplier-processador []
  ProcessorSupplier
  (get [_]
    (Processador. nil)))

(defn topology []
  (doto (Topology.)
    (.addSource     "registros-in"                          (into-array String ["controle" "registro" "relatorio"]))
    (.addProcessor  "processador" (Supplier-processador.)  (into-array String ["registros-in"]))
    (.addSink       "cmd-controle" "controle"                       (into-array String ["processador"]))
    (.addSink       "cmd-registro" "registro"                       (into-array String ["processador"]))
    (.addSink       "cmd-relatorio" "relatorio"                     (into-array String ["processador"]))))

(defn -main [& args]
(.start (KafkaStreams. (topology) props)))