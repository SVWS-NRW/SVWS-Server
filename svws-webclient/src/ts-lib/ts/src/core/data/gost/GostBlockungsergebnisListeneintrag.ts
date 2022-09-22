import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostBlockungsergebnisListeneintrag extends JavaObject {

	public id : number = -1;

	public blockungID : number = -1;

	public name : String = "Blockung";

	public gostHalbjahr : number = 0;

	public anzahlUmwaehler : number = 0;

	public bewertung : number = -1;

	public istMarkiert : boolean = false;

	public bewertungNichtErfuellteRegeln : Array<Number> = Array(0).fill(null);

	public bewertungNichtZugeordneteFachwahlen : number = 0;

	public bewertungHistogrammDerKursdifferenzen : Array<Number> = Array(0).fill(null);

	public bewertungGleicheFachartProSchiene : number = 0;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisListeneintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsergebnisListeneintrag {
		const obj = JSON.parse(json);
		const result = new GostBlockungsergebnisListeneintrag();
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
		if (typeof obj.anzahlUmwaehler === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlUmwaehler');
		result.anzahlUmwaehler = obj.anzahlUmwaehler;
		if (typeof obj.bewertung === "undefined")
			 throw new Error('invalid json format, missing attribute bewertung');
		result.bewertung = obj.bewertung;
		if (typeof obj.istMarkiert === "undefined")
			 throw new Error('invalid json format, missing attribute istMarkiert');
		result.istMarkiert = obj.istMarkiert;
		for (let i : number = 0; i < obj.bewertungNichtErfuellteRegeln.length; i++) {
			result.bewertungNichtErfuellteRegeln[i] = obj.bewertungNichtErfuellteRegeln[i];
		}
		if (typeof obj.bewertungNichtZugeordneteFachwahlen === "undefined")
			 throw new Error('invalid json format, missing attribute bewertungNichtZugeordneteFachwahlen');
		result.bewertungNichtZugeordneteFachwahlen = obj.bewertungNichtZugeordneteFachwahlen;
		for (let i : number = 0; i < obj.bewertungHistogrammDerKursdifferenzen.length; i++) {
			result.bewertungHistogrammDerKursdifferenzen[i] = obj.bewertungHistogrammDerKursdifferenzen[i];
		}
		if (typeof obj.bewertungGleicheFachartProSchiene === "undefined")
			 throw new Error('invalid json format, missing attribute bewertungGleicheFachartProSchiene');
		result.bewertungGleicheFachartProSchiene = obj.bewertungGleicheFachartProSchiene;
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnisListeneintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"blockungID" : ' + obj.blockungID + ',';
		result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		result += '"anzahlUmwaehler" : ' + obj.anzahlUmwaehler + ',';
		result += '"bewertung" : ' + obj.bewertung + ',';
		result += '"istMarkiert" : ' + obj.istMarkiert + ',';
		if (!obj.bewertungNichtErfuellteRegeln) {
			result += '"bewertungNichtErfuellteRegeln" : []';
		} else {
			result += '"bewertungNichtErfuellteRegeln" : [ ';
			for (let i : number = 0; i < obj.bewertungNichtErfuellteRegeln.length; i++) {
				let elem = obj.bewertungNichtErfuellteRegeln[i];
				result += elem;
				if (i < obj.bewertungNichtErfuellteRegeln.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"bewertungNichtZugeordneteFachwahlen" : ' + obj.bewertungNichtZugeordneteFachwahlen + ',';
		if (!obj.bewertungHistogrammDerKursdifferenzen) {
			result += '"bewertungHistogrammDerKursdifferenzen" : []';
		} else {
			result += '"bewertungHistogrammDerKursdifferenzen" : [ ';
			for (let i : number = 0; i < obj.bewertungHistogrammDerKursdifferenzen.length; i++) {
				let elem = obj.bewertungHistogrammDerKursdifferenzen[i];
				result += elem;
				if (i < obj.bewertungHistogrammDerKursdifferenzen.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"bewertungGleicheFachartProSchiene" : ' + obj.bewertungGleicheFachartProSchiene + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnisListeneintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.blockungID !== "undefined") {
			result += '"blockungID" : ' + obj.blockungID + ',';
		}
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		}
		if (typeof obj.gostHalbjahr !== "undefined") {
			result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		}
		if (typeof obj.anzahlUmwaehler !== "undefined") {
			result += '"anzahlUmwaehler" : ' + obj.anzahlUmwaehler + ',';
		}
		if (typeof obj.bewertung !== "undefined") {
			result += '"bewertung" : ' + obj.bewertung + ',';
		}
		if (typeof obj.istMarkiert !== "undefined") {
			result += '"istMarkiert" : ' + obj.istMarkiert + ',';
		}
		if (typeof obj.bewertungNichtErfuellteRegeln !== "undefined") {
			let a = obj.bewertungNichtErfuellteRegeln;
			if (!a) {
				result += '"bewertungNichtErfuellteRegeln" : []';
			} else {
				result += '"bewertungNichtErfuellteRegeln" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += elem;
					if (i < a.length - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.bewertungNichtZugeordneteFachwahlen !== "undefined") {
			result += '"bewertungNichtZugeordneteFachwahlen" : ' + obj.bewertungNichtZugeordneteFachwahlen + ',';
		}
		if (typeof obj.bewertungHistogrammDerKursdifferenzen !== "undefined") {
			let a = obj.bewertungHistogrammDerKursdifferenzen;
			if (!a) {
				result += '"bewertungHistogrammDerKursdifferenzen" : []';
			} else {
				result += '"bewertungHistogrammDerKursdifferenzen" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += elem;
					if (i < a.length - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.bewertungGleicheFachartProSchiene !== "undefined") {
			result += '"bewertungGleicheFachartProSchiene" : ' + obj.bewertungGleicheFachartProSchiene + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisListeneintrag(obj : unknown) : GostBlockungsergebnisListeneintrag {
	return obj as GostBlockungsergebnisListeneintrag;
}
