package de.svws_nrw.service.statistik;

import java.util.List;
import java.util.Optional;

import de.svws_nrw.asd.data.statistik.SchulKatalogStatistikGesamt;
import de.svws_nrw.asd.types.schueler.HerkunftSchulform;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.repo.schule.kataloge.schule.SchuleRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Daten zu den Religionen für die Statistik
 */
public final class SchulKatalogStatistikService {

	/** Das Repository für den Zugriff auf die Schul-Statistikdaten */
	private final EigeneSchuleRepository eigeneSchuleRepository;

	/** Der Service für den Zugriff auf die Schuljahresabschnitte */
	private final SchuljahresabschnittService schuljahresabschnitteService;

	/** Das Repository für den Zugriff auf die Daten zu dem Schuleigenen Katalog der Schulen */
	private final SchuleRepository schuleRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param eigeneSchuleRepository         das Repository für den Zugriff auf die Schuldaten der Schule
	 * @param schuljahresabschnitteService   der Service für den Zugriff auf die Schuljahresabschnitte
	 * @param schuleRepository               das Repository für den Schuleignen Katalog der Schulen
	 */
	public SchulKatalogStatistikService(final EigeneSchuleRepository eigeneSchuleRepository, final SchuljahresabschnittService schuljahresabschnitteService,
			final SchuleRepository schuleRepository) {
		this.eigeneSchuleRepository = eigeneSchuleRepository;
		this.schuljahresabschnitteService = schuljahresabschnitteService;
		this.schuleRepository = schuleRepository;
	}


	private static SchulKatalogStatistikGesamt map(final DTOSchuleNRW dto, final int schuljahr) {
		final var daten = new SchulKatalogStatistikGesamt();
		daten.id = dto.ID;
		daten.kurzbezeichnung = dto.KurzBez;
		daten.schulnummerStatistik = dto.SchulNr_SIM;
		daten.name = dto.Name;
		daten.idSchulform = mapIdSchulform(dto.SchulformNr, dto.SchulNr_SIM, schuljahr);
		return daten;
	}


	/**
	 * Gibt die Liste zu allen statistik-Relevanten Daten der Religionen zurück.
	 *
	 * @return die Liste zu allen statistik-Relevanten Daten der Religionen
	 */
	public @NotNull List<SchulKatalogStatistikGesamt> getList() {
		final var idSchuljahresabschnitt = eigeneSchuleRepository.getIdSchuljahresabschnitt();
		final var schuljahresabschnitt = schuljahresabschnitteService.getById(idSchuljahresabschnitt);
		return schuleRepository.getAll().stream().map(f -> map(f, schuljahresabschnitt.schuljahr)).toList();
	}

	private static Long mapIdSchulform(final String schulformNr, final String schulnummerStatistik, final int schuljahr) {
		if ((schulformNr == null) || (schulnummerStatistik == null)) {
			return null;
		}
		if (isOeffentlicheOderErsatzschuleNRW(schulnummerStatistik)) {
			return getIdSchulformForOeffentlicheOderErsatzschuleNRW(schulformNr, schuljahr);
		}
		if (isSonstigeSchule(schulnummerStatistik)) {
			return getIdSchulformForSonstigeSchule(schulformNr, schuljahr);
		}
		return null;
	}

	private static boolean isOeffentlicheOderErsatzschuleNRW(final String schulnummerStatistik) {
		return schulnummerStatistik.startsWith("1");
	}

	private static boolean isSonstigeSchule(final String schulnummerStatistik) {
		return schulnummerStatistik.startsWith("9");
	}

	private static Long getIdSchulformForOeffentlicheOderErsatzschuleNRW(final String schulformNr, final int schuljahr) {
		return Optional.ofNullable(Schulform.data().getWertBySchluessel(schulformNr))
				.map(s -> s.daten(schuljahr))
				.map(s -> s.id)
				.orElse(null);
	}

	private static Long getIdSchulformForSonstigeSchule(final String schulformNr, final int schuljahr) {
		return Optional.ofNullable(HerkunftSchulform.data().getWertBySchluessel(schulformNr))
				.map(s -> s.daten(schuljahr))
				.map(s -> s.id)
				.orElse(null);
	}

}
