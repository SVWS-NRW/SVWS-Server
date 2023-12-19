[Setup]
AppName="SVWS Server"
AppVerName="SVWS Server @version@@snapshot@"
AppPublisher=Ministerium für Schule und Bildung NRW
AppPublisherURL=https://www.svws.nrw.de
AppSupportURL=https://www.svws.nrw.de/forum-0
AppUpdatesURL=https://www.svws.nrw.de
VersionInfoVersion=@version@
VersionInfoCompany=Ministerium für Schule und Bildung NRW
VersionInfoCopyright=Ministerium für Schule und Bildung NRW
VersionInfoProductName="SVWS Server @version@@snapshot@"
VersionInfoProductVersion=@version@
WizardImageFile=img/NRWLogo.bmp
WizardSmallImageFile=img/NRWLogoSmall.bmp
WizardImageStretch=no
WizardSizePercent=120

OutputDir=../output
OutputBaseFilename=win64-installer-@version@@snapshot@
MinVersion=10.0
ArchitecturesAllowed=x64
ArchitecturesInstallIn64BitMode=x64
PrivilegesRequired=admin
RestartIfNeededByRun=yes

@signtool@
@signuninstaller@

AllowRootDirectory=no
UsePreviousAppDir=yes
DefaultDirName={commonpf}\SVWS-Server
//UsePreviousGroup=yes
DisableProgramGroupPage=yes
//DefaultGroupName=SVWS-Server

ShowLanguageDialog=no
SetupLogging=yes
UninstallLogMode=append

[Languages]
Name: de; MessagesFile: compiler:Languages\German.isl; LicenseFile: licence_de.txt
//; InfoBeforeFile: info_before.txt;InfoAfterFile: info_after.txt

[LangOptions]

[Types]

[Components]

[Dirs]
Name: "{app}\db"; Check: IsInstallMariaDB
Name: "{app}\java"; Check: IsInstallJava
Name: "{app}\curl"; Check: IsInstallCurl
Name: "{app}\svws-server"
Name: "{code:GetDataDir}"; Permissions: admins-full system-full; Flags: uninsneveruninstall; AfterInstall: DisableACLInheritance('{code:GetDataDir}')
Name: "{code:GetDataDir}\client"
Name: "{code:GetDataDir}\temp"
Name: "{code:GetDataDir}\res"; Flags: uninsneveruninstall

[Files]
// Kopiere die Datei licence_de.txt zu Beginn, um die Ereignisse vor und nach dem Installieren der Dateien korrekt zu Erzeugen
Source: "licence_de.txt"; DestDir: "{app}"; BeforeInstall: StartInstall
Source: "files\mariadb-@mariadb_version@-winx64\*"; DestDir: "{app}\db\mariadb"; Flags: recursesubdirs; Check: IsInstallMariaDB
Source: "mariaDB_Create_Data_Directory.cmd"; DestDir: "{app}\db"; Check: IsInstallMariaDB
Source: "files\jdk-@jdk_version@\*"; DestDir: "{app}/java"; Flags: recursesubdirs; Check: IsInstallJava
Source: "files\curl-@curl_version@-win64-mingw\*"; DestDir: "{app}\curl"; Flags: recursesubdirs; Check: IsInstallCurl
Source: "files\servicewrapper-v@windowsservicewrapper_version@-windows.exe"; DestDir: "{app}"; DestName: "svws_server_service.exe"; Check: IsInstallWSW
Source: "files\client\*"; DestDir: "{code:GetDataDir}\client"; Flags: recursesubdirs
Source: "files\adminclient\*"; DestDir: "{code:GetDataDir}\adminclient"; Flags: recursesubdirs
Source: "files\jar\*"; DestDir: "{app}/svws-server"; Flags: recursesubdirs
Source: "check_port.cmd"; DestDir: "{app}"
Source: "files/run_server.cmd"; DestDir: "{app}"
Source: "files/config_writer.cmd"; DestDir: "{app}"
Source: "firewall_add.cmd"; DestDir: "{app}"
Source: "firewall_remove.cmd"; DestDir: "{app}"
Source: "firewall_mariadb_add.cmd"; DestDir: "{app}"
Source: "firewall_mariadb_remove.cmd"; DestDir: "{app}"
Source: "migrate_mdb.cmd"; DestDir: "{app}"
Source: "create_default_schema.cmd"; DestDir: "{app}"; AfterInstall: FinishInstall

