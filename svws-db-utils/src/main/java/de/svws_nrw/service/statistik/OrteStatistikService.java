package de.svws_nrw.service.statistik;

import java.util.List;

import de.svws_nrw.asd.data.statistik.OrteStatistikGesamt;
import de.svws_nrw.asd.types.schule.Laender;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Daten zu den Orten für die Statistik
 */
public final class OrteStatistikService {

	/** Das Repository für den Zugriff auf die Schul-Statistikdaten */
	private final EigeneSchuleRepository eigeneSchuleRepository;

	/** Der Service für den Zugriff auf die Schuljahresabschnitte */
	private final SchuljahresabschnittService schuljahresabschnitteService;

	/** Das Repository für den Zugriff auf die Orts-Daten */
	private final OrtRepository ortRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param eigeneSchuleRepository               das Repository für die Schule
	 * @param schuljahresabschnitteService   der Service für den Zugriff auf die Schuljahresabschnitte
	 * @param ortRepository                 das Repository für die Orte
	 */
	public OrteStatistikService(final EigeneSchuleRepository eigeneSchuleRepository, final SchuljahresabschnittService schuljahresabschnitteService,
			final OrtRepository ortRepository) {
		this.eigeneSchuleRepository = eigeneSchuleRepository;
		this.schuljahresabschnitteService = schuljahresabschnitteService;
		this.ortRepository = ortRepository;
	}


	private static OrteStatistikGesamt map(final DTOOrt dto, final int schuljahr) {
		final var daten = new OrteStatistikGesamt();
		daten.id = dto.id;
		daten.plz = dto.plz;
		daten.ortsname = dto.ortsname;
		daten.idLand = Laender.data().getIDByWertAndSchuljahr(Laender.data().getWertBySchluessel(dto.schluesselBundesland), schuljahr);
		return daten;
	}


	/**
	 * Gibt die Liste aller Orte zurück.
	 *
	 * @return die Liste aller Orte
	 */
	public @NotNull List<OrteStatistikGesamt> getList() {
		final var idSchuljahresabschnitt = eigeneSchuleRepository.getIdSchuljahresabschnitt();
		final var schuljahresabschnitt = schuljahresabschnitteService.getById(idSchuljahresabschnitt);
		return ortRepository.getAll().stream().map(f -> map(f, schuljahresabschnitt.schuljahr)).toList();
	}

}
