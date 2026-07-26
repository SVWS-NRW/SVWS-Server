package de.svws_nrw.repo.gost.klausuren;

import java.util.List;

import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.data.stundenplan.DataStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.data.stundenplan.DataStundenplanPausenaufsichten;
import de.svws_nrw.data.stundenplan.DataStundenplanUnterricht;
import de.svws_nrw.data.stundenplan.DataStundenplanUnterrichtsverteilung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanRaum;
import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Repository für externe Stundenplan-Lesedaten der GOSt-Klausurplanung.
 *
 * TODO Diese Klasse ist eine Übergangskapsel, damit die Klausurplan-Services nicht direkt auf alte
 * DataStundenplan*-Klassen und deren DBEntityManager-basierte APIs zugreifen müssen. Sie kann entfernt werden, sobald
 * die benötigten Stundenplan-Lesedaten über eigene Stundenplan-Repositories bzw. DTO-nahe Stundenplan-Services
 * bereitgestellt werden:
 * - aktive Stundenpläne eines Schuljahresabschnitts
 * - StundenplanManager-Daten für Stundenplan, Unterricht, Pausenaufsichten und Unterrichtsverteilung
 * - Existenzprüfung für Stundenplanräume
 */
public final class GostKlausurenStundenplanDataRepository {

	private final DBEntityManager conn;

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn die aktuelle Datenbank-Verbindung
	 */
	public GostKlausurenStundenplanDataRepository(final DBEntityManager conn) {
		this.conn = conn;
	}

	/**
	 * Ermittelt die aktiven Stundenpläne eines Schuljahresabschnitts.
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 *
	 * @return die aktiven Stundenpläne
	 */
	public List<StundenplanListeEintrag> getStundenplaeneAktiv(final long idSchuljahresabschnitt) {
		return DataStundenplanListe.getStundenplaeneAktiv(conn, idSchuljahresabschnitt);
	}

	/**
	 * Erstellt den Stundenplanmanager.
	 *
	 * @param idStundenplan die ID des Stundenplans
	 *
	 * @return der Stundenplanmanager
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public StundenplanManager getStundenplanManager(final long idStundenplan) throws ApiOperationException {
		return new StundenplanManager(
				new DataStundenplan(conn).getById(idStundenplan),
				DataStundenplanUnterricht.getUnterrichte(conn, idStundenplan),
				DataStundenplanPausenaufsichten.getAufsichten(conn, idStundenplan),
				DataStundenplanUnterrichtsverteilung.getUnterrichtsverteilung(conn, idStundenplan));
	}

	/**
	 * Prüft, ob ein Stundenplanraum existiert.
	 *
	 * @param idStundenplanRaum die ID des Stundenplanraums
	 *
	 * @return true, falls der Raum existiert
	 */
	public boolean existsStundenplanRaum(final long idStundenplanRaum) {
		return conn.queryByKey(DTOStundenplanRaum.class, idStundenplanRaum) != null;
	}

}
