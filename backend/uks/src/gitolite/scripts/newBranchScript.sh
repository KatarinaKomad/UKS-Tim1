#!/bin/bash

if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
  echo "Error: Repo name, origin branch, and new branch name parameters are required."
  exit 1
fi

repo="$1"
origin_branch="$2"
new_branch="$3"

exec > >(tee -i ../logs/gitolite_admin_new_branch_script.log)
exec 2>&1

if [ -d "$repo" ]; then
  echo "Repository '$repo' already exists. Skipping cloning."
else
  GIT_SSH_COMMAND="ssh -p 2222 -i gitolite" git clone git@localhost:"$repo"
fi

cd "$repo"

# Fetch changes from the remote
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git fetch

# Pull changes from the origin branch
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git pull origin "$origin_branch"

# Checkout new branch
git checkout -b "$new_branch"

# Push the changes to the destination branch
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite"  git push --set-upstream origin "$new_branch"

echo "New branch created successfully."

cd ..

rm -rf "$repo"
