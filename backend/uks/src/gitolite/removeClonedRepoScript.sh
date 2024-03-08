#!/bin/bash
if [ -z "$1" ]; then
  echo "Error: Repo name is required."
  exit 1
fi

repo="$1"

exec > >(tee -i logs/remove_cloned_repo.log)
exec 2>&1

if [ -d "$repo" ]; then
  rm -rf "$repo"
else
  echo "Repository '$repo' does not exists."
fi
