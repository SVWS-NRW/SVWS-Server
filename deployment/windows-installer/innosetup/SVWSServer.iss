var
  SVWSDBSchema: String;
  SVWSDBUser: String;
  SVWSDBPassword: String;
  SVWSKeyStorePassword: String;
  IsCreateSchema: Boolean;
  IsMigrateSchema: Boolean;
  CheckBoxInstallSVWSServer: TNewCheckBox;
  StaticTextEditSVWSServerDBSchema: TNewStaticText;
  EditSVWSServerDBSchema: TNewEdit;
  StaticTextEditSVWSServerDBUser: TNewStaticText;
  EditSVWSServerDBUser: TNewEdit;
  StaticTextEditSVWSServerDBPassword: TNewStaticText;
  EditSVWSServerDBPassword: TNewEdit;
  CheckBoxCreateSchema: TNewCheckBox;
  CheckBoxMigrateSchema: TNewCheckBox;

  ZertifikatInputPage: TWizardPage;
  StaticTextEditHostname: TNewStaticText;
  StaticTextEditZertifikatHostname: TNewStaticText;
  EditZertifikatHostname: TNewEdit;
  StaticTextEditZertifikatSchulname: TNewStaticText;
  EditZertifikatSchulname: TNewEdit;
  StaticTextEditZertifikatOrtsname: TNewStaticText;
  EditZertifikatOrtsname: TNewEdit;
  ZertifikatHostname: String;
  ZertifikatSchulname: String;
  ZertifikatOrtsname: String;

  QueryDBRootPassword: Boolean;
  DBRootPasswordInputPage: TWizardPage;
  StaticTextEditDBRootPassword: TNewStaticText;
  EditDBRootPassword: TNewEdit;

  ManualSVWSConfig: Boolean;
  ManualSVWSConfigInputPage: TWizardPage;
  ManualSVWSConfigDBMSLabel: TNewStaticText;
  ManualSVWSConfigDBMSComboBox: TNewComboBox;
  ManualSVWSConfigDBLocationLabel: TNewStaticText;
  ManualSVWSConfigDBLocation: TNewEdit;
  ManualSVWSConfigDBPortLabel: TNewStaticText;
  ManualSVWSConfigDBPort: TNewEdit;
  SVWSConfigDBMS: String;
  SVWSConfigDBHost: String;
  SVWSConfigDBPort: Integer;

#include "Migration.iss"

procedure UpdateSVWSServerSchemaInfosEnabled();
  var
    EnableSchemaInfos: Boolean;
  begin
    EnableSchemaInfos := InstallSVWSServer and (CheckBoxCreateSchema.Checked or CheckBoxMigrateSchema.Checked);
    StaticTextEditSVWSServerDBSchema.Enabled := EnableSchemaInfos;
    EditSVWSServerDBSchema.Enabled := EnableSchemaInfos;
    StaticTextEditSVWSServerDBUser.Enabled := EnableSchemaInfos;
    EditSVWSServerDBUser.Enabled := EnableSchemaInfos;
    StaticTextEditSVWSServerDBPassword.Enabled := EnableSchemaInfos;
    EditSVWSServerDBPassword.Enabled := EnableSchemaInfos;
  end;

{ Wird aufgerufen, wenn der Zustand der Checkbox zur Installation des SVWS-Servers geändert wird. }
procedure CheckBoxInstallSVWSServerOnClick(Sender: TObject);
  begin
    InstallSVWSServer := CheckBoxInstallSVWSServer.Checked;
    UpdateSVWSServerSchemaInfosEnabled();
    CheckBoxCreateSchema.Enabled := InstallSVWSServer;
    CheckBoxMigrateSchema.Enabled := InstallSVWSServer;
  end;


{ Wird aufgerufen, wenn der Zustand der Checkbox wegen dem Erstellen eines neuen Datenbank-Schemas geändert wird. }
procedure CheckBoxCreateSchemaOnClick(Sender: TObject);
  begin
    if CheckBoxCreateSchema.Checked then
      begin
        CheckBoxMigrateSchema.Checked := False;
      end;
    UpdateSVWSServerSchemaInfosEnabled();
  end;


{ Wird aufgerufen, wenn der Zustand der Checkbox wegen dem Migrieren eines Schild 2 - Schemas geändert wird. }
procedure CheckBoxMigrateSchemaOnClick(Sender: TObject);
  begin
    if CheckBoxMigrateSchema.Checked then
      begin
        CheckBoxCreateSchema.Checked := False;
      end;
    UpdateSVWSServerSchemaInfosEnabled();
  end;


{ Initialisiert den Bereich für die SVWS-Server-Konfiguration
  @param Page   die Konfigurationsseite 
  @param Top    die Position in y-Richtung ab der der Bereich gezeichnet wird }
