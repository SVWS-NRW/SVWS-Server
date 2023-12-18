var
  MariaDBRootPasswort: String;
  MariaDBPort: Integer;
  CheckBoxInstallMariaDB: TNewCheckBox;
  StaticTextEditMariaDBRootPassword: TNewStaticText;
  EditMariaDBRootPassword: TNewEdit;
  StaticTextEditMariaDBPort: TNewStaticText;
  EditMariaDBPort: TNewEdit;


{ Wird aufgerufen, wenn der Zustand der Checkbox zur Installation von MariaDB geändert wird. }
procedure CheckBoxInstallMariaDBOnClick(Sender: TObject);
  begin
    InstallMariaDB := CheckBoxInstallMariaDB.Checked;
    StaticTextEditMariaDBRootPassword.Enabled := InstallMariaDB;
    EditMariaDBRootPassword.Enabled := InstallMariaDB;
    StaticTextEditMariaDBPort.Enabled := InstallMariaDB;
    EditMariaDBPort.Enabled := InstallMariaDB;
  end;


{ Initialisiert den Bereich für die MariaDB-Konfiguration
  @param Page   die Konfigurationsseite 
  @param Top    die Position in y-Richtung ab der der Bereich gezeichnet wird }
procedure InitializeMariaDBConfigurationSection(Page: TWizardPage; Top: Integer);
  var
    IsUnchangable: Boolean;
  begin
    CheckBoxInstallMariaDB := TNewCheckBox.Create(Page);
    CheckBoxInstallMariaDB.Left := ScaleX(0);
    CheckBoxInstallMariaDB.Top := Top;
    CheckBoxInstallMariaDB.Width := Page.SurfaceWidth;
    CheckBoxInstallMariaDB.Height := ScaleY(17);
    CheckBoxInstallMariaDB.Caption := ' Installiere MariaDB @mariadb_version@';
    if CompareStr(SVWSExistingMariaDBVersion, '') <> 0 then
      begin
        // Update
        CheckBoxInstallMariaDB.Caption := ' Aktualisiere MariaDB @mariadb_version@ (bereits installiert: ' + SVWSExistingMariaDBVersion + ')';
        IsUnchangable := True;
      end
    else
      begin
        // Fresh Install
        CheckBoxInstallMariaDB.Caption := ' Installiere MariaDB @mariadb_version@';
      end;
    CheckBoxInstallMariaDB.Enabled := not IsUnchangable;
    CheckBoxInstallMariaDB.Checked := InstallMariaDB;
    CheckBoxInstallMariaDB.Parent := Page.Surface;
    CheckBoxInstallMariaDB.OnClick := @CheckBoxInstallMariaDBOnClick;

    StaticTextEditMariaDBRootPassword := TNewStaticText.Create(Page);
    StaticTextEditMariaDBRootPassword.Left := ScaleX(20);
    StaticTextEditMariaDBRootPassword.Top := CheckBoxInstallMariaDB.Top + CheckBoxInstallMariaDB.Height + ScaleY(10);
    StaticTextEditMariaDBRootPassword.AutoSize := True;
    StaticTextEditMariaDBRootPassword.Caption := 'root-Kennwort: ';
    StaticTextEditMariaDBRootPassword.Enabled := not IsUnchangable and InstallMariaDB;
    StaticTextEditMariaDBRootPassword.Parent := Page.Surface;

    EditMariaDBRootPassword := TNewEdit.Create(Page);
    EditMariaDBRootPassword.Left := StaticTextEditMariaDBRootPassword.Left + StaticTextEditMariaDBRootPassword.Width + ScaleX(3);
    EditMariaDBRootPassword.Top := StaticTextEditMariaDBRootPassword.Top - ScaleY(2);
    EditMariaDBRootPassword.Width := Page.SurfaceWidth div 2 - ScaleX(8);
    if (CompareStr(SVWSExistingMariaDBVersion, '') <> 0) then
      EditMariaDBRootPassword.Text := ''
    else
      EditMariaDBRootPassword.Text := CreateRandomPassword(20);
    EditMariaDBRootPassword.Parent := Page.Surface;
    EditMariaDBRootPassword.Enabled := not IsUnchangable and InstallMariaDB;
    StaticTextEditMariaDBRootPassword.Height := EditMariaDBRootPassword.Height;

    StaticTextEditMariaDBPort := TNewStaticText.Create(Page);
    StaticTextEditMariaDBPort.Left := ScaleX(20);
    StaticTextEditMariaDBPort.Top := StaticTextEditMariaDBRootPassword.Top + StaticTextEditMariaDBRootPassword.Height + ScaleY(8);
    StaticTextEditMariaDBPort.AutoSize := True;
    StaticTextEditMariaDBPort.Caption := 'Port: ';
    StaticTextEditMariaDBPort.Enabled := not IsUnchangable and InstallMariaDB;
    StaticTextEditMariaDBPort.Parent := Page.Surface;

    EditMariaDBPort := TNewEdit.Create(Page);
    EditMariaDBPort.Left := StaticTextEditMariaDBPort.Left + StaticTextEditMariaDBPort.Width + ScaleX(3);
    EditMariaDBPort.Top := StaticTextEditMariaDBPort.Top - ScaleY(2);
    EditMariaDBPort.Width := Page.SurfaceWidth div 2 - ScaleX(8);
    EditMariaDBPort.Text := '3306';
    EditMariaDBPort.Parent := Page.Surface;
    EditMariaDBPort.Enabled := not IsUnchangable and InstallMariaDB;
    StaticTextEditMariaDBPort.Height := EditMariaDBPort.Height;    
  end;


