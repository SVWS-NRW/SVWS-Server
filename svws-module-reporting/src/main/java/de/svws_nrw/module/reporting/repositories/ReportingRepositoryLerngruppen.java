package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

/**
 * Domänen-Repository für Klassen und Kurse.
 * Die Daten werden bei Bedarf aus der Datenbank nachgeladen und im Cache gehalten.
 */
public class ReportingRepositoryLerngruppen {

	private final ReportingRepositorySchule repositorySchule;
	private final DBEntityManager conn;
	private final Logger logger;

	private final Map<Long, ReportingKlasse> mapKlassen = new HashMap<>();
	private final Map<Long, ReportingKurs> mapKurse = new HashMap<>();

	/**
	 * Erstellt ein neues ReportingLerngruppenRepository.
	 *
	 * @param repositorySchule Das Domänen-Repository für Schuldaten und Schuljahresabschnitte.
	 * @param conn             Die Datenbankverbindung.
	 * @param logger           Der Logger.
	 */
	public ReportingRepositoryLerngruppen(final ReportingRepositorySchule repositorySchule, final DBEntityManager conn, final Logger logger) {
		this.repositorySchule = repositorySchule;
		this.conn = conn;
		this.logger = logger;
	}

	/**
	 * Gibt das ReportingKlasse-Objekt zur übergebenen ID zurück. Fehlt der Eintrag im Cache, wird er aus der Datenbank nachgeladen.
	 *
	 * @param idKlasse Die eindeutige ID der Klasse.
	 *
	 * @return Das ReportingKlasse-Objekt oder null, falls die Klasse nicht existiert.
	 */
	public ReportingKlasse klasse(final long idKlasse) {
		if (idKlasse < 0) {
			return null;
		}
		ergaenzeKlasseInMapKlassen(idKlasse);
		return mapKlassen.get(idKlasse);
	}

	/**
	 * Gibt eine nach Kürzel sortierte Liste von ReportingKlasse-Objekten zu den übergebenen IDs zurück.
	 *
	 * @param idsKlassen Liste der Klassen-IDs.
	 *
	 * @return Sortierte Liste von ReportingKlasse-Objekten.
	 */
	public List<ReportingKlasse> klassen(final List<Long> idsKlassen) {
		final List<ReportingKlasse> resultKlassen = new ArrayList<>();
		for (final Long idKlasse : idsKlassen) {
			if ((idKlasse == null) || (idKlasse < 0)) {
				continue;
			}
			ergaenzeKlasseInMapKlassen(idKlasse);
			resultKlassen.add(mapKlassen.get(idKlasse));
		}
		return resultKlassen.stream().sorted(Comparator.comparing(ReportingKlasse::kuerzel)).toList();
	}

	private void ergaenzeKlasseInMapKlassen(final long idKlasse) {
		if (!mapKlassen.containsKey(idKlasse)) {
			final KlassenDaten klassenDaten;
			try {
				klassenDaten = new DataKlassendaten(this.conn).getById(idKlasse);
				this.repositorySchule.schuljahresabschnitt(klassenDaten.idSchuljahresabschnitt).klasse(idKlasse);
			} catch (final ApiOperationException e) {
				ReportingExceptionUtils.logException(
						"FEHLER: Fehler bei der Ermittlung der Daten für des Klassen %s.".formatted(idKlasse), e, this.logger,
						LogLevel.ERROR, 0);
			}
		}
	}

	/**
	 * Gibt die Map der bereits erzeugten ReportingKlasse-Objekte zurück, indiziert nach Klassen-ID.
	 *
	 * @return Map der Klassen
	 */
	public Map<Long, ReportingKlasse> mapKlassen() {
		return mapKlassen;
	}

	/**
	 * Gibt die Map der bereits erzeugten ReportingKurs-Objekte zurück, indiziert nach Kurs-ID.
	 *
	 * @return Map der Kurse
	 */
	public Map<Long, ReportingKurs> mapKurse() {
		return mapKurse;
	}
}
