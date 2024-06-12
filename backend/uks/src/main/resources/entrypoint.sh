#!/bin/sh
set -e

# Wait for the gitolite service to be up
until nc -z gitolite 22; do
  echo "Waiting for gitolite service to be available..."
  sleep 2
done

# Change directory to /app/gitolite and run the git clone command
cd /app/gitolite
GIT_SSH_COMMAND="ssh -p 22 -i /app/gitolite/gitolite -o StrictHostKeyChecking=no" git clone git@gitolite:gitolite-admin
cd /app

git config --global user.email "bane@bane-portegez930"
git config --global user.name "Admin Admin"

# Execute the main command (passed as arguments to the script)
exec "$@"
