import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class KalenderEintrag extends JavaObject {

	public id : String = "";

	public kalenderId : String = "";

	public uid : String = "";

	public version : String = "";

	public data : String = "";

	public kalenderStart : String | null = null;

	public kalenderEnde : String | null = null;

	public darfSchreiben : boolean = false;

	public darfLesen : boolean = false;

	public istBesitzer : boolean = false;

	public kalenderTyp : String = "VEVENT";


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
		result.id = String(obj.id);
		if (typeof obj.kalenderId === "undefined")
			 throw new Error('invalid json format, missing attribute kalenderId');
		result.kalenderId = String(obj.kalenderId);
		if (typeof obj.uid === "undefined")
			 throw new Error('invalid json format, missing attribute uid');
		result.uid = String(obj.uid);
		if (typeof obj.version === "undefined")
			 throw new Error('invalid json format, missing attribute version');
		result.version = String(obj.version);
		if (typeof obj.data === "undefined")
			 throw new Error('invalid json format, missing attribute data');
		result.data = String(obj.data);
		result.kalenderStart = typeof obj.kalenderStart === "undefined" ? null : obj.kalenderStart === null ? null : String(obj.kalenderStart);
		result.kalenderEnde = typeof obj.kalenderEnde === "undefined" ? null : obj.kalenderEnde === null ? null : String(obj.kalenderEnde);
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
		result.kalenderTyp = String(obj.kalenderTyp);
		return result;
	}

	public static transpilerToJSON(obj : KalenderEintrag) : string {
		let result = '{';
		result += '"id" : ' + '"' + obj.id.valueOf() + '"' + ',';
		result += '"kalenderId" : ' + '"' + obj.kalenderId.valueOf() + '"' + ',';
		result += '"uid" : ' + '"' + obj.uid.valueOf() + '"' + ',';
		result += '"version" : ' + '"' + obj.version.valueOf() + '"' + ',';
		result += '"data" : ' + '"' + obj.data.valueOf() + '"' + ',';
		result += '"kalenderStart" : ' + ((!obj.kalenderStart) ? 'null' : '"' + obj.kalenderStart.valueOf() + '"') + ',';
		result += '"kalenderEnde" : ' + ((!obj.kalenderEnde) ? 'null' : '"' + obj.kalenderEnde.valueOf() + '"') + ',';
		result += '"darfSchreiben" : ' + obj.darfSchreiben + ',';
		result += '"darfLesen" : ' + obj.darfLesen + ',';
		result += '"istBesitzer" : ' + obj.istBesitzer + ',';
		result += '"kalenderTyp" : ' + '"' + obj.kalenderTyp.valueOf() + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KalenderEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + '"' + obj.id.valueOf() + '"' + ',';
		}
		if (typeof obj.kalenderId !== "undefined") {
			result += '"kalenderId" : ' + '"' + obj.kalenderId.valueOf() + '"' + ',';
		}
		if (typeof obj.uid !== "undefined") {
			result += '"uid" : ' + '"' + obj.uid.valueOf() + '"' + ',';
		}
		if (typeof obj.version !== "undefined") {
			result += '"version" : ' + '"' + obj.version.valueOf() + '"' + ',';
		}
		if (typeof obj.data !== "undefined") {
			result += '"data" : ' + '"' + obj.data.valueOf() + '"' + ',';
		}
		if (typeof obj.kalenderStart !== "undefined") {
			result += '"kalenderStart" : ' + ((!obj.kalenderStart) ? 'null' : '"' + obj.kalenderStart.valueOf() + '"') + ',';
		}
		if (typeof obj.kalenderEnde !== "undefined") {
			result += '"kalenderEnde" : ' + ((!obj.kalenderEnde) ? 'null' : '"' + obj.kalenderEnde.valueOf() + '"') + ',';
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
			result += '"kalenderTyp" : ' + '"' + obj.kalenderTyp.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_kalender_KalenderEintrag(obj : unknown) : KalenderEintrag {
	return obj as KalenderEintrag;
}
