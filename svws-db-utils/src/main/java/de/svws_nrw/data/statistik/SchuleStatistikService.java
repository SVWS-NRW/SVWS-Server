package de.svws_nrw.data.statistik;

import java.util.List;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.data.statistik.SchuleStatistikGesamt;
import de.svws_nrw.data.schule.SchuljahresabschnittService;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.repo.schule.SchuleRepository;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Schuldaten für die Statistik
 */
public final class SchuleStatistikService {

	/** Das Repository für den Zugriff auf die Schul-Statistikdaten */
	private final SchuleRepository schuleRepository;

	/** Der Service für den Zugriff auf die Schuljahresabschnitte */
	private final SchuljahresabschnittService schuljahresabschnitteService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuleRepository               das Repository für den Zugriff auf die Schuldaten der Schule
	 * @param schuljahresabschnitteService   der Service für den Zugriff auf die Schuljahresabschnitte
	 */
	public SchuleStatistikService(final SchuleRepository schuleRepository,
			final SchuljahresabschnittService schuljahresabschnitteService) {
		this.schuleRepository = schuleRepository;
		this.schuljahresabschnitteService = schuljahresabschnitteService;
	}


	private static SchuleStatistikGesamt map(final DTOEigeneSchule dtoSchule, final List<Schuljahresabschnitt> schuljahresabschnitte) {
		final var daten = new SchuleStatistikGesamt();
		daten.schulNr = dtoSchule.SchulNr;
		daten.schulform = dtoSchule.SchulformKuerzel;
		daten.bezeichnung1 = dtoSchule.Bezeichnung1;
		daten.bezeichnung2 = dtoSchule.Bezeichnung2;
		daten.bezeichnung3 = dtoSchule.Bezeichnung3;
		daten.strassenname = dtoSchule.Strassenname;
		daten.hausnummer = dtoSchule.HausNr;
		daten.hausnummerZusatz = dtoSchule.HausNrZusatz;
		daten.plz = dtoSchule.PLZ;
		daten.ort = dtoSchule.Ort;
		daten.telefon = dtoSchule.Telefon;
		daten.fax = dtoSchule.Fax;
		daten.email = dtoSchule.Email;
		daten.webAdresse = dtoSchule.WebAdresse;
		daten.idSchuljahresabschnitt = dtoSchule.Schuljahresabschnitts_ID;
		daten.dauerUnterrichtseinheit = (dtoSchule.DauerUnterrichtseinheit == null) ? 45 : dtoSchule.DauerUnterrichtseinheit;
		daten.abschnitte.addAll(schuljahresabschnitte);
		return daten;
	}


	/**
	 * Gibt die Statistik-Daten für die gesamte Schule für den aktuellen Schuljahresabschnitt der Schule zurück.
	 *
	 * @return die Liste aller Statistik-relevanten Lehrer
	 */
	public @NotNull SchuleStatistikGesamt get() {
		final var dtoSchule = schuleRepository.getFirst();
		final var dtosSchuljahresabschnitte = schuljahresabschnitteService.getList();
		return map(dtoSchule, dtosSchuljahresabschnitte);
	}

}
