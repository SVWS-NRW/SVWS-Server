import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchulwechselAbgang extends JavaObject {

	/**
	 * Die ID des Wechselvorgangs.
	 */
	public id: number = 0;

	/**
	 * Die Id des Schülerdatensatzes auf den sich der Wechselvorgang bezieht.
	 */
	public idSchueler: number = 0;

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
		return 'de.svws_nrw.core.data.schule.SchulwechselAbgang';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.SchulwechselAbgang'].includes(name);
	}

	public static readonly class = new Class<SchulwechselAbgang>('de.svws_nrw.core.data.schule.SchulwechselAbgang');

	public static transpilerFromJSON(json: string): SchulwechselAbgang {
		const obj = JSON.parse(json) as Partial<SchulwechselAbgang>;
		const result = new SchulwechselAbgang();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idSchueler === undefined)
			throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		if (obj.idStatus === undefined)
			throw new Error('invalid json format, missing attribute idStatus');
		result.idStatus = obj.idStatus;
		result.lastModified = (obj.lastModified === undefined) ? null : obj.lastModified === null ? null : obj.lastModified;
		result.idDocument = (obj.idDocument === undefined) ? null : obj.idDocument === null ? null : obj.idDocument;
		result.idSchulkindSchulbewerbung = (obj.idSchulkindSchulbewerbung === undefined) ? null : obj.idSchulkindSchulbewerbung === null ? null : obj.idSchulkindSchulbewerbung;
		return result;
	}

	public static transpilerToJSON(obj: SchulwechselAbgang): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		result += '"idStatus" : ' + obj.idStatus.toString() + ',';
		result += '"lastModified" : ' + ((obj.lastModified === null) ? 'null' : JSON.stringify(obj.lastModified)) + ',';
		result += '"idDocument" : ' + ((obj.idDocument === null) ? 'null' : obj.idDocument.toString()) + ',';
		result += '"idSchulkindSchulbewerbung" : ' + ((obj.idSchulkindSchulbewerbung === null) ? 'null' : JSON.stringify(obj.idSchulkindSchulbewerbung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchulwechselAbgang>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
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

export function cast_de_svws_nrw_core_data_schule_SchulwechselAbgang(obj: unknown): SchulwechselAbgang {
	return obj as SchulwechselAbgang;
}
