package de.svws_nrw.db.utils;

import java.util.List;

import de.svws_nrw.core.data.schueler.Sprachbelegung;
import de.svws_nrw.core.data.schueler.Sprachendaten;
import de.svws_nrw.core.data.schueler.Sprachpruefung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachpruefungen;

/**
 * Diese Klasse stellt Hilfsmethoden für den Zugriff auf Informationen
 * von Schülern zur Verfügung.
 */
public class Schueler {

	/**
	 * Bestimmt die Sprachbelegungen (Sprachenfolge) und die Sprachprüfungen für den Schüler mit der angegebenen ID aus
	 * den entsprechenden Tabellen in der Datenbank.
	 *
	 * @param conn   die Datenbank-Verbindung
	 * @param id     die ID des Schülers
	 *
	 * @return die Sprachenfolge
	 */
	public static Sprachendaten getSchuelerSprachendaten(DBEntityManager conn, long id) {

        Sprachendaten sprachendaten = new Sprachendaten();
        sprachendaten.schuelerID = id;

        // Lese die Sprachbelegungen (Sprachenfolge) aus der Datenbank ein
        List<DTOSchuelerSprachenfolge> dtoSprachenfolge = conn.queryNamed("DTOSchuelerSprachenfolge.schueler_id", id, DTOSchuelerSprachenfolge.class);
		for (DTOSchuelerSprachenfolge dtoSprachbelegung : dtoSprachenfolge) {
			if ((dtoSprachbelegung.Schueler_ID == null) || (dtoSprachbelegung.ASDJahrgangVon == null))
				continue;
			Sprachbelegung belegung = new Sprachbelegung();
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
			sprachendaten.belegungen.add(belegung);
		}

        // Lese die Sprachprüfungen aus der Datenbank ein
        List<DTOSchuelerSprachpruefungen> dtoSprachpruefungen = conn.queryNamed("DTOSchuelerSprachpruefungen.schueler_id", id, DTOSchuelerSprachpruefungen.class);
        for (DTOSchuelerSprachpruefungen dtoSprachpruefung : dtoSprachpruefungen) {
            if ((dtoSprachpruefung.Schueler_ID == null) || (dtoSprachpruefung.Sprache == null) || (dtoSprachpruefung.Anspruchsniveau == null) || (!dtoSprachpruefung.IstHSUPruefung && !dtoSprachpruefung.IstFeststellungspruefung))
                continue;
            Sprachpruefung pruefung = new Sprachpruefung();
            pruefung.sprache = dtoSprachpruefung.Sprache;
            pruefung.anspruchsniveauId = dtoSprachpruefung.Anspruchsniveau.daten.id;
            pruefung.ersetzteSprache =dtoSprachpruefung.ErsetzteSprache;
            pruefung.jahrgang =dtoSprachpruefung.ASDJahrgang;
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
            sprachendaten.pruefungen.add(pruefung);
        }

        return sprachendaten;
	}
}
