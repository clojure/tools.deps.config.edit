(ns clojure.tools.deps.config.edit-test
  (:require
    [clojure.java.io :as jio]
    [clojure.test :refer [deftest is]]
    [clojure.tools.deps.config :as cfg]
    [clojure.tools.deps.config.edit :as sut]
    [clojure.tools.deps.util.dir :as dir])
  (:import
    [java.nio.file Files]
    [java.nio.file.attribute FileAttribute]))

(deftest test-write-config-round-trip
  (let [tmp (.toFile (Files/createTempDirectory "cli-config-test" (into-array FileAttribute [])))
        expected {:a 1 :b "two"}
        actual (dir/with-dir tmp
                 (sut/write-config :project 'my.test/example-tool expected)
                 (cfg/read-config :project 'my.test/example-tool))]
    (is (= expected actual))))

(deftest test-assoc-config-creates-file-when-missing
  (let [tmp (.toFile (Files/createTempDirectory "cli-config-test" (into-array FileAttribute [])))
        actual (dir/with-dir tmp
                 (sut/assoc-config :project 'my.test/example-tool :a 1)
                 (cfg/read-config :project 'my.test/example-tool))
        expected {:a 1}]
    (is (= expected actual))))

(deftest test-assoc-config-preserves-formatting
  (let [tmp (.toFile (Files/createTempDirectory "cli-config-test" (into-array FileAttribute [])))
        file (dir/with-dir tmp (cfg/config-file :project 'my.test/example-tool))
        original ";; highly customized\n{:a 1\n ;; the custom b setting\n :b 2}\n"
        expected ";; highly customized\n{:a 42\n ;; the custom b setting\n :b 2}\n"]
    (jio/make-parents file)
    (spit file original)
    (dir/with-dir tmp (sut/assoc-config :project 'my.test/example-tool :a 42))
    (is (= expected (slurp file)))))

(deftest test-assoc-config-adds-new-key
  (let [tmp (.toFile (Files/createTempDirectory "cli-config-test" (into-array FileAttribute [])))
        file (dir/with-dir tmp (cfg/config-file :project 'my.test/example-tool))
        expected {:a 1 :b 2}]
    (jio/make-parents file)
    (spit file "{:a 1}")
    (dir/with-dir tmp (sut/assoc-config :project 'my.test/example-tool :b 2))
    (is (= expected (dir/with-dir tmp (cfg/read-config :project 'my.test/example-tool))))))
