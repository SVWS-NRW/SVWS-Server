package de.nrw.schule.svws.davapi.api;

import de.nrw.schule.svws.core.data.adressbuch.Adressbuch;
import de.nrw.schule.svws.core.data.adressbuch.AdressbuchEintrag;
import de.nrw.schule.svws.davapi.data.AdressbuchQueryParameters;
import de.nrw.schule.svws.davapi.data.IAdressbuchRepository;
import de.nrw.schule.svws.davapi.model.dav.Getetag;
import de.nrw.schule.svws.davapi.model.dav.Multistatus;
import de.nrw.schule.svws.davapi.model.dav.Prop;
import de.nrw.schule.svws.davapi.model.dav.Response;
import de.nrw.schule.svws.davapi.model.dav.SyncCollection;
import de.nrw.schule.svws.davapi.model.dav.card.AddressbookMultiget;
import de.nrw.schule.svws.davapi.model.dav.card.CardAddressData;
import de.nrw.schule.svws.davapi.util.XmlUnmarshallingUtil;
import de.nrw.schule.svws.davapi.util.vcard.VCard;

import javax.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.NotNull;

/**
 * Dispatcher-Klasse für die Verarbeitung von Requests auf das DAV-API mittels
 * der HTTP-Methode REPORT auf die Ressource Adressbuch.
 */
public class ReportAddressbookDispatcher extends DavDispatcher {

	/** Repository-Klasse zur Abfrage von Adressbüchern aus der SVWS-Datenbank */
	private final IAdressbuchRepository repository;

	/** URI-Parameter für die Erzeugung von URIs des Ergebnisobjekts */
	private final CardDavUriParameter uriParameter;

	/**
	 * Erstellt einen neuen Dispatcher mit den angegebenen Repositorys und
	 * URI-Parametern
	 * 
	 * @param adressbuchRepository das Repository für Adressbuecher
	 * @param uriParameter         die URI-Parameter für im Response verwendete URIs
	 */
	public ReportAddressbookDispatcher(final IAdressbuchRepository adressbuchRepository,
			final CardDavUriParameter uriParameter) {
		this.repository = adressbuchRepository;
		this.uriParameter = uriParameter;
	}

	/**
	 * Verarbeitet einen InputStream mit XMl-Request für die gegebene Ressourcen-ID
	 * des Adressbuchs
	 * 
	 * @param inputStream der InputStream mit dem enthaltenen XML-Request
	 * @param ressourceId die ID des Adressbuchs
	 * @return die entsprechend des Requests verarbeitete Datenstruktur, welche das
	 *         Antwort-XML repräsentiert.
	 * @throws IOException bei der Verarbeitung des InputStreams/XML-Unmarshalling
	 */
	public Object dispatch(ServletInputStream inputStream, String ressourceId) throws IOException {
		AdressbuchQueryParameters queryParameters = new AdressbuchQueryParameters();
		queryParameters.includeAdressbuchEintraege = true;
		queryParameters.includeAdressbuchEintragIDsOnly = false;
		Optional<Adressbuch> adressbuch = this.repository.getAdressbuchById(ressourceId, queryParameters);
		if (adressbuch.isEmpty()) {
			return this.createResourceNotFoundError("Adressbuch mit der angegebenen Id wurde nicht gefunden!");
		}

		/*
		 * Ein REPORT-Request auf die Adressbuch-Resource kann als AddessbookMultiget-
		 * und SyncCollection-Objekt formuliert werden. Der InputStream kann nur einmal
		 * gelesen werden. Daher erfolgt die Typ-Ermittlung nach dem try..error-Prinzip.
		 * Dazu wird zunächst ein Klon des InputStreams erstellt
		 */
		ByteArrayOutputStream inputStreamAsByteArray = new ByteArrayOutputStream();
		inputStream.transferTo(inputStreamAsByteArray);
		try (InputStream inputStreamClone1 = new ByteArrayInputStream(inputStreamAsByteArray.toByteArray())) {
			Optional<AddressbookMultiget> multiget = XmlUnmarshallingUtil.tryUnmarshal(inputStreamClone1,
					AddressbookMultiget.class);
			inputStreamAsByteArray.close();
			if (multiget.isPresent()) {
				return this.handleAdressbookMultigetRequest(adressbuch.get(), multiget.get());
			}
		}

		try (InputStream inputStreamClone2 = new ByteArrayInputStream(inputStreamAsByteArray.toByteArray())) {
			Optional<SyncCollection> syncCollection = XmlUnmarshallingUtil.tryUnmarshal(inputStreamClone2,
					SyncCollection.class);
			inputStreamAsByteArray.close();
			if (syncCollection.isPresent()) {
				return this.handleSyncCollectionRequest(adressbuch.get(), syncCollection.get());
			}
		}

		// Input muss entweder ein Adressbook-Multiget oder ein Sync-Collection Request
		// sein
		throw new UnsupportedOperationException();
	}

	/**
	 * Ermittelt das Ergebnisobjekt für den Fall, dass ein
	 * "addressbook-multiget"-Request gestellt wurde.
	 * 
	 * @param adressbuch Adressbuch, zu dem die Informationen abgerufen werden
	 *                   sollen
	 * @param multiget   Parameter des Requests. Diese steuern, welche Informationen
	 *                   im Einzelnen zu der Ressource zurückgeliefert werden
	 *                   sollen.
	 * @return Multistatus-Objekt
	 */
	private Multistatus handleAdressbookMultigetRequest(Adressbuch adressbuch, AddressbookMultiget multiget) {
		Multistatus ms = new Multistatus();
		List<AdressbuchEintrag> eintraegeByHrefs = this.getEintraegeByHrefs(adressbuch, multiget.getHref());
		uriParameter.setAdressbuchId(adressbuch.id);
		for (AdressbuchEintrag eintrag : eintraegeByHrefs) {
			ms.getResponse().add(this.generateResponseContactLevel(eintrag, multiget.getProp()));
		}
		return ms;
	}

