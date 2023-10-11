var
  SVWSExisting: Boolean;
  SVWSExistingDataDirectory: Boolean;
  SVWSExistingDirectory: String;
  SVWSExistingVersion: String;
  SVWSExistingClientVersion: String;
  SVWSExistingJavaVersion: String;
  SVWSExistingMariaDBVersion: String;
  SVWSExistingCurlVersion: String;
  SVWSExistingWSWVersion: String;
  SVWSNewVersion: String;
  SVWSNewClientVersion: String;
  SVWSNewJavaVersion: String;
  SVWSNewMariaDBVersion: String;
  SVWSNewCurlVersion: String;
  SVWSNewWSWVersion: String;
  SVWSDataDir: String;
  InstallSVWSServer: Boolean;
  InstallSVWSClient: Boolean;
  InstallJava: Boolean;
  InstallMariaDB: Boolean;
  InstallCurl: Boolean;
  InstallWSW: Boolean;
  ProgressPage: TWizardPage;
  ProgressMemo: TNewMemo;
  IsUninstallSVWSClient: Boolean;
  IsUninstallSVWSServer: Boolean;
  IsUninstallJava: Boolean;
  IsUninstallMariaDB: Boolean;


{ Liefert das Verzeichnis in welchem die Daten des SVWS-Servers abgelegt werden. 
  @return das Verzeichnis mit den SVWS-Server-Daten}
function GetDataDir(Param: String): String;
  begin
    result := SVWSDataDir;
  end;


{ Gibt zur�ck, ob eine SVWS-Client-Installation vorgenommen werden soll
  @return true, falls der SVWS-Client installiert werden soll, sonst false }
function IsInstallSVWSClient(): Boolean;
  begin
    result := InstallSVWSClient;
  end;


{ Gibt zur�ck, ob eine MariaDB-Installation vorgenommen werden soll
  @return true, falls MariaDB installiert werden soll, sonst false }
function IsInstallMariaDB(): Boolean;
  begin
    result := InstallMariaDB;
  end;


{ Gibt zur�ck, ob eine Java-Installation vorgenommen werden soll
  @return true, falls Java installiert werden soll, sonst false }
function IsInstallJava(): Boolean;
  begin
    result := InstallJava;
  end;


{ Gibt zur�ck, ob eine Windows-Service-Wrapper-Installation vorgenommen werden soll
  @return true, falls der Windows-Service-Wrapper installiert werden soll, sonst false }
function IsInstallWSW(): Boolean;
  begin
    result := InstallWSW;
  end;


{ Gibt zur�ck, ob eine Curl-Installation vorgenommen werden soll
  @return true, falls Curl installiert werden soll, sonst false }
function IsInstallCurl(): Boolean;
  begin
    result := InstallCurl;
  end;



{ Initialisiert das Setup und pr�ft, ob bereits eine Installation vorhanden ist.
  @return true, falls die Installation ausgef�hrt werden soll und false, falls nicht }
