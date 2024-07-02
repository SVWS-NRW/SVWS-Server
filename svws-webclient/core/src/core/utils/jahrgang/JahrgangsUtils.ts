import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { Jahrgaenge } from '../../../core/types/jahrgang/Jahrgaenge';
import { Schulform } from '../../../core/types/schule/Schulform';
import { JavaLong } from '../../../java/lang/JavaLong';
import { Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import { JahrgangsDaten } from '../../../core/data/jahrgang/JahrgangsDaten';
import { JavaString } from '../../../java/lang/JavaString';
import type { Comparator } from '../../../java/util/Comparator';

export class JahrgangsUtils extends JavaObject {

	/**
	 * Ein Default-Comparator für den Vergleich von Jahrgängen in Jahrgangslisten.
	 */
	public static readonly comparator : Comparator<JahrgangsDaten> = { compare : (a: JahrgangsDaten, b: JahrgangsDaten) => {
		let cmp : number = a.sortierung - b.sortierung;
		if (cmp !== 0)
			return cmp;
		if ((a.kuerzel === null) || (b.kuerzel === null))
			return JavaLong.compare(a.id, b.id);
		cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	/**
	 * Bestimmt für die angegebene Schulform, die übergebene Schulgliederung (auch beim Schüler eingetragenen Schulgliederung)
	 * und den angegebenen Jahrgang die restlichen Jahre an der Schule.
	 * Ist keine Berechnung für diese Kombination implementiert,
	 * so wird null zurückgegeben.
	 *
	 * @param schulform    die Schulform
	 * @param gliederung   die Schulgliederung
	 * @param jahrgang     der Jahrgang, für den die restlichen Jahre bestimmt werden sollen
	 *
	 * @return die restlichen Jahre oder null
	 */
	public static getRestlicheJahre(schulform : Schulform, gliederung : Schulgliederung, jahrgang : string) : number | null {
		if (gliederung === null)
			return null;
		if ((schulform as unknown === Schulform.FW as unknown) || (schulform as unknown === Schulform.WB as unknown) || (schulform as unknown === Schulform.BK as unknown) || (schulform as unknown === Schulform.SB as unknown))
			return null;
		if (jahrgang === null)
			return null;
		if (schulform as unknown === Schulform.GY as unknown) {
			switch (jahrgang) {
				case "05": {
					return (gliederung.istG8() || (gliederung as unknown === Schulgliederung.DEFAULT as unknown)) ? 8 : 9;
				}
				case "06": {
					return (gliederung.istG8() || (gliederung as unknown === Schulgliederung.DEFAULT as unknown)) ? 7 : 8;
				}
				case "07": {
					return (gliederung.istG8() || (gliederung as unknown === Schulgliederung.DEFAULT as unknown)) ? 6 : 7;
				}
				case "08": {
					return (gliederung.istG8() || (gliederung as unknown === Schulgliederung.DEFAULT as unknown)) ? 5 : 6;
				}
				case "09": {
					return (gliederung.istG8() || (gliederung as unknown === Schulgliederung.DEFAULT as unknown)) ? 4 : 5;
				}
				case "10": {
					return gliederung.istG8() ? null : 4;
				}
				case "EF": {
					return 3;
				}
				case "Q1": {
					return 2;
				}
				case "Q2": {
					return 1;
				}
				case "11": {
					return 3;
				}
				case "12": {
					return 2;
				}
				case "13": {
					return 1;
				}
				default: {
					return null;
				}
			}
		}
		switch (jahrgang) {
			case "E1": {
				return 4;
			}
			case "E2": {
				return 3;
			}
			case "E3": {
				return 3;
			}
			case "03": {
				return 2;
			}
			case "04": {
				return 1;
			}
			case "05": {
				return 6;
			}
			case "06": {
				return 5;
			}
			case "07": {
				return 4;
			}
			case "08": {
				return 3;
			}
			case "09": {
				return 2;
			}
			case "10": {
				return 1;
			}
			case "EF": {
				return 3;
			}
			case "Q1": {
				return 2;
			}
			case "Q2": {
				return 1;
			}
			case "11": {
				return 3;
			}
			case "12": {
				return 2;
			}
			case "13": {
				return 1;
			}
			default: {
				return null;
			}
		}
	}

	/**
	 * Gibt zurück, ob es sich bei dem Statistik-Jahrgang um einen Jahrgang der Sekundarstufe I handelt oder nicht.
	 *
	 * @param jahrgang   der Statistik-Jahrgang
	 *
	 * @return true, falls es sich um einen Sek I-Jahrgang handelt, und ansonsten false
	 */
	public static istSekI(jahrgang : string) : boolean {
		return JavaObject.equalsTranspiler(Jahrgaenge.JG_05.daten.kuerzel, (jahrgang)) || JavaObject.equalsTranspiler(Jahrgaenge.JG_06.daten.kuerzel, (jahrgang)) || JavaObject.equalsTranspiler(Jahrgaenge.JG_07.daten.kuerzel, (jahrgang)) || JavaObject.equalsTranspiler(Jahrgaenge.JG_08.daten.kuerzel, (jahrgang)) || JavaObject.equalsTranspiler(Jahrgaenge.JG_09.daten.kuerzel, (jahrgang)) || JavaObject.equalsTranspiler(Jahrgaenge.JG_10.daten.kuerzel, (jahrgang));
	}

	/**
	 * Gibt zurück, ob es sich bei dem Statistik-Jahrgang um einen Jahrgang der Gymnasialen Oberstufe handelt oder nicht.
	 *
	 * @param jahrgang   der Statistik-Jahrgang
	 *
	 * @return true, falls es sich um einen Jahrgang der Gymnasialen Oberstufe handelt, und ansonsten false
	 */
	public static istGymOb(jahrgang : string) : boolean {
		return JavaObject.equalsTranspiler(Jahrgaenge.JG_EF.daten.kuerzel, (jahrgang)) || JavaObject.equalsTranspiler(Jahrgaenge.JG_Q1.daten.kuerzel, (jahrgang)) || JavaObject.equalsTranspiler(Jahrgaenge.JG_Q2.daten.kuerzel, (jahrgang));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.jahrgang.JahrgangsUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.jahrgang.JahrgangsUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_jahrgang_JahrgangsUtils(obj : unknown) : JahrgangsUtils {
	return obj as JahrgangsUtils;
}