{ Gibt das untere Ende für den Bereich der MariaDB-Konfiguration zurück
  @return das untere Ende des Konfigurationsbereichs }
function GetMariaDBConfigurationSectionBottom : Integer;
  begin
    result := EditMariaDBPort.Top + EditMariaDBPort.Height + ScaleY(8);
  end;



{ Prüft, ob die Eintragungen im Bereich der MariaDB-Konfiguration korrekt sind 
  @param CurPageID   die Page-ID der Konfigurationsseite, für die der Check durchgeführt wird.
  @return True, falls die Prüfung erfolgreich war, sonst False }
function CheckMariaDBConfigurationSectionValues(CurPageID: Integer) : Boolean;
  begin
    if CurPageID = SVWSConfigurationPage.ID then
      begin
        // TODO Warnung bei unsicheren Kennwörtern, Prüfung nur, wenn es später benötigt wird (also bei einer Neuinstallation des Servers)...
        MariaDBRootPasswort := EditMariaDBRootPassword.Text;
        MariaDBPort := StrToIntDef(EditMariaDBPort.Text, -1);
        if (MariaDBPort < 1024) or (MariaDBPort > 65535) then
          begin
            MsgBox('Der angegebene Port für MariaDB ist nicht zulässig. Der Wert muss korrigiert werden bevor die Installation fortgesetzt werden kann.', mbInformation, mb_Ok);
            result := False;
            Exit;
          end;
      end;
    result := True;
  end;


{ Prüfe, ob der MariaDB-Dienst existiert und stoppe diesen in diesem Fall }
procedure StopServiceMariaDB;
  begin
    StopWindowsService('svws_server_mariadb', 'SVWS-Server-MariaDB');
  end;


{ Starte den Datenbank-Service neu }
procedure StartServiceMariaDB;
  var
    msgBoxResult: Integer;
  begin
    if not StartWindowsService('svws_server_mariadb', 'SVWS-Server-MariaDB') then
      begin
        msgBoxResult := MsgBox('Fehler beim Starten des MariaDB-Dienstes. Überprüfen Sie Ihre System-Konfiguration. Ist evtl. schon ein anderer Datenbankdienst unter der gleichen Port-Adresse vorhanden? Die Installation wird dennoch fortgesetzt. Sie müssen den Dienst manuell starten.', mbError, MB_OK);
      end;
  end;


{ Erstellt das Datenbankverzeichnis für den Service neu - falls es nicht bereits existiert - 
  und erstellt den MariaDB-Service für dieses Verzeichnis }