procedure InitializeSVWSServerConfigurationSection(Page: TWizardPage; Top: Integer);
  var 
    IsSVWSServerUpdate: Boolean;
    HostName: String;
    IsValidHostName: Boolean;
  begin
    IsSVWSServerUpdate := (CompareStr(SVWSExistingVersion, '') <> 0);

    CheckBoxInstallSVWSServer := TNewCheckBox.Create(Page);
    CheckBoxInstallSVWSServer.Left := ScaleX(0);
    CheckBoxInstallSVWSServer.Top := Top;
    CheckBoxInstallSVWSServer.Width := Page.SurfaceWidth;
    CheckBoxInstallSVWSServer.Height := ScaleY(17);
    CheckBoxInstallSVWSServer.Caption := ' Installiere SVWS-Server @version@';
    if IsSVWSServerUpdate then
      begin
        CheckBoxInstallSVWSServer.Caption := CheckBoxInstallSVWSServer.Caption + ' (bereits installiert: ' + SVWSExistingVersion + ')';
      end;
    CheckBoxInstallSVWSServer.Checked := InstallSVWSServer;
    CheckBoxInstallSVWSServer.Parent := Page.Surface;
    CheckBoxInstallSVWSServer.OnClick := @CheckBoxInstallSVWSServerOnClick;

    CheckBoxCreateSchema := TNewCheckBox.Create(Page);
    CheckBoxCreateSchema.Left := ScaleX(20);
    CheckBoxCreateSchema.Top := CheckBoxInstallSVWSServer.Top + CheckBoxInstallSVWSServer.Height + ScaleY(10);
    CheckBoxCreateSchema.Width := Page.SurfaceWidth;
    CheckBoxCreateSchema.Height := ScaleY(17);
    CheckBoxCreateSchema.Caption := ' Erstelle ein neues Schema';
    CheckBoxCreateSchema.Checked := not IsSVWSServerUpdate;
    CheckBoxCreateSchema.Enabled := InstallSVWSServer;
    CheckBoxCreateSchema.Parent := Page.Surface;
    CheckBoxCreateSchema.OnClick := @CheckBoxCreateSchemaOnClick;

    CheckBoxMigrateSchema := TNewCheckBox.Create(Page);
    CheckBoxMigrateSchema.Left := ScaleX(20);
    CheckBoxMigrateSchema.Top := CheckBoxCreateSchema.Top + CheckBoxCreateSchema.Height + ScaleY(8);
    CheckBoxMigrateSchema.Width := Page.SurfaceWidth;
    CheckBoxMigrateSchema.Height := ScaleY(17);
    CheckBoxMigrateSchema.Caption := ' Migriere aus einem Schild v2.x - Schema';
    CheckBoxMigrateSchema.Checked := False;
    CheckBoxMigrateSchema.Enabled := InstallSVWSServer;
    CheckBoxMigrateSchema.Parent := Page.Surface;
    CheckBoxMigrateSchema.OnClick := @CheckBoxMigrateSchemaOnClick;

    StaticTextEditSVWSServerDBSchema := TNewStaticText.Create(Page);
    StaticTextEditSVWSServerDBSchema.Left := ScaleX(20);
    StaticTextEditSVWSServerDBSchema.Top := CheckBoxMigrateSchema.Top + CheckBoxMigrateSchema.Height + ScaleY(8);
    StaticTextEditSVWSServerDBSchema.AutoSize := True;
    StaticTextEditSVWSServerDBSchema.Caption := 'Datenbank-Schema: ';
    StaticTextEditSVWSServerDBSchema.Parent := Page.Surface;

    EditSVWSServerDBSchema := TNewEdit.Create(Page);
    EditSVWSServerDBSchema.Left := StaticTextEditSVWSServerDBSchema.Left + StaticTextEditSVWSServerDBSchema.Width + ScaleX(3);
    EditSVWSServerDBSchema.Top := StaticTextEditSVWSServerDBSchema.Top - ScaleY(2);
    EditSVWSServerDBSchema.Width := Page.SurfaceWidth div 2 - ScaleX(8);
    EditSVWSServerDBSchema.Text := 'svwsdb';
    EditSVWSServerDBSchema.Parent := Page.Surface;
    StaticTextEditSVWSServerDBSchema.Height := EditSVWSServerDBSchema.Height;

    StaticTextEditSVWSServerDBUser := TNewStaticText.Create(Page);
    StaticTextEditSVWSServerDBUser.Left := ScaleX(20);
    StaticTextEditSVWSServerDBUser.Top := StaticTextEditSVWSServerDBSchema.Top + StaticTextEditSVWSServerDBSchema.Height + ScaleY(8);
    StaticTextEditSVWSServerDBUser.AutoSize := True;
    StaticTextEditSVWSServerDBUser.Caption := 'Datenbank-Benutzer: ';
    StaticTextEditSVWSServerDBUser.Parent := Page.Surface;

    EditSVWSServerDBUser := TNewEdit.Create(Page);
    EditSVWSServerDBUser.Left := StaticTextEditSVWSServerDBUser.Left + StaticTextEditSVWSServerDBUser.Width + ScaleX(3);
    EditSVWSServerDBUser.Top := StaticTextEditSVWSServerDBUser.Top - ScaleY(2);
    EditSVWSServerDBUser.Width := Page.SurfaceWidth div 2 - ScaleX(8);
    EditSVWSServerDBUser.Text := 'svwsadmin';
    EditSVWSServerDBUser.Parent := Page.Surface;
    StaticTextEditSVWSServerDBUser.Height := EditSVWSServerDBUser.Height;

    StaticTextEditSVWSServerDBPassword := TNewStaticText.Create(Page);
    StaticTextEditSVWSServerDBPassword.Left := ScaleX(20);
    StaticTextEditSVWSServerDBPassword.Top := StaticTextEditSVWSServerDBUser.Top + StaticTextEditSVWSServerDBUser.Height + ScaleY(8);
    StaticTextEditSVWSServerDBPassword.AutoSize := True;
    StaticTextEditSVWSServerDBPassword.Caption := 'Datenbank-Kennwort: ';
    StaticTextEditSVWSServerDBPassword.Parent := Page.Surface;

    EditSVWSServerDBPassword := TNewEdit.Create(Page);
    EditSVWSServerDBPassword.Left := StaticTextEditSVWSServerDBPassword.Left + StaticTextEditSVWSServerDBPassword.Width + ScaleX(3);
    EditSVWSServerDBPassword.Top := StaticTextEditSVWSServerDBPassword.Top - ScaleY(2);
    EditSVWSServerDBPassword.Width := Page.SurfaceWidth div 2 - ScaleX(8);
    EditSVWSServerDBPassword.Text := CreateRandomPassword(20);
    EditSVWSServerDBPassword.Parent := Page.Surface;
    StaticTextEditSVWSServerDBPassword.Height := EditSVWSServerDBPassword.Height;

    UpdateSVWSServerSchemaInfosEnabled();

    if not FileExists(ExpandConstant('{code:GetDataDir}\res\keystore')) then
      begin
        ZertifikatInputPage := CreateCustomPage(Page.ID, 'Zertifikatsinformationen', 'Information für das Server-Zertifikat');

        StaticTextEditHostname := TNewStaticText.Create(ZertifikatInputPage);
        StaticTextEditHostname.Left := ScaleX(0);
        StaticTextEditHostname.Top := ScaleY(8);
        StaticTextEditHostname.AutoSize := True;
        Hostname := GetComputerNameString;
        IsValidHostName := CheckCertificateHostname(Hostname);
        if (IsValidHostName) then
          StaticTextEditHostname.Caption := 'Hostname (genutzt für das Zertifikat): ' + Hostname
        else
          StaticTextEditHostname.Caption := 'Hostname (genutzt für das Zertifikat): ' + Hostname + ' enthält ungültige Zeichen!';
        StaticTextEditHostname.Enabled := InstallSVWSServer;
        StaticTextEditHostname.Parent := ZertifikatInputPage.Surface;

        StaticTextEditZertifikatHostname := TNewStaticText.Create(ZertifikatInputPage);
        StaticTextEditZertifikatHostname.Left := ScaleX(0);
        StaticTextEditZertifikatHostname.Top := StaticTextEditHostname.Top + StaticTextEditHostname.Height + ScaleY(8);
        StaticTextEditZertifikatHostname.AutoSize := True;
        StaticTextEditZertifikatHostname.Caption := 'Weiterer Zertifikat-Hostname: ';
        StaticTextEditZertifikatHostname.Enabled := InstallSVWSServer;
        StaticTextEditZertifikatHostname.Parent := ZertifikatInputPage.Surface;

        EditZertifikatHostname := TNewEdit.Create(ZertifikatInputPage);
        EditZertifikatHostname.Left := StaticTextEditZertifikatHostname.Left + StaticTextEditZertifikatHostname.Width + ScaleX(3);
        EditZertifikatHostname.Top := StaticTextEditZertifikatHostname.Top - ScaleY(2);
        EditZertifikatHostname.Width := ZertifikatInputPage.SurfaceWidth div 2 - ScaleX(8);
        EditZertifikatHostname.Text := '';
        EditZertifikatHostname.Parent := ZertifikatInputPage.Surface;
        EditZertifikatHostname.Enabled := InstallSVWSServer;
        StaticTextEditZertifikatHostname.Height := EditZertifikatHostname.Height;

        StaticTextEditZertifikatSchulname := TNewStaticText.Create(ZertifikatInputPage);
        StaticTextEditZertifikatSchulname.Left := ScaleX(0);
        StaticTextEditZertifikatSchulname.Top := StaticTextEditZertifikatHostname.Top + StaticTextEditZertifikatHostname.Height + ScaleY(8);
        StaticTextEditZertifikatSchulname.AutoSize := True;
        StaticTextEditZertifikatSchulname.Caption := 'Schulname: ';
        StaticTextEditZertifikatSchulname.Enabled := InstallSVWSServer;
        StaticTextEditZertifikatSchulname.Parent := ZertifikatInputPage.Surface;

        EditZertifikatSchulname := TNewEdit.Create(ZertifikatInputPage);
        EditZertifikatSchulname.Left := StaticTextEditZertifikatSchulname.Left + StaticTextEditZertifikatSchulname.Width + ScaleX(3);
        EditZertifikatSchulname.Top := StaticTextEditZertifikatSchulname.Top - ScaleY(2);
        EditZertifikatSchulname.Width := ZertifikatInputPage.SurfaceWidth div 2 - ScaleX(8);
        EditZertifikatSchulname.Text := '';
        EditZertifikatSchulname.Parent := ZertifikatInputPage.Surface;
        EditZertifikatSchulname.Enabled := InstallSVWSServer;
        StaticTextEditZertifikatSchulname.Height := EditZertifikatSchulname.Height;

        StaticTextEditZertifikatOrtsname := TNewStaticText.Create(ZertifikatInputPage);
        StaticTextEditZertifikatOrtsname.Left := ScaleX(0);
        StaticTextEditZertifikatOrtsname.Top := StaticTextEditZertifikatSchulname.Top + StaticTextEditZertifikatSchulname.Height + ScaleY(8);
        StaticTextEditZertifikatOrtsname.AutoSize := True;
        StaticTextEditZertifikatOrtsname.Caption := 'Ortsname: ';
        StaticTextEditZertifikatOrtsname.Enabled := InstallSVWSServer;
        StaticTextEditZertifikatOrtsname.Parent := ZertifikatInputPage.Surface;

        EditZertifikatOrtsname := TNewEdit.Create(ZertifikatInputPage);
        EditZertifikatOrtsname.Left := StaticTextEditZertifikatOrtsname.Left + StaticTextEditZertifikatOrtsname.Width + ScaleX(3);
        EditZertifikatOrtsname.Top := StaticTextEditZertifikatOrtsname.Top - ScaleY(2);
        EditZertifikatOrtsname.Width := ZertifikatInputPage.SurfaceWidth div 2 - ScaleX(8);
        EditZertifikatOrtsname.Text := '';
        EditZertifikatOrtsname.Parent := ZertifikatInputPage.Surface;
        EditZertifikatOrtsname.Enabled := InstallSVWSServer;
        StaticTextEditZertifikatOrtsname.Height := EditZertifikatOrtsname.Height;
      end;

      QueryDBRootPassword := False;
  end;


