import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisSchiene } from '../../../core/data/gost/GostBlockungsergebnisSchiene';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { GostBlockungsergebnisBewertung } from '../../../core/data/gost/GostBlockungsergebnisBewertung';

export class GostBlockungsergebnis extends JavaObject {

	/**
	 * Die ID des Zwischenergebnisses der Blockung
	 */
	public id : number = -1;

	/**
	 * Die ID der Blockung
	 */
	public blockungID : number = -1;

	/**
	 * Der Name der Blockung
	 */
	public name : string = "Blockung";

	/**
	 * Das Halbjahr, welchem die Kursblockung zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2)
	 */
	public gostHalbjahr : number = 0;

	/**
	 * Gibt an, ob dieses Ergebnis markiert wurde. Dies kann verwendet werden, um besonders geeignete Blockungsergebnisse hervorzuheben.
	 */
	public istMarkiert : boolean = false;

	/**
	 * Gibt an, ob dieses Ergebnis bei einer aktivieren einen Blockung verwendet wurde.
	 */
	public istVorlage : boolean = false;

	/**
	 * Eine Liste der Schienen, welche zugeordnete Kurse beinhalten.
	 */
	public readonly schienen : List<GostBlockungsergebnisSchiene> = new ArrayList();

	/**
	 * Die Informationen zur aktuellen Bewertung des Blockungsergebnisses
	 */
	public bewertung : GostBlockungsergebnisBewertung = new GostBlockungsergebnisBewertung();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungsergebnis';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungsergebnis'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsergebnis {
		const obj = JSON.parse(json);
		const result = new GostBlockungsergebnis();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.blockungID === "undefined")
			 throw new Error('invalid json format, missing attribute blockungID');
		result.blockungID = obj.blockungID;
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (typeof obj.gostHalbjahr === "undefined")
			 throw new Error('invalid json format, missing attribute gostHalbjahr');
		result.gostHalbjahr = obj.gostHalbjahr;
		if (typeof obj.istMarkiert === "undefined")
			 throw new Error('invalid json format, missing attribute istMarkiert');
		result.istMarkiert = obj.istMarkiert;
		if (typeof obj.istVorlage === "undefined")
			 throw new Error('invalid json format, missing attribute istVorlage');
		result.istVorlage = obj.istVorlage;
		if ((obj.schienen !== undefined) && (obj.schienen !== null)) {
			for (const elem of obj.schienen) {
				result.schienen?.add(GostBlockungsergebnisSchiene.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (typeof obj.bewertung === "undefined")
			 throw new Error('invalid json format, missing attribute bewertung');
		result.bewertung = GostBlockungsergebnisBewertung.transpilerFromJSON(JSON.stringify(obj.bewertung));
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnis) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"blockungID" : ' + obj.blockungID + ',';
		result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		result += '"istMarkiert" : ' + obj.istMarkiert + ',';
		result += '"istVorlage" : ' + obj.istVorlage + ',';
		if (!obj.schienen) {
			result += '"schienen" : []';
		} else {
			result += '"schienen" : [ ';
			for (let i = 0; i < obj.schienen.size(); i++) {
				const elem = obj.schienen.get(i);
				result += GostBlockungsergebnisSchiene.transpilerToJSON(elem);
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"bewertung" : ' + GostBlockungsergebnisBewertung.transpilerToJSON(obj.bewertung) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnis>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.blockungID !== "undefined") {
			result += '"blockungID" : ' + obj.blockungID + ',';
		}
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		}
		if (typeof obj.gostHalbjahr !== "undefined") {
			result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		}
		if (typeof obj.istMarkiert !== "undefined") {
			result += '"istMarkiert" : ' + obj.istMarkiert + ',';
		}
		if (typeof obj.istVorlage !== "undefined") {
			result += '"istVorlage" : ' + obj.istVorlage + ',';
		}
		if (typeof obj.schienen !== "undefined") {
			if (!obj.schienen) {
				result += '"schienen" : []';
			} else {
				result += '"schienen" : [ ';
				for (let i = 0; i < obj.schienen.size(); i++) {
					const elem = obj.schienen.get(i);
					result += GostBlockungsergebnisSchiene.transpilerToJSON(elem);
					if (i < obj.schienen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.bewertung !== "undefined") {
			result += '"bewertung" : ' + GostBlockungsergebnisBewertung.transpilerToJSON(obj.bewertung) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBlockungsergebnis(obj : unknown) : GostBlockungsergebnis {
	return obj as GostBlockungsergebnis;
}
