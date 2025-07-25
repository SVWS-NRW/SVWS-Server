<?php

	require_once 'Config.php';
	require_once 'SMTPClient.php';

	/**
	 * Diese Klasse dient dem Zugriff auf die SQLite-Datenbank aus der Konfiguration.
	 */
	class Database {

		// Die Konfiguration der Anwendung
		public $config;

		// Die Datenbank-Verbindung
		public $conn;

		/**
		 * Erstellt eine neue Verbindung zu der SQLite-Datenbank, welche in der übergebenen Konfiguration
		 * angegeben ist. Existiert diese Datenbank noch nicht, so wird sie mit Default-Werten initialisiert.
		 */
		public function __construct(Config $config) {
			$this->config = $config;
			// Prüfe, ob die Datenbank bereits existiert. Wenn nicht, dann lege eine neue mit Default-Werten an
			$dbPath = $config->getAppRoot()."/".$config->getDatabaseFile();
			$dbNeedsInitialization = file_exists($dbPath);
			try {
				$this->conn = new PDO("sqlite:".$dbPath);
				$this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
			} catch (PDOException $e) {
				Http::exit500("Database (".$config->getDatabaseFile().") - Fehler beim Öffnen (".$e->getCode()."): ".$e->getMessage());
			}
			if (!$dbNeedsInitialization)
				$this->initDatabase();
		}

		/**
		 * Erstellt eine Tabelle mit dem übergebenen SQL-Befehl
		 *
		 * @param string $tablename   der Name der Tabelle
		 * @param string $sql         der SQL-Befehl
		 */
		protected function createTable(string $tablename, string $sql): void {
			try {
				$this->conn->exec($sql);
			} catch (PDOException $e) {
				Http::exit500("Database (".$this->config->getDatabaseFile().") - Fehler beim Erstellen der Tabelle $tablename (".$e->getCode()."): ".$e->getMessage());
			}
		}

		/**
		 * Leert die Tabelle mit dem übergegebenen Namen.
		 *
		 * @param string $tablename   der Name der Tabelle
		 */
		protected function clearTable(string $tablename): void {
			try {
				$this->conn->exec("DELETE FROM $tablename");
			} catch (PDOException $e) {
				Http::exit500("Database (".$this->config->getDatabaseFile().") - Fehler beim Leeren der Tabelle $tablename (".$e->getCode()."): ".$e->getMessage());
			}
		}

		/**
		 * Aktualisiere die Tabelle mit dem übergegebenen Namen und dem übergebenen Befehl.
		 *
		 * @param string $tablename   der Name der Tabelle
		 * @param string $sqlpart     der Teil des SQL-Befehls hinter dem SET
		 */
		protected function updateSet(string $tablename, string $sqlpart): void {
			try {
				$sql = "UPDATE $tablename SET ".$sqlpart;
				$this->conn->exec($sql);
			} catch (PDOException $e) {
				Http::exit500("Database (".$this->config->getDatabaseFile().") - Fehler beim Aktualisieren der Tabelle $tablename (".$e->getCode()."): ".$e->getMessage()." - Befehl: '$sql'");
			}
		}

		/**
		 * Fügt Daten mithilfe des übergebenen SQL-Strings in die Tabelle mit dem übergegebenen Namen ein.
		 *
		 * @param string $tablename   der Name der Tabelle
		 * @param string $sql         der SQL-Befehl
		 */
		public function insertInto(string $tablename, string $sql): void {
			try {
				$this->conn->exec($sql);
			} catch (PDOException $e) {
				Http::exit500("Database (".$this->config->getDatabaseFile().") - Fehler beim Schreiben in die Tabelle $tablename (".$e->getCode()."): ".$e->getMessage()." - Befehl: '$sql'");
			}
		}

		/**
		 * Entfernt Daten aus der Tabelle mit dem übergebenen Namen und der übergebenen Lösch-Bedingung
		 *
		 * @param string $tablename   der Tabellenname
		 * @param string $bedingung   die Lösch-Bedingung
		 */
		public function dropFrom(string $tablename, string $bedingung): void {
			try {
				$sql = "DELETE FROM $tablename WHERE $bedingung";
				$this->conn->exec($sql);
			} catch (PDOException $e) {
				Http::exit500("Database (".$this->config->getDatabaseFile().") - Fehler beim Entfernen von Daten aus der Tabelle $tablename (".$e->getCode()."): ".$e->getMessage()." - Befehl: '$sql'");
			}
		}

		/**
		 * Initialisiert die Datenbank mit Default-Werten
		 */
		protected function initDatabase(): void {
			$this->createTable('OAuth', 'CREATE TABLE OAuth(clientID INTEGER PRIMARY KEY, token TEXT, tokenTimestamp INTEGER, tokenValidForSecs INTEGER)');
			$this->insertInto('OAuth', "INSERT INTO OAuth(clientID, token, tokenTimestamp, tokenValidForSecs) VALUES (1, NULL, NULL, NULL)");
			$this->createTable('ServerConfig', 'CREATE TABLE ServerConfig(schluessel TEXT PRIMARY KEY, wert TEXT)');
			$this->createTable('ClientConfig', 'CREATE TABLE ClientConfig(schluessel TEXT PRIMARY KEY, wert TEXT)');
			$this->createTable('ClientLehrerConfig', 'CREATE TABLE ClientLehrerConfig(idLehrer INTEGER, schluessel TEXT, wert TEXT, PRIMARY KEY (idLehrer, schluessel))');
			$this->createTable('Daten', 'CREATE TABLE Daten(ts INTEGER PRIMARY KEY, schulnummer INTEGER, daten TEXT)');
			$this->createTable('Schueler', 'CREATE TABLE Schueler(id INTEGER, ts INTEGER, idJahrgang INTEGER, idKlasse INTEGER, daten TEXT, tsFehlstundenGesamt TEXT, tsFehlstundenGesamtUnentschuldigt TEXT, tsASV TEXT, tsAUE TEXT, tsZB TEXT, tsLELS TEXT, tsSchulformEmpf TEXT, tsIndividuelleVersetzungsbemerkungen TEXT, tsFoerderbemerkungen TEXT, PRIMARY KEY(id, ts))');
			$this->createTable('Leistungsdaten', 'CREATE TABLE Leistungsdaten(id INTEGER, ts INTEGER, idSchueler INTEGER, idLerngruppe INTEGER, daten TEXT, tsNote TEXT, tsNoteQuartal TEXT, tsFehlstundenFach TEXT, tsFehlstundenUnentschuldigtFach TEXT, tsFachbezogeneBemerkungen TEXT, tsIstGemahnt TEXT, PRIMARY KEY(id, ts))');
			$this->createTable('Teilleistungen', 'CREATE TABLE Teilleistungen(id INTEGER, ts INTEGER, idLeistung INTEGER, daten TEXT, tsArtID TEXT, tsDatum TEXT, tsBemerkung TEXT, tsNote TEXT, PRIMARY KEY(id, ts))');
			$this->createTable('Ankreuzkompetenzen', 'CREATE TABLE Ankreuzkompetenzen(id INTEGER, ts INTEGER, idSchueler INTEGER, idKompetenz INTEGER, daten TEXT, tsStufe TEXT, PRIMARY KEY(id, ts))');
			$this->createTable('Sprachenfolge', 'CREATE TABLE Sprachenfolge(id INTEGER, sprache TEXT, ts INTEGER, idSchueler INTEGER, daten TEXT, PRIMARY KEY (id, sprache, ts))');
			$this->createTable('Lehrer', 'CREATE TABLE Lehrer(id INTEGER, ts INTEGER, daten TEXT, eMailDienstlich TEXT, passwordHash TEXT, tsPasswordHash TEXT, PRIMARY KEY(id, ts))');
			$this->createTable('Lehrertoken', 'CREATE TABLE Lehrertoken(idLehrer INTEGER PRIMARY KEY, token TEXT, tokenTimestamp INTEGER, tokenValidForSecs INTEGER)');
		}

		/**
		 * Reinitialisiert die Datenbank, indem das Client-Secret neu gesetzt wird und die vorhanden ENM-Daten gelöscht werden.
		 */
		public function reinitDatbase(): void {
			$this->clearENMDaten();
			$this->clearTable('OAuth');
			$this->insertInto('OAuth', "INSERT INTO OAuth(clientID, token, tokenTimestamp, tokenValidForSecs) VALUES (1, NULL, NULL, NULL)");
		}

		/**
		 * Beendet die Datenbankverbindung.
		 * @return void
 		 */
		public function __destruct() {
			$this->conn = null;
		}

		/**
		 * Führt die übergebene SQL-Anfrage aus und gibt alle Ergebnisse als Objekte zurück.
		 * Im Fehlerfall wird null zurückgegeben.
		 *
		 * @param string $sql   die SQL-Anfrage
		 *
		 * @return array | null   ein Array mit Objekten oder null
		 */
		public function queryAllOrNull(string $sql): array | null {
			try {
				return $this->conn->query($sql, PDO::FETCH_OBJ)->fetchAll(PDO::FETCH_OBJ);
			} catch (PDOException $e) {
				return null;
			}
		}

		/**
		 * Führt die übergebene SQL-Anfrage aus und gibt alle Ergebnisse als Objekte zurück.
		 * Im Fehlerfall wird ein 500er Response Code erzeugt
		 *
		 * @param string $sql     die SQL-Anfrage
		 * @param string $error   ein
		 *
		 * @return array   ein Array mit Objekten
		 */
		public function queryAllOrExit500(string $sql, string $error): array {
			try {
				return $this->conn->query($sql, PDO::FETCH_OBJ)->fetchAll(PDO::FETCH_OBJ);
			} catch (PDOException $e) {
				Http::exit500("Database (".$this->config->getDatabaseFile().") - ".$error." (".$e->getCode()."): ".$e->getMessage());
			}
		}

		/**
		 * Führt die übergebene SQL-Anfrage aus und gibt das Ergebnis als Objekt zurück.
		 * Im Fehlerfall wird  null zurückgegeben.
		 *
		 * @param string $sql   die sql-Anfrage
		 *
		 * @return object | null   das Objekt oder null
		 */
		public function querySingleOrNull(string $sql): object | null {
			try {
				return $this->conn->query($sql)->fetchObject();
			} catch (PDOException $e) {
				return null;
			}
		}

		/**
		 * Erstellt ein neues Token und gibt dieses zurück. Ein zuvor bestendes Token wird dabei ersetzt.
		 *
		 * @param int $id   die ID für welche der Access-Token generiert werden soll
		 *
		 * @return object die Informationen zum Token oder null, wenn das Erstellen nicht erfolgreich war
		 */
		public function createClientAccessToken(int $id): object | null {
			$token = Config::generateRandomSecret();
			$time = time();
			$validFor = 3600; // eine Stunde (in Sekunden)
			try {
				$this->conn->exec("UPDATE OAuth SET token='$token', tokenTimestamp=$time, tokenValidForSecs=$validFor WHERE clientID = $id");
			} catch (PDOException $e) {
				return null;
			}
			return (object)[
				'token_type' => 'Bearer',
				'access_token' => $token,
				'expires_in' => $validFor,
			];
		}

		/**
		 * Bestimmt den Client-Eintrag anhand des übergebenen Access-Tokens und gibt diesen zurück.
		 *
		 * @param string $token   das Token anhand welchem der Client-Eintrag ermittelt werden soll
		 *
		 * @return object der Client-Eintrag oder null, falls keiner gefunden wird
		 */
		public function getClientByAccessToken(string | null $token): object | null {
			if ($token == null)
				return null;
			try {
				// Verwende $token nicht direkt, um SQL-Injection zu verhindern
				$stmt = $this->conn->query("SELECT clientID, token, tokenTimestamp, tokenValidForSecs FROM OAuth", PDO::FETCH_OBJ);
				foreach ($stmt->fetchAll(PDO::FETCH_OBJ) as $row) {
					if ($row->token == null)
						continue;
					if (strcmp($row->token, $token) == 0)
						return $row;
				}
				return null;
			} catch (PDOException $e) {
				return null;
			}
		}

		/**
		 * Entfernt aus allen Tabellen mit bestehenden ENM-Daten, die Daten, welche nicht den
		 * angegebenen Zeitstempel tragen. Daten mit dem Zeitstempel werden also dabei erhalten.
		 *
		 * @param int $ts   der Zeitstempel
		 */
		public function retainENMDaten(int $ts): void {
			$this->dropFrom('Daten', "ts <> $ts");
			$this->dropFrom('Schueler', "ts <> $ts");
			$this->dropFrom('Leistungsdaten', "ts <> $ts");
			$this->dropFrom('Teilleistungen', "ts <> $ts");
			$this->dropFrom('Ankreuzkompetenzen', "ts <> $ts");
			$this->dropFrom('Sprachenfolge', "ts <> $ts");
			$this->dropFrom('Lehrer', "ts <> $ts");
		}

		/**
		 * Leert alle Tabellen mit bestehenden ENM-Daten. Die Client-Credentials bleiben dabei erhalten
		 */
		public function clearENMDaten(): void {
			$this->clearTable('Daten');
			$this->clearTable('Schueler');
			$this->clearTable('Leistungsdaten');
			$this->clearTable('Teilleistungen');
			$this->clearTable('Ankreuzkompetenzen');
			$this->clearTable('Sprachenfolge');
			$this->clearTable('Lehrer');
		}


		/**
		 * Schreibe das ENM-Datenobjekt in die Datenbank.
		 *
		 * @param int $ts            der Zeitstempel, dem die Daten zugeordnet werden
		 * @param object $enmDaten   die zu schreibenden ENM-Daten
		 */
		public function writeENMDaten(int $ts, object $enmDaten): void {
			$jsonEnmDaten = json_encode($enmDaten, JSON_UNESCAPED_SLASHES);
			$this->insertInto("Daten", "INSERT INTO Daten(ts, schulnummer, daten) VALUES ($ts, $enmDaten->schulnummer, '$jsonEnmDaten')");
		}

		/**
		 * Beginnt eine Transaktion. Ist dies nicht erfolgreich, so wird ein Fehlercode 500 ausgeführt.
		 */
		public function beginTransaction(): void {
			try {
				$this->conn->beginTransaction();
			} catch (PDOException $e) {
				Http::exit500("Database (".$this->config->getDatabaseFile().") - Fehler beim Erstellen der Transaction (".$e->getCode()."): ".$e->getMessage());
			}
		}

		/**
		 * Führt bei einer Transaktion einen Commit aus.
		 * Ist dies nicht erfolgreich, so wird ein Fehlercode 500 ausgeführt.
		 */
		public function commitTransaction(): void {
			try {
				$this->conn->commit();
			} catch (PDOException $e) {
				Http::exit500("Database (".$this->config->getDatabaseFile().") - Fehler beim Commit der Transaction (".$e->getCode()."): ".$e->getMessage());
			}
		}

		/**
		 * Bereitet ein Statement mit dem übergenenen SQL-Befehl vor und gibt dieses zurück.
		 * Ist dies nicht erfolgreich, so wird ein Fehlercode 500 ausgeführt.
		 *
		 * @return PDOStatement   das Statement
		 */
		public function prepareStatement(string $sql): PDOStatement {
			try {
				return $this->conn->prepare($sql);
			} catch (PDOException $e) {
				Http::exit500("Database (".$this->config->getDatabaseFile().") - Fehler beim Aufruf von prepare (".$e->getCode()."): ".$e->getMessage());
			}
		}

		/**
		 * Bindet den Wert des Parameters an das Statement.
		 * Ist dies nicht erfolgreich, so wird ein Fehlercode 500 ausgeführt.
		 *
		 * @param PDOStatement $statement   das Statement
		 * @param string $param             der Parameter, z.B. ':id'
		 * @param mixed $value              der Wert
		 * @param int $type                 der PDO-Datentyp des Parameters
		 */
		public function bindStatementValue(PDOStatement $statement, string $param, mixed $value, int $type): void {
			try {
				$statement->bindValue($param, $value, $type);
			} catch (PDOException $e) {
				Http::exit500("Database (".$this->config->getDatabaseFile().") - Fehler bei bindValue mit '$param' (".$e->getCode()."): ".$e->getMessage());
			}
		}

		/**
		 * Führt das übergebene Statement aus.
		 * Ist dies nicht erfolgreich, so wird ein Fehlercode 500 ausgeführt.
		 *
		 * @param PDOStatement $statement   das Statement
		 */
		public function executeStatement(PDOStatement $statement): void {
			try {
				$statement->execute();
			} catch (PDOException $e) {
				Http::exit500("Database (".$this->config->getDatabaseFile().") - Fehler beim Ausführen des Insert-Statements (".$e->getCode()."): ".$e->getMessage());
			}
		}

		/**
		 * Schreibe die ENM-Lehrer-Objekte in die Datenbank.
		 * Ist dies nicht erfolgreich, so wird ein Fehlercode 500 ausgeführt.
		 *
		 * @param int $ts            der Zeitstempel, dem die Daten zugeordnet werden
		 * @param array $enmLehrer   die zu schreibenden ENM-Lehrer-Daten
		 */
		public function writeENMLehrer(int $ts, array $enmLehrer): void {
			$this->beginTransaction();
			$stmt = $this->prepareStatement("INSERT INTO Lehrer(id, ts, daten, eMailDienstlich, passwordHash, tsPasswordHash) VALUES (:id, :ts, :daten, :email, :pw, :tspw)");
			foreach ($enmLehrer as $lehrer) {
				$jsonLehrer = json_encode($lehrer, JSON_UNESCAPED_SLASHES);
				$this->bindStatementValue($stmt, ":id", $lehrer->id, PDO::PARAM_INT);
				$this->bindStatementValue($stmt, ":ts", $ts, PDO::PARAM_INT);
				$this->bindStatementValue($stmt, ":daten", $jsonLehrer, PDO::PARAM_STR);
				$this->bindStatementValue($stmt, ":email", $lehrer->eMailDienstlich, PDO::PARAM_STR);
				$this->bindStatementValue($stmt, ":pw", $lehrer->passwordHash, PDO::PARAM_STR);
				$this->bindStatementValue($stmt, ":tspw", $lehrer->tsPasswordHash, PDO::PARAM_STR);
				$this->executeStatement($stmt);
			}
			$this->commitTransaction();
		}

		/**
		 * Schreibe die ENM-Schüler-Objekte in die Datenbank.
		 * Ist dies nicht erfolgreich, so wird ein Fehlercode 500 ausgeführt.
		 *
		 * @param int $ts              der Zeitstempel, dem die Daten zugeordnet werden
		 * @param array $enmSchueler   die zu schreibenden ENM-Schüler-Daten
		 */
		public function writeENMSchueler(int $ts, array $enmSchueler): void {
			$this->beginTransaction();
			$stmtSchueler = $this->prepareStatement("INSERT INTO Schueler(id, ts, idJahrgang, idKlasse, daten, tsFehlstundenGesamt, tsFehlstundenGesamtUnentschuldigt, tsASV, tsAUE, tsZB, tsLELS, tsSchulformEmpf, tsIndividuelleVersetzungsbemerkungen, tsFoerderbemerkungen) VALUES (:id, :ts, :idJahrgang, :idKlasse, :daten, :tsFehlstundenGesamt, :tsFehlstundenGesamtUnentschuldigt, :tsASV, :tsAUE, :tsZB, :tsLELS, :tsSchulformEmpf, :tsIndividuelleVersetzungsbemerkungen, :tsFoerderbemerkungen)");
			$stmtLeistung = $this->prepareStatement("INSERT INTO Leistungsdaten(id, ts, idSchueler, idLerngruppe, daten, tsNote, tsNoteQuartal, tsFehlstundenFach, tsFehlstundenUnentschuldigtFach, tsFachbezogeneBemerkungen, tsIstGemahnt) VALUES (:id, :ts, :idSchueler, :idLerngruppe, :daten, :tsNote, :tsNoteQuartal, :tsFehlstundenFach, :tsFehlstundenUnentschuldigtFach, :tsFachbezogeneBemerkungen, :tsIstGemahnt)");
			$stmtTeilleistung = $this->prepareStatement("INSERT INTO Teilleistungen(id, ts, idLeistung, daten, tsArtID, tsDatum, tsBemerkung, tsNote) VALUES (:id, :ts, :idLeistung, :daten, :tsArtID, :tsDatum, :tsBemerkung, :tsNote)");
			$stmtAnkreuzkomp = $this->prepareStatement("INSERT INTO Ankreuzkompetenzen(id, ts, idSchueler, idKompetenz, daten, tsStufe) VALUES (:id, :ts, :idSchueler, :idKompetenz, :daten, :tsStufe)");
			$stmtSprachenfolge = $this->prepareStatement("INSERT INTO Sprachenfolge(id, sprache, ts, idSchueler, daten) VALUES (:id, :sprache, :ts, :idSchueler, :daten)");
			foreach ($enmSchueler as $schueler) {
				// Erstelle den Schülereintrag mit einem JSON ohne Detaildaten zu der Sprachenfolge, den Leistungsdaten und den Ankreuzkompetenzen ...
				$tmpJsonSchueler = json_encode($schueler, JSON_UNESCAPED_SLASHES);
				$tmpSchueler = json_decode($tmpJsonSchueler);
				$tmpSchueler->leistungsdaten = [];
				$tmpSchueler->ankreuzkompetenzen = [];
				$tmpSchueler->sprachenfolge = [];
				$jsonSchueler = json_encode($tmpSchueler, JSON_UNESCAPED_SLASHES);
				$this->bindStatementValue($stmtSchueler, ":id", $schueler->id, PDO::PARAM_INT);
				$this->bindStatementValue($stmtSchueler, ":ts", $ts, PDO::PARAM_INT);
				$this->bindStatementValue($stmtSchueler, ":idJahrgang", $schueler->jahrgangID, PDO::PARAM_INT);
				$this->bindStatementValue($stmtSchueler, ":idKlasse", $schueler->klasseID, PDO::PARAM_INT);
				$this->bindStatementValue($stmtSchueler, ":daten", $jsonSchueler, PDO::PARAM_STR);
				$this->bindStatementValue($stmtSchueler, ":tsFehlstundenGesamt", $schueler->lernabschnitt->tsFehlstundenGesamt, PDO::PARAM_STR);
				$this->bindStatementValue($stmtSchueler, ":tsFehlstundenGesamtUnentschuldigt", $schueler->lernabschnitt->tsFehlstundenGesamtUnentschuldigt, PDO::PARAM_STR);
				$this->bindStatementValue($stmtSchueler, ":tsASV", $schueler->bemerkungen->tsASV, PDO::PARAM_STR);
				$this->bindStatementValue($stmtSchueler, ":tsAUE", $schueler->bemerkungen->tsAUE, PDO::PARAM_STR);
				$this->bindStatementValue($stmtSchueler, ":tsZB", $schueler->bemerkungen->tsZB, PDO::PARAM_STR);
				$this->bindStatementValue($stmtSchueler, ":tsLELS", $schueler->bemerkungen->tsLELS, PDO::PARAM_STR);
				$this->bindStatementValue($stmtSchueler, ":tsSchulformEmpf", $schueler->bemerkungen->tsSchulformEmpf, PDO::PARAM_STR);
				$this->bindStatementValue($stmtSchueler, ":tsIndividuelleVersetzungsbemerkungen", $schueler->bemerkungen->tsIndividuelleVersetzungsbemerkungen, PDO::PARAM_STR);
				$this->bindStatementValue($stmtSchueler, ":tsFoerderbemerkungen", $schueler->bemerkungen->tsFoerderbemerkungen, PDO::PARAM_STR);
				$this->executeStatement($stmtSchueler);
				// ... dann die Leistungsdaten
				foreach ($schueler->leistungsdaten as $leistung) {
					$tmpJsonLeistung = json_encode($leistung, JSON_UNESCAPED_SLASHES);
					$tmpLeistung = json_decode($tmpJsonLeistung);
					$tmpLeistung->teilleistungen = [];
					$jsonLeistung = json_encode($tmpLeistung, JSON_UNESCAPED_SLASHES);
					$this->bindStatementValue($stmtLeistung, ":id", $leistung->id, PDO::PARAM_INT);
					$this->bindStatementValue($stmtLeistung, ":ts", $ts, PDO::PARAM_INT);
					$this->bindStatementValue($stmtLeistung, ":idSchueler", $schueler->id, PDO::PARAM_INT);
					$this->bindStatementValue($stmtLeistung, ":idLerngruppe", $leistung->lerngruppenID, PDO::PARAM_INT);
					$this->bindStatementValue($stmtLeistung, ":daten", $jsonLeistung, PDO::PARAM_STR);
					$this->bindStatementValue($stmtLeistung, ":tsNote", $leistung->tsNote, PDO::PARAM_STR);
					$this->bindStatementValue($stmtLeistung, ":tsNoteQuartal", $leistung->tsNoteQuartal, PDO::PARAM_STR);
					$this->bindStatementValue($stmtLeistung, ":tsFehlstundenFach", $leistung->tsFehlstundenFach, PDO::PARAM_STR);
					$this->bindStatementValue($stmtLeistung, ":tsFehlstundenUnentschuldigtFach", $leistung->tsFehlstundenUnentschuldigtFach, PDO::PARAM_STR);
					$this->bindStatementValue($stmtLeistung, ":tsFachbezogeneBemerkungen", $leistung->tsFachbezogeneBemerkungen, PDO::PARAM_STR);
					$this->bindStatementValue($stmtLeistung, ":tsIstGemahnt", $leistung->tsIstGemahnt, PDO::PARAM_STR);
					$this->executeStatement($stmtLeistung);
					// ... mit den Teilleistungen
					foreach ($leistung->teilleistungen as $teilleistung) {
						$jsonTeilleistung = json_encode($teilleistung, JSON_UNESCAPED_SLASHES);
						$this->bindStatementValue($stmtTeilleistung, ":id", $teilleistung->id, PDO::PARAM_INT);
						$this->bindStatementValue($stmtTeilleistung, ":ts", $ts, PDO::PARAM_INT);
						$this->bindStatementValue($stmtTeilleistung, ":idLeistung", $leistung->id, PDO::PARAM_INT);
						$this->bindStatementValue($stmtTeilleistung, ":daten", $jsonTeilleistung, PDO::PARAM_STR);
						$this->bindStatementValue($stmtTeilleistung, ":tsArtID", $teilleistung->tsArtID, PDO::PARAM_STR);
						$this->bindStatementValue($stmtTeilleistung, ":tsDatum", $teilleistung->tsDatum, PDO::PARAM_STR);
						$this->bindStatementValue($stmtTeilleistung, ":tsBemerkung", $teilleistung->tsBemerkung, PDO::PARAM_STR);
						$this->bindStatementValue($stmtTeilleistung, ":tsNote", $teilleistung->tsNote, PDO::PARAM_STR);
						$this->executeStatement($stmtTeilleistung);
					}
				}
				// ... dann die Ankreuzkompetenzen
				foreach ($schueler->ankreuzkompetenzen as $komp) {
					$jsonKompetenz = json_encode($komp, JSON_UNESCAPED_SLASHES);
					$this->bindStatementValue($stmtAnkreuzkomp, ":id", $komp->id, PDO::PARAM_INT);
					$this->bindStatementValue($stmtAnkreuzkomp, ":ts", $ts, PDO::PARAM_INT);
					$this->bindStatementValue($stmtAnkreuzkomp, ":idSchueler", $schueler->id, PDO::PARAM_INT);
					$this->bindStatementValue($stmtAnkreuzkomp, ":idKompetenz", $komp->kompetenzID, PDO::PARAM_INT);
					$this->bindStatementValue($stmtAnkreuzkomp, ":daten", $jsonKompetenz, PDO::PARAM_STR);
					$this->bindStatementValue($stmtAnkreuzkomp, ":tsStufe", $komp->tsStufe, PDO::PARAM_STR);
					$this->executeStatement($stmtAnkreuzkomp);
				}
				// ... und die Sprachenfolge
				foreach ($schueler->sprachenfolge as $sprachenfolge) {
					$jsonSprachenfolge = json_encode($sprachenfolge, JSON_UNESCAPED_SLASHES);
					$this->bindStatementValue($stmtSprachenfolge, ":id", $sprachenfolge->id, PDO::PARAM_INT);
					$this->bindStatementValue($stmtSprachenfolge, ":sprache", $sprachenfolge->sprache, PDO::PARAM_STR);
					$this->bindStatementValue($stmtSprachenfolge, ":ts", $ts, PDO::PARAM_INT);
					$this->bindStatementValue($stmtSprachenfolge, ":idSchueler", $schueler->id, PDO::PARAM_INT);
					$this->bindStatementValue($stmtSprachenfolge, ":daten", $jsonSprachenfolge, PDO::PARAM_STR);
					$this->executeStatement($stmtSprachenfolge);
				}
			}
			$this->commitTransaction();
		}

		/**
		 * Erstellt einen Abgleich von vorherigen Einträgen zu den Einträgen mit dem angegebenen Zeitstempel.
		 *
		 * @param int $ts   der Zeitstempel der neu importierten Daten
		 */
		public function importDiffSchueler(int $ts): void {
			// Entferne zunächst alle alten Schüler-Einträge, die nicht in den neuen Daten enthalten sind oder keine Änderungen haben
			$this->dropFrom('Schueler', "ts < $ts AND (id, ts) NOT IN (SELECT a.id, a.ts FROM Schueler a JOIN Schueler b WHERE a.id = b.id AND a.ts < b.ts AND (a.tsFehlstundenGesamt <> b.tsFehlstundenGesamt OR a.tsFehlstundenGesamtUnentschuldigt <> b.tsFehlstundenGesamtUnentschuldigt OR a.tsASV <> b.tsASV OR a.tsAUE <> b.tsAUE OR a.tsZB <> b.tsZB OR a.tsLELS <> b.tsLELS OR a.tsSchulformEmpf <> b.tsSchulformEmpf OR a.tsIndividuelleVersetzungsbemerkungen <> b.tsIndividuelleVersetzungsbemerkungen OR a.tsFoerderbemerkungen <> b.tsFoerderbemerkungen))");
			// Lese dann alle Daten mit dem alten Zeitstempel ein, da diese ggf. Änderungen beinhalten
			$diffsOld = $this->queryAllOrNull("SELECT id, ts, idJahrgang, idKlasse, daten, tsFehlstundenGesamt, tsFehlstundenGesamtUnentschuldigt, tsASV, tsAUE, tsZB, tsLELS, tsSchulformEmpf, tsIndividuelleVersetzungsbemerkungen, tsFoerderbemerkungen FROM Schueler WHERE ts < $ts");
			if ($diffsOld === null)
				return;
			// Erstelle aus den alten Daten eine Map basierend auf der id und eine Liste der ids
			$mapOld = [];
			$idsArray = [];
			foreach ($diffsOld as $row) {
				$mapOld[$row->id] = $row;
				$idsArray[] = $row->id;
			}
			if (count($idsArray) <= 0)
				return;
			$ids = implode(",", $idsArray);
			// Lese dann alle dazugehörigen Daten mit neuem Zeitstempel ein
			$diffsNeu = $this->queryAllOrExit500("SELECT * FROM Schueler WHERE ts = $ts AND id IN ($ids)", "Fehler Lesen der neuen Schülerdaten");
			foreach ($diffsNeu as $neu) {
				$alt = $mapOld[$neu->id];
				$needUpdate = (($alt->tsFehlstundenGesamt > $neu->tsFehlstundenGesamt)
					|| ($alt->tsFehlstundenGesamtUnentschuldigt > $neu->tsFehlstundenGesamtUnentschuldigt)
					|| ($alt->tsASV > $neu->tsASV) || ($alt->tsAUE > $neu->tsAUE) || ($alt->tsZB > $neu->tsZB)
					|| ($alt->tsLELS > $neu->tsLELS) || ($alt->tsSchulformEmpf > $neu->tsSchulformEmpf)
					|| ($alt->tsIndividuelleVersetzungsbemerkungen > $neu->tsIndividuelleVersetzungsbemerkungen)
					|| ($alt->tsFoerderbemerkungen > $neu->tsFoerderbemerkungen));
				if ($needUpdate > 0) {
					$jsonAlt = json_decode($alt->daten);
					$jsonNeu = json_decode($neu->daten);
					$update = "";
					if ($alt->tsFehlstundenGesamt > $neu->tsFehlstundenGesamt) {
						$update .= "tsFehlstundenGesamt='$alt->tsFehlstundenGesamt',";
						$jsonNeu->lernabschnitt->fehlstundenGesamt = $jsonAlt->lernabschnitt->fehlstundenGesamt;
						$jsonNeu->lernabschnitt->tsFehlstundenGesamt = $jsonAlt->lernabschnitt->tsFehlstundenGesamt;
					}
					if ($alt->tsFehlstundenGesamtUnentschuldigt > $neu->tsFehlstundenGesamtUnentschuldigt) {
						$update .= "tsFehlstundenGesamtUnentschuldigt='$alt->tsFehlstundenGesamtUnentschuldigt',";
						$jsonNeu->lernabschnitt->fehlstundenGesamtUnentschuldigt = $jsonAlt->lernabschnitt->fehlstundenGesamtUnentschuldigt;
						$jsonNeu->lernabschnitt->tsFehlstundenGesamtUnentschuldigt = $jsonAlt->lernabschnitt->tsFehlstundenGesamtUnentschuldigt;
					}
					if ($alt->tsASV > $neu->tsASV) {
						$update .= "tsASV='$alt->tsASV',";
						$jsonNeu->bemerkungen->ASV = $jsonAlt->bemerkungen->ASV;
						$jsonNeu->bemerkungen->tsASV = $jsonAlt->bemerkungen->tsASV;
					}
					if ($alt->tsAUE > $neu->tsAUE) {
						$update .= "tsAUE='$alt->tsAUE',";
						$jsonNeu->bemerkungen->AUE = $jsonAlt->bemerkungen->AUE;
						$jsonNeu->bemerkungen->tsAUE = $jsonAlt->bemerkungen->tsAUE;
					}
					if ($alt->tsZB > $neu->tsZB) {
						$update .= "tsZB='$alt->tsZB',";
						$jsonNeu->bemerkungen->ZB = $jsonAlt->bemerkungen->ZB;
						$jsonNeu->bemerkungen->tsZB = $jsonAlt->bemerkungen->tsZB;
					}
					if ($alt->tsLELS > $neu->tsLELS) {
						$update .= "tsLELS='$alt->tsLELS',";
						$jsonNeu->bemerkungen->LELS = $jsonAlt->bemerkungen->LELS;
						$jsonNeu->bemerkungen->tsLELS = $jsonAlt->bemerkungen->tsLELS;
					}
					if ($alt->tsSchulformEmpf > $neu->tsSchulformEmpf) {
						$update .= "tsSchulformEmpf='$alt->tsSchulformEmpf',";
						$jsonNeu->bemerkungen->schulformEmpf = $jsonAlt->bemerkungen->schulformEmpf;
						$jsonNeu->bemerkungen->tsSchulformEmpf = $jsonAlt->bemerkungen->tsSchulformEmpf;
					}
					if ($alt->tsIndividuelleVersetzungsbemerkungen > $neu->tsIndividuelleVersetzungsbemerkungen) {
						$update .= "tsIndividuelleVersetzungsbemerkungen='$alt->tsIndividuelleVersetzungsbemerkungen',";
						$jsonNeu->bemerkungen->individuelleVersetzungsbemerkungen = $jsonAlt->bemerkungen->individuelleVersetzungsbemerkungen;
						$jsonNeu->bemerkungen->tsIndividuelleVersetzungsbemerkungen = $jsonAlt->bemerkungen->tsIndividuelleVersetzungsbemerkungen;
					}
					if ($alt->tsFoerderbemerkungen > $neu->tsFoerderbemerkungen) {
						$update .= "tsFoerderbemerkungen='$alt->tsFoerderbemerkungen',";
						$jsonNeu->bemerkungen->foerderbemerkungen = $jsonAlt->bemerkungen->foerderbemerkungen;
						$jsonNeu->bemerkungen->tsFoerderbemerkungen = $jsonAlt->bemerkungen->tsFoerderbemerkungen;
					}
					$updatedData = json_encode($jsonNeu, JSON_UNESCAPED_SLASHES);
					$update .= "daten='$updatedData' WHERE id=$neu->id and ts=$neu->ts";
					$this->updateSet('Schueler', $update);
				}
			}
		}

		/**
		 * Erstellt einen Abgleich von vorherigen Einträgen zu den Einträgen mit dem angegebenen Zeitstempel.
		 *
		 * @param int $ts   der Zeitstempel der neu importierten Daten
		 */
		public function importDiffLeistungen(int $ts): void {
			// Entferne zunächst alle alten Leistungsdaten-Einträge, die nicht in den neuen Daten enthalten sind oder keine Änderungen haben
			$this->dropFrom('Leistungsdaten', "ts < $ts AND (id, ts) NOT IN (SELECT a.id, a.ts FROM Leistungsdaten a JOIN Leistungsdaten b WHERE a.id = b.id AND a.ts < b.ts AND (a.tsNote <> b.tsNote OR a.tsNoteQuartal <> b.tsNoteQuartal OR a.tsFehlstundenFach <> b.tsFehlstundenFach OR a.tsFehlstundenUnentschuldigtFach <> b.tsFehlstundenUnentschuldigtFach OR a.tsFachbezogeneBemerkungen <> b.tsFachbezogeneBemerkungen OR a.tsIstGemahnt <> b.tsIstGemahnt))");
			// Lese dann alle Daten mit dem alten Zeitstempel ein, da diese ggf. Änderungen beinhalten
			$diffsOld = $this->queryAllOrNull("SELECT id, ts, idSchueler, idLerngruppe, daten, tsNote, tsNoteQuartal, tsFehlstundenFach, tsFehlstundenUnentschuldigtFach, tsFachbezogeneBemerkungen, tsIstGemahnt FROM Leistungsdaten WHERE ts < $ts");
			if ($diffsOld === null)
				return;
			// Erstelle aus den alten Daten eine Map basierend auf der id und eine Liste der ids
			$mapOld = [];
			$idsArray = [];
			foreach ($diffsOld as $row) {
				$mapOld[$row->id] = $row;
				$idsArray[] = $row->id;
			}
			if (count($idsArray) <= 0)
				return;
			$ids = implode(",", $idsArray);
			// Lese dann alle dazugehörigen Daten mit neuem Zeitstempel ein
			$diffsNeu = $this->queryAllOrExit500("SELECT * FROM Leistungsdaten WHERE ts = $ts AND id IN ($ids)", "Fehler Lesen der neuen Leistungsdaten");
			foreach ($diffsNeu as $neu) {
				$alt = $mapOld[$neu->id];
				$needUpdate = (($alt->tsNote > $neu->tsNote) || ($alt->tsNoteQuartal > $neu->tsNoteQuartal)
					|| ($alt->tsFehlstundenFach > $neu->tsFehlstundenFach)
					|| ($alt->tsFehlstundenUnentschuldigtFach > $neu->tsFehlstundenUnentschuldigtFach)
					|| ($alt->tsFachbezogeneBemerkungen > $neu->tsFachbezogeneBemerkungen)
					|| ($alt->tsIstGemahnt > $neu->tsIstGemahnt));
				if ($needUpdate > 0) {
					$jsonAlt = json_decode($alt->daten);
					$jsonNeu = json_decode($neu->daten);
					$update = "";
					if ($alt->tsNote > $neu->tsNote) {
						$update .= "tsNote='$alt->tsNote',";
						$jsonNeu->note = $jsonAlt->note;
						$jsonNeu->tsNote = $jsonAlt->tsNote;
					}
					if ($alt->tsNoteQuartal > $neu->tsNoteQuartal) {
						$update .= "tsNoteQuartal='$alt->tsNoteQuartal',";
						$jsonNeu->noteQuartal = $jsonAlt->noteQuartal;
						$jsonNeu->tsNoteQuartal = $jsonAlt->tsNoteQuartal;
					}
					if ($alt->tsFehlstundenFach > $neu->tsFehlstundenFach) {
						$update .= "tsFehlstundenFach='$alt->tsFehlstundenFach',";
						$jsonNeu->fehlstundenFach = $jsonAlt->fehlstundenFach;
						$jsonNeu->tsFehlstundenFach = $jsonAlt->tsFehlstundenFach;
					}
					if ($alt->tsFehlstundenUnentschuldigtFach > $neu->tsFehlstundenUnentschuldigtFach) {
						$update .= "tsFehlstundenUnentschuldigtFach='$alt->tsFehlstundenUnentschuldigtFach',";
						$jsonNeu->fehlstundenUnentschuldigtFach = $jsonAlt->fehlstundenUnentschuldigtFach;
						$jsonNeu->tsFehlstundenUnentschuldigtFach = $jsonAlt->tsFehlstundenUnentschuldigtFach;
					}
					if ($alt->tsFachbezogeneBemerkungen > $neu->tsFachbezogeneBemerkungen) {
						$update .= "tsFachbezogeneBemerkungen='$alt->tsFachbezogeneBemerkungen',";
						$jsonNeu->fachbezogeneBemerkungen = $jsonAlt->fachbezogeneBemerkungen;
						$jsonNeu->tsFachbezogeneBemerkungen = $jsonAlt->tsFachbezogeneBemerkungen;
					}
					if ($alt->tsIstGemahnt > $neu->tsIstGemahnt) {
						$update .= "tsIstGemahnt='$alt->tsIstGemahnt',";
						$jsonNeu->istGemahnt = $jsonAlt->istGemahnt;
						$jsonNeu->tsIstGemahnt = $jsonAlt->tsIstGemahnt;
					}
					$updatedData = json_encode($jsonNeu, JSON_UNESCAPED_SLASHES);
					$update .= "daten='$updatedData' WHERE id=$neu->id and ts=$neu->ts";
					$this->updateSet('Leistungsdaten', $update);
				}
			}
		}

		/**
		 * Erstellt einen Abgleich von vorherigen Einträgen zu den Einträgen mit dem angegebenen Zeitstempel.
		 *
		 * @param int $ts   der Zeitstempel der neu importierten Daten
		 */
		public function importDiffTeilleistungen(int $ts): void {
			// Entferne zunächst alle alten Teilleistungen, die nicht in den neuen Daten enthalten sind oder keine Änderungen haben
			$this->dropFrom('Teilleistungen', "ts < $ts AND (id, ts) NOT IN (SELECT a.id, a.ts FROM Teilleistungen a JOIN Teilleistungen b WHERE a.id = b.id AND a.ts < b.ts AND (a.tsArtID <> b.tsArtID OR a.tsDatum <> b.tsDatum OR a.tsBemerkung <> b.tsBemerkung OR a.tsNote <> b.tsNote))");
			// Lese dann alle Daten mit dem alten Zeitstempel ein, da diese ggf. Änderungen beinhalten
			$diffsOld = $this->queryAllOrNull("SELECT id, ts, idLeistung, daten, tsArtID, tsDatum, tsBemerkung, tsNote FROM Teilleistungen WHERE ts < $ts");
			if ($diffsOld === null)
				return;
			// Erstelle aus den alten Daten eine Map basierend auf der id und eine Liste der ids
			$mapOld = [];
			$idsArray = [];
			foreach ($diffsOld as $row) {
				$mapOld[$row->id] = $row;
				$idsArray[] = $row->id;
			}
			if (count($idsArray) <= 0)
				return;
			$ids = implode(",", $idsArray);
			// Lese dann alle dazugehörigen Daten mit neuem Zeitstempel ein
			$diffsNeu = $this->queryAllOrExit500("SELECT * FROM Teilleistungen WHERE ts = $ts AND id IN ($ids)", "Fehler Lesen der neuen Teilleistungen");
			foreach ($diffsNeu as $neu) {
				$alt = $mapOld[$neu->id];
				$needUpdate = ($alt->tsArtID > $neu->tsArtID) || ($alt->tsDatum > $neu->tsDatum) || ($alt->tsBemerkung > $neu->tsBemerkung) || ($alt->tsNote > $neu->tsNote);
				if ($needUpdate > 0) {
					$jsonAlt = json_decode($alt->daten);
					$jsonNeu = json_decode($neu->daten);
					$update = "";
					if ($alt->tsArtID > $neu->tsArtID) {
						$update .= "tsArtID='$alt->tsArtID',";
						$jsonNeu->artID = $jsonAlt->artID;
						$jsonNeu->tsArtID = $jsonAlt->tsArtID;
					}
					if ($alt->tsDatum > $neu->tsDatum) {
						$update .= "tsDatum='$alt->tsDatum',";
						$jsonNeu->datum = $jsonAlt->datum;
						$jsonNeu->tsDatum = $jsonAlt->tsDatum;
					}
					if ($alt->tsBemerkung > $neu->tsBemerkung) {
						$update .= "tsBemerkung='$alt->tsBemerkung',";
						$jsonNeu->bemerkung = $jsonAlt->bemerkung;
						$jsonNeu->tsBemerkung = $jsonAlt->tsBemerkung;
					}
					if ($alt->tsNote > $neu->tsNote) {
						$update .= "tsNote='$alt->tsNote',";
						$jsonNeu->note = $jsonAlt->note;
						$jsonNeu->tsNote = $jsonAlt->tsNote;
					}
					$updatedData = json_encode($jsonNeu, JSON_UNESCAPED_SLASHES);
					$update .= "daten='$updatedData' WHERE id=$neu->id and ts=$neu->ts";
					$this->updateSet('Teilleistungen', $update);
				}
			}
		}

		/**
		 * Erstellt einen Abgleich von vorherigen Einträgen zu den Einträgen mit dem angegebenen Zeitstempel.
		 *
		 * @param int $ts   der Zeitstempel der neu importierten Daten
		 */
		public function importDiffAnkreuzkompetenzen(int $ts): void {
			// Entferne zunächst alle alten Ankreuzkompetenzen-Einträge, die nicht in den neuen Daten enthalten sind oder keine Änderungen haben
			$this->dropFrom('Ankreuzkompetenzen', "ts < $ts AND (id, ts) NOT IN (SELECT a.id, a.ts FROM Ankreuzkompetenzen a JOIN Ankreuzkompetenzen b WHERE a.id = b.id AND a.ts < b.ts AND (a.tsStufe <> b.tsStufe))");
			// Lese dann alle Daten mit dem alten Zeitstempel ein, da diese ggf. Änderungen beinhalten
			$diffsOld = $this->queryAllOrNull("SELECT id, ts, idSchueler, idKompetenz, daten, tsStufe FROM Ankreuzkompetenzen WHERE ts < $ts");
			if ($diffsOld === null)
				return;
			// Erstelle aus den alten Daten eine Map basierend auf der id und eine Liste der ids
			$mapOld = [];
			$idsArray = [];
			foreach ($diffsOld as $row) {
				$mapOld[$row->id] = $row;
				$idsArray[] = $row->id;
			}
			if (count($idsArray) <= 0)
				return;
			$ids = implode(",", $idsArray);
			// Lese dann alle dazugehörigen Daten mit neuem Zeitstempel ein
			$diffsNeu = $this->queryAllOrExit500("SELECT * FROM Ankreuzkompetenzen WHERE ts = $ts AND id IN ($ids)", "Fehler Lesen der neuen Ankreuzkompetenzen beim Schüler");
			foreach ($diffsNeu as $neu) {
				$alt = $mapOld[$neu->id];
				$needUpdate = ($alt->tsStufe > $neu->tsStufe);
				if ($needUpdate > 0) {
					$jsonAlt = json_decode($alt->daten);
					$jsonNeu = json_decode($neu->daten);
					$update = "tsStufe='$alt->tsStufe',";
					$jsonNeu->stufen = $jsonAlt->stufen;
					$jsonNeu->tsStufe = $jsonAlt->tsStufe;
					$updatedData = json_encode($jsonNeu, JSON_UNESCAPED_SLASHES);
					$update .= "daten='$updatedData' WHERE id=$neu->id and ts=$neu->ts";
					$this->updateSet('Ankreuzkompetenzen', $update);
				}
			}
		}

		/**
		 * Erstellt einen Abgleich von vorherigen Einträgen zu den Einträgen mit dem angegebenen Zeitstempel.
		 *
		 * @param int $ts   der Zeitstempel der neu importierten Daten
		 */
		public function importDiffLehrer(int $ts): void {
			// Entferne zunächst alle alten Lehrer-Einträge, die nicht in den neuen Daten enthalten sind oder keine Änderungen haben
			$this->dropFrom('Lehrer', "ts < $ts AND (id, ts) NOT IN (SELECT a.id, a.ts FROM Lehrer a JOIN Lehrer b WHERE a.id = b.id AND a.ts < b.ts AND (a.tsPasswordHash <> b.tsPasswordHash))");
			// Lese dann alle Daten mit dem alten Zeitstempel ein, da diese ggf. Änderungen beinhalten
			$diffsOld = $this->queryAllOrNull("SELECT id, ts, daten, eMailDienstlich, passwordHash, tsPasswordHash FROM Lehrer WHERE ts < $ts");
			if ($diffsOld === null)
				return;
			// Erstelle aus den alten Daten eine Map basierend auf der id und eine Liste der ids
			$mapOld = [];
			$idsArray = [];
			foreach ($diffsOld as $row) {
				$mapOld[$row->id] = $row;
				$idsArray[] = $row->id;
			}
			if (count($idsArray) <= 0)
				return;
			$ids = implode(",", $idsArray);
			// Lese dann alle dazugehörigen Daten mit neuem Zeitstempel ein
			$diffsNeu = $this->queryAllOrExit500("SELECT * FROM Lehrer WHERE ts = $ts AND id IN ($ids)", "Fehler Lesen der neuen Lehrerdaten");
			foreach ($diffsNeu as $neu) {
				$alt = $mapOld[$neu->id];
				$needUpdate = ($alt->tsPasswordHash > $neu->tsPasswordHash);
				if ($needUpdate > 0) {
					$jsonAlt = json_decode($alt->daten);
					$jsonNeu = json_decode($neu->daten);
					$update = "passwordHash='$alt->passwordHash', tsPasswordHash='$alt->tsPasswordHash',";
					$jsonNeu->passwordHash = $jsonAlt->passwordHash;
					$jsonNeu->tsPasswordHash = $jsonAlt->tsPasswordHash;
					$updatedData = json_encode($jsonNeu, JSON_UNESCAPED_SLASHES);
					$update .= "daten='$updatedData' WHERE id=$neu->id and ts=$neu->ts";
					$this->updateSet('Lehrer', $update);
				}
			}
		}

		/**
		 * Lädt die SMTP-Konfiguration aus der Datenbank und initialisiert den SMTP-Client damit.
		 *
		 * @return ?SMTPClient   der SMTP-Client oder null, wenn keine Konfiguration vorliegt
		 */
		public function getSMTPClient(): ?SMTPClient {
			$result = $this->queryAllOrNull("SELECT wert AS value FROM ServerConfig WHERE schluessel='smtp'");
			if ($result === null)
				return null;
			if (count($result) !== 1)
				return null;
			$json = $result[0]->value;
			$client = new SMTPClient($json);
			// Prüfe noch, ob der Client eine vollständige und plausible Konfiguration hat
			return $client->isValid() ? $client : null;
		}

		/**
		 * Ermittelt die globale Konfiguration und die benutzerspezifische Konfiguration anhand der
		 * übergebenen Lehrer-ID und gibt diese als JSON-String zurück.
		 *
		 * @param int $idLehrer   die ID des Lehrers, dessen benutzerspezifische Konfiguration ermittelt wird
		 *
		 * @return string ein JSON mit der globalen und der benutzerspezifischen Konfiguration
		 */
		public function getClientConfig(int $idLehrer): string {
			$configBenutzer = $this->queryAllOrExit500("SELECT schluessel AS key, wert AS value FROM ClientLehrerConfig WHERE idLehrer=$idLehrer", "Fehler beim Lesen der benutzerspezifischen Konfigurationsdaten");
			$configGlobal = $this->queryAllOrExit500("SELECT schluessel AS key, wert AS value FROM ClientConfig", "Fehler beim Lesen der globalen Konfigurationsdaten");
			$jsonBenutzer = json_encode($configBenutzer, JSON_UNESCAPED_SLASHES);
			$jsonGlobal = json_encode($configGlobal, JSON_UNESCAPED_SLASHES);
			return "{ \"user\": $jsonBenutzer, \"global\": $jsonGlobal }";
		}

		/**
		 * Ermittelt die Konfiguration des Server, d.h. die Server-sepzifische Konfiguration und die globale Konfiguration für
		 * den Client und gibt diese als JSON-String zurück.
		 *
		 * @return string ein JSON mit den beiden Konfigurationen
		 */
		public function getServerConfig(): string {
			$configServer = $this->queryAllOrExit500("SELECT schluessel AS key, wert AS value FROM ServerConfig", "Fehler beim Lesen der Server-spezifischen Konfigurationsdaten");
			$configGlobal = $this->queryAllOrExit500("SELECT schluessel AS key, wert AS value FROM ClientConfig", "Fehler beim Lesen der globalen Konfigurationsdaten");
			$jsonServer = json_encode($configServer, JSON_UNESCAPED_SLASHES);
			$jsonGlobal = json_encode($configGlobal, JSON_UNESCAPED_SLASHES);
			return "{ \"server\": $jsonServer, \"global\": $jsonGlobal }";
		}

		/**
		 * Setzt einen Eintrag in der globalen Konfiguration.
		 *
		 * @param bool $nurServer   gibt an, ob der Konfigurationseintrag nur den Server betrifft oder global ist.
		 * @param string $key       der zu setzende Schlüssel
		 * @param string $value     der zu setzende Wert für den Schlüssel
		 */
		public function putConfig(bool $nurServer, string $key, string | null $value): void {
			$table = $nurServer ? "ServerConfig" : "ClientConfig";
			// Prüfe, ob bereits ein Eintrag vorliegt
			$stmt = $this->prepareStatement("SELECT wert FROM $table WHERE schluessel = :schluessel");
			$this->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
			$this->executeStatement($stmt);
			$result = $stmt->fetchAll(PDO::FETCH_OBJ);
			$hatEintrag = (count($result) > 0);
			// Wenn der Wert null ist und kein Eintrag vorliegt, dann ist ein Einfügen nicht nötig
			if (!$hatEintrag && ($value === null))
				return;
			// Wenn der Wert null ist und ein Eintrag vorliegt, dann muss dieser entfernt werden
			if ($hatEintrag && ($value === null)) {
				$stmt = $this->prepareStatement("DELETE FROM $table WHERE schluessel = :schluessel");
				$this->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
				$this->executeStatement($stmt);
				return;
			}
			// Schreibe den Eintrag
			$this->beginTransaction();
			$stmt = null;
			if ($hatEintrag) {
				// UPDATE...
				$stmt = $this->prepareStatement("UPDATE $table SET wert=:wert WHERE schluessel = :schluessel");
			} else {
				// INSERT...
				$stmt = $this->prepareStatement("INSERT INTO $table(schluessel, wert) VALUES (:schluessel, :wert)");
			}
			$this->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
			$this->bindStatementValue($stmt, ":wert", $value, PDO::PARAM_STR);
			$this->executeStatement($stmt);
			$this->commitTransaction();
		}

		/**
		 * Setzt einen Eintrag in der benutzerspezifischen Konfiguration anhand der übergebenen Lehrer-ID.
		 *
		 * @param int $idLehrer   die ID des Lehrers, dessen benutzerspezifische Konfiguration angepasst wird
		 * @param string $key     der zu setzende Schlüssel
		 * @param string $value   der zu setzende Wert für den Schlüssel
		 */
		public function putClientUserConfig(int $idLehrer, string $key, string | null $value): void {
			// Prüfe, ob bereits ein Eintrag vorliegt
			$stmt = $this->prepareStatement("SELECT wert FROM ClientLehrerConfig WHERE idLehrer = :idLehrer AND schluessel = :schluessel");
			$this->bindStatementValue($stmt, ":idLehrer", $idLehrer, PDO::PARAM_INT);
			$this->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
			$this->executeStatement($stmt);
			$result = $stmt->fetchAll(PDO::FETCH_OBJ);
			$hatEintrag = (count($result) > 0);
			// Wenn der Wert null ist und kein Eintrag vorliegt, dann ist ein Einfügen nicht nötig
			if (!$hatEintrag && ($value === null))
				return;
			// Wenn der Wert null ist und ein Eintrag vorliegt, dann muss dieser entfernt werden
			if ($hatEintrag && ($value === null)) {
				$stmt = $this->prepareStatement("DELETE FROM ClientLehrerConfig WHERE idLehrer = :idLehrer AND schluessel = :schluessel");
				$this->bindStatementValue($stmt, ":idLehrer", $idLehrer, PDO::PARAM_INT);
				$this->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
				$this->executeStatement($stmt);
				return;
			}
			// Schreibe den Eintrag
			$this->beginTransaction();
			$stmt = null;
			if ($hatEintrag) {
				// UPDATE...
				$stmt = $this->prepareStatement("UPDATE ClientLehrerConfig SET wert=:wert WHERE idLehrer = :idLehrer AND schluessel = :schluessel");
			} else {
				// INSERT...
				$stmt = $this->prepareStatement("INSERT INTO ClientLehrerConfig(idLehrer, schluessel, wert) VALUES (:idLehrer, :schluessel, :wert)");
			}
			$this->bindStatementValue($stmt, ":idLehrer", $idLehrer, PDO::PARAM_INT);
			$this->bindStatementValue($stmt, ":schluessel", $key, PDO::PARAM_STR);
			$this->bindStatementValue($stmt, ":wert", $value, PDO::PARAM_STR);
			$this->executeStatement($stmt);
			$this->commitTransaction();
		}

		/**
		 * Ermittelt den Zeitstempel und die grundlegenden ENM-Daten aus der Datenbank und gibt
		 * diese in einem Array zurück.
		 *
		 * @return object der Zeitstempel und die ENM-Daten als JSON-Objekt
		 */
		public function getJsonENMDaten(): object {
			$results = $this->queryAllOrExit500("SELECT ts, daten FROM Daten", "Fehler Lesen der ENM-Daten");
			if (count($results) === 0)
				Http::exit404NotFound("Keine zu exportierenden Daten in der Datenbank vorhanden.");
			if (count($results) > 1)
				Http::exit500("Zu viele Einträge für ENM-Daten in den Datenbank vorhanden.");
			return $results[0];
		}

		/**
		 * Ermittelt die Lehrerdaten aus der Datenbank und gibt diese in einem Array zurück.
		 *
		 * @return array die Lehrer-Daten
		 */
		public function getENMLehrerdaten(): array {
			$results = $this->queryAllOrExit500("SELECT daten FROM Lehrer", "Fehler beim Lesen der Lehrer-Daten");
			$result = [];
			foreach ($results as $row)
				$result[] = json_decode($row->daten);
			return $result;
		}

		/**
		 * Ermittelt die Lehrerdaten aus der Datenbank und gibt diese in einem Array zurück.
		 *
		 * @return ?object die Lehrer-Daten
		 */
		public function getENMLehrerByEmail(string $email): object | null {
			// Lese die Email-Adresse zur Vermeidung von SQL-Injection nicht direkt aus der DB
			$results = $this->queryAllOrExit500("SELECT daten FROM Lehrer", "Fehler beim Lesen der Lehrer-Daten");
			foreach ($results as $row) {
				$tmp = json_decode($row->daten);
				if (($tmp->eMailDienstlich !== null) && (strcasecmp($tmp->eMailDienstlich, $email) === 0))
					return $tmp;
			}
			return null;
		}

		/**
 		 * Ermittelt die Lehrerdaten aus der Datenbank und gibt bei einem gefundenen Datensatz 'true' zurück,
 		 * andernfalls bei null oder mehreren Datensätzen 'false'.
 		 *
 		 * @return bool true bei genau einem gefundenen Datensatz, false bei keinem oder mehreren
 		 */
		public function checkENMLehrerByEmail(string $email): bool {
			// Lese die Email-Adresse zur Vermeidung von SQL-Injection nicht direkt aus der DB
			$results = $this->queryAllOrExit500("SELECT daten FROM Lehrer", "Fehler beim Lesen der Lehrer-Daten");
			$matchingCount = 0; // Zähler für passende Datensätze
			foreach ($results as $row) {
				$tmp = json_decode($row->daten);
				if (($tmp->eMailDienstlich !== null) && (strcasecmp($tmp->eMailDienstlich, $email) === 0))
					$matchingCount++;
			}
			// Wenn genau ein Datensatz gefunden wurde, return true, sonst false
			return $matchingCount === 1;
		}

		/**
		 * Ermittelt die Schülerdaten aus der Datenbank und gibt diese in einem Array zurück.
		 *
		 * @return array die Schüler-Daten
		 */
		public function getENMSchuelerdaten(): array {
			$results = $this->queryAllOrExit500("SELECT daten FROM Schueler", "Fehler beim Lesen der Schüler-Daten");
			$result = [];
			$mapSchueler = [];
			foreach ($results as $row) {
				$schueler = json_decode($row->daten);
				$result[] = $schueler;
				$mapSchueler[$schueler->id] = $schueler;
			}
			// Integration der Leistungsdaten...
			$results = $this->queryAllOrNull("SELECT idSchueler, daten FROM Leistungsdaten");
			if ($results != null) {
				$mapLeistung = [];
				foreach ($results as $row) {
					$schueler = $mapSchueler[$row->idSchueler];
					$leistung = json_decode($row->daten);
					$schueler->leistungsdaten[] = $leistung;
					$mapLeistung[$leistung->id] = $leistung;
				}
				// ... und deren Teilleistungen
				$results = $this->queryAllOrNull("SELECT idLeistung, daten FROM Teilleistungen");
				if ($results != null) {
					foreach ($results as $row) {
						$leistung = $mapLeistung[$row->idLeistung];
						$leistung->teilleistungen[] = json_decode($row->daten);
					}
				}
			}
			// Integration der Ankreuzkompetenzen
			$results = $this->queryAllOrNull("SELECT idSchueler, daten FROM Ankreuzkompetenzen");
			if ($results != null) {
				foreach ($results as $row) {
					$schueler = $mapSchueler[$row->idSchueler];
					$schueler->ankreuzkompetenzen[] = json_decode($row->daten);
				}
			}
			// Integration der Sprachenfolge
			$results = $this->queryAllOrNull("SELECT idSchueler, daten FROM Sprachenfolge");
			if ($results != null) {
				foreach ($results as $row) {
					$schueler = $mapSchueler[$row->idSchueler];
					$schueler->sprachenfolge[] = json_decode($row->daten);
				}
			}
			return $result;
		}


		/**
		 * Prüft, ob die beiden Strings sich unterscheiden. Dabei wird auch auf Null-Werte geprüft.
		 *
		 * @param string | null $a   der erste String
		 * @param string | null $b   der zweite String
		 *
		 * @return bool   true, wenn die beiden Werte unterschiedlich sind
		 */
		protected function diffStringNullable(string | null $a, string | null $b) : bool {
			if (($a === null) && ($b === null))
				return false;
			if (($a === null) || ($b === null))
				return true;
			return (strcmp($a, $b) !== 0);
		}

		/**
		 * Prüft, ob die Arrays sich unterscheiden. Dabei wird zum Vergleich der Werte
		 * der Vergleichsoperator !== verwendet.
		 *
		 * @param array $a   das erste Array
		 * @param array $b   das zweite Array
		 *
		 * @return bool   true, wenn die beiden Arrays unterschiedlich sind
		 */
		protected function diffArraySimple(array $a, array $b) : bool {
			if (count($a) !== count($b))
				return true;
			foreach ($a as $k => $v)
				if ((!array_key_exists($k, $b)) || ($b[$k] !== $v))
					return true;
			return false;
		}

		/**
		 * Erstellt einen Update-Befehl für die Datenbank aus den übergebenen Daten für einen
		 * Patch von Leistungsdaten.
		 *
		 * @param string $ts      der Zeitstempel der neu importierten Daten
		 * @param object $daten   die Daten aus der Datenbank
		 * @param object $patch   der Patch für die Daten
		 * Folgende Werte können durch das Patch Objekt überschrieben werden: tsNote, tsNoteQuartal,
		 * 																	  tsFehlstundenFach, tsFehlstundenUnentschuldigtFach,
		 * 																	  fehlstundenUnentschuldigtFach, fachbezogeneBemerkungen, istGemahnt
		 */
		public function patchENMLeistung(string $ts, object $daten, object $patch): void {
			$update = "";
			if (property_exists($patch, 'note') && $this->diffStringNullable($patch->note, $daten->note) && ($ts > $daten->tsNote)) {
				$update .= "tsNote='$ts',";
				$daten->note = $patch->note;
				$daten->tsNote = $ts;
			}
			if (property_exists($patch, 'noteQuartal') && $this->diffStringNullable($patch->noteQuartal, $daten->noteQuartal) && ($ts > $daten->tsNoteQuartal)) {
				$update .= "tsNoteQuartal='$ts',";
				$daten->noteQuartal = $patch->noteQuartal;
				$daten->tsNoteQuartal = $ts;
			}
			if (property_exists($patch, 'fehlstundenFach') && ($patch->fehlstundenFach !== $daten->fehlstundenFach) && ($ts > $daten->tsFehlstundenFach)) {
				$update .= "tsFehlstundenFach='$ts',";
				$daten->fehlstundenFach = $patch->fehlstundenFach;
				$daten->tsFehlstundenFach = $ts;
			}
			if (property_exists($patch, 'fehlstundenUnentschuldigtFach') && ($patch->fehlstundenUnentschuldigtFach !== $daten->fehlstundenUnentschuldigtFach) && ($ts > $daten->tsFehlstundenUnentschuldigtFach)) {
				$update .= "tsFehlstundenUnentschuldigtFach='$ts',";
				$daten->fehlstundenUnentschuldigtFach = $patch->fehlstundenUnentschuldigtFach;
				$daten->tsFehlstundenUnentschuldigtFach = $ts;
			}
			if (property_exists($patch, 'fachbezogeneBemerkungen') && $this->diffStringNullable($patch->fachbezogeneBemerkungen, $daten->fachbezogeneBemerkungen) && ($ts > $daten->tsFachbezogeneBemerkungen)) {
				$update .= "tsFachbezogeneBemerkungen='$ts',";
				$daten->fachbezogeneBemerkungen = $patch->fachbezogeneBemerkungen;
				$daten->tsFachbezogeneBemerkungen = $ts;
			}
			if (property_exists($patch, 'istGemahnt') && ($patch->istGemahnt !== $daten->istGemahnt) && ($ts > $daten->tsIstGemahnt)) {
				$update .= "tsIstGemahnt='$ts',";
				$daten->istGemahnt = $patch->istGemahnt;
				$daten->tsIstGemahnt = $ts;
			}
			if (strlen($update) > 0) {
				// Stelle sicher in der Datenbanktabelle nicht Objekte auftauchen die woanders in der Datenbank gespeichert werden
				$daten->teilleistungen = [];
				// Schreibe das gepatchte JSON in die Datenbank zurück
				$updatedData = json_encode($daten, JSON_UNESCAPED_SLASHES);

				$this->beginTransaction();
				$stmt = $this->prepareStatement("UPDATE Leistungsdaten SET $update daten=:daten WHERE id=:id");
				$this->bindStatementValue($stmt, ":daten", $updatedData, PDO::PARAM_STR);
				$this->bindStatementValue($stmt, ":id", $patch->id, PDO::PARAM_INT);
				$this->executeStatement($stmt);
				$this->commitTransaction();
			}
		}

		/**
		 * Erstellt einen Update-Befehl für die Datenbank aus den übergebenen Daten für einen
		 * Patch von Lernabschnittsdaten eines Schülers.
		 *
		 * @param string $ts      der Zeitstempel der neu importierten Daten
		 * @param object $daten   die Daten aus der Datenbank
		 * @param object $patch   der Patch für die Daten
		 * Folgende Werte können durch das Patch Objekt überschrieben werden: fehlstundenGesamt, fehlstundenGesamtUnentschuldigt
		 */
		public function patchENMSchuelerLernabschnitt(string $ts, object $daten, object $patch): void {
			$update = "";
			if (property_exists($patch, 'fehlstundenGesamt') && ($ts > $daten->lernabschnitt->tsFehlstundenGesamt)
					&& ($patch->fehlstundenGesamt !== $daten->lernabschnitt->fehlstundenGesamt)) {
				$update .= "tsFehlstundenGesamt='$ts',";
				$daten->lernabschnitt->fehlstundenGesamt = $patch->fehlstundenGesamt;
				$daten->lernabschnitt->tsFehlstundenGesamt = $ts;
			}
			if (property_exists($patch, 'fehlstundenGesamtUnentschuldigt') && ($ts > $daten->lernabschnitt->tsFehlstundenGesamtUnentschuldigt)
					&& ($patch->fehlstundenGesamtUnentschuldigt !== $daten->lernabschnitt->fehlstundenGesamtUnentschuldigt)) {
				$update .= "tsFehlstundenGesamtUnentschuldigt='$ts',";
				$daten->lernabschnitt->fehlstundenGesamtUnentschuldigt = $patch->fehlstundenGesamtUnentschuldigt;
				$daten->lernabschnitt->tsFehlstundenGesamtUnentschuldigt = $ts;
			}
			if (strlen($update) > 0) {
				// Stelle sicher in der Datenbanktabelle nicht Objekte auftauchen, die woanders in der Datenbank gespeichert werden
				$daten->ankreuzkompetenzen = [];
				$daten->leistungsdaten = [];
				// Schreibe das gepatchte JSON in die Datenbank zurück
				$updatedData = json_encode($daten, JSON_UNESCAPED_SLASHES);

				$this->beginTransaction();
				$stmt = $this->prepareStatement("UPDATE Schueler SET $update daten=:daten WHERE id=:id");
				$this->bindStatementValue($stmt, ":daten", $updatedData, PDO::PARAM_STR);
				$this->bindStatementValue($stmt, ":id", $daten->id, PDO::PARAM_INT);
				$this->executeStatement($stmt);
				$this->commitTransaction();
			}
		}

		/**
		 * Erstellt einen Update-Befehl für die Datenbank aus den übergebenen Daten für einen
		 * Patch von Bemerkungsdaten eines Schülers.
		 *
		 * @param string $ts        der Zeitstempel der neu importierten Daten
		 * @param int $idSchueler   die ID des Schülers
		 * @param object $daten     die Daten aus der Datenbank
		 * @param object $patch     der Patch für die Daten
		 * Folgende Werte können durch das Patch Objekt überschrieben werden: ASV, AUE, ZB, LELS, schulformEmpf, individuelleVersetzungsbemerkungen, foerderbemerkungen
		 */
		public function patchENMSchuelerBemerkungen(string $ts, int $idSchueler, object $daten, object $patch): void {
			$update = "";
			if (property_exists($patch, 'ASV') && ($ts > $daten->bemerkungen->tsASV)
					&& $this->diffStringNullable($daten->bemerkungen->ASV, $patch->ASV)) {
				$update .= "tsASV='$ts',";
				$daten->bemerkungen->ASV = $patch->ASV;
				$daten->bemerkungen->tsASV = $ts;
			}
			if (property_exists($patch, 'AUE') && ($ts > $daten->bemerkungen->tsAUE)
					&& $this->diffStringNullable($daten->bemerkungen->AUE, $patch->AUE)) {
				$update .= "tsAUE='$ts',";
				$daten->bemerkungen->AUE = $patch->AUE;
				$daten->bemerkungen->tsAUE = $ts;
			}
			if (property_exists($patch, 'ZB') && ($ts > $daten->bemerkungen->tsZB)
					&& $this->diffStringNullable($daten->bemerkungen->ZB, $patch->ZB)) {
				$update .= "tsZB='$ts',";
				$daten->bemerkungen->ZB = $patch->ZB;
				$daten->bemerkungen->tsZB = $ts;
			}
			if (property_exists($patch, 'LELS') && ($ts > $daten->bemerkungen->tsLELS)
					&& $this->diffStringNullable($daten->bemerkungen->LELS, $patch->LELS)) {
				$update .= "tsLELS='$ts',";
				$daten->bemerkungen->LELS = $patch->LELS;
				$daten->bemerkungen->tsLELS = $ts;
			}
			if (property_exists($patch, 'schulformEmpf') && ($ts > $daten->bemerkungen->tsSchulformEmpf)
					&& $this->diffStringNullable($daten->bemerkungen->schulformEmpf, $patch->schulformEmpf)) {
				$update .= "tsSchulformEmpf='$ts',";
				$daten->bemerkungen->schulformEmpf = $patch->schulformEmpf;
				$daten->bemerkungen->tsSchulformEmpf = $ts;
			}
			if (property_exists($patch, 'individuelleVersetzungsbemerkungen') && ($ts > $daten->bemerkungen->tsIndividuelleVersetzungsbemerkungen)
					&& $this->diffStringNullable($daten->bemerkungen->individuelleVersetzungsbemerkungen, $patch->individuelleVersetzungsbemerkungen)) {
				$update .= "tsIndividuelleVersetzungsbemerkungen='$ts',";
				$daten->bemerkungen->individuelleVersetzungsbemerkungen = $patch->individuelleVersetzungsbemerkungen;
				$daten->bemerkungen->tsIndividuelleVersetzungsbemerkungen = $ts;
			}
			if (property_exists($patch, 'foerderbemerkungen') && ($ts > $daten->bemerkungen->tsFoerderbemerkungen)
					&& $this->diffStringNullable($daten->bemerkungen->foerderbemerkungen, $patch->foerderbemerkungen)) {
				$update .= "tsFoerderbemerkungen='$ts',";
				$daten->bemerkungen->foerderbemerkungen = $patch->foerderbemerkungen;
				$daten->bemerkungen->tsFoerderbemerkungen = $ts;
			}
			if (strlen($update) > 0) {
				// Stelle sicher in der Datenbanktabelle nicht Objekte auftauchen die woanders in der Datenbank gespeichert werden
				$daten->ankreuzkompetenzen = [];
				$daten->leistungsdaten = [];
				// Schreibe das gepatchte JSON in die Datenbank zurück
				$updatedData = json_encode($daten, JSON_UNESCAPED_SLASHES);

				$this->beginTransaction();
				$stmt = $this->prepareStatement("UPDATE Schueler SET $update daten=:daten WHERE id=:id");
				$this->bindStatementValue($stmt, ":daten", $updatedData, PDO::PARAM_STR);
				$this->bindStatementValue($stmt, ":id", $idSchueler, PDO::PARAM_INT);
				$this->executeStatement($stmt);
				$this->commitTransaction();
			}
		}

		/**
		 * Erstellt einen Update-Befehl für die Datenbank aus den übergebenen Daten für einen
		 * Patch von Daten zu Teilleistungen.
		 *
		 * @param string $ts      der Zeitstempel der neu importierten Daten
		 * @param object $daten   die Daten aus der Datenbank
		 * @param object $patch   der Patch für die Daten
		 * Folgende Werte können durch das Patch Objekt überschrieben werden: artID, datum, bemerkung, note
		 */
		public function patchENMTeilleistung(string $ts, object $daten, object $patch): void {
			$update = "";
			if (property_exists($patch, 'artID') && ($patch->artID !== $daten->artID) && ($ts > $daten->tsArtID)) {
				$update .= "tsArtID='$ts',";
				$daten->artID = $patch->artID;
				$daten->tsArtID = $ts;
			}
			if (property_exists($patch, 'datum') && $this->diffStringNullable($patch->datum, $daten->datum) && ($ts > $daten->tsDatum)) {
				$update .= "tsDatum='$ts',";
				$daten->datum = $patch->datum;
				$daten->tsDatum = $ts;
			}
			if (property_exists($patch, 'bemerkung') && $this->diffStringNullable($patch->bemerkung, $daten->bemerkung) && ($ts > $daten->tsBemerkung)) {
				$update .= "tsBemerkung='$ts',";
				$daten->bemerkung = $patch->bemerkung;
				$daten->tsBemerkung = $ts;
			}
			if (property_exists($patch, 'note') && $this->diffStringNullable($patch->note, $daten->note) && ($ts > $daten->tsNote)) {
				$update .= "tsNote='$ts',";
				$daten->note = $patch->note;
				$daten->tsNote = $ts;
			}
			if (strlen($update) > 0) {
				$updatedData = json_encode($daten, JSON_UNESCAPED_SLASHES);

				$this->beginTransaction();
				$stmt = $this->prepareStatement("UPDATE Teilleistungen SET $update daten=:daten WHERE id=:id");
				$this->bindStatementValue($stmt, ":daten", $updatedData, PDO::PARAM_STR);
				$this->bindStatementValue($stmt, ":id", $patch->id, PDO::PARAM_INT);
				$this->executeStatement($stmt);
				$this->commitTransaction();
			}
		}

		/**
		 * Erstellt einen Update-Befehl für die Datenbank aus den übergebenen Daten für einen
		 * Patch von Schüler-Daten zu den Ankreuzkompetenzen.
		 *
		 * @param string $ts      der Zeitstempel der neu importierten Daten
		 * @param object $daten   die Daten aus der Datenbank
		 * @param object $patch   der Patch für die Daten
		 *
		 * Folgende Werte können durch das Patch Objekt überschrieben werden: Stufen
		 */
		public function patchENMSchuelerAnkreuzkompetenzen(string $ts, object $daten, object $patch): void {
			$update = "";
			if (property_exists($patch, 'stufen') && $this->diffArraySimple($patch->stufen, $daten->stufen) && ($ts > $daten->tsStufe)) {
				foreach ($patch->stufen as $index=>$stufe)
					if (!is_bool($stufe))
						Http::exit500("Fehler beim Ausführen des Patch-Statements. Stufe mit Index ".$index." in der Ankreuzkompetenz ist kein Boolean-Wert. Patch wurde abgebrochen.");
				$update .= "tsStufe='$ts',";
				$daten->stufen = $patch->stufen;
				$daten->tsStufe = $ts;
			}
			if (strlen($update) > 0) {
				$updatedData = json_encode($daten, JSON_UNESCAPED_SLASHES);

				$this->beginTransaction();
				$stmt = $this->prepareStatement("UPDATE Ankreuzkompetenzen SET $update daten=:daten WHERE id=:id");
				$this->bindStatementValue($stmt, ":daten", $updatedData, PDO::PARAM_STR);
				$this->bindStatementValue($stmt, ":id", $patch->id, PDO::PARAM_INT);
				$this->executeStatement($stmt);
				$this->commitTransaction();
			}
		}

		/**
		 * Erstellt einen Update-Befehl für die Datenbank aus den übergebenen Daten für einen
		 * Patch von Lehrer-Daten zu dem Passwort.
		 *
		 * @param string $ts      der Zeitstempel der neu importierten Daten
		 * @param object $daten   die Daten aus der Datenbank
		 * @param object $patch   der Patch für die Daten
		 */
		public function patchENMLehrerPassword(string $ts, object $daten, object $patch): void {
			$update = "";
			if (property_exists($patch, 'passwordHash') && $this->diffStringNullable($patch->passwordHash, $daten->passwordHash) && ($ts > $daten->tsPasswordHash)) {
				$update .= "tsPasswordHash='$ts',";
				$update .= "passwordHash='$patch->passwordHash',";
				$daten->passwordHash = $patch->passwordHash;
				$daten->tsPasswordHash = $ts;
			}
			if (strlen($update) > 0) {
				$updatedData = json_encode($daten, JSON_UNESCAPED_SLASHES);
				$update .= "daten='$updatedData' WHERE id='$patch->id'";
				$this->updateSet('Lehrer', $update);
			}
		}

		/**
		 * Erstellt ein neues Password-Token. Ein zuvor bestendes Password-Token wird dabei ersetzt.
		 *
		 * @param int $lehrerId   Die ID des Lehrers
		 * @return string $token  Das generierte und gespeicherte Password-Token
		 */
		public function writeENMLehrerToken(int $lehrerId): string {
			$token = Config::generateRandomSecret();
			$time = time();
			$validFor = 600;
			$this->beginTransaction();

			// Alten Token löschen
			$stmt = $this->prepareStatement("DELETE FROM Lehrertoken WHERE idLehrer = :idLehrer");
			$this->bindStatementValue($stmt, ":idLehrer", $lehrerId, PDO::PARAM_INT);
			$this->executeStatement($stmt);

			// Neuen Token speichern
			$stmt = $this->prepareStatement("INSERT INTO Lehrertoken (idLehrer, token, tokenTimestamp, tokenValidForSecs) VALUES (:id, :token, :ts, :valid)");
			$this->bindStatementValue($stmt, ":id", $lehrerId, PDO::PARAM_INT);
			$this->bindStatementValue($stmt, ":token", $token, PDO::PARAM_STR);
			$this->bindStatementValue($stmt, ":ts", $time, PDO::PARAM_INT);
			$this->bindStatementValue($stmt, ":valid", $validFor, PDO::PARAM_STR);
			$this->executeStatement($stmt);

			$this->commitTransaction();
			return $token;
		}

		/**
		 * Überprüft, ob zu einer LehrerId ein gültiger Token vorliegt oder prüft, ob ein übergebener Token noch gültig ist.
		 *
		 * @param string|int $identifier   Kann die LehrerId oder ein Token sein
		 * @return boolean                 Token gültig true; Token abgelaufen oder nicht vorhanden false
		 */
		public function isENMLehrerTokenValid($identifier): bool {
			// Überprüfen, ob $identifier eine ID oder ein Token ist
			if (is_int($identifier)) {
				// Abfrage nach ID
				$stmt = $this->prepareStatement("SELECT * FROM Lehrertoken WHERE idLehrer = :idLehrer");
				$this->bindStatementValue($stmt, ":idLehrer", $identifier, PDO::PARAM_INT);
			} else {
				// Abfrage nach Token
				$stmt = $this->prepareStatement("SELECT * FROM Lehrertoken WHERE token = :token");
				$this->bindStatementValue($stmt, ":token", $identifier, PDO::PARAM_STR);
			}
			$this->executeStatement($stmt);
			$result = $stmt->fetchAll(PDO::FETCH_OBJ);

			// Prüfen, ob ein Ergebnis vorliegt
			if (empty($result))
				return false;

			// Das erste und einzige Ergebnis
			$tokenObj = $result[0];

			// Überprüfe, ob ein Token existiert und ob es noch gültig ist
			if (isset($tokenObj->token)) {
				$tokenTimestamp = $tokenObj->tokenTimestamp; // Zeitstempel des Tokens
				$tokenValidForSecs = $tokenObj->tokenValidForSecs; // Gültigkeitsdauer in Sekunden
				// Berechne, ob das Token noch gültig ist
				$tokenExpiryTime = $tokenTimestamp + $tokenValidForSecs; // Ablaufzeit des Tokens
				$currentTime = time(); // Aktuelle Zeit
				if ($currentTime < $tokenExpiryTime)
					return true;
			}
			return false;
		}

		/**
		 * Löscht das Password-Token eines Lehrers aus der Datenbank.
		 *
		 * @param int $lehrerId   Die ID des Lehrers
		 */
		public function deleteENMLehrerToken(int $lehrerId): void {
			$this->beginTransaction();

			// Token für den Lehrer löschen
			$stmt = $this->prepareStatement("DELETE FROM Lehrertoken WHERE idLehrer = :idLehrer");
			$this->bindStatementValue($stmt, ":idLehrer", $lehrerId, PDO::PARAM_INT);
			$this->executeStatement($stmt);

			$this->commitTransaction();
		}

	}

?>
