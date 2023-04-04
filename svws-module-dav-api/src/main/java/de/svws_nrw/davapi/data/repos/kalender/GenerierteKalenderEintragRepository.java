package de.svws_nrw.davapi.data.repos.kalender;

import java.util.Optional;

import de.svws_nrw.core.data.kalender.KalenderEintrag;
import de.svws_nrw.davapi.data.CollectionRessourceQueryParameters;
import de.svws_nrw.davapi.data.IKalenderEintragRepository;
import de.svws_nrw.db.DBEntityManager;

/**
 * Implementierung des {@link IKalenderEintragRepository} für generierte
 * Kalendereinträge
 */
public final class GenerierteKalenderEintragRepository implements IKalenderEintragRepository {

	/** der {@link DBEntityManager} auf den zugegriffen werden soll. */
	private final DBEntityManager conn;

	/**
	 * Konstruktor mit zu nutzendem {@link DBEntityManager}
	 *
	 * @param conn der zu nutzende {@link DBEntityManager}
	 */
	public GenerierteKalenderEintragRepository(final DBEntityManager conn) {
		this.conn = conn;
	}

	@Override
	public Optional<KalenderEintrag> getKalenderEintragByKalenderAndUID(final String kalenderId, final String kalenderEintragUID,
			final CollectionRessourceQueryParameters params) {
		return Optional.empty();
	}

	@Override
	public KalenderEintrag saveKalenderEintrag(final KalenderEintrag kalenderEintrag) {
		throw new UnsupportedOperationException(
				"Speichern von Kalendereinträgen für generierte Kalender nicht möglich.");
	}
}
