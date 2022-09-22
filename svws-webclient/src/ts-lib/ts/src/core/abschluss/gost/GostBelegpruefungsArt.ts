import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostBelegpruefungsArt extends JavaObject {

	public static readonly EF1 : GostBelegpruefungsArt = new GostBelegpruefungsArt("EF.1", "nur EF.1");

	public static readonly GESAMT : GostBelegpruefungsArt = new GostBelegpruefungsArt("Gesamt", "die gesamte Oberstufe");

	public readonly kuerzel : String;

	public readonly beschreibung : String;


	/**
	 * Erzeugt ein neues Abitur-Belegungsart-Objekt
	 * 
	 * @param kuerzel        das der Kurs-Belegungsart
	 * @param beschreibung   die textuelle Beschreibung der Kurs-Belegungsart
	 */
	private constructor(kuerzel : String, beschreibung : String) {
		super();
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
	}

	/**
	 * Gibt die Art der Belegprüfung anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Art der Belegprüfung
	 *  
	 * @return die Art der Belegprüfung
	 */
	public static fromKuerzel(kuerzel : String | null) : GostBelegpruefungsArt | null {
		if (kuerzel === null) 
			return null;
		switch (kuerzel) {
			case "EF.1": 
				return GostBelegpruefungsArt.EF1;
			case "Gesamt": 
				return GostBelegpruefungsArt.GESAMT;
		}
		return null;
	}

	public toString() : String {
		return this.kuerzel;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefungsArt'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefungsArt(obj : unknown) : GostBelegpruefungsArt {
	return obj as GostBelegpruefungsArt;
}
