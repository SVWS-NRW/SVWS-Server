package de.svws_nrw.repo.gost.klausuren;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.data.gost.DataGostFaecher;
import de.svws_nrw.data.gost.DataGostJahrgangSchuelerliste;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.data.lehrer.DataLehrerliste;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.data.schule.DataSchuljahresabschnitte;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.gost.klausuren.GostKlausurenAllDataService;

/**
 * Repository für externe Lesedaten der vollständigen GOSt-Klausurplanungsdaten.
 *
 * TODO Diese Klasse ist eine Übergangskapsel, damit {@link GostKlausurenAllDataService}
 * nicht direkt auf alte Data*-Klassen und deren DBEntityManager-basierte APIs zugreifen muss. Sie kann entfernt werden,
 * sobald die benötigten Lesedaten über eigene Repositories bzw. DTO-nahe Services bereitgestellt werden:
 * - Schüler eines GOSt-Jahrgangs
 * - GOSt-Fächer eines Abiturjahrgangs
 * - Schuljahresabschnitte nach Schuljahr und Abschnitt
 * - Kurse eines Schuljahresabschnitts
 * - Schülerlisten-Einträge anhand von Schüler-IDs
 * - Lehrerliste
 */
public final class GostKlausurenAllDataRepository {

	private final DBEntityManager conn;

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn die aktuelle Datenbank-Verbindung
	 */
	public GostKlausurenAllDataRepository(final DBEntityManager conn) {
		this.conn = conn;
	}

	/**
	 * Ermittelt die Schüler eines GOSt-Jahrgangs.
	 *
	 * @param abiturjahr der Abiturjahrgang
	 *
	 * @return die Schülerliste
	 */
	public List<SchuelerListeEintrag> getSchuelerByAbiturjahr(final int abiturjahr) {
		return new DataGostJahrgangSchuelerliste(conn, abiturjahr).getAllSchueler();
	}

	/**
	 * Ermittelt die GOSt-Fächer eines Abiturjahrgangs.
	 *
	 * @param abiturjahr der Abiturjahrgang
	 *
	 * @return die Fächer
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<GostFach> getFaecherByAbiturjahr(final int abiturjahr) throws ApiOperationException {
		return DataGostFaecher.getFaecherManager(conn, abiturjahr).faecher();
	}

	/**
	 * Ermittelt einen Schuljahresabschnitt.
	 *
	 * @param schuljahr das Schuljahr
	 * @param abschnitt der Abschnitt
	 *
	 * @return der Schuljahresabschnitt oder null
	 */
	public Schuljahresabschnitt getSchuljahresabschnitt(final int schuljahr, final int abschnitt) {
		return DataSchuljahresabschnitte.getFromSchuljahrUndAbschnitt(conn, schuljahr, abschnitt);
	}

	/**
	 * Ermittelt die Kurse eines Schuljahresabschnitts.
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 *
	 * @return die Kurse
	 */
	public List<KursDaten> getKurseBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		return DataKurse.getKursListenFuerAbschnitt(conn, idSchuljahresabschnitt, true);
	}

	/**
	 * Ermittelt Schüler anhand der IDs.
	 *
	 * @param schuljahr das Schuljahr
	 * @param schuelerIds die Schüler-IDs
	 *
	 * @return die Schülerliste
	 */
	public List<SchuelerListeEintrag> getSchuelerByIds(final int schuljahr, final List<Long> schuelerIds) {
		if (schuelerIds.isEmpty()) {
			return new ArrayList<>();
		}
		final List<DTOSchueler> schuelerListe = conn.queryList(DTOSchueler.QUERY_LIST_BY_ID, DTOSchueler.class, schuelerIds);
		return schuelerListe.stream().map(s -> DataSchuelerliste.erstelleSchuelerlistenEintrag(s, schuljahr, null, null, null)).toList();
	}

	/**
	 * Ermittelt die Lehrerliste.
	 *
	 * @return die Lehrerliste
	 */
	public List<LehrerListeEintrag> getLehrer() {
		return new DataLehrerliste(conn, null).getLehrerListe(false);
	}

}
