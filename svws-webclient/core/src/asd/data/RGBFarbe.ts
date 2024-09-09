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


	public constructor() {
		super();
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
