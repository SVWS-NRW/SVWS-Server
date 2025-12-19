<?php

namespace wenom;

/**
 * Diese Klasse stellt Hilfsmethoden für den Umgang mit ENM-Daten zur Verfügung.
 */
class ENMDatenManager {

    /** Die Datenbank-Verbindung, welche zum Laden der ENM-Daten genutzt wurde */
    public DBConnection $conn;

    /** Die ENM-Revision mit welcher diese Klasse arbeitet */
    public int $enmRevisionRequired = 1;

    /** Die ENM-Daten ohne Lehrer und Schüler-Informationen */
    protected object $enmDaten;

    /** Die Informationen zu Lehrern, die in der Notendatei vorhanden sind. */
    protected array $enmLehrer;

    /** Die Informationen zu den Schülern, deren Noten in dieser Notendatei verwaltet werden. */
    protected array $enmSchueler;

    /** Ein Cache für die Noten. */
    protected array | null $mapNoten = null;

    /** Ein Cache für die Klassen, bei denen ein angemeldeter Lehrer ein Klassenlehrer ist. */
    protected array | null $mapKlassen = null;

    /** Ein Cache für die Lerngruppen */
    protected array | null $mapLerngruppen = null;

    /** Ein Cache für die Lerngruppen, bei denen ein angemeldeter Lehrer ein Fachlehrer ist. */
    protected array | null $mapLerngruppenFachlehrer = null;

    /** Ein Cache für die Ankreuzkompetenzen */
    protected array | null $mapAnkreuzkompetenzen = null;

    /** Ein Cache für die Schüler */
    protected object | null $mapsSchueler = null;

    /** Ein Cache für die Teilleistungen */
    protected object | null $mapsTeilleistungen = null;

    /** Ein Cache für die Leistungsdaten */
    protected object | null $mapsLeistungsdaten = null;

    /** Ein Cache für die Lernabschnittsdaten */
    protected object | null $mapsLernabschnittsdaten = null;


    /**
     * Erstellt einen neuen nicht initialisierten Manager zur Verfügung.
     *
     * @param DBConnection $conn   die Datenbank-Verbindung
     */
    private function __construct(DBConnection $conn) {
        $this->conn = $conn;
    }

    /**
     * Erstellt ein neues Objekt aus der übergebenen Datenbank.
     *
     * @param string $jsonEnmDaten   die ENM-Daten
     *
     * @return ENMDatenManager   der initialisierte Manager
     */
    public static function createFromDatabase(Database $db): ENMDatenManager {
        $manager = new ENMDatenManager($db->conn);
        $enmDaten = $db->getJsonENMDaten();
        $manager->enmDaten = json_decode($enmDaten->daten);
        $manager->enmLehrer = $db->getENMLehrerdaten();
        $manager->enmSchueler = $db->getENMSchuelerdaten();
        return $manager;
    }

    /**
     * Führt den Export der ENM-Daten aus dem Manager in ein Json-Objekt ENMDaten durch
     *
     * @return string die vollständigen, dh. zusammengesetzten ENM-Daten als php-Objekt
     */
    public function doExport(): string {
        // Nehme die ENM-Daten ohne Lehrer- und Schülerdaten ...
        $daten = $this->enmDaten;
        // ... und integriere die Lehrer-Daten
        $daten->lehrer = $this->enmLehrer;
        // ... und die Schüler-Daten
        $daten->schueler = $this->enmSchueler;
        return json_encode($daten, JSON_UNESCAPED_SLASHES);
    }

    /**
     * Bestimme die Noten zugeordnet zu den Kürzeln.
     *
     * @return array eine Map von dem Kürzel der Noten auf das zugehörige Notenobjekt
     */
    public function getMapNoten(): array {
        if ($this->mapNoten === null) {
            $this->mapNoten = [];
            foreach ($this->enmDaten->noten as $note) {
                $this->mapNoten[$note->kuerzel] = $note;
            }
        }
        return $this->mapNoten;
    }

    /**
     * Bestimme die Klassen, bei denen der Lehrer Klassenlehrer ist.
     *
     * @param object $lehrer   der Lehrer, der aktuell angemeldet ist
     *
     * @return array eine Map von der ID der Klasse auf das zugehörige Klassenobjekt
     */
    public function getMapKlassen(object $lehrer): array {
        if ($this->mapKlassen === null) {
            $this->mapKlassen = [];
            foreach ($this->enmDaten->klassen as $klasse) {
                foreach ($klasse->klassenlehrer as $klid) {
                    if ($klid === $lehrer->id) {
                        $this->mapKlassen[$klasse->id] = $klasse;
                    }
                }
            }
        }
        return $this->mapKlassen;
    }

