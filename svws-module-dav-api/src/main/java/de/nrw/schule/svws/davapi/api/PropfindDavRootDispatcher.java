package de.nrw.schule.svws.davapi.api;

import de.nrw.schule.svws.davapi.model.dav.Collection;
import de.nrw.schule.svws.davapi.model.dav.CurrentUserPrincipal;
import de.nrw.schule.svws.davapi.model.dav.CurrentUserPrivilegeSet;
import de.nrw.schule.svws.davapi.model.dav.Multistatus;
import de.nrw.schule.svws.davapi.model.dav.Prop;
import de.nrw.schule.svws.davapi.model.dav.Propfind;
import de.nrw.schule.svws.davapi.model.dav.Resourcetype;
import de.nrw.schule.svws.davapi.model.dav.Response;
import de.nrw.schule.svws.davapi.util.XmlUnmarshallingUtil;

import javax.servlet.ServletInputStream;
import java.io.IOException;

/**
 * Dispatcher-Klasse für die Verarbeitung von Requests auf das DAV-API mittels
 * der HTTP-Methode PROPFIND auf die Ressource Adressbuch.
 */
public class PropfindDavRootDispatcher extends DavDispatcher {

	/** URI-Parameter zum Erzeugen von URIs für dieses Adressbuch */
	private CardDavUriParameter uriParameter;

	/**
	 * Konstruktor für einen neuen Dispatcher mit Repository und den gegebenen
	 * UriParametern
	 *
	 * @param uriParameter die UriParameter zum Erstellen von URIs
	 */
	public PropfindDavRootDispatcher(CardDavUriParameter uriParameter) {
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
	public Object dispatchCollection(ServletInputStream inputStream) throws IOException {
		Propfind propfind = XmlUnmarshallingUtil.unmarshal(inputStream, Propfind.class);

		Multistatus ms = new Multistatus();
		ms.getResponse().add(this.generateResponseRootLevel(propfind.getProp()));
		ms.getResponse().add(this.generateResponseAddressbooksLevel(propfind.getProp()));
		return ms;
	}

	private Response generateResponseRootLevel(Prop propRequested) {
		DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		Prop prop200 = new Prop();

		if (dynamicPropUtil.getIsFieldRequested(Resourcetype.class)) {
			Resourcetype resourcetype = new Resourcetype();
			resourcetype.setCollection(new Collection());
			prop200.setResourcetype(resourcetype);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrincipal.class)) {
			CurrentUserPrincipal principal = new CurrentUserPrincipal();
			principal.getHref().add(CardDavUriBuilder.getPrincipalUri(this.uriParameter));
			prop200.setCurrentUserPrincipal(principal);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			prop200.setCurrentUserPrivilegeSet(this.getReadOnlyPrivilegeSet());
		}

		Response response = createResponse(propRequested, prop200);
		response.getHref().add(CardDavUriBuilder.getCardDavRootUri(this.uriParameter));
		return response;
	}

	private Response generateResponseAddressbooksLevel(Prop propRequested) {
		DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		Prop prop200 = new Prop();

		if (dynamicPropUtil.getIsFieldRequested(Resourcetype.class)) {
			Resourcetype resourcetype = new Resourcetype();
			resourcetype.setCollection(new Collection());
			prop200.setResourcetype(resourcetype);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrincipal.class)) {
			CurrentUserPrincipal principal = new CurrentUserPrincipal();
			principal.getHref().add(CardDavUriBuilder.getPrincipalUri(uriParameter));
			prop200.setCurrentUserPrincipal(principal);
		}

		if (dynamicPropUtil.getIsFieldRequested(CurrentUserPrivilegeSet.class)) {
			prop200.setCurrentUserPrivilegeSet(this.getReadOnlyPrivilegeSet());
		}

		Response response = createResponse(propRequested, prop200);
		response.getHref().add(CardDavUriBuilder.getAddressbookCollectionUri(uriParameter));
		return response;
	}
}
