import { JavaObject } from '../../../java/lang/JavaObject';

export class AbiturKursMarkierung extends JavaObject {

	/**
	 * Gibt an, ob der Kurs in die Berechnung eingeht
	 */
	public fuerBerechnung : boolean = false;

	/**
	 * Gibt an, ob der Kurs auf dem Zeugnis angegeben werden soll.
	 */
	public aufAbiturZeugnis : boolean = false;


	/**
	 * Erzeugt eine neue Markierung, die angibt, dass der Kurs nicht in die Berechnung eingeht,
	 * aber auf dem Abiturzeugnis erscheinen soll.
	 */
	public constructor();

	/**
	 * Erzeugt eine neue Markierung. Diese gibt an, dass der Kurs auf dem Abiturzeugnis erscheinen soll.
	 *
	 * @param fuerBerechnung   gibt an, on der Kurs in die Berechnung eingehen soll oder nicht
	 */
	public constructor(fuerBerechnung : boolean);

	/**
	 * Erzeugt eine neue Markierung.
	 *
	 * @param fuerBerechnung      gibt an, on der Kurs in die Berechnung eingehen soll oder nicht
	 * @param aufAbiturZeugnis    gibt an, ob der Kurs auf dem Abiturzeugnis erscheinen soll
	 */
	public constructor(fuerBerechnung : boolean, aufAbiturZeugnis : boolean);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : boolean, __param1? : boolean) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined)) {
			this.fuerBerechnung = false;
			this.aufAbiturZeugnis = true;
		} else if (((__param0 !== undefined) && typeof __param0 === "boolean") && (__param1 === undefined)) {
			const fuerBerechnung : boolean = __param0 as boolean;
			this.fuerBerechnung = fuerBerechnung;
			this.aufAbiturZeugnis = true;
		} else if (((__param0 !== undefined) && typeof __param0 === "boolean") && ((__param1 !== undefined) && typeof __param1 === "boolean")) {
			const fuerBerechnung : boolean = __param0 as boolean;
			const aufAbiturZeugnis : boolean = __param1 as boolean;
			this.fuerBerechnung = fuerBerechnung;
			this.aufAbiturZeugnis = aufAbiturZeugnis;
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.AbiturKursMarkierung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.AbiturKursMarkierung'].includes(name);
	}

	public static transpilerFromJSON(json : string): AbiturKursMarkierung {
		const obj = JSON.parse(json);
		const result = new AbiturKursMarkierung();
		if (obj.fuerBerechnung === undefined)
			 throw new Error('invalid json format, missing attribute fuerBerechnung');
		result.fuerBerechnung = obj.fuerBerechnung;
		if (obj.aufAbiturZeugnis === undefined)
			 throw new Error('invalid json format, missing attribute aufAbiturZeugnis');
		result.aufAbiturZeugnis = obj.aufAbiturZeugnis;
		return result;
	}

	public static transpilerToJSON(obj : AbiturKursMarkierung) : string {
		let result = '{';
		result += '"fuerBerechnung" : ' + obj.fuerBerechnung + ',';
		result += '"aufAbiturZeugnis" : ' + obj.aufAbiturZeugnis + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AbiturKursMarkierung>) : string {
		let result = '{';
		if (obj.fuerBerechnung !== undefined) {
			result += '"fuerBerechnung" : ' + obj.fuerBerechnung + ',';
		}
		if (obj.aufAbiturZeugnis !== undefined) {
			result += '"aufAbiturZeugnis" : ' + obj.aufAbiturZeugnis + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_AbiturKursMarkierung(obj : unknown) : AbiturKursMarkierung {
	return obj as AbiturKursMarkierung;
}
