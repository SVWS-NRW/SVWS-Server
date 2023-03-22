import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { Comparable, cast_java_lang_Comparable } from '../../../java/lang/Comparable';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SprachBelegungSekI extends JavaObject implements Comparable<SprachBelegungSekI | null> {

	/**
	 * Gibt an, dass eine Sprache in der Sekundarstufe I nicht oder weniger als 2 Jahre belegt wurde
	 */
	public static readonly NICHT_BELEGT : SprachBelegungSekI = new SprachBelegungSekI(0);

	/**
	 * Gibt an, dass eine Sprache in der Sekundarstufe I mindestens 2 Jahre - aber nicht 4 oder mehr Jahre - belegt wurde
	 */
	public static readonly MIND_2_JAHRE : SprachBelegungSekI = new SprachBelegungSekI(2);

	/**
	 * Gibt an, dass eine Sprache in der Sekundarstufe I mindestens 4 Jahre - aber nicht ab Klasse 5 - belegt wurde
	 */
	public static readonly MIND_4_JAHRE : SprachBelegungSekI = new SprachBelegungSekI(4);

	/**
	 * Gibt an, dass eine Sprache in der Sekundarstufe I ab Klasse 5, d.h. 5 (in G8) oder 6 Jahre belegt wurde.
	 */
	public static readonly AB_JAHRGANG_5 : SprachBelegungSekI = new SprachBelegungSekI(6);

	/**
	 * Die Dauer der Sprachbelegung in der SekI - der Wert kann von der realen Belegung abweichen, da nur die relevante Dauer angeben ist und im Falle des Jahrgangs 5 abweichen kann, falls der G8-Bildungsgang vorliegt
	 */
	public readonly dauer : number;


	/**
	 * Erstellt einen neuen enum-Wert mit der angegebenen Dauer der Sprachbelegung.
	 * 
	 * @param dauer   die Dauer der Sprachbelegung in der Sek I
	 */
	private constructor(dauer : number) {
		super();
		this.dauer = dauer;
	}

	/**
	 * Ermittelt die Spachbelegung in der Sek I anhand des übergebenen Jahrgangs. 
	 * WICHTIG: Sollte ein Schüler sich im G8-Bildungsgang bewegen, so wird die Dauer 
	 * mit 6 Jahren hier nicht korrekt zugeordnet.  
	 * 
	 * @param ASDJahrgang der Jahrgang in welchem mit der Sprache begonnen wurde
	 * 
	 * @return die Sprachbelegung in der Sek I
	 */
	public static getByASDJahrgang(ASDJahrgang : string | null) : SprachBelegungSekI {
		if (ASDJahrgang === null) 
			return SprachBelegungSekI.NICHT_BELEGT;
		if (JavaString.compareTo(ASDJahrgang, "05") <= 0) 
			return SprachBelegungSekI.AB_JAHRGANG_5;
		if (JavaString.compareTo(ASDJahrgang, "07") <= 0) 
			return SprachBelegungSekI.MIND_4_JAHRE;
		if (JavaString.compareTo(ASDJahrgang, "09") <= 0) 
			return SprachBelegungSekI.MIND_2_JAHRE;
		return SprachBelegungSekI.NICHT_BELEGT;
	}

	/**
	 * Ermittelt die Spachbelegung in der Sek I anhand der übergebenen Dauer der Belegung in der Sek I.
	 * WICHTIG: Sollte ein Schüler sich im G8-Bildungsgang bewegen, so wird die Dauer 
	 * mit 6 Jahren hier nicht korrekt zugeordnet.  
	 *  
	 * @param dauer   die Dauer in vollen Jahren bei der Sprachbelegung in der Sek I
	 * 
	 * @return die Sprachbelegung in der Sek I
	 */
	public static getByDauer(dauer : number) : SprachBelegungSekI {
		if (dauer <= 0) 
			return SprachBelegungSekI.NICHT_BELEGT;
		if (dauer <= 3) 
			return SprachBelegungSekI.MIND_2_JAHRE;
		if (dauer <= 4) 
			return SprachBelegungSekI.MIND_4_JAHRE;
		return SprachBelegungSekI.AB_JAHRGANG_5;
	}

	public compareTo(other : SprachBelegungSekI | null) : number {
		if (other === null) 
			return 1;
		return JavaInteger.compare(this.dauer, other.dauer);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.fach.SprachBelegungSekI', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_fach_SprachBelegungSekI(obj : unknown) : SprachBelegungSekI {
	return obj as SprachBelegungSekI;
}
