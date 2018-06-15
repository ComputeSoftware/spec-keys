(ns compute.spec-keys.core-test
  (:require
    [clojure.test :refer :all]
    [clojure.spec.alpha :as s]
    [compute.spec-keys.core :as sk]))

(s/def ::a int?)
(s/def ::map (s/keys :req [::a]))

(deftest parse-spec-keys-test
  (is (= #{:a :b :c :d :e}
         (sk/parse-spec-keys
           `(s/keys :req [:a (and :b (or :c :d))]
                    :opt [:e]))))
  (is (= #{:a :b :c}
         (sk/parse-spec-keys
           `(s/merge (s/keys :req [:a])
                     (s/merge (s/keys :opt-un [:b])
                              (s/keys :req [:c]))))))
  (is (= #{:a :b}
         (sk/parse-spec-keys `(s/or :a (s/keys :req [:a])
                                    :b (s/keys :req [:b])))))
  (is (= #{:a}
         (sk/parse-spec-keys `(s/and (s/keys :req [:a])
                                     (fn [_] true)))))

  (is (= #{::a} (sk/parse-spec-keys ::map)))
  (is (= #{::a} (sk/parse-spec-keys (s/keys :req [::a])))))