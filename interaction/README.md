# interaction

A Clojure library designed to ... well, that part is up to you.

## Usage

`lein run` will run the main function in interaction.core, which will launch the repl your coded

`lein repl` will start a read-execute-print-loop for you to test your code interactively

`docs/uberdoc.html` provides a web documentation for this MP 

## How to run the tests

`lein midje` will run all tests.

`lein midje namespace.*` will run only tests beginning with "namespace.".

`lein midje :autotest` will run all the tests indefinitely. It sets up a
watcher on the code files. If they change, only the relevant tests will be
run again.


## License

Copyright © 2019 Mattox Beckman

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
