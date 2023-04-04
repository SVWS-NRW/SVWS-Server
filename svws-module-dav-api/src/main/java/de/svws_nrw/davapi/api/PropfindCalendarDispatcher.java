package de.svws_nrw.davapi.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import de.svws_nrw.core.data.kalender.Kalender;
import de.svws_nrw.core.data.kalender.KalenderEintrag;
import de.svws_nrw.davapi.data.CollectionRessourceQueryParameters;
import de.svws_nrw.davapi.data.IKalenderRepository;
import de.svws_nrw.davapi.model.dav.Collection;
import de.svws_nrw.davapi.model.dav.CurrentUserPrincipal;
import de.svws_nrw.davapi.model.dav.CurrentUserPrivilegeSet;
import de.svws_nrw.davapi.model.dav.Displayname;
import de.svws_nrw.davapi.model.dav.Getcontenttype;
import de.svws_nrw.davapi.model.dav.Getetag;
import de.svws_nrw.davapi.model.dav.Multistatus;
import de.svws_nrw.davapi.model.dav.Owner;
import de.svws_nrw.davapi.model.dav.Prop;
import de.svws_nrw.davapi.model.dav.Propfind;
import de.svws_nrw.davapi.model.dav.Report;
import de.svws_nrw.davapi.model.dav.Resourcetype;
import de.svws_nrw.davapi.model.dav.Response;
import de.svws_nrw.davapi.model.dav.SupportedReport;
import de.svws_nrw.davapi.model.dav.SupportedReportSet;
import de.svws_nrw.davapi.model.dav.SyncCollection;
import de.svws_nrw.davapi.model.dav.SyncToken;
import de.svws_nrw.davapi.model.dav.cal.CalCalendar;
import de.svws_nrw.davapi.model.dav.cal.CalendarComponent;
import de.svws_nrw.davapi.model.dav.cal.CalendarHomeSet;
import de.svws_nrw.davapi.model.dav.cal.CalendarMultiget;
import de.svws_nrw.davapi.model.dav.cal.CalendarQuery;
import de.svws_nrw.davapi.model.dav.cal.SupportedCalendarComponentSet;
import de.svws_nrw.davapi.model.dav.cs.Getctag;
import de.svws_nrw.davapi.util.XmlUnmarshallingUtil;

/**
 * Dispatcher-Klasse für die Verarbeitung von Requests auf das DAV-API mittels
 * der HTTP-Methode PROPFIND auf die Ressource Kalender.
 */
public class PropfindCalendarDispatcher extends DavDispatcher {

	/** Repository-Klasse zur Abfrage von Kalendern aus der SVWS-Datenbank */
	private final IKalenderRepository repository;
	/** URI-Parameter zum Erzeugen von URIs für Kalender */
	private final DavUriParameter uriParameter;

	/**
	 * Konstruktor für einen neuen Dispatcher mit Repository und den gegebenen
	 * UriParametern
	 *
	 * @param repository   das Repository zum Zugriff auf Kalender der Datenbank
	 * @param uriParameter die UriParameter zum Erstellen von URIs
	 */
	public PropfindCalendarDispatcher(final IKalenderRepository repository, final DavUriParameter uriParameter) {
		this.repository = repository;
		this.uriParameter = uriParameter;
	}

	/**
	 * Interpretiert den gegebenen Propfind-Request als Anfrage zu allen Kalender
	 * dieses Nutzers
	 *
	 * @param propfind das Request Propfind
	 * @return das Ergebnisobjekt für den Request
	 * @throws IOException
	 */
	private Object dispatchCollection(final Propfind propfind) {
		final List<Kalender> kalenderList = this.repository
				.getAvailableKalender(CollectionRessourceQueryParameters.EXCLUDE_RESSOURCES);
		if (kalenderList.isEmpty()) {
			return this.createResourceNotFoundError(
					"Es wurden keine Adressbücher für den angemeldeten Benutzer gefunden!");
		}
		final Multistatus ms = new Multistatus();
		// wichtig ist, dass dem Client die Collection selbst im Response beschrieben
		// wird
		final Response response = generateResponseCalendarCollectionLevel(propfind.getProp());
		ms.getResponse().add(response);
		for (final Kalender kalender : kalenderList) {
			ms.getResponse().add(this.generateResponseCalendarLevel(kalender, propfind.getProp()));
		}
		return ms;
	}

