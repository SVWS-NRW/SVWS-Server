<?php

namespace wenom;

use \ValueError as ValueError;

$inc_memory_limit_success = @ini_set('memory_limit', '1024M');
if ($inc_memory_limit_success === false) {
    $inc_memory_limit_success = @ini_set('memory_limit', '768M');
    if ($inc_memory_limit_success === false) {
        $inc_memory_limit_success = @ini_set('memory_limit', '512M');
        if ($inc_memory_limit_success === false) {
            $inc_memory_limit_success = @ini_set('memory_limit', '256M');
        }
    }
}

/**
 * Diese Klasse stellt Hilfsmethoden für den Zugriff auf HTTP-Requests und das Schreiben
 * von Http-Responses zur Verfügung.
 */
class Http {

    /**
     * Hilfsmethode für das Erstellen einer URL-Sicheren Variante der Base-64-Kodierung zu den übergebenen Daten
     * (+ wird zu -, / zu _ und = als Padding wird am Ende entfernt)
     *
     * @param string $data   die zu kodierenden Daten
     *
     * @return string die Base-64-kodierten Daten
     */
    private static function base64UrlEncode(string $data): string {
        return rtrim(strtr(base64_encode($data), '+/', '-_'), '=');
    }

    /**
     * Hilfsmethode für das Wiederherstellen von Daten aus einer URL-Sicheren Variante der Base-64-Kodierung.
     * (- wird zu +, _ zu / und = wird ggf. als Padding ergänzt)
     *
     * @param string $data   die Base-64-kodierten Daten
     *
     * @return string die dekodierten Daten
     */
    private static function base64UrlDecode(string $data): string {
        $remainder = strtr($data, '-_', '+/');
        $padding = strlen($remainder) % 4;
        if ($padding) {
            $remainder .= str_repeat('=', 4 - $padding);
        }
        return base64_decode($remainder);
    }

    /**
     * Erzeugt ein mit dem übergebenen Schlüssel signiertes Json-Web-Token und dem übergebenen Payload
     * und gibt dieses zurück.
     *
     * @param array $payload   die Payload für das Json-Web-Token
     * @param string $key      der Schlüssel für das Signieren
     *
     * @return string das Json-Web-Token
     */
    public static function createJsonWebToken(array $payload, string $key): string {
        $header = json_encode(['alg' => 'HS256', 'typ' => 'JWT']); // HMAC-SHA256-Algorithmus, Json-Web-Token
        $base64UrlHeader = self::base64UrlEncode($header);
        $base64UrlPayload = self::base64UrlEncode(json_encode($payload));

        $signature = hash_hmac('sha256', $base64UrlHeader.".".$base64UrlPayload, $key, true);
        $base64UrlSignature = self::base64UrlEncode($signature);

        return $base64UrlHeader.".".$base64UrlPayload.".".$base64UrlSignature;
    }

    /**
     * Prüft ein Json-Web-Token und gibt den Payload zurück.
     * Im Fehlerfall wird null zurückgegeben.
     *
     * @param string $jwt   das Json-Web-Token
     * @param string $key   der Schlüssel für die Überprüfung der Signatur
     *
     * @return object die Payload oder null
     */
    public static function verifyJsonWebToken(string $jwt, string $key): ?object {
        $parts = explode('.', $jwt);
        if (count($parts) !== 3) {
            return null;
        }

        list($header, $payload, $signature) = $parts;
        $validSig = self::base64UrlEncode(hash_hmac('sha256', $header.".".$payload, $key, true));
        if (!hash_equals($validSig, $signature)) {
            return null;
        }

        return json_decode(self::base64UrlDecode($payload));
    }


