package de.nrw.schule.svws.davapi.data.repos.kalender;

import java.util.Optional;
import java.util.UUID;

import de.nrw.schule.svws.core.data.kalender.KalenderEintrag;
import de.nrw.schule.svws.davapi.data.CollectionRessourceQueryParameters;
import de.nrw.schule.svws.davapi.data.IKalenderEintragRepository;

/**
 * Dummy Repository für Prototypische Implementierung des
 * {@link IKalenderEintragRepository}
 *
 */
public class KalenderEintragDummyRepository implements IKalenderEintragRepository {

	/**
	 * Konstruktor zum Erstellen des Repositories
	 *
	 */
	public KalenderEintragDummyRepository() {
		//empty default constructor
	}

	@Override
	public Optional<KalenderEintrag> getKalenderEintragByKalenderAndUID(String kalenderId, String kalenderEintragId,
			CollectionRessourceQueryParameters params) {
		KalenderEintrag eintrag123 = new KalenderEintrag();
		eintrag123.id = "123";
		eintrag123.kalenderId = "dummy";
		eintrag123.version = UUID.randomUUID().toString();

		KalenderEintrag eintrag456 = new KalenderEintrag();
		eintrag456.id = "456";
		eintrag456.kalenderId = "dummy";
		eintrag456.version = UUID.randomUUID().toString();

		return Optional.of(eintrag123);
	}

	@Override
	public KalenderEintrag saveKalenderEintrag(KalenderEintrag kalenderEintrag) {
		return null;
	}
}
