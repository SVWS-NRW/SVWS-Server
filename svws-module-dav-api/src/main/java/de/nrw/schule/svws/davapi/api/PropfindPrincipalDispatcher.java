package de.nrw.schule.svws.davapi.api;

import de.nrw.schule.svws.davapi.model.dav.Multistatus;
import de.nrw.schule.svws.davapi.model.dav.Prop;
import de.nrw.schule.svws.davapi.model.dav.Propfind;
import de.nrw.schule.svws.davapi.model.dav.Response;
import de.nrw.schule.svws.davapi.model.dav.cal.CalendarHomeSet;
import de.nrw.schule.svws.davapi.model.dav.card.AddressbookHomeSet;
import de.nrw.schule.svws.davapi.util.XmlUnmarshallingUtil;

import jakarta.servlet.ServletInputStream;

import java.io.IOException;

/**
 * Dispatcher-Klasse für die Verarbeitung von Requests auf das DAV-API mittels
 * der HTTP-Methode PROPFIND auf die Ressource Benutzer (Principal).
 */
public class PropfindPrincipalDispatcher extends DavDispatcher {

	/**
	 * Die URI-Parameter zum Erzeugen von im Responseobjekt enthaltenen URIs
	 */
	private DavUriParameter uriParameter;

	/**
	 * Erstellt einen Dispatcher mit den gegebenen URI-Parametern
	 *
	 * @param uriParameter die URI-Parameter für diesen Dispatcher
	 */
	public PropfindPrincipalDispatcher(DavUriParameter uriParameter) {
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
	public Object dispatch(ServletInputStream inputStream, String ressourceId) throws IOException {
		Propfind propfind = XmlUnmarshallingUtil.unmarshal(inputStream, Propfind.class);
		// TODO: Ergänzen einer Prüfung, ob Schema und Benutzer gefunden werden
		Multistatus ms = new Multistatus();
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
	private Response generateResponsePrincipalLevel(String benutzerId, Prop propRequested) {
		String currentBenutzerId = this.uriParameter.getBenutzerId();
		if (!benutzerId.equals(currentBenutzerId)) {
			// TODO: Fall behandeln, wenn der angemeldete Benutzer die Eigenschaften eines
			// anderen abfragt.
		}

		DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);

		Prop prop200 = new Prop();
		if (dynamicPropUtil.getIsFieldRequested(AddressbookHomeSet.class)) {
			AddressbookHomeSet addressbookHomeSet = new AddressbookHomeSet();
			addressbookHomeSet.getHref().add(DavUriBuilder.getAddressbookCollectionUri(uriParameter));
			prop200.setAddressbookHomeSet(addressbookHomeSet);
		}

		if (dynamicPropUtil.getIsFieldRequested(CalendarHomeSet.class)) {
			CalendarHomeSet calendarHomeSet = new CalendarHomeSet();
			calendarHomeSet.getHref().add(DavUriBuilder.getCalendarCollectionUri(uriParameter));
			prop200.setCalendarHomeSet(calendarHomeSet);
		}

		Response response = createResponse(propRequested, prop200);
		response.getHref().add(DavUriBuilder.getPrincipalUri(uriParameter));
		return response;
	}

}