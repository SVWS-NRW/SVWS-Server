package de.svws_nrw.davapi.api;

import de.svws_nrw.davapi.model.dav.Multistatus;
import de.svws_nrw.davapi.model.dav.Prop;
import de.svws_nrw.davapi.model.dav.Propfind;
import de.svws_nrw.davapi.model.dav.Response;
import de.svws_nrw.davapi.model.dav.cal.CalendarHomeSet;
import de.svws_nrw.davapi.model.dav.card.AddressbookHomeSet;
import de.svws_nrw.davapi.util.XmlUnmarshallingUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * Dispatcher-Klasse für die Verarbeitung von Requests auf das DAV-API mittels
 * der HTTP-Methode PROPFIND auf die Ressource Benutzer (Principal).
 */
public final class PropfindPrincipalDispatcher extends DavDispatcher {

	/**
	 * Die URI-Parameter zum Erzeugen von im Responseobjekt enthaltenen URIs
	 */
	private final DavUriParameter uriParameter;

	/**
	 * Erstellt einen Dispatcher mit den gegebenen URI-Parametern
	 *
	 * @param uriParameter die URI-Parameter für diesen Dispatcher
	 */
	public PropfindPrincipalDispatcher(final DavUriParameter uriParameter) {
		this.uriParameter = uriParameter;
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
		final String currentBenutzerId = this.uriParameter.getBenutzerId();
		if (!benutzerId.equals(currentBenutzerId)) {
			// TODO: Fall behandeln, wenn der angemeldete Benutzer die Eigenschaften eines
			// anderen abfragt.
		}

		final DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);

		final Prop prop200 = new Prop();
		if (dynamicPropUtil.getIsFieldRequested(AddressbookHomeSet.class)) {
			final AddressbookHomeSet addressbookHomeSet = new AddressbookHomeSet();
			addressbookHomeSet.getHref().add(DavUriBuilder.getAddressbookCollectionUri(uriParameter));
			prop200.setAddressbookHomeSet(addressbookHomeSet);
		}

		if (dynamicPropUtil.getIsFieldRequested(CalendarHomeSet.class)) {
			final CalendarHomeSet calendarHomeSet = new CalendarHomeSet();
			calendarHomeSet.getHref().add(DavUriBuilder.getCalendarCollectionUri(uriParameter));
			prop200.setCalendarHomeSet(calendarHomeSet);
		}

		final Response response = createResponse(propRequested, prop200);
		response.getHref().add(DavUriBuilder.getPrincipalUri(uriParameter));
		return response;
	}

}
