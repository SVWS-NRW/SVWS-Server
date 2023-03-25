import { JavaObject } from '../../java/lang/JavaObject';

export class RGBFarbe extends JavaObject {

	/**
	 * Der Rot-Anteil der Farbe (0-255)
	 */
	public red : number = 220;

	/**
	 * Der Grün-Anteil der Farbe (0-255)
	 */
	public green : number = 220;

	/**
	 * Der Blau-Anteil der Farbe (0-255)
	 */
	public blue : number = 220;


	/**
	 * Erstellt einen RGB-Farbwert mit dem Standardwerten (220,220,220)
	 */
	public constructor();

	/**
	 *
	 * Erstellt einen RGB-Farbwert aus dem übergebenen 24-Bit-Farbwert.
	 *
	 * @param color24Bit   RGB-Farbwerte
	 */
	public constructor(color24Bit : number);

	/**
	 * Erstellt einen RGB-Farbwert mit den angegebenen RGB-Werten.
	 *
	 * @param red     der Rot-Anteil (0-255)
	 * @param green   der Grün-Anteil (0-255)
	 * @param blue    der Blau-Anteil (0-255)
	 */
	public constructor(red : number, green : number, blue : number);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : number, __param2? : number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined")) {
			// empty block
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined")) {
			const color24Bit : number = __param0 as number;
			this.red = color24Bit & 255;
			this.green = (color24Bit >> 8) & 255;
			this.blue = color24Bit >> 16;
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number") && ((typeof __param2 !== "undefined") && typeof __param2 === "number")) {
			const red : number = __param0 as number;
			const green : number = __param1 as number;
			const blue : number = __param2 as number;
			this.red = red;
			this.green = green;
			this.blue = blue;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.RGBFarbe'].includes(name);
	}

	public static transpilerFromJSON(json : string): RGBFarbe {
		const obj = JSON.parse(json);
		const result = new RGBFarbe();
		if (typeof obj.red === "undefined")
			 throw new Error('invalid json format, missing attribute red');
		result.red = obj.red;
		if (typeof obj.green === "undefined")
			 throw new Error('invalid json format, missing attribute green');
		result.green = obj.green;
		if (typeof obj.blue === "undefined")
			 throw new Error('invalid json format, missing attribute blue');
		result.blue = obj.blue;
		return result;
	}

	public static transpilerToJSON(obj : RGBFarbe) : string {
		let result = '{';
		result += '"red" : ' + obj.red + ',';
		result += '"green" : ' + obj.green + ',';
		result += '"blue" : ' + obj.blue + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<RGBFarbe>) : string {
		let result = '{';
		if (typeof obj.red !== "undefined") {
			result += '"red" : ' + obj.red + ',';
		}
		if (typeof obj.green !== "undefined") {
			result += '"green" : ' + obj.green + ',';
		}
		if (typeof obj.blue !== "undefined") {
			result += '"blue" : ' + obj.blue + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_RGBFarbe(obj : unknown) : RGBFarbe {
	return obj as RGBFarbe;
}
