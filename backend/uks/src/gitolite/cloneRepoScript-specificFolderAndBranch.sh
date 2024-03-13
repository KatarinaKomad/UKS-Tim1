#!/bin/bash
if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
  echo "Error: Repo name, branch name, and folder path are required."
  exit 1
fi

repo="$1"
branch_name="$2"
folder_path="$3"

exec > >(tee -i logs/clone_folder_of_branch_script.log)
exec 2>&1

if [ -d "$repo" ]; then
  echo "Repository '$repo' already exists. Removing clone."
  rm -rf "$repo"
fi




# Clone the repository without checking out files
GIT_SSH_COMMAND="ssh -p 2222 -i gitolite" git clone --depth 1 --no-checkout git@localhost:"$repo"

if [ $? -ne 0 ]; then
  echo "Error: Cloning failed. Exiting."
  exit 1
fi



# Navigate into the cloned repository directory
cd "$repo" || exit 1

echo "Before sparse-checkout init"
# Enable sparse-checkout
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git sparse-checkout init

echo "Before git sparse-checkout set '$folder_path'"
# Specify the folder path you want to clone
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git sparse-checkout set "$folder_path"

echo "Before git checkout '$branch_name'"
# Fetch the content of the specified folder
GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git checkout "$branch_name"
