package de.svws_nrw.core.types.gost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.data.gost.GostFach;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert die Fachbereich für die Laufbahnprüfung in der
 * gymnasialen Oberstufe.
 */
public enum GostFachbereich {
	/** Fachbereich deutsch */
	DEUTSCH(null, Fach.D),

	/** Fachbereich fremdsprachlich */
	FREMDSPRACHE(null,
			Fach.E,
			Fach.C, Fach.C0, Fach.C5, Fach.C6, Fach.C7, Fach.C8, Fach.C9,
			Fach.F, Fach.F0, Fach.F5, Fach.F6, Fach.F7, Fach.F8, Fach.F9,
			Fach.G, Fach.G0, Fach.G5, Fach.G6, Fach.G7, Fach.G8, Fach.G9,
			Fach.H, Fach.H0, Fach.H5, Fach.H6, Fach.H7, Fach.H8, Fach.H9,
			Fach.I, Fach.I0, Fach.I5, Fach.I6, Fach.I7, Fach.I8, Fach.I9,
			Fach.K, Fach.K0, Fach.K5, Fach.K6, Fach.K7, Fach.K8, Fach.K9,
			Fach.L, Fach.L0, Fach.L5, Fach.L6, Fach.L7, Fach.L8, Fach.L9,
			Fach.N, Fach.N0, Fach.N5, Fach.N6, Fach.N7, Fach.N8, Fach.N9,
			Fach.O, Fach.O0, Fach.O5, Fach.O6, Fach.O7, Fach.O8, Fach.O9,
			Fach.R, Fach.R0, Fach.R5, Fach.R6, Fach.R7, Fach.R8, Fach.R9,
			Fach.S, Fach.S0, Fach.S5, Fach.S6, Fach.S7, Fach.S8, Fach.S9,
			Fach.T, Fach.T0, Fach.T5, Fach.T6, Fach.T7, Fach.T8, Fach.T9,
			Fach.Z, Fach.Z0, Fach.Z5, Fach.Z6, Fach.Z7, Fach.Z8, Fach.Z9
	),

	/** Fachbereich künstlerisch musikalisch */
	KUNST_MUSIK(null, Fach.KU, Fach.MU),

	/** Fachbereich Ersatz für literarisch künstlerisch */
	LITERARISCH_KUENSTLERISCH_ERSATZ(null, Fach.LI, Fach.IN, Fach.VO),

	/** Fachbereich literarisch künstlerisch */
	LITERARISCH_KUENSTLERISCH(Arrays.asList(KUNST_MUSIK, LITERARISCH_KUENSTLERISCH_ERSATZ)),

	/** Fachbereich sprachlich literarisch künstlerisch */
	SPRACHLICH_LITERARISCH_KUENSTLERISCH(Arrays.asList(DEUTSCH, FREMDSPRACHE, KUNST_MUSIK, LITERARISCH_KUENSTLERISCH_ERSATZ)),

	/** Fachbereich geschichtlich */
	GESCHICHTE(null, Fach.GE),

	/** Fachbereich sozialwissenschaftlich */
	SOZIALWISSENSCHAFTEN(null, Fach.SW),

	/** Fachbereich philosophisch */
	PHILOSOPHIE(null, Fach.PL),

	/** Fachbereich Sonstige gesellschaftswissenschaftliche */
	GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE(null, Fach.EK, Fach.PA, Fach.PS, Fach.RK),

	/** Fachbereich gesellschaftswissenschaftlich */
	GESELLSCHAFTSWISSENSCHAFTLICH(Arrays.asList(GESCHICHTE, SOZIALWISSENSCHAFTEN, PHILOSOPHIE, GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE)),

	/** Fachbereich Religion */
	RELIGION(null, Fach.HR, Fach.OR, Fach.YR, Fach.ER, Fach.KR, Fach.IL),

	/** Fachbereich gesellschaftswissenschaftlich mit Religion */
	GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION(Arrays.asList(GESCHICHTE, SOZIALWISSENSCHAFTEN, PHILOSOPHIE, GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE, RELIGION)),

	/** Fachbereich mathematisch */
	MATHEMATIK(null, Fach.M),

	/** Fachbereich klassisch naturwissenschaftlich */
	NATURWISSENSCHAFTLICH_KLASSISCH(null, Fach.BI, Fach.CH, Fach.PH),

	/** Fachbereich naturwissenschaftlich neueinsetzend */
	NATURWISSENSCHAFTLICH_NEU_EINSETZEND(null, Fach.EL, Fach.IF, Fach.TC),

	/** Fachbereich naturwissenschaftlich */
	NATURWISSENSCHAFTLICH(Arrays.asList(NATURWISSENSCHAFTLICH_KLASSISCH, NATURWISSENSCHAFTLICH_NEU_EINSETZEND)),

	/** Fachbereich mathematisch naturwissenschaftlich */
	MATHEMATISCH_NATURWISSENSCHAFTLICH(Arrays.asList(MATHEMATIK, NATURWISSENSCHAFTLICH_KLASSISCH, NATURWISSENSCHAFTLICH_NEU_EINSETZEND)),