	/**
	 * Generiert das Responseobjekt für die Kalendersammlung unter
	 * db/schema/dav/kalender Thunderbird erwartet den resourcetype collection. Dies
	 * ist dem client als calendar-home-set bekannt und er wird versuchen hier
	 * weitere kalender zu suchen.
	 *
	 * @param prop das Prop-Objekt mit angefragten Informationen
	 * @return das Responseobjekt für die Kalendersammlung
	 */
	private Response generateResponseCalendarCollectionLevel(final Prop prop) {
		final Prop prop200 = new Prop();
		final DynamicPropUtil propUtil = new DynamicPropUtil(prop);
		if (propUtil.getIsFieldRequested(Resourcetype.class)) {
			final Resourcetype r = new Resourcetype();
			// Die Sammlung von Kalendern selbst ist kein Kalender, sondern nur eine
			// Dav-Collection.
			r.setCollection(new Collection());
			prop200.setResourcetype(r);
		}
		if (propUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			// auf der Dav Collection darf derzeit nur gelesen, nicht geschrieben werden
			// ggf muss das geändert werden, wenn eigene Kalender/Adressbücher oder
			// DavCollections angelegt werden sollen.
			final CurrentUserPrivilegeSet privilegeSet = getPrivilegeSet(true, false);
			prop200.setCurrentUserPrivilegeSet(privilegeSet);
		}

		final Response createResponse = createResponse(prop, prop200);
		createResponse.getHref().add(DavUriBuilder.getCalendarCollectionUri(uriParameter));
		return createResponse;
	}

	/**
	 * Verarbeitet einen Inputstream als PROPFIND-Anfrage auf einen gegebenen
	 * Kalender, bei nicht gegebener Kalender-ID wird es die Anfrage auf eine
	 * Sammlung von Kalendern übertragen
	 *
	 * @param inputStream der Inputstream des Requests
	 * @param ressourceId die id der Ressourcensammlung, bei nicht gegebener ID wird
	 *                    es die Anfrage auf eine Sammlung von Kalendern übertragen
	 * @return das Multistatusobjekt für die Anfrage
	 * @throws IOException bei Fehlern im Inputstream
	 */
	public Object dispatch(final InputStream inputStream, final String ressourceId) throws IOException {
		final Propfind propfind = XmlUnmarshallingUtil.unmarshal(inputStream, Propfind.class);
		if (ressourceId == null || ressourceId.isBlank()) {
			// ohne Ressource ID wird die Liste der Kalender zurückgegeben
			return dispatchCollection(propfind);
		}

		final Optional<Kalender> kalender = this.repository.getKalenderById(ressourceId,
				CollectionRessourceQueryParameters.INCLUDE_RESSOURCES_EXCLUDE_PAYLOAD);
		if (kalender.isEmpty()) {
			return this.createResourceNotFoundError("Kalender mit der angegebenen Id wurde nicht gefunden!");
		}

		final Multistatus ms = new Multistatus();
		ms.getResponse().add(this.generateResponseCalendarLevel(kalender.get(), propfind.getProp()));
		for (final KalenderEintrag eintrag : kalender.get().kalenderEintraege) {
			eintrag.kalenderId = kalender.get().id;
			ms.getResponse().add(this.generateResponseEventLevel(eintrag, propfind.getProp()));
		}
		return ms;
	}

	/**
	 * Generiert ein Response-Objekt für ein angegebenes Adressbuch.
	 *
	 * @param kalender      Adressbuch, zu dem Informationen zurückgeliefert werden
	 *                      sollen
	 * @param propRequested Prop aus dem Request. Definiert, welche Informationen
	 *                      zur Ressource zurückgeliefert werden sollen.
	 * @return Response-Objekt zum angegebenen Adressbuch
	 */
	private Response generateResponseCalendarLevel(final Kalender kalender, final Prop propRequested) {
		final DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		final Prop prop200 = new Prop();
		uriParameter.setResourceCollectionId(kalender.id);

		if (dynamicPropUtil.getIsFieldRequested(Resourcetype.class)) {
			final Resourcetype resourcetype = new Resourcetype();
			resourcetype.setCollection(new Collection());
			resourcetype.setCalendar(new CalCalendar());
			prop200.setResourcetype(resourcetype);
		}

		if (dynamicPropUtil.getIsFieldRequested(Displayname.class)) {
			final Displayname displayname = new Displayname();
			displayname.getContent().add(kalender.displayname);
			prop200.setDisplayname(displayname);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrincipal.class)) {
			final CurrentUserPrincipal principal = new CurrentUserPrincipal();
			principal.getHref().add(DavUriBuilder.getPrincipalUri(uriParameter));
			prop200.setCurrentUserPrincipal(principal);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			prop200.setCurrentUserPrivilegeSet(getPrivilegeSet(kalender.darfLesen, kalender.darfSchreiben));
		}

