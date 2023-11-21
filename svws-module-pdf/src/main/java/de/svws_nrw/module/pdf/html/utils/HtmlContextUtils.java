package de.svws_nrw.module.pdf.html.utils;

import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.module.pdf.reptypes.gost.kursplanung.RepGostKursplanungKurs;
import de.svws_nrw.module.pdf.reptypes.gost.kursplanung.RepGostKursplanungKursSchueler;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


/**
 * Klasse enthält statische Hilfsmethoden rund um die HtmlContexts.
 */
public final class HtmlContextUtils {

	private HtmlContextUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Erstellt ein neues Objekt vom Typ DruckGostKursplanungKurs.
	 * @param conn				Datenbankverbindung
	 * @param datenManager		Datenmanager der Blockung
	 * @param ergebnisManager	Ergebnismanager für das Blockungsergebnis
	 * @param kursID			Die Kurs-ID, für die das Kursobjekt erstellt werden soll.
	 *
	 * @return					Ein Kurs aus Kursplanung für die GOSt.
	 */
	public static RepGostKursplanungKurs getDruckGostKursplanungKurs(final DBEntityManager conn, final GostBlockungsdatenManager datenManager, final GostBlockungsergebnisManager ergebnisManager, final Long kursID) {
		// Die Kursschüler können in einer beliebigen Reihenfolge sein. Für die Ausgabe der Liste sollten
		// sie aber in alphabetischer Reihenfolge der Schüler sein.
		// Erzeuge daher eine Liste mit Schülern, die in der alphabetischen Reihenfolge der Schüler sortiert ist
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);

		final List<SchuelerStammdaten> sortiertKursschueler = DataSchuelerStammdaten.getListStammdaten(conn, ergebnisManager.getOfKursSchuelermenge(kursID).stream().map(ks -> ks.id).toList()).stream()
			.sorted(Comparator.comparing((final SchuelerStammdaten s) -> s.nachname, colGerman)
				.thenComparing((final SchuelerStammdaten s) -> s.vorname, colGerman)
				.thenComparing((final SchuelerStammdaten s) -> s.id))
			.toList();

		final List<RepGostKursplanungKursSchueler> listeKursschueler = sortiertKursschueler.stream()
			.map(ks -> new RepGostKursplanungKursSchueler(
				ks,
				ergebnisManager.getOfSchuelerOfKursFachwahl(ks.id, kursID).istSchriftlich,
				(ergebnisManager.getOfSchuelerOfKursFachwahl(ks.id, kursID).abiturfach != null) ? ergebnisManager.getOfSchuelerOfKursFachwahl(ks.id, kursID).abiturfach.toString() : ""))
			.toList();

		// Fach für Hintergrundfarbe ermitteln
		final ZulaessigesFach fach = ZulaessigesFach.getByKuerzelASD(datenManager.faecherManager().get(datenManager.kursGet(kursID).fach_id).kuerzel);
		final String farbeClientRGB = (fach != null) ? fach.getHMTLFarbeRGB().replace("rgba(", "").replace(")", "") : "";

		return new RepGostKursplanungKurs(
			kursID,
			GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel,
			datenManager.kursGetName(kursID),
			datenManager.kursGetLehrkraefteSortiert(kursID).isEmpty() ? "" : datenManager.kursGetLehrkraefteSortiert(kursID).stream().map(l -> l.kuerzel).collect(Collectors.joining(",")),
			GostKursart.fromID(ergebnisManager.getKursE(kursID).kursart).kuerzel,
			ergebnisManager.getOfKursAnzahlSchueler(kursID),
			ergebnisManager.getOfKursAnzahlSchuelerExterne(kursID),
			ergebnisManager.getOfKursAnzahlSchuelerSchriftlich(kursID),
			ergebnisManager.getOfKursAnzahlSchuelerDummy(kursID),
			ergebnisManager.getOfKursAnzahlSchuelerAbiturLK(kursID),
			ergebnisManager.getOfKursAnzahlSchuelerAbitur3(kursID),
			ergebnisManager.getOfKursAnzahlSchuelerAbitur4(kursID),
			farbeClientRGB,
			listeKursschueler
		);
	}
}
