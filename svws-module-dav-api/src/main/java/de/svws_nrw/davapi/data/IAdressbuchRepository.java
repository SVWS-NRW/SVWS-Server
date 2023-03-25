package de.svws_nrw.davapi.data;

import java.util.List;
import java.util.Optional;

import de.svws_nrw.core.data.adressbuch.Adressbuch;

/**
 * Schnittstelle für Adressbuch Repositories. Diese Repositories kapseln den
 * Zugriff auf Adressbuchdaten.
 *
 */
public interface IAdressbuchRepository {

	/**
	 * Ermittelt ein Adressbuch über eine Adressbuch-Id.
	 *
	 * @param adressbuchId Id des Adressbuchs
	 * @param params       QueryParameter zum Filtern des Inhalts des Adressbuch
	 * @return Optional eines Adressbuchs oder Optional.empty(), falls kein
	 *         Adressbuch mit der angegebenen Id gefunden werden konnte.
	 */
	public Optional<Adressbuch> getAdressbuchById(String adressbuchId, CollectionRessourceQueryParameters params);

	/**
	 * Ermittelt eine Liste aller für den angemeldeten Benutzer verfügbaren
	 * Adressbücher.
	 *
	 * @param params QueryParameter zum Filtern des Inhalts der Adressbuecher
	 * @return Liste verfügbarer Adressbücher.
	 */
	public List<Adressbuch> getAvailableAdressbuecher(CollectionRessourceQueryParameters params);

}
