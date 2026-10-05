import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchulwechselDokument extends JavaObject {

	/**
	 * Die ID des Dokuments.
	 */
	public id: number = 0;

	/**
	 * Der Dateiname des Dokuments
	 */
	public fileName: string | null = null;

	/**
	 * Das XML-Dokument
	 */
	public xmlDocument: string | null = null;

	/**
	 * Der Zeitpunkt der Erstellung des Dokuments.
	 */
	public createdAt: string = "";

	/**
	 * Der Zeitpunkt der letzten Änderung des Dokuments.
	 */
	public lastModified: string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.SchulwechselDokument';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.SchulwechselDokument'].includes(name);
	}

	public static readonly class = new Class<SchulwechselDokument>('de.svws_nrw.core.data.schule.SchulwechselDokument');

	public static transpilerFromJSON(json: string): SchulwechselDokument {
		const obj = JSON.parse(json) as Partial<SchulwechselDokument>;
		const result = new SchulwechselDokument();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.fileName = (obj.fileName === undefined) ? null : obj.fileName === null ? null : obj.fileName;
		result.xmlDocument = (obj.xmlDocument === undefined) ? null : obj.xmlDocument === null ? null : obj.xmlDocument;
		if (obj.createdAt === undefined)
			throw new Error('invalid json format, missing attribute createdAt');
		result.createdAt = obj.createdAt;
		if (obj.lastModified === undefined)
			throw new Error('invalid json format, missing attribute lastModified');
		result.lastModified = obj.lastModified;
		return result;
	}

	public static transpilerToJSON(obj: SchulwechselDokument): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"fileName" : ' + ((obj.fileName === null) ? 'null' : JSON.stringify(obj.fileName)) + ',';
		result += '"xmlDocument" : ' + ((obj.xmlDocument === null) ? 'null' : JSON.stringify(obj.xmlDocument)) + ',';
		result += '"createdAt" : ' + JSON.stringify(obj.createdAt) + ',';
		result += '"lastModified" : ' + JSON.stringify(obj.lastModified) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchulwechselDokument>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.fileName !== undefined) {
			result += '"fileName" : ' + ((obj.fileName === null) ? 'null' : JSON.stringify(obj.fileName)) + ',';
		}
		if (obj.xmlDocument !== undefined) {
			result += '"xmlDocument" : ' + ((obj.xmlDocument === null) ? 'null' : JSON.stringify(obj.xmlDocument)) + ',';
		}
		if (obj.createdAt !== undefined) {
			result += '"createdAt" : ' + JSON.stringify(obj.createdAt) + ',';
		}
		if (obj.lastModified !== undefined) {
			result += '"lastModified" : ' + JSON.stringify(obj.lastModified) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_SchulwechselDokument(obj: unknown): SchulwechselDokument {
	return obj as SchulwechselDokument;
}
