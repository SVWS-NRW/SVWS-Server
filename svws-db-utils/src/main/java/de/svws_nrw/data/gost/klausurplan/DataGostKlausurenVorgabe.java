package de.svws_nrw.data.gost.klausurplan;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.data.gost.DataGostFaecher;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenVorgaben;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse bündelt verbliebene Hilfsoperationen für GOSt-Klausurvorgaben, die noch nicht
 * auf die Repository/Service/Controller-Architektur umgestellt wurden.
 */
public final class DataGostKlausurenVorgabe {

	private final DBEntityManager conn;
	private final int _abiturjahr;

	/**
	 * Erstellt eine neue Hilfsklasse für GOSt-Klausurvorgaben.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public DataGostKlausurenVorgabe(final DBEntityManager conn, final int abiturjahr) throws ApiOperationException {
		this.conn = conn;
		_abiturjahr = abiturjahr;
		if ((abiturjahr != -1) && (conn.queryByKey(DTOGostJahrgangsdaten.class, abiturjahr) == null)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Jahrgang nicht gefunden, ID: " + abiturjahr);
		}
	}

	/**
	 * Erstellt eine neue Hilfsklasse für GOSt-Klausurvorgaben.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public DataGostKlausurenVorgabe(final DBEntityManager conn) throws ApiOperationException {
		this(conn, -1);
	}


	private static GostKlausurvorgabe map(final DTOGostKlausurenVorgaben dto) {
		final GostKlausurvorgabe daten = new GostKlausurvorgabe();
		daten.id = dto.ID;
		daten.abiJahrgang = dto.Abi_Jahrgang;
		daten.idFach = dto.Fach_ID;
		daten.kursart = dto.Kursart.kuerzel;
		daten.halbjahr = dto.Halbjahr.id;
		daten.quartal = dto.Quartal;
		daten.bemerkungVorgabe = dto.Bemerkungen;
		daten.auswahlzeit = dto.Auswahlzeit;
		daten.dauer = dto.Dauer;
		daten.istAudioNotwendig = dto.IstAudioNotwendig;
		daten.istVideoNotwendig = dto.IstVideoNotwendig;
		daten.istMdlPruefung = dto.IstMdlPruefung;
		daten.istGklMoeglich = dto.IstGklMoeglich;
		return daten;
	}

	private static List<GostKlausurvorgabe> mapList(final List<DTOGostKlausurenVorgaben> dtos) {
		return dtos.stream().map(DataGostKlausurenVorgabe::map).toList();
	}

	/**
	 * Liefert zu einer Halbjahres-ID das entsprechende Gost-Halbjahr.
	 *
	 * @param halbjahr das Gost-Halbjahr
	 *
	 * @return das Gost-Halbjahr
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static GostHalbjahr checkHalbjahr(final int halbjahr) throws ApiOperationException {
		final GostHalbjahr hj = GostHalbjahr.fromID(halbjahr);
		if (hj == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Kein gültiges GostHalbjahr angegeben: " + halbjahr);
		}
		return hj;
	}

	/**
	 * Überprüft, ob der Wert für ein Quartal gültig ist.
	 *
	 * @param quartal das Quartal
	 *
	 * @return das das Quartal
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static int checkQuartal(final int quartal) throws ApiOperationException {
		if (quartal < 0) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Quartal ungültig: " + quartal);
		}
		return quartal;
	}

	/**
	 * Kopiert die Klausurvorgaben in einen Abiturjahrgang
	 *
	 * @param halbjahr das Halbjahr der gymnasialen Oberstufe
	 * @param quartal  das Quartal, 0 für das gesamte Halbjahr
	 *
	 * @return erfolgreich / nicht erfolgreich
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKlausurvorgabe> copyVorgaben(final int halbjahr, final int quartal) throws ApiOperationException {
		checkQuartal(quartal);
		return copyVorgabenToJahrgang(_abiturjahr, checkHalbjahr(halbjahr), checkQuartal(quartal));
	}

	/**
	 * Kopiert die Klausurvorgaben in einen Abiturjahrgang
	 *
	 * @param abiturjahr das Abiturjahr
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 * @param quartal    das Quartal, 0 für das gesamte Halbjahr
	 *
	 * @return erfolgreich / nicht erfolgreich
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKlausurvorgabe> copyVorgabenToJahrgang(final int abiturjahr, final GostHalbjahr halbjahr,
			final int quartal) throws ApiOperationException {
		final List<DTOGostKlausurenVorgaben> vorgabenVorlage =
				conn.queryList(DTOGostKlausurenVorgaben.QUERY_BY_ABI_JAHRGANG, DTOGostKlausurenVorgaben.class, -1);
		final List<DTOGostKlausurenVorgaben> vorgabenJg =
				conn.queryList(DTOGostKlausurenVorgaben.QUERY_BY_ABI_JAHRGANG, DTOGostKlausurenVorgaben.class, abiturjahr);
		// Prüfe, ob die Vorlage eingelesen werden kann
		if (vorgabenVorlage == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		}

		// Bestimme die ID, für welche der Datensatz eingefügt wird
		long idNMK = conn.transactionGetNextID(DTOGostKlausurenVorgaben.class);
		final List<DTOGostKlausurenVorgaben> vorgabenNeu = new ArrayList<>();
		for (final DTOGostKlausurenVorgaben vorgabe : vorgabenVorlage) {
			if (((halbjahr != null) && (vorgabe.Halbjahr != halbjahr)) || ((quartal > 0) && (quartal != vorgabe.Quartal))) {
				continue;
			}
			boolean exists = false;
			for (final DTOGostKlausurenVorgaben v : vorgabenJg) {
				if ((vorgabe.Halbjahr.id == v.Halbjahr.id) && (vorgabe.Quartal == v.Quartal) && (vorgabe.Fach_ID == v.Fach_ID)
						&& vorgabe.Kursart.equals(v.Kursart)) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				final DTOGostKlausurenVorgaben k =
						new DTOGostKlausurenVorgaben(idNMK++, abiturjahr, vorgabe.Halbjahr, vorgabe.Quartal, vorgabe.Fach_ID, vorgabe.Kursart, vorgabe.Dauer,
								vorgabe.Auswahlzeit, vorgabe.IstGklMoeglich, vorgabe.IstMdlPruefung, vorgabe.IstAudioNotwendig, vorgabe.IstVideoNotwendig);
				k.Bemerkungen = vorgabe.Bemerkungen;
				vorgabenNeu.add(k);
			}
		}
		if (!conn.transactionPersistAll(vorgabenNeu)) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Gost-Klausurvorgaben.");
		}
		return mapList(vorgabenNeu);
	}

	/**
	 * Legt für alle Jahrgänge die Klausurvorgaben laut APO-GOSt an.
	 *
	 * @param halbjahr das Halbjahr der gymnasialen Oberstufe
	 * @param quartal  das Quartal, 0 für das gesamte Halbjahr
	 *
	 * @return die Liste der neuen Klausurvorgaben
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKlausurvorgabe> createDefaultVorgaben(final GostHalbjahr halbjahr, final int quartal)
			throws ApiOperationException {
		final List<DTOGostKlausurenVorgaben> vorgabenVorlage =
				conn.queryList(DTOGostKlausurenVorgaben.QUERY_BY_ABI_JAHRGANG, DTOGostKlausurenVorgaben.class, -1);
		// Prüfe, ob die Vorlage eingelesen werden kann
		if (vorgabenVorlage == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		}
		final EnumMap<GostHalbjahr, GostKlausurplanManager> manager = new EnumMap<>(GostHalbjahr.class);
		for (final GostHalbjahr hj : GostHalbjahr.values()) {
			manager.put(hj, new GostKlausurplanManager(mapList(vorgabenVorlage.stream().filter(v -> v.Halbjahr == hj).toList())));
		}
		final List<GostFach> faecher = DataGostFaecher.getFaecherManager(conn, -1).getFaecherSchriftlichMoeglich();
		final List<DTOGostKlausurenVorgaben> neueVorgaben = new ArrayList<>();
		// Bestimme die ID, für welche der Datensatz eingefügt wird
		long idNMK = conn.transactionGetNextID(DTOGostKlausurenVorgaben.class);
		final Set<Integer> quartale = new HashSet<>();
		if (quartal == 0) {
			quartale.add(1);
			quartale.add(2);
		} else {
			quartale.add(quartal);
		}
		final GostKursart[] arten =
				halbjahr.istEinfuehrungsphase() ? new GostKursart[] { GostKursart.GK } : new GostKursart[] { GostKursart.GK, GostKursart.LK };
		for (final GostFach fach : faecher) {
			for (final GostKursart ka : arten) {
				if (((ka == GostKursart.LK) && !fach.istMoeglichAbiLK) || ((halbjahr == GostHalbjahr.Q22) && !(fach.istMoeglichAbiGK || fach.istMoeglichAbiLK))) {
					continue;
				}
				for (final int q : quartale) {
					final DTOGostKlausurenVorgaben vorgabeNeu = new DTOGostKlausurenVorgaben(idNMK++, -1, halbjahr, q, fach.id, ka,
							berechneApoKlausurdauer(halbjahr, ka, fach), 0, false, false, false, false);
					if (manager.get(vorgabeNeu.Halbjahr).vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(-1, halbjahr, vorgabeNeu.Quartal,
							vorgabeNeu.Kursart, vorgabeNeu.Fach_ID) == null) {
						neueVorgaben.add(vorgabeNeu);
					}
				}
			}
		}
		if (!conn.transactionPersistAll(neueVorgaben)) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Gost-Klausurvorgaben.");
		}
		return mapList(neueVorgaben);
	}

	private static int berechneApoKlausurdauer(final GostHalbjahr halbjahr, final GostKursart kursart, final GostFach fach) {
	    if (halbjahr.istEinfuehrungsphase()) {
			return 90;
		}
	    if (halbjahr.id <= 3) {
			return (kursart == GostKursart.LK) ? 180 : 135;
		}
	    if (halbjahr.id == 4) {
			return (kursart == GostKursart.LK) ? 225 : 180;
		}
	    if (halbjahr.id == 5) { // Abiturhalbjahr
			return berechneAbiturKlausurdauer(kursart, fach);
		}
	    throw new DeveloperNotificationException("Berechnung Klausurdauer fehlgeschlagen.");
	}

	private static int berechneAbiturKlausurdauer(final GostKursart kursart, final GostFach fach) {
		// Alte Sprachen
		if (fach.kuerzel.matches("^[GLH]\\d?$")) {
			if (!fach.istFremdSpracheNeuEinsetzend) {
				return (kursart == GostKursart.LK) ? 300 : 240; // fortgeführt
			}
		    return 210; // GK neu einsetzend
		}

		// Moderne Fremdsprachen
		if (fach.istFremdsprache) {
			if (!fach.istFremdSpracheNeuEinsetzend) {
				return (kursart == GostKursart.LK) ? 315 : 285; // fortgeführt
			}
			return 255; // GK neu einsetzend
		}

		// Naturwissenschaften
		if (List.of(Fach.BI.toString(), Fach.CH.toString(), Fach.PH.toString()).contains(fach.kuerzel)) {
			return (kursart == GostKursart.LK) ? 300 : 255;
		}

		if (Fach.D.toString().equals(fach.kuerzel)) {
			return (kursart == GostKursart.LK) ? 315 : 255;
		}

		if (Fach.M.toString().equals(fach.kuerzel)) {
			return (kursart == GostKursart.LK) ? 300 : 255;
		}

		// Informatik, Ernährungslehre, Technik
		if (List.of(Fach.IF.toString(), Fach.EL.toString(), Fach.TC.toString()).contains(fach.kuerzel)) {
			return (kursart == GostKursart.LK) ? 270 : 225;
		}

		// alle anderen Fächer
		return (kursart == GostKursart.LK) ? 300 : 240;
	}

}
