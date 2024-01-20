package de.svws_nrw.data.schueler;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESAlgo;
import de.svws_nrw.core.data.schueler.Sprachbelegung;
import de.svws_nrw.core.data.schueler.Sprachendaten;
import de.svws_nrw.core.data.schueler.Sprachpruefung;
import de.svws_nrw.data.crypto.DBUtilsCrypto;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachpruefungen;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentials;
import jakarta.ws.rs.WebApplicationException;

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
	 * @throws WebApplicationException falls ein Fehler auftritt
	 */
	public static AES getOrCreateSchuelerAES(final DBEntityManager conn, final long id) throws WebApplicationException {
		final DTOCredentials cred = DBUtilsCrypto.getOrCreateSchuelerCredentials(conn, id);
		if (cred.AES == null)
			DBUtilsCrypto.addAESKey(conn, cred);
		return new AES(AESAlgo.CBC_PKCS5PADDING, AES.getKeyFromByteArray(Base64.getDecoder().decode(cred.AES)));
	}


	private static Sprachbelegung dtoMapperSprachenfolge(final DTOSchuelerSprachenfolge dtoSprachbelegung) {
		final Sprachbelegung belegung = new Sprachbelegung();
		belegung.sprache = dtoSprachbelegung.Sprache;
		belegung.reihenfolge = dtoSprachbelegung.ReihenfolgeNr;
		belegung.belegungVonJahrgang = dtoSprachbelegung.ASDJahrgangVon;
		belegung.belegungVonAbschnitt = dtoSprachbelegung.AbschnittVon;
		belegung.belegungBisJahrgang = dtoSprachbelegung.ASDJahrgangBis;
		belegung.belegungBisAbschnitt = dtoSprachbelegung.AbschnittBis;
		if (dtoSprachbelegung.Referenzniveau != null) {
			belegung.referenzniveau = dtoSprachbelegung.Referenzniveau.daten.kuerzel;
		} else {
			belegung.referenzniveau = null;
		}
		return belegung;
	}


	private static Sprachpruefung dtoMapperSprachenpruefung(final DTOSchuelerSprachpruefungen dtoSprachpruefung) {
        final Sprachpruefung pruefung = new Sprachpruefung();
        pruefung.sprache = dtoSprachpruefung.Sprache;
        pruefung.anspruchsniveauId = dtoSprachpruefung.Anspruchsniveau.daten.id;
        pruefung.ersetzteSprache = dtoSprachpruefung.ErsetzteSprache;
        pruefung.jahrgang = dtoSprachpruefung.ASDJahrgang;
        pruefung.istHSUPruefung = dtoSprachpruefung.IstHSUPruefung;
        pruefung.istFeststellungspruefung = dtoSprachpruefung.IstFeststellungspruefung;
        pruefung.kannErstePflichtfremdspracheErsetzen = dtoSprachpruefung.KannErstePflichtfremdspracheErsetzen;
        pruefung.kannZweitePflichtfremdspracheErsetzen = dtoSprachpruefung.KannZweitePflichtfremdspracheErsetzen;
        pruefung.kannWahlpflichtfremdspracheErsetzen = dtoSprachpruefung.KannWahlpflichtfremdspracheErsetzen;
        pruefung.kannBelegungAlsFortgefuehrteSpracheErlauben = dtoSprachpruefung.KannBelegungAlsFortgefuehrteSpracheErlauben;
        pruefung.note = dtoSprachpruefung.NotePruefung == null ? null : dtoSprachpruefung.NotePruefung.getNoteSekI();
        if (dtoSprachpruefung.Referenzniveau != null) {
            pruefung.referenzniveau = dtoSprachpruefung.Referenzniveau.daten.kuerzel;
        } else {
            pruefung.referenzniveau = null;
        }
        return pruefung;
	}


	private static Sprachendaten dtoMapperSprachendaten(final long idSchueler, final List<DTOSchuelerSprachenfolge> dtoSprachbelegungen, final List<DTOSchuelerSprachpruefungen> dtoSprachpruefungen) {
        final Sprachendaten sprachendaten = new Sprachendaten();
        sprachendaten.schuelerID = idSchueler;
		for (final DTOSchuelerSprachenfolge dtoSprachbelegung : dtoSprachbelegungen) {
			if (dtoSprachbelegung.ASDJahrgangVon == null)
				continue;
			sprachendaten.belegungen.add(dtoMapperSprachenfolge(dtoSprachbelegung));
		}
        for (final DTOSchuelerSprachpruefungen dtoSprachpruefung : dtoSprachpruefungen) {
            if ((dtoSprachpruefung.Sprache == null) || (dtoSprachpruefung.Anspruchsniveau == null) || (!dtoSprachpruefung.IstHSUPruefung && !dtoSprachpruefung.IstFeststellungspruefung))
                continue;
            sprachendaten.pruefungen.add(dtoMapperSprachenpruefung(dtoSprachpruefung));
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
	 */
	public static Sprachendaten getSchuelerSprachendaten(final DBEntityManager conn, final long id) {
        // Lese die Sprachbelegungen (Sprachenfolge) und die Sprachprüfungen aus der Datenbank ein
        final List<DTOSchuelerSprachenfolge> dtoSprachenfolge = conn.queryNamed("DTOSchuelerSprachenfolge.schueler_id", id, DTOSchuelerSprachenfolge.class);
        final List<DTOSchuelerSprachpruefungen> dtoSprachpruefungen = conn.queryNamed("DTOSchuelerSprachpruefungen.schueler_id", id, DTOSchuelerSprachpruefungen.class);
        // ... und gibt sie als Sprachendaten-Objekt zurück.
        return dtoMapperSprachendaten(id, dtoSprachenfolge, dtoSprachpruefungen);
	}


	/**
	 * Bestimmt die Sprachbelegungen (Sprachenfolge) und die Sprachprüfungen für die Schüler mit der angegebenen IDs aus
	 * den entsprechenden Tabellen in der Datenbank.
	 *
	 * @param conn   die Datenbank-Verbindung
	 * @param ids    die IDs der Schüler
	 *
	 * @return die Sprachendaten der Schüler
	 */
	public static List<Sprachendaten> getSchuelerSprachendaten(final DBEntityManager conn, final List<Long> ids) {
        // Lese die Sprachbelegungen (Sprachenfolge) und die Sprachprüfungen aus der Datenbank ein
        final Map<Long, List<DTOSchuelerSprachenfolge>> mapSprachenfolgen = conn.queryNamed("DTOSchuelerSprachenfolge.schueler_id.multiple", ids, DTOSchuelerSprachenfolge.class)
        		.stream().collect(Collectors.groupingBy(f -> f.Schueler_ID, Collectors.toList()));
        final Map<Long, List<DTOSchuelerSprachpruefungen>> mapSprachpruefungen = conn.queryNamed("DTOSchuelerSprachpruefungen.schueler_id.multiple", ids, DTOSchuelerSprachpruefungen.class)
        		.stream().collect(Collectors.groupingBy(f -> f.Schueler_ID, Collectors.toList()));
        // ... und gibt sie als Sprachendaten-Objekte zurück.
        return ids.stream().map(id -> dtoMapperSprachendaten(id, mapSprachenfolgen.computeIfAbsent(id, k -> new ArrayList<>()), mapSprachpruefungen.computeIfAbsent(id, k -> new ArrayList<>()))).toList();
	}

}
