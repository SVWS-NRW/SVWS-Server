import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostBelegungsfehlerArt extends JavaObject {

	public static readonly BELEGUNG : GostBelegungsfehlerArt = new GostBelegungsfehlerArt("BELEGUNG");

	public static readonly SCHRIFTLICHKEIT : GostBelegungsfehlerArt = new GostBelegungsfehlerArt("SCHRIFTLICHKEIT");

	public static readonly HINWEIS : GostBelegungsfehlerArt = new GostBelegungsfehlerArt("HINWEIS");

	public readonly kuerzel : String;


	/**
	 * Erzeugt ein neues Abitur-Belegungsfehler-Objekt
	 * 
	 * @param kuerzel        das Kürzel der Fehler-Art
	 */
	private constructor(kuerzel : String) {
		super();
		this.kuerzel = kuerzel;
	}

	/**
	 * Gibt die Belegungsfehler-Art anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Belegungsfehler-Art
	 *  
	 * @return die Belegungsfehler-Art
	 */
	public static fromKuerzel(kuerzel : String | null) : GostBelegungsfehlerArt | null {
		if (kuerzel === null) 
			return null;
		switch (kuerzel) {
			case "BELEGUNG": 
				return GostBelegungsfehlerArt.BELEGUNG;
			case "SCHRIFTLICHKEIT": 
				return GostBelegungsfehlerArt.SCHRIFTLICHKEIT;
			case "HINWEIS": 
				return GostBelegungsfehlerArt.HINWEIS;
		}
		return null;
	}

	public toString() : String {
		return this.kuerzel;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.gost.GostBelegungsfehlerArt'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegungsfehlerArt(obj : unknown) : GostBelegungsfehlerArt {
	return obj as GostBelegungsfehlerArt;
}
