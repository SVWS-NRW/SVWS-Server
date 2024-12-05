package de.svws_nrw.data.faecher;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.types.fach.Fachgruppe;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFaecher;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse stellt Hilfsmethoden für den Zugriff auf Informationen
 * zu Fächern der gymnasialen Oberstufe zur Verfügung.
 */
public final class DBUtilsFaecherGost {

	private DBUtilsFaecherGost() {
		throw new IllegalStateException("Instantiation of " + DBUtilsFaecherGost.class.getName() + " not allowed");
	}

	/**
	 * Diese Methode erstellt ein {@link GostFach}-Objekt mit den Daten aus dem Datenbank-Objekt
	 * von Typ {@link DTOFach}. Dabei werden Informationen aus der Liste der Fächer verwendet.
	 *
	 * @param schuljahr   das Schuljahr, auf welches sich die Anfrage mit dem Mapping bezieht
	 * @param fach        das Datenbank-Objekt
	 * @param faecher     eine Map mit Fach-Informationen
	 *
	 * @return das {@link GostFach}-Objekt
	 */
	public static GostFach mapFromDTOFach(final int schuljahr, final DTOFach fach, final Map<Long, DTOFach> faecher) {
		final GostFach eintrag = new GostFach();
		eintrag.id = fach.ID;
		eintrag.kuerzel = fach.StatistikKuerzel;
		eintrag.kuerzelAnzeige = fach.Kuerzel;
		eintrag.bezeichnung = fach.Bezeichnung;
		eintrag.sortierung = fach.SortierungAllg;
		eintrag.istPruefungsordnungsRelevant = fach.IstPruefungsordnungsRelevant;
		eintrag.istFremdsprache = fach.IstFremdsprache;
		eintrag.istFremdSpracheNeuEinsetzend = fach.IstMoeglichAlsNeueFremdspracheInSekII;
		eintrag.biliSprache = ((fach.Unterrichtssprache != null) && (!"".equals(fach.Unterrichtssprache)) && (!"D".equals(fach.Unterrichtssprache)))
				? fach.Unterrichtssprache.substring(0, 1) : null;
		eintrag.istMoeglichAbiLK = fach.IstMoeglichAbiLK;
		eintrag.istMoeglichAbiGK = fach.IstMoeglichAbiGK;
		eintrag.istMoeglichEF1 = fach.IstMoeglichEF1;
		eintrag.istMoeglichEF2 = fach.IstMoeglichEF2;
		eintrag.istMoeglichQ11 = fach.IstMoeglichQ11;
		eintrag.istMoeglichQ12 = fach.IstMoeglichQ12;
		eintrag.istMoeglichQ21 = fach.IstMoeglichQ21;
		eintrag.istMoeglichQ22 = fach.IstMoeglichQ22;
		final Fach tmpFach = Fach.getBySchluesselOrDefault(fach.StatistikKuerzel);
		final int defaultWochenstundenQ_GK = (Jahrgaenge.EF == tmpFach.getJahrgangAb(schuljahr)) ? 4 : 3;
		final int defaultWochenstundenQ = ((tmpFach.getFachgruppe(schuljahr) == Fachgruppe.FG_VX) || (tmpFach.getFachgruppe(schuljahr) == Fachgruppe.FG_PX))
				? 2 : defaultWochenstundenQ_GK;
		eintrag.wochenstundenQualifikationsphase =
				(fach.WochenstundenQualifikationsphase == null) ? defaultWochenstundenQ : fach.WochenstundenQualifikationsphase;
		eintrag.projektKursLeitfach1ID = fach.ProjektKursLeitfach1_ID;
		if (fach.ProjektKursLeitfach1_ID == null) {
			eintrag.projektKursLeitfach1Kuerzel = null;
		} else {
			final DTOFach dtoFach = faecher.get(fach.ProjektKursLeitfach1_ID);
			eintrag.projektKursLeitfach1Kuerzel = ((dtoFach == null) || (dtoFach.StatistikKuerzel == null)) ? null : dtoFach.StatistikKuerzel;
		}
		eintrag.projektKursLeitfach2ID = fach.ProjektKursLeitfach2_ID;
		if (fach.ProjektKursLeitfach2_ID == null) {
			eintrag.projektKursLeitfach2Kuerzel = null;
		} else {
			final DTOFach dtoFach = faecher.get(fach.ProjektKursLeitfach2_ID);
			eintrag.projektKursLeitfach2Kuerzel = ((dtoFach == null) || (dtoFach.StatistikKuerzel == null)) ? null : dtoFach.StatistikKuerzel;
		}
		return eintrag;
	}


