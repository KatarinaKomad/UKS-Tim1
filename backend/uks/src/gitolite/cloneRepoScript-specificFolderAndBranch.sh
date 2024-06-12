#!/bin/sh
if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Error: Repo name and branch name are required."
  exit 1
fi

repo="$1"
branch_name="$2"

# Ensure the logs directory exists
mkdir -p logs

# Open the log file for writing
LOGFILE="logs/clone_all_files_of_branch_script.log"
exec >"$LOGFILE" 2>&1

if [ -d "$repo" ]; then
  echo "Repository '$repo' already exists. Skipping cloning."
else
  GIT_SSH_COMMAND="ssh -p 22 -i gitolite -o StrictHostKeyChecking=no" git clone --single-branch git@gitolite:"$repo" -b "$branch_name"
  if [ $? -ne 0 ]; then
    echo "Error: Cloning failed. Exiting."
    exit 1
  fi
fi

cd "$repo" || exit 1
