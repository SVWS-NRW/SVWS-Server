import { SchuldateiEintrag } from '../../../schulen/v1/data/SchuldateiEintrag';
import { Class } from '../../../java/lang/Class';

export class SchuldateiOrganisationseinheitEigenschaft extends SchuldateiEintrag {

	/**
	 * Die Schulnummer.
	 */
	public schulnummer : string = "";

	/**
	 * Die ID des Eigenschafts-Eintrags.
	 */
	public id : number | null = null;

	/**
	 * Die Eigenschaftsnummer
	 */
	public eigenschaft : string = "";

	/**
	 * Beschreibung
	 */
	public beschreibung : string = "";

	/**
	 * Detail
	 */
	public detail : string = "";


	/**
	 * Erstellt eine neue weitere Eigenschaft einer Organiationseinheit der Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitEigenschaft';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.data.SchuldateiEintrag', 'de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitEigenschaft'].includes(name);
	}

	public static class = new Class<SchuldateiOrganisationseinheitEigenschaft>('de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitEigenschaft');

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheitEigenschaft {
		const obj = JSON.parse(json) as Partial<SchuldateiOrganisationseinheitEigenschaft>;
		const result = new SchuldateiOrganisationseinheitEigenschaft();
		result.gueltigab = (obj.gueltigab === undefined) ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = (obj.gueltigbis === undefined) ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = (obj.geaendertam === undefined) ? null : obj.geaendertam === null ? null : obj.geaendertam;
		if (obj.schulnummer === undefined)
			throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		result.id = (obj.id === undefined) ? null : obj.id === null ? null : obj.id;
		if (obj.eigenschaft === undefined)
			throw new Error('invalid json format, missing attribute eigenschaft');
		result.eigenschaft = obj.eigenschaft;
		if (obj.beschreibung === undefined)
			throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		if (obj.detail === undefined)
			throw new Error('invalid json format, missing attribute detail');
		result.detail = obj.detail;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheitEigenschaft) : string {
		let result = '{';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result += '"schulnummer" : ' + JSON.stringify(obj.schulnummer) + ',';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id.toString()) + ',';
		result += '"eigenschaft" : ' + JSON.stringify(obj.eigenschaft) + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		result += '"detail" : ' + JSON.stringify(obj.detail) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiOrganisationseinheitEigenschaft>) : string {
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
		if (obj.eigenschaft !== undefined) {
			result += '"eigenschaft" : ' + JSON.stringify(obj.eigenschaft) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		}
		if (obj.detail !== undefined) {
			result += '"detail" : ' + JSON.stringify(obj.detail) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schulen_v1_data_SchuldateiOrganisationseinheitEigenschaft(obj : unknown) : SchuldateiOrganisationseinheitEigenschaft {
	return obj as SchuldateiOrganisationseinheitEigenschaft;
}
