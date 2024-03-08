#!/bin/bash
if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Error: Repo name and branch parameters are required."
  exit 1
fi

repo="$1"
branch="$2"

exec > >(tee -i logs/gitolite_admin_delete_branch_script.log)
exec 2>&1

if [ -d "$repo" ]; then
  echo "Repository '$repo' already exists. Skipping cloning."
else
  GIT_SSH_COMMAND="ssh -p 2222 -i gitolite" git clone -b "$branch" git@localhost:"$repo"

  if [ $? -ne 0 ]; then
    echo "Error: Cloning failed. Exiting."
    exit 1
  fi

fi

cd "$repo" || exit 1

GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git checkout master
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git branch -d "$branch"
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git push origin --delete "$branch"

cd ..

rm -rf "$repo"