	/** Fachbereich sportlich */
	SPORT(null, Fach.SP),

	/** Pseudo-Fachbereich Vertiefungskurse */
	VERTIEFUNGSKURSE(null, Fach.VX),

	/** Pseudo-Fachbereich Projektkurse */
	PROJEKTKURSE(null, Fach.PX);


	/** Eine Map mit allen Statistik-Fächern, die einem Fachbereich der gymnasialen Oberstufe zugeordnet sind. Die Map verweist auf einen Long-Wert für die Sortierung der Fächer */
	private static final @NotNull Map<Fach, Integer> _mapAlleFaecher = new HashMap<>();

	/** Eine Liste mit allen Statistik-Fächern in einer Standard-Sortierung, die einem Fachbereich der gymnasialen Oberstufe zugeordnet sind. */
	private static final @NotNull List<Fach> _listAlleFaecher = new ArrayList<>();

	/** Eine Map, welche dem zulässigen Fach alle seine Fachbereiche zuordnet. */
	private static final @NotNull Map<Fach, List<GostFachbereich>> _mapFachbereichByFach =
			new ArrayMap<>(Fach.values());


	/** Eine Liste der Fächern dieses Fachbereichs */
	private final @NotNull ArrayList<Fach> faecher = new ArrayList<>();
	/** Eine Liste der Fächerkürzel dieses Fachbereichs */
	private final @NotNull ArrayList<String> kuerzel = new ArrayList<>();


	/**
	 * Erstellt einen neuen Fachbereich als Kombination der übergebenen Fachbereiche
	 * und der übergebenen Fächer
	 *
	 * @param fachbereiche   die Fachbereiche
	 * @param faecher        die Fächer des Fachbereichs
	 */
	GostFachbereich(final List<GostFachbereich> fachbereiche, final @NotNull Fach... faecher) {
		if (fachbereiche != null) {
			for (final GostFachbereich fb : fachbereiche) {
				for (final Fach fach : fb.faecher) {
					this.faecher.add(fach);
					this.kuerzel.add(fach.name());
				}
			}
		}
		for (final Fach fach : faecher) {
			this.faecher.add(fach);
			this.kuerzel.add(fach.name());
		}
	}


	/**
	 * Gibt eine Map von den Fächern auf die zugehörigen Fachbereiche
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Fächern auf die zugehörigen Fachbereiche
	 */
	private static @NotNull Map<Fach, List<GostFachbereich>> getMapFachbereichByFach() {
		if (_mapFachbereichByFach.size() == 0) {
			for (final @NotNull GostFachbereich fb : GostFachbereich.values()) {
				for (final Fach fach : fb.faecher) {
					List<GostFachbereich> listFachbereichByFach = _mapFachbereichByFach.get(fach);
					if (listFachbereichByFach == null) {
						listFachbereichByFach = new ArrayList<>();
						_mapFachbereichByFach.put(fach, listFachbereichByFach);
					}
					listFachbereichByFach.add(fb);
				}
			}
		}
		return _mapFachbereichByFach;
	}


	/**
	 * Gibt die Liste der Fächer des Fachbereichs zurück.
	 *
	 * @return die Liste der Fächer des Fachbereichs
	 */
	public @NotNull List<Fach> getFaecher() {
		return this.faecher;
	}


	/**
	 * Prüft, ob das übergebene Fach zu diesem Fachbereich gehört.
	 *
	 * @param fach   das zu prüfende Fach
	 *
	 * @return true, falls das Fach zu dem Fachbereich gehört, sonst false
	 */
	@JsonIgnore
	public boolean hat(final GostFach fach) {
		return (fach != null) && hatKuerzel(fach.kuerzel);
	}


	/**
	 * Prüft, ob das Fach mit dem übergebenen Kürzel zu diesem Fachbereich gehört.
	 *
	 * @param kuerzel   das Kürzel des zu prüfenden Faches
	 *
	 * @return true, falls das Fach zu dem Fachbereich gehört, sonst false
	 */
	@JsonIgnore
	public boolean hatKuerzel(final String kuerzel) {
		if (kuerzel == null)
			return false;
		return this.kuerzel.contains(kuerzel);
	}


	/**
	 * Ermittelt die, dem Fach zugehörigen, Fachbereiche
	 *
	 * @param fach   das Fach
	 *
	 * @return die zugehörigen Fachbereiche
	 */
	public static @NotNull List<GostFachbereich> getBereiche(final GostFach fach) {
		if (fach == null)
			return new ArrayList<>();
		final @NotNull Fach zulFach = Fach.getBySchluesselOrDefault(fach.kuerzel);
		final List<GostFachbereich> bereiche = getMapFachbereichByFach().get(zulFach);
		if (bereiche != null)
			return bereiche;
		return new ArrayList<>();
	}


