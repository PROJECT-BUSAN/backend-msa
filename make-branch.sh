#!/bin/bash

echo "Enter new branch name: "
read newbranch

git checkout --orphan $newbranch
git rm --cached -r .

find . \! \( -type d -name .git -prune \) \! -name make-branch.sh -exec rm -rf {} \;

echo " ========================================="
echo "| Deleting unnecessary function files ... |"
echo " ========================================="
commitmsg="init"

git add .
git commit -m $commitmsg
git push origin $newbranch

git switch dev
git pull origin dev
git merge $newbranch --allow-unrelated-histories
git push origin dev

git switch $newbranch
echo " =========================================="
echo "| Start developing on your new branch now! |"
echo " =========================================="