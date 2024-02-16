#!/bin/bash

if [ -z "$1" ]; then
  echo "Error: Message parameter is missing."
  exit 1
fi

message="$1"

exec > >(tee -i gitolite_admin_script.log)
exec 2>&1

echo "Committing to gitolite-admin with message: $message"

cd gitolite-admin

GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git add .

GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git commit -m "$message"

GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git push