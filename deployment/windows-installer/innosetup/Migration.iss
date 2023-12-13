var
  Schild2AccessPassword: String;
  QueryAccessPassword: Boolean;
  MigrationPage: TInputFileWizardPage;
  ComboboxDBMS: TNewComboBox;
  StaticTextMDBPasswd: TNewStaticText;
  EditMDBPasswd: TNewEdit;
  StaticTextSrcUsername: TNewStaticText;
  EditSrcUsername: TNewEdit;
  StaticTextSrcPassword: TNewStaticText;
  EditSrcPassword: TNewEdit;
  StaticTextSrcLocation: TNewStaticText;
  EditSrcLocation: TNewEdit;
  StaticTextSrcSchema: TNewStaticText;
  EditSrcSchema: TNewEdit;
  MigrationsOption: Integer;
  MDBFileLocation: String;
  MDBPassword: String;
  SrcUsername: String;
  SrcPassword: String;
  SrcLocation: String;
  SrcSchema: String;


{ Wird aufgerufen, sobald ein}
procedure ComboboxDBMSOnChange(Sender: TObject);
  begin
    // Zeige File Selection nur bei Option 0 (Access MDB)
    MigrationPage.PromptLabels[0].Enabled := (ComboboxDBMS.ItemIndex = 0);
    MigrationPage.PromptLabels[0].Visible := (ComboboxDBMS.ItemIndex = 0);
    MigrationPage.Edits[0].Enabled := (ComboboxDBMS.ItemIndex = 0);
    MigrationPage.Edits[0].Visible := (ComboboxDBMS.ItemIndex = 0);
    MigrationPage.Buttons[0].Enabled := (ComboboxDBMS.ItemIndex = 0);
    MigrationPage.Buttons[0].Visible := (ComboboxDBMS.ItemIndex = 0);
    StaticTextMDBPasswd.Enabled := (ComboboxDBMS.ItemIndex = 0);
    StaticTextMDBPasswd.Visible := (ComboboxDBMS.ItemIndex = 0) AND QueryAccessPassword;
    EditMDBPasswd.Enabled := (ComboboxDBMS.ItemIndex = 0);
    EditMDBPasswd.Visible := (ComboboxDBMS.ItemIndex = 0) AND QueryAccessPassword;

    StaticTextSrcUsername.Enabled := (ComboboxDBMS.ItemIndex <> 0);
    StaticTextSrcUsername.Visible := (ComboboxDBMS.ItemIndex <> 0);
    EditSrcUsername.Enabled := (ComboboxDBMS.ItemIndex <> 0);
    EditSrcUsername.Visible := (ComboboxDBMS.ItemIndex <> 0);
    StaticTextSrcPassword.Enabled := (ComboboxDBMS.ItemIndex <> 0);
    StaticTextSrcPassword.Visible := (ComboboxDBMS.ItemIndex <> 0);
    EditSrcPassword.Enabled := (ComboboxDBMS.ItemIndex <> 0);
    EditSrcPassword.Visible := (ComboboxDBMS.ItemIndex <> 0);
    StaticTextSrcLocation.Enabled := (ComboboxDBMS.ItemIndex <> 0);
    StaticTextSrcLocation.Visible := (ComboboxDBMS.ItemIndex <> 0);
    EditSrcLocation.Enabled := (ComboboxDBMS.ItemIndex <> 0);
    EditSrcLocation.Visible := (ComboboxDBMS.ItemIndex <> 0);
    StaticTextSrcSchema.Enabled := (ComboboxDBMS.ItemIndex <> 0);
    StaticTextSrcSchema.Visible := (ComboboxDBMS.ItemIndex <> 0);
    EditSrcSchema.Enabled := (ComboboxDBMS.ItemIndex <> 0);
    EditSrcSchema.Visible := (ComboboxDBMS.ItemIndex <> 0);
  end;