{ Gibt das untere Ende für den Bereich der SVWS-Server-Konfiguration zurück
  @return das untere Ende des Konfigurationsbereichs }
function GetSVWSServerConfigurationSectionBottom : Integer;
  begin
    result := StaticTextEditSVWSServerDBPassword.Top + StaticTextEditSVWSServerDBPassword.Height + ScaleY(8);
  end;


{ Wird aufgerufen, wenn der Zustand der Combobox zur Auswahl des DBMS bei manueller DB-Konfigurationgeändert wurde. }
procedure ManualSVWSConfigDBMSComboBoxOnChange(Sender: TObject);
  begin
    case ManualSVWSConfigDBMSComboBox.ItemIndex of
      0:
        begin
          ManualSVWSConfigDBPort.Text := '3306';
          StaticTextEditDBRootPassword.Caption := 'Datenbank-Kennwort für root: ';
        end;
      1:
        begin
          ManualSVWSConfigDBPort.Text := '1433';
          StaticTextEditDBRootPassword.Caption := 'Datenbank-Kennwort für sa: ';
        end;
    end;    
  end;


{ Prüft, ob die Eintragungen im Bereich der SVWS-Server-Konfiguration korrekt sind.
  @param CurPageID   die Page-ID der Konfigurationsseite, für die der Check durchgeführt wird.
  @return True, falls die Prüfung erfolgreich war, sonst False }
