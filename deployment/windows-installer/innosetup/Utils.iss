{ Prüft, ob der angebene Hostname für ein Zertifkat nur gültige Zeichen beinhaltet,
  d.h. Ziffern, Buchstaben und ein Bindestrich
  @param name   der zu prüfende Name
  @return true, falls nur gültige Zeichen enthalten sind und ansonsten false
}
function CheckCertificateHostname(name : String): Boolean;
  var i : Integer;
  begin
    result := true;
    for i := 1 to Length(name) do
      begin
        case name[i] of 
          '0'..'9', 'A'..'Z', 'a'..'z', '-':
            ;
          else
            begin
              result := false;
              Break;
            end;
        end;
      end;
  end;

{ Ermittelt die drei Integer-Werte einer dreistelligen Versionsnummer 
  @param v   die Versionsnummer als String
  @return ein Array mit den drei Teilen der Version }
function GetVersionFromString(v: String) : array[0..2] of Integer;
  var p: Integer;
      tmp: String;
  begin
    p := Pos('.', v);
    result[0] := StrToIntDef(Copy(v, 1, p-1), -1);
    tmp := Copy(v, p+1, Length(v) - p);
    p := Pos('.', tmp);
    result[1] := StrToIntDef(Copy(tmp, 1, p-1), -1);
    result[2] := StrToIntDef(Copy(tmp, p+1, Length(tmp) - p), -1);
  end;

{ Vergleicht die beiden als String übergebenen Versionen miteinander und gibt
  zurück, welche größer (>0) bzw. kleiner (<0) ist oder ob sie identisch sind (=0)
  @param a   die erste Version
  @param b   die zweite Version
  @return <0, falls a < b, >0, falls a > b und =0, falls a = b}
function CompareVersions(a, b: String) : Integer;
  var versionA, versionB: array of Integer;
      tmp: Integer;
  begin
    versionA := GetVersionFromString(a);
    versionB := GetVersionFromString(b);
    tmp := versionA[0] - versionB[0];
    if tmp = 0 then
      begin
        tmp := versionA[1] - versionB[1];
        if tmp = 0 then
          tmp := versionA[2] - versionB[2];
      end;
    result := tmp;
  end;


{ Ermittelt den vollständigen Domainnamen mithilfe der Umgebungsvariable 
  'UserDnsDomain' und gibt diesen zurück. Ist die Variable nicht gesetzt, so 
  wird ein leerer String zurückgegeben.
  @return der vollständige Domainname oder ein leerer String }
function GetFullHostname(): String;
  var DNSDomain: String;
      HostName: String;
  begin
    DNSDomain := GetEnv('UserDnsDomain');
    HostName := GetComputerNameString;
    if (DNSDomain = '') OR (HostName = '') then
      begin
        result := '';
        Exit;
      end;
    result := HostName + '.' + DNSDomain;
  end;


{ Ermittelt die Hostnamen unter denen der Host im Netzwerk 
  bekannt ist und liefert diese in einem SAN-String (ServerAlternateName) zurück. 
  @param ManualHostname   ein manuell eingegebener Hostname, der auch eingetragen werden soll
  @return der SAN-String }
function GetHostNames4Certificates(ManualHostname: String) : String;
  var HostName: String;
  begin
    result := 'SAN=DNS:localhost,IP:127.0.0.1';
    HostName := GetComputerNameString;
    if HostName <> '' then
      result := result + ',DNS:' + HostName;
    HostName := GetFullHostname;
    if HostName <> '' then
      result := result + ',DNS:' + HostName;
    if ManualHostname <> '' then
      result := result + ',DNS:' + ManualHostname;
  end;


{ Prüft mit netstat, ob das angegebene Port derzeit genutzt wird oder nicht.
  @param Port   der zu prüfende Port
  @return true, falls das Port genutzt wird, sonst false }
function IsPortUsed(Port: Integer): Boolean;
  var
    ResultCode: Integer;
  begin
    Log('Prüfe, ob Port ' + IntToStr(Port) + ' in Nutzung ist...');
    if not FileExists(ExpandConstant('{tmp}/check_port.cmd')) then 
      begin
        if not SaveStringToFile(ExpandConstant('{tmp}/check_port.cmd'), '@netstat -p TCP -an | findstr "0.0:%1" >nul 2>&1', True) then
          begin
            Log('  -> Prüfung fehlgeschlagen. (Fehler beim Anlegen des Batch-Skriptes)');
            result := False;
            Exit;
          end;
      end;
    Exec(ExpandConstant('cmd'), 
         ExpandConstant('/C {tmp}/check_port.cmd ' + IntToStr(Port)), 
         ExpandConstant('{tmp}'),
         SW_HIDE, ewWaitUntilTerminated, ResultCode);
    if ResultCode = 0 then
      begin
        Log('  -> Port wird derzeit genutzt.');
        result := True;
      end
    else
      begin
        Log('  -> Port ist ungenutzt.');
        result := False;
      end;
  end;


