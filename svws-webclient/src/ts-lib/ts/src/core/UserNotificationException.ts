import { JavaObject, cast_java_lang_Object } from '../java/lang/JavaObject';
import { RuntimeException, cast_java_lang_RuntimeException } from '../java/lang/RuntimeException';
import { JavaString, cast_java_lang_String } from '../java/lang/JavaString';

export class UserNotificationException extends RuntimeException {

	private static readonly serialVersionUID : number = -6612891372229214720;


	/**
	 *
	 * Erzeugt eine Beschreibung des Fehlers, die dem User hilft die Ursache des Fehlers zu beheben.
	 * 
	 * @param pFehlermeldung Eine Beschreibung des Fehlers, die dem User hilft die Ursache des Fehlers zu beheben.
	 */
	public constructor(pFehlermeldung : String) {
		super(pFehlermeldung);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.lang.Throwable', 'de.nrw.schule.svws.core.UserNotificationException', 'java.lang.RuntimeException', 'java.lang.Exception', 'java.io.Serializable'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_UserNotificationException(obj : unknown) : UserNotificationException {
	return obj as UserNotificationException;
}