function CheckSVWSServerConfigurationSectionValues(CurPageID: Integer) : Boolean;
  var
    PrevPage: TWizardPage;
  begin
    if CurPageID = SVWSConfigurationPage.ID then
      begin
        SVWSDBSchema := EditSVWSServerDBSchema.Text;
        if SVWSDBSchema = '' then
          begin
            MsgBox('Es muss ein gültiges Datenbank-Schema angegeben werden bevor die Installation fortgesetzt werden kann.', mbInformation, mb_Ok);
            result := False;
            Exit;
          end;
        SVWSDBUser := EditSVWSServerDBUser.Text;
        if SVWSDBSchema = '' then
          begin
            MsgBox('Es muss ein gültiger Datenbank-Benutzer angegeben werden bevor die Installation fortgesetzt werden kann.', mbInformation, mb_Ok);
            result := False;
            Exit;
          end;
        // TODO Warnung bei unsicheren Kennwörtern
        SVWSDBPassword := EditSVWSServerDBPassword.Text;
        if SVWSDBPassword = '' then
          begin
            MsgBox('Es muss ein gültiges Kennwort für den Datenbank-Benutzer angegeben werden bevor die Installation fortgesetzt werden kann.', mbInformation, mb_Ok);
            result := False;
            Exit;
          end;
        SVWSKeyStorePassword := CreateRandomPassword(20);
        IsCreateSchema := CheckBoxCreateSchema.Checked;
        IsMigrateSchema := CheckBoxMigrateSchema.Checked;
        PrevPage := SVWSConfigurationPage;
        if not FileExists(SVWSDataDir + '\res\keystore') then
          PrevPage := ZertifikatInputPage;
        ManualSVWSConfig := False;
        if not InstallMariaDB then
          begin
            ManualSVWSConfig := (CompareStr(SVWSExistingMariaDBVersion, '') = 0) and not FileExists(SVWSDataDir + '\res\svwsconfig.json');
            if ManualSVWSConfig then
              begin  
                ManualSVWSConfigInputPage := CreateCustomPage(PrevPage.ID, 'DB-Konfiguration', 'Erstellen einer manuellen Datenbank-Konfiguration');
                PrevPage := ManualSVWSConfigInputPage;

                ManualSVWSConfigDBMSLabel := TNewStaticText.Create(ManualSVWSConfigInputPage);
                with ManualSVWSConfigDBMSLabel do
                  begin
                    Left := ScaleX(0);
                    Top := ScaleY(8);
                    AutoSize := True;
                    Caption := 'DBMS: ';
                    Enabled := True;
                    Parent := ManualSVWSConfigInputPage.Surface;                    
                  end;

                ManualSVWSConfigDBMSComboBox := TNewComboBox.Create(ManualSVWSConfigInputPage);
                with ManualSVWSConfigDBMSComboBox do
                  begin
                    Left := ManualSVWSConfigDBMSLabel.Left + ManualSVWSConfigDBMSLabel.Width + ScaleX(3);
                    Top := ManualSVWSConfigDBMSLabel.Top - ScaleY(2);
                    Width := ScaleX(200);
                    Enabled := True;
                    Parent := ManualSVWSConfigInputPage.Surface;
                    Style := csDropDown;
                    Items.Add('MariaDB');
                    Items.Add('Microsoft SQL Server');
                    OnChange := @ManualSVWSConfigDBMSComboBoxOnChange;
                  end;

                ManualSVWSConfigDBLocationLabel :=  TNewStaticText.Create(ManualSVWSConfigInputPage);
                with ManualSVWSConfigDBLocationLabel do
                  begin
                    Left := ScaleX(0);
                    Top := ManualSVWSConfigDBMSComboBox.Top + ManualSVWSConfigDBMSComboBox.Height + ScaleY(8);
                    Caption := 'Server:';
                    Parent := ManualSVWSConfigInputPage.Surface;                    
                  end;

                ManualSVWSConfigDBLocation := TNewEdit.Create(ManualSVWSConfigInputPage);
                with ManualSVWSConfigDBLocation do
                  begin
                    Left := ManualSVWSConfigDBLocationLabel.Left + ManualSVWSConfigDBLocationLabel.Width + ScaleX(3);
                    Top := ManualSVWSConfigDBLocationLabel.Top - ScaleY(2);
                    Width := ScaleX(200);
                    Text := 'localhost';
                    Parent := ManualSVWSConfigInputPage.Surface;
                  end;

                ManualSVWSConfigDBPortLabel := TNewStaticText.Create(ManualSVWSConfigInputPage);
                with ManualSVWSConfigDBPortLabel do
                  begin
                    Left := ScaleX(0);
                    Top := ManualSVWSConfigDBLocation.Top + ManualSVWSConfigDBLocation.Height + ScaleY(8);
                    AutoSize := True;
                    Caption := 'Port: ';
                    Parent := ManualSVWSConfigInputPage.Surface;
                  end;

                ManualSVWSConfigDBPort := TNewEdit.Create(ManualSVWSConfigInputPage);
                with ManualSVWSConfigDBPort do
                  begin
                    Left := ManualSVWSConfigDBPortLabel.Left + ManualSVWSConfigDBPortLabel.Width + ScaleX(3);
                    Top := ManualSVWSConfigDBPortLabel.Top - ScaleY(2);
                    Width := ScaleX(50);
                    Text := '3306';
                    Parent := ManualSVWSConfigInputPage.Surface;
                  end;
                ManualSVWSConfigDBPortLabel.Height := ManualSVWSConfigDBPort.Height;    
              end;
            if IsCreateSchema or IsMigrateSchema then
              begin
                QueryDBRootPassword := True;
                DBRootPasswordInputPage := CreateCustomPage(PrevPage.ID, 'DB-Admin-Zugriff erlauben', 'Administrativen Zugriff für das Erstellen des DB-Schemas erlauben');
                PrevPage := DBRootPasswordInputPage;

                StaticTextEditDBRootPassword := TNewStaticText.Create(DBRootPasswordInputPage);
                StaticTextEditDBRootPassword.Left := ScaleX(0);
                StaticTextEditDBRootPassword.Top := ScaleY(8);
                StaticTextEditDBRootPassword.AutoSize := True;
                StaticTextEditDBRootPassword.Caption := 'Datenbank-Kennwort für root: ';
                StaticTextEditDBRootPassword.Enabled := True;
                StaticTextEditDBRootPassword.Parent := DBRootPasswordInputPage.Surface;

                EditDBRootPassword := TNewEdit.Create(DBRootPasswordInputPage);
                EditDBRootPassword.Left := StaticTextEditDBRootPassword.Left + StaticTextEditDBRootPassword.Width + ScaleX(3);
                EditDBRootPassword.Top := StaticTextEditDBRootPassword.Top - ScaleY(2);
                EditDBRootPassword.Width := DBRootPasswordInputPage.SurfaceWidth div 2 - ScaleX(8);
                EditDBRootPassword.Text := '';
                EditDBRootPassword.Parent := DBRootPasswordInputPage.Surface;
                EditDBRootPassword.Enabled := True;
                StaticTextEditDBRootPassword.Height := EditDBRootPassword.Height;
              end;
          end;
        // Hinzufügen der Seite mit den Migrations-Informationen zur Quelldatenbank
        if IsMigrateSchema then
          InitializeMigrationPage(PrevPage);
      end
    else if ManualSVWSConfig and (CurPageID = ManualSVWSConfigInputPage.ID) then
      begin 
        case ManualSVWSConfigDBMSComboBox.ItemIndex of
          0:
            begin
              SVWSConfigDBMS := 'MARIA_DB';
            end;
          1:
            begin
              SVWSConfigDBMS := 'MSSQL';
            end;
        end;    
        SVWSConfigDBHost := ManualSVWSConfigDBLocation.Text;
        SVWSConfigDBPort := StrToIntDef(ManualSVWSConfigDBPort.Text, -1);
        if (SVWSConfigDBPort < 1024) or (SVWSConfigDBPort > 65535) then
          begin
            MsgBox('Der angegebene Port ist nicht zulässig. Der Wert muss korrigiert werden bevor die Installation fortgesetzt werden kann.', mbInformation, mb_Ok);
            result := False;
            Exit;
          end;
      end
    else if (not FileExists(SVWSDataDir + '\res\keystore')) and (CurPageID = ZertifikatInputPage.ID) then
      begin
        ZertifikatHostname := EditZertifikatHostname.Text;
        if not CheckCertificateHostname(ZertifikatHostname) or not CheckCertificateHostname(GetComputerNameString) then
          begin
            MsgBox('Ein gültiger Hostname besteht nur aus eine Kombination der folgenden Zeichen: Buchstabe, Zahl und Bindestrich (ohn ä, ö, ü und ß). Dies muss korrigiert werden bevor die Installation fortgesetzt werden kann.', mbInformation, mb_Ok);
            result := False;
            Exit;
          end;
        ZertifikatSchulname := EditZertifikatSchulname.Text;
        if ZertifikatSchulname = '' then
          begin
            MsgBox('Es muss ein gültiger Schulname für das Server-Zertifikat eingegeben werden bevor die Installation fortgesetzt werden kann.', mbInformation, mb_Ok);
            result := False;
            Exit;
          end;
        ZertifikatOrtsname := EditZertifikatOrtsname.Text;
        if ZertifikatOrtsname = '' then
          begin
            MsgBox('Es muss ein gültiger Ort, wo sich die Schule befindet, für das Server-Zertifikat eingegeben werden bevor die Installation fortgesetzt werden kann.', mbInformation, mb_Ok);
            result := False;
            Exit;
          end;
      end
    else if QueryDBRootPassword and (CurPageID = DBRootPasswordInputPage.ID) then
      begin
        MariaDBRootPasswort := EditDBRootPassword.Text;
        if MariaDBRootPasswort = '' then
          begin
            if IsCreateSchema then
              MsgBox('Es muss ein gültiges Root-Kennwort für den Datenbank-Zugriff eingegeben werden, damit ein neues Schema erstellt werden kann.', mbInformation, mb_Ok)
            else
              MsgBox('Es muss ein gültiges Root-Kennwort für den Datenbank-Zugriff eingegeben werden, damit eine Schild2-Datenbank migriert werden kann.', mbInformation, mb_Ok);
            result := False;
            Exit;
          end;
        Log('  -> Datenbankkennwort für den Root-Zugriff zum Anlegen eines Schemas wurde eingegeben.');
      end
    else if IsMigrateSchema then
      begin
        result := CheckMigrationConfigurationValues(CurPageID);
        if result = False then
          Exit;
      end;
    result := True;
  end;


