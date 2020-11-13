(ns compute.spec-keys.core-test
  (:require
    [clojure.test :refer :all]
    [clojure.spec.alpha :as s]
    [compute.spec-keys.core :as sk]))

(s/def ::a int?)
(s/def ::map (s/keys :req [::a]))
(s/def ::map-pointer ::map)

(defmulti my-multi-spec ::dispatch)

(s/def ::default-k int?)
(defmethod my-multi-spec :default [_] (s/keys :req [::default-k]))

(s/def ::b int?)
(defmethod my-multi-spec "b" [_] (s/keys :opt [::b]))

(s/def ::map-multispec
  (s/merge ::map (s/multi-spec my-multi-spec ::dispatch)))

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
  (is (= #{::a} (sk/parse-spec-keys (s/keys :req [::a]))))
  (is (= #{::a ::b ::default-k} (sk/parse-spec-keys ::map-multispec))))