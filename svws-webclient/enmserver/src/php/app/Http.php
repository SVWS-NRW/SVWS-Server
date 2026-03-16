<?php

namespace wenom;

use \ValueError as ValueError;

$inc_memory_limit_success = ini_set('memory_limit', '1024M');
if ($inc_memory_limit_success === false) {
    $inc_memory_limit_success = ini_set('memory_limit', '768M');
    if ($inc_memory_limit_success === false) {
        $inc_memory_limit_success = ini_set('memory_limit', '512M');
        if ($inc_memory_limit_success === false) {
            $inc_memory_limit_success = ini_set('memory_limit', '256M');
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
            Http::exit400BadRequest("Fehler beim Dekodieren des JSON-Strings des HTTP-Body (" + $e->getCode() + "): " + $e->getMessage());
        }
    }


    /**
     * Gibt für den übergebenen Fehler eines Multipart-Uploads einen Fehlertext zurück.
     *
     * @param mixed   der Fehler
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
        $contentType = $_SERVER["CONTENT_TYPE"];
        if ($contentType == null) {
            Http::exit400BadRequest("Fehler im HTTP-Header: Content Type ist nicht angegeben.");
        }
        $contentType = trim(explode(";", $contentType)[0]);
        if (strcmp($contentType, "multipart/form-data") != 0) {
            Http::exit400BadRequest("Fehler im HTTP-Header: Content Type ist nicht 'multipart/form-data'.");
        }

        $file = $_FILES[$name];
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
        $zd = gzopen($tmpFilename, "r");
        if ($zd == false) {
            Http::exit400BadRequest("Fehler beim Upload der Datei: Die Datei ist nicht im gzip-Format.");
        }
        while (!gzeof($zd)) {
            $content .= gzread($zd, 1000000);
        }
        if (strcmp($content, "") === 0) {
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
     * Prüft on die HTTP-Methode von Typ OPTIONS
     */
    public static function checkCORS() {
        if (strcasecmp($_SERVER['REQUEST_METHOD'], "OPTIONS") === 0) {
            $cors_sec_fetch_site = null;
            $cors_sec_fetch_mode = null;
            foreach (getallheaders() as $name => $value) {
                if (strcasecmp($name, "Sec-Fetch-Mode") === 0) {
                    $cors_sec_fetch_mode = $value;
                }
                if (strcasecmp($name, "Sec-Fetch-Site") === 0) {
                    $cors_sec_fetch_site = $value;
                }
            }
            if (strcasecmp($cors_sec_fetch_mode, "cors") === 0) {
                http_response_code(204);
                exit;
            }
        }
    }

    /**
     * Gibt einen BAD_REQUEST (400) zurück und beendet das PHP-Skript.
     *
     * @param ?string msg   ein optionaler Parameter, um eine Nachricht als plain text zurückzugeben
     */
    public static function exit400BadRequest(?string $msg = null) {
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
    public static function exit401Unauthorized(?string $headerinfo = null) {
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
     * Gibt einen FORBIDDEN (403) zurück und beendet das PHP-Skript.
     */
    public static function exit403Forbidden() {
        http_response_code(403);
        exit;
    }

    /**
     * Gibt einen NOT_FOUND (404) zurück und beendet das PHP-Skript.
     *
     * @param string msg   ein optionaler Parameter, um eine Nachricht als plain text zurückzugeben
     */
    public static function exit404NotFound(?string $msg = null) {
        http_response_code(404);
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
    public static function exit500(string $err) {
        http_response_code(500);
        echo $err;
        exit;
    }

}

