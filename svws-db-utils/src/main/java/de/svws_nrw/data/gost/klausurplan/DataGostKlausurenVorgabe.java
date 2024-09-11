package de.svws_nrw.data.gost.klausurplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenKursklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenVorgaben;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link GostKlausurvorgabe}.
 */
public final class DataGostKlausurenVorgabe extends DataManagerRevised<Long, DTOGostKlausurenVorgaben, GostKlausurvorgabe> {

	private final int _abiturjahr;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link GostKlausurvorgabe}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public DataGostKlausurenVorgabe(final DBEntityManager conn, final int abiturjahr) throws ApiOperationException {
		super(conn);
		super.setAttributesNotPatchable("abiJahrgang", "halbjahr", "quartal", "idFach", "kursart");
		super.setAttributesRequiredOnCreation("abiJahrgang", "halbjahr", "quartal", "idFach", "kursart");
		_abiturjahr = abiturjahr;
		if ((abiturjahr != -1) && (conn.queryByKey(DTOGostJahrgangsdaten.class, abiturjahr) == null))
			throw new ApiOperationException(Status.BAD_REQUEST, "Jahrgang nicht gefunden, ID: " + abiturjahr);
	}

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link GostKlausurvorgabe}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public DataGostKlausurenVorgabe(final DBEntityManager conn) throws ApiOperationException {
		this(conn, -1);
	}


	/**
	 * Gibt die Daten einer {@link GostKlausurvorgabe} zu deren ID zurück.
	 *
	 * @param id   Die ID der {@link GostKlausurvorgabe}.
	 *
	 * @return die Daten der {@link GostKlausurvorgabe} zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public GostKlausurvorgabe getById(final Long id) throws ApiOperationException {
		final DTOGostKlausurenVorgaben klasseDto = getDTO(id);
		return map(klasseDto);
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOGostKlausurenVorgaben} Objekt zur angegebenen Klassen ID.
	 *
	 * @param id ID der {@link DTOGostKlausurenVorgaben}
	 *
	 * @return Ein {@link DTOGostKlausurenVorgaben} Objekt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOGostKlausurenVorgaben getDTO(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die GostKlausurvorgabe darf nicht null sein.");

		final DTOGostKlausurenVorgaben klasseDto = conn.queryByKey(DTOGostKlausurenVorgaben.class, id);
		if (klasseDto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine GostKlausurvorgabe zur ID " + id + " gefunden.");

		return klasseDto;
	}

	@Override
	protected void initDTO(final DTOGostKlausurenVorgaben dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected GostKlausurvorgabe map(final DTOGostKlausurenVorgaben dto) throws ApiOperationException {
		final GostKlausurvorgabe daten = new GostKlausurvorgabe();
		daten.idVorgabe = dto.ID;
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
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOGostKlausurenVorgaben dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idVorgabe" -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}
			case "abiJahrgang" -> {
				dto.Abi_Jahrgang = JSONMapper.convertToInteger(value, false);
				if (conn.queryByKey(DTOGostJahrgangsdaten.class, dto.Abi_Jahrgang) == null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Jahrgang nicht gefunden, ID: " + dto.Abi_Jahrgang);
			}
			case "halbjahr" -> dto.Halbjahr = checkHalbjahr(JSONMapper.convertToInteger(value, false));
			case "quartal" -> dto.Quartal = checkQuartal(JSONMapper.convertToInteger(value, false));
			case "idFach" -> {
				dto.Fach_ID = JSONMapper.convertToLong(value, false);
				if (conn.queryByKey(DTOFach.class, dto.Fach_ID) == null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Fach nicht gefunden, ID: " + dto.Fach_ID);
			}
			case "kursart" -> dto.Kursart = checkKursart(JSONMapper.convertToString(value, false, false, null));
			case "dauer" -> dto.Dauer = JSONMapper.convertToInteger(value, false);
			case "auswahlzeit" -> dto.Auswahlzeit = JSONMapper.convertToInteger(value, false);
			case "istMdlPruefung" -> dto.IstMdlPruefung = JSONMapper.convertToBoolean(value, false);
			case "istAudioNotwendig" -> dto.IstAudioNotwendig = JSONMapper.convertToBoolean(value, false);
			case "istVideoNotwendig" -> dto.IstVideoNotwendig = JSONMapper.convertToBoolean(value, false);
			case "bemerkungVorgabe" -> dto.Bemerkungen =
					JSONMapper.convertToString(value, true, true, Schema.tab_Gost_Klausuren_Vorgaben.col_Bemerkungen.datenlaenge());
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
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
		if (hj == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Kein gültiges GostHalbjahr angegeben: " + halbjahr);
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
		if (quartal < 0)
			throw new ApiOperationException(Status.BAD_REQUEST, "Quartal ungültig: " + quartal);
		return quartal;
	}

	private static GostKursart checkKursart(final String kursart) throws ApiOperationException {
		final GostKursart ka = GostKursart.fromKuerzel(kursart);
		if (ka == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Keine gültige Kursart angegeben: " + kursart);
		return ka;
	}

	/**
	 * Gibt die Liste der Klausurvorgaben einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr das Gost-Halbjahr
	 * @param ganzesSchuljahr true, um Vorgaben für das gesamte Schuljahr zu erhalten, false nur für das übergeben Halbjahr
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKlausurvorgabe> getKlausurvorgaben(final int abiturjahr, final int halbjahr,
			final boolean ganzesSchuljahr) throws ApiOperationException {
		final List<DTOGostKlausurenVorgaben> vorgaben = (halbjahr <= 0)
				? conn.query("SELECT v FROM DTOGostKlausurenVorgaben v WHERE v.Abi_Jahrgang = :jgid", DTOGostKlausurenVorgaben.class)
						.setParameter("jgid", abiturjahr)
						.getResultList()
				: conn.query("SELECT v FROM DTOGostKlausurenVorgaben v WHERE v.Abi_Jahrgang = :jgid AND v.Halbjahr IN :hj", DTOGostKlausurenVorgaben.class)
						.setParameter("jgid", abiturjahr)
						.setParameter("hj",
								Arrays.asList(ganzesSchuljahr ? GostHalbjahr.fromIDorException(halbjahr).getSchuljahr()
										: new GostHalbjahr[] { GostHalbjahr.fromIDorException(halbjahr) }))
						.getResultList();
		return mapList(vorgaben);
	}

	/**
	 * Gibt die Liste der Klausurvorgaben zu einer Menge von IDs zurück.
	 *
	 * @param vids die IDs der Kursklausuren, zu denen die Vorgaben gesucht werden.
	 *
	 * @return die Liste der Klausurvorgaben
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKlausurvorgabe> getKlausurvorgabenZuIds(final List<Long> vids) throws ApiOperationException {
		final List<DTOGostKlausurenVorgaben> vorgaben = getKlausurvorgabDTOsZuIds(conn, vids);
		return mapList(vorgaben);
	}

	/**
	 * Gibt die Liste der Klausurvorgaben-DTOs zu den übergebenen IDs zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param vids die IDs der Kursklausuren, zu denen die Vorgaben gesucht werden.
	 *
	 * @return die Liste der Klausurvorgaben
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<DTOGostKlausurenVorgaben> getKlausurvorgabDTOsZuIds(final DBEntityManager conn, final List<Long> vids) throws ApiOperationException {
		if (vids.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenVorgaben> vorgaben = conn.queryByKeyList(DTOGostKlausurenVorgaben.class, vids);
		if (vorgaben.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Klausurvorgabe-DTOs zu angegebenen IDs nicht gefunden.");
		return vorgaben;
	}

	/**
	 * Gibt die Liste der Klausurvorgaben-DTOs zu den übergebenen IDs zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param kks die Kursklausur-DTOs, zu denen die Vorgaben gesucht werden.
	 *
	 * @return die Liste der Klausurvorgaben
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<DTOGostKlausurenVorgaben> getKlausurvorgabeDTOsZuKursklausurDTOs(final DBEntityManager conn,
			final List<DTOGostKlausurenKursklausuren> kks) throws ApiOperationException {
		return getKlausurvorgabDTOsZuIds(conn, kks.stream().map(kk -> kk.Vorgabe_ID).toList());
	}

	/**
	 * Gibt die Liste der Klausurvorgaben-DTOs zu den übergebenen IDs zurück.
	 *
	 * @param kks die Kursklausur-DTOs, zu denen die Vorgaben gesucht werden.
	 *
	 * @return die Liste der Klausurvorgaben
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKlausurvorgabe> getKlausurvorgabenZuKursklausurDTOs(final List<DTOGostKlausurenKursklausuren> kks)
			throws ApiOperationException {
		final List<DTOGostKlausurenVorgaben> vorgaben = getKlausurvorgabeDTOsZuKursklausurDTOs(conn, kks);
		return mapList(vorgaben);
	}

	/**
	 * Gibt die Liste der Klausurvorgaben zu einer Menge von Kursklausuren zurück.
	 *
	 * @param kks die Kursklausuren, zu denen die Vorgaben gesucht werden.
	 *
	 * @return die Liste der Klausurvorgaben
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKlausurvorgabe> getKlausurvorgabenZuKursklausuren(final List<GostKursklausur> kks)
			throws ApiOperationException {
		return getKlausurvorgabenZuIds(kks.stream().map(kk -> kk.idVorgabe).toList());
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
		if (vorgabenVorlage == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);

		// Bestimme die ID, für welche der Datensatz eingefügt wird
		long idNMK = conn.transactionGetNextID(DTOGostKlausurenVorgaben.class);
		final List<DTOGostKlausurenVorgaben> vorgabenNeu = new ArrayList<>();
		for (final DTOGostKlausurenVorgaben vorgabe : vorgabenVorlage) {
			if (((halbjahr != null) && (vorgabe.Halbjahr != halbjahr)) || ((quartal > 0) && (quartal != vorgabe.Quartal)))
				continue;
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
								vorgabe.Auswahlzeit, vorgabe.IstMdlPruefung, vorgabe.IstAudioNotwendig, vorgabe.IstVideoNotwendig);
				k.Bemerkungen = vorgabe.Bemerkungen;
				vorgabenNeu.add(k);
			}
		}
		if (!conn.transactionPersistAll(vorgabenNeu))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Gost-Klausurvorgaben.");
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
		final DTOEigeneSchule schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine gültiger Schuljahresabschnitt vorhanden.");
		final List<DTOGostKlausurenVorgaben> vorgabenVorlage =
				conn.queryList(DTOGostKlausurenVorgaben.QUERY_BY_ABI_JAHRGANG, DTOGostKlausurenVorgaben.class, -1);
		// Prüfe, ob die Vorlage eingelesen werden kann
		if (vorgabenVorlage == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		final EnumMap<GostHalbjahr, GostKlausurplanManager> manager = new EnumMap<>(GostHalbjahr.class);
		for (final GostHalbjahr hj : GostHalbjahr.values())
			manager.put(hj, new GostKlausurplanManager(schuljahresabschnitt.Jahr,
					mapList(vorgabenVorlage.stream().filter(v -> v.Halbjahr == hj).toList())));
		final List<GostFach> faecher = DBUtilsFaecherGost.getFaecherManager(schuljahresabschnitt.Jahr, conn, null).getFaecherSchriftlichMoeglich();
		final List<DTOGostKlausurenVorgaben> neueVorgaben = new ArrayList<>();
		// Bestimme die ID, für welche der Datensatz eingefügt wird
		long idNMK = conn.transactionGetNextID(DTOGostKlausurenVorgaben.class);
		final Set<Integer> quartale = new HashSet<>();
		if (quartal == 0) {
			quartale.add(1);
			quartale.add(2);
		} else
			quartale.add(quartal);
		final GostKursart[] arten =
				halbjahr.istEinfuehrungsphase() ? new GostKursart[] { GostKursart.GK } : new GostKursart[] { GostKursart.GK, GostKursart.LK };
		for (final GostFach fach : faecher) {
			for (final GostKursart ka : arten) {
				if (((ka == GostKursart.LK) && !fach.istMoeglichAbiLK) || ((halbjahr == GostHalbjahr.Q22) && !(fach.istMoeglichAbiGK || fach.istMoeglichAbiLK)))
					continue;
				for (final int q : quartale) {
					final DTOGostKlausurenVorgaben vorgabeNeu = new DTOGostKlausurenVorgaben(idNMK++, -1, halbjahr, q, fach.id, ka,
							berechneApoKlausurdauer(halbjahr, ka, fach), 0, false, false, false);
					if (manager.get(vorgabeNeu.Halbjahr).vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(-1, halbjahr, vorgabeNeu.Quartal,
							vorgabeNeu.Kursart, vorgabeNeu.Fach_ID) == null)
						neueVorgaben.add(vorgabeNeu);
				}
			}
		}
		if (!conn.transactionPersistAll(neueVorgaben))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Gost-Klausurvorgaben.");
		return mapList(neueVorgaben);
	}

	private static int berechneApoKlausurdauer(final GostHalbjahr halbjahr, final GostKursart kursart, final GostFach fach) {
		if (halbjahr.istEinfuehrungsphase())
			return 90;
		if (kursart == GostKursart.LK) {
			if (halbjahr.id <= 3)
				return 180;
			if (halbjahr.id == 4)
				return 225;
			return fach.istFremdsprache ? 315 : 300;
		}
		if (halbjahr.id <= 3)
			return 135;
		if (halbjahr.id == 4)
			return 180;
		return fach.istFremdsprache ? 285 : 255;
	}

}
