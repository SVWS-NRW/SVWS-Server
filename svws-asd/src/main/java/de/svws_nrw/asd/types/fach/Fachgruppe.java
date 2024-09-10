package de.svws_nrw.asd.types.fach;

import java.util.HashMap;
import java.util.Set;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.RGBFarbe;
import de.svws_nrw.asd.data.fach.FachgruppeKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für den Zeugnisdruck erforderlichen Fachgruppen
 */
public enum Fachgruppe implements CoreType<FachgruppeKatalogEintrag, Fachgruppe> {

	/** Fachgruppe Deutsch */
	FG_D,

	/** Fachgruppe Arbeitslehre */
	FG_AL,

	/** Fachgruppe Fremdsprachen */
	FG_FS,

	/** Fachgruppe Kunst und Musik */
	FG_MS,

	/** Fachgruppe Literatur, instrumental- oder vokalpraktischer Kurs */
	FG_ME,

	/** Fachgruppe Gesellschaftswissenschaft */
	FG_GS,

	/** Fachgruppe Philosophie */
	FG_PL,

	/** Fachgruppe Religion */
	FG_RE,

	/** Fachgruppe Mathematik */
	FG_M,

	/** Fachgruppe Naturwissenschaften */
	FG_NW,

	/** Fachgruppe weiteres naturwissenschaftliches / technisches Fach */
	FG_WN,

	/** Fachgruppe Sport */
	FG_SP,

	/** Fachgruppe Vertiefungskurs */
	FG_VX,

	/** Fachgruppe Projektkurs */
	FG_PX,

	/** Fachgruppe Berufsübergreifender Bereich */
	FG_BUE,

	/** Fachgruppe Berufsbezogener Bereich */
	FG_BBS,

	/** Fachgruppe Berufsbezogener Bereich (Schwerpunkt) */
	FG_BBS_SCHWERPUNKT,

	/** Fachgruppe Differenzierungsbereich */
	FG_DF,

	/** Fachgruppe Berufspraktikum */
	FG_BP,

	/** Fachgruppe besondere Lernleistung */
	FG_BLL,

	/** Fachgruppe Wahlpflichtbereich */
	FG_WP,

	/** Fachgruppe Zusätzliche Unterrichtsveranstaltungen */
	FG_ZUV,

	/** Fachgruppe Angleichungskurse */
	FG_ANG,

	/** Fachgruppe Sprache */
	FG_D_SP,

	/** Fachgruppe Sachunterricht */
	FG_SU,

	/** Fachgruppe Förderunterricht */
	FG_FOE,

	/** Fachgruppe Abschlussarbeit */
	FG_ABA,

	/** Fachgruppe Projektarbeit */
	FG_PA,

	/** Fachgruppe Informatik (Sek I) */
	FG_IF;


	/** Die Menge der Schulformen. Diese ist nach der Initialisierung nicht leer. */
	private static final @NotNull HashMap<Long, Set<Schulform>> _mapSchulformenByID = new HashMap<>();


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<FachgruppeKatalogEintrag, Fachgruppe> manager) {
		CoreTypeDataManager.putManager(Fachgruppe.class, manager);
		for (final var ct : data().getWerte())
			for (final var e : ct.historie())
				_mapSchulformenByID.put(e.id, Schulform.data().getWerteByBezeichnerAsNonEmptySet(e.schulformen));
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<FachgruppeKatalogEintrag, Fachgruppe> data() {
		return CoreTypeDataManager.getManager(Fachgruppe.class);
	}


	/**
	 * Prüft, ob die Schulform bei diesem Core-Type-Wert in dem angegeben Schuljahr zulässig ist oder nicht.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 * @param sf          die Schulform, auf die geprüft wird
	 *
	 * @return true, falls die Schulform zulässig ist, und ansonsten false
	 */
	public boolean hatSchulform(final int schuljahr, final @NotNull Schulform sf) {
		final FachgruppeKatalogEintrag ke = this.daten(schuljahr);
		if (ke != null) {
			final Set<Schulform> result = _mapSchulformenByID.get(ke.id);
			if (result == null)
				throw new CoreTypeException("Fehler beim prüfen der Schulform. Der Core-Type %s ist nicht korrekt initialisiert.".formatted(this.getClass().getSimpleName()));
			return result.contains(sf);
		}
		return false;
	}


	/**
	 * Gibt die Farbe der Fachgruppe zurück.
	 *
	 * @param schuljahr   das Schuljahr, auf welches sich die Anfrage bezieht
	 *
	 * @return die Farbe der Fachgruppe
	 */
	public @NotNull RGBFarbe getFarbe(final int schuljahr) {
		final FachgruppeKatalogEintrag fgke = Fachgruppe.data().getEintragBySchuljahrUndWert(schuljahr, this);
		if (fgke == null)
			return new RGBFarbe();
		return fgke.farbe;
	}

}
