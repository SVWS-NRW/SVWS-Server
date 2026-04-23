<?php

namespace wenom;

use wenom\Base32;

/**
 * Diese Klasse stellt die Funktionalität für die Authentifizierung zur Verfügung.
 */
class ENMAuth {

    // Die Konfiguration
    protected Config $config;

    // Die Datenbank für die Überprüfung von Credentials
    protected $db = null;

    // Die Authentifizierungsmethode (Unerstützt werden aktuell "Basic" und "Bearer")
    protected $authMethod = null;

    // Der Benutzername bei einer Basic-Authentifizierung
    protected $authUser = null;

    // Das Kennwort bei einer Basic-Authentifizierung
    protected $authPassword = null;

    // Das Token bei einer Bearer-Authentifizierung
    protected $authToken = null;

    // Ein neues Kennwort, sofern es in einer Passwort-Session bei der Authentifizierung enthalten war
    protected $newPassword = null;


    /**
     * Erstellt ein neues Authentifizierungsobjekt mit den Informationen aus dem HTTP-Request
     */
    public function __construct(Database $db, Config $config) {
        $this->db = $db;
        $this->config = $config;

        $authHeader = $_SERVER["HTTP_AUTHORIZATION"] ?? $_SERVER["REDIRECT_HTTP_AUTHORIZATION"] ?? null;
        if (($authHeader === null) && function_exists('apache_request_headers')) {
            $requestHeaders = array_change_key_case(apache_request_headers(), CASE_LOWER);
            if (isset($requestHeaders['authorization'])) {
                $authHeader = $requestHeaders['authorization'];
            }
        }

        if ($authHeader === null) {
            Http::exit500("HTTP-Authorization-Header kann nicht gelesen werden. Überprüfen sie die Anfrage oder die Server-Konfiguration.");
        }
        $parts = explode(" ", $authHeader, 2);
        if (strcasecmp($parts[0], "Basic") == 0) {
            $this->authMethod = "Basic";
            $decoded = base64_decode($parts[1]);
            if ($decoded === false) {
                Http::exit400BadRequest("Fehler beim Base64-Dekodieren der Credentials.");
            }
            $creds = explode(":", $decoded);
            if (count($creds) != 2) {
                Http::exit500("Fehler bei dem HTTP-Authorization-Header. Die Kodierung von Benutzername und Kennwort ist fehlerhaft.");
            }
            $this->authUser = $creds[0];
            $this->authPassword = $creds[1];
        } elseif (strcasecmp($parts[0], "Bearer") == 0) {
            $this->authMethod = "Bearer";
            $this->authToken = $parts[1];
        } else {
            Http::exit500("Die Authentifizierungsmethode wird aktuell noch nicht unterstützt.");
        }
    }

    /**
     * Prüfe den Authorization-Header, ob dieser eine Basic-Authentifizierung mit dem übergebenen Benutzernamen und
     * dem übergebenen Benutzer-Kennwort hat.
     * Tritt ein Fehler bei der Prüfung auf, so wird ein Fehlercode 401 zurückgegeben.
     *
     * @param string $username   der Benutzername
     * @param string $password   das Kennwort
     * @return void
         */
    public function pruefeBasicAuth(string $username, string $password): void {
        if ((strcmp($this->authMethod, "Basic") != 0)
            || (strcasecmp($this->authUser, $username) != 0)
            || (strcmp($this->authPassword, $password) != 0)) {
            Http::exit401UnauthorizedRealm();
        }
    }


    /**
     * Gibt die Remote-IP-Adresse der Anfrage zurück. Ist die Server-Variable nicht gesetzt,
     * so wird unkown als string zurückgegeben.
     *
     * @return string   die IP-Adresse oder unknown, falls keine gesetzt ist
     */
    public function getRemoteAddr(): string {
        return $_SERVER['REMOTE_ADDR'] ?? 'unknown';
    }


