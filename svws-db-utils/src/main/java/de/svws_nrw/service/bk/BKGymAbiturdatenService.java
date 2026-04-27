 package de.svws_nrw.service.bk;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.bk.abi.BKGymAbiturdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;


/**
 * Ein Service für den Zugriff auf die Anrechnungsstunden bei Lehrern
 */
public final class BKGymAbiturdatenService {

	private final BKGymAbiturdatenServiceKontext kontext;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param kontext   der Daten-Kontext für diesen Service
	 */
	public BKGymAbiturdatenService(final BKGymAbiturdatenServiceKontext kontext) {
		this.kontext = kontext;
	}


	/**
	 * Ermittelt die Abiturdaten für einen Schüler des Beruflichen Gymnasiums mit der übergebenen ID.
	 *
	 * @param id   die ID des Schülers, für den die Abiturdaten ermittelt werden
	 *
	 * @return die Abiturdaten für den Schüler mit der übergebenen ID
	 */
	public BKGymAbiturdaten get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Abiturdaten für den Schüler mit der ID %d gefunden.".formatted(id));
		}
		return list.get(0);
	}


	/**
	 * Ermittelt die Abiturdaten für Schüler des Beruflichen Gymnasiums mit den übergebenen IDs.
	 *
	 * @param ids   die IDs der Schüler, für die die Abiturdaten ermittelt werden sollen
	 *
	 * @return die Abiturdaten für die Schüler mit den übergebenen IDs
	 */
	public List<BKGymAbiturdaten> getList(final Collection<Long> ids) {
		return kontext.getAbiturdaten(ids);
	}

}