function InitializeSetup(): Boolean;
  var msgBoxResult: Integer;
  begin
    // Setze die neuen, in diesem Installer verwendeten Versionen
    Log('Initialisiere den Installer f�r den SVWS-Server @version@ (Java-Version @jdk_version@, MariaDB-Version @mariadb_version@)');
    SVWSNewVersion := '@version@';
    SVWSNewClientVersion := '@svws_client_version@';
    SVWSNewJavaVersion := '@jdk_version@';
    SVWSNewMariaDBVersion := '@mariadb_version@';
    SVWSNewCurlVersion := '@curl_version@';
    SVWSNewWSWVersion := '@windowsservicewrapper_version@';
    InstallSVWSServer := True;

    // Registry: Pr�fe, ob der SVWS-Server bereits zuvor installiert wurde
    SVWSExisting := RegKeyExists(HKLM, 'SOFTWARE\SVWSServer');
    if not SVWSExisting then
      Log('  - Eine Neuinstallation wird gestartet.');

    // Registry: Lese das Programm-Verzeichnis aus
    SVWSExistingDirectory := '';
    RegQueryStringValue(HKLM, 'SOFTWARE\SVWSServer', 'ProgrammVerzeichnis', SVWSExistingDirectory);
    if SVWSExisting then
      Log('  - Eine bestehende Installation wurde in dem Verzeichnis "' + SVWSExistingDirectory + '" gefunden.');

    // Registry: Lese das Datenverzeichnis aus
    SVWSExistingDataDirectory := RegQueryStringValue(HKLM, 'SOFTWARE\SVWSServer', 'DatenVerzeichnis', SVWSDataDir);
    if not SVWSExistingDataDirectory then
      SVWSDataDir := ExpandConstant('{commonappdata}\SVWS-Server');
    if SVWSExisting then
      begin
        Log('  - Datenverzeichnis: ' + SVWSDataDir);
      end;

    // Registry: Lese die bereits installierte Version des SVWS-Servers aus
    SVWSExistingVersion := '';
    RegQueryStringValue(HKLM, 'SOFTWARE\SVWSServer', 'Version', SVWSExistingVersion);
    if SVWSExisting then
      Log('    * SVWS-Server-Version: "' + SVWSExistingVersion + '"');

    // Pr�fe, ob die zu installierende SVWS-Server-Version neuer ist als eine bereits installierte
    if CompareStr(SVWSExistingVersion, '') <> 0 then
      begin
        if CompareVersions(SVWSNewVersion, SVWSExistingVersion) < 0 then
          begin
            Log('FEHLER: Die SVWS-Server-Version ' + SVWSNewVersion + ' ist �lter als die bereits installierte Version ' + SVWSExistingVersion + '. Die Installation wird abgebrochen!');
            msgBoxResult := MsgBox('Die Version ' + SVWSNewVersion + ' ist �lter als die bereits installierte Version ' + SVWSExistingVersion + '. Die Installation wird abgebrochen!', mbCriticalError, MB_OK);
            result := false;
            Exit;
          end;
        if CompareVersions(SVWSNewVersion, SVWSExistingVersion) = 0 then
          begin
            Log('WARNUNG: Die SVWS-Server-Version ' + SVWSNewVersion + ' ist bereits installiert.');
            msgBoxResult := MsgBox('Die Version ' + SVWSNewVersion + ' ist bereits installiert. Soll die Installation dennoch fortgesetzt und die bestehenden Programmdateien �berschrieben werden?', mbConfirmation, MB_YESNO);
            if msgBoxResult = IDNO then
              begin
                Log('ABBRUCH: Die Installation wurde vom Benutzer abgebrochen.');
                result := false;
                Exit;
              end;
            Log('HINWEIS: Die Installation wird auf Wunsch des Benutzers fortgesetzt.');
          end;
      end;


    // Registry: Lese die bereits installierte Version des SVWS-Clients aus
    SVWSExistingClientVersion := '';
    RegQueryStringValue(HKLM, 'SOFTWARE\SVWSServer', 'SVWSClientVersion', SVWSExistingClientVersion);
    if SVWSExisting then
      Log('    * SVWS-Client-Version: "' + SVWSExistingClientVersion + '"');

    // Pr�fe, ob die zu installierende SVWS-Client-Version neuer ist als eine bereits installierte
    InstallSVWSClient := true;
    if CompareStr(SVWSExistingClientVersion, '') <> 0 then
      begin
        if CompareVersions(SVWSNewClientVersion, SVWSExistingClientVersion) < 0 then
          begin
            Log('      ist �lter als die bereits installierte SVWS-Client-Version und wird daher nicht installiert. Die Installation sollte gepr�ft werden!');
            InstallSVWSClient := false;
          end;
        if CompareVersions(SVWSNewClientVersion, SVWSExistingClientVersion) = 0 then
          begin
            Log('      ist bereits installiert und wird daher nicht nochmal installiert.');
            InstallSVWSClient := false;
          end;
      end;

    // Registry: Lese die bereits installierte Version der f�r den SVWS-Server verwendeten Java Installation aus
    SVWSExistingJavaVersion := '';
    RegQueryStringValue(HKLM, 'SOFTWARE\SVWSServer', 'JavaVersion', SVWSExistingJavaVersion);
    if SVWSExisting then
      Log('    * Java-Version: "' + SVWSExistingJavaVersion + '"');

    // Pr�fe, ob Java installiert werden muss
    InstallJava := true;
    if CompareStr(SVWSExistingJavaVersion, '') <> 0 then
      begin
        if CompareVersions(SVWSNewJavaVersion, SVWSExistingJavaVersion) < 0 then
          begin
            Log('      ist �lter als die bereits installierte Java-Version und wird daher nicht installiert. Die Installation sollte gepr�ft werden!');
            InstallJava := false;
          end;
        if CompareVersions(SVWSNewJavaVersion, SVWSExistingJavaVersion) = 0 then
          begin
            Log('      ist bereits installiert und wird daher nicht nochmal installiert.');
            InstallJava := false;
          end;
      end;

    // Registry: Lese die bereits installierte Version der f�r den SVWS-Server verwendeten SVWS-MariaDB-Installation aus
    SVWSExistingMariaDBVersion := '';
    RegQueryStringValue(HKLM, 'SOFTWARE\SVWSServer', 'MariaDBVersion', SVWSExistingMariaDBVersion);
    if SVWSExisting then
      Log('    * MariaDB-Version: "' + SVWSExistingMariaDBVersion + '"');

    // Pr�fe, ob MariaDB installiert werden muss
    InstallMariaDB := true;
    if CompareStr(SVWSExistingMariaDBVersion, '') <> 0 then
      begin
        if CompareVersions(SVWSNewMariaDBVersion, SVWSExistingMariaDBVersion) < 0 then
          begin
            Log('      ist �lter als die bereits installierte MariaDB-Version und wird daher nicht installiert. Die Installation sollte gepr�ft werden!');
            InstallMariaDB := false;
          end;
        if CompareVersions(SVWSNewMariaDBVersion, SVWSExistingMariaDBVersion) = 0 then
          begin
            Log('      ist bereits installiert und wird daher nicht nochmal installiert.');
            InstallMariaDB := false;
          end;
      end;

    // Registry: Lese die bereits installierte Version des verwendeten Windows-Service-Wrapper aus
    SVWSExistingWSWVersion := '';
    RegQueryStringValue(HKLM, 'SOFTWARE\SVWSServer', 'WindowsServiceWrapperVersion', SVWSExistingWSWVersion);
    if SVWSExisting then
      Log('    * Windows-Service-Wrapper-Version: "' + SVWSExistingWSWVersion + '"');

    // Pr�fe, ob der Windows-Service-Wrapper installiert werden muss
    InstallWSW := true;
    if CompareStr(SVWSExistingWSWVersion, '') <> 0 then
      begin
        if CompareVersions(SVWSNewWSWVersion, SVWSExistingWSWVersion) < 0 then
          begin
            Log('      ist �lter als die bereits installierte Windows-Service-Wrapper-Version und wird daher nicht installiert. Die Installation sollte gepr�ft werden!');
            InstallWSW := false;
          end;
        if CompareVersions(SVWSNewWSWVersion, SVWSExistingWSWVersion) = 0 then
          begin
            Log('      ist bereits installiert und wird daher nicht nochmal installiert.');
            InstallWSW := false;
          end;
      end;

    // Registry: Lese die bereits installierte Version der verwendeten Curl-Installation aus
    SVWSExistingCurlVersion := '';
    RegQueryStringValue(HKLM, 'SOFTWARE\SVWSServer', 'CurlVersion', SVWSExistingCurlVersion);
    if SVWSExisting then
      Log('    * Curl-Version: "' + SVWSExistingCurlVersion + '"');

    // Pr�fe, ob Curl installiert werden muss
    InstallCurl := true;
    if CompareStr(SVWSExistingCurlVersion, '') <> 0 then
      begin
        if CompareVersions(SVWSNewCurlVersion, SVWSExistingCurlVersion) < 0 then
          begin
            Log('      ist �lter als die bereits installierte Curl-Version und wird daher nicht installiert. Die Installation sollte gepr�ft werden!');
            InstallCurl := false;
          end;
        if CompareVersions(SVWSNewCurlVersion, SVWSExistingCurlVersion) = 0 then
          begin
            Log('      ist bereits installiert und wird daher nicht nochmal installiert.');
            InstallCurl := false;
          end;
      end;

    result := true;
  end;


