package de.svws_nrw.davapi.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import de.svws_nrw.core.data.kalender.Kalender;
import de.svws_nrw.core.data.kalender.KalenderEintrag;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.davapi.data.caldav.CalDavKalender;
import de.svws_nrw.davapi.data.dav.DavException;
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
import de.svws_nrw.davapi.model.dav.Propstat;
import de.svws_nrw.davapi.model.dav.Report;
import de.svws_nrw.davapi.model.dav.Resourcetype;
import de.svws_nrw.davapi.model.dav.SupportedReport;
import de.svws_nrw.davapi.model.dav.SupportedReportSet;
import de.svws_nrw.davapi.model.dav.SyncCollection;
import de.svws_nrw.davapi.model.dav.SyncToken;
import de.svws_nrw.davapi.model.dav.cal.CalCalendar;
import de.svws_nrw.davapi.model.dav.cal.CalendarComponent;
import de.svws_nrw.davapi.model.dav.cal.CalendarData;
import de.svws_nrw.davapi.model.dav.cal.CalendarHomeSet;
import de.svws_nrw.davapi.model.dav.cal.CalendarMultiget;
import de.svws_nrw.davapi.model.dav.cal.CalendarQuery;
import de.svws_nrw.davapi.model.dav.cal.CompFilter;
import de.svws_nrw.davapi.model.dav.cal.SupportedCalendarComponentSet;
import de.svws_nrw.davapi.model.dav.cal.TimeRange;
import de.svws_nrw.davapi.model.dav.cs.Getctag;
import de.svws_nrw.davapi.util.icalendar.DateTimeUtil;
import de.svws_nrw.davapi.util.icalendar.VCalendar;
import de.svws_nrw.db.DBEntityManager;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.EntityTag;


/**
 * Diese Klasse handhabt HTTP-Requests an der CalDAV-API des SVWS-Servers.
 */
public class CalDavRequestManager extends AbstractDavRequestManager {

	/** Das Kalender-Repository für den Datenbankzugriff */
	private final CalDavKalender repo;


	/**
	 * Erstellt einen neuen Manager für CalDAV-HTTP-Requests über die angegebene Datenbankverbindung
	 *
	 * @param conn   die Datenbankverbindung
	 * @param is     der Input-Stream mit dem Body der Anfrage
	 *
	 * @throws IOException   wenn der Request-Body nicht gelesen werden kann
	 */
	public CalDavRequestManager(final @NotNull DBEntityManager conn, final InputStream is) throws IOException {
		super(conn, is);
		repo = new CalDavKalender(conn);
	}


	/**
	 * Erstellt eine HTTP-Response für eine Propfind-HTTP-Anfrage auf die Kalendersammlung.
	 *
	 * @return die HTTP-Response
	 *
	 * @throws IOException             für den Fall, dass die Anfrage nicht erfolgreich deserialisiert werden kann
	 */
	public jakarta.ws.rs.core.Response propfindCollection() throws IOException {
		logRequest("CalDAV->propfindCollection");

		try {
			final Propfind propfind = mapper.readValue(requestBody, Propfind.class);
			final List<Kalender> kalenderList = repo.getAvailableKalender(false, false);
			if (kalenderList.isEmpty())
				return buildResponse(createResourceNotFoundError("Es wurden keine Adressbücher für den angemeldeten Benutzer gefunden!"));
			final Multistatus ms = new Multistatus();
			// wichtig ist, dass dem Client die Collection selbst im Response beschrieben wird
			final var response = genPropfindCollectionResponse(propfind.getProp());
			ms.getResponse().add(response);
			for (final Kalender kalender : kalenderList)
				ms.getResponse().add(this.genPropfindCalendarResponse(kalender, propfind.getProp()));
			return buildResponse(ms);
		} catch (final DavException e) {
			return buildResponse(e.getDavResponse(getKalenderUri()).getError());
		}
	}


