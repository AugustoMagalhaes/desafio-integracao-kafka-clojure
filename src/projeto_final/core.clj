(ns projeto-final.core
  (:gen-class)
  (:require
   [clojure.tools.logging :as log]
   [taoensso.timbre :as logt]
   [clojure.data.json :as json])
  (:import
   [java.util Properties]
   [org.apache.kafka.common.serialization Serdes Serde Serializer Deserializer StringSerializer StringDeserializer]
   [org.apache.kafka.streams KafkaStreams StreamsConfig Topology]
   [org.apache.kafka.streams.processor Processor ProcessorSupplier To]
   [org.apache.kafka.clients.admin AdminClientConfig NewTopic KafkaAdminClient]
   org.apache.kafka.clients.consumer.KafkaConsumer
   [org.apache.kafka.clients.producer KafkaProducer ProducerRecord]
   (org.apache.kafka.common TopicPartition)
   (java.time Duration)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println Properties Serdes Serde Serializer Deserializer StringSerializer StringDeserializer KafkaStreams StreamsConfig Topology Processor ProcessorSupplier To .log .logt .json AdminClientConfig NewTopic KafkaAdminClient KafkaConsumer KafkaProducer ProducerRecord TopicPartition Duration))
