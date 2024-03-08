#!/bin/bash
if [ -z "$1" ]; then
  echo "Error: Repo name is required."
  exit 1
fi

repo="$1"

exec > >(tee -i logs/clone_repo_script.log)
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
