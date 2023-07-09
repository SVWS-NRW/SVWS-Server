package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostSchuelerFachwahl;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.jahrgang.Jahrgaenge;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachbelegungen;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangSprachenfolge;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link Abiturdaten}.
 */
public final class DataGostJahrgangLaufbahnplanung extends DataManager<Integer> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Abiturdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostJahrgangLaufbahnplanung(final DBEntityManager conn) {
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
	public Response get(final Integer abijahr) {
		if (abijahr == null)
			return OperationError.NOT_FOUND.getResponse();
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final Abiturdaten daten = DBUtilsGostLaufbahn.getVorlage(conn, abijahr);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Integer abijahr, final InputStream is) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Ermittelt die Fachwahl für die gymnasiale Oberstufe zu einem Fach in der Vorlage
	 * des angegebenen Abiturjahrgangs.
	 *
	 * @param abijahr   der Abiturjahrgang
	 * @param fach_id   die ID des Faches
	 *
	 * @return Die HTTP-Response der Get-Operation
	 */
	public Response getFachwahl(final Integer abijahr, final Long fach_id) {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOGostJahrgangsdaten jahrgang = conn.queryByKey(DTOGostJahrgangsdaten.class, abijahr);
		if (jahrgang == null)
			return OperationError.NOT_FOUND.getResponse();
		final DTOFach fach = conn.queryByKey(DTOFach.class, fach_id);
		if ((fach == null) || (fach.IstOberstufenFach == null) || Boolean.FALSE.equals(fach.IstOberstufenFach))
			return OperationError.NOT_FOUND.getResponse();
		final DTOGostJahrgangFachbelegungen fachbelegung = conn.queryByKey(DTOGostJahrgangFachbelegungen.class, abijahr, fach.ID);
		final GostSchuelerFachwahl fachwahl = new GostSchuelerFachwahl();
		fachwahl.halbjahre[0] = (fachbelegung == null) ? null : fachbelegung.EF1_Kursart;
		fachwahl.halbjahre[1] = (fachbelegung == null) ? null : fachbelegung.EF2_Kursart;
		fachwahl.halbjahre[2] = (fachbelegung == null) ? null : fachbelegung.Q11_Kursart;
		fachwahl.halbjahre[3] = (fachbelegung == null) ? null : fachbelegung.Q12_Kursart;
		fachwahl.halbjahre[4] = (fachbelegung == null) ? null : fachbelegung.Q21_Kursart;
		fachwahl.halbjahre[5] = (fachbelegung == null) ? null : fachbelegung.Q22_Kursart;
		fachwahl.abiturFach = (fachbelegung == null) ? null : fachbelegung.AbiturFach;
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(fachwahl).build();
	}



	/**
	 * Führt den Fachwahl-Patch für das angegebene Halbjahr aus, sofern dieser gültig ist.
	 *
	 * @param jahrgang      der Abiturjahrgang, für welchen die Fachwahl angepasst wird
	 * @param fwDB          der Wert für die Fachwahl aus der DB
	 * @param halbjahr      das Halbjahr, auf welches sich der Patch bezieht
	 * @param fach          das Fach, für welches die Fachwahl angepasst werden soll
	 * @param fw            der Wert für die Fachwahl
	 *
	 * @return der zu übertragende Wert
	 *
	 * @throws WebApplicationException (CONFLICT) falls die Fachwahl ungültig ist
	 */
	private static String patchFachwahlHalbjahr(final DTOGostJahrgangsdaten jahrgang, final String fwDB, final GostHalbjahr halbjahr, final DTOFach fach, final String fw) throws WebApplicationException {
		if ("".equals(fw))
			return null;
		if (((fw == null) && (fwDB == null)) || ((fw != null) && (fw.equals(fwDB))))
			return fwDB;
		final boolean valid = (fw == null)
				|| (fw.equals("M")) || (fw.equals("S"))
				|| (((fw.equals("LK")) || (fw.equals("ZK"))) && (!halbjahr.istEinfuehrungsphase()))
				|| ((fw.equals("AT")) && ("SP".equals(fach.StatistikFach.daten.kuerzelASD)));
		if (!valid)
			throw OperationError.CONFLICT.exception();
		return fw;
	}


