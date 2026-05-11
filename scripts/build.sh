#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
mvn -q -DskipTests clean package
rm -f "$ROOT"/target/original-*.jar
echo "Built $ROOT/target/traffic-car-2d.jar"
