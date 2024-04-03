#!/bin/bash

if [ -z "$1" ]; then
  echo "Error: Repo name is required parameter."
  exit 1
fi

repo="$1"

exec > >(tee -i logs/gitolite_init_commit_script.log)
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


GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" touch README.md
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git add README.md
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git commit -m "Initial commit"
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git push -u origin master

echo "Initial commit successfully."

cd ..

rm -rf "$repo"
