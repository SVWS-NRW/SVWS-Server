package de.svws_nrw.service.gost.klausuren;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurterminraumstunde;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermineRaumstunden;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenSchuelerklausurterminraumstundeRepository;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für den Zugriff auf GOSt-Schülerklausurtermin-Raumstunden-Zuordnungen.
 */
public final class GostKlausurenSchuelerklausurterminraumstundeService {

	private final GostKlausurenSchuelerklausurterminraumstundeRepository repository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository das Repository für GOSt-Schülerklausurtermin-Raumstunden-Zuordnungen
	 */
	public GostKlausurenSchuelerklausurterminraumstundeService(final GostKlausurenSchuelerklausurterminraumstundeRepository repository) {
		this.repository = repository;
	}

	/**
	 * Wandelt ein Datenbank-DTO in ein API-DTO um.
	 *
	 * @param dto das Datenbank-DTO
	 *
	 * @return das API-DTO
	 */
	public static GostSchuelerklausurterminraumstunde toApi(final DTOGostKlausurenSchuelerklausurenTermineRaumstunden dto) {
		final GostSchuelerklausurterminraumstunde daten = new GostSchuelerklausurterminraumstunde();
		daten.idSchuelerklausurtermin = dto.Schuelerklausurtermin_ID;
		daten.idRaumstunde = dto.Raumstunde_ID;
		return daten;
	}

	/**
	 * Ermittelt eine Zuordnung anhand des zusammengesetzten Schlüssels.
	 *
	 * @param idSchuelerklausurtermin die ID des Schülerklausurtermins
	 * @param idRaumstunde die ID der Klausurraumstunde
	 *
	 * @return die Zuordnung
	 */
	public GostSchuelerklausurterminraumstunde get(final long idSchuelerklausurtermin, final long idRaumstunde) {
		return toApi(repository.getById(new DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK(idSchuelerklausurtermin, idRaumstunde)));
	}

	/**
	 * Ermittelt Zuordnungen zu den angegebenen Schülerklausurterminen.
	 *
	 * @param ids die IDs der Schülerklausurtermine
	 *
	 * @return die Zuordnungen
	 */
	public List<GostSchuelerklausurterminraumstunde> getListBySchuelerklausurterminIds(final Collection<Long> ids) {
		return repository.getListBySchuelerklausurterminIds(ids).stream()
				.map(GostKlausurenSchuelerklausurterminraumstundeService::toApi).toList();
	}

	/**
	 * Ermittelt Zuordnungen zu den angegebenen Klausurraumstunden.
	 *
	 * @param ids die IDs der Klausurraumstunden
	 *
	 * @return die Zuordnungen
	 */
	public List<GostSchuelerklausurterminraumstunde> getListByRaumstundeIds(final Collection<Long> ids) {
		return repository.getListByRaumstundeIds(ids).stream()
				.map(GostKlausurenSchuelerklausurterminraumstundeService::toApi).toList();
	}

	/**
	 * Löscht Zuordnungen zu den angegebenen Schülerklausurterminen.
	 *
	 * @param ids die IDs der Schülerklausurtermine
	 *
	 * @return die gelöschten Zuordnungen
	 */
	public List<GostSchuelerklausurterminraumstunde> deleteBySchuelerklausurterminIds(final Collection<Long> ids) {
		final List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> dtos = repository.getListBySchuelerklausurterminIds(ids);
		repository.delete(dtos);
		repository.flush();
		return dtos.stream().map(GostKlausurenSchuelerklausurterminraumstundeService::toApi).toList();
	}

	/**
	 * Löscht eine Zuordnung.
	 *
	 * @param idSchuelerklausurtermin die ID des Schülerklausurtermins
	 * @param idRaumstunde die ID der Klausurraumstunde
	 *
	 * @return die gelöschte Zuordnung
	 */
	public GostSchuelerklausurterminraumstunde delete(final long idSchuelerklausurtermin, final long idRaumstunde) {
		return transactional(() -> {
			final DTOGostKlausurenSchuelerklausurenTermineRaumstunden dto =
					repository.getById(new DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK(idSchuelerklausurtermin, idRaumstunde));
			final GostSchuelerklausurterminraumstunde result = toApi(dto);
			repository.delete(dto);
			repository.flush();
			return result;
		});
	}

}
