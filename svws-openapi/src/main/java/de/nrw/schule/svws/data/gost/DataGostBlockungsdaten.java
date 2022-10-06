package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungRegel;
import de.nrw.schule.svws.core.data.gost.GostBlockungSchiene;
import de.nrw.schule.svws.core.data.gost.GostBlockungsdaten;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.data.gost.GostFachwahl;
import de.nrw.schule.svws.core.data.gost.GostStatistikFachwahl;
import de.nrw.schule.svws.core.data.gost.GostStatistikFachwahlHalbjahr;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInput;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputFach;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputFachwahl;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputKurs;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputKursart;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputRegel;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputSchueler;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutput;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputFachwahlZuKurs;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputKursZuSchiene;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputs;
import de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmus;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelParameterTyp;
import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp;
import de.nrw.schule.svws.core.types.statkue.Jahrgaenge;
import de.nrw.schule.svws.core.types.statkue.ZulaessigesFach;
import de.nrw.schule.svws.core.utils.gost.GostBlockungsdatenManager;
import de.nrw.schule.svws.core.utils.gost.GostFaecherManager;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungKurslehrer;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungRegel;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungRegelParameter;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchiene;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchueler;
import de.nrw.schule.svws.db.dto.current.schild.faecher.DTOFach;
import de.nrw.schule.svws.db.dto.current.svws.db.DTODBAutoInkremente;
import de.nrw.schule.svws.db.dto.current.views.gost.DTOViewGostSchuelerAbiturjahrgang;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/** 
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO {@link GostBlockungsdaten}. 
 */
public class DataGostBlockungsdaten extends DataManager<Long> {