    /**
     * Prüfe den Authorization-Header, ob dieser eine Basic-Authentifizierung mit den Crendentials
     * eines Lehrers hat.
     * Tritt ein Fehler bei der Prüfung auf, so wird ein Fehlercode 401 zurückgegeben.
     *
     * @return object   das Lehrer-Objekt des angemeldeten Benutzer
     */
    public function pruefeLehrerBasicAuth() : object {
        if (strcmp($this->authMethod, "Basic") != 0) {
            Http::exit401UnauthorizedRealm();
        }

        // Prüfe, ob bereits zu viele Login-Versuche innerhalb kürzerer Zeit stattgefunden haben
        $ip = $this->getRemoteAddr();
        $lehrer = $this->db->getENMLehrerByEmail($this->authUser);
        $idLehrer = ($lehrer !== null) ? $lehrer->id : -1;
        if ($this->db->istLoginGesperrt($ip, $idLehrer)) {
            Http::exit429TooManyRequests("Zu viele Fehlversuche. Bitte warten Sie einige Minuten.");
        }

        // Überprüfe das Lehrer-Kennwort (Nutze ggf. einen ungültigen Hash, damit keine Timing-Angriffe gegen Benutzernamen funktionieren)
        $hash = ($lehrer !== null) ? $lehrer->passwordHash : '$2y$10$abcdefghijklmnopqrstuvwABCDEFGHIJKLMNOPQRSTUVWXYZ012345';
        $isValid = password_verify($this->authPassword, $hash);

        // Vermerke ggf. den Fehlerversuch für den Login
        if (!$isValid || ($lehrer === null)) {
            $this->db->updateLoginFailures($ip, $idLehrer);
            Http::exit401UnauthorizedRealm();
        }

        // Bei einem Erfolg hingegen können vergangene Fehlversuche gelöscht werden
        $this->db->clearLoginFailures($ip, $idLehrer);
        return $lehrer;
    }


    /**
     * Erstellt ein aktuelles JWT für eine Session für einen Lehrer.
     *
     * @param string $sessionKey   der Session-Key für das Signieren des JWT
     * @param string $idLehrer     die ID des Lehrers
     * @param string $expTime      die Zeit in Sekunden für die Gültigkeit des JWT
     *
     * @return string | false   das JSON mit dem JWT
     */
    public static function createJsonWebToken(string $sessionKey, int $idLehrer, int $expTime): string | false {
        // Erstelle das Json-Web-Token mit einer Gültigkeit von 8 Stunden
        $payload = [
            'sub' => $idLehrer,
            'exp' => time() + $expTime,
            'iat' => time()
        ];
        $jwt = Http::createJsonWebToken($payload, $sessionKey);
        return json_encode([ 'token' => $jwt, 'id' => $idLehrer ]);
    }


    /**
     * Erstellt ein aktuelles JWT für eine Session für einen Lehrer, welche zum Ersetzen eines Kennwortes bestimmt ist.
     *
     * @param string $sessionKey   der Session-Key für das Signieren des JWT
     * @param string $idLehrer     die ID des Lehrers
     * @param string $expTime      die Zeit in Sekunden für die Gültigkeit des JWT
     *
     * @return string | false   das JSON mit dem JWT
     */
    public static function createJsonWebTokenWithPassword(string $sessionKey, int $idLehrer, int $expTime): string | false {
        // Erstelle das Json-Web-Token mit einer Gültigkeit von 8 Stunden
        $payload = [
            'sub' => $idLehrer,
            'pwd' => Password::generate(),
            'exp' => time() + $expTime,
            'iat' => time()
        ];
        $jwt = Http::createJsonWebToken($payload, $sessionKey);
        return json_encode([ 'token' => $jwt, 'id' => $idLehrer ]);
    }


    /**
     * Prüft das Json-Web-Token und gibt bei erfolgreicher Authentifizierung das Lehrer-Objekt des
     * angemeldeten Benutzers zurück.
     *
     * @return object   das Lehrer-Objekt des angemeldeten Benutzer
     */
    public function pruefeLehrerSession(): object {
        if (strcasecmp(($this->authMethod ?? ''), "Bearer") !== 0) {
            Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server"');
        }

        $payload = Http::verifyJsonWebToken($this->authToken, $this->config->getClientSessionKey());
        if (($payload === null) || ($payload->exp < time())) {
            Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server", error="invalid_token", error_description="The access token has expired"');
        }

        // Schneller ID-Lookup statt teurem Passwort-Hash-Vergleich
        $lehrer = $this->db->getENMLehrerByID((int) $payload->sub);
        if (!$lehrer) {
            Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server"');
        }
        return $lehrer;
    }

