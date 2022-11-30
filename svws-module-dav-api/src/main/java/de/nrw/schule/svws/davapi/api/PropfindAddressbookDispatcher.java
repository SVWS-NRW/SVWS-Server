package de.nrw.schule.svws.davapi.api;

import de.nrw.schule.svws.core.data.adressbuch.Adressbuch;
import de.nrw.schule.svws.core.data.adressbuch.AdressbuchEintrag;
import de.nrw.schule.svws.davapi.data.CollectionRessourceQueryParameters;
import de.nrw.schule.svws.davapi.data.IAdressbuchRepository;
import de.nrw.schule.svws.davapi.model.dav.Collection;
import de.nrw.schule.svws.davapi.model.dav.CurrentUserPrincipal;
import de.nrw.schule.svws.davapi.model.dav.CurrentUserPrivilegeSet;
import de.nrw.schule.svws.davapi.model.dav.Displayname;
import de.nrw.schule.svws.davapi.model.dav.Getetag;
import de.nrw.schule.svws.davapi.model.dav.Multistatus;
import de.nrw.schule.svws.davapi.model.dav.Prop;
import de.nrw.schule.svws.davapi.model.dav.Propfind;
import de.nrw.schule.svws.davapi.model.dav.Resourcetype;
import de.nrw.schule.svws.davapi.model.dav.Response;
import de.nrw.schule.svws.davapi.model.dav.SyncToken;
import de.nrw.schule.svws.davapi.model.dav.card.CardAddressDataType;
import de.nrw.schule.svws.davapi.model.dav.card.CardAddressbook;
import de.nrw.schule.svws.davapi.model.dav.card.SupportedAddressData;
import de.nrw.schule.svws.davapi.model.dav.cs.Getctag;
import de.nrw.schule.svws.davapi.util.XmlUnmarshallingUtil;

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
	private DavUriParameter uriParameter;

	/**
	 * Konstruktor für einen neuen Dispatcher mit Repository und den gegebenen
	 * UriParametern
	 *
	 * @param repository   das Repository zum Zugriff auf Adressbuecher der
	 *                     Datenbank
	 * @param uriParameter die UriParameter zum Erstellen von URIs
	 */
	public PropfindAddressbookDispatcher(IAdressbuchRepository repository, DavUriParameter uriParameter) {
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
	private Object dispatchCollection(Propfind propfind) {
		List<Adressbuch> adressbuecher = this.repository
				.getAvailableAdressbuecher(CollectionRessourceQueryParameters.EXCLUDE_RESSOURCES);
		if (adressbuecher.isEmpty()) {
			return this.createResourceNotFoundError(
					"Es wurden keine Adressbücher für den angemeldeten Benutzer gefunden!");
		}
		Multistatus ms = new Multistatus();
		for (Adressbuch adressbuch : adressbuecher) {
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
	public Object dispatch(ServletInputStream inputStream, String ressourceId) throws IOException {
		Propfind propfind = XmlUnmarshallingUtil.unmarshal(inputStream, Propfind.class);
		if (ressourceId == null || ressourceId.isBlank()) {
			// ohne Ressource ID wird die Liste der Adressbuecher zurückgegeben
			return dispatchCollection(propfind);
		}

		Optional<Adressbuch> adressbuch = this.repository.getAdressbuchById(ressourceId,
				CollectionRessourceQueryParameters.INCLUDE_RESSOURCES_EXCLUDE_PAYLOAD);
		if (adressbuch.isEmpty()) {
			return this.createResourceNotFoundError("Adressbuch mit der angegebenen Id wurde nicht gefunden!");
		}

		Multistatus ms = new Multistatus();
		ms.getResponse().add(this.generateResponseAddressbookLevel(adressbuch.get(), propfind.getProp()));
		for (AdressbuchEintrag eintrag : adressbuch.get().adressbuchEintraege) {
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
	private Response generateResponseAddressbookLevel(Adressbuch adressbuch, Prop propRequested) {
		DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		Prop prop200 = new Prop();
		uriParameter.setResourceCollectionId(adressbuch.id);

		if (dynamicPropUtil.getIsFieldRequested(Resourcetype.class)) {
			Resourcetype resourcetype = new Resourcetype();
			resourcetype.setCollection(new Collection());
			resourcetype.setAddressbook(new CardAddressbook());
			prop200.setResourcetype(resourcetype);
		}

		if (dynamicPropUtil.getIsFieldRequested(Displayname.class)) {
			Displayname displayname = new Displayname();
			displayname.getContent().add(adressbuch.displayname);
			prop200.setDisplayname(displayname);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrincipal.class)) {
			CurrentUserPrincipal principal = new CurrentUserPrincipal();
			principal.getHref().add(DavUriBuilder.getPrincipalUri(uriParameter));
			prop200.setCurrentUserPrincipal(principal);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			prop200.setCurrentUserPrivilegeSet(getReadOnlyPrivilegeSet());
		}

		if (dynamicPropUtil.getIsFieldRequested(Getctag.class)) {
			Getctag getctag = new Getctag();
			getctag.getContent().add(String.valueOf(adressbuch.synctoken));
			prop200.setGetctag(getctag);
		}

		if (dynamicPropUtil.getIsFieldRequested(SyncToken.class)) {
			SyncToken syncToken = new SyncToken();
			syncToken.getContent().add(String.valueOf(adressbuch.synctoken));
			prop200.setSyncToken(syncToken);
		}

		if (dynamicPropUtil.getIsFieldRequested(SupportedAddressData.class)) {
			SupportedAddressData supportedAddressData = new SupportedAddressData();
			CardAddressDataType addressDataType = new CardAddressDataType();
			addressDataType.setContentType("text/vcard");
			addressDataType.setVersion("4.0");
			supportedAddressData.getAddressDataTypes().add(addressDataType);
			prop200.setSupportedAddressData(supportedAddressData);
		}

		Response response = createResponse(propRequested, prop200);
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
	private Response generateResponseContactLevel(AdressbuchEintrag eintrag, Prop propRequested) {
		DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		Prop prop200 = new Prop();

		uriParameter.setResourceCollectionId(eintrag.adressbuchId);
		uriParameter.setResourceId(eintrag.id);

		if (dynamicPropUtil.getIsFieldRequested(Resourcetype.class)) {
			Resourcetype resourcetype = new Resourcetype();
			prop200.setResourcetype(resourcetype);
		}

		if (dynamicPropUtil.getIsFieldRequested(Getetag.class)) {
			Getetag getetag = new Getetag();
			/*
			 * TODO: "Echten" Versions-E-Tag vergeben. Mit dem folgenden Code werden immer
			 * alle Kontakte als "geändert" angesehen. Derzeit gibt es im SVWS-Datenmodell
			 * keine Änderungshistorie. Insofern kann nicht ermittelt werden, ob ein Kontakt
			 * geändert wurde oder nicht. Es ist ineffizient alle Kontakte immer als
			 * "geändert" anzusehen.
			 */
			UUID uuid = UUID.randomUUID();
			getetag.getContent().add(uuid.toString());
			prop200.setGetetag(getetag);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			prop200.setCurrentUserPrivilegeSet(getReadOnlyPrivilegeSet());
		}

		Response response = createResponse(propRequested, prop200);
		response.getHref().add(DavUriBuilder.getAddressEntryUri(uriParameter));
		return response;
	}

}