	/**
	 * Gibt eine Map aller Fächer zurück, die einem Fachbereich der gymnasialen Oberstufe zugeordnet
	 * sind. Dabei wird jedem Fach eine Nummer zugeordnet, welche der Sortierung in der Standard-Sortierung
	 * der Oberstufenfächer entspricht.
	 *
	 * @return die Menge der Fächer
	 */
	public static @NotNull Map<Fach, Integer> getAlleFaecher() {
		if (_mapAlleFaecher.isEmpty()) {
			final @NotNull List<Fach> alleFaecher = getAlleFaecherSortiert();
			for (int i = 0; i < alleFaecher.size(); i++) {
				final @NotNull Fach fach = alleFaecher.get(i);
				_mapAlleFaecher.put(fach, i);
			}
		}
		return _mapAlleFaecher;
	}


	/**
	 * Gibt den Integer Wert für die Sortierreihenfolge der Oberstufen-Fächer zu dem
	 * angegebenen Fach zurück.
	 *
	 * @param fach   das Fach
	 *
	 * @return der Integer-Wert für die Sortierreihenfolge des Faches und Integer-MAX_VALUE, falls
	 *   das übergeben Fach kein Fach der Gymnasialen Oberstufe ist.
	 */
	public static int getSortierung(final @NotNull Fach fach) {
		final Integer sortierung = getAlleFaecher().get(fach);
		if (sortierung == null)
			return Integer.MAX_VALUE;
		return sortierung;
	}


	/**
	 * Vergleicht die Fächer fa und fb anhand ihrer Standard-Sortierung für die gymnasiale Oberstufe
	 *
	 * @param fa   das erste Fach
	 * @param fb   das zweite Fach
	 *
	 * @return der int-wert für den Vergleich (siehe {@link Comparable#compareTo(Object)}
	 */
	public static int compareFach(final Fach fa, final Fach fb) {
		// Prüfe zunächst auf null-Werte ...
		if ((fa == null) && (fb == null))
			return 0;
		if (fa == null)
			return Integer.MAX_VALUE;
		if (fb == null)
			return Integer.MIN_VALUE;
		return Integer.compare(GostFachbereich.getSortierung(fa), GostFachbereich.getSortierung(fb));
	}


	/**
	 * Vergleicht die Fächer mit den Statistik-Kürzeln ka und kb anhand ihrer Standard-Sortierung
	 * für die gymnasiale Oberstufe
	 *
	 * @param ka   das Kürzel des ersten Faches
	 * @param kb   das Kürzel des zweiten Faches
	 *
	 * @return der int-wert für den Vergleich (siehe {@link Comparable#compareTo(Object)}
	 */
	public static int compareFachByKuerzel(final String ka, final String kb) {
		return compareFach((ka == null) ? null : Fach.getBySchluesselOrDefault(ka), (kb == null) ? null : Fach.getBySchluesselOrDefault(kb));
	}


	/**
	 * Vergleicht die beiden Fächer der Gymnasialen Oberstufe anhand ihrer Standard-Sortierung
	 * für die gymnasiale Oberstufe
	 *
	 * @param fa   das erste Fach
	 * @param fb   das zweite Fach
	 *
	 * @return der int-wert für den Vergleich (siehe {@link Comparable#compareTo(Object)}
	 */
	public static int compareGostFach(final GostFach fa, final GostFach fb) {
		// Prüfe zunächst auf null-Werte ...
		if ((fa == null) && (fb == null))
			return 0;
		if (fa == null)
			return Integer.MAX_VALUE;
		if (fb == null)
			return Integer.MIN_VALUE;
		// ... sortiere dann zunächst anhand des Fachbereichs ...
		final int cmp = GostFachbereich.compareFachByKuerzel(fa.kuerzel, fb.kuerzel);
		if (cmp != 0)
			return cmp;
		// ... und nutze als zweites Kriterium die Sortierung des Faches
		return Integer.compare(fa.sortierung, fb.sortierung);
	}


	/**
	 * Gibt alle Fächer in einer Standard-Sortierung zurück, welche einem Fachbereich der gymnasialen
	 * Oberstufe zugeordnet sind.
	 *
	 * @return die Liste der Fächer
	 */
	public static @NotNull List<Fach> getAlleFaecherSortiert() {
		if (_listAlleFaecher.isEmpty()) {
			_listAlleFaecher.addAll(GostFachbereich.SPRACHLICH_LITERARISCH_KUENSTLERISCH.getFaecher());
			_listAlleFaecher.addAll(GostFachbereich.GESCHICHTE.getFaecher());
			_listAlleFaecher.addAll(GostFachbereich.SOZIALWISSENSCHAFTEN.getFaecher());
			_listAlleFaecher.addAll(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE.getFaecher());
			_listAlleFaecher.addAll(GostFachbereich.PHILOSOPHIE.getFaecher());
			_listAlleFaecher.addAll(GostFachbereich.RELIGION.getFaecher());
			_listAlleFaecher.addAll(GostFachbereich.MATHEMATISCH_NATURWISSENSCHAFTLICH.getFaecher());
			_listAlleFaecher.addAll(GostFachbereich.SPORT.getFaecher());
			_listAlleFaecher.addAll(GostFachbereich.VERTIEFUNGSKURSE.getFaecher());
			_listAlleFaecher.addAll(GostFachbereich.PROJEKTKURSE.getFaecher());
		}
		return _listAlleFaecher;
	}

}
