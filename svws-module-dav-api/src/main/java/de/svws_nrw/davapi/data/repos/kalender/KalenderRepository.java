package de.svws_nrw.davapi.data.repos.kalender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.kalender.Kalender;
import de.svws_nrw.core.data.kalender.KalenderEintrag;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.dav.DavRessourceCollectionTyp;
import de.svws_nrw.davapi.data.CollectionRessourceQueryParameters;
import de.svws_nrw.davapi.data.IDavRepository;
import de.svws_nrw.davapi.data.IKalenderEintragRepository;
import de.svws_nrw.davapi.data.IKalenderRepository;
import de.svws_nrw.davapi.data.repos.dav.DavException;
import de.svws_nrw.davapi.data.repos.dav.DavRepository;
import de.svws_nrw.davapi.data.repos.dav.DavRessource;
import de.svws_nrw.davapi.data.repos.dav.DavRessourceCollection;
import de.svws_nrw.davapi.data.repos.dav.DavException.ErrorCode;
import de.svws_nrw.davapi.util.KalenderIdUtil;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import jakarta.validation.constraints.NotNull;

/**
 * das DB-Repository für Kalender und Kalendereinträge. Greift auf ein
 * {@link DavRepository} und {@link GenerierteKalenderEintragRepository} bzw
 * {@link GenerierteKalenderRepository} zurück.
 *
 */
public final class KalenderRepository implements IKalenderRepository, IKalenderEintragRepository {

	/** das DavRepository für den Zugriff auf schreibbare Kalender */
	private final IDavRepository davRepository;
	/** das Kalenderrepository für generierte Kalender */
	private final IKalenderRepository generierteKalenderRepository;
	/** das KalenderEintragrepository für generierte Kalendereinträge */
	private final IKalenderEintragRepository generierteKalenderEintraegeRepository;
	/** der angemeldete Benutzer */
	private final Benutzer user;

	/**
	 * Konstruktor für dieses Repository, erstellt eigenes {@link DavRepository},
	 * {@link GenerierteKalenderEintragRepository} und
	 * {@link GenerierteKalenderRepository}
	 *
	 * @param conn der {@link DBEntityManager} auf den zugegriffen werden soll
	 */
	public KalenderRepository(final DBEntityManager conn) {
		this.davRepository = new DavRepository(conn);
		this.user = conn.getUser();
		this.generierteKalenderRepository = new GenerierteKalenderRepository(conn);
		this.generierteKalenderEintraegeRepository = new GenerierteKalenderEintragRepository(conn);
	}

	@Override
	public Optional<Kalender> getKalenderById(final String kalenderId, final CollectionRessourceQueryParameters params) {
		if (!user.pruefeKompetenz(BenutzerKompetenz.KALENDER_ANSEHEN)) {
			return Optional.empty();
		}
		if (user.pruefeKompetenz(BenutzerKompetenz.KALENDER_FUNKTIONSBEZOGEN_ANSEHEN)
				&& KalenderIdUtil.isGenerated(kalenderId)) {
			return generierteKalenderRepository.getKalenderById(kalenderId, params);
		}
		final List<Long> ids = new ArrayList<>();
		ids.add(KalenderIdUtil.parseId(kalenderId));
		final Collection<DavRessourceCollection> davRessourceCollections = davRepository.getDavRessourceCollections(ids);
		if (davRessourceCollections.size() == 1) {
			final Kalender k = mapDavRessourceCollectionToKalender(davRessourceCollections.iterator().next());
			if (params.includeRessources) {
				k.kalenderEintraege.addAll(davRepository.getDavRessources(ids, params).stream()
						.map(this::mapDavRessourceToKalenderEintrag).toList());
			}
			return Optional.of(k);
		}
		return Optional.empty();
	}

	@Override
	public List<Kalender> getAvailableKalender(final CollectionRessourceQueryParameters params) {
		if (!user.pruefeKompetenz(BenutzerKompetenz.KALENDER_ANSEHEN)) {
			return new ArrayList<>();
		}
		createEigenenKalenderIfNotExists();
		final List<Kalender> result = new ArrayList<>();
		result.addAll(generierteKalenderRepository.getAvailableKalender(params));
		final Map<Long, Kalender> kalenderById = davRepository
				.getDavRessourceCollections(DavRessourceCollectionTyp.KALENDER,
						DavRessourceCollectionTyp.EIGENER_KALENDER)
				.stream()
				.collect(Collectors.toMap(collection -> collection.id, this::mapDavRessourceCollectionToKalender));
		if (params.includeRessources) {
			final Collection<DavRessource> davRessources = davRepository.getDavRessources(kalenderById.keySet(), params);
			for (final DavRessource ressource : davRessources) {
				final Kalender k = kalenderById.get(ressource.ressourceCollectionId);
				k.kalenderEintraege.add(mapDavRessourceToKalenderEintrag(ressource));
			}
		}
		result.addAll(kalenderById.values());
		return result;
	}

