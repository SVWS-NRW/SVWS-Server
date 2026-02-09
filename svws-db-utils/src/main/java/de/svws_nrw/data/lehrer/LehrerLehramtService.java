package de.svws_nrw.data.lehrer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
import de.svws_nrw.repo.lehrer.LehrerPersonaldatenLehramtRepository;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Lehrämter von Lehrern
 */
public final class LehrerLehramtService {

	/** Das Repository für die Lehrämter von Lehrern */
	private final LehrerPersonaldatenLehramtRepository lehramtRepository;

	/** Der Service für die Fachrichtungen von Lehrern */
	private final LehrerFachrichtungService lehrerFachrichtungenService;

	/** Der Service für die Lehrbefähigungen von Lehrern*/
	private final LehrerLehrbefaehigungService lehrerLehrbefaehigungenService;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param lehramtRepository                das Repository für die Lehrämter
	 * @param lehrerFachrichtungenService      der Service für die Fachrichtungen
	 * @param lehrerLehrbefaehigungenService   der Service für die Lehrbefähigungen
	 */
	public LehrerLehramtService(final LehrerPersonaldatenLehramtRepository lehramtRepository,
			final LehrerFachrichtungService lehrerFachrichtungenService,
			final LehrerLehrbefaehigungService lehrerLehrbefaehigungenService) {
		this.lehramtRepository = lehramtRepository;
		this.lehrerFachrichtungenService = lehrerFachrichtungenService;
		this.lehrerLehrbefaehigungenService = lehrerLehrbefaehigungenService;
	}


	private static LehrerLehramtEintrag map(final DTOLehrerPersonaldatenLehramt dtoLehramt,
			final List<LehrerFachrichtungEintrag> fachrichtungen,
			final List<LehrerLehrbefaehigungEintrag> lehrbefaehigungen) {
		final LehrerLehramtEintrag daten = new LehrerLehramtEintrag();
		daten.id = dtoLehramt.ID;
		daten.idLehrer = dtoLehramt.Lehrer_ID;
		daten.idKatalogLehramt = dtoLehramt.Lehramt_Katalog_ID;
		daten.idAnerkennungsgrund = dtoLehramt.LehramtAnerkennung_Katalog_ID;
		daten.fachrichtungen.addAll(fachrichtungen);
		daten.lehrbefaehigungen.addAll(lehrbefaehigungen);
		return daten;
	}


	/**
	 * Gibt eine Map mit der Zuordnung der Lehrämter zu den Lehrern mit den übergebenen IDs zurück.
	 *
	 * @param idsLehrer   die IDs der Lehrer
	 *
	 * @return die Zuordnung
	 */
	public @NotNull Map<Long, List<LehrerLehramtEintrag>> getMapByLehrer(final Collection<Long> idsLehrer) {
		final var listLehraemter = lehramtRepository.findListByIds(idsLehrer);
		final var idsLehraemter = listLehraemter.stream().map(la -> la.ID).toList();
		final var mapFachrichtungen = lehrerFachrichtungenService.getMapByLehramt(idsLehraemter);
		final var mapLehrbefaehigungen = lehrerLehrbefaehigungenService.getMapByLehramt(idsLehraemter);

		// Erstelle die Liste der Core-DTOs anhand der zuvor geladenen Daten
		return listLehraemter.stream().map(dtoLehramt -> {
			final var listFachrichtungen = mapFachrichtungen.get(dtoLehramt.ID);
			final var listLehrbefaehigungen = mapLehrbefaehigungen.get(dtoLehramt.ID);
			return map(dtoLehramt,
					(listFachrichtungen == null) ? new ArrayList<>() : listFachrichtungen,
					(listLehrbefaehigungen == null) ? new ArrayList<>() : listLehrbefaehigungen);
		}).collect(Collectors.groupingBy(la -> la.idLehrer));
	}

}
