import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GEAbschlussFach, cast_de_nrw_schule_svws_core_data_abschluss_GEAbschlussFach } from '../../../core/data/abschluss/GEAbschlussFach';
import { StringBuilder, cast_java_lang_StringBuilder } from '../../../java/lang/StringBuilder';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { AbschlussFaecherGruppe, cast_de_nrw_schule_svws_core_abschluss_ge_AbschlussFaecherGruppe } from '../../../core/abschluss/ge/AbschlussFaecherGruppe';
import { Predicate, cast_java_util_function_Predicate } from '../../../java/util/function/Predicate';

export class AbschlussFaecherGruppen extends JavaObject {

	/**
	 * Die Fächergruppe 1
	 */
	public readonly fg1 : AbschlussFaecherGruppe;

	/**
	 * Die Fächergruppe 1
	 */
	public readonly fg2 : AbschlussFaecherGruppe;


	/**
	 * Erzeugt eine neues Objekt AbschlussFaecherGruppen
	 *
	 * @param fg1   die Fächergruppe 1
	 * @param fg2   die Fächergruppe 2
	 */
	public constructor(fg1 : AbschlussFaecherGruppe, fg2 : AbschlussFaecherGruppe) {
		super();
		this.fg1 = fg1;
		this.fg2 = fg2;
	}

	/**
	 * Prüft, ob eine der beiden Fächergruppen leer ist.
	 *
	 * @return true, falls eine der beiden Fächergruppen leer ist.
	 */
	public isEmpty() : boolean {
		return this.fg1 === null || this.fg2 === null || this.fg1.isEmpty() || this.fg2.isEmpty();
	}

	/**
	 * Prüft, ob das Fach mit dem angegebenen Fachkürzel in einer der beiden
	 * Fächergruppen enthalten ist oder nicht.
	 *
	 * @param kuerzel   das Kürzel des Faches, welches geprüft werden soll.
	 *
	 * @return true, falls das Fach vorhanden ist, und ansonsten false
	 */
	public contains(kuerzel : string | null) : boolean {
		return this.fg1.contains(kuerzel) || this.fg2.contains(kuerzel);
	}

	/**
	 * Bestimmt alle Fächer beider Fächergruppen, welche dem übergebenen
	 * Filterkriterium entsprechen.
	 *
	 * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
	 *
	 * @return eine Liste der Fächer, die dem Filterkriterium entsprechen
	 */
	public getFaecher(filter : Predicate<GEAbschlussFach>) : List<GEAbschlussFach> {
		let faecher : List<GEAbschlussFach> = this.fg1.getFaecher(filter);
		faecher.addAll(this.fg2.getFaecher(filter));
		return faecher;
	}

	/**
	 * Gibt die Anzahl der Fächer beider Fächergruppen zurück, welche dem übergebenen
	 * Filterkriterium entsprechen.
	 *
	 * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
	 *
	 * @return die Anzahl der Fächer
	 */
	public getFaecherAnzahl(filter : Predicate<GEAbschlussFach>) : number {
		return this.fg1.getFaecherAnzahl(filter) + this.fg2.getFaecherAnzahl(filter);
	}

	/**
	 * Bestimmt die Kürzel aller Fächer beider Fächergruppen, welche dem übergebenen
	 * Filterkriterium entsprechen.
	 *
	 * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
	 *
	 * @return eine Liste der Kürzel der Fächer, die dem Filterkriterium entsprechen
	 */
	public getKuerzel(filter : Predicate<GEAbschlussFach>) : List<string> {
		let faecher : List<string> = this.fg1.getKuerzel(filter);
		faecher.addAll(this.fg2.getKuerzel(filter));
		return faecher;
	}

	/**
	 * Erstellt eine Zeichenkette mit einer Komma-separierten Liste der Kürzel aller Fächer
	 * beider Fächergruppen, welche dem übergebenen Filterkriterium entsprechen.
	 *
	 * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
	 *
	 * @return die Zeichenkette mit einer Komma-separierten Liste der Fächerkürzel
	 */
	public getKuerzelListe(filter : Predicate<GEAbschlussFach>) : string;

	/**
	 * Erstellt eine Zeichenkette mit einer Komma-separierten Liste der Kürzel aller Fächer
	 * beider Fächergruppen, welche dem übergebenen Filterkriterium entsprechen. Dabei
	 * werden für die Fächergruppen jedoch unterschiedliche Filterkriterien angewendet.
	 *
	 * @param filterFG1   die Funktion, die das Kriterium für die gesuchten Fächer der Fächergruppe 1 angibt.
	 * @param filterFG2   die Funktion, die das Kriterium für die gesuchten Fächer der Fächergruppe 2 angibt.
	 *
	 * @return die Zeichenkette mit einer Komma-separierten Liste der Fächerkürzel
	 */
	public getKuerzelListe(filterFG1 : Predicate<GEAbschlussFach>, filterFG2 : Predicate<GEAbschlussFach>) : string;

	/**
	 * Implementation for method overloads of 'getKuerzelListe'
	 */
	public getKuerzelListe(__param0 : Predicate<GEAbschlussFach>, __param1? : Predicate<GEAbschlussFach>) : string {
		if (((typeof __param0 !== "undefined") && ((typeof __param0 !== 'undefined') && (__param0 instanceof Object) && (__param0 !== null) && ('test' in __param0) && (typeof __param0.test === 'function')) || (__param0 === null)) && (typeof __param1 === "undefined")) {
			let filter : Predicate<GEAbschlussFach> = cast_java_util_function_Predicate(__param0);
			let sb : StringBuilder = new StringBuilder();
			let faecher : List<string> = this.getKuerzel(filter);
			for (let fach of faecher) {
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(fach);
			}
			return sb.toString();
		} else if (((typeof __param0 !== "undefined") && ((typeof __param0 !== 'undefined') && (__param0 instanceof Object) && (__param0 !== null) && ('test' in __param0) && (typeof __param0.test === 'function')) || (__param0 === null)) && ((typeof __param1 !== "undefined") && ((typeof __param1 !== 'undefined') && (__param1 instanceof Object) && (__param1 !== null) && ('test' in __param1) && (typeof __param1.test === 'function')) || (__param1 === null))) {
			let filterFG1 : Predicate<GEAbschlussFach> = cast_java_util_function_Predicate(__param0);
			let filterFG2 : Predicate<GEAbschlussFach> = cast_java_util_function_Predicate(__param1);
			let sb : StringBuilder = new StringBuilder();
			let faecherFG1 : List<string> = this.fg1.getKuerzel(filterFG1);
			let faecherFG2 : List<string> = this.fg2.getKuerzel(filterFG2);
			for (let fach of faecherFG1) {
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(fach);
			}
			for (let fach of faecherFG2) {
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(fach);
			}
			return sb.toString();
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.ge.AbschlussFaecherGruppen'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_ge_AbschlussFaecherGruppen(obj : unknown) : AbschlussFaecherGruppen {
	return obj as AbschlussFaecherGruppen;
}
