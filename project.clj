(defproject projeto-final "0.1.0-SNAPSHOT"
  :description "Projeto final de integração Clojure, Kafka e Cassandra"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[com.fzakaria/slf4j-timbre "0.3.17"]
                 [org.apache.kafka/kafka-streams "3.2.1"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/data.json "2.4.0"]
                 [cc.qbits/alia-all "5.0.0-alpha7"]
                 [org.clojure/tools.logging "0.4.0"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jmdk/jmxtools
                                                    com.sun.jmx/jmxri]]]
  :main ^:skip-aot projeto-final.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
