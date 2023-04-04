package de.svws_nrw.davapi.api;

import de.svws_nrw.core.data.adressbuch.Adressbuch;
import de.svws_nrw.core.data.adressbuch.AdressbuchEintrag;
import de.svws_nrw.davapi.data.CollectionRessourceQueryParameters;
import de.svws_nrw.davapi.data.IAdressbuchRepository;
import de.svws_nrw.davapi.model.dav.Collection;
import de.svws_nrw.davapi.model.dav.CurrentUserPrincipal;
import de.svws_nrw.davapi.model.dav.CurrentUserPrivilegeSet;
import de.svws_nrw.davapi.model.dav.Displayname;
import de.svws_nrw.davapi.model.dav.Getetag;
import de.svws_nrw.davapi.model.dav.Multistatus;
import de.svws_nrw.davapi.model.dav.Prop;
import de.svws_nrw.davapi.model.dav.Propfind;
import de.svws_nrw.davapi.model.dav.Resourcetype;
import de.svws_nrw.davapi.model.dav.Response;
import de.svws_nrw.davapi.model.dav.SyncToken;
import de.svws_nrw.davapi.model.dav.card.CardAddressDataType;
import de.svws_nrw.davapi.model.dav.card.CardAddressbook;
import de.svws_nrw.davapi.model.dav.card.SupportedAddressData;
import de.svws_nrw.davapi.model.dav.cs.Getctag;
import de.svws_nrw.davapi.util.XmlUnmarshallingUtil;
import jakarta.servlet.ServletInputStream;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Dispatcher-Klasse für die Verarbeitung von Requests auf das DAV-API mittels
 * der HTTP-Methode PROPFIND auf die Ressource Adressbuch.
 */
public class PropfindAddressbookDispatcher extends DavDispatcher {

	/** Repository-Klasse zur Abfrage von Adressbüchern aus der SVWS-Datenbank */
	private final IAdressbuchRepository repository;
	/** URI-Parameter zum Erzeugen von URIs für dieses Adressbuch */
	private final DavUriParameter uriParameter;

	/**
	 * Konstruktor für einen neuen Dispatcher mit Repository und den gegebenen
	 * UriParametern
	 *
	 * @param repository   das Repository zum Zugriff auf Adressbuecher der
	 *                     Datenbank
	 * @param uriParameter die UriParameter zum Erstellen von URIs
	 */
	public PropfindAddressbookDispatcher(final IAdressbuchRepository repository, final DavUriParameter uriParameter) {
		this.repository = repository;
		this.uriParameter = uriParameter;
	}

	/**
	 * Interpretiert den gegebenen Propfind-Request als Anfrage zu allen
	 * Adressbuechern dieses Nutzers
	 *
	 * @param propfind das Request Propfind
	 * @return das Ergebnisobjekt für den Request
	 * @throws IOException
	 */
	private Object dispatchCollection(final Propfind propfind) {
		final List<Adressbuch> adressbuecher = this.repository
				.getAvailableAdressbuecher(CollectionRessourceQueryParameters.EXCLUDE_RESSOURCES);
		if (adressbuecher.isEmpty()) {
			return this.createResourceNotFoundError(
					"Es wurden keine Adressbücher für den angemeldeten Benutzer gefunden!");
		}
		final Multistatus ms = new Multistatus();
		for (final Adressbuch adressbuch : adressbuecher) {
			ms.getResponse().add(this.generateResponseAddressbookLevel(adressbuch, propfind.getProp()));
		}
		return ms;
	}

	/**
	 * Verarbeitung eines PropFind auf Adressbuchebene anhand des InputStreams und
	 * der gegebenen Ressource
	 *
	 * @param inputStream der InputStream mit dem zu verarbeitenden XML-Request
	 * @param ressourceId die Ressource für die dieser Request gilt
	 * @return die entsprechend des Requests verarbeitete Datenstruktur, welche das
	 *         Antwort-XML repräsentiert. Als Objekt zurückgegeben, da das
	 *         Zielobjekt vor der Verarbeitung nicht festgestellt wird
	 * @throws IOException bei Fehlern in der Streambehandlung/dem XML-Unmarshalling
	 */
	public Object dispatch(final ServletInputStream inputStream, final String ressourceId) throws IOException {
		final Propfind propfind = XmlUnmarshallingUtil.unmarshal(inputStream, Propfind.class);
		if (ressourceId == null || ressourceId.isBlank()) {
			// ohne Ressource ID wird die Liste der Adressbuecher zurückgegeben
			return dispatchCollection(propfind);
		}

		final Optional<Adressbuch> adressbuch = this.repository.getAdressbuchById(ressourceId,
				CollectionRessourceQueryParameters.INCLUDE_RESSOURCES_EXCLUDE_PAYLOAD);
		if (adressbuch.isEmpty()) {
			return this.createResourceNotFoundError("Adressbuch mit der angegebenen Id wurde nicht gefunden!");
		}

		final Multistatus ms = new Multistatus();
		ms.getResponse().add(this.generateResponseAddressbookLevel(adressbuch.get(), propfind.getProp()));
		for (final AdressbuchEintrag eintrag : adressbuch.get().adressbuchEintraege) {
			eintrag.adressbuchId = adressbuch.get().id;
			ms.getResponse().add(this.generateResponseContactLevel(eintrag, propfind.getProp()));
		}
		return ms;
	}

