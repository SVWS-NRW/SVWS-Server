import { JavaObject } from '../../../../java/lang/JavaObject';
import { KursDaten } from '../../../../asd/data/kurse/KursDaten';
import { GostFach } from '../../../../core/data/gost/GostFach';
import { SchuelerListeEintrag } from '../../../../core/data/schueler/SchuelerListeEintrag';
import { LehrerListeEintrag } from '../../../../core/data/lehrer/LehrerListeEintrag';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class GostKlausurenKlausurdatenMetadaten extends JavaObject {

	/**
	 * Ein Array mit den Daten der Fächer.
	 */
	public faecher: List<GostFach> = new ArrayList<GostFach>();

	/**
	 * Ein Array mit den Daten der Schüler.
	 */
	public schueler: List<SchuelerListeEintrag> = new ArrayList<SchuelerListeEintrag>();

	/**
	 * Ein Array mit den Daten der Lehrer.
	 */
	public lehrer: List<LehrerListeEintrag> = new ArrayList<LehrerListeEintrag>();

	/**
	 * Ein Array mit den Daten der Kurse.
	 */
	public kurse: List<KursDaten> = new ArrayList<KursDaten>();


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdatenMetadaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdatenMetadaten'].includes(name);
	}

	public static readonly class = new Class<GostKlausurenKlausurdatenMetadaten>('de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdatenMetadaten');

	public static transpilerFromJSON(json: string): GostKlausurenKlausurdatenMetadaten {
		const obj = JSON.parse(json) as Partial<GostKlausurenKlausurdatenMetadaten>;
		const result = new GostKlausurenKlausurdatenMetadaten();
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(GostFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schueler !== undefined) {
			for (const elem of obj.schueler) {
				result.schueler.add(SchuelerListeEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lehrer !== undefined) {
			for (const elem of obj.lehrer) {
				result.lehrer.add(LehrerListeEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.kurse !== undefined) {
			for (const elem of obj.kurse) {
				result.kurse.add(KursDaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: GostKlausurenKlausurdatenMetadaten): string {
		let result = '{';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += GostFach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schueler" : [ ';
		for (let i = 0; i < obj.schueler.size(); i++) {
			const elem = obj.schueler.get(i);
			result += SchuelerListeEintrag.transpilerToJSON(elem);
			if (i < obj.schueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lehrer" : [ ';
		for (let i = 0; i < obj.lehrer.size(); i++) {
			const elem = obj.lehrer.get(i);
			result += LehrerListeEintrag.transpilerToJSON(elem);
			if (i < obj.lehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kurse" : [ ';
		for (let i = 0; i < obj.kurse.size(); i++) {
			const elem = obj.kurse.get(i);
			result += KursDaten.transpilerToJSON(elem);
			if (i < obj.kurse.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostKlausurenKlausurdatenMetadaten>): string {
		let result = '{';
		if (obj.faecher !== undefined) {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += GostFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schueler !== undefined) {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += SchuelerListeEintrag.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += LehrerListeEintrag.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kurse !== undefined) {
			result += '"kurse" : [ ';
			for (let i = 0; i < obj.kurse.size(); i++) {
				const elem = obj.kurse.get(i);
				result += KursDaten.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausuren_GostKlausurenKlausurdatenMetadaten(obj: unknown): GostKlausurenKlausurdatenMetadaten {
	return obj as GostKlausurenKlausurdatenMetadaten;
}
