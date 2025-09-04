package de.svws_nrw.davapi.api;

import de.svws_nrw.davapi.model.dav.Multistatus;
import de.svws_nrw.davapi.model.dav.Prop;
import de.svws_nrw.davapi.model.dav.Propfind;
import de.svws_nrw.davapi.model.dav.Response;
import de.svws_nrw.davapi.model.dav.cal.CalendarHomeSet;
import de.svws_nrw.davapi.model.dav.card.AddressbookHomeSet;
import de.svws_nrw.davapi.util.XmlUnmarshallingUtil;
import de.svws_nrw.db.DBEntityManager;
import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * Dispatcher-Klasse für die Verarbeitung von Requests auf das DAV-API mittels
 * der HTTP-Methode PROPFIND auf die Ressource Benutzer (Principal).
 */
public final class PropfindPrincipalDispatcher extends DavRequestManager {

	/**
	 * Konstruktor für einen neuen Dispatcher und den gegebenen UriParametern
	 *
	 * @param conn         die Datenbankverbindung
	 */
	public PropfindPrincipalDispatcher(final @NotNull DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Verarbeitet die XML-Anfrage aus einem InputStream für eine gegebene
	 * Principal-Ressource
	 *
	 * @param inputStream der InputStream, welcher das XML-Request enthält
	 * @param ressourceId die RessourceID des Principals
	 * @return die entsprechend des Requests verarbeitete Datenstruktur, welche das
	 *         Antwort-XML repräsentiert,
	 * @throws IOException bei der Verarbeitung des InputStreams/XML-Unmarshalling
	 */
	public Object dispatch(final InputStream inputStream, final String ressourceId) throws IOException {
		final Propfind propfind = XmlUnmarshallingUtil.unmarshal(inputStream, Propfind.class);
		// TODO: Ergänzen einer Prüfung, ob Schema und Benutzer gefunden werden
		final Multistatus ms = new Multistatus();
		ms.getResponse().add(this.generateResponsePrincipalLevel(ressourceId, propfind.getProp()));
		return ms;
	}

	/**
	 * Generiert ein Response-Objekt für einen angegebenen Benutzer (Principal).
	 *
	 * @param benutzerId    Id des Benutzers (Principals), zu dem Informationen
	 *                      zurückgeliefert werden sollen
	 * @param propRequested Prop aus dem Request. Definiert, welche Informationen
	 *                      zur Ressource zurückgeliefert werden sollen.
	 * @return Response-Objekt zur angegebenen Benutzer
	 */
	private Response generateResponsePrincipalLevel(final String benutzerId, final Prop propRequested) {
		final String currentBenutzerId = this.getParameterBenutzerId();
		if (!benutzerId.equals(currentBenutzerId)) {
			// TODO: Fall behandeln, wenn der angemeldete Benutzer die Eigenschaften eines
			// anderen abfragt.
		}

		final DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);

		final Prop prop200 = new Prop();
		if (dynamicPropUtil.getIsFieldRequested(AddressbookHomeSet.class)) {
			final AddressbookHomeSet addressbookHomeSet = new AddressbookHomeSet();
			addressbookHomeSet.getHref().add(getCardDavUri());
			prop200.setAddressbookHomeSet(addressbookHomeSet);
		}

		if (dynamicPropUtil.getIsFieldRequested(CalendarHomeSet.class)) {
			final CalendarHomeSet calendarHomeSet = new CalendarHomeSet();
			calendarHomeSet.getHref().add(getKalenderUri());
			prop200.setCalendarHomeSet(calendarHomeSet);
		}

		return createResponse(propRequested, prop200, getBenutzerUri());
	}

}
