import { SchuldateiEintrag } from '../../../schulen/v1/data/SchuldateiEintrag';
import { Class } from '../../../java/lang/Class';

export class SchuldateiOrganisationseinheitErreichbarkeit extends SchuldateiEintrag {

	/**
	 * Die Schulnummer.
	 */
	public schulnummer : string = "";

	/**
	 * Die ID des Erreichbarkeits-Eintrags.
	 */
	public id : number | null = null;

	/**
	 * Die Nummer der Liegenschaft der Organisationseinheit
	 */
	public liegenschaft : number = 0;

	/**
	 * Kommgruppe des Eintrags
	 */
	public kommgruppe : string = "";

	/**
	 * codekey des Eintrags
	 */
	public codekey : string = "";

	/**
	 * codewert des Eintrags
	 */
	public codewert : string = "";


	/**
	 * Erstellt einen neuen Eintrag zur Erreichbarkeit einer Organisationseinheit der Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitErreichbarkeit';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.data.SchuldateiEintrag', 'de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitErreichbarkeit'].includes(name);
	}

	public static class = new Class<SchuldateiOrganisationseinheitErreichbarkeit>('de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitErreichbarkeit');

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheitErreichbarkeit {
		const obj = JSON.parse(json) as Partial<SchuldateiOrganisationseinheitErreichbarkeit>;
		const result = new SchuldateiOrganisationseinheitErreichbarkeit();
		result.gueltigab = (obj.gueltigab === undefined) ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = (obj.gueltigbis === undefined) ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = (obj.geaendertam === undefined) ? null : obj.geaendertam === null ? null : obj.geaendertam;
		if (obj.schulnummer === undefined)
			throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		result.id = (obj.id === undefined) ? null : obj.id === null ? null : obj.id;
		if (obj.liegenschaft === undefined)
			throw new Error('invalid json format, missing attribute liegenschaft');
		result.liegenschaft = obj.liegenschaft;
		if (obj.kommgruppe === undefined)
			throw new Error('invalid json format, missing attribute kommgruppe');
		result.kommgruppe = obj.kommgruppe;
		if (obj.codekey === undefined)
			throw new Error('invalid json format, missing attribute codekey');
		result.codekey = obj.codekey;
		if (obj.codewert === undefined)
			throw new Error('invalid json format, missing attribute codewert');
		result.codewert = obj.codewert;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheitErreichbarkeit) : string {
		let result = '{';
		result += '"gueltigab" : ' + ((obj.gueltigab === null) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((obj.gueltigbis === null) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((obj.geaendertam === null) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result += '"schulnummer" : ' + JSON.stringify(obj.schulnummer) + ',';
		result += '"id" : ' + ((obj.id === null) ? 'null' : obj.id.toString()) + ',';
		result += '"liegenschaft" : ' + obj.liegenschaft.toString() + ',';
		result += '"kommgruppe" : ' + JSON.stringify(obj.kommgruppe) + ',';
		result += '"codekey" : ' + JSON.stringify(obj.codekey) + ',';
		result += '"codewert" : ' + JSON.stringify(obj.codewert) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiOrganisationseinheitErreichbarkeit>) : string {
		let result = '{';
		if (obj.gueltigab !== undefined) {
			result += '"gueltigab" : ' + ((obj.gueltigab === null) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		}
		if (obj.gueltigbis !== undefined) {
			result += '"gueltigbis" : ' + ((obj.gueltigbis === null) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		}
		if (obj.geaendertam !== undefined) {
			result += '"geaendertam" : ' + ((obj.geaendertam === null) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		}
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + JSON.stringify(obj.schulnummer) + ',';
		}
		if (obj.id !== undefined) {
			result += '"id" : ' + ((obj.id === null) ? 'null' : obj.id.toString()) + ',';
		}
		if (obj.liegenschaft !== undefined) {
			result += '"liegenschaft" : ' + obj.liegenschaft.toString() + ',';
		}
		if (obj.kommgruppe !== undefined) {
			result += '"kommgruppe" : ' + JSON.stringify(obj.kommgruppe) + ',';
		}
		if (obj.codekey !== undefined) {
			result += '"codekey" : ' + JSON.stringify(obj.codekey) + ',';
		}
		if (obj.codewert !== undefined) {
			result += '"codewert" : ' + JSON.stringify(obj.codewert) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schulen_v1_data_SchuldateiOrganisationseinheitErreichbarkeit(obj : unknown) : SchuldateiOrganisationseinheitErreichbarkeit {
	return obj as SchuldateiOrganisationseinheitErreichbarkeit;
}