procedure CreateServiceMariaDB;
  var
    ResultCode: Integer;
    msgBoxResult: Integer;
    ServiceName: String;
  begin
    if not InstallMariaDB then
      Exit;
    Log('Prüfe, ob das Datenverzeichnis von MariaDB eingerichtet werden muss...');
    if FileExists(SVWSDataDir + '\data\my.ini') then
      begin
        Log('  -> Ist bereits vorhanden (my.ini gefunden). Eine Einrichtung ist somit nicht nötig!');
        Exit;
      end;
    Log('  -> Nicht vorhanden (my.ini nicht gefunden). Richte das Verzeichnis und den zugehörigen Dienst ein...');
    ServiceName := 'svws_server_mariadb';
    if Exec(ExpandConstant('cmd'), 
            ExpandConstant('/C .\mariaDB_Create_Data_Directory.cmd "' + SVWSDataDir + '\data" "' + ServiceName + '" "' + MariaDBRootPasswort + '" ' + IntToStr(MariaDBPort)),
            ExpandConstant('{app}\db'), SW_HIDE, ewWaitUntilTerminated, ResultCode) then
      begin
        if SetIniString('mysqld', 'sort_buffer_size', '16777216', SVWSDataDir + '\data\my.ini') then
          begin
            Log('  -> sort_buffer_size wurde auf 16777216 gesetzt!');
          end
        else
          begin
            Log('  -> Fehler beim setzen der sort_buffer_size!');
          end;
        RegWriteStringValue(HKLM, 'SOFTWARE\SVWSServer', 'MariaDBPort', IntToStr(MariaDBPort));
        Log('    -> Verzeichnis und Dienst erfolgreich eingerichtet. (ResultCode=' + IntToStr(ResultCode) + ')');
        RemoveExplicitACLsRecursive(SVWSDataDir + '\data');
        AddExplicitACLs(SVWSDataDir + '\data', ServiceName, true, true, true);
      end
    else
      begin
        Log('    -> Fehler beim Erstellen des Verzeichnisses oder des Dienstes. (Fehlercode: ' + IntToStr(ResultCode) + ')');
        msgBoxResult := MsgBox('MariaDB-Installation: Fehler beim Erstellen des Verzeichnisses oder des Dienstes. (Fehlercode: ' + IntToStr(ResultCode) + '). Die Installation wird abgebrochen!', mbCriticalError, MB_OK);
        Abort();
      end;
  end;


{ Entfernt den MariaDB-Service }
procedure DestroyServiceMariaDB;
  var
    ResultCode: Integer;
  begin
    Log('Prüfe, ob der MariaDB-Dienst existiert und entferne diesen, falls er existiert...');
    if Exec(ExpandConstant('.\sc.exe'), 
            'query svws_server_mariadb', 
            ExpandConstant('{sys}'),
            SW_HIDE, ewWaitUntilTerminated, ResultCode) then
      begin
        Log('  -> Dienst existiert (ResultCode=' + IntToStr(ResultCode) + '), entferne ihn...');
        if Exec(ExpandConstant('.\sc.exe'), 
                'delete svws_server_mariadb', 
                ExpandConstant('{sys}'),
                SW_HIDE, ewWaitUntilTerminated, ResultCode) then
          begin
            Log('  -> Dienst wurde entfernt. (ResultCode=' + IntToStr(ResultCode) + ')');
          end
        else
          begin
            Log('  -> Dienst konnte nicht entfernt werden. (ResultCode=' + IntToStr(ResultCode) + ')');
          end;
      end
    else
      begin
        Log('  -> Dienst existiert nicht. (ResultCode=' + IntToStr(ResultCode) + ')');
      end;
  end;


{ Ruft das Skript zum Erstellen der Firewall-Regeln auf. }
procedure AddMariaDBFirewallRules();
  var
    ResultCode: Integer;
    msgBoxResult: Integer;
  begin
    Log('  -> Anlegen der Firewall-Regeln für den MariaDB-Server (Freischalten von Port ' + IntToStr(MariaDBPort) + ' für ein- und ausgehende Datenbank-Anfragen)');
    if Exec(ExpandConstant('{sys}\cmd.exe'), 
            '/C .\firewall_mariadb_add.cmd ' + IntToStr(MariaDBPort),
            ExpandConstant('{app}'),
            SW_HIDE, ewWaitUntilTerminated, ResultCode) then
      begin
        Log('  -> Firewall-Regeln für den MariaDB-Server erfolgreich angelegt. (ResultCode=' + IntToStr(ResultCode) + ')');
      end
    else
      begin
        Log('  -> Firewall-Regeln für den MariaDB-Server konnten nicht angelegt werden. (ResultCode=' + IntToStr(ResultCode) + ')');
        msgBoxResult := MsgBox('Fehler beim Erzeugen der Firewall-Regeln. Die Installation wird dennoch fortgesetzt. Die Regeln müssen später ggf. von Hand erzeugt werden!', mbError, MB_OK);
      end;
  end;


