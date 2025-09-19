package de.svws_nrw.davapi.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
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
import de.svws_nrw.davapi.model.dav.card.AddressbookHomeSet;
import de.svws_nrw.db.DBEntityManager;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse handhabt allgemeine HTTP-Requests an der DAV-API des SVWS-Servers, welche
 * nicht unbedingt CardDAV- oder CalDAV-spezifisch sind.
 */
public class DavRequestManager extends AbstractDavRequestManager {

	/**
	 * Erstellt einen neuen Manager für DAV-HTTP-Requests über die angegebene Datenbankverbindung
	 *
	 * @param conn   die Datenbankverbindung
	 * @param is     der Input-Stream mit dem Body der Anfrage
	 *
	 * @throws IOException   wenn der Request-Body nicht gelesen werden kann
	 */
	public DavRequestManager(final @NotNull DBEntityManager conn, final InputStream is) throws IOException {
		super(conn, is);
	}


	/**
	 * Erstellt eine HTTP-Response für eine Propfind-HTTP-Anfrage auf die DAV-Root-Collection.
	 * Hier müssen dann ggf. die alle zugeordneten Sammlungen für den Benutzer zurückgegeben werden
	 * (Adressbücher und Kalender).
	 *
	 * @return die HTTP-Response
	 *
	 * @throws IOException  für den Fall, dass die Anfrage nicht erfolgreich deserialisiert werden kann
	 */
	public jakarta.ws.rs.core.Response propfindRootCollection() throws IOException {
		logRequest("DAV->propfindRootCollection");

		final Propfind propfind = mapper.readValue(requestBody, Propfind.class);

		final Multistatus ms = new Multistatus();
		ms.getResponse().add(this.genPropfindRootResponse(propfind.getProp()));
		ms.getResponse().add(this.genPropfindAddressbookCollectionResponse(propfind.getProp()));
		if (propfind.getProp().getCalendarHomeSet() != null)
			ms.getResponse().add(this.genPropfindCalendarCollectionResponse(propfind.getProp()));
		return buildResponse(ms);
	}


	/**
	 * Erzeugt für das Root-Verzeichnis der DAV-API des SVWS-Servers in Abhängigkeit der angefragten Properties.
	 *
	 * @param req   die angefragten Properties
	 *
	 * @return die Response mit den gefundenen und nicht-gefundenen Properties
	 */
	private de.svws_nrw.davapi.model.dav.Response genPropfindRootResponse(final Prop req) {
		final Prop resp = new Prop();
		final DynamicPropUtil propUtil = new DynamicPropUtil(req);

		if (propUtil.getIsFieldRequested(Resourcetype.class)) {
			final Resourcetype resourcetype = new Resourcetype();
			resourcetype.setCollection(new Collection());
			// auch wenn das root objekt eigentlich nur eine Sammlung mit den darunter
			// liegenden Sammlungen für Kalender und Adressbücher ist, benötigt thunderbird
			// hier das Principal objekt, da dieser Ordner quasi dem User gehört.
			// Ansonsten würde Thunderbird das Calendar-Home-Set nicht interpretieren,
			// vgl. Kommentar an #583
			resourcetype.setPrincipal(new Principal());
			resp.setResourcetype(resourcetype);
		}

		if (propUtil.getIsFieldRequested(CurrentUserPrincipal.class)) {
			final CurrentUserPrincipal principal = new CurrentUserPrincipal();
			principal.getHref().add(getBenutzerUri());
			resp.setCurrentUserPrincipal(principal);
		}

		if (propUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class))
			resp.setCurrentUserPrivilegeSet(getReadOnlyPrivilegeSet());

		if (propUtil.getIsFieldRequested(CalendarHomeSet.class)) {
			// Kalenderanfragen auf unserer Wurzel benötigen den Link zur Kalendersammlung unter dav/schema/kalender
			final CalendarHomeSet calendarHomeSet = new CalendarHomeSet();
			calendarHomeSet.getHref().add(getKalenderUri());
			resp.setCalendarHomeSet(calendarHomeSet);
		}

