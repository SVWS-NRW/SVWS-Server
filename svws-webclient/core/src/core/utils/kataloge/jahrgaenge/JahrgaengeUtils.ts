import { JavaObject } from '../../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../../java/lang/IllegalStateException';
import { Jahrgaenge } from '../../../../asd/types/jahrgang/Jahrgaenge';
import { Schulform } from '../../../../asd/types/schule/Schulform';
import { SchulformKatalogEintrag } from '../../../../asd/data/schule/SchulformKatalogEintrag';
import { Schulgliederung } from '../../../../asd/types/schule/Schulgliederung';
import { Class } from '../../../../java/lang/Class';

export class JahrgaengeUtils extends JavaObject {


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
	public static getRestlicheJahre(schulform : Schulform, gliederung : Schulgliederung | null, jahrgang : string | null) : number | null {
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
	 * Bestimmt für die angegebene Schulform, die übergebene Schulgliederung (auch beim Schüler eingetragenen Schulgliederung)
	 * und den angegebenen Jahrgang die restlichen Jahre an der Schule bis zum Abitur.
	 *
	 * @param schulform    die Schulform
	 * @param gliederung   die Schulgliederung
	 * @param schuljahr    das Schuljahr
	 * @param jahrgang     der Jahrgang, für den die restlichen Jahre bestimmt werden sollen
	 *
	 * @return die restlichen Jahre oder null
	 */
	public static getRestlicheJahreBisAbitur(schulform : Schulform, gliederung : Schulgliederung | null, schuljahr : number, jahrgang : string | null) : number | null {
		const sf : SchulformKatalogEintrag | null = schulform.daten(schuljahr);
		if ((sf === null) || (!sf.hatGymOb) || (gliederung === null) || (jahrgang === null))
			return null;
		let _sevar_1739790870 : any;
		const _seexpr_1739790870 = (jahrgang);
		if (_seexpr_1739790870 === "05") {
			_sevar_1739790870 = ((schulform as unknown === Schulform.GY as unknown) && (gliederung.istG8() || (gliederung as unknown === Schulgliederung.DEFAULT as unknown))) ? 8 : 9;
		} else if (_seexpr_1739790870 === "06") {
			_sevar_1739790870 = ((schulform as unknown === Schulform.GY as unknown) && (gliederung.istG8() || (gliederung as unknown === Schulgliederung.DEFAULT as unknown))) ? 7 : 8;
		} else if (_seexpr_1739790870 === "07") {
			_sevar_1739790870 = ((schulform as unknown === Schulform.GY as unknown) && (gliederung.istG8() || (gliederung as unknown === Schulgliederung.DEFAULT as unknown))) ? 6 : 7;
		} else if (_seexpr_1739790870 === "08") {
			_sevar_1739790870 = ((schulform as unknown === Schulform.GY as unknown) && (gliederung.istG8() || (gliederung as unknown === Schulgliederung.DEFAULT as unknown))) ? 5 : 6;
		} else if (_seexpr_1739790870 === "09") {
			_sevar_1739790870 = ((schulform as unknown === Schulform.GY as unknown) && (gliederung.istG8() || (gliederung as unknown === Schulgliederung.DEFAULT as unknown))) ? 4 : 5;
		} else if (_seexpr_1739790870 === "10") {
			_sevar_1739790870 = ((schulform as unknown === Schulform.GY as unknown) && (gliederung.istG8() || (gliederung as unknown === Schulgliederung.DEFAULT as unknown))) ? null : 4;
		} else if (_seexpr_1739790870 === "EF") {
			_sevar_1739790870 = 3;
		} else if (_seexpr_1739790870 === "11") {
			_sevar_1739790870 = 3;
		} else if (_seexpr_1739790870 === "Q1") {
			_sevar_1739790870 = 2;
		} else if (_seexpr_1739790870 === "12") {
			_sevar_1739790870 = 2;
		} else if (_seexpr_1739790870 === "Q2") {
			_sevar_1739790870 = 1;
		} else if (_seexpr_1739790870 === "13") {
			_sevar_1739790870 = 1;
		} else {
			_sevar_1739790870 = null;
		}
		return _sevar_1739790870;
	}

	/**
	 * Gibt zurück, ob es sich bei dem Statistik-Jahrgang um einen Jahrgang der Sekundarstufe I handelt oder nicht.
	 *
	 * @param jahrgang   der Statistik-Jahrgang
	 *
	 * @return true, falls es sich um einen Sek I-Jahrgang handelt, und ansonsten false
	 */
	public static istSekI(jahrgang : string) : boolean {
		const jg : Jahrgaenge | null = Jahrgaenge.data().getWertByKuerzel(jahrgang);
		let _sevar_590951878 : any;
		const _seexpr_590951878 = (jg);
		if (_seexpr_590951878 === Jahrgaenge.JAHRGANG_05) {
			_sevar_590951878 = true;
		} else if (_seexpr_590951878 === Jahrgaenge.JAHRGANG_06) {
			_sevar_590951878 = true;
		} else if (_seexpr_590951878 === Jahrgaenge.JAHRGANG_07) {
			_sevar_590951878 = true;
		} else if (_seexpr_590951878 === Jahrgaenge.JAHRGANG_08) {
			_sevar_590951878 = true;
		} else if (_seexpr_590951878 === Jahrgaenge.JAHRGANG_09) {
			_sevar_590951878 = true;
		} else if (_seexpr_590951878 === Jahrgaenge.JAHRGANG_10) {
			_sevar_590951878 = true;
		} else {
			_sevar_590951878 = false;
		}
		return _sevar_590951878;
	}

	/**
	 * Gibt zurück, ob es sich bei dem Statistik-Jahrgang um einen Jahrgang der Gymnasialen Oberstufe handelt oder nicht.
	 *
	 * @param jahrgang   der Statistik-Jahrgang
	 *
	 * @return true, falls es sich um einen Jahrgang der Gymnasialen Oberstufe handelt, und ansonsten false
	 */
	public static istGymOb(jahrgang : string) : boolean {
		const jg : Jahrgaenge | null = Jahrgaenge.data().getWertByKuerzel(jahrgang);
		let _sevar_1826259116 : any;
		const _seexpr_1826259116 = (jg);
		if (_seexpr_1826259116 === Jahrgaenge.EF) {
			_sevar_1826259116 = true;
		} else if (_seexpr_1826259116 === Jahrgaenge.Q1) {
			_sevar_1826259116 = true;
		} else if (_seexpr_1826259116 === Jahrgaenge.Q2) {
			_sevar_1826259116 = true;
		} else {
			_sevar_1826259116 = false;
		}
		return _sevar_1826259116;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.kataloge.jahrgaenge.JahrgaengeUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.kataloge.jahrgaenge.JahrgaengeUtils'].includes(name);
	}

	public static class = new Class<JahrgaengeUtils>('de.svws_nrw.core.utils.kataloge.jahrgaenge.JahrgaengeUtils');

}

export function cast_de_svws_nrw_core_utils_kataloge_jahrgaenge_JahrgaengeUtils(obj : unknown) : JahrgaengeUtils {
	return obj as JahrgaengeUtils;
}
