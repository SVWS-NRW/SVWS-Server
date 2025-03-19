package de.svws_nrw.module.reporting.types.schule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Schuljahresabschnitt.
 */
public class ReportingSchuljahresabschnitt extends ReportingBaseType {

	/** Die ID des Schuljahresabschnittes */
	protected long id;

	/** Das Schuljahr, in welchem der Schuljahresabschnitt liegt */
	protected int schuljahr;

	/** Die Nummer des Abschnitts im Schuljahr */
	protected int abschnitt;

	/** Die ID des Schuljahresabschnittes, der diesem Abschnitt folgt. */
	protected Long idFolgenderAbschnitt;

	/** Die ID des Schuljahresabschnittes, der diesem Abschnitt vorhergeht. */
	protected Long idVorherigerAbschnitt;

	/** Der Schuljahresabschnitt, der diesem Abschnitt folgt. */
	protected ReportingSchuljahresabschnitt folgenderAbschnitt;

	/** Der Schuljahresabschnitt, der diesem Abschnitt vorhergeht. */
	protected ReportingSchuljahresabschnitt vorherigerAbschnitt;

	/** Die Map der Fächer des Schuljahresabschnitts */
	protected Map<Long, ReportingFach> mapFaecher;

	/** Die Map der Jahrgänge des Schuljahresabschnitts */
	protected Map<Long, ReportingJahrgang> mapJahrgaenge;

	/** Die Map der Klassen des Schuljahresabschnitts */
	protected Map<Long, ReportingKlasse> mapKlassen;

	/** Die Map der Kurse des Schuljahresabschnitts */
	protected Map<Long, ReportingKurs> mapKurse;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param id 					Die ID des Schuljahresabschnittes
	 * @param schuljahr 			Das Schuljahr, in welchem der Schuljahresabschnitt liegt
	 * @param abschnitt 			Die Nummer des Abschnitts im Schuljahr
	 * @param idFolgenderAbschnitt 	Die ID des Schuljahresabschnittes, der diesem Abschnitt folgt.
	 * @param idVorherigerAbschnitt Die ID des Schuljahresabschnittes, der diesem Abschnitt vorhergeht.
	 * @param folgenderAbschnitt 	Der Schuljahresabschnitt, der diesem Abschnitt folgt.
	 * @param vorherigerAbschnitt 	Der Schuljahresabschnitt, der diesem Abschnitt vorhergeht.
	 * @param faecher				Die Fächer des Schuljahresabschnitts
	 * @param jahrgaenge			Die Jahrgänge des Schuljahresabschnitts
	 * @param klassen				Die Klassen des Schuljahresabschnitts
	 * @param kurse					Die Kurse des Schuljahresabschnitts
	 */
	public ReportingSchuljahresabschnitt(final long id, final int schuljahr, final int abschnitt, final Long idFolgenderAbschnitt,
			final Long idVorherigerAbschnitt, final ReportingSchuljahresabschnitt folgenderAbschnitt, final ReportingSchuljahresabschnitt vorherigerAbschnitt,
			final Map<Long, ReportingFach> faecher, final Map<Long, ReportingJahrgang> jahrgaenge, final Map<Long, ReportingKlasse> klassen,
			final Map<Long, ReportingKurs> kurse) {
		this.id = id;
		this.schuljahr = schuljahr;
		this.abschnitt = abschnitt;
		this.idFolgenderAbschnitt = idFolgenderAbschnitt;
		this.idVorherigerAbschnitt = idVorherigerAbschnitt;
		this.folgenderAbschnitt = folgenderAbschnitt;
		this.vorherigerAbschnitt = vorherigerAbschnitt;
		this.mapFaecher = faecher;
		this.mapJahrgaenge = jahrgaenge;
		this.mapKlassen = klassen;
		this.mapKurse = kurse;
	}


	// ##### Berechnete Methoden #####
	/**
	 * Kurzer Text zum Schuljahresabschnitt im Format 20XX/YY.A
	 *
	 * @return Kurzer Text zum Schuljahresabschnitt
	 */
	public String textSchuljahresabschnittKurz() {
		return "%s/%s.%s".formatted(schuljahr, (schuljahr % 100) + 1, abschnitt);
	}

	/**
	 * Kurzer Text zum Schuljahresabschnitt im Format 20XX/YY.A
	 *
	 * @return Kurzer Text zum Schuljahresabschnitt
	 */
	public String textSchuljahresabschnittLang() {
		return "%s/%s %s. Halbjahr".formatted(schuljahr, (schuljahr % 100) + 1, abschnitt);
	}

	/**
	 * Gibt das Fach zur ID aus der Liste der Fächer des Schuljahresabschnitts zurück
	 *
	 * @param id	Die ID des Faches
	 *
	 * @return 		Das Fach zur ID oder null, wenn das Fach nicht vorhanden ist.
	 */
	public ReportingFach fach(final long id) {
		return mapFaecher().get(id);
	}

