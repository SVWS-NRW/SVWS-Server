package de.svws_nrw.module.reporting.repositories;

import java.util.HashMap;
import java.util.Map;

import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;

/**
 * Domänen-Repository für GOSt-Daten (Abiturjahrgänge, Beratungsdaten, Kursplanung).
 * Wird lazy befüllt, d. h. es erfolgt keine Initialisierungslogik im Konstruktor.
 */
public class ReportingRepositoryGost {

	private final Map<Integer, GostJahrgangsdaten> mapAbiturjahrgangDaten = new HashMap<>();
	private final Map<Integer, GostFaecherManager> mapAbiturjahrgangFaecher = new HashMap<>();
	private final Map<Long, GostLaufbahnplanungBeratungsdaten> mapBeratungsdaten = new HashMap<>();
	private final Map<Long, Abiturdaten> mapBeratungsdatenAbiturdaten = new HashMap<>();
	private final Map<Long, Abiturdaten> mapSchuelerAbiturdaten = new HashMap<>();
	private final Map<Long, ReportingGostKursplanungKurs> mapKursplanungKurse = new HashMap<>();

	/**
	 * Gibt die Map der Jahrgangsdaten zu den Abiturjahrgängen zurück, indiziert nach dem Abiturjahrgang.
	 *
	 * @return Map der Daten zu den Abiturjahrgängen
	 */
	public Map<Integer, GostJahrgangsdaten> abiturjahrgangDaten() {
		return mapAbiturjahrgangDaten;
	}

	/**
	 * Gibt die Map der Fächermanager zu den Abiturjahrgängen zurück, indiziert nach dem Abiturjahrgang.
	 *
	 * @return Map der Fächermanager zu den Abiturjahrgängen
	 */
	public Map<Integer, GostFaecherManager> abiturjahrgangFaecher() {
		return mapAbiturjahrgangFaecher;
	}

	/**
	 * Gibt die Map der GOSt-Laufbahnberatungsdaten zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map mit GOSt-Beratungsdaten der Schüler
	 */
	public Map<Long, GostLaufbahnplanungBeratungsdaten> beratungsdaten() {
		return mapBeratungsdaten;
	}

	/**
	 * Gibt die Map der Abiturdaten zurück, die im Rahmen der Laufbahnberatung ermittelt wurden, indiziert nach Schüler-ID.
	 *
	 * @return Map mit GOSt-Beratungsdaten-Abiturdaten
	 */
	public Map<Long, Abiturdaten> beratungsdatenAbiturdaten() {
		return mapBeratungsdatenAbiturdaten;
	}

	/**
	 * Gibt die Map der GOSt-Abiturdaten der Schüler zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map mit GOSt-Abiturdaten der Schüler
	 */
	public Map<Long, Abiturdaten> schuelerAbiturdaten() {
		return mapSchuelerAbiturdaten;
	}

	/**
	 * Gibt die Map der aktuell geladenen Kursplanungs-Kurse zurück, indiziert nach Kurs-ID.
	 *
	 * @return Map der aktuell geladenen Kursplanung-Kurse
	 */
	public Map<Long, ReportingGostKursplanungKurs> kursplanungKurse() {
		return mapKursplanungKurse;
	}
}
