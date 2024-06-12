#!/bin/bash
if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Error: Repo name and branch name are required."
  exit 1
fi

repo="$1"
branch_name="$2"

exec > >(tee -i logs/clone_all_files_of_branch_script.log)
exec 2>&1

if [ -d "$repo" ]; then
  echo "Repository '$repo' already exists. Skipping cloning."
else
  GIT_SSH_COMMAND="ssh -p 2222 -i gitolite" git clone --single-branch git@localhost:"$repo" -b "$branch_name"
  if [ $? -ne 0 ]; then
    echo "Error: Cloning failed. Exiting."
    exit 1
  fi
fi

cd "$repo" || exit 1
