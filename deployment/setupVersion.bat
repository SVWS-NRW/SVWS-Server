@echo off
setlocal enabledelayedexpansion

if not "%CI_COMMIT_TAG%"=="" (
    echo Modifying buildconfig.json...

    set "version=%CI_COMMIT_TAG:v=%"

    set "tempFile=temp.json"
    set "outputFile=buildconfig.json"

    REM Lösche die temporäre Datei, falls sie bereits existiert
    if exist "!tempFile!" del /q "!tempFile!"

    REM Lese die buildconfig.json-Datei Zeile für Zeile
    for /f "tokens=*" %%a in (buildconfig.json) do (
        set "line=%%a"

        REM Überprüfe, ob die Zeile das gewünschte JSON-Element enthält
        echo !line! | findstr /C:".project.version" >nul

        if !errorlevel! equ 0 (
            REM Ersetze den Wert des JSON-Elements durch die neue Version
            echo !line:": %CI_COMMIT_TAG:v=%"!" >> "!tempFile!"
        ) else (
            REM Füge die Zeile unverändert zur temporären Datei hinzu
            echo !line! >> "!tempFile!"
        )
    )

    REM Ersetze die ursprüngliche Datei durch die temporäre Datei
    move /y "!tempFile!" "!outputFile!"
)

REM Zeige den Inhalt der aktualisierten Datei auf der Konsole an
type buildconfig.json