	/**
	 * Erstellt eine HTTP-Response für eine Propfind-HTTP-Anfrage auf einen Kalender.
	 *
	 * @param idCal   die Ressourcen-ID für den Kalender
	 *
	 * @return die HTTP-Response
	 *
	 * @throws IOException             für den Fall, dass die Anfrage nicht erfolgreich deserialisiert werden kann
	 */
	public jakarta.ws.rs.core.Response propfindCalendar(final String idCal) throws IOException {
		logRequest("CalDAV->propfindCalendar", "idCal=" + idCal);
		this.setParameterResourceCollectionId(idCal);

			// Wurde keine gültige Ressource angegeben, so ist dies eigentlicht ein Zugriff auf die Kalendersammlung
			if ((idCal == null) || idCal.isBlank())
				return propfindCollection();

			final Propfind propfind = mapper.readValue(requestBody, Propfind.class);

			final Kalender kalender = repo.getKalenderById(idCal, true, false);
			if (kalender == null)
				return buildResponse(createResourceNotFoundError("Kalender mit der angegebenen Id wurde nicht gefunden!"));

			final Multistatus ms = new Multistatus();
			ms.getResponse().add(this.genPropfindCalendarResponse(kalender, propfind.getProp()));
			for (final KalenderEintrag eintrag : kalender.kalenderEintraege) {
				eintrag.kalenderId = kalender.id;
				ms.getResponse().add(this.genPropfindEntryResponse(eintrag, propfind.getProp()));
			}
			return buildResponse(ms);
	}


	/**
	 * Generiert ein DAV-XML-Response-Objekt für die Kalendersammlung. Hierbei wird der Typ
	 * Collection zurückgegeben.
	 * Dies ist dem Thunderbird-Client als calendar-home-set bekannt und dieser wird
	 * hier ggf. weitere Kalender zu suchen.
	 *
	 * @param req   das Prop-Objekt mit angefragten Informationen
	 *
	 * @return die XML-Response mit den Informationen zur Kalendersammlung
	 */
	private de.svws_nrw.davapi.model.dav.Response genPropfindCollectionResponse(final Prop req) {
		final Prop resp = new Prop();
		final DynamicPropUtil propUtil = new DynamicPropUtil(req);

		// Der Typ der Ressource ist nur eine Collection, also eine Kalendersammlung, da kein Kalender angegeben wird
		if (propUtil.getIsFieldRequested(Resourcetype.class)) {
			final Resourcetype r = new Resourcetype();
			r.setCollection(new Collection());
			resp.setResourcetype(r);
		}

		// Bei der Privilegien gilt aktuell: nur Lesen
		// Dies muss ggf. geändert werden, sobald auch eigene Kalender erlaubt werden sollen, dann ist auch schreiben erlaubt
		if (propUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			final CurrentUserPrivilegeSet privilegeSet = getPrivilegeSet(true, false);
			resp.setCurrentUserPrivilegeSet(privilegeSet);
		}

		return createResponse(req, resp, getKalenderUri());
	}


	/**
	 * Generiert ein DAV-XML-Response-Objekt für einen Kalender.
	 *
	 * @param kalender   der Kalender, zu dem Informationen zurückgeliefert werden sollen
	 * @param req        das Prop-Objekt mit angefragten Informationen
	 *
	 * @return die XML-Response mit den Informationen zum Kalender
	 */
	private de.svws_nrw.davapi.model.dav.Response genPropfindCalendarResponse(final Kalender kalender, final Prop req) {
		setParameterResourceCollectionId(kalender.id);

		final Prop resp = new Prop();
		final DynamicPropUtil propUtil = new DynamicPropUtil(req);

		// Der Typ der Ressource ist Collection und Calendar
		if (propUtil.getIsFieldRequested(Resourcetype.class)) {
			final Resourcetype resourcetype = new Resourcetype();
			resourcetype.setCollection(new Collection());
			resourcetype.setCalendar(new CalCalendar());
			resp.setResourcetype(resourcetype);
		}

		// Der Anzeigename
		if (propUtil.getIsFieldRequested(Displayname.class)) {
			final Displayname displayname = new Displayname();
			displayname.getContent().add(kalender.displayname);
			resp.setDisplayname(displayname);
		}

		// Der Benutzer der aktuellen Verbindung
		if (propUtil.getIsFieldRequested(CurrentUserPrincipal.class)) {
			final CurrentUserPrincipal principal = new CurrentUserPrincipal();
			principal.getHref().add(getBenutzerUri());
			resp.setCurrentUserPrincipal(principal);
		}

		// Die Privilegien des Benutzers der aktuellen Verbindungen auf den Kalender
		if (propUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class))
			resp.setCurrentUserPrivilegeSet(getPrivilegeSet(kalender.darfLesen, kalender.darfSchreiben));