	/**
	 * Passt die Fachwahl für die gymnasiale Oberstufe zu einem Fach bei der Vorlage des angebenenen
	 * Abiturjahrgangs an.
	 *
	 * @param abijahr   der Abiturjahrgang
	 * @param fach_id   die ID des Faches
	 * @param is        der {@link InputStream} mit dem JSON-Patch für die Fachwahl
	 *
	 * @return Die HTTP-Response der Patch-Operation
	 */
	public Response patchFachwahl(final Integer abijahr, final Long fach_id, final InputStream is) {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			try {
				conn.transactionBegin();
				DBUtilsGost.pruefeSchuleMitGOSt(conn);
				final DTOGostJahrgangsdaten jahrgang = conn.queryByKey(DTOGostJahrgangsdaten.class, abijahr);
				if (jahrgang == null)
					return OperationError.NOT_FOUND.getResponse();
				// Bestimme das Fach und die Fachbelegungen in der DB. Liegen keine vor, so erstelle eine neue Fachnbelegung in der DB,um den Patch zu speichern
				final DTOFach fach = conn.queryByKey(DTOFach.class, fach_id);
				if ((fach == null) || (fach.IstOberstufenFach == null) || Boolean.FALSE.equals(fach.IstOberstufenFach))
					throw OperationError.NOT_FOUND.exception();
				DTOGostJahrgangFachbelegungen fachbelegung = conn.queryByKey(DTOGostJahrgangFachbelegungen.class, abijahr, fach.ID);
				if (fachbelegung == null)
					fachbelegung = new DTOGostJahrgangFachbelegungen(abijahr, fach.ID);
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
								fachbelegung.EF1_Kursart = patchFachwahlHalbjahr(jahrgang, fachbelegung.EF1_Kursart, GostHalbjahr.EF1, fach, wahlen[0]);
								fachbelegung.EF2_Kursart = patchFachwahlHalbjahr(jahrgang, fachbelegung.EF2_Kursart, GostHalbjahr.EF2, fach, wahlen[1]);
								fachbelegung.Q11_Kursart = patchFachwahlHalbjahr(jahrgang, fachbelegung.Q11_Kursart, GostHalbjahr.Q11, fach, wahlen[2]);
								fachbelegung.Q12_Kursart = patchFachwahlHalbjahr(jahrgang, fachbelegung.Q12_Kursart, GostHalbjahr.Q12, fach, wahlen[3]);
								fachbelegung.Q21_Kursart = patchFachwahlHalbjahr(jahrgang, fachbelegung.Q21_Kursart, GostHalbjahr.Q21, fach, wahlen[4]);
								fachbelegung.Q22_Kursart = patchFachwahlHalbjahr(jahrgang, fachbelegung.Q22_Kursart, GostHalbjahr.Q22, fach, wahlen[5]);
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
	 * Setzt die Vorlage-Fachwahlen für den angegebenen Abiturjahrgang zurück.
	 * Handelt es sich um den Vorlage-Abiturjahrgang, so werden alle Fachwahlen entfernt.
	 * Ansonsten werden die Faten aus dem Vorlage-Abiturjahrgang übernommen.
	 *
	 * @param abijahr   der Abiturjahrgang
	 *
	 * @return Die HTTP-Response der Operation
	 */
	public Response reset(final Integer abijahr) {
		try {
			conn.transactionBegin();
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
			final DTOGostJahrgangsdaten jahrgang = conn.queryByKey(DTOGostJahrgangsdaten.class, abijahr);
			if (jahrgang == null)
				return OperationError.NOT_FOUND.getResponse();
			if (abijahr == -1) {
				final @NotNull GostFaecherManager faecherManager = DBUtilsFaecherGost.getFaecherListeGost(conn, abijahr);
				conn.transactionExecuteDelete("DELETE FROM DTOGostJahrgangFachbelegungen e WHERE e.Abi_Jahrgang = %d".formatted(abijahr));
				conn.transactionExecuteDelete("DELETE FROM DTOGostJahrgangSprachenfolge e WHERE e.Abi_Jahrgang = %d".formatted(abijahr));
				// Setze Default-Einträge für die Fächer Deutsch, Mathematik und Sport und für die Sprachenfolge bei Englisch
				final @NotNull List<@NotNull GostFach> d = faecherManager.getByKuerzel(ZulaessigesFach.D.daten.kuerzelASD);
				if (d.size() == 1) {
					final DTOGostJahrgangFachbelegungen fw = new DTOGostJahrgangFachbelegungen(abijahr, d.get(0).id);
					fw.EF1_Kursart = "S";
					fw.EF2_Kursart = "S";
					fw.Q11_Kursart = "S";
					fw.Q12_Kursart = "S";
					fw.Q21_Kursart = "S";
					fw.Q22_Kursart = "M";
					conn.transactionPersist(fw);
				}
				final @NotNull List<@NotNull GostFach> m = faecherManager.getByKuerzel(ZulaessigesFach.M.daten.kuerzelASD);
				if (m.size() == 1) {
					final DTOGostJahrgangFachbelegungen fw = new DTOGostJahrgangFachbelegungen(abijahr, m.get(0).id);
					fw.EF1_Kursart = "S";
					fw.EF2_Kursart = "S";
					fw.Q11_Kursart = "S";
					fw.Q12_Kursart = "S";
					fw.Q21_Kursart = "S";
					fw.Q22_Kursart = "M";
					conn.transactionPersist(fw);
				}
				final @NotNull List<@NotNull GostFach> sp = faecherManager.getByKuerzel(ZulaessigesFach.SP.daten.kuerzelASD);
				if (sp.size() == 1) {
					final DTOGostJahrgangFachbelegungen fw = new DTOGostJahrgangFachbelegungen(abijahr, sp.get(0).id);
					fw.EF1_Kursart = "M";
					fw.EF2_Kursart = "M";
					fw.Q11_Kursart = "M";
					fw.Q12_Kursart = "M";
					fw.Q21_Kursart = "M";
					fw.Q22_Kursart = "M";
					conn.transactionPersist(fw);
				}
				final DTOGostJahrgangSprachenfolge sfE = new DTOGostJahrgangSprachenfolge(abijahr, "E");
				sfE.ReihenfolgeNr = 1;
				sfE.ASDJahrgangVon = Jahrgaenge.JG_05.daten.kuerzel;
				conn.transactionPersist(sfE);
				conn.transactionCommit();
				return Response.status(Status.NO_CONTENT).build();
			}
	    	final List<DTOGostJahrgangFachbelegungen> dtoFachwahlen = conn.queryNamed("DTOGostJahrgangFachbelegungen.abi_jahrgang", -1, DTOGostJahrgangFachbelegungen.class);
	        final List<DTOGostJahrgangSprachenfolge> dtoSprachenfolge = conn.queryNamed("DTOGostJahrgangSprachenfolge.abi_jahrgang", -1, DTOGostJahrgangSprachenfolge.class);
			conn.transactionExecuteDelete("DELETE FROM DTOGostJahrgangFachbelegungen e WHERE e.Abi_Jahrgang = %d".formatted(abijahr));
			conn.transactionExecuteDelete("DELETE FROM DTOGostJahrgangSprachenfolge e WHERE e.Abi_Jahrgang = %d".formatted(abijahr));
			for (final DTOGostJahrgangFachbelegungen dto : dtoFachwahlen) {
				final DTOGostJahrgangFachbelegungen fw = new DTOGostJahrgangFachbelegungen(abijahr, dto.Fach_ID);
				fw.EF1_Kursart = dto.EF1_Kursart;
				fw.EF2_Kursart = dto.EF2_Kursart;
				fw.Q11_Kursart = dto.Q11_Kursart;
				fw.Q12_Kursart = dto.Q11_Kursart;
				fw.Q21_Kursart = dto.Q21_Kursart;
				fw.Q22_Kursart = dto.Q22_Kursart;
				fw.AbiturFach = dto.AbiturFach;
				fw.Bemerkungen = dto.Bemerkungen;
				conn.transactionPersist(fw);
			}
			for (final DTOGostJahrgangSprachenfolge dto : dtoSprachenfolge) {
				final DTOGostJahrgangSprachenfolge sf = new DTOGostJahrgangSprachenfolge(abijahr, dto.Sprache);
				sf.ReihenfolgeNr = dto.ReihenfolgeNr;
				sf.ASDJahrgangVon = dto.ASDJahrgangVon;
				conn.transactionPersist(sf);
			}
			conn.transactionCommit();
			return Response.status(Status.NO_CONTENT).build();
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webAppException)
				return webAppException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			// Perform a rollback if necessary
			conn.transactionRollback();
		}
	}

}
