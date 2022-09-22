import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { RuntimeException, cast_java_lang_RuntimeException } from '../../java/lang/RuntimeException';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';

export class KursblockungException extends RuntimeException {

	private static readonly serialVersionUID : number = -8026873013947670692;


	/**
	 *Erzeugt ein Fehler-Exception, die während der Kursblockung entstehen kann.
	 * 
	 * @param fehlermeldung Eine Beschreibung des Fehlers. 
	 */
	public constructor(fehlermeldung : String) {
		super(fehlermeldung);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.lang.Throwable', 'java.lang.RuntimeException', 'java.lang.Exception', 'de.nrw.schule.svws.core.kursblockung.KursblockungException', 'java.io.Serializable'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungException(obj : unknown) : KursblockungException {
	return obj as KursblockungException;
}