		// Das Collection-Tag, um Änderungen am Kalenderinhalt zu verfolgen (Verwendung eines Sync-Tokens, welches auch bei Änderungen am Inhalt angepasst werden muss)
		if (propUtil.getIsFieldRequested(Getctag.class)) {
			final Getctag getctag = new Getctag();
			getctag.getContent().add(String.valueOf(kalender.synctoken));
			resp.setGetctag(getctag);
		}

		// Der Sync-Token für den Kalender
		if (propUtil.getIsFieldRequested(SyncToken.class)) {
			final SyncToken syncToken = new SyncToken();
			syncToken.getContent().add(String.valueOf(kalender.synctoken));
			resp.setSyncToken(syncToken);
		}

		// Die Menge der unterstützten Kalender-Komponenten
		if (propUtil.getIsFieldRequested(SupportedCalendarComponentSet.class)) {
			final SupportedCalendarComponentSet supportedCalendarComponentSet = new SupportedCalendarComponentSet();
			final CalendarComponent calendarComponent = new CalendarComponent();
			calendarComponent.setName("VEVENT");
			supportedCalendarComponentSet.getCalendarComponents().add(calendarComponent);
			resp.setSupportedCalendarComponentSet(supportedCalendarComponentSet);
		}

		// Die Menge der unterstützten Report-Anfrage an den Kalender
		if (propUtil.getIsFieldRequested(SupportedReportSet.class)) {
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
			supportedReportSet.getSupportedReport()
					.addAll(List.of(supportedReportCalendarMultiget, supportedReportCalendarQuery, supportedReportSyncCollection));
			resp.setSupportedReportSet(supportedReportSet);
		}

		// Der Content-Type der Collection
		if (propUtil.getIsFieldRequested(Getcontenttype.class)) {
			final Getcontenttype getContentType = new Getcontenttype();
			getContentType.getContent().add("text/calendar");
			resp.setGetcontenttype(getContentType);
		}

		// Der Besitzer des Kalenders
		if (propUtil.getIsFieldRequested(Owner.class)) {
			this.setParameterBenutzerId(Long.toString(kalender.besitzer));
			final Owner owner = new Owner();
			owner.setHref(getBenutzerUri());
			resp.setOwner(owner);
		}

		// Die URI des Kalenders ("calendar-home-set")
		if (propUtil.getIsFieldRequested(CalendarHomeSet.class)) {
			final CalendarHomeSet calendarHomeSet = new CalendarHomeSet();
			calendarHomeSet.getHref().add(getKalenderUri());
			resp.setCalendarHomeSet(calendarHomeSet);
		}

