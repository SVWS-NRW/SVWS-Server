<?php

namespace wenom;

use \JsonException as JsonException;
use \PDO as PDO;
use \PDOStatement as PDOStatement;

/**
 * Diese Klasse regelt den Import der ENM-Daten von einem SVWS-Server über die Secure-API
 */
class ImportManager {

    /** Die ENM-Revision mit welcher diese Klasse arbeitet */
    public int $enmRevisionRequired = 1;

    /** Die Datenbank-Verbindung für die Verbindung zur WeNoM-Datenbank */
    protected DBConnection $conn;

    /** Der Zeitstempel, wann der Manager erzeugt wurde. Dies ist für den Import von Daten relevant, um die neuen Daten von den alten Daten zu unterscheiden */
    protected int $ts;

    /** Die ENM-Daten ohne Lehrer und Schüler-Informationen */
    protected object $enmDaten;

    /** Die Informationen zu Lehrern, die in der Notendatei vorhanden sind. */
    protected array $enmLehrer;

    /** Die Informationen zu den Schülern, deren Noten in dieser Notendatei verwaltet werden. */
    protected array $enmSchueler;


    /**
     * Erstellt einen neuen nicht initialisierten Manager zur Verfügung.
     *
     * @param DBConnection $conn     die Datenbank-Verbindung
     */
    private function __construct(DBConnection $conn) {
        $this->ts = time();
        $this->conn = $conn;
    }

