package de.svws_nrw.data.gost;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.Collator;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESException;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostBelegpruefungsErgebnisse;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungDaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungDatenFachbelegung;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungDatenSchueler;
import de.svws_nrw.core.data.gost.GostSchuelerFachwahl;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.data.schueler.Sprachpruefung;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.schueler.DBUtilsSchueler;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangBeratungslehrer;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachkombinationen;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/*
 * Die Implementierung enthält Teile von experimentellem Code. Für diesen gilt folgendes:
 *
 * Bei dieser Implementierung handelt es sich um eine Umsetzung in Bezug auf möglichen zukünftigen
 * Änderungen in der APO-GOSt. Diese basiert auf der aktuellen Implementierung und integriert Aspekte
 * aus dem Eckpunktepapier und auf in den Schulleiterdienstbesprechungen erläuterten Vorhaben.
 * Sie dient der Evaluierung von möglichen Umsetzungsvarianten und als Vorbereitung einer späteren
 * Implementierung der Belegprüfung. Insbesondere sollen erste Versuche mit Laufbahnen mit einem
 * 5. Abiturfach und Projektkursen erprobt werden. Detailaspekte können erst nach Erscheinen der APO-GOSt
 * umgesetzt werden.
 * Es handelt sich also um experimentellen Code, der keine Rückschlüsse auf Details einer zukünftigen APO-GOSt
 * erlaubt.
 */
/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link Abiturdaten}.
 */