{ Prüfe, ob der SVWS-Server-Dienst existiert und stoppe diesen in diesem Fall }
procedure StopServiceSVWSServer;
  begin
    StopWindowsService('svws_server_service', 'SVWS-Server-Java');
  end;


{ Starte den SVWS-Server-Service neu }
procedure StartServiceSVWSServer;
  var
    msgBoxResult: Integer;
  begin
    if not StartWindowsService('svws_server_service', 'SVWS-Server-Java') then
      begin
        msgBoxResult := MsgBox('Fehler beim Starten des SVWS-Server-Java-Dienstes. Die Installation wird dennoch fortgesetzt. Sie müssen den SVWS-Server-Java-Dienst überprüfen und ggf. später manuell starten!', mbError, MB_OK);
      end;    
  end;


{ Erstellt den Dienst für den SVWS-Java-Server }
procedure CreateServiceSVWSServer;
  var
    ResultCode: Integer;
    msgBoxResult: Integer;
    serviceConfig: String;
  begin
    // Erzeuge die Konfiguration für den SVWS-Java-Server-Dienst
    serviceConfig := AnsiString(ExpandConstant(
      '<service>' + #13#10 +
      '  <id>svws_server_service</id>' + #13#10 +
      '  <name>SVWS-Java-Service</name>' + #13#10 +
      '  <description>Dieser Dienst startet den SVWS-Java-Server.</description>' + #13#10 +
      '  <workingdirectory>{app}</workingdirectory>' + #13#10 +
      '  <executable>{sys}\cmd.exe</executable>' + #13#10 +
      '  <arguments>/C run_server.cmd</arguments>' + #13#10 +
      '  <logpath>' + SVWSDataDir + '\logs</logpath>' + #13#10 +
      '  <log mode="roll-by-size">' + #13#10 +
      '    <sizeThreshold>10240</sizeThreshold>' + #13#10 +
      '    <keepFiles>4</keepFiles>' + #13#10 +
      '  </log>' + #13#10 +
      '</service>' + #13#10
    ));
    if SaveStringToFile(ExpandConstant('{app}/svws_server_service.xml'), serviceConfig, false) then
      begin
        Log('  -> Fertig: Konfiguration für den SVWS-Java-Server-Dienst gespeichert.');
      end
    else
      begin
        Log('  -> Fehler beim Speichern der Konfiguration für den SVWS-Java-Server-Dienst!');
        // TODO error
      end;


    // Erzeuge den SVWS-Server-Java-Dienst, falls er nicht bereits vorhanden ist
    if Exec(ExpandConstant('{sys}\cmd.exe'), 
            '/C .\svws_server_service.exe install svws_server_service.xml', 
            ExpandConstant('{app}'),
            SW_HIDE, ewWaitUntilTerminated, ResultCode) then
      begin
        Log('  -> SVWS-Server-Java-Dienst erfolgreich eingerichtet. (ResultCode=' + IntToStr(ResultCode) + ')');
      end
    else
      begin
        Log('  -> SVWS-Server-Java-Dienst konnte nicht eingerichtet werden. (ResultCode=' + IntToStr(ResultCode) + ')');
        msgBoxResult := MsgBox('Fehler beim Erzeugen des SVWS-Server-Java-Dienst. Die Installation wird dennoch fortgesetzt. Sie müssen den SVWS-Server-Java-Dienst überprüfen und ggf. später manuell einrichten!', mbError, MB_OK);
      end;
  end;



{ Entfernt den Dienst für den SVWS-Java-Server }
procedure DestroyServiceSVWSServer;
  var
    ResultCode: Integer;
    msgBoxResult: Integer;
  begin
    // Erzeuge den SVWS-Server-Java-Dienst, falls er nicht bereits vorhanden ist
    if Exec(ExpandConstant('{sys}\cmd.exe'), 
            '/C .\svws_server_service.exe uninstall svws_server_service.xml', 
            ExpandConstant('{app}'),
            SW_HIDE, ewWaitUntilTerminated, ResultCode) then
      begin
        Log('  -> SVWS-Server-Java-Dienst erfolgreich entfernt. (ResultCode=' + IntToStr(ResultCode) + ')');
      end
    else
      begin
        Log('  -> SVWS-Server-Java-Dienst konnte nicht entfernt werden. (ResultCode=' + IntToStr(ResultCode) + ')');
        msgBoxResult := MsgBox('Fehler beim Entfernen des SVWS-Server-Java-Dienst. Sie müssen den SVWS-Server-Java-Dienst ggf. später manuell entfernen!', mbError, MB_OK);
      end;

    // Lösche die Konfiguration für den SVWS-Java-Server-Dienst
    if DeleteFile(ExpandConstant('{app}/svws_server_service.xml')) then
      begin
        Log('  -> Konfiguration für den SVWS-Java-Server-Dienst gelöscht.');
      end
    else
      begin
        Log('  -> Fehler beim Löschen der Konfiguration für den SVWS-Java-Server-Dienst!');
      end;
  end;



