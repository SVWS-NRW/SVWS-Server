package de.nrw.schule.svws.davapi.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import de.nrw.schule.svws.core.data.kalender.Kalender;
import de.nrw.schule.svws.core.data.kalender.KalenderEintrag;
import de.nrw.schule.svws.davapi.data.CollectionRessourceQueryParameters;
import de.nrw.schule.svws.davapi.data.IKalenderRepository;
import de.nrw.schule.svws.davapi.model.dav.Collection;
import de.nrw.schule.svws.davapi.model.dav.CurrentUserPrincipal;
import de.nrw.schule.svws.davapi.model.dav.CurrentUserPrivilegeSet;
import de.nrw.schule.svws.davapi.model.dav.Displayname;
import de.nrw.schule.svws.davapi.model.dav.Getcontenttype;
import de.nrw.schule.svws.davapi.model.dav.Getetag;
import de.nrw.schule.svws.davapi.model.dav.Multistatus;
import de.nrw.schule.svws.davapi.model.dav.Owner;
import de.nrw.schule.svws.davapi.model.dav.Prop;
import de.nrw.schule.svws.davapi.model.dav.Propfind;
import de.nrw.schule.svws.davapi.model.dav.Report;
import de.nrw.schule.svws.davapi.model.dav.Resourcetype;
import de.nrw.schule.svws.davapi.model.dav.Response;
import de.nrw.schule.svws.davapi.model.dav.SupportedReport;
import de.nrw.schule.svws.davapi.model.dav.SupportedReportSet;
import de.nrw.schule.svws.davapi.model.dav.SyncCollection;
import de.nrw.schule.svws.davapi.model.dav.SyncToken;
import de.nrw.schule.svws.davapi.model.dav.cal.CalCalendar;
import de.nrw.schule.svws.davapi.model.dav.cal.CalendarComponent;
import de.nrw.schule.svws.davapi.model.dav.cal.CalendarHomeSet;
import de.nrw.schule.svws.davapi.model.dav.cal.CalendarMultiget;
import de.nrw.schule.svws.davapi.model.dav.cal.CalendarQuery;
import de.nrw.schule.svws.davapi.model.dav.cal.SupportedCalendarComponentSet;
import de.nrw.schule.svws.davapi.model.dav.cs.Getctag;
import de.nrw.schule.svws.davapi.util.XmlUnmarshallingUtil;

/**
 * Dispatcher-Klasse für die Verarbeitung von Requests auf das DAV-API mittels
 * der HTTP-Methode PROPFIND auf die Ressource Kalender.
 */
public class PropfindCalendarDispatcher extends DavDispatcher {

	/** Repository-Klasse zur Abfrage von Kalendern aus der SVWS-Datenbank */
	private final IKalenderRepository repository;
	/** URI-Parameter zum Erzeugen von URIs für Kalender */
	private DavUriParameter uriParameter;

