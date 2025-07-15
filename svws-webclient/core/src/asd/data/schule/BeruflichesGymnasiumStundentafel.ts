import { JavaObject } from '../../../java/lang/JavaObject';
import { BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit } from '../../../asd/data/schule/BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { BeruflichesGymnasiumStundentafelFach } from '../../../asd/data/schule/BeruflichesGymnasiumStundentafelFach';

export class BeruflichesGymnasiumStundentafel extends JavaObject {

	/**
	 * Die Variante der Stundentafel bezogen auf einen Bildungsgang
	 */
	public variante : number = 0;

	/**
	 * Die allgemeine Bezeichnung der Abschnitte (z.B. Quartal oder Halbjahr)
	 */
	public faecher : List<BeruflichesGymnasiumStundentafelFach> = new ArrayList<BeruflichesGymnasiumStundentafelFach>();

	/**
	 * Die Wahlmöglichkeiten der Abiturfächer dieser Variante inkl. zukünftigem fünften Fach
	 */
	public wahlmoeglichkeiten : List<BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit> = new ArrayList<BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafel';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafel'].includes(name);
	}

	public static class = new Class<BeruflichesGymnasiumStundentafel>('de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafel');

	public static transpilerFromJSON(json : string): BeruflichesGymnasiumStundentafel {
		const obj = JSON.parse(json) as Partial<BeruflichesGymnasiumStundentafel>;
		const result = new BeruflichesGymnasiumStundentafel();
		if (obj.variante === undefined)
			throw new Error('invalid json format, missing attribute variante');
		result.variante = obj.variante;
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(BeruflichesGymnasiumStundentafelFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.wahlmoeglichkeiten !== undefined) {
			for (const elem of obj.wahlmoeglichkeiten) {
				result.wahlmoeglichkeiten.add(BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BeruflichesGymnasiumStundentafel) : string {
		let result = '{';
		result += '"variante" : ' + obj.variante.toString() + ',';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += BeruflichesGymnasiumStundentafelFach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"wahlmoeglichkeiten" : [ ';
		for (let i = 0; i < obj.wahlmoeglichkeiten.size(); i++) {
			const elem = obj.wahlmoeglichkeiten.get(i);
			result += BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit.transpilerToJSON(elem);
			if (i < obj.wahlmoeglichkeiten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BeruflichesGymnasiumStundentafel>) : string {
		let result = '{';
		if (obj.variante !== undefined) {
			result += '"variante" : ' + obj.variante.toString() + ',';
		}
		if (obj.faecher !== undefined) {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += BeruflichesGymnasiumStundentafelFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.wahlmoeglichkeiten !== undefined) {
			result += '"wahlmoeglichkeiten" : [ ';
			for (let i = 0; i < obj.wahlmoeglichkeiten.size(); i++) {
				const elem = obj.wahlmoeglichkeiten.get(i);
				result += BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit.transpilerToJSON(elem);
				if (i < obj.wahlmoeglichkeiten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schule_BeruflichesGymnasiumStundentafel(obj : unknown) : BeruflichesGymnasiumStundentafel {
	return obj as BeruflichesGymnasiumStundentafel;
}
