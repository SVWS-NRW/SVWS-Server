<?php

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
		 * Liest den Body des HTTP-Requests ein. Tritt dabei ein Fehler auf, so wird
		 * eine HTTP-Response 400 generiert.
		 *
		 * @return string der Body des HTTP-Requests
		 */
		public static function getBody() : string {
			$body = file_get_contents("php://input");
			if ($body === false)
				Http::exit400BadRequest("Fehler beim Lesen des HTTP-Body.");
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
				$obj = json_decode(HTTP::getBody(), false);
				if (!is_object($obj))
					Http::exit400BadRequest("Fehler beim Dekodieren des JSON-Strings des HTTP-Body.");
				return $obj;
			} catch (ValueError $e) {
				Http::exit400BadRequest("Fehler beim Dekodieren des JSON-Strings des HTTP-Body (" + $e->getCode() + "): " + $e->getMessage());
			}
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
			if ($contentType == null)
				Http::exit400BadRequest("Fehler im HTTP-Header: Content Type ist nicht angegeben.");
			$contentType = trim(explode(";", $contentType)[0]);
			if (strcmp($contentType, "multipart/form-data") != 0)
				Http::exit400BadRequest("Fehler im HTTP-Header: Content Type ist nicht 'multipart/form-data'.");
		
			$file = $_FILES[$name];
			if ($file == null)
				Http::exit400BadRequest("Fehler in der Anfrage: Die Anfrage muss einen Datei-Anhang mit dem Namen '$name' enthalten.");
			switch ($file["error"]) {
				case 0: // UPLOAD_ERROR_OK
					break;
				case 1: // UPLOAD_ERR_INI_SIZE
					Http::exit400BadRequest("Fehler beim Upload der Datei: Die Datei ist größer als die maximal erlaubte Dateigröße. Der Wert sollte in der php.ini angepasst werden.");
				case 2: // UPLOAD_ERR_FORM_SIZE
					Http::exit400BadRequest("Fehler beim Upload der Datei: Die Datei ist größer als die maximal erlaubte Dateigröße, welche in der HTML form als MAX_FILE_SIZE angegeben wurde.");
				case 3: // UPLOAD_ERR_PARTIAL
					Http::exit400BadRequest("Fehler beim Upload der Datei: Die Datei wurde nur teilweise hochgeladen.");
				case 4: // UPLOAD_ERR_NO_FILE
					Http::exit400BadRequest("Fehler beim Upload der Datei: Es wurde keine Datei hochgeladen.");
				case 6: // UPLOAD_ERR_NO_TMP_DIR
					Http::exit400BadRequest("Fehler beim Upload der Datei: In der php.ini wurde kein temporäres Verzeichnis spezifiziert.");
				case 7: // UPLOAD_ERR_CANT_WRITE
					Http::exit400BadRequest("Fehler beim Upload der Datei: Es konnte nicht auf das Dateisystem geschrieben werden.");
				case 8: // UPLOAD_ERR_EXTENSION
					Http::exit400BadRequest("Fehler beim Upload der Datei: Die PHP extension hat den Upload gestoppt.");
				default:
					Http::exit400BadRequest("Fehler beim Upload der Datei: Unbekannter Fehlercode ".$file["error"].".");
			}
			if ($file["tmp_name"] == null)
				Http::exit400BadRequest("Fehler beim Upload der Datei: Es ist keine temporäre Datei vorhanden.");
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
			if ($zd == false)
				Http::exit400BadRequest("Fehler beim Upload der Datei: Die Datei ist nicht im gzip-Format.");
			while (!gzeof($zd))
				$content .= gzread($zd, 1000000);
			if (strcmp($content, "") === 0)
				Http::exit400BadRequest("Fehler beim Upload der Datei: Die gzip-Datei konnte nicht gelesen werden.");
			$success = gzclose($zd);
			if ($success == false)
				Http::exit500("Fehler beim Upload der Datei: Die gzip-Datei konnte nicht erfolgreich geschlossen werden.");
			return $content;
		}

		/**
		 * Prüft on die HTTP-Methode von Typ OPTIONS
		 */
		public static function checkCORS() {
			if (strcasecmp($_SERVER['REQUEST_METHOD'], "OPTIONS") === 0) {
				$cors_sec_fetch_site = null;
				$cors_sec_fetch_mode = null;
				foreach (getallheaders() as $name => $value) {
					if (strcasecmp($name, "Sec-Fetch-Mode") === 0)
						$cors_sec_fetch_mode = $value;
					if (strcasecmp($name, "Sec-Fetch-Site") === 0)
						$cors_sec_fetch_site = $value;
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
			if ($headerinfo != null)
				header($headerinfo);
			exit;
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

?>