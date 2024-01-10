package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.data.gost.GostStatistikFachwahlHalbjahr;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.kursblockung.KursblockungAlgorithmus;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.jahrgang.Jahrgaenge;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelParameterTyp;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.core.utils.gost.GostFachwahlManager;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schule.DataSchuljahresabschnitte;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFaecher;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungKurslehrer;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungRegel;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungRegelParameter;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchiene;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchueler;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO {@link GostBlockungsdaten}.
 */
public final class DataGostBlockungsdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBlockungsdaten}.
	 *
	 * @param conn    die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostBlockungsdaten(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOGostBlockung} in einen Core-DTO
	 * {@link GostBlockungsdaten}.
	 */
	private final Function<DTOGostBlockung, GostBlockungsdaten> dtoMapper = (final DTOGostBlockung blockung) -> {
		final GostBlockungsdaten daten = new GostBlockungsdaten();
		daten.id = blockung.ID;
		daten.name = blockung.Name;
		daten.abijahrgang = blockung.Abi_Jahrgang;
		daten.gostHalbjahr = blockung.Halbjahr.id;
		daten.istAktiv = blockung.IstAktiv;
		return daten;
	};


	/**
	 * Prüft, ob die übergebene Blockung nur ein Vorlage-Ergebnis hat oder auch weitere
	 * Ergebnisse.
	 *
	 * @param conn       die Datenbankverbindung
	 * @param blockung   die zu prüfende Blockung
	 *
	 * @return das Vorlage-Ergebnis, falls dies das einzige Ergebnis ist, sonst null
	 */
	public static DTOGostBlockungZwischenergebnis pruefeNurVorlageErgebnis(final DBEntityManager conn, final DTOGostBlockung blockung) {
		if (blockung == null)
			throw OperationError.NOT_FOUND.exception("Blockung nicht gefunden.");
		final List<DTOGostBlockungZwischenergebnis> ergebnisse = conn.queryNamed("DTOGostBlockungZwischenergebnis.blockung_id", blockung.ID, DTOGostBlockungZwischenergebnis.class);
		if ((ergebnisse == null) || (ergebnisse.isEmpty()))
			throw OperationError.INTERNAL_SERVER_ERROR.exception("Kein Vorlage-Ergebnis für die Blockung in der Datenbank vorhanden.");
		if (ergebnisse.size() > 1)
			return null;
		return ergebnisse.get(0);
	}


	/**
	 * Prüft, ob der Schüler bei dem angegebehen GOSt-Halbjahr des angegeben Halbjahres an der Schule gewesen ist.
	 *
	 * @param conn          die Datenbankverbindung
	 * @param dto           der Schüler
	 * @param halbjahr      das GOSt-Halbjahr
	 * @param abijahrgang   der Abiturjahrgang
	 *
	 * @return true, wenn der Schüler an der Schule ist, und ansonsten false
	 */
	public static boolean checkIstAnSchule(final DBEntityManager conn, final DTOSchueler dto, final GostHalbjahr halbjahr, final int abijahrgang) {
		// Ist ein aktueller Schuljahresabschnitt zugewiesen? Das ist notwendig, wenn der Schüler an der Schule ist oder war
		if (dto.Schuljahresabschnitts_ID == null)
			return false;
		// Dieser Schuljahresabschnitt muss auch gültig sein
		final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, dto.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			return false;
		// In dem Fall, dass der Schüler bereits abgegangen ist, wird das Entlassdatum und der Schuljahresabschnitt mit dem Schuljahresabschnitt des GOSt-Halbjahres abgegleichen
		if ((dto.Status == SchuelerStatus.ABGANG) || (dto.Status == SchuelerStatus.ABSCHLUSS)) {
			final int blockungSchuljahr = halbjahr.getSchuljahrFromAbiturjahr(abijahrgang);
			final int[] entlassung = (dto.Entlassdatum == null) ? null : DateUtils.getSchuljahrUndHalbjahrFromDateISO8601(dto.Entlassdatum);
			if (entlassung == null) {
				// Prüfe, ob der aktuelle Schuljahresabschnitt des Schülers < dem Schuljahresabschnitt des GOSt-Halbjahres / der Blockung ist -> dann muss der Schüler ignoriert werden
				if ((schuljahresabschnitt.Jahr < blockungSchuljahr) || ((schuljahresabschnitt.Jahr == blockungSchuljahr) && (schuljahresabschnitt.Abschnitt < halbjahr.halbjahr)))
					return false;
			} else {
				// Prüfe, ob der Schuljahresabschnitt der Entlassung des Schülers < dem Schuljahresabschnitt des GOSt-Halbjahres / der Blockung ist -> dann muss der Schüler ignoriert werden
				if ((entlassung[0] < blockungSchuljahr) || ((entlassung[0] == blockungSchuljahr) && (entlassung[1] < halbjahr.halbjahr)))
					return false;
			}
		}
		return true;
	}


	/**
	 * Bestimmt für die angegebene ID alle Daten für die Initialisierung eines
	 * Blockungsdaten-Managers zur Bestimmung der Blockungsdaten.
	 * Folgende Information werden nicht geladen: die Liste
	 * der Blockungsergebnisse und das aktuelle Blockungsergebnis
	 *
	 * @param id   die ID der Blockung
	 *
	 * @return der Blockungsdaten-Manager
	 */
	public GostBlockungsdatenManager getBlockungsdatenManagerFromDB(final Long id) {
		// Bestimme den aktuellen Schuljahresabschnitt
		final DTOEigeneSchule schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOSchuljahresabschnitte dtoSchuleSchuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (dtoSchuleSchuljahresabschnitt == null)
			throw OperationError.NOT_FOUND.exception("Der Schuljahresabschnitt für die Schule konnte nicht aus der Datenbank bestimmt werden.");

		// Bestimme die Blockung
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, id);
		if (blockung == null)
			throw OperationError.NOT_FOUND.exception("Keine Blockung mit der ID %d gefunden.".formatted(id));

		// Bestimme den Schuljahresabschnitt (oder null) für die Blockung, sofern der Schuljahresabschnitt schon angelegt ist.
		Schuljahresabschnitt lehrerSchuljahresabschnitt = DataSchuljahresabschnitte.getFromSchuljahrUndAbschnitt(conn, blockung.Halbjahr.getSchuljahrFromAbiturjahr(blockung.Abi_Jahrgang), blockung.Halbjahr.halbjahr);
		if (lehrerSchuljahresabschnitt == null)
			lehrerSchuljahresabschnitt = DataSchuljahresabschnitte.dtoMapper.apply(dtoSchuleSchuljahresabschnitt);

		// Fächer hinzufügen.
		final GostFaecherManager faecherManager = (new DataGostFaecher(conn, blockung.Abi_Jahrgang)).getListInternal();
		if (faecherManager == null)
			throw OperationError.NOT_FOUND.exception();
		final GostBlockungsdatenManager manager = new GostBlockungsdatenManager(dtoMapper.apply(blockung), faecherManager);

		// Schienen hinzufügen.
		final List<DTOGostBlockungSchiene> schienen = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", blockung.ID, DTOGostBlockungSchiene.class);
		for (final DTOGostBlockungSchiene schiene : schienen)
			manager.schieneAdd(DataGostBlockungSchiene.dtoMapper.apply(schiene));

		// Kurse hinzufügen.
		final List<DTOGostBlockungKurs> kurse = conn.queryNamed("DTOGostBlockungKurs.blockung_id", blockung.ID, DTOGostBlockungKurs.class);
		for (final DTOGostBlockungKurs kurs : kurse)
			manager.kursAdd(DataGostBlockungKurs.dtoMapper.apply(kurs));

		// Kurs-Lehrer hinzufügen
		final List<Long> kursIDs = kurse.stream().map(k -> k.ID).toList();
		if (!kursIDs.isEmpty()) {
			final List<DTOGostBlockungKurslehrer> kurslehrerListe = conn.queryNamed("DTOGostBlockungKurslehrer.blockung_kurs_id.multiple", kursIDs, DTOGostBlockungKurslehrer.class);
			final List<Long> kurslehrerIDs = kurslehrerListe.stream().map(kl -> kl.Lehrer_ID).distinct().toList();
			if (!kurslehrerIDs.isEmpty()) {
				final Map<Long, DTOLehrer> mapLehrer = conn.queryNamed("DTOLehrer.id.multiple", kurslehrerIDs, DTOLehrer.class)
						.stream().collect(Collectors.toMap(l -> l.ID, l -> l));
				final Map<Long, DTOLehrerAbschnittsdaten> mapLehrerabschnittsdaten = conn.queryList("SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID IN ?1 AND e.Schuljahresabschnitts_ID = ?2", DTOLehrerAbschnittsdaten.class, kurslehrerIDs, lehrerSchuljahresabschnitt.id)
					.stream().collect(Collectors.toMap(l -> l.Lehrer_ID, l -> l));
				for (final DTOGostBlockungKurslehrer kurslehrer : kurslehrerListe) {
					final DTOLehrer lehrer = mapLehrer.get(kurslehrer.Lehrer_ID);
					if (lehrer == null)
						throw OperationError.NOT_FOUND.exception();
					final DTOLehrerAbschnittsdaten abschnitt = mapLehrerabschnittsdaten.get(kurslehrer.Lehrer_ID);
					final GostBlockungKursLehrer kl = new GostBlockungKursLehrer();
					kl.id = lehrer.ID;
					kl.kuerzel = lehrer.Kuerzel;
					kl.vorname = lehrer.Vorname;
					kl.nachname = lehrer.Nachname;
					kl.istExtern = (abschnitt != null) && (abschnitt.StammschulNr != null) && (!abschnitt.StammschulNr.equals("" + schule.SchulNr));
					kl.reihenfolge = kurslehrer.Reihenfolge;
					kl.wochenstunden = kurslehrer.Wochenstunden;
					manager.kursAddLehrkraft(kurslehrer.Blockung_Kurs_ID, kl);
				}
			}
		}

		// Regeln hinzufügen.
		final List<DTOGostBlockungRegel> regeln = conn.queryNamed("DTOGostBlockungRegel.blockung_id", blockung.ID, DTOGostBlockungRegel.class);
		if (!regeln.isEmpty()) {
			final List<Long> regelIDs = regeln.stream().map(r -> r.ID).toList();
			final List<DTOGostBlockungRegelParameter> regelParamsDB = conn.queryNamed(
					"DTOGostBlockungRegelParameter.regel_id.multiple", regelIDs, DTOGostBlockungRegelParameter.class);
			final Map<Long, List<DTOGostBlockungRegelParameter>> regelParams = regelParamsDB.stream()
					.collect(Collectors.groupingBy(r -> r.Regel_ID));
			for (final DTOGostBlockungRegel regel : regeln) {
				final GostBlockungRegel eintrag = new GostBlockungRegel();
				eintrag.id = regel.ID;
				eintrag.typ = regel.Typ.typ;
				final List<DTOGostBlockungRegelParameter> p = regelParams.get(eintrag.id);
				if ((p != null) && (!p.isEmpty()))
					eintrag.parameter.addAll(p.stream().sorted((a, b) -> Integer.compare(a.Nummer, b.Nummer))
							.map(r -> r.Parameter).toList());
				manager.regelAdd(eintrag);
			}
		}

		// Schüler-Menge hinzufügen.
		final List<DTOSchueler> schuelerDTOs = (new DataGostJahrgangSchuelerliste(conn, blockung.Abi_Jahrgang)).getSchuelerDTOs();
		final List<Schueler> schuelerListe = new ArrayList<>();
		for (final DTOSchueler dto : schuelerDTOs) {
			if (!checkIstAnSchule(conn, dto, blockung.Halbjahr, blockung.Abi_Jahrgang))
				continue;
			final Schueler daten = new Schueler();
			daten.id = dto.ID;
			daten.nachname = dto.Nachname;
			daten.vorname = dto.Vorname;
			daten.status = dto.Status.id;
			daten.geschlecht = dto.Geschlecht.id;
			schuelerListe.add(daten);
		}
		manager.schuelerAddListe(schuelerListe);

        // Schüler-Fachwahl-Menge hinzufügen.
        final List<GostFachwahl> fachwahlen = (new DataGostAbiturjahrgangFachwahlen(conn, blockung.Abi_Jahrgang)).getSchuelerFachwahlenHalbjahr(blockung.Halbjahr).fachwahlen;
        manager.fachwahlAddListe(fachwahlen);

		return manager;
	}


	@Override
	public Response get(final Long id) {
		GostBlockungsdaten daten;
		try {
			conn.transactionBegin();
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
			// Erstellen den Manager mit den Blockungsdaten
			final GostBlockungsdatenManager manager = getBlockungsdatenManagerFromDB(id);
            daten = manager.daten();
			// Ergänze Blockungsliste
			(new DataGostBlockungsergebnisse(conn)).getErgebnisListe(manager);
			conn.transactionCommit();
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webAppException)
				return webAppException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.exception(e).getResponse();
		} finally {
			// Perform a rollback if necessary
			conn.transactionRollback();
		}
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Markiert die Blockung als aktiv und alle anderen Blockung mit gleichem Abiturjahrgang und Halbjahr
	 * als inaktiv, wenn der Wert auf true gesetzt ist und ansonsten nur die angegebene Blockung auf inaktiv
	 *
	 * @param conn         die Datenbankverbindung
	 * @param idBlockung   die ID der Blockung
	 * @param aktiv        gibt an,
	 *
	 * @return das DTO zur Blockung, falls damit weitergearbeitet werden soll
	 */
	public static DTOGostBlockung markiereBlockungAktiv(final DBEntityManager conn, final long idBlockung, final boolean aktiv) {
		conn.transactionFlush();
		DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
		if (aktiv) {
			conn.transactionNativeUpdate("UPDATE %s SET %s = 0 WHERE %s = %d AND %s = %d".formatted(
				Schema.tab_Gost_Blockung.name(), Schema.tab_Gost_Blockung.col_IstAktiv.name(),
				Schema.tab_Gost_Blockung.col_Abi_Jahrgang.name(), blockung.Abi_Jahrgang,
				Schema.tab_Gost_Blockung.col_Halbjahr.name(), blockung.Halbjahr.id));
			conn.transactionFlush();
			blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
		}
		blockung.IstAktiv = aktiv;
    	if (!conn.transactionPersist(blockung))
        	throw OperationError.INTERNAL_SERVER_ERROR.exception();
		conn.transactionFlush();
		return blockung;
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() <= 0)
			return Response.status(Status.OK).build();
		try {
			conn.transactionBegin();
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
			// Bestimme die Blockung
			DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, id);
			if (blockung == null)
				return OperationError.NOT_FOUND.getResponse();
			for (final Entry<String, Object> entry : map.entrySet()) {
				final String key = entry.getKey();
				final Object value = entry.getValue();
				switch (key) {
					case "id" -> {
						final Long patch_id = JSONMapper.convertToLong(value, true);
						if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "name" -> blockung.Name = JSONMapper.convertToString(value, false, false, Schema.tab_Gost_Blockung.col_Name.datenlaenge());
					case "gostHalbjahr" -> throw OperationError.BAD_REQUEST.exception();
					case "istAktiv" -> {
						final boolean result = JSONMapper.convertToBoolean(value, false);
						if (result)
							blockung = markiereBlockungAktiv(conn, blockung.ID, true);
						else
							blockung.IstAktiv = false;
					}
					// TODO: ggf. Unterstützung für das Setzen von "schienen", "regeln" und "kurse
					default -> throw OperationError.BAD_REQUEST.exception();
				}
			}
			conn.transactionPersist(blockung);
			conn.transactionCommit();
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webAppException)
				return webAppException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			// Perform a rollback if necessary
			conn.transactionRollback();
		}
		return Response.status(Status.OK).build();
	}

	/**
	 * Erstellt eine neue Blockung auf Basis der aktuellen Fachwahlen, dem angegeben Namen der neuen Blockung und der
	 * Anzahl der Schienen mit Vorgabe-Werten.
	 *
	 * @param  abiturjahr   der Abitur-Jahrgang
	 * @param  halbjahr     das Halbjahr der gymnasialen Oberstufe
	 *
	 * @return Eine Response mit der neuen Blockung
	 */
	public Response create(final int abiturjahr, final int halbjahr) {
		try {
			conn.transactionBegin();
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
			// Prüfe die Parameter
			final GostHalbjahr gostHalbjahr = GostHalbjahr.fromID(halbjahr);
			if (gostHalbjahr == null)
				throw OperationError.CONFLICT.exception();
			final int anzahlSchienen = GostBlockungsdatenManager.schieneGetDefaultAnzahl(gostHalbjahr);
			final DTOGostJahrgangsdaten abijahrgang = conn.queryByKey(DTOGostJahrgangsdaten.class, abiturjahr);
			if (abijahrgang == null)
				throw OperationError.CONFLICT.exception();
			// Lese die Fachwahlstatistiken aus der Datenbank - liegen keine vor, so kann auch keine Blockung erstellt
			// werden.
			final DataGostAbiturjahrgangFachwahlen dataFachwahlen = new DataGostAbiturjahrgangFachwahlen(conn, abiturjahr);
			final List<GostStatistikFachwahl> fachwahlen = dataFachwahlen.getFachwahlen();
			if (fachwahlen == null)
				throw OperationError.NOT_FOUND.exception();
			// Bestimme die ID der neuen Blockung
			final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung");
			final Long blockungID = lastID == null ? 1 : lastID.MaxID + 1;
			// Lese zunächst die bestehenden Blockungen mit dem Abiturjahr und dem Halbjahr ein (für weitere Prüfungen)
			final List<DTOGostBlockung> blockungen = conn.queryList("SELECT e FROM DTOGostBlockung e WHERE e.Abi_Jahrgang = ?1 and e.Halbjahr = ?2", DTOGostBlockung.class, abijahrgang.Abi_Jahrgang, gostHalbjahr);
			// Bestimme den Namen der neuen Blockung
			final Set<String> namen = blockungen.stream().map(b -> b.Name).collect(Collectors.toUnmodifiableSet());
			int nameIndex = 1;
			String name = "Blockung ";
			while (namen.contains(name + nameIndex))
				nameIndex++;
			name += nameIndex;
			// Lese die Fächer der gymnasialen Oberstufe ein. Diese müssen für den Abiturjahrgang vorhanden sein, damit eine Blockung angelegt werden darf
			final GostFaecherManager faecherManager = (new DataGostFaecher(conn, abiturjahr)).getListInternal();
			if (faecherManager == null)
				return OperationError.NOT_FOUND.getResponse();
			// Lege ein "leeres" Ergebnis für manuelles Blocken an
			final DTOSchemaAutoInkremente lastErgebnisID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Zwischenergebnisse");
			final long ergebnisID = lastErgebnisID == null ? 1 : lastErgebnisID.MaxID + 1;
			final DTOGostBlockungZwischenergebnis erg = new DTOGostBlockungZwischenergebnis(ergebnisID, blockungID, false);
			// Blockung anlegen
			final DTOGostBlockung blockung = new DTOGostBlockung(blockungID, name, abiturjahr, gostHalbjahr, false);
			conn.transactionPersist(blockung);
			conn.transactionPersist(erg);
			final GostBlockungsdaten daten = dtoMapper.apply(blockung);
			final GostBlockungsdatenManager manager = new GostBlockungsdatenManager(daten, faecherManager);
			// Schienen anlegen
			final DTOSchemaAutoInkremente dbSchienenID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Schienen");
			final long schienenID = dbSchienenID == null ? 0 : dbSchienenID.MaxID;
			for (int i = 1; i <= anzahlSchienen; i++) {
				final DTOGostBlockungSchiene schiene = new DTOGostBlockungSchiene(schienenID + i, blockungID, i,
						"Schiene " + i, 3);
				conn.transactionPersist(schiene);
				manager.schieneAdd(DataGostBlockungSchiene.dtoMapper.apply(schiene));
			}
			// Anhand der Fachwahlstatistik eine Default-Anzahl für die Kursanzahl ermitteln und
			// DTOGostBlockungKurs-Objekte dafür persistieren
			// TODO Verbesserung des Algorithmus -> Optimierung... erstmal nur eine primitive Variante
			final DTOSchemaAutoInkremente dbKurseID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Kurse");
			long kurseID = dbKurseID == null ? 0 : dbKurseID.MaxID;
			for (final GostStatistikFachwahl fw : fachwahlen) {
				final ZulaessigesFach zulFach = ZulaessigesFach.getByKuerzelASD(fw.kuerzelStatistik);
				if (zulFach == ZulaessigesFach.VF)
					throw OperationError.INTERNAL_SERVER_ERROR.exception();
				final GostStatistikFachwahlHalbjahr fwHj = fw.fachwahlen[gostHalbjahr.id];
				final int anzahlLK = (fwHj.wahlenLK + 10) / 20;
				final int anzahlGK = (fwHj.wahlenGK + 10) / 20;
				int anzahlZK = (fwHj.wahlenZK + 10) / 20;
				if ((fwHj.wahlenZK > 0) && (anzahlZK < 1))
					anzahlZK = 1;
				for (int i = 1; i <= anzahlLK; i++) {
					final DTOGostBlockungKurs kurs = new DTOGostBlockungKurs(++kurseID, blockungID, fw.id, GostKursart.LK, i, false, 1, 5);
					conn.transactionPersist(kurs);
					manager.kursAdd(DataGostBlockungKurs.dtoMapper.apply(kurs));
				}
				for (int i = 1; i <= anzahlGK; i++) {
					GostKursart kursart = GostKursart.GK;
					if (zulFach == ZulaessigesFach.VX)
						kursart = GostKursart.VTF;
					if (zulFach == ZulaessigesFach.PX)
						kursart = GostKursart.PJK;
					int wstd = 3;
					if ((kursart == GostKursart.GK) && ((Jahrgaenge.JG_EF == zulFach.getJahrgangAb()) || (Jahrgaenge.JG_11 == zulFach.getJahrgangAb()))) {
						wstd = 4;
					} else if (kursart == GostKursart.PJK) {
						// Wochenstunden des PJK anhand der Tabelle GostFaecher bestimmen
					} else if (kursart == GostKursart.VTF) {
						wstd = 2;
					}
					final DTOGostBlockungKurs kurs = new DTOGostBlockungKurs(++kurseID, blockungID, fw.id, kursart, i, false, 1, wstd);
					conn.transactionPersist(kurs);
					manager.kursAdd(DataGostBlockungKurs.dtoMapper.apply(kurs));
				}
				for (int i = 1; i <= anzahlZK; i++) {
					final DTOGostBlockungKurs kurs = new DTOGostBlockungKurs(++kurseID, blockungID, fw.id, GostKursart.ZK, i, false, 1, 3);
					conn.transactionPersist(kurs);
					manager.kursAdd(DataGostBlockungKurs.dtoMapper.apply(kurs));
				}
			}
			// Lege eine Kurs-Schienen-Zuordnung für das "leere" Ergebnis fest. Diese Kurse werden der ersten Schiene der neuen Blockung zugeordnet.
			for (final GostBlockungKurs kurs : manager.daten().kurse)
				conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchiene(ergebnisID, kurs.id, schienenID + 1));
            // Bestimme die Fachwahlen aus der DB
            daten.fachwahlen.addAll((new DataGostAbiturjahrgangFachwahlen(conn, daten.abijahrgang)).getSchuelerFachwahlenHalbjahr(gostHalbjahr).fachwahlen);
            // Ergänze Blockungsliste
            conn.transactionCommit();
            conn.transactionBegin();
            (new DataGostBlockungsergebnisse(conn)).getErgebnisListe(manager);
			conn.transactionCommit();
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (final Exception exception) {
			conn.transactionRollback();
			if (exception instanceof final WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}

	/**
	 * Entfernt die Blockung mit der angegebenen ID aus der Datenbank.
	 *
	 * @param id   die ID der zu löschenden Blockung
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
		// TODO use transaction
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme die Blockung
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, id);
		if (blockung == null)
			return OperationError.NOT_FOUND.getResponse();
		// Entferne die Blockung
		conn.remove(blockung);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(id).build();
	}


	private static synchronized List<Long> schreibeErgebnisse(final DBEntityManager conn, final long id, final List<GostBlockungsergebnisManager> outputs) {
		try {
			final ArrayList<Long> ergebnisse = new ArrayList<>();
			conn.transactionBegin();
			final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Zwischenergebnisse");
			long ergebnisID = lastID == null ? 1 : lastID.MaxID + 1;
			for (final GostBlockungsergebnisManager output : outputs) {
				// Schreibe das Ergebnis in die Datenbank.
				final DTOGostBlockungZwischenergebnis erg = new DTOGostBlockungZwischenergebnis(ergebnisID, id, false);
				conn.transactionPersist(erg);

				// Kurse <--> Schüler
				final Map<Long, Set<@NotNull Long>> map_KursID_SchuelerIDs = output.getMappingKursIDSchuelerIDs();
				for (final Map.Entry<Long, Set<Long>> entry : map_KursID_SchuelerIDs.entrySet())
					for (final long schuelerID : entry.getValue())
						conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchueler(ergebnisID, entry.getKey(), schuelerID));

				// Kurse <--> Schienen
				final Map<Long, Set<@NotNull GostBlockungsergebnisSchiene>> map_KursID_SchienenIDs = output.getMappingKursIDSchienenmenge();
				for (final Map.Entry<Long, Set<GostBlockungsergebnisSchiene>> entry : map_KursID_SchienenIDs.entrySet())
					for (@NotNull final GostBlockungsergebnisSchiene schiene : entry.getValue())
						conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchiene(ergebnisID, entry.getKey(), schiene.id));

				// Ergänze die ID bei der Liste der berechneten Ergebnisse
				ergebnisse.add(ergebnisID);
				ergebnisID++;
			}
			if (conn.transactionCommit())
				return ergebnisse;
			throw OperationError.INTERNAL_SERVER_ERROR.exception("\"Fehler beim Schreiben der Zwischenergebnisse für die Kursblockung.\"");
		} catch (final Exception exception) {
			conn.transactionRollback();
			throw exception;
		}
	}

	/**
	 * Berechnet für die übergebene Blockung Zwischenergebnis, wobei der Algorithmus nur maximal eine spezifizierte
	 * Zeit läuft.
	 *
	 * @param  id     die ID der Blockung
	 * @param  zeit   die maximale Zeit in ms
	 *
	 * @return die HTTP-Response mit einer Liste von IDs der Zwischenergebnisse
	 */
	public Response berechne(final long id, final long zeit) {
		// Erzeuge den Input für den Kursblockungsalgorithmus
		final GostBlockungsdatenManager manager = getBlockungsdatenManagerFromDB(id);
		if (manager.daten().fachwahlen.isEmpty())
			return OperationError.NOT_FOUND.getResponse("Keine Fachwahlen für den Abiturjahrgang gefunden.");
		if (manager.faecherManager().faecher().isEmpty())
			return OperationError.NOT_FOUND.getResponse("Keine Fächer für den Abiturjahrgang gefunden.");
		if (manager.daten().kurse.isEmpty())
			return OperationError.NOT_FOUND.getResponse("Es sind keine Kurse für die Blockung angelegt.");

		manager.setMaxTimeMillis(zeit);

		final KursblockungAlgorithmus algo = new KursblockungAlgorithmus();

		ArrayList<GostBlockungsergebnisManager> outputs;

		try {
			outputs = algo.handle(manager);
		} catch (final Exception exception) {
			return OperationError.INTERNAL_SERVER_ERROR.getResponse(exception);
		}

		try {
			final List<Long> ergebnisse = schreibeErgebnisse(conn, id, outputs);
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(ergebnisse).build();
		} catch (final Exception exception) {
			if (exception instanceof final WebApplicationException webex)
				return webex.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse(exception);
		}
	}


	/**
	 * Erzeugt ein Duplikat der Blockung des angegebenen Ergebnis.
	 * Dabei wird auch das Ergebnis dupliziert.
	 *
	 * @param idErgebnisOriginal   das zu duplizierende Blockungsergebnis
	 *
	 * @return die Blockungsdaten der duplizierten Blockung
	 */
	public Response dupliziere(final long idErgebnisOriginal) {
		try {
			conn.transactionBegin();
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
			// Bestimme die Blockung und das zugehörige Ergebnis
			DTOGostBlockung blockungOriginal;
			final DTOGostBlockungZwischenergebnis ergebnisOriginal = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idErgebnisOriginal);
			if (ergebnisOriginal == null)
				throw OperationError.NOT_FOUND.exception();
			// Bestimme die Blockung
			blockungOriginal = conn.queryByKey(DTOGostBlockung.class, ergebnisOriginal.Blockung_ID);
			if (blockungOriginal == null)
				throw OperationError.NOT_FOUND.exception();
			// Bestimme die ID für das Duplikat der Blockung
			final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung");
			final Long idBlockungDuplikat = lastID == null ? 1 : lastID.MaxID + 1;
			// Bestimme die ID für das Vorlage-Ergebnis der duplizierten Blockung
			final DTOSchemaAutoInkremente dbErgebnisID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Zwischenergebnisse");
			final long idErgebnisDuplikat = dbErgebnisID == null ? 1 : dbErgebnisID.MaxID + 1;
			// Bestimme den Namen der neuen Blockung
			final List<DTOGostBlockung> blockungen = conn.queryList("SELECT e FROM DTOGostBlockung e WHERE e.Abi_Jahrgang = ?1 and e.Halbjahr = ?2", DTOGostBlockung.class, blockungOriginal.Abi_Jahrgang, blockungOriginal.Halbjahr);
			final Set<String> namen = blockungen.stream().map(b -> b.Name).collect(Collectors.toUnmodifiableSet());
			final String trimmedName = blockungOriginal.Name.replaceAll("\\d+$", "").stripTrailing();
			int nameIndex = 1;
			while (namen.contains(trimmedName + " " + nameIndex))
				nameIndex++;
			final String name = trimmedName + " " + nameIndex;
			// Erstelle das Duplikat
			final DTOGostBlockung blockungDuplikat = new DTOGostBlockung(idBlockungDuplikat, name, blockungOriginal.Abi_Jahrgang, blockungOriginal.Halbjahr, false);
			conn.transactionPersist(blockungDuplikat);
			// Dupliziere die Schienen
			final DTOSchemaAutoInkremente dbSchienenID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Schienen");
			long idSchieneDuplikat = dbSchienenID == null ? 0 : dbSchienenID.MaxID + 1;
			final HashMap<Long, Long> mapSchienenIDs = new HashMap<>();
			final List<DTOGostBlockungSchiene> schienenOriginal = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", ergebnisOriginal.Blockung_ID,
					DTOGostBlockungSchiene.class);
			for (final DTOGostBlockungSchiene schieneOriginal : schienenOriginal) {
				final DTOGostBlockungSchiene schieneDuplikat = new DTOGostBlockungSchiene(idSchieneDuplikat, idBlockungDuplikat, schieneOriginal.Nummer, schieneOriginal.Bezeichnung, schieneOriginal.Wochenstunden);
				mapSchienenIDs.put(schieneOriginal.ID, schieneDuplikat.ID);
				conn.transactionPersist(schieneDuplikat);
				idSchieneDuplikat++;
			}
			// Dupliziere die Kurse
			final DTOSchemaAutoInkremente dbKurseID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Kurse");
			long idKursDuplikat = dbKurseID == null ? 0 : dbKurseID.MaxID + 1;
			final HashMap<Long, Long> mapKursIDs = new HashMap<>();
			final List<DTOGostBlockungKurs> kurseOriginal = conn.queryNamed("DTOGostBlockungKurs.blockung_id", ergebnisOriginal.Blockung_ID,
					DTOGostBlockungKurs.class);
			final List<Long> kursIDsOriginal = kurseOriginal.stream().map(k -> k.ID).toList();
			for (final DTOGostBlockungKurs kursOriginal : kurseOriginal) {
				final DTOGostBlockungKurs kursDuplikat = new DTOGostBlockungKurs(idKursDuplikat, idBlockungDuplikat, kursOriginal.Fach_ID,
						kursOriginal.Kursart, kursOriginal.Kursnummer, kursOriginal.IstKoopKurs, kursOriginal.Schienenanzahl, kursOriginal.Wochenstunden);
				kursDuplikat.BezeichnungSuffix = kursOriginal.BezeichnungSuffix;
				mapKursIDs.put(kursOriginal.ID, kursDuplikat.ID);
				conn.transactionPersist(kursDuplikat);
				idKursDuplikat++;
			}
			// Dupliziere die KursLehrer
			if (!kursIDsOriginal.isEmpty()) {
				final List<DTOGostBlockungKurslehrer> kurslehrerListeOriginal = conn.queryNamed("DTOGostBlockungKurslehrer.blockung_kurs_id.multiple", kursIDsOriginal, DTOGostBlockungKurslehrer.class);
				for (final DTOGostBlockungKurslehrer kurslehrerOriginal : kurslehrerListeOriginal) {
					idKursDuplikat = mapKursIDs.get(kurslehrerOriginal.Blockung_Kurs_ID);
					final DTOGostBlockungKurslehrer kurslehrerDuplikat = new DTOGostBlockungKurslehrer(idKursDuplikat,
							kurslehrerOriginal.Lehrer_ID, kurslehrerOriginal.Reihenfolge, kurslehrerOriginal.Wochenstunden);
					conn.transactionPersist(kurslehrerDuplikat);
				}
			}
			// Dupliziere die Regeln
			final DTOSchemaAutoInkremente dbRegelID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Regeln");
			long idRegelDuplikat = dbRegelID == null ? 1 : dbRegelID.MaxID + 1;
			final HashMap<Long, Long> mapRegelIDs = new HashMap<>();
			final HashMap<Long, GostKursblockungRegelTyp> mapRegelTypen = new HashMap<>(); // Die Typen für die neuen Regel-IDs
			final List<DTOGostBlockungRegel> regelnOriginal = conn.queryNamed("DTOGostBlockungRegel.blockung_id", ergebnisOriginal.Blockung_ID,
					DTOGostBlockungRegel.class);
			final List<Long> regelIDsOriginal = regelnOriginal.stream().map(k -> k.ID).toList();
			for (final DTOGostBlockungRegel regelOriginal : regelnOriginal) {
			    mapRegelTypen.put(idRegelDuplikat, regelOriginal.Typ);
				final DTOGostBlockungRegel regelDuplikat = new DTOGostBlockungRegel(idRegelDuplikat, idBlockungDuplikat, regelOriginal.Typ);
				mapRegelIDs.put(regelOriginal.ID, regelDuplikat.ID);
				conn.transactionPersist(regelDuplikat);
				idRegelDuplikat++;
			}
			// Dupliziere die RegelParameter
			if (!regelIDsOriginal.isEmpty()) {
				final List<DTOGostBlockungRegelParameter> paramListeOriginal = conn.queryNamed("DTOGostBlockungRegelParameter.regel_id.multiple", regelIDsOriginal, DTOGostBlockungRegelParameter.class);
				for (final DTOGostBlockungRegelParameter paramOriginal : paramListeOriginal) {
					idRegelDuplikat = mapRegelIDs.get(paramOriginal.Regel_ID);
					// Passe den Parameter an...
					final GostKursblockungRegelTyp typ = mapRegelTypen.get(idRegelDuplikat);
					final GostKursblockungRegelParameterTyp paramTyp = typ.getParamType(paramOriginal.Nummer);
					final Long paramValue = switch (paramTyp) {
                        case KURSART -> paramOriginal.Parameter;
                        case KURS_ID -> mapKursIDs.get(paramOriginal.Parameter);
                        case SCHIENEN_NR -> paramOriginal.Parameter;
                        case SCHUELER_ID -> paramOriginal.Parameter;
                        default -> paramOriginal.Parameter;
					};
					final DTOGostBlockungRegelParameter paramDuplikat = new DTOGostBlockungRegelParameter(idRegelDuplikat,
							paramOriginal.Nummer, paramValue);
					conn.transactionPersist(paramDuplikat);
				}
			}
			// Dupliziere das Zwischenergebnis und markiere es als Duplikat
			final DTOGostBlockungZwischenergebnis ergebnisDuplikat = new DTOGostBlockungZwischenergebnis(
					idErgebnisDuplikat, idBlockungDuplikat, false);
			conn.transactionPersist(ergebnisDuplikat);
			// Dupliziere Kurs-Schienen-Zuordnung
			final List<DTOGostBlockungZwischenergebnisKursSchiene> zuordnungKursSchieneListeOriginal = conn.queryNamed("DTOGostBlockungZwischenergebnisKursSchiene.zwischenergebnis_id", idErgebnisOriginal,
					DTOGostBlockungZwischenergebnisKursSchiene.class);
			for (final DTOGostBlockungZwischenergebnisKursSchiene zuordnungKursSchieneOriginal : zuordnungKursSchieneListeOriginal) {
				idKursDuplikat = mapKursIDs.get(zuordnungKursSchieneOriginal.Blockung_Kurs_ID);
				idSchieneDuplikat = mapSchienenIDs.get(zuordnungKursSchieneOriginal.Schienen_ID);
				final DTOGostBlockungZwischenergebnisKursSchiene zuordnungKursSchieneDuplikat = new DTOGostBlockungZwischenergebnisKursSchiene(
						idErgebnisDuplikat, idKursDuplikat, idSchieneDuplikat);
				conn.transactionPersist(zuordnungKursSchieneDuplikat);
			}
			// Dupliziere Kurs-Schüler-Zuordnung
			final List<DTOGostBlockungZwischenergebnisKursSchueler> zuordnungKursSchuelerListeOriginal = conn.queryNamed("DTOGostBlockungZwischenergebnisKursSchueler.zwischenergebnis_id", idErgebnisOriginal,
					DTOGostBlockungZwischenergebnisKursSchueler.class);
			for (final DTOGostBlockungZwischenergebnisKursSchueler zuordnungKursSchuelerOriginal : zuordnungKursSchuelerListeOriginal) {
				idKursDuplikat = mapKursIDs.get(zuordnungKursSchuelerOriginal.Blockung_Kurs_ID);
				final DTOGostBlockungZwischenergebnisKursSchueler zuordnungKursSchuelerDuplikat = new DTOGostBlockungZwischenergebnisKursSchueler(
						idErgebnisDuplikat, idKursDuplikat, zuordnungKursSchuelerOriginal.Schueler_ID);
				conn.transactionPersist(zuordnungKursSchuelerDuplikat);
			}
			conn.transactionCommit();
			return get(idBlockungDuplikat);
		} catch (final Exception exception) {
			conn.transactionRollback();
			if (exception instanceof IllegalArgumentException)
				throw OperationError.NOT_FOUND.exception();
			if (exception instanceof final WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}



	/**
	 * Erzeugt ein Duplikat der Blockung des angegebenen Ergebnis und des Ergebnisses
	 * selber und schreibt dieses direkt in das nächste Halbjahr hoch. Wird diese
	 * Methode auf eine Blockung der Q2.2 ausgeführt, so wird eine Fehlermeldung erzeugt
	 *
	 * @param idErgebnisOriginal   das hochzuschreibende Blockungsergebnis
	 *
	 * @return die Blockungsdaten der hochgeschriebenen Blockung
	 */
	public Response hochschreiben(final long idErgebnisOriginal) {
		try {
			conn.transactionBegin();
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
			// Bestimme die Blockung und das zugehörige Ergebnis
			DTOGostBlockung blockungOriginal;
			final DTOGostBlockungZwischenergebnis ergebnisOriginal = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idErgebnisOriginal);
			if (ergebnisOriginal == null)
				throw OperationError.NOT_FOUND.exception();
			// Bestimme die Blockung
			blockungOriginal = conn.queryByKey(DTOGostBlockung.class, ergebnisOriginal.Blockung_ID);
			if (blockungOriginal == null)
				throw OperationError.NOT_FOUND.exception();
			if (blockungOriginal.Halbjahr == GostHalbjahr.Q22)   // Blockungen der Q2.2 können nicht hochgeschrieben werden...
				throw OperationError.BAD_REQUEST.exception();
			// Bestimme die ID für die hochgeschriebene Blockung
			final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung");
			final Long idBlockungDuplikat = lastID == null ? 1 : lastID.MaxID + 1;
			// Bestimme die ID für das Vorlage-Ergebnis der hochgeschriebenen Blockung
			final DTOSchemaAutoInkremente dbErgebnisID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Zwischenergebnisse");
			final long idErgebnisDuplikat = dbErgebnisID == null ? 1 : dbErgebnisID.MaxID + 1;
			// Bestimme den Namen der neuen Blockung
			final String name = blockungOriginal.Name + " - hochgeschrieben von Ergebnis " + idErgebnisOriginal + ")";
			// Erstelle die Hochgeschriebene Blockung
			final DTOGostBlockung blockungDuplikat = new DTOGostBlockung(idBlockungDuplikat, name, blockungOriginal.Abi_Jahrgang, blockungOriginal.Halbjahr.next(), false);
			conn.transactionPersist(blockungDuplikat);
			// Dupliziere die Schienen
			final DTOSchemaAutoInkremente dbSchienenID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Schienen");
			long idSchieneDuplikat = dbSchienenID == null ? 0 : dbSchienenID.MaxID + 1;
			final HashMap<Long, Long> mapSchienenIDs = new HashMap<>();
			final List<DTOGostBlockungSchiene> schienenOriginal = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", ergebnisOriginal.Blockung_ID,
					DTOGostBlockungSchiene.class);
			for (final DTOGostBlockungSchiene schieneOriginal : schienenOriginal) {
				final DTOGostBlockungSchiene schieneDuplikat = new DTOGostBlockungSchiene(idSchieneDuplikat, idBlockungDuplikat, schieneOriginal.Nummer, schieneOriginal.Bezeichnung, schieneOriginal.Wochenstunden);
				mapSchienenIDs.put(schieneOriginal.ID, schieneDuplikat.ID);
				conn.transactionPersist(schieneDuplikat);
				idSchieneDuplikat++;
			}
			// Dupliziere die Kurse
			final DTOSchemaAutoInkremente dbKurseID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Kurse");
			long idKursDuplikat = dbKurseID == null ? 0 : dbKurseID.MaxID + 1;
			final HashMap<Long, Long> mapKursIDs = new HashMap<>();
			final HashMap<Long, DTOGostBlockungKurs> mapKurseHochgeschrieben = new HashMap<>();
			final List<DTOGostBlockungKurs> kurseOriginal = conn.queryNamed("DTOGostBlockungKurs.blockung_id", ergebnisOriginal.Blockung_ID,
					DTOGostBlockungKurs.class);
			final List<Long> kursIDsOriginal = kurseOriginal.stream().map(k -> k.ID).toList();
			for (final DTOGostBlockungKurs kursOriginal : kurseOriginal) {
				final DTOGostBlockungKurs kursDuplikat = new DTOGostBlockungKurs(idKursDuplikat, idBlockungDuplikat, kursOriginal.Fach_ID,
						kursOriginal.Kursart, kursOriginal.Kursnummer, kursOriginal.IstKoopKurs, kursOriginal.Schienenanzahl, kursOriginal.Wochenstunden);
				kursDuplikat.BezeichnungSuffix = kursOriginal.BezeichnungSuffix;
				mapKursIDs.put(kursOriginal.ID, kursDuplikat.ID);
				mapKurseHochgeschrieben.put(kursDuplikat.ID, kursDuplikat);
				conn.transactionPersist(kursDuplikat);
				idKursDuplikat++;
			}
			// Dupliziere die KursLehrer
			if (!kursIDsOriginal.isEmpty()) {
				final List<DTOGostBlockungKurslehrer> kurslehrerListeOriginal = conn.queryNamed("DTOGostBlockungKurslehrer.blockung_kurs_id.multiple", kursIDsOriginal, DTOGostBlockungKurslehrer.class);
				for (final DTOGostBlockungKurslehrer kurslehrerOriginal : kurslehrerListeOriginal) {
					idKursDuplikat = mapKursIDs.get(kurslehrerOriginal.Blockung_Kurs_ID);
					final DTOGostBlockungKurslehrer kurslehrerDuplikat = new DTOGostBlockungKurslehrer(idKursDuplikat,
							kurslehrerOriginal.Lehrer_ID, kurslehrerOriginal.Reihenfolge, kurslehrerOriginal.Wochenstunden);
					conn.transactionPersist(kurslehrerDuplikat);
				}
			}
			// Dupliziere die Regeln
			final DTOSchemaAutoInkremente dbRegelID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Regeln");
			long idRegelDuplikat = dbRegelID == null ? 1 : dbRegelID.MaxID + 1;
			final HashMap<Long, Long> mapRegelIDs = new HashMap<>();
			final HashMap<Long, GostKursblockungRegelTyp> mapRegelTypen = new HashMap<>(); // Die Typen für die neuen Regel-IDs
			final List<DTOGostBlockungRegel> regelnOriginal = conn.queryNamed("DTOGostBlockungRegel.blockung_id", ergebnisOriginal.Blockung_ID,
					DTOGostBlockungRegel.class);
			final List<Long> regelIDsOriginal = regelnOriginal.stream().map(k -> k.ID).toList();
			for (final DTOGostBlockungRegel regelOriginal : regelnOriginal) {
			    mapRegelTypen.put(idRegelDuplikat, regelOriginal.Typ);
				final DTOGostBlockungRegel regelDuplikat = new DTOGostBlockungRegel(idRegelDuplikat, idBlockungDuplikat, regelOriginal.Typ);
				mapRegelIDs.put(regelOriginal.ID, regelDuplikat.ID);
				conn.transactionPersist(regelDuplikat);
				idRegelDuplikat++;
			}
			// Dupliziere die RegelParameter
			if (!regelIDsOriginal.isEmpty()) {
				final List<DTOGostBlockungRegelParameter> paramListeOriginal = conn.queryNamed("DTOGostBlockungRegelParameter.regel_id.multiple", regelIDsOriginal, DTOGostBlockungRegelParameter.class);
				for (final DTOGostBlockungRegelParameter paramOriginal : paramListeOriginal) {
					idRegelDuplikat = mapRegelIDs.get(paramOriginal.Regel_ID);
					// Passe den Parameter an...
					final GostKursblockungRegelTyp typ = mapRegelTypen.get(idRegelDuplikat);
					final GostKursblockungRegelParameterTyp paramTyp = typ.getParamType(paramOriginal.Nummer);
					final Long paramValue = switch (paramTyp) {
                        case KURSART -> paramOriginal.Parameter;
                        case KURS_ID -> mapKursIDs.get(paramOriginal.Parameter);
                        case SCHIENEN_NR -> paramOriginal.Parameter;
                        case SCHUELER_ID -> paramOriginal.Parameter;
                        default -> paramOriginal.Parameter;
					};
					final DTOGostBlockungRegelParameter paramDuplikat = new DTOGostBlockungRegelParameter(idRegelDuplikat,
							paramOriginal.Nummer, paramValue);
					conn.transactionPersist(paramDuplikat);
				}
			}
			// Dupliziere das Zwischenergebnis
			final DTOGostBlockungZwischenergebnis ergebnisDuplikat = new DTOGostBlockungZwischenergebnis(
					idErgebnisDuplikat, idBlockungDuplikat, false);
			conn.transactionPersist(ergebnisDuplikat);
			// Dupliziere Kurs-Schienen-Zuordnung
			final List<DTOGostBlockungZwischenergebnisKursSchiene> zuordnungKursSchieneListeOriginal = conn.queryNamed("DTOGostBlockungZwischenergebnisKursSchiene.zwischenergebnis_id", idErgebnisOriginal,
					DTOGostBlockungZwischenergebnisKursSchiene.class);
			for (final DTOGostBlockungZwischenergebnisKursSchiene zuordnungKursSchieneOriginal : zuordnungKursSchieneListeOriginal) {
				idKursDuplikat = mapKursIDs.get(zuordnungKursSchieneOriginal.Blockung_Kurs_ID);
				idSchieneDuplikat = mapSchienenIDs.get(zuordnungKursSchieneOriginal.Schienen_ID);
				final DTOGostBlockungZwischenergebnisKursSchiene zuordnungKursSchieneDuplikat = new DTOGostBlockungZwischenergebnisKursSchiene(
						idErgebnisDuplikat, idKursDuplikat, idSchieneDuplikat);
				conn.transactionPersist(zuordnungKursSchieneDuplikat);
			}
			// Ermittle die Fachwahlen des Abiturjahrgangs
			final GostFachwahlManager managerFachwahlen = (new DataGostAbiturjahrgangFachwahlen(conn, blockungDuplikat.Abi_Jahrgang)).getFachwahlManager(blockungDuplikat.Halbjahr);
			// Dupliziere Kurs-Schüler-Zuordnung
			final List<DTOGostBlockungZwischenergebnisKursSchueler> zuordnungKursSchuelerListeOriginal = conn.queryNamed("DTOGostBlockungZwischenergebnisKursSchueler.zwischenergebnis_id", idErgebnisOriginal,
					DTOGostBlockungZwischenergebnisKursSchueler.class);
			for (final DTOGostBlockungZwischenergebnisKursSchueler zuordnungKursSchuelerOriginal : zuordnungKursSchuelerListeOriginal) {
				idKursDuplikat = mapKursIDs.get(zuordnungKursSchuelerOriginal.Blockung_Kurs_ID);
				final DTOGostBlockungKurs kurs = mapKurseHochgeschrieben.get(idKursDuplikat);
				// Prüfe Fachwahlen
				if (managerFachwahlen.hatFachwahl(zuordnungKursSchuelerOriginal.Schueler_ID, kurs.Fach_ID, kurs.Kursart)) {
					final DTOGostBlockungZwischenergebnisKursSchueler zuordnungKursSchuelerDuplikat = new DTOGostBlockungZwischenergebnisKursSchueler(
						idErgebnisDuplikat, idKursDuplikat, zuordnungKursSchuelerOriginal.Schueler_ID);
					conn.transactionPersist(zuordnungKursSchuelerDuplikat);
				}
			}
			conn.transactionCommit();
			return get(idBlockungDuplikat);
		} catch (final Exception exception) {
			conn.transactionRollback();
			if (exception instanceof IllegalArgumentException)
				throw OperationError.NOT_FOUND.exception();
			if (exception instanceof final WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}


	/**
	 * Versucht eine Blockung aus den Kursen und den Leistungsdaten wiederherzustellen,
	 * wenn für den Abiturjahrgang in dem angegebenen Halbjahr bereits eine Blockung
	 * aktiviert wurde.
	 *
	 * @param abiturjahr   das Abiturjahr
	 * @param halbjahrID   die ID des Halbjahres der gymnasialen Oberstufe (siehe auch {@link GostHalbjahr})
	 *
	 * @return die Blockungsdaten der restaurierten Blockung
	 */
	public Response restore(final int abiturjahr, final int halbjahrID) {
		try {
			conn.transactionBegin();
			DBUtilsGost.pruefeSchuleMitGOSt(conn);

			// Bestimme das Halbjahr der gymnasialen Oberstufe und das Schuljahr, wo der Abiturjahrgang in diesem Halbjahr war
			final GostHalbjahr halbjahr = GostHalbjahr.fromID(halbjahrID);
			if (halbjahr == null)
				throw OperationError.BAD_REQUEST.exception();
			final int schuljahr = halbjahr.getSchuljahrFromAbiturjahr(abiturjahr);

			// Bestimme den zugehörigen Schuljahresabschnitt
			final List<DTOSchuljahresabschnitte> listSchuljahresabschnitte = conn
					.queryList("SELECT e FROM DTOSchuljahresabschnitte e WHERE e.Jahr = ?1 AND e.Abschnitt = ?2",
							DTOSchuljahresabschnitte.class, schuljahr, halbjahr.halbjahr);
			if (listSchuljahresabschnitte.size() != 1)
				throw OperationError.NOT_FOUND.exception();
			final DTOSchuljahresabschnitte schuljahresabschnitt = listSchuljahresabschnitte.get(0);

			// Bestimme die ID des Jahrgangs
			final List<DTOJahrgang> listJahrgaenge = conn.queryList("SELECT e FROM DTOJahrgang e WHERE e.ASDJahrgang = ?1",
							DTOJahrgang.class, halbjahr.jahrgang);
			if (listJahrgaenge.isEmpty())
				throw OperationError.NOT_FOUND.exception();
			final List<Long> jahrgangIDs = listJahrgaenge.stream().map(j -> j.ID).toList();

			// Lese die Kurse für den Schuljahresabschnitt und den zugehörigen Jahrgang ein
			final List<DTOKurs> listKurse = conn.queryList("SELECT e FROM DTOKurs e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.Jahrgang_ID IN ?2",
							DTOKurs.class, schuljahresabschnitt.ID, jahrgangIDs);
			if (listKurse.isEmpty())
				throw OperationError.NOT_FOUND.exception();
			// Bestimme die Fächer und die Schienen aus der Kurstabelle
			final HashMap<Long, DTOKurs> mapKurse = new HashMap<>();
			final HashSet<Integer> setSchienen = new HashSet<>();
			final Set<Long> setFachIDs = new HashSet<>();
			for (final DTOKurs kurs : listKurse) {
				final GostKursart kursart = GostKursart.fromKuerzel(kurs.KursartAllg);
				if (kursart == null)
					continue;
				setFachIDs.add(kurs.Fach_ID);
				mapKurse.put(kurs.ID, kurs);
				String alleSchienen = kurs.Schienen;
				if (alleSchienen == null)
					alleSchienen = "1";
				for (final String strSchiene : alleSchienen.split(",")) {
					if ("".equals(strSchiene.trim()))
						continue;
					try {
						setSchienen.add(Integer.parseInt(strSchiene.trim()));
					} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
						// ignore exception
					}
				}
			}

			// TODO weitere Kurslehrer ermitteln

			// Lese die Leistungsdaten zu den Kursen ein
			final List<DTOSchuelerLeistungsdaten> listLeistungsdaten = conn.queryNamed(
					"DTOSchuelerLeistungsdaten.kurs_id.multiple", mapKurse.keySet(), DTOSchuelerLeistungsdaten.class);
			final List<Long> listLernabschnittIDs = listLeistungsdaten.stream().map(ld -> ld.Abschnitt_ID).toList();
			final Map<Long, DTOSchuelerLernabschnittsdaten> mapLernabschnitte = listLernabschnittIDs.isEmpty()
					? new HashMap<>()
					: conn.queryNamed("DTOSchuelerLernabschnittsdaten.id.multiple", listLernabschnittIDs, DTOSchuelerLernabschnittsdaten.class)
						.stream().collect(Collectors.toMap(lad -> lad.ID, lad -> lad));

			// Prüfe, ob jeweils der Schüler des Lernabschnittes ein Entlassdatum eingetragen hat, welches vor dem Lernabschnitt liegt - inkonsistente Daten?!
			final List<Long> listSchuelerIDs = listLernabschnittIDs.stream().map(mapLernabschnitte::get).filter(la -> la != null).map(la -> la.Schueler_ID).toList();
			final Map<Long, DTOSchueler> mapSchueler = conn.queryByKeyList(DTOSchueler.class, listSchuelerIDs).stream().collect(Collectors.toMap(s -> s.ID, s -> s));
			for (final long laid : listLernabschnittIDs) {
				final DTOSchuelerLernabschnittsdaten la = mapLernabschnitte.get(laid);
				if (la == null) {
					mapLernabschnitte.remove(laid);
				} else {
					final DTOSchueler dtoSchueler = mapSchueler.get(la.Schueler_ID);
					if ((dtoSchueler == null) || (!checkIstAnSchule(conn, dtoSchueler, halbjahr, abiturjahr)))
						mapLernabschnitte.remove(laid);
				}
			}

			// Bestimme die ID für die hochgeschriebene Blockung
			final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung");
			final Long idBlockung = lastID == null ? 1 : lastID.MaxID + 1;
			// Bestimme die ID für das Vorlage-Ergebnis der hochgeschriebenen Blockung
			final DTOSchemaAutoInkremente lastErgebnisID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Zwischenergebnisse");
			final long idErgebnis = lastErgebnisID == null ? 1 : lastErgebnisID.MaxID + 1;

			// Erstelle die Blockung
			final DTOGostBlockung blockung = new DTOGostBlockung(idBlockung, "Restaurierte Blockung", abiturjahr, halbjahr, false);
			conn.transactionPersist(blockung);

			// Erstelle das Zwischenergebnis
			final DTOGostBlockungZwischenergebnis ergebnis = new DTOGostBlockungZwischenergebnis(idErgebnis, idBlockung, false);
			conn.transactionPersist(ergebnis);

			// Fehlervermeidung: Prüfe, ob alle Fächer auch in der Liste der Fächer der Oberstufe für diesen Jahrgang vorhanden sind
			if (!setFachIDs.isEmpty()) {
				final List<DTOGostJahrgangFaecher> faecher = conn.queryList("SELECT e FROM DTOGostJahrgangFaecher e WHERE e.Abi_Jahrgang = ?1 AND e.Fach_ID IN ?2",
						DTOGostJahrgangFaecher.class, abiturjahr, setFachIDs);
				setFachIDs.removeAll(faecher.stream().map(f -> f.Fach_ID).toList());
				if (!setFachIDs.isEmpty()) {
					// wenn nicht, dann füge sie hinzu...
					final List<DTOFach> faecherAdd = conn.queryNamed("DTOFach.id.multiple", setFachIDs, DTOFach.class);
					for (final DTOFach dtoFach : faecherAdd) {
						final DTOGostJahrgangFaecher dtoJgFach = new DTOGostJahrgangFaecher(abiturjahr, dtoFach.ID,
								dtoFach.IstMoeglichEF1, dtoFach.IstMoeglichEF2, dtoFach.IstMoeglichQ11,
								dtoFach.IstMoeglichQ12, dtoFach.IstMoeglichQ21, dtoFach.IstMoeglichQ22,
								dtoFach.IstMoeglichAbiGK, dtoFach.IstMoeglichAbiLK);
						conn.transactionPersist(dtoJgFach);
					}
				}
			}

			// Erstelle die Schienen
			final DTOSchemaAutoInkremente lastSchienenID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Schienen");
			long idSchiene = lastSchienenID == null ? 0 : lastSchienenID.MaxID + 1;
			final HashMap<Integer, Long> mapSchienen = new HashMap<>();
			for (final Integer schienenNr : setSchienen) {
				final DTOGostBlockungSchiene schiene = new DTOGostBlockungSchiene(idSchiene, idBlockung, schienenNr, "Schiene " + schienenNr, 3);
				mapSchienen.put(schienenNr, idSchiene);
				conn.transactionPersist(schiene);
				idSchiene++;
			}

			// Erstelle die Kurse
			final DTOSchemaAutoInkremente lastKurseID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Kurse");
			long idKurs = lastKurseID == null ? 0 : lastKurseID.MaxID + 1;
			final HashMap<Long, Long> mapKursIDs = new HashMap<>(); // Von der Originals-Kurs-ID auf die Blockungs-Kurs-ID
			final HashMap<Long, DTOGostBlockungKurs> mapKurseErstellt = new HashMap<>();
			for (final DTOKurs kurs : listKurse) {
				final GostKursart kursart = GostKursart.fromKuerzel(kurs.KursartAllg);
				if (kursart == null)
					continue;
				mapKursIDs.put(kurs.ID, idKurs);
				final String[] strKursnummer = kurs.KurzBez.split("\\D+");
				int kursNummer = 1;
				if (strKursnummer.length > 0) {
					final String tmpNummer = strKursnummer[strKursnummer.length - 1];
					if (!"".equals(tmpNummer))
						kursNummer = Integer.parseInt(tmpNummer);
				}
				final ArrayList<Long> schienen = new ArrayList<>();
				if (kurs.Schienen != null) {
					final String[] strSchienen = kurs.Schienen.split(",");
					for (final String strSchiene : strSchienen) {
						if ("".equals(strSchiene.trim()))
							continue;
						try {
							final Long schienenID = mapSchienen.get(Integer.parseInt(strSchiene.trim()));
							if (schienenID == null)
								throw new NullPointerException(); // Dies sollte nicht passieren, da zuvor die Schienen für die Kurse angelegt wurden.
							schienen.add(schienenID);
						} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
							// ignore exception
						}
					}
				}
				if (schienen.isEmpty())
					schienen.add(mapSchienen.values().iterator().next());
				final DTOGostBlockungKurs kursErstellt = new DTOGostBlockungKurs(idKurs, idBlockung, kurs.Fach_ID,
						kursart, kursNummer, false, schienen.size(), kurs.WochenStd == null ? 3 : kurs.WochenStd);
				kursErstellt.BezeichnungSuffix = "";
				mapKurseErstellt.put(kursErstellt.ID, kursErstellt);
				conn.transactionPersist(kursErstellt);
				// Erstelle die Kurs-Lehrer
				if (kurs.Lehrer_ID != null) {
					final int wochenStunden = (int) (kurs.WochenstdKL == null ? Math.round(kurs.WochenStd) : Math.round(kurs.WochenstdKL));
					final DTOGostBlockungKurslehrer kurslehrer = new DTOGostBlockungKurslehrer(idKurs, kurs.Lehrer_ID, 1, wochenStunden);
					conn.transactionPersist(kurslehrer);
				}
				// Füge die Kurs-Schienen-Zuordnung hinzu
				for (final long schienenID : schienen) {
					final DTOGostBlockungZwischenergebnisKursSchiene zuordnungKursSchiene = new DTOGostBlockungZwischenergebnisKursSchiene(
							idErgebnis, idKurs, schienenID);
					conn.transactionPersist(zuordnungKursSchiene);
				}
				// TODO Weitere Kurs-Lehrer ergänzen (s.o.)
				idKurs++;
			}

			// Regeln sind keine bekannt, also werden auch keine erstellt.

			// Ermittle die Fachwahlen des Abiturjahrgangs
			final GostFachwahlManager managerFachwahlen = (new DataGostAbiturjahrgangFachwahlen(conn, blockung.Abi_Jahrgang)).getFachwahlManager(blockung.Halbjahr);

			// Erstelle die Kurs-Schüler-Zuordnungen
			for (final DTOSchuelerLeistungsdaten ld : listLeistungsdaten) {
				final DTOSchuelerLernabschnittsdaten lernabschnitt = mapLernabschnitte.get(ld.Abschnitt_ID);
				if (lernabschnitt == null)
					continue; // Liegt kein Lernabschnitt in der Map vor, so wurde dieser zuvor entfernt - z.B. weil das Entlassdatum zeigt, dass der Schüler nicht mehr an der Schule war
				final long idSchueler = lernabschnitt.Schueler_ID;
				final GostKursart kursart = GostKursart.fromKuerzel(ld.KursartAllg);
				if (managerFachwahlen.hatFachwahl(idSchueler, ld.Fach_ID, kursart)) {
					final long kursID = mapKursIDs.get(ld.Kurs_ID);
					final DTOGostBlockungKurs kursErstellt = mapKurseErstellt.get(kursID);
					if ((kursErstellt == null) || (kursErstellt.Fach_ID != ld.Fach_ID))
						continue;
					final DTOGostBlockungZwischenergebnisKursSchueler zuordnungKursSchueler = new DTOGostBlockungZwischenergebnisKursSchueler(
						idErgebnis, kursID, idSchueler);
					conn.transactionPersist(zuordnungKursSchueler);
				}
			}
			conn.transactionCommit();
			return get(idBlockung);
		} catch (final Exception exception) {
			conn.transactionRollback();
			if (exception instanceof IllegalArgumentException)
				throw OperationError.NOT_FOUND.exception();
			if (exception instanceof final WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}


}