	/**
	 * Konstruktor für einen neuen Dispatcher mit Repository und den gegebenen
	 * UriParametern
	 *
	 * @param repository   das Repository zum Zugriff auf Kalender der Datenbank
	 * @param uriParameter die UriParameter zum Erstellen von URIs
	 */
	public PropfindCalendarDispatcher(IKalenderRepository repository, DavUriParameter uriParameter) {
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
	private Object dispatchCollection(Propfind propfind) {
		List<Kalender> kalenderList = this.repository
				.getAvailableKalender(CollectionRessourceQueryParameters.EXCLUDE_RESSOURCES);
		if (kalenderList.isEmpty()) {
			return this.createResourceNotFoundError(
					"Es wurden keine Adressbücher für den angemeldeten Benutzer gefunden!");
		}
		Multistatus ms = new Multistatus();
		// wichtig ist, dass dem Client die Collection selbst im Response beschrieben
		// wird
		Response response = generateResponseCalendarCollectionLevel(propfind.getProp());
		ms.getResponse().add(response);
		for (Kalender kalender : kalenderList) {
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
	private Response generateResponseCalendarCollectionLevel(Prop prop) {
		Prop prop200 = new Prop();
		DynamicPropUtil propUtil = new DynamicPropUtil(prop);
		if (propUtil.getIsFieldRequested(Resourcetype.class)) {
			Resourcetype r = new Resourcetype();
			// Die Sammlung von Kalendern selbst ist kein Kalender, sondern nur eine
			// Dav-Collection.
			r.setCollection(new Collection());
			prop200.setResourcetype(r);
		}
		if (propUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			// auf der Dav Collection darf derzeit nur gelesen, nicht geschrieben werden
			// ggf muss das geändert werden, wenn eigene Kalender/Adressbücher oder
			// DavCollections angelegt werden sollen.
			CurrentUserPrivilegeSet privilegeSet = getPrivilegeSet(true, false);
			prop200.setCurrentUserPrivilegeSet(privilegeSet);
		}

		Response createResponse = createResponse(prop, prop200);
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
	public Object dispatch(InputStream inputStream, String ressourceId) throws IOException {
		Propfind propfind = XmlUnmarshallingUtil.unmarshal(inputStream, Propfind.class);
		if (ressourceId == null || ressourceId.isBlank()) {
			// ohne Ressource ID wird die Liste der Kalender zurückgegeben
			return dispatchCollection(propfind);
		}

		Optional<Kalender> kalender = this.repository.getKalenderById(ressourceId,
				CollectionRessourceQueryParameters.INCLUDE_RESSOURCES_EXCLUDE_PAYLOAD);
		if (kalender.isEmpty()) {
			return this.createResourceNotFoundError("Kalender mit der angegebenen Id wurde nicht gefunden!");
		}

		Multistatus ms = new Multistatus();
		ms.getResponse().add(this.generateResponseCalendarLevel(kalender.get(), propfind.getProp()));
		for (KalenderEintrag eintrag : kalender.get().kalenderEintraege) {
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
	private Response generateResponseCalendarLevel(Kalender kalender, Prop propRequested) {
		DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		Prop prop200 = new Prop();
		uriParameter.setResourceCollectionId(kalender.id);

		if (dynamicPropUtil.getIsFieldRequested(Resourcetype.class)) {
			Resourcetype resourcetype = new Resourcetype();
			resourcetype.setCollection(new Collection());
			resourcetype.setCalendar(new CalCalendar());
			prop200.setResourcetype(resourcetype);
		}

		if (dynamicPropUtil.getIsFieldRequested(Displayname.class)) {
			Displayname displayname = new Displayname();
			displayname.getContent().add(kalender.displayname);
			prop200.setDisplayname(displayname);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrincipal.class)) {
			CurrentUserPrincipal principal = new CurrentUserPrincipal();
			principal.getHref().add(DavUriBuilder.getPrincipalUri(uriParameter));
			prop200.setCurrentUserPrincipal(principal);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			prop200.setCurrentUserPrivilegeSet(getPrivilegeSet(kalender.darfLesen, kalender.darfSchreiben));
		}

		if (dynamicPropUtil.getIsFieldRequested(Getctag.class)) {
			Getctag getctag = new Getctag();
			getctag.getContent().add(String.valueOf(kalender.synctoken));
			prop200.setGetctag(getctag);
		}

		if (dynamicPropUtil.getIsFieldRequested(SyncToken.class)) {
			SyncToken syncToken = new SyncToken();
			syncToken.getContent().add(String.valueOf(kalender.synctoken));
			prop200.setSyncToken(syncToken);
		}

		if (dynamicPropUtil.getIsFieldRequested(SupportedCalendarComponentSet.class)) {
			SupportedCalendarComponentSet supportedCalendarComponentSet = new SupportedCalendarComponentSet();
			CalendarComponent calendarComponent = new CalendarComponent();
			calendarComponent.setName("VEVENT");
			supportedCalendarComponentSet.getCalendarComponents().add(calendarComponent);
			prop200.setSupportedCalendarComponentSet(supportedCalendarComponentSet);
		}

		if (dynamicPropUtil.getIsFieldRequested(SupportedReportSet.class)) {
			SupportedReport supportedReportCalendarMultiget = new SupportedReport();
			Report reportCalendarMultiget = new Report();
			reportCalendarMultiget.setCalendarMultiget(new CalendarMultiget());
			supportedReportCalendarMultiget.setReport(reportCalendarMultiget);

			SupportedReport supportedReportCalendarQuery = new SupportedReport();
			Report reportCalendarQuery = new Report();
			reportCalendarQuery.setCalendarQuery(new CalendarQuery());
			supportedReportCalendarQuery.setReport(reportCalendarQuery);

			SupportedReport supportedReportSyncCollection = new SupportedReport();
			Report reportSyncCollection = new Report();
			reportSyncCollection.setSyncCollection(new SyncCollection());
			supportedReportSyncCollection.setReport(reportSyncCollection);

			SupportedReportSet supportedReportSet = new SupportedReportSet();
			supportedReportSet.getSupportedReport().addAll(List.of(supportedReportCalendarMultiget,
					supportedReportCalendarQuery, supportedReportSyncCollection));
			prop200.setSupportedReportSet(supportedReportSet);
		}

		if (dynamicPropUtil.getIsFieldRequested(Getcontenttype.class)) {
			Getcontenttype getContentType = new Getcontenttype();
			getContentType.getContent().add("text/calendar");
			prop200.setGetcontenttype(getContentType);
		}

		if (dynamicPropUtil.getIsFieldRequested(Owner.class)) {
			uriParameter.setBenutzerId(Long.toString(kalender.besitzer));
			Owner owner = new Owner();
			owner.setHref(DavUriBuilder.getPrincipalUri(uriParameter));
			prop200.setOwner(owner);
		}

		if (dynamicPropUtil.getIsFieldRequested(CalendarHomeSet.class)) {
			CalendarHomeSet calendarHomeSet = new CalendarHomeSet();
			calendarHomeSet.getHref().add(DavUriBuilder.getCalendarCollectionUri(uriParameter));
			prop200.setCalendarHomeSet(calendarHomeSet);
		}

		Response response = createResponse(propRequested, prop200);
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
	private Response generateResponseEventLevel(KalenderEintrag eintrag, Prop propRequested) {
		DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		Prop prop200 = new Prop();

		uriParameter.setResourceCollectionId(eintrag.kalenderId);
		uriParameter.setResourceId(eintrag.uid);

		if (dynamicPropUtil.getIsFieldRequested(Resourcetype.class)) {
			Resourcetype resourcetype = new Resourcetype();
			prop200.setResourcetype(resourcetype);
		}

		if (dynamicPropUtil.getIsFieldRequested(Getetag.class)) {
			Getetag getetag = new Getetag();
			getetag.getContent().add(eintrag.version);
			prop200.setGetetag(getetag);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			prop200.setCurrentUserPrivilegeSet(getPrivilegeSet(eintrag.darfLesen, eintrag.darfSchreiben));
		}

		Response response = createResponse(propRequested, prop200);
		response.getHref().add(DavUriBuilder.getCalendarEntryUri(uriParameter));
		return response;
	}

}
