(defproject keymouse "0.1.0-FINAL"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.1stleg/jnativehook "2.0.2"]]
  :main ^:skip-aot keymouse.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
