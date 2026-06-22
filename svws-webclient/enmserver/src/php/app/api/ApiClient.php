<?php

namespace wenom\api;

use wenom\Config;
use wenom\Database;
use wenom\ENMAuth;
use wenom\Http;
use wenom\PatchManager;
use wenom\ENMDatenManager;
use wenom\TimeUtils;
use wenom\Version;

/**
 * Diese Klasse verwaltet die Client-Schnittstelle des WeNoM-Servers.
 * Die Endpunkte befinden sich unter /api/* (nicht unter /api/secure/).
 */
class ApiClient {

    private function createAppContext(): array {
        $config = new Config();
        $db = new Database($config);
        return [$config, $db, new ENMAuth($db, $config)];
    }


    /**
     * Prüft den HTTP-Request und delegiert den Aufruf - sofern gültig an die konkrete API-Methode
     * Eine Prüfung der HTTP-Methode erfolgt hier vor dem konkreten Methodenaufruf.
     *
     * @param string $endpoint   der Name des Endpunkts (z.B. 'Check')
     */
    public function handle(string $endpoint): void {
        $method = $_SERVER['REQUEST_METHOD'];

        $routes = [
            'alive' => [
                'GET' => [
                    'init' => fn() => [],
                    'auth' => fn() => true,
                    'call' => fn() => $this->alive()
                ]
            ],
            'ankreuzkompetenz' => [
                'POST' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => $auth->pruefeLehrerSession(),
                    'call' => fn($config, $db, $auth, $lehrer) => $this->patchAnkreuzkompetenz($db, $lehrer)
                ]
            ],
            'bemerkungen' => [
                'POST' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => $auth->pruefeLehrerSession(),
                    'call' => fn($config, $db, $auth, $lehrer) => $this->patchBemerkungen($db, $lehrer)
                ]
            ],
            'check_smtp' => [
                'GET' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => true,
                    'call' => fn($config, $db, $auth) => $this->checkSmtp($db)
                ]
            ],
            'clientconfig' => [
                'GET' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => $auth->pruefeLehrerSession(),
                    'call' => fn($config, $db, $auth, $lehrer) => $this->getClientConfig($db, $lehrer)
                ],
                'PUT' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => $auth->pruefeLehrerSession(),
                    'call' => fn($config, $db, $auth, $lehrer) => $this->putClientConfig($db, $lehrer)
                ]
            ],
            'change_password' => [
                'POST' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => $auth->pruefeLehrerPasswordChangeSession(),
                    'call' => fn($config, $db, $auth, $lehrer) => $this->changePassword($config, $auth, $lehrer)
                ]
            ],
            'create_pwt' => [
                'POST' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => true,
                    'call' => fn($config, $db, $auth) => $this->createPwt($db)
                ]
            ],
            'daten' => [
                'GET' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => $auth->pruefeLehrerSession(),
                    'call' => fn($config, $db, $auth, $lehrer) => $this->getDaten($db, $lehrer)
                ]
            ],
            'leistung' => [
                'POST' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => $auth->pruefeLehrerSession(),
                    'call' => fn($config, $db, $auth, $lehrer) => $this->patchLeistung($db, $lehrer)
                ]
            ],
            'lernabschnitt' => [
                'POST' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => $auth->pruefeLehrerSession(),
                    'call' => fn($config, $db, $auth, $lehrer) => $this->patchLernabschnitt($db, $lehrer)
                ]
            ],
            'login' => [
                'POST' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => $auth->pruefeLehrerBasicAuth(),
                    'call' => fn($config, $db, $auth, $lehrer) => $this->login($config, $auth, $lehrer)
                ]
            ],
            'login_totp' => [
                'POST' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => $auth->pruefeLehrerTotpSession(),
                    'call' => fn($config, $db, $auth, $lehrer) => $this->loginTotp($config, $db, $auth, $lehrer)
                ]
            ],
            'logout' => [
                'POST' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => $auth->pruefeLehrerSession(true),
                    'call' => fn($config, $db, $auth, $lehrer) => $this->logout($config)
                ]
            ],
            'mode' => [
                'GET' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => true,
                    'call' => fn($config, $db, $auth) => $this->getMode($config)
                ]
            ],
            'refresh_token' => [
                'POST' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => $auth->pruefeLehrerSessionForAccessTokenRefresh(),
                    'call' => fn($config, $db, $auth, $lehrer) => $this->refreshToken($config, $auth, $lehrer)
                ]
            ],
            'reset_password' => [
                'PUT' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => true, // Validierung erfolgt fachlich über den Token
                    'call' => fn($config, $db, $auth) => $this->resetPassword($db)
                ]
            ],
            'schulform' => [
                'GET' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => $auth->pruefeLehrerSession(),
                    'call' => fn($config, $db, $auth, $lehrer) => $this->getSchulform($db)
                ]
            ],
            'setup' => [
                'GET' => [
                    'init' => fn() => [],
                    'auth' => fn() => true,
                    'call' => fn() => $this->setup()
                ]
            ],
            'teilleistung' => [
                'POST' => [
                    'init' => fn() => $this->createAppContext(),
                    'auth' => fn($config, $db, $auth) => $auth->pruefeLehrerSession(),
                    'call' => fn($config, $db, $auth, $lehrer) => $this->patchTeilleistung($db, $lehrer)
                ]
            ],
            'version' => [
                'GET' => [
                    'init' => fn() => [],
                    'auth' => fn() => true,
                    'call' => fn() => $this->version()
                ]
            ]
        ];

        if (!isset($routes[$endpoint][$method])) {
            isset($routes[$endpoint]) ? Http::exit405MethodNotAllowed() : Http::exit404NotFound();
        }

        $route = $routes[$endpoint][$method];
        $context = $route['init']();
        $lehrer = $route['auth'](...$context);
        $route['call'](...array_merge($context, [$lehrer]));
    }

    /**
     * Einfache Überprüfung, ob die API erreichbar ist
     */
    private function alive(): void {
        Http::exit204NoContent();
    }

    /**
     * Einfache Abfrage der Version und des Git-Hash
     */
    private function version(): void {
        try {
            $version = Version::VERSION;
            $githash = Version::GITHASH;
        } catch (\Throwable $e) {
            $version = "";
            $githash = null;
        }
        Http::exit200OKJson(json_encode([
            "version" => $version,
            "githash" => $githash
        ]));
    }

    /**
     * Initialisiert den Server beim ersten Aufruf.
     * Erstellt das Client Secret und die Datenbankstruktur.
     */
    private function setup(): void {
        // Wenn die Anwendung bereits initialisiert ist, dann gib den Code 409 (Conflict) zurück.
        if (Config::isAppInitialized()) {
            http_response_code(409);
            exit();
        }

        // Initialisiere die Konfiguration
        $config = new Config();

        // Initialisiere die Datenbank-Verbindung
        $db = new Database($config);
        unset($db);

        // Erfolg melden
        Http::exit204NoContent();
    }

    /**
     * Gibt die Schulform aus den ENM-Daten zurück.
     *
     * @param Database $db die Datenbank-Verbindung
     */
    private function getSchulform(Database $db): void {
        $enmDaten = json_decode($db->getJsonENMDaten()->daten);
        Http::exit200OKJson($enmDaten->schulform);
    }

    /**
     * Gibt den konfigurierten Server-Modus (dev / alpha / beta / stable) zurück.
     *
     * @param Config $config die WeNoM-Server-Konfiguration
     */
    private function getMode(Config $config): void {
        Http::exit200OKJson($config->getServerMode());
    }


    /**
     * Authentifiziert den Lehrer mit dem ersten Faktor. Ist ein zweiter Faktor aktiviert,
     * so wird dies zurückgemeldet (202). Ansonsten wird das JSON-Web-Token (JWT) zurückgegeben (200)
     *
     * @param Config $config   die WeNoM-Server-Konfiguration
     * @param object $lehrer   das authentifizierte Lehrer-Objekt
     */
    private function continueLogin(Config $config, ENMAuth $auth, object $lehrer): void {
        // Fall 0: 2FA deaktiviert (für den Lehrer)
        if ($lehrer->art2FA === 0) {
            $jwt = $auth->createJsonWebToken($config->getClientSessionKey(), $lehrer->id, $config->getLifetimeAccessToken());
            Http::exit200OKJson($jwt);
        }
        // Fall 1: 2FA mit TOTP wird verwendet
        if ($lehrer->art2FA === 1) {
            $lifetimeAccessToken = $lehrer->istErstanmeldung ? $config->getLifetimeTotpAccessTokenInitial() : $config->getLifetimeTotpAccessToken();
            $jwt = $auth->createJsonWebToken($config->getClientTotpAuthSessionKey(), $lehrer->id, $lifetimeAccessToken);
            if ($lehrer->istErstanmeldung) {
                $token = json_decode($jwt);
                $response = [
                    'token' => $token,
                    'setup' => [
                        'secret'  => $lehrer->totpSecret,
                        'issuer'  => 'WeNoM',
                        'account' => $lehrer->eMailDienstlich
                    ]
                ];
                Http::exit202AcceptedJson(json_encode($response, JSON_UNESCAPED_SLASHES));
            }
            Http::exit202AcceptedJson($jwt);
        }
        Http::exit400BadRequest("Eine 2FA mit der ID ".$lehrer->art2FA." wird vom Server noch nicht unterstützt.");
    }


    /**
     * Authentifiziert den Lehrer mit dem ersten Faktor.
     * Wird dabei ein Initial-Kennwort verwendet, so wird eine Änderung des Kennwortes gefordert.
     * Ansonsten wird entweder ein JSON-Web-Token (JWT) zurückgegeben oder ein zweiter Faktor angefordert.
     *
     * @param Config $config   die WeNoM-Server-Konfiguration
     * @param object $lehrer   das authentifizierte Lehrer-Objekt
     */
    private function login(Config $config, ENMAuth $auth, object $lehrer): void {
        if ($lehrer->istInitialPassword) {
            $jwt = $auth->createJsonWebToken($config->getClientChangePasswordSessionKey(), $lehrer->id, $config->getLifetimeChangePasswordToken(), true);
            $token = json_decode($jwt);
            Http::exit202AcceptedJson(json_encode([
                'token' => $token,
                'changePassword' => true
            ]));
        }
        $this->continueLogin($config, $auth, $lehrer);
    }


    /**
     * Ändert das Initial-Kennwort eines Benutzers und setzt danach den Login fort.
     *
     * @param Config $config   die Konfiguration
     * @param ENMAuth $auth    die Klasse für Authentifizierung
     * @param object $lehrer   das authentifizierte Lehrer-Objekt
     */
    private function changePassword(Config $config, ENMAuth $auth, object $lehrer): void {
        $newPassword = $auth->getNewPassword();
        if ($newPassword === null) {
            Http::exit400BadRequest("Kein neues Passwort im Token gefunden.");
        }
        $auth->updatePassword($lehrer, $newPassword);
        $this->continueLogin($config, $auth, $lehrer);
    }


    /**
     * Prüft beim Login-Vorgang nach erfolgreicher Prüfung des ersten Faktors den zweiten Faktor per TOTP
     * und gibt ein JSON-Web-Token (JWT) für den Client-Zugriff zurück.
     *
     * Anmerkung: Der Zugang zu diesem Endpunkt erfolgt über ein spezielles JWT des login-Endpunktes, welches nur für
     * TOTP bestimmt ist.
     *
     * @param Config $config   die Konfiguration
     * @param Database $db     die Datenbank-Verbindung
     * @param ENMAuth $auth    die Klasse für Authentifizierung
     * @param object $lehrer   das authentifizierte Lehrer-Objekt
     */
    private function loginTotp(Config $config, Database $db, ENMAuth $auth, object $lehrer): void {
        $body = Http::getBodyJsonObject();

        if (!isset($body->code)) {
            Http::exit400BadRequest("Es muss ein JSON-Objekt mit dem Attribut 'code' übergeben werden.");
        }
        $code = (string)$body->code;
        if (!preg_match('/^\d{6}$/', $code)) {
            Http::exit400BadRequest("Der Code muss 6 Ziffern beinhalten.");
        }

        $success = $auth->pruefeLehrerTotpToken($lehrer->totpSecret, $code);
        if ($success) {
            $db->clearLoginFailures($auth->getRemoteAddr(), $lehrer->id);
            if ($lehrer->istErstanmeldung) {
                $db->setLehrerErstanmeldungAbgeschlossen($lehrer->id);
            }
            $jwt = $auth->createJsonWebToken($config->getClientSessionKey(), $lehrer->id, $config->getLifetimeAccessToken());
            Http::exit200OKJson($jwt);
        } else {
            $db->updateLoginFailures($auth->getRemoteAddr(), $lehrer->id);
            Http::exit403Forbidden();
        }
    }


    /**
     * Loggt den Lehrer aus. Die Token-Version wurde bei korrektem JWT-Token bereits erhöht. Hier werden
     * dann die Cookies gelöscht.
     *
     * @param Config $config   die Konfiguration
     */
    private function logout(Config $config): void {
        // Entferne den Hardened Cookie beim Client, dadurch, das der Ablauf in die Vergangenheit gesetzt wird
        if ($config->getUseHardenedCookies()) {
            Http::setHardenedCookie($config->getHardenedCookieName(), '', time() - 3600, Http::isTrustedConnection($config->getTrustedProxies()));
        }

        Http::exit204NoContent();
    }


    /**
     * Prüft, ob eine SMTP-Client-Konfiguration vorhanden ist oder nicht.
     *
     * @param Database $db   die Datenbank-Verbindung
     */
    private function checkSmtp(Database $db): void {
        $smtpClient = $db->getSMTPClient();
        $isValid = ($smtpClient !== null);
        Http::exit200OKJson(json_encode(['isValid' => $isValid]));
    }


    /**
     * Endpunkt zur Anforderung eines neuen Passworts für Lehrer.
     *
     * @param Database $db die Datenbank-Verbindung
     */
    private function createPwt(Database $db): void {
        // Lehrerdaten aus der Datenbank lesen
        $data = Http::getBodyJsonObject();
        $eMailDienstlich = $data->eMailDienstlich ?? null;

        // Überprüfen, ob die erforderlichen Daten vorhanden sind
        if (empty($eMailDienstlich)) {
            Http::exit400BadRequest(json_encode(['fehler' => 'Die Dienst-E-Mail ist erforderlich.']));
        }

        // Lehrerdaten anhand der E-Mail-Adresse holen
        if (!$db->checkENMLehrerByEmail($eMailDienstlich)) {
            http_response_code(409);
            echo json_encode(['fehler' => 'Mehrere Lehrer mit dieser E-Mail-Adresse gefunden.']);
            exit;
        }

        $lehrerDaten = $db->getENMLehrerByEmail($eMailDienstlich);
        $lehrerId = $lehrerDaten->id;

        // Abbrechen, wenn das Token noch gültig ist
        if ($db->isENMLehrerTokenValid($lehrerId)) {
            // Token ist noch gültig
            http_response_code(429);
            echo json_encode(['error' => 'Es wurde bereits eine E-Mail zum Zurücksetzen des Passworts versendet. Bitte warten Sie, bevor Sie es erneut versuchen.']);
            exit;
        }

        // Token in DB speichern/aktualisieren
        $token = $db->writeENMLehrerToken($lehrerId);

        // Email Attribute setzen
        $to = $eMailDienstlich;
        $subject = 'WeNoM - neue Passwortanforderung';
        $body = "Sie haben ein neues Passwort für Ihr WeNoM-Account angefordert. \r\n";
        $body .= "Ihr Passwort-Token: " . $token;

        // SMTP-Client prüfen
        $smtpClient = $db->getSMTPClient();
        if ($smtpClient !== null) {
            // und E-Mail versenden ...
            $smtpClient->setEmail($to, $subject, $body);
            $smtpClient->sendEmail();
        }

        Http::exit204NoContent();
    }


    /**
     * Führt ein Refresh des Access Tokens aus und gib den neuen Access-Token in der Response zurück.
     *
     * @param Config $config   die Konfiguration
     * @param ENMAuth $auth    die Klasse für Authentifizierung
     * @param object $lehrer   die Daten zum Lehrer
     */
    private function refreshToken(Config $config, ENMAuth $auth, object $lehrer): void {
        $jwt = $auth->createJsonWebToken($config->getClientSessionKey(), $lehrer->id, $config->getLifetimeAccessToken());
        if ($jwt === false) {
            Http::exit500("Fehler beim Generieren des neuen Access-Tokens.");
        }
        Http::exit200OKJson($jwt);
    }


    /**
     * Setzt das Passwort eines Lehrers mithilfe eines Tokens zurück.
     *
     * @param Database $db die Datenbank-Verbindung
     */
    private function resetPassword(Database $db): void {
        // Lehrerdaten aus der Datenbank lesen
        $data = Http::getBodyJsonObject();
        $eMailDienstlich = $data->eMailDienstlich ?? null;
        $password = $data->password ?? null;
        $token = $data->token ?? null;

        // Überprüfen, ob die erforderlichen Daten vorhanden sind
        if (empty($eMailDienstlich) || empty($password) || empty($token)) {
            http_response_code(400);
            echo json_encode(['fehler' => 'Die Dienst-E-Mail und das Passwort sind erforderlich.']);
            exit;
        }

        // Gültigkeit des Tokens prüfen (Dauer und Token vorhanden)
        if (!$db->isENMLehrerTokenValid($token)) {
            http_response_code(409);
            echo json_encode(['fehler' => 'Der Token ist nicht gültig.']);
            exit;
        }

        // Passwort validieren
        if (!Config::validatePassword($password)) {
            http_response_code(409);
            echo json_encode(['fehler' => 'Das Passwort entspricht nicht den Konventionen.']);
            exit;
        }

        // Lehrerdaten anhand der E-Mail-Adresse holen
        if (!$db->checkENMLehrerByEmail($eMailDienstlich)) {
            http_response_code(409);
            echo json_encode(['fehler' => 'Mehrere Lehrer mit dieser E-Mail-Adresse gefunden.']);
            exit;
        }

        // Erstelle einen Patch für den Patch-Manager
        $lehrerDaten = $db->getENMLehrerByEmail($eMailDienstlich);
        $lehrerId = $lehrerDaten->id;
        $passwordHash = password_hash($password, PASSWORD_DEFAULT);
        $lehrerPatch = (object)[
            'id' => $lehrerId,
            'passwordHash' => $passwordHash,
            'tsPasswordHash' => TimeUtils::now(),
        ];

        // Daten in die Datenbank zurückschreiben
        PatchManager::patchENMLehrerPassword($db->conn, $lehrerDaten, $lehrerPatch);
        $db->deleteENMLehrerToken($lehrerId);

        Http::exit204NoContent();
    }


    /**
     * Liefere die gesamte Konfiguration - benutzerspezifisch und global zurück.
     *
     * @param Database $db      die Datenbank-Verbindung
     * @param object   $lehrer  das authentifizierte Lehrer-Objekt
     */
    private function getClientConfig(Database $db, object $lehrer): void {
        Http::exit200OKJson(Database::getClientConfig($db->conn, $lehrer->id));
    }

    /**
     * Setze einen Eintrag bei der benutzerspezifischen Konfiguration.
     *
     * @param Database $db      die Datenbank-Verbindung
     * @param object   $lehrer  das authentifizierte Lehrer-Objekt
     */
    private function putClientConfig(Database $db, object $lehrer): void {
        $obj = Http::getBodyJsonObject();
        if (!property_exists($obj, "key") || !property_exists($obj, "value")) {
            Http::exit400BadRequest("Fehlerhafte Anfrage: Es muss ein Schlüsselwert angegeben sein und ein Wert muss entweder gültig gesetzt oder explizit null für ein Entfernen des Eintrags sein.");
        }
        $keytype = gettype($obj->key);
        if (strcmp($keytype, "string") !== 0) {
            Http::exit400BadRequest("Fehlerhafte Anfrage: Der Schlüsselwert muss eine Zeichenkette sein.");
        }
        $valuetype = gettype($obj->value);
        if ((strcmp($valuetype, "string") !== 0) && (strcmp($valuetype, "NULL") !== 0)) {
            Http::exit400BadRequest("Fehlerhafte Anfrage: Der Wert muss entweder eine Zeichenkette oder NULL sein.");
        }
        $db->putClientUserConfig($lehrer->id, $obj->key, $obj->value);
        Http::exit204NoContent();
    }


    /**
     * Liefert die gefilterten ENM-Daten für den angemeldeten Lehrer als GZip.
     *
     * @param Database $db      die Datenbank-Verbindung
     * @param object   $lehrer  das authentifizierte Lehrer-Objekt
     */
    private function getDaten(Database $db, object $lehrer): void {
        // Prüfung, GZip-Komprimierung unterstützt wird. Das wird von /api/daten gefordert
        if (!Http::checkAcceptGZipEncoding()) {
            Http::exit400BadRequest("Der Client unterstützt laut Header (accept-encoding) keine GZip-Komprimierung.");
        }

        // 2. Daten über den Manager abrufen
        $enmDatenManager = ENMDatenManager::createFromDatabase($db);
        $content = $enmDatenManager->getENMDatenForLehrer($lehrer);

        // 3. Export via Http-Helper (nutzt intern gzencode($data, 5))
        Http::exit200OKGZipJson($content);
    }


    /**
     * Aktualisiert Schüler-Ankreuzkompetenzen
     *
     * @param Database $db      die Datenbank-Verbindung
     * @param object   $lehrer  das authentifizierte Lehrer-Objekt
     */
    private function patchAnkreuzkompetenz(Database $db, object $lehrer): void {
        $enmDatenManager = ENMDatenManager::createFromDatabase($db);
        $patchManager = new PatchManager($enmDatenManager);

        $patch = Http::getBodyJsonObject();
        $patchManager->patchENMSchuelerAnkreuzkompetenzen($lehrer, $patch);
        Http::exit204NoContent();
    }

    /**
     * Aktualisiert Schülerbemerkungen (ASV, AUE, ZB, etc.)
     *
     * @param Database $db      die Datenbank-Verbindung
     * @param object   $lehrer  das authentifizierte Lehrer-Objekt
     */
    private function patchBemerkungen(Database $db, object $lehrer): void {
        $enmDatenManager = ENMDatenManager::createFromDatabase($db);
        $patchManager = new PatchManager($enmDatenManager);
        $patch = Http::getBodyJsonObject();
        $patchManager->patchENMSchuelerBemerkungen($lehrer, $patch->id, $patch->patch);
        Http::exit204NoContent();
    }

    /**
     * Aktualisiert Schüler-Leistungsdaten (Noten, Fehlstunden, Bemerkungen).
     *
     * @param Database $db      die Datenbank-Verbindung
     * @param object   $lehrer  das authentifizierte Lehrer-Objekt
     */
    private function patchLeistung(Database $db, object $lehrer): void {
        $enmDatenManager = ENMDatenManager::createFromDatabase($db);
        $patchManager = new PatchManager($enmDatenManager);
        $patch = Http::getBodyJsonObject();
        $patchManager->patchENMLeistung($lehrer, $patch);
        Http::exit204NoContent();
    }

    /**
     * Aktualisiert Schüler-Lernabschnittsdaten (Gesamt-Fehlstunden).
     *
     * @param Database $db      die Datenbank-Verbindung
     * @param object   $lehrer  das authentifizierte Lehrer-Objekt
     */
    private function patchLernabschnitt(Database $db, object $lehrer): void {
        $enmDatenManager = ENMDatenManager::createFromDatabase($db);
        $patchManager = new PatchManager($enmDatenManager);
        $patch = Http::getBodyJsonObject();
        $patchManager->patchENMSchuelerLernabschnitt($lehrer, $patch);
        Http::exit204NoContent();
    }

    /**
     * Aktualisiert Schüler-Teilleistungsdaten (Noten, Art, Datum, Bemerkung).
     *
     * @param Database $db      die Datenbank-Verbindung
     * @param object   $lehrer  das authentifizierte Lehrer-Objekt
     */
    private function patchTeilleistung(Database $db, object $lehrer): void {
        $enmDatenManager = ENMDatenManager::createFromDatabase($db);
        $patchManager = new PatchManager($enmDatenManager);
        $patch = Http::getBodyJsonObject();
        $patchManager->patchENMTeilleistung($lehrer, $patch);
        Http::exit204NoContent();
    }

}