	/**
	 * Diese Methode erstellt ein {@link GostFach}-Objekt mit den Daten aus dem Datenbank-Objekt
	 * von Typ {@link DTOGostJahrgangFaecher}. Dabei werden Informationen aus der übergebenen Liste
	 * der Fächer verwendet.
	 *
	 * @param schuljahr   das Schuljahr, auf welches sich die Anfrage mit dem Mapping bezieht
	 * @param idFach     die ID des Faches
	 * @param jf         das Datenbank-Objekt
	 * @param faecher    eine Map mit Fach-Informationen
	 *
	 * @return das {@link GostFach}-Objekt
	 */
	public static GostFach mapFromDTOGostJahrgangFaecher(final int schuljahr, final long idFach, final DTOGostJahrgangFaecher jf,
			final Map<Long, DTOFach> faecher) {
		if ((jf != null) && (idFach != jf.Fach_ID))
			return null;
		final DTOFach fach = faecher.get(idFach);
		if (fach == null)
			return null;
		final GostFach eintrag = new GostFach();
		eintrag.id = fach.ID;
		eintrag.kuerzel = fach.StatistikKuerzel;
		eintrag.kuerzelAnzeige = fach.Kuerzel;
		eintrag.bezeichnung = fach.Bezeichnung;
		eintrag.sortierung = fach.SortierungAllg;
		eintrag.istPruefungsordnungsRelevant = fach.IstPruefungsordnungsRelevant;
		eintrag.istFremdsprache = fach.IstFremdsprache;
		eintrag.istFremdSpracheNeuEinsetzend = fach.IstMoeglichAlsNeueFremdspracheInSekII;
		eintrag.biliSprache = ((fach.Unterrichtssprache != null) && (!"".equals(fach.Unterrichtssprache)) && (!"D".equals(fach.Unterrichtssprache)))
				? fach.Unterrichtssprache.substring(0, 1) : null;
		if (jf != null) { // Ansonsten ist alles mit false initialisiert
			eintrag.istMoeglichAbiLK = jf.WaehlbarAbiLK;
			eintrag.istMoeglichAbiGK = jf.WaehlbarAbiGK;
			eintrag.istMoeglichEF1 = jf.WaehlbarEF1;
			eintrag.istMoeglichEF2 = jf.WaehlbarEF2;
			eintrag.istMoeglichQ11 = jf.WaehlbarQ11;
			eintrag.istMoeglichQ12 = jf.WaehlbarQ12;
			eintrag.istMoeglichQ21 = jf.WaehlbarQ21;
			eintrag.istMoeglichQ22 = jf.WaehlbarQ22;
		}
		final Fach tmpFach = Fach.getBySchluesselOrDefault(fach.StatistikKuerzel);
		final int defaultWochenstundenQ_GK = (Jahrgaenge.EF == tmpFach.getJahrgangAb(schuljahr)) ? 4 : 3;
		final int defaultWochenstundenQ = ((tmpFach.getFachgruppe(schuljahr) == Fachgruppe.FG_VX) || (tmpFach.getFachgruppe(schuljahr) == Fachgruppe.FG_PX))
				? 2 : defaultWochenstundenQ_GK;
		if ((jf == null) || (jf.WochenstundenQPhase == null)) {
			eintrag.wochenstundenQualifikationsphase = (fach.WochenstundenQualifikationsphase == null)
					? defaultWochenstundenQ : fach.WochenstundenQualifikationsphase;
		} else {
			eintrag.wochenstundenQualifikationsphase = jf.WochenstundenQPhase;
		}
		eintrag.projektKursLeitfach1ID = fach.ProjektKursLeitfach1_ID;
		eintrag.projektKursLeitfach1Kuerzel = (fach.ProjektKursLeitfach1_ID == null)
				? null : faecher.get(fach.ProjektKursLeitfach1_ID).StatistikKuerzel;
		eintrag.projektKursLeitfach2ID = fach.ProjektKursLeitfach2_ID;
		eintrag.projektKursLeitfach2Kuerzel = (fach.ProjektKursLeitfach2_ID == null)
				? null : faecher.get(fach.ProjektKursLeitfach2_ID).StatistikKuerzel;
		return eintrag;
	}


