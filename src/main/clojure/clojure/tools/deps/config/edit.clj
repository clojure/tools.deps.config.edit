;   Copyright (c) Rich Hickey. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns clojure.tools.deps.config.edit
  "Functions for writing tool config files."
  (:require
    [clojure.java.io :as jio]
    [clojure.pprint :as pprint]
    [clojure.string :as str]
    [clojure.tools.deps.config :as cfg]
    [rewrite-clj.zip :as z]))

(set! *warn-on-reflection* true)

(defn write-config
  "Writes config as EDN to <location>/.cli-config/<lib-ns>/<lib-name>.edn
  with location defined by :user or :project. Overwrites any existing file."
  [location lib config]
  (let [file (cfg/config-file location lib)]
    (jio/make-parents file)
    (with-open [w (jio/writer file)]
      (binding [*print-namespace-maps* false]
        (pprint/pprint config w)))))

(defn assoc-config
  "Sets the value at k in <location>/.cli-config/<lib-ns>/<lib-name>.edn
  with location defined by :user or :project. Preserves all existing
  formatting. Creates the file with {k v} if it does not exist or is empty.
  Throws if the file cannot be parsed as a single EDN map."
  [location lib k v]
  (let [file (cfg/config-file location lib)
        content (when (.exists file) (slurp file))]
    (if (str/blank? content)
      (write-config location lib {k v})
      (let [zloc (z/of-string content)
            is-map? (= :map (z/tag zloc))
            nothing-after? (nil? (z/right zloc))
            path (.getPath file)]
        (if (and is-map? nothing-after?)
          (spit file (-> zloc (z/assoc k v) z/root-string))
          (throw (ex-info (format "Expected single map in %s" path) {:path path})))))))
