#!/bin/sh

if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
  echo "Error: Repo name, origin branch, and new branch name parameters are required."
  exit 1
fi

repo="$1"
origin_branch="$2"
new_branch="$3"

# Ensure the logs directory exists
mkdir -p logs

# Open the log file for writing
LOGFILE="logs/gitolite_admin_new_branch_script.log"
exec >"$LOGFILE" 2>&1

if [ -d "$repo" ]; then
  echo "Repository '$repo' already exists. Skipping cloning."
else
  GIT_SSH_COMMAND="ssh -p 22 -i gitolite -o StrictHostKeyChecking=no" git clone git@gitolite:"$repo"
  if [ $? -ne 0 ]; then
    echo "Error: Cloning failed. Exiting."
    exit 1
  fi
fi

cd "$repo" || exit 1

GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git pull
GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git remote prune origin --dry-run
GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git pull origin "$origin_branch"
GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git checkout -b "$new_branch"
GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git push --set-upstream origin "$new_branch"

echo "New branch created successfully."

cd ..

rm -rf "$repo"
