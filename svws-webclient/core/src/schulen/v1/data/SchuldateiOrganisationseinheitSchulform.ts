import { SchuldateiEintrag } from '../../../schulen/v1/data/SchuldateiEintrag';
import { Class } from '../../../java/lang/Class';

export class SchuldateiOrganisationseinheitSchulform extends SchuldateiEintrag {

	/**
	 * Die ID des Schulform-Eintrages.
	 */
	public id : string | null = null;

	/**
	 * Die Schulnummer.
	 */
	public schulnummer : string = "";

	/**
	 * Schulformcode
	 */
	public schulformcode : string = "";

	/**
	 * Schulformwert
	 */
	public schulformwert : string = "";


	/**
	 * Erstellt eine neue Schulform für eine Organiationseinheit der Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitSchulform';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitSchulform', 'de.svws_nrw.schulen.v1.data.SchuldateiEintrag'].includes(name);
	}

	public static class = new Class<SchuldateiOrganisationseinheitSchulform>('de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitSchulform');

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheitSchulform {
		const obj = JSON.parse(json) as Partial<SchuldateiOrganisationseinheitSchulform>;
		const result = new SchuldateiOrganisationseinheitSchulform();
		result.gueltigab = (obj.gueltigab === undefined) ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = (obj.gueltigbis === undefined) ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = (obj.geaendertam === undefined) ? null : obj.geaendertam === null ? null : obj.geaendertam;
		result.id = (obj.id === undefined) ? null : obj.id === null ? null : obj.id;
		if (obj.schulnummer === undefined)
			throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		if (obj.schulformcode === undefined)
			throw new Error('invalid json format, missing attribute schulformcode');
		result.schulformcode = obj.schulformcode;
		if (obj.schulformwert === undefined)
			throw new Error('invalid json format, missing attribute schulformwert');
		result.schulformwert = obj.schulformwert;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheitSchulform) : string {
		let result = '{';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result += '"id" : ' + ((!obj.id) ? 'null' : JSON.stringify(obj.id)) + ',';
		result += '"schulnummer" : ' + JSON.stringify(obj.schulnummer) + ',';
		result += '"schulformcode" : ' + JSON.stringify(obj.schulformcode) + ',';
		result += '"schulformwert" : ' + JSON.stringify(obj.schulformwert) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiOrganisationseinheitSchulform>) : string {
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
			result += '"id" : ' + ((!obj.id) ? 'null' : JSON.stringify(obj.id)) + ',';
		}
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + JSON.stringify(obj.schulnummer) + ',';
		}
		if (obj.schulformcode !== undefined) {
			result += '"schulformcode" : ' + JSON.stringify(obj.schulformcode) + ',';
		}
		if (obj.schulformwert !== undefined) {
			result += '"schulformwert" : ' + JSON.stringify(obj.schulformwert) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schulen_v1_data_SchuldateiOrganisationseinheitSchulform(obj : unknown) : SchuldateiOrganisationseinheitSchulform {
	return obj as SchuldateiOrganisationseinheitSchulform;
}
