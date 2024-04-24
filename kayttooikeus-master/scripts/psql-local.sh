#!/usr/bin/env bash
set -o errexit -o nounset -o pipefail

function main {
  docker exec -it kayttooikeus-db psql --username oph kayttooikeus
}

main "$@"
