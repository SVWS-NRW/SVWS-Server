package de.svws_nrw.data.gost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.types.gost.AbiturBelegungsart;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostBesondereLernleistung;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbitur;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbiturFach;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Abiturdaten}.
 */
public final class DataGostAbiturdaten extends DataManagerRevised<Long, DTOSchuelerAbitur, Abiturdaten> {

	/** Der Schuljahresabschnitt für das Abitur, sofern sich die Anfrage auf ein konkretes Abiturjahr bezieht. Ansonsten null */
	private final Schuljahresabschnitt schuljahresabschnitt;

	/**
	 * Erstellt einen neuen Daten-Manager für das übergebene Abiturjahr. Dieses kann null sein,
	 * wenn das Abiturjahr aus den Laufbahndaten des Schülers ermittelt werden soll.
	 *
	 * @param conn         die Datenbank-Verbindung
	 * @param abiturjahr   das Abiturjahr oder null
	 */
	public DataGostAbiturdaten(final DBEntityManager conn, final Integer abiturjahr) {
		super(conn);
		setAttributesNotPatchable("schuelerID", "abiturjahr", "schuljahrAbitur", "bewertetesHalbjahr", "fachbelegungen", "sprachendaten", "bilingualeSprache");
		// Bestimme den Schuljahresabschnitt für das Abiturjahr, sofern eines angegeben ist
		this.schuljahresabschnitt = (abiturjahr == null) ? null : conn.getUser().schuleGetAbschnittBySchuljahrUndHalbjahr(abiturjahr - 1, 2);
	}


	@Override
	public DTOSchuelerAbitur getDatabaseDTOByID(final Long id) {
		// Lese die Abiturdaten anhand der ID aus der Datenbank
		final List<DTOSchuelerAbitur> dtosSchuelerAbitur = conn.queryList(DTOSchuelerAbitur.QUERY_BY_SCHUELER_ID, DTOSchuelerAbitur.class, id);
		if ((dtosSchuelerAbitur == null) || (dtosSchuelerAbitur.isEmpty()))
			return null;
		// Abiturjahr wurde nicht angegeben - ggf. auswählen
		if (schuljahresabschnitt == null) {
			DTOSchuelerAbitur current = null;
			for (final DTOSchuelerAbitur dtoSchuelerAbitur : dtosSchuelerAbitur) {
				final Schuljahresabschnitt dtoSja = (dtoSchuelerAbitur.Schuljahresabschnitts_ID) == null ? null
						: conn.getUser().schuleGetAbschnittById(dtoSchuelerAbitur.Schuljahresabschnitts_ID);
				if ((current == null) || ((dtoSja != null) && ((dtoSja.schuljahr > schuljahresabschnitt.schuljahr)
						|| ((dtoSja.schuljahr == schuljahresabschnitt.schuljahr) && (dtoSja.abschnitt > schuljahresabschnitt.abschnitt)))))
					current = dtoSchuelerAbitur;
			}
			return current;
		}
		for (final DTOSchuelerAbitur dtoSchuelerAbitur : dtosSchuelerAbitur)
			if (dtoSchuelerAbitur.Schuljahresabschnitts_ID == schuljahresabschnitt.id)
				return dtoSchuelerAbitur;
		return null;
	}


