import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { RuntimeException, cast_java_lang_RuntimeException } from '../../java/lang/RuntimeException';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';

export class SchuelerblockungException extends RuntimeException {

	private static readonly serialVersionUID : number = -7308290111735862056;


	/**
	 *Erzeugt ein Fehler-Exception, die während der Kursblockung entstehen kann.
	 * 
	 * @param fehlermeldung Eine Beschreibung des Fehlers. 
	 */
	public constructor(fehlermeldung : String) {
		super(fehlermeldung);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.SchuelerblockungException', 'java.lang.Throwable', 'java.lang.RuntimeException', 'java.lang.Exception', 'java.io.Serializable'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_SchuelerblockungException(obj : unknown) : SchuelerblockungException {
	return obj as SchuelerblockungException;
}
