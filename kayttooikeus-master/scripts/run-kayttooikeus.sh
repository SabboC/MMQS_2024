#!/usr/bin/env bash
set -o errexit -o nounset -o pipefail
readonly repo="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd .. && pwd )"

function main {
  cd "$repo"

  wait_for_postgres
  DB_HOST_AND_PORT="$( docker compose port database 5432 )"
  use_correct_jvm_version
  cd "$repo/kayttooikeus-service"
  mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED -Dserver.port=8101 -Dspring.datasource.url=\"jdbc:postgresql://${DB_HOST_AND_PORT}/kayttooikeus?ApplicationName=kayttooikeus-service\""
}

function wait_for_postgres {
  while ! docker compose port database 5432 >&2; do
    echo "Waiting for database to start..."
    sleep 1
  done
}

function use_correct_jvm_version {
  JAVA_HOME="$( /usr/libexec/java_home -v "21" )"
  export JAVA_HOME
}

main "$@"
