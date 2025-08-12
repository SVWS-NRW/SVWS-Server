package de.svws_nrw.davapi.api;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import de.svws_nrw.core.data.kalender.KalenderEintrag;
import de.svws_nrw.davapi.data.caldav.IKalenderEintragRepository;
import de.svws_nrw.davapi.data.dav.DavException;
import de.svws_nrw.davapi.model.dav.Error;
import de.svws_nrw.davapi.util.icalendar.DateTimeUtil;
import de.svws_nrw.davapi.util.icalendar.VCalendar;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.Response.Status;

/**
 * Dispatcher-Klasse für die Verarbeitung von Requests auf das DAV-API mittels
 * der HTTP-Methode REPORT auf die Ressource Kalender.
 */
public class PutCalendarDispatcher extends DavDispatcher {

	/**
	 * Repository-Klasse zur Abfrage von Kalendereinträgen aus der SVWS-Datenbank
	 */
	private final IKalenderEintragRepository kalenderEintragRepository;

	/**
	 * Erstellt einen neuen Dispatcher mit den angegebenen Repositorys und
	 * URI-Parametern
	 *
	 * @param kalenderEintragRepository   das Repository für Kalendereinträge
	 */
	public PutCalendarDispatcher(final IKalenderEintragRepository kalenderEintragRepository) {
		this.kalenderEintragRepository = kalenderEintragRepository;
	}

	/**
	 * Aktualisiert einen vorhandenen Kalendereintrag in der gegebenen
	 * Ressourcensammlung mit der gegebenen RessourcenUID, sofern das gegebene eTag
	 * (die Version des Clients) der Version des Servers entspricht.
	 *
	 * @param inputStream           der InputStream mit dem VCalendar payload
	 * @param ressourceCollectionId die ID des Kalenders
	 * @param ressourceUID          die UID des Kalendereintrags
	 * @param eTag                  eTag des Clients, also die Versionsnummer, die
	 *                              dem Client zuletzt bekannt war
	 * @return das aktuellste eTag des Servers nach aktualisieren (also die
	 *         Versionsnummer) oder ein {@link Error}-Objekt im Fehlerfall
	 */
	public Object dispatchUpdate(final InputStream inputStream, final String ressourceCollectionId, final String ressourceUID,
			final String eTag) {
		return dispatchCreateOrUpdate(inputStream, ressourceCollectionId, ressourceUID, Optional.of(eTag));
	}

	/**
	 * Erstellt einen neuen Kalendereintrag in der gegebenen Ressourcensammlung mit
	 * der gegebenen Ressourcen-UID.
	 *
	 * @param inputStream           der InputStream mit dem VCalendar payload
	 * @param ressourceCollectionId die ID des Kalenders
	 * @param ressourceUID          die UID des Kalendereintrags dem Client zuletzt
	 *                              bekannt war
	 * @return das aktuellste eTag des Servers nach Erstellen (also die
	 *         Versionsnummer) oder ein {@link Error}-Objekt im Fehlerfall
	 */
	public Object dispatchCreate(final InputStream inputStream, final String ressourceCollectionId, final String ressourceUID) {
		return dispatchCreateOrUpdate(inputStream, ressourceCollectionId, ressourceUID, Optional.empty());
	}

	/**
	 * Erstellt einen neuen Kalendereintrag oder aktualisiert einen vorhandenen.
	 * Zwischen erstellen und aktualisieren wird anhand des gegebenen eTags
	 * entschieden, ein leeres {@link Optional} versucht einen neuen Eintrag mit der
	 * gegebenen UID zu erstellen, ist das Optional mit einem eTag gefüllt, wird der
	 * vorhandene Kalendereintrag bei gleicher UID aktualisiert, sofern der Client
	 * die aktuellste Version hatte.
	 *
	 * @param inputStream           der InputStream mit dem VCalendar payload
	 * @param ressourceCollectionId die ID des Kalenders
	 * @param ressourceUID          die UID des Kalendereintrags
	 * @param eTag                  eTag des Clients, also die Versionsnummer, die
	 *                              dem Client zuletzt bekannt war
	 * @return das aktuellste eTag des Servers nach aktualisieren (also die
	 *         Versionsnummer) oder ein {@link Error}-Objekt im Fehlerfall
	 */
	private Object dispatchCreateOrUpdate(final InputStream inputStream, final String ressourceCollectionId, final String ressourceUID,
			final Optional<String> eTag) {
		// iCalender Payload aus dem Request auslesen
		// request content ist kein xml String, sondern direkt das .ics
		this.setParameterResourceCollectionId(ressourceCollectionId);
		this.setParameterResourceId(ressourceUID);
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final byte[] buffer = new byte[2048];
		try {
			for (int length; (length = inputStream.read(buffer)) != -1;) {
				bos.write(buffer, 0, length);
			}
			final String vCalPayload = bos.toString(StandardCharsets.UTF_8);
			final VCalendar vCalendar = VCalendar.parse(vCalPayload);
			final KalenderEintrag updatedKalendereintrag = new KalenderEintrag();
			updatedKalendereintrag.data = vCalPayload;
			updatedKalendereintrag.kalenderId = ressourceCollectionId;
			updatedKalendereintrag.uid = ressourceUID;

			updatedKalendereintrag.kalenderEnde = DateTimeUtil.toSQLTimeStamp(vCalendar.getMaxDTEnd());
			updatedKalendereintrag.kalenderStart = DateTimeUtil.toSQLTimeStamp(vCalendar.getMinDTStart());
			updatedKalendereintrag.kalenderTyp = vCalendar.getTyp().name();
			if (eTag.isPresent() && !eTag.get().isBlank()) {
				updatedKalendereintrag.version = adjustETags(eTag.get());
			}
			final KalenderEintrag saveKalenderEintrag = this.kalenderEintragRepository
					.saveKalenderEintrag(updatedKalendereintrag);
			return new EntityTag(saveKalenderEintrag.version);
		} catch (final Exception e) {
			if (e instanceof final DavException eDav)
				return eDav.getDavResponse(getKalenderResourceUri()).getError();
			return new DavException(Status.INTERNAL_SERVER_ERROR).getDavResponse(getKalenderResourceUri()).getError();
		}
	}
}
