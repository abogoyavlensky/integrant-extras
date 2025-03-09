(ns integrant-extras.core-test
  (:require [clojure.test :refer :all]
            [integrant-extras.core :as core]))

(deftest test-sum
  (is (= 3 (core/sum 1 2))))
