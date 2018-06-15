# spec-keys

A small library that parses a Spec form and returns a set of keys used.

## Install

```clojure
[compute/spec-keys "0.1.0"]
```

## Usage

```clojure
(require '[clojure.spec.alpha :as s])
(require '[compute.spec-keys.core :as sk])

(s/def ::a int?)
(s/def ::map (s/keys :req [::a]))

(sk/parse-spec-keys ::map)
=> #{::a}
```

## License

Copyright © 2018 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