{ Passt den Classpath in run_server.cmd und für den Windows-Service so an, dass die Konfigurationsdatei für den Server auch in commmonappdata/svws-server gefunden wird. }
procedure AdjustClasspathSVWSServer();
  var
    codeAnsi: AnsiString;
    code: String;
    codeService: String;
    filename: String;
  begin
    Log('Ergänze ' + SVWSDataDir + '\res zum Java-Classpath des SVWS-Servers');
    filename := ExpandConstant('{app}/run_server.cmd');
    if LoadStringFromFile(filename, codeAnsi) then
      begin
        code := String(codeAnsi);
        codeService := code;
        Log('  -> Datei zum Ergänzen geladen:' + code);
        // Anpassungen für die Datei run_server.cmd
        if StringChangeEx(code, '--class-path ', ExpandConstant('--class-path ' + SVWSDataDir + '\res;'), false) = 1 then
          begin
            Log('  -> Ergänzung hinzugefügt');
            code := ExpandConstant('cd {app}') + #13#10 + code;
            codeAnsi := AnsiString(code);
            if SaveStringToFile(filename, codeAnsi, false) then
              begin
                Log('  -> Fertig: Datei wieder gespeichert.');
              end
            else
              begin
                Log('  -> Fehler beim Speichern der Datei!');
                // TODO error
              end;
          end
        else
          begin
            Log('  -> Die zu korrigierende Stelle in der Datei konnte nicht um den Pfad ergänzt werden!');
            // TODO error
          end;
      end
    else
      begin
        Log('  -> Datei konnte nicht gelesen werden!');
        // TODO error
      end;
  end;


{ Erzeugt eine neue SVWS-Server-Konfiguration. }
procedure CreateDefaultSVWSServerConfiguration;
  var
    ResultCode: Integer;
    msgBoxResult: Integer;
  begin
    // Prüfe zunächst, ob bereits eine Konfigurationsdatei vorliegt
    if FileExists(SVWSDataDir + '\res\svwsconfig.json') then
      begin
        // TODO Ergänze die Konfigurationsoptionen um einen Eintrag, so dass die Konfiguration gezielt überschrieben werden kann
        // ODER: Frage nach, ob diese ersetzt werden soll
        Log('  -> SVWS-Konfigurationsdatei exisitiert bereits. Sie wird nicht überschrieben!');
        Exit;
      end;

    if not ManualSVWSConfig then
      begin
        SVWSConfigDBHost := 'localhost';
        SVWSConfigDBPort := MariaDBPort;
        SVWSConfigDBMS := 'MARIA_DB';
      end;

    if Exec(ExpandConstant('cmd'), 
            '/C .\config_writer.cmd ' +
            '"' + SVWSDataDir + '/res/svwsconfig.json" ' + 
            '"' + SVWSDataDir + '/client" ' +
            '"' + SVWSDataDir + '/adminclient" ' +
            '"' + SVWSDataDir + '/logs" ' +
            '"' + SVWSDataDir + '/temp" ' +
            '"' + SVWSDataDir + '/res" ' +
            '"' + SVWSKeyStorePassword + '" ' +
            '"' + SVWSConfigDBHost + '" ' +
            '"' + IntToStr(SVWSConfigDBPort) + '" ', 
            ExpandConstant('{app}'),
            SW_HIDE, ewWaitUntilTerminated, ResultCode) then
      begin
        Log('  -> SVWS-Konfigurationsdatei erfolgreich geschrieben. (ResultCode=' + IntToStr(ResultCode) + ')');
      end
    else
      begin
        Log('  -> SVWS-Konfigurationsdatei konnte nicht geschrieben werden. (ResultCode=' + IntToStr(ResultCode) + ')');
        msgBoxResult := MsgBox('Fehler beim Erstellen der SVWS-Konfigurationsdatei. Die Installation wird dennoch fortgesetzt. Sie müssen die SVWS-Konfigurationsdatei später manuell anlegen!', mbError, MB_OK);
      end;
  end;


{ Erstelle einen neuen Keystore für den SVWS-Server zur Nutzung bei dem https-Zugriff }
procedure CreateDefaultKeystoreSelfSigned;
  var
    ResultCode: Integer;
    msgBoxResult: Integer;
  begin
    // Prüfe zunächst, ob ein Keystore bereits existiert
    if FileExists(SVWSDataDir + '\res\keystore') then
      begin
        // TODO Ergänze die Konfigurationsoptionen um einen Eintrag, so dass der Keystore gezielt überschrieben werden kann
        // ODER: Frage nach, ob dieser ersetzt werden soll
        Log('  -> SVWS-Keystore exisitiert bereits. Er wird nicht überschrieben!');
        Exit;
      end;

    // Erzeuge ggf. das Log-Verzeichnis, um das Fehler-Log beim Erzeugen eines keystore schreiben zu können
    if not DirExists(SVWSDataDir + '/logs') then
      begin
        Log('  -> Lege Log-Verzeichnis unter "' + SVWSDataDir + '/logs" an...');
        if CreateDir(SVWSDataDir + '/logs') then
          begin
            Log('    OK. Log-Verzeichnis angelegt.');
          end
        else
          begin
            Log('    Fehler beim Anlegen des Log-Verzeichnisses!');
          end;
      end;

    Log('  -> SVWS-Keystore wird erzeugt... (Datei="' + SVWSDataDir + '/res/keystore")');
    if Exec(ExpandConstant('cmd'), 
            '/C .\keytool.exe -genkey -alias selfsigned -keyalg RSA -keysize 8192 -keystore ' +
            '"' + SVWSDataDir + '/res/keystore" ' +
            '-validity 3650 -storepass "' + SVWSKeyStorePassword + '" ' +
            '-dname "CN=localhost,OU=Schule,O=' + ZertifikatSchulname + ',L=' + ZertifikatOrtsname + ',ST=NRW,C=DE" ' +
            '-ext "' + GetHostNames4Certificates(ZertifikatHostname) + '" ' +
            '> "' + SVWSDataDir + '/logs/keystore.log" 2>&1',
            ExpandConstant('{app}/java/bin'),
            SW_HIDE, ewWaitUntilTerminated, ResultCode) then
      begin
        Log('  -> SVWS-Keystore erfolgreich erzeugt. (Datei="' + SVWSDataDir + '/res/keystore"; ResultCode=' + IntToStr(ResultCode) + ')');
        if ResultCode = 0 then
          begin
            if Exec(ExpandConstant('cmd'), 
                    '/C .\keytool -export -keystore "' + SVWSDataDir + '/res/keystore" ' +
                        '-alias selfsigned -file ' + ExpandConstant('"{userdocs}/SVWS.cer"') + ' ' +
                        '-storepass "' + SVWSKeyStorePassword + '" ' + 
                        '>> "' + SVWSDataDir + '/logs/keystore.log" 2>&1',
                    ExpandConstant('{app}/java/bin'),
                    SW_HIDE, ewWaitUntilTerminated, ResultCode) then
              begin
                if ResultCode = 0 then
                  begin
                    Log('  -> SVWS-Zertifikat erfolgreich extrahiert. (Datei=' + ExpandConstant('"{userdocs}/SVWS.cer"') + '; ResultCode=' + IntToStr(ResultCode) + ')');
                  end
                else
                  begin
                    Log('  -> SVWS-Zertifikat konnte nicht extrahiert werden. (ResultCode=' + IntToStr(ResultCode) + ')');
                    msgBoxResult := MsgBox('Fehler beim Extrahieren des generierten Zertifikats. Die Installation wird dennoch fortgesetzt. Sie müssen das Zertifikat später ggf. manuell extrahieren!', mbError, MB_OK);
                  end;
              end
            else
              begin
                Log('  -> SVWS-Zertifikat konnte nicht extrahiert werden. (ResultCode=' + IntToStr(ResultCode) + ')');
                msgBoxResult := MsgBox('Fehler beim Extrahieren des generierten Zertifikats. Die Installation wird dennoch fortgesetzt. Sie müssen das Zertifikat später ggf. manuell extrahieren!', mbError, MB_OK);
              end;
          end
        else
          begin
            Log('  -> SVWS-Keystore konnte nicht erzeugt werden. (ResultCode=' + IntToStr(ResultCode) + ')');
            msgBoxResult := MsgBox('Fehler beim Erzeugen des SVWS-Keystores. Die Installation wird dennoch fortgesetzt. Sie müssen den SVWS-Keystore überprüfen und ggf. später manuell anlegen!', mbError, MB_OK);
          end;
      end
    else
      begin
        Log('  -> SVWS-Keystore konnte nicht erzeugt werden. (ResultCode=' + IntToStr(ResultCode) + ')');
        msgBoxResult := MsgBox('Fehler beim Erzeugen des SVWS-Keystores. Die Installation wird dennoch fortgesetzt. Sie müssen den SVWS-Keystore überprüfen und ggf. später manuell anlegen!', mbError, MB_OK);
      end;
  end;


