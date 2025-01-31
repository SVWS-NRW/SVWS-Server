package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursAufteilung;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchiene;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchueler;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostBlockungKurs}.
 */
public final class DataGostBlockungKurs extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBlockungKurs}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostBlockungKurs(final DBEntityManager conn) {
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
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOGostBlockungKurs} in einen Core-DTO {@link GostBlockungKurs}.
	 */
	public static final Function<DTOGostBlockungKurs, GostBlockungKurs> dtoMapper = (final DTOGostBlockungKurs kurs) -> {
		final GostBlockungKurs daten = new GostBlockungKurs();
		daten.id = kurs.ID;
		daten.fach_id = kurs.Fach_ID;
		daten.kursart = kurs.Kursart.id;
		daten.nummer = kurs.Kursnummer;
		daten.istKoopKurs = kurs.IstKoopKurs;
		daten.suffix = (kurs.BezeichnungSuffix == null) ? "" : kurs.BezeichnungSuffix;
		daten.anzahlSchienen = kurs.Schienenanzahl;
		daten.wochenstunden = kurs.Wochenstunden;
		return daten;
	};


	@Override
	public Response get(final Long id) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme den Kurs der Blockung
		final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, id);
		if (kurs == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final GostBlockungKurs daten = dtoMapper.apply(kurs);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Passt die Anzahl der Schienen für den angegebenen Kurs auf die übergebene Schienenzahl an
	 *
	 * @param kurs             der Kurs
	 * @param idErgebnis       die ID des Vorlagen-Ergebnis der Blockung
	 * @param schienenAnzahl   die neue Schienenzahl
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private void updateSchienenAnzahl(final DTOGostBlockungKurs kurs, final long idErgebnis, final int schienenAnzahl) throws ApiOperationException {
		if (schienenAnzahl == kurs.Schienenanzahl)
			return;
		// Bestimme die Schienen der Blockung und sortiere die nach der Schienennummer
		List<DTOGostBlockungSchiene> schienen = conn.queryList(DTOGostBlockungSchiene.QUERY_BY_BLOCKUNG_ID, DTOGostBlockungSchiene.class, kurs.Blockung_ID);
		if ((schienenAnzahl < 1) || (schienenAnzahl > schienen.size()))
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Anzahl der Schienen für den Kurs ist entweder < 1 oder größer als die Anzahl der verfügbaren Schienen.");
		// Bestimme die aktuelle Schienenzuordnungen des Kurses
		final List<DTOGostBlockungZwischenergebnisKursSchiene> zuordnungen =
				conn.queryList("SELECT e FROM DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Zwischenergebnis_ID = ?1 AND e.Blockung_Kurs_ID = ?2",
						DTOGostBlockungZwischenergebnisKursSchiene.class, idErgebnis, kurs.ID);
		if (schienenAnzahl > kurs.Schienenanzahl) {
			final Set<Long> kursSchienenIDs = zuordnungen.stream().map(z -> z.Schienen_ID).collect(Collectors.toSet());
			// Erhöhe die Anzahl der Schienen solange mit den nächsten freien Schienen bis die geforderte Schienenanzahl erreicht wurde
			schienen = schienen.stream().sorted((a, b) -> Integer.compare(a.Nummer, b.Nummer)).toList();
			for (final DTOGostBlockungSchiene schiene : schienen) {
				if (kursSchienenIDs.contains(schiene.ID))
					continue;
				conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchiene(idErgebnis, kurs.ID, schiene.ID));
				kursSchienenIDs.add(schiene.ID);
				kurs.Schienenanzahl++;
				if (kurs.Schienenanzahl == schienenAnzahl)
					break;
			}
		} else if (schienenAnzahl < kurs.Schienenanzahl) {
			final Map<Long, DTOGostBlockungZwischenergebnisKursSchiene> mapKursSchienen =
					zuordnungen.stream().collect(Collectors.toMap(z -> z.Schienen_ID, z -> z));
			// Reduziere die Anzahl der Schienen solange mit den hinteren Schienennummern bis die geforderte Schienenanzahl erreicht wurde
			schienen = schienen.stream().sorted((a, b) -> (-1) * Integer.compare(a.Nummer, b.Nummer)).toList();
			for (final DTOGostBlockungSchiene schiene : schienen) {
				final DTOGostBlockungZwischenergebnisKursSchiene zuordnung = mapKursSchienen.get(schiene.ID);
				if (zuordnung == null)
					continue;
				conn.transactionRemove(zuordnung);
				mapKursSchienen.remove(schiene.ID);
				kurs.Schienenanzahl--;
				if (kurs.Schienenanzahl == schienenAnzahl)
					break;
			}
		}
	}


	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() <= 0)
			return Response.status(Status.OK).build();
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme den Kurs der Blockung
		final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, id);
		if (kurs == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		// Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, kurs.Blockung_ID);
		final DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
		for (final Entry<String, Object> entry : map.entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			switch (key) {
				case "id" -> {
					final Long patch_id = JSONMapper.convertToLong(value, true);
					if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
						throw new ApiOperationException(Status.BAD_REQUEST);
				}
				case "fach_id" -> {
					final Long patch_fach_id = JSONMapper.convertToLong(value, true);
					if ((patch_fach_id == null) || (patch_fach_id.longValue() != kurs.Fach_ID))
						throw new ApiOperationException(Status.BAD_REQUEST);
				}
				case "kursart" -> {
					final Integer patch_kursart = JSONMapper.convertToInteger(value, true);
					if ((patch_kursart == null) || (patch_kursart.intValue() != kurs.Kursart.id))
						throw new ApiOperationException(Status.BAD_REQUEST);
				}
				case "nummer" -> {
					final Integer patch_nummer = JSONMapper.convertToInteger(value, true);
					if ((patch_nummer == null) || (patch_nummer.intValue() != kurs.Kursnummer))
						throw new ApiOperationException(Status.BAD_REQUEST);
				}
				case "istKoopKurs" -> kurs.IstKoopKurs = JSONMapper.convertToBoolean(value, false);
				case "suffix" ->
					kurs.BezeichnungSuffix = JSONMapper.convertToString(value, false, true, Schema.tab_Gost_Blockung_Kurse.col_BezeichnungSuffix.datenlaenge());
				case "anzahlSchienen" -> {
					if (vorlage == null)
						throw new ApiOperationException(Status.BAD_REQUEST,
								"Der Kurs kann nicht angepasst werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
					final int schienenAnzahl = JSONMapper.convertToInteger(value, false);
					updateSchienenAnzahl(kurs, vorlage.ID, schienenAnzahl);
				}
				case "wochenstunden" -> {
					if (vorlage == null)
						throw new ApiOperationException(Status.BAD_REQUEST,
								"Der Kurs kann nicht angepasst werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
					kurs.Wochenstunden = JSONMapper.convertToInteger(value, false);
					if ((kurs.Wochenstunden < 1) || (kurs.Wochenstunden > 40))
						throw new ApiOperationException(Status.BAD_REQUEST);
				}
				default -> throw new ApiOperationException(Status.BAD_REQUEST);
			}
		}
		conn.transactionPersist(kurs);
		return Response.status(Status.OK).build();
	}


	/**
	 * Fügt einen weiteren Kurses zu einer Blockung der Gymnasialen Oberstufe hinzu
	 *
	 * @param idBlockung   die ID der Blockung
	 * @param idFach       die ID des Faches
	 * @param idKursart    die ID der Kursart
	 *
	 * @return Eine Response mit der ID des neuen Kurses der Blockung
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response addKurs(final long idBlockung, final long idFach, final int idKursart) throws ApiOperationException {
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Prüfe, ob die Blockung mit der ID existiert
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
		if (blockung == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		// Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
		final DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
		if (vorlage == null)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Kurs kann nicht hinzugefügt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
		// Bestimme das Fach und prüfe, ob es ein Fach der gymnasialen Oberstufe ist
		final DTOFach fach = conn.queryByKey(DTOFach.class, idFach);
		if (fach == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final Fach fachStatistik = Fach.getBySchluesselOrDefault(fach.StatistikKuerzel);
		if ((fachStatistik == Fach.VF) || (!fach.IstOberstufenFach))
			throw new ApiOperationException(Status.CONFLICT, "Es handelt sich nicht um ein Fach der gymnasialen Oberstufe.");
		// Bestimme die Kursart
		GostKursart kursart = GostKursart.fromID(idKursart);
		if (kursart == GostKursart.GK) {  // Korrigiere ggf. für Vertiefungs- und Projektkurse
			if (fachStatistik == Fach.VX)
				kursart = GostKursart.VTF;
			else if (fachStatistik == Fach.PX)
				kursart = GostKursart.PJK;
		}
		// Bestimme die ID, für welche der Datensatz eingefügt wird
		final DTOSchemaAutoInkremente dbKurseID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Kurse");
		final long idKurs = (dbKurseID == null) ? 1 : (dbKurseID.MaxID + 1);
		// Ermittle, ob bereits Kurse mit für das Fach und die Kursart existieren
		final String jpql = "SELECT e FROM DTOGostBlockungKurs e WHERE e.Blockung_ID = ?1 and e.Fach_ID = ?2 and e.Kursart = ?3";
		final List<DTOGostBlockungKurs> kurse = conn.queryList(jpql, DTOGostBlockungKurs.class, idBlockung, idFach, kursart);
		int kursnummer = 1;
		if ((kurse != null) && (!kurse.isEmpty())) { // Bestimme die erste freie Kursnummer
			final Set<Integer> kursIDs = kurse.stream().map(e -> e.Kursnummer).collect(Collectors.toSet());
			while (kursIDs.contains(kursnummer))
				kursnummer++;
		}
		DTOGostBlockungKurs kurs = null;
		if (kursart == GostKursart.LK) {
			kurs = new DTOGostBlockungKurs(idKurs, idBlockung, idFach, kursart, kursnummer, false, 1, 5);
		} else if (kursart == GostKursart.GK) {
			kurs = new DTOGostBlockungKurs(idKurs, idBlockung, idFach, kursart, kursnummer, false, 1, fach.IstMoeglichAlsNeueFremdspracheInSekII ? 4 : 3);
		} else if (kursart == GostKursart.PJK) {
			// TODO Wochenstunden anhand der Tabelle GostFaecher bestimmen (PJK)
			kurs = new DTOGostBlockungKurs(idKurs, idBlockung, idFach, kursart, kursnummer, false, 1, 3);
		} else if (kursart == GostKursart.VTF) {
			kurs = new DTOGostBlockungKurs(idKurs, idBlockung, idFach, kursart, kursnummer, false, 1, 2);
		} else if (kursart == GostKursart.ZK) {
			kurs = new DTOGostBlockungKurs(idKurs, idBlockung, idFach, kursart, kursnummer, false, 1, 3);
		}
		conn.transactionPersist(kurs);
		conn.transactionFlush();
		// Füge den Kurs in die erste Schiene des Zwischenergebnisses ein
		final List<DTOGostBlockungSchiene> schienen = conn.queryList("SELECT e FROM DTOGostBlockungSchiene e WHERE e.Blockung_ID = ?1 AND e.Nummer = ?2",
				DTOGostBlockungSchiene.class, blockung.ID, 1);
		if (schienen.size() != 1)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		final DTOGostBlockungZwischenergebnisKursSchiene ks = new DTOGostBlockungZwischenergebnisKursSchiene(vorlage.ID, idKurs, schienen.get(0).ID);
		conn.transactionPersist(ks);
		conn.transactionFlush();
		final GostBlockungKurs daten = dtoMapper.apply(kurs);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs {@link DTOSchueler}.
	 */
	private static final Comparator<DTOSchueler> dtoSchuelerComparator = (a, b) -> {
		final Collator collator = Collator.getInstance(Locale.GERMAN);
		if ((a.Nachname == null) && (b.Nachname != null))
			return -1;
		else if ((a.Nachname != null) && (b.Nachname == null))
			return 1;
		else if ((a.Nachname == null) && (b.Nachname == null))
			return 0;
		int result = collator.compare(a.Nachname, b.Nachname);
		if (result != 0)
			return result;
		if ((a.Vorname == null) && (b.Vorname != null))
			return -1;
		else if ((a.Vorname != null) && (b.Vorname == null))
			return 1;
		else if ((a.Vorname == null) && (b.Vorname == null))
			return 0;
		result = collator.compare(a.Vorname, b.Vorname);
		if (result != 0)
			return result;
		return Long.compare(a.ID, b.ID);
	};


	/**
	 * Teilt einen Kurs in zwei Kurse auf. Beide Kurse liegen
	 * danach in der gleichen Schiene. Eine evtl. zugeordnete Schülermenge
	 * wird zwischen den Kursen geteilt.
	 *
	 * @param idKurs   die ID des zu teilenden Kurses
	 *
	 * @return eine HTTP-Response mit einem Array mit den beiden resultierenden Kursen
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response splitKurs(final long idKurs) throws ApiOperationException {
		// Bestimme den Kurs der Blockung
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, idKurs);
		if (kurs == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		// Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, kurs.Blockung_ID);
		final DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
		if (vorlage == null)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Kurs kann nicht aufgeteilt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
		// Bestimme die erste freie Kursnummer
		final List<DTOGostBlockungKurs> kurse =
				conn.queryList("SELECT e FROM DTOGostBlockungKurs e WHERE e.Blockung_ID = ?1 AND e.Fach_ID = ?2 AND e.Kursart = ?3", DTOGostBlockungKurs.class,
						kurs.Blockung_ID, kurs.Fach_ID, kurs.Kursart);
		if ((kurse == null) || (kurse.isEmpty()))
			throw new ApiOperationException(Status.NOT_FOUND);
		final Set<Integer> kursnummern = kurse.stream().map(k -> k.Kursnummer).collect(Collectors.toSet());
		int nummer = 1;
		while (kursnummern.contains(nummer))
			nummer++;
		// Bestimme die ID, für welche der Datensatz eingefügt wird
		final DTOSchemaAutoInkremente dbKurseID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Kurse");
		final long idKurs2 = (dbKurseID == null) ? 1 : (dbKurseID.MaxID + 1);
		// Lege den neuen Kurs an.
		final DTOGostBlockungKurs kursNeu = new DTOGostBlockungKurs(idKurs2, kurs.Blockung_ID, kurs.Fach_ID,
				kurs.Kursart, nummer, kurs.IstKoopKurs, kurs.Schienenanzahl, kurs.Wochenstunden);
		if (!conn.transactionPersist(kursNeu))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		conn.transactionFlush();
		// Passe nun die zugeordneten Schienen an und ordne die Hälfte der Schüler dem zweiten Kurs zu
		final List<DTOGostBlockungZwischenergebnisKursSchiene> schienen = conn.queryList(
				DTOGostBlockungZwischenergebnisKursSchiene.QUERY_BY_BLOCKUNG_KURS_ID, DTOGostBlockungZwischenergebnisKursSchiene.class, kurs.ID);
		for (final DTOGostBlockungZwischenergebnisKursSchiene schiene : schienen)
			conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchiene(schiene.Zwischenergebnis_ID, kursNeu.ID, schiene.Schienen_ID));
		conn.transactionFlush();
		final List<DTOGostBlockungZwischenergebnisKursSchueler> schuelerListe = conn.queryList(
				DTOGostBlockungZwischenergebnisKursSchueler.QUERY_BY_BLOCKUNG_KURS_ID, DTOGostBlockungZwischenergebnisKursSchueler.class, kurs.ID);
		final Map<Long, DTOGostBlockungZwischenergebnisKursSchueler> mapKursSchueler =
				schuelerListe.stream().collect(Collectors.toMap(s -> s.Schueler_ID, s -> s));
		final List<Long> schuelerIDs = schuelerListe.stream().map(s -> s.Schueler_ID).toList();
		final List<DTOSchueler> listSchuelerDTOs = schuelerIDs.isEmpty() ? new ArrayList<>() : conn.queryByKeyList(DTOSchueler.class, schuelerIDs);
		final Map<Long, DTOSchueler> mapSchuelerDTOs = listSchuelerDTOs.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		for (final DTOGostBlockungZwischenergebnisKursSchueler schueler : schuelerListe) { // Prüfe die Konsistenz der Daten in der Datenbank
			final DTOSchueler schuelerDTO = mapSchuelerDTOs.get(schueler.Schueler_ID);
			if (schuelerDTO == null)
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Schüler mit der ID " + schueler.Schueler_ID + " nicht in der Datenbank gefunden.");
			final DTOGostBlockungZwischenergebnisKursSchueler kursSchueler = mapKursSchueler.get(schueler.Schueler_ID);
			if (kursSchueler == null)
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Unerwarteter interner Fehler bei dem erstellen einer Map.");
		}
		final List<DTOSchueler> listSchuelerDTOsSortiert = listSchuelerDTOs.stream().sorted(dtoSchuelerComparator).toList();
		for (int i = listSchuelerDTOsSortiert.size() / 2; i < listSchuelerDTOsSortiert.size(); i++) {
			final DTOSchueler schuelerDTO = listSchuelerDTOsSortiert.get(i);
			final DTOGostBlockungZwischenergebnisKursSchueler schueler = mapKursSchueler.get(schuelerDTO.ID);
			conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchueler(schueler.Zwischenergebnis_ID, kursNeu.ID, schueler.Schueler_ID));
			conn.transactionRemove(schueler);
		}
		conn.transactionFlush();
		// Gebe die beiden Kurse zurück
		final GostBlockungKursAufteilung daten = new GostBlockungKursAufteilung();
		daten.kurs1 = dtoMapper.apply(kurs);
		daten.kurs2 = dtoMapper.apply(kursNeu);
		for (int i = 0; i < (listSchuelerDTOsSortiert.size() / 2); i++)
			daten.schueler1.add(listSchuelerDTOsSortiert.get(i).ID);
		for (int i = listSchuelerDTOsSortiert.size() / 2; i < listSchuelerDTOsSortiert.size(); i++)
			daten.schueler2.add(listSchuelerDTOsSortiert.get(i).ID);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Kombiniert zwei Kurse, sofern sie dem gleichen Fach und der gleichen
	 * Kursart zugeordnet sind.
	 *
	 * @param idKurs1   die ID des ersten Kurses
	 * @param idKurs2   die ID des zweiten Kurses
	 *
	 * @return eine HTTP-Response mit einem Array mit den beiden resultierenden Kursen
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response combineKurs(final long idKurs1, final long idKurs2) throws ApiOperationException {
		// Bestimme die Kurse der Blockung
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOGostBlockungKurs kurs1 = conn.queryByKey(DTOGostBlockungKurs.class, idKurs1);
		final DTOGostBlockungKurs kurs2 = conn.queryByKey(DTOGostBlockungKurs.class, idKurs2);
		if ((kurs1 == null) || (kurs2 == null))
			throw new ApiOperationException(Status.NOT_FOUND);
		// Prüfe, ob die Kurse zu der gleichen Blockung gehören
		if (kurs1.Blockung_ID != kurs2.Blockung_ID)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die beiden Kurse müssen zur gleichen Blockung gehören.");
		// Prüfe, ob das Fach übereinstimmt
		if (kurs1.Fach_ID != kurs2.Fach_ID)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Fächer der beiden Kurse müssen übereinstimmen.");
		// Prüfe, ob die Kursart übereinstimmt
		if (kurs1.Kursart != kurs2.Kursart)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Kursarten der beiden Kurse müssen übereinstimmen.");
		// Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, kurs1.Blockung_ID);
		final DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
		if (vorlage == null)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Kurs kann nicht aufgeteilt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
		// Verschiebe die Schüler des zweiten Kurses in den ersten Kurs
		final List<DTOGostBlockungZwischenergebnisKursSchueler> schuelerListe = conn.queryList(
				DTOGostBlockungZwischenergebnisKursSchueler.QUERY_BY_BLOCKUNG_KURS_ID, DTOGostBlockungZwischenergebnisKursSchueler.class, kurs2.ID);
		for (final DTOGostBlockungZwischenergebnisKursSchueler schueler : schuelerListe) {
			conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchueler(schueler.Zwischenergebnis_ID, kurs1.ID, schueler.Schueler_ID));
			conn.transactionFlush();
			conn.transactionRemove(schueler);
			conn.transactionFlush();
		}
		// Lösche dann den zweiten Kurs
		conn.transactionRemove(kurs2);
		conn.transactionFlush();
		DataGostBlockungRegel.updateKursRegelnOnDelete(conn, kurs2, kurs1);
		// Gebe den ersten Kurs zurück
		final GostBlockungKurs daten = dtoMapper.apply(kurs1);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
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
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response deleteKurs(final long idBlockung, final long idFach, final int idKursart) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
		final DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
		if (vorlage == null)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Kurs kann nicht entfernt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
		// Bestimme das Fach und prüfe, ob es ein Fach der gymnasialen Oberstufe ist
		final DTOFach fach = conn.queryByKey(DTOFach.class, idFach);
		if (fach == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final Fach fachStatistik = Fach.getBySchluesselOrDefault(fach.StatistikKuerzel);
		if ((fachStatistik == Fach.VF) || (!fach.IstOberstufenFach))
			throw new ApiOperationException(Status.CONFLICT, "Es handelt sich nicht um ein Fach der gymnasialen Oberstufe.");
		// Bestimme die Kursart
		GostKursart kursart = GostKursart.fromID(idKursart);
		if (kursart == GostKursart.GK) {
			if (fachStatistik == Fach.VX)
				kursart = GostKursart.VTF;
			if (fachStatistik == Fach.PX)
				kursart = GostKursart.PJK;
		}
		// Bestimme die Kurse der Blockung, welche das Kriterium erfüllen und löschen Kurs mit der höchsten Kursnummer
		final String jpql = "SELECT e FROM DTOGostBlockungKurs e WHERE e.Blockung_ID = ?1 and e.Fach_ID = ?2 and e.Kursart = ?3";
		final List<DTOGostBlockungKurs> kurse = conn.queryList(jpql, DTOGostBlockungKurs.class, idBlockung, idFach, kursart);
		if ((kurse == null) || (kurse.isEmpty()))
			throw new ApiOperationException(Status.NOT_FOUND);
		final Optional<DTOGostBlockungKurs> optKurs = kurse.stream().max((a, b) -> Integer.compare(a.Kursnummer, b.Kursnummer));
		if (optKurs.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOGostBlockungKurs kurs = optKurs.get();
		final GostBlockungKurs daten = dtoMapper.apply(kurs);
		if (!conn.transactionRemove(kurs))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		conn.transactionFlush();
		DataGostBlockungRegel.updateKursRegelnOnDelete(conn, kurs, null);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Löscht einen Kurs einer Blockung der Gymnasialen Oberstufe
	 *
	 * @param id   die ID des Kurses
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		// Bestimme den Kurs der Blockung
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, id);
		if (kurs == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		// Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, kurs.Blockung_ID);
		final DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
		if (vorlage == null)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Kurs kann nicht entfernt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
		// Entferne den Kurs
		final GostBlockungKurs daten = dtoMapper.apply(kurs);
		if (!conn.transactionRemove(kurs))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		conn.transactionFlush();
		DataGostBlockungRegel.updateKursRegelnOnDelete(conn, kurs, null);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Löscht mehrere Kurse einer Blockung der Gymnasialen Oberstufe
	 *
	 * @param ids   die ID der Kurse
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response deleteMultiple(final List<Long> ids) throws ApiOperationException {
		// Bestimme die Kurse der Blockung
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final List<DTOGostBlockungKurs> kurse = conn.queryByKeyList(DTOGostBlockungKurs.class, ids);
		final List<GostBlockungKurs> daten = new ArrayList<>();
		if (!kurse.isEmpty()) {
			// Prüfe, ob alle Kurs zur gleichen Blockung gehören
			final long idBlockung = kurse.get(0).Blockung_ID;
			for (final DTOGostBlockungKurs kurs : kurse)
				if (kurs.Blockung_ID != idBlockung)
					throw new ApiOperationException(Status.CONFLICT, "Die zu löschenden Kurse gehören nicht zur gleichen Blockung. Dies ist nicht zulässig");
			// Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
			final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
			final DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
			if (vorlage == null)
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die Kurse können nicht entfernt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
			// Entferne die Kurse
			for (final DTOGostBlockungKurs kurs : kurse) {
				daten.add(dtoMapper.apply(kurs));
				if (!conn.transactionRemove(kurs))
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
			}
		}
		conn.transactionFlush();
		for (final DTOGostBlockungKurs kurs : kurse)
			DataGostBlockungRegel.updateKursRegelnOnDelete(conn, kurs, null);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}
