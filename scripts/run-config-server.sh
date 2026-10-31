#!/usr/bin/env bash
set -euo pipefail
project_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
source "$project_dir/scripts/java17.sh"
cd "$project_dir"
if [[ -z "${CONFIG_GIT_URI:-}" ]]; then
  bash scripts/init-config-repo.sh
fi
exec java -jar config-server/target/config-server-1.0.0-SNAPSHOT.jar "$@"
