#!/bin/bash

# --- Prüfen, ob Argumente übergeben wurden ---
if [ $# -lt 1 ]; then
  echo "Usage: $0 <container-id>"
  exit 1
fi

CONTAINER_ID="$1"

echo "🧹 Entferne Integration-Test Container und Images (ID: ${CONTAINER_ID}) ..."

# stop and remove running integration-test-job containers
docker ps --filter name="-${CONTAINER_ID}" --filter status=running -aq | xargs -r docker stop
docker ps -a --filter name="-${CONTAINER_ID}" -aq | xargs -r docker rm
docker images --filter reference="-${CONTAINER_ID}" -q | xargs -r docker rmi

echo "✅ Integration-Test Container und Images wurden erfolgreich entfernt! (ID: ${CONTAINER_ID}) ..."