	/** 
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBlockungsdaten}.
	 * 
	 * @param conn    die Datenbank-Verbindung für den Datenbankzugriff 
	 */
	public DataGostBlockungsdaten(DBEntityManager conn) {
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
	private Function<DTOGostBlockung, GostBlockungsdaten> dtoMapper = (DTOGostBlockung blockung) -> {
		GostBlockungsdaten daten = new GostBlockungsdaten();
		daten.id = blockung.ID;
		daten.name = blockung.Name;
		daten.abijahrgang = blockung.Abi_Jahrgang;
		daten.gostHalbjahr = blockung.Halbjahr.id;
		daten.istAktiv = blockung.IstAktiv;
		daten.vorlageID = blockung.Vorlage_ID;
		return daten;
	};

	/** 
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOGostBlockungSchiene} in einen Core-DTO
	 * {@link GostBlockungSchiene}.
	 */
	public static Function<DTOGostBlockungSchiene, GostBlockungSchiene> dtoMapperSchiene = (
			DTOGostBlockungSchiene schiene) -> {
		GostBlockungSchiene daten = new GostBlockungSchiene();
		daten.id = schiene.ID;
		daten.nummer = schiene.Nummer == null ? 1 : schiene.Nummer;
		daten.bezeichnung = schiene.Bezeichnung;
		daten.wochenstunden = schiene.Wochenstunden;
		return daten;
	};

	/** 
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOGostBlockungKurs} in einen Core-DTO
	 * {@link GostBlockungKurs}. 
	 */
	public static Function<DTOGostBlockungKurs, GostBlockungKurs> dtoMapperKurse = (DTOGostBlockungKurs kurs) -> {
		GostBlockungKurs daten = new GostBlockungKurs();
		daten.id = kurs.ID;
		daten.fach_id = kurs.Fach_ID;
		daten.kursart = kurs.Kursart.id;
		daten.nummer = kurs.Kursnummer;
		daten.istKoopKurs = kurs.IstKoopKurs;
		daten.suffix = kurs.BezeichnungSuffix == null ? "" : kurs.BezeichnungSuffix;
		daten.wochenstunden = kurs.Wochenstunden;
		return daten;
	};
	

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
	GostBlockungsdatenManager getBlockungsdatenManagerFromDB(Long id) {
		// Bestimme die Blockung
		DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, id);
		if (blockung == null)
			throw OperationError.NOT_FOUND.exception();
		// Bestimme die Fächer des Abiturjahrgangs
		GostFaecherManager faecherManager = (new DataGostFaecher(conn, blockung.Abi_Jahrgang)).getListInternal();
		if (faecherManager == null)
			throw OperationError.NOT_FOUND.exception();
		GostBlockungsdatenManager manager = new GostBlockungsdatenManager(dtoMapper.apply(blockung), faecherManager);
		// Bestimme alle Schienen
		List<DTOGostBlockungSchiene> schienen = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", blockung.ID,
				DTOGostBlockungSchiene.class);
		for (DTOGostBlockungSchiene schiene : schienen)
			manager.addSchiene(dtoMapperSchiene.apply(schiene));
		// Bestimme die Kurse, welche für die Blockung angelegt wurden
		List<DTOGostBlockungKurs> kurse = conn.queryNamed("DTOGostBlockungKurs.blockung_id", blockung.ID,
				DTOGostBlockungKurs.class);
		for (DTOGostBlockungKurs kurs : kurse)
			manager.addKurs(dtoMapperKurse.apply(kurs));
		// Bestimme alle Regeln
		List<DTOGostBlockungRegel> regeln = conn.queryNamed("DTOGostBlockungRegel.blockung_id", blockung.ID,
				DTOGostBlockungRegel.class);
		if (regeln.size() > 0) {
			List<Long> regelIDs = regeln.stream().map(r -> r.ID).collect(Collectors.toList());
			List<DTOGostBlockungRegelParameter> regelParamsDB = conn.queryNamed(
					"DTOGostBlockungRegelParameter.regel_id.multiple", regelIDs, DTOGostBlockungRegelParameter.class);
			Map<Long, List<DTOGostBlockungRegelParameter>> regelParams = regelParamsDB.stream()
					.collect(Collectors.groupingBy(r -> r.Regel_ID));
			for (DTOGostBlockungRegel regel : regeln) {
				GostBlockungRegel eintrag = new GostBlockungRegel();
				eintrag.id = regel.ID;
				eintrag.typ = regel.Typ.typ;
				List<DTOGostBlockungRegelParameter> p = regelParams.get(eintrag.id);
				if ((p != null) && (p.size() > 0))
					eintrag.parameter.addAll(p.stream().sorted((a, b) -> Integer.compare(a.Nummer, b.Nummer))
							.map(r -> r.Parameter).collect(Collectors.toList()));
				manager.addRegel(eintrag);
			}
		}
		return manager;
	}


	@Override
	public Response get(Long id) {
		GostBlockungsdaten daten;
		try {
			conn.transactionBegin();
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Erstellen den Manager mit den Blockungsdaten
			GostBlockungsdatenManager manager = getBlockungsdatenManagerFromDB(id);
			// Ergänze Blockungsliste und das aktuelle Blockungsergebnis
			(new DataGostBlockungsergebnisse(conn)).getErgebnisListe(manager);
			daten = manager.daten();
			conn.transactionCommit();
		} catch (Exception e) {
			if (e instanceof WebApplicationException webAppException)
				return webAppException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			// Perform a rollback if necessary
			conn.transactionRollback();
		}
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() <= 0)
			return Response.status(Status.OK).build();
		try {
			conn.transactionBegin();
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Bestimme die Blockung
			DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, id);
			if (blockung == null)
				return OperationError.NOT_FOUND.getResponse();
			for (Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				switch (key) {
					case "id" -> {
						Long patch_id = JSONMapper.convertToLong(value, true);
						if ((patch_id == null) || (patch_id != id))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "name" -> blockung.Name = JSONMapper.convertToString(value, false, false);
					case "gostHalbjahr" -> throw OperationError.BAD_REQUEST.exception();
					// TODO: ggf. Unterstützung für das Setzen von "schienen", "regeln" und "kurse
					default -> throw OperationError.BAD_REQUEST.exception();
				}
			}
			conn.transactionPersist(blockung);
			conn.transactionCommit();
		} catch (Exception e) {
			if (e instanceof WebApplicationException webAppException)
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
	public Response create(int abiturjahr, int halbjahr) {
		try {
			conn.transactionBegin();
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Prüfe die Parameter
			GostHalbjahr gostHalbjahr = GostHalbjahr.fromID(halbjahr);
			if (gostHalbjahr == null)
				throw OperationError.CONFLICT.exception();
			int anzahlSchienen = GostBlockungsdatenManager.getDefaultSchienenAnzahl(gostHalbjahr);
			DTOGostJahrgangsdaten abijahrgang = conn.queryByKey(DTOGostJahrgangsdaten.class, abiturjahr);
			if (abijahrgang == null)
				throw OperationError.CONFLICT.exception();
			// Lese die Fachwahlstatistiken aus der Datenbank - liegen keine vor, so kann auch keine Blockung erstellt
			// werden.
			DataGostAbiturjahrgangFachwahlen dataFachwahlen = new DataGostAbiturjahrgangFachwahlen(conn, abiturjahr);
			List<GostStatistikFachwahl> fachwahlen = dataFachwahlen.getFachwahlen();
			if (fachwahlen == null)
				throw OperationError.NOT_FOUND.exception();
			// Bestimme die ID der neuen Blockung
			DTODBAutoInkremente lastID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung");
			Long blockungID = lastID == null ? 1 : lastID.MaxID + 1;
			// Lese zunächst die bestehenden Blockungen mit dem Abiturjahr und dem Halbjahr ein (für weitere Prüfungen)
			List<DTOGostBlockung> blockungen = conn.queryList("SELECT e FROM DTOGostBlockung e WHERE e.Abi_Jahrgang = ?1 and e.Halbjahr = ?2", DTOGostBlockung.class, abijahrgang.Abi_Jahrgang, gostHalbjahr);
			// Prüfe, ob es bereits eine aktivierte Blockung gibt. In diesem Fall sollte der Aufruf fehlschlagen... (ggf. muss die aktivierte Blockung deaktiviert werden) 
			// TODO evtl. weitere Prüfungen: Prüfe abiturjahr + gostHalbjahr in Bezug auf den aktuellen Abschnitt (GUI-Warnung sinnvoll)
			// TODO evtl. weitere Prüfungen: Fehler: alte Blockung -> nicht erstellen...
			if (blockungen.stream().filter(b -> b.IstAktiv).count() > 0)
				throw OperationError.CONFLICT.exception("Es gibt bereits eine aktivierte Blockung, so dass die Daten der aktivierten Blockung bereits in die Leistungsdaten der Schüler übertragen wurden.");
			// Bestimme den Namen der neuen Blockung
			Set<String> namen = blockungen.stream().map(b -> b.Name).collect(Collectors.toUnmodifiableSet());
			int nameIndex = 1;
			String name = "Blockung ";
			while (namen.contains(name + nameIndex))
				nameIndex++;
			name += nameIndex;
			// Lese die Fächer dey gymnasialen Oberstufe ein. Diese müssen für den Abiturjahrgang vorhanden sein, damit eine Blockung angelegt werden darf
			GostFaecherManager faecherManager = (new DataGostFaecher(conn, abiturjahr)).getListInternal();
			if (faecherManager == null)
				return OperationError.NOT_FOUND.getResponse();
			// Lege ein "leeres" Ergebnis für manuelles Blocken an
			DTODBAutoInkremente lastErgebnisID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Zwischenergebnisse");
			long ergebnisID = lastErgebnisID == null ? 1 : lastErgebnisID.MaxID + 1;
			DTOGostBlockungZwischenergebnis erg = new DTOGostBlockungZwischenergebnis(ergebnisID, blockungID, false, true);
			// Blockung anlegen
			DTOGostBlockung blockung = new DTOGostBlockung(blockungID, name, abiturjahr, gostHalbjahr, false, ergebnisID);
			conn.transactionPersist(blockung);
			conn.transactionPersist(erg);
			GostBlockungsdaten daten = dtoMapper.apply(blockung);
			GostBlockungsdatenManager manager = new GostBlockungsdatenManager(daten, faecherManager);
			// Schienen anlegen
			DTODBAutoInkremente dbSchienenID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Schienen");
			long schienenID = dbSchienenID == null ? 0 : dbSchienenID.MaxID;
			for (int i = 1; i <= anzahlSchienen; i++) {
				DTOGostBlockungSchiene schiene = new DTOGostBlockungSchiene(schienenID + i, blockungID, i,
						"Schiene " + i, 3);
				conn.transactionPersist(schiene);
				manager.addSchiene(dtoMapperSchiene.apply(schiene));
			}
			// Anhand der Fachwahlstatistik eine Default-Anzahl für die Kursanzahl ermitteln und
			// DTOGostBlockungKurs-Objekte dafür persistieren
			// TODO Verbesserung des Algorithmus -> Optimierung... erstmal nur eine primitive Variante
			DTODBAutoInkremente dbKurseID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Kurse");
			long kurseID = dbKurseID == null ? 0 : dbKurseID.MaxID;
			for (GostStatistikFachwahl fw : fachwahlen) {
				ZulaessigesFach zulFach = ZulaessigesFach.getByKuerzelASD(fw.kuerzelStatistik);
				if (zulFach == ZulaessigesFach.VF)
					throw OperationError.INTERNAL_SERVER_ERROR.exception();
				GostStatistikFachwahlHalbjahr fwHj = fw.fachwahlen[gostHalbjahr.id];
				int anzahlLK = (fwHj.wahlenLK + 10) / 20;
				int anzahlGK = (fwHj.wahlenGK + 10) / 20;
				int anzahlZK = (fwHj.wahlenZK + 10) / 20;
				if ((fwHj.wahlenZK > 0) && (anzahlZK < 1))
					anzahlZK = 1;
				for (int i = 1; i <= anzahlLK; i++) {
					DTOGostBlockungKurs kurs = new DTOGostBlockungKurs(++kurseID, blockungID, fw.id, GostKursart.LK, i, false, 5);
					conn.transactionPersist(kurs);
					manager.addKurs(dtoMapperKurse.apply(kurs));
				}
				for (int i = 1; i <= anzahlGK; i++) {
					GostKursart kursart = (zulFach == ZulaessigesFach.VX) ? GostKursart.VTF
							: (zulFach == ZulaessigesFach.PX) ? GostKursart.PJK : GostKursart.GK;
					int wstd = 3;
					if ((kursart == GostKursart.GK) && ((Jahrgaenge.JG_EF == zulFach.getJahrgangAb()) || (Jahrgaenge.JG_11 == zulFach.getJahrgangAb()))) {
						wstd = 4;
					} else if (kursart == GostKursart.PJK) {
						// Wochenstunden des PJK anhand der Tabelle GostFaecher bestimmen
					} else if (kursart == GostKursart.VTF) {
						wstd = 2;
					}
					DTOGostBlockungKurs kurs = new DTOGostBlockungKurs(++kurseID, blockungID, fw.id, kursart, i, false, wstd);
					conn.transactionPersist(kurs);
					manager.addKurs(dtoMapperKurse.apply(kurs));
				}
				for (int i = 1; i <= anzahlZK; i++) {
					DTOGostBlockungKurs kurs = new DTOGostBlockungKurs(++kurseID, blockungID, fw.id, GostKursart.ZK, i, false, 3);
					conn.transactionPersist(kurs);
					manager.addKurs(dtoMapperKurse.apply(kurs));
				}
			}
			// Lege eine Kurs-Schienen-Zuordnung für das "leere" Ergebnis fest. Diese Kurse werden der ersten Schiene der neuen Blockung zugeordnet.
			for (GostBlockungKurs kurs : manager.daten().kurse)
				conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchiene(ergebnisID, kurs.id, schienenID + 1));
            // Ergänze Blockungsliste und das aktuelle Blockungsergebnis
            (new DataGostBlockungsergebnisse(conn)).getErgebnisListe(manager);
			conn.transactionCommit();
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof WebApplicationException webex)
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
	public Response delete(Long id) {
		// TODO use transaction
		GostUtils.pruefeSchuleMitGOSt(conn);
		// Bestimme die Blockung
		DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, id);
		if (blockung == null)
			return OperationError.NOT_FOUND.getResponse();
		// Entferne die Blockung
		conn.remove(blockung);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(id).build();		
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
	public Response berechne(long id, long zeit) {
		GostUtils.pruefeSchuleMitGOSt(conn);
		// Bestimme die Blockung
		DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, id);
		if (blockung == null)
			return OperationError.NOT_FOUND.getResponse();
		// Bestimme alle Schienen
		List<DTOGostBlockungSchiene> schienen = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", blockung.ID,
				DTOGostBlockungSchiene.class);
		if ((schienen == null) || (schienen.size() == 0))
			return OperationError.NOT_FOUND.getResponse();
		Map<Integer, Long> mapSchienenNummerToID = schienen.stream()
				.collect(Collectors.toMap(s -> s.Nummer, s -> s.ID));
		// Bestimme die Kurse, welche für die Blockung angelegt wurden
		List<DTOGostBlockungKurs> kurse = conn.queryNamed("DTOGostBlockungKurs.blockung_id", blockung.ID,
				DTOGostBlockungKurs.class);
		if ((kurse == null) || (kurse.size() == 0))
			return OperationError.NOT_FOUND.getResponse();
		// Bestimme alle Regeln
		List<DTOGostBlockungRegel> regeln = conn.queryNamed("DTOGostBlockungRegel.blockung_id", blockung.ID,
				DTOGostBlockungRegel.class);
		if (regeln == null)
			return OperationError.NOT_FOUND.getResponse();
		// Bestimme die Fächer des Abiturjahrgangs
		GostFaecherManager faecherManager = (new DataGostFaecher(conn, blockung.Abi_Jahrgang)).getListInternal();
		if (faecherManager == null)
			return OperationError.NOT_FOUND.getResponse();
		// Bestimme die Fachwahlen
		List<GostFachwahl> fachwahlen = (new DataGostAbiturjahrgangFachwahlen(conn, blockung.Abi_Jahrgang))
				.getSchuelerFachwahlen(blockung.Halbjahr);
		if (fachwahlen == null)
			return OperationError.NOT_FOUND.getResponse();
		// Erzeuge den Input für den Kursblockungsalgorithmus
		KursblockungInput input = new KursblockungInput();
		input.input = id;
		input.maxTimeMillis = zeit;
		input.maxSchienen = schienen.size();
		// Ergänze die Regeln
		if (regeln.size() > 0) {
			List<Long> regelIDs = regeln.stream().map(r -> r.ID).collect(Collectors.toList());
			List<DTOGostBlockungRegelParameter> regelParamsDB = conn.queryNamed(
					"DTOGostBlockungRegelParameter.regel_id.multiple", regelIDs, DTOGostBlockungRegelParameter.class);
			Map<Long, List<DTOGostBlockungRegelParameter>> regelParams = regelParamsDB.stream()
					.collect(Collectors.groupingBy(r -> r.Regel_ID));
			for (DTOGostBlockungRegel regel : regeln) {
				List<DTOGostBlockungRegelParameter> p = regelParams.get(regel.ID);
				if ((p == null) || (p.size() < 0))
					continue;
				List<Long> params = p.stream().sorted((a, b) -> Integer.compare(a.Nummer, b.Nummer))
						.map(r -> r.Parameter).collect(Collectors.toList());
				KursblockungInputRegel r = new KursblockungInputRegel();
				r.databaseID = regel.ID;
				r.typ = regel.Typ.typ;
				r.daten = params.toArray(new Long[0]);
				input.regeln.add(r);
			}
		}
		for (GostKursart kursart : GostKursart.values()) {
			KursblockungInputKursart ka = new KursblockungInputKursart();
			ka.id = kursart.id;
			ka.representation = kursart.kuerzel;
			input.kursarten.add(ka);
		}
		for (DTOGostBlockungKurs kurs : kurse) {
			KursblockungInputKurs k = new KursblockungInputKurs();
			k.id = kurs.ID;
			k.fach = kurs.Fach_ID;
			k.kursart = kurs.Kursart.id;
			k.schienen = 1;
			k.representation = "Kurs " + kurs.Kursart.kuerzel + kurs.ID; // TODO
			input.kurse.add(k);
		}
		for (GostFach fach : faecherManager.faecher()) {
			KursblockungInputFach f = new KursblockungInputFach();
			f.id = fach.id;
			f.representation = fach.kuerzelAnzeige;
			input.faecher.add(f);
		}
		HashMap<Long, GostFachwahl> schueler = new HashMap<>();
		HashMap<Long, Long> fachwahl2schueler = new HashMap<>();
		for (GostFachwahl fachwahl : fachwahlen) {
			if (!schueler.containsKey(fachwahl.schuelerID))
				schueler.put(fachwahl.schuelerID, fachwahl);
			fachwahl2schueler.put(fachwahl.id, fachwahl.schuelerID);
			KursblockungInputFachwahl fw = new KursblockungInputFachwahl();
			fw.id = fachwahl.id;
			fw.schueler = fachwahl.schuelerID;
			fw.fach = fachwahl.fachID;
			fw.kursart = fachwahl.kursartID;
			fw.representation = "%s;%s;%d;%d".formatted(fachwahl.schuelerNachname, fachwahl.schuelerVorname,
					fachwahl.fachID, fachwahl.kursartID);
			input.fachwahlen.add(fw);
		}
		for (GostFachwahl fachwahl : schueler.values()) {
			KursblockungInputSchueler s = new KursblockungInputSchueler();
			s.id = fachwahl.schuelerID;
			s.representation = fachwahl.schuelerNachname + "," + fachwahl.schuelerVorname;
			input.schueler.add(s);
		}
		KursblockungAlgorithmus algo = new KursblockungAlgorithmus();
		KursblockungOutputs outputs = algo.handle(input);
		try {
			Vector<Long> ergebnisse = new Vector<>();
			conn.transactionBegin();
			DTODBAutoInkremente lastID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Zwischenergebnisse");
			long ergebnisID = lastID == null ? 1 : lastID.MaxID + 1;
			for (KursblockungOutput output : outputs.outputs) {
				// TODO Der Test unten funktioniert nicht ... output.input wurde scheinbar vom Algo nicht gesetzt
				// if (output.input != id)
				// throw new Exception("Die ID der Blockung ist bei dem Blockungsergebnis fehlerhaft.");

				// Erstelle die Zurordnungen von Schülern zu Kursen und von Kursen zu Schienen. Ermittle dabei auch die
				// Anzahl der Umwähler
				HashSet<Long> setUmwaehler = new HashSet<>();
				for (KursblockungOutputFachwahlZuKurs fzk : output.fachwahlenZuKurs) {
					Long schuelerID = fachwahl2schueler.get(fzk.fachwahl);
					if (fzk.kurs == -1) {
						setUmwaehler.add(schuelerID);
					}
				}

				// Schreiben das Ergebnis in die Datenbank
				DTOGostBlockungZwischenergebnis erg = new DTOGostBlockungZwischenergebnis(ergebnisID, id, false, false);
				// TODO Bewertung
				conn.transactionPersist(erg);
				for (KursblockungOutputFachwahlZuKurs fzk : output.fachwahlenZuKurs) {
					if (fzk.kurs == -1) // Beachte nur Fachwahlen, die zugeordnet werden konnten
						continue;
					Long schuelerID = fachwahl2schueler.get(fzk.fachwahl);
					conn.transactionPersist(
							new DTOGostBlockungZwischenergebnisKursSchueler(ergebnisID, fzk.kurs, schuelerID));
				}
				for (KursblockungOutputKursZuSchiene kzs : output.kursZuSchiene) {
					for (int schiene : kzs.schienen) {
						long schienenID = mapSchienenNummerToID.get(schiene + 1);
						conn.transactionPersist(
								new DTOGostBlockungZwischenergebnisKursSchiene(ergebnisID, kzs.kurs, schienenID));
					}
				}

				// Ergänze die ID bei der Liste der berechneten Ergebnisse
				ergebnisse.add(ergebnisID);
				ergebnisID++;
			}
			if (conn.transactionCommit())
				return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(ergebnisse).build();
			return OperationError.INTERNAL_SERVER_ERROR
					.getResponse("\"Fehler beim Schreiben der Zwischenergebnisse für die Kursblockung.\"");
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse(exception);
		}
	}

	
	/**
	 * Erzeugt ein Duplikat der angegebenen Blockung und für den
	 * Fall das ein Ergebnis angegeben wurde auch des Ergebnisses.
	 * Ist kein Ergebnis angegeben, so wird nur die Blockungsdefinition
	 * ohne ein Ergebnis dupliziert.
	 * 
	 * @param idBlockungOriginal   die ID der zu duplizierenden Blockungsdefinition
	 * @param idErgebnisOriginal   das zu duplizierende Blockungsergebnis
	 * 
	 * @return die Blockungsdaten der duplizierten Blockung
	 */
	public Response dupliziere(Long idBlockungOriginal, Long idErgebnisOriginal) {
		try {
			if ((idBlockungOriginal == null) && (idErgebnisOriginal == null))
				throw OperationError.CONFLICT.exception();
			conn.transactionBegin();
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Bestimme die Blockung und das zugehörige Ergebnis
			DTOGostBlockungZwischenergebnis ergebnisOriginal;
			DTOGostBlockung blockungOriginal;
			if (idErgebnisOriginal != null) {
				ergebnisOriginal = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idErgebnisOriginal);
				if (ergebnisOriginal == null)
					throw OperationError.NOT_FOUND.exception();
				if (idBlockungOriginal == null)
					idBlockungOriginal = ergebnisOriginal.Blockung_ID;
				if (idBlockungOriginal != ergebnisOriginal.Blockung_ID)
					throw OperationError.CONFLICT.exception();
				// Bestimme die Blockung			
				blockungOriginal = conn.queryByKey(DTOGostBlockung.class, idBlockungOriginal);
				if (blockungOriginal == null)
					throw OperationError.NOT_FOUND.exception();
			} else {
				blockungOriginal = conn.queryByKey(DTOGostBlockung.class, idBlockungOriginal);
				if (blockungOriginal == null)
					throw OperationError.NOT_FOUND.exception();
				idErgebnisOriginal = blockungOriginal.Vorlage_ID;
				ergebnisOriginal = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idErgebnisOriginal);
				if (ergebnisOriginal == null)
					throw OperationError.NOT_FOUND.exception();
				if (idBlockungOriginal != ergebnisOriginal.Blockung_ID)
					throw OperationError.CONFLICT.exception();
			}
			// Bestimme die ID für das Duplikat der Blockung
			DTODBAutoInkremente lastID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung");
			Long idBlockungDuplikat = lastID == null ? 1 : lastID.MaxID + 1;
			// Bestimme die für das Vorlage-Ergebnis der duplizierten Blockung
			DTODBAutoInkremente dbErgebnisID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Zwischenergebnisse");
			long idErgebnisDuplikat = dbErgebnisID == null ? 1 : dbErgebnisID.MaxID + 1;
			// Erstelle das Duplikat
			DTOGostBlockung blockungDuplikat = new DTOGostBlockung(idBlockungDuplikat, blockungOriginal.Name + " (Duplikat)", blockungOriginal.Abi_Jahrgang, blockungOriginal.Halbjahr, false, idErgebnisDuplikat);
			conn.transactionPersist(blockungDuplikat);
			// Dupliziere die Schienen
			DTODBAutoInkremente dbSchienenID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Schienen");
			long idSchieneDuplikat = dbSchienenID == null ? 0 : dbSchienenID.MaxID + 1;
			HashMap<Long, Long> mapSchienenIDs = new HashMap<>();
			List<DTOGostBlockungSchiene> schienenOriginal = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", idBlockungOriginal,
					DTOGostBlockungSchiene.class);
			for (DTOGostBlockungSchiene schieneOriginal : schienenOriginal) {
				DTOGostBlockungSchiene schieneDuplikat = new DTOGostBlockungSchiene(idSchieneDuplikat, idBlockungDuplikat, schieneOriginal.Nummer, schieneOriginal.Bezeichnung, schieneOriginal.Wochenstunden);
				mapSchienenIDs.put(schieneOriginal.ID, schieneDuplikat.ID);
				conn.transactionPersist(schieneDuplikat);
				idSchieneDuplikat++;
			}
			// Dupliziere die Kurse
			DTODBAutoInkremente dbKurseID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Kurse");
			long idKursDuplikat = dbKurseID == null ? 0 : dbKurseID.MaxID + 1;
			HashMap<Long, Long> mapKursIDs = new HashMap<>();
			List<DTOGostBlockungKurs> kurseOriginal = conn.queryNamed("DTOGostBlockungKurs.blockung_id", idBlockungOriginal,
					DTOGostBlockungKurs.class);
			List<Long> kursIDsOriginal = kurseOriginal.stream().map(k -> k.ID).collect(Collectors.toList());
			for (DTOGostBlockungKurs kursOriginal : kurseOriginal) {
				DTOGostBlockungKurs kursDuplikat = new DTOGostBlockungKurs(idKursDuplikat, idBlockungDuplikat, kursOriginal.Fach_ID,
						kursOriginal.Kursart, kursOriginal.Kursnummer, kursOriginal.IstKoopKurs, kursOriginal.Wochenstunden);
				kursDuplikat.BezeichnungSuffix = kursOriginal.BezeichnungSuffix;
				mapKursIDs.put(kursOriginal.ID, kursDuplikat.ID);
				conn.transactionPersist(kursDuplikat);
				idKursDuplikat++;
			}
			// Dupliziere die KursLehrer
			if (kursIDsOriginal.size() > 0) {
				List<DTOGostBlockungKurslehrer> kurslehrerListeOriginal = conn.queryNamed("DTOGostBlockungKurslehrer.blockung_kurs_id.multiple", kursIDsOriginal, DTOGostBlockungKurslehrer.class);
				for (DTOGostBlockungKurslehrer kurslehrerOriginal : kurslehrerListeOriginal) {
					idKursDuplikat = mapKursIDs.get(kurslehrerOriginal.Blockung_Kurs_ID);
					DTOGostBlockungKurslehrer kurslehrerDuplikat = new DTOGostBlockungKurslehrer(idKursDuplikat, 
							kurslehrerOriginal.Lehrer_ID, kurslehrerOriginal.Reihenfolge, kurslehrerOriginal.Wochenstunden);
					conn.transactionPersist(kurslehrerDuplikat);
				}
			}
			// Dupliziere die Regeln
			DTODBAutoInkremente dbRegelID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Regeln");
			long idRegelDuplikat = dbRegelID == null ? 1 : dbRegelID.MaxID + 1;
			HashMap<Long, Long> mapRegelIDs = new HashMap<>();
			List<DTOGostBlockungRegel> regelnOriginal = conn.queryNamed("DTOGostBlockungRegel.blockung_id", idBlockungOriginal,
					DTOGostBlockungRegel.class);
			List<Long> regelIDsOriginal = regelnOriginal.stream().map(k -> k.ID).collect(Collectors.toList());
			for (DTOGostBlockungRegel regelOriginal : regelnOriginal) {
				DTOGostBlockungRegel regelDuplikat = new DTOGostBlockungRegel(idRegelDuplikat, idBlockungDuplikat, regelOriginal.Typ);
				mapRegelIDs.put(regelOriginal.ID, regelDuplikat.ID);
				conn.transactionPersist(regelDuplikat);
				idRegelDuplikat++;
			}
			// Dupliziere die RegelParameter
			if (regelIDsOriginal.size() > 0) {
				List<DTOGostBlockungRegelParameter> paramListeOriginal = conn.queryNamed("DTOGostBlockungRegelParameter.regel_id.multiple", regelIDsOriginal, DTOGostBlockungRegelParameter.class);
				for (DTOGostBlockungRegelParameter paramOriginal : paramListeOriginal) {
					idRegelDuplikat = mapRegelIDs.get(paramOriginal.Regel_ID);
					DTOGostBlockungRegelParameter paramDuplikat = new DTOGostBlockungRegelParameter(idRegelDuplikat, 
							paramOriginal.Nummer, paramOriginal.Parameter);
					conn.transactionPersist(paramDuplikat);
				}
			}
			// Dupliziere das Zwischenergebnis und markiere es als Duplikat
			DTOGostBlockungZwischenergebnis ergebnisDuplikat = new DTOGostBlockungZwischenergebnis(
					idErgebnisDuplikat, idBlockungDuplikat, ergebnisOriginal.IstMarkiert, true);
			conn.transactionPersist(ergebnisDuplikat);
			// Dupliziere Kurs-Schienen-Zuordnung
			List<DTOGostBlockungZwischenergebnisKursSchiene> zuordnungKursSchieneListeOriginal = conn.queryNamed("DTOGostBlockungZwischenergebnisKursSchiene.zwischenergebnis_id", idErgebnisOriginal,
					DTOGostBlockungZwischenergebnisKursSchiene.class);
			for (DTOGostBlockungZwischenergebnisKursSchiene zuordnungKursSchieneOriginal : zuordnungKursSchieneListeOriginal) {
				idKursDuplikat = mapKursIDs.get(zuordnungKursSchieneOriginal.Blockung_Kurs_ID);
				idSchieneDuplikat = mapSchienenIDs.get(zuordnungKursSchieneOriginal.Schienen_ID);
				DTOGostBlockungZwischenergebnisKursSchiene zuordnungKursSchieneDuplikat = new DTOGostBlockungZwischenergebnisKursSchiene(
						idErgebnisDuplikat, idKursDuplikat, idSchieneDuplikat);
				conn.transactionPersist(zuordnungKursSchieneDuplikat);
			}
			// Dupliziere Kurs-Schüler-Zuordnung
			List<DTOGostBlockungZwischenergebnisKursSchueler> zuordnungKursSchuelerListeOriginal = conn.queryNamed("DTOGostBlockungZwischenergebnisKursSchueler.zwischenergebnis_id", idErgebnisOriginal,
					DTOGostBlockungZwischenergebnisKursSchueler.class);
			for (DTOGostBlockungZwischenergebnisKursSchueler zuordnungKursSchuelerOriginal : zuordnungKursSchuelerListeOriginal) {
				idKursDuplikat = mapKursIDs.get(zuordnungKursSchuelerOriginal.Blockung_Kurs_ID);
				DTOGostBlockungZwischenergebnisKursSchueler zuordnungKursSchuelerDuplikat = new DTOGostBlockungZwischenergebnisKursSchueler(
						idErgebnisDuplikat, idKursDuplikat, zuordnungKursSchuelerOriginal.Schueler_ID);
				conn.transactionPersist(zuordnungKursSchuelerDuplikat);
			}
			conn.transactionCommit();
			return get(idBlockungDuplikat);
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof IllegalArgumentException e)
				throw OperationError.NOT_FOUND.exception();
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}
	

	/** 
     * Fügt einen weiteren Kurses zu einer Blockung der Gymnasialen Oberstufe hinzu
	 * 
     * @param idBlockung   die ID der Blockung
     * @param idFach       die ID des Faches
     * @param idKursart    die ID der Kursart
	 * 
	 * @return Eine Response mit der ID des neuen Kurses der Blockung 
	 */
	public Response addKurs(long idBlockung, long idFach, int idKursart) {
		try {
			conn.transactionBegin();
			// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Prüfe, ob die Blockung mit der ID existiert			
			DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
			if (blockung == null)
				throw OperationError.NOT_FOUND.exception();
			// Bestimme das Fach und prüfe, ob es ein Fach der gymnasialen Oberstufe ist
			DTOFach fach = conn.queryByKey(DTOFach.class, idFach);
			if (fach == null)
				throw OperationError.NOT_FOUND.exception();
			if ((fach.StatistikFach == ZulaessigesFach.VF) || (!fach.IstOberstufenFach))
				throw OperationError.CONFLICT.exception();
			// Bestimme die Kursart
			GostKursart kursart = GostKursart.fromID(idKursart);
			if (kursart == GostKursart.GK)
				kursart = (fach.StatistikFach == ZulaessigesFach.VX) 
					? GostKursart.VTF
					: (fach.StatistikFach == ZulaessigesFach.PX) ? GostKursart.PJK : GostKursart.GK;
			// Bestimme die ID, für welche der Datensatz eingefügt wird
			DTODBAutoInkremente dbKurseID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Kurse");
			long idKurs = dbKurseID == null ? 1 : dbKurseID.MaxID + 1;
			// Ermittle, ob bereits Kurse mit für das Fach und die Kursart existieren
	    	String jpql = "SELECT e FROM DTOGostBlockungKurs e WHERE e.Blockung_ID = ?1 and e.Fach_ID = ?2 and e.Kursart = ?3";
	    	List<DTOGostBlockungKurs> kurse = conn.queryList(jpql, DTOGostBlockungKurs.class, idBlockung, idFach, kursart);
	    	int kursnummer = 1;
	    	if ((kurse != null) && (kurse.size() > 0)) { // Bestimme die erste freie Kursnummer
	    		Set<Integer> kursIDs = kurse.stream().map(e -> e.Kursnummer).collect(Collectors.toSet());
	    		while (kursIDs.contains(kursnummer))
	    			kursnummer++;
	    	}
	    	DTOGostBlockungKurs kurs = null;
	    	if (kursart == GostKursart.LK) {
	    		kurs = new DTOGostBlockungKurs(idKurs, idBlockung, idFach, kursart, kursnummer, false, 5);	    		
	    	} else if (kursart == GostKursart.GK) {
	    		kurs = new DTOGostBlockungKurs(idKurs, idBlockung, idFach, kursart, kursnummer, false, fach.IstMoeglichAlsNeueFremdspracheInSekII ? 4 : 3);
	    	} else if (kursart == GostKursart.PJK) {
	    		// TODO Wochenstunden anhand der Tabelle GostFaecher bestimmen (PJK)
	    		kurs = new DTOGostBlockungKurs(idKurs, idBlockung, idFach, kursart, kursnummer, false, 3);
	    	} else if (kursart == GostKursart.VTF) {
	    		kurs = new DTOGostBlockungKurs(idKurs, idBlockung, idFach, kursart, kursnummer, false, 2);	    		
	    	} else if (kursart == GostKursart.ZK) {
	    		kurs = new DTOGostBlockungKurs(idKurs, idBlockung, idFach, kursart, kursnummer, false, 3);	    		
	    	}
			conn.transactionPersist(kurs);	    		
			conn.transactionCommit();
			GostBlockungKurs daten = dtoMapperKurse.apply(kurs);
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof IllegalArgumentException e)
				throw OperationError.NOT_FOUND.exception();
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}

	
	/** 
     * Entfernt einen Kurs des angegebenen Faches und Kursart bei einer Blockung der 
     * Gymnasialen Oberstufe.
	 * 
     * @param idBlockung   die ID der Blockung
     * @param idFach       die ID des Faches
     * @param idKursart    die ID der Kursart
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt. 
	 */
	public Response deleteKurs(long idBlockung, long idFach, int idKursart) {
		// TODO use transaction
		GostUtils.pruefeSchuleMitGOSt(conn);
		// Bestimme das Fach und prüfe, ob es ein Fach der gymnasialen Oberstufe ist
		DTOFach fach = conn.queryByKey(DTOFach.class, idFach);
		if (fach == null)
			throw OperationError.NOT_FOUND.exception();
		if ((fach.StatistikFach == ZulaessigesFach.VF) || (!fach.IstOberstufenFach))
			throw OperationError.CONFLICT.exception();
		// Bestimme die Kursart
		GostKursart kursart = GostKursart.fromID(idKursart);
		if (kursart == GostKursart.GK)
			kursart = (fach.StatistikFach == ZulaessigesFach.VX) 
				? GostKursart.VTF
				: (fach.StatistikFach == ZulaessigesFach.PX) ? GostKursart.PJK : GostKursart.GK;
		// Bestimme die Kurse der Blockung, welche das Kriterium erfüllen und löschen Kurs mit der höchsten Kursnummer
    	String jpql = "SELECT e FROM DTOGostBlockungKurs e WHERE e.Blockung_ID = ?1 and e.Fach_ID = ?2 and e.Kursart = ?3";
    	List<DTOGostBlockungKurs> kurse = conn.queryList(jpql, DTOGostBlockungKurs.class, idBlockung, idFach, kursart);
    	if ((kurse == null) || (kurse.size() == 0))
    		throw OperationError.NOT_FOUND.exception();
    	DTOGostBlockungKurs kurs = kurse.stream().max((a,b) -> Integer.compare(a.Kursnummer, b.Kursnummer)).get();
		GostBlockungKurs daten = dtoMapperKurse.apply(kurs);
		if (!conn.remove(kurs))
			throw OperationError.INTERNAL_SERVER_ERROR.exception();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	
	/** 
     * Fügt eine weitere Schiene zu einer Blockung der Gymnasialen Oberstufe hinzu
	 * 
     * @param idBlockung   die ID der Blockung
	 * 
	 * @return Eine Response mit der ID der neuen Schiene 
	 */
	public Response addSchiene(long idBlockung) {
		try {
			conn.transactionBegin();
			// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Prüfe, ob die Blockung mit der ID existiert			
			DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
			if (blockung == null)
				throw OperationError.NOT_FOUND.exception();
			// Bestimme die ID, für welche der Datensatz eingefügt wird
			DTODBAutoInkremente dbSchienenID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Schienen");
			long idSchiene = dbSchienenID == null ? 1 : dbSchienenID.MaxID + 1;
			// Ermittle, ob bereits Schienen existieren
			List<DTOGostBlockungSchiene> schienen = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", idBlockung, DTOGostBlockungSchiene.class);
	    	int schienennummer = 1;
	    	if ((schienen != null) && (schienen.size() > 0)) { // Bestimme die erste freie Schienennummer
	    		Set<Integer> schienenIDs = schienen.stream().map(e -> e.Nummer).collect(Collectors.toSet());
	    		while (schienenIDs.contains(schienennummer))
	    			schienennummer++;
	    	}
	    	DTOGostBlockungSchiene schiene = new DTOGostBlockungSchiene(idSchiene, idBlockung, schienennummer, "Schiene " + schienennummer, 3);
	    	conn.transactionPersist(schiene);
			conn.transactionCommit();
			GostBlockungSchiene daten = dtoMapperSchiene.apply(schiene);
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof IllegalArgumentException e)
				throw OperationError.NOT_FOUND.exception();
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}

	
	/** 
     * Entfernt eine Schiene bei einer Blockung der Gymnasialen Oberstufe.
	 * 
     * @param idBlockung   die ID der Blockung
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt. 
	 */
	public Response deleteSchiene(long idBlockung) {
		// TODO use transaction
		GostUtils.pruefeSchuleMitGOSt(conn);
		// Bestimme die Schienen der Blockung und löschen die Schiene mit der höchsten Nummer
    	List<DTOGostBlockungSchiene> schienen = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", idBlockung, DTOGostBlockungSchiene.class);
    	if ((schienen == null) || (schienen.size() == 0))
    		throw OperationError.NOT_FOUND.exception();
    	DTOGostBlockungSchiene schiene = schienen.stream().max((a,b) -> Integer.compare(a.Nummer, b.Nummer)).get();
    	GostBlockungSchiene daten = dtoMapperSchiene.apply(schiene);
		if (!conn.remove(schiene))
			throw OperationError.INTERNAL_SERVER_ERROR.exception();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	
	/** 
     * Fügt eine Regel mit Default-Werten zu einer Blockung der Gymnasialen Oberstufe hinzu.
	 * 
     * @param idBlockung   die ID der Blockung
	 * @param idRegelTyp   die ID des Typs der Blockungsregel (siehe {@link GostKursblockungRegelTyp})
	 * 
	 * @return Eine Response mit der neuen Regel 
	 */
	public Response addRegel(long idBlockung, int idRegelTyp) {
		try {
			conn.transactionBegin();
			// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Prüfe, ob die Blockung mit der ID existiert			
			DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
			if (blockung == null)
				throw OperationError.NOT_FOUND.exception();
			// Prüfe ob die ID des Typs korrekt ist
			GostKursblockungRegelTyp regelTyp = GostKursblockungRegelTyp.fromTyp(idRegelTyp);
			if (regelTyp == GostKursblockungRegelTyp.UNDEFINIERT)
				throw OperationError.CONFLICT.exception();
			// Bestimme die ID, für welche der Datensatz eingefügt wird
			DTODBAutoInkremente dbRegelID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Regeln");
			long idRegel = dbRegelID == null ? 1 : dbRegelID.MaxID + 1;
			// Füge die Regel hinzu
	    	DTOGostBlockungRegel regel = new DTOGostBlockungRegel(idRegel, idBlockung, regelTyp);
	    	conn.transactionPersist(regel);
	    	GostBlockungRegel daten = new GostBlockungRegel();
	    	daten.id = idRegel;
	    	daten.typ = regelTyp.typ;
	    	// Füge Default-Parameter zu der Regel hinzu.
	    	for (int i = 0; i < regelTyp.getParamCount(); i++) {
	    		GostKursblockungRegelParameterTyp paramType = regelTyp.getParamType(i);
	    		long paramValue = switch (paramType) {
					case KURSART -> GostKursart.LK.id;
					case KURS_ID -> {
				    	List<DTOGostBlockungKurs> kurse = conn.queryNamed("DTOGostBlockungKurs.blockung_id", idBlockung, DTOGostBlockungKurs.class);
						if ((kurse == null) || (kurse.size() == 0))
							throw OperationError.NOT_FOUND.exception();
						yield kurse.get(0).ID;
					}
					case SCHIENEN_NR -> {
						Optional<Integer> minSchiene = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", idBlockung, DTOGostBlockungSchiene.class).stream().map(s -> s.Nummer).min((a,b) -> Integer.compare(a, b));
						if (minSchiene.isEmpty())
							throw OperationError.NOT_FOUND.exception();
						yield minSchiene.get();
					}
					case SCHUELER_ID -> {
						List<DTOViewGostSchuelerAbiturjahrgang> schueler = conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", blockung.Abi_Jahrgang, DTOViewGostSchuelerAbiturjahrgang.class);
						if ((schueler == null) || (schueler.size() == 0))
							throw OperationError.NOT_FOUND.exception();
						yield schueler.get(0).ID;
					}
	    		};
	    		DTOGostBlockungRegelParameter param = new DTOGostBlockungRegelParameter(idRegel, i, paramValue);
	    		conn.transactionPersist(param);
	    		daten.parameter.add(paramValue);
	    	}
			conn.transactionCommit();
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof IllegalArgumentException e)
				throw OperationError.NOT_FOUND.exception(e);
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}

}