//[Icons]
//Name: {group}\MariaDB; Filename: {app}\mariadb.exe

[Run]

[UninstallRun]
// Filename: {sys}\sc.exe; Parameters: "stop svws_server_mariadb" ; Flags: runhidden
// Filename: {sys}\sc.exe; Parameters: "delete svws_server_mariadb" ; Flags: runhidden
; TODO Remove MariaDB binaries
; TODO Add "delete data directory" to uninstaller
; TODO AfterInstall should remove the svws-server-db data directory if the "delete data directory"-option is selected
; TODO Remove registry entries

[UninstallDelete]
Type: filesandordirs; Name: "{app}\db\mariadb"
Type: dirifempty; Name: "{app}\db"
Type: filesandordirs; Name: "{app}\jdk"
Type: dirifempty; Name: "{app}"

[_ISTool]
EnableISX=false
Use7zip=false

[ThirdParty]
CompileLogMethod=append

[Code]

var
  SVWSConfigurationPage: TWizardPage;
  SVWSDirSelectionPage: TWizardPage;
  SVWSDataDirSelectionLabel: TNewStaticText;
  SVWSDataDirSelectionLabel2: TNewStaticText;
  SVWSDataDirEdit: TNewEdit;
  SVWSDataDirBrowseButton: TNewButton;
  SVWSDataDirTemp: String;

#include "Utils.iss"
#include "InitInstaller.iss"
#include "MariaDB.iss"
#include "SVWSServer.iss"


procedure SVWSDataDirBrowseButtonClick(Sender: TObject);
  begin
    If BrowseForFolder('Wählen Sie das SVWS-Datenverzeichnis aus: ', SVWSDataDirTemp, True) then
      begin
        SVWSDataDirEdit.Text := SVWSDataDirTemp + '\SVWS-Server';
      end;
  end;


{ Initialisiert die Wizard-Seiten. }
procedure InitializeWizard;
  begin
    // Finde die WizardPage, welche der Auswahl des Installationsverzeichnisses dient und ergänze die Auswahl des Datenverzeichnisses
    If not SVWSExistingDataDirectory then
      begin
        SVWSDirSelectionPage := PageFromID(wpSelectDir);
        SVWSDataDirTemp := ExpandConstant('{commonappdata}');

        SVWSDataDirSelectionLabel := TNewStaticText.Create(SVWSDirSelectionPage);
        SVWSDataDirSelectionLabel.Left := ScaleX(0);
        SVWSDataDirSelectionLabel.Top := WizardForm.DirBrowseButton.Top + ScaleY(40);
        SVWSDataDirSelectionLabel.AutoSize := True;
        SVWSDataDirSelectionLabel.Caption := 'Für das Datenverzeichnis wird normalerweise der nachfolgende Ordner verwendet.';
        SVWSDataDirSelectionLabel.Parent := SVWSDirSelectionPage.Surface;

        SVWSDataDirSelectionLabel2 := TNewStaticText.Create(SVWSDirSelectionPage);
        SVWSDataDirSelectionLabel2.Left := ScaleX(0);
        SVWSDataDirSelectionLabel2.Top := SVWSDataDirSelectionLabel.Top + SVWSDataDirSelectionLabel.Height + ScaleY(0);
        SVWSDataDirSelectionLabel2.AutoSize := True;
        SVWSDataDirSelectionLabel2.Caption := 'Passen Sie diesen an, wenn Sie von der Standardinstallation abweichen wollen.';
        SVWSDataDirSelectionLabel2.Parent := SVWSDirSelectionPage.Surface;

        SVWSDataDirEdit := TNewEdit.Create(SVWSDirSelectionPage);
        SVWSDataDirEdit.Left := WizardForm.DirEdit.Left;
        SVWSDataDirEdit.Top := SVWSDataDirSelectionLabel2.Top + SVWSDataDirSelectionLabel2.Height + ScaleY(5);
        SVWSDataDirEdit.Width := WizardForm.DirEdit.Width;
        SVWSDataDirEdit.Height := WizardForm.DirEdit.Height;
        SVWSDataDirEdit.Parent := SVWSDirSelectionPage.Surface;
        SVWSDataDirEdit.Text := SVWSDataDirTemp + '\SVWS-Server';

        SVWSDataDirBrowseButton := TNewButton.Create(SVWSDirSelectionPage);
        SVWSDataDirBrowseButton.Left := WizardForm.DirBrowseButton.Left;
        SVWSDataDirBrowseButton.Top := SVWSDataDirEdit.Top;
        SVWSDataDirBrowseButton.Width := WizardForm.DirBrowseButton.Width;
        SVWSDataDirBrowseButton.Height := WizardForm.DirBrowseButton.Height;
        SVWSDataDirBrowseButton.Parent := SVWSDirSelectionPage.Surface;
        SVWSDataDirBrowseButton.Caption := WizardForm.DirBrowseButton.Caption;
        SVWSDataDirBrowseButton.OnClick := @SVWSDataDirBrowseButtonClick;
      end;

    // Erzeugt die Konfigurationsseite vor dem Start der Installation
    SVWSConfigurationPage := CreateCustomPage(wpSelectTasks, 'SVWS-Server-Konfiguration', 'Konfigurationseinstellungen für den SVWS-Server');

    // Initialisiere den Konfigurationsbereich für die Installation des MariaDB-Servers
    InitializeMariaDBConfigurationSection(SVWSConfigurationPage, 0);

    // Initialisiere den Konfigurationsbereich für die Installation des SVWS-Servers
    InitializeSVWSServerConfigurationSection(SVWSConfigurationPage, GetMariaDBConfigurationSectionBottom);
  end;


