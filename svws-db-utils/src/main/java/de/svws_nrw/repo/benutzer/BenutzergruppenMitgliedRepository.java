package de.svws_nrw.repo.benutzer;

import java.util.List;

import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppenMitglied;
import de.svws_nrw.repo.Repository;

/**
 * Repository-Interface für den Zugriff auf {@link DTOBenutzergruppenMitglied}-Entitäten.
 * Stellt domänenspezifische Abfragen zur Verwaltung von Benutzergruppenmitgliedschaften bereit.
 */
public interface BenutzergruppenMitgliedRepository extends Repository<DTOBenutzergruppenMitglied> {

	/**
	 * Gibt alle Mitgliedschaftseinträge zurück, die dem angegebenen Benutzer zugeordnet sind.
	 *
	 * @param idBenutzer die ID des Benutzers
	 *
	 * @return Liste der {@link DTOBenutzergruppenMitglied}-Einträge des Benutzers
	 */
	List<DTOBenutzergruppenMitglied> findByBenutzerId(long idBenutzer);

	/**
	 * Gibt alle Mitgliedschaftseinträge zurück, die der angegebenen Benutzergruppe zugeordnet sind.
	 *
	 * @param idBenutzergruppe die ID der Benutzergruppe
	 *
	 * @return Liste der {@link DTOBenutzergruppenMitglied}-Einträge der Benutzergruppe
	 */
	List<DTOBenutzergruppenMitglied> findByBenutzergruppeId(long idBenutzergruppe);

	/**
	 * Prüft, ob für die angegebene Benutzergruppe Gruppenrechte vorhanden sind.
	 * <p>
	 * Die Methode liefert {@code true}, wenn die Benutzergruppe mit der ID {@code idGruppe}
	 * über die erforderlichen Gruppenrechte verfügt (bzw. wenn entsprechende Rechte
	 * für diese Gruppe hinterlegt sind), andernfalls {@code false}.
	 * </p>
	 * @param idUser ID der Users
	 * @param idGruppe
	 *         ID der Benutzergruppe, für die die Rechte geprüft werden sollen
	 *
	 * @return {@code true}, wenn Gruppenrechte vorhanden sind, sonst {@code false}
	 */
	boolean hasGroupRights(long idUser, long idGruppe);

}
