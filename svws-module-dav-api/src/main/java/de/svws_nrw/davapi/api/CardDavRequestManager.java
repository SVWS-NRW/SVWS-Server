package de.svws_nrw.davapi.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import de.svws_nrw.core.data.adressbuch.Adressbuch;
import de.svws_nrw.core.data.adressbuch.AdressbuchEintrag;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.davapi.data.carddav.CardDavAdressbuch;
import de.svws_nrw.davapi.model.dav.Collection;
import de.svws_nrw.davapi.model.dav.CurrentUserPrincipal;
import de.svws_nrw.davapi.model.dav.CurrentUserPrivilegeSet;
import de.svws_nrw.davapi.model.dav.Displayname;
import de.svws_nrw.davapi.model.dav.Getetag;
import de.svws_nrw.davapi.model.dav.Multistatus;
import de.svws_nrw.davapi.model.dav.Prop;
import de.svws_nrw.davapi.model.dav.Propfind;
import de.svws_nrw.davapi.model.dav.Resourcetype;
import de.svws_nrw.davapi.model.dav.SyncCollection;
import de.svws_nrw.davapi.model.dav.SyncToken;
import de.svws_nrw.davapi.model.dav.card.AddressbookMultiget;
import de.svws_nrw.davapi.model.dav.card.CardAddressData;
import de.svws_nrw.davapi.model.dav.card.CardAddressDataType;
import de.svws_nrw.davapi.model.dav.card.CardAddressbook;
import de.svws_nrw.davapi.model.dav.card.SupportedAddressData;
import de.svws_nrw.davapi.model.dav.cs.Getctag;
import de.svws_nrw.davapi.util.vcard.VCard;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse handhabt HTTP-Requests an der CardDAV-API des SVWS-Servers.
 */
public class CardDavRequestManager extends AbstractDavRequestManager {

	/** Das Adressbuch-Repository für den Datenbankzugriff */
	private final CardDavAdressbuch repo;


	/**
	 * Erstellt einen neuen Manager für CardDAV-HTTP-Requests über die angegebene Datenbankverbindung
	 *
	 * @param conn   die Datenbankverbindung
	 * @param is     der Input-Stream mit dem Body der Anfrage
	 *
	 * @throws IOException   wenn der Request-Body nicht gelesen werden kann
	 */
	public CardDavRequestManager(final @NotNull DBEntityManager conn, final InputStream is) throws IOException {
		super(conn, is);
		repo = new CardDavAdressbuch(conn);
	}


	/**
	 * Erstellt eine HTTP-Response für eine Propfind-HTTP-Anfrage auf die Adressbuchsammlung.
	 *
	 * @return die HTTP-Response
	 *
	 * @throws IOException  für den Fall, dass die Anfrage nicht erfolgreich deserialisiert werden kann
	 */
	public jakarta.ws.rs.core.Response propfindCollection() throws IOException {
		logRequest("CardDAV->propfindCollection");

		final Propfind propfind = mapper.readValue(requestBody, Propfind.class);

		final List<Adressbuch> adressbuecher = repo.getAvailableAdressbuecher();
		if (adressbuecher.isEmpty())
			return buildResponse(createResourceNotFoundError("Es wurden keine Adressbücher für den angemeldeten Benutzer gefunden!"));
		final Multistatus ms = new Multistatus();
		for (final Adressbuch adressbuch : adressbuecher)
			ms.getResponse().add(this.genPropfindAddressbookResponse(adressbuch, propfind.getProp()));
		return buildResponse(ms);
	}