	@Override
	public Abiturdaten getById(final Long id) throws ApiOperationException {
		// Prüfe zunächst den Schüler auf Existenz.
		final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, id);
		if (dtoSchueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert kein Schüler mit der ID %d in der Datenbank.".formatted(id));

		// Ermittle für einen Vergleich die Abiturdaten für Block I aus den Leistungsdaten, nutze dafür den entsprechenden Service
		final Abiturdaten abidatenVergleich = DBUtilsGostLaufbahn.get(conn, id);
		if (abidatenVergleich == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es konnten keine Abiturdaten aus den Leistungsdaten ausgelesen werden.");

		// Lese die Abiturdaten anhand der ID aus der Datenbank
		final DTOSchuelerAbitur dtoSchuelerAbitur = getDatabaseDTOByID(id);
		if (dtoSchuelerAbitur == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden keine Abiturdaten für den Schüler mit der ID %d in der Datenbank gefunden.".formatted(id));

		final List<DTOSchuelerAbiturFach> faecher = conn.queryList(DTOSchuelerAbiturFach.QUERY_BY_SCHUELER_ID, DTOSchuelerAbiturFach.class, id);
		if (faecher == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es konnten keine Fachinformationen für die Abiturdaten des Schülers aus der DB ausgelesen werden.");

		// Lese beide Tabellen mit den Informationen zu den belegten oder geprüften Sprachen aus.
		final List<DTOSchuelerSprachenfolge> sprachenfolge = conn.queryList(DTOSchuelerSprachenfolge.QUERY_BY_SCHUELER_ID, DTOSchuelerSprachenfolge.class, id);

		// Map erstellen, um Fächer-Manager zu sammeln und nicht für jeden Schüler anlegen zu müssen.
		final Map<Integer, GostFaecherManager> mapGostFaecherManager = new HashMap<>();

		// gib die Abiturdaten zurück.
		return erzeugeAbiturdaten(dtoSchuelerAbitur, abidatenVergleich, sprachenfolge, faecher, mapGostFaecherManager);
	}

	@Override
	protected Abiturdaten map(final DTOSchuelerAbitur dto) throws ApiOperationException {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * Ermittelt die für das Abitur relevanten Daten für die Schüler mit den angegebenen IDs aus den in der Datenbank gespeicherten Abiturtabellen.
	 *
	 * @param idsSchueler die IDs der Schüler
	 *
	 * @return die für das Abitur relevanten Daten der Schüler als Map zur Schüler-ID.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Map<Long, Abiturdaten> getAbiturdatenFromIDs(final List<Long> idsSchueler) throws ApiOperationException {
		// Prüfe zunächst die Schüler auf Existenz.
		if (idsSchueler == null)
			throw new ApiOperationException(Status.NOT_FOUND);

		final List<Long> idsSchuelerNonNull = new ArrayList<>(idsSchueler.stream().filter(Objects::nonNull).toList());
		if (idsSchuelerNonNull.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);

		final List<DTOSchueler> dtoSchueler = conn.queryByKeyList(DTOSchueler.class, idsSchuelerNonNull);
		if ((dtoSchueler == null) || dtoSchueler.isEmpty() || (dtoSchueler.size() != idsSchuelerNonNull.size()))
			throw new ApiOperationException(Status.NOT_FOUND);

		// Lese die Abiturdaten anhand der IDs aus der Datenbank
		final Map<Long, List<DTOSchuelerAbitur>> mapDTOsSchuelerAbitur =
				conn.queryList(DTOSchuelerAbitur.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerAbitur.class, idsSchuelerNonNull)
						.stream().collect(Collectors.groupingBy(sAbi -> sAbi.Schueler_ID));

		final Map<Long, List<DTOSchuelerAbiturFach>> mapSchuelerAbiturFaecher =
				conn.queryList(DTOSchuelerAbiturFach.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerAbiturFach.class, idsSchuelerNonNull)
						.stream().collect(Collectors.groupingBy(f -> f.Schueler_ID));

		// Lese beide Tabellen mit den Informationen zu den belegten oder geprüften Sprachen aus.
		final Map<Long, List<DTOSchuelerSprachenfolge>> mapSchuelerSprachenfolge =
				conn.queryList(DTOSchuelerSprachenfolge.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerSprachenfolge.class, idsSchuelerNonNull)
						.stream().collect(Collectors.groupingBy(sf -> sf.Schueler_ID));

		// Erstelle die Map, in der die Rückgabe Werte gesammelt werden.
		final Map<Long, Abiturdaten> mapAbiturdaten = new HashMap<>();

		// Map erstellen, um Fächer-Manager zu sammeln und nicht für jeden Schüler anlegen zu müssen.
		final Map<Integer, GostFaecherManager> mapGostFaecherManager = new HashMap<>();

		for (final Long idSchueler : idsSchuelerNonNull) {
			// Hole die Abiturdaten zur Schüler-ID aus der Map. Wenn diese nicht existieren, gibt es keine Abiturdaten zum Schüler.
			if ((mapDTOsSchuelerAbitur.get(idSchueler) == null) || mapDTOsSchuelerAbitur.get(idSchueler).isEmpty())
				throw new ApiOperationException(Status.NOT_FOUND);
			// TODO: Hier wird der erste Eintrag verwendet. Hier müsste bei mehreren Einträgen der neuste Eintrag bestimmt werden.
			final DTOSchuelerAbitur dtoSchuelerAbitur = mapDTOsSchuelerAbitur.get(idSchueler).getFirst();

			// Hole die Abiturfächer zur Schüler-ID aus der Map. Wenn diese nicht existieren, gibt es keine Abiturdaten zum Schüler.
			if ((mapSchuelerAbiturFaecher.get(idSchueler) == null) || mapSchuelerAbiturFaecher.get(idSchueler).isEmpty())
				throw new ApiOperationException(Status.NOT_FOUND);
			final List<DTOSchuelerAbiturFach> faecher = mapSchuelerAbiturFaecher.get(idSchueler);

			// Hole die Sprachenfolge zur Schüler-ID aus der Map.
			final List<DTOSchuelerSprachenfolge> sprachenfolge = new ArrayList<>();
			if ((mapSchuelerSprachenfolge.get(idSchueler) != null) && !mapSchuelerSprachenfolge.get(idSchueler).isEmpty())
				sprachenfolge.addAll(mapSchuelerSprachenfolge.get(idSchueler));

			// Ermittle für einen Vergleich die Abiturdaten für Block I aus den Leistungsdaten, nutze dafür den entsprechenden Service.
			// TODO: Hier müsste auch die folgende Methode mehrere IDs umgestellt und aus der for-Schleife gezogen werden.
			final Abiturdaten abidatenVergleich = DBUtilsGostLaufbahn.get(conn, idSchueler);
			if (abidatenVergleich == null)
				throw new ApiOperationException(Status.NOT_FOUND);

			mapAbiturdaten.put(idSchueler, erzeugeAbiturdaten(dtoSchuelerAbitur, abidatenVergleich, sprachenfolge, faecher, mapGostFaecherManager));
		}

		// Gibt die Abiturdaten zurück.
		return mapAbiturdaten;
	}


	private Abiturdaten erzeugeAbiturdaten(final DTOSchuelerAbitur dtoSchuelerAbitur, final Abiturdaten abidatenVergleich,
			final List<DTOSchuelerSprachenfolge> sprachenfolge, final List<DTOSchuelerAbiturFach> faecher,
			final Map<Integer, GostFaecherManager> mapGostFaecherManager)
			throws ApiOperationException {
		final Schuljahresabschnitt schuljahresabschnittPruefung = conn.getUser().schuleGetAbschnittById(dtoSchuelerAbitur.Schuljahresabschnitts_ID);

		// Bestimme zunächst das Abiturjahr
		Integer abiturjahr = null;
		if (dtoSchuelerAbitur.Schuljahresabschnitts_ID != null) {
			final DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, dtoSchuelerAbitur.Schuljahresabschnitts_ID);
			if (abschnitt != null)
				abiturjahr = abschnitt.Jahr + 1;
		}
		if (abiturjahr == null)
			abiturjahr = abidatenVergleich.abiturjahr;

		// Lese die Oberstufenfächer aus der DB ein, um schnell Daten zu einzelnen Fächern nachschlagen zu können
		if (!mapGostFaecherManager.containsKey(abiturjahr))
			mapGostFaecherManager.put(abiturjahr, DBUtilsFaecherGost.getFaecherManager(abidatenVergleich.schuljahrAbitur, conn, abiturjahr));
		final GostFaecherManager gostFaecher = mapGostFaecherManager.get(abiturjahr);

		// Kopiere die DTOs in die Abiturdaten-Klasse
		final Abiturdaten abidaten = new Abiturdaten();
		abidaten.schuelerID = dtoSchuelerAbitur.Schueler_ID;
		abidaten.schuljahrAbitur = schuljahresabschnittPruefung.schuljahr;
		abidaten.abiturjahr = abiturjahr;
		abidaten.projektKursThema = dtoSchuelerAbitur.ProjektkursThema;
		abidaten.block1FehlstundenGesamt = (dtoSchuelerAbitur.FehlstundenSumme == null) ? -1 : dtoSchuelerAbitur.FehlstundenSumme;
		abidaten.block1FehlstundenUnentschuldigt = (dtoSchuelerAbitur.FehlstundenSummeUnentschuldigt == null) ? -1
				: dtoSchuelerAbitur.FehlstundenSummeUnentschuldigt;
		abidaten.latinum = false;
		for (final DTOSchuelerSprachenfolge folge : sprachenfolge)
			if ((Fach.L == Fach.data().getWertByKuerzel(folge.Sprache)) && (folge.LatinumErreicht != null)
					&& Boolean.TRUE.equals((folge.LatinumErreicht))) {
				abidaten.latinum = true;
				break;
			}
		abidaten.kleinesLatinum = false;
		for (final DTOSchuelerSprachenfolge folge : sprachenfolge)
			if ((Fach.L == Fach.data().getWertByKuerzel(folge.Sprache)) && (folge.KleinesLatinumErreicht != null)
					&& Boolean.TRUE.equals((folge.KleinesLatinumErreicht))) {
				abidaten.kleinesLatinum = true;
				break;
			}
		abidaten.graecum = false;
		for (final DTOSchuelerSprachenfolge folge : sprachenfolge)
			if ((Fach.G == Fach.data().getWertByKuerzel(folge.Sprache)) && (folge.GraecumErreicht != null)
					&& Boolean.TRUE.equals((folge.GraecumErreicht))) {
				abidaten.graecum = true;
				break;
			}
		abidaten.hebraicum = false;
		for (final DTOSchuelerSprachenfolge folge : sprachenfolge)
			if ((Fach.H == Fach.data().getWertByKuerzel(folge.Sprache)) && (folge.HebraicumErreicht != null)
					&& Boolean.TRUE.equals((folge.HebraicumErreicht))) {
				abidaten.hebraicum = true;
				break;
			}
		abidaten.besondereLernleistung = dtoSchuelerAbitur.BesondereLernleistungArt.kuerzel;
		abidaten.besondereLernleistungNotenKuerzel =
				Note.fromNotenpunkte(dtoSchuelerAbitur.BesondereLernleistungNotenpunkte).daten(abidaten.abiturjahr - 1).kuerzel;
		abidaten.besondereLernleistungThema = dtoSchuelerAbitur.BesondereLernleistungThema;

		abidaten.block1AnzahlKurse = dtoSchuelerAbitur.BlockI_AnzahlKurseEingebracht;
		abidaten.block1DefiziteGesamt = dtoSchuelerAbitur.BlockI_AnzahlDefiziteEingebracht;
		abidaten.block1DefiziteLK = dtoSchuelerAbitur.BlockI_AnzahlDefiziteLK;
		abidaten.block1PunktSummeGK = dtoSchuelerAbitur.BlockI_SummeNotenpunkteGK;
		abidaten.block1PunktSummeLK = dtoSchuelerAbitur.BlockI_SummeNotenpunkteLK;
		abidaten.block1PunktSummeNormiert = dtoSchuelerAbitur.BlockI_PunktsummeNormiert;
		abidaten.block1NotenpunkteDurchschnitt = dtoSchuelerAbitur.BlockI_NotenpunktdurchschnittEingebrachterKurse;
		abidaten.block1Zulassung = !"-".equals(dtoSchuelerAbitur.BlockI_HatZulassung);
		abidaten.freiwilligerRuecktritt = "R".equals(dtoSchuelerAbitur.BlockI_HatZulassung);

		abidaten.block2DefiziteGesamt = dtoSchuelerAbitur.Pruefung_AnzahlDefizite;
		abidaten.block2DefiziteLK = dtoSchuelerAbitur.Pruefung_AnzahlDefiziteLK;
		abidaten.block2PunktSumme = dtoSchuelerAbitur.Pruefung_Punktsumme;

		abidaten.gesamtPunkte = dtoSchuelerAbitur.AbiturGesamtPunktzahl;
		abidaten.gesamtPunkteVerbesserung = dtoSchuelerAbitur.VerbesserungAbPunktzahl;
		abidaten.pruefungBestanden = dtoSchuelerAbitur.Pruefung_hatBestanden;
		abidaten.note = dtoSchuelerAbitur.AbiturNote;

		// Füge die Fächerdaten hinzu...
		for (final DTOSchuelerAbiturFach dto : faecher) {
			final AbiturFachbelegung fach = new AbiturFachbelegung();
			final GostFach gostFach = gostFaecher.get(dto.Fach_ID);
			fach.fachID = dto.Fach_ID;
			fach.letzteKursart = (dto.KursartAllgemein == null) ? null : dto.KursartAllgemein.kuerzel;
			fach.abiturFach = (dto.AbiturFach == null) ? null : dto.AbiturFach.id;
			if (dto.KursartAllgemein == GostKursart.PJK) {
				if (gostFach == null) {
					final DTOFach dtoFach = conn.queryByKey(DTOFach.class, dto.Fach_ID);
					final DTOFach dtoLeitfach1 =
							((dtoFach == null) || (dtoFach.ProjektKursLeitfach1_ID == null)) ? null : conn.queryByKey(DTOFach.class,
									dtoFach.ProjektKursLeitfach1_ID);
					final DTOFach dtoLeitfach2 =
							((dtoFach == null) || (dtoFach.ProjektKursLeitfach2_ID == null)) ? null : conn.queryByKey(DTOFach.class,
									dtoFach.ProjektKursLeitfach2_ID);
					abidaten.projektkursLeitfach1Kuerzel = (dtoLeitfach1 == null) ? null : dtoLeitfach1.Kuerzel;
					abidaten.projektkursLeitfach2Kuerzel = (dtoLeitfach2 == null) ? null : dtoLeitfach2.Kuerzel;
				} else {
					abidaten.projektkursLeitfach1Kuerzel = gostFach.projektKursLeitfach1Kuerzel;
					abidaten.projektkursLeitfach2Kuerzel = gostFach.projektKursLeitfach2Kuerzel;
				}
			}
			fach.block1PunktSumme = dto.ZulassungPunktsumme;
			fach.block1NotenpunkteDurchschnitt = dto.ZulassungNotenpunktdurchschnitt;

			fach.block2NotenKuerzelPruefung = Note.fromNotenpunkte(dto.PruefungNotenpunkte).daten(abidaten.abiturjahr - 1).kuerzel;
			fach.block2PunkteZwischenstand = dto.PruefungPunktsummeZwischenstand;
			fach.block2MuendlichePruefungAbweichung = dto.PruefungMuendlichAbweichung;
			fach.block2MuendlichePruefungBestehen = dto.PruefungMuendlichBestehen;
			fach.block2MuendlichePruefungFreiwillig = dto.PruefungMuendlichFreiwillig;
			fach.block2MuendlichePruefungReihenfolge = dto.PruefungMuendlichReihenfolge;
			fach.block2MuendlichePruefungNotenKuerzel = Note.fromNotenpunkte(dto.PruefungMuendlichNotenpunkte).daten(abidaten.abiturjahr - 1).kuerzel;
			fach.block2Punkte = dto.PruefungPunktsummeGesamt;
			fach.block2Pruefer = dto.Fachlehrer_ID;
			if (dto.EF_HJ1_BelegungArt != AbiturBelegungsart.NICHT_BELEGT) {
				final AbiturFachbelegungHalbjahr ef1 = new AbiturFachbelegungHalbjahr();
				ef1.lehrer = dto.Fachlehrer_ID;
				ef1.idKurs = dto.Kurs_ID;
				ef1.halbjahrKuerzel = GostHalbjahr.EF1.kuerzel;
				ef1.notenkuerzel = Note.fromNotenpunkteString(dto.EF_HJ1_Notenpunkte).daten(abidaten.abiturjahr - 1).kuerzel;
				ef1.schriftlich = (dto.EF_HJ1_BelegungArt == AbiturBelegungsart.SCHRIFTLICH);
				ef1.block1gewertet = false;
				ef1.block1kursAufZeugnis = false;
				fach.belegungen[GostHalbjahr.EF1.id] = ef1;
			}
			if (dto.EF_HJ2_BelegungArt != AbiturBelegungsart.NICHT_BELEGT) {
				final AbiturFachbelegungHalbjahr ef2 = new AbiturFachbelegungHalbjahr();
				ef2.lehrer = dto.Fachlehrer_ID;
				ef2.idKurs = dto.Kurs_ID;
				ef2.halbjahrKuerzel = GostHalbjahr.EF2.kuerzel;
				ef2.notenkuerzel = Note.fromNotenpunkteString(dto.EF_HJ2_Notenpunkte).daten(abidaten.abiturjahr - 1).kuerzel;
				ef2.schriftlich = (dto.EF_HJ2_BelegungArt == AbiturBelegungsart.SCHRIFTLICH);
				ef2.block1gewertet = false;
				ef2.block1kursAufZeugnis = false;
				fach.belegungen[GostHalbjahr.EF2.id] = ef2;
			}
			if (dto.Q1_HJ1_BelegungArt != AbiturBelegungsart.NICHT_BELEGT) {
				final AbiturFachbelegungHalbjahr q11 = new AbiturFachbelegungHalbjahr();
				q11.lehrer = dto.Fachlehrer_ID;
				q11.idKurs = dto.Kurs_ID;
				q11.halbjahrKuerzel = GostHalbjahr.Q11.kuerzel;
				q11.notenkuerzel = Note.fromNotenpunkteString(dto.Q1_HJ1_Notenpunkte).daten(abidaten.abiturjahr - 1).kuerzel;
				q11.schriftlich = (dto.Q1_HJ1_BelegungArt == AbiturBelegungsart.SCHRIFTLICH);
				q11.wochenstunden = dto.Q1_HJ1_Wochenstunden;
				q11.block1gewertet = dto.Q1_HJ1_MarkiertFuerAbiturBerechnung.fuerBerechnung;
				q11.block1kursAufZeugnis = dto.Q1_HJ1_MarkiertFuerAbiturBerechnung.aufAbiturZeugnis;
				fach.belegungen[GostHalbjahr.Q11.id] = q11;
			}
			if (dto.Q1_HJ2_BelegungArt != AbiturBelegungsart.NICHT_BELEGT) {
				final AbiturFachbelegungHalbjahr q12 = new AbiturFachbelegungHalbjahr();
				q12.lehrer = dto.Fachlehrer_ID;
				q12.idKurs = dto.Kurs_ID;
				q12.halbjahrKuerzel = GostHalbjahr.Q12.kuerzel;
				q12.notenkuerzel = Note.fromNotenpunkteString(dto.Q1_HJ2_Notenpunkte).daten(abidaten.abiturjahr - 1).kuerzel;
				q12.schriftlich = (dto.Q1_HJ2_BelegungArt == AbiturBelegungsart.SCHRIFTLICH);
				q12.wochenstunden = dto.Q1_HJ2_Wochenstunden;
				q12.block1gewertet = dto.Q1_HJ2_MarkiertFuerAbiturBerechnung.fuerBerechnung;
				q12.block1kursAufZeugnis = dto.Q1_HJ2_MarkiertFuerAbiturBerechnung.aufAbiturZeugnis;
				fach.belegungen[GostHalbjahr.Q12.id] = q12;
			}
			if (dto.Q2_HJ1_BelegungArt != AbiturBelegungsart.NICHT_BELEGT) {
				final AbiturFachbelegungHalbjahr q21 = new AbiturFachbelegungHalbjahr();
				q21.lehrer = dto.Fachlehrer_ID;
				q21.idKurs = dto.Kurs_ID;
				q21.halbjahrKuerzel = GostHalbjahr.Q21.kuerzel;
				q21.notenkuerzel = Note.fromNotenpunkteString(dto.Q2_HJ1_Notenpunkte).daten(abidaten.abiturjahr - 1).kuerzel;
				q21.schriftlich = (dto.Q2_HJ1_BelegungArt == AbiturBelegungsart.SCHRIFTLICH);
				q21.wochenstunden = dto.Q2_HJ1_Wochenstunden;
				q21.block1gewertet = dto.Q2_HJ1_MarkiertFuerAbiturBerechnung.fuerBerechnung;
				q21.block1kursAufZeugnis = dto.Q2_HJ1_MarkiertFuerAbiturBerechnung.aufAbiturZeugnis;
				fach.belegungen[GostHalbjahr.Q21.id] = q21;
			}
			if (dto.Q2_HJ2_BelegungArt != AbiturBelegungsart.NICHT_BELEGT) {
				final AbiturFachbelegungHalbjahr q22 = new AbiturFachbelegungHalbjahr();
				q22.lehrer = dto.Fachlehrer_ID;
				q22.idKurs = dto.Kurs_ID;
				q22.halbjahrKuerzel = GostHalbjahr.Q22.kuerzel;
				q22.notenkuerzel = Note.fromNotenpunkteString(dto.Q2_HJ2_Notenpunkte).daten(abidaten.abiturjahr - 1).kuerzel;
				q22.schriftlich = (dto.Q2_HJ2_BelegungArt == AbiturBelegungsart.SCHRIFTLICH);
				q22.wochenstunden = dto.Q2_HJ2_Wochenstunden;
				q22.block1gewertet = dto.Q2_HJ2_MarkiertFuerAbiturBerechnung.fuerBerechnung;
				q22.block1kursAufZeugnis = dto.Q2_HJ2_MarkiertFuerAbiturBerechnung.aufAbiturZeugnis;
				fach.belegungen[GostHalbjahr.Q22.id] = q22;
			}
			abidaten.fachbelegungen.add(fach);
		}

		// Markiere alles Gost-Halbjahre als gewertet
		for (final GostHalbjahr hj : GostHalbjahr.values())
			abidaten.bewertetesHalbjahr[hj.id] = true;

		// Kopiere Abiturdaten, welche nicht in den Abitur-DB-Tabellen direkt vorhanden sind
		abidaten.sprachendaten = abidatenVergleich.sprachendaten;
		abidaten.bilingualeSprache = abidatenVergleich.bilingualeSprache;
		for (final AbiturFachbelegung fach : abidaten.fachbelegungen) {
			AbiturFachbelegung fachVergleich = null;
			for (final AbiturFachbelegung f : abidatenVergleich.fachbelegungen) {
				if (f.fachID == fach.fachID) {
					fachVergleich = f;
					break;
				}
			}
			if (fachVergleich == null)
				continue;
			fach.istFSNeu = fachVergleich.istFSNeu;
			for (final AbiturFachbelegungHalbjahr belegung : fach.belegungen) {
				if (belegung == null)
					continue;
				final AbiturFachbelegungHalbjahr belegungVergleich = fachVergleich.belegungen[GostHalbjahr.fromKuerzel(belegung.halbjahrKuerzel).id];
				if (belegungVergleich == null)
					continue;
				if (GostHalbjahr.fromKuerzel(belegung.halbjahrKuerzel).istEinfuehrungsphase()) {
					belegung.wochenstunden = belegungVergleich.wochenstunden;
				}
				final GostKursart tmpKursart = GostKursart.fromKuerzel(belegungVergleich.kursartKuerzel);
				belegung.kursartKuerzel = (tmpKursart == null) ? null : tmpKursart.kuerzel;
				fach.letzteKursart = belegung.kursartKuerzel;
				belegung.biliSprache = belegungVergleich.biliSprache;
				belegung.idKurs = belegungVergleich.idKurs;
				belegung.lehrer = belegungVergleich.lehrer;
				belegung.fehlstundenGesamt = belegungVergleich.fehlstundenGesamt;
				belegung.fehlstundenUnentschuldigt = belegungVergleich.fehlstundenUnentschuldigt;
			}
		}
		return abidaten;
	}


	/**
	 * Perisistiert die Abiturdaten aus dem übergebenen Abiturdaten-Manager in der Datenbank.
	 *
	 * @param manager   der Abiturdaten-Manager
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private void persistAbiturdaten(final @NotNull AbiturdatenManager manager) throws ApiOperationException {
		final @NotNull Abiturdaten abidaten = manager.daten();
		// Bestimme den Schuljahresabschnitt für das Abitur
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittBySchuljahrUndHalbjahr(manager.getSchuljahr(), 2);
		if (schuljahresabschnitt == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Es existiert kein Schuljahresabschnitt für das Schuljahr der Q2.2 der Abiturdaten.");
		// Bestimme den Eintrag des Schülers in der Abiturdaten-Tabelle, sofern einer vorhanden ist
		final long idSchueler = manager.daten().schuelerID;
		final @NotNull List<DTOSchuelerAbitur> dtosSchueler = conn.queryList(DTOSchuelerAbitur.QUERY_BY_SCHUELER_ID, DTOSchuelerAbitur.class, idSchueler);
		DTOSchuelerAbitur dtoSchueler = null;
		if (!dtosSchueler.isEmpty()) {
			// Prüfe, ob ein Eintrag mit dem korrekten Schuljahresabschnitt bereits existiert
			dtoSchueler = dtosSchueler.stream().filter(s -> (s.Schuljahresabschnitts_ID != null) && (s.Schuljahresabschnitts_ID == schuljahresabschnitt.id))
					.findFirst().orElse(null);
			if (dtoSchueler == null)
				dtoSchueler = dtosSchueler.stream().filter(s -> s.Schuljahresabschnitts_ID == null).findFirst().orElse(null);
		}
		if (dtoSchueler == null)
			dtoSchueler = new DTOSchuelerAbitur(conn.transactionGetNextID(DTOSchuelerAbitur.class), idSchueler);
		dtoSchueler.Schuljahresabschnitts_ID = schuljahresabschnitt.id;
		dtoSchueler.ProjektkursThema = abidaten.projektKursThema;
		dtoSchueler.FremdspracheSekIManuellGeprueft = false;
		dtoSchueler.BlockI_AnzahlKurseEingebracht = abidaten.block1AnzahlKurse;
		dtoSchueler.BlockI_AnzahlDefiziteEingebracht = abidaten.block1DefiziteGesamt;
		dtoSchueler.BlockI_AnzahlDefiziteLK = abidaten.block1DefiziteLK;
		dtoSchueler.BlockI_AnzahlDefizite0Punkte = manager.zaehleNullPunkteBelegungenInQPhase();
		dtoSchueler.BlockI_PunktsummeNormiert = abidaten.block1PunktSummeNormiert;
		dtoSchueler.BlockI_NotenpunktdurchschnittEingebrachterKurse = abidaten.block1NotenpunkteDurchschnitt;
		dtoSchueler.BlockI_SummeNotenpunkteGK = abidaten.block1PunktSummeGK;
		dtoSchueler.BlockI_SummeNotenpunkteLK = abidaten.block1PunktSummeLK;
		dtoSchueler.BlockI_HatZulassung = abidaten.block1Zulassung == null ? null : (Boolean.TRUE.equals(abidaten.block1Zulassung) ? "+" : "-");
		dtoSchueler.BesondereLernleistungArt = GostBesondereLernleistung.fromKuerzel(abidaten.besondereLernleistung);
		dtoSchueler.BesondereLernleistungNotenpunkte = manager.getNotenpunkteFromKuerzel(abidaten.besondereLernleistungNotenKuerzel);
		dtoSchueler.BesondereLernleistungThema = abidaten.besondereLernleistungThema;
		dtoSchueler.Pruefung_Punktsumme = abidaten.block2PunktSumme;
		dtoSchueler.Pruefung_AnzahlDefizite = abidaten.block2DefiziteGesamt;
		dtoSchueler.Pruefung_AnzahlDefiziteLK = abidaten.block2DefiziteLK;
		dtoSchueler.Pruefung_hatBestanden = abidaten.pruefungBestanden;
		dtoSchueler.AbiturNote = abidaten.note;
		dtoSchueler.AbiturGesamtPunktzahl = abidaten.gesamtPunkte;
		dtoSchueler.VerbesserungAbPunktzahl = abidaten.gesamtPunkteVerbesserung;
		dtoSchueler.VerbesserungBenoetigePunkte = ((abidaten.gesamtPunkte == null) || (abidaten.gesamtPunkteVerbesserung == null))
				? null : abidaten.gesamtPunkteVerbesserung - abidaten.gesamtPunkte;
		conn.transactionPersist(dtoSchueler);
		conn.transactionFlush();
		// Entferne eventuell vorhandene Daten zu den Abiturfächern
		final @NotNull List<DTOSchuelerAbiturFach> vorhandeneFaecher =
				conn.queryList(DTOSchuelerAbiturFach.QUERY_BY_SCHUELER_ID, DTOSchuelerAbiturFach.class, idSchueler);
		if (!vorhandeneFaecher.isEmpty()) {
			conn.transactionRemoveAll(vorhandeneFaecher);
			conn.transactionFlush();
		}
		// Schreibe die Daten zu allen Fachbelegungen in der Oberstufe
		long idNext = conn.transactionGetNextID(DTOSchuelerAbiturFach.class);
		for (final @NotNull AbiturFachbelegung belegung : abidaten.fachbelegungen) {
			final @NotNull GostFach fach = manager.faecher().get(belegung.fachID);
			if (fach == null)
				continue;
			// Informationen zum Fach bestimmen...
			final @NotNull DTOSchuelerAbiturFach dto = new DTOSchuelerAbiturFach(idNext++, idSchueler, fach.id);
			dto.FachKuerzel = fach.kuerzelAnzeige;
			dto.FachSortierung = fach.sortierung;
			dto.KursartAllgemein = GostKursart.fromKuerzel(belegung.letzteKursart);
			dto.AbiturFach = GostAbiturFach.fromID(belegung.abiturFach);
			// Information zu den einzelnen Halbjahren ermitteln
			Long idKurs = null;
			Long idFachlehrer = null;
			AbiturFachbelegungHalbjahr belegungHalbjahr = belegung.belegungen[GostHalbjahr.EF1.id];
			dto.EF_HJ1_Notenpunkte = manager.getNotenpunkteStringHalbjahresbelegung(belegungHalbjahr);
			dto.EF_HJ1_BelegungArt = AbiturdatenManager.getBelegungsartFromHalbjahresbelegung(belegungHalbjahr);
			idKurs = ((belegungHalbjahr != null) && (belegungHalbjahr.idKurs != null)) ? belegungHalbjahr.idKurs : idKurs;
			idFachlehrer = ((belegungHalbjahr != null) && (belegungHalbjahr.lehrer != null)) ? belegungHalbjahr.lehrer : idFachlehrer;
			belegungHalbjahr = belegung.belegungen[GostHalbjahr.EF2.id];
			dto.EF_HJ2_Notenpunkte = manager.getNotenpunkteStringHalbjahresbelegung(belegungHalbjahr);
			dto.EF_HJ2_BelegungArt = AbiturdatenManager.getBelegungsartFromHalbjahresbelegung(belegungHalbjahr);
			dto.Facharbeit_Notenpunkte = null; // nur BK
			dto.Facharbeit_MarkiertFuerAbiturBerechnung = null; // nur BK
			idKurs = ((belegungHalbjahr != null) && (belegungHalbjahr.idKurs != null)) ? belegungHalbjahr.idKurs : idKurs;
			idFachlehrer = ((belegungHalbjahr != null) && (belegungHalbjahr.lehrer != null)) ? belegungHalbjahr.lehrer : idFachlehrer;
			belegungHalbjahr = belegung.belegungen[GostHalbjahr.Q11.id];
			dto.Q1_HJ1_Wochenstunden = (belegungHalbjahr == null) ? null : belegungHalbjahr.wochenstunden;
			dto.Q1_HJ1_Notenpunkte = manager.getNotenpunkteStringHalbjahresbelegung(belegungHalbjahr);
			dto.Q1_HJ1_MarkiertFuerAbiturBerechnung = AbiturdatenManager.getKursmarkierungFromHalbjahresbelegung(belegungHalbjahr);
			dto.Q1_HJ1_BelegungArt = AbiturdatenManager.getBelegungsartFromHalbjahresbelegung(belegungHalbjahr);
			idKurs = ((belegungHalbjahr != null) && (belegungHalbjahr.idKurs != null)) ? belegungHalbjahr.idKurs : idKurs;
			idFachlehrer = ((belegungHalbjahr != null) && (belegungHalbjahr.lehrer != null)) ? belegungHalbjahr.lehrer : idFachlehrer;
			belegungHalbjahr = belegung.belegungen[GostHalbjahr.Q12.id];
			dto.Q1_HJ2_Wochenstunden = (belegungHalbjahr == null) ? null : belegungHalbjahr.wochenstunden;
			dto.Q1_HJ2_Notenpunkte = manager.getNotenpunkteStringHalbjahresbelegung(belegungHalbjahr);
			dto.Q1_HJ2_MarkiertFuerAbiturBerechnung = AbiturdatenManager.getKursmarkierungFromHalbjahresbelegung(belegungHalbjahr);
			dto.Q1_HJ2_BelegungArt = AbiturdatenManager.getBelegungsartFromHalbjahresbelegung(belegungHalbjahr);
			idKurs = ((belegungHalbjahr != null) && (belegungHalbjahr.idKurs != null)) ? belegungHalbjahr.idKurs : idKurs;
			idFachlehrer = ((belegungHalbjahr != null) && (belegungHalbjahr.lehrer != null)) ? belegungHalbjahr.lehrer : idFachlehrer;
			belegungHalbjahr = belegung.belegungen[GostHalbjahr.Q21.id];
			dto.Q2_HJ1_Wochenstunden = (belegungHalbjahr == null) ? null : belegungHalbjahr.wochenstunden;
			dto.Q2_HJ1_Notenpunkte = manager.getNotenpunkteStringHalbjahresbelegung(belegungHalbjahr);
			dto.Q2_HJ1_MarkiertFuerAbiturBerechnung = AbiturdatenManager.getKursmarkierungFromHalbjahresbelegung(belegungHalbjahr);
			dto.Q2_HJ1_BelegungArt = AbiturdatenManager.getBelegungsartFromHalbjahresbelegung(belegungHalbjahr);
			idKurs = ((belegungHalbjahr != null) && (belegungHalbjahr.idKurs != null)) ? belegungHalbjahr.idKurs : idKurs;
			idFachlehrer = ((belegungHalbjahr != null) && (belegungHalbjahr.lehrer != null)) ? belegungHalbjahr.lehrer : idFachlehrer;
			belegungHalbjahr = belegung.belegungen[GostHalbjahr.Q22.id];
			dto.Q2_HJ2_Wochenstunden = (belegungHalbjahr == null) ? null : belegungHalbjahr.wochenstunden;
			dto.Q2_HJ2_Notenpunkte = manager.getNotenpunkteStringHalbjahresbelegung(belegungHalbjahr);
			dto.Q2_HJ2_MarkiertFuerAbiturBerechnung = AbiturdatenManager.getKursmarkierungFromHalbjahresbelegung(belegungHalbjahr);
			dto.Q2_HJ2_BelegungArt = AbiturdatenManager.getBelegungsartFromHalbjahresbelegung(belegungHalbjahr);
			idKurs = ((belegungHalbjahr != null) && (belegungHalbjahr.idKurs != null)) ? belegungHalbjahr.idKurs : idKurs;
			idFachlehrer = ((belegungHalbjahr != null) && (belegungHalbjahr.lehrer != null)) ? belegungHalbjahr.lehrer : idFachlehrer;

			// Weitere Informationen zum ganzen Fach
			dto.Kurs_ID = idKurs;       // TODO in Abiturdaten bei den Halbjahresbelegungen aufnehmen !!!
			dto.Fachlehrer_ID = idFachlehrer; // aus der letzten Halbjahresbelegung übernehmen
			dto.ZulassungPunktsumme = belegung.block1PunktSumme;
			dto.ZulassungNotenpunktdurchschnitt = belegung.block1NotenpunkteDurchschnitt;
			dto.PruefungNotenpunkte = manager.getNotenpunkteFromKuerzel(belegung.block2NotenKuerzelPruefung);
			dto.PruefungPunktsummeZwischenstand = belegung.block2PunkteZwischenstand;
			dto.PruefungMuendlichAbweichung = belegung.block2MuendlichePruefungAbweichung;
			dto.PruefungMuendlichBestehen = belegung.block2MuendlichePruefungBestehen;
			dto.PruefungMuendlichFreiwillig = belegung.block2MuendlichePruefungFreiwillig;
			dto.PruefungMuendlichNotenpunkte = manager.getNotenpunkteFromKuerzel(belegung.block2MuendlichePruefungNotenKuerzel);
			dto.PruefungPunktsummeGesamt = belegung.block2Punkte;
			conn.transactionPersist(dto);
		}
		conn.transactionFlush();
	}


	/**
	 * Überträgt die Abitur-relevanten Daten aus den Leistungsdaten in den Abiturbereich
	 *
	 * @param id     die ID des Schülers
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response copyAbiturdatenAusLeistungsdaten(final long id) throws ApiOperationException {
		final Abiturdaten abidaten = (new DataGostSchuelerLaufbahnplanung(conn)).getById(id);
		if ((!abidaten.bewertetesHalbjahr[GostHalbjahr.Q11.id]) || (!abidaten.bewertetesHalbjahr[GostHalbjahr.Q12.id])
				|| (!abidaten.bewertetesHalbjahr[GostHalbjahr.Q21.id]) || (!abidaten.bewertetesHalbjahr[GostHalbjahr.Q22.id]))
			throw new ApiOperationException(Status.BAD_REQUEST, "Es liegen noch nicht alle Leistungen für die Qualifikationsphase vor.");
		final @NotNull GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherManager(abidaten.schuljahrAbitur, conn, abidaten.abiturjahr);
		final @NotNull GostJahrgangsdaten jahrgangsdaten = DataGostJahrgangsdaten.getJahrgangsdaten(conn, abidaten.abiturjahr);
		final @NotNull AbiturdatenManager manager =
				new AbiturdatenManager(SVWSKonfiguration.get().getServerMode(), abidaten, jahrgangsdaten, gostFaecher, GostBelegpruefungsArt.GESAMT);
		// Fasse die Belegungen Fächern zusammen, die inhaltsgleich sind - z.B. von bilingualen und nicht blingualen Sachfächern
		manager.kombiniereFachbelegungenEinesFaches();
		// Führe dann die Markierungsberechnung durch
		manager.applyErgebnisMarkierungsalgorithmus();
		// Persistiere die Abiturdaten in die zugehörige Datenbank-Tabelle
		persistAbiturdaten(manager);
		return Response.status(Status.NO_CONTENT).build();
	}

}
