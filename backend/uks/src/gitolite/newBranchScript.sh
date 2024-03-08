#!/bin/bash

if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
  echo "Error: Repo name, origin branch, and new branch name parameters are required."
  exit 1
fi

repo="$1"
origin_branch="$2"
new_branch="$3"

exec > >(tee -i logs/gitolite_admin_new_branch_script.log)
exec 2>&1

if [ -d "$repo" ]; then
  echo "Repository '$repo' already exists. Skipping cloning."
else
  GIT_SSH_COMMAND="ssh -p 2222 -i gitolite" git clone git@localhost:"$repo"
  if [ $? -ne 0 ]; then
    echo "Error: Cloning failed. Exiting."
    exit 1
  fi
fi

cd "$repo" || exit 1

GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git pull
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git remote prune origin --dry-run
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git pull origin "$origin_branch"
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git checkout -b "$new_branch"
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git push --set-upstream origin "$new_branch"

echo "New branch created successfully."

cd ..

rm -rf "$repo"