{ Ruft das Skript zum Entfernen der Firewall-Regeln auf. }
procedure RemoveMariaDBFirewallRules();
  var
    ResultCode: Integer;
    msgBoxResult: Integer;
  begin
    Log('  -> Entfernen der Firewall-Regeln für den MariaDB-Server für ein- und ausgehende Datenbank-Anfragen');
    if Exec(ExpandConstant('{sys}\cmd.exe'), 
            '/C .\firewall_mariadb_remove.cmd', 
            ExpandConstant('{app}'),
            SW_HIDE, ewWaitUntilTerminated, ResultCode) then
      begin
        Log('  -> Firewall-Regeln für den MariaDB-Server erfolgreich entfernt. (ResultCode=' + IntToStr(ResultCode) + ')');
      end
    else
      begin
        Log('  -> Firewall-Regeln für den MariaDB-Server konnten nicht entfernt werden. (ResultCode=' + IntToStr(ResultCode) + ')');
        msgBoxResult := MsgBox('Fehler beim Entfernen der Firewall-Regeln. Die Regeln müssen später ggf. von Hand entfernt werden!', mbError, MB_OK);
      end;
  end;


{ Wird zum Starten der Installation des SVWS-MariaDB-Servers aufgerufen. }
procedure StartMariaDBInstall;
  begin
    if IsInstallMariaDB then
      begin
        Log('Starte die Installation von MariaDB...');
        Log('- Version ' + SVWSNewMariaDBVersion);
      end;
  end;


{ Wird zum Beenden der Installation des SVWS-MariaDB-Servers aufgerufen. }
procedure FinishMariaDBInstall;
  begin
    if IsInstallMariaDB then
      begin
        ProgressMemo.Lines.Append('Installiere MariaDB...');
        WizardForm.ProgressGauge.Position := 50 * WizardForm.ProgressGauge.Max div 100;

        Log('- Setze MariaDB-Version in der Registry');
        RegWriteStringValue(HKLM, 'SOFTWARE\SVWSServer', 'MariaDBVersion', SVWSNewMariaDBVersion);
        Log('- FERTIG: MariaDB auf Version ' + SVWSNewMariaDBVersion + ' aktualisiert!');

        ProgressMemo.Lines.Append('-> Richte Datenbankservice ein...');
        WizardForm.ProgressGauge.Position := 51 * WizardForm.ProgressGauge.Max div 100;

        // Erstelle ggf. den MariaDB-Service
        CreateServiceMariaDB;

        // Füge die Firewall-Regeln hinzu
        AddMariaDBFirewallRules;

        ProgressMemo.Lines.Append('MariaDB-Installation fertig!');
        WizardForm.ProgressGauge.Position := 59 * WizardForm.ProgressGauge.Max div 100;
      end;
  end;


{ Deinstalliere den MariaDB-Server. Führe dabei die einzelnen Schritte der Installation in umgekehrter Reihenfolge durch. }
procedure UninstallMariaDB;
  var
    KeyExists : Boolean;
    Port: String;
  begin
    if IsUninstallMariaDB then
      begin
        Log('Deinstalliere MariaDB...');

        // Stoppe ggf. den Datenbank-Server-Service
        Log('  - Stoppe den MariaDB-Dienst');
        StopServiceMariaDB();

        // Warte darauf, dass der MariaDB-Server schon vollständig beendet wurde...
        if not RegQueryStringValue(HKLM, 'SOFTWARE\SVWSServer', 'MariaDBPort', Port) then
          Port := '3306';
        if WaitTillPortFree(StrToInt(Port),20,100) then
          begin
            Log('  - MariaDB-Dienst beendet');
          end
        else
          begin
            Log('  - MariaDB-Dienst wurde nicht beendet');
          end;

        // Entferne die Firewall-Regeln sofern sie definiert sind.
        Log('- Entferne die Firewall-Regeln');
        RemoveMariaDBFirewallRules; 

        // Entferne den MariaDB-Service
        Log('- Entferne den MariaDB-Dienst');
        DestroyServiceMariaDB;

        // Delete Registry Entries
        Log('- Entferne die MariaDB-Version aus der Registry');
        KeyExists := RegDeleteValue(HKLM, 'SOFTWARE\SVWSServer', 'MariaDBVersion');
      end;
  end;