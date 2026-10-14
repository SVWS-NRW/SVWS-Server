<?php

namespace wenom;

use \PDO as PDO;

/**
 * Diese Klasse regelt das Patchen von ENM-Daten durch den Client.
 */
class PatchManager {

    /** Die Datenbank-Verbindung, welche zum Patchen verwendet wird */
    public DBConnection $conn;

    /** Der Manager mit den, aus der WeNoM-Datenbank geladenen, ENM-Daten  */
    private ENMDatenManager $enmManager;

    /**
     * Erstellt einen neuen Patch-Manager
     *
     * @param ENMDatenManager $enmManager   der Manager mit den, aus der WeNoM-Datenbank geladenen, ENM-Daten
     */
    public function __construct(ENMDatenManager $enmManager) {
        $this->enmManager = $enmManager;
        $this->conn = $enmManager->conn;
    }

    /**
     * Prüft, ob die beiden Strings sich unterscheiden. Dabei wird auch auf Null-Werte geprüft.
     *
     * @param string | null $a   der erste String
     * @param string | null $b   der zweite String
     *
     * @return bool   true, wenn die beiden Werte unterschiedlich sind
     */
    private static function diffStringNullable(string | null $a, string | null $b) : bool {
        if (($a === null) && ($b === null)) {
            return false;
        }
        if (($a === null) || ($b === null)) {
            return true;
        }
        return strcmp($a, $b) !== 0;
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
    private static function diffArraySimple(array $a, array $b) : bool {
        if (count($a) !== count($b)) {
            return true;
        }
        foreach ($a as $k => $v) {
            if ((!array_key_exists($k, $b)) || ($b[$k] !== $v)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Prüfe, ob die zeitliche Eingabebeschränkung für den Eingabebeginn die Notenanpassung erlaubt oder nicht.
     *
     * @param object $config   die Konfiguration für die Klasse
     * @param string $now      der aktuelle Zeitpunkt
     */
    private function pruefeEingabebeginn(object $config, string $now): void {
        if (($config->tsEingabeAb !== null) && ($now < $config->tsEingabeAb)) {
            Http::exit403Forbidden("Die Eingabe ist noch nicht freigegeben. (Das Datum für den Eingabebeginn liegt in der Zukunft).");
        }
    }


    /**
     * Prüfe, ob die zeitliche Eingabebeschränkung die Notenanpassung erlaubt oder nicht. Für eine Leitungsfunktion gilt
     * das Eingabeende der Klassenleitung, sofern eines gesetzt ist. Die Konfiguration stellt sicher, dass es nicht vor
     * dem Eingabeende liegt.
     *
     * @param object $config   die Konfiguration für die Klasse
     * @param string $now      der aktuelle Zeitpunkt
     * @param object $rollen   die Rollen, über welche der Lehrer berechtigt ist (siehe getRollen)
     */
    private function pruefeEingabeende(object $config, string $now, object $rollen): void {
        if ($config->tsEingabeBis === null) {
            return;
        }
        $ende = $rollen->leitung ? ($config->tsEingabeBisKlassenleitung ?? $config->tsEingabeBis) : $config->tsEingabeBis;
        if ($now > $ende) {
            Http::exit403Forbidden("Die Eingabe ist nicht mehr freigegeben. (Das Datum für das Eingabeende liegt in der Vergangenheit).");
        }
    }

    /**
     * Erstellt ein Objekt mit den Rollen, über welche ein Lehrer für eine Eingabe berechtigt ist.
     *
     * @param bool $fachlehrer   gibt an, ob die Berechtigung aus der Rolle als Fachlehrer folgt
     * @param bool $leitung      gibt an, ob die Berechtigung aus einer Klassen-, Abteilungs- oder Schulleitung folgt
     *
     * @return object das Objekt mit den Attributen fachlehrer und leitung
     */
    private static function getRollen(bool $fachlehrer, bool $leitung): object {
        return (object) ['fachlehrer' => $fachlehrer, 'leitung' => $leitung];
    }

    private function pruefeSperrungSpalte(int $idKlasse, string $attr, object $rollen) {
        $config = $this->enmManager->getSperrkonfiguration($idKlasse);
        if ($config === null) {
            Http::exit403Forbidden("Es liegt keine Konfiguration für die Eingabe von Noten für die Klasse mit der ID {$idKlasse} vor.");
        }

        // Prüfe generelle Berechtigung bei der Eingabespalte
        $allowed = false;
        foreach ($config->spalten as $col) {
            if ((strcmp($attr, $col->name) === 0) && (!$col->gesperrt)) {
                $allowed = true;
                break;
            }
        }
        if ($allowed === false) {
            Http::exit403Forbidden("Eine Änderung wurde nicht explizit für die Klasse mit der ID {$idKlasse} erlaubt.");
        }

        // Prüfe die zeitliche Einschränkung für die Eingabe, sofern eine gesetzt wurde
        $now = TimeUtils::now();
        $this->pruefeEingabebeginn($config, $now);
        $this->pruefeEingabeende($config, $now, $rollen);
    }

    private function pruefeSperrungSpalteFehlstunden(int $idKlasse, bool $istGesamtFS, object $rollen) {
        $config = $this->enmManager->getSperrkonfiguration($idKlasse);
        if ($config === null) {
            Http::exit403Forbidden("Es liegt keine Konfiguration für die Eingabe von Noten für die Klasse mit der ID {$idKlasse} vor.");
        }
        // Prüfe generelle Berechtigung bei der Eingabespalte
        $allowed = false;
        foreach ($config->spalten as $col) {
            if ((strcmp("Fehlstunden" , $col->name) === 0) && (!$col->gesperrt)) {
                $allowed = true;
                break;
            }
        }
        if ($allowed === false) {
            Http::exit403Forbidden("Eine Änderung von Fehlstunden wurde nicht explizit für die Klasse mit der ID {$idKlasse} erlaubt.");
        }

        // TODO prüfe auch die Information, ob nur Gesamtfehlstunden eingegeben werden sollen oder auf Basis von Lerngruppen

        // Prüfe die zeitliche Einschränkung für die Eingabe, sofern eine gesetzt wurde
        $now = TimeUtils::now();
        $this->pruefeEingabebeginn($config, $now);
        $this->pruefeEingabeende($config, $now, $rollen);
    }

    private function pruefeSperrungSpalteTeilleistung(int $idKlasse, int $idTeilleistungsart, object $rollen) {
        $config = $this->enmManager->getSperrkonfiguration($idKlasse);
        if ($config === null) {
            Http::exit403Forbidden("Es liegt keine Konfiguration für die Eingabe von Noten für die Klasse mit der ID {$idKlasse} vor.");
        }

        // Prüfe generelle Berechtigung bei der Eingabespalte
        $allowed = false;
        $allowedSpecial = false;
        foreach ($config->spalten as $col) {
            if ((strcmp("Teilnoten", $col->name) === 0) && (!$col->gesperrt)) {
                $allowed = true;
            } elseif (($col->idTeilleistung != null) && ($col->idTeilleistung == $idTeilleistungsart) && (!$col->gesperrt)) {
                $allowedSpecial = true;
            }
        }
        if (!$allowed) {
            Http::exit403Forbidden("Eine Änderung von Teilleistungen wurde nicht explizit für die Klasse mit der ID {$idKlasse} erlaubt.");
        }
        if (!$allowedSpecial) {
            Http::exit403Forbidden("Eine Änderung der Teilleistungsart mit der ID {$idTeilleistungsart} wurde nicht explizit für die Klasse mit der ID {$idKlasse} erlaubt.");
        }

        // Prüfe die zeitliche Einschränkung für die Eingabe, sofern eine gesetzt wurde
        $now = TimeUtils::now();
        $this->pruefeEingabebeginn($config, $now);
        $this->pruefeEingabeende($config, $now, $rollen);
    }


    /**
     * Bestimmt die Rollen, über welche der Lehrer die Leistungsdaten ändern darf. Die Leitungsrolle wird auch bei
     * Fachlehrern geprüft, da sie das Eingabeende bestimmt.
     *
     * @param object $lehrer     der angemeldete Lehrer
     * @param object $leistung   die Leistungsdaten
     *
     * @return object die Rollen (siehe getRollen)
     */
    private function getRollenLeistung(object $lehrer, object $leistung): object {
        // Prüfe, ob der Lehrer Fachlehrer für die Lerngruppe der Leistungsdaten ist
        $mapLerngruppenFachlehrer = $this->enmManager->getMapLerngruppenFachlehrer($lehrer);
        $istFachlehrer = array_key_exists($leistung->lerngruppenID, $mapLerngruppenFachlehrer);
        if ($this->enmManager->istSchulleitung($lehrer)) {
            return self::getRollen($istFachlehrer, true);
        }

        // Prüfe, ob der Lehrer Klassenleiter oder Abteilungsleiter ist
        $mapKlassen = $this->enmManager->getMapKlassen($lehrer);
        $mapKlassenAbteilung = $this->enmManager->getMapKlassenAbteilung($lehrer);

        $mapsLeistungsdaten = $this->enmManager->getMapsLeistungsdaten();
        if (!array_key_exists($leistung->id, $mapsLeistungsdaten->schueler)) {
            return self::getRollen($istFachlehrer, false);
        }
        $schueler = $mapsLeistungsdaten->schueler[$leistung->id];
        if (array_key_exists($schueler->klasseID, $mapKlassenAbteilung)) {
            return self::getRollen($istFachlehrer, true);
        }

        // Die Klassenleitung darf Leistungen nur ändern, wenn die Konfiguration der Klasse dies freigibt
        $config = $this->enmManager->getSperrkonfiguration($schueler->klasseID);
        $istFreigegeben = ($config !== null) && (($config->istNoteneingabeDurchKlassenleitung ?? false) === true);
        return self::getRollen($istFachlehrer, $istFreigegeben && array_key_exists($schueler->klasseID, $mapKlassen));
    }


    private function pruefeEingabeSchuelerKlassenlehrer(object $lehrer, object $schueler): bool {
        // Prüfe, ob der Lehrer Mitglied der Schulleitung ist
        if ($this->enmManager->istSchulleitung($lehrer)) {
            return true;
        }
        // Prüfe, ob der Lehrer Abteilungsleiter der Klasse des Schülers ist
        $mapKlassenAbteilung = $this->enmManager->getMapKlassenAbteilung($lehrer);
        if (array_key_exists($schueler->klasseID, $mapKlassenAbteilung)) {
            return true;
        }
        // Prüfe, ob der Lehrer Klassenlehrer für den Schüler ist
        $mapKlassen = $this->enmManager->getMapKlassen($lehrer);
        return array_key_exists($schueler->klasseID, $mapKlassen);
    }


    /**
     * Führt einen Patch auf ENM-Leistungen durch. Dabei wird die ID aus dem Patch verwendet, um die
     * zugehörigen Leistungsdaten aus der Datenbank zu ermitteln. Anschließend werden dies zusammen mit
     * dem Patch an die Datenbank zur Durchführung der Update-Methode übergeben.
     * Folgende Werte und Zeitstempel können durch das Patch Objekt überschrieben werden:
     *   note, noteQuartal, fehlstundenFach, fehlstundenUnentschuldigtFach, fachbezogeneBemerkungen, istGemahnt
     *
     * @param object $lehrer   der angemeldete Lehrer
     * @param object $patch    der Patch
     */
    public function patchENMLeistung(object $lehrer, object $patch): void {
        // Prüfe, ob eine ID für die Leistungsdaten im Patch vorhanden ist
        if ($patch->id === null) {
            Http::exit400BadRequest("Es muss eine ID angegeben werden, damit die Leistungsdaten angepasst werden können.");
        }
        // Prüfe, ob Leistungsdaten für die ID vorhanden sind
        $mapsSchueler = $this->enmManager->getMapsSchueler();
        if (!array_key_exists($patch->id, $mapsSchueler->leistungen)) {
            Http::exit404NotFound("Es wurde keine Leistung mit der ID {$patch->id} gefunden.");
        }
        $leistung = $mapsSchueler->leistungen[$patch->id];
        $rollen = $this->getRollenLeistung($lehrer, $leistung);
        if (!$rollen->fachlehrer && !$rollen->leitung) {
            Http::exit403Forbidden("Es wurde keine Lerngruppe für die ID {$leistung->lerngruppenID} zu der Leistung mit der ID {$patch->id} gefunden, wo der angemeldete Lehrer Fachlehrer ist oder funktionsbezogene Kompetenzen hat.");
        }

        $mapNoten = $this->enmManager->getMapNoten();
        $this->dbPatchENMLeistung($leistung, $patch, $mapNoten, $rollen);
    }

    /**
     * Führt einen Patch auf ENM-Lernabschnitte von Schülern durch. Dabei wird die ID aus dem Patch verwendet, um die
     * zugehörigen Lernabschnittsdaten aus der Datenbank zu ermitteln. Anschließend werden dies zusammen mit
     * dem Patch an die Datenbank zur Durchführung der Update-Methode übergeben.
     * Folgende Werte und Zeitstempel können durch das Patch Objekt überschrieben werden:
     *   fehlstundenGesamt, fehlstundenGesamtUnentschuldigt
     *
     * @param object $lehrer   der angemeldete Lehrer
     * @param object $patch    der Patch
     */
    public function patchENMSchuelerLernabschnitt(object $lehrer, object $patch): void {
        // Prüfe, ob eine ID für die Lernabschnittsdaten im Patch vorhanden ist
        if ($patch->id === null) {
            Http::exit400BadRequest("Es muss eine ID angegeben werden, damit die Lernabschnittsdaten angepasst werden können.");
        }
        // Prüfe, ob Lernabschnittsdaten für die ID vorhanden sind
        $mapsSchueler = $this->enmManager->getMapsSchueler();
        if (!array_key_exists($patch->id, $mapsSchueler->lernabschnitte) || !array_key_exists($patch->id, $mapsSchueler->lernabschnittSchueler)) {
            Http::exit404NotFound("Es wurde kein Lernabschnitt mit der ID {$patch->id} gefunden.");
        }
        $lernabschnitt = $mapsSchueler->lernabschnitte[$patch->id];
        $schueler = $mapsSchueler->lernabschnittSchueler[$patch->id];
        // Prüfe, ob der Lehrer Klassenlehrer für den Schüler des Lernabschnittes ist - oder auch Abteilungsleitung oderSchulleitung
        if (!$this->pruefeEingabeSchuelerKlassenlehrer($lehrer, $schueler)) {
            Http::exit403Forbidden("Der angemeldete Lehrer ist kein Klassenlehrer, Ableitungsleiter oder Schulleiter der Klasse mit der ID {$schueler->klasseID}.");
        }
        $this->dbPatchENMSchuelerLernabschnitt($schueler, $patch, self::getRollen(false, true));
    }

    /**
     * Führt einen Patch auf ENM-Bemerkungen von Schülern durch. Dabei muss die ID des Schülers mit dem Patch
     * übergeben verwendet, um die zugehörigen Bemerkungen aus der Datenbank zu ermitteln.
     * Anschließend werden dies zusammen mit dem Patch an die Datenbank zur Durchführung der Update-Methode übergeben.
     * Folgende Werte und Zeitstempel können durch das Patch Objekt überschrieben werden:
     *   ASV, AUE, ZB, LELS, schulformEmpf, individuelleVersetzungsbemerkungen, foerderbemerkungen
     *
     * @param object $lehrer    der angemeldete Lehrer
     * @param int $idSchueler   die ID des Schülers
     * @param object $patch     der Patch
     */
    public function patchENMSchuelerBemerkungen(object $lehrer, int $idSchueler, object $patch): void {
        // Prüfe, ob Bemerkungen für die Schüler-ID vorhanden sind
        $mapsSchueler = $this->enmManager->getMapsSchueler();
        if (!array_key_exists($idSchueler, $mapsSchueler->bemerkungen) || !array_key_exists($idSchueler, $mapsSchueler->schueler)) {
            Http::exit404NotFound("Es wurden kein Schüler mit der ID ".$idSchueler." bzw. Bemerkungen für einen solchen Schüler gefunden.");
        }
        $schueler = $mapsSchueler->schueler[$idSchueler];
        // Prüfe, ob der Lehrer Klassenlehrer für den Schüler ist - oder auch Abteilungsleitung oderSchulleitung
        if (!$this->pruefeEingabeSchuelerKlassenlehrer($lehrer, $schueler)) {
            Http::exit403Forbidden("Der angemeldete Lehrer ist kein Klassenlehrer, Ableitungsleiter oder Schulleiter der Klasse mit der ID {$schueler->klasseID}.");
        }
        $this->dbPatchENMSchuelerBemerkungen($idSchueler, $schueler, $patch, self::getRollen(false, true));
    }

    /**
     * Führt einen Patch auf ENM-Teilleistungen durch. Dabei wird die ID aus dem Patch verwendet, um die
     * zugehörigen Teilleistungen aus der Datenbank zu ermitteln. Anschließend werden dies zusammen mit
     * dem Patch an die Datenbank zur Durchführung der Update-Methode übergeben.
     * Folgende Werte und Zeitstempel können durch das Patch Objekt überschrieben werden:
     *   datum, bemerkung, note
     *
     * @param object $lehrer   der angemeldete Lehrer
     * @param object $patch    der Patch
     */
    public function patchENMTeilleistung(object $lehrer, object $patch): void {
        // Prüfe, ob eine ID für die Teilleistungen im Patch vorhanden ist
        if ($patch->id === null) {
            Http::exit400BadRequest("Es muss eine ID angegeben werden, damit die Teilleistungen angepasst werden können.");
        }
        // Prüfe, ob Teilleistungen für die ID vorhanden sind
        $mapsSchueler = $this->enmManager->getMapsSchueler();
        if (!array_key_exists($patch->id, $mapsSchueler->teilleistungen) || !array_key_exists($patch->id, $mapsSchueler->teilleistungLeistung)) {
            Http::exit404NotFound("Es wurde keine Teilleistung mit der ID ".$patch->id." gefunden.");
        }
        $teilleistung = $mapsSchueler->teilleistungen[$patch->id];
        $teilleistungLeistung = $mapsSchueler->teilleistungLeistung[$patch->id];
        // Prüfe, ob der Lehrer Fachlehrer für die Lerngruppe der Leistungsdaten ist
        $rollen = $this->getRollenLeistung($lehrer, $teilleistungLeistung);
        if (!$rollen->fachlehrer && !$rollen->leitung) {
            Http::exit403Forbidden("Es wurde keine Lerngruppe für die ID ".$teilleistungLeistung->lerngruppenID." zu der Teilleistung mit der ID ".$patch->id." gefunden, wo der angemeldete Lehrer Fachlehrer ist.");
        }
        $mapNoten = $this->enmManager->getMapNoten();
        $this->dbPatchENMTeilleistung($teilleistung, $patch, $mapNoten, $rollen);
    }

    /**
     * Führt einen Patch auf ENM-Ankreuzkompetenzen von Schülern durch. Dabei wird die ID aus dem Patch verwendet,
     * um die zugehörigen Ankreuzkompetenzen aus der Datenbank zu ermitteln. Anschließend werden dies zusammen mit
     * dem Patch an die Datenbank zur Durchführung der Update-Methode übergeben.
     * Folgende Werte und Zeitstempel können durch das Patch Objekt überschrieben werden:
     *   Stufen
     *
     * @param object $lehrer   der angemeldete Lehrer
     * @param object $patch    der Patch
     */
    public function patchENMSchuelerAnkreuzkompetenzen(object $lehrer, object $patch): void {
        // Prüfe, ob eine ID für die Ankreuzkompetenz im Patch vorhanden ist
        if ($patch->id === null) {
            Http::exit400BadRequest("Es muss eine ID angegeben werden, damit die Ankreuzkompetenz angepasst werden kann.");
        }
        // Prüfe, ob eine Ankreuzkompetenz für die ID vorhanden sind
        $mapsSchueler = $this->enmManager->getMapsSchueler();
        if (!array_key_exists($patch->id, $mapsSchueler->ankreuzkompetenzen) || !array_key_exists($patch->id, $mapsSchueler->ankreuzkompetenzSchueler)) {
            Http::exit404NotFound("Es wurden keine Ankreuzkompetenz mit der ID ".$patch->id." gefunden.");
        }
        $ankreuzkompetenz = $mapsSchueler->ankreuzkompetenzen[$patch->id];
        $schueler = $mapsSchueler->ankreuzkompetenzSchueler[$patch->id];
        // Prüfe, ob der Lehrer Klassenlehrer für den Schüler der Ankreuzkompetenz ist
        $istKlassenLehrer = $this->pruefeEingabeSchuelerKlassenlehrer($lehrer, $schueler);
        $istFachlehrer = $this->enmManager->istAnkreuzkompetenzFachlehrer($lehrer, $schueler, $ankreuzkompetenz);
        // Bei Kompetenzen eines Fachs braucht die Klassenleitung die Freigabe der Noteneingabe, die Abteilungs- und Schulleitung nicht
        $kompetenz = $this->enmManager->getMapAnkreuzkompetenzen()[$ankreuzkompetenz->kompetenzID];
        if ($istKlassenLehrer && ($kompetenz->fachID !== null) && !$this->enmManager->istSchulleitung($lehrer)
                && !array_key_exists($schueler->klasseID, $this->enmManager->getMapKlassenAbteilung($lehrer))) {
            $config = $this->enmManager->getSperrkonfiguration($schueler->klasseID);
            $istKlassenLehrer = ($config !== null) && (($config->istNoteneingabeDurchKlassenleitung ?? false) === true);
        }
        if (!$istKlassenLehrer && !$istFachlehrer) {
            Http::exit403Forbidden("Der angemeldete Lehrer darf die Ankreuzkompetenz in der Klasse mit der ID ".$schueler->klasseID." nicht bearbeiten.");
        }
        $this->dbPatchENMSchuelerAnkreuzkompetenzen($ankreuzkompetenz, $patch, self::getRollen($istFachlehrer, $istKlassenLehrer));
    }

    /**
     * Erstellt einen Update-Befehl für die Datenbank aus den übergebenen Daten für einen
     * Patch von Lehrer-Daten zu dem Passwort.
     *
     * @param DBConnection $conn   die Datenbank-Verbindung
     * @param object $daten        die Daten aus der Datenbank
     * @param object $patch        der Patch für die Daten
     */
    public static function patchENMLehrerPassword(DBConnection $conn, object $daten, object $patch): void {
        $ts = TimeUtils::now();
        if (property_exists($patch, 'passwordHash') && PatchManager::diffStringNullable($patch->passwordHash, $daten->passwordHash) && ($ts > $daten->tsPasswordHash)) {
            $sql = "UPDATE Lehrer SET passwordHash=:passwordHash,tsPasswordHash='$ts',daten=:daten WHERE id=:id";
            $stmt = $conn->prepareStatement($sql);
            $conn->bindStatementValue($stmt, ":passwordHash", $patch->passwordHash, PDO::PARAM_STR);
            $daten->passwordHash = $patch->passwordHash;
            $daten->tsPasswordHash = $ts;
            $conn->bindStatementValue($stmt, ":daten", json_encode($daten, JSON_UNESCAPED_SLASHES), PDO::PARAM_STR);
            $conn->bindStatementValue($stmt, ":id", $patch->id, PDO::PARAM_INT);
            $conn->executeStatement($stmt);
        }
    }

    /**
     * Erstellt einen Update-Befehl für die Datenbank aus den übergebenen Daten für einen
     * Patch von Leistungsdaten.
     * Folgende Werte und Zeitstempel können durch das Patch Objekt überschrieben werden:
     *   note, noteQuartal, fehlstundenFach, fehlstundenUnentschuldigtFach, fachbezogeneBemerkungen, istGemahnt
     *
     * @param object $daten     die Daten aus der Datenbank
     * @param object $patch     der Patch für die Daten
     * @param array $mapNoten   eine Array, welches von dem Noten-Kürzel auf das Noten-Objekt der ENM-Daten verweist
     * @param object $rollen    die Rollen, über welche der Lehrer berechtigt ist (siehe getRollen)
     */
    protected function dbPatchENMLeistung(object $daten, object $patch, array $mapNoten, object $rollen): void {
        $idKlasse = $this->enmManager->getKlassenIdByLeistungsdatenId($patch->id);
        $ts = TimeUtils::now();
        $update = "";
        if (property_exists($patch, 'note') && PatchManager::diffStringNullable($patch->note, $daten->note) && ($ts > $daten->tsNote)) {
            $this->pruefeSperrungSpalte($idKlasse, 'Note', $rollen);
            $istNote = array_key_exists($patch->note, $mapNoten);
            if (!$istNote && ($patch->note !== null)) {
                Http::exit400BadRequest("Der Patch-Methode wurde eine ungültige Note übergeben.");
            }
            $update .= "tsNote='$ts',";
            $daten->note = $patch->note;
            $daten->tsNote = $ts;
        }
        if (property_exists($patch, 'noteQuartal') && PatchManager::diffStringNullable($patch->noteQuartal, $daten->noteQuartal) && ($ts > $daten->tsNoteQuartal)) {
            $this->pruefeSperrungSpalte($idKlasse, 'Quartal', $rollen);
            $istNote = array_key_exists($patch->noteQuartal, $mapNoten);
            if (!$istNote && ($patch->noteQuartal !== null)) {
                Http::exit400BadRequest("Der Patch-Methode wurde eine ungültige Quartals-Note übergeben.");
            }
            $update .= "tsNoteQuartal='$ts',";
            $daten->noteQuartal = $patch->noteQuartal;
            $daten->tsNoteQuartal = $ts;
        }
        if (property_exists($patch, 'fehlstundenFach') && ($patch->fehlstundenFach !== $daten->fehlstundenFach) && ($ts > $daten->tsFehlstundenFach)) {
            $this->pruefeSperrungSpalteFehlstunden($idKlasse, false, $rollen);
            if (!is_null($patch->fehlstundenFach) && (!is_int($patch->fehlstundenFach) || ($patch->fehlstundenFach < 0))) {
                Http::exit400BadRequest("Es wurde eine fehlerhafter Wert für die Fehlstunden angegeben.");
            }
            $update .= "tsFehlstundenFach='$ts',";
            $daten->fehlstundenFach = $patch->fehlstundenFach;
            $daten->tsFehlstundenFach = $ts;
        }
        if (property_exists($patch, 'fehlstundenUnentschuldigtFach') && ($patch->fehlstundenUnentschuldigtFach !== $daten->fehlstundenUnentschuldigtFach) && ($ts > $daten->tsFehlstundenUnentschuldigtFach)) {
            $this->pruefeSperrungSpalteFehlstunden($idKlasse, false, $rollen);
            if (!is_null($patch->fehlstundenUnentschuldigtFach) && (!is_int($patch->fehlstundenUnentschuldigtFach) || ($patch->fehlstundenUnentschuldigtFach < 0))) {
                Http::exit400BadRequest("Es wurde eine fehlerhafter Wert für die unentschuldigten Fehlstunden angegeben.");
            }
            $update .= "tsFehlstundenUnentschuldigtFach='$ts',";
            $daten->fehlstundenUnentschuldigtFach = $patch->fehlstundenUnentschuldigtFach;
            $daten->tsFehlstundenUnentschuldigtFach = $ts;
        }
        if (property_exists($patch, 'fachbezogeneBemerkungen') && PatchManager::diffStringNullable($patch->fachbezogeneBemerkungen, $daten->fachbezogeneBemerkungen) && ($ts > $daten->tsFachbezogeneBemerkungen)) {
            $this->pruefeSperrungSpalte($idKlasse, 'FB', $rollen);
            $update .= "tsFachbezogeneBemerkungen='$ts',";
            $daten->fachbezogeneBemerkungen = $patch->fachbezogeneBemerkungen;
            $daten->tsFachbezogeneBemerkungen = $ts;
        }
        if (property_exists($patch, 'istGemahnt') && ($patch->istGemahnt !== $daten->istGemahnt) && ($ts > $daten->tsIstGemahnt)) {
            $this->pruefeSperrungSpalte($idKlasse, 'Mahnung', $rollen);
            if (($patch->istGemahnt !== null) && !is_bool($patch->istGemahnt)) {
                Http::exit400BadRequest("Es wurde eine fehlerhafter Wert für das Feld istGemahnt angegeben.");
            }
            $update .= "tsIstGemahnt='$ts',";
            $daten->istGemahnt = $patch->istGemahnt;
            $daten->tsIstGemahnt = $ts;
        }
        if (property_exists($patch, 'neueZuweisungKursart') && PatchManager::diffStringNullable($patch->neueZuweisungKursart, $daten->neueZuweisungKursart) && (($daten->tsNeueZuweisungKursart === null) || ($ts > $daten->tsNeueZuweisungKursart))) {
            $this->pruefeSperrungSpalte($idKlasse, 'Note', $rollen);
            if (($patch->neueZuweisungKursart !== null) && ($patch->neueZuweisungKursart !== "E") && ($patch->neueZuweisungKursart !== "G")) {
                Http::exit400BadRequest("Der Patch-Methode wurde eine ungültige Kursart-Zuweisung übergeben.");
            }
            $update .= "tsNeueZuweisungKursart='$ts',";
            $daten->neueZuweisungKursart = $patch->neueZuweisungKursart;
            $daten->tsNeueZuweisungKursart = $ts;
        }
        if (strlen($update) > 0) {
            // Stelle sicher in der Datenbanktabelle nicht Objekte auftauchen die woanders in der Datenbank gespeichert werden
            $daten->teilleistungen = [];
            // Schreibe das gepatchte JSON in die Datenbank zurück
            $updatedData = json_encode($daten, JSON_UNESCAPED_SLASHES);

            $this->conn->beginTransaction();
            $stmt = $this->conn->prepareStatement("UPDATE Leistungsdaten SET $update daten=:daten WHERE id=:id");
            $this->conn->bindStatementValue($stmt, ":daten", $updatedData, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmt, ":id", $patch->id, PDO::PARAM_INT);
            $this->conn->executeStatement($stmt);
            $this->conn->commitTransaction();
        }
    }

    /**
     * Erstellt einen Update-Befehl für die Datenbank aus den übergebenen Daten für einen
     * Patch von Lernabschnittsdaten eines Schülers.
     * Folgende Werte und Zeitstempel können durch das Patch Objekt überschrieben werden:
     *   fehlstundenGesamt, fehlstundenGesamtUnentschuldigt
     *
     * @param object $daten   die Daten aus der Datenbank
     * @param object $patch   der Patch für die Daten
     * @param object $rollen  die Rollen, über welche der Lehrer berechtigt ist (siehe getRollen)
     */
    protected function dbPatchENMSchuelerLernabschnitt(object $daten, object $patch, object $rollen): void {
        $idKlasse = $this->enmManager->getKlassenIdByLernabschnittsId($patch->id);
        $ts = TimeUtils::now();
        $update = "";
        if (property_exists($patch, 'fehlstundenGesamt') && ($ts > $daten->lernabschnitt->tsFehlstundenGesamt)
                && ($patch->fehlstundenGesamt !== $daten->lernabschnitt->fehlstundenGesamt)) {
            $this->pruefeSperrungSpalteFehlstunden($idKlasse, true, $rollen);
            if (!is_null($patch->fehlstundenGesamt) && (!is_int($patch->fehlstundenGesamt) || ($patch->fehlstundenGesamt < 0))) {
                Http::exit400BadRequest("Es wurde eine fehlerhafter Wert für Gesamt-Fehlstunden angegeben.");
            }
            $update .= "tsFehlstundenGesamt='$ts',";
            $daten->lernabschnitt->fehlstundenGesamt = $patch->fehlstundenGesamt;
            $daten->lernabschnitt->tsFehlstundenGesamt = $ts;
        }
        if (property_exists($patch, 'fehlstundenGesamtUnentschuldigt') && ($ts > $daten->lernabschnitt->tsFehlstundenGesamtUnentschuldigt)
                && ($patch->fehlstundenGesamtUnentschuldigt !== $daten->lernabschnitt->fehlstundenGesamtUnentschuldigt)) {
            $this->pruefeSperrungSpalteFehlstunden($idKlasse, true, $rollen);
            if (!is_null($patch->fehlstundenGesamtUnentschuldigt) && (!is_int($patch->fehlstundenGesamtUnentschuldigt) || ($patch->fehlstundenGesamtUnentschuldigt < 0))) {
                Http::exit400BadRequest("Es wurde eine fehlerhafter Wert für unentschuldigten Gesamt-Fehlstunden angegeben.");
            }
            $update .= "tsFehlstundenGesamtUnentschuldigt='$ts',";
            $daten->lernabschnitt->fehlstundenGesamtUnentschuldigt = $patch->fehlstundenGesamtUnentschuldigt;
            $daten->lernabschnitt->tsFehlstundenGesamtUnentschuldigt = $ts;
        }
        if (strlen($update) > 0) {
            // Stelle sicher in der Datenbanktabelle nicht Objekte auftauchen, die woanders in der Datenbank gespeichert werden
            $daten->ankreuzkompetenzen = [];
            $daten->leistungsdaten = [];
            $daten->zp10 = [];
            $daten->sprachenfolge = [];
            // Schreibe das gepatchte JSON in die Datenbank zurück
            $updatedData = json_encode($daten, JSON_UNESCAPED_SLASHES);

            $this->conn->beginTransaction();
            $stmt = $this->conn->prepareStatement("UPDATE Schueler SET $update daten=:daten WHERE id=:id");
            $this->conn->bindStatementValue($stmt, ":daten", $updatedData, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmt, ":id", $daten->id, PDO::PARAM_INT);
            $this->conn->executeStatement($stmt);
            $this->conn->commitTransaction();
        }
    }

    /**
     * Erstellt einen Update-Befehl für die Datenbank aus den übergebenen Daten für einen
     * Patch von Bemerkungsdaten eines Schülers.
     * Folgende Werte und Zeitstempel können durch das Patch Objekt überschrieben werden:
     *   ASV, AUE, ZB, LELS, schulformEmpf, individuelleVersetzungsbemerkungen, foerderbemerkungen
     *
     * @param int $idSchueler   die ID des Schülers
     * @param object $daten     die Daten aus der Datenbank
     * @param object $patch     der Patch für die Daten
     * @param object $rollen    die Rollen, über welche der Lehrer berechtigt ist (siehe getRollen)
     */
    protected function dbPatchENMSchuelerBemerkungen(int $idSchueler, object $daten, object $patch, object $rollen): void {
        $idKlasse = $this->enmManager->getKlassenIdBySchuelerId($idSchueler);
        $ts = TimeUtils::now();
        $update = "";
        if (property_exists($patch, 'ASV') && ($ts > $daten->bemerkungen->tsASV)
                && PatchManager::diffStringNullable($daten->bemerkungen->ASV, $patch->ASV)) {
            $this->pruefeSperrungSpalte($idKlasse, 'ASV', $rollen);
            $update .= "tsASV='$ts',";
            $daten->bemerkungen->ASV = $patch->ASV;
            $daten->bemerkungen->tsASV = $ts;
        }
        if (property_exists($patch, 'AUE') && ($ts > $daten->bemerkungen->tsAUE)
                && PatchManager::diffStringNullable($daten->bemerkungen->AUE, $patch->AUE)) {
            $this->pruefeSperrungSpalte($idKlasse, 'AUE', $rollen);
            $update .= "tsAUE='$ts',";
            $daten->bemerkungen->AUE = $patch->AUE;
            $daten->bemerkungen->tsAUE = $ts;
        }
        if (property_exists($patch, 'ZB') && ($ts > $daten->bemerkungen->tsZB)
                && PatchManager::diffStringNullable($daten->bemerkungen->ZB, $patch->ZB)) {
            $this->pruefeSperrungSpalte($idKlasse, 'ZB', $rollen);
            $update .= "tsZB='$ts',";
            $daten->bemerkungen->ZB = $patch->ZB;
            $daten->bemerkungen->tsZB = $ts;
        }
        if (property_exists($patch, 'LELS') && ($ts > $daten->bemerkungen->tsLELS)
                && PatchManager::diffStringNullable($daten->bemerkungen->LELS, $patch->LELS)) {
            $this->pruefeSperrungSpalte($idKlasse, 'LELS', $rollen);
            $update .= "tsLELS='$ts',";
            $daten->bemerkungen->LELS = $patch->LELS;
            $daten->bemerkungen->tsLELS = $ts;
        }
        if (property_exists($patch, 'schulformEmpf') && ($ts > $daten->bemerkungen->tsSchulformEmpf)
                && PatchManager::diffStringNullable($daten->bemerkungen->schulformEmpf, $patch->schulformEmpf)) {
            $this->pruefeSperrungSpalte($idKlasse, 'SchulformEmpfehlung', $rollen);
            $update .= "tsSchulformEmpf='$ts',";
            $daten->bemerkungen->schulformEmpf = $patch->schulformEmpf;
            $daten->bemerkungen->tsSchulformEmpf = $ts;
        }
        if (property_exists($patch, 'individuelleVersetzungsbemerkungen') && ($ts > $daten->bemerkungen->tsIndividuelleVersetzungsbemerkungen)
                && PatchManager::diffStringNullable($daten->bemerkungen->individuelleVersetzungsbemerkungen, $patch->individuelleVersetzungsbemerkungen)) {
            $this->pruefeSperrungSpalte($idKlasse, 'Versetzungsbemerkungen', $rollen);
            $update .= "tsIndividuelleVersetzungsbemerkungen='$ts',";
            $daten->bemerkungen->individuelleVersetzungsbemerkungen = $patch->individuelleVersetzungsbemerkungen;
            $daten->bemerkungen->tsIndividuelleVersetzungsbemerkungen = $ts;
        }
        if (property_exists($patch, 'foerderbemerkungen') && ($ts > $daten->bemerkungen->tsFoerderbemerkungen)
                && PatchManager::diffStringNullable($daten->bemerkungen->foerderbemerkungen, $patch->foerderbemerkungen)) {
            $this->pruefeSperrungSpalte($idKlasse, 'Förderbemerkungen', $rollen);
            $update .= "tsFoerderbemerkungen='$ts',";
            $daten->bemerkungen->foerderbemerkungen = $patch->foerderbemerkungen;
            $daten->bemerkungen->tsFoerderbemerkungen = $ts;
        }
        if (strlen($update) > 0) {
            // Stelle sicher in der Datenbanktabelle nicht Objekte auftauchen die woanders in der Datenbank gespeichert werden
            $daten->ankreuzkompetenzen = [];
            $daten->leistungsdaten = [];
            $daten->zp10 = [];
            $daten->sprachenfolge = [];
            // Schreibe das gepatchte JSON in die Datenbank zurück
            $updatedData = json_encode($daten, JSON_UNESCAPED_SLASHES);

            $this->conn->beginTransaction();
            $stmt = $this->conn->prepareStatement("UPDATE Schueler SET $update daten=:daten WHERE id=:id");
            $this->conn->bindStatementValue($stmt, ":daten", $updatedData, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmt, ":id", $idSchueler, PDO::PARAM_INT);
            $this->conn->executeStatement($stmt);
            $this->conn->commitTransaction();
        }
    }

    /**
     * Erstellt einen Update-Befehl für die Datenbank aus den übergebenen Daten für einen
     * Patch von Daten zu Teilleistungen.
     * Folgende Werte und Zeitstempel können durch das Patch Objekt überschrieben werden:
     *   datum, bemerkung, note
     *
     * @param object $daten     die Daten aus der Datenbank
     * @param object $patch     der Patch für die Daten
     * @param array $mapNoten   eine Array, welches von dem Noten-Kürzel auf das Noten-Objekt der ENM-Daten verweist
     * @param object $rollen    die Rollen, über welche der Lehrer berechtigt ist (siehe getRollen)
     */
    protected function dbPatchENMTeilleistung(object $daten, object $patch, array $mapNoten, object $rollen): void {
        $idKlasse = $this->enmManager->getKlassenIdByTeilleistungId($patch->id);
        $ts = TimeUtils::now();
        $update = "";
        if (property_exists($patch, 'artID') && ($patch->artID !== $daten->artID) && ($ts > $daten->tsArtID)) {
            Http::exit400BadRequest("Das Verändern der Teilleistungsart ist nicht erlaubt.");
        }
        if (property_exists($patch, 'datum') && PatchManager::diffStringNullable($patch->datum, $daten->datum) && ($ts > $daten->tsDatum)) {
            $this->pruefeSperrungSpalteTeilleistung($idKlasse, $daten->artID, $rollen);
            $update .= "tsDatum='$ts',";
            $daten->datum = $patch->datum;
            $daten->tsDatum = $ts;
        }
        if (property_exists($patch, 'bemerkung') && PatchManager::diffStringNullable($patch->bemerkung, $daten->bemerkung) && ($ts > $daten->tsBemerkung)) {
            $this->pruefeSperrungSpalteTeilleistung($idKlasse, $daten->artID, $rollen);
            $update .= "tsBemerkung='$ts',";
            $daten->bemerkung = $patch->bemerkung;
            $daten->tsBemerkung = $ts;
        }
        if (property_exists($patch, 'note') && PatchManager::diffStringNullable($patch->note, $daten->note) && ($ts > $daten->tsNote)) {
            $this->pruefeSperrungSpalteTeilleistung($idKlasse, $daten->artID, $rollen);
            $istNote = array_key_exists($patch->note, $mapNoten);
            if (!$istNote && ($patch->note !== null)) {
                Http::exit400BadRequest("Der Patch-Methode wurde eine ungültige Note übergeben.");
            }
            $update .= "tsNote='$ts',";
            $daten->note = $patch->note;
            $daten->tsNote = $ts;
        }
        if (strlen($update) > 0) {
            $updatedData = json_encode($daten, JSON_UNESCAPED_SLASHES);

            $this->conn->beginTransaction();
            $stmt = $this->conn->prepareStatement("UPDATE Teilleistungen SET $update daten=:daten WHERE id=:id");
            $this->conn->bindStatementValue($stmt, ":daten", $updatedData, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmt, ":id", $patch->id, PDO::PARAM_INT);
            $this->conn->executeStatement($stmt);
            $this->conn->commitTransaction();
        }
    }

    /**
     * Erstellt einen Update-Befehl für die Datenbank aus den übergebenen Daten für einen
     * Patch von Schüler-Daten zu den Ankreuzkompetenzen.
     * Folgende Werte und Zeitstempel können durch das Patch Objekt überschrieben werden:
     *   Stufen
     *
     * @param object $daten   die Daten aus der Datenbank
     * @param object $patch   der Patch für die Daten
     * @param object $rollen  die Rollen, über welche der Lehrer berechtigt ist (siehe getRollen)
     *
     */
    protected function dbPatchENMSchuelerAnkreuzkompetenzen(object $daten, object $patch, object $rollen): void {
        $idKlasse = $this->enmManager->getKlassenIdByAnkreuzkompetenzId($patch->id);
        $ts = TimeUtils::now();
        $update = "";
        if (property_exists($patch, 'stufen') && PatchManager::diffArraySimple($patch->stufen, $daten->stufen) && ($ts > $daten->tsStufe)) {
            if ($this->enmManager->istAnkreuzkompetenzASV($daten)) {
                $this->pruefeSperrungSpalte($idKlasse, 'ASV', $rollen);
            } else {
                $this->pruefeSperrungSpalte($idKlasse, 'Note', $rollen);
            }
            foreach ($patch->stufen as $index=>$stufe) {
                if (!is_bool($stufe)) {
                    Http::exit500("Fehler beim Ausführen des Patch-Statements. Stufe mit Index ".$index." in der Ankreuzkompetenz ist kein Boolean-Wert. Patch wurde abgebrochen.");
                }
            }
            $update .= "tsStufe='$ts',";
            $daten->stufen = $patch->stufen;
            $daten->tsStufe = $ts;
        }
        if (strlen($update) > 0) {
            $updatedData = json_encode($daten, JSON_UNESCAPED_SLASHES);

            $this->conn->beginTransaction();
            $stmt = $this->conn->prepareStatement("UPDATE Ankreuzkompetenzen SET $update daten=:daten WHERE id=:id");
            $this->conn->bindStatementValue($stmt, ":daten", $updatedData, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmt, ":id", $patch->id, PDO::PARAM_INT);
            $this->conn->executeStatement($stmt);
            $this->conn->commitTransaction();
        }
    }

}
