import { SchuldateiEintrag } from '../../../schuldatei/v1/data/SchuldateiEintrag';

export class SchuldateiOrganisationseinheitGliederung extends SchuldateiEintrag {

	/**
	 * Die ID des Gliederungs-Eintrages.
	 */
	public id : number | null = null;

	/**
	 * Die Schulnummer.
	 */
	public schulnummer : number = 0;

	/**
	 * Die Gliederung
	 */
	public gliederung : string | null = null;

	/**
	 * Der Förderschwerpunkt
	 */
	public foerderschwerpunkt : string | null = null;


	/**
	 * Erstellt eine neue Gliederung für eine Organisationseinheit der Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheitGliederung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.data.SchuldateiEintrag', 'de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheitGliederung'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheitGliederung {
		const obj = JSON.parse(json);
		const result = new SchuldateiOrganisationseinheitGliederung();
		result.gueltigab = (obj.gueltigab === undefined) ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = (obj.gueltigbis === undefined) ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = (obj.geaendertam === undefined) ? null : obj.geaendertam === null ? null : obj.geaendertam;
		result.id = (obj.id === undefined) ? null : obj.id === null ? null : obj.id;
		if (obj.schulnummer === undefined)
			 throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		result.gliederung = (obj.gliederung === undefined) ? null : obj.gliederung === null ? null : obj.gliederung;
		result.foerderschwerpunkt = (obj.foerderschwerpunkt === undefined) ? null : obj.foerderschwerpunkt === null ? null : obj.foerderschwerpunkt;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheitGliederung) : string {
		let result = '{';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id) + ',';
		result += '"schulnummer" : ' + obj.schulnummer! + ',';
		result += '"gliederung" : ' + ((!obj.gliederung) ? 'null' : JSON.stringify(obj.gliederung)) + ',';
		result += '"foerderschwerpunkt" : ' + ((!obj.foerderschwerpunkt) ? 'null' : JSON.stringify(obj.foerderschwerpunkt)) + ',';
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
		if (obj.id !== undefined) {
			result += '"id" : ' + ((!obj.id) ? 'null' : obj.id) + ',';
		}
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + obj.schulnummer + ',';
		}
		if (obj.gliederung !== undefined) {
			result += '"gliederung" : ' + ((!obj.gliederung) ? 'null' : JSON.stringify(obj.gliederung)) + ',';
		}
		if (obj.foerderschwerpunkt !== undefined) {
			result += '"foerderschwerpunkt" : ' + ((!obj.foerderschwerpunkt) ? 'null' : JSON.stringify(obj.foerderschwerpunkt)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schuldatei_v1_data_SchuldateiOrganisationseinheitGliederung(obj : unknown) : SchuldateiOrganisationseinheitGliederung {
	return obj as SchuldateiOrganisationseinheitGliederung;
}
