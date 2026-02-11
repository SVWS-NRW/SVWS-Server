#!/usr/bin/env bash
set -euo pipefail

###
### CONFIGURATION (required environment vars)
###
SONAR_URL="${SONAR_URL:?SONAR_URL required, e.g. https://sonarqube.svws-nrw.de}"
SONAR_TOKEN="${SONAR_TOKEN:?SONAR_TOKEN required}"
SONAR_PROJECT_KEY="${SONAR_PROJECT_KEY:?SONAR_PROJECT_KEY required}"

GITLAB_HOST="https://git.svws-nrw.de"
GITLAB_API_TOKEN="${GITLAB_API_TOKEN:?GITLAB_API_TOKEN required}"
GITLAB_PROJECT_ID=6

echo "-------------------"
echo "Fetching open Gitlab MRs..."
gitlab_mr_ids=$(curl -s --header "PRIVATE-TOKEN: ${GITLAB_API_TOKEN}" \
    "${GITLAB_HOST}/api/v4/projects/${GITLAB_PROJECT_ID}/merge_requests?state=opened" \
    | grep -o '"iid"[[:space:]]*:[[:space:]]*[0-9]*' \
  	| sed 's/.*: *//')
printf "Open Gitlab MRs (IDs):\n%s\n" "$gitlab_mr_ids"

echo "-------------------"
echo "Fetching Sonarqube MR Analyses..."
sonar_mr_ids=$(curl -s -u "${SONAR_TOKEN}:" \
    "${SONAR_URL}/api/project_pull_requests/list?project=${SONAR_PROJECT_KEY}" \
    | grep -o '"key"[[:space:]]*:[[:space:]]*\"[0-9]*\"' \
    | sed 's/[^0-9]//g')
printf "Sonarqube MR Analyses (IDs):\n%s\n" "$sonar_mr_ids"

###
### CLEANUP MR ANALYSES BRANCHES
###
echo "-------------------"
echo "Checking for orphan Sonarqube MR Analyses..."

for sonar_mr_id in $sonar_mr_ids; do
	if [[ " ${gitlab_mr_ids[*]} " =~ ${sonar_mr_id} ]]; then
        echo "✔ MR '$sonar_mr_id' exists in GitLab → keeping Sonarqube MR Analyse"
    else
    	echo "❌ MR '$sonar_mr_id' no longer exists in GitLab → deleting MR Analyse from Sonarqube..."
		curl -s -u "${SONAR_TOKEN}:" \
			-X POST "${SONAR_URL}/api/project_pull_requests/delete" \
			-d "project=${SONAR_PROJECT_KEY}" \
			-d "pullRequest=${sonar_mr_id}"
    fi
done

echo "-----"
echo "Sonarqube cleanup completed."
