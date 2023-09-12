package de.svws_nrw.data.schild3.reporting;

import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerGOStLaufbahnplanungFachwahlen;
import de.svws_nrw.core.data.schueler.Sprachbelegung;
import de.svws_nrw.core.data.schueler.Sprachpruefung;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.schild3.SchildReportingAttributTyp;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.gost.DBUtilsGostLaufbahn;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.OperationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

/**
 * Die Definition einer Schild-Reporting-Datenquelle für die Fachwahlen eines Schülers in der Laufbahnplanung für die gymnasiale Oberstufe
 */
public final class DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungFachwahlen extends DataSchildReportingDatenquelle<SchildReportingSchuelerGOStLaufbahnplanungFachwahlen, Long> {

    /**
     * Erstelle die Datenquelle SchuelerGOStLaufbahnplanungFachwahlen
     */
	public DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungFachwahlen() {
        super(SchildReportingSchuelerGOStLaufbahnplanungFachwahlen.class);
        this.setMaster("schuelerID", "Schueler", "id", SchildReportingAttributTyp.INT, Long.class);
        // Beispiel für die Einschränkung auf Schulformen: this.restrictTo(Schulform.GY, Schulform.GE)
    }

	@Override
	public List<SchildReportingSchuelerGOStLaufbahnplanungFachwahlen> getDaten(final DBEntityManager conn, final List<Long> params) {
		// Prüfe, ob die Schüler in der DB vorhanden sind
        final Map<Long, DTOSchueler> schueler = conn
                .queryNamed("DTOSchueler.id.multiple", params, DTOSchueler.class)
                .stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		for (final Long schuelerID : params) {
			if (schueler.get(schuelerID) == null)
				throw OperationError.NOT_FOUND.exception("Parameter der Abfrage ungültig: Ein Schüler mit der ID " + schuelerID.toString() + " existiert nicht.");
		}

		// Map für die GostFaecherManager der Abiturjahrgänge, damit diese nur einmal geladen werden müssen.
		final Map<Integer, GostFaecherManager> jahrgangGostFaecher = new HashMap<>();

		// Aggregiere die benötigten Daten aus der Datenbank, wenn alle Schüler-IDs existieren
		final ArrayList<SchildReportingSchuelerGOStLaufbahnplanungFachwahlen> result = new ArrayList<>();
		for (final Long schuelerID : params) {
			// GOSt-Daten des Schülers und Abiturdaten zur Schueler_ID ermitteln
			final DTOGostSchueler gostSchueler = conn.queryByKey(DTOGostSchueler.class, schuelerID);
			final Abiturdaten abidaten = DBUtilsGostLaufbahn.get(conn, schuelerID);

			if ((gostSchueler != null) && (abidaten.abiturjahr > 0)) {
				// Nur wenn zum Schüler GOSt-Daten und Abiturdaten gefunden werden, dann werden die gefundenen Fächer in den Ergebnisvektor eingetragen. Andernfalls wird ein leerer Vektor zurückgegeben.
				// Alternativ wäre der vollständige Abbruch im Fehlerfall: throw OperationError.INTERNAL_SERVER_ERROR.exception("Parameter der Abfrage ungültig: Die GOSt-Daten oder Abiturdaten des Schülers mit der ID " + schuelerID.toString() + " konnten nicht ermittelt werden.")

				// Ergänze die Map Abiturjahr → GostFaecher des Abiturjahrgangs, wenn die Fächer des Abiturjahrgangs noch nicht enthalten sind.
				if (!jahrgangGostFaecher.containsKey(abidaten.abiturjahr)) {
					jahrgangGostFaecher.put(abidaten.abiturjahr, DBUtilsFaecherGost.getNurWaehlbareFaecherListeGost(conn, abidaten.abiturjahr));
				}

				// Erzeuge eine Map Fach-ID → AbiturFachbelegung aus den AbiturDaten
				final Map<Long, AbiturFachbelegung> belegungen = abidaten.fachbelegungen.stream().collect(Collectors.toMap(b -> b.fachID, b -> b));
				// Erzeuge eine Map einstelliges Sprachkürzel → Sprachbelegung aus den AbiturDaten
				final Map<String, Sprachbelegung> sprachbelegungen = abidaten.sprachendaten.belegungen.stream().collect(Collectors.toMap(b -> b.sprache, b -> b));
				// Erzeuge eine Map einstelliges Sprachkürzel → Sprachpruefung aus den AbiturDaten
				final Map<String, Sprachpruefung> sprachpruefungen = abidaten.sprachendaten.pruefungen.stream().collect(Collectors.toMap(b -> b.sprache, b -> b));

				// Erzeuge für jedes Fach des Abiturjahrgangs eine Zeile, wobei ggf. die Belegungen aus der Map verwendet werden
				for (final GostFach fach : jahrgangGostFaecher.get(abidaten.abiturjahr).faecher()) {
					final SchildReportingSchuelerGOStLaufbahnplanungFachwahlen laufbahnplanungFach = new SchildReportingSchuelerGOStLaufbahnplanungFachwahlen();

					laufbahnplanungFach.schuelerID = schuelerID;

					// Erzeuge die Core-DTOs für das Ergebnis der Datenquelle
					final AbiturFachbelegung belegung = belegungen.get(fach.id);

					laufbahnplanungFach.kuerzel = fach.kuerzelAnzeige;
					laufbahnplanungFach.bezeichnung = fach.bezeichnung;
					laufbahnplanungFach.fachIstFortfuehrbareFremdspracheInGOSt = false;
					laufbahnplanungFach.jahrgangFremdsprachenbeginn = "";
					laufbahnplanungFach.positionFremdsprachenfolge = "";

					eintragFremdspracheInLaufbahnplanungFachErgaenzen(laufbahnplanungFach, fach, abidaten, sprachbelegungen, sprachpruefungen);

					if (belegung == null) {
						laufbahnplanungFach.belegungEF1 = "";
						laufbahnplanungFach.belegungEF2 = "";
						laufbahnplanungFach.belegungQ11 = "";
						laufbahnplanungFach.belegungQ12 = "";
						laufbahnplanungFach.belegungQ21 = "";
						laufbahnplanungFach.belegungQ22 = "";
					} else {
						laufbahnplanungFach.belegungEF1 = eintragFachbelegung(belegung.belegungen[0]);
						laufbahnplanungFach.belegungEF2 = eintragFachbelegung(belegung.belegungen[1]);
						laufbahnplanungFach.belegungQ11 = eintragFachbelegung(belegung.belegungen[2]);
						laufbahnplanungFach.belegungQ12 = eintragFachbelegung(belegung.belegungen[3]);
						laufbahnplanungFach.belegungQ21 = eintragFachbelegung(belegung.belegungen[4]);
						laufbahnplanungFach.belegungQ22 = eintragFachbelegung(belegung.belegungen[5]);

						if (belegung.abiturFach == null) {
							laufbahnplanungFach.abiturfach = "";
						} else {
							laufbahnplanungFach.abiturfach = belegung.abiturFach.toString();
						}
					}

					final ZulaessigesFach zulaessigesFach = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);
					if (zulaessigesFach.daten.aufgabenfeld == null)
						laufbahnplanungFach.aufgabenfeld = 0;
					else
						laufbahnplanungFach.aufgabenfeld = zulaessigesFach.daten.aufgabenfeld;
					laufbahnplanungFach.fachgruppe = zulaessigesFach.daten.fachgruppe;
					laufbahnplanungFach.farbeClientRGB = zulaessigesFach.getHMTLFarbeRGB().replace("rgba(", "").replace(")", "");

					result.add(laufbahnplanungFach);
				}
			}
		}

