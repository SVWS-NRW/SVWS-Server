package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.ArrayList;
import java.util.stream.Collectors;

import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostBelegpruefungsErgebnisse;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungDaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungDatenFachbelegung;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungDatenSchueler;
import de.svws_nrw.core.data.gost.GostSchuelerFachwahl;
import de.svws_nrw.core.data.schueler.Sprachbelegung;
import de.svws_nrw.core.data.schueler.Sprachpruefung;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kurse.ZulaessigeKursart;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangBeratungslehrer;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachkombinationen;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link Abiturdaten}.
 */
public final class DataGostSchuelerLaufbahnplanung extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Abiturdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostSchuelerLaufbahnplanung(final DBEntityManager conn) {
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

	@Override
	public Response get(final Long schueler_id) {
		if (schueler_id == null)
			return OperationError.NOT_FOUND.getResponse();
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final Abiturdaten daten = DBUtilsGostLaufbahn.get(conn, schueler_id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long schueler_id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Ermittelt die Fachwahl für die gymnasiale Oberstufe zu einem Fach von dem angegebenen Schüler.
	 *
	 * @param schueler_id   die ID des Schülers
	 * @param fach_id       die ID des Faches
	 *
	 * @return Die HTTP-Response der Get-Operation
	 */
	public Response getFachwahl(final Long schueler_id, final Long fach_id) {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
		if (schueler == null)
			return OperationError.NOT_FOUND.getResponse();
		final DTOFach fach = conn.queryByKey(DTOFach.class, fach_id);
		if ((fach == null) || (fach.IstOberstufenFach == null) || !fach.IstOberstufenFach)
			return OperationError.NOT_FOUND.getResponse();
		final DTOGostSchuelerFachbelegungen fachbelegung = conn.queryByKey(DTOGostSchuelerFachbelegungen.class, schueler.ID, fach.ID);
		if (fachbelegung == null)
			return OperationError.NOT_FOUND.getResponse();
		final GostSchuelerFachwahl fachwahl = new GostSchuelerFachwahl();
		fachwahl.halbjahre[0] = fachbelegung.EF1_Kursart;
		fachwahl.halbjahre[1] = fachbelegung.EF2_Kursart;
		fachwahl.halbjahre[2] = fachbelegung.Q11_Kursart;
		fachwahl.halbjahre[3] = fachbelegung.Q12_Kursart;
		fachwahl.halbjahre[4] = fachbelegung.Q21_Kursart;
		fachwahl.halbjahre[5] = fachbelegung.Q22_Kursart;
		fachwahl.abiturFach = fachbelegung.AbiturFach;
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(fachwahl).build();
	}



	/**
	 * Führt den Fachwahl-Patch für das angegebene Halbjahr aus, sofern dieser gültig ist und in dem
	 * angegebenen Halbjahr erlaubt ist. Ein Patch ist nicht erlaubt, wenn dieser in das aktuelle
	 * Halbjahr oder ein Halbjahr davor fällt, da hier bereits eine Kursblockung stattgefunden hat
	 * und Anpassungen über die Kurswahlen bzw. die Leistungsdaten erfolgen sollten.
	 *
	 * @param schueler      der Schüler, für welchen die Fachwahl angepasst wird
	 * @param fwDB          der Wert für die Fachwahl aus der DB
	 * @param halbjahr      das Halbjahr, auf welches sich der Patch bezieht
	 * @param aktHalbjahr   das Halbjahr, in welchem sich der Schüler befindet
	 * @param fach          das Fach, für welches die Fachwahl angepasst werden soll
	 * @param fw            der Wert für die Fachwahl
	 *
	 * @return der zu übertragende Wert
	 *
	 * @throws WebApplicationException (CONFLICT) falls die Fachwahl ungültig ist
	 */
	private String patchFachwahlHalbjahr(final DTOSchueler schueler, final String fwDB, final GostHalbjahr halbjahr, final GostHalbjahr aktHalbjahr, final DTOFach fach, final String fw) throws WebApplicationException {
		if ("".equals(fw))
			return null;
		if (((fw == null) && (fwDB == null)) || ((fw != null) && (fw.equals(fwDB))))
			return fwDB;
		// prüfe, ob eine Änderung bei diesem Schüler überhaupt erlaubt ist oder in das aktuelle Halbjahr des Schülers oder früher fällt...
		if ((aktHalbjahr != null) && (aktHalbjahr.compareTo(halbjahr) >= 0)) {
			// Prüfe, ob die eingebene Fachwahl den Leistungsdaten entspricht
			final int anzahlAbschnitte = DataSchuleStammdaten.getAnzahlAbschnitte(conn);
			final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryList(
					"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 AND e.ASDJahrgang = ?2",
					DTOSchuelerLernabschnittsdaten.class,
					schueler.ID, halbjahr.jahrgang);
			for (final DTOSchuelerLernabschnittsdaten lernabschnitt : lernabschnitte) {
				final DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, lernabschnitt.Schuljahresabschnitts_ID);
				if (halbjahr.halbjahr * ((anzahlAbschnitte == 4) ? 2 : 1) == abschnitt.Abschnitt) {
					final List<DTOSchuelerLeistungsdaten> leistungen =  conn.queryList(
							"SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID = ?1 AND e.Fach_ID = ?2",
							DTOSchuelerLeistungsdaten.class,
							lernabschnitt.ID, fach.ID);
					for (final DTOSchuelerLeistungsdaten leistung : leistungen) {
						final ZulaessigeKursart zulkursart = ZulaessigeKursart.getByASDKursart(leistung.Kursart);
						final GostKursart kursart = GostKursart.fromKursart(zulkursart);
						if (kursart == null)
							continue;
						if (fw == null)
							throw OperationError.CONFLICT.exception();
						switch (fw) {
							case "M" -> {
								if ((kursart == GostKursart.PJK) || (kursart == GostKursart.VTF)
										|| (kursart == GostKursart.GK) && ((zulkursart == ZulaessigeKursart.GKM) || ((zulkursart == ZulaessigeKursart.AB4) && (halbjahr == GostHalbjahr.Q22))))
									return fw;
							}
							case "S" -> {
								if ((kursart == GostKursart.GK) && ((zulkursart == ZulaessigeKursart.GKS) || (zulkursart == ZulaessigeKursart.AB3)
										|| ((zulkursart == ZulaessigeKursart.AB4) && (halbjahr != GostHalbjahr.Q22))))
									return fw;
							}
							case "LK" -> {
								if ((kursart == GostKursart.LK))
									return fw;
							}
							case "ZK" -> {
								if ((kursart == GostKursart.ZK))
									return fw;
							}
							case "AT" -> {
								if ("SP".equals(fach.StatistikFach.daten.kuerzelASD) && (leistung.NotenKrz == Note.ATTEST))
									return fw;
							}
							default -> throw OperationError.CONFLICT.exception();
						}
					}
				}
			}
			if (fw == null)
				return fw;
			throw OperationError.CONFLICT.exception();
		}
		final boolean valid = (fw == null)
				|| (fw.equals("M")) || (fw.equals("S"))
				|| (((fw.equals("LK")) || (fw.equals("ZK"))) && (!halbjahr.istEinfuehrungsphase()))
				|| ((fw.equals("AT")) && ("SP".equals(fach.StatistikFach.daten.kuerzelASD)));
		if (!valid)
			throw OperationError.CONFLICT.exception();
		return fw;
	}


	/**
	 * Passt die Fachwahl für die gymnasiale Oberstufe zu einem Fach von dem angegebenen Schüler an.
	 *
	 * @param schueler_id   die ID des Schülers
	 * @param fach_id       die ID des Faches
	 * @param is            der {@link InputStream} mit dem JSON-Patch für die Fachwahl
	 *
	 * @return Die HTTP-Response der Patch-Operation
	 */
	public Response patchFachwahl(final Long schueler_id, final Long fach_id, final InputStream is) {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			try {
				conn.transactionBegin();
				final DTOEigeneSchule schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);
				final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
				if (schueler == null)
					throw OperationError.NOT_FOUND.exception();
				// Ermittle den aktuellen Schuljahresaschnitt und den dazugehörigen Schüler-Lernabschnitt
				final DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schueler.Schuljahresabschnitts_ID);
				if (abschnitt == null)
					throw OperationError.NOT_FOUND.exception();
				final TypedQuery<DTOSchuelerLernabschnittsdaten> queryAktAbschnitt = conn.query("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = :schueler_id AND e.Schuljahresabschnitts_ID = :abschnitt_id", DTOSchuelerLernabschnittsdaten.class);
				final DTOSchuelerLernabschnittsdaten lernabschnitt = queryAktAbschnitt
						.setParameter("schueler_id", schueler.ID)
						.setParameter("abschnitt_id", schueler.Schuljahresabschnitts_ID)
						.getResultList().stream().findFirst().orElse(null);
				if (lernabschnitt == null)
					throw OperationError.NOT_FOUND.exception();
				final GostHalbjahr aktHalbjahr = (schule.AnzahlAbschnitte == 4)
						? GostHalbjahr.fromJahrgangUndHalbjahr(lernabschnitt.ASDJahrgang, (abschnitt.Abschnitt + 1) / 2)
						: GostHalbjahr.fromJahrgangUndHalbjahr(lernabschnitt.ASDJahrgang, abschnitt.Abschnitt);
				// Bestimme das Fach und die Fachbelegungen in der DB. Liegen keine vor, so erstelle eine neue Fachnbelegung in der DB,um den Patch zu speichern
				final DTOFach fach = conn.queryByKey(DTOFach.class, fach_id);
				if ((fach == null) || (fach.IstOberstufenFach == null) || !fach.IstOberstufenFach)
					throw OperationError.NOT_FOUND.exception();
				DTOGostSchuelerFachbelegungen fachbelegung = conn.queryByKey(DTOGostSchuelerFachbelegungen.class, schueler.ID, fach.ID);
				if (fachbelegung == null)
					fachbelegung = new DTOGostSchuelerFachbelegungen(schueler.ID, fach.ID);
				for (final Entry<String, Object> entry : map.entrySet()) {
					final String key = entry.getKey();
					final Object value = entry.getValue();
					switch (key) {
						case "halbjahre" -> {
							final String[] wahlen = JSONMapper.convertToStringArray(value, true, true, 6);
							if ((wahlen == null) || (wahlen.length != 0 && wahlen.length != 6))
								throw OperationError.CONFLICT.exception();
							if (wahlen.length == 0) {
								fachbelegung.EF1_Kursart = null;
								fachbelegung.EF2_Kursart = null;
								fachbelegung.Q11_Kursart = null;
								fachbelegung.Q12_Kursart = null;
								fachbelegung.Q21_Kursart = null;
								fachbelegung.Q22_Kursart = null;
							} else {
								fachbelegung.EF1_Kursart = patchFachwahlHalbjahr(schueler, fachbelegung.EF1_Kursart, GostHalbjahr.EF1, aktHalbjahr, fach, wahlen[0]);
								fachbelegung.EF2_Kursart = patchFachwahlHalbjahr(schueler, fachbelegung.EF2_Kursart, GostHalbjahr.EF2, aktHalbjahr, fach, wahlen[1]);
								fachbelegung.Q11_Kursart = patchFachwahlHalbjahr(schueler, fachbelegung.Q11_Kursart, GostHalbjahr.Q11, aktHalbjahr, fach, wahlen[2]);
								fachbelegung.Q12_Kursart = patchFachwahlHalbjahr(schueler, fachbelegung.Q12_Kursart, GostHalbjahr.Q12, aktHalbjahr, fach, wahlen[3]);
								fachbelegung.Q21_Kursart = patchFachwahlHalbjahr(schueler, fachbelegung.Q21_Kursart, GostHalbjahr.Q21, aktHalbjahr, fach, wahlen[4]);
								fachbelegung.Q22_Kursart = patchFachwahlHalbjahr(schueler, fachbelegung.Q22_Kursart, GostHalbjahr.Q22, aktHalbjahr, fach, wahlen[5]);
							}
						}
						case "abiturFach" -> {
							final Integer af = JSONMapper.convertToInteger(value, true);
								if ((af != null) && ((af < 1) || (af > 4)))
									throw OperationError.CONFLICT.exception();
								fachbelegung.AbiturFach = af;
						}
						default -> throw OperationError.BAD_REQUEST.exception();
					}
				}
				conn.transactionPersist(fachbelegung);
				conn.transactionCommit();
			} catch (final Exception e) {
				if (e instanceof final WebApplicationException webAppException)
					return webAppException.getResponse();
				return OperationError.INTERNAL_SERVER_ERROR.getResponse();
			} finally {
				// Perform a rollback if necessary
				conn.transactionRollback();
			}
		}
		return Response.status(Status.OK).build();
	}

	/**
	 * Erstellt das Export-Objekt mit den Laufbahnplanungsdaten des
	 * angegebenen Schülers.
	 *
	 * @param dtoSchueler   das Schüler-DTO
	 *
	 * @return das Laufbahnplanungsdaten-Objekt
	 */
	private GostLaufbahnplanungDaten getLaufbahnplanungsdaten(final DTOSchueler dtoSchueler) {
		// Lese die Daten aus der Datenbank
		final Abiturdaten abidaten = DBUtilsGostLaufbahn.get(conn, dtoSchueler.ID);
		final GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherListeGost(conn, abidaten.abiturjahr);
		final List<DTOGostJahrgangFachkombinationen> kombis = conn
				.queryNamed("DTOGostJahrgangFachkombinationen.abi_jahrgang", abidaten.abiturjahr, DTOGostJahrgangFachkombinationen.class);
		if (kombis == null)
			throw OperationError.NOT_FOUND.exception();
		final DTOGostJahrgangsdaten jahrgangsdaten = conn.queryByKey(DTOGostJahrgangsdaten.class, abidaten.abiturjahr);
		if (jahrgangsdaten == null)
				throw OperationError.NOT_FOUND.exception();
			final List<DTOGostJahrgangBeratungslehrer> dtosBeratungslehrer = conn.queryNamed("DTOGostJahrgangBeratungslehrer.abi_jahrgang", abidaten.abiturjahr, DTOGostJahrgangBeratungslehrer.class);
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw OperationError.NOT_FOUND.exception();
		final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			throw OperationError.NOT_FOUND.exception();
		GostHalbjahr halbjahr = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(abidaten.abiturjahr, schuljahresabschnitt.Jahr, schuljahresabschnitt.Abschnitt);
		if ((halbjahr == null) && (schuljahresabschnitt.Jahr >= abidaten.abiturjahr))
			halbjahr = GostHalbjahr.Q22;
			// Schreibe die Daten in das Export-DTO
		final GostLaufbahnplanungDaten daten = new GostLaufbahnplanungDaten();
		daten.schulNr = schule.SchulNr;
		daten.schulBezeichnung1 = schule.Bezeichnung1 == null ? "" : schule.Bezeichnung1;
		daten.schulBezeichnung2 = schule.Bezeichnung2 == null ? "" : schule.Bezeichnung2;
		daten.schulBezeichnung3 = schule.Bezeichnung3 == null ? "" : schule.Bezeichnung3;
		daten.anmerkungen = "Exportiert am " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		daten.abiturjahr = abidaten.abiturjahr;
		daten.jahrgang = halbjahr == null ? "" : halbjahr.jahrgang;
		daten.textBeratungsbogen = jahrgangsdaten.TextBeratungsbogen;
		daten.hatZusatzkursGE = jahrgangsdaten.ZusatzkursGEVorhanden;
		daten.beginnZusatzkursGE = jahrgangsdaten.ZusatzkursGEErstesHalbjahr;
		daten.hatZusatzkursSW = jahrgangsdaten.ZusatzkursSWVorhanden;
		daten.beginnZusatzkursSW = jahrgangsdaten.ZusatzkursSWErstesHalbjahr;
			daten.beratungslehrer.addAll(DataGostBeratungslehrer.getBeratungslehrer(conn, dtosBeratungslehrer));
			daten.faecher.addAll(gostFaecher.toList());
		for (final DTOGostJahrgangFachkombinationen kombi : kombis)
			daten.fachkombinationen.add(DataGostJahrgangFachkombinationen.dtoMapper.apply(kombi));
		final GostLaufbahnplanungDatenSchueler schuelerDaten = new GostLaufbahnplanungDatenSchueler();
		schuelerDaten.id = dtoSchueler.ID;
		schuelerDaten.vorname = dtoSchueler.Vorname;
		schuelerDaten.nachname = dtoSchueler.Nachname;
		schuelerDaten.geschlecht = dtoSchueler.Geschlecht.kuerzel;
		schuelerDaten.bilingualeSprache = abidaten.bilingualeSprache;
		for (int i = 0; i < GostHalbjahr.maxHalbjahre; i++)
			System.arraycopy(abidaten.bewertetesHalbjahr, 0, schuelerDaten.bewertetesHalbjahr, 0, GostHalbjahr.maxHalbjahre);
		for (final AbiturFachbelegung fbel : abidaten.fachbelegungen) {
			final GostLaufbahnplanungDatenFachbelegung fb = new GostLaufbahnplanungDatenFachbelegung();
			fb.fachID = fbel.fachID;
			fb.abiturFach = fbel.abiturFach;
			for (int i = 0; i < GostHalbjahr.maxHalbjahre; i++) {
				final GostKursart kursart = fbel.belegungen[i] == null ? null : GostKursart.fromKuerzel(fbel.belegungen[i].kursartKuerzel);
				fb.kursart[i] = kursart == null ? null : kursart.kuerzel;
				fb.schriftlich[i] = (kursart != null) && fbel.belegungen[i].schriftlich;
			}
			schuelerDaten.fachbelegungen.add(fb);
		}
		schuelerDaten.sprachendaten = abidaten.sprachendaten;
		daten.schueler.add(schuelerDaten);
		return daten;
	}

	/**
	 * Erstellt eine Export-Datei mit den Laufbahnplanungsdaten des
	 * angegebenen Schülers zur Bearbeitung in einem externen Tool.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response mit der GZip-Komprimierten Laufbahnplanungs-Datei
	 */
	public Response exportGZip(final long idSchueler) {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (dtoSchueler == null)
			throw OperationError.NOT_FOUND.exception();
		return JSONMapper.gzipFileResponseFromObject(getLaufbahnplanungsdaten(dtoSchueler), "Laufbahnplanung_Schueler_" + dtoSchueler.ID + "_" + dtoSchueler.Nachname + "_" + dtoSchueler.Vorname + ".lp");
	}

	/**
	 * Erstellt den Export mit den Laufbahnplanungsdaten des
	 * angegebenen Schülers zur Bearbeitung in einem externen Tool.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response mit den Laufbahnplanungsdaten
	 */
	public Response exportJSON(final long idSchueler) {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (dtoSchueler == null)
			throw OperationError.NOT_FOUND.exception();
		final GostLaufbahnplanungDaten daten = getLaufbahnplanungsdaten(dtoSchueler);
		return Response.ok(daten).type(MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Führt den Import der Laufbahndaten des angegebenen Schülers aus den übergebenenen Laufbahnplanungsdaten
	 * durch. Dabei werde Fehler ggf. im logger protokolliert.
	 *
	 * @param dtoSchueler
	 * @param laufbahnplanungsdaten
	 * @param logger
	 *
	 * @return true im Erfolgsfall
	 */
	private boolean doImport(final DTOSchueler dtoSchueler, final GostLaufbahnplanungDaten laufbahnplanungsdaten, final Logger logger) {
		// Lese zunächst die Informationen zur Schule ein und prüfe, ob die Schulnummer übereinstimmt.
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null) {
			logger.logLn("Fehler: Die Daten der Schule können nicht aus der Datenbank eingelesen werden.");
			return false;
		}
		if (laufbahnplanungsdaten.schulNr != schule.SchulNr) {
			logger.logLn("Fehler: Die Schulnummer der Planungsdatei simmt nicht mit der Schulnummer der Datenbank überein.");
			return false;
		}
		// Lese zunächst die Abiturdaten des Schülers ein, welche in der Datenbank gespeichert sind.
		final Abiturdaten abidaten = DBUtilsGostLaufbahn.get(conn, dtoSchueler.ID);
		final GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherListeGost(conn, abidaten.abiturjahr);
		// Prüfe zunächst, ob der Abiturjahrgang in der Datenbank existiert und mit dem des Schülers übereinstimmt
		if (abidaten.abiturjahr != laufbahnplanungsdaten.abiturjahr) {
			logger.logLn("Fehler: Das Abiturjahrgang der Planungsdatei stimmt nicht mit dem Abiturjahrgang des Schülers überein.");
			return false;
		}
		// Bestimme die Daten des Schülers in den Laufbahnplanungsdaten
		final GostLaufbahnplanungDatenSchueler daten = laufbahnplanungsdaten.schueler.stream().filter(s -> s.id == dtoSchueler.ID).findFirst().orElse(null);
		if (daten == null) {
			logger.logLn("Fehler: Die Laufbahnplanungsdatei enthält keinen Schüler mit der ID " + dtoSchueler.ID + ".");
			return false;
		}
		// Prüfe den Bilingualen Bildungsgang
		if ((daten.bilingualeSprache == null && abidaten.bilingualeSprache != null) || (daten.bilingualeSprache != null && abidaten.bilingualeSprache == null) 
				|| (daten.bilingualeSprache != null && !daten.bilingualeSprache.equals(abidaten.bilingualeSprache))) {
			logger.logLn("Fehler: Die Angaben zum Bilingualen Bildungsgang stimmen nicht überein.");
			return false;
		}
		// Überprüfe die Sprachenfolge
		if (abidaten.sprachendaten.belegungen.size() != daten.sprachendaten.belegungen.size()) {
			logger.logLn("Fehler: Die Anzahl der Sprachbelegungen stimmen nicht überein.");
			return false;
		}
		if (abidaten.sprachendaten.pruefungen.size() != daten.sprachendaten.pruefungen.size()) {
			logger.logLn("Fehler: Die Anzahl der Sprachprüfungen stimmen nicht überein.");
			return false;
		}
		final Map<String, Sprachbelegung> sprachBelegungen = abidaten.sprachendaten.belegungen.stream().collect(Collectors.toMap(b -> b.sprache, b -> b));
		for (final Sprachbelegung belegung: daten.sprachendaten.belegungen) {
			final Sprachbelegung vergleich = sprachBelegungen.get(belegung.sprache);
			if (vergleich == null) {
				logger.logLn("Fehler: Die Sprachbelegung für die Sprache " + belegung.sprache + " wurde in der Datenbank nicht gefunden.");
				return false;
			}
			final boolean vglReihenfolge = (belegung.reihenfolge == null) && (vergleich.reihenfolge == null)
					|| (belegung.reihenfolge != null) && (vergleich.reihenfolge != null) && (belegung.reihenfolge.intValue() == vergleich.reihenfolge.intValue());
			final boolean vglVonJg = (belegung.belegungVonJahrgang == null) && (vergleich.belegungVonJahrgang == null)
					|| (belegung.belegungVonJahrgang != null) && (vergleich.belegungVonJahrgang != null)
					&& (belegung.belegungVonJahrgang.equals(vergleich.belegungVonJahrgang));
			final boolean vglVonAbschnitt = (belegung.belegungVonAbschnitt == null) && (vergleich.belegungVonAbschnitt == null)
					|| (belegung.belegungVonAbschnitt != null) && (vergleich.belegungVonAbschnitt != null)
					&& (belegung.belegungVonAbschnitt.equals(vergleich.belegungVonAbschnitt));
			if (!vglReihenfolge || !vglVonJg || !vglVonAbschnitt) {
				logger.logLn("Fehler: Die Sprachbelegung für die Sprache " + belegung.sprache + " stimmt nicht mit der Eintragung in der Datenbank überein.");
				return false;
			}
		}
		final Map<String, Sprachpruefung> sprachPruefungen = abidaten.sprachendaten.pruefungen.stream().collect(Collectors.toMap(b -> b.sprache, b -> b));
		for (final Sprachpruefung pruefung: daten.sprachendaten.pruefungen) {
			final Sprachpruefung vergleich = sprachPruefungen.get(pruefung.sprache);
			if (vergleich == null) {
				logger.logLn("Fehler: Die Sprachprüfung für die Sprache " + pruefung.sprache + " wurde in der Datenbank nicht gefunden.");
				return false;
			}
			final boolean vglNiveau = (pruefung.anspruchsniveauId == null) && (vergleich.anspruchsniveauId == null)
					|| (pruefung.anspruchsniveauId != null) && (vergleich.anspruchsniveauId != null)
					&& (pruefung.anspruchsniveauId.intValue() == vergleich.anspruchsniveauId.intValue());
			final boolean vglErsSprache = (pruefung.ersetzteSprache == null) && (vergleich.ersetzteSprache == null)
					|| (pruefung.ersetzteSprache != null) && (vergleich.ersetzteSprache != null)
					&& (pruefung.ersetzteSprache.equals(vergleich.ersetzteSprache));
			if (!vglNiveau || !vglErsSprache
					|| (pruefung.kannErstePflichtfremdspracheErsetzen != vergleich.kannErstePflichtfremdspracheErsetzen)
					|| (pruefung.kannZweitePflichtfremdspracheErsetzen != vergleich.kannZweitePflichtfremdspracheErsetzen)
					|| (pruefung.kannWahlpflichtfremdspracheErsetzen != vergleich.kannWahlpflichtfremdspracheErsetzen)
					|| (pruefung.kannBelegungAlsFortgefuehrteSpracheErlauben != vergleich.kannBelegungAlsFortgefuehrteSpracheErlauben)) {
				logger.logLn("Fehler: Die Sprachprüfung für die Sprache " + pruefung.sprache + " stimmt nicht nicht mit der Eintragung in der Datenbank überein.");
				return false;
			}
		}
		// Prüfe die Fachbelegungen bei den Fachbelegungen, wo bereits Leistungsdaten in der Datenbank hinterlegt sind und übernehme die restlichen Fachwahlen
		final Map<Long, AbiturFachbelegung> dbBelegungen = abidaten.fachbelegungen.stream().collect(Collectors.toMap(b -> b.fachID, b -> b));
		final Map<Long, GostLaufbahnplanungDatenFachbelegung> dateiBelegungen = daten.fachbelegungen.stream().collect(Collectors.toMap(b -> b.fachID, b -> b));
		for (final Long idFach : dateiBelegungen.keySet()) {
			final GostFach fach = gostFaecher.get(idFach);
			if (fach == null) {
				logger.logLn("Fehler: Das Fach mit der ID " + idFach + " wird in der Datei verwendet, existiert aber nicht als Fach der gymnasialen Oberstufe in der Datenbank.");
				return false;
			}
		}
		Set<Long> beide = dbBelegungen.keySet().stream().filter(dateiBelegungen::containsKey).collect(Collectors.toSet());
		final Set<Long> nurDB = dbBelegungen.keySet().stream().filter(id -> !dateiBelegungen.containsKey(id)).collect(Collectors.toSet());
		final Set<Long> nurDatei = dateiBelegungen.keySet().stream().filter(id -> !dbBelegungen.containsKey(id)).collect(Collectors.toSet());
		// ... erster Durchgang: Zulässigkeit der Daten in der Datei prüfen
		final HashSet<Long> tmp = new HashSet<>();
		for (final Long idFach : beide) {
			// Prüfe, ob sich Fachbelegungen in Halbjahren unterscheiden, die bereits Leistungsdaten enthalten
			final AbiturFachbelegung db = dbBelegungen.get(idFach);
			final GostLaufbahnplanungDatenFachbelegung datei = dateiBelegungen.get(idFach);
			boolean identisch = true;
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				final String dbKursart = db.belegungen[halbjahr.id] == null ? null : db.belegungen[halbjahr.id].kursartKuerzel;
				final boolean dbSchriftlich = (db.belegungen[halbjahr.id] != null) && db.belegungen[halbjahr.id].schriftlich;
				final boolean istGleich = ((dbKursart == null) && (datei.kursart[halbjahr.id] == null))
						|| ((dbKursart != null) && (datei.kursart[halbjahr.id] != null)
						&& (dbKursart.equals(datei.kursart[halbjahr.id])) && (dbSchriftlich == datei.schriftlich[halbjahr.id]));
				if (abidaten.bewertetesHalbjahr[halbjahr.id]) {
					if (!istGleich) {
						logger.logLn("Fehler: Das Halbjahr " + halbjahr.kuerzel + " ist in der Datenbank bereits in den Leistungsdaten enthalten. Die Laufbahnplanung muss hier für einen Import übereinstimmen.");
						return false;
					}
					continue;
				}
				if (!istGleich) {
					identisch = false;
					break;
				}
			}
			if (!identisch || (db.abiturFach != null && !db.abiturFach.equals(datei.abiturFach)) || ((db.abiturFach == null) && (datei.abiturFach != null)))
				tmp.add(idFach);
		}
		beide = tmp;
		for (final Long idFach : nurDatei) {
			// Prüfe, ob Fachbelegungen zu einem Halbjahr hinzugefügt werden sollen, die bereits Leistungsdaten enthalten
			final GostLaufbahnplanungDatenFachbelegung datei = dateiBelegungen.get(idFach);
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				if ((abidaten.bewertetesHalbjahr[halbjahr.id]) && (datei.kursart[halbjahr.id] != null)) {
					logger.logLn("Fehler: Das Halbjahr " + halbjahr.kuerzel + " ist in der Datenbank bereits in den Leistungsdaten enthalten. Die Laufbahnplanung kann hier für einen Import keine Fachwahlen ergänzen.");
					return false;
				}
			}
		}
		for (final Long idFach : nurDB) {
			// Prüfe, ob Fachbelegungen aus Halbjahres entfernt werden sollen, die bereits Leistungsdaten enthalten
			final AbiturFachbelegung db = dbBelegungen.get(idFach);
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				if ((abidaten.bewertetesHalbjahr[halbjahr.id]) && (db.belegungen[halbjahr.id] != null)) {
					logger.logLn("Fehler: Das Halbjahr " + halbjahr.kuerzel + " ist in der Datenbank bereits in den Leistungsdaten enthalten. Die Laufbahnplanung kann hier für einen Import keine Fachwahlen entfernen.");
					return false;
				}
			}
		}
		// ... zweiter Durchgang: Anpassungen der Fachwahlen in der Datenbank durchführen
		final HashSet<Long> alle = new HashSet<>();
		alle.addAll(beide);
		alle.addAll(nurDB);
		alle.addAll(nurDatei);
		if (!alle.isEmpty()) {
			final ArrayList<DTOGostSchuelerFachbelegungen> fachwahlenGeaendert = new ArrayList<>();
			final List<DTOGostSchuelerFachbelegungen> fachwahlen = conn.queryList("SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Schueler_ID = ?1 AND e.Fach_ID IN ?2",
					DTOGostSchuelerFachbelegungen.class, dtoSchueler.ID, alle);
			final Map<Long, DTOGostSchuelerFachbelegungen> mapFachwahlen = fachwahlen.stream().collect(Collectors.toMap(f -> f.Fach_ID, f -> f));
			for (final Long idFach : beide) {
				final AbiturFachbelegung db = dbBelegungen.get(idFach);
				final GostLaufbahnplanungDatenFachbelegung datei = dateiBelegungen.get(idFach);
				final DTOGostSchuelerFachbelegungen fachwahl = mapFachwahlen.get(idFach);
				for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
					final String dbKursart = db.belegungen[halbjahr.id] == null ? null : db.belegungen[halbjahr.id].kursartKuerzel;
					final boolean dbSchriftlich = (db.belegungen[halbjahr.id] != null) && db.belegungen[halbjahr.id].schriftlich;
					final String dateiKursart = datei.kursart[halbjahr.id];
					if (((dbKursart == null) && (dateiKursart == null)) || ((dbKursart != null) && (dateiKursart != null)
							&& (dbKursart.equals(dateiKursart)) && (dbSchriftlich == datei.schriftlich[halbjahr.id])))
						continue;
					final String kursart = (dateiKursart == null) ? null
							: "AT".equals(dateiKursart) ? "AT"
							: GostKursart.LK.kuerzel.equals(dateiKursart) ? "LK"
							: GostKursart.ZK.kuerzel.equals(dateiKursart) ? "M"
							: GostKursart.PJK.kuerzel.equals(dateiKursart) ? "M"
							: GostKursart.VTF.kuerzel.equals(dateiKursart) ? "M"
							: datei.schriftlich[halbjahr.id] ? "S" : "M";
					switch (halbjahr) {
						case EF1 -> fachwahl.EF1_Kursart = kursart;
						case EF2 -> fachwahl.EF2_Kursart = kursart;
						case Q11 -> fachwahl.Q11_Kursart = kursart;
						case Q12 -> fachwahl.Q12_Kursart = kursart;
						case Q21 -> fachwahl.Q21_Kursart = kursart;
						case Q22 -> fachwahl.Q22_Kursart = kursart;
					}
				}
				fachwahl.AbiturFach = datei.abiturFach;
				fachwahlenGeaendert.add(fachwahl);
			}
			for (final Long idFach : nurDatei) {
				final GostLaufbahnplanungDatenFachbelegung datei = dateiBelegungen.get(idFach);
				DTOGostSchuelerFachbelegungen fachwahl = mapFachwahlen.get(idFach);
				if (fachwahl == null)
					fachwahl = new DTOGostSchuelerFachbelegungen(dtoSchueler.ID, idFach);
				for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
					final String dateiKursart = datei.kursart[halbjahr.id];
					final String kursart = (dateiKursart == null) ? null
							: "AT".equals(dateiKursart) ? "AT"
							: GostKursart.LK.kuerzel.equals(dateiKursart) ? "LK"
							: GostKursart.ZK.kuerzel.equals(dateiKursart) ? "M"
							: GostKursart.PJK.kuerzel.equals(dateiKursart) ? "M"
							: GostKursart.VTF.kuerzel.equals(dateiKursart) ? "M"
							: datei.schriftlich[halbjahr.id] ? "S" : "M";
					switch (halbjahr) {
						case EF1 -> fachwahl.EF1_Kursart = kursart;
						case EF2 -> fachwahl.EF2_Kursart = kursart;
						case Q11 -> fachwahl.Q11_Kursart = kursart;
						case Q12 -> fachwahl.Q12_Kursart = kursart;
						case Q21 -> fachwahl.Q21_Kursart = kursart;
						case Q22 -> fachwahl.Q22_Kursart = kursart;
					}
				}
				fachwahl.AbiturFach = datei.abiturFach;
				fachwahlenGeaendert.add(fachwahl);
			}
			if (!fachwahlenGeaendert.isEmpty())
				conn.persistAll(fachwahlenGeaendert);
			for (final Long idFach : nurDB) {
				final DTOGostSchuelerFachbelegungen fachwahl = mapFachwahlen.get(idFach);
				conn.remove(fachwahl);
			}
		} else {
			logger.logLn("Keine Änderungen für den Schüler mit der ID " + dtoSchueler.ID + " gegenüber der Datenbank in der Datei enthalten.");
		}
		return true;
	}


	/**
	 * Importiert die Daten des Schülers mit der angegebenen ID aus der übergebenen
	 * Laufbahnplanungsdatei.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param data         die Laufbahnplanungsdatei als GZIP-Komprimiertes JSON
	 *
	 * @return die HTTP-Response mit dem Log
	 */
	public Response importGZip(final long idSchueler, final byte[] data) {
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat und ob der Schüler überhaupt existiert.
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (dtoSchueler == null)
			throw OperationError.NOT_FOUND.exception();
		// Erstelle den Logger
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		logger.addConsumer(new LogConsumerConsole());
		// Importiere die Daten...
		GostLaufbahnplanungDaten laufbahnplanungsdaten = null;
		try {
			laufbahnplanungsdaten = JSONMapper.toObjectGZip(data, GostLaufbahnplanungDaten.class);
		} catch (final CompressionException e) {
			logger.log("Fehler beim Öffnen der Datei.");
			logger.log("Fehlernachricht: " + e.getMessage());
		}
		// Führe den Import durch und erstelle die Response mit dem Log
		final SimpleOperationResponse daten = new SimpleOperationResponse();
		daten.success = doImport(dtoSchueler, laufbahnplanungsdaten, logger);
		logger.logLn("Import " + (daten.success ? "erfolgreich." : "fehlgeschlagen."));
		daten.log = log.getStrings();
		return Response.status(daten.success ? Status.OK : Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}



	/**
	 * Importiert die Daten des Schülers mit der angegebenen ID aus den übergebenen
	 * Laufbahnplanungsdaten.
	 *
	 * @param idSchueler              die ID des Schülers
	 * @param laufbahnplanungsdaten   die Laufbahnplanungsdaten
	 *
	 * @return die HTTP-Response mit dem Log
	 */
	public Response importJSON(final long idSchueler, final GostLaufbahnplanungDaten laufbahnplanungsdaten) {
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat und ob der Schüler überhaupt existiert.
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (dtoSchueler == null)
			throw OperationError.NOT_FOUND.exception();
		// Erstelle den Logger
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		logger.addConsumer(new LogConsumerConsole());
		// Führe den Import durch und erstelle die Response mit dem Log
		final SimpleOperationResponse daten = new SimpleOperationResponse();
		daten.success = doImport(dtoSchueler, laufbahnplanungsdaten, logger);
		logger.logLn("Import " + (daten.success ? "erfolgreich." : "fehlgeschlagen."));
		daten.log = log.getStrings();
		return Response.status(daten.success ? Status.OK : Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Führt eine Belegprüfung für alles Schüler des angebenen Abitur-Jahrgangs durch
	 * und gibt die Belegprüfungsergebnisse für die Schüler zurück.
	 *
	 * @param abiturjahr     der zu prüfende Abiturjahrgang
	 * @param pruefungsArt   die Art der Belegprüfung
	 *
	 * @return die Belegprüfungsergebnisse
	 */
	public Response pruefeBelegungAbitujahrgang(final int abiturjahr, final GostBelegpruefungsArt pruefungsArt) {
		try {
			// Prüfe, ob die Schule eine gymnasiale Oberstufe hat und ob der Schüler überhaupt existiert.
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
			final List<DTOSchueler> listSchuelerDTOs = (new DataGostJahrgangSchuelerliste(conn, abiturjahr)).getSchuelerDTOs();

			// Erstelle das DTO für die Eregbnisrückmeldung
			final List<GostBelegpruefungsErgebnisse> daten = new ArrayList<>();

			// Bestimme die Fächer, welche in dem Abiturjahrgang vorhanden sind.
			final @NotNull List<@NotNull GostFach> gostFaecher = DBUtilsFaecherGost.getFaecherListeGost(conn, abiturjahr).toList();

			// Führe für alle Schüler nacheinander die Belegprüfung durch
			for (final DTOSchueler dtoSchueler : listSchuelerDTOs) {
				// Bestimme die Laufbahndaten des Schülers
				final Abiturdaten abidaten = DBUtilsGostLaufbahn.get(conn, dtoSchueler.ID);

				// Erzeuge das Ergebnis-DTO für die Rückgabe
				final GostBelegpruefungsErgebnisse ergebnisse = new GostBelegpruefungsErgebnisse();

				// Führe die Belegprüfung für den Schüler durch
				final AbiturdatenManager abiManager = new AbiturdatenManager(abidaten, gostFaecher, pruefungsArt);
				ergebnisse.ergebnis = abiManager.getBelegpruefungErgebnis();

				// F+lle das zugehörige Schüler-DTO
				ergebnisse.schueler.id = dtoSchueler.ID;
				ergebnisse.schueler.vorname = dtoSchueler.Nachname;
				ergebnisse.schueler.nachname = dtoSchueler.Vorname;
				ergebnisse.schueler.status = dtoSchueler.Status.id;
				ergebnisse.schueler.geschlecht = dtoSchueler.Geschlecht.id;

				// Schreibe das Ergebnis in die Rückmeldung
				daten.add(ergebnisse);
			}

			// Erzeuge die Response mit den Belegprüfungsergebnissen
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (final WebApplicationException wae) {
			return wae.getResponse();
		}
	}


}
