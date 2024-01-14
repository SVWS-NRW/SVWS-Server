package de.svws_nrw.data.faecher;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.types.fach.Fachgruppe;
import de.svws_nrw.core.types.jahrgang.Jahrgaenge;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.schule.SchulUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFaecher;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;

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
	 * @param fach       das Datenbank-Objekt
	 * @param faecher    eine Map mit Fach-Informationen
	 *
	 * @return das {@link GostFach}-Objekt
	 */
	public static GostFach mapFromDTOFach(final DTOFach fach, final Map<Long, DTOFach> faecher) {
		final GostFach eintrag = new GostFach();
		eintrag.id = fach.ID;
		eintrag.kuerzel = fach.StatistikFach.daten.kuerzelASD;
		eintrag.kuerzelAnzeige = fach.Kuerzel;
		eintrag.bezeichnung = fach.Bezeichnung;
		eintrag.sortierung = fach.SortierungAllg;
		eintrag.istFremdsprache = fach.IstFremdsprache;
		eintrag.istFremdSpracheNeuEinsetzend = fach.IstMoeglichAlsNeueFremdspracheInSekII;
		eintrag.biliSprache = ((fach.Unterichtssprache != null) && (!"".equals(fach.Unterichtssprache)) && (!"D".equals(fach.Unterichtssprache))) ? fach.Unterichtssprache.substring(0, 1) : null;
		eintrag.istMoeglichAbiLK = fach.IstMoeglichAbiLK;
		eintrag.istMoeglichAbiGK = fach.IstMoeglichAbiGK;
		eintrag.istMoeglichEF1 = fach.IstMoeglichEF1;
		eintrag.istMoeglichEF2 = fach.IstMoeglichEF2;
		eintrag.istMoeglichQ11 = fach.IstMoeglichQ11;
		eintrag.istMoeglichQ12 = fach.IstMoeglichQ12;
		eintrag.istMoeglichQ21 = fach.IstMoeglichQ21;
		eintrag.istMoeglichQ22 = fach.IstMoeglichQ22;
		final int defaultWochenstundenQ_GK = Jahrgaenge.JG_EF == fach.StatistikFach.getJahrgangAb() ? 4 : 3;
		final int defaultWochenstundenQ = (fach.StatistikFach.getFachgruppe() == Fachgruppe.FG_VX || fach.StatistikFach.getFachgruppe() == Fachgruppe.FG_PX) ? 2 : defaultWochenstundenQ_GK;
		eintrag.wochenstundenQualifikationsphase = fach.WochenstundenQualifikationsphase == null ? defaultWochenstundenQ : fach.WochenstundenQualifikationsphase;
		eintrag.projektKursLeitfach1ID = fach.ProjektKursLeitfach1_ID;
		if (fach.ProjektKursLeitfach1_ID == null) {
			eintrag.projektKursLeitfach1Kuerzel = null;
		} else {
			final DTOFach dtoFach = faecher.get(fach.ProjektKursLeitfach1_ID);
			eintrag.projektKursLeitfach1Kuerzel = ((dtoFach == null) || (dtoFach.StatistikFach == null)) ? null : dtoFach.StatistikFach.daten.kuerzelASD;
		}
		eintrag.projektKursLeitfach2ID = fach.ProjektKursLeitfach2_ID;
		if (fach.ProjektKursLeitfach2_ID == null) {
			eintrag.projektKursLeitfach2Kuerzel = null;
		} else {
			final DTOFach dtoFach = faecher.get(fach.ProjektKursLeitfach2_ID);
			eintrag.projektKursLeitfach2Kuerzel = ((dtoFach == null) || (dtoFach.StatistikFach == null)) ? null : dtoFach.StatistikFach.daten.kuerzelASD;
		}
		return eintrag;
	}


	/**
	 * Diese Methode erstellt ein {@link GostFach}-Objekt mit den Daten aus dem Datenbank-Objekt
	 * von Typ {@link DTOGostJahrgangFaecher}. Dabei werden Informationen aus der übergebenen Liste
	 * der Fächer verwendet.
	 *
	 * @param idFach     die ID des Faches
	 * @param jf         das Datenbank-Objekt
	 * @param faecher    eine Map mit Fach-Informationen
	 *
	 * @return das {@link GostFach}-Objekt
	 */
	public static GostFach mapFromDTOGostJahrgangFaecher(final long idFach, final DTOGostJahrgangFaecher jf, final Map<Long, DTOFach> faecher) {
		if ((jf != null) && (idFach != jf.Fach_ID))
			return null;
		final DTOFach fach = faecher.get(idFach);
		if (fach == null)
			return null;
		final GostFach eintrag = new GostFach();
		eintrag.id = fach.ID;
		eintrag.kuerzel = fach.StatistikFach.daten.kuerzelASD;
		eintrag.kuerzelAnzeige = fach.Kuerzel;
		eintrag.bezeichnung = fach.Bezeichnung;
		eintrag.sortierung = fach.SortierungAllg;
		eintrag.istFremdsprache = fach.IstFremdsprache;
		eintrag.istFremdSpracheNeuEinsetzend = fach.IstMoeglichAlsNeueFremdspracheInSekII;
		eintrag.biliSprache = ((fach.Unterichtssprache != null) && (!"".equals(fach.Unterichtssprache)) && (!"D".equals(fach.Unterichtssprache))) ? fach.Unterichtssprache.substring(0, 1) : null;
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
		final int defaultWochenstundenQ_GK = Jahrgaenge.JG_EF == fach.StatistikFach.getJahrgangAb() ? 4 : 3;
		final int defaultWochenstundenQ = (fach.StatistikFach.getFachgruppe() == Fachgruppe.FG_VX || fach.StatistikFach.getFachgruppe() == Fachgruppe.FG_PX) ? 2 : defaultWochenstundenQ_GK;
		if ((jf == null) || (jf.WochenstundenQPhase == null)) {
			eintrag.wochenstundenQualifikationsphase = fach.WochenstundenQualifikationsphase == null ? defaultWochenstundenQ : fach.WochenstundenQualifikationsphase;
		} else {
			eintrag.wochenstundenQualifikationsphase = jf.WochenstundenQPhase;
		}
		eintrag.projektKursLeitfach1ID = fach.ProjektKursLeitfach1_ID;
		eintrag.projektKursLeitfach1Kuerzel = fach.ProjektKursLeitfach1_ID == null ? null : faecher.get(fach.ProjektKursLeitfach1_ID).StatistikFach.daten.kuerzelASD;
		eintrag.projektKursLeitfach2ID = fach.ProjektKursLeitfach2_ID;
		eintrag.projektKursLeitfach2Kuerzel = fach.ProjektKursLeitfach2_ID == null ? null : faecher.get(fach.ProjektKursLeitfach2_ID).StatistikFach.daten.kuerzelASD;
		return eintrag;
	}


	/**
	 * Ermittelt die Liste aller Fächer der gymnasialen Oberstufe
	 *
	 * @param conn          die Datenbank-Verbindung
	 * @param abiJahrgang   der Abiturjahrgang, für den die Liste erstellt werden soll
	 *
	 * @return die Liste aller Fächer der gymnasialen Oberstufe
	 */
	public static @NotNull GostFaecherManager getFaecherListeGost(final DBEntityManager conn, final Integer abiJahrgang) {
		return getListeGOStFaecher(conn, abiJahrgang, false);
    }

	/**
	 * Ermittelt die Liste aller Fächer der gymnasialen Oberstufe, die in mindestens einem Halbjahr des Abiturjahrgangs wählbar sind.
	 *
	 * @param conn          die Datenbank-Verbindung
	 * @param abiJahrgang   der Abiturjahrgang, für den die Liste erstellt werden soll
	 *
	 * @return die Liste aller Fächer der gymnasialen Oberstufe die in mindestens einem Halbjahr des Abiturjahrgangs wählbar sind.
	 */
	public static @NotNull GostFaecherManager getNurWaehlbareFaecherListeGost(final DBEntityManager conn, final Integer abiJahrgang) {
		return getListeGOStFaecher(conn, abiJahrgang, true);
	}

	/**
	 * Ermittelt die Liste aller Fächer der gymnasialen Oberstufe, je nach Parameter alle oder nur die in mindestens einem Halbjahr anwählbaren Fächer.
	 *
	 * @param conn          		die Datenbank-Verbindung
	 * @param abiJahrgang   		der Abiturjahrgang, für den die Liste erstellt werden soll
	 * @param nurWaehlbareFaecher   legt fest, ob nur Fächer zurückgegeben werden, die in mindestens einem Halbjahr angewählt werden können.
	 *
	 * @return die Liste aller Fächer der gymnasialen Oberstufe
	 */
	private static @NotNull GostFaecherManager getListeGOStFaecher(final DBEntityManager conn, final Integer abiJahrgang, final boolean nurWaehlbareFaecher) {
		final @NotNull DTOEigeneSchule schule = SchulUtils.getDTOSchule(conn);
		if ((schule.Schulform.daten == null) || (!schule.Schulform.daten.hatGymOb))
			return new GostFaecherManager();
		final Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		if (faecher == null)
			throw OperationError.NOT_FOUND.exception();
		if ((abiJahrgang == null) || (abiJahrgang == -1)) {
			return new GostFaecherManager(faecher.values().stream().filter(fach -> fach.IstOberstufenFach)
				.map(fach -> mapFromDTOFach(fach, faecher)).filter(Objects::nonNull).toList()
			);
		}

		final Map<Long, DTOGostJahrgangFaecher> jahrgangfaecher = conn.queryNamed("DTOGostJahrgangFaecher.abi_jahrgang", abiJahrgang, DTOGostJahrgangFaecher.class)
			.stream().collect(Collectors.toMap(f -> f.Fach_ID, f -> f));
		List<GostFach> tmpFaecher = faecher.values().stream().filter(fach -> fach.IstOberstufenFach)
			.map(fach -> mapFromDTOGostJahrgangFaecher(fach.ID, jahrgangfaecher.get(fach.ID), faecher)).filter(Objects::nonNull).toList();
		if (nurWaehlbareFaecher)
			tmpFaecher = tmpFaecher.stream().filter(f -> (f.istMoeglichEF1 || f.istMoeglichEF2 || f.istMoeglichQ11 || f.istMoeglichQ12 || f.istMoeglichQ21 || f.istMoeglichQ22)).toList();
		return new GostFaecherManager(tmpFaecher);
	}




}
