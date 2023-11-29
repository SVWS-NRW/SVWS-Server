if ($env:CI_COMMIT_TAG -ne $null) {
    # Update version in buildconfig.json
    $version = $env:CI_COMMIT_TAG -replace '^v'
    $jsonContent = Get-Content -Raw -Path "buildconfig.json" | ConvertFrom-Json
    $jsonContent.project.version = $version
    $jsonContent | ConvertTo-Json | Set-Content -Path "buildconfig.json"
}

# Display buildconfig.json
Get-Content -Path "buildconfig.json"