		return createResponse(req, resp, getKalenderResourceCollectionUri());
	}

	/**
	 * Generiert ein DAV-XML-Response-Objekt für einen Kalender-Eintrag.
	 *
	 * @param eintrag   der Kalender-Eintrag
	 * @param req       das Prop-Objekt mit angefragten Informationen
	 *
	 * @return die XML-Response mit den Informationen zum Kalender-Eintrag
	 */
	private de.svws_nrw.davapi.model.dav.Response genPropfindEntryResponse(final KalenderEintrag eintrag, final Prop req) {
		this.setParameterResourceCollectionId(eintrag.kalenderId);
		this.setParameterResourceId(eintrag.uid);

		final Prop resp = new Prop();
		final DynamicPropUtil propUtil = new DynamicPropUtil(req);

		// Der Typ der Ressource ist leer, da es sich um einen einfachen Eintrag handelt
		if (propUtil.getIsFieldRequested(Resourcetype.class)) {
			final Resourcetype resourcetype = new Resourcetype();
			resp.setResourcetype(resourcetype);
		}

		// Das Entity-Tag, um Änderungen am Eintrag zu erkennen (dient der Synchronisation des Eintrages)
		if (propUtil.getIsFieldRequested(Getetag.class)) {
			final Getetag getetag = new Getetag();
			getetag.getContent().add(eintrag.version);
			resp.setGetetag(getetag);
		}

		// Die Privilegien des Benutzers der aktuellen Verbindungen auf den Kalender-Eintrag
		if (propUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class))
			resp.setCurrentUserPrivilegeSet(getPrivilegeSet(eintrag.darfLesen, eintrag.darfSchreiben));

		return createResponse(req, resp, getKalenderResourceUri());
	}



	/**
	 * Erstellt eine HTTP-Response für eine Report-HTTP-Anfrage auf einen Kalender.
	 * Anfragen mit den Typen CalendarMultiget, SyncCollection und CalendarQuery werden
	 * dabei unterstützt.
	 *
	 * @param idCal   die Ressourcen-ID für den Kalender
	 *
	 * @return die HTTP-Response
	 *
	 * @throws IOException              für den Fall, dass die Anfrage nicht erfolgreich deserialisiert werden kann
	 */
	public jakarta.ws.rs.core.Response reportCalendar(final String idCal) throws IOException {
		logRequest("CalDAV->reportCalendar", "idCal=" + idCal);
		this.setParameterResourceCollectionId(idCal);

		try {
			// Bestimme die Kalenderdaten aus der Datenbank und gebe einen Fehler zurück, falls kein Kalender mit der ID gefunden wurde
			final Kalender kalender = this.repo.getKalenderById(idCal, true, true);
			if (kalender == null)
				return buildResponse(this.createResourceNotFoundError("Kalender mit der angegebenen ID wurde nicht gefunden!"));

			// Prüfe, ob es sich um eine Anfrage mit dem Typ CalendarMultiget handelt
			final CalendarMultiget calendarMultiget = deserialiseCalendarMultiget(requestBody);
			if (calendarMultiget != null)
				return reportCalendarMultigetRequest(kalender, calendarMultiget);

			// Prüfe, ob es sich um eine Anfrage mit dem Typ SyncCollection handelt
			final SyncCollection syncCollection = deserialiseSyncCollection(requestBody);
			if (syncCollection != null)
				return reportSyncCollectionRequest(kalender, syncCollection);

			// Prüfe, ob es sich um eine Anfrage mit dem Typ CalendarQuery handelt
			final CalendarQuery calendarQuery = deserialiseCalendarQuery(requestBody);
			if (calendarQuery != null)
				return reportCalendarQueryRequest(kalender, calendarQuery);

			throw new UnsupportedOperationException(
					"Es werden nur die Typen CalendarMultiget, SyncCollection und CalendarQuery für die Methode REPORT bei einem Kalender unterstützt.");
		} catch (final DavException e) {
			return buildResponse(e.getDavResponse(getKalenderResourceCollectionUri()).getError());
		}
	}


	/**
	 * Deserialisiert eine CalendarMultiget-XML-Anfrage
	 *
	 * @param data   die xml-Daten als Byte-Array
	 *
	 * @return die deserialisierte Anfrage
	 */
	private CalendarMultiget deserialiseCalendarMultiget(final byte[] data) {
		try {
			return mapper.readValue(data, CalendarMultiget.class);
		} catch (@SuppressWarnings("unused") final IOException e) {
			return null;
		}
	}

	/**
	 * Deserialisiert eine SyncCollection-XML-Anfrage
	 *
	 * @param data   die xml-Daten als Byte-Array
	 *
	 * @return die deserialisierte Anfrage
	 */
	private SyncCollection deserialiseSyncCollection(final byte[] data) {
		try {
			return mapper.readValue(data, SyncCollection.class);
		} catch (@SuppressWarnings("unused") final IOException e) {
			return null;
		}
	}

	/**
	 * Deserialisiert eine CalendarQuery-XML-Anfrage
	 *
	 * @param data   die xml-Daten als Byte-Array
	 *
	 * @return die deserialisierte Anfrage
	 */
	private CalendarQuery deserialiseCalendarQuery(final byte[] data) {
		try {
			return mapper.readValue(data, CalendarQuery.class);
		} catch (@SuppressWarnings("unused") final IOException e) {
			return null;
		}
	}


	/**
	 * Erstellt eine HTTP-Response für eine Report-HTTP-Anfrage des Typs CalendarMultiget auf einen Kalender.
	 *
	 * @param kalender   der Kalender
	 * @param multiget   die Anfrage vom Typ CalendarMultiget mit den angeforderten Einträgen als HRefs
	 *
	 * @return die HTTP-Response
	 */
	private jakarta.ws.rs.core.Response reportCalendarMultigetRequest(final Kalender kalender, final CalendarMultiget multiget) {
		// Bestimme zunächst die Kalender-Einträge anhand der HRefs aus der Anfrage.
		final List<String> hrefs = multiget.getHref();
		final List<KalenderEintrag> eintraege = kalender.kalenderEintraege.stream().filter(k -> {
			this.setParameterResourceId(k.uid);
			return hrefs.isEmpty() || hrefs.contains(getKalenderResourceUri());
		}).toList();

		// Generiere die Response mit den Kalender-Einträgen
		final Multistatus ms = new Multistatus();
		for (final KalenderEintrag eintrag : eintraege)
			ms.getResponse().add(this.genReportEntryResponse(eintrag, multiget.getProp()));
		return buildResponse(ms);
	}


	/**
	 * Erstellt eine HTTP-Response für eine Report-HTTP-Anfrage des Typs SyncCollection auf einen Kalender.
	 * Es wird eine Liste von geänderten Eintraegen ab einem bestimmten "Aufsetzpunkt" (Differenzdaten) ermiitelt.
	 * Diese Funktion dient der Synchronisation von Kalendern mit dem aufrufenden Client.
	 * Die Methode liefert alle Eintraege eines Kalenders zurück, die sich nach dem angegebenen des Sync-Tokens
	 * serverseitig geändert haben oder neu hinzugekommen sind und NOT-FOUND für die Einträge, die entfernt wurden.
	 *
	 * @param kalender         der Kalender
	 * @param syncCollection   die Anfrage vom Typ SyncCollection mit dem SyncToken
	 *
	 * @return die HTTP-Response
	 *
	 * @throws DavException   im Fehlerfall
	 */
	private jakarta.ws.rs.core.Response reportSyncCollectionRequest(final Kalender kalender, final SyncCollection syncCollection) throws DavException {
		// Bestimme den Sync-Token
		final Long syncTokenMillis = syncCollection.getSyncToken().isBlank() ? 0 : Long.valueOf(syncCollection.getSyncToken());

		// Bestimme die Einträge mit Änderungen seit diesem Sync-Token und die UIDs der entfernten Einträge
		final Kalender kalenderById = this.repo.getKalenderById(kalender.id, true, true);
		final List<KalenderEintrag> eintraege = (kalenderById == null) ? Collections.emptyList()
				: kalenderById.kalenderEintraege.stream().filter(e -> Long.valueOf(e.version) > syncTokenMillis).toList();
		final List<String> deletedUIDs = repo.getDeletedEintragUIDs(kalender.id, syncTokenMillis);

		// Generiere die Response mit den Kalender-Einträgen und den UIDs der entfernten Einträge
		final Multistatus ms = new Multistatus();
		for (final KalenderEintrag eintrag : eintraege)
			ms.getResponse().add(this.genReportEntryResponse(eintrag, syncCollection.getProp()));
		for (final String uid : deletedUIDs) {
			this.setParameterResourceId(uid);
			ms.getResponse().add(this.genReportResourceNotFoundResponse(getKalenderResourceUri(), uid));
		}
		ms.setSyncToken(Long.toString(kalender.synctoken));
		return buildResponse(ms);
	}


	/**
	 * Erstellt eine HTTP-Response für eine Report-HTTP-Anfrage des Typs CalendarQuery auf einen Kalender.
	 * Dieser Typ stellt eine Filterdefinition bereit, welcher die angefragten Einträge definiert.
	 *
	 * @param kalender        der Kalender
	 * @param calendarQuery   die Anfrage vom Typ CalendarQuery mit den angeforderten Einträgen als HRefs
	 *
	 * @return die HTTP-Response
	 *
	 * @throws DavException   im Fehlerfall
	 */
	private jakarta.ws.rs.core.Response reportCalendarQueryRequest(final Kalender kalender, final CalendarQuery calendarQuery) throws DavException {
		// Erstelle den Filter als Prädikat und wende diesen zur Filterung der Einträge an
		final Predicate<? super @NotNull KalenderEintrag> eintragFilter = getCalendarQueryFilter(calendarQuery);
		final Kalender kalenderById = this.repo.getKalenderById(kalender.id, true, true);
		final List<KalenderEintrag> eintraege = (kalenderById == null) ? Collections.emptyList()
				: kalenderById.kalenderEintraege.stream().filter(eintragFilter).toList();

		// Generiere die Response mit den Kalender-Einträgen
		final Multistatus ms = new Multistatus();
		for (final KalenderEintrag eintrag : eintraege)
			ms.getResponse().add(this.genReportEntryResponse(eintrag, calendarQuery.getProp()));
		return buildResponse(ms);
	}


	/**
	 * Erstellt ein Prädikat für die Filterung anhand einer CalendarQuery.
	 * Dieses wurde anhand von Abschnitt 9.7.1 as RFC 4791 implementiert (siehe https://datatracker.ietf.org/doc/html/rfc4791#section-9.7.1).
	 *
	 * @param calendarQuery   die Information zum Filter als CalendarQuery
	 *
	 * @return das Prädikat zum Filtern von Kalendereinträgen anhand der CalendarQuery
	 */
	private static Predicate<? super @NotNull KalenderEintrag> getCalendarQueryFilter(final CalendarQuery calendarQuery) {
		// Prüfe, ob eine gültige Filterdefinition vorliegt. Wenn nicht, dann gib ein Prädikat zurück, welche immer true liefert.
		if ((calendarQuery == null) || (calendarQuery.getFilter() == null) || (calendarQuery.getFilter().getCompFilter() == null))
			return e -> true;
		final CompFilter filter = calendarQuery.getFilter().getCompFilter();

		// Erzeuge den Filter für die Art der Ressource, bspw. VCALENDAR (andere Einträge haben wir nicht)
		final String ressourceTypeFilter = filter.getName();
		final Predicate<KalenderEintrag> ressourceTypePredicate = (ressourceTypeFilter == null)
				? e -> true
				: e -> (e.data != null) && e.data.startsWith("BEGIN:" + ressourceTypeFilter);

		// Gibt es noch Filterdefinitionen auf Component-Ebene? Wenn nicht, dann ist das Filter-Prädikat bereits vollständig erstellt ...
		final CompFilter filterComp = filter.getCompFilter();
		if (filterComp == null)
			return ressourceTypePredicate;

		// Erzeuge den Filter für den Komponenten-Typ, bspw. VEVENT, VTODO, VFREEBUSY oder VTIMEZONE
		final String componentTypeFilter = filterComp.getName();
		final Predicate<KalenderEintrag> componentTypePredicate = (componentTypeFilter == null)
				? e -> true
				: e -> (e.data != null) && e.data.contains("BEGIN:" + componentTypeFilter);

		// Gibt es auch einen Filter in Bezug ein Zeitintervall? Wenn nicht, dann ist das Filter-Prädikat bereits vollständig erstellt ...
		if ((filterComp.getTimeRange() == null) || (filterComp.getTimeRange().getStart() == null) || (filterComp.getTimeRange().getEnd() == null))
			return ressourceTypePredicate.and(componentTypePredicate);

		// Erzeuge den Filter für das Zeitintervall ...
		final TimeRange timeRange = filterComp.getTimeRange();
		final Instant timeRangeMin = DateTimeUtil.parseCalDav(timeRange.getStart());
		final Instant timeRangeMax = DateTimeUtil.parseCalDav(timeRange.getEnd());
		final Predicate<KalenderEintrag> timeRangePredicate = e -> DateTimeUtil.intersect(timeRangeMin, timeRangeMax,
				DateTimeUtil.fromSqlTimeStamp(e.kalenderStart), DateTimeUtil.fromSqlTimeStamp(e.kalenderEnde));
		return ressourceTypePredicate.and(componentTypePredicate).and(timeRangePredicate);
	}


	/**
	 * Generiert ein Response-Objekt für eine fehlende Resource
	 *
	 * @param uri           der URI für die Ressource
	 * @param resourceUID   die UID für die Ressource
	 *
	 * @return die XML-Response mit den Informationen zum Kalender-Eintrag
	 */
	private de.svws_nrw.davapi.model.dav.Response genReportResourceNotFoundResponse(final String uri, final String resourceUID) {
		final var response = new de.svws_nrw.davapi.model.dav.Response();
		response.setStatus(Propstat.PROP_STATUS_404_NOT_FOUND);
		this.setParameterResourceId(resourceUID);
		response.getHref().add(uri);
		return response;
	}


	/**
	 * Generiert ein Response-Objekt für einen angegebenen Eintrag.
	 *
	 * @param eintrag       Eintrag, zu dem Informationen zurückgeliefert werden
	 *                      sollen
	 * @param req Prop aus dem Request. Definiert, welche Informationen
	 *                      zur Ressource zurückgeliefert werden sollen.
	 * @return Response-Objekt zum angegebenen KalenderEintrag
	 */
	private de.svws_nrw.davapi.model.dav.Response genReportEntryResponse(final KalenderEintrag eintrag, final Prop req) {
		this.setParameterResourceId(eintrag.uid);

		final Prop resp = new Prop();
		final DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(req);

		// Erstelle den Kalendereintrag als VCALENDAR
		if (dynamicPropUtil.getIsFieldRequested(CalendarData.class)) {
			final CalendarData calendarData = new CalendarData();
			final VCalendar vCalendar = VCalendar.createVCalendar(eintrag);
			calendarData.getContent().add(vCalendar.serialize());
			resp.setCalendarData(calendarData);
		}

		// Das Entity-Tag, um Änderungen am Eintrag zu erkennen (dient der Synchronisation des Eintrages)
		if (dynamicPropUtil.getIsFieldRequested(Getetag.class)) {
			final Getetag getetag = new Getetag();
			getetag.getContent().add(eintrag.version);
			resp.setGetetag(getetag);
		}

		// Der Content-Type des Eintrags
		if (dynamicPropUtil.getIsFieldRequested(Getcontenttype.class)) {
			final Getcontenttype getcontenttype = new Getcontenttype();
			getcontenttype.getContent().add("text/calendar");
			resp.setGetcontenttype(getcontenttype);
		}

		return createResponse(req, resp, getKalenderResourceUri());
	}


	/**
	 * Erstellt eine HTTP-Response für eine PUT-HTTP-Anfrage für einen Kalender-Eintrag.
	 *
	 * Erstellt einen neuen Kalendereintrag oder aktualisiert einen vorhandenen.
	 * Welcher dieser beiden Fälle vorliegt wird anhand der Parameter "If-None-Match"
	 * und "If-Match" entschieden. Ist "If-Match" gesetzt, so wird der vorhandene Kalendereintrag
	 * bei gleicher UID aktualisiert, sofern der Client die aktuellste Version
	 * des Kalender-Eintrags hatte.
	 *
	 * @param idCal      die Ressourcen-ID für den Kalender
	 * @param uid        die URI für den Kalender-Eintrag
	 * @param nonMatch   der "If-None-Match"-Header der HTTP-Anfrage
	 * @param match      der "If-Match"-Header der HTTP-Anfrage
	 *
	 * @return die HTTP-Response
	 */
	public jakarta.ws.rs.core.Response putEntry(final String idCal, final String uid, final String nonMatch, final String match) {
		logRequest("CalDAV->putEntry", "idCal=" + idCal, "uid=" + uid, "nonMatch=" + nonMatch, "match=" + match);

		this.setParameterResourceCollectionId(idCal);
		this.setParameterResourceId(uid);

		final boolean isCreate = (nonMatch != null) && "*".equals(nonMatch);
		final boolean isUpdate = (match != null) && !match.isBlank();
		if (!isCreate && !isUpdate)
			return buildBadRequest(new BadRequestException("Ungültige Anfrage"));

		// Lese den Payload: Dies ist kein xml, sondern direkt der Kalendereintrag, also die .ics-Datei ...
		final String vCalPayload = new String(requestBody, StandardCharsets.UTF_8);
		final VCalendar vCalendar = VCalendar.parse(vCalPayload);

		// Erstelle den Kalender-Eintrag
		final KalenderEintrag eintrag = new KalenderEintrag();
		eintrag.data = vCalPayload;
		eintrag.kalenderId = idCal;
		eintrag.uid = uid;
		eintrag.kalenderEnde = DateTimeUtil.toSQLTimeStamp(vCalendar.getMaxDTEnd());
		eintrag.kalenderStart = DateTimeUtil.toSQLTimeStamp(vCalendar.getMinDTStart());
		eintrag.kalenderTyp = vCalendar.getTyp().name();
		// Wenn ein Update stattfindet, so trage das zuvor bestimmte eTag aus der Anfrage unter version ein
		final String eTag = isCreate ? null : adjustETags(match);
		if (eTag != null)
			eintrag.version = eTag;

		// Speichert den Kalender-Eintrag und gibt die neue Version des Eintrags als Entity-Tag zurück
		final String version = this.repo.persistEintrag(eintrag);
		if (version == null)
			return buildResponse(createResourceNotFoundError("Zugriff auf %s nicht möglich.".formatted(getKalenderResourceUri())));
		final EntityTag entityTag = new EntityTag(version);
		if (isCreate)
			return buildCreatedResponse(entityTag);
		return buildNoContentResponse(entityTag);
	}


	/**
	 * Erstellt eine HTTP-Response für eine DELETE-HTTP-Anfrage für einen Kalender-Eintrag.
	 * Der zu entfernende Kalendereintrag wird über die URI und den "If-Match"-Header der Anfrage
	 * identifiziert wird.
	 *
	 * @param idCal      die Ressourcen-ID für den Kalender
	 * @param uid        die URI für den Kalender-Eintrag
	 * @param match      der "If-Match"-Header der HTTP-Anfrage
	 *
	 * @return die HTTP-Response
	 */
	public jakarta.ws.rs.core.Response deleteEntry(final String idCal, final String uid, final String match) {
		logRequest("CalDAV->deleteEntry", "idCal=" + idCal, "uid=" + uid, "match=" + match);

		this.setParameterResourceCollectionId(idCal);
		this.setParameterResourceId(uid);

		String errorMessage;
		try {
			if (repo.deleteKalenderEintrag(idCal, uid, Long.valueOf(adjustETags(match))))
				return buildNoContentResponse();
			errorMessage = "DavRessource<" + uid + "> in Collection<" + idCal + "> nicht gefunden.";
		} catch (final Exception e) {
			errorMessage = e.getMessage();
		}

		final var response = new de.svws_nrw.davapi.model.dav.Response();
		response.setError(createResourceNotFoundError(errorMessage));
		response.getHref().add(getKalenderResourceUri());

		final Multistatus ms = new Multistatus();
		ms.getResponse().add(response);
		return buildResponse(ms);
	}


	/**
	 * Erstellt eine HTTP-Response für eine DELETE-HTTP-Anfrage für einen Kalender.
	 *
	 * @param idCal      die Ressourcen-ID für den Kalender
	 *
	 * @return die HTTP-Response
	 */
	public jakarta.ws.rs.core.Response deleteCalendar(final String idCal) {
		logRequest("CalDAV->deleteCalendar", "idCal=" + idCal);

		this.setParameterResourceCollectionId(idCal);

		// Im Moment wird noch kein Löschen von Kalendern unterstützt -> Implementierung kann ggf. noch erfolgen
		// Der Status NO_CONTENT zeigt dann ein erfolgreiches Löschen des Kalender an: buildNoContentResponse
		final String errorMessage = "Das Löschen von Ressourcensammlungen ist nicht erlaubt.";
		final var response = new de.svws_nrw.davapi.model.dav.Response();
		response.setError(createResourceNotFoundError(errorMessage));
		response.getHref().add(getKalenderResourceUri());

		final Multistatus ms = new Multistatus();
		ms.getResponse().add(response);
		return buildResponse(ms);
	}


	/**
	 * Loggt einen Request mit seinen Parametern und dem Body auf dem globalen Logger.
	 *
	 * @param methodName   der Name der Request-Methode aus dieser Klasse
	 * @param params       die Parameter des Request, welche der Methode übergeben wurden.
	 */
	protected void logRequest(final String methodName, final String... params) {
		if (LOG_REQUESTS) {
			final Logger logger = Logger.global();
			logger.log(LogLevel.WARNING, methodName);
			for (final String s : params)
				logger.log(LogLevel.WARNING, s);
			logger.log(methodName + "\n");
			logger.log(LogLevel.WARNING, new String(requestBody, StandardCharsets.UTF_8));
		}
	}

}
