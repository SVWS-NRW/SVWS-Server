package de.svws_nrw.davapi.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import de.svws_nrw.core.data.kalender.Kalender;
import de.svws_nrw.core.data.kalender.KalenderEintrag;
import de.svws_nrw.davapi.data.CollectionRessourceQueryParameters;
import de.svws_nrw.davapi.data.IKalenderRepository;
import de.svws_nrw.davapi.model.dav.Getcontenttype;
import de.svws_nrw.davapi.model.dav.Getetag;
import de.svws_nrw.davapi.model.dav.Multistatus;
import de.svws_nrw.davapi.model.dav.Prop;
import de.svws_nrw.davapi.model.dav.Propstat;
import de.svws_nrw.davapi.model.dav.Response;
import de.svws_nrw.davapi.model.dav.SyncCollection;
import de.svws_nrw.davapi.model.dav.cal.CalendarData;
import de.svws_nrw.davapi.model.dav.cal.CalendarMultiget;
import de.svws_nrw.davapi.model.dav.cal.CalendarQuery;
import de.svws_nrw.davapi.model.dav.cal.CompFilter;
import de.svws_nrw.davapi.model.dav.cal.TimeRange;
import de.svws_nrw.davapi.util.XmlUnmarshallingUtil;
import de.svws_nrw.davapi.util.icalendar.DateTimeUtil;
import de.svws_nrw.davapi.util.icalendar.VCalendar;
import jakarta.validation.constraints.NotNull;

/**
 * Dispatcher-Klasse für die Verarbeitung von Requests auf das DAV-API mittels
 * der HTTP-Methode REPORT auf die Ressource Kalender.
 */
public class ReportCalendarDispatcher extends DavDispatcher {

	/** Repository-Klasse zur Abfrage von Kalendern aus der SVWS-Datenbank */
	private final IKalenderRepository repository;

	/** URI-Parameter für die Erzeugung von URIs des Ergebnisobjekts */
	private final DavUriParameter uriParameter;

	/**
	 * Erstellt einen neuen Dispatcher mit dem angegebenen Repository und
	 * URI-Parameter
	 *
	 * @param kalenderRepository das Repository für Kalender
	 * @param uriParameter       die URI-Parameter für im Response verwendete URIs
	 */
	public ReportCalendarDispatcher(final IKalenderRepository kalenderRepository, final DavUriParameter uriParameter) {
		this.repository = kalenderRepository;
		this.uriParameter = uriParameter;
	}

	/**
	 * Verarbeitet den gegebenen Inputstream als CalDav-Protokollanfrage und
	 * versucht eine Anfrage als Multiget, SyncCollection oder CalendarQuery zu
	 * interpretieren und entsprechende Antwortobjekte zu erstellen.
	 *
	 * @param inputStream           der Request-Inputstream
	 * @param ressourceCollectionId die Ressourcensammlung-Id
	 * @return ein Antwortobjekt für die Reportanfrage, abhängig vom Request
	 * @throws IOException beim Verarbeiten des Inputstreams
	 */
	public Object dispatch(final InputStream inputStream, final String ressourceCollectionId) throws IOException {
		final Optional<Kalender> kalender = this.repository.getKalenderById(ressourceCollectionId,
				CollectionRessourceQueryParameters.INCLUDE_RESSOURCES_INCLUDE_PAYLOAD);
		if (kalender.isEmpty()) {
			return this.createResourceNotFoundError("Kalender mit der angegebenen Id wurde nicht gefunden!");
		}

		/*
		 * Ein REPORT-Request auf die Kalender-Resource kann als CalendarMultiget- und
		 * SyncCollection-Objekt formuliert werden. Der InputStream kann nur einmal
		 * gelesen werden. Daher erfolgt die Typ-Ermittlung nach dem try..error-Prinzip.
		 * Dazu wird zunächst ein Klon des InputStreams erstellt
		 */
		final ByteArrayOutputStream inputStreamAsByteArray = new ByteArrayOutputStream();
		inputStream.transferTo(inputStreamAsByteArray);
		try (InputStream inputStreamClone1 = new ByteArrayInputStream(inputStreamAsByteArray.toByteArray())) {
			//Versuche Multiget
			final CalendarMultiget multiget = XmlUnmarshallingUtil.unmarshal(inputStreamClone1,
					CalendarMultiget.class);
				inputStreamAsByteArray.close();
				return this.handleCalendarMultigetRequest(kalender.get(), multiget);
		} catch (IOException e1) {
			// Kein Multiget, versuche Sync-Collection
			try (InputStream inputStreamClone2 = new ByteArrayInputStream(inputStreamAsByteArray.toByteArray())) {
				final SyncCollection syncCollection = XmlUnmarshallingUtil.unmarshal(inputStreamClone2,
						SyncCollection.class);
					inputStreamAsByteArray.close();
					return this.handleSyncCollectionRequest(kalender.get(), syncCollection);
			} catch (Exception e2) {
				// Kein Sync-Collection, Versuche CalendarQuery
				try (InputStream inputStreamClone3 = new ByteArrayInputStream(inputStreamAsByteArray.toByteArray())) {
					final CalendarQuery calendarQuery = XmlUnmarshallingUtil.unmarshal(inputStreamClone3,
							CalendarQuery.class);
						inputStreamAsByteArray.close();
						return this.handleCalendarQueryRequest(kalender.get(), calendarQuery);
				} catch (Exception e3) {
					throw new UnsupportedOperationException("Weder Multiget noch Sync-Collection noch CalendarQuery bei REPORT Calendar: " + e3.getMessage(), e3);
				}
			}
		}
	}


