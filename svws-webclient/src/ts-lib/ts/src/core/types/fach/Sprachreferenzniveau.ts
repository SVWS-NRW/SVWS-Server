import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { Comparable, cast_java_lang_Comparable } from '../../../java/lang/Comparable';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Sprachreferenzniveau extends JavaObject implements Comparable<Sprachreferenzniveau | null> {

	private static mapBezeichnung : HashMap<String, Sprachreferenzniveau> = new HashMap();

	public static readonly A1 : Sprachreferenzniveau = new Sprachreferenzniveau(1, "A1");

	public static readonly A1P : Sprachreferenzniveau = new Sprachreferenzniveau(2, "A1+");

	public static readonly A1A2 : Sprachreferenzniveau = new Sprachreferenzniveau(3, "A1/A2");

	public static readonly A2 : Sprachreferenzniveau = new Sprachreferenzniveau(4, "A2");

	public static readonly A2P : Sprachreferenzniveau = new Sprachreferenzniveau(5, "A2+");

	public static readonly A2B1 : Sprachreferenzniveau = new Sprachreferenzniveau(6, "A2/B1");

	public static readonly B1 : Sprachreferenzniveau = new Sprachreferenzniveau(7, "B1");

	public static readonly B1P : Sprachreferenzniveau = new Sprachreferenzniveau(8, "B1+");

	public static readonly B1B2 : Sprachreferenzniveau = new Sprachreferenzniveau(9, "B1/B2");

	public static readonly B2 : Sprachreferenzniveau = new Sprachreferenzniveau(10, "B2");

	public static readonly B2C1 : Sprachreferenzniveau = new Sprachreferenzniveau(11, "B2/C1");

	public static readonly C1 : Sprachreferenzniveau = new Sprachreferenzniveau(12, "C1");

	public static readonly C2 : Sprachreferenzniveau = new Sprachreferenzniveau(13, "C2");

	private readonly sortierung : number;

	public readonly bezeichnung : String;


	/**
	 *
	 * Erstellt ein neues Sprachreferenzniveau dieser Aufzählung.
	 *
	 * @param sortierung    die Sortierung, welche nur intern für den vergleich von 
	 *                      Referenzniveaus genutzt wird. 
	 * @param bezeichnung   die Bezeichnung des Sprachreferenzniveaus
	 */
	private constructor(sortierung : number, bezeichnung : String) {
		super();
		this.sortierung = sortierung;
		this.bezeichnung = bezeichnung;
		Sprachreferenzniveau.mapBezeichnung.put(bezeichnung, this);
	}

	/**
	 * Gibt das Sprachreferenzniveau für die übergebene Bezeichnung zurück.
	 * 
	 * @param bezeichnung   die Bezeichnung des Sprachreferenzniveaus
	 * 
	 * @return das Sprachreferenzniveau oder null im Fehlerfall
	 */
	public static getByBezeichnung(bezeichnung : String | null) : Sprachreferenzniveau | null {
		return Sprachreferenzniveau.mapBezeichnung.get(bezeichnung);
	}

	public compareTo(other : Sprachreferenzniveau | null) : number;

	/**
	 * Vergleicht dieses Sprachreferenzniveau mit dem Niveau der übergebenen Bezeichnung.
	 * 
	 * @param bezeichnung   die Bezeichnung des anderen Sprachreferenzniveaus
	 * 
	 * @return siehe {@link Sprachreferenzniveau#compareTo(Sprachreferenzniveau)}
	 */
	public compareTo(bezeichnung : String | null) : number;

	/**
	 * Implementation for method overloads of 'compareTo'
	 */
	public compareTo(__param0 : Sprachreferenzniveau | String | null) : number {
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.fach.Sprachreferenzniveau'))) || (__param0 === null))) {
			let other : Sprachreferenzniveau | null = cast_de_nrw_schule_svws_core_types_fach_Sprachreferenzniveau(__param0);
			if (other === null) 
				return 1;
			return JavaInteger.compare(this.sortierung, other.sortierung);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof String) || (typeof __param0 === "string")) || (__param0 === null))) {
			let bezeichnung : String | null = __param0;
			return this.compareTo(Sprachreferenzniveau.getByBezeichnung(bezeichnung));
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.fach.Sprachreferenzniveau', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_fach_Sprachreferenzniveau(obj : unknown) : Sprachreferenzniveau {
	return obj as Sprachreferenzniveau;
}
