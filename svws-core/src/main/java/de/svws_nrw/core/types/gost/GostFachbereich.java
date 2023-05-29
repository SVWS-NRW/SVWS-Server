package de.svws_nrw.core.types.gost;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert die Fachbereich für die Laufbahnprüfung in der
 * gymnasialen Oberstufe.
 */
public enum GostFachbereich {
	/** Fachbereich deutsch */
	DEUTSCH(null, ZulaessigesFach.D),

	/** Fachbereich fremdsprachlich */
	FREMDSPRACHE(null,
			ZulaessigesFach.C, ZulaessigesFach.C0, ZulaessigesFach.C5, ZulaessigesFach.C6, ZulaessigesFach.C7, ZulaessigesFach.C8, ZulaessigesFach.C9,
			ZulaessigesFach.E,
			ZulaessigesFach.F, ZulaessigesFach.F0, ZulaessigesFach.F5, ZulaessigesFach.F6, ZulaessigesFach.F7, ZulaessigesFach.F8, ZulaessigesFach.F9,
			ZulaessigesFach.G, ZulaessigesFach.G0, ZulaessigesFach.G5, ZulaessigesFach.G6, ZulaessigesFach.G7, ZulaessigesFach.G8, ZulaessigesFach.G9,
			ZulaessigesFach.H, ZulaessigesFach.H0, ZulaessigesFach.H5, ZulaessigesFach.H6, ZulaessigesFach.H7, ZulaessigesFach.H8, ZulaessigesFach.H9,
			ZulaessigesFach.I, ZulaessigesFach.I0, ZulaessigesFach.I5, ZulaessigesFach.I6, ZulaessigesFach.I7, ZulaessigesFach.I8, ZulaessigesFach.I9,
			ZulaessigesFach.K, ZulaessigesFach.K0, ZulaessigesFach.K5, ZulaessigesFach.K6, ZulaessigesFach.K7, ZulaessigesFach.K8, ZulaessigesFach.K9,
			ZulaessigesFach.L, ZulaessigesFach.L0, ZulaessigesFach.L5, ZulaessigesFach.L6, ZulaessigesFach.L7, ZulaessigesFach.L8, ZulaessigesFach.L9,
			ZulaessigesFach.N, ZulaessigesFach.N0, ZulaessigesFach.N5, ZulaessigesFach.N6, ZulaessigesFach.N7, ZulaessigesFach.N8, ZulaessigesFach.N9,
			ZulaessigesFach.O, ZulaessigesFach.O0, ZulaessigesFach.O5, ZulaessigesFach.O6, ZulaessigesFach.O7, ZulaessigesFach.O8, ZulaessigesFach.O9,
			ZulaessigesFach.R, ZulaessigesFach.R0, ZulaessigesFach.R5, ZulaessigesFach.R6, ZulaessigesFach.R7, ZulaessigesFach.R8, ZulaessigesFach.R9,
			ZulaessigesFach.S, ZulaessigesFach.S0, ZulaessigesFach.S5, ZulaessigesFach.S6, ZulaessigesFach.S7, ZulaessigesFach.S8, ZulaessigesFach.S9,
			ZulaessigesFach.T, ZulaessigesFach.T0, ZulaessigesFach.T5, ZulaessigesFach.T6, ZulaessigesFach.T7, ZulaessigesFach.T8, ZulaessigesFach.T9,
			ZulaessigesFach.Z, ZulaessigesFach.Z0, ZulaessigesFach.Z5, ZulaessigesFach.Z6, ZulaessigesFach.Z7, ZulaessigesFach.Z8, ZulaessigesFach.Z9
			),

	/** Fachbereich künstlerisch musikalisch */
	KUNST_MUSIK(null, ZulaessigesFach.KU, ZulaessigesFach.MU),

	/** Fachbereich Ersatz für literarisch künstlerisch */
	LITERARISCH_KUENSTLERISCH_ERSATZ(null, ZulaessigesFach.LI, ZulaessigesFach.IN, ZulaessigesFach.VO),

	/** Fachbereich literarisch künstlerisch */
	LITERARISCH_KUENSTLERISCH(Arrays.asList(KUNST_MUSIK, LITERARISCH_KUENSTLERISCH_ERSATZ)),

	/** Fachbereich sprachlich literarisch künstlerisch */
	SPRACHLICH_LITERARISCH_KUENSTLERISCH(Arrays.asList(DEUTSCH, FREMDSPRACHE, KUNST_MUSIK, LITERARISCH_KUENSTLERISCH_ERSATZ)),

	/** Fachbereich geschichtlich */
	GESCHICHTE(null, ZulaessigesFach.GE),

	/** Fachbereich sozialwissenschaftlich */
	SOZIALWISSENSCHAFTEN(null, ZulaessigesFach.SW),

	/** Fachbereich philosophisch */
	PHILOSOPHIE(null, ZulaessigesFach.PL),

	/** Fachbereich Sonstige gesellschaftswissenschaftliche */
	GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE(null, ZulaessigesFach.EK, ZulaessigesFach.PA, ZulaessigesFach.PS, ZulaessigesFach.RK),

