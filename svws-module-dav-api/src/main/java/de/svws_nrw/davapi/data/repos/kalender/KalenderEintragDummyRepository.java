package de.svws_nrw.davapi.data.repos.kalender;

import java.util.Optional;
import java.util.UUID;

import de.svws_nrw.core.data.kalender.KalenderEintrag;
import de.svws_nrw.davapi.data.CollectionRessourceQueryParameters;
import de.svws_nrw.davapi.data.IKalenderEintragRepository;

/**
 * Dummy Repository für Prototypische Implementierung des
 * {@link IKalenderEintragRepository}
 *
 */
public final class KalenderEintragDummyRepository implements IKalenderEintragRepository {

	/**
	 * Konstruktor zum Erstellen des Repositories
	 *
	 */
	public KalenderEintragDummyRepository() {
		//empty default constructor
	}

	@Override
	public Optional<KalenderEintrag> getKalenderEintragByKalenderAndUID(final String kalenderId, final String kalenderEintragId,
			final CollectionRessourceQueryParameters params) {
		final KalenderEintrag eintrag123 = new KalenderEintrag();
		eintrag123.id = "123";
		eintrag123.kalenderId = "dummy";
		eintrag123.version = UUID.randomUUID().toString();

		final KalenderEintrag eintrag456 = new KalenderEintrag();
		eintrag456.id = "456";
		eintrag456.kalenderId = "dummy";
		eintrag456.version = UUID.randomUUID().toString();

		return Optional.of(eintrag123);
	}

	@Override
	public KalenderEintrag saveKalenderEintrag(final KalenderEintrag kalenderEintrag) {
		return null;
	}
}
