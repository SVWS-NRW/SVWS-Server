import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';

export class RGBFarbe extends JavaObject {

	private red : number = 0;

	private green : number = 0;

	private blue : number = 0;


	/**
	 *
	 * Setzt die Farbwerte aus den Werten red, blue, green zusammen. 
	 * 
	 * @param color24Bit        RGB-Farbwerte
	 * 
	 * 
	 */
	public constructor(color24Bit : number);

	/**
	 * Erstellt eine neue Farbe mit den angegebenen RGB-Werten.
	 * 
	 * @param red     der Rot-Anteil (0-255)
	 * @param green   der Grün-Anteil (0-255)
	 * @param blue    der Blau-Anteil (0-255)
	 */
	public constructor(red : number, green : number, blue : number);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : number, __param1? : number, __param2? : number) {
		super();
		if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined")) {
			let color24Bit : number = __param0 as number;
			if (color24Bit === 0) {
				this.red = this.green = this.blue = 255;
			} else {
				this.red = color24Bit & 255;
				this.green = (color24Bit >> 8) & 255;
				this.blue = color24Bit >> 16;
			}
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number") && ((typeof __param2 !== "undefined") && typeof __param2 === "number")) {
			let red : number = __param0 as number;
			let green : number = __param1 as number;
			let blue : number = __param2 as number;
			this.red = red;
			this.green = green;
			this.blue = blue;
		} else throw new Error('invalid method overload');
	}

	public toString() : String {
		return "rgb(" + this.red + ", " + this.green + ", " + this.blue + ")";
	}

	/**
	 *
	 * Holt den Wert des Farbwerts rot
	 * 
	 * @return Farbwert rot
	 */
	public getRed() : number {
		return this.red;
	}

	/**
	 *
	 * Bestimmt den Farbwert für rot
	 * 
	 * @param red          Farbwert rot
	 *    
	 */
	public setRed(red : number) : void {
		this.red = red;
	}

	/**
	 *
	 * Holt den Wert des Farbwerts grün
	 * 
	 * @return Farbwert grün
	 */
	public getGreen() : number {
		return this.green;
	}

	/**
	 *
	 * Bestimmt den Farbwert für grün
	 * 
	 * @param green          Farbwert grün
	 *    
	 */
	public setGreen(green : number) : void {
		this.green = green;
	}

	/**
	 *
	 * Holt den Wert des Farbwerts blau
	 * 
	 * @return Farbwert blau
	 */
	public getBlue() : number {
		return this.blue;
	}

	/**
	 *
	 * Bestimmt den Farbwert für blau
	 * 
	 * @param blue          Farbwert blau
	 *    
	 */
	public setBlue(blue : number) : void {
		this.blue = blue;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.RGBFarbe'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_RGBFarbe(obj : unknown) : RGBFarbe {
	return obj as RGBFarbe;
}