	/**
	 * Erstellt eine HTTP-Response für eine Propfind-HTTP-Anfrage auf ein Adressbuch.
	 *
	 * @param idBook   die Ressourcen-ID für das Adressbuch
	 *
	 * @return die HTTP-Response
	 *
	 * @throws IOException             für den Fall, dass die Anfrage nicht erfolgreich deserialisiert werden kann
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public jakarta.ws.rs.core.Response propfindAddressbook(final String idBook) throws IOException, ApiOperationException {
		logRequest("CardDAV->propfindCalendar", "idBook=" + idBook);

		// Wurde keine gültige Ressource angegeben, so ist dies eigentlich ein Zugriff auf die Adressbuch-Sammlung
		if ((idBook == null) || idBook.isBlank())
			return propfindCollection();

		final Propfind propfind = mapper.readValue(requestBody, Propfind.class);

		final Adressbuch adressbuch = repo.getAdressbuchById(idBook, true, false);
		if (adressbuch == null)
			return buildResponse(createResourceNotFoundError("Adressbuch mit der angegebenen Id wurde nicht gefunden!"));

		final Multistatus ms = new Multistatus();
		ms.getResponse().add(this.genPropfindAddressbookResponse(adressbuch, propfind.getProp()));
		for (final AdressbuchEintrag eintrag : adressbuch.adressbuchEintraege) {
			eintrag.adressbuchId = adressbuch.id;
			ms.getResponse().add(this.genPropfindContactResponse(eintrag, propfind.getProp()));
		}
		return buildResponse(ms);
	}


	/**
	 * Generiert ein DAV-XML-Response-Objekt für ein Adressbuch.
	 *
	 * @param adressbuch   das Adressbuch, zu dem Informationen zurückgeliefert werden sollen
	 * @param req          das Prop-Objekt mit angefragten Informationen
	 *
	 * @return die XML-Response mit den Informationen zum Adressbuch
	 */
	private de.svws_nrw.davapi.model.dav.Response genPropfindAddressbookResponse(final Adressbuch adressbuch, final Prop req) {
		setParameterResourceCollectionId(adressbuch.id);

		final Prop resp = new Prop();
		final DynamicPropUtil propUtil = new DynamicPropUtil(req);

		// Der Typ der Ressource ist Collection und Adressbuch
		if (propUtil.getIsFieldRequested(Resourcetype.class)) {
			final Resourcetype resourcetype = new Resourcetype();
			resourcetype.setCollection(new Collection());
			resourcetype.setAddressbook(new CardAddressbook());
			resp.setResourcetype(resourcetype);
		}

		// Der Anzeigename
		if (propUtil.getIsFieldRequested(Displayname.class)) {
			final Displayname displayname = new Displayname();
			displayname.getContent().add(adressbuch.displayname);
			resp.setDisplayname(displayname);
		}

		// Der Benutzer der aktuellen Verbindung
		if (propUtil.getIsFieldRequested(CurrentUserPrincipal.class)) {
			final CurrentUserPrincipal principal = new CurrentUserPrincipal();
			principal.getHref().add(getBenutzerUri());
			resp.setCurrentUserPrincipal(principal);
		}

		// Die Privilegien des Benutzers der aktuellen Verbindungen auf das Adressbuch
		if (propUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class))
			resp.setCurrentUserPrivilegeSet(getPrivilegeSet(true, false));

		// Das Collection-Tag, um Änderungen am Adressbuchinhalt zu verfolgen (Verwendung eines Sync-Tokens, welches auch bei Änderungen am Inhalt angepasst werden muss)
		if (propUtil.getIsFieldRequested(Getctag.class)) {
			final Getctag getctag = new Getctag();
			getctag.getContent().add(String.valueOf(adressbuch.synctoken));
			resp.setGetctag(getctag);
		}

		// Der Sync-Token für das Adressbuch
		if (propUtil.getIsFieldRequested(SyncToken.class)) {
			final SyncToken syncToken = new SyncToken();
			syncToken.getContent().add(String.valueOf(adressbuch.synctoken));
			resp.setSyncToken(syncToken);
		}

		// Die unterstützten Formate für die Adressdaten
		if (propUtil.getIsFieldRequested(SupportedAddressData.class)) {
			final SupportedAddressData supportedAddressData = new SupportedAddressData();
			final CardAddressDataType addressDataType = new CardAddressDataType();
			addressDataType.setContentType("text/vcard");
			addressDataType.setVersion("4.0");
			supportedAddressData.getAddressDataTypes().add(addressDataType);
			resp.setSupportedAddressData(supportedAddressData);
		}

