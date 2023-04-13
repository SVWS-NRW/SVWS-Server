import { JavaObject } from '../../java/lang/JavaObject';
import { IllegalStateException } from '../../java/lang/IllegalStateException';
import { JavaString } from '../../java/lang/JavaString';

export class AdressenUtils extends JavaObject {


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
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
	public static splitStrasse(strasse : string | null) : Array<string> {
		const result : Array<string> = Array(3).fill(null);
		if (strasse === null) {
			result[0] = "";
			result[1] = "";
			result[2] = "";
			return result;
		}
		const tmp : string = strasse.trim().replace("  ", " ").replace("  ", " ").replace(" -", "-").replace("- ", "-");
		result[0] = JavaString.replaceFirst(tmp, " *(\\d+ *[-\\+]+)* *\\d+\\D*$", "");
		const rest : string = tmp.substring(result[0].length).trim();
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
	public static combineStrasse(name : string | null, hausNummer : string | null, zusatz : string | null) : string | null {
		if ((name === null) || (hausNummer === null) || (zusatz === null))
			return null;
		if (JavaObject.equalsTranspiler("", (hausNummer.trim())) && (JavaObject.equalsTranspiler("", (zusatz.trim()))))
			return name;
		return name! + " " + hausNummer.trim()! + zusatz.trim()!;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AdressenUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_AdressenUtils(obj : unknown) : AdressenUtils {
	return obj as AdressenUtils;
}
