import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { Fach } from '../../../asd/types/fach/Fach';
import { GostFach } from '../../../core/data/gost/GostFach';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { ArrayMap } from '../../../core/adt/map/ArrayMap';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Arrays } from '../../../java/util/Arrays';
import type { JavaMap } from '../../../java/util/JavaMap';

export class GostFachbereich extends JavaEnum<GostFachbereich> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<GostFachbereich> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, GostFachbereich> = new Map<string, GostFachbereich>();

	/**
	 * Fachbereich deutsch
	 */
	public static readonly DEUTSCH : GostFachbereich = new GostFachbereich("DEUTSCH", 0, null, Fach.D);

	/**
	 * Fachbereich fremdsprachlich
	 */
	public static readonly FREMDSPRACHE : GostFachbereich = new GostFachbereich("FREMDSPRACHE", 1, null, Fach.E, Fach.C, Fach.C0, Fach.C5, Fach.C6, Fach.C7, Fach.C8, Fach.C9, Fach.F, Fach.F0, Fach.F5, Fach.F6, Fach.F7, Fach.F8, Fach.F9, Fach.G, Fach.G0, Fach.G5, Fach.G6, Fach.G7, Fach.G8, Fach.G9, Fach.H, Fach.H0, Fach.H5, Fach.H6, Fach.H7, Fach.H8, Fach.H9, Fach.I, Fach.I0, Fach.I5, Fach.I6, Fach.I7, Fach.I8, Fach.I9, Fach.K, Fach.K0, Fach.K5, Fach.K6, Fach.K7, Fach.K8, Fach.K9, Fach.L, Fach.L0, Fach.L5, Fach.L6, Fach.L7, Fach.L8, Fach.L9, Fach.N, Fach.N0, Fach.N5, Fach.N6, Fach.N7, Fach.N8, Fach.N9, Fach.O, Fach.O0, Fach.O5, Fach.O6, Fach.O7, Fach.O8, Fach.O9, Fach.R, Fach.R0, Fach.R5, Fach.R6, Fach.R7, Fach.R8, Fach.R9, Fach.S, Fach.S0, Fach.S5, Fach.S6, Fach.S7, Fach.S8, Fach.S9, Fach.T, Fach.T0, Fach.T5, Fach.T6, Fach.T7, Fach.T8, Fach.T9, Fach.Z, Fach.Z0, Fach.Z5, Fach.Z6, Fach.Z7, Fach.Z8, Fach.Z9);

	/**
	 * Fachbereich künstlerisch musikalisch
	 */
	public static readonly KUNST_MUSIK : GostFachbereich = new GostFachbereich("KUNST_MUSIK", 2, null, Fach.KU, Fach.MU);

	/**
	 * Fachbereich Ersatz für literarisch künstlerisch
	 */
	public static readonly LITERARISCH_KUENSTLERISCH_ERSATZ : GostFachbereich = new GostFachbereich("LITERARISCH_KUENSTLERISCH_ERSATZ", 3, null, Fach.LI, Fach.IN, Fach.VO);

	/**
	 * Fachbereich literarisch künstlerisch
	 */
	public static readonly LITERARISCH_KUENSTLERISCH : GostFachbereich = new GostFachbereich("LITERARISCH_KUENSTLERISCH", 4, Arrays.asList(GostFachbereich.KUNST_MUSIK, GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ));

	/**
	 * Fachbereich sprachlich literarisch künstlerisch
	 */
	public static readonly SPRACHLICH_LITERARISCH_KUENSTLERISCH : GostFachbereich = new GostFachbereich("SPRACHLICH_LITERARISCH_KUENSTLERISCH", 5, Arrays.asList(GostFachbereich.DEUTSCH, GostFachbereich.FREMDSPRACHE, GostFachbereich.KUNST_MUSIK, GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ));

	/**
	 * Fachbereich geschichtlich
	 */
	public static readonly GESCHICHTE : GostFachbereich = new GostFachbereich("GESCHICHTE", 6, null, Fach.GE);

	/**
	 * Fachbereich sozialwissenschaftlich
	 */
	public static readonly SOZIALWISSENSCHAFTEN : GostFachbereich = new GostFachbereich("SOZIALWISSENSCHAFTEN", 7, null, Fach.SW);

	/**
	 * Fachbereich philosophisch
	 */
	public static readonly PHILOSOPHIE : GostFachbereich = new GostFachbereich("PHILOSOPHIE", 8, null, Fach.PL);

	/**
	 * Fachbereich Sonstige gesellschaftswissenschaftliche
	 */
	public static readonly GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE : GostFachbereich = new GostFachbereich("GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE", 9, null, Fach.EK, Fach.PA, Fach.PS, Fach.RK);

	/**
	 * Fachbereich gesellschaftswissenschaftlich
	 */
	public static readonly GESELLSCHAFTSWISSENSCHAFTLICH : GostFachbereich = new GostFachbereich("GESELLSCHAFTSWISSENSCHAFTLICH", 10, Arrays.asList(GostFachbereich.GESCHICHTE, GostFachbereich.SOZIALWISSENSCHAFTEN, GostFachbereich.PHILOSOPHIE, GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE));

	/**
	 * Fachbereich Religion
	 */
	public static readonly RELIGION : GostFachbereich = new GostFachbereich("RELIGION", 11, null, Fach.HR, Fach.OR, Fach.YR, Fach.ER, Fach.KR, Fach.IL);

	/**
	 * Fachbereich gesellschaftswissenschaftlich mit Religion
	 */
	public static readonly GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION : GostFachbereich = new GostFachbereich("GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION", 12, Arrays.asList(GostFachbereich.GESCHICHTE, GostFachbereich.SOZIALWISSENSCHAFTEN, GostFachbereich.PHILOSOPHIE, GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE, GostFachbereich.RELIGION));

	/**
	 * Fachbereich mathematisch
	 */
	public static readonly MATHEMATIK : GostFachbereich = new GostFachbereich("MATHEMATIK", 13, null, Fach.M);

	/**
	 * Fachbereich klassisch naturwissenschaftlich
	 */
	public static readonly NATURWISSENSCHAFTLICH_KLASSISCH : GostFachbereich = new GostFachbereich("NATURWISSENSCHAFTLICH_KLASSISCH", 14, null, Fach.BI, Fach.CH, Fach.PH);

	/**
	 * Fachbereich naturwissenschaftlich neueinsetzend
	 */
	public static readonly NATURWISSENSCHAFTLICH_NEU_EINSETZEND : GostFachbereich = new GostFachbereich("NATURWISSENSCHAFTLICH_NEU_EINSETZEND", 15, null, Fach.EL, Fach.IF, Fach.TC);

	/**
	 * Fachbereich naturwissenschaftlich
	 */
	public static readonly NATURWISSENSCHAFTLICH : GostFachbereich = new GostFachbereich("NATURWISSENSCHAFTLICH", 16, Arrays.asList(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH, GostFachbereich.NATURWISSENSCHAFTLICH_NEU_EINSETZEND));

	/**
	 * Fachbereich mathematisch naturwissenschaftlich
	 */
	public static readonly MATHEMATISCH_NATURWISSENSCHAFTLICH : GostFachbereich = new GostFachbereich("MATHEMATISCH_NATURWISSENSCHAFTLICH", 17, Arrays.asList(GostFachbereich.MATHEMATIK, GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH, GostFachbereich.NATURWISSENSCHAFTLICH_NEU_EINSETZEND));

	/**
	 * Fachbereich sportlich
	 */
	public static readonly SPORT : GostFachbereich = new GostFachbereich("SPORT", 18, null, Fach.SP);

	/**
	 * Pseudo-Fachbereich Vertiefungskurse
	 */
	public static readonly VERTIEFUNGSKURSE : GostFachbereich = new GostFachbereich("VERTIEFUNGSKURSE", 19, null, Fach.VX);

	/**
	 * Pseudo-Fachbereich Projektkurse
	 */
	public static readonly PROJEKTKURSE : GostFachbereich = new GostFachbereich("PROJEKTKURSE", 20, null, Fach.PX);

	/**
	 * Eine Map mit allen Statistik-Fächern, die einem Fachbereich der gymnasialen Oberstufe zugeordnet sind. Die Map verweist auf einen Long-Wert für die Sortierung der Fächer
	 */
	private static readonly _mapAlleFaecher : JavaMap<Fach, number> = new HashMap<Fach, number>();

	/**
	 * Eine Liste mit allen Statistik-Fächern in einer Standard-Sortierung, die einem Fachbereich der gymnasialen Oberstufe zugeordnet sind.
	 */
	private static readonly _listAlleFaecher : List<Fach> = new ArrayList<Fach>();

	/**
	 * Eine Map, welche dem zulässigen Fach alle seine Fachbereiche zuordnet.
	 */
	private static readonly _mapFachbereichByFach : JavaMap<Fach, List<GostFachbereich>> = new ArrayMap<Fach, List<GostFachbereich>>(Fach.values());

	/**
	 * Eine Liste der Fächern dieses Fachbereichs
	 */
	private readonly faecher : ArrayList<Fach> = new ArrayList<Fach>();

	/**
	 * Eine Liste der Fächerkürzel dieses Fachbereichs
	 */
	private readonly kuerzel : ArrayList<string> = new ArrayList<string>();

	/**
	 * Erstellt einen neuen Fachbereich als Kombination der übergebenen Fachbereiche
	 * und der übergebenen Fächer
	 *
	 * @param fachbereiche   die Fachbereiche
	 * @param faecher        die Fächer des Fachbereichs
	 */
	private constructor(name : string, ordinal : number, fachbereiche : List<GostFachbereich> | null, ...faecher : Array<Fach>) {
		super(name, ordinal);
		GostFachbereich.all_values_by_ordinal.push(this);
		GostFachbereich.all_values_by_name.set(name, this);
		if (fachbereiche !== null) {
			for (const fb of fachbereiche) {
				for (const fach of fb.faecher) {
					this.faecher.add(fach);
					this.kuerzel.add(fach.name());
				}
			}
		}
		for (const fach of faecher) {
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
	private static getMapFachbereichByFach() : JavaMap<Fach, List<GostFachbereich>> {
		if (GostFachbereich._mapFachbereichByFach.size() === 0) {
			for (const fb of GostFachbereich.values()) {
				for (const fach of fb.faecher) {
					let listFachbereichByFach : List<GostFachbereich> | null = GostFachbereich._mapFachbereichByFach.get(fach);
					if (listFachbereichByFach === null) {
						listFachbereichByFach = new ArrayList();
						GostFachbereich._mapFachbereichByFach.put(fach, listFachbereichByFach);
					}
					listFachbereichByFach.add(fb);
				}
			}
		}
		return GostFachbereich._mapFachbereichByFach;
	}

	/**
	 * Gibt die Liste der Fächer des Fachbereichs zurück.
	 *
	 * @return die Liste der Fächer des Fachbereichs
	 */
	public getFaecher() : List<Fach> {
		return this.faecher;
	}

	/**
	 * Prüft, ob das übergebene Fach zu diesem Fachbereich gehört.
	 *
	 * @param fach   das zu prüfende Fach
	 *
	 * @return true, falls das Fach zu dem Fachbereich gehört, sonst false
	 */
	public hat(fach : GostFach | null) : boolean {
		return (fach !== null) && this.hatKuerzel(fach.kuerzel);
	}

	/**
	 * Prüft, ob das Fach mit dem übergebenen Kürzel zu diesem Fachbereich gehört.
	 *
	 * @param kuerzel   das Kürzel des zu prüfenden Faches
	 *
	 * @return true, falls das Fach zu dem Fachbereich gehört, sonst false
	 */
	public hatKuerzel(kuerzel : string | null) : boolean {
		if (kuerzel === null)
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
	public static getBereiche(fach : GostFach | null) : List<GostFachbereich> {
		if (fach === null)
			return new ArrayList();
		const zulFach : Fach = Fach.data().getWertBySchluesselOrException(fach.kuerzel);
		const bereiche : List<GostFachbereich> | null = GostFachbereich.getMapFachbereichByFach().get(zulFach);
		if (bereiche !== null)
			return bereiche;
		return new ArrayList<GostFachbereich>();
	}

	/**
	 * Gibt eine Map aller Fächer zurück, die einem Fachbereich der gymnasialen Oberstufe zugeordnet
	 * sind. Dabei wird jedem Fach eine Nummer zugeordnet, welche der Sortierung in der Standard-Sortierung
	 * der Oberstufenfächer entspricht.
	 *
	 * @return die Menge der Fächer
	 */
	public static getAlleFaecher() : JavaMap<Fach, number> {
		if (GostFachbereich._mapAlleFaecher.isEmpty()) {
			const alleFaecher : List<Fach> = GostFachbereich.getAlleFaecherSortiert();
			for (let i : number = 0; i < alleFaecher.size(); i++) {
				const fach : Fach = alleFaecher.get(i);
				GostFachbereich._mapAlleFaecher.put(fach, i);
			}
		}
		return GostFachbereich._mapAlleFaecher;
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
	public static getSortierung(fach : Fach) : number {
		const sortierung : number | null = GostFachbereich.getAlleFaecher().get(fach);
		if (sortierung === null)
			return JavaInteger.MAX_VALUE;
		return sortierung!;
	}

	/**
	 * Vergleicht die Fächer fa und fb anhand ihrer Standard-Sortierung für die gymnasiale Oberstufe
	 *
	 * @param fa   das erste Fach
	 * @param fb   das zweite Fach
	 *
	 * @return der int-wert für den Vergleich (siehe {@link Comparable#compareTo(Object)}
	 */
	public static compareFach(fa : Fach | null, fb : Fach | null) : number {
		if ((fa === null) && (fb === null))
			return 0;
		if (fa === null)
			return JavaInteger.MAX_VALUE;
		if (fb === null)
			return JavaInteger.MIN_VALUE;
		return JavaInteger.compare(GostFachbereich.getSortierung(fa), GostFachbereich.getSortierung(fb));
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
	public static compareFachByKuerzel(ka : string | null, kb : string | null) : number {
		return GostFachbereich.compareFach((ka === null) ? null : Fach.data().getWertBySchluessel(ka), (kb === null) ? null : Fach.data().getWertBySchluessel(kb));
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
	public static compareGostFach(fa : GostFach | null, fb : GostFach | null) : number {
		if ((fa === null) && (fb === null))
			return 0;
		if (fa === null)
			return JavaInteger.MAX_VALUE;
		if (fb === null)
			return JavaInteger.MIN_VALUE;
		const cmp : number = GostFachbereich.compareFachByKuerzel(fa.kuerzel, fb.kuerzel);
		if (cmp !== 0)
			return cmp;
		return JavaInteger.compare(fa.sortierung, fb.sortierung);
	}

	/**
	 * Gibt alle Fächer in einer Standard-Sortierung zurück, welche einem Fachbereich der gymnasialen
	 * Oberstufe zugeordnet sind.
	 *
	 * @return die Liste der Fächer
	 */
	public static getAlleFaecherSortiert() : List<Fach> {
		if (GostFachbereich._listAlleFaecher.isEmpty()) {
			GostFachbereich._listAlleFaecher.addAll(GostFachbereich.SPRACHLICH_LITERARISCH_KUENSTLERISCH.getFaecher());
			GostFachbereich._listAlleFaecher.addAll(GostFachbereich.GESCHICHTE.getFaecher());
			GostFachbereich._listAlleFaecher.addAll(GostFachbereich.SOZIALWISSENSCHAFTEN.getFaecher());
			GostFachbereich._listAlleFaecher.addAll(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE.getFaecher());
			GostFachbereich._listAlleFaecher.addAll(GostFachbereich.PHILOSOPHIE.getFaecher());
			GostFachbereich._listAlleFaecher.addAll(GostFachbereich.RELIGION.getFaecher());
			GostFachbereich._listAlleFaecher.addAll(GostFachbereich.MATHEMATISCH_NATURWISSENSCHAFTLICH.getFaecher());
			GostFachbereich._listAlleFaecher.addAll(GostFachbereich.SPORT.getFaecher());
			GostFachbereich._listAlleFaecher.addAll(GostFachbereich.VERTIEFUNGSKURSE.getFaecher());
			GostFachbereich._listAlleFaecher.addAll(GostFachbereich.PROJEKTKURSE.getFaecher());
		}
		return GostFachbereich._listAlleFaecher;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostFachbereich> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostFachbereich | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.gost.GostFachbereich';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.gost.GostFachbereich', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<GostFachbereich>('de.svws_nrw.core.types.gost.GostFachbereich');

}

export function cast_de_svws_nrw_core_types_gost_GostFachbereich(obj : unknown) : GostFachbereich {
	return obj as GostFachbereich;
}