		if (dynamicPropUtil.getIsFieldRequested(Getctag.class)) {
			final Getctag getctag = new Getctag();
			getctag.getContent().add(String.valueOf(kalender.synctoken));
			prop200.setGetctag(getctag);
		}

		if (dynamicPropUtil.getIsFieldRequested(SyncToken.class)) {
			final SyncToken syncToken = new SyncToken();
			syncToken.getContent().add(String.valueOf(kalender.synctoken));
			prop200.setSyncToken(syncToken);
		}

		if (dynamicPropUtil.getIsFieldRequested(SupportedCalendarComponentSet.class)) {
			final SupportedCalendarComponentSet supportedCalendarComponentSet = new SupportedCalendarComponentSet();
			final CalendarComponent calendarComponent = new CalendarComponent();
			calendarComponent.setName("VEVENT");
			supportedCalendarComponentSet.getCalendarComponents().add(calendarComponent);
			prop200.setSupportedCalendarComponentSet(supportedCalendarComponentSet);
		}

		if (dynamicPropUtil.getIsFieldRequested(SupportedReportSet.class)) {
			final SupportedReport supportedReportCalendarMultiget = new SupportedReport();
			final Report reportCalendarMultiget = new Report();
			reportCalendarMultiget.setCalendarMultiget(new CalendarMultiget());
			supportedReportCalendarMultiget.setReport(reportCalendarMultiget);

			final SupportedReport supportedReportCalendarQuery = new SupportedReport();
			final Report reportCalendarQuery = new Report();
			reportCalendarQuery.setCalendarQuery(new CalendarQuery());
			supportedReportCalendarQuery.setReport(reportCalendarQuery);

			final SupportedReport supportedReportSyncCollection = new SupportedReport();
			final Report reportSyncCollection = new Report();
			reportSyncCollection.setSyncCollection(new SyncCollection());
			supportedReportSyncCollection.setReport(reportSyncCollection);

			final SupportedReportSet supportedReportSet = new SupportedReportSet();
			supportedReportSet.getSupportedReport().addAll(List.of(supportedReportCalendarMultiget,
					supportedReportCalendarQuery, supportedReportSyncCollection));
			prop200.setSupportedReportSet(supportedReportSet);
		}

		if (dynamicPropUtil.getIsFieldRequested(Getcontenttype.class)) {
			final Getcontenttype getContentType = new Getcontenttype();
			getContentType.getContent().add("text/calendar");
			prop200.setGetcontenttype(getContentType);
		}

		if (dynamicPropUtil.getIsFieldRequested(Owner.class)) {
			uriParameter.setBenutzerId(Long.toString(kalender.besitzer));
			final Owner owner = new Owner();
			owner.setHref(DavUriBuilder.getPrincipalUri(uriParameter));
			prop200.setOwner(owner);
		}

		if (dynamicPropUtil.getIsFieldRequested(CalendarHomeSet.class)) {
			final CalendarHomeSet calendarHomeSet = new CalendarHomeSet();
			calendarHomeSet.getHref().add(DavUriBuilder.getCalendarCollectionUri(uriParameter));
			prop200.setCalendarHomeSet(calendarHomeSet);
		}

		final Response response = createResponse(propRequested, prop200);
		response.getHref().add(DavUriBuilder.getCalendarUri(uriParameter));
		return response;
	}

	/**
	 * Generiert ein Response-Objekt für einen angegebenen KalenderEintrag.
	 *
	 * @param eintrag       Eintrag, zu dem Informationen zurückgeliefert werden
	 *                      sollen
	 * @param propRequested Prop aus dem Request. Definiert, welche Informationen
	 *                      zur Ressource zurückgeliefert werden sollen.
	 * @return Response-Objekt zum angegebenen KalenderEintrag
	 */
	private Response generateResponseEventLevel(final KalenderEintrag eintrag, final Prop propRequested) {
		final DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		final Prop prop200 = new Prop();

		uriParameter.setResourceCollectionId(eintrag.kalenderId);
		uriParameter.setResourceId(eintrag.uid);

		if (dynamicPropUtil.getIsFieldRequested(Resourcetype.class)) {
			final Resourcetype resourcetype = new Resourcetype();
			prop200.setResourcetype(resourcetype);
		}

		if (dynamicPropUtil.getIsFieldRequested(Getetag.class)) {
			final Getetag getetag = new Getetag();
			getetag.getContent().add(eintrag.version);
			prop200.setGetetag(getetag);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			prop200.setCurrentUserPrivilegeSet(getPrivilegeSet(eintrag.darfLesen, eintrag.darfSchreiben));
		}

		final Response response = createResponse(propRequested, prop200);
		response.getHref().add(DavUriBuilder.getCalendarEntryUri(uriParameter));
		return response;
	}

}
