#!/usr/bin/env bash
set -o errexit -o nounset -o pipefail -o xtrace
readonly repo="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

function main {
  cd "$repo"
  local -r session="kayttooikeus"
  tmux kill-session -t "$session" || true
  tmux start-server
  tmux new-session -d -s "$session"

  tmux select-pane -t 0
  tmux send-keys "$repo/scripts/run-database.sh" C-m

  tmux splitw -v
  tmux select-pane -t 1
  tmux send-keys "$repo/scripts/run-kayttooikeus.sh" C-m

  tmux attach-session -t "$session"
}

main "$@"