		return createResponse(req, resp, getRootUri());
	}


	/**
	 * Erzeugt ein Antwortobjekt auf dem Level, welches die Adressbücher enthält in Abhängigkeit
	 * von den angefragten Properties.
	 *
	 * @param req   die angefragten Properties
	 *
	 * @return die Response mit den gefundenen und nicht gefundenen Properties
	 */
	private de.svws_nrw.davapi.model.dav.Response genPropfindAddressbookCollectionResponse(final Prop req) {
		final Prop resp = new Prop();
		final DynamicPropUtil propUtil = new DynamicPropUtil(req);

		if (propUtil.getIsFieldRequested(Resourcetype.class)) {
			final Resourcetype resourcetype = new Resourcetype();
			resourcetype.setCollection(new Collection());
			resp.setResourcetype(resourcetype);
		}

		if (propUtil.getIsFieldRequested(CurrentUserPrincipal.class)) {
			final CurrentUserPrincipal principal = new CurrentUserPrincipal();
			principal.getHref().add(getBenutzerUri());
			resp.setCurrentUserPrincipal(principal);
		}

		if (propUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class))
			resp.setCurrentUserPrivilegeSet(getReadOnlyPrivilegeSet());

		return createResponse(req, resp, getCardDavUri());
	}

	/**
	 *
	 * Erzeugt ein Antwortobjekt auf dem Level, welches die Kalender enthält,
	 * abhängig von den angefragten Properties
	 *
	 * @param req die angefragten Properties
	 * @return ein Response-Objekt mit den gefundenen und nicht gefundenen
	 *         Properties
	 *
	 */
	private de.svws_nrw.davapi.model.dav.Response genPropfindCalendarCollectionResponse(final Prop req) {
		// thunderbird interpretiert diesen teil der Antwort einfach mal nicht
		final Prop resp = new Prop();
		final DynamicPropUtil propUtil = new DynamicPropUtil(req);

		if (propUtil.getIsFieldRequested(Resourcetype.class)) {
			final Resourcetype resourcetype = new Resourcetype();
			resourcetype.setCollection(new Collection());
			resp.setResourcetype(resourcetype);
		}

		if (propUtil.getIsFieldRequested(CurrentUserPrincipal.class)) {
			final CurrentUserPrincipal principal = new CurrentUserPrincipal();
			principal.getHref().add(getBenutzerUri());
			resp.setCurrentUserPrincipal(principal);
		}

		if (propUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class))
			resp.setCurrentUserPrivilegeSet(getReadOnlyPrivilegeSet());

		if (propUtil.getIsFieldRequested(CalendarHomeSet.class)) {
			final CalendarHomeSet calendarHomeSet = new CalendarHomeSet();
			calendarHomeSet.getHref().add(getKalenderUri());
			resp.setCalendarHomeSet(calendarHomeSet);
		}

		return createResponse(req, resp, getKalenderUri());
	}


	/**
	 * Erstellt eine HTTP-Response für eine Propfind-HTTP-Anfrage auf einen DAV-Benutzer-Principal.
	 *
	 * @param idUser   die ID des Benutzer-Principals
	 *
	 * @return die HTTP-Response
	 *
	 * @throws IOException  für den Fall, dass die Anfrage nicht erfolgreich deserialisiert werden kann
	 */
	public jakarta.ws.rs.core.Response propfindUserPrincipalCollection(final String idUser) throws IOException {
		logRequest("DAV->propfindUserPrincipalCollection");

		final Propfind propfind = mapper.readValue(requestBody, Propfind.class);

		final Multistatus ms = new Multistatus();
		ms.getResponse().add(this.genPropfindPrincipalResponse(idUser, propfind.getProp()));
		return buildResponse(ms);
	}


	/**
	 * Generiert ein Response-Objekt für einen angegebenen Benutzer (Principal).
	 *
	 * @param idUser   die ID des Benutzer-Principals, zu welchem Informationen zurückgegeben werden sollen
	 * @param req      die Anfrage-Properties
	 *
	 * @return die Response zu dem angegebenen Benutzer
	 */
	private Response genPropfindPrincipalResponse(final String idUser, final Prop req) {
		final String curIdUser = this.getParameterBenutzerId();
		if (!idUser.equals(curIdUser)) {
			// TODO: Fall behandeln, wenn der angemeldete Benutzer die Eigenschaften eines // anderen abfragt.
		}

		final Prop resp = new Prop();
		final DynamicPropUtil propUtil = new DynamicPropUtil(req);

		if (propUtil.getIsFieldRequested(AddressbookHomeSet.class)) {
			final AddressbookHomeSet addressbookHomeSet = new AddressbookHomeSet();
			addressbookHomeSet.getHref().add(getCardDavUri());
			resp.setAddressbookHomeSet(addressbookHomeSet);
		}

		if (propUtil.getIsFieldRequested(CalendarHomeSet.class)) {
			final CalendarHomeSet calendarHomeSet = new CalendarHomeSet();
			calendarHomeSet.getHref().add(getKalenderUri());
			resp.setCalendarHomeSet(calendarHomeSet);
		}

		return createResponse(req, resp, getBenutzerUri());
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