	/**
	 * Ermittelt das Ergebnisobjekt für den Fall, dass ein
	 * "calendar-multiget"-Request gestellt wurde.
	 *
	 * @param kalender Kalender, zu dem die Informationen abgerufen werden sollen
	 * @param multiget Parameter des Requests. Diese steuern, welche Informationen
	 *                 im Einzelnen zu der Ressource zurückgeliefert werden sollen.
	 * @return Multistatus-Objekt
	 */
	private Multistatus handleCalendarMultigetRequest(final Kalender kalender, final CalendarMultiget multiget) {
		final Multistatus ms = new Multistatus();
		final List<KalenderEintrag> eintraegeByHrefs = this.getEintraegeByHrefs(kalender, multiget.getHref());
		uriParameter.setResourceCollectionId(kalender.id);
		for (final KalenderEintrag eintrag : eintraegeByHrefs) {
			ms.getResponse().add(this.generateResponseEventLevel(eintrag, multiget.getProp()));
		}
		return ms;
	}

	/**
	 * Ermittelt das Ergebnisobjekt für den Fall, dass ein "sync-collection"-Request
	 * gestellt wurde.
	 *
	 * @param kalender       Kalender, zu dem die Informationen abgerufen werden
	 *                       sollen
	 * @param syncCollection Parameter des Requests. Diese steuern, welche
	 *                       Informationen im Einzelnen zu der Ressource
	 *                       zurückgeliefert werden sollen.
	 * @return Multistatus-Objekt
	 */
	private Multistatus handleSyncCollectionRequest(final Kalender kalender, final SyncCollection syncCollection) {
		final Multistatus ms = new Multistatus();
		uriParameter.setResourceCollectionId(kalender.id);
		final Long syncTokenMillis = syncCollection.getSyncToken().isBlank() ? 0
				: Long.valueOf(syncCollection.getSyncToken());
		for (final KalenderEintrag eintrag : this.getEintraegeBySyncToken(kalender.id, syncTokenMillis)) {
			ms.getResponse().add(this.generateResponseEventLevel(eintrag, syncCollection.getProp()));
		}
		final List<String> deletedResourceUIDsSince = repository.getDeletedResourceUIDsSince(kalender.id, syncTokenMillis);
		for (final String deletedResourceUID : deletedResourceUIDsSince) {
			ms.getResponse().add(this.generateResponseResourceNotFound(deletedResourceUID));
		}
		ms.setSyncToken(Long.toString(kalender.synctoken));
		return ms;
	}

	private Response generateResponseResourceNotFound(final String deletedResourceUID) {
		final Response r = new Response();
		r.setStatus(Propstat.PROP_STATUS_404_NOT_FOUND);
		uriParameter.setResourceId(deletedResourceUID);
		r.getHref().add(DavUriBuilder.getCalendarEntryUri(uriParameter));
		return r;
	}

