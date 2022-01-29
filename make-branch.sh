#!/bin/bash

echo "Enter new branch name: "
read newbranch

git checkout --orphan $newbranch
git rm --cached -r .

find . \! \( -type d -name .git -prune \) -exec rm -rf {} \;

echo "Delete unnecessary function files ..."

commitmsg="init"

git add .
git commit -m $commitmsg
git push origin $newbranch

git switch dev
git merge $newbranch --allow-unrelated-histories
git push origin dev

git switch $newbranch
