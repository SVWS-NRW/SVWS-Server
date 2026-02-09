package de.svws_nrw.data.lehrer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtFachrichtung;
import de.svws_nrw.repo.lehrer.LehrerPersonaldatenLehramtFachrichtungRepository;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Fachrichtungen von Lehrern
 */
public final class LehrerFachrichtungService {

	/** Das Repository für den Zugriff auf die Fachrichtung zu den Lehrämtern */
	private final LehrerPersonaldatenLehramtFachrichtungRepository fachrichtungenRepository;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param fachrichtungenRepository   das Repository für den Datenbank-Zugriff auf die DB-DTOs der Fachrichtungen
	 */
	public LehrerFachrichtungService(final LehrerPersonaldatenLehramtFachrichtungRepository fachrichtungenRepository) {
		this.fachrichtungenRepository = fachrichtungenRepository;
	}


	private static LehrerFachrichtungEintrag map(final DTOLehrerPersonaldatenLehramtFachrichtung dto) {
		final LehrerFachrichtungEintrag daten = new LehrerFachrichtungEintrag();
		daten.id = dto.ID;
		daten.idLehramt = dto.Lehreramt_ID;
		daten.idFachrichtung = dto.Fachrichtung_Katalog_ID;
		daten.idAnerkennungsgrund = dto.FachrichtungAnerkennung_Katalog_ID;
		return daten;
	}


	/**
	 * Gibt eine Map mit der Zuordnung der Fachrichtungen zu den Lehrämtern mit den übergebenen IDs zurück.
	 *
	 * @param idsLehraemter   die IDs der Lehrämter
	 *
	 * @return die Zuordnung
	 */
	public @NotNull Map<Long, List<LehrerFachrichtungEintrag>> getMapByLehramt(final Collection<Long> idsLehraemter) {
		final var map = fachrichtungenRepository.getMapByLehramt(idsLehraemter);
		return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().map(f -> map(f)).toList()));
	}

}