	/**
	 * Generiert ein Response-Objekt für ein angegebenes Adressbuch.
	 *
	 * @param adressbuch    Adressbuch, zu dem Informationen zurückgeliefert werden
	 *                      sollen
	 * @param propRequested Prop aus dem Request. Definiert, welche Informationen
	 *                      zur Ressource zurückgeliefert werden sollen.
	 * @return Response-Objekt zum angegebenen Adressbuch
	 */
	private Response generateResponseAddressbookLevel(final Adressbuch adressbuch, final Prop propRequested) {
		final DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		final Prop prop200 = new Prop();
		uriParameter.setResourceCollectionId(adressbuch.id);

		if (dynamicPropUtil.getIsFieldRequested(Resourcetype.class)) {
			final Resourcetype resourcetype = new Resourcetype();
			resourcetype.setCollection(new Collection());
			resourcetype.setAddressbook(new CardAddressbook());
			prop200.setResourcetype(resourcetype);
		}

		if (dynamicPropUtil.getIsFieldRequested(Displayname.class)) {
			final Displayname displayname = new Displayname();
			displayname.getContent().add(adressbuch.displayname);
			prop200.setDisplayname(displayname);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrincipal.class)) {
			final CurrentUserPrincipal principal = new CurrentUserPrincipal();
			principal.getHref().add(DavUriBuilder.getPrincipalUri(uriParameter));
			prop200.setCurrentUserPrincipal(principal);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			prop200.setCurrentUserPrivilegeSet(getReadOnlyPrivilegeSet());
		}

		if (dynamicPropUtil.getIsFieldRequested(Getctag.class)) {
			final Getctag getctag = new Getctag();
			getctag.getContent().add(String.valueOf(adressbuch.synctoken));
			prop200.setGetctag(getctag);
		}

		if (dynamicPropUtil.getIsFieldRequested(SyncToken.class)) {
			final SyncToken syncToken = new SyncToken();
			syncToken.getContent().add(String.valueOf(adressbuch.synctoken));
			prop200.setSyncToken(syncToken);
		}

		if (dynamicPropUtil.getIsFieldRequested(SupportedAddressData.class)) {
			final SupportedAddressData supportedAddressData = new SupportedAddressData();
			final CardAddressDataType addressDataType = new CardAddressDataType();
			addressDataType.setContentType("text/vcard");
			addressDataType.setVersion("4.0");
			supportedAddressData.getAddressDataTypes().add(addressDataType);
			prop200.setSupportedAddressData(supportedAddressData);
		}

		final Response response = createResponse(propRequested, prop200);
		response.getHref().add(DavUriBuilder.getAddressbookUri(uriParameter));
		return response;
	}

	/**
	 * Generiert ein Response-Objekt für einen angegebenen AdressbuchEintrag.
	 *
	 * @param eintrag       Eintrag, zu dem Informationen zurückgeliefert werden
	 *                      sollen
	 * @param propRequested Prop aus dem Request. Definiert, welche Informationen
	 *                      zur Ressource zurückgeliefert werden sollen.
	 * @return Response-Objekt zum angegebenen AdressbuchEintrag
	 */
	private Response generateResponseContactLevel(final AdressbuchEintrag eintrag, final Prop propRequested) {
		final DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		final Prop prop200 = new Prop();

		uriParameter.setResourceCollectionId(eintrag.adressbuchId);
		uriParameter.setResourceId(eintrag.id);

		if (dynamicPropUtil.getIsFieldRequested(Resourcetype.class)) {
			final Resourcetype resourcetype = new Resourcetype();
			prop200.setResourcetype(resourcetype);
		}

		if (dynamicPropUtil.getIsFieldRequested(Getetag.class)) {
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
			prop200.setGetetag(getetag);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			prop200.setCurrentUserPrivilegeSet(getReadOnlyPrivilegeSet());
		}

		final Response response = createResponse(propRequested, prop200);
		response.getHref().add(DavUriBuilder.getAddressEntryUri(uriParameter));
		return response;
	}

}
