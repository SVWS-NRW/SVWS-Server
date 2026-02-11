import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Floskelgruppe extends JavaObject {

	/**
	 * Die ID der Floskelgruppe
	 */
	public id: number = -1;

	/**
	 * Das Kürzel der Floskelgruppe
	 */
	public kuerzel: string = "";

	/**
	 * Die Bezeichnung der Floskelgruppe
	 */
	public bezeichnung: string = "";

	/**
	 * Die ID der Floskelgruppenart
	 */
	public idFloskelgruppenart: number | null = null;

	/**
	 * Gibt an, ob die Telefonart in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen: boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Floskelgruppe';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.Floskelgruppe'].includes(name);
	}

	public static readonly class = new Class<Floskelgruppe>('de.svws_nrw.core.data.schule.Floskelgruppe');

	public static transpilerFromJSON(json: string): Floskelgruppe {
		const obj = JSON.parse(json) as Partial<Floskelgruppe>;
		const result = new Floskelgruppe();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		result.idFloskelgruppenart = (obj.idFloskelgruppenart === undefined) ? null : obj.idFloskelgruppenart === null ? null : obj.idFloskelgruppenart;
		if (obj.referenziertInAnderenTabellen === undefined)
			throw new Error('invalid json format, missing attribute referenziertInAnderenTabellen');
		result.referenziertInAnderenTabellen = obj.referenziertInAnderenTabellen;
		return result;
	}

	public static transpilerToJSON(obj: Floskelgruppe): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"idFloskelgruppenart" : ' + ((obj.idFloskelgruppenart === null) ? 'null' : obj.idFloskelgruppenart.toString()) + ',';
		result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<Floskelgruppe>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.idFloskelgruppenart !== undefined) {
			result += '"idFloskelgruppenart" : ' + ((obj.idFloskelgruppenart === null) ? 'null' : obj.idFloskelgruppenart.toString()) + ',';
		}
		if (obj.referenziertInAnderenTabellen !== undefined) {
			result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Floskelgruppe(obj: unknown): Floskelgruppe {
	return obj as Floskelgruppe;
}
