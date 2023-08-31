#!/bin/bash

git config --global user.email "info@schildnrw.de"
git config --global user.name "Gitlab Pipeline"

git add buildconfig.json
git add package.json
git add svws-transpile/src/test/ts/package.json
git add svws-webclient/src/api-test/ts/package.json
git add svws-webclient/src/browser-test/ts/package.json
git add svws-webclient/src/client-test/ts/package.json
git add svws-webclient/src/client/ts/package.json
git add svws-webclient/src/client/ts/version.ts
git add svws-webclient/src/components/ts/package.json
git add svws-webclient/src/core-test/ts/package.json
git add svws-webclient/src/core/ts/package.json
git add svws-webclient/src/ui/ts/package.json

git commit -m "pushback - ${NEW_VERSION}"

echo $CI_COMMIT_REF_NAME

git remote show origin
git remote set-url --push origin git@gitlab:$CI_PROJECT_PATH
git remote show origin
git push --follow-tags origin HEAD:1115-git-push-back
