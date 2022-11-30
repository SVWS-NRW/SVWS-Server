package de.nrw.schule.svws.davapi.data.repos.kalender;

import java.util.Optional;

import de.nrw.schule.svws.core.data.kalender.KalenderEintrag;
import de.nrw.schule.svws.davapi.data.CollectionRessourceQueryParameters;
import de.nrw.schule.svws.davapi.data.IKalenderEintragRepository;
import de.nrw.schule.svws.db.DBEntityManager;

/**
 * Implementierung des {@link IKalenderEintragRepository} für generierte
 * Kalendereinträge
 */
public class GenerierteKalenderEintragRepository implements IKalenderEintragRepository {
	/** der {@link DBEntityManager} auf den zugegriffen werden soll. */
	private DBEntityManager conn;

	/**
	 * Konstruktor mit zu nutzendem {@link DBEntityManager}
	 * 
	 * @param conn der zu nutzende {@link DBEntityManager}
	 */
	public GenerierteKalenderEintragRepository(DBEntityManager conn) {
		this.conn = conn;
	}

	@Override
	public Optional<KalenderEintrag> getKalenderEintragByKalenderAndUID(String kalenderId, String kalenderEintragUID,
			CollectionRessourceQueryParameters params) {
		return Optional.empty();
	}

	@Override
	public KalenderEintrag saveKalenderEintrag(KalenderEintrag kalenderEintrag) {
		throw new UnsupportedOperationException(
				"Speichern von Kalendereinträgen für generierte Kalender nicht möglich.");
	}
}