	/**
	 * Ermittelt die Liste aller Fächer der gymnasialen Oberstufe
	 *
	 * @param schuljahr     das Schuljahr, auf welches sich die Anfrage bezieht
	 * @param conn          die Datenbank-Verbindung
	 * @param abiJahrgang   der Abiturjahrgang, für den die Liste erstellt werden soll
	 *
	 * @return die Liste aller Fächer der gymnasialen Oberstufe
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static @NotNull GostFaecherManager getFaecherManager(final int schuljahr, final DBEntityManager conn, final Integer abiJahrgang)
			throws ApiOperationException {
		return getGostFaecherManager(schuljahr, conn, abiJahrgang, false);
	}

	/**
	 * Ermittelt die Liste aller Fächer der gymnasialen Oberstufe, die in mindestens einem Halbjahr des Abiturjahrgangs wählbar sind.
	 *
	 * @param schuljahr     das Schuljahr, auf welches sich die Anfrage bezieht
	 * @param conn          die Datenbank-Verbindung
	 * @param abiJahrgang   der Abiturjahrgang, für den die Liste erstellt werden soll
	 *
	 * @return die Liste aller Fächer der gymnasialen Oberstufe die in mindestens einem Halbjahr des Abiturjahrgangs wählbar sind.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static @NotNull GostFaecherManager getNurWaehlbareFaecherListeGost(final int schuljahr, final DBEntityManager conn, final Integer abiJahrgang)
			throws ApiOperationException {
		return getGostFaecherManager(schuljahr, conn, abiJahrgang, true);
	}

	/**
	 * Ermittelt die Liste aller Fächer der gymnasialen Oberstufe, je nach Parameter alle oder nur die in mindestens einem Halbjahr anwählbaren Fächer.
	 *
	 * @param schuljahr             das Schuljahr, auf welches sich die Anfrage bezieht
	 * @param conn          		die Datenbank-Verbindung
	 * @param abiJahrgang   		der Abiturjahrgang, für den die Liste erstellt werden soll
	 * @param nurWaehlbareFaecher   legt fest, ob nur Fächer zurückgegeben werden, die in mindestens einem Halbjahr angewählt werden können.
	 *
	 * @return die Liste aller Fächer der gymnasialen Oberstufe
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static @NotNull GostFaecherManager getGostFaecherManager(final int schuljahr, final DBEntityManager conn, final Integer abiJahrgang,
			final boolean nurWaehlbareFaecher)
			throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		if (faecher == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		if ((abiJahrgang == null) || (abiJahrgang == -1)) {
			final @NotNull List<GostFach> tmpFaecher = faecher.values().stream().filter(fach -> fach.IstOberstufenFach)
					.map(fach -> mapFromDTOFach(schuljahr, fach, faecher)).filter(Objects::nonNull).toList();
			return new GostFaecherManager(schuljahr, tmpFaecher);
		}

		final Map<Long, DTOGostJahrgangFaecher> jahrgangfaecher =
				conn.queryList(DTOGostJahrgangFaecher.QUERY_BY_ABI_JAHRGANG, DTOGostJahrgangFaecher.class, abiJahrgang)
						.stream().collect(Collectors.toMap(f -> f.Fach_ID, f -> f));
		List<GostFach> tmpFaecher = faecher.values().stream().filter(fach -> fach.IstOberstufenFach)
				.map(fach -> mapFromDTOGostJahrgangFaecher(schuljahr, fach.ID, jahrgangfaecher.get(fach.ID), faecher)).filter(Objects::nonNull).toList();
		if (nurWaehlbareFaecher)
			tmpFaecher = tmpFaecher.stream()
					.filter(f -> (f.istMoeglichEF1 || f.istMoeglichEF2 || f.istMoeglichQ11 || f.istMoeglichQ12 || f.istMoeglichQ21 || f.istMoeglichQ22))
					.toList();
		return new GostFaecherManager(schuljahr, tmpFaecher);
	}

}