public final class DataGostSchuelerLaufbahnplanung extends DataManagerRevised<Long, Long, Abiturdaten> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link Abiturdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostSchuelerLaufbahnplanung(final DBEntityManager conn) {
		super(conn);
	}

	private void checkFunktionsbezogeneKompetenz(final Integer abiturjahrgang) throws ApiOperationException {
		if (hatBenutzerNurFunktionsbezogeneKompetenz(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
				Set.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)))
			checkBenutzerFunktionsbezogeneKompetenzAbiturjahrgang(abiturjahrgang);
	}

	@Override
	protected Abiturdaten map(final Long idSchueler) throws ApiOperationException {
		if (idSchueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Schüler-ID darf nicht null sein.");
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		return DBUtilsGostLaufbahn.get(conn, idSchueler);
	}

	@Override
	public Abiturdaten getById(final Long idSchueler) throws ApiOperationException {
		final Abiturdaten daten = map(idSchueler);
		checkFunktionsbezogeneKompetenz(daten.abiturjahr);
		return daten;
	}


	/**
	 * Ermittelt die Fachwahl für die gymnasiale Oberstufe zu einem Fach von dem angegebenen Schüler.
	 *
	 * @param schueler_id   die ID des Schülers
	 * @param fach_id       die ID des Faches
	 *
	 * @return Die HTTP-Response der Get-Operation
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response getFachwahl(final Long schueler_id, final Long fach_id) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOFach fach = conn.queryByKey(DTOFach.class, fach_id);
		if ((fach == null) || (fach.IstOberstufenFach == null) || Boolean.FALSE.equals(fach.IstOberstufenFach))
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOGostSchuelerFachbelegungen fachbelegung = conn.queryByKey(DTOGostSchuelerFachbelegungen.class, schueler.ID, fach.ID);
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
	 * Prüft, ob die Fachwahl in dem Halbjahr zu den Leistungsdaten passt. Ist dies nicht der Fall, so wird eine Exception generiert.
	 *
	 * @param leistungen    die Leistungen die geprüft werden
	 * @param halbjahr      das Halbjahr, auf welches sich der Patch bezieht
	 * @param istSP         gibt an, ob das Fach für die Leistungsdaten Sport ist
	 * @param fw            der Wert für die Fachwahl
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private static void patchFachwahlHalbjahrCheckLeistungen(final List<DTOSchuelerLeistungsdaten> leistungen, final GostHalbjahr halbjahr, final boolean istSP,
			final String fw) throws ApiOperationException {
		for (final DTOSchuelerLeistungsdaten leistung : leistungen) {
			final ZulaessigeKursart zulkursart = ZulaessigeKursart.data().getWertByKuerzel(leistung.Kursart);
			final GostKursart kursart = GostKursart.fromKursart(zulkursart);
			if (kursart == null)
				continue;
			// Keine Fachwahl -> Konflikt, da Leistungsdaten vorhanden sind
			if (fw == null)
				throw new ApiOperationException(Status.CONFLICT);
			// Prüfe, ob Fachwahl mündlich passt
			if (("M".equals(fw)) && ((kursart == GostKursart.PJK) || (kursart == GostKursart.VTF)
					|| ((kursart == GostKursart.GK) && ((zulkursart == ZulaessigeKursart.GKM)
							|| ((zulkursart == ZulaessigeKursart.AB4) && (halbjahr == GostHalbjahr.Q22))))))
				return;
			// Prüfe, ob Fachwahl schriftlich passt
			if (("S".equals(fw)) && ((kursart == GostKursart.GK) && ((zulkursart == ZulaessigeKursart.GKS) || (zulkursart == ZulaessigeKursart.AB3)
					|| ((zulkursart == ZulaessigeKursart.AB4) && (halbjahr != GostHalbjahr.Q22)))))
				return;
			// Prüfe, ob Fachwahl Leistungskurs passt
			if (("LK".equals(fw)) && (kursart == GostKursart.LK))
				return;
			// Prüfe, ob Fachwahl Zusatzkurs passt
			if (("ZK".equals(fw)) && (kursart == GostKursart.ZK))
				return;
			// Prüfe, ob ein Sportattest passt
			if (("AT".equals(fw)) && (istSP && (Note.data().getWertByKuerzel(leistung.NotenKrz) == Note.ATTEST)))
				return;
		}
		if (fw != null)
			throw new ApiOperationException(Status.CONFLICT);
	}

	/**
	 * Prüft, ob die Fachwahl in dem Halbjahr zu den Leistungsdaten in den Lernabschnitten passt.
	 * Ist dies nicht der Fall, so wird eine Exception generiert.
	 *
	 * @param schueler      der Schüler, für welchen die Fachwahl angepasst wird
	 * @param halbjahr      das Halbjahr, auf welches sich der Patch bezieht
	 * @param fach          das Fach, für welches die Fachwahl angepasst werden soll
	 * @param fw            der Wert für die Fachwahl
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private void patchFachwahlHalbjahrCheckLernabschnitt(final DTOSchueler schueler, final GostHalbjahr halbjahr, final DTOFach fach, final String fw)
			throws ApiOperationException {
		// Prüfe, ob die eingebene Fachwahl den Leistungsdaten entspricht
		final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryList(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 AND e.ASDJahrgang = ?2 AND e.SemesterWertung = true AND e.WechselNr = 0",
				DTOSchuelerLernabschnittsdaten.class,
				schueler.ID, halbjahr.jahrgang);
		for (final DTOSchuelerLernabschnittsdaten lernabschnitt : lernabschnitte) {
			final DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, lernabschnitt.Schuljahresabschnitts_ID);
			if (halbjahr.halbjahr != abschnitt.Abschnitt)
				continue;
			final List<DTOSchuelerLeistungsdaten> leistungen =
					conn.queryList("SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID = ?1 AND e.Fach_ID = ?2", DTOSchuelerLeistungsdaten.class,
							lernabschnitt.ID, fach.ID);
			if (leistungen.isEmpty()) {
				final boolean valid = (fw == null)
						|| (fw.equals("M")) || (fw.equals("S"))
						|| (((fw.equals("LK")) || (fw.equals("ZK"))) && (!halbjahr.istEinfuehrungsphase()))
						|| ((fw.equals("AT")) && ("SP".equals(fach.StatistikKuerzel)));
				if (!valid)
					throw new ApiOperationException(Status.CONFLICT);
				return;
			}
			patchFachwahlHalbjahrCheckLeistungen(leistungen, halbjahr, "SP".equals(fach.StatistikKuerzel), fw);
			return;
		}
		if (fw != null)
			throw new ApiOperationException(Status.CONFLICT);
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
	 * @throws ApiOperationException (CONFLICT) falls die Fachwahl ungültig ist
	 */
	private String patchFachwahlHalbjahr(final DTOSchueler schueler, final String fwDB, final GostHalbjahr halbjahr, final GostHalbjahr aktHalbjahr,
			final DTOFach fach, final String fw) throws ApiOperationException {
		if ("".equals(fw))
			return null;
		if (((fw == null) && (fwDB == null)) || ((fw != null) && (fw.equals(fwDB))))
			return fwDB;
		final boolean valid = (fw == null)
				|| (fw.equals("M")) || (fw.equals("S"))
				|| (fw.equals("LK") && !halbjahr.istEinfuehrungsphase()
						&& !GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hatKuerzel(fach.StatistikKuerzel))
				|| (fw.equals("ZK") && !halbjahr.istEinfuehrungsphase())
				|| (fw.equals("AT") && "SP".equals(fach.StatistikKuerzel));
		if (!valid)
			throw new ApiOperationException(Status.CONFLICT, "Die angegebene Fachwahl ist ungültig.");
		// prüfe, ob eine Änderung bei diesem Schüler überhaupt erlaubt ist oder in das aktuelle Halbjahr des Schülers oder früher fällt...
		if ((aktHalbjahr != null) && (aktHalbjahr.compareTo(halbjahr) >= 0)) {
			patchFachwahlHalbjahrCheckLernabschnitt(schueler, halbjahr, fach, fw);
			return fw;
		}
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
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response patchFachwahl(final Long schueler_id, final Long fach_id, final InputStream is) throws ApiOperationException {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty())
			return Response.status(Status.OK).build();
		final DTOEigeneSchule schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final Schulform schulform = conn.getUser().schuleGetSchulform();
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(schueler.Schuljahresabschnitts_ID);
		// Ermittle den aktuellen Schuljahresabschnitt und den dazugehörigen Schüler-Lernabschnitt
		final DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schueler.Schuljahresabschnitts_ID);
		if (abschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final TypedQuery<DTOSchuelerLernabschnittsdaten> queryAktAbschnitt = conn.query(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = :schueler_id AND e.Schuljahresabschnitts_ID = :abschnitt_id AND e.WechselNr = 0",
				DTOSchuelerLernabschnittsdaten.class);
		final DTOSchuelerLernabschnittsdaten lernabschnitt = queryAktAbschnitt
				.setParameter("schueler_id", schueler.ID)
				.setParameter("abschnitt_id", schueler.Schuljahresabschnitts_ID)
				.getResultList().stream().findFirst().orElse(null);
		if (lernabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final GostHalbjahr aktHalbjahr = (schule.AnzahlAbschnitte == 4)
				? GostHalbjahr.fromJahrgangUndHalbjahr(lernabschnitt.ASDJahrgang, (abschnitt.Abschnitt + 1) / 2)
				: GostHalbjahr.fromJahrgangUndHalbjahr(lernabschnitt.ASDJahrgang, abschnitt.Abschnitt);
		final Schulgliederung schulgliederung = (lernabschnitt.Schulgliederung == null)
				? Schulgliederung.getDefault(schulform)
				: Schulgliederung.data().getWertByKuerzel(lernabschnitt.Schulgliederung);
		final DTOJahrgang dtoJahrgang = conn.queryByKey(DTOJahrgang.class, lernabschnitt.Jahrgang_ID);
		final Jahrgaenge jahrgang =
				((dtoJahrgang == null) || (dtoJahrgang.ASDJahrgang == null)) ? null : Jahrgaenge.data().getWertBySchluessel(dtoJahrgang.ASDJahrgang);
		final Integer abiturjahr = DBUtilsGost.getAbiturjahr(schulform, schulgliederung, schuljahresabschnitt.schuljahr, jahrgang);
		if (abiturjahr == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Das Abiturjahr konnte für den Schüler nicht ermittelt werden.");
		// Bestimme das Fach und die Fachbelegungen in der DB. Liegen keine vor, so erstelle eine neue Fachnbelegung in der DB,um den Patch zu speichern
		final DTOFach fach = conn.queryByKey(DTOFach.class, fach_id);
		if ((fach == null) || (fach.IstOberstufenFach == null) || Boolean.TRUE.equals(!fach.IstOberstufenFach))
			throw new ApiOperationException(Status.NOT_FOUND);
		DTOGostSchuelerFachbelegungen fachbelegung = conn.queryByKey(DTOGostSchuelerFachbelegungen.class, schueler.ID, fach.ID);
		if (fachbelegung == null)
			fachbelegung = new DTOGostSchuelerFachbelegungen(schueler.ID, fach.ID);
		for (final Entry<String, Object> entry : map.entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			switch (key) {
				case "halbjahre" -> {
					final String[] wahlen = JSONMapper.convertToStringArray(value, true, 6);
					if ((wahlen == null) || ((wahlen.length != 0) && (wahlen.length != 6)))
						throw new ApiOperationException(Status.CONFLICT);
					if (wahlen.length == 0) {
						fachbelegung.EF1_Kursart = null;
						fachbelegung.EF2_Kursart = null;
						fachbelegung.Q11_Kursart = null;
						fachbelegung.Q12_Kursart = null;
						fachbelegung.Q21_Kursart = null;
						fachbelegung.Q22_Kursart = null;
					} else {
						fachbelegung.EF1_Kursart =
								patchFachwahlHalbjahr(schueler, fachbelegung.EF1_Kursart, GostHalbjahr.EF1, aktHalbjahr, fach, wahlen[0]);
						fachbelegung.EF2_Kursart =
								patchFachwahlHalbjahr(schueler, fachbelegung.EF2_Kursart, GostHalbjahr.EF2, aktHalbjahr, fach, wahlen[1]);
						fachbelegung.Q11_Kursart =
								patchFachwahlHalbjahr(schueler, fachbelegung.Q11_Kursart, GostHalbjahr.Q11, aktHalbjahr, fach, wahlen[2]);
						fachbelegung.Q12_Kursart =
								patchFachwahlHalbjahr(schueler, fachbelegung.Q12_Kursart, GostHalbjahr.Q12, aktHalbjahr, fach, wahlen[3]);
						fachbelegung.Q21_Kursart =
								patchFachwahlHalbjahr(schueler, fachbelegung.Q21_Kursart, GostHalbjahr.Q21, aktHalbjahr, fach, wahlen[4]);
						fachbelegung.Q22_Kursart =
								patchFachwahlHalbjahr(schueler, fachbelegung.Q22_Kursart, GostHalbjahr.Q22, aktHalbjahr, fach, wahlen[5]);
					}
				}
				case "abiturFach" -> {
					// experimenteller Code für das 5. Abiturfach
					final int maxAbifach = ((abiturjahr >= 2029) && (ServerMode.DEV.checkServerMode(SVWSKonfiguration.get().getServerMode())))
							? 5 : 4;
					fachbelegung.AbiturFach = JSONMapper.convertToIntegerInRange(value, true, 1, maxAbifach + 1);
				}
				default -> throw new ApiOperationException(Status.BAD_REQUEST);
			}
		}
		conn.transactionPersist(fachbelegung);
		return Response.status(Status.OK).build();
	}

	/**
	 * Erstellt das Export-Objekt mit den Laufbahnplanungsdaten des
	 * angegebenen Schülers.
	 *
	 * @param dtoSchueler   das Schüler-DTO
	 *
	 * @return das Laufbahnplanungsdaten-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private GostLaufbahnplanungDaten getLaufbahnplanungsdaten(final DTOSchueler dtoSchueler) throws ApiOperationException {
		// Lese die Daten aus der Datenbank
		final Abiturdaten abidaten = DBUtilsGostLaufbahn.get(conn, dtoSchueler.ID);
		final GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherManager(abidaten.schuljahrAbitur, conn, abidaten.abiturjahr);
		final List<DTOGostJahrgangFachkombinationen> kombis = conn
				.queryList(DTOGostJahrgangFachkombinationen.QUERY_BY_ABI_JAHRGANG, DTOGostJahrgangFachkombinationen.class, abidaten.abiturjahr);
		if (kombis == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOGostJahrgangsdaten jahrgangsdaten = conn.queryByKey(DTOGostJahrgangsdaten.class, abidaten.abiturjahr);
		if (jahrgangsdaten == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<DTOGostJahrgangBeratungslehrer> dtosBeratungslehrer = conn.queryList(DTOGostJahrgangBeratungslehrer.QUERY_BY_ABI_JAHRGANG,
				DTOGostJahrgangBeratungslehrer.class, abidaten.abiturjahr);
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		GostHalbjahr halbjahr = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(abidaten.abiturjahr, schuljahresabschnitt.Jahr, schuljahresabschnitt.Abschnitt);
		if ((halbjahr == null) && (schuljahresabschnitt.Jahr >= abidaten.abiturjahr))
			halbjahr = GostHalbjahr.Q22;
		final List<DTOSchuelerLernabschnittsdaten> schuelerLernabschnittsdaten = conn.queryList(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 AND e.Schuljahresabschnitts_ID = ?2 AND e.WechselNr = 0",
				DTOSchuelerLernabschnittsdaten.class, dtoSchueler.ID, schuljahresabschnitt.ID);
		String aktJahrgang = (schuelerLernabschnittsdaten.size() == 1) ? schuelerLernabschnittsdaten.get(0).ASDJahrgang : null;
		if (aktJahrgang == null)
			aktJahrgang = (halbjahr == null) ? "" : halbjahr.jahrgang;
		// Schreibe die Daten in das Export-DTO
		final GostLaufbahnplanungDaten daten = new GostLaufbahnplanungDaten();
		daten.schulNr = schule.SchulNr;
		daten.schulBezeichnung1 = (schule.Bezeichnung1 == null) ? "" : schule.Bezeichnung1;
		daten.schulBezeichnung2 = (schule.Bezeichnung2 == null) ? "" : schule.Bezeichnung2;
		daten.schulBezeichnung3 = (schule.Bezeichnung3 == null) ? "" : schule.Bezeichnung3;
		daten.anmerkungen = "Exportiert am " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		daten.abiturjahr = abidaten.abiturjahr;
		daten.jahrgang = aktJahrgang;
		daten.textBeratungsbogen = jahrgangsdaten.TextBeratungsbogen;
		daten.hatZusatzkursGE = jahrgangsdaten.ZusatzkursGEVorhanden;
		daten.beginnZusatzkursGE = jahrgangsdaten.ZusatzkursGEErstesHalbjahr;
		daten.hatZusatzkursSW = jahrgangsdaten.ZusatzkursSWVorhanden;
		daten.beginnZusatzkursSW = jahrgangsdaten.ZusatzkursSWErstesHalbjahr;
		daten.beratungslehrer.addAll(DataGostBeratungslehrer.getBeratungslehrer(conn, dtosBeratungslehrer));
		daten.faecher.addAll(gostFaecher.faecher());
		for (final DTOGostJahrgangFachkombinationen kombi : kombis)
			daten.fachkombinationen.add(DataGostJahrgangFachkombinationen.dtoMapper.apply(kombi));
		final AES aes = DBUtilsSchueler.getOrCreateSchuelerAES(conn, dtoSchueler.ID);
		final GostLaufbahnplanungDatenSchueler schuelerDaten = new GostLaufbahnplanungDatenSchueler();
		schuelerDaten.id = dtoSchueler.ID;
		try {
			schuelerDaten.idEnc = aes.encryptBase64(ByteBuffer.wrap(new byte[8]).putLong(schuelerDaten.id).array());
		} catch (final AESException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e);
		}
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
				final GostKursart kursart = (fbel.belegungen[i] == null) ? null : GostKursart.fromKuerzel(fbel.belegungen[i].kursartKuerzel);
				fb.kursart[i] = (kursart == null) ? null : kursart.kuerzel;
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
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response exportGZip(final long idSchueler) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (dtoSchueler == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final GostLaufbahnplanungDaten daten = getLaufbahnplanungsdaten(dtoSchueler);
		final String filename =
				"Laufbahnplanung_%d_%s_%s_%s_%d.lp".formatted(daten.abiturjahr, daten.jahrgang, dtoSchueler.Nachname, dtoSchueler.Vorname, dtoSchueler.ID);
		return JSONMapper.gzipFileResponseFromObject(daten, filename);
	}


	/**
	 * Erstellt Export-Dateien mit den Laufbahnplanungsdaten der
	 * angegebenen Schüler zur Bearbeitung in einem externen Tool.
	 * Die Dateien werden in einer ZIP-Datei gebündelt.
	 *
	 * @param ids   die ID der Schüler
	 *
	 * @return die Response mit der ZIP-Datei mit den GZip-Komprimierten Laufbahnplanungs-Dateien
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response exportGZip(final List<Long> ids) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final List<DTOSchueler> dtos = conn.queryByKeyList(DTOSchueler.class, ids);
		if (dtos.size() != ids.size())
			throw new ApiOperationException(Status.NOT_FOUND);
		final String zipname = "Laufbahnplanungen.zip";
		final byte[] zipdata;
		try {
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				try (ZipOutputStream zos = new ZipOutputStream(baos)) {
					for (final DTOSchueler dtoSchueler : dtos) {
						final GostLaufbahnplanungDaten daten = getLaufbahnplanungsdaten(dtoSchueler);
						final String filename = "Laufbahnplanung_%d_%s_%s_%s_%d.lp".formatted(daten.abiturjahr, daten.jahrgang, dtoSchueler.Nachname,
								dtoSchueler.Vorname, dtoSchueler.ID);
						final byte[] filedata = JSONMapper.gzipByteArrayFromObject(daten);
						zos.putNextEntry(new ZipEntry(filename));
						zos.write(filedata);
						zos.closeEntry();
					}
					baos.flush();
				}
				zipdata = baos.toByteArray();
			}
		} catch (@SuppressWarnings("unused") final IOException | CompressionException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		}
		return Response.ok(zipdata).header("Content-Disposition", "attachment; filename=\"" + zipname + "\"").build();
	}


	/**
	 * Erstellt den Export mit den Laufbahnplanungsdaten des
	 * angegebenen Schülers zur Bearbeitung in einem externen Tool.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response mit den Laufbahnplanungsdaten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response exportJSON(final long idSchueler) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (dtoSchueler == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final GostLaufbahnplanungDaten daten = getLaufbahnplanungsdaten(dtoSchueler);
		return Response.ok(daten).type(MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Führt den Import der Laufbahndaten des angegebenen Schülers aus den übergebenenen Laufbahnplanungsdaten
	 * durch. Dabei werde Fehler ggf. im logger protokolliert.
	 *
	 * @param conn                    die Datenbankverbindung
	 * @param dtoSchueler             der Schüler
	 * @param laufbahnplanungsdaten   die Laufbahnplanungsdaten
	 * @param logger                  der Logger
	 *
	 * @return true im Erfolgsfall
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static boolean doImport(final DBEntityManager conn, final DTOSchueler dtoSchueler, final GostLaufbahnplanungDaten laufbahnplanungsdaten,
			final Logger logger) throws ApiOperationException {
		// Lese die Informationen zur Schule ein und prüfe, ob die Schulnummer übereinstimmt.
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null) {
			logger.logLn("Fehler: Die Daten der Schule können nicht aus der Datenbank eingelesen werden.");
			return false;
		}
		if (laufbahnplanungsdaten.schulNr != schule.SchulNr) {
			logger.logLn("Fehler: Die Schulnummer der Planungsdatei simmt nicht mit der Schulnummer der Datenbank überein.");
			return false;
		}
		// Lese die allgemeinen Informationen des Schülers zu der Laufbahn ein.
		final DTOGostSchueler gostSchueler = conn.queryByKey(DTOGostSchueler.class, dtoSchueler.ID);
		if (gostSchueler == null) {
			logger.logLn("Fehler: Es konnte kein Eintrag für den Schüler mit der ID %d in der Laufbahnplung ermittelt werden.".formatted(dtoSchueler.ID));
			return false;
		}
		// Lese zunächst die Abiturdaten des Schülers ein, welche in der Datenbank gespeichert sind.
		final Abiturdaten abidaten = DBUtilsGostLaufbahn.get(conn, dtoSchueler.ID);
		final GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherManager(abidaten.schuljahrAbitur, conn, abidaten.abiturjahr);
		// Prüfe zunächst, ob der Abiturjahrgang in der Datenbank existiert und mit dem des Schülers übereinstimmt
		if (abidaten.abiturjahr != laufbahnplanungsdaten.abiturjahr) {
			logger.logLn("Fehler: Der Abiturjahrgang der Planungsdatei stimmt nicht mit dem Abiturjahrgang des Schülers überein.");
			return false;
		}
		// Bestimme die Daten des Schülers in den Laufbahnplanungsdaten
		final GostLaufbahnplanungDatenSchueler daten = laufbahnplanungsdaten.schueler.stream().filter(s -> s.id == dtoSchueler.ID).findFirst().orElse(null);
		if (daten == null) {
			logger.logLn("Fehler: Die Laufbahnplanungsdatei enthält keinen Schüler mit der ID " + dtoSchueler.ID + ".");
			return false;
		}
		// Überprüfe, ob die Schüler-ID in den Laufbahnplanungsdaten manipuliert wurde und damit eine falsch Zuordnung vorliegen würde
		final AES aes = DBUtilsSchueler.getOrCreateSchuelerAES(conn, dtoSchueler.ID);
		try {
			final long idDec = ByteBuffer.wrap(aes.decryptBase64(daten.idEnc)).getLong();
			if (idDec != daten.id)
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die ID des Schülers wurde verändert oder der AES-Schlüssel in der Datenbank wurde zwischenzeitlich angepasst. Die Daten können daher nicht geladen werden.");
		} catch (@SuppressWarnings("unused") final AESException e) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID des Schülers wurde verändert oder der AES-Schlüssel in der Datenbank wurde zwischenzeitlich angepasst. Die Daten können daher nicht geladen werden.");
		}

		// Prüfe den Bilingualen Bildungsgang
		if (((daten.bilingualeSprache == null) && (abidaten.bilingualeSprache != null))
				|| ((daten.bilingualeSprache != null) && (abidaten.bilingualeSprache == null))
				|| ((daten.bilingualeSprache != null) && !daten.bilingualeSprache.equals(abidaten.bilingualeSprache))) {
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
		for (final Sprachbelegung belegung : daten.sprachendaten.belegungen) {
			final Sprachbelegung vergleich = sprachBelegungen.get(belegung.sprache);
			if (vergleich == null) {
				logger.logLn("Fehler: Die Sprachbelegung für die Sprache " + belegung.sprache + " wurde in der Datenbank nicht gefunden.");
				return false;
			}
			final boolean vglReihenfolge = ((belegung.reihenfolge == null) && (vergleich.reihenfolge == null))
					|| ((belegung.reihenfolge != null) && (vergleich.reihenfolge != null)
							&& (belegung.reihenfolge.intValue() == vergleich.reihenfolge.intValue()));
			final boolean vglVonJg = ((belegung.belegungVonJahrgang == null) && (vergleich.belegungVonJahrgang == null))
					|| ((belegung.belegungVonJahrgang != null) && (vergleich.belegungVonJahrgang != null)
							&& (belegung.belegungVonJahrgang.equals(vergleich.belegungVonJahrgang)));
			final boolean vglVonAbschnitt = ((belegung.belegungVonAbschnitt == null) && (vergleich.belegungVonAbschnitt == null))
					|| ((belegung.belegungVonAbschnitt != null) && (vergleich.belegungVonAbschnitt != null)
							&& (belegung.belegungVonAbschnitt.equals(vergleich.belegungVonAbschnitt)));
			if (!vglReihenfolge || !vglVonJg || !vglVonAbschnitt) {
				logger.logLn("Fehler: Die Sprachbelegung für die Sprache " + belegung.sprache + " stimmt nicht mit der Eintragung in der Datenbank überein.");
				return false;
			}
		}
		final Map<String, Sprachpruefung> sprachPruefungen = abidaten.sprachendaten.pruefungen.stream().collect(Collectors.toMap(b -> b.sprache, b -> b));
		for (final Sprachpruefung pruefung : daten.sprachendaten.pruefungen) {
			final Sprachpruefung vergleich = sprachPruefungen.get(pruefung.sprache);
			if (vergleich == null) {
				logger.logLn("Fehler: Die Sprachprüfung für die Sprache " + pruefung.sprache + " wurde in der Datenbank nicht gefunden.");
				return false;
			}
			final boolean vglNiveau = ((pruefung.anspruchsniveauId == null) && (vergleich.anspruchsniveauId == null))
					|| ((pruefung.anspruchsniveauId != null) && (vergleich.anspruchsniveauId != null)
							&& (pruefung.anspruchsniveauId.intValue() == vergleich.anspruchsniveauId.intValue()));
			final boolean vglErsSprache = ((pruefung.ersetzteSprache == null) && (vergleich.ersetzteSprache == null))
					|| ((pruefung.ersetzteSprache != null) && (vergleich.ersetzteSprache != null)
							&& (pruefung.ersetzteSprache.equals(vergleich.ersetzteSprache)));
			if (!vglNiveau || !vglErsSprache
					|| (pruefung.kannErstePflichtfremdspracheErsetzen != vergleich.kannErstePflichtfremdspracheErsetzen)
					|| (pruefung.kannZweitePflichtfremdspracheErsetzen != vergleich.kannZweitePflichtfremdspracheErsetzen)
					|| (pruefung.kannWahlpflichtfremdspracheErsetzen != vergleich.kannWahlpflichtfremdspracheErsetzen)
					|| (pruefung.kannBelegungAlsFortgefuehrteSpracheErlauben != vergleich.kannBelegungAlsFortgefuehrteSpracheErlauben)) {
				logger.logLn("Fehler: Die Sprachprüfung für die Sprache " + pruefung.sprache
						+ " stimmt nicht nicht mit der Eintragung in der Datenbank überein.");
				return false;
			}
		}
		// Prüfe die Fachbelegungen bei den Fachbelegungen, wo bereits Leistungsdaten in der Datenbank hinterlegt sind und übernehme die restlichen Fachwahlen
		final Map<Long, AbiturFachbelegung> dbBelegungen = abidaten.fachbelegungen.stream().collect(Collectors.toMap(b -> b.fachID, b -> b));
		final Map<Long, GostLaufbahnplanungDatenFachbelegung> dateiBelegungen = daten.fachbelegungen.stream().collect(Collectors.toMap(b -> b.fachID, b -> b));
		for (final Long idFach : dateiBelegungen.keySet()) {
			final GostFach fach = gostFaecher.get(idFach);
			if (fach == null) {
				logger.logLn("Fehler: Das Fach mit der ID " + idFach
						+ " wird in der Datei verwendet, existiert aber nicht als Fach der gymnasialen Oberstufe in der Datenbank.");
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
				final String dbKursart = (db.belegungen[halbjahr.id] == null) ? null : db.belegungen[halbjahr.id].kursartKuerzel;
				final boolean dbSchriftlich = (db.belegungen[halbjahr.id] != null) && db.belegungen[halbjahr.id].schriftlich;
				final boolean istGleich = ((dbKursart == null) && (datei.kursart[halbjahr.id] == null))
						|| ((dbKursart != null) && (datei.kursart[halbjahr.id] != null)
								&& (dbKursart.equals(datei.kursart[halbjahr.id])) && (dbSchriftlich == datei.schriftlich[halbjahr.id]));
				if (abidaten.bewertetesHalbjahr[halbjahr.id]) {
					if (!istGleich) {
						logger.logLn("Fehler: Das Halbjahr " + halbjahr.kuerzel
								+ " ist in der Datenbank bereits in den Leistungsdaten enthalten. Die Laufbahnplanung muss hier für einen Import übereinstimmen.");
						return false;
					}
					continue;
				}
				if (!istGleich) {
					identisch = false;
					break;
				}
			}
			if (!identisch || ((db.abiturFach != null) && !db.abiturFach.equals(datei.abiturFach)) || ((db.abiturFach == null) && (datei.abiturFach != null)))
				tmp.add(idFach);
		}
		beide = tmp;
		for (final Long idFach : nurDatei) {
			// Prüfe, ob Fachbelegungen zu einem Halbjahr hinzugefügt werden sollen, die bereits Leistungsdaten enthalten
			final GostLaufbahnplanungDatenFachbelegung datei = dateiBelegungen.get(idFach);
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				if ((abidaten.bewertetesHalbjahr[halbjahr.id]) && (datei.kursart[halbjahr.id] != null)) {
					logger.logLn("Fehler: Das Halbjahr " + halbjahr.kuerzel
							+ " ist in der Datenbank bereits in den Leistungsdaten enthalten. Die Laufbahnplanung kann hier für einen Import keine Fachwahlen ergänzen.");
					return false;
				}
			}
		}
		for (final Long idFach : nurDB) {
			// Prüfe, ob Fachbelegungen aus Halbjahres entfernt werden sollen, die bereits Leistungsdaten enthalten
			final AbiturFachbelegung db = dbBelegungen.get(idFach);
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				if ((abidaten.bewertetesHalbjahr[halbjahr.id]) && (db.belegungen[halbjahr.id] != null)) {
					logger.logLn("Fehler: Das Halbjahr " + halbjahr.kuerzel
							+ " ist in der Datenbank bereits in den Leistungsdaten enthalten. Die Laufbahnplanung kann hier für einen Import keine Fachwahlen entfernen.");
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
			final List<DTOGostSchuelerFachbelegungen> fachwahlen =
					conn.queryList("SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Schueler_ID = ?1 AND e.Fach_ID IN ?2",
							DTOGostSchuelerFachbelegungen.class, dtoSchueler.ID, alle);
			final Map<Long, DTOGostSchuelerFachbelegungen> mapFachwahlen = fachwahlen.stream().collect(Collectors.toMap(f -> f.Fach_ID, f -> f));
			for (final Long idFach : Stream.concat(beide.stream(), nurDatei.stream()).collect(Collectors.toSet())) {
				final GostLaufbahnplanungDatenFachbelegung datei = dateiBelegungen.get(idFach);
				DTOGostSchuelerFachbelegungen fachwahl = mapFachwahlen.get(idFach);
				// Ergänze ggf. Fachwahl-Einträge, welche zwar durch Leistungsdaten bestehen, aber nicht wirklich in der DB abgelegt sind.
				if (fachwahl == null)
					fachwahl = new DTOGostSchuelerFachbelegungen(dtoSchueler.ID, idFach);
				for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
					final String dateiKursart = datei.kursart[halbjahr.id];
					final String kursart = (dateiKursart == null) ? null
							: ("AT".equals(dateiKursart) ? "AT"
									: (GostKursart.LK.kuerzel.equals(dateiKursart) ? "LK"
											: (GostKursart.ZK.kuerzel.equals(dateiKursart) ? "ZK"
													: (GostKursart.PJK.kuerzel.equals(dateiKursart) ? "M"
															: (GostKursart.VTF.kuerzel.equals(dateiKursart) ? "M"
																	: (datei.schriftlich[halbjahr.id] ? "S" : "M"))))));
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
				conn.transactionPersistAll(fachwahlenGeaendert);
			for (final Long idFach : nurDB) {
				final DTOGostSchuelerFachbelegungen fachwahl = mapFachwahlen.get(idFach);
				conn.transactionRemove(fachwahl);
			}
			// Und setzen des Rücklaufdatums
			gostSchueler.DatumRuecklauf = DateTimeFormatter.ISO_DATE.format(LocalDate.now(ZoneId.of("Europe/Berlin")));
			conn.transactionPersist(gostSchueler);
		} else {
			logger.logLn("Keine Änderungen für den Schüler mit der ID " + dtoSchueler.ID + " gegenüber der Datenbank in der Datei enthalten.");
		}
		return true;
	}


	/**
	 * Importiert die Daten des Schülers mit der angegebenen ID aus den übergebenen
	 * Laufbahnplanungsdatein.
	 *
	 * @param multipart   die Laufbahnplanungsdatein als GZIP-Komprimierte JSONs
	 *
	 * @return die HTTP-Response mit dem Log
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response importGZip(final MultipartFormDataInput multipart) throws ApiOperationException {
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Erstelle den Logger
		final Logger logger = new Logger();
		logger.copyConsumer(Logger.global());
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		boolean success = true;
		final List<InputPart> l = multipart.getFormDataMap().get("data");
		// Gehe die Dateien durch und führe jeweils dein Import durch
		for (final InputPart file : l) {
			final byte[] daten;
			try (InputStream input = file.getBody()) {
				daten = input.readAllBytes();
			} catch (final IOException e) {
				logger.log("Eine lp-Datei konnte nicht eingelesen werden: " + e.getMessage());
				success = false;
				break;
			}
			// Entpacke die ZIP-Datei
			GostLaufbahnplanungDaten laufbahnplanungsdaten = null;
			try {
				laufbahnplanungsdaten = JSONMapper.toObjectGZip(daten, GostLaufbahnplanungDaten.class);
			} catch (final CompressionException e) {
				logger.log("Fehler beim Öffnen der Datei.");
				logger.log("Fehlernachricht: " + e.getMessage());
				break;
			}
			if (laufbahnplanungsdaten.schueler.size() != 1) {
				logger.log("Es wurde keiner oder mehr als ein Schüler-Eintrag in der lp-Datei gefunden. Dies ist nicht zulässig");
				success = false;
				break;
			}
			// Prüfe, ob der Schüler überhaupt existiert
			final long idSchueler = laufbahnplanungsdaten.schueler.get(0).id;
			final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, idSchueler);
			if (dtoSchueler == null) {
				logger.log("Der Schüler mit der ID %d wurde nicht in der Datenbank gefunden.".formatted(idSchueler));
				success = false;
				break;
			}
			// Importiere die Daten...
			if (!doImport(conn, dtoSchueler, laufbahnplanungsdaten, logger)) {
				success = false;
				break;
			}
		}
		// Prüfe, ob ggf. ein Rollback nötig ist
		if (!success)
			conn.transactionRollbackOrThrow();
		// Führe den Import durch und erstelle die Response mit dem Log
		final SimpleOperationResponse daten = new SimpleOperationResponse();
		daten.success = success;
		logger.logLn("Import " + (daten.success ? "erfolgreich." : "fehlgeschlagen."));
		daten.log = log.getStrings();
		return Response.status(daten.success ? Status.OK : Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Importiert die Daten des Schülers mit der angegebenen ID aus der übergebenen
	 * Laufbahnplanungsdatei.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param data         die Laufbahnplanungsdatei als GZIP-Komprimiertes JSON
	 *
	 * @return die HTTP-Response mit dem Log
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response importGZip(final long idSchueler, final byte[] data) throws ApiOperationException {
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat und ob der Schüler überhaupt existiert.
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (dtoSchueler == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		// Erstelle den Logger
		final Logger logger = new Logger();
		logger.copyConsumer(Logger.global());
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
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
		daten.success = doImport(conn, dtoSchueler, laufbahnplanungsdaten, logger);
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
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response importJSON(final long idSchueler, final GostLaufbahnplanungDaten laufbahnplanungsdaten) throws ApiOperationException {
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat und ob der Schüler überhaupt existiert.
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (dtoSchueler == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		// Erstelle den Logger
		final Logger logger = new Logger();
		logger.copyConsumer(Logger.global());
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);
		// Führe den Import durch und erstelle die Response mit dem Log
		final SimpleOperationResponse daten = new SimpleOperationResponse();
		daten.success = doImport(conn, dtoSchueler, laufbahnplanungsdaten, logger);
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
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response pruefeBelegungAbiturjahrgang(final int abiturjahr, final GostBelegpruefungsArt pruefungsArt) throws ApiOperationException {
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat und ob der Schüler überhaupt existiert.
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final int schuljahr = abiturjahr - 1;
		final List<DTOSchueler> listSchuelerDTOs = (new DataGostJahrgangSchuelerliste(conn, abiturjahr)).getSchuelerDTOs();
		final List<Long> listSchuelerIDs = listSchuelerDTOs.stream().map(s -> s.ID).toList();
		final Map<Long, DTOGostSchueler> mapGostSchuelerDTOs = conn.queryByKeyList(DTOGostSchueler.class, listSchuelerIDs)
				.stream().collect(Collectors.toMap(s -> s.Schueler_ID, s -> s));

		// Erstelle das DTO für die Eregbnisrückmeldung
		final List<GostBelegpruefungsErgebnisse> daten = new ArrayList<>();

		// Bestimme die Jahrgangsdaten des Abiturjahrgangs
		final @NotNull GostJahrgangsdaten jahrgangsdaten = DataGostJahrgangsdaten.getJahrgangsdaten(conn, abiturjahr);

		// Bestimme die Fächer, welche in dem Abiturjahrgang vorhanden sind.
		final @NotNull GostFaecherManager faecherManager = DBUtilsFaecherGost.getFaecherManager(schuljahr, conn, abiturjahr);

		// Bestimme die nicht erlaubten und die geforderten Fächerkombinationen des Abiturjahrgangs
		faecherManager.addFachkombinationenAll(DataGostJahrgangFachkombinationen.getFachkombinationen(conn, abiturjahr));

		// Führe für alle Schüler nacheinander die Belegprüfung durch
		for (final DTOSchueler dtoSchueler : listSchuelerDTOs) {
			final SchuelerStatus status = SchuelerStatus.data().getWertByID(dtoSchueler.idStatus);
			if ((status != SchuelerStatus.AKTIV) && (status != SchuelerStatus.EXTERN) && (status != SchuelerStatus.NEUAUFNAHME)
					&& (status != SchuelerStatus.WARTELISTE))
				continue;

			// Bestimme die Laufbahndaten des Schülers
			final Abiturdaten abidaten = DBUtilsGostLaufbahn.get(conn, dtoSchueler.ID);

			// Bestimme die Beratungsdaten des Schülers
			final DTOGostSchueler gostSchueler = mapGostSchuelerDTOs.get(dtoSchueler.ID);

			// Erzeuge das Ergebnis-DTO für die Rückgabe
			final GostBelegpruefungsErgebnisse ergebnisse = new GostBelegpruefungsErgebnisse();

			// Führe die Belegprüfung für den Schüler durch
			final AbiturdatenManager abiManager =
					new AbiturdatenManager(SVWSKonfiguration.get().getServerMode(), abidaten, jahrgangsdaten, faecherManager, pruefungsArt);
			ergebnisse.ergebnis = abiManager.getBelegpruefungErgebnis();
			ergebnisse.hatFachwahlen = abiManager.existsFachbelegung();
			ergebnisse.beratungsDatum = (gostSchueler == null) ? null : gostSchueler.DatumBeratung;
			ergebnisse.ruecklaufDatum = (gostSchueler == null) ? null : gostSchueler.DatumRuecklauf;

			// F+lle das zugehörige Schüler-DTO
			ergebnisse.schueler.id = dtoSchueler.ID;
			ergebnisse.schueler.vorname = dtoSchueler.Vorname;
			ergebnisse.schueler.nachname = dtoSchueler.Nachname;
			ergebnisse.schueler.status = dtoSchueler.idStatus;
			ergebnisse.schueler.geschlecht = dtoSchueler.Geschlecht.id;

			// Schreibe das Ergebnis in die Rückmeldung
			daten.add(ergebnisse);
		}

		daten.sort((a, b) -> {
			final Collator collator = Collator.getInstance(Locale.GERMAN);
			if ((a.schueler.nachname == null) && (b.schueler.nachname != null))
				return -1;
			else if ((a.schueler.nachname != null) && (b.schueler.nachname == null))
				return 1;
			else if ((a.schueler.nachname == null) && (b.schueler.nachname == null))
				return 0;
			int result = collator.compare(a.schueler.nachname, b.schueler.nachname);
			if (result == 0) {
				if ((a.schueler.vorname == null) && (b.schueler.vorname != null))
					return -1;
				else if ((a.schueler.vorname != null) && (b.schueler.vorname == null))
					return 1;
				else if ((a.schueler.vorname == null) && (b.schueler.vorname == null))
					return 0;
				result = collator.compare(a.schueler.vorname, b.schueler.vorname);
			}
			return result;
		});

		// Erzeuge die Response mit den Belegprüfungsergebnissen
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Setzt die Fachwahlen für den angegebenen Schüler zurück.
	 * Liegen bereits bewertete Halbjahre vor, so werden die zukünftigen Fachwahlen entfernt.
	 * Ansonsten wir die Vorlage für die Fachwahlen des Abiturjahrgangs übernommen.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return Die HTTP-Response der Operation
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response reset(final long idSchueler) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final Abiturdaten abidaten = DBUtilsGostLaufbahn.get(conn, idSchueler);
		final DTOGostJahrgangsdaten jahrgang = conn.queryByKey(DTOGostJahrgangsdaten.class, abidaten.abiturjahr);
		if (!abidaten.bewertetesHalbjahr[GostHalbjahr.EF1.id]) {
			DataGostJahrgangLaufbahnplanung.transactionResetSchueler(conn, jahrgang, idSchueler);
			return Response.status(Status.NO_CONTENT).build();
		}
		final List<DTOGostSchuelerFachbelegungen> fachwahlen = conn.queryList(DTOGostSchuelerFachbelegungen.QUERY_BY_SCHUELER_ID,
				DTOGostSchuelerFachbelegungen.class, idSchueler);
		for (final DTOGostSchuelerFachbelegungen fw : fachwahlen) {
			fw.AbiturFach = null;
			if (!abidaten.bewertetesHalbjahr[GostHalbjahr.EF1.id])
				fw.EF1_Kursart = null;
			if (!abidaten.bewertetesHalbjahr[GostHalbjahr.EF2.id])
				fw.EF2_Kursart = null;
			if (!abidaten.bewertetesHalbjahr[GostHalbjahr.Q11.id])
				fw.Q11_Kursart = null;
			if (!abidaten.bewertetesHalbjahr[GostHalbjahr.Q12.id])
				fw.Q12_Kursart = null;
			if (!abidaten.bewertetesHalbjahr[GostHalbjahr.Q21.id])
				fw.Q21_Kursart = null;
			if (!abidaten.bewertetesHalbjahr[GostHalbjahr.Q22.id])
				fw.Q22_Kursart = null;
			conn.transactionPersist(fw);
		}
		return Response.status(Status.NO_CONTENT).build();
	}

	/**
	 * Löscht die Fachwahlen für den angegebenen Schüler.
	 * Liegen bereits bewertete Halbjahre vor, so wird der
	 * Fehlercode 409 CONFLICT zurückgegeben.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return Die HTTP-Response der Operation
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response delete(final long idSchueler) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final Abiturdaten abidaten = DBUtilsGostLaufbahn.get(conn, idSchueler);
		for (final GostHalbjahr hj : GostHalbjahr.values())
			if (abidaten.bewertetesHalbjahr[hj.id])
				throw new ApiOperationException(Status.CONFLICT, "Die Fachwahlen können nicht vollständig gelöscht werden, da bereits bewertete Abschnitt vorliegen.");
		final List<DTOGostSchuelerFachbelegungen> fachwahlen = conn.queryList(DTOGostSchuelerFachbelegungen.QUERY_BY_SCHUELER_ID,
				DTOGostSchuelerFachbelegungen.class, idSchueler);
		for (final DTOGostSchuelerFachbelegungen fw : fachwahlen)
			conn.transactionRemove(fw);
		return Response.status(Status.NO_CONTENT).build();
	}

}
