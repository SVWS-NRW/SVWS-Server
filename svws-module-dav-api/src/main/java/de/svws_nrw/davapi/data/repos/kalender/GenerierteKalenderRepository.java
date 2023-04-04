package de.svws_nrw.davapi.data.repos.kalender;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.svws_nrw.core.data.kalender.Kalender;
import de.svws_nrw.davapi.data.CollectionRessourceQueryParameters;
import de.svws_nrw.davapi.data.IKalenderRepository;
import de.svws_nrw.db.DBEntityManager;

/**
 * Diese Klasse stellt die Implementierung des {@link IKalenderRepository} für
 * generierte Kalender dar
 */
public final class GenerierteKalenderRepository implements IKalenderRepository {

	/** der {@link DBEntityManager} auf den zugegriffen werden soll. */
	private final DBEntityManager conn;

	/**
	 * Konstruktor mit {@link DBEntityManager}
	 *
	 * @param conn der genutzte {@link DBEntityManager}
	 */
	public GenerierteKalenderRepository(final DBEntityManager conn) {
		this.conn = conn;
	}

	@Override
	public Optional<Kalender> getKalenderById(final String kalenderId, final CollectionRessourceQueryParameters params) {
		return Optional.empty();
	}

	@Override
	public List<Kalender> getAvailableKalender(final CollectionRessourceQueryParameters params) {
		return new ArrayList<>();
	}

	@Override
	public List<String> getDeletedResourceUIDsSince(final String kalenderId, final Long syncTokenMillis) {
		return new ArrayList<>();
	}

}
