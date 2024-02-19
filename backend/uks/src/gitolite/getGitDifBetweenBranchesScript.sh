#!/bin/bash
if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3"] ; then
  echo "Error: Repo name, origin branch and destination branch parameters are required."
  exit 1
fi

repo="$1"
origin_branch="$2"
destination_branch="$3"

exec > >(tee -i gitolite_admin_get_git_diff_script.log)
exec 2>&1

if [ -d "$repo" ]; then
  echo "Repository '$repo' already exists. Skipping cloning."
else
  GIT_SSH_COMMAND="ssh -p 2222 -i gitolite" git clone git@localhost:"$repo"
fi

cd "$repo"


GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git fetch
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git pull origin "$origin_branch"

git checkout "$destination_branch"

echo "Differences"
git diff -U3 --color --word-diff-regex=. origin/"$origin_branch"..origin/"$destination_branch"

cd ..

rm -rf "$repo"