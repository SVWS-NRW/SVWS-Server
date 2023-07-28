package de.svws_nrw.davapi.api;

import java.util.Optional;

import de.svws_nrw.davapi.data.IDavRepository;
import de.svws_nrw.davapi.model.dav.Error;
import de.svws_nrw.davapi.model.dav.Multistatus;
import de.svws_nrw.davapi.model.dav.Response;

/**
 * Dispatcher-Klasse für die Verarbeitung von Requests auf das DAV-API mittels
 * der HTTP-Methode DELETE auf eine DavRessource.
 *
 */
public class DeleteRessourceDispatcher extends DavDispatcher {

	/** das DavRepository */
	private final IDavRepository repository;
	/** die URI-Parameter für die aktuelle Anfrage */
	private final DavUriParameter uriParameter;

	/**
	 * Konstruktor mit Repository für Datenbankzugriff und URI-Parametern zum
	 * erstellen von URIs in Antworten
	 *
	 * @param repository   das DavRepository
	 * @param uriParameter die URI-Parameter
	 */
	public DeleteRessourceDispatcher(final IDavRepository repository, final DavUriParameter uriParameter) {
		this.repository = repository;
		this.uriParameter = uriParameter;
	}

	/**
	 * Verarbeitet eine gegebene Löschanfrage zu einer bestimmten ID, sofern das vom
	 * Client gegebene ifMatchToken dem Synctoken in der Datenbank entspricht, also
	 * der Client die aktuellste Version der Ressource kennt
	 *
	 * @param ressourceCollectionId die ID der Ressourcensammlung
	 * @param ressourceUID          die UID der DavRessource, welche gelöscht werden
	 *                              soll
	 * @param ifMatchToken          das Synctoken, welches dem Client bekannt war
	 * @return ein Multistatus als Antwort auf die Anfrage, gefüllt mit
	 *         Informationen, falls die Anfrage nicht erfolgreich war
	 */
	public Optional<Multistatus> dispatch(final String ressourceCollectionId, final String ressourceUID, final String ifMatchToken) {
		uriParameter.setResourceCollectionId(ressourceCollectionId);
		uriParameter.setResourceId(ressourceUID);
		try {
			if (repository.deleteRessourceIfUpToDate(Long.valueOf(ressourceCollectionId), ressourceUID,
					Long.valueOf(adjustETags(ifMatchToken)))) {
				return Optional.empty();
			}
			return createMultiStatus(
					createResourceNotFoundError("DavRessource<" + ressourceUID + "> in Collection<"
							+ ressourceCollectionId + "> nicht gefunden."),
					DavUriBuilder.getCalendarEntryUri(uriParameter));
		} catch (final Exception e) {
			return createMultiStatus(createResourceNotFoundError(e.getMessage()),
					DavUriBuilder.getCalendarEntryUri(uriParameter));
		}

	}

	/**
	 * Ansatz einer Methode zum Löschen von Ressourcensammlungen, gibt derzeit nur
	 * einen Fehler zurück
	 *
	 * @param ressourceCollectionID die ID der Ressourcensammlung, die gelöscht
	 *                              werden soll
	 * @return ein Multistatus-Objekt mit Fehlermeldungen, falls das Löschen
	 *         fehlgeschlagen ist.
	 */
	public Optional<Multistatus> dispatch(final String ressourceCollectionID) {
		// im Moment kein Löschen von RessourceCollections erlaubt
		final Error e = new Error();
		e.setAny("Das Löschen von Ressourcensammlungen ist nicht erlaubt.");
		uriParameter.setResourceCollectionId(ressourceCollectionID);
		return createMultiStatus(e, DavUriBuilder.getCalendarUri(uriParameter));
	}

	/**
	 * Hilfsmethode zum erstellen eines Multistatus-Objekts auf Basis eines
	 * gegebenen Fehlerobjekts und einer URI
	 *
	 * @param error das Fehlerobjekt
	 * @param href  die URI zur Anfrage, welche Fehlerhaft war
	 * @return ein Multistatusobjekt, welches den Fehler und die URI enthält
	 */
	private static Optional<Multistatus> createMultiStatus(final Error error, final String href) {
		final Multistatus ms = new Multistatus();
		final Response response = new Response();
		response.setError(error);
		response.getHref().add(href);
		ms.getResponse().add(response);
		return Optional.of(ms);
	}
}