{ Wartet darauf, dass das angegebene Port genutzt wird. Es finden soviele Prüfungen
  statt, wie in dem Parameter Retries angegeben sind. Zwischen den Prüfungen wird solange pausiert,
  wir in dem Parameter IntervalMS angebeben ist. Wurden alle Prüfungen durchgeführt und das Port 
  wurde noch nicht genutzt, so wird false zurückgegeben. Ansonsten wird true zurückgegeben.
  @param Port   das zu prüfende Port
  @param Retries   die max. Anzahl durchzuführender Prüfungen
  @param IntervalMS   das Zeitintervall zum Warten zwischen zwei Prüfungen
  @return true, sobald das Port genutzt ist, und false, falls alle Prüfungen fehlschlagen
}
function WaitTillPortUsed(Port: Integer; Retries: Integer; IntervalMS: Integer): Boolean;
  var
    i: Integer;
  begin
    for i := 1 to Retries do
      begin
        if IsPortUsed(Port) then
          begin
            result := True;
            Exit;
          end;
        Sleep(IntervalMS);
      end;
    result := False;
  end;


{ Wartet darauf, dass das angegebene Port frei wird. Es finden soviele Prüfungen
  statt, wie in dem Parameter Retries angegeben sind. Zwischen den Prüfungen wird solange pausiert,
  wir in dem Parameter IntervalMS angebeben ist. Wurden alle Prüfungen durchgeführt und das Port 
  ist noch nicht frei, so wird false zurückgegeben. Ansonsten wird true zurückgegeben.
  @param Port   das zu prüfende Port
  @param Retries   die max. Anzahl durchzuführender Prüfungen
  @param IntervalMS   das Zeitintervall zum Warten zwischen zwei Prüfungen
  @return true, sobald das Port frei ist, und false, falls alle Prüfungen fehlschlagen
}
function WaitTillPortFree(Port: Integer; Retries: Integer; IntervalMS: Integer): Boolean;
  var
    i: Integer;
  begin
    for i := 1 to Retries do
      begin
        if not IsPortUsed(Port) then
          begin
            result := True;
            Exit;
          end;
        Sleep(IntervalMS);
      end;
    result := False;
  end;


{ Konstanten, die bei der Generierung eines zufälligen Kennwortes genutzt werden }
const
  PasswordChars_Numbers = '0123456789';
  PasswordChars_Lowercase = 'abcdefghijklmnopqrstuvwxyz';
  PasswordChars_Uppercase = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
  PasswordChars_Special = '+-*!?$%@=#';
  PasswordChars_All = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+-*!?$%@=#';

{ Erstellt ein neues Passwort mit der angegebenen Länge.
  @param Length   die Länge des neuen Passwortes
  @return das generierte Passwort mit der angegebenen Länge }
function CreateRandomPassword(Length: Integer): String;
  var
   i: Integer;
  begin
    result := '';
    for i := 1 to Length-4 do
      result := result + Copy(PasswordChars_All, Random(72) + 1, 1);
    Insert(Copy(PasswordChars_Numbers, Random(10) + 1, 1), result, Random(Length-4) + 1);
    Insert(Copy(PasswordChars_Lowercase, Random(26) + 1, 1), result, Random(Length-3) + 1);
    Insert(Copy(PasswordChars_Uppercase, Random(26) + 1, 1), result, Random(Length-2) + 1);
    Insert(Copy(PasswordChars_Special, Random(10) + 1, 1), result, Random(Length-1) + 1);
  end;


{ Deaktiviert das vererben von ACL-Informationen an diesen Ordner von übergeordneten Ordnern }
procedure DisableACLInheritance(FolderWithConstants: String);
  var
    Folder: String;
    ResultCode: Integer;
  begin
    Folder := ExpandConstant(FolderWithConstants);
    Log('Entferne die Vererbung von Rechten auf den Ordner "' + Folder + '"...');
    if Exec(ExpandConstant('.\icacls.exe'), 
            '"' + Folder + '" /inheritance:r', 
            ExpandConstant('{sys}'),
            SW_HIDE, ewWaitUntilTerminated, ResultCode) then
      Log('  -> Vererbung der Rechte durch den übergeordneten Ordner deaktiviert.')
    else
      Log('  -> Fehler: Vererbung der Rechte durch den übergeordneten Ordner konnte nicht deaktiviert werden!');
  end;


