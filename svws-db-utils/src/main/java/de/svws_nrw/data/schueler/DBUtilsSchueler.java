package de.svws_nrw.data.schueler;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESAlgo;
import de.svws_nrw.core.data.schueler.Sprachendaten;
import de.svws_nrw.data.crypto.DBUtilsCrypto;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachpruefungen;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentials;
import de.svws_nrw.db.utils.ApiOperationException;


/**
 * Diese Klasse stellt Hilfsmethoden für den Zugriff auf Informationen
 * von Schülern zur Verfügung.
 */
public final class DBUtilsSchueler {

	private DBUtilsSchueler() {
		throw new IllegalStateException("Instantiation of " + DBUtilsSchueler.class.getName() + " not allowed");
	}


	/**
	 * Liest den AES-Schlüssel des Schülers aus der Datenbank ein und gibt das zugehörige AES-Crypto-Objekt zurück.
	 * Sollte noch kein AES-Schlüssel in der Datenbank vorhanden sein, so wird ein neuer Schlüssel angelegt.
	 *
	 * Für die Kommunikation mit der Datenbank wird die angegebene Verbindung genutzt, welche eine aktive Transaktion
	 * haben muss.
	 *
	 * @param conn   die Datenbank-Verbindung
	 * @param id     die ID des Schülers
	 *
	 * @return das AES-Crypto-Objekt zum Ver- und Entschlüsseln von Daten
	 *
	 * @throws ApiOperationException falls ein Fehler auftritt
	 */
	public static AES getOrCreateSchuelerAES(final DBEntityManager conn, final long id) throws ApiOperationException {
		final DTOCredentials cred = DBUtilsCrypto.getOrCreateSchuelerCredentials(conn, id);
		if (cred.AES == null)
			DBUtilsCrypto.addAESKey(conn, cred);
		return new AES(AESAlgo.CBC_PKCS5PADDING, AES.getKeyFromByteArray(Base64.getDecoder().decode(cred.AES)));
	}


	private static Sprachendaten dtoMapperSprachendaten(final DBEntityManager conn, final long idSchueler,
			final List<DTOSchuelerSprachenfolge> dtoSprachbelegungen, final List<DTOSchuelerSprachpruefungen> dtoSprachpruefungen) throws ApiOperationException {
		final Sprachendaten sprachendaten = new Sprachendaten();
		sprachendaten.schuelerID = idSchueler;
		for (final DTOSchuelerSprachenfolge dtoSprachbelegung : dtoSprachbelegungen) {
			if (dtoSprachbelegung.ASDJahrgangVon == null)
				continue;
			sprachendaten.belegungen.add(new DataSchuelerSprachbelegung(conn, idSchueler).map(dtoSprachbelegung));
		}
		for (final DTOSchuelerSprachpruefungen dtoSprachpruefung : dtoSprachpruefungen) {
			if ((dtoSprachpruefung.Sprache == null) || (dtoSprachpruefung.Anspruchsniveau == null)
					|| (!dtoSprachpruefung.IstHSUPruefung && !dtoSprachpruefung.IstFeststellungspruefung))
				continue;
			sprachendaten.pruefungen.add((new DataSchuelerSprachpruefung(conn, idSchueler)).map(dtoSprachpruefung));
		}
		return sprachendaten;
	}


	/**
	 * Bestimmt die Sprachbelegungen (Sprachenfolge) und die Sprachprüfungen für den Schüler mit der angegebenen ID aus
	 * den entsprechenden Tabellen in der Datenbank.
	 *
	 * @param conn   die Datenbank-Verbindung
	 * @param id     die ID des Schülers
	 *
	 * @return die Sprachendaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static Sprachendaten getSchuelerSprachendaten(final DBEntityManager conn, final long id) throws ApiOperationException {
		// Lese die Sprachbelegungen (Sprachenfolge) und die Sprachprüfungen aus der Datenbank ein
		final List<DTOSchuelerSprachenfolge> dtoSprachenfolge =
				conn.queryList(DTOSchuelerSprachenfolge.QUERY_BY_SCHUELER_ID, DTOSchuelerSprachenfolge.class, id);
		final List<DTOSchuelerSprachpruefungen> dtoSprachpruefungen =
				conn.queryList(DTOSchuelerSprachpruefungen.QUERY_BY_SCHUELER_ID, DTOSchuelerSprachpruefungen.class, id);
		// ... und gibt sie als Sprachendaten-Objekt zurück.
		return dtoMapperSprachendaten(conn, id, dtoSprachenfolge, dtoSprachpruefungen);
	}


	/**
	 * Bestimmt die Sprachbelegungen (Sprachenfolge) und die Sprachprüfungen für die Schüler mit der angegebenen IDs aus
	 * den entsprechenden Tabellen in der Datenbank.
	 *
	 * @param conn   die Datenbank-Verbindung
	 * @param ids    die IDs der Schüler
	 *
	 * @return die Sprachendaten der Schüler
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static List<Sprachendaten> getSchuelerSprachendaten(final DBEntityManager conn, final List<Long> ids) throws ApiOperationException {
		// Lese die Sprachbelegungen (Sprachenfolge) und die Sprachprüfungen aus der Datenbank ein
		final Map<Long, List<DTOSchuelerSprachenfolge>> mapSprachenfolgen =
				conn.queryList(DTOSchuelerSprachenfolge.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerSprachenfolge.class, ids)
						.stream().collect(Collectors.groupingBy(f -> f.Schueler_ID, Collectors.toList()));
		final Map<Long, List<DTOSchuelerSprachpruefungen>> mapSprachpruefungen =
				conn.queryList(DTOSchuelerSprachpruefungen.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerSprachpruefungen.class, ids)
						.stream().collect(Collectors.groupingBy(f -> f.Schueler_ID, Collectors.toList()));
		// ... und gibt sie als Sprachendaten-Objekte zurück.
		final List<Sprachendaten> result = new ArrayList<>();
		for (final Long id : ids)
			result.add(dtoMapperSprachendaten(conn, id, mapSprachenfolgen.computeIfAbsent(id, k -> new ArrayList<>()),
					mapSprachpruefungen.computeIfAbsent(id, k -> new ArrayList<>())));
		return result;
	}

}
