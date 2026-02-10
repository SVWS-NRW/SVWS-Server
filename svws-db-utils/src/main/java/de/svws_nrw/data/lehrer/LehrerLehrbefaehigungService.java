package de.svws_nrw.data.lehrer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtBefaehigung;
import de.svws_nrw.repo.lehrer.LehrerPersonaldatenLehramtLehrbefaehigungenRepository;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Lehrbefähigungen von Lehrern
 */
public final class LehrerLehrbefaehigungService {

	/** Das Repository für den Zugriff auf die Lehrbefähigungen zu den Lehrämtern */
	private final LehrerPersonaldatenLehramtLehrbefaehigungenRepository lehrbefaehigungenRepository;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param lehrbefaehigungenRepository   das Repository für den Datenbank-Zugriff auf die DB-DTOs der Lehrbefähigungen
	 */
	public LehrerLehrbefaehigungService(final LehrerPersonaldatenLehramtLehrbefaehigungenRepository lehrbefaehigungenRepository) {
		this.lehrbefaehigungenRepository = lehrbefaehigungenRepository;
	}


	private static LehrerLehrbefaehigungEintrag map(final DTOLehrerPersonaldatenLehramtBefaehigung dto) {
		final LehrerLehrbefaehigungEintrag daten = new LehrerLehrbefaehigungEintrag();
		daten.id = dto.ID;
		daten.idLehramt = dto.Lehreramt_ID;
		daten.idLehrbefaehigung = dto.Lehrbefaehigung_Katalog_ID;
		daten.idAnerkennungsgrund = dto.LehrbefaehigungAnerkennung_Katalog_ID;
		return daten;
	}


	/**
	 * Gibt eine Map mit der Zuordnung der Lehrbefähigungen zu den Lehrämtern mit den übergebenen IDs zurück.
	 *
	 * @param idsLehraemter   die IDs der Lehrämter
	 *
	 * @return die Zuordnung
	 */
	public @NotNull Map<Long, List<LehrerLehrbefaehigungEintrag>> getMapByLehramt(final Collection<Long> idsLehraemter) {
		final var map = lehrbefaehigungenRepository.getMapByLehramt(idsLehraemter);
		return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().map(f -> map(f)).toList()));
	}

}
