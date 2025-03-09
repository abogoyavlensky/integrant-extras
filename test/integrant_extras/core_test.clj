(ns integrant-extras.core-test
  (:require [clojure.test :refer :all]
            [integrant-extras.core :as core]))

(deftest test-get-config
  (testing "Should load config with test profile"
    (let [config (core/get-config :test "test-config.edn")]
      (is (= "test-value" (:test-key config)))
      (is (= {:key "value"} (:test/nested config)))
      (is (true? (get-in config [:profile-specific :only-in-test])))
      (is (nil? (get-in config [:profile-specific :only-in-dev])))
      (is (= :test/nested (:key (:ref-value config))))
      (is (number? (:port config)))))

  (testing "Should use default config path when not specified"
    (with-redefs [core/SYSTEM-CONFIG-PATH-DEFAULT "test-config.edn"]
      (let [config (core/get-config :test)]
        (is (= "test-value" (:test-key config)))))))

(deftest test-validate-schema!
  (testing "Should throw exception when data doesn't match schema"
    (let [component "test-component"
          schema [:map [:name string?] [:age int?]]
          invalid-data {:name "John"
                        :age "30"}]
      (is (thrown-with-msg?
            Exception
            #"Invalid test-component component config:.*"
            (core/validate-schema! {:component component
                                    :schema schema
                                    :data invalid-data})))))

  (testing "Should not throw exception when schema is nil"
    (is (nil? (core/validate-schema! {:component "test-component"
                                      :schema nil
                                      :data {:name "John"
                                             :age 30}}))))

  (testing "Should throw exception with proper error message for missing required keys"
    (let [component "test-component"
          schema [:map [:name string?] [:age int?]]
          invalid-data {:name "John"}]
      (is (thrown-with-msg?
            Exception
            #"Invalid test-component component config:.*:age.*"
            (core/validate-schema! {:component component
                                    :schema schema
                                    :data invalid-data})))))

  (testing "Should throw exception with proper error message for extra keys"
    (let [component "test-component"
          schema [:map [:name string?] [:age int?]]
          invalid-data {:name "John"
                        :age 30
                        :extra "value"}]
      (is (thrown-with-msg?
            Exception
            #"Invalid test-component component config:.*:extra.*"
            (core/validate-schema! {:component component
                                    :schema schema
                                    :data invalid-data}))))))
