#!/bin/bash
if [ -n "$CI_COMMIT_TAG" ]; then
	apt-get install jq -y
	jq --arg version "${CI_COMMIT_TAG#v}" '.project."version" = $version' buildconfig.json > temp.json && mv temp.json buildconfig.json
fi

cat buildconfig.json
