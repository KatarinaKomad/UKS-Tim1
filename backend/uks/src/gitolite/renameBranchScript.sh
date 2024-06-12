#!/bin/sh

if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
  echo "Error: Repo name, old branch name, and new branch name parameters are required."
  exit 1
fi

repo="$1"
old_name="$2"
new_name="$3"

# Ensure the logs directory exists
mkdir -p logs

# Open the log file for writing
LOGFILE="logs/rename_branch_script.log"
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
GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git checkout "$old_name"
GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git checkout master
GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git branch -m "$old_name" "$new_name"
GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git push origin :"$old_name" "$new_name"
GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git checkout "$new_name"
GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git push origin -u "$new_name"

echo "Branch renamed successfully."

cd ..

rm -rf "$repo"
