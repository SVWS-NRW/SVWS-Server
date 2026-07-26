package de.svws_nrw.service.gost.klausuren;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumstunde;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenRaumstunden;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenRaumstundeRepository;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf GOSt-Klausurraumstunden.
 */
public final class GostKlausurenRaumstundeService {

	private final GostKlausurenRaumstundeRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository das Repository für GOSt-Klausurraumstunden
	 */
	public GostKlausurenRaumstundeService(final GostKlausurenRaumstundeRepository repository) {
		this.repository = repository;
	}

	/**
	 * Wandelt ein Datenbank-DTO in ein API-DTO um.
	 *
	 * @param dto das Datenbank-DTO
	 *
	 * @return das API-DTO
	 */
	public static GostKlausurraumstunde toApi(final DTOGostKlausurenRaumstunden dto) {
		final GostKlausurraumstunde daten = new GostKlausurraumstunde();
		daten.id = dto.ID;
		daten.idRaum = dto.Klausurraum_ID;
		daten.idZeitraster = dto.Zeitraster_ID;
		return daten;
	}

	/**
	 * Ermittelt eine Klausurraumstunde anhand der ID.
	 *
	 * @param id die ID
	 *
	 * @return die Klausurraumstunde
	 */
	public GostKlausurraumstunde get(final long id) {
		return toApi(repository.getById(id));
	}

	/**
	 * Ermittelt Klausurraumstunden zu den angegebenen IDs.
	 *
	 * @param ids die IDs
	 *
	 * @return die Klausurraumstunden
	 */
	public List<GostKlausurraumstunde> getListByIds(final Collection<Long> ids) {
		return repository.findListByIds(ids).stream().map(GostKlausurenRaumstundeService::toApi).toList();
	}

	/**
	 * Ermittelt Klausurraumstunden zu den angegebenen Klausurräumen.
	 *
	 * @param raumIds die IDs der Klausurräume
	 *
	 * @return die Klausurraumstunden
	 */
	public List<GostKlausurraumstunde> getListByRaumIds(final Collection<Long> raumIds) {
		return repository.getListByRaumIds(raumIds).stream().map(GostKlausurenRaumstundeService::toApi).toList();
	}

	/**
	 * Löscht verwaiste Klausurraumstunden.
	 *
	 * @return die gelöschten Klausurraumstunden
	 */
	public List<GostKlausurraumstunde> deleteUnreferenced() {
		final List<DTOGostKlausurenRaumstunden> dtos = repository.getUnreferenced();
		repository.delete(dtos);
		repository.flush();
		return dtos.stream().map(GostKlausurenRaumstundeService::toApi).toList();
	}

	/**
	 * Löscht eine Klausurraumstunde.
	 *
	 * @param id die ID
	 *
	 * @return die gelöschte Klausurraumstunde
	 */
	public GostKlausurraumstunde delete(final long id) {
		return transactional(() -> {
			final DTOGostKlausurenRaumstunden dto = repository.getById(id);
			final GostKlausurraumstunde result = toApi(dto);
			repository.delete(dto);
			repository.flush();
			return result;
		});
	}

}