	/** Fachbereich gesellschaftswissenschaftlich */
	GESELLSCHAFTSWISSENSCHAFTLICH(Arrays.asList(GESCHICHTE, SOZIALWISSENSCHAFTEN, PHILOSOPHIE, GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE)),

	/** Fachbereich Religion */
	RELIGION(null, ZulaessigesFach.HR, ZulaessigesFach.OR, ZulaessigesFach.YR, ZulaessigesFach.ER, ZulaessigesFach.KR, ZulaessigesFach.IL),

	/** Fachbereich gesellschaftswissenschaftlich mit Religion */
	GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION(Arrays.asList(GESCHICHTE, SOZIALWISSENSCHAFTEN, PHILOSOPHIE, GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE, RELIGION)),

	/** Fachbereich mathematisch */
	MATHEMATIK(null, ZulaessigesFach.M),

	/** Fachbereich klassisch naturwissenschaftlich */
	NATURWISSENSCHAFTLICH_KLASSISCH(null, ZulaessigesFach.BI, ZulaessigesFach.CH, ZulaessigesFach.PH),

	/** Fachbereich naturwissenschaftlich neueinsetzend */
	NATURWISSENSCHAFTLICH_NEU_EINSETZEND(null, ZulaessigesFach.EL, ZulaessigesFach.IF, ZulaessigesFach.TC),

	/** Fachbereich naturwissenschaftlich */
	NATURWISSENSCHAFTLICH(Arrays.asList(NATURWISSENSCHAFTLICH_KLASSISCH, NATURWISSENSCHAFTLICH_NEU_EINSETZEND)),

	/** Fachbereich mathematisch naturwissenschaftlich */
	MATHEMATISCH_NATURWISSENSCHAFTLICH(Arrays.asList(MATHEMATIK, NATURWISSENSCHAFTLICH_KLASSISCH, NATURWISSENSCHAFTLICH_NEU_EINSETZEND)),

	/** Fachbereich sportlich */
	SPORT(null, ZulaessigesFach.SP);

	/** Eine Map, welche dem zulässigen Fach alle seine Fachbereiche zuordnet. */
	private static final @NotNull Map<@NotNull ZulaessigesFach, @NotNull List<@NotNull GostFachbereich>> _mapFachbereichByFach = new ArrayMap<>(ZulaessigesFach.values());


	/** Eine Liste der Fächern dieses Fachbereichs */
	private final @NotNull ArrayList<@NotNull ZulaessigesFach> faecher = new ArrayList<>();
	/** Eine Liste der Fächerkürzel dieses Fachbereichs */
	private final @NotNull ArrayList<@NotNull String> kuerzel = new ArrayList<>();


	/**
	 * Erstellt einen neuen Fachbereich als Kombination der übergebenen Fachbereiche
	 * und der übergebenen Fächer
	 *
	 * @param fachbereiche   die Fachbereiche
	 * @param faecher        die Fächer des Fachbereichs
	 */
	GostFachbereich(final List<@NotNull GostFachbereich> fachbereiche, final @NotNull ZulaessigesFach... faecher) {
		if (fachbereiche != null) {
			for (final GostFachbereich fb : fachbereiche) {
				for (final ZulaessigesFach fach : fb.faecher) {
					this.faecher.add(fach);
					this.kuerzel.add(fach.daten.kuerzelASD);
				}
			}
		}
		for (final ZulaessigesFach fach : faecher) {
			this.faecher.add(fach);
			this.kuerzel.add(fach.daten.kuerzelASD);
		}
	}


	/**
	 * Gibt eine Map von den Fächern auf die zugehörigen Fachbereiche
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Fächern auf die zugehörigen Fachbereiche
	 */
	private static @NotNull Map<@NotNull ZulaessigesFach, @NotNull List<@NotNull GostFachbereich>> getMapFachbereichByFach() {
		if (_mapFachbereichByFach.size() == 0) {
			for (final @NotNull GostFachbereich fb : GostFachbereich.values()) {
				for (final ZulaessigesFach fach : fb.faecher) {
					List<@NotNull GostFachbereich> listFachbereichByFach = _mapFachbereichByFach.get(fach);
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
	public @NotNull List<@NotNull ZulaessigesFach> getFaecher() {
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
		return (fach != null) && hat(fach.kuerzel);
	}


	/**
	 * Prüft, ob das Fach mit dem übergebenen Kürzel zu diesem Fachbereich gehört.
	 *
	 * @param kuerzel   das Kürzel des zu prüfenden Faches
	 *
	 * @return true, falls das Fach zu dem Fachbereich gehört, sonst false
	 */
	@JsonIgnore
	public boolean hat(final String kuerzel) {
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
	public static @NotNull List<@NotNull GostFachbereich> getBereiche(final GostFach fach) {
		if (fach == null)
			return new ArrayList<>();
		final @NotNull ZulaessigesFach zulFach = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);
		final List<@NotNull GostFachbereich> bereiche = getMapFachbereichByFach().get(zulFach);
		if (bereiche != null)
			return bereiche;
		return new ArrayList<>();
	}

}