procedure InitProgressPage();
  var 
    TmpControl: TControl;
  begin
    // Ermittle die vordefinierte Progress-Page des Installers anhand der ID
    ProgressPage := PageFromID(wpInstalling);

    TmpControl := GetWizardForm.InstallingPage.Controls[GetWizardForm.InstallingPage.ControlCount-1];

    // Erstelle ein neues Feld auf der Progress-Page, um Kommentare einblenden zu k�nnen
    ProgressMemo := TNewMemo.Create(ProgressPage);
    with ProgressMemo do
      begin
        Left := ScaleX(0);
        Top := TmpControl.Top + TmpControl.Height + ScaleY(5);
        Width := TmpControl.Width;
        Height := ProgressPage.SurfaceHeight - Top - ScaleY(5);
        Parent := ProgressPage.Surface;
        Lines.Append('Kopiere Dateien...');
      end;

    WizardForm.ProgressGauge.Max := WizardForm.ProgressGauge.Max * 2;
  end;


{ Prepare the Uninstaller }
function InitializeUninstall(): Boolean;
  begin
    Log('Initialisiere den Uninstaller f�r den SVWS-Server @version@(Java-Version @jdk_version@, MariaDB-Version @mariadb_version@)');
    SVWSNewVersion := '@version@';
    SVWSNewClientVersion := '@svws_client_version@';
    SVWSNewJavaVersion := '@jdk_version@';
    SVWSNewMariaDBVersion := '@mariadb_version@';
    SVWSNewCurlVersion := '@curl_version@';
    SVWSNewWSWVersion := '@windowsservicewrapper_version@';

    Log('Pr�fe, welche Komponenten deinstalliert werden k�nnen...');

    // Pr�fe, ob der SVWS-Server �berhaupt installiert ist
    SVWSExisting := RegKeyExists(HKLM, 'SOFTWARE\SVWSServer');
    if not SVWSExisting then
      begin
        Log('  - Es wurde keine Installation des SVWS-Servers gefunden.');
        Result := False;
        Exit;
      end;

    // Pr�fe, ob die SVWS-Server-Version deinstalliert werden kann (ist der Uninstaller passend?)
    IsUninstallSVWSServer := False;
    if RegQueryStringValue(HKLM, 'SOFTWARE\SVWSServer', 'Version', SVWSExistingVersion) then
      begin
        Log('  - SVWS-Server-Version: "' + SVWSExistingVersion + '"');
        IsUninstallSVWSServer := (SVWSExistingVersion = SVWSNewVersion);
      end;
    if IsUninstallSVWSServer then
      Log('  - Deinstalliere SVWS-Server')
    else
      Log('  - SVWS-Server wird nicht deinstalliert');

    // Pr�fe, ob die Java-Version deinstalliert werden kann (ist es installiert und der Uninstaller passend?)
    IsUninstallJava := False;
    if RegQueryStringValue(HKLM, 'SOFTWARE\SVWSServer', 'JavaVersion', SVWSExistingJavaVersion) then
      begin
        Log('  - Java-Version: "' + SVWSExistingJavaVersion + '"');
        IsUninstallJava := (SVWSExistingJavaVersion = SVWSNewJavaVersion);
      end;
    if IsUninstallJava then
      Log('  - Deinstalliere Java')
    else
      Log('  - Java wird nicht deinstalliert');

    // Pr�fe, ob die MariaDB-Version deinstalliert werden kann (ist es installiert und der Uninstaller passend?)
    IsUninstallMariaDB := False;
    if RegQueryStringValue(HKLM, 'SOFTWARE\SVWSServer', 'MariaDBVersion', SVWSExistingMariaDBVersion) then
      begin
        Log('  - MariaDB-Version: "' + SVWSExistingMariaDBVersion + '"');
        IsUninstallMariaDB := (SVWSExistingMariaDBVersion = SVWSNewMariaDBVersion);
      end;
    if IsUninstallMariaDB then
      Log('  - Deinstalliere MariaDB')
    else
      Log('  - MariaDB wird nicht deinstalliert');

    // Pr�fe, ob die SVWS-Client-Version deinstalliert werden kann (ist er installiert und der Uninstaller passend?)
    IsUninstallSVWSClient := False;
    if RegQueryStringValue(HKLM, 'SOFTWARE\SVWSServer', 'SVWSClientVersion', SVWSExistingClientVersion) then
      begin
        Log('  - SVWS-Client-Version: "' + SVWSExistingClientVersion + '"');
        IsUninstallSVWSClient := (SVWSExistingClientVersion = SVWSNewClientVersion);
      end;
    if IsUninstallSVWSClient then
      Log('  - Deinstalliere SVWS-Client')
    else
      Log('  - SVWS-Client wird nicht deinstalliert');

    Result := True;
  end;