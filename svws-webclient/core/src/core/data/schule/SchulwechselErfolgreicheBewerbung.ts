import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchulwechselErfolgreicheBewerbung extends JavaObject {

	/**
	 * Die ID des Wechselvorgangs.
	 */
	public id: number = 0;

	/**
	 * Die Id des Schülerdatensatzes auf den sich der Wechselvorgang bezieht.
	 */
	public idSchueler: number | null = null;

	/**
	 * Die Id des aktuellen Status des Wechselvorgangs.
	 */
	public idStatus: number = 0;

	/**
	 * Der Zeitpunkt der letzten Statusänderung des Wechselvorgangs.
	 */
	public lastModified: string | null = null;

	/**
	 * Die Id des XSchule-Dokuments, das zu diesem Wechselvorgang gehört.
	 */
	public idDocument: number | null = null;

	/**
	 * Die eindeutige UUID des Schulkinds bei schulbewerbung.de – identifiziert den Wechselvorgang.
	 */
	public idSchulkindSchulbewerbung: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.SchulwechselErfolgreicheBewerbung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.SchulwechselErfolgreicheBewerbung'].includes(name);
	}

	public static readonly class = new Class<SchulwechselErfolgreicheBewerbung>('de.svws_nrw.core.data.schule.SchulwechselErfolgreicheBewerbung');

	public static transpilerFromJSON(json: string): SchulwechselErfolgreicheBewerbung {
		const obj = JSON.parse(json) as Partial<SchulwechselErfolgreicheBewerbung>;
		const result = new SchulwechselErfolgreicheBewerbung();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.idSchueler = (obj.idSchueler === undefined) ? null : obj.idSchueler === null ? null : obj.idSchueler;
		if (obj.idStatus === undefined)
			throw new Error('invalid json format, missing attribute idStatus');
		result.idStatus = obj.idStatus;
		result.lastModified = (obj.lastModified === undefined) ? null : obj.lastModified === null ? null : obj.lastModified;
		result.idDocument = (obj.idDocument === undefined) ? null : obj.idDocument === null ? null : obj.idDocument;
		result.idSchulkindSchulbewerbung = (obj.idSchulkindSchulbewerbung === undefined) ? null : obj.idSchulkindSchulbewerbung === null ? null : obj.idSchulkindSchulbewerbung;
		return result;
	}

	public static transpilerToJSON(obj: SchulwechselErfolgreicheBewerbung): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchueler" : ' + ((obj.idSchueler === null) ? 'null' : obj.idSchueler.toString()) + ',';
		result += '"idStatus" : ' + obj.idStatus.toString() + ',';
		result += '"lastModified" : ' + ((obj.lastModified === null) ? 'null' : JSON.stringify(obj.lastModified)) + ',';
		result += '"idDocument" : ' + ((obj.idDocument === null) ? 'null' : obj.idDocument.toString()) + ',';
		result += '"idSchulkindSchulbewerbung" : ' + ((obj.idSchulkindSchulbewerbung === null) ? 'null' : JSON.stringify(obj.idSchulkindSchulbewerbung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchulwechselErfolgreicheBewerbung>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + ((obj.idSchueler === null) ? 'null' : obj.idSchueler.toString()) + ',';
		}
		if (obj.idStatus !== undefined) {
			result += '"idStatus" : ' + obj.idStatus.toString() + ',';
		}
		if (obj.lastModified !== undefined) {
			result += '"lastModified" : ' + ((obj.lastModified === null) ? 'null' : JSON.stringify(obj.lastModified)) + ',';
		}
		if (obj.idDocument !== undefined) {
			result += '"idDocument" : ' + ((obj.idDocument === null) ? 'null' : obj.idDocument.toString()) + ',';
		}
		if (obj.idSchulkindSchulbewerbung !== undefined) {
			result += '"idSchulkindSchulbewerbung" : ' + ((obj.idSchulkindSchulbewerbung === null) ? 'null' : JSON.stringify(obj.idSchulkindSchulbewerbung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_SchulwechselErfolgreicheBewerbung(obj: unknown): SchulwechselErfolgreicheBewerbung {
	return obj as SchulwechselErfolgreicheBewerbung;
}