    /**
     * Erstelle eine Map von der ID der Lerngruppen auf das zugehörige Objekt.
     *
     * @return array eine Map von der ID der Lerngruppe auf das zugehörige Lerngruppenobjekt
     */
    public function getMapLerngruppen(): array {
        if ($this->mapLerngruppen === null) {
            $this->mapLerngruppen = [];
            foreach ($this->enmDaten->lerngruppen as $lerngruppe) {
                $this->mapLerngruppen[$lerngruppe->id] = $lerngruppe;
            }
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
    public function getMapLerngruppenFachlehrer(object $lehrer): array {
        if ($this->mapLerngruppenFachlehrer === null) {
            $this->mapLerngruppenFachlehrer = [];
            foreach ($this->enmDaten->lerngruppen as $lerngruppe) {
                foreach ($lerngruppe->lehrerID as $lid) {
                    if ($lid === $lehrer->id) {
                        $this->mapLerngruppenFachlehrer[$lerngruppe->id] = $lerngruppe;
                    }
                }
            }
        }
        return $this->mapLerngruppenFachlehrer;
    }

    /**
     * Erstelle eine Map von den IDs der Ankreuzkompetenzen auf das zugehörige Objekt.
     *
     * @return array die Map
     */
    public function getMapAnkreuzkompetenzen(): array {
        if ($this->mapAnkreuzkompetenzen === null) {
            $this->mapAnkreuzkompetenzen = [];
            foreach ($this->enmDaten->ankreuzkompetenzen->kompetenzen as $kompetenz) {
                $this->mapAnkreuzkompetenzen[$kompetenz->id] = $kompetenz;
            }
        }
        return $this->mapAnkreuzkompetenzen;
    }

    /**
     * Erstelle Maps bezüglich der Schülerdaten, den Leistungsdaten, den Teilleistungen und den Ankreuzkompetenzen,
     * jeweils von deren IDs auf das jeweils zugehörige Objekt.
     */
    private function initMapsSchueler(): void {
        $this->mapsSchueler = (object)[
            'schueler' => [],
            'bemerkungen' => [],
            'lernabschnitte' => [],
            'lernabschnittSchueler' => [],
            'leistungen' => [],
            'teilleistungen' => [],
            'teilleistungLeistung' => [],
            'ankreuzkompetenzen' => [],
            'ankreuzkompetenzSchueler' => [],
        ];
        $this->mapsLeistungsdaten = (object)[
            'schueler' => [],
        ];
        $this->mapsTeilleistungen = (object)[
            'schueler' => [],
        ];
        $this->mapsLernabschnittsdaten = (object)[
            'schueler' => [],
        ];
        foreach ($this->enmSchueler as $schueler) {
            $this->mapsSchueler->schueler[$schueler->id] = $schueler;
            $this->mapsSchueler->bemerkungen[$schueler->id] = $schueler->bemerkungen;
            $this->mapsSchueler->lernabschnitte[$schueler->lernabschnitt->id] = $schueler->lernabschnitt;
            $this->mapsSchueler->lernabschnittSchueler[$schueler->lernabschnitt->id] = $schueler;
            $this->mapsLernabschnittsdaten->schueler[$schueler->lernabschnitt->id] = $schueler;
            foreach ($schueler->leistungsdaten as $leistung) {
                $this->mapsSchueler->leistungen[$leistung->id] = $leistung;
                $this->mapsLeistungsdaten->schueler[$leistung->id] = $schueler;
                foreach ($leistung->teilleistungen as $teilleistung) {
                    $this->mapsSchueler->teilleistungen[$teilleistung->id] = $teilleistung;
                    $this->mapsSchueler->teilleistungLeistung[$teilleistung->id] = $leistung;
                    $this->mapsTeilleistungen->schueler[$teilleistung->id] = $schueler;
                }
            }
            foreach ($schueler->ankreuzkompetenzen as $ankreuzkompetenz) {
                $this->mapsSchueler->ankreuzkompetenzen[$ankreuzkompetenz->id] = $ankreuzkompetenz;
                $this->mapsSchueler->ankreuzkompetenzSchueler[$ankreuzkompetenz->id] = $schueler;
            }
        }
    }

    /**
     * Hole Maps bezüglich der Schülerdaten, den Leistungsdaten, den Teilleistungen und den Ankreuzkompetenzen,
     * jeweils von deren IDs auf das jeweils zugehörige Objekt.
     *
     * @return object ein Objekt mit Maps unter den Attributen 'schueler', 'bemerkungen', 'lernabschnitte',
     *                'lernabschnittSchueler', 'leistungen', 'teilleistungen', 'teilleistungLeistung',
     *                'ankreuzkompetenzen', 'ankreuzkompetenzSchueler'
     */
    public function getMapsSchueler(): object {
        if ($this->mapsSchueler === null) {
            $this->initMapsSchueler();
        }
        return $this->mapsSchueler;
    }

    /**
     * Erstelle eine Map bezüglich der Teilleistungen von Schülern.
     *
     * @return object ein Objekt mit Maps unter den Attributen 'schueler'
     */
    public function getMapsTeilleistungen(): object {
        if ($this->mapsTeilleistungen === null) {
            $this->initMapsSchueler();
        }
        return $this->mapsTeilleistungen;
    }

    /**
     * Erstelle eine Map bezüglich der Leistungsdaten von Schülern.
     *
     * @return object ein Objekt mit Maps unter den Attributen 'schueler'
     */
    public function getMapsLeistungsdaten(): object {
        if ($this->mapsLeistungsdaten === null) {
            $this->initMapsSchueler();
        }
        return $this->mapsLeistungsdaten;
    }

    /**
     * Erstelle eine Map bezüglich der Lernabschnittsdaten von Schülern.
     *
     * @return object ein Objekt mit Maps unter den Attributen 'schueler'
     */
    public function getMapsLernabschnittsdaten(): object {
        if ($this->mapsLernabschnittsdaten === null) {
            $this->initMapsSchueler();
        }
        return $this->mapsLernabschnittsdaten;
    }

    /**
     * Ermittelt die ID der Klasse des Schülers für eine ID von einer Teilleistung
     *
     * @param int $idTeilleistung   die ID der Teilleistung
     *
     * @return int   die ID der Klasse
     */
    public function getKlassenIdByTeilleistungId(int $idTeilleistung): int {
        $map = $this->getMapsTeilleistungen();
        if (!array_key_exists($idTeilleistung, $map->schueler)) {
            Http::exit500("Die ENM-Daten sind inkonsistent. Zu der Teilleistungs-ID {$idTeilleistung} konnte kein Schüler bestimmt werden.");
        }
        return $map->schueler[$idTeilleistung]->klasseID;
    }

    /**
     * Ermittelt die ID der Klasse des Schüler für eine ID von Leistungsdaten
     *
     * @param int $idLeistung   die ID der Leistungsdaten
     *
     * @return int   die ID der Klasse
     */
    public function getKlassenIdByLeistungsdatenId(int $idLeistung): int {
        $map = $this->getMapsLeistungsdaten();
        if (!array_key_exists($idLeistung, $map->schueler)) {
            Http::exit500("Die ENM-Daten sind inkonsistent. Zu der Leistungsdaten-ID {$idLeistung} konnte kein Schüler bestimmt werden.");
        }
        return $map->schueler[$idLeistung]->klasseID;
    }

    /**
     * Ermittelt die ID der Klasse des Schüler für eine ID von Lernabschnittsdaten
     *
     * @param int $idLernabschnitt   die ID der Lernabschnittsdaten
     *
     * @return int   die ID der Klasse
     */
    public function getKlassenIdByLernabschnittsId(int $idLernabschnitt): int {
        $map = $this->getMapsLernabschnittsdaten();
        if (!array_key_exists($idLernabschnitt, $map->schueler)) {
            Http::exit500("Die ENM-Daten sind inkonsistent. Zu der Lernabschnitts-ID {$idLernabschnitt} konnte kein Schüler bestimmt werden.");
        }
        return $map->schueler[$idLernabschnitt]->klasseID;
    }

    /**
     * Ermittelt die ID der Klasse des Schülers für eine ID des Schülers
     *
     * @param int $idSchueler   die ID des Schülers
     *
     * @return int   die ID der Klasse
     */
    public function getKlassenIdBySchuelerId(int $idSchueler): int {
        $map = $this->getMapsSchueler();
        if (!array_key_exists($idSchueler, $map->schueler)) {
            Http::exit500("Die ENM-Daten sind inkonsistent. Zu der Schüler-ID {$idSchueler} konnte kein Schüler bestimmt werden.");
        }
        return $map->schueler[$idSchueler]->klasseID;
    }

    /**
     * Erstellt die ENM-Daten angepasst für den den übergebenen Lehrer
     *
     * @param object $lehrer   der Lehrer, für den die ENM-Daten zusammengestellt werden sollen
     *
     * @return string die für den Lehrer zusammengestellten ENM-Daten
     */
    public function getENMDatenForLehrer(object $lehrer): string {
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
                    if ($tmpLerngruppe != null) {
                        $setFachIDs[$tmpLerngruppe->fachID] = $tmpLerngruppe->fachID;
                    }
                }
            }
            // Bestimme die Ankreuzkompetenzen, die dem Lehrer über einen Leistungsdatensatz zugeordnet sind
            $kompetenzen = [];
            foreach ($schueler->ankreuzkompetenzen as $schuelerkompetenz) {
                $kompetenzVorhanden = array_key_exists($schuelerkompetenz->kompetenzID, $mapAnkreuzkompetenzen);
                if (!$kompetenzVorhanden) {
                    continue;
                }
                $kompetenz = $mapAnkreuzkompetenzen[$schuelerkompetenz->kompetenzID];
                if ((($kompetenz->istFachkompetenz === true) && ($istKlassenlehrer))
                    || (($kompetenz->fachID != null) && (array_key_exists($kompetenz->fachID, $setFachIDs)))) {
                    $kompetenzen[] = $schuelerkompetenz;
                }
            }
            // Prüfe, ob der Schüler zurückgegeben werden soll
            if ((empty($leistungen)) && (empty($kompetenzen))) {
                continue;
            }
            // Ersetze die Leistungsdaten und die Ankreuzkompetenzen
            $schueler->leistungsdaten = $leistungen;
            $schueler->ankreuzkompetenzen = $kompetenzen;
            $listSchueler[] = $schueler;
        }
        $daten->schueler = $listSchueler;
        $daten->lehrerID = $lehrer->id;
        return json_encode($daten, JSON_UNESCAPED_SLASHES);
    }

}