	/**
	 * erstellt einen eigenen Kalender für den Benutzer bei gegebener Berechtigung
	 */
	private void createEigenenKalenderIfNotExists() {
		if (user.pruefeKompetenz(BenutzerKompetenz.EIGENEN_KALENDER_BEARBEITEN)) {
			davRepository.tryCreateOwnedCollectionIfNotExists(DavRessourceCollectionTyp.EIGENER_KALENDER);
		}
	}

	/**
	 * mappt eine DavRessource zu einem Kalendereintrag
	 *
	 * @param ressource die zu mappende ressource
	 * @return der gemappte Kalendereintrag
	 */
	private @NotNull KalenderEintrag mapDavRessourceToKalenderEintrag(final DavRessource ressource) {
		final KalenderEintrag e = new KalenderEintrag();
		e.kalenderId = String.valueOf(ressource.ressourceCollectionId);
		e.id = String.valueOf(ressource.id);
		e.uid = ressource.uid;
		e.version = String.valueOf(ressource.syncToken);
		e.data = ressource.data;
		e.kalenderStart = ressource.kalenderStart;
		e.kalenderEnde = ressource.kalenderEnde;
		e.kalenderTyp = ressource.kalenderTyp;
		if (ressource.permissions != null) {
			e.darfLesen = ressource.permissions.darfLesen();
			e.darfSchreiben = ressource.permissions.darfSchreiben();
		}
		return e;
	}

	/**
	 * mappt eine gegebene DavRessourceCollection zu einem Kalender-Objekt
	 *
	 * @param collection die gegebene Collection die gemappt werden soll
	 * @return der gemappte Kalender
	 */
	private Kalender mapDavRessourceCollectionToKalender(final DavRessourceCollection collection) {
		final Kalender k = new Kalender();
		k.displayname = collection.anzeigename;
		k.beschreibung = collection.beschreibung;
		k.id = String.valueOf(collection.id);
		k.kalenderTyp = "DavRessource";
		k.synctoken = collection.syncToken;
		k.darfLesen = collection.permissions.darfLesen();
		k.darfSchreiben = collection.permissions.darfSchreiben();
		k.besitzer = collection.besitzer;
		return k;
	}

	@Override
	public Optional<KalenderEintrag> getKalenderEintragByKalenderAndUID(final String kalenderId, final String kalenderEintragUID,
			final CollectionRessourceQueryParameters params) {
		if (!user.pruefeKompetenz(BenutzerKompetenz.KALENDER_ANSEHEN)) {
			return Optional.empty();
		}
		if (user.pruefeKompetenz(BenutzerKompetenz.KALENDER_FUNKTIONSBEZOGEN_ANSEHEN)
				&& KalenderIdUtil.isGenerated(kalenderId)) {
			return generierteKalenderEintraegeRepository.getKalenderEintragByKalenderAndUID(kalenderId,
					kalenderEintragUID, params);
		}
		final List<Long> ids = new ArrayList<>();
		ids.add(KalenderIdUtil.parseId(kalenderId));
		final List<DavRessource> davRessources = davRepository.getDavRessources(ids, params).stream()
				.filter(r -> kalenderEintragUID.equals(r.uid)).toList();
		if (davRessources.size() != 1) {
			return Optional.empty();
		}
		final KalenderEintrag k = mapDavRessourceToKalenderEintrag(davRessources.get(0));
		return Optional.of(k);
	}

	@Override
	public KalenderEintrag saveKalenderEintrag(final KalenderEintrag kalenderEintrag) throws DavException {
		if (KalenderIdUtil.isGenerated(kalenderEintrag.kalenderId)) {
			// das repository verwaltet das speichern auf generierten kalendern
			return generierteKalenderEintraegeRepository.saveKalenderEintrag(kalenderEintrag);
		}
		final DavRessource davRessource = new DavRessource();
		if (kalenderEintrag.kalenderId != null) {
			davRessource.ressourceCollectionId = KalenderIdUtil.parseId(kalenderEintrag.kalenderId);
		}
		davRessource.data = kalenderEintrag.data;
		davRessource.kalenderStart = kalenderEintrag.kalenderStart;
		davRessource.kalenderEnde = kalenderEintrag.kalenderEnde;
		davRessource.kalenderTyp = kalenderEintrag.kalenderTyp;
		davRessource.uid = kalenderEintrag.uid;
		if (kalenderEintrag.version != null && !kalenderEintrag.version.isBlank()) {
			davRessource.syncToken = Long.parseLong(kalenderEintrag.version);
		}
		final Optional<DavRessource> upsertDavRessource = davRepository.upsertDavRessource(davRessource);

		if (upsertDavRessource.isEmpty()) {
			throw new DavException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		return mapDavRessourceToKalenderEintrag(upsertDavRessource.get());
	}

	@Override
	public List<String> getDeletedResourceUIDsSince(final String kalenderId, final Long syncTokenMillis) {
		return this.davRepository.getDeletedResourceUIDsSince(Long.valueOf(kalenderId), syncTokenMillis);
	}
}