{ Erstellt ein Schema. }
procedure CreateSVWSSchema;
  var
    ResultCode: Integer;
    msgBoxResult: Integer;
  begin
    // Prüfe, ob überhaupt ein Schema angelegt werden soll
    if not IsCreateSchema and not IsMigrateSchema then
      begin
        Exit;
      end;

    ProgressMemo.Lines.Append('Warte bis der SVWS-Server gestartet ist...');
    WizardForm.ProgressGauge.Position := 75 * WizardForm.ProgressGauge.Max div 100;

    // Warte darauf, dass der Server schon vollständig gestartet wurde...
    while not WaitTillPortUsed(443,100,100) do
      begin
        msgBoxResult := MsgBox('Fehler beim Erzeugen des Schemas. Das Warten auf den Start des SVWS-Servers dauert ungewöhnlich lange. ' +
                               'Sie können entweder weiter warten oder die Installation fortsetzen. Wird die Installation ohne Verfügbarkeit ' +
                               'des Servers fortgesetzt, so muss dann ein neues Schema dann später ggf. von Hand erzeugt werden. ' +
                               'Möchten Sie weiter auf den Start des Servers warten?', mbError, MB_YESNO);
        if msgBoxResult = IDNO then
          begin
            Log('  -> Schema konnte nicht angelegt werden.');
            Exit;
          end;
      end;

    ProgressMemo.Lines.Append('Server erreichbar!');
    WizardForm.ProgressGauge.Position := 80 * WizardForm.ProgressGauge.Max div 100;

    if IsCreateSchema then
      begin
        ProgressMemo.Lines.Append('Erzeuge ein neues Datenbankschema... (dies dauert mehrere Minuten!)');
        // TODO Prüfe, ob eine neue DB angelegt werden muss und frage ggf. nach (MsgBox)
        if Exec(ExpandConstant('{sys}\cmd.exe'), 
                '/C .\create_default_schema.cmd ' +
                '"root" ' +
                '"' + MariaDBRootPasswort + '" ' +
                '"' + SVWSDBSchema + '" ' +
                '"' + SVWSDBUser + '" ' +
                '"' + SVWSDBPassword + '" ', 
                ExpandConstant('{app}'),
                SW_HIDE, ewWaitUntilTerminated, ResultCode) then
          begin
            Log('  -> Schema erfolgreich angelegt. (ResultCode=' + IntToStr(ResultCode) + ')');
            ProgressMemo.Lines.Append('Datenbankschema wurde erstellt!');
            WizardForm.ProgressGauge.Position := 100 * WizardForm.ProgressGauge.Max div 100;
          end
        else
          begin
            Log('  -> Schema konnte nicht angelegt werden. (ResultCode=' + IntToStr(ResultCode) + ')');
            msgBoxResult := MsgBox('Fehler beim Erzeugen des Schema. Die Installation wird dennoch fortgesetzt. Ein neues Schema muss später ggf. von Hand erzeugt werden!', mbError, MB_OK);
          end;
      end
    else if IsMigrateSchema then
      begin
        DoMigration;
      end;
  end;


{ Ruft das Skript zum Erstellen der Firewall-Regeln auf. }
procedure AddSVWSServerFirewallRules();
  var
    ResultCode: Integer;
    msgBoxResult: Integer;
  begin
    Log('  -> Anlegen der Firewall-Regeln für den SVWS-Server (Freischalten von Port 443 für ein- und ausgehenden Traffic))');
    if Exec(ExpandConstant('{sys}\cmd.exe'), 
            '/C .\firewall_add.cmd', 
            ExpandConstant('{app}'),
            SW_HIDE, ewWaitUntilTerminated, ResultCode) then
      begin
        Log('  -> Firewall-Regeln für den SVWS-Server erfolgreich angelegt. (ResultCode=' + IntToStr(ResultCode) + ')');
      end
    else
      begin
        Log('  -> Firewall-Regeln für den SVWS-Server konnten nicht angelegt werden. (ResultCode=' + IntToStr(ResultCode) + ')');
        msgBoxResult := MsgBox('Fehler beim Erzeugen der Firewall-Regeln. Die Installation wird dennoch fortgesetzt. Die Regeln müssen später ggf. von Hand erzeugt werden!', mbError, MB_OK);
      end;
  end;


