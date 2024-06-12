#!/bin/sh
if [ -z "$1" ]; then
  echo "Error: Repo name is required."
  exit 1
fi

repo="$1"

# Ensure the logs directory exists
mkdir -p logs

# Open the log file for writing
LOGFILE="logs/remove_cloned_repo.log"
exec >"$LOGFILE" 2>&1

if [ -d "$repo" ]; then
  rm -rf "$repo"
else
  echo "Repository '$repo' does not exists."
fi