		// Geben die Ergebnis-Liste mit den Core-DTOs zurück
        return result;
    }


	/**
	 * Ergänzt im übergebenen LaufbahnplanungFachwahl-Objekt den Fremdspracheneintrag, wenn es sich um eine Fremdsprache handelt.
	 * @param laufbahnplanungFach 	Das Laufbahnplanungsfach, bei dem die Fremdspracheninformationen ergänzt werden sollen.
	 * @param fach 					GOST-Fach der Fremdsprache
	 * @param abiturdaten 			Abiturdaten des Schülers
	 * @param sprachbelegungen 		Sprachbelegungen des Schülers aus der Sprachenfolge
	 * @param sprachpruefungen 		Sprachprüfungen des Schülers
	 */
	private static void eintragFremdspracheInLaufbahnplanungFachErgaenzen(final SchildReportingSchuelerGOStLaufbahnplanungFachwahlen laufbahnplanungFach, final GostFach fach, final Abiturdaten abiturdaten, final Map<String, Sprachbelegung> sprachbelegungen, final Map<String, Sprachpruefung> sprachpruefungen) {

		if (!fach.istFremdsprache)
			return;

		final ZulaessigesFach zfach = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);

		// Verhindern, dass Pseudofächer der Statistik hier als zulässiges Fach verwendet werden.
		if (!(zfach.daten.kuerzelASD.equals("PX") || zfach.daten.kuerzelASD.equals("VX"))) {
			final Sprachbelegung sprachbelegung = sprachbelegungen.get(zfach.daten.kuerzel);
			final Sprachpruefung sprachpruefung = sprachpruefungen.get(zfach.daten.kuerzel);

			if (sprachbelegung != null) {
				if (((sprachbelegung.belegungVonJahrgang != null) && !sprachbelegung.belegungVonJahrgang.isEmpty())
					&& ((zfach.daten.abJahrgang == null)
					|| zfach.daten.abJahrgang.isEmpty()
					|| ((zfach.daten.abJahrgang.compareToIgnoreCase("EF") >= 0) && fach.istFremdSpracheNeuEinsetzend && (sprachbelegung.belegungVonJahrgang.compareToIgnoreCase("EF") >= 0))
					|| ((zfach.daten.abJahrgang.compareToIgnoreCase("EF") < 0) && !fach.istFremdSpracheNeuEinsetzend && (sprachbelegung.belegungVonJahrgang.compareToIgnoreCase("EF") < 0)))) {
					// Nur Sprachen heranziehen, die auch vor oder mit der eigenen Belegung hätten starten können. So wird bspw. die neue Fremdsprache ab EF nicht durch die Belegung der gleichen Sprache in der Sek-I als belegt markiert.
					laufbahnplanungFach.fachIstFortfuehrbareFremdspracheInGOSt = true;
					laufbahnplanungFach.jahrgangFremdsprachenbeginn = sprachbelegung.belegungVonJahrgang;
					laufbahnplanungFach.positionFremdsprachenfolge = sprachbelegung.reihenfolge.toString();
				}
			} else if ((sprachpruefung != null) && (SprachendatenUtils.istFortfuehrbareSpracheInGOSt(abiturdaten.sprachendaten, zfach.daten.kuerzel))) {
				laufbahnplanungFach.fachIstFortfuehrbareFremdspracheInGOSt = true;
				if (sprachpruefung.istFeststellungspruefung) {
					laufbahnplanungFach.jahrgangFremdsprachenbeginn = "SFP";
				} else if (sprachpruefung.istHSUPruefung) {
					laufbahnplanungFach.jahrgangFremdsprachenbeginn = "HSU";
				}
				if (sprachpruefung.kannErstePflichtfremdspracheErsetzen)
					laufbahnplanungFach.positionFremdsprachenfolge = "1";
				else if (sprachpruefung.kannZweitePflichtfremdspracheErsetzen)
					laufbahnplanungFach.positionFremdsprachenfolge = "2";
				else if (sprachpruefung.kannWahlpflichtfremdspracheErsetzen)
					laufbahnplanungFach.positionFremdsprachenfolge = "2";
				else {
					laufbahnplanungFach.positionFremdsprachenfolge = "";
				}
			}
		}
	}


	/**Gibt den Belegungseintrag eines Faches für die Halbjahres-Belegung zurück.
	 *
	 * @param belegungHj die Halbjahresbelegung des Faches
	 *
	 * @return String mit dem Belegungskürzel des Faches gemäß dessen Halbjahresbelegung
	 */
	private static String eintragFachbelegung(final AbiturFachbelegungHalbjahr belegungHj) {
		if (belegungHj == null)
			return "";

		final GostKursart kursart = GostKursart.fromKuerzel(belegungHj.kursartKuerzel);
		if (kursart == GostKursart.GK)
			return belegungHj.schriftlich ? "S" : "M";
		if (kursart == GostKursart.LK)
			return "LK";
		if ((kursart == GostKursart.PJK) || (kursart == GostKursart.VTF))
			return "M";
		if (kursart == GostKursart.ZK)
			return "ZK";
		if ("AT".equals(belegungHj.kursartKuerzel))
			return "AT";
		return "";
	}

}