	/**
	 * Ermittelt das Ergebnisobjekt für den Fall, dass ein "sync-collection"-Request
	 * gestellt wurde.
	 *
	 * @param kalender      Kalender, zu dem die Informationen abgerufen werden
	 *                      sollen
	 * @param calendarQuery Parameter des Requests. Diese steuern, welche
	 *                      Informationen im Einzelnen zu der Ressource
	 *                      zurückgeliefert werden sollen.
	 * @return Multistatus-Objekt
	 */
	private Multistatus handleCalendarQueryRequest(final Kalender kalender, final CalendarQuery calendarQuery) {
		final Multistatus ms = new Multistatus();
		uriParameter.setResourceCollectionId(kalender.id);
		for (final KalenderEintrag eintrag : this.getEintraegeByFilter(kalender.id, calendarQuery)) {
			ms.getResponse().add(this.generateResponseEventLevel(eintrag, calendarQuery.getProp()));
		}
		return ms;
	}

	/**
	 * Ermittelt eine Liste von angefragten KalenderEintraegen zu einem Kalender.
	 * Welche Eintraege im Einzelnen zurückgegeben werden sollen, wird im Request
	 * über die Angabe von URIs der Eintrag-Ressourcen gesteuert.
	 *
	 * @param kalender     Kalender, aus dem KalenderEintraege ermittelt werden
	 *                     sollen.
	 * @param eintragHrefs List von URIs zu den angefragen Eintrag-Ressourcen im
	 *                     Kalender.
	 * @return Liste von Eintraegen.
	 */
	private List<KalenderEintrag> getEintraegeByHrefs(@NotNull final Kalender kalender, final List<String> eintragHrefs) {
		uriParameter.setResourceCollectionId(kalender.id);
		return kalender.kalenderEintraege.stream().filter(k -> {
			uriParameter.setResourceId(k.uid);
			return eintragHrefs.isEmpty() || eintragHrefs.contains(DavUriBuilder.getCalendarEntryUri(uriParameter));
		}).toList();
	}

	/**
	 * Ermittelt eine Liste von geänderten Eintraegen zu einem Kalender ab einem
	 * bestimmten "Aufsetzpunkt" (Differenzdaten). Diese Funktion dient der
	 * Synchronisation von Kalendern mit dem aufrufenden Client. Die Methode liefert
	 * alle Eintraege eines Kalenders zurück, die sich nach dem angegebenen des
	 * Sync-Tokens serverseitig geändert haben oder neu hinzugekommen sind.
	 *
	 * @param kalenderId Id des Kalenders, aus dem Eintraege ermittelt werden
	 *                   sollen.
	 * @param syncToken  Sync-Token bzw. Aufsetzpunkt zur Abfrage von
	 *                   Differenzdaten.
	 * @return Liste von Eintraegen.
	 */
	private List<KalenderEintrag> getEintraegeBySyncToken(final String kalenderId, final Long syncToken) {
		final Optional<Kalender> kalenderById = this.repository.getKalenderById(kalenderId,
				CollectionRessourceQueryParameters.INCLUDE_RESSOURCES_INCLUDE_PAYLOAD);
		if (kalenderById.isEmpty()) {
			return Collections.emptyList();
		}
		return kalenderById.get().kalenderEintraege.stream().filter(e -> Long.valueOf(e.version) > syncToken).toList();
	}

	/**
	 * Ermittelt eine Liste von geänderten Eintraegen zu einem Kalender über einen
	 * dynamischen Filter, der im Calendar-Query Request definiert werden kann.
	 *
	 * @param kalenderId    Id des Kalenders, aus dem Einträge ermittelt werden
	 *                      sollen.
	 * @param calendarQuery Calendar-Query, enthält Filterkriterien für die Abfrage
	 *                      von Kalender-Einträgen (z.B. innerhalb eines
	 *                      Zeitintervalls)
	 * @return Liste von Einträgen.
	 */
	private List<KalenderEintrag> getEintraegeByFilter(final String kalenderId, final CalendarQuery calendarQuery) {
		final Optional<Kalender> kalenderById = this.repository.getKalenderById(kalenderId,
				CollectionRessourceQueryParameters.INCLUDE_RESSOURCES_INCLUDE_PAYLOAD);
		if (kalenderById.isEmpty()) {
			return Collections.emptyList();
		}
		final Predicate<? super @NotNull KalenderEintrag> eintragFilter = getEintragFilter(calendarQuery);
		return kalenderById.get().kalenderEintraege.stream().filter(eintragFilter).toList();
	}

