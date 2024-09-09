import { SchuldateiEintrag } from '../../../schulen/v1/data/SchuldateiEintrag';
import { Class } from '../../../java/lang/Class';

export class SchuldateiOrganisationseinheitGliederung extends SchuldateiEintrag {

	/**
	 * Die Schulnummer.
	 */
	public schulnummer : string = "";

	/**
	 * Die ID des Gliederungs-Eintrages.
	 */
	public id : number | null = null;

	/**
	 * Die Gliederung
	 */
	public gliederung : string = "";

	/**
	 * Der Förderschwerpunkt
	 */
	public foerderschwerpunkt : string = "";


	/**
	 * Erstellt eine neue Gliederung für eine Organisationseinheit der Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitGliederung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitGliederung', 'de.svws_nrw.schulen.v1.data.SchuldateiEintrag'].includes(name);
	}

	public static class = new Class<SchuldateiOrganisationseinheitGliederung>('de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitGliederung');

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheitGliederung {
		const obj = JSON.parse(json) as Partial<SchuldateiOrganisationseinheitGliederung>;
		const result = new SchuldateiOrganisationseinheitGliederung();
		result.gueltigab = (obj.gueltigab === undefined) ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = (obj.gueltigbis === undefined) ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = (obj.geaendertam === undefined) ? null : obj.geaendertam === null ? null : obj.geaendertam;
		if (obj.schulnummer === undefined)
			throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		result.id = (obj.id === undefined) ? null : obj.id === null ? null : obj.id;
		if (obj.gliederung === undefined)
			throw new Error('invalid json format, missing attribute gliederung');
		result.gliederung = obj.gliederung;
		if (obj.foerderschwerpunkt === undefined)
			throw new Error('invalid json format, missing attribute foerderschwerpunkt');
		result.foerderschwerpunkt = obj.foerderschwerpunkt;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheitGliederung) : string {
		let result = '{';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result += '"schulnummer" : ' + JSON.stringify(obj.schulnummer) + ',';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id.toString()) + ',';
		result += '"gliederung" : ' + JSON.stringify(obj.gliederung) + ',';
		result += '"foerderschwerpunkt" : ' + JSON.stringify(obj.foerderschwerpunkt) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiOrganisationseinheitGliederung>) : string {
		let result = '{';
		if (obj.gueltigab !== undefined) {
			result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		}
		if (obj.gueltigbis !== undefined) {
			result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		}
		if (obj.geaendertam !== undefined) {
			result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		}
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + JSON.stringify(obj.schulnummer) + ',';
		}
		if (obj.id !== undefined) {
			result += '"id" : ' + ((!obj.id) ? 'null' : obj.id.toString()) + ',';
		}
		if (obj.gliederung !== undefined) {
			result += '"gliederung" : ' + JSON.stringify(obj.gliederung) + ',';
		}
		if (obj.foerderschwerpunkt !== undefined) {
			result += '"foerderschwerpunkt" : ' + JSON.stringify(obj.foerderschwerpunkt) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schulen_v1_data_SchuldateiOrganisationseinheitGliederung(obj : unknown) : SchuldateiOrganisationseinheitGliederung {
	return obj as SchuldateiOrganisationseinheitGliederung;
}
