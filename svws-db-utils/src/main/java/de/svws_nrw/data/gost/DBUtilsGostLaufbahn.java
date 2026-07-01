package de.svws_nrw.data.gost;

import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachbelegungen;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse stellt Hilfsmethoden für den Zugriff auf Informationen
 * zu der Laufbahnplanung von Schülern der gymnasialen Oberstufe zur Verfügung.
 */
public final class DBUtilsGostLaufbahn {

	private DBUtilsGostLaufbahn() {
		throw new IllegalStateException("Instantiation of " + DBUtilsGostLaufbahn.class.getName() + " not allowed");
	}

	/**
	 * Ermittelt die für die Laufbahnplanung der gymnasialen Oberstufe relevanten Fachwahldaten
	 * für die Vorlagen von Laufbahnplanungen bei den Abiturjahrgängen
	 * den Schüler mit der angegebenen ID aus den in der Datenbank gespeicherten
	 * Laufbahnplanungstabellen.
	 *
	 * @param conn       die Datenbank-Verbindung
	 * @param abijahr    das Abiturjahr
	 *
	 * @return die Fachwahlinformationen für die Laufbahnplanungs-Vorlage des angegebenen Abiturjahrgangs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Abiturdaten getVorlage(final DBEntityManager conn, final int abijahr) throws ApiOperationException {
		final DTOGostJahrgangsdaten jahrgang = conn.queryByKey(DTOGostJahrgangsdaten.class, abijahr);
		final int schuljahr = DBUtilsGost.pruefeSchuleMitGOStAndGetSchuljahr(conn, abijahr);
		if (jahrgang == null) {
			throw new ApiOperationException(Status.NOT_FOUND);
		}
		final GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherManager(schuljahr, conn, abijahr);
		final Map<Long, DTOGostJahrgangFachbelegungen> dtoFachwahlen =
				conn.queryList(DTOGostJahrgangFachbelegungen.QUERY_BY_ABI_JAHRGANG, DTOGostJahrgangFachbelegungen.class, abijahr)
						.stream().collect(Collectors.toMap(fb -> fb.Fach_ID, fb -> fb));

		final Abiturdaten abidaten = new Abiturdaten();
		abidaten.schuelerID = -1;
		abidaten.abiturjahr = abijahr;
		abidaten.schuljahrAbitur = abijahr - 1;
		// Erstelle Fake-Einträge für die Sprachenfolge, da die konkrete Belegung der Schüler in der Sprachenfolge unklar ist
		abidaten.bilingualeSprache = null;               // TODO ggf. auch ein alternatives Defaulting für den bilingualen Zweig erlauben
		abidaten.sprachendaten.schuelerID = -1;
		for (final String sprachkuerzel : gostFaecher.getFremdsprachenkuerzel()) {
			final Sprachbelegung belegung = new Sprachbelegung();
			belegung.sprache = sprachkuerzel;
			belegung.reihenfolge = 1;
			belegung.belegungVonJahrgang = Jahrgaenge.JAHRGANG_05.daten(schuljahr).kuerzel;
			abidaten.sprachendaten.belegungen.add(belegung);
		}
		for (final GostHalbjahr hj : GostHalbjahr.values()) {
			abidaten.bewertetesHalbjahr[hj.id] = false;  // Da es sich um eine Vorlage handelt, sind die Halbjahre nicht bewertet
		}
		// Füge gewählte Fächer hinzu
		for (final DTOGostJahrgangFachbelegungen belegungPlanung : dtoFachwahlen.values()) {
			// filtere leere Belegungen aus der Planung
			if ((belegungPlanung.EF1_Kursart == null) && (belegungPlanung.EF2_Kursart == null)
					&& (belegungPlanung.Q11_Kursart == null) && (belegungPlanung.Q12_Kursart == null)
					&& (belegungPlanung.Q21_Kursart == null) && (belegungPlanung.Q22_Kursart == null)) {
				continue;
			}

			final AbiturFachbelegung fach = new AbiturFachbelegung();
			fach.fachID = belegungPlanung.Fach_ID;
			abidaten.fachbelegungen.add(fach);

			final GostFach gostFach = gostFaecher.get(fach.fachID);
			if (gostFach == null) {
				continue;
			}
			final Fach zulFach = Fach.getBySchluesselOrDefault(gostFach.kuerzel);
			fach.istFSNeu = zulFach.daten(schuljahr).istFremdsprache && zulFach.daten(schuljahr).nurSII;
			final GostAbiturFach tmpAbiturFach = GostAbiturFach.fromID(belegungPlanung.AbiturFach);
			fach.abiturFach = (tmpAbiturFach == null) ? null : tmpAbiturFach.id;
			fach.idReferenzfach = null; // Die Vorlage enthält keine Schüler-spezifische Wahl für das Referenzfach
			GostKursart fachKursart = GostKursart.GK;
			if ("PX".equals(gostFach.kuerzel)) {
				fachKursart = GostKursart.PJK;
			} else if ("VX".equals(gostFach.kuerzel)) {
				fachKursart = GostKursart.VTF;
			}
			if (belegungPlanung.EF1_Kursart != null) {
				setFachbelegung(fach, GostHalbjahr.EF1, belegungPlanung.EF1_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
			}
			if (belegungPlanung.EF2_Kursart != null) {
				setFachbelegung(fach, GostHalbjahr.EF2, belegungPlanung.EF2_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
			}
			if (belegungPlanung.Q11_Kursart != null) {
				setFachbelegung(fach, GostHalbjahr.Q11, belegungPlanung.Q11_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
			}
			if (belegungPlanung.Q12_Kursart != null) {
				setFachbelegung(fach, GostHalbjahr.Q12, belegungPlanung.Q12_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
			}
			if (belegungPlanung.Q21_Kursart != null) {
				setFachbelegung(fach, GostHalbjahr.Q21, belegungPlanung.Q21_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
			}
			if (belegungPlanung.Q22_Kursart != null) {
				setFachbelegung(fach, GostHalbjahr.Q22, belegungPlanung.Q22_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
			}
		}
		return abidaten;
	}


	private static void setFachbelegung(final AbiturFachbelegung fach, final GostHalbjahr halbjahr,
			final String belegungPlanungKursart, final GostKursart fachKursart, final int wochenstunden, final boolean istInAbiwertung) {
		final AbiturFachbelegungHalbjahr belegung = new AbiturFachbelegungHalbjahr();
		belegung.halbjahrKuerzel = halbjahr.kuerzel;
		belegung.kursartKuerzel = (belegungPlanungKursart == null) ? null : switch (belegungPlanungKursart) {
			case "AT" -> "AT";
			case "LK" -> "LK";
			case "ZK" -> "ZK";
			default -> fachKursart.toString();
		};
		belegung.schriftlich = (belegungPlanungKursart == null) ? false : ("LK".equals(belegungPlanungKursart) || "S".equals(belegungPlanungKursart));
		belegung.wochenstunden = "LK".equals(belegungPlanungKursart) ? 5 : wochenstunden;
		belegung.block1gewertet = istInAbiwertung;
		belegung.block1kursAufZeugnis = true;
		belegung.notenkuerzel = null;
		fach.belegungen[halbjahr.id] = belegung;
		boolean isLetzte = true;
		for (GostHalbjahr hj = halbjahr.next(); hj != null; hj = hj.next()) {
			if (fach.belegungen[hj.id] != null) {
				isLetzte = false;
				break;
			}
		}
		if (isLetzte) {
			fach.letzteKursart = belegung.kursartKuerzel;
		}
	}

}