    /**
     * Ermittelt die Client-IP. Dabei wird die Verwendung von Proxies verücksichtigt, die
     * als trusted proxies an diese Methode übergeben werden.
     *
     * @param array $trustedProxies   die Liste der IP-Adressen von vertrauenswürdigen Proxies
     *
     * @return string die ermittelte Client-IP-Adresse
     */
    public static function getClientIP(array $trustedProxies): string {
        // Bestimme zunächst die angegebene IP-Adresse - dies kann auch die IP-Adresse eines Proxies sein
        $remoteAddr = $_SERVER['REMOTE_ADDR'] ?? '127.0.0.1';
        $clientIP = $remoteAddr;

        // Wenn die angegebene IP-Adresse die eines vertrauenswürdigen Proxies ist, dann lies die Information zum Forward aus, um die echte IP zu bestimmen
        if (in_array($remoteAddr, $trustedProxies)) {
            $forwardedFor = $_SERVER['HTTP_X_FORWARDED_FOR'] ?? '';
            if (!empty($forwardedFor)) {
                // X-Forwarded-For ist ggf. eine komma-separierte Liste von IP-Adressen ("Client, Proxy1, Proxy2") mit der Client-IP als erstem Eintrag
                $ips = explode(',', $forwardedFor);
                $clientIP = trim($ips[0]);
            }
        }
        return $clientIP;
    }


    /**
     * Bestimmt den Wert eines Cookies. Wird der Wert nicht sofort gefunden, so wird dieser unter
     * Berücksichtigung von Trusted Proxies gesucht.
     *
     * @param string $name            der Name des Cookies
     * @param array $trustedProxies   die Liste der vertrauenswürdigen Proxies
     *
     * @return string | null der Wert oder null, falls kein Wert für den Cookie bestimmt werden konnte
     */
    public static function getCookie(string $name, array $trustedProxies): ?string {
        // Prüfe zunächst, ob der Cookie-Header befüllt wurde
        if (isset($_COOKIE[$name])) {
            return $_COOKIE[$name];
        }

        // Wenn nicht dann prüfe zur Vermeidung von potentiellen Angriffen, ob ein Trusted Proxy verwendet wurde
        $remoteAddr = $_SERVER['REMOTE_ADDR'] ?? '';
        if (in_array($remoteAddr, $trustedProxies)) {
            // Prüfe Apache/Nginx-spezifische Header, wenn wir dem Proxy vertrauen
            $headers = function_exists('getallheaders') ? getallheaders() : [];
            $cookieHeader = $headers['Cookie'] ?? $_SERVER['HTTP_COOKIE'] ?? '';

            // Wenn ein Header gefunden wurde, dann extrahiere den Cookie-Wert
            if (!empty($cookieHeader) && preg_match('/' . preg_quote($name, '/') . '=(?<value>[^;]+)/', $cookieHeader, $matches)) {
                return urldecode($matches['value']);
            }
        }
        return null;
    }


    /**
     * Setzt ein Hardened Cookie, welches als Fingerprint für den Client-Browser dient.
     *
     * @param string $name       der Name des Cookies
     * @param string $value      der Zufallswert (Nonce) im Klartext (nicht als Hash!)
     * @param int $timeExpires   der Zeitstempel in Sekunden seit dem 1.1.1970 (UTC), wann das Token abläuft - soll dem des JWT entsprechen
     * @param bool $isSecure     gibt an, ob die Verbindung über HTTPS läuft
     */
    public static function setHardenedCookie(string $name, string $value, int $timeExpires, bool $isSecure): void {
        setcookie($name, $value, [
            'expires'  => $timeExpires,
            'path'     => '/',         // bei einem __Host- Präfix muss dies '/' sein
            'domain'   => '',          // bei einem __Host- Präfix muss dies leer bleiben
            'secure'   => $isSecure,   // gibt an, ob das Cookie nur über HTTPS gesendet wird
            'httponly' => true,        // gibt an, dass das Cookie nicht für JavaScript sichtbar ist (Schutz vor XSS)
            'samesite' => 'Strict'     // zum Schutz vor Cross-Site-Request-Forgery (CSRF)
        ]);
    }


