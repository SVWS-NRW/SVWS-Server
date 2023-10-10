package de.svws_nrw.davapi.api;

import java.io.IOException;
import java.io.InputStream;

import de.svws_nrw.davapi.model.dav.Collection;
import de.svws_nrw.davapi.model.dav.CurrentUserPrincipal;
import de.svws_nrw.davapi.model.dav.CurrentUserPrivilegeSet;
import de.svws_nrw.davapi.model.dav.Multistatus;
import de.svws_nrw.davapi.model.dav.Principal;
import de.svws_nrw.davapi.model.dav.Prop;
import de.svws_nrw.davapi.model.dav.Propfind;
import de.svws_nrw.davapi.model.dav.Resourcetype;
import de.svws_nrw.davapi.model.dav.Response;
import de.svws_nrw.davapi.model.dav.cal.CalendarHomeSet;
import de.svws_nrw.davapi.util.XmlUnmarshallingUtil;

/**
 * Dispatcher-Klasse für die Verarbeitung von Requests auf das DAV-API mittels
 * der HTTP-Methode PROPFIND auf die Ressource Adressbuch.
 */
public class PropfindDavRootDispatcher extends DavDispatcher {

	/** URI-Parameter zum Erzeugen von URIs für dieses Adressbuch */
	private final DavUriParameter uriParameter;

	/**
	 * Konstruktor für einen neuen Dispatcher mit Repository und den gegebenen
	 * UriParametern
	 *
	 * @param uriParameter die UriParameter zum Erstellen von URIs
	 */
	public PropfindDavRootDispatcher(final DavUriParameter uriParameter) {
		this.uriParameter = uriParameter;
	}

	/**
	 * Verarbeitet ein XML-Request aus dem gegebenen InputStream für die Sammlung
	 * von Ressourcen
	 *
	 * @param inputStream der InputStream, welcher den XML-Request enthält
	 * @return Das Objekt zur Repräsentation der XML-Antwort
	 * @throws IOException bei der Verarbeitung des InputStreams/XML-Unmarshalling
	 */
	public Object dispatchCollection(final InputStream inputStream) throws IOException {
		final Propfind propfind = XmlUnmarshallingUtil.unmarshal(inputStream, Propfind.class);

		final Multistatus ms = new Multistatus();
		ms.getResponse().add(this.generateResponseRootLevel(propfind.getProp()));
		ms.getResponse().add(this.generateResponseAddressbookCollectionLevel(propfind.getProp()));
		if (propfind.getProp().getCalendarHomeSet() != null) {
			ms.getResponse().add(this.generateResponseCalendarCollectionLevel(propfind.getProp()));
		}
		return ms;
	}

	/**
	 * Erzeugt eine Antwort auf dem Root-Level unserer Dav-Schnittstelle abhängig
	 * von den angefragten Properties
	 *
	 * @param propRequested die angefragten Properties
	 * @return eine Response mit den gefundenen und nicht-gefundenen Properties
	 */
	private Response generateResponseRootLevel(final Prop propRequested) {
		final DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		final Prop prop200 = new Prop();

		if (dynamicPropUtil.getIsFieldRequested(Resourcetype.class)) {
			final Resourcetype resourcetype = new Resourcetype();
			resourcetype.setCollection(new Collection());
			// auch wenn das root objekt eigentlich nur eine Sammlung mit den darunter
			// liegenden Sammlungen für Kalender und Adressbücher ist, benötigt thunderbird
			// hier das Principal objekt, da dieser Ordner quasi dem User gehört.
			// Ansonsten würde Thunderbird das Calendar-Home-Set nicht interpretieren,
			// vgl. Kommentar an #583
			resourcetype.setPrincipal(new Principal());
			prop200.setResourcetype(resourcetype);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrincipal.class)) {
			final CurrentUserPrincipal principal = new CurrentUserPrincipal();
			principal.getHref().add(DavUriBuilder.getPrincipalUri(this.uriParameter));
			prop200.setCurrentUserPrincipal(principal);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			prop200.setCurrentUserPrivilegeSet(getReadOnlyPrivilegeSet());
		}

		if (dynamicPropUtil.getIsFieldRequested(CalendarHomeSet.class)) {
			// Kalenderanfragen auf unserer Wurzel benötigen den Link zur Kalendersammlung
			// unter dav/schema/kalender
			final CalendarHomeSet calendarHomeSet = new CalendarHomeSet();
			calendarHomeSet.getHref().add(DavUriBuilder.getCalendarCollectionUri(uriParameter));
			prop200.setCalendarHomeSet(calendarHomeSet);
		}

		final Response response = createResponse(propRequested, prop200);
		response.getHref().add(DavUriBuilder.getCardDavRootUri(this.uriParameter));
		return response;
	}

	/**
	 * Erzeugt ein Antwortobjekt auf dem Level, welches die Adressbücher enthält,
	 * abhängig von den angefragten Properties
	 *
	 * @param propRequested die angefragten Properties
	 * @return ein Response-Objekt mit den gefundenen und nicht gefundenen
	 *         Properties
	 */
	private Response generateResponseAddressbookCollectionLevel(final Prop propRequested) {
		final DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		final Prop prop200 = new Prop();

		if (dynamicPropUtil.getIsFieldRequested(Resourcetype.class)) {
			final Resourcetype resourcetype = new Resourcetype();
			resourcetype.setCollection(new Collection());
			prop200.setResourcetype(resourcetype);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrincipal.class)) {
			final CurrentUserPrincipal principal = new CurrentUserPrincipal();
			principal.getHref().add(DavUriBuilder.getPrincipalUri(uriParameter));
			prop200.setCurrentUserPrincipal(principal);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			prop200.setCurrentUserPrivilegeSet(getReadOnlyPrivilegeSet());
		}

		final Response response = createResponse(propRequested, prop200);
		response.getHref().add(DavUriBuilder.getAddressbookCollectionUri(uriParameter));
		return response;
	}

	/**
	 *
	 * Erzeugt ein Antwortobjekt auf dem Level, welches die Kalender enthält,
	 * abhängig von den angefragten Properties
	 *
	 * @param propRequested die angefragten Properties
	 * @return ein Response-Objekt mit den gefundenen und nicht gefundenen
	 *         Properties
	 *
	 */
	private Response generateResponseCalendarCollectionLevel(final Prop propRequested) {
		// thunderbird interpretiert diesen teil der Antwort einfach mal nicht
		final DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		final Prop prop200 = new Prop();

		if (dynamicPropUtil.getIsFieldRequested(Resourcetype.class)) {
			final Resourcetype resourcetype = new Resourcetype();
			resourcetype.setCollection(new Collection());
			prop200.setResourcetype(resourcetype);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrincipal.class)) {
			final CurrentUserPrincipal principal = new CurrentUserPrincipal();
			principal.getHref().add(DavUriBuilder.getPrincipalUri(uriParameter));
			prop200.setCurrentUserPrincipal(principal);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			prop200.setCurrentUserPrivilegeSet(getReadOnlyPrivilegeSet());
		}

		if (dynamicPropUtil.getIsFieldRequested(CalendarHomeSet.class)) {
			final CalendarHomeSet calendarHomeSet = new CalendarHomeSet();
			calendarHomeSet.getHref().add(DavUriBuilder.getCalendarCollectionUri(uriParameter));
			prop200.setCalendarHomeSet(calendarHomeSet);
		}

		final Response response = createResponse(propRequested, prop200);
		response.getHref().add(DavUriBuilder.getCalendarCollectionUri(uriParameter));
		return response;
	}
}
