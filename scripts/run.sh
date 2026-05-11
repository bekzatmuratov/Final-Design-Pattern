#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
"$ROOT/scripts/build.sh"
java -XstartOnFirstThread -jar "$ROOT/target/traffic-car-2d.jar"
