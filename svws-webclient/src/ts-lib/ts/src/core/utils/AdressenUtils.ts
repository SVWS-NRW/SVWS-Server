import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';

export class AdressenUtils extends JavaObject {


	public constructor() {
		super();
	}

	/**
	 * Teilt eine Strassenangabe bestehend aus dem 
	 * Strassennamen, der Hausnummer und dem Hausnummerzusatz
	 * in die Bestandteile auf.
	 * 
	 * @param strasse   die Strassenangabe
	 * 
	 * @return ein Array mit den 3 Elementen (0 - Strassennamen, 1 - Hausnummer und 2 - Hausnummerzusatz)
	 */
	public static splitStrasse(strasse : String | null) : Array<String> {
		let result : Array<String> = Array(3).fill(null);
		if (strasse === null) {
			result[0] = "";
			result[1] = "";
			result[2] = "";
			return result;
		}
		let tmp : String = strasse.trim().replace("  ", " ").replace("  ", " ").replace(" -", "-").replace("- ", "-");
		result[0] = JavaString.replaceFirst(tmp, " *([0-9]+ *[-\\+]+)* *[0-9]+\\D*$", "");
		let rest : String = tmp.substring(result[0].length).trim();
		result[1] = JavaString.replaceFirst(rest, "\\D*$", "").trim();
		result[2] = rest.substring(result[1].length).trim();
		if (result[0].length > 55) 
			result[0] = result[0].substring(0, 55);
		if (result[1].length > 10) 
			result[1] = result[1].substring(0, 10);
		if (result[2].length > 30) 
			result[2] = result[2].substring(0, 30);
		return result;
	}

	/**
	 * Kombiniert die übergebenen Werte für den Strassennamen, die Hausnummer und
	 * den Zusatz zu einer Strassenangabe in einem String.
	 * 
	 * @param name         der Strassenname
	 * @param hausNummer   die Hausnummer
	 * @param zusatz       der Hausnummerzusatz
	 * 
	 * @return die kombinierte Strassenangabe
	 */
	public static combineStrasse(name : String | null, hausNummer : String | null, zusatz : String | null) : String | null {
		if ((name === null) || (hausNummer === null) || (zusatz === null)) 
			return null;
		if (JavaObject.equalsTranspiler("", (hausNummer.trim())) && (JavaObject.equalsTranspiler("", (zusatz.trim())))) 
			return name;
		return name.valueOf() + " " + hausNummer.trim().valueOf() + zusatz.trim().valueOf();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.AdressenUtils'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_AdressenUtils(obj : unknown) : AdressenUtils {
	return obj as AdressenUtils;
}