    /**
     * Prüft, ob die Verbindung sicher ist (HTTPS). Bei Trusted Proxies wird die Verbindung
     * auch als sicher akzeptiert, wenn HTTP_X_FORWARDED_PROTO auf https gesetzt ist.
     *
     * @param array $trustedProxies   die Liste der vertrauenswürdigen Proxies
     *
     * @return bool true, wenn die Verbindung als sicher angesehen wird
     */
    public static function isTrustedConnection(array $trustedProxies): bool {
        // Prüfe, ob die direkte Verbindung HTTPS unterstützt
        if (!empty($_SERVER['HTTPS']) && ($_SERVER['HTTPS'] !== 'off')) {
            return true;
        }

        // Prüfe, ob die Verbindung ab einem Trusted Proxy über https erfolgt
        $remoteAddr = $_SERVER['REMOTE_ADDR'] ?? '';
        if (in_array($remoteAddr, $trustedProxies)) {
            $forwardedProto = $_SERVER['HTTP_X_FORWARDED_PROTO'] ?? '';
            if (strcasecmp($forwardedProto, 'https') === 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * Liest den Body des HTTP-Requests ein. Tritt dabei ein Fehler auf, so wird
     * eine HTTP-Response 400 generiert.
     *
     * @return string der Body des HTTP-Requests
     */
    public static function getBody() : string {
        $body = file_get_contents("php://input");
        if ($body === false) {
            Http::exit400BadRequest("Fehler beim Lesen des HTTP-Body.");
        }
        return $body;
    }

    /**
     * Liest den Body des HTTP-Requests ein. Tritt dabei ein Fehler auf, so wird
     * eine HTTP-Response 400 generiert.
     *
     * @return object der Body des HTTP-Requests
     */
    public static function getBodyJsonObject() : object {
        try {
            $obj = json_decode(Http::getBody(), false);
            if (!is_object($obj)) {
                Http::exit400BadRequest("Fehler beim Dekodieren des JSON-Strings des HTTP-Body.");
            }
            return $obj;
        } catch (ValueError $e) {
            Http::exit400BadRequest("Fehler beim Dekodieren des JSON-Strings des HTTP-Body (".$e->getCode()."): ".$e->getMessage());
        }
    }


    /**
     * Gibt für den übergebenen Fehler eines Multipart-Uploads einen Fehlertext zurück.
     *
     * @param mixed $err  der Fehler
     *
     * @return string der Fehlertext
     */
    private static function getUploadError(mixed $err) : string {
        $str = "";
        switch ($err) {
            case 1: // UPLOAD_ERR_INI_SIZE
                $str = "Fehler beim Upload der Datei: Die Datei ist größer als die maximal erlaubte Dateigröße. Der Wert sollte in der php.ini angepasst werden.";
                break;
            case 2: // UPLOAD_ERR_FORM_SIZE
                $str = "Fehler beim Upload der Datei: Die Datei ist größer als die maximal erlaubte Dateigröße, welche in der HTML form als MAX_FILE_SIZE angegeben wurde.";
                break;
            case 3: // UPLOAD_ERR_PARTIAL
                $str = "Fehler beim Upload der Datei: Die Datei wurde nur teilweise hochgeladen.";
                break;
            case 4: // UPLOAD_ERR_NO_FILE
                $str = "Fehler beim Upload der Datei: Es wurde keine Datei hochgeladen.";
                break;
            case 6: // UPLOAD_ERR_NO_TMP_DIR
                $str = "Fehler beim Upload der Datei: In der php.ini wurde kein temporäres Verzeichnis spezifiziert.";
                break;
            case 7: // UPLOAD_ERR_CANT_WRITE
                $str = "Fehler beim Upload der Datei: Es konnte nicht auf das Dateisystem geschrieben werden.";
                break;
            case 8: // UPLOAD_ERR_EXTENSION
                $str = "Fehler beim Upload der Datei: Die PHP extension hat den Upload gestoppt.";
                break;
            default:
                $str = "Fehler beim Upload der Datei: Unbekannter Fehlercode {$err}.";
        }
        return $str;
    }


    /**
     * Ermittelt den Namen der temporären Datei, welche für einen Http-Request in einem Multipart
     * Body übergeben wurde.
     *
     * @param string $name   der Name der Datei in der Form-Daten des Multipart
     *
     * @return string der Pfad zu der temporären Datei, in dem die Datei zwischengespeichert ist
     */
    public static function getMultipartTmpFilename(string $name) : string {
        $contentType = $_SERVER["CONTENT_TYPE"] ?? null;
        if ($contentType == null) {
            Http::exit400BadRequest("Fehler im HTTP-Header: Content Type ist nicht angegeben.");
        }
        $contentType = trim(explode(";", $contentType)[0]);
        if (strcmp($contentType, "multipart/form-data") != 0) {
            Http::exit400BadRequest("Fehler im HTTP-Header: Content Type ist nicht 'multipart/form-data'.");
        }

        $file = $_FILES[$name] ?? null;
        if ($file == null) {
            Http::exit400BadRequest("Fehler in der Anfrage: Die Anfrage muss einen Datei-Anhang mit dem Namen '$name' enthalten.");
        }
        if ($file["error"] !== 0) { // nicht UPLOAD_ERROR_OK -> Gib den Fehler zurück
            Http::exit400BadRequest(Http::getUploadError($file["error"]));
        }
        if ($file["tmp_name"] == null) {
            Http::exit400BadRequest("Fehler beim Upload der Datei: Es ist keine temporäre Datei vorhanden.");
        }
        return $file["tmp_name"];
    }

    /**
     * Ermittelt den Inhalt der angegebenen Datei aus dem Multipart-Body des HTTP-Requests,
     * dekomprimiert deren GZIP-komprimierten Inhalt und gibt dies zurück.
     *
     * @param string $name   der Name der Datei in der Form-Daten des Multipart
     *
     * @return string der Inhalt der Datei
     */
    public static function getMultipartGzipFileContent(string $name) : string {
        $tmpFilename = Http::getMultipartTmpFilename($name);
        $content = "";
        $zd = @gzopen($tmpFilename, "r");
        if ($zd === false) {
            Http::exit400BadRequest("Fehler beim Upload der Datei: Die Datei ist nicht im gzip-Format.");
        }

        $content = "";
        while (!gzeof($zd)) {
            $data = gzread($zd, 1000000);
            if ($data === false) {
                break;
            }
            $content .= $data;
        }
        if ($content === "") {
            Http::exit400BadRequest("Fehler beim Upload der Datei: Die gzip-Datei konnte nicht gelesen werden.");
        }
        $success = gzclose($zd);
        if (!$success) {
            Http::exit500("Fehler beim Upload der Datei: Die gzip-Datei konnte nicht erfolgreich geschlossen werden.");
        }
        return $content;
    }


    /**
     * Prüft, ob der HTTP-Request-Header angibt, dass eine GZip-Komprimierung unterstützt oder nicht.
     *
     * @return bool true, wenn der Header angibt, dass GZip unterstützt wird
     */
    public static function checkAcceptGZipEncoding(): bool {
        return strpos($_SERVER['HTTP_ACCEPT_ENCODING'] ?? '', 'gzip') !== false;
    }


    /**
     * Gibt einen NO_CONTENT (204) zurück und beendet das PHP-Skript.
     */
    public static function exit204NoContent(?string $header = null): never {
        header("Content-Length: 0");
        http_response_code(204);
        if ($header != null) {
            header($header);
        }
        exit;
    }

    /**
     * Gibt einen OK (200) für JSON-Daten ggf. mit Daten für ein echo zurück und beendet das PHP-Skript.
     *
     * @param ?string $data   die Daten, welche ggf. noch ausgegeben werden
     */
    public static function exit200OKJson(?string $data = null): never {
        header('Content-Type: application/json; charset=utf-8');
        http_response_code(200);
        if ($data != null) {
            echo $data;
        }
        exit;
    }

    /**
     * Gibt einen OK (200) für GZip-Daten aus und beendet das PHP-Skript.
     *
     * @param string $data   die Daten, welche als GZip in der Nachricht zurückzugeben werden
     */
    public static function exit200OKGZipJson(string $data): never {
        header('Content-Encoding: gzip');
        header('Content-Type: application/json; charset=utf-8');
        echo gzencode($data, 5);
        http_response_code(200);
        exit;
    }


    /**
     * Gibt einen OK (200) für GZip-Daten aus und beendet das PHP-Skript.
     *
     * @param string $data   die Daten, welche als GZip in der Nachricht zurückzugeben werden
     */
    public static function exit200OKGZip(string $data): never {
        header('Content-Type: application/gzip;');
        echo gzencode($data, 5);
        http_response_code(200);
        exit;
    }

    /**
     * Gibt ein Accepted (202) für JSON-Daten ggf. mit Daten für ein echo zurück und beendet das PHP-Skript.
     *
     * @param ?string $data   die Daten, welche ggf. noch ausgegeben werden
     */
    public static function exit202AcceptedJson(?string $data = null): never {
        header('Content-Type: application/json; charset=utf-8');
        http_response_code(202);
        if ($data != null) {
            echo $data;
        }
        exit;
    }

    /**
     * Gibt einen BAD_REQUEST (400) zurück und beendet das PHP-Skript.
     *
     * @param ?string $msg   ein optionaler Parameter, um eine Nachricht als plain text zurückzugeben
     */
    public static function exit400BadRequest(?string $msg = null): never {
        http_response_code(400);
        if ($msg != null) {
            header('Content-Type: text/plain; charset=utf-8');
            echo $msg;
        }
        exit;
    }

    /**
     * Gibt einen UNAUTHORIZED (401) zurück und beendet das PHP-Skript.
     */
    public static function exit401Unauthorized(?string $headerinfo = null): never {
        http_response_code(401);
        if ($headerinfo != null) {
            header($headerinfo);
        }
        exit;
    }

    /**
     * Gibt einen UNAUTHORIZED (401) zurück und beendet das PHP-Skript.
     */
    public static function exit401UnauthorizedRealm() {
        Http::exit401Unauthorized('WWW-Authenticate: Basic realm="ENM-Server", charset="UTF-8"');
    }


   /**
     * Gibt einen UNAUTHORIZED (401) Fehler mit einer standardisierten JSON-Fehlermeldung (z.B. SESSION_INVALIDATED)
     * zurück und beendet das Skript.
     *
     * @param string $error         der Fehlercode
     * @param string | null $msg    eine optionale Fehlertext
     */
    public static function exit401UnauthorizedJson(string $error, ?string $msg = null): never {
        http_response_code(401);
        header('Content-Type: application/json; charset=utf-8');

        $response = ['error' => $error];
        if ($msg !== null) {
            $response['message'] = $msg;
        }
        
        echo json_encode($response, JSON_UNESCAPED_SLASHES);
        exit;
    }


    /**
     * Gibt einen FORBIDDEN (403) zurück und beendet das PHP-Skript.
     *
     * @param ?string $msg   ein optionaler Parameter, um eine Nachricht als plain text zurückzugeben
     */
    public static function exit403Forbidden(?string $msg = null): never {
        http_response_code(403);
        if ($msg != null) {
            header('Content-Type: text/plain; charset=utf-8');
            echo $msg;
        }
        exit;
    }

    /**
     * Gibt einen METHOD_NOT_ALLOWED (405) zurück und beendet das PHP-Skript.
     */
    public static function exit405MethodNotAllowed(): never {
        http_response_code(405);
        exit;
    }

    /**
     * Gibt einen NOT_FOUND (404) zurück und beendet das PHP-Skript.
     *
     * @param string $msg   ein optionaler Parameter, um eine Nachricht als plain text zurückzugeben
     */
    public static function exit404NotFound(?string $msg = null): never {
        http_response_code(404);
        if ($msg != null) {
            header('Content-Type: text/plain; charset=utf-8');
            echo $msg;
        }
        exit;
    }

    /**
     * Gibt einen TOO MANY REQUESTS (429) zurück und beendet das PHP-Skript.
     *
     * @param string $msg   ein optionaler Parameter, um eine Nachricht als plain text zurückzugeben
     */
    public static function exit429TooManyRequests(?string $msg = null): never {
        http_response_code(429);
        if ($msg != null) {
            header('Content-Type: text/plain; charset=utf-8');
            echo $msg;
        }
        exit;
    }

    /**
     * Gibt einen INTERNAL_SERVER_ERROR (500) mit der übergebenen Nachricht zurück und
     * beendet das PHP-Skript.
     *
     * @param string $err   die Fehlermeldung
     */
    public static function exit500(string $err): never {
        if (!headers_sent()) {
            http_response_code(500);
            header('Content-Type: text/plain; charset=utf-8');
        }
        echo $err;
        exit;
    }

}

