package de.svws_nrw.davapi.data.caldav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.core.data.kalender.Kalender;
import de.svws_nrw.core.data.kalender.KalenderEintrag;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.davapi.data.dav.DavCollection;
import de.svws_nrw.davapi.data.dav.DavDBRepository;
import de.svws_nrw.davapi.data.dav.DavException;
import de.svws_nrw.davapi.data.dav.DavRessource;
import de.svws_nrw.db.DBEntityManager;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse dient dem Zugriff auf die Datenbank, um einen Eigenen Kalender zu verwalten.
 */
public final class DataCalDavEigenerKalender extends DataManagerCalDav {

	/** Das DavRepository für den Zugriff auf den schreibbaren Kalender */
	private final DavDBRepository davRepository;

	/**
	 * Erzeugt eine neue Data-Klasse für den Zugriff auf den Eigenen Kalender
	 *
	 * @param conn   die Datenbank-Verbindung
	 */
	public DataCalDavEigenerKalender(final DBEntityManager conn) {
		super(conn, conn.getUser().schuleGetSchuljahresabschnitt().id, CalDavKalenderTyp.PERSOENLICH);
		this.davRepository = new DavDBRepository(conn);
	}

	@Override
	public Kalender getKalender(final String idCal) {
		if (!conn.getUser().pruefeKompetenz(BenutzerKompetenz.CALDAV_EIGENER_KALENDER))
			return null;

		// Bestimme zunächst die Datenbank-ID der Kalender-Collection
		final Long idCollection = CalDavKalenderTyp.PERSOENLICH.getDbId(idCal);
		if (idCollection == null)
			return null;

		// Bestimme die Collection
		final DavCollection collection = davRepository.getCollectionByID(idCollection);
		if (collection == null)
			return null;

		return mapCollectionToKalender(collection);
	}


	@Override
	public @NotNull List<KalenderEintrag> getEintraege(final String idCal, final boolean withPayload) {
		if (!conn.getUser().pruefeKompetenz(BenutzerKompetenz.CALDAV_EIGENER_KALENDER))
			return Collections.emptyList();

		final Long idCollection = CalDavKalenderTyp.PERSOENLICH.getDbId(idCal);
		if (idCollection == null)
			return Collections.emptyList();

		final List<KalenderEintrag> result = new ArrayList<>();
		result.addAll(davRepository.getRessources(List.of(idCollection), withPayload).stream()
				.map(r -> mapRessourceToEintrag(typ, r)).toList());
		return result;
	}


	@Override
	public String persistEintrag(final @NotNull KalenderEintrag eintrag) {
		if (!conn.getUser().pruefeKompetenz(BenutzerKompetenz.CALDAV_EIGENER_KALENDER))
			return null;

		final String idCal = eintrag.kalenderId;
		final CalDavKalenderTyp typ = CalDavKalenderTyp.getByID(idCal);
		if (typ != CalDavKalenderTyp.PERSOENLICH)
			return null;
		final Long idCollection = CalDavKalenderTyp.PERSOENLICH.getDbId(idCal);

		final DavRessource davRessource = new DavRessource();
		davRessource.idCollection = idCollection;
		davRessource.data = eintrag.data;
		davRessource.kalenderStart = eintrag.kalenderStart;
		davRessource.kalenderEnde = eintrag.kalenderEnde;
		davRessource.kalenderTyp = eintrag.kalenderTyp;
		davRessource.uid = eintrag.uid;
		if ((eintrag.version != null) && !eintrag.version.isBlank())
			davRessource.syncToken = Long.parseLong(eintrag.version);
		final DavRessource result = davRepository.insertOrUpdateRessource(davRessource);
		return String.valueOf(result.syncToken);
	}


	@Override
	public @NotNull List<String> getDeletedEintragUIDs(final @NotNull String idCal, final long syncToken) throws DavException {
		if (!conn.getUser().pruefeKompetenz(BenutzerKompetenz.CALDAV_EIGENER_KALENDER))
			throw new DavException(Status.NOT_FOUND);
		final Long idCollection = CalDavKalenderTyp.PERSOENLICH.getDbId(idCal);
		if (idCollection == null)
			throw new DavException(Status.NOT_FOUND);
		return this.davRepository.getDeletedResourceUIDsSince(idCollection, syncToken);
	}


	@Override
	public boolean deleteEintrag(final @NotNull String idCal, final String uid, final Long syncToken) throws DavException {
		if (!conn.getUser().pruefeKompetenz(BenutzerKompetenz.CALDAV_EIGENER_KALENDER))
			throw new DavException(Status.NOT_FOUND);
		final Long idCollection = CalDavKalenderTyp.PERSOENLICH.getDbId(idCal);
		if (idCollection == null)
			throw new DavException(Status.NOT_FOUND);
		return this.davRepository.deleteRessource(idCollection, uid, syncToken);
	}

}
