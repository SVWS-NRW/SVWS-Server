import { JavaObject } from '../../java/lang/JavaObject';
import { Class } from '../../java/lang/Class';

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
	 * Leerer Standardkonstruktor.
	 */
	public constructor();

	/**
	 * Erstellt eine neue Farbe mit den übergebenenen Komponenten
	 *
	 * @param red     die Rot-Komponente (0-225)
	 * @param green   die Grün-Komponente (0-225)
	 * @param blue    die Blau-Komponente (0-225)
	 */
	public constructor(red : number, green : number, blue : number);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : number, __param2? : number) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined) && (__param2 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && typeof __param1 === "number") && ((__param2 !== undefined) && typeof __param2 === "number")) {
			const red : number = __param0 as number;
			const green : number = __param1 as number;
			const blue : number = __param2 as number;
			this.red = red;
			this.green = green;
			this.blue = blue;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt die Farbe als komma-separierten String zurück.
	 *
	 * @return der String
	 */
	public toString() : string | null {
		return this.red + "," + this.green + "," + this.blue;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.RGBFarbe';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.RGBFarbe'].includes(name);
	}

	public static class = new Class<RGBFarbe>('de.svws_nrw.asd.data.RGBFarbe');

	public static transpilerFromJSON(json : string): RGBFarbe {
		const obj = JSON.parse(json) as Partial<RGBFarbe>;
		const result = new RGBFarbe();
		if (obj.red === undefined)
			throw new Error('invalid json format, missing attribute red');
		result.red = obj.red;
		if (obj.green === undefined)
			throw new Error('invalid json format, missing attribute green');
		result.green = obj.green;
		if (obj.blue === undefined)
			throw new Error('invalid json format, missing attribute blue');
		result.blue = obj.blue;
		return result;
	}

	public static transpilerToJSON(obj : RGBFarbe) : string {
		let result = '{';
		result += '"red" : ' + obj.red.toString() + ',';
		result += '"green" : ' + obj.green.toString() + ',';
		result += '"blue" : ' + obj.blue.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<RGBFarbe>) : string {
		let result = '{';
		if (obj.red !== undefined) {
			result += '"red" : ' + obj.red.toString() + ',';
		}
		if (obj.green !== undefined) {
			result += '"green" : ' + obj.green.toString() + ',';
		}
		if (obj.blue !== undefined) {
			result += '"blue" : ' + obj.blue.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_RGBFarbe(obj : unknown) : RGBFarbe {
	return obj as RGBFarbe;
}
