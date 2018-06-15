(defproject compute/spec-keys "0.1.2-SNAPSHOT"
  :description "Get the keys from a Spec."
  :url "https://github.com/ComputeSoftware/spec-keys"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0" :scope "provided"]]
  :deploy-repositories [["releases" {:url      "https://clojars.org/repo"
                                     :username :env/clojars_user
                                     :password :env/clojars_pass}]])