	/**
	 * gibt das Filterprädikat für die gegebene Kalenderquery zurück. vgl.
	 * https://datatracker.ietf.org/doc/html/rfc4791#section-9.7.1
	 *
	 * @param calendarQuery das CalendarQuery, für das der Filter erstellt werden
	 *                      soll
	 * @return ein Prädikat zum Filtern von Kalendereinträgen auf Basis des
	 *         CalendarQuery
	 */
	private static Predicate<? super @NotNull KalenderEintrag> getEintragFilter(final CalendarQuery calendarQuery) {
		Predicate<KalenderEintrag> ressourceTypePredicate = e -> true;
		Predicate<KalenderEintrag> componentTypePredicate = e -> true;
		Predicate<KalenderEintrag> timeRangePredicate = e -> true;
		if (calendarQuery != null && calendarQuery.getFilter() != null
				&& calendarQuery.getFilter().getCompFilter() != null) {

			CompFilter filter = calendarQuery.getFilter().getCompFilter();
			// Filter für die Art der Ressource, bspw. VCALENDAR (andere
			// Einträge haben wir nicht)
			final String ressourceTypeFilter = filter.getName();
			if (ressourceTypeFilter != null) {
				ressourceTypePredicate = e -> e.data != null && e.data.startsWith("BEGIN:" + ressourceTypeFilter);
			}

			if (filter.getCompFilter() != null) {
				filter = filter.getCompFilter();
				final String componentTypeFilter = filter.getName();
				if (componentTypeFilter != null) {
					// component type, bspw VEVENT, VTODO, VFREEBUSY oder VTIMEZONE
					componentTypePredicate = e -> e.data != null && e.data.contains("BEGIN:" + componentTypeFilter);
				}
				if (filter.getTimeRange() != null && filter.getTimeRange().getStart() != null
						&& filter.getTimeRange().getEnd() != null) {
					final TimeRange timeRange = filter.getTimeRange();
					final Instant timeRangeMin = DateTimeUtil.parseCalDav(timeRange.getStart());
					final Instant timeRangeMax = DateTimeUtil.parseCalDav(timeRange.getEnd());
					timeRangePredicate = e -> DateTimeUtil.intersect(timeRangeMin, timeRangeMax,
							DateTimeUtil.fromSqlTimeStamp(e.kalenderStart),
							DateTimeUtil.fromSqlTimeStamp(e.kalenderEnde));
				}
			}
		}
		return ressourceTypePredicate.and(componentTypePredicate).and(timeRangePredicate);
	}

	/**
	 * Generiert ein Response-Objekt für einen angegebenen Eintrag.
	 *
	 * @param eintrag       Eintrag, zu dem Informationen zurückgeliefert werden
	 *                      sollen
	 * @param propRequested Prop aus dem Request. Definiert, welche Informationen
	 *                      zur Ressource zurückgeliefert werden sollen.
	 * @return Response-Objekt zum angegebenen KalenderEintrag
	 */
	private Response generateResponseEventLevel(final KalenderEintrag eintrag, final Prop propRequested) {
		final DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		uriParameter.setResourceId(eintrag.uid);

		final Prop prop200 = new Prop();
		if (dynamicPropUtil.getIsFieldRequested(CalendarData.class)) {
			final CalendarData calendarData = new CalendarData();
			final VCalendar vCalendar = VCalendar.createVCalendar(eintrag);
			calendarData.getContent().add(vCalendar.serialize());
			prop200.setCalendarData(calendarData);
		}

		if (dynamicPropUtil.getIsFieldRequested(Getetag.class)) {
			final Getetag getetag = new Getetag();
			getetag.getContent().add(eintrag.version);
			prop200.setGetetag(getetag);
		}
		if (dynamicPropUtil.getIsFieldRequested(Getcontenttype.class)) {
			final Getcontenttype getcontenttype = new Getcontenttype();
			getcontenttype.getContent().add("text/calendar");
			prop200.setGetcontenttype(getcontenttype);
		}

		final Response response = createResponse(propRequested, prop200);
		response.getHref().add(DavUriBuilder.getCalendarEntryUri(uriParameter));
		return response;
	}

}