{ Ruft das Skript zum Entfernen der Firewall-Regeln auf. }
procedure RemoveSVWSServerFirewallRules();
  var
    ResultCode: Integer;
    msgBoxResult: Integer;
  begin
    Log('  -> Entfernen der Firewall-Regeln für den SVWS-Server (Freischalten von Port 443 für ein- und ausgehenden Traffic))');
    if Exec(ExpandConstant('{sys}\cmd.exe'), 
            '/C .\firewall_remove.cmd', 
            ExpandConstant('{app}'),
            SW_HIDE, ewWaitUntilTerminated, ResultCode) then
      begin
        Log('  -> Firewall-Regeln für den SVWS-Server erfolgreich entfernt. (ResultCode=' + IntToStr(ResultCode) + ')');
      end
    else
      begin
        Log('  -> Firewall-Regeln für den SVWS-Server konnten nicht entfernt werden. (ResultCode=' + IntToStr(ResultCode) + ')');
        msgBoxResult := MsgBox('Fehler beim Entfernen der Firewall-Regeln. Die Regeln müssen später ggf. von Hand entfernt werden!', mbError, MB_OK);
      end;
  end;


{ Wird zum Starten der Installation des SVWS-Servers aufgerufen. }
procedure StartSVWSServerInstall();
  begin
    Log('Starte die Installation des SVWS-Servers...');
    Log('- Version ' + SVWSNewVersion);
    if IsInstallJava then
      begin
        Log('Starte die Installation von Java...');
        Log('- Version ' + SVWSNewJavaVersion);
      end;
    Log('Starte die Installation des Windows-Service-Wrappers...');
    Log('- Version ' + SVWSNewWSWVersion);
    if IsInstallCurl then
      begin
        Log('Starte die Installation von Curl...');
        Log('- Version ' + SVWSNewCurlVersion);
      end;
  end;


{ Wird zum Beenden der Installation des SVWS-Servers aufgerufen. }
procedure FinishSVWSServerInstall();
  begin
    ProgressMemo.Lines.Append('Installiere SVWS-Server...');
    WizardForm.ProgressGauge.Position := 59 * WizardForm.ProgressGauge.Max div 100;
    if IsInstallJava then
      begin
        ProgressMemo.Lines.Append('Installiere Java...');
        Log('- Setze Java-Version in der Registry');
        RegWriteStringValue(HKLM, 'SOFTWARE\SVWSServer', 'JavaVersion', SVWSNewJavaVersion);
        Log('- FERTIG: Java auf Version ' + SVWSNewJavaVersion + ' aktualisiert!');
        ProgressMemo.Lines.Append('Java-Installation fertig!');
      end;
    WizardForm.ProgressGauge.Position := 60 * WizardForm.ProgressGauge.Max div 100;
    if IsInstallCurl then
      begin
        Log('- Setze Curl-Version in der Registry');
        RegWriteStringValue(HKLM, 'SOFTWARE\SVWSServer', 'CurlVersion', SVWSNewCurlVersion);
        Log('- FERTIG: Curl auf Version ' + SVWSNewCurlVersion + ' aktualisiert!');
      end;
    if IsInstallWSW then
      begin
        Log('- Setze die Version des Windows-Service-Wrappers in der Registry');
        RegWriteStringValue(HKLM, 'SOFTWARE\SVWSServer', 'WindowsServiceWrapperVersion', SVWSNewWSWVersion);
        Log('- FERTIG: Windows-Service-Wrapper auf Version ' + SVWSNewWSWVersion + ' aktualisiert!');
      end;
    Log('- Setze SVWS-Server-Version in der Registry');
    RegWriteStringValue(HKLM, 'SOFTWARE\SVWSServer', 'Version', SVWSNewVersion);

    // Erzeuge die SVWS-Konfigurationsdatei
    ProgressMemo.Lines.Append('-> Erzeuge die SVWS-Konfigurationsdatei...');
    WizardForm.ProgressGauge.Position := 61 * WizardForm.ProgressGauge.Max div 100;
    CreateDefaultSVWSServerConfiguration;

    // Erzeuge einen Keystore, falls dieser nicht bereits vorhanden ist
    ProgressMemo.Lines.Append('-> Erzeuge die Server-Zertifikatsdateien... (dies dauert eine Weile)');
    WizardForm.ProgressGauge.Position := 63 * WizardForm.ProgressGauge.Max div 100;
    CreateDefaultKeystoreSelfSigned;

    // Passe den Classpath-Pfad für das Starten des SVWS-Servers an, so dass die Konfigurationsdatei und der Keystore gefunden werden
    AdjustClasspathSVWSServer;

    // Erstelle ggf. den SVWS-Server-Java-Dienst
    ProgressMemo.Lines.Append('-> Erzeuge den SVWS-Server-Dienst...');
    WizardForm.ProgressGauge.Position := 72 * WizardForm.ProgressGauge.Max div 100;
    if IsInstallWSW then
      CreateServiceSVWSServer;

    // Füge die Firewall-Regeln hinzu
    AddSVWSServerFirewallRules;

    Log('- FERTIG: SVWS-Server auf Version ' + SVWSNewVersion + ' aktualisiert!');
    ProgressMemo.Lines.Append('SVWS-Server-Installation fertig!');
    WizardForm.ProgressGauge.Position := 75 * WizardForm.ProgressGauge.Max div 100;
  end;


{ Deinstalliere den SVWS-Server. Führe dabei die einzelnen Schritte in umgekehrter Reihenfolge durch. }
procedure UninstallSVWSServer();
  var
    KeyExists : Boolean;
  begin
    if IsUninstallSVWSServer then
      begin
        Log('Deinstalliere den SVWS-Server...');
        // Stoppe ggf. den SVWS-Server-Service
        Log('- Stoppe den SVWS-Server-Dienst');
        StopServiceSVWSServer;

        // Warte darauf, dass der SVWS-Server-Dienst schon vollständig beendet wurde...
        if WaitTillPortFree(443,30,100) then
          begin
            Log('  - SVWS-Server-Dienst beendet');
          end
        else
          begin
            Log('  - SVWS-Server-Dienst wurde nicht beendet');
          end;

        // Entferne die Firewall-Regeln sofern sie definiert sind.
        Log('- Entferne die Firewall-Regeln');
        RemoveSVWSServerFirewallRules; 

        // Entferne den SVWS-Server-Java-Dienst
        Log('- Entferne den SVWS-Server-Dienst');
        DestroyServiceSVWSServer;

        // Delete Registry Entries
        Log('- Entferne die SVWS-Server-Version aus der Registry');
        KeyExists := RegDeleteValue(HKLM, 'SOFTWARE\SVWSServer', 'Version');
        Log('- Entferne die Version des Windows-Service-Wrappers aus der Registry');
        KeyExists := RegDeleteValue(HKLM, 'SOFTWARE\SVWSServer', 'WindowsServiceWrapperVersion');
        Log('- Entferne die Curl-Version aus der Registry');
        KeyExists := RegDeleteValue(HKLM, 'SOFTWARE\SVWSServer', 'CurlVersion');
      end;
    if IsUninstallJava then
      begin
        Log('Deinstalliere Java...');

        // Delete Registry Entries
        Log('- Entferne die Java-Version aus der Registry');
        KeyExists := RegDeleteValue(HKLM, 'SOFTWARE\SVWSServer', 'JavaVersion');
      end;
  end;