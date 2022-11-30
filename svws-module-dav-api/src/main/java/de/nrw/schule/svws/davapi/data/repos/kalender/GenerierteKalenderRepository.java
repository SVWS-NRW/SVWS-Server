package de.nrw.schule.svws.davapi.data.repos.kalender;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.nrw.schule.svws.core.data.kalender.Kalender;
import de.nrw.schule.svws.davapi.data.CollectionRessourceQueryParameters;
import de.nrw.schule.svws.davapi.data.IKalenderRepository;
import de.nrw.schule.svws.db.DBEntityManager;

/**
 * Diese Klasse stellt die Implementierung des {@link IKalenderRepository} für
 * generierte Kalender dar
 */
public class GenerierteKalenderRepository implements IKalenderRepository {

	/** der {@link DBEntityManager} auf den zugegriffen werden soll. */
	private DBEntityManager conn;

	/**
	 * Konstruktor mit {@link DBEntityManager}
	 * 
	 * @param conn der genutzte {@link DBEntityManager}
	 */
	public GenerierteKalenderRepository(DBEntityManager conn) {
		this.conn = conn;
	}

	@Override
	public Optional<Kalender> getKalenderById(String kalenderId, CollectionRessourceQueryParameters params) {
		return Optional.empty();
	}

	@Override
	public List<Kalender> getAvailableKalender(CollectionRessourceQueryParameters params) {
		return new ArrayList<>();
	}

	@Override
	public List<String> getDeletedResourceUIDsSince(String kalenderId, Long syncTokenMillis) {
		return new ArrayList<>();
	}

}