		return createResponse(req, resp, getCardDavResourceCollectionUri());
	}

	/**
	 * Generiert ein DAV-XML-Response-Objekt für einen Adressbuch-Eintrag.
	 *
	 * @param eintrag   der Adressbuch-Eintrag
	 * @param req       das Prop-Objekt mit angefragten Informationen
	 *
	 * @return die XML-Response mit den Informationen zum Adressbuch-Eintrag
	 */
	private de.svws_nrw.davapi.model.dav.Response genPropfindContactResponse(final AdressbuchEintrag eintrag, final Prop req) {
		this.setParameterResourceCollectionId(eintrag.adressbuchId);
		this.setParameterResourceId(eintrag.id);

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
			/*
			 * TODO: "Echten" Versions-E-Tag vergeben. Mit dem folgenden Code werden immer
			 * alle Kontakte als "geändert" angesehen. Derzeit gibt es im SVWS-Datenmodell
			 * keine Änderungshistorie. Insofern kann nicht ermittelt werden, ob ein Kontakt
			 * geändert wurde oder nicht. Es ist ineffizient alle Kontakte immer als
			 * "geändert" anzusehen.
			 */
			final UUID uuid = UUID.randomUUID();
			getetag.getContent().add(uuid.toString());
			resp.setGetetag(getetag);
		}

		// Die Privilegien des Benutzers der aktuellen Verbindungen auf den Kalender-Eintrag
		if (propUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class))
			resp.setCurrentUserPrivilegeSet(getPrivilegeSet(true, false));

		return createResponse(req, resp, getCardDavResourceUri());
	}



	/**
	 * Erstellt eine HTTP-Response für eine Report-HTTP-Anfrage auf ein Adressbuch.
	 * Anfragen mit den Typen AdressbookMultiget und SyncCollection
	 *
	 * @param idBook   die Ressourcen-ID für das Adressbuch
	 *
	 * @return die HTTP-Response
	 *
	 * @throws IOException             für den Fall, dass die Anfrage nicht erfolgreich deserialisiert werden kann
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public jakarta.ws.rs.core.Response reportAddressbook(final String idBook) throws IOException, ApiOperationException {
		logRequest("CardDAV->reportCalendar", "idBook=" + idBook);

		// Bestimme die Kalenderdaten aus der Datenbank und gebe einen Fehler zurück, falls kein Kalender mit der ID gefunden wurde
		final Adressbuch adressbuch = this.repo.getAdressbuchById(idBook, true, true);
		if (adressbuch == null)
			return buildResponse(this.createResourceNotFoundError("Adressbuch mit der angegebenen ID wurde nicht gefunden!"));
		this.setParameterResourceCollectionId(adressbuch.id);

		// Prüfe, ob es sich um eine Anfrage mit dem Typ CalendarMultiget handelt
		final AddressbookMultiget adressbuchMultiget = deserialiseAddressbookMultiget(requestBody);
		if (adressbuchMultiget != null)
			return reportAddressbookMultigetRequest(adressbuch, adressbuchMultiget);

		// Prüfe, ob es sich um eine Anfrage mit dem Typ SyncCollection handelt
		final SyncCollection syncCollection = deserialiseSyncCollection(requestBody);
		if (syncCollection != null)
			return reportSyncCollectionRequest(adressbuch, syncCollection);

		throw new UnsupportedOperationException(
				"Es werden nur die Typen AddressbookMultiget und SyncCollection für die Methode REPORT bei einem Adressbuch unterstützt.");
	}


	/**
	 * Deserialisiert eine AdressbookMultiget-XML-Anfrage
	 *
	 * @param data   die xml-Daten als Byte-Array
	 *
	 * @return die deserialisierte Anfrage
	 */
	private AddressbookMultiget deserialiseAddressbookMultiget(final byte[] data) {
		try {
			return mapper.readValue(data, AddressbookMultiget.class);
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
	 * Erstellt eine HTTP-Response für eine Report-HTTP-Anfrage des Typs AddressbookMultiget auf ein Adressbuch.
	 *
	 * @param adressbuch   das Adressbuch
	 * @param multiget     die Anfrage vom Typ AddressbookMultiget mit den angeforderten Einträgen als HRefs
	 *
	 * @return die HTTP-Response
	 */
	private jakarta.ws.rs.core.Response reportAddressbookMultigetRequest(final Adressbuch adressbuch, final AddressbookMultiget multiget) {
		// Bestimme zunächst die Adressbuch-Einträge anhand der HRefs aus der Anfrage.
		final List<String> hrefs = multiget.getHref();
		final List<AdressbuchEintrag> eintraege = new ArrayList<>();
		for (final AdressbuchEintrag a : adressbuch.adressbuchEintraege) {
			this.setParameterResourceId(a.id);
			a.uri = getCardDavResourceUri();
			if (!hrefs.isEmpty() && !hrefs.contains(a.uri))
				continue;
			eintraege.add(a);
		}

		// Generiere die Response mit den Adressbuch-Einträgen
		final Multistatus ms = new Multistatus();
		for (final AdressbuchEintrag eintrag : eintraege)
			ms.getResponse().add(this.genReportContactResponse(eintrag, multiget.getProp()));
		return buildResponse(ms);
	}


	/**
	 * Erstellt eine HTTP-Response für eine Report-HTTP-Anfrage des Typs SyncCollection auf ein Adressbuch.
	 * Es wird eine Liste von geänderten Eintraegen ab einem bestimmten "Aufsetzpunkt" (Differenzdaten) ermittelt.
	 * Diese Funktion dient der Synchronisation von Adressbüchern mit dem aufrufenden Client.
	 * Die Methode liefert alle Eintraege eines Adressbuchs zurück, die sich nach dem angegebenen des Sync-Tokens
	 * serverseitig geändert haben oder neu hinzugekommen sind und NOT-FOUND für die Einträge, die entfernt wurden.
	 *
	 * @param adressbuch       das Adressbuch
	 * @param syncCollection   die Anfrage vom Typ SyncCollection mit dem SyncToken
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private jakarta.ws.rs.core.Response reportSyncCollectionRequest(final Adressbuch adressbuch, final SyncCollection syncCollection)
			throws ApiOperationException {
		// Bestimme den Sync-Token
		// TODO final Long syncTokenMillis = syncCollection.getSyncToken().isBlank() ? 0 : Long.valueOf(syncCollection.getSyncToken());

		// Bestimme die Einträge mit Änderungen seit diesem Sync-Token und die UIDs der entfernten Einträge
		final Adressbuch adressbuchById = this.repo.getAdressbuchById(adressbuch.id, true, true);
		// TODO: Filterung über Sync-Token ergänzen - siehe auch CalDAV-Lösung
		final List<AdressbuchEintrag> eintraege = (adressbuchById == null) ? Collections.emptyList()
				: adressbuchById.adressbuchEintraege.stream().toList();

		// Generiere die Response mit den Adressbuch-Einträgen und den UIDs der entfernten Einträge
		final Multistatus ms = new Multistatus();
		for (final AdressbuchEintrag eintrag : eintraege)
			ms.getResponse().add(this.genReportContactResponse(eintrag, syncCollection.getProp()));
		// TODO UIDs der entfernten Einträge - siehe CalDAV-Lösung
		ms.setSyncToken(Long.toString(adressbuch.synctoken));
		return buildResponse(ms);
	}


	/**
	 * Generiert ein Response-Objekt für dev angegebenen Eintrag.
	 *
	 * @param eintrag   der Eintrag, zu dem Informationen zurückgeliefert werden sollen
	 * @param req       die Props aus dem Request. Definiert, welche Informationen zur Ressource zurückgeliefert werden sollen.
	 *
	 * @return Response-Objekt zum angegebenen AdressbuchEintrag
	 */
	private de.svws_nrw.davapi.model.dav.Response genReportContactResponse(final AdressbuchEintrag eintrag, final Prop req) {
		this.setParameterResourceId(eintrag.id);

		final Prop resp = new Prop();
		final DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(req);

		// Erstelle den Kalendereintrag als VCARD
		if (dynamicPropUtil.getIsFieldRequested(CardAddressData.class)) {
			final CardAddressData addressData = new CardAddressData();
			final VCard vCard = VCard.createVCard(eintrag);
			addressData.getContent().add(vCard.serialize());
			resp.setAddressData(addressData);
		}

		// Das Entity-Tag, um Änderungen am Eintrag zu erkennen (dient der Synchronisation des Eintrages)
		if (dynamicPropUtil.getIsFieldRequested(Getetag.class)) {
			final Getetag getetag = new Getetag();
			getetag.getContent().add(eintrag.version);
			resp.setGetetag(getetag);
		}

		return createResponse(req, resp, getCardDavResourceUri());
	}



	/**
	 * Erstellt eine HTTP-Response für eine Report-HTTP-Anfrage auf einen Adressbuch-Eintrag.
	 * Anfragen mit den Typen AdressbookMultiget und SyncCollection
	 *
	 * @param idBook      die Ressourcen-ID für das Adressbuch
	 * @param idContact   die ID des Kontaktes
	 *
	 * @return die HTTP-Response
	 *
	 * @throws IOException           für den Fall, dass die Anfrage nicht erfolgreich deserialisiert werden kann
	 * @throws ApiOperationException im Fehlerfall
	 */
	public jakarta.ws.rs.core.Response reportContact(final String idBook, final String idContact) throws IOException, ApiOperationException {
		logRequest("CardDAV->reportCalendar", "idBook=" + idBook, "idContact=" + idContact);

		// TODO Erzeuge eine Response speziell für den Kontakt mit der übergebenen ID
		return this.reportAddressbook(idBook);
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
