#!/bin/sh
if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
  echo "Error: Repo name, origin branch and destination branch parameters are required."
  exit 1
fi

repo="$1"
branch="$2"
commit="$3"

# Ensure the logs directory exists
mkdir -p logs

# Open the log file for writing
LOGFILE="logs/gitolite_admin_get_git_diff_script.log"
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

GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git checkout "$branch"

echo "Stats"
GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git show --stat "$commit"

echo "Differences"
GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git show -U5 "$commit"

cd ..

rm -rf "$repo"