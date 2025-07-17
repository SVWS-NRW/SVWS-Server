import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class BeruflichesGymnasiumStundentafelFach extends JavaObject {

	/**
	 * Die Bezeichnung des Fachs laut Prüfungsordnung
	 */
	public fachbezeichnung : string = "";

	/**
	 * Die Kursart des Fachs
	 */
	public kursart : string = "";

	/**
	 * Der Stundenumfang für alle sechs Halbjahre EF.1, EF.2, Q1.1, Q1.2, Q2.1, Q2.2
	 */
	public stundenumfang : Array<number> = Array(6).fill(0);

	/**
	 * Der Zeugnisbereich des Fachs
	 */
	public zeugnisbereich : string = "";

	/**
	 * Die Position des Fachs in der Stundentafel
	 */
	public sortierung : number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelFach';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelFach'].includes(name);
	}

	public static class = new Class<BeruflichesGymnasiumStundentafelFach>('de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelFach');

	public static transpilerFromJSON(json : string): BeruflichesGymnasiumStundentafelFach {
		const obj = JSON.parse(json) as Partial<BeruflichesGymnasiumStundentafelFach>;
		const result = new BeruflichesGymnasiumStundentafelFach();
		if (obj.fachbezeichnung === undefined)
			throw new Error('invalid json format, missing attribute fachbezeichnung');
		result.fachbezeichnung = obj.fachbezeichnung;
		if (obj.kursart === undefined)
			throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (obj.stundenumfang !== undefined) {
			for (let i = 0; i < obj.stundenumfang.length; i++) {
				result.stundenumfang[i] = obj.stundenumfang[i];
			}
		}
		if (obj.zeugnisbereich === undefined)
			throw new Error('invalid json format, missing attribute zeugnisbereich');
		result.zeugnisbereich = obj.zeugnisbereich;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		return result;
	}

	public static transpilerToJSON(obj : BeruflichesGymnasiumStundentafelFach) : string {
		let result = '{';
		result += '"fachbezeichnung" : ' + JSON.stringify(obj.fachbezeichnung) + ',';
		result += '"kursart" : ' + JSON.stringify(obj.kursart) + ',';
		result += '"stundenumfang" : [ ';
		for (let i = 0; i < obj.stundenumfang.length; i++) {
			const elem = obj.stundenumfang[i];
			result += JSON.stringify(elem);
			if (i < obj.stundenumfang.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"zeugnisbereich" : ' + JSON.stringify(obj.zeugnisbereich) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BeruflichesGymnasiumStundentafelFach>) : string {
		let result = '{';
		if (obj.fachbezeichnung !== undefined) {
			result += '"fachbezeichnung" : ' + JSON.stringify(obj.fachbezeichnung) + ',';
		}
		if (obj.kursart !== undefined) {
			result += '"kursart" : ' + JSON.stringify(obj.kursart) + ',';
		}
		if (obj.stundenumfang !== undefined) {
			const a = obj.stundenumfang;
			result += '"stundenumfang" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += JSON.stringify(elem);
				if (i < a.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.zeugnisbereich !== undefined) {
			result += '"zeugnisbereich" : ' + JSON.stringify(obj.zeugnisbereich) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schule_BeruflichesGymnasiumStundentafelFach(obj : unknown) : BeruflichesGymnasiumStundentafelFach {
	return obj as BeruflichesGymnasiumStundentafelFach;
}
