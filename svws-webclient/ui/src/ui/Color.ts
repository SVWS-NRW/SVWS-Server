import { RGBFarbe } from "../../../core/src";

/**
 * Diese Klasse stellt Hilfsmethoden für den Umgang mit RGB-Farben zur Verfügung.
 */
export class Color {

	/** Die Rot-Komponente mit einem Wert im Bereich von 0 bis 255 */
	private red: number;

	/** Die Grün-Komponente mit einem Wert im Bereich von 0 bis 255 */
	private green: number;

	/** Die Blau-Komponente mit einem Wert im Bereich von 0 bis 255 */
	private blue: number;

	/** Die Luminant, sofern sie schon berechnet wurde */
	private _luminance: number | null = null;


	/**
	 * Erstellt eine Farb-Klasse für die übergebene RGB-Farbe.
	 *
	 * @param color   ein RGB-Farbobjekt
	 */
	public constructor(color: RGBFarbe);

	/**
	 * Erstellt eine Farb-Klasse für die übergebene Farbe.
	 *
	 * @param red     die Rot-Komponente
	 * @param green   die Grün-Komponente
	 * @param blue    die Blau-Komponente
	 */
	public constructor(red: number, green: number, blue: number);

	public constructor(color: RGBFarbe | number, green?: number, blue?: number) {
		if ((color instanceof RGBFarbe) && (green === undefined) && (blue === undefined)) {
			this.red = color.red;
			this.green = color.green;
			this.blue = color.blue;
		} else if ((!(color instanceof RGBFarbe)) && (green !== undefined) && (blue !== undefined)) {
			this.red = color;
			this.green = green;
			this.blue = blue;
		} else
			throw new Error("Fehlerhafter Konstruktoraufruf!");
	}


	/**
	 * Hilfsmethode zum Berechnen der relativen Luminanz (Formel für die einzelnen RGB-Farb-Komponenten)
	 *
	 * @param value   der Wert der RGB-Komponente in einem Bereich von 0 bis 255
	 *
	 * @returns der Wert für die RGB-Komponente zur weiteren Berechnung der Luminanz
	 */
	private calcComponent(value: number): number {
		return (value <= 10.31475) ? (value / 3294.6) : Math.pow((((value / 255.0) + 0.055) / 1.055), 2.4);
	}


	/**
	 * Gibt die relative Luminanz der Farbe zurück.
	 */
	public get luminance(): number {
		if (this._luminance === null)
			this._luminance = (0.2126 * this.calcComponent(this.red)) + (0.7152 * this.calcComponent(this.green)) + (0.0722 * this.calcComponent(this.blue));
		return this._luminance;
	}

	/**
	 * Gibt den Kontrast dieser Farbe zu Weiß zurück.
	 */
	public get contrastWhite() : number {
		return 1.05 / (this.luminance + 0.05);
	}

	/**
	 * Gibt den Kontrast dieser Farbe zu Schwarz zurück.
	 */
	public get contrastBlack() : number {
		return (this.luminance + 0.05) / 0.05;
	}

	/**
	 * Ermittelt den Kontrastwert zwischen den beiden Farben.
	 *
	 * @param other   die andere Farbe
	 *
	 * @returns der Kontrastwert
	 */
	public getContrast(other: Color) {
		const l1 = this.luminance + 0.05;
		const l2 = other.luminance + 0.05;
		return (l1 > l2) ? l1 / l2 : l2 / l1;
	}

	/**
	 * Prüft, ob die Farbe einen höheren Kontrast zu Weiß hat oder nicht.
	 *
	 * @param farbe   die zu prüfende Farbe
	 *
	 * @returns true, falls der Kontrast zu Weiß höher ist als zu Schwarz.
	 */
	public static preferWhiteAsContrastColor(farbe: RGBFarbe): boolean {
		const color = new Color(farbe);
		return (color.contrastWhite > color.contrastBlack);
	}

}