{ Fügt die expliziten ACls dem angegebenen Ordner hinzu }
procedure AddExplicitACLs(FolderWithConstants: String; setService: String; setSystem: Boolean; setAdmins: Boolean; setNetworkService: Boolean);
  var
    Folder: String;
    GrantString: String;
    ResultCode: Integer;
  begin
    Folder := ExpandConstant(FolderWithConstants);
    Log('Entferne die Rechte durch explizite ACLs auf dem Ordner "' + Folder + '"...');
    if setSystem then
      GrantString := ' /grant:r SYSTEM:(OI)(CI)F '
    else
      GrantString := '';
    if setAdmins then
      GrantString := ' /grant:r Administrators:(OI)(CI)F ';
    if setNetworkService then
      GrantString := ' /grant:r "NT AUTHORITY\NetworkService":(OI)(CI)F ';
    if setService <> '' then
      GrantString := ' /grant:r "NT SERVICE\' + setService + '":(OI)(CI)F ';
    if GrantString <> '' then
      begin
        if Exec(ExpandConstant('.\icacls.exe'), 
            '"' + Folder + '"' + GrantString, 
            ExpandConstant('{sys}'),
            SW_HIDE, ewWaitUntilTerminated, ResultCode) then
          Log('  -> Entfernen der Rechte erfolgreich.')
        else
          Log('  -> Fehler: Entfernen der Rechte fehlgeschlagen!');
      end;
  end;


{ Entfernt alle expliziten ACls rekursiv von dem angegebenen Ordner }
procedure RemoveExplicitACLsRecursive(FolderWithConstants: String);
  var
    Folder: String;
    ResultCode: Integer;
  begin
    Folder := ExpandConstant(FolderWithConstants);
    Log('Entferne die Rechte durch explizite ACLs auf dem Ordner "' + Folder + '"...');
    if Exec(ExpandConstant('.\icacls.exe'), 
            '"' + Folder + '" /reset /T /C', 
            ExpandConstant('{sys}'),
            SW_HIDE, ewWaitUntilTerminated, ResultCode) then
      Log('  -> Entfernen der Rechte erfolgreich.')
    else
      Log('  -> Fehler: Entfernen der Rechte fehlgeschlagen!');
  end;


{ Startet den angegebenen Windows-Dienst 
  @param ServiceName   der Name des Dienstes
  @param ServiceDescription   die Beschreibung des Dienstes, welche in Log-Ausgaben verwendet wird 
  @return true, falls der Dienst gestartet wurde, sonst false }
function StartWindowsService(ServiceName: String; ServiceDescription: String): Boolean;
  var
    ResultCode: Integer;
  begin
    // TODO Prüfe, ob der Dienst eingerichtet wurde und starte ihn nur in diesem Fall
    Log('Starte den Dienst ' + ServiceDescription + '...');
    if Exec(ExpandConstant('.\sc.exe'), 
            'start ' + ServiceName,
            ExpandConstant('{sys}'),
            SW_HIDE, ewWaitUntilTerminated, ResultCode) then
      begin
        Log('  -> Dienst erfolgreich gestartet. (ResultCode=' + IntToStr(ResultCode) + ')');
        Result := True;
      end
    else
      begin
        Log('  -> Dienst konnte nicht gestartet werden. (ResultCode=' + IntToStr(ResultCode) + ')');
        Result := False;
      end;
  end;


{ Stoppt den angegebenen Windows-Dienst 
  @param ServiceName   der Name des Dienstes
  @param ServiceDescription   die Beschreibung des Dienstes, welche in Log-Ausgaben verwendet wird 
  @return true, falls der Dienst gestoppt wurde, sonst false }
function StopWindowsService(ServiceName: String; ServiceDescription: String): Boolean;
  var
    ResultCode: Integer;
  begin
    Log('Prüfe, ob der Dienst ' + ServiceDescription + ' existiert und stoppe diesen, falls er existiert...');
    if Exec(ExpandConstant('.\sc.exe'), 
            'query ' + ServiceName,
            ExpandConstant('{sys}'),
            SW_HIDE, ewWaitUntilTerminated, ResultCode) then
      begin
        Log('  -> Dienst existiert (ResultCode=' + IntToStr(ResultCode) + '), stoppe ihn...');
        if Exec(ExpandConstant('.\sc.exe'), 
                'stop ' + ServiceName,
                ExpandConstant('{sys}'),
                SW_HIDE, ewWaitUntilTerminated, ResultCode) then
          begin
            Log('  -> Dienst wurde beendet. (ResultCode=' + IntToStr(ResultCode) + ')');
            Result := True;
          end
        else
          begin
            Log('  -> Dienst konnte nicht beendet werden. Eventuell war er nicht gestartet. (ResultCode=' + IntToStr(ResultCode) + ')');
            Result := False;
          end;
      end
    else
      begin
        Log('  -> Dienst existiert nicht. (ResultCode=' + IntToStr(ResultCode) + ')');
        Result := False;
      end;
  end;