{ Prüft, ob die aktuelle Seite über den Next-Button verlassen werden kann oder nicht. }
function NextButtonClick(CurPageID: Integer): Boolean;
  begin
    If CurPageID = wpSelectDir then
      begin
        If SVWSDataDirTemp = '' then
          begin
            MsgBox('Es muss ein gültiges Daten-Verzeichnis angegeben werden bevor die Installation fortgesetzt werden kann.', mbInformation, mb_Ok);
            result := False;
            Exit;
          end;
        SVWSDataDir := SVWSDataDirEdit.Text;
      end;

    // Prüfe die SVWS-Server-Konfiguration, bevor diese verlassen werden kann
    result := 
      CheckMariaDBConfigurationSectionValues(CurPageID) and 
      CheckSVWSServerConfigurationSectionValues(CurPageID);
  end;


{ Wird vor dem Kopieren aller Dateien ausgeführt... }
procedure StartInstall();
  begin
    // Initialisiere die Erweiterungen an der Progress-Page
    InitProgressPage();

    // Stoppe ggf. den Java-Server und den Datenbank-Server
    StopServiceSVWSServer;
    StopServiceMariaDB;

    // Schreibe das verwendete Programm- und Datenverzeichnis in die Registry
    RegWriteStringValue(HKLM, 'SOFTWARE\SVWSServer', 'ProgrammVerzeichnis', ExpandConstant('{app}'));
    RegWriteStringValue(HKLM, 'SOFTWARE\SVWSServer', 'DatenVerzeichnis', SVWSDataDir);

    // Starte die Installation von MariaDB
    StartMariaDBInstall;

    // Starte die Installation des SVWS-Servers
    StartSVWSServerInstall;

    // SVWS-Client: Nothing-To-Do
  end;


{ Wird nach dem Kopieren aller Dateien ausgeführt... }
procedure FinishInstall();
  begin
    // Beende die MariaDB-Installation
    FinishMariaDBInstall;

    // Starte den MariaDB-Service
    StartServiceMariaDB;

    // Beende die Installation des SVW-Servers
    FinishSVWSServerInstall;

    // Starte den SVWS-Server-Java-Dienst
    StartServiceSVWSServer;

    // Erzeuge ggf. eine SVWS-Datenbank mit dem aktuellen Schema
    CreateSVWSSchema;

    Log('Installation der SVWS-Server-Umgebung abgeschlossen.');
  end;


procedure CurUninstallStepChanged(CurUninstallStep: TUninstallStep);
  var
    KeyExists : Boolean;
  begin
    if CurUninstallStep = usUninstall then
      begin
        // Deinstalliere die einzelnen Komponenten
        UninstallSVWSServer();
        UninstallMariaDB();

        // Delete Registry Entries
        KeyExists := RegDeleteKeyIncludingSubkeys(HKLM, 'SOFTWARE\SVWSServer');
      end;
  end;

