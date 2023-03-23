import { JavaObject } from '../../../java/lang/JavaObject';

export class KalenderEintrag extends JavaObject {

	/**
	 * ID des Kalendereintrags
	 */
	public id : string = "";

	/**
	 * ID des Kalenders
	 */
	public kalenderId : string = "";

	/**
	 *  URI der ICS-Repräsentation des Kalendereintrags
	 */
	public uid : string = "";

	/**
	 *  Versionskennzeichen des Kontaks
	 */
	public version : string = "";

	/**
	 * das serialisierte .ics dieses Kalendereintrags
	 */
	public data : string = "";

	/**
	 * der Startzeitpunkt dieses Kalendereintrags als SQL-Timestamp
	 */
	public kalenderStart : string | null = null;

	/**
	 * der Endzeitpunkt dieses Kalendereintrags als SQL-Timestamp
	 */
	public kalenderEnde : string | null = null;

	/**
	 * ob der angemeldete Nutzer Schreibrecht auf dem Kalender hat
	 */
	public darfSchreiben : boolean = false;

	/**
	 * ob der angemeldete Nutzer Leserecht auf dem Kalender hat
	 */
	public darfLesen : boolean = false;

	/**
	 * ob der angemeldete Benutzer Besitzer des Kalenders ist, zu dem dieser Eintrag gehört
	 */
	public istBesitzer : boolean = false;

	/**
	 * der Typ des Kalendereintrags
	 */
	public kalenderTyp : string = "VEVENT";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kalender.KalenderEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): KalenderEintrag {
		const obj = JSON.parse(json);
		const result = new KalenderEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kalenderId === "undefined")
			 throw new Error('invalid json format, missing attribute kalenderId');
		result.kalenderId = obj.kalenderId;
		if (typeof obj.uid === "undefined")
			 throw new Error('invalid json format, missing attribute uid');
		result.uid = obj.uid;
		if (typeof obj.version === "undefined")
			 throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		if (typeof obj.data === "undefined")
			 throw new Error('invalid json format, missing attribute data');
		result.data = obj.data;
		result.kalenderStart = typeof obj.kalenderStart === "undefined" ? null : obj.kalenderStart === null ? null : obj.kalenderStart;
		result.kalenderEnde = typeof obj.kalenderEnde === "undefined" ? null : obj.kalenderEnde === null ? null : obj.kalenderEnde;
		if (typeof obj.darfSchreiben === "undefined")
			 throw new Error('invalid json format, missing attribute darfSchreiben');
		result.darfSchreiben = obj.darfSchreiben;
		if (typeof obj.darfLesen === "undefined")
			 throw new Error('invalid json format, missing attribute darfLesen');
		result.darfLesen = obj.darfLesen;
		if (typeof obj.istBesitzer === "undefined")
			 throw new Error('invalid json format, missing attribute istBesitzer');
		result.istBesitzer = obj.istBesitzer;
		if (typeof obj.kalenderTyp === "undefined")
			 throw new Error('invalid json format, missing attribute kalenderTyp');
		result.kalenderTyp = obj.kalenderTyp;
		return result;
	}

	public static transpilerToJSON(obj : KalenderEintrag) : string {
		let result = '{';
		result += '"id" : ' + '"' + obj.id! + '"' + ',';
		result += '"kalenderId" : ' + '"' + obj.kalenderId! + '"' + ',';
		result += '"uid" : ' + '"' + obj.uid! + '"' + ',';
		result += '"version" : ' + '"' + obj.version! + '"' + ',';
		result += '"data" : ' + '"' + obj.data! + '"' + ',';
		result += '"kalenderStart" : ' + ((!obj.kalenderStart) ? 'null' : '"' + obj.kalenderStart + '"') + ',';
		result += '"kalenderEnde" : ' + ((!obj.kalenderEnde) ? 'null' : '"' + obj.kalenderEnde + '"') + ',';
		result += '"darfSchreiben" : ' + obj.darfSchreiben + ',';
		result += '"darfLesen" : ' + obj.darfLesen + ',';
		result += '"istBesitzer" : ' + obj.istBesitzer + ',';
		result += '"kalenderTyp" : ' + '"' + obj.kalenderTyp! + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KalenderEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + '"' + obj.id + '"' + ',';
		}
		if (typeof obj.kalenderId !== "undefined") {
			result += '"kalenderId" : ' + '"' + obj.kalenderId + '"' + ',';
		}
		if (typeof obj.uid !== "undefined") {
			result += '"uid" : ' + '"' + obj.uid + '"' + ',';
		}
		if (typeof obj.version !== "undefined") {
			result += '"version" : ' + '"' + obj.version + '"' + ',';
		}
		if (typeof obj.data !== "undefined") {
			result += '"data" : ' + '"' + obj.data + '"' + ',';
		}
		if (typeof obj.kalenderStart !== "undefined") {
			result += '"kalenderStart" : ' + ((!obj.kalenderStart) ? 'null' : '"' + obj.kalenderStart + '"') + ',';
		}
		if (typeof obj.kalenderEnde !== "undefined") {
			result += '"kalenderEnde" : ' + ((!obj.kalenderEnde) ? 'null' : '"' + obj.kalenderEnde + '"') + ',';
		}
		if (typeof obj.darfSchreiben !== "undefined") {
			result += '"darfSchreiben" : ' + obj.darfSchreiben + ',';
		}
		if (typeof obj.darfLesen !== "undefined") {
			result += '"darfLesen" : ' + obj.darfLesen + ',';
		}
		if (typeof obj.istBesitzer !== "undefined") {
			result += '"istBesitzer" : ' + obj.istBesitzer + ',';
		}
		if (typeof obj.kalenderTyp !== "undefined") {
			result += '"kalenderTyp" : ' + '"' + obj.kalenderTyp + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_kalender_KalenderEintrag(obj : unknown) : KalenderEintrag {
	return obj as KalenderEintrag;
}
