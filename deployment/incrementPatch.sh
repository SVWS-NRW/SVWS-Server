#!/bin/bash

#!/bin/bash
if [ -n "$CI_COMMIT_TAG" ]; then

	VERSION=$CI_COMMIT_TAG

    # Extrahiere die Patch-Version (letzte Zahl)
    PATCH=$(echo "$VERSION" | grep -o '[0-9]*$')

    # Erhöhe die Patch-Version um 1
    NEW_PATCH=$((PATCH + 1))

    # Erstelle die neue Version
    export NEW_VERSION="${VERSION%.*}.$NEW_PATCH"-SNAPSHOT

    echo "Alte Version: $VERSION"
    echo "Neue Version: $NEW_VERSION"
	apt-get install jq -y
	jq --arg version "${NEW_VERSION#v}" '.project."version" = $version' buildconfig.json > temp.json && mv temp.json buildconfig.json
fi

cat buildconfig.json