	/**
	 * Ermittelt das Ergebnisobjekt für den Fall, dass ein "sync-collection"-Request
	 * gestellt wurde.
	 * 
	 * @param adressbuch     Adressbuch, zu dem die Informationen abgerufen werden
	 *                       sollen
	 * @param syncCollection Parameter des Requests. Diese steuern, welche
	 *                       Informationen im Einzelnen zu der Ressource
	 *                       zurückgeliefert werden sollen.
	 * @return Multistatus-Objekt
	 */
	private Multistatus handleSyncCollectionRequest(Adressbuch adressbuch, SyncCollection syncCollection) {
		Multistatus ms = new Multistatus();
		uriParameter.setAdressbuchId(adressbuch.id);
		for (AdressbuchEintrag eintrag : this.getEintraegeBySyncToken(adressbuch.id, syncCollection.getSyncToken())) {
			ms.getResponse().add(this.generateResponseContactLevel(eintrag, syncCollection.getProp()));
		}
		ms.setSyncToken(Integer.toString(adressbuch.synctoken));
		return ms;
	}

	/**
	 * Ermittelt eine Liste von angefragten AdressbuchEintraegen zu einem
	 * Adressbuch. Welche Eintraege im Einzelnen zurückgegeben werden sollen, wird
	 * im Request über die Angabe von URIs der Eintrag-Ressourcen gesteuert.
	 * 
	 * @param adressbuch   Adressbuch, aus dem AdressbuchEintraege ermittelt werden
	 *                     sollen.
	 * @param eintragHrefs List von URIs zu den angefragen Eintrag-Ressourcen im
	 *                     Adressbuch.
	 * @return Liste von Eintraegen.
	 */
	private List<AdressbuchEintrag> getEintraegeByHrefs(@NotNull Adressbuch adressbuch, List<String> eintragHrefs) {
		uriParameter.setAdressbuchId(adressbuch.id);
		adressbuch.adressbuchEintraege.forEach(this::modifyEintragUri);
		return adressbuch.adressbuchEintraege.stream().filter(k -> eintragHrefs.contains(k.uri)).toList();
	}

	/**
	 * Ermittelt eine Liste von geänderten Eintraegen zu einem Adressbuch ab einem
	 * bestimmten "Aufsetzpunkt" (Differenzdaten). Diese Funktion dient der
	 * Synchronisation von Adressbüchern mit dem aufrufenden Client. Die Methode
	 * liefert alle Eintraege eines Adressbuchs zurück, die sich nach dem
	 * angegebenen des Sync-Tokens serverseitig geändert haben oder neu
	 * hinzugekommen sind.
	 * 
	 * @param adressbuchId Id des Adressbuchs, aus dem Eintraege ermittelt werden
	 *                     sollen.
	 * @param syncToken    Sync-Token bzw. Aufsetzpunkt zur Abfrage von
	 *                     Differenzdaten.
	 * @return Liste von Eintraegen.
	 */
	private List<AdressbuchEintrag> getEintraegeBySyncToken(String adressbuchId, String syncToken) {
		// TODO: Filterung über Sync-Token ergänzen
		AdressbuchQueryParameters queryParameters = new AdressbuchQueryParameters();
		queryParameters.includeAdressbuchEintraege = true;
		queryParameters.includeAdressbuchEintragIDsOnly = false;
		Optional<Adressbuch> adressbuchById = this.repository.getAdressbuchById(adressbuchId, queryParameters);
		if (adressbuchById.isEmpty()) {
			return Collections.emptyList();
		}
		return adressbuchById.get().adressbuchEintraege.stream().toList();
	}

	/**
	 * Generiert/Ergänzt die URI des DAV-APIs zu einer angegebenen Eintrag-Ressource
	 * 
	 * @param eintrag Eintrag (ohne URI)
	 * @return eintrag Eintrag mit URI
	 */
	private AdressbuchEintrag modifyEintragUri(AdressbuchEintrag eintrag) {
		uriParameter.setAdressbuchEintragId(eintrag.id);
		eintrag.uri = (CardDavUriBuilder.getAdressEntryUri(uriParameter));
		return eintrag;
	}

	/**
	 * Generiert ein Response-Objekt für einen angegebenen Eintrag.
	 * 
	 * @param eintrag       Eintrag, zu dem Informationen zurückgeliefert werden
	 *                      sollen
	 * @param propRequested Prop aus dem Request. Definiert, welche Informationen
	 *                      zur Ressource zurückgeliefert werden sollen.
	 * @return Response-Objekt zum angegebenen AdressbuchEintrag
	 */
	private Response generateResponseContactLevel(AdressbuchEintrag eintrag, Prop propRequested) {
		DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		uriParameter.setAdressbuchEintragId(eintrag.id);

		Prop prop200 = new Prop();
		if (dynamicPropUtil.getIsFieldRequested(CardAddressData.class)) {
			CardAddressData addressData = new CardAddressData();
			VCard vCard = VCard.createVCard(eintrag);
			addressData.getContent().add(vCard.serialize());
			prop200.setAddressData(addressData);
		}

		if (dynamicPropUtil.getIsFieldRequested(Getetag.class)) {
			Getetag getetag = new Getetag();
			getetag.getContent().add(eintrag.version);
			prop200.setGetetag(getetag);
		}

		Response response = createResponse(propRequested, prop200);
		response.getHref().add(CardDavUriBuilder.getAdressEntryUri(uriParameter));
		return response;
	}

}
