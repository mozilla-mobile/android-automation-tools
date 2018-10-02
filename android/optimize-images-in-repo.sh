#!/bin/bash

# Pulls the latest changes from master, losslessly optimizes all images recursively in
# the working directory, and pushes the changes on a new branch in a git fork.
#
# This script expects the working directory to be a git repository with the following:
# - A remote called "upstream" pointed to the latest master
# - A remote called "origin" pointed to the fork to create the pull request from
#
# On macOS, this script will launch the ImageOptim GUI application and close it
# upon completion.

GIT=$(which git)
IMAGE_OPTIM=/Applications/ImageOptim.app/Contents/MacOS/ImageOptim

# config
GIT_REMOTE_FORK=origin
GIT_REMOTE_UPSTREAM=upstream

SCRIPT_NAME=$(basename "$0")
UUID=$(uuidgen)

# macOS only. We should support linux via `trimage` later.
if [ ! -f $IMAGE_OPTIM ]; then
    echo "Error: $IMAGE_OPTIM not found. Install from https://imageoptim.com/mac. Exiting..." 1>&2
    exit 1
fi

# via https://unix.stackexchange.com/a/155077
if output=$(git status --porcelain) && [ -n "$output" ]; then
    echo "Error: uncommited git changes. Skipping $(basename $(pwd))..." 1>&2
    exit 1
fi

# I don't know if these are porcelain commands but we're not parsing output so it should be fine.
# Quiet the commands, except push, so the PR URLs stand out. ImageOptim is chatty on stderr too.
$GIT fetch --quiet $GIT_REMOTE_UPSTREAM &&
        $GIT checkout --quiet $GIT_REMOTE_UPSTREAM/master && \
        $GIT checkout --quiet -b ni-imageoptim-$UUID && \
        $IMAGE_OPTIM . > /dev/null 2>&1 && \
        $GIT commit --quiet --all --message="No issue: losslessly optimize image assets.

This process was completed automatically by the android-automation-tools script $SCRIPT_NAME." && \
        # push prints to stderr so redirect to pipe; grep will filter lines except
        # "Create a pull request for this URL:" and the actual URL.
        $GIT push --no-verify $GIT_REMOTE_FORK 2>&1 | grep pull
