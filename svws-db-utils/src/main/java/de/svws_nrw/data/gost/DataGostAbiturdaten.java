package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.AbiturKursMarkierung;
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
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbitur;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbiturFach;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.schema.Schema;
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
			Schuljahresabschnitt currentSja = null;
			for (final DTOSchuelerAbitur dtoSchuelerAbitur : dtosSchuelerAbitur) {
				final Schuljahresabschnitt dtoSja = (dtoSchuelerAbitur.Schuljahresabschnitts_ID) == null ? null
						: conn.getUser().schuleGetAbschnittById(dtoSchuelerAbitur.Schuljahresabschnitts_ID);
				if ((currentSja == null) || ((dtoSja != null) && ((dtoSja.schuljahr > currentSja.schuljahr)
						|| ((dtoSja.schuljahr == currentSja.schuljahr) && (dtoSja.abschnitt > currentSja.abschnitt))))) {
					current = dtoSchuelerAbitur;
					currentSja = dtoSja;
				}
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
		final Abiturdaten abidaten = erzeugeAbiturdaten(dtoSchuelerAbitur, abidatenVergleich, sprachenfolge, faecher, mapGostFaecherManager);
		if (abidaten == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es konnten keine Abiturdaten für den Schüler mit der ID %d aus der Datenbank zusammengestellt werden, da der zugehörige Schuljahresabschnitt in der Datenbank (noch) nicht angelegt ist.".formatted(id));
		return abidaten;
	}


	@Override
	public List<Abiturdaten> getList() throws ApiOperationException {
		if (schuljahresabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Abiturjahrgang angegeben oder dieser wurde nicht in der Datenbank gefunden.");
		final List<DTOSchueler> schuelerListe = DBUtilsGostLaufbahn.getSchuelerOfAbiturjahrgang(conn, schuljahresabschnitt.schuljahr + 1);
		if (schuelerListe.isEmpty())
			return new ArrayList<>();
		return getAbiturdatenFromIDs(schuelerListe.stream().map(s -> s.ID).toList());
	}


	@Override
	protected Abiturdaten map(final DTOSchuelerAbitur dto) throws ApiOperationException {
		// Bestimme die Daten komplett neu. Evtl. vorher zu persistierenden Daten müssen dann persistiert sein...
		conn.transactionFlush();
		return this.getById(dto.Schueler_ID);
	}


	private void mapAttributesFachbelegungenHalbjahr(final DTOSchuelerAbiturFach dto, final Map<String, Object> map, final int schuljahr)
			throws ApiOperationException {
		if (map.containsKey("block1gewertet") || map.containsKey("block1kursAufZeugnis")) {
			final String hjKuerzel = JSONMapper.convertToString(map.get("halbjahrKuerzel"), false, false, 4);
			final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(hjKuerzel);
			if (halbjahr == null)
				throw new ApiOperationException(Status.BAD_REQUEST, "Bei den Fachbelegungen des Halbjahres muss ein gültiges Kürzel angegeben werden.");
			if (halbjahr == GostHalbjahr.Q11) {
				boolean block1gewertet = dto.Q1_HJ1_MarkiertFuerAbiturBerechnung.fuerBerechnung;
				boolean block1kursAufZeugnis = dto.Q1_HJ1_MarkiertFuerAbiturBerechnung.aufAbiturZeugnis;
				if (map.containsKey("block1gewertet"))
					block1gewertet = JSONMapper.convertToBoolean(map.get("block1gewertet"), false);
				if (map.containsKey("block1kursAufZeugnis"))
					block1kursAufZeugnis = JSONMapper.convertToBoolean(map.get("block1kursAufZeugnis"), false);
				dto.Q1_HJ1_MarkiertFuerAbiturBerechnung = new AbiturKursMarkierung(block1gewertet, block1kursAufZeugnis);
			} else if (halbjahr == GostHalbjahr.Q12) {
				boolean block1gewertet = dto.Q1_HJ2_MarkiertFuerAbiturBerechnung.fuerBerechnung;
				boolean block1kursAufZeugnis = dto.Q1_HJ2_MarkiertFuerAbiturBerechnung.aufAbiturZeugnis;
				if (map.containsKey("block1gewertet"))
					block1gewertet = JSONMapper.convertToBoolean(map.get("block1gewertet"), false);
				if (map.containsKey("block1kursAufZeugnis"))
					block1kursAufZeugnis = JSONMapper.convertToBoolean(map.get("block1kursAufZeugnis"), false);
				dto.Q1_HJ2_MarkiertFuerAbiturBerechnung = new AbiturKursMarkierung(block1gewertet, block1kursAufZeugnis);
			} else if (halbjahr == GostHalbjahr.Q21) {
				boolean block1gewertet = dto.Q2_HJ1_MarkiertFuerAbiturBerechnung.fuerBerechnung;
				boolean block1kursAufZeugnis = dto.Q2_HJ1_MarkiertFuerAbiturBerechnung.aufAbiturZeugnis;
				if (map.containsKey("block1gewertet"))
					block1gewertet = JSONMapper.convertToBoolean(map.get("block1gewertet"), false);
				if (map.containsKey("block1kursAufZeugnis"))
					block1kursAufZeugnis = JSONMapper.convertToBoolean(map.get("block1kursAufZeugnis"), false);
				dto.Q2_HJ1_MarkiertFuerAbiturBerechnung = new AbiturKursMarkierung(block1gewertet, block1kursAufZeugnis);
			} else if (halbjahr == GostHalbjahr.Q22) {
				boolean block1gewertet = dto.Q2_HJ2_MarkiertFuerAbiturBerechnung.fuerBerechnung;
				boolean block1kursAufZeugnis = dto.Q2_HJ2_MarkiertFuerAbiturBerechnung.aufAbiturZeugnis;
				if (map.containsKey("block1gewertet"))
					block1gewertet = JSONMapper.convertToBoolean(map.get("block1gewertet"), false);
				if (map.containsKey("block1kursAufZeugnis"))
					block1kursAufZeugnis = JSONMapper.convertToBoolean(map.get("block1kursAufZeugnis"), false);
				dto.Q2_HJ2_MarkiertFuerAbiturBerechnung = new AbiturKursMarkierung(block1gewertet, block1kursAufZeugnis);
			}
		}
	}


	private void mapAttributeFachbelegungen(final DTOSchuelerAbiturFach dto, final String name, final Object value, final Map<String, Object> map,
			final int schuljahr) throws ApiOperationException {
		switch (name) {
			case "fachID" -> {
				final Long idFach = JSONMapper.convertToLong(value, false);
				if (dto.Fach_ID != idFach)
					throw new ApiOperationException(Status.BAD_REQUEST, "Die Fach-ID aus dem Patch passt nicht zu der Fach-ID aus der Anfrage.");
			}
			case "block1PunktSumme" -> dto.ZulassungPunktsumme = JSONMapper.convertToIntegerInRange(value, true, 0, 121);
			case "block1NotenpunkteDurchschnitt" -> dto.ZulassungNotenpunktdurchschnitt = JSONMapper.convertToDouble(value, true);
			case "block2NotenKuerzelPruefung" -> {
				final String kuerzel = JSONMapper.convertToString(value, true, true, 2);
				dto.PruefungNotenpunkte = (kuerzel == null) ? null : Note.fromKuerzel(kuerzel).daten(schuljahr).notenpunkte;
			}
			case "block2PunkteZwischenstand" -> dto.PruefungPunktsummeZwischenstand = JSONMapper.convertToIntegerInRange(value, true, 0, 76);
			case "block2MuendlichePruefungAbweichung" -> dto.PruefungMuendlichAbweichung = JSONMapper.convertToBoolean(value, true);
			case "block2MuendlichePruefungBestehen" -> dto.PruefungMuendlichBestehen = JSONMapper.convertToBoolean(value, true);
			case "block2MuendlichePruefungFreiwillig" -> dto.PruefungMuendlichFreiwillig = JSONMapper.convertToBoolean(value, true);
			case "block2MuendlichePruefungReihenfolge" -> dto.PruefungMuendlichReihenfolge = JSONMapper.convertToIntegerInRange(value, true, 1, 5);
			case "block2MuendlichePruefungNotenKuerzel" -> {
				final String kuerzel = JSONMapper.convertToString(value, true, true, 2);
				dto.PruefungMuendlichNotenpunkte = (kuerzel == null) ? null : Note.fromKuerzel(kuerzel).daten(schuljahr).notenpunkte;
			}
			case "block2Punkte" -> dto.PruefungPunktsummeGesamt = JSONMapper.convertToIntegerInRange(value, true, 0, 76);
			case "block2Pruefer" -> {
				final Long idFachlehrer = JSONMapper.convertToLongInRange(value, true, 0L, null, "block2Pruefer");
				if (idFachlehrer != null) {
					final DTOLehrer dtoLehrer = conn.queryByKey(DTOLehrer.class, idFachlehrer);
					if (dtoLehrer == null)
						throw new ApiOperationException(Status.NOT_FOUND, "Ein Fachlehrer mit der ID %d existiert nicht.".formatted(idFachlehrer));
				}
				dto.Fachlehrer_ID = idFachlehrer;
			}
			case "belegungen" -> {
				final List<Map<String, Object>> listMapsBelegungen = JSONMapper.toListOfMaps(value);
				for (final Map<String, Object> mapBelegung : listMapsBelegungen)
					if (mapBelegung != null)
						mapAttributesFachbelegungenHalbjahr(dto, mapBelegung, schuljahr);
			}
			default -> {
				// ignoriere Attribute, die nicht für die Patch-Operation relevant sind
			}
		}
	}

	@Override
	protected void mapAttribute(final DTOSchuelerAbitur dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		final Schuljahresabschnitt sja = conn.getUser().schuleGetAbschnittById(dto.Schuljahresabschnitts_ID);
		switch (name) {
			case "schuelerID" -> {
				final Long idSchueler = JSONMapper.convertToLong(value, false);
				if (dto.Schueler_ID != idSchueler)
					throw new ApiOperationException(Status.BAD_REQUEST, "Die Schüler-ID aus dem Patch passt nicht zu der Schüler-ID aus der Anfrage.");
			}
			case "block1FehlstundenGesamt" -> dto.FehlstundenSumme = JSONMapper.convertToIntegerInRange(value, true, 0, 10000);
			case "block1FehlstundenUnentschuldigt" -> dto.FehlstundenSummeUnentschuldigt = JSONMapper.convertToIntegerInRange(value, true, 0, 10000);
			case "besondereLernleistung" ->
				dto.BesondereLernleistungArt = GostBesondereLernleistung.fromKuerzel(JSONMapper.convertToString(value, false, false, 1));
			case "besondereLernleistungNotenKuerzel" -> {
				final String kuerzel = JSONMapper.convertToString(value, true, true, 2);
				final Note note = Note.fromKuerzel(kuerzel);
				dto.BesondereLernleistungNotenpunkte = (note == null) ? null : note.daten(sja.schuljahr).notenpunkte;
			}
			case "besondereLernleistungThema" ->
				dto.BesondereLernleistungThema = JSONMapper.convertToString(value, true, false, Schema.tab_SchuelerAbitur.col_Thema_BLL.datenlaenge());

			case "block1AnzahlKurse" -> dto.BlockI_AnzahlKurseEingebracht = JSONMapper.convertToIntegerInRange(value, true, 0, 41);
			case "block1DefiziteGesamt" -> dto.BlockI_AnzahlDefiziteEingebracht = JSONMapper.convertToIntegerInRange(value, true, 0, 41);
			case "block1DefiziteLK" -> dto.BlockI_AnzahlDefiziteLK = JSONMapper.convertToIntegerInRange(value, true, 0, 9);
			case "block1PunktSummeGK" -> dto.BlockI_SummeNotenpunkteGK = JSONMapper.convertToIntegerInRange(value, true, 0, 481);
			case "block1PunktSummeLK" -> dto.BlockI_SummeNotenpunkteLK = JSONMapper.convertToIntegerInRange(value, true, 0, 241);
			case "block1PunktSummeNormiert" -> dto.BlockI_PunktsummeNormiert = JSONMapper.convertToIntegerInRange(value, true, 0, 601);
			case "block1NotenpunkteDurchschnitt" -> dto.BlockI_NotenpunktdurchschnittEingebrachterKurse = JSONMapper.convertToDouble(value, true);
			case "block1Zulassung", "freiwilligerRuecktritt" -> {
				boolean block1Zulassung = !"-".equals(dto.BlockI_HatZulassung);
				boolean freiwilligerRuecktritt = "R".equals(dto.BlockI_HatZulassung);
				if (map.containsKey("block1Zulassung"))
					block1Zulassung = JSONMapper.convertToBoolean(map.get("block1Zulassung"), false);
				if (map.containsKey("freiwilligerRuecktritt"))
					freiwilligerRuecktritt = JSONMapper.convertToBoolean(map.get("freiwilligerRuecktritt"), false);
				dto.BlockI_HatZulassung = freiwilligerRuecktritt ? "R" : (block1Zulassung ? "+" : "-");
			}

			case "block2DefiziteGesamt" -> dto.Pruefung_AnzahlDefizite = JSONMapper.convertToIntegerInRange(value, true, 0, 5);
			case "block2DefiziteLK" -> dto.Pruefung_AnzahlDefiziteLK = JSONMapper.convertToIntegerInRange(value, true, 0, 3);
			case "block2PunktSumme" -> dto.Pruefung_Punktsumme = JSONMapper.convertToIntegerInRange(value, true, 0, 301);

			case "gesamtPunkte" -> dto.AbiturGesamtPunktzahl = JSONMapper.convertToIntegerInRange(value, true, 0, 901);
			case "gesamtPunkteVerbesserung" -> dto.VerbesserungAbPunktzahl = JSONMapper.convertToIntegerInRange(value, true, 0, 901);
			case "pruefungBestanden" -> dto.Pruefung_hatBestanden = JSONMapper.convertToBoolean(value, true);
			case "note" -> {
				final String strNote = JSONMapper.convertToString(value, true, false, 3);
				if (strNote != null) {
					if (strNote.length() == 2)
						throw new ApiOperationException(Status.BAD_REQUEST,
								"Die Abiturnote muss entweder eine Note ohne Nachkommstelle oder eine Note mit genau einer Nachkommastelle sein. '1,' ist nicht zulässig.");
					final char note0 = strNote.charAt(0);
					if ((note0 != '1') && (note0 != '2') && (note0 != '3') && (note0 != '4'))
						throw new ApiOperationException(Status.BAD_REQUEST, "Es sind nur Abiturnoten zwischen 1 und 4 zulässig.");
					if (strNote.length() == 3) {
						final char note2 = strNote.charAt(2);
						if ((note2 != '0') && (note2 != '1') && (note2 != '2') && (note2 != '3') && (note2 != '4') && (note2 != '5') && (note2 != '6')
								&& (note2 != '7') && (note2 != '8') && (note2 != '9'))
							throw new ApiOperationException(Status.BAD_REQUEST, "Für die Nachkommstellen sind nur Ziffern von 0-9 zulässig.");
						if ((note0 == '4') && (note2 != '0'))
							throw new ApiOperationException(Status.BAD_REQUEST, "Bei einer Note 4 ist eine Nachkommstellen ungleich 0 nicht zulässig.");
					}
				}
				dto.AbiturNote = strNote;
			}
			// "fachbelegungen" werden hier übersprungen, da die Einträge auf DTOSchuelerAbiturFach gemappt werden
			default -> {
				// ignoriere Attribute, die nicht für die Patch-Operation relevant sind
			}
		}
	}


	/**
	 * Führt einen Patch auf Abiturdaten aus.
	 *
	 * @param id   die ID des Schülers
	 * @param is   der Input-Stream mit den Daten des Patches
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException
	 */
	public Response patchAbiturdatenAsResponse(final Long id, final InputStream is) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Patchen muss eine ID angegeben werden. Null ist nicht zulässig.");
		final Map<String, Object> attributesToPatch = JSONMapper.toMap(is);
		if (attributesToPatch.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST, "In dem Patch sind keine Daten enthalten.");
		final DTOSchuelerAbitur dto = getDatabaseDTOByID(id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Konnte die Abiturdaten für die angegebene ID in der Datenbank nicht finden.");

		// Patche zunächst die Abiturdaten, die keinen speziellen Fach-bezug haben
		for (final Entry<String, Object> patchMapping : attributesToPatch.entrySet())
			mapAttribute(dto, patchMapping.getKey(), patchMapping.getValue(), attributesToPatch);

		// Patche zunächst die Abiturdaten für die einzelnen Fachbelegungen
		final Object tmpFachbelegungen = attributesToPatch.get("fachbelegungen");
		final List<DTOSchuelerAbiturFach> dtosFachbelegungen = new ArrayList<>();
		if (tmpFachbelegungen != null) {
			final List<Map<String, Object>> listMapsFachbelegungen = JSONMapper.toListOfMaps(tmpFachbelegungen);
			// Bestimme zunächst alle Fächer-IDs im Patch
			final @NotNull List<Long> idsFaecher = new ArrayList<>();
			for (final Map<String, Object> mapFachbelegung : listMapsFachbelegungen)
				idsFaecher.add(JSONMapper.convertToLong(mapFachbelegung.get("fachID"), false));
			if (!idsFaecher.isEmpty())
				dtosFachbelegungen.addAll(conn.queryList("SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Schueler_ID = ?1 AND e.Fach_ID IN ?2",
						DTOSchuelerAbiturFach.class, id, idsFaecher));
			if (idsFaecher.size() != dtosFachbelegungen.size())
				throw new ApiOperationException(Status.NOT_FOUND, "Es konnten nicht alle Fachbelegungen aus dem Patch in der Datenbank gefunden werden.");
			final Map<Long, DTOSchuelerAbiturFach> mapDTOsFachbelegungen = dtosFachbelegungen.stream().collect(Collectors.toMap(fb -> fb.Fach_ID, fb -> fb));
			final Schuljahresabschnitt sja = conn.getUser().schuleGetAbschnittById(dto.Schuljahresabschnitts_ID);
			// Führe einen Patch auf die einzelnen Fachbelegungen durch.
			for (final Map<String, Object> mapFachbelegung : listMapsFachbelegungen) {
				final long idFach = JSONMapper.convertToLong(mapFachbelegung.get("fachID"), false);
				final DTOSchuelerAbiturFach dtoFachbelegung = mapDTOsFachbelegungen.get(idFach);
				for (final Entry<String, Object> patchMapping : mapFachbelegung.entrySet())
					mapAttributeFachbelegungen(dtoFachbelegung, patchMapping.getKey(), patchMapping.getValue(), mapFachbelegung, sja.schuljahr);
			}
		}

		// Persistiere die einzelnen DTOs
		conn.transactionPersist(dto);
		conn.transactionPersistAll(dtosFachbelegungen);
		return Response.status(Status.NO_CONTENT).build();
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
	public List<Abiturdaten> getAbiturdatenFromIDs(final List<Long> idsSchueler) throws ApiOperationException {
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

		// Erstelle die Liste, in der die Rückgabe Werte gesammelt werden.
		final List<Abiturdaten> listAbiturdaten = new ArrayList<>();

		// Map erstellen, um Fächer-Manager zu sammeln und nicht für jeden Schüler anlegen zu müssen.
		final Map<Integer, GostFaecherManager> mapGostFaecherManager = new HashMap<>();

		for (final Long idSchueler : idsSchuelerNonNull) {
			// Hole die Abiturdaten zur Schüler-ID aus der Map. Wenn diese nicht existieren, gibt es keine Abiturdaten zum Schüler.
			if ((mapDTOsSchuelerAbitur.get(idSchueler) == null) || mapDTOsSchuelerAbitur.get(idSchueler).isEmpty())
				continue;
			// TODO: Hier wird der erste Eintrag verwendet. Hier müsste bei mehreren Einträgen der neuste Eintrag bestimmt werden.
			final DTOSchuelerAbitur dtoSchuelerAbitur = mapDTOsSchuelerAbitur.get(idSchueler).getFirst();

			// Hole die Abiturfächer zur Schüler-ID aus der Map. Wenn diese nicht existieren, gibt es keine Abiturdaten zum Schüler.
			if ((mapSchuelerAbiturFaecher.get(idSchueler) == null) || mapSchuelerAbiturFaecher.get(idSchueler).isEmpty())
				continue;
			final List<DTOSchuelerAbiturFach> faecher = mapSchuelerAbiturFaecher.get(idSchueler);

			// Hole die Sprachenfolge zur Schüler-ID aus der Map.
			final List<DTOSchuelerSprachenfolge> sprachenfolge = new ArrayList<>();
			if ((mapSchuelerSprachenfolge.get(idSchueler) != null) && !mapSchuelerSprachenfolge.get(idSchueler).isEmpty())
				sprachenfolge.addAll(mapSchuelerSprachenfolge.get(idSchueler));

			// Ermittle für einen Vergleich die Abiturdaten für Block I aus den Leistungsdaten, nutze dafür den entsprechenden Service.
			// TODO: Hier müsste auch die folgende Methode mehrere IDs umgestellt und aus der for-Schleife gezogen werden.
			final Abiturdaten abidatenVergleich = DBUtilsGostLaufbahn.get(conn, idSchueler);
			if (abidatenVergleich == null)
				continue;

			final Abiturdaten abidaten = erzeugeAbiturdaten(dtoSchuelerAbitur, abidatenVergleich, sprachenfolge, faecher, mapGostFaecherManager);
			if (abidaten != null)
				listAbiturdaten.add(abidaten);
		}

		// Gibt die Liste der Abiturdaten zurück.
		return listAbiturdaten;
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
	public Map<Long, Abiturdaten> getMapAbiturdatenFromIDs(final List<Long> idsSchueler) throws ApiOperationException {
		return getAbiturdatenFromIDs(idsSchueler).stream().collect(Collectors.toMap(a -> a.schuelerID, a -> a));
	}


	private Abiturdaten erzeugeAbiturdaten(final DTOSchuelerAbitur dtoSchuelerAbitur, final Abiturdaten abidatenVergleich,
			final List<DTOSchuelerSprachenfolge> sprachenfolge, final List<DTOSchuelerAbiturFach> faecher,
			final Map<Integer, GostFaecherManager> mapGostFaecherManager)
			throws ApiOperationException {
		final Schuljahresabschnitt schuljahresabschnittPruefung = (dtoSchuelerAbitur.Schuljahresabschnitts_ID != null)
				? conn.getUser().schuleGetAbschnittById(dtoSchuelerAbitur.Schuljahresabschnitts_ID)
				: conn.getUser().schuleGetAbschnittBySchuljahrUndHalbjahr(abidatenVergleich.schuljahrAbitur, 2);
		if (schuljahresabschnittPruefung == null)
			return null;

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
		abidaten.block1FehlstundenGesamt = (dtoSchuelerAbitur.FehlstundenSumme == null) ? 0 : dtoSchuelerAbitur.FehlstundenSumme;
		abidaten.block1FehlstundenUnentschuldigt = (dtoSchuelerAbitur.FehlstundenSummeUnentschuldigt == null) ? 0
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
		abidaten.block1Zulassung = "+".equals(dtoSchuelerAbitur.BlockI_HatZulassung);
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
		final Abiturdaten abidaten = (new DataGostSchuelerLaufbahnplanung(conn, null)).getById(id);
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
