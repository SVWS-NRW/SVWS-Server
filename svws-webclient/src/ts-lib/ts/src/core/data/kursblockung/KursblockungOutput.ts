import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { KursblockungOutputKursZuSchiene, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungOutputKursZuSchiene } from '../../../core/data/kursblockung/KursblockungOutputKursZuSchiene';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { KursblockungOutputFachwahlZuKurs, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungOutputFachwahlZuKurs } from '../../../core/data/kursblockung/KursblockungOutputFachwahlZuKurs';

export class KursblockungOutput extends JavaObject {

	public blockungsRevision : number = -1;

	public seed : number = 0;

	public input : number = 0;

	public kursZuSchiene : Vector<KursblockungOutputKursZuSchiene> = new Vector();

	public fachwahlenZuKurs : Vector<KursblockungOutputFachwahlZuKurs> = new Vector();

	public bewertungNichtErfuellteRegeln : Array<Number> = Array(0).fill(null);

	public bewertungNichtZugeordneteFachwahlen : number = 0;

	public bewertungHistogrammDerKursdifferenzen : Array<Number> = Array(0).fill(null);

	public bewertungGleicheFachartProSchiene : number = 0;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kursblockung.KursblockungOutput'].includes(name);
	}

	public static transpilerFromJSON(json : string): KursblockungOutput {
		const obj = JSON.parse(json);
		const result = new KursblockungOutput();
		if (typeof obj.blockungsRevision === "undefined")
			 throw new Error('invalid json format, missing attribute blockungsRevision');
		result.blockungsRevision = obj.blockungsRevision;
		if (typeof obj.seed === "undefined")
			 throw new Error('invalid json format, missing attribute seed');
		result.seed = obj.seed;
		if (typeof obj.input === "undefined")
			 throw new Error('invalid json format, missing attribute input');
		result.input = obj.input;
		if (!!obj.kursZuSchiene) {
			for (let elem of obj.kursZuSchiene) {
				result.kursZuSchiene?.add(KursblockungOutputKursZuSchiene.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.fachwahlenZuKurs) {
			for (let elem of obj.fachwahlenZuKurs) {
				result.fachwahlenZuKurs?.add(KursblockungOutputFachwahlZuKurs.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
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

	public static transpilerToJSON(obj : KursblockungOutput) : string {
		let result = '{';
		result += '"blockungsRevision" : ' + obj.blockungsRevision + ',';
		result += '"seed" : ' + obj.seed + ',';
		result += '"input" : ' + obj.input + ',';
		if (!obj.kursZuSchiene) {
			result += '"kursZuSchiene" : []';
		} else {
			result += '"kursZuSchiene" : [ ';
			for (let i : number = 0; i < obj.kursZuSchiene.size(); i++) {
				let elem = obj.kursZuSchiene.get(i);
				result += KursblockungOutputKursZuSchiene.transpilerToJSON(elem);
				if (i < obj.kursZuSchiene.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.fachwahlenZuKurs) {
			result += '"fachwahlenZuKurs" : []';
		} else {
			result += '"fachwahlenZuKurs" : [ ';
			for (let i : number = 0; i < obj.fachwahlenZuKurs.size(); i++) {
				let elem = obj.fachwahlenZuKurs.get(i);
				result += KursblockungOutputFachwahlZuKurs.transpilerToJSON(elem);
				if (i < obj.fachwahlenZuKurs.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
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

	public static transpilerToJSONPatch(obj : Partial<KursblockungOutput>) : string {
		let result = '{';
		if (typeof obj.blockungsRevision !== "undefined") {
			result += '"blockungsRevision" : ' + obj.blockungsRevision + ',';
		}
		if (typeof obj.seed !== "undefined") {
			result += '"seed" : ' + obj.seed + ',';
		}
		if (typeof obj.input !== "undefined") {
			result += '"input" : ' + obj.input + ',';
		}
		if (typeof obj.kursZuSchiene !== "undefined") {
			if (!obj.kursZuSchiene) {
				result += '"kursZuSchiene" : []';
			} else {
				result += '"kursZuSchiene" : [ ';
				for (let i : number = 0; i < obj.kursZuSchiene.size(); i++) {
					let elem = obj.kursZuSchiene.get(i);
					result += KursblockungOutputKursZuSchiene.transpilerToJSON(elem);
					if (i < obj.kursZuSchiene.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.fachwahlenZuKurs !== "undefined") {
			if (!obj.fachwahlenZuKurs) {
				result += '"fachwahlenZuKurs" : []';
			} else {
				result += '"fachwahlenZuKurs" : [ ';
				for (let i : number = 0; i < obj.fachwahlenZuKurs.size(); i++) {
					let elem = obj.fachwahlenZuKurs.get(i);
					result += KursblockungOutputFachwahlZuKurs.transpilerToJSON(elem);
					if (i < obj.fachwahlenZuKurs.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
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

export function cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungOutput(obj : unknown) : KursblockungOutput {
	return obj as KursblockungOutput;
}