	/**
	 * Gibt die Fächer zu den IDs aus der Liste der Fächer des Schuljahresabschnitts zurück
	 *
	 * @param ids	Die IDs der Fächer
	 *
	 * @return 		Die Fächer zu den IDs oder eine leere Liste, wenn kein Fach vorhanden ist.
	 */
	public List<ReportingFach> faecher(final List<Long> ids) {
		final List<ReportingFach> result = new ArrayList<>();
		if (ids == null)
			return result;
		final List<Long> idsNonNull =  ids.stream().filter(Objects::nonNull).distinct().toList();
		if (idsNonNull.isEmpty())
			return result;
		idsNonNull.forEach(idFach -> result.add(fach(idFach)));
		return result;
	}

	/**
	 * Gibt den Jahrgang zur ID aus der Liste der Jahrgänge des Schuljahresabschnitts zurück
	 *
	 * @param id	Die ID des Jahrgangs
	 *
	 * @return 		Der Jahrgang zur ID oder null, wenn der Jahrgang nicht vorhanden ist.
	 */
	public ReportingJahrgang jahrgang(final long id) {
		return mapJahrgaenge().get(id);
	}

	/**
	 * Gibt die Klasse zur ID aus der Liste der Klassen des Schuljahresabschnitts zurück
	 *
	 * @param id	Die ID der Klasse
	 *
	 * @return 		Die Klasse zur ID oder null, wenn die Klasse nicht vorhanden ist.
	 */
	public ReportingKlasse klasse(final long id) {
		return mapKlassen().get(id);
	}

	/**
	 * Die Klassen des Schuljahresabschnitts
	 *
	 * @return Inhalt des Feldes klassen
	 */
	public List<ReportingKlasse> klassen() {
		return mapKlassen.values().stream().toList();
	}

	/**
	 * Gibt den Kurs zur ID aus der Liste der Kurse des Schuljahresabschnitts zurück
	 *
	 * @param id	Die ID des Kurses
	 *
	 * @return 		Der Kurs zur ID oder null, wenn der Kurs nicht vorhanden ist.
	 */
	public ReportingKurs kurs(final long id) {
		return mapKurse().get(id);
	}

	/**
	 * Die Fächer des Schuljahresabschnitts
	 *
	 * @return Inhalt des Feldes faecher
	 */
	public List<ReportingFach> faecher() {
		return mapFaecher.values().stream().toList();
	}

	/**
	 * Die Jahrgänge des Schuljahresabschnitts
	 *
	 * @return Inhalt des Feldes jahrgaenge
	 */
	public List<ReportingJahrgang> jahrgaenge() {
		return mapJahrgaenge.values().stream().toList();
	}

	/**
	 * Die Kurse des Schuljahresabschnitts
	 *
	 * @return Inhalt des Feldes kurse
	 */
	public List<ReportingKurs> kurse() {
		return mapKurse.values().stream().toList();
	}


	// ##### Getter #####

	/**
	 * Die ID des Schuljahresabschnittes
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Das Schuljahr, in welchem der Schuljahresabschnitt liegt
	 *
	 * @return Inhalt des Feldes schuljahr
	 */
	public int schuljahr() {
		return schuljahr;
	}

	/**
	 * Die Nummer des Abschnitts im Schuljahr
	 *
	 * @return Inhalt des Feldes abschnitt
	 */
	public int abschnitt() {
		return abschnitt;
	}

	/**
	 * Die ID des Schuljahresabschnittes, der diesem Abschnitt folgt.
	 *
	 * @return Inhalt des Feldes idFolgenderAbschnitt
	 */
	public Long idFolgenderAbschnitt() {
		return idFolgenderAbschnitt;
	}

	/**
	 * Die ID des Schuljahresabschnittes, der diesem Abschnitt vorhergeht.
	 *
	 * @return Inhalt des Feldes idVorherigerAbschnitt
	 */
	public Long idVorherigerAbschnitt() {
		return idVorherigerAbschnitt;
	}

	/**
	 * Der Schuljahresabschnitt, der diesem Abschnitt folgt.
	 *
	 * @return Inhalt des Feldes folgenderAbschnitt
	 */
	public ReportingSchuljahresabschnitt folgenderAbschnitt() {
		return folgenderAbschnitt;
	}

	/**
	 * Der Schuljahresabschnitt, der diesem Abschnitt vorhergeht.
	 *
	 * @return Inhalt des Feldes vorherigerAbschnitt
	 */
	public ReportingSchuljahresabschnitt vorherigerAbschnitt() {
		return vorherigerAbschnitt;
	}

	/**
	 * Die Map der Fächer des Schuljahresabschnitts
	 *
	 * @return Inhalt des Feldes mapFaecher
	 */
	public Map<Long, ReportingFach> mapFaecher() {
		return mapFaecher;
	}

	/**
	 * Die Map der Jahrgänge des Schuljahresabschnitts
	 *
	 * @return Inhalt des Feldes mapJahrgaenge
	 */
	public Map<Long, ReportingJahrgang> mapJahrgaenge() {
		return mapJahrgaenge;
	}

	/**
	 * Die Map der Klassen des Schuljahresabschnitts
	 *
	 * @return Inhalt des Feldes mapKlassen
	 */
	public Map<Long, ReportingKlasse> mapKlassen() {
		return mapKlassen;
	}

	/**
	 * Die Map der Kurse des Schuljahresabschnitts
	 *
	 * @return Inhalt des Feldes mapKurse
	 */
	public Map<Long, ReportingKurs> mapKurse() {
		return mapKurse;
	}
}
