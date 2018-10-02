#!/bin/bash

# Optimizes images in the subdirectories of the working directory via
# optimize-images-in-repo.sh and prints a URL to open a PR against the
# repos.
#
# This script expects the repositories in the subdirectories to have
# correctly configured remotes and no changes in their working directory:
# see optimize-images-in-repo.sh
#
# Since this script requires this set-up, it is recommended to use a
# separate clone of each # repository ONLY for this script. For example:
# dev/
#   firefox-tv/
#   focus-android/
#   imageoptim/ # Run script in here
#     firefox-tv
#     focus-android/
#
# This script may not be robust to edge cases (spaces in paths, symlinks)
# and failures: you have been warned.

DIR_SCRIPT=$(pwd)/$(dirname "$0") # absolute path.
SCRIPT_OPTIM_FOR_REPO=$DIR_SCRIPT/optimize-images-in-repo.sh

if [[ ! -f $SCRIPT_OPTIM_FOR_REPO ]]; then
    echo "Unable to find companion script, $SCRIPT_OPTIM_FOR_REPO. Exiting."
    exit 1
fi

echo "Optimizing images in all subdirectories."
echo "  On success, GitHub will print a URL to create a pull request."
echo "  On failure, a URL will not be printed and you may not receive an error message."
echo "  Failure may occur if optimization did not change any images."
echo ""

for file in *; do
    if [[ -d $file ]]; then
        pushd $file > /dev/null
        echo "Optimizing $file..."
        $SCRIPT_OPTIM_FOR_REPO
        popd > /dev/null
    fi
done
