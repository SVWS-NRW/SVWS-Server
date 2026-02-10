package de.svws_nrw.data.statistik;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.klassen.KlassenleitungenRepository;
import de.svws_nrw.repo.schueler.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.SchuleRepository;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Klassendaten für die Statistik
 */
public final class KlassenStatistikService {

	/** Das Repository für den Zugriff auf die Klassen-Daten */
	private final SchuleRepository schuleRepository;

	/** Das Repository für den Zugriff auf die Klassen-Daten */
	private final KlassenRepository klassenRepository;

	/** Das Repository für den Zugriff auf die Klassenleitungen */
	private final KlassenleitungenRepository klassenleitungenRepository;

	/** Das Repository für den Zugriff auf die Lernabschnitte für die Bestimmung der Schüler einer Klasse */
	private final SchuelerLernabschnittRepository lernabschnitteRepository;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuleRepository             das Repository für die Schuldaten
	 * @param klassenRepository            das Repository für die Klassendaten
	 * @param klassenleitungenRepository   das Repository für die Klassenleitungen
	 * @param lernabschnitteRepository     das Repository für die Lernabschnitte
	 */
	public KlassenStatistikService(final SchuleRepository schuleRepository,
			final KlassenRepository klassenRepository,
			final KlassenleitungenRepository klassenleitungenRepository,
			final SchuelerLernabschnittRepository lernabschnitteRepository) {
		this.schuleRepository = schuleRepository;
		this.klassenRepository = klassenRepository;
		this.klassenleitungenRepository = klassenleitungenRepository;
		this.lernabschnitteRepository = lernabschnitteRepository;
	}


	private static KlassenStatistikGesamt map(final DTOKlassen dtoKlasse, final List<Long> klassenleitungen, final List<Long> idsSchueler) {
		// Erzeuge das Core-DTO mit den grundlegenden Daten
		final var daten = new KlassenStatistikGesamt();
		daten.id = dtoKlasse.ID;
		daten.idSchuljahresabschnitt = dtoKlasse.Schuljahresabschnitts_ID;
		daten.kuerzel = dtoKlasse.Klasse;
		daten.idJahrgang = dtoKlasse.Jahrgang_ID;
		daten.parallelitaet = ((dtoKlasse.ASDKlasse == null) || (dtoKlasse.ASDKlasse.length() < 3)) ? null : dtoKlasse.ASDKlasse.substring(2);
		daten.sortierung = dtoKlasse.Sortierung;
		daten.teilstandort = Objects.toString(dtoKlasse.AdrMerkmal, "");

		daten.klassenLeitungen = klassenleitungen;
		daten.idsSchueler = idsSchueler;
		return daten;
	}


	/**
	 * Gibt die Liste aller Statistik-relevanten Lehrer für den an der Schule aktuellen Schuljahresabschnitt zurück.
	 *
	 * @return die Liste aller Statistik-relevanten Lehrer
	 */
	public @NotNull List<KlassenStatistikGesamt> getList() {
		// Bestimme den aktuellen Schuljahresabschnitt der Schule
		final long idSchuljahresabschnitt = schuleRepository.getSchuljahresabschnitt();

		// Bestimme zunächst die Statistik-Relevanten Lehrkräfte und deren IDs
		final List<DTOKlassen> listKlassen = klassenRepository.getListBySchuljahresabschnitt(idSchuljahresabschnitt);
		final List<Long> listKlassenIDs = listKlassen.stream().map(l -> l.ID).toList();

		// Bestimme die Zuordnung der Klassenleitungen zu den einzelnen Klassen
		final var mapKlassenleitungen = klassenleitungenRepository.getMapKlassenleitungen(listKlassenIDs);

		// Bestimme über die zugehörigen Lernabschnittsdaten die Zuordnung der Schüler zu den einzelnen Klassen
		final var mapKlassenSchueler = lernabschnitteRepository.getMapKlassenSchueler(listKlassenIDs);

		// Erstelle die Liste der Core-DTOs anhand der zuvor geladenen Daten
		return listKlassen.stream().map(dtoKlasse -> map(dtoKlasse,
				mapKlassenleitungen.getOrDefault(dtoKlasse.ID, Collections.emptyList()),
				mapKlassenSchueler.getOrDefault(dtoKlasse.ID, Collections.emptyList()))).toList();
	}

}
