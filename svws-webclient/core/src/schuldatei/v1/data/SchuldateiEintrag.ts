import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiOrganisationseinheit } from '../../../schuldatei/v1/data/SchuldateiOrganisationseinheit';

export class SchuldateiEintrag extends JavaObject {

	/**
	 * Die Organisationseinheit des Eintrags
	 */
	public organisationseinheit : SchuldateiOrganisationseinheit = new SchuldateiOrganisationseinheit();


	/**
	 * Erstellt einen neuen Eintrag für die Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.data.SchuldateiEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.data.SchuldateiEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuldateiEintrag {
		const obj = JSON.parse(json);
		const result = new SchuldateiEintrag();
		if (typeof obj.organisationseinheit === "undefined")
			 throw new Error('invalid json format, missing attribute organisationseinheit');
		result.organisationseinheit = SchuldateiOrganisationseinheit.transpilerFromJSON(JSON.stringify(obj.organisationseinheit));
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiEintrag) : string {
		let result = '{';
		result += '"organisationseinheit" : ' + SchuldateiOrganisationseinheit.transpilerToJSON(obj.organisationseinheit) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiEintrag>) : string {
		let result = '{';
		if (typeof obj.organisationseinheit !== "undefined") {
			result += '"organisationseinheit" : ' + SchuldateiOrganisationseinheit.transpilerToJSON(obj.organisationseinheit) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schuldatei_v1_data_SchuldateiEintrag(obj : unknown) : SchuldateiEintrag {
	return obj as SchuldateiEintrag;
}
