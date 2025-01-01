<?php

	require_once 'Http.php';
	require_once 'Database.php';

	/**
	 * Diese Klasse stellt Hilfsmethoden für den Umgang mit ENM-Daten zur Verfügung.
	 */
	class ENMDatenManager {

		/** Die ENM-Revision mit welcher diese Klasse arbeitet */
		public int $enmRevisionRequired = 1;

		/** Der Zeitstempel, wann der Manager erzeugt wurde. Dies ist für den Import von Daten relevant, um die neuen Daten von den alten Daten zu unterscheiden */
		protected int $ts;

		/** Die ENM-Daten ohne Lehrer und Schüler-Informationen */
		protected object $enmDaten;

		/** Die Informationen zu Lehrern, die in der Notendatei vorhanden sind. */
		protected array $enmLehrer;

		/** Die Informationen zu den Schülern, deren Noten in dieser Notendatei verwaltet werden. */
		protected array $enmSchueler;

		/** Ein Cache für die Klassen, bei denen ein angemeldeter Lehrer ein Klassenlehrer ist. */ 
		protected array | null $mapKlassen = null;

		/** Ein Cache für die Lerngruppen */ 
		protected array | null $mapLerngruppen = null;

		/** Ein Cache für die Lerngruppen, bei denen ein angemeldeter Lehrer ein Fachlehrer ist. */ 
		protected array | null $mapLerngruppenFachlehrer = null;

		/** Ein Cache für die Ankreuzkompetenzen */ 
		protected array | null $mapAnkreuzkompetenzen = null;


		/**
		 * Erstellt einen neuen nicht initialisierten Manager zur Verfügung.
		 */
		private function __construct() {
			// empty - use static creators for further initialisization
			$this->ts = time();
		}

		/**
		 * Erstellt ein neues Objekt mit den übergebenen ENM-Daten als JSON-String und stellt Methoden für
		 * den Zugriff auf diese Daten zur Verfügung.
		 *
		 * @param string $jsonEnmDaten   die ENM-Daten
		 *
		 * @return ENMDatenManager   der initialisierte Manager
		 */
		public static function createFromJson(string $jsonEnmDaten): ENMDatenManager {
			$manager = new ENMDatenManager();
			$enmDaten = json_decode($jsonEnmDaten);
			// Prüfe zunächst die ENM-Revision
			if ($enmDaten->enmRevision != $manager->enmRevisionRequired)
				Http::exit400BadRequest("Die Revision der ENM-Daten ist nicht $manager->enmRevisionRequired.");
			// Prüfe, ob die Schulform gesetzt ist
			if ($enmDaten->schulform == null)
				Http::exit400BadRequest("Es muss eine Schulform angegeben sein.");
			// Speichere die Lehrer-Daten und die Schüler-Daten zwischen, da diese im ENM-Server veränderbare Daten beinhalten
			$manager->enmLehrer = $enmDaten->lehrer;
			$manager->enmSchueler = $enmDaten->schueler;
			// Leere die Lehrer- und Schülerdaten in den ENM-Daten, da diese auf anderem Wege dem Client bereitgestellt werden müssen
			$enmDaten->lehrer = [];
			$enmDaten->schueler = [];
			$manager->enmDaten = $enmDaten;
			return $manager;
		}


		/**
		 * Erstellt ein neues Objekt aus der übergebenen Datenbank.
		 *
		 * @param string $jsonEnmDaten   die ENM-Daten
		 *
		 * @return ENMDatenManager   der initialisierte Manager
		 */
		public static function createFromDatabase(Database $db): ENMDatenManager {
			$manager = new ENMDatenManager();
			$enmDaten = $db->getJsonENMDaten();
			$manager->ts = $enmDaten->ts;
			$manager->enmDaten = json_decode($enmDaten->daten);
			$manager->enmLehrer = $db->getENMLehrerdaten();
			$manager->enmSchueler = $db->getENMSchuelerdaten();
			return $manager;
		}

		/**
		 * Führt den Import der ENM-Daten in die Datenbank durch
		 * 
		 * @param Database $db   das Objekt für den Datenbankzugriff
		 */
		public function doImport(Database $db) {
			// Prüfe anhand der Schulnummer, ob bereits importierte Daten vorliegen
			$schulnummer = $this->enmDaten->schulnummer;
			$dbEnmDaten = $db->queryAllOrNull("SELECT * FROM Daten WHERE schulnummer = $schulnummer", true);
			$updateMode = ($dbEnmDaten != null) && (count($dbEnmDaten) != 0);
			// Wenn nicht aktualisiert wird, dann leere zunächst alle Tabellen mit evtl. zuvor importierten ENM-Daten
			if (!$updateMode)
				$db->clearENMDaten();
			// Schreibe die allgemeinen ENM-Daten
			$db->writeENMDaten($this->ts, $this->enmDaten);
			// Schreibe die ENM-Daten für die Lehrer-Zugänge
			$db->writeENMLehrer($this->ts, $this->enmLehrer);
			// Schreibe die ENM-Daten für die Schüler
			$db->writeENMSchueler($this->ts, $this->enmSchueler);
			// Bei einem Update werden ggf. aus den vorhanden Daten aktuellere Informationen in die neuen übertragen
			if ($updateMode) {
				// Übertrage ggf. aktuellere Informationen aus den zuvor vorhandenen Daten in die neu importierten Daten
				$db->importDiffSchueler($this->ts);
				$db->importDiffLeistungen($this->ts);
				$db->importDiffTeilleistungen($this->ts);
				$db->importDiffAnkreuzkompetenzen($this->ts);
				$db->importDiffLehrer($this->ts);
			}
			// Räume auf und entferne alle restlichen Daten, die nicht den neuen Zeitstempel haben
			$db->retainENMDaten($this->ts);
		}

		/**
		 * Führt den Export der ENM-Daten aus dem Manager in ein Json-Objekt ENMDaten durch
		 * 
		 * @return string die vollständigen, dh. zusammengesetzten ENM-Daten als php-Objekt
		 */
		public function doExport() : string {
			// Nehme die ENM-Daten ohne Lehrer- und Schülerdaten ...
			$daten = $this->enmDaten;
			// ... und integriere die Lehrer-Daten
			$daten->lehrer = $this->enmLehrer;
			// ... und die Schüler-Daten
			$daten->schueler = $this->enmSchueler;
			return json_encode($daten);
		}

		/**
		 * Bestimme die Klassen, bei denen der Lehrer Klassenlehrer ist.
		 * 
		 * @param object $lehrer   der Lehrer, der aktuell angemeldet ist
		 * 
		 * @return array eine Map von der ID der Klasse auf das zugehörige Klassenobjekt
		 */
		public function getMapKlassen(object $lehrer) : array {
			if ($this->mapKlassen === null) {
				$this->mapKlassen = [];
				foreach ($this->enmDaten->klassen as $klasse)
					foreach ($klasse->klassenlehrer as $klid)
						if ($klid === $lehrer->id)
							$this->mapKlassen[$klasse->id] = $klasse;
			}
			return $this->mapKlassen;
		}

		/**
		 * Erstelle eine Map von der ID der Lerngruppen auf das zugehörige Objekt.
		 * 
		 * @return array eine Map von der ID der Lerngruppe auf das zugehörige Lerngruppenobjekt
		 */
		public function getMapLerngruppen() : array {
			if ($this->mapLerngruppen === null) {
				$this->mapLerngruppen = [];
				foreach ($this->enmDaten->lerngruppen as $lerngruppe)
					$this->mapLerngruppen[$lerngruppe->id] = $lerngruppe;
			}
			return $this->mapLerngruppen;
		}	

		/**
		 * Bestimme die Lerngruppen, bei denen der Lehrer als Fachlehrer eingesetzt ist.
		 * 
		 * @param object $lehrer   der Lehrer, der aktuell angemeldet ist
		 * 
		 * @return array eine Map von der ID der Lerngruppe auf das zugehörige Lerngruppenobjekt
		 */
		public function getMapLerngruppenFachlehrer(object $lehrer) : array {
			if ($this->mapLerngruppenFachlehrer === null) {
				$this->mapLerngruppenFachlehrer = [];
				foreach ($this->enmDaten->lerngruppen as $lerngruppe)
					foreach ($lerngruppe->lehrerID as $lid)
						if ($lid === $lehrer->id)
							$this->mapLerngruppenFachlehrer[$lerngruppe->id] = $lerngruppe;
			}
			return $this->mapLerngruppenFachlehrer;
		}	

		/** 
		 * Erstelle eine Map von den IDs der Ankreuzkompetenzen auf das zugehörige Objekt.
		 * 
		 * @return array die Map
		 */
		public function getMapAnkreuzkompetenzen() : array {
			if ($this->mapAnkreuzkompetenzen === null) {
				$this->mapAnkreuzkompetenzen = [];
				foreach ($this->enmDaten->ankreuzkompetenzen->kompetenzen as $kompetenz)
					$this->mapAnkreuzkompetenzen[$kompetenz->id] = $kompetenz;
			}
			return $this->mapAnkreuzkompetenzen;
		}

		/**
		 * Erstellt die ENM-Daten angepasst für den den übergebenen Lehrer
		 *
		 * @param object $lehrer   der Lehrer, für den die ENM-Daten zusammengestellt werden sollen
		 *
		 * @return string die für den Lehrer zusammengestellten ENM-Daten
		 */
		public function getENMDatenForLehrer(object $lehrer) : string {
			// Nehme die ENM-Daten ohne Lehrer- und Schülerdaten ...
			$daten = $this->enmDaten;
			// Bestimme die zu integrierenden Lehrer-Daten, entferne dabei die Informationen zu den Kennwörtern
			$daten->lehrer = $this->enmLehrer;
			foreach ($daten->lehrer as $l) {
				$l->passwordHash = "";
				$l->tsPasswordHash = null;
			}
			$mapAnkreuzkompetenzen = $this->getMapAnkreuzkompetenzen();
			$mapKlassen = $this->getMapKlassen($lehrer);
			$mapLerngruppen = $this->getMapLerngruppen();
			$mapLerngruppenFachlehrer = $this->getMapLerngruppenFachlehrer($lehrer);
			$listSchueler = [];
			foreach ($this->enmSchueler as $schueler) {
				$istKlassenlehrer = array_key_exists($schueler->klasseID, $mapKlassen);
				// Bestimme die Leistungsdaten, die übernommen werden müssen
				$leistungen = [];
				$setFachIDs = [];
				foreach ($schueler->leistungsdaten as $leistung) {
					$istFachlehrer = array_key_exists($leistung->lerngruppenID, $mapLerngruppenFachlehrer);
					if ($istFachlehrer || $istKlassenlehrer) {
						$leistungen[] = $leistung;
						$tmpLerngruppe = $mapLerngruppen[$leistung->lerngruppenID];
						if ($tmpLerngruppe != null)
							$setFachIDs[$tmpLerngruppe->fachID] = $tmpLerngruppe->fachID;
					}
				}
				// Bestimme die Ankreuzkompetenzen, die dem Lehrer über einen Leistungsdatensatz zugeordnet sind
				$kompetenzen = [];
				foreach ($schueler->ankreuzkompetenzen as $kompetenz)
					if ((($kompetenz->istFachkompetenz === true) && ($istKlassenlehrer))
						|| (($kompetenz->fachID != null) && (setFachIDs[$kompetenz->fachID] != null)))
						$kompetenzen[] = $kompetenz;
				// Prüfe, ob der Schüler zurückgegeben werden soll
				if ((count($leistungen) === 0) && (count($kompetenzen) === 0))
					continue;
				// Ersetze die Leistungsdaten und die Ankreuzkompetenzen
				$schueler->leistungsdaten = $leistungen;
				$schueler->ankreuzkompetenzen = $kompetenzen;
				$listSchueler[] = $schueler;
			}
			$daten->schueler = $listSchueler;
			$daten->lehrerID = $lehrer->id;
			return json_encode($daten);
		}

	}

?>
