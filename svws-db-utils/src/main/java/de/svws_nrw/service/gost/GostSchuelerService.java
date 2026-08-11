package de.svws_nrw.service.gost;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.kataloge.jahrgaenge.JahrgaengeUtils;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schueler.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für die Schüler des Gymnasialen Oberstufe
 */
public class GostSchuelerService {

	private final BenutzerAllgemeinRepository benutzerRepository;
	private final JahrgangRepository jahrgangRepository;
	private final SchuelerRepository schuelerRepository;
	private final SchuelerLernabschnittRepository schuelerLernabschnittRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param benutzerRepository                     das Repository für den Zugriff auf Benutzerdaten und den angemeldeten Benutzer
	 * @param jahrgangRepository                   das Repository für den Zugriff auf Jahrgänge
	 * @param schuelerRepository                     das Repository für den Zugriff auf Schülerdaten
	 * @param schuelerLernabschnittRepository        das Repository für den Zugriff auf die Schüler-Lernabschnittsdaten
	 */
	public GostSchuelerService(final BenutzerAllgemeinRepository benutzerRepository,
			final JahrgangRepository jahrgangRepository,
			final SchuelerRepository schuelerRepository,
			final SchuelerLernabschnittRepository schuelerLernabschnittRepository) {
		this.benutzerRepository = benutzerRepository;
		this.jahrgangRepository = jahrgangRepository;
		this.schuelerRepository = schuelerRepository;
		this.schuelerLernabschnittRepository = schuelerLernabschnittRepository;
	}


	/**
	 * Bestimmt alle aufgenommenen Schüler, die in der Gymnasialen Oberstufe waren, sind oder potentiell noch dorthin kommen können. <br>
	 * - die Schüler dürfen nicht gelöscht sein. <br>
	 * - bei Status 10 (Ehemalige) muss gefiltert werden, da diese keine weiteren Schuldaten und damit auch keine Oberstufendaten haben <br>
	 * - Status 8, 9 (Abschluss, Abgänger) muss gefiltert werden, da nur Schüler mit einem Abschluss in einem der Entlassjahrgänge der Oberstufe zurückgegeben werden sollen <br>
	 * Alle anderen Schüler werden zunächst eingelesen, da sie entweder in der Oberstufe sind oder noch dahin kommen können
	 *
	 * @param mapJahrgaenge   eine Map mit den an der Schule definierten Jahrgängen - aus diesen werden die Jahrgänge der gymnasialen Oberstufe gefiltert
	 *
	 * @return die Liste mit den Schülern
	 */
	private List<DTOSchueler> getPotentielleGostSchueler(final Map<Long, DTOJahrgang> mapJahrgaenge) {
		// Bestimme alle Jahrgänge der Schule, welche als ASD-Jahrgang 'EF', 'Q1', 'Q2' haben
		final List<Long> idsGostJahrgaenge = mapJahrgaenge.values().stream()
				.filter(j -> "EF".equals(j.ASDJahrgang) || "Q1".equals(j.ASDJahrgang) || "Q2".equals(j.ASDJahrgang))
				.map(j -> j.ID).toList();

		return schuelerRepository.getAll().stream().filter(
				s -> (!s.Geloescht) && (s.idStatus != 10) && (((s.idStatus != 8) && (s.idStatus != 9)) || (idsGostJahrgaenge.contains(s.Entlassjahrgang_ID))))
				.toList();
	}

	/**
	 * Bestimmt die Entitäten aller Schüler des angebebenen Abiturjahrgangs.
	 *
	 * @param abijahrgang   der Abiturjahrgang
	 *
	 * @return die Liste mit den Schülern
	 */
	public List<DTOSchueler> getByAbiturjahrgang(final int abijahrgang) {
		// Bestimme zunächst informationen zur Schule
		final Benutzer benutzer = benutzerRepository.getAktuellerBenutzer();
		final Schuljahresabschnitt schuleSchuljahresabschnitt = benutzer.schuleGetSchuljahresabschnitt();
		final Schulform schulform = benutzer.schuleGetSchulform();
		final Map<Long, DTOJahrgang> mapJahrgaenge = jahrgangRepository.getMap();

		// Bestimme Schüler, welche in den Abiturjahrgängen sein können - auch in zukünftigen.
		final List<DTOSchueler> listSchueler = this.getPotentielleGostSchueler(mapJahrgaenge);
		final List<Long> idsSchueler = listSchueler.stream().map(s -> s.ID).toList();

		// Bestimme die aktuellen SchuelerLernabschnitte der Schüler
		final Map<Long, DTOSchuelerLernabschnittsdaten> mapLernabschnitte = schuelerLernabschnittRepository.getMapAktuelleBySchuelerIDs(idsSchueler);

		// Filtere die Schüler auf die Schüler des konkreten Abiturjahrgangs
		final List<DTOSchueler> result = new ArrayList<>();
		for (final DTOSchueler schueler : listSchueler) {
			final Schuljahresabschnitt schuljahresabschnitt = benutzer.schuleGetAbschnittById(schueler.Schuljahresabschnitts_ID);
			if (schuljahresabschnitt == null) {
				continue;
			}
			final DTOSchuelerLernabschnittsdaten lernabschnitt = mapLernabschnitte.get(schueler.ID);
			if (lernabschnitt == null) {
				continue;
			}
			final DTOJahrgang jahrgang = mapJahrgaenge.get(lernabschnitt.Jahrgang_ID);
			if (jahrgang == null) {
				continue;
			}
			// Filtere Schüler, die vor der Oberstufe die Schule verlassen haben
			final @NotNull SchuelerStatus status = SchuelerStatus.data().getWertByID(schueler.idStatus == null ? null : schueler.idStatus.longValue());
			if ((schuljahresabschnitt.schuljahr < schuleSchuljahresabschnitt.schuljahr)
					&& !"EF".equals(jahrgang.ASDJahrgang) && !"Q1".equals(jahrgang.ASDJahrgang) && !"Q2".equals(jahrgang.ASDJahrgang)
					&& ((status == SchuelerStatus.ABGANG) || (status == SchuelerStatus.ABSCHLUSS) || (status == SchuelerStatus.EHEMALIGE)
							|| (status == SchuelerStatus.WARTELISTE))) {
				continue;
			}
			// Bestimme die Restjahre in Bezug auf den Abiturjahrgang und den Schuljahresabschnitt
			final int restjahreNachAbiturjahr = abijahrgang - schuljahresabschnitt.schuljahr;
			final Integer restjahreNachJahrgang =
					JahrgaengeUtils.getRestlicheJahreBisAbitur(schulform, Schulgliederung.data().getWertByKuerzel(jahrgang.GliederungKuerzel),
							schuljahresabschnitt.schuljahr, jahrgang.ASDJahrgang);
			if ((restjahreNachJahrgang != null) && (restjahreNachAbiturjahr == restjahreNachJahrgang)) {
				result.add(schueler);
			}
		}
		return result;
	}

}
