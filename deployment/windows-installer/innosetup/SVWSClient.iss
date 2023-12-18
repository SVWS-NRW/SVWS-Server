var
  CheckBoxInstallSVWSClient: TNewCheckBox;


{ Wird aufgerufen, wenn der Zustand der Checkbox zur Installation des SVWS-Clients ge�ndert wird. }
procedure CheckBoxInstallSVWSClientOnClick(Sender: TObject);
  begin
    InstallSVWSClient := CheckBoxInstallSVWSClient.Checked;
  end;


{ Initialisiert den Bereich f�r die SVWS-Client-Konfiguration
  @param Page   die Konfigurationsseite 
  @param Top    die Position in y-Richtung ab der der Bereich gezeichnet wird }
procedure InitializeSVWSClientConfigurationSection(Page: TWizardPage; Top: Integer);
  begin
    CheckBoxInstallSVWSClient := TNewCheckBox.Create(Page);
    CheckBoxInstallSVWSClient.Left := ScaleX(0);
    CheckBoxInstallSVWSClient.Top := Top;
    CheckBoxInstallSVWSClient.Width := Page.SurfaceWidth;
    CheckBoxInstallSVWSClient.Height := ScaleY(17);
    CheckBoxInstallSVWSClient.Caption := ' Installiere SVWS-Client @svws_client_version@';
    if CompareStr(SVWSExistingClientVersion, '') <> 0 then
      begin
        CheckBoxInstallSVWSClient.Caption := CheckBoxInstallSVWSClient.Caption + ' (bereits installiert: ' + SVWSExistingClientVersion + ')';
      end;
    CheckBoxInstallSVWSClient.Checked := InstallSVWSClient;
    CheckBoxInstallSVWSClient.Parent := Page.Surface;
    CheckBoxInstallSVWSClient.OnClick := @CheckBoxInstallSVWSClientOnClick;

    // TODO
  end;


{ Gibt das untere Ende f�r den Bereich der SVWS-Client-Konfiguration zur�ck
  @return das untere Ende des Konfigurationsbereichs }
function GetSVWSClientConfigurationSectionBottom : Integer;
  begin
    result := CheckBoxInstallSVWSClient.Top + CheckBoxInstallSVWSClient.Height + ScaleY(8);
  end;



{ Pr�ft, ob die Eintragungen im Bereich der SVWS-Client-Konfiguration korrekt sind 
  @param CurPageID   die Page-ID der Konfigurationsseite, f�r die der Check durchgef�hrt wird.
  @return True, falls die Pr�fung erfolgreich war, sonst False }
function CheckSVWSClientConfigurationSectionValues(CurPageID: Integer) : Boolean;
  begin
    result := True;
  end;


{ Wird zum Beenden der Installation des SVWS-Clients aufgerufen. }
procedure FinishSVWSClientInstall();
  begin
    Log('- Setze SVWS-Client-Version in der Registry');
    RegWriteStringValue(HKLM, 'SOFTWARE\SVWSServer', 'SVWSClientVersion', SVWSNewClientVersion);
  end;


{ Deinstalliere den SVWS-Client. F�hre dabei die einzelnen Schritte der Installation in umgekehrter Reihenfolge durch. }
procedure UninstallSVWSClient;
  var
    KeyExists : Boolean;
  begin
    if IsUninstallSVWSClient then
      begin
        Log('Deinstalliere SVWSClient...');

        // Delete Registry Entries
        Log('- Entferne die SVWS-Client-Version aus der Registry');
        KeyExists := RegDeleteValue(HKLM, 'SOFTWARE\SVWSServer', 'SVWSClientVersion');
      end;
  end;