package de.svws_nrw.davapi.data;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import de.svws_nrw.core.data.kalender.Kalender;
import jakarta.validation.constraints.NotNull;

/**
 * Schnittstelle für Adressbuch Repositories. Diese Repositories kapseln den
 * Zugriff auf Adressbuchdaten.
 *
 */
public interface IKalenderRepository {

	/**
	 * Ermittelt ein Kalender über eine Kalender-Id.
	 *
	 * @param kalenderId Id des Kalenders
	 * @param params     QueryParameter zum Filtern des Inhalts des Kalenders
	 * @return Optional eines Kalenders oder Optional.empty(), falls kein Kalender
	 *         mit der angegebenen Id gefunden werden konnte.
	 */
	Optional<Kalender> getKalenderById(String kalenderId, CollectionRessourceQueryParameters params);

	/**
	 * Ermittelt eine Liste aller für den angemeldeten Benutzer verfügbaren
	 * Adressbücher.
	 *
	 * @param params QueryParameter zum Filtern des Inhalts der Adressbuecher
	 * @return Liste verfügbarer Adressbücher.
	 */
	@NotNull List<Kalender> getAvailableKalender(CollectionRessourceQueryParameters params);

	/**
	 * Sucht die gelöschten Ressourcen-UIDs einer Ressourcensammlung seit einem
	 * bestimmten Zeitpunkt
	 *
	 * @param id              die ID der Ressourcensammlung
	 * @param syncTokenMillis der Zeitpunkt als Millisekunden seit 1970,
	 *                        {@link Timestamp#getTime()}
	 * @return eine Liste der Ressourcen-UIDs, welche seit dem gesuchten Zeitpunkt
	 *         als gelöscht markiert wurden
	 */
	List<String> getDeletedResourceUIDsSince(String id, Long syncTokenMillis);

}
