# Use the official Gitolite Docker image based on Alpine Linux
FROM jgiannuzzi/gitolite:latest

# Copy SSH keys into the Docker image
COPY backend/uks/src/gitolite/gitolite.pub /home/git/.ssh/id_rsa.pub
COPY backend/uks/src/gitolite/gitolite /home/git/.ssh/id_rsa

# Set environment variables for Gitolite setup
ENV SSH_KEY="$(cat /home/git/.ssh/id_rsa.pub)" \
    SSH_KEY_NAME="gitolite"

# Run Gitolite setup
RUN su - git -c "gitolite setup -pk /home/git/.ssh/id_rsa.pub"

RUN chmod  600 /home/git/.ssh/id_rsa