{ Initialisiert den Bereich für die SVWS-Server-Konfiguration
  @param Page   die vorherige Konfigurationsseite 
}
procedure InitializeMigrationPage(Page: TWizardPage);
  begin
    Schild2AccessPassword := '@schild2_access_password@';
    QueryAccessPassword := (Schild2AccessPassword = '');
    MigrationPage := CreateInputFilePage(Page.ID, 'Migration', 'Migration einer Schild-Datenbank der Version 2.x', '');

    ComboboxDBMS := TNewComboBox.Create(MigrationPage);
    with ComboboxDBMS do
      begin
        Parent := MigrationPage.Surface;
        Left := ScaleX(0);
        Top := ScaleY(8);
        Width := Page.SurfaceWidth div 3 - ScaleX(8);
        Items.Add('Access MDB');
        Items.Add('MariaDB');
        Items.Add('MySQL');
        Items.Add('Microsoft SQL Server');
        ItemIndex := 0;
        Enabled := True;
        OnChange := @ComboboxDBMSOnChange;
      end;    

    with MigrationPage do
      begin
        Add('Wählen Sie die Schild2-Access-Datenbank aus:', 'MDB|*.mdb', '*.mdb');
        if FileExists('C:\SchILD-NRW\DB\SchILD2000n.mdb') then
          Values[0] := 'C:\SchILD-NRW\DB\SchILD2000n.mdb'
        else
          Values[0] := ExpandConstant('{userdocs}') + '\SchILD2000n.mdb';
        Edits[0].Top := Edits[0].Top + ComboboxDBMS.Top;
        Buttons[0].Top := Buttons[0].Top + ComboboxDBMS.Top;
        PromptLabels[0].Top := PromptLabels[0].Top + ComboboxDBMS.Top;
        Edits[0].Enabled := True;
        Buttons[0].Enabled := True;
        PromptLabels[0].Enabled := True;
      end;

    StaticTextMDBPasswd := TNewStaticText.Create(MigrationPage);
    with StaticTextMDBPasswd do
      begin
        Left := ScaleX(0);
        Top := MigrationPage.Edits[0].Top + MigrationPage.Edits[0].Height + ScaleY(8);
        AutoSize := True;
        Caption := 'MDB-Kennwort';
        Parent := MigrationPage.Surface;
        Enabled := True;
        Visible := QueryAccessPassword;
      end;

    EditMDBPasswd := TNewEdit.Create(MigrationPage);
    with EditMDBPasswd do
      begin
        Left := StaticTextMDBPasswd.Left + StaticTextMDBPasswd.Width + ScaleX(3);
        Top := StaticTextMDBPasswd.Top - ScaleY(2);
        Width := Page.SurfaceWidth div 2 - ScaleX(8);
        Text := '';
        Parent := MigrationPage.Surface;
        Enabled := True;        
        Visible := QueryAccessPassword;
      end;

    StaticTextSrcUsername := TNewStaticText.Create(MigrationPage);
    with StaticTextSrcUsername do
      begin
        Left := ScaleX(0);
        Top := ComboboxDBMS.Top + ComboboxDBMS.Height + ScaleY(8);
        AutoSize := True;
        Caption := 'Benutzername';
        Parent := MigrationPage.Surface;
        Enabled := False;
        Visible := False;
      end;

    EditSrcUsername := TNewEdit.Create(MigrationPage);
    with EditSrcUsername do
      begin
        Left := StaticTextSrcUsername.Left + StaticTextSrcUsername.Width + ScaleX(3);
        Top := StaticTextSrcUsername.Top - ScaleY(2);
        Width := Page.SurfaceWidth div 2 - ScaleX(8);
        Text := '';
        Parent := MigrationPage.Surface;
        Enabled := False;
        Visible := False;
      end;

    StaticTextSrcPassword := TNewStaticText.Create(MigrationPage);
    with StaticTextSrcPassword do
      begin
        Left := ScaleX(0);
        Top := EditSrcUsername.Top + EditSrcUsername.Height + ScaleY(8);
        AutoSize := True;
        Caption := 'Kennwort';
        Parent := MigrationPage.Surface;
        Enabled := False;
        Visible := False;
      end;

    EditSrcPassword := TNewEdit.Create(MigrationPage);
    with EditSrcPassword do
      begin
        Left := StaticTextSrcPassword.Left + StaticTextSrcPassword.Width + ScaleX(3);
        Top := StaticTextSrcPassword.Top - ScaleY(2);
        Width := Page.SurfaceWidth div 2 - ScaleX(8);
        Text := '';
        Parent := MigrationPage.Surface;
        Enabled := False;
        Visible := False;
      end;

    StaticTextSrcLocation := TNewStaticText.Create(MigrationPage);
    with StaticTextSrcLocation do
      begin
        Left := ScaleX(0);
        Top := EditSrcPassword.Top + EditSrcPassword.Height + ScaleY(8);
        AutoSize := True;
        Caption := 'Ort der Datenbank';
        Parent := MigrationPage.Surface;
        Enabled := False;
        Visible := False;
      end;

    EditSrcLocation := TNewEdit.Create(MigrationPage);
    with EditSrcLocation do
      begin
        Left := StaticTextSrcLocation.Left + StaticTextSrcLocation.Width + ScaleX(3);
        Top := StaticTextSrcLocation.Top - ScaleY(2);
        Width := Page.SurfaceWidth div 2 - ScaleX(8);
        Text := 'localhost';
        Parent := MigrationPage.Surface;
        Enabled := False;
        Visible := False;
      end;

    StaticTextSrcSchema := TNewStaticText.Create(MigrationPage);
    with StaticTextSrcSchema do
      begin
        Left := ScaleX(0);
        Top := EditSrcLocation.Top + EditSrcLocation.Height + ScaleY(8);
        AutoSize := True;
        Caption := 'Schema';
        Parent := MigrationPage.Surface;
        Enabled := False;
        Visible := False;
      end;

    EditSrcSchema := TNewEdit.Create(MigrationPage);
    with EditSrcSchema do
      begin
        Left := StaticTextSrcSchema.Left + StaticTextSrcSchema.Width + ScaleX(3);
        Top := StaticTextSrcSchema.Top - ScaleY(2);
        Width := Page.SurfaceWidth div 2 - ScaleX(8);
        Text := 'schild_nrw';
        Parent := MigrationPage.Surface;
        Enabled := False;
        Visible := False;
      end;

  end;
  

function CheckMigrationConfigurationValues(CurPageID: Integer) : Boolean;
  begin
    if CurPageID = MigrationPage.ID then
      begin
        MigrationsOption := ComboboxDBMS.ItemIndex;
        if MigrationsOption = 0 then
          begin
            MDBFileLocation := MigrationPage.Values[0];
            if not FileExists(MDBFileLocation) then
              begin
                MsgBox('Die angegebene Schild2-Datenbank existiert nicht. Wählen Sie eine gültige Datenbank aus!', mbInformation, mb_Ok);
                result := False;
                Exit;
              end;
            if QueryAccessPassword then
                MDBPassword := EditMDBPasswd.Text
            else
                MDBPassword := Schild2AccessPassword;
            if MDBPassword = '' then
              begin
                MsgBox('Es muss ein gültiges Kennwort für die Access-Datenbank angegeben werden bevor die Installation fortgesetzt werden kann.', mbInformation, mb_Ok);
                result := False;
                Exit;
              end;
          end
        else
          begin
            SrcUsername := EditSrcUsername.Text;
            if SrcUsername = '' then
              begin
                MsgBox('Es muss ein gültiger Benutzername für die Datenbank angegeben werden bevor die Installation fortgesetzt werden kann.', mbInformation, mb_Ok);
                result := False;
                Exit;
              end;
            SrcPassword := EditSrcPassword.Text;
            if SrcPassword = '' then
              begin
                MsgBox('Es muss ein gültiges Kennwort für die Datenbank angegeben werden bevor die Installation fortgesetzt werden kann.', mbInformation, mb_Ok);
                result := False;
                Exit;
              end;
            SrcLocation := EditSrcLocation.Text;
            if SrcLocation = '' then
              begin
                MsgBox('Es muss ein gültiger Ort für die Datenbank angegeben werden bevor die Installation fortgesetzt werden kann.', mbInformation, mb_Ok);
                result := False;
                Exit;
              end;
            StringChangeEx(SrcLocation, '\', '\\', True);
            SrcSchema := EditSrcSchema.Text;
            if SrcSchema = '' then
              begin
                MsgBox('Es muss ein gültiges Schema für die Datenbank angegeben werden bevor die Installation fortgesetzt werden kann.', mbInformation, mb_Ok);
                result := False;
                Exit;
              end;            
          end;
      end;
   result := true;
 end;


procedure DoMigration;
  var 
    ResultCode: Integer;
    msgBoxResult: Integer;
    DBRootUsername: String;
  begin
    ProgressMemo.Lines.Append('Migriere Datenbank... (dies dauert mehrere Minuten!)');
    WizardForm.ProgressGauge.Position := 80 * WizardForm.ProgressGauge.Max div 100;
    DBRootUsername := 'root';
    if ManualSVWSConfig and (CompareStr(SVWSConfigDBMS, 'MSSQL') = 0) then
      begin
        DBRootUsername := 'sa';
      end;
    case MigrationsOption of
      0: // Access MDB
        begin
          Log('  -> Migriere Datenbank aus ' + MDBFileLocation);
          if Exec(ExpandConstant('cmd'), 
                  '/C .\curl.exe --user "' + DBRootUsername + ':' + MariaDBRootPasswort + '" ' +
                  '-k -X POST "https://localhost/api/schema/root/migrate/mdb/' + SVWSDBSchema + '" ' +
                  '-H  "accept: application/json" ' +
                  '-H  "Content-Type: multipart/form-data" ' +
                  '-F "databasePassword=' + MDBPassword + '" ' +
                  '-F "schemaUsername=' + SVWSDBUser + '" ' +
                  '-F "schemaUserPassword=' + SVWSDBPassword + '" ' +
                  '-F "database=@' + MDBFileLocation + '"',
                  ExpandConstant('{app}/curl/bin'),
                  SW_HIDE, ewWaitUntilTerminated, ResultCode) then
            begin
              Log('  -> Schema durch Migration erfolgreich angelegt. (ResultCode=' + IntToStr(ResultCode) + ')');
              ProgressMemo.Lines.Append('Migration beendet!');
              WizardForm.ProgressGauge.Position := 100 * WizardForm.ProgressGauge.Max div 100;
            end
          else
            begin
              Log('  -> Schema durch Migration konnte nicht angelegt werden. (ResultCode=' + IntToStr(ResultCode) + ')');
              msgBoxResult := MsgBox('Fehler beim Migrieren des Schema. Die Installation wird dennoch fortgesetzt. Ein neues Schema muss später ggf. von Hand erzeugt werden!', mbError, MB_OK);
            end;
        end;
      1: // MariaDB
        begin                                      
          Log('  -> Migriere Datenbank aus ' + SrcLocation + '/' + SrcSchema);
          if Exec(ExpandConstant('cmd'), 
                  '/C .\curl.exe --user "' + DBRootUsername + ':' + MariaDBRootPasswort + '" ' +
                  '-k -X POST "https://localhost/api/schema/root/migrate/mariadb/' + SVWSDBSchema + '" ' +
                  '-H  "accept: application/json" ' +
                  '-H  "Content-Type: application/json" ' +
                  '-d "{\"srcUsername\":\"' + SrcUsername+ '\",\"srcPassword\":\"' + SrcPassword+ '\",\"srcLocation\":\"' + SrcLocation + '\",\"srcSchema\":\"' + SrcSchema + '\",' +
                      '\"schemaUsername\":\"' + SVWSDBUser + '\",\"schemaUserPassword\":\"' + SVWSDBPassword + '\"}"',
                  ExpandConstant('{app}/curl/bin'),
                  SW_HIDE, ewWaitUntilTerminated, ResultCode) then
            begin
              Log('  -> Schema durch Migration erfolgreich angelegt. (ResultCode=' + IntToStr(ResultCode) + ')');
              ProgressMemo.Lines.Append('Migration beendet!');
              WizardForm.ProgressGauge.Position := 100 * WizardForm.ProgressGauge.Max div 100;
            end
          else
            begin
              Log('  -> Schema durch Migration konnte nicht angelegt werden. (ResultCode=' + IntToStr(ResultCode) + ')');
              msgBoxResult := MsgBox('Fehler beim Migrieren des Schema. Die Installation wird dennoch fortgesetzt. Ein neues Schema muss später ggf. von Hand erzeugt werden!', mbError, MB_OK);
            end;
        end;
      2: // MySQL
        begin
          Log('  -> Migriere Datenbank aus ' + SrcLocation + '/' + SrcSchema);
          if Exec(ExpandConstant('cmd'), 
                  '/C .\curl.exe --user "' + DBRootUsername + ':' + MariaDBRootPasswort + '" ' +
                  '-k -X POST "https://localhost/api/schema/root/migrate/mysql/' + SVWSDBSchema + '" ' +
                  '-H  "accept: application/json" ' +
                  '-H  "Content-Type: application/json" ' +
                  '-d "{\"srcUsername\":\"' + SrcUsername+ '\",\"srcPassword\":\"' + SrcPassword+ '\",\"srcLocation\":\"' + SrcLocation + '\",\"srcSchema\":\"' + SrcSchema + '\",' +
                      '\"schemaUsername\":\"' + SVWSDBUser + '\",\"schemaUserPassword\":\"' + SVWSDBPassword + '\"}"',
                  ExpandConstant('{app}/curl/bin'),
                  SW_HIDE, ewWaitUntilTerminated, ResultCode) then
            begin
              Log('  -> Schema durch Migration erfolgreich angelegt. (ResultCode=' + IntToStr(ResultCode) + ')');
              ProgressMemo.Lines.Append('Migration beendet!');
              WizardForm.ProgressGauge.Position := 100 * WizardForm.ProgressGauge.Max div 100;
            end
          else
            begin
              Log('  -> Schema durch Migration konnte nicht angelegt werden. (ResultCode=' + IntToStr(ResultCode) + ')');
              msgBoxResult := MsgBox('Fehler beim Migrieren des Schema. Die Installation wird dennoch fortgesetzt. Ein neues Schema muss später ggf. von Hand erzeugt werden!', mbError, MB_OK);
            end;
        end;
      3: // SQL Server
        begin
          Log('  -> Migriere Datenbank aus ' + SrcLocation + '/' + SrcSchema);
          if Exec(ExpandConstant('cmd'), 
                  '/C .\curl.exe --user "' + DBRootUsername + ':' + MariaDBRootPasswort + '" ' +
                  '-k -X POST "https://localhost/api/schema/root/migrate/mssql/' + SVWSDBSchema + '" ' +
                  '-H  "accept: application/json" ' +
                  '-H  "Content-Type: application/json" ' +
                  '-d "{\"srcUsername\":\"' + SrcUsername+ '\",\"srcPassword\":\"' + SrcPassword+ '\",\"srcLocation\":\"' + SrcLocation + '\",\"srcSchema\":\"' + SrcSchema + '\",' +
                      '\"schemaUsername\":\"' + SVWSDBUser + '\",\"schemaUserPassword\":\"' + SVWSDBPassword + '\"}"',
                  ExpandConstant('{app}/curl/bin'),
                  SW_HIDE, ewWaitUntilTerminated, ResultCode) then
            begin
              Log('  -> Schema durch Migration erfolgreich angelegt. (ResultCode=' + IntToStr(ResultCode) + ')');
              ProgressMemo.Lines.Append('Migration beendet!');
              WizardForm.ProgressGauge.Position := 100 * WizardForm.ProgressGauge.Max div 100;
            end
          else
            begin
              Log('  -> Schema durch Migration konnte nicht angelegt werden. (ResultCode=' + IntToStr(ResultCode) + ')');
              msgBoxResult := MsgBox('Fehler beim Migrieren des Schema. Die Installation wird dennoch fortgesetzt. Ein neues Schema muss später ggf. von Hand erzeugt werden!', mbError, MB_OK);
            end;
        end;
    end;    
  end;
