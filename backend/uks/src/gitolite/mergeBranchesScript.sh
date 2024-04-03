#!/bin/bash

if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
  echo "Error: Repo name, origin branch, and destination branch parameters are required."
  exit 1
fi

repo="$1"
origin_branch="$2"
destination_branch="$3"

exec > >(tee -i logs/gitolite_admin_merge_script.log)
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

# Fetch changes from the remote
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git fetch

# Pull changes from the origin branch
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git pull origin "$origin_branch"

# Checkout the destination branch
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git checkout "$destination_branch"

# Merge changes from the origin branch into the destination branch
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git merge origin/"$origin_branch"

# Push the changes to the destination branch
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git push origin "$destination_branch"

echo "Merge completed successfully."

cd ..

rm -rf "$repo"