    /**
     * Prüft das spezielle Json-Web-Token für TOTP-Prüfung und gibt bei erfolgreicher Authentifizierung das
     * Lehrer-Objekt des angemeldeten Benutzers zurück.
     *
     * @return object   das Lehrer-Objekt des angemeldeten Benutzer
     */
    public function pruefeLehrerTotpSession(): object {
        if (strcasecmp(($this->authMethod ?? ''), "Bearer") !== 0) {
            Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server"');
        }

        $payload = Http::verifyJsonWebToken($this->authToken, $this->config->getClientTotpAuthSessionKey());
        if (!$payload || ($payload->exp < time())) {
            Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server", error="invalid_token", error_description="The access token has expired"');
        }

        $lehrer = $this->db->getENMLehrerByID((int) $payload->sub);
        if (!$lehrer) {
            Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server"');
        }

        if ($this->db->istLoginGesperrt($this->getRemoteAddr(), $lehrer->id)) {
            Http::exit429TooManyRequests("Zu viele Fehlversuche. Bitte warten Sie einige Minuten.");
        }

        return $lehrer;
    }


    /**
     * Prüft das spezielle Json-Web-Token für die erzwungene Passwortänderung und gibt
     * bei erfolgreicher Authentifizierung das Lehrer-Objekt zurück.
     *
     * @return object   das Lehrer-Objekt des angemeldeten Benutzers
     */
    public function pruefeLehrerPasswordChangeSession(): object {
        if (strcasecmp(($this->authMethod ?? ''), "Bearer") !== 0) {
            Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server"');
        }

        $payload = Http::verifyJsonWebToken($this->authToken, $this->config->getClientChangePasswordSessionKey());
        if (!$payload || ($payload->exp < time())) {
            Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server", error="invalid_token", error_description="The password change token has expired"');
        }

        $lehrer = $this->db->getENMLehrerByID((int) $payload->sub);
        if (!$lehrer) {
            Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server"');
        }

        // Prüfe, ob das Initial-Kennwort nicht schon angepasst wurde (verhindere Replay-Attacken mit dem Token)
        if (!$lehrer->istInitialPassword) {
            Http::exit403Forbidden();
        }

        $this->newPassword = $payload->pwd;
        return $lehrer;
    }


    /**
     * Gibt das neue Kennwort zurück, falls eine erfolgreiche Authentifizierung bei einer Passwort-Änderungs-Session
     * vorliegt.
     *
     * @return string | null das neue Kennwort
     */
    public function getNewPassword(): string | null {
        return $this->newPassword;
    }


    /**
     * Bestimmt den TOTP-Token für das angegebene Zeitfenster
     *
     * @param string $secret   das TOTP Shared Secret (nicht Base32-kodiert!)
     * @param int $timeSlice   das Zeitfenster
     *
     * @return string das TOTP-Token
     */
    private function calculateTotpToken(string $secret, int $timeSlice): string {
        $hash = hash_hmac('sha1', pack('N*', 0).pack('N*', $timeSlice), $secret, true);

        // Dynamisches Truncating gemäß RFC 4226
        $offset = ord($hash[strlen($hash) - 1]) & 0xf;
        $part = substr($hash, $offset, 4);
        $value = unpack('N', $part)[1] & 0x7fffffff;
        $truncatedHash = $value % 1000000;

        return str_pad($truncatedHash, 6, '0', STR_PAD_LEFT);
    }