    /**
     * Erstellt ein neues Objekt mit den übergebenen ENM-Daten als JSON-String und stellt Methoden für
     * den Zugriff auf diese Daten zur Verfügung.
     *
     * @param DBConnection $conn     die Datenbank-Verbindung
     * @param string $jsonEnmDaten   die ENM-Daten
     *
     * @return ImportManager   der initialisierte Manager
     */
    public static function createFromJson(DBConnection $conn, string $jsonEnmDaten): ImportManager {
        if ($jsonEnmDaten === null) {
            Http::exit500("Fehler bei dem Dekodieren der JSON-Daten: Der JSON-String ist null.");
        }
        $manager = new ImportManager($conn);
        $enmDaten = null;
        try {
            $enmDaten = json_decode($jsonEnmDaten, false, 512, JSON_THROW_ON_ERROR);
        } catch (JsonException $e) {
            Http::exit400BadRequest("Fehler bei dem Dekodieren der JSON-Daten. Prüfen ie ggf. die php-Konfiguration. Ein zu niedriger Wert bei der Einstellung memory_limit kann evtl. dazu führen. Fehler ".$e->getCode().": ".$e->getMessage()."\n".$e->getTraceAsString());
        }
        // Prüfe zunächst die ENM-Revision
        if ($enmDaten->enmRevision != $manager->enmRevisionRequired) {
            Http::exit400BadRequest("Die Revision der ENM-Daten ist nicht $manager->enmRevisionRequired.");
        }
        // Prüfe, ob die Schulform gesetzt ist
        if ($enmDaten->schulform == null) {
            Http::exit400BadRequest("Es muss eine Schulform angegeben sein.");
        }
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
     * Führt den Import der ENM-Daten in die Datenbank durch
     *
     * @param Database $db   das Objekt für den Datenbankzugriff
     */
    public function doImport(): void {
        // Prüfe anhand der Schulnummer, ob bereits importierte Daten vorliegen
        $schulnummer = $this->enmDaten->schulnummer;
        $dbEnmDaten = $this->conn->queryAllOrNull("SELECT * FROM Daten WHERE schulnummer = $schulnummer", true);
        $updateMode = ($dbEnmDaten != null) && (count($dbEnmDaten) != 0);
        // Wenn nicht aktualisiert wird, dann leere zunächst alle Tabellen mit evtl. zuvor importierten ENM-Daten
        if (!$updateMode) {
            ImportManager::clearENMDaten($this->conn);
        }
        // Schreibe die allgemeinen ENM-Daten
        $this->writeENMDaten();
        // Schreibe die ENM-Daten für die Lehrer-Zugänge
        $this->writeENMLehrer();
        // Schreibe die ENM-Daten für die Schüler
        $this->writeENMSchueler();
        // Bei einem Update werden ggf. aus den vorhanden Daten aktuellere Informationen in die neuen übertragen
        if ($updateMode) {
            // Übertrage ggf. aktuellere Informationen aus den zuvor vorhandenen Daten in die neu importierten Daten
            $this->importDiffSchueler($this->ts);
            $this->importDiffLeistungen($this->ts);
            $this->importDiffTeilleistungen($this->ts);
            $this->importDiffAnkreuzkompetenzen($this->ts);
            $this->importDiffLehrer($this->ts);
        }
        // Räume auf und entferne alle restlichen Daten, die nicht den neuen Zeitstempel haben
        $this->retainENMDaten($this->ts);
    }


    /**
     * Schreibe das ENM-Datenobjekt in die Datenbank.
     */
    protected function writeENMDaten(): void {
        $jsonEnmDaten = json_encode($this->enmDaten, JSON_UNESCAPED_SLASHES);
        $this->conn->beginTransaction();
        $stmt = $this->conn->prepareStatement("INSERT INTO Daten(ts, schulnummer, daten) VALUES (:ts, :schulnummer, :daten)");
        $this->conn->bindStatementValue($stmt, ":ts", $this->ts, PDO::PARAM_INT);
        $this->conn->bindStatementValue($stmt, ":schulnummer", $this->enmDaten->schulnummer, PDO::PARAM_INT);
        $this->conn->bindStatementValue($stmt, ":daten", $jsonEnmDaten, PDO::PARAM_STR);
        $this->conn->executeStatement($stmt);
        $this->conn->commitTransaction();
    }

    /**
     * Schreibe die ENM-Lehrer-Objekte in die Datenbank.
     * Ist dies nicht erfolgreich, so wird ein Fehlercode 500 ausgeführt.
     */
    protected function writeENMLehrer(): void {
        $this->conn->beginTransaction();
        $stmt = $this->conn->prepareStatement("INSERT INTO Lehrer(id, ts, daten, eMailDienstlich, passwordHash, tsPasswordHash) VALUES (:id, :ts, :daten, :email, :pw, :tspw)");
        foreach ($this->enmLehrer as $lehrer) {
            $jsonLehrer = json_encode($lehrer, JSON_UNESCAPED_SLASHES);
            $this->conn->bindStatementValue($stmt, ":id", $lehrer->id, PDO::PARAM_INT);
            $this->conn->bindStatementValue($stmt, ":ts", $this->ts, PDO::PARAM_INT);
            $this->conn->bindStatementValue($stmt, ":daten", $jsonLehrer, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmt, ":email", $lehrer->eMailDienstlich, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmt, ":pw", $lehrer->passwordHash, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmt, ":tspw", $lehrer->tsPasswordHash, PDO::PARAM_STR);
            $this->conn->executeStatement($stmt);
        }
        $this->conn->commitTransaction();
    }

    /**
     * Schreibe die ENM-Schüler-Objekte in die Datenbank.
     * Ist dies nicht erfolgreich, so wird ein Fehlercode 500 ausgeführt.
     */
    protected function writeENMSchueler(): void {
        $this->conn->beginTransaction();
        $stmtSchueler = $this->conn->prepareStatement("INSERT INTO Schueler(id, ts, idJahrgang, idKlasse, daten, tsFehlstundenGesamt, tsFehlstundenGesamtUnentschuldigt, tsASV, tsAUE, tsZB, tsLELS, tsSchulformEmpf, tsIndividuelleVersetzungsbemerkungen, tsFoerderbemerkungen) VALUES (:id, :ts, :idJahrgang, :idKlasse, :daten, :tsFehlstundenGesamt, :tsFehlstundenGesamtUnentschuldigt, :tsASV, :tsAUE, :tsZB, :tsLELS, :tsSchulformEmpf, :tsIndividuelleVersetzungsbemerkungen, :tsFoerderbemerkungen)");
        $stmtLeistung = $this->conn->prepareStatement("INSERT INTO Leistungsdaten(id, ts, idSchueler, idLerngruppe, daten, tsNote, tsNoteQuartal, tsFehlstundenFach, tsFehlstundenUnentschuldigtFach, tsFachbezogeneBemerkungen, tsIstGemahnt) VALUES (:id, :ts, :idSchueler, :idLerngruppe, :daten, :tsNote, :tsNoteQuartal, :tsFehlstundenFach, :tsFehlstundenUnentschuldigtFach, :tsFachbezogeneBemerkungen, :tsIstGemahnt)");
        $stmtTeilleistung = $this->conn->prepareStatement("INSERT INTO Teilleistungen(id, ts, idLeistung, daten, tsArtID, tsDatum, tsBemerkung, tsNote) VALUES (:id, :ts, :idLeistung, :daten, :tsArtID, :tsDatum, :tsBemerkung, :tsNote)");
        $stmtAnkreuzkomp = $this->conn->prepareStatement("INSERT INTO Ankreuzkompetenzen(id, ts, idSchueler, idKompetenz, daten, tsStufe) VALUES (:id, :ts, :idSchueler, :idKompetenz, :daten, :tsStufe)");
        $stmtSprachenfolge = $this->conn->prepareStatement("INSERT INTO Sprachenfolge(id, sprache, ts, idSchueler, daten) VALUES (:id, :sprache, :ts, :idSchueler, :daten)");
        foreach ($this->enmSchueler as $schueler) {
            // Erstelle den Schülereintrag mit einem JSON ohne Detaildaten zu der Sprachenfolge, den Leistungsdaten und den Ankreuzkompetenzen ...
            $tmpJsonSchueler = json_encode($schueler, JSON_UNESCAPED_SLASHES);
            $tmpSchueler = json_decode($tmpJsonSchueler);
            $tmpSchueler->leistungsdaten = [];
            $tmpSchueler->ankreuzkompetenzen = [];
            $tmpSchueler->sprachenfolge = [];
            $jsonSchueler = json_encode($tmpSchueler, JSON_UNESCAPED_SLASHES);
            $this->conn->bindStatementValue($stmtSchueler, ":id", $schueler->id, PDO::PARAM_INT);
            $this->conn->bindStatementValue($stmtSchueler, ":ts", $this->ts, PDO::PARAM_INT);
            $this->conn->bindStatementValue($stmtSchueler, ":idJahrgang", $schueler->jahrgangID, PDO::PARAM_INT);
            $this->conn->bindStatementValue($stmtSchueler, ":idKlasse", $schueler->klasseID, PDO::PARAM_INT);
            $this->conn->bindStatementValue($stmtSchueler, ":daten", $jsonSchueler, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmtSchueler, ":tsFehlstundenGesamt", $schueler->lernabschnitt->tsFehlstundenGesamt, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmtSchueler, ":tsFehlstundenGesamtUnentschuldigt", $schueler->lernabschnitt->tsFehlstundenGesamtUnentschuldigt, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmtSchueler, ":tsASV", $schueler->bemerkungen->tsASV, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmtSchueler, ":tsAUE", $schueler->bemerkungen->tsAUE, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmtSchueler, ":tsZB", $schueler->bemerkungen->tsZB, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmtSchueler, ":tsLELS", $schueler->bemerkungen->tsLELS, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmtSchueler, ":tsSchulformEmpf", $schueler->bemerkungen->tsSchulformEmpf, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmtSchueler, ":tsIndividuelleVersetzungsbemerkungen", $schueler->bemerkungen->tsIndividuelleVersetzungsbemerkungen, PDO::PARAM_STR);
            $this->conn->bindStatementValue($stmtSchueler, ":tsFoerderbemerkungen", $schueler->bemerkungen->tsFoerderbemerkungen, PDO::PARAM_STR);
            $this->conn->executeStatement($stmtSchueler);
            // ... dann die Leistungsdaten
            foreach ($schueler->leistungsdaten as $leistung) {
                $tmpJsonLeistung = json_encode($leistung, JSON_UNESCAPED_SLASHES);
                $tmpLeistung = json_decode($tmpJsonLeistung);
                $tmpLeistung->teilleistungen = [];
                $jsonLeistung = json_encode($tmpLeistung, JSON_UNESCAPED_SLASHES);
                $this->conn->bindStatementValue($stmtLeistung, ":id", $leistung->id, PDO::PARAM_INT);
                $this->conn->bindStatementValue($stmtLeistung, ":ts", $this->ts, PDO::PARAM_INT);
                $this->conn->bindStatementValue($stmtLeistung, ":idSchueler", $schueler->id, PDO::PARAM_INT);
                $this->conn->bindStatementValue($stmtLeistung, ":idLerngruppe", $leistung->lerngruppenID, PDO::PARAM_INT);
                $this->conn->bindStatementValue($stmtLeistung, ":daten", $jsonLeistung, PDO::PARAM_STR);
                $this->conn->bindStatementValue($stmtLeistung, ":tsNote", $leistung->tsNote, PDO::PARAM_STR);
                $this->conn->bindStatementValue($stmtLeistung, ":tsNoteQuartal", $leistung->tsNoteQuartal, PDO::PARAM_STR);
                $this->conn->bindStatementValue($stmtLeistung, ":tsFehlstundenFach", $leistung->tsFehlstundenFach, PDO::PARAM_STR);
                $this->conn->bindStatementValue($stmtLeistung, ":tsFehlstundenUnentschuldigtFach", $leistung->tsFehlstundenUnentschuldigtFach, PDO::PARAM_STR);
                $this->conn->bindStatementValue($stmtLeistung, ":tsFachbezogeneBemerkungen", $leistung->tsFachbezogeneBemerkungen, PDO::PARAM_STR);
                $this->conn->bindStatementValue($stmtLeistung, ":tsIstGemahnt", $leistung->tsIstGemahnt, PDO::PARAM_STR);
                $this->conn->executeStatement($stmtLeistung);
                // ... mit den Teilleistungen
                foreach ($leistung->teilleistungen as $teilleistung) {
                    $jsonTeilleistung = json_encode($teilleistung, JSON_UNESCAPED_SLASHES);
                    $this->conn->bindStatementValue($stmtTeilleistung, ":id", $teilleistung->id, PDO::PARAM_INT);
                    $this->conn->bindStatementValue($stmtTeilleistung, ":ts", $this->ts, PDO::PARAM_INT);
                    $this->conn->bindStatementValue($stmtTeilleistung, ":idLeistung", $leistung->id, PDO::PARAM_INT);
                    $this->conn->bindStatementValue($stmtTeilleistung, ":daten", $jsonTeilleistung, PDO::PARAM_STR);
                    $this->conn->bindStatementValue($stmtTeilleistung, ":tsArtID", $teilleistung->tsArtID, PDO::PARAM_STR);
                    $this->conn->bindStatementValue($stmtTeilleistung, ":tsDatum", $teilleistung->tsDatum, PDO::PARAM_STR);
                    $this->conn->bindStatementValue($stmtTeilleistung, ":tsBemerkung", $teilleistung->tsBemerkung, PDO::PARAM_STR);
                    $this->conn->bindStatementValue($stmtTeilleistung, ":tsNote", $teilleistung->tsNote, PDO::PARAM_STR);
                    $this->conn->executeStatement($stmtTeilleistung);
                }
            }
            // ... dann die Ankreuzkompetenzen
            foreach ($schueler->ankreuzkompetenzen as $komp) {
                $jsonKompetenz = json_encode($komp, JSON_UNESCAPED_SLASHES);
                $this->conn->bindStatementValue($stmtAnkreuzkomp, ":id", $komp->id, PDO::PARAM_INT);
                $this->conn->bindStatementValue($stmtAnkreuzkomp, ":ts", $this->ts, PDO::PARAM_INT);
                $this->conn->bindStatementValue($stmtAnkreuzkomp, ":idSchueler", $schueler->id, PDO::PARAM_INT);
                $this->conn->bindStatementValue($stmtAnkreuzkomp, ":idKompetenz", $komp->kompetenzID, PDO::PARAM_INT);
                $this->conn->bindStatementValue($stmtAnkreuzkomp, ":daten", $jsonKompetenz, PDO::PARAM_STR);
                $this->conn->bindStatementValue($stmtAnkreuzkomp, ":tsStufe", $komp->tsStufe, PDO::PARAM_STR);
                $this->conn->executeStatement($stmtAnkreuzkomp);
            }
            // ... und die Sprachenfolge
            foreach ($schueler->sprachenfolge as $sprachenfolge) {
                $jsonSprachenfolge = json_encode($sprachenfolge, JSON_UNESCAPED_SLASHES);
                $this->conn->bindStatementValue($stmtSprachenfolge, ":id", $sprachenfolge->id, PDO::PARAM_INT);
                $this->conn->bindStatementValue($stmtSprachenfolge, ":sprache", $sprachenfolge->sprache, PDO::PARAM_STR);
                $this->conn->bindStatementValue($stmtSprachenfolge, ":ts", $this->ts, PDO::PARAM_INT);
                $this->conn->bindStatementValue($stmtSprachenfolge, ":idSchueler", $schueler->id, PDO::PARAM_INT);
                $this->conn->bindStatementValue($stmtSprachenfolge, ":daten", $jsonSprachenfolge, PDO::PARAM_STR);
                $this->conn->executeStatement($stmtSprachenfolge);
            }
        }
        $this->conn->commitTransaction();
    }


    private function importUpdateSchuelerGeneratePreparedStatement(mixed $alt, mixed $neu): PDOStatement {
        $sql = "UPDATE Schueler SET ";
        if ($alt->tsFehlstundenGesamt > $neu->tsFehlstundenGesamt) {
            $sql .= "tsFehlstundenGesamt=:tsFehlstundenGesamt,";
        }
        if ($alt->tsFehlstundenGesamtUnentschuldigt > $neu->tsFehlstundenGesamtUnentschuldigt) {
            $sql .= "tsFehlstundenGesamtUnentschuldigt=:tsFehlstundenGesamtUnentschuldigt,";
        }
        if ($alt->tsASV > $neu->tsASV) {
            $sql .= "tsASV=:tsASV,";
        }
        if ($alt->tsAUE > $neu->tsAUE) {
            $sql .= "tsAUE=:tsAUE,";
        }
        if ($alt->tsZB > $neu->tsZB) {
            $sql .= "tsZB=:tsZB,";
        }
        if ($alt->tsLELS > $neu->tsLELS) {
            $sql .= "tsLELS=:tsLELS,";
        }
        if ($alt->tsSchulformEmpf > $neu->tsSchulformEmpf) {
            $sql .= "tsSchulformEmpf=:tsSchulformEmpf,";
        }
        if ($alt->tsIndividuelleVersetzungsbemerkungen > $neu->tsIndividuelleVersetzungsbemerkungen) {
            $sql .= "tsIndividuelleVersetzungsbemerkungen=:tsIndividuelleVersetzungsbemerkungen,";
        }
        if ($alt->tsFoerderbemerkungen > $neu->tsFoerderbemerkungen) {
            $sql .= "tsFoerderbemerkungen=:tsFoerderbemerkungen,";
        }
        $sql .= "daten=:daten WHERE id=:id and ts=:ts";
        return $this->conn->prepareStatement($sql);
    }


    private function importUpdateSchuelerBindStatementVariables(PDOStatement $stmt, mixed $alt, mixed $neu): void {
        $jsonAlt = json_decode($alt->daten);
        $jsonNeu = json_decode($neu->daten);
        if ($alt->tsFehlstundenGesamt > $neu->tsFehlstundenGesamt) {
            $this->conn->bindStatementValue($stmt, ":tsFehlstundenGesamt", $alt->tsFehlstundenGesamt, PDO::PARAM_STR);
            $jsonNeu->lernabschnitt->fehlstundenGesamt = $jsonAlt->lernabschnitt->fehlstundenGesamt;
            $jsonNeu->lernabschnitt->tsFehlstundenGesamt = $jsonAlt->lernabschnitt->tsFehlstundenGesamt;
        }
        if ($alt->tsFehlstundenGesamtUnentschuldigt > $neu->tsFehlstundenGesamtUnentschuldigt) {
            $this->conn->bindStatementValue($stmt, ":tsFehlstundenGesamtUnentschuldigt", $alt->tsFehlstundenGesamtUnentschuldigt, PDO::PARAM_STR);
            $jsonNeu->lernabschnitt->fehlstundenGesamtUnentschuldigt = $jsonAlt->lernabschnitt->fehlstundenGesamtUnentschuldigt;
            $jsonNeu->lernabschnitt->tsFehlstundenGesamtUnentschuldigt = $jsonAlt->lernabschnitt->tsFehlstundenGesamtUnentschuldigt;
        }
        if ($alt->tsASV > $neu->tsASV) {
            $this->conn->bindStatementValue($stmt, ":tsASV", $alt->tsASV, PDO::PARAM_STR);
            $jsonNeu->bemerkungen->ASV = $jsonAlt->bemerkungen->ASV;
            $jsonNeu->bemerkungen->tsASV = $jsonAlt->bemerkungen->tsASV;
        }
        if ($alt->tsAUE > $neu->tsAUE) {
            $this->conn->bindStatementValue($stmt, ":tsAUE", $alt->tsAUE, PDO::PARAM_STR);
            $jsonNeu->bemerkungen->AUE = $jsonAlt->bemerkungen->AUE;
            $jsonNeu->bemerkungen->tsAUE = $jsonAlt->bemerkungen->tsAUE;
        }
        if ($alt->tsZB > $neu->tsZB) {
            $this->conn->bindStatementValue($stmt, ":tsZB", $alt->tsZB, PDO::PARAM_STR);
            $jsonNeu->bemerkungen->ZB = $jsonAlt->bemerkungen->ZB;
            $jsonNeu->bemerkungen->tsZB = $jsonAlt->bemerkungen->tsZB;
        }
        if ($alt->tsLELS > $neu->tsLELS) {
            $this->conn->bindStatementValue($stmt, ":tsLELS", $alt->tsLELS, PDO::PARAM_STR);
            $jsonNeu->bemerkungen->LELS = $jsonAlt->bemerkungen->LELS;
            $jsonNeu->bemerkungen->tsLELS = $jsonAlt->bemerkungen->tsLELS;
        }
        if ($alt->tsSchulformEmpf > $neu->tsSchulformEmpf) {
            $this->conn->bindStatementValue($stmt, ":tsSchulformEmpf", $alt->tsSchulformEmpf, PDO::PARAM_STR);
            $jsonNeu->bemerkungen->schulformEmpf = $jsonAlt->bemerkungen->schulformEmpf;
            $jsonNeu->bemerkungen->tsSchulformEmpf = $jsonAlt->bemerkungen->tsSchulformEmpf;
        }
        if ($alt->tsIndividuelleVersetzungsbemerkungen > $neu->tsIndividuelleVersetzungsbemerkungen) {
            $this->conn->bindStatementValue($stmt, ":tsIndividuelleVersetzungsbemerkungen", $alt->tsIndividuelleVersetzungsbemerkungen, PDO::PARAM_STR);
            $jsonNeu->bemerkungen->individuelleVersetzungsbemerkungen = $jsonAlt->bemerkungen->individuelleVersetzungsbemerkungen;
            $jsonNeu->bemerkungen->tsIndividuelleVersetzungsbemerkungen = $jsonAlt->bemerkungen->tsIndividuelleVersetzungsbemerkungen;
        }
        if ($alt->tsFoerderbemerkungen > $neu->tsFoerderbemerkungen) {
            $this->conn->bindStatementValue($stmt, ":tsFoerderbemerkungen", $alt->tsFoerderbemerkungen, PDO::PARAM_STR);
            $jsonNeu->bemerkungen->foerderbemerkungen = $jsonAlt->bemerkungen->foerderbemerkungen;
            $jsonNeu->bemerkungen->tsFoerderbemerkungen = $jsonAlt->bemerkungen->tsFoerderbemerkungen;
        }
        $this->conn->bindStatementValue($stmt, ":daten", json_encode($jsonNeu, JSON_UNESCAPED_SLASHES), PDO::PARAM_STR);
        $this->conn->bindStatementValue($stmt, ":id", $neu->id, PDO::PARAM_INT);
        $this->conn->bindStatementValue($stmt, ":ts", $neu->ts, PDO::PARAM_INT);
    }

    /**
     * Erstellt einen Abgleich von vorherigen Einträgen zu den Einträgen mit dem angegebenen Zeitstempel.
     *
     * @param int $ts   der Zeitstempel der neu importierten Daten
     */
    protected function importDiffSchueler(int $ts): void {
        // Entferne zunächst alle alten Schüler-Einträge, die nicht in den neuen Daten enthalten sind oder keine Änderungen haben
        $this->conn->dropFrom('Schueler', "ts < $ts AND (id, ts) NOT IN (SELECT a.id, a.ts FROM Schueler a JOIN Schueler b WHERE a.id = b.id AND a.ts < b.ts AND (a.tsFehlstundenGesamt <> b.tsFehlstundenGesamt OR a.tsFehlstundenGesamtUnentschuldigt <> b.tsFehlstundenGesamtUnentschuldigt OR a.tsASV <> b.tsASV OR a.tsAUE <> b.tsAUE OR a.tsZB <> b.tsZB OR a.tsLELS <> b.tsLELS OR a.tsSchulformEmpf <> b.tsSchulformEmpf OR a.tsIndividuelleVersetzungsbemerkungen <> b.tsIndividuelleVersetzungsbemerkungen OR a.tsFoerderbemerkungen <> b.tsFoerderbemerkungen))");
        // Lese dann alle Daten mit dem alten Zeitstempel ein, da diese ggf. Änderungen beinhalten
        $diffsOld = $this->conn->queryAllOrNull("SELECT id, ts, idJahrgang, idKlasse, daten, tsFehlstundenGesamt, tsFehlstundenGesamtUnentschuldigt, tsASV, tsAUE, tsZB, tsLELS, tsSchulformEmpf, tsIndividuelleVersetzungsbemerkungen, tsFoerderbemerkungen FROM Schueler WHERE ts < $ts");
        if ($diffsOld === null) {
            return;
        }
        // Erstelle aus den alten Daten eine Map basierend auf der id und eine Liste der ids
        $mapOld = [];
        $idsArray = [];
        foreach ($diffsOld as $row) {
            $mapOld[$row->id] = $row;
            $idsArray[] = $row->id;
        }
        if (empty($idsArray)) {
            return;
        }
        $ids = implode(",", $idsArray);
        // Lese dann alle dazugehörigen Daten mit neuem Zeitstempel ein
        $this->conn->beginTransaction();
        $diffsNeu = $this->conn->queryAllOrExit500("SELECT * FROM Schueler WHERE ts = $ts AND id IN ($ids)", "Fehler Lesen der neuen Schülerdaten");
        foreach ($diffsNeu as $neu) {
            $alt = $mapOld[$neu->id];
            $needUpdate = (($alt->tsFehlstundenGesamt > $neu->tsFehlstundenGesamt)
                || ($alt->tsFehlstundenGesamtUnentschuldigt > $neu->tsFehlstundenGesamtUnentschuldigt)
                || ($alt->tsASV > $neu->tsASV) || ($alt->tsAUE > $neu->tsAUE) || ($alt->tsZB > $neu->tsZB)
                || ($alt->tsLELS > $neu->tsLELS) || ($alt->tsSchulformEmpf > $neu->tsSchulformEmpf)
                || ($alt->tsIndividuelleVersetzungsbemerkungen > $neu->tsIndividuelleVersetzungsbemerkungen)
                || ($alt->tsFoerderbemerkungen > $neu->tsFoerderbemerkungen));
            if ($needUpdate > 0) {
                $stmt = $this->importUpdateSchuelerGeneratePreparedStatement($alt, $neu);
                $this->importUpdateSchuelerBindStatementVariables($stmt, $alt, $neu);
                $this->conn->executeStatement($stmt);
            }
        }
        $this->conn->commitTransaction();
    }


    private function importUpdateLeistungenGeneratePreparedStatement(mixed $alt, mixed $neu) {
        $sql = "UPDATE Leistungsdaten SET ";
        if ($alt->tsNote > $neu->tsNote) {
            $sql .= "tsNote=:tsNote,";
        }
        if ($alt->tsNoteQuartal > $neu->tsNoteQuartal) {
            $sql .= "tsNoteQuartal=:tsNoteQuartal,";
        }
        if ($alt->tsFehlstundenFach > $neu->tsFehlstundenFach) {
            $sql .= "tsFehlstundenFach=:tsFehlstundenFach,";
        }
        if ($alt->tsFehlstundenUnentschuldigtFach > $neu->tsFehlstundenUnentschuldigtFach) {
            $sql .= "tsFehlstundenUnentschuldigtFach=:tsFehlstundenUnentschuldigtFach,";
        }
        if ($alt->tsFachbezogeneBemerkungen > $neu->tsFachbezogeneBemerkungen) {
            $sql .= "tsFachbezogeneBemerkungen=:tsFachbezogeneBemerkungen,";
        }
        if ($alt->tsIstGemahnt > $neu->tsIstGemahnt) {
            $sql .= "tsIstGemahnt=:tsIstGemahnt,";
        }
        $sql .= "daten=:daten WHERE id=:id and ts=:ts";
        return $this->conn->prepareStatement($sql);
    }


    private function importUpdateLeistungenBindStatementVariables(PDOStatement $stmt, mixed $alt, mixed $neu): void {
        $jsonAlt = json_decode($alt->daten);
        $jsonNeu = json_decode($neu->daten);
        if ($alt->tsNote > $neu->tsNote) {
            $this->conn->bindStatementValue($stmt, ":tsNote", $alt->tsNote, PDO::PARAM_STR);
            $jsonNeu->note = $jsonAlt->note;
            $jsonNeu->tsNote = $jsonAlt->tsNote;
        }
        if ($alt->tsNoteQuartal > $neu->tsNoteQuartal) {
            $this->conn->bindStatementValue($stmt, ":tsNoteQuartal", $alt->tsNoteQuartal, PDO::PARAM_STR);
            $jsonNeu->noteQuartal = $jsonAlt->noteQuartal;
            $jsonNeu->tsNoteQuartal = $jsonAlt->tsNoteQuartal;
        }
        if ($alt->tsFehlstundenFach > $neu->tsFehlstundenFach) {
            $this->conn->bindStatementValue($stmt, ":tsFehlstundenFach", $alt->tsFehlstundenFach, PDO::PARAM_STR);
            $jsonNeu->fehlstundenFach = $jsonAlt->fehlstundenFach;
            $jsonNeu->tsFehlstundenFach = $jsonAlt->tsFehlstundenFach;
        }
        if ($alt->tsFehlstundenUnentschuldigtFach > $neu->tsFehlstundenUnentschuldigtFach) {
            $this->conn->bindStatementValue($stmt, ":tsFehlstundenUnentschuldigtFach", $alt->tsFehlstundenUnentschuldigtFach, PDO::PARAM_STR);
            $jsonNeu->fehlstundenUnentschuldigtFach = $jsonAlt->fehlstundenUnentschuldigtFach;
            $jsonNeu->tsFehlstundenUnentschuldigtFach = $jsonAlt->tsFehlstundenUnentschuldigtFach;
        }
        if ($alt->tsFachbezogeneBemerkungen > $neu->tsFachbezogeneBemerkungen) {
            $this->conn->bindStatementValue($stmt, ":tsFachbezogeneBemerkungen", $alt->tsFachbezogeneBemerkungen, PDO::PARAM_STR);
            $jsonNeu->fachbezogeneBemerkungen = $jsonAlt->fachbezogeneBemerkungen;
            $jsonNeu->tsFachbezogeneBemerkungen = $jsonAlt->tsFachbezogeneBemerkungen;
        }
        if ($alt->tsIstGemahnt > $neu->tsIstGemahnt) {
            $this->conn->bindStatementValue($stmt, ":tsIstGemahnt", $alt->tsIstGemahnt, PDO::PARAM_STR);
            $jsonNeu->istGemahnt = $jsonAlt->istGemahnt;
            $jsonNeu->tsIstGemahnt = $jsonAlt->tsIstGemahnt;
        }
        $this->conn->bindStatementValue($stmt, ":daten", json_encode($jsonNeu, JSON_UNESCAPED_SLASHES), PDO::PARAM_STR);
        $this->conn->bindStatementValue($stmt, ":id", $neu->id, PDO::PARAM_INT);
        $this->conn->bindStatementValue($stmt, ":ts", $neu->ts, PDO::PARAM_INT);
    }


    /**
     * Erstellt einen Abgleich von vorherigen Einträgen zu den Einträgen mit dem angegebenen Zeitstempel.
     *
     * @param int $ts   der Zeitstempel der neu importierten Daten
     */
    protected function importDiffLeistungen(int $ts): void {
        // Entferne zunächst alle alten Leistungsdaten-Einträge, die nicht in den neuen Daten enthalten sind oder keine Änderungen haben
        $this->conn->dropFrom('Leistungsdaten', "ts < $ts AND (id, ts) NOT IN (SELECT a.id, a.ts FROM Leistungsdaten a JOIN Leistungsdaten b WHERE a.id = b.id AND a.ts < b.ts AND (a.tsNote <> b.tsNote OR a.tsNoteQuartal <> b.tsNoteQuartal OR a.tsFehlstundenFach <> b.tsFehlstundenFach OR a.tsFehlstundenUnentschuldigtFach <> b.tsFehlstundenUnentschuldigtFach OR a.tsFachbezogeneBemerkungen <> b.tsFachbezogeneBemerkungen OR a.tsIstGemahnt <> b.tsIstGemahnt))");
        // Lese dann alle Daten mit dem alten Zeitstempel ein, da diese ggf. Änderungen beinhalten
        $diffsOld = $this->conn->queryAllOrNull("SELECT id, ts, idSchueler, idLerngruppe, daten, tsNote, tsNoteQuartal, tsFehlstundenFach, tsFehlstundenUnentschuldigtFach, tsFachbezogeneBemerkungen, tsIstGemahnt FROM Leistungsdaten WHERE ts < $ts");
        if ($diffsOld === null) {
            return;
        }
        // Erstelle aus den alten Daten eine Map basierend auf der id und eine Liste der ids
        $mapOld = [];
        $idsArray = [];
        foreach ($diffsOld as $row) {
            $mapOld[$row->id] = $row;
            $idsArray[] = $row->id;
        }
        if (empty($idsArray)) {
            return;
        }
        $ids = implode(",", $idsArray);
        // Lese dann alle dazugehörigen Daten mit neuem Zeitstempel ein
        $this->conn->beginTransaction();
        $diffsNeu = $this->conn->queryAllOrExit500("SELECT * FROM Leistungsdaten WHERE ts = $ts AND id IN ($ids)", "Fehler Lesen der neuen Leistungsdaten");
        foreach ($diffsNeu as $neu) {
            $alt = $mapOld[$neu->id];
            $needUpdate = (($alt->tsNote > $neu->tsNote) || ($alt->tsNoteQuartal > $neu->tsNoteQuartal)
                || ($alt->tsFehlstundenFach > $neu->tsFehlstundenFach)
                || ($alt->tsFehlstundenUnentschuldigtFach > $neu->tsFehlstundenUnentschuldigtFach)
                || ($alt->tsFachbezogeneBemerkungen > $neu->tsFachbezogeneBemerkungen)
                || ($alt->tsIstGemahnt > $neu->tsIstGemahnt));
            if ($needUpdate > 0) {
                $stmt = $this->importUpdateLeistungenGeneratePreparedStatement($alt, $neu);
                $this->importUpdateLeistungenBindStatementVariables($stmt, $alt, $neu);
                $this->conn->executeStatement($stmt);
            }
        }
        $this->conn->commitTransaction();
    }

    private function importUpdateTeilleistungenGeneratePreparedStatement(mixed $alt, mixed $neu): PDOStatement {
        $sql = "UPDATE Teilleistungen SET ";
        if ($alt->tsArtID > $neu->tsArtID) {
            $sql .= "tsArtID=:tsArtID,";
        }
        if ($alt->tsDatum > $neu->tsDatum) {
            $sql .= "tsDatum=:tsDatum,";
        }
        if ($alt->tsBemerkung > $neu->tsBemerkung) {
            $sql .= "tsBemerkung=:tsBemerkung,";
        }
        if ($alt->tsNote > $neu->tsNote) {
            $sql .= "tsNote=:tsNote,";
        }
        $sql .= "daten=:daten WHERE id=:id and ts=:ts";
        return $this->conn->prepareStatement($sql);
    }


    private function importUpdateTeilleistungenBindStatementVariables(PDOStatement $stmt, mixed $alt, mixed $neu): void {
        $jsonAlt = json_decode($alt->daten);
        $jsonNeu = json_decode($neu->daten);
        if ($alt->tsArtID > $neu->tsArtID) {
            $this->conn->bindStatementValue($stmt, ":tsArtID", $alt->tsArtID, PDO::PARAM_STR);
            $jsonNeu->artID = $jsonAlt->artID;
            $jsonNeu->tsArtID = $jsonAlt->tsArtID;
        }
        if ($alt->tsDatum > $neu->tsDatum) {
            $this->conn->bindStatementValue($stmt, ":tsDatum", $alt->tsDatum, PDO::PARAM_STR);
            $jsonNeu->datum = $jsonAlt->datum;
            $jsonNeu->tsDatum = $jsonAlt->tsDatum;
        }
        if ($alt->tsBemerkung > $neu->tsBemerkung) {
            $this->conn->bindStatementValue($stmt, ":tsBemerkung", $alt->tsBemerkung, PDO::PARAM_STR);
            $jsonNeu->bemerkung = $jsonAlt->bemerkung;
            $jsonNeu->tsBemerkung = $jsonAlt->tsBemerkung;
        }
        if ($alt->tsNote > $neu->tsNote) {
            $this->conn->bindStatementValue($stmt, ":tsNote", $alt->tsNote, PDO::PARAM_STR);
            $jsonNeu->note = $jsonAlt->note;
            $jsonNeu->tsNote = $jsonAlt->tsNote;
        }
        $this->conn->bindStatementValue($stmt, ":daten", json_encode($jsonNeu, JSON_UNESCAPED_SLASHES), PDO::PARAM_STR);
        $this->conn->bindStatementValue($stmt, ":id", $neu->id, PDO::PARAM_INT);
        $this->conn->bindStatementValue($stmt, ":ts", $neu->ts, PDO::PARAM_INT);
    }


    /**
     * Erstellt einen Abgleich von vorherigen Einträgen zu den Einträgen mit dem angegebenen Zeitstempel.
     *
     * @param int $ts   der Zeitstempel der neu importierten Daten
     */
    protected function importDiffTeilleistungen(int $ts): void {
        // Entferne zunächst alle alten Teilleistungen, die nicht in den neuen Daten enthalten sind oder keine Änderungen haben
        $this->conn->dropFrom('Teilleistungen', "ts < $ts AND (id, ts) NOT IN (SELECT a.id, a.ts FROM Teilleistungen a JOIN Teilleistungen b WHERE a.id = b.id AND a.ts < b.ts AND (a.tsArtID <> b.tsArtID OR a.tsDatum <> b.tsDatum OR a.tsBemerkung <> b.tsBemerkung OR a.tsNote <> b.tsNote))");
        // Lese dann alle Daten mit dem alten Zeitstempel ein, da diese ggf. Änderungen beinhalten
        $diffsOld = $this->conn->queryAllOrNull("SELECT id, ts, idLeistung, daten, tsArtID, tsDatum, tsBemerkung, tsNote FROM Teilleistungen WHERE ts < $ts");
        if ($diffsOld === null) {
            return;
        }
        // Erstelle aus den alten Daten eine Map basierend auf der id und eine Liste der ids
        $mapOld = [];
        $idsArray = [];
        foreach ($diffsOld as $row) {
            $mapOld[$row->id] = $row;
            $idsArray[] = $row->id;
        }
        if (empty($idsArray)) {
            return;
        }
        $ids = implode(",", $idsArray);
        // Lese dann alle dazugehörigen Daten mit neuem Zeitstempel ein
        $this->conn->beginTransaction();
        $diffsNeu = $this->conn->queryAllOrExit500("SELECT * FROM Teilleistungen WHERE ts = $ts AND id IN ($ids)", "Fehler Lesen der neuen Teilleistungen");
        foreach ($diffsNeu as $neu) {
            $alt = $mapOld[$neu->id];
            $needUpdate = ($alt->tsArtID > $neu->tsArtID) || ($alt->tsDatum > $neu->tsDatum) || ($alt->tsBemerkung > $neu->tsBemerkung) || ($alt->tsNote > $neu->tsNote);
            if ($needUpdate > 0) {
                $stmt = $this->importUpdateTeilleistungenGeneratePreparedStatement($alt, $neu);
                $this->importUpdateTeilleistungenBindStatementVariables($stmt, $alt, $neu);
                $this->conn->executeStatement($stmt);
            }
        }
        $this->conn->commitTransaction();
    }

    /**
     * Erstellt einen Abgleich von vorherigen Einträgen zu den Einträgen mit dem angegebenen Zeitstempel.
     *
     * @param int $ts   der Zeitstempel der neu importierten Daten
     */
    protected function importDiffAnkreuzkompetenzen(int $ts): void {
        // Entferne zunächst alle alten Ankreuzkompetenzen-Einträge, die nicht in den neuen Daten enthalten sind oder keine Änderungen haben
        $this->conn->dropFrom('Ankreuzkompetenzen', "ts < $ts AND (id, ts) NOT IN (SELECT a.id, a.ts FROM Ankreuzkompetenzen a JOIN Ankreuzkompetenzen b WHERE a.id = b.id AND a.ts < b.ts AND (a.tsStufe <> b.tsStufe))");
        // Lese dann alle Daten mit dem alten Zeitstempel ein, da diese ggf. Änderungen beinhalten
        $diffsOld = $this->conn->queryAllOrNull("SELECT id, ts, idSchueler, idKompetenz, daten, tsStufe FROM Ankreuzkompetenzen WHERE ts < $ts");
        if ($diffsOld === null) {
            return;
        }
        // Erstelle aus den alten Daten eine Map basierend auf der id und eine Liste der ids
        $mapOld = [];
        $idsArray = [];
        foreach ($diffsOld as $row) {
            $mapOld[$row->id] = $row;
            $idsArray[] = $row->id;
        }
        if (empty($idsArray)) {
            return;
        }
        $ids = implode(",", $idsArray);
        // Lese dann alle dazugehörigen Daten mit neuem Zeitstempel ein
        $this->conn->beginTransaction();
        $sql = "UPDATE Ankreuzkompetenzen SET tsStufe=:tsStufe, daten=:daten WHERE id=:id AND ts=:ts";
        $stmt = $this->conn->prepareStatement($sql);
        $diffsNeu = $this->conn->queryAllOrExit500("SELECT * FROM Ankreuzkompetenzen WHERE ts = $ts AND id IN ($ids)", "Fehler Lesen der neuen Ankreuzkompetenzen beim Schüler");
        foreach ($diffsNeu as $neu) {
            $alt = $mapOld[$neu->id];
            $needUpdate = ($alt->tsStufe > $neu->tsStufe);
            if ($needUpdate > 0) {
                $jsonAlt = json_decode($alt->daten);
                $jsonNeu = json_decode($neu->daten);
                $this->conn->bindStatementValue($stmt, ":tsStufe", $alt->tsStufe, PDO::PARAM_STR);
                $jsonNeu->stufen = $jsonAlt->stufen;
                $jsonNeu->tsStufe = $jsonAlt->tsStufe;
                $this->conn->bindStatementValue($stmt, ":daten", json_encode($jsonNeu, JSON_UNESCAPED_SLASHES), PDO::PARAM_STR);
                $this->conn->bindStatementValue($stmt, ":id", $neu->id, PDO::PARAM_INT);
                $this->conn->bindStatementValue($stmt, ":ts", $neu->ts, PDO::PARAM_INT);
                $this->conn->executeStatement($stmt);
            }
        }
        $this->conn->commitTransaction();
    }

    /**
     * Erstellt einen Abgleich von vorherigen Einträgen zu den Einträgen mit dem angegebenen Zeitstempel.
     *
     * @param int $ts   der Zeitstempel der neu importierten Daten
     */
    protected function importDiffLehrer(int $ts): void {
        // Entferne zunächst alle alten Lehrer-Einträge, die nicht in den neuen Daten enthalten sind oder keine Änderungen haben
        $this->conn->dropFrom('Lehrer', "ts < $ts AND (id, ts) NOT IN (SELECT a.id, a.ts FROM Lehrer a JOIN Lehrer b WHERE a.id = b.id AND a.ts < b.ts AND (a.tsPasswordHash <> b.tsPasswordHash))");
        // Lese dann alle Daten mit dem alten Zeitstempel ein, da diese ggf. Änderungen beinhalten
        $diffsOld = $this->conn->queryAllOrNull("SELECT id, ts, daten, eMailDienstlich, passwordHash, tsPasswordHash FROM Lehrer WHERE ts < $ts");
        if ($diffsOld === null) {
            return;
        }
        // Erstelle aus den alten Daten eine Map basierend auf der id und eine Liste der ids
        $mapOld = [];
        $idsArray = [];
        foreach ($diffsOld as $row) {
            $mapOld[$row->id] = $row;
            $idsArray[] = $row->id;
        }
        if (empty($idsArray)) {
            return;
        }
        $ids = implode(",", $idsArray);
        // Lese dann alle dazugehörigen Daten mit neuem Zeitstempel ein
        $this->conn->beginTransaction();
        $sql = "UPDATE Lehrer SET passwordHash=:passwordHash,tsPasswordHash=:tsPasswordHash,daten=:daten WHERE id=:id AND ts=:ts";
        $stmt = $this->conn->prepareStatement($sql);
        $diffsNeu = $this->conn->queryAllOrExit500("SELECT * FROM Lehrer WHERE ts = $ts AND id IN ($ids)", "Fehler Lesen der neuen Lehrerdaten");
        foreach ($diffsNeu as $neu) {
            $alt = $mapOld[$neu->id];
            $needUpdate = ($alt->tsPasswordHash > $neu->tsPasswordHash);
            if ($needUpdate > 0) {
                $jsonAlt = json_decode($alt->daten);
                $jsonNeu = json_decode($neu->daten);
                $this->conn->bindStatementValue($stmt, ":passwordHash", $alt->passwordHash, PDO::PARAM_STR);
                $this->conn->bindStatementValue($stmt, ":tsPasswordHash", $alt->tsPasswordHash, PDO::PARAM_STR);
                $jsonNeu->passwordHash = $jsonAlt->passwordHash;
                $jsonNeu->tsPasswordHash = $jsonAlt->tsPasswordHash;
                $this->conn->bindStatementValue($stmt, ":daten", json_encode($jsonNeu, JSON_UNESCAPED_SLASHES), PDO::PARAM_STR);
                $this->conn->bindStatementValue($stmt, ":id", $neu->id, PDO::PARAM_INT);
                $this->conn->bindStatementValue($stmt, ":ts", $neu->ts, PDO::PARAM_INT);
                $this->conn->executeStatement($stmt);
            }
        }
        $this->conn->commitTransaction();
    }

    /**
     * Entfernt aus allen Tabellen mit bestehenden ENM-Daten, die Daten, welche nicht den
     * angegebenen Zeitstempel tragen. Daten mit dem Zeitstempel werden also dabei erhalten.
     *
     * @param int $ts   der Zeitstempel
     */
    protected function retainENMDaten(int $ts): void {
        $this->conn->dropFrom('Daten', "ts <> $ts");
        $this->conn->dropFrom('Schueler', "ts <> $ts");
        $this->conn->dropFrom('Leistungsdaten', "ts <> $ts");
        $this->conn->dropFrom('Teilleistungen', "ts <> $ts");
        $this->conn->dropFrom('Ankreuzkompetenzen', "ts <> $ts");
        $this->conn->dropFrom('Sprachenfolge', "ts <> $ts");
        $this->conn->dropFrom('Lehrer', "ts <> $ts");
    }

    /**
     * Leert alle Tabellen mit bestehenden ENM-Daten. Die Client-Credentials bleiben dabei erhalten
     *
     * $param DBConnection $conn   die Datenbank-Verbindung
     */
    public static function clearENMDaten(DBConnection $conn): void {
        $conn->clearTable('Daten');
        $conn->clearTable('Schueler');
        $conn->clearTable('Leistungsdaten');
        $conn->clearTable('Teilleistungen');
        $conn->clearTable('Ankreuzkompetenzen');
        $conn->clearTable('Sprachenfolge');
        $conn->clearTable('Lehrer');
    }

}
