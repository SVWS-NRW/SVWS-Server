#!/bin/bash

NEW_VERSION=v0.0.4

git status

git switch 1115-git-push-back
git pull

git config --global user.name "${GITLAB_USER_NAME}"
git config --global user.email "${GITLAB_USER_EMAIL}"

git checkout -b ${NEW_VERSION}

git add /builds/svws/SVWS-Server/buildconfig.json
git add /builds/svws/SVWS-Server/package.json
git add /builds/svws/SVWS-Server/svws-transpile/src/test/ts/package.json
git add /builds/svws/SVWS-Server/svws-webclient/src/api-test/ts/package.json
git add /builds/svws/SVWS-Server/svws-webclient/src/browser-test/ts/package.json
git add /builds/svws/SVWS-Server/svws-webclient/src/client-test/ts/package.json
git add /builds/svws/SVWS-Server/svws-webclient/src/client/ts/package.json
git add /builds/svws/SVWS-Server/svws-webclient/src/client/ts/version.ts
git add /builds/svws/SVWS-Server/svws-webclient/src/components/ts/package.json
git add /builds/svws/SVWS-Server/svws-webclient/src/core-test/ts/package.json
git add /builds/svws/SVWS-Server/svws-webclient/src/core/ts/package.json
git add /builds/svws/SVWS-Server/svws-webclient/src/ui/ts/package.json
git status
git commit -m "pushback - ${NEW_VERSION}"
git status
git push http://root:$ACCESS_TOKEN@$CI_SERVER_HOST/$CI_PROJECT_PATH.git --set-upstream origin ${NEW_VERSION}
#git push http://root:$ACCESS_TOKEN@$CI_SERVER_HOST/$CI_PROJECT_PATH.git