    /**
     * Verifziert den übergebenen TOTP-Token mit dem übergebenen Shared Secret gemäß RFC 6238.
     * Dabei wird aus praktischen Gründen auch eine Zukunftstoleranz zugelassen, falls die Uhren nicht
     * perfekt synchronisiert sind.
     *
     * @param string $secret   das Base32-kodierte TOTP Shared Secret
     * @param string $token    der 6-stellige TOTP-Token
     *
     * @return true, wenn der Token gültig ist, und ansonsten false
     */
    public function pruefeLehrerTotpToken(string $secret, string $token): bool {
        $secretDecoded = Base32::decode($secret);

        // Bestimme das aktuelle Zeitfenster und den Toleranzbereich darum
        $sizeTimeslice = $this->config->getTotpTimeslice();
        $currentTimeSlice = intdiv(time(), $sizeTimeslice);
        $tolerance = $this->config->getTotpTolerance();

        // Prüfe das aktuelle Zeitfenster
        if (hash_equals($this->calculateTotpToken($secretDecoded, $currentTimeSlice), $token)) {
            return true;
        }

        // Prüfe den Toleranzbereich, jeweils abwechselnd die Vergangenheit und die Zukunft...
        for ($i = 1; $i <= $tolerance; $i++) {
            if (hash_equals($this->calculateTotpToken($secretDecoded, $currentTimeSlice - $i), $token) ||
                    hash_equals($this->calculateTotpToken($secretDecoded, $currentTimeSlice + $i), $token)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prüft, ob das Access-Token der Anfrage zu einem Client gehört und gültig ist.
     * Tritt ein Fehler bei der Prüfung auf, so wird ein Fehlercode 401 zurückgegeben.
     * @return void
         */
    public function pruefeAccessToken(): void {
        if (strcmp($this->authMethod, "Bearer") != 0) {
            Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server", error="invalid_request", error_description="An access token is required"');
        }
        $client = $this->db->getClientByAccessToken($this->authToken);
        if ($client == null) {
            Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server", error="invalid_token", error_description="The access token is not valid"');
        }
        $elapsed = time() - $client->tokenTimestamp;
        if ($elapsed < 0) {
            Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server", error="invalid_token", error_description="The access token has an invalid timestamp"');
        }
        if ($elapsed > $client->tokenValidForSecs) {
            Http::exit401Unauthorized('WWW-Authenticate: Bearer realm="ENM-Server", error="invalid_token", error_description="The access token has expired"');
        }
    }

    /**
     * Prüfe den Authorization-Header, ob die Methode "Basic" vorliegt, der Benutzername
     * eine gültige Client-ID ist und das Client-Secret zu dem Secret in der Datenbank passt.
     * Tritt ein Fehler bei der Prüfung auf, so wird ein Fehlercode 401 zurückgegeben.
     *
     * @return int im Erfolgsfall wird die authorisierte Client-ID zurückgegeben
     */
    public function pruefeClientSecret(): int {
        if (strcmp($this->authMethod, "Basic") != 0) {
            Http::exit401Unauthorized('WWW-Authenticate: Basic realm="ENM-Server", error="invalid_client", error_description="Client authentication is required"');
        }
        $clientID = intval($this->authUser);
        if ($clientID <= 0) {
            Http::exit401Unauthorized('WWW-Authenticate: Basic realm="ENM-Server", error="invalid_client", error_description="Client is unknown"');
        }
        $dbSecret = $this->config->getClientSecret();
        if ($dbSecret == null) {
            Http::exit401Unauthorized('WWW-Authenticate: Basic realm="ENM-Server", error="invalid_client", error_description="Client secret does not exist"');
        }
        if (strcmp($this->authPassword, $dbSecret) != 0) {
            Http::exit401Unauthorized('WWW-Authenticate: Basic realm="ENM-Server", error="invalid_client", error_description="Invalid client secret"');
        }
        return $clientID;
    }

    /**
     * Gibt die HTTP-Methode zurück.
     */
    public function getHTTPMethod(): string {
        return $_SERVER['REQUEST_METHOD'];
    }

    /**
     * Prüft, ob die HTTP-Methode erlaubt ist oder nicht.
     *
     * @param array $allowed   die erlaubten HTTP-Methoden
     * @return void
         */
    public function pruefeHTTPMethod(array $allowed): void {
        $hasMethod = false;
        foreach ($allowed as $tmp) {
            if (strcmp($_SERVER['REQUEST_METHOD'], $tmp) === 0) {
                $hasMethod = true;
            }
        }
        if (!$hasMethod) {
            Http::exit403Forbidden();
        }
    }


    /**
     * Ändert das Kennwort eines Benutzers.
     *
     * @param object $lehrer        das authentifizierte Lehrer-Objekt
     * @param string | null $newPassword   das neue Kennwort
     */
    public function updatePassword(object $lehrer, string | null $newPassword): void {
        if (($newPassword === null) || !Config::validatePassword($newPassword)) {
            Http::exit400BadRequest("Das neue Passwort entspricht nicht den Richtlinien.");
        }
        $this->db->setLehrerKennwort($lehrer->id, $newPassword);
    }

}
