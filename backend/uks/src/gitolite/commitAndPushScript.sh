#!/bin/sh

if [ -z "$1" ]; then
  echo "Error: Message parameter is missing."
  exit 1
fi

message="$1"

# Ensure the logs directory exists
mkdir -p logs

# Open the log file for writing
LOGFILE="logs/gitolite_admin_script.log"
exec >"$LOGFILE" 2>&1

echo "Committing to gitolite-admin with message: $message"

cd gitolite-admin || exit 1

GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git add .

GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git commit -m "$message"

GIT_SSH_COMMAND="ssh -p 22 -i ../gitolite -o StrictHostKeyChecking=no" git push