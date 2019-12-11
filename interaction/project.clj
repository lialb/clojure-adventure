(defproject interaction "0.1.0-SNAPSHOT"
  :description "A parser that remembers simple facts."
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [io.aviso/pretty "0.1.37"]]
  :profiles {:dev {:dependencies [[midje "1.9.9"]]}}
  :middleware [io.aviso.lein-pretty/inject]
  :plugins [[lein-marginalia "0.9.1"]
            [io.aviso/pretty "0.1.37"]
            [lein-midje "3.2.1"]
          ]
  :repl-options {:init-ns interaction.core}
  :main ^:skip-aot interaction.core/main
  )
  
