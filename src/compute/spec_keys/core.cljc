(ns compute.spec-keys.core
  (:require
    [clojure.spec.alpha :as s]))

(defn spec-form
  [keyword-or-form]
  (if (or (keyword? keyword-or-form)
        (s/spec? keyword-or-form))
    (s/form keyword-or-form)
    keyword-or-form))

(defn parse-spec-keys
  "Given a `spec`, returns a set of keys the spec has."
  [spec]
  (let [form (spec-form spec)
        [form-sym & form-args] form
        combine-composite-specs (fn [forms]
                                  (into #{} (mapcat parse-spec-keys) forms))]
    (case form-sym
      (clojure.spec.alpha/keys
        cljs.spec.alpha/keys)
      (->> (rest form-args)
        (take-nth 2)
        (flatten)
        (filter keyword?)
        (set))

      ;; TODO: support cljs
      #?@(:clj
          [(clojure.spec.alpha/multi-spec
             cljs.spec.alpha/multi-spec)
           (let [multf @(resolve (first form-args))]
             (combine-composite-specs
               (map (fn [[_ multi-impl-f]]
                      (multi-impl-f nil))
                 (methods multf))))])

      (clojure.spec.alpha/merge
        cljs.spec.alpha/merge
        clojure.spec.alpha/and
        cljs.spec.alpha/and)
      (combine-composite-specs form-args)


      (clojure.spec.alpha/or
        cljs.spec.alpha/or)
      (combine-composite-specs (->> (rest form-args) (take-nth 2)))

      nil)))