#!/usr/bin/env bash
set -euo pipefail
project_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
source "$project_dir/scripts/java17.sh"
cd "$project_dir"
exec java -jar pharmacy-service/target/pharmacy-service-1.0.0-SNAPSHOT.jar "$@"
