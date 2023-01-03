package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungKursLehrer;
import de.nrw.schule.svws.core.data.gost.GostBlockungRegel;
import de.nrw.schule.svws.core.data.gost.GostBlockungsdaten;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisSchiene;
import de.nrw.schule.svws.core.data.gost.GostFachwahl;
import de.nrw.schule.svws.core.data.gost.GostStatistikFachwahl;
import de.nrw.schule.svws.core.data.gost.GostStatistikFachwahlHalbjahr;
import de.nrw.schule.svws.core.data.schueler.Schueler;
import de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmus;
import de.nrw.schule.svws.core.types.fach.ZulaessigesFach;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.core.types.jahrgang.Jahrgaenge;
import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelParameterTyp;
import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp;
import de.nrw.schule.svws.core.utils.gost.GostBlockungsdatenManager;
import de.nrw.schule.svws.core.utils.gost.GostBlockungsergebnisManager;
import de.nrw.schule.svws.core.utils.gost.GostFachwahlManager;
import de.nrw.schule.svws.core.utils.gost.GostFaecherManager;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.data.JSONMapper;
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
import de.nrw.schule.svws.db.dto.current.schild.lehrer.DTOLehrer;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.svws.db.DTODBAutoInkremente;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
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
	public static DTOGostBlockungZwischenergebnis pruefeNurVorlageErgebnis(DBEntityManager conn, DTOGostBlockung blockung) {
		if (blockung == null)
			throw OperationError.NOT_FOUND.exception("Blockung nicht gefunden.");
		List<DTOGostBlockungZwischenergebnis> ergebnisse = conn.queryNamed("DTOGostBlockungZwischenergebnis.blockung_id", blockung.ID, DTOGostBlockungZwischenergebnis.class);
		if ((ergebnisse == null) || (ergebnisse.size() == 0))
			throw OperationError.INTERNAL_SERVER_ERROR.exception("Kein Vorlage-Ergebnis für die Blockung in der Datenbank vorhanden.");
		if (ergebnisse.size() > 1)
			return null;
		return ergebnisse.get(0);
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
	GostBlockungsdatenManager getBlockungsdatenManagerFromDB(Long id) {
		// Bestimme die Blockung
		DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, id);
		if (blockung == null)
			throw OperationError.NOT_FOUND.exception();

		// Fächer hinzufügen.
		GostFaecherManager faecherManager = (new DataGostFaecher(conn, blockung.Abi_Jahrgang)).getListInternal();
		if (faecherManager == null)
			throw OperationError.NOT_FOUND.exception();
		GostBlockungsdatenManager manager = new GostBlockungsdatenManager(dtoMapper.apply(blockung), faecherManager);
		
		// Schienen hinzufügen.
		List<DTOGostBlockungSchiene> schienen = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", blockung.ID, DTOGostBlockungSchiene.class);
		for (DTOGostBlockungSchiene schiene : schienen)
			manager.addSchiene(DataGostBlockungSchiene.dtoMapper.apply(schiene));
		
		// Kurse hinzufügen.
		List<DTOGostBlockungKurs> kurse = conn.queryNamed("DTOGostBlockungKurs.blockung_id", blockung.ID, DTOGostBlockungKurs.class);
		for (DTOGostBlockungKurs kurs : kurse)
			manager.addKurs(DataGostBlockungKurs.dtoMapper.apply(kurs));

		// Kurs-Lehrer hinzufügen
		List<Long> kursIDs = kurse.stream().map(k -> k.ID).toList();
		if (kursIDs.size() > 0) {
			List<DTOGostBlockungKurslehrer> kurslehrerListe = conn.queryNamed("DTOGostBlockungKurslehrer.blockung_kurs_id.multiple", kursIDs, DTOGostBlockungKurslehrer.class);
			List<Long> kurslehrerIDs = kurslehrerListe.stream().map(kl -> kl.Lehrer_ID).distinct().toList();
			if (kurslehrerIDs.size() > 0) {
				Map<Long, DTOLehrer> mapLehrer = conn.queryNamed("DTOLehrer.id.multiple", kurslehrerIDs, DTOLehrer.class)
						.stream().collect(Collectors.toMap(l -> l.ID, l -> l));
				for (DTOGostBlockungKurslehrer kurslehrer : kurslehrerListe) {
					DTOLehrer lehrer = mapLehrer.get(kurslehrer.Lehrer_ID);
					if (lehrer == null)
						throw OperationError.NOT_FOUND.exception();
					GostBlockungKursLehrer kl = new GostBlockungKursLehrer();
					kl.id = lehrer.ID;
					kl.kuerzel = lehrer.Kuerzel;
					kl.vorname = lehrer.Vorname;
					kl.nachname = lehrer.Nachname;
					kl.istExtern = (lehrer.StammschulNr != null);
					kl.reihenfolge = kurslehrer.Reihenfolge;
					kl.wochenstunden = kurslehrer.Wochenstunden;
					manager.patchOfKursAddLehrkraft(kurslehrer.Blockung_Kurs_ID, kl);
				}
			}
		}

		// Regeln hinzufügen.
		List<DTOGostBlockungRegel> regeln = conn.queryNamed("DTOGostBlockungRegel.blockung_id", blockung.ID, DTOGostBlockungRegel.class);
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
		
		// Schüler-Menge hinzufügen.
        List<Schueler> schueler = (new DataGostJahrgangSchuelerliste(conn, blockung.Abi_Jahrgang)).getSchuelerDTOs().stream().map((DTOSchueler dto) -> {
            Schueler daten = new Schueler();
            daten.id = dto.ID;
            daten.nachname = dto.Nachname;
            daten.vorname = dto.Vorname;
            daten.geschlecht = dto.Geschlecht.id;
            return daten;
        }).collect(Collectors.toList());
        manager.addSchuelerListe(schueler);
        
        // Schüler-Fachwahl-Menge hinzufügen.
        List<GostFachwahl> fachwahlen = (new DataGostAbiturjahrgangFachwahlen(conn, blockung.Abi_Jahrgang)).getSchuelerFachwahlen(blockung.Halbjahr);
        manager.addFachwahlListe(fachwahlen);
        
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
            daten = manager.daten();
			// Ergänze Blockungsliste
			(new DataGostBlockungsergebnisse(conn)).getErgebnisListe(manager);
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
						if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
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
			// Lese die Fächer der gymnasialen Oberstufe ein. Diese müssen für den Abiturjahrgang vorhanden sein, damit eine Blockung angelegt werden darf
			GostFaecherManager faecherManager = (new DataGostFaecher(conn, abiturjahr)).getListInternal();
			if (faecherManager == null)
				return OperationError.NOT_FOUND.getResponse();
			// Lege ein "leeres" Ergebnis für manuelles Blocken an
			DTODBAutoInkremente lastErgebnisID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Zwischenergebnisse");
			long ergebnisID = lastErgebnisID == null ? 1 : lastErgebnisID.MaxID + 1;
			DTOGostBlockungZwischenergebnis erg = new DTOGostBlockungZwischenergebnis(ergebnisID, blockungID, false, false);
			// Blockung anlegen
			DTOGostBlockung blockung = new DTOGostBlockung(blockungID, name, abiturjahr, gostHalbjahr, false);
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
				manager.addSchiene(DataGostBlockungSchiene.dtoMapper.apply(schiene));
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
					DTOGostBlockungKurs kurs = new DTOGostBlockungKurs(++kurseID, blockungID, fw.id, GostKursart.LK, i, false, 1, 5);
					conn.transactionPersist(kurs);
					manager.addKurs(DataGostBlockungKurs.dtoMapper.apply(kurs));
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
					DTOGostBlockungKurs kurs = new DTOGostBlockungKurs(++kurseID, blockungID, fw.id, kursart, i, false, 1, wstd);
					conn.transactionPersist(kurs);
					manager.addKurs(DataGostBlockungKurs.dtoMapper.apply(kurs));
				}
				for (int i = 1; i <= anzahlZK; i++) {
					DTOGostBlockungKurs kurs = new DTOGostBlockungKurs(++kurseID, blockungID, fw.id, GostKursart.ZK, i, false, 1, 3);
					conn.transactionPersist(kurs);
					manager.addKurs(DataGostBlockungKurs.dtoMapper.apply(kurs));
				}
			}
			// Lege eine Kurs-Schienen-Zuordnung für das "leere" Ergebnis fest. Diese Kurse werden der ersten Schiene der neuen Blockung zugeordnet.
			for (GostBlockungKurs kurs : manager.daten().kurse)
				conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchiene(ergebnisID, kurs.id, schienenID + 1));
            // Bestimme die Fachwahlen aus der DB
            daten.fachwahlen.addAll((new DataGostAbiturjahrgangFachwahlen(conn, daten.abijahrgang)).getSchuelerFachwahlen(gostHalbjahr));
            // Ergänze Blockungsliste
            conn.transactionCommit();
            conn.transactionBegin();
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

	
	private static synchronized List<Long> schreibeErgebnisse(DBEntityManager conn, long id, List<GostBlockungsergebnisManager> outputs) {
		try {
			Vector<Long> ergebnisse = new Vector<>();
			conn.transactionBegin();
			DTODBAutoInkremente lastID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Zwischenergebnisse");
			long ergebnisID = lastID == null ? 1 : lastID.MaxID + 1;
			for (GostBlockungsergebnisManager output : outputs) {
				// Schreibe das Ergebnis in die Datenbank.
				DTOGostBlockungZwischenergebnis erg = new DTOGostBlockungZwischenergebnis(ergebnisID, id, false, false);
				conn.transactionPersist(erg);
				
				// Kurse <--> Schüler
				HashMap<Long, HashSet<@NotNull Long>> map_KursID_SchuelerIDs = output.getMappingKursIDSchuelerIDs(); 
				for (long kursID : map_KursID_SchuelerIDs.keySet()) 
					for (long schuelerID : map_KursID_SchuelerIDs.get(kursID)) 
						conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchueler(ergebnisID, kursID, schuelerID));
				
				// Kurse <--> Schienen
				HashMap<Long, HashSet<@NotNull GostBlockungsergebnisSchiene>> map_KursID_SchienenIDs = output.getMappingKursIDSchienenmenge(); 
				for (long kursID : map_KursID_SchienenIDs.keySet()) 
					for (@NotNull GostBlockungsergebnisSchiene schiene : map_KursID_SchienenIDs.get(kursID)) 
						conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchiene(ergebnisID, kursID, schiene.id));
				
				// Ergänze die ID bei der Liste der berechneten Ergebnisse
				ergebnisse.add(ergebnisID);
				ergebnisID++;
			}			
			if (conn.transactionCommit())
				return ergebnisse;
			throw OperationError.INTERNAL_SERVER_ERROR.exception("\"Fehler beim Schreiben der Zwischenergebnisse für die Kursblockung.\"");
		} catch (Exception exception) {
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
	public Response berechne(long id, long zeit) {
		// Erzeuge den Input für den Kursblockungsalgorithmus
		GostBlockungsdatenManager manager = getBlockungsdatenManagerFromDB(id);
		manager.setMaxTimeMillis(zeit);
		
		KursblockungAlgorithmus algo = new KursblockungAlgorithmus();
		
		Vector<GostBlockungsergebnisManager> outputs;
		
		try {
			outputs = algo.handle(manager);
		} catch (Exception exception) {
			return OperationError.INTERNAL_SERVER_ERROR.getResponse(exception);
		}
		
		try {
			List<Long> ergebnisse = schreibeErgebnisse(conn, id, outputs);
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(ergebnisse).build();
		} catch (Exception exception) {
			if (exception instanceof WebApplicationException webex)
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
	public Response dupliziere(long idErgebnisOriginal) {
		try {
			conn.transactionBegin();
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Bestimme die Blockung und das zugehörige Ergebnis
			DTOGostBlockung blockungOriginal;
			DTOGostBlockungZwischenergebnis ergebnisOriginal = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idErgebnisOriginal);
			if (ergebnisOriginal == null)
				throw OperationError.NOT_FOUND.exception();
			// Bestimme die Blockung			
			blockungOriginal = conn.queryByKey(DTOGostBlockung.class, ergebnisOriginal.Blockung_ID);
			if (blockungOriginal == null)
				throw OperationError.NOT_FOUND.exception();
			// Bestimme die ID für das Duplikat der Blockung
			DTODBAutoInkremente lastID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung");
			Long idBlockungDuplikat = lastID == null ? 1 : lastID.MaxID + 1;
			// Bestimme die ID für das Vorlage-Ergebnis der duplizierten Blockung
			DTODBAutoInkremente dbErgebnisID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Zwischenergebnisse");
			long idErgebnisDuplikat = dbErgebnisID == null ? 1 : dbErgebnisID.MaxID + 1;
			// Bestimme den Namen der neuen Blockung
			List<DTOGostBlockung> blockungen = conn.queryList("SELECT e FROM DTOGostBlockung e WHERE e.Abi_Jahrgang = ?1 and e.Halbjahr = ?2", DTOGostBlockung.class, blockungOriginal.Abi_Jahrgang, blockungOriginal.Halbjahr);
			Set<String> namen = blockungen.stream().map(b -> b.Name).collect(Collectors.toUnmodifiableSet());
			String trimmedName = blockungOriginal.Name.replaceAll("\\d+$", "").stripTrailing();
			int nameIndex = 1;
			while (namen.contains(trimmedName + " " + nameIndex))
				nameIndex++;
			String name = trimmedName + " " + nameIndex;
			// Erstelle das Duplikat
			DTOGostBlockung blockungDuplikat = new DTOGostBlockung(idBlockungDuplikat, name, blockungOriginal.Abi_Jahrgang, blockungOriginal.Halbjahr, false);
			conn.transactionPersist(blockungDuplikat);
			// Dupliziere die Schienen
			DTODBAutoInkremente dbSchienenID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Schienen");
			long idSchieneDuplikat = dbSchienenID == null ? 0 : dbSchienenID.MaxID + 1;
			HashMap<Long, Long> mapSchienenIDs = new HashMap<>();
			List<DTOGostBlockungSchiene> schienenOriginal = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", ergebnisOriginal.Blockung_ID,
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
			List<DTOGostBlockungKurs> kurseOriginal = conn.queryNamed("DTOGostBlockungKurs.blockung_id", ergebnisOriginal.Blockung_ID,
					DTOGostBlockungKurs.class);
			List<Long> kursIDsOriginal = kurseOriginal.stream().map(k -> k.ID).collect(Collectors.toList());
			for (DTOGostBlockungKurs kursOriginal : kurseOriginal) {
				DTOGostBlockungKurs kursDuplikat = new DTOGostBlockungKurs(idKursDuplikat, idBlockungDuplikat, kursOriginal.Fach_ID,
						kursOriginal.Kursart, kursOriginal.Kursnummer, kursOriginal.IstKoopKurs, kursOriginal.Schienenanzahl, kursOriginal.Wochenstunden);
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
			HashMap<Long, GostKursblockungRegelTyp> mapRegelTypen = new HashMap<>(); // Die Typen für die neuen Regel-IDs
			List<DTOGostBlockungRegel> regelnOriginal = conn.queryNamed("DTOGostBlockungRegel.blockung_id", ergebnisOriginal.Blockung_ID,
					DTOGostBlockungRegel.class);
			List<Long> regelIDsOriginal = regelnOriginal.stream().map(k -> k.ID).collect(Collectors.toList());
			for (DTOGostBlockungRegel regelOriginal : regelnOriginal) {
			    mapRegelTypen.put(idRegelDuplikat, regelOriginal.Typ);
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
					// Passe den Parameter an...
					GostKursblockungRegelTyp typ = mapRegelTypen.get(idRegelDuplikat);
					GostKursblockungRegelParameterTyp paramTyp = typ.getParamType(paramOriginal.Nummer);
					Long paramValue = switch(paramTyp) {
                        case KURSART -> paramOriginal.Parameter;
                        case KURS_ID -> mapKursIDs.get(paramOriginal.Parameter); 
                        case SCHIENEN_NR -> paramOriginal.Parameter;
                        case SCHUELER_ID -> paramOriginal.Parameter;
                        default -> paramOriginal.Parameter;
					};
					DTOGostBlockungRegelParameter paramDuplikat = new DTOGostBlockungRegelParameter(idRegelDuplikat, 
							paramOriginal.Nummer, paramValue);
					conn.transactionPersist(paramDuplikat);
				}
			}
			// Dupliziere das Zwischenergebnis und markiere es als Duplikat
			DTOGostBlockungZwischenergebnis ergebnisDuplikat = new DTOGostBlockungZwischenergebnis(
					idErgebnisDuplikat, idBlockungDuplikat, ergebnisOriginal.IstMarkiert, false);
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
	 * Erzeugt ein Duplikat der Blockung des angegebenen Ergebnis und des Ergebnisses
	 * selber und schreibt dieses direkt in das nächste Halbjahr hoch. Wird diese
	 * Methode auf eine Blockung der Q2.2 ausgeführt, so wird eine Fehlermeldung erzeugt
	 * 
	 * @param idErgebnisOriginal   das hochzuschreibende Blockungsergebnis
	 * 
	 * @return die Blockungsdaten der hochgeschriebenen Blockung
	 */
	public Response hochschreiben(long idErgebnisOriginal) {
		try {
			conn.transactionBegin();
			GostUtils.pruefeSchuleMitGOSt(conn);
			// Bestimme die Blockung und das zugehörige Ergebnis
			DTOGostBlockung blockungOriginal;
			DTOGostBlockungZwischenergebnis ergebnisOriginal = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idErgebnisOriginal);
			if (ergebnisOriginal == null)
				throw OperationError.NOT_FOUND.exception();
			// Bestimme die Blockung			
			blockungOriginal = conn.queryByKey(DTOGostBlockung.class, ergebnisOriginal.Blockung_ID);
			if (blockungOriginal == null)
				throw OperationError.NOT_FOUND.exception();
			if (blockungOriginal.Halbjahr == GostHalbjahr.Q22)   // Blockungen der Q2.2 können nicht hochgeschrieben werden...
				throw OperationError.BAD_REQUEST.exception();
			// Bestimme die ID für die hochgeschriebene Blockung
			DTODBAutoInkremente lastID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung");
			Long idBlockungDuplikat = lastID == null ? 1 : lastID.MaxID + 1;
			// Bestimme die ID für das Vorlage-Ergebnis der hochgeschriebenen Blockung
			DTODBAutoInkremente dbErgebnisID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Zwischenergebnisse");
			long idErgebnisDuplikat = dbErgebnisID == null ? 1 : dbErgebnisID.MaxID + 1;
			// Bestimme den Namen der neuen Blockung
			String name = blockungOriginal.Name + " - hochgeschrieben von Ergebnis " + idErgebnisOriginal + ")";
			// Erstelle die Hochgeschriebene Blockung
			DTOGostBlockung blockungDuplikat = new DTOGostBlockung(idBlockungDuplikat, name, blockungOriginal.Abi_Jahrgang, blockungOriginal.Halbjahr.next(), false);
			conn.transactionPersist(blockungDuplikat);
			// Dupliziere die Schienen
			DTODBAutoInkremente dbSchienenID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Schienen");
			long idSchieneDuplikat = dbSchienenID == null ? 0 : dbSchienenID.MaxID + 1;
			HashMap<Long, Long> mapSchienenIDs = new HashMap<>();
			List<DTOGostBlockungSchiene> schienenOriginal = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", ergebnisOriginal.Blockung_ID,
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
			HashMap<Long, DTOGostBlockungKurs> mapKurseHochgeschrieben = new HashMap<>();
			List<DTOGostBlockungKurs> kurseOriginal = conn.queryNamed("DTOGostBlockungKurs.blockung_id", ergebnisOriginal.Blockung_ID,
					DTOGostBlockungKurs.class);
			List<Long> kursIDsOriginal = kurseOriginal.stream().map(k -> k.ID).collect(Collectors.toList());
			for (DTOGostBlockungKurs kursOriginal : kurseOriginal) {
				DTOGostBlockungKurs kursDuplikat = new DTOGostBlockungKurs(idKursDuplikat, idBlockungDuplikat, kursOriginal.Fach_ID,
						kursOriginal.Kursart, kursOriginal.Kursnummer, kursOriginal.IstKoopKurs, kursOriginal.Schienenanzahl, kursOriginal.Wochenstunden);
				kursDuplikat.BezeichnungSuffix = kursOriginal.BezeichnungSuffix;
				mapKursIDs.put(kursOriginal.ID, kursDuplikat.ID);
				mapKurseHochgeschrieben.put(kursDuplikat.ID, kursDuplikat);
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
			HashMap<Long, GostKursblockungRegelTyp> mapRegelTypen = new HashMap<>(); // Die Typen für die neuen Regel-IDs
			List<DTOGostBlockungRegel> regelnOriginal = conn.queryNamed("DTOGostBlockungRegel.blockung_id", ergebnisOriginal.Blockung_ID,
					DTOGostBlockungRegel.class);
			List<Long> regelIDsOriginal = regelnOriginal.stream().map(k -> k.ID).collect(Collectors.toList());
			for (DTOGostBlockungRegel regelOriginal : regelnOriginal) {
			    mapRegelTypen.put(idRegelDuplikat, regelOriginal.Typ);
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
					// Passe den Parameter an...
					GostKursblockungRegelTyp typ = mapRegelTypen.get(idRegelDuplikat);
					GostKursblockungRegelParameterTyp paramTyp = typ.getParamType(paramOriginal.Nummer);
					Long paramValue = switch(paramTyp) {
                        case KURSART -> paramOriginal.Parameter;
                        case KURS_ID -> mapKursIDs.get(paramOriginal.Parameter); 
                        case SCHIENEN_NR -> paramOriginal.Parameter;
                        case SCHUELER_ID -> paramOriginal.Parameter;
                        default -> paramOriginal.Parameter;
					};
					DTOGostBlockungRegelParameter paramDuplikat = new DTOGostBlockungRegelParameter(idRegelDuplikat, 
							paramOriginal.Nummer, paramValue);
					conn.transactionPersist(paramDuplikat);
				}
			}
			// Dupliziere das Zwischenergebnis und markiere es als Duplikat
			DTOGostBlockungZwischenergebnis ergebnisDuplikat = new DTOGostBlockungZwischenergebnis(
					idErgebnisDuplikat, idBlockungDuplikat, ergebnisOriginal.IstMarkiert, false);
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
			// Ermittle die Fachwahlen des Abiturjahrgangs
			GostFachwahlManager managerFachwahlen = (new DataGostAbiturjahrgangFachwahlen(conn, blockungDuplikat.Abi_Jahrgang)).getFachwahlManager(blockungDuplikat.Halbjahr);
			// Dupliziere Kurs-Schüler-Zuordnung
			List<DTOGostBlockungZwischenergebnisKursSchueler> zuordnungKursSchuelerListeOriginal = conn.queryNamed("DTOGostBlockungZwischenergebnisKursSchueler.zwischenergebnis_id", idErgebnisOriginal,
					DTOGostBlockungZwischenergebnisKursSchueler.class);
			for (DTOGostBlockungZwischenergebnisKursSchueler zuordnungKursSchuelerOriginal : zuordnungKursSchuelerListeOriginal) {
				idKursDuplikat = mapKursIDs.get(zuordnungKursSchuelerOriginal.Blockung_Kurs_ID);
				DTOGostBlockungKurs kurs = mapKurseHochgeschrieben.get(idKursDuplikat);
				// Prüfe Fachwahlen
				if (managerFachwahlen.hatFachwahl(zuordnungKursSchuelerOriginal.Schueler_ID, kurs.Fach_ID, kurs.Kursart)) {
					DTOGostBlockungZwischenergebnisKursSchueler zuordnungKursSchuelerDuplikat = new DTOGostBlockungZwischenergebnisKursSchueler(
						idErgebnisDuplikat, idKursDuplikat, zuordnungKursSchuelerOriginal.Schueler_ID);
					conn.transactionPersist(zuordnungKursSchuelerDuplikat);
				}
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
	
}
