(ns projeto-final.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [taoensso.timbre :as logt])
  (:import (java.time Duration)
           [java.util Properties]
           [org.apache.kafka.clients.admin AdminClientConfig KafkaAdminClient NewTopic]
           org.apache.kafka.clients.consumer.KafkaConsumer
           [org.apache.kafka.clients.producer KafkaProducer ProducerRecord]
           (org.apache.kafka.common TopicPartition)
           [org.apache.kafka.common.serialization
            Deserializer
            Serde
            Serdes
            Serializer
            StringDeserializer
            StringSerializer]
           [org.apache.kafka.streams KafkaStreams StreamsConfig Topology]
           [org.apache.kafka.streams.processor Processor ProcessorSupplier To]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println Properties Serdes Serde Serializer Deserializer StringSerializer StringDeserializer KafkaStreams StreamsConfig Topology Processor ProcessorSupplier To 
           log/info logt/info .json AdminClientConfig NewTopic KafkaAdminClient KafkaConsumer KafkaProducer ProducerRecord TopicPartition Duration))


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
             StreamsConfig/BOOTSTRAP_SERVERS_CONFIG "host.docker.internal:9096"
             StreamsConfig/DEFAULT_KEY_SERDE_CLASS_CONFIG (.getClass (Serdes/String))
             StreamsConfig/DEFAULT_VALUE_SERDE_CLASS_CONFIG (.getClass (Serde-personalizado.))})))

