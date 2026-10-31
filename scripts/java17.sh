#!/usr/bin/env bash
# Source this file from other scripts to select and verify JDK 17.
if [[ "$(uname -s)" == "Darwin" ]]; then
  export JAVA_HOME="$(/usr/libexec/java_home -v 17)"
fi
if [[ -n "${JAVA_HOME:-}" ]]; then
  export PATH="$JAVA_HOME/bin:$PATH"
fi
java_version="$(java -version 2>&1 | head -n 1)"
if [[ ! "$java_version" =~ \"17\. ]]; then
  echo "Can JDK 17. Hay dat JAVA_HOME den JDK 17. Hien tai: $java_version" >&2
  exit 1
fi
