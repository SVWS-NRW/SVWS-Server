import { RuntimeException } from '../../java/lang/RuntimeException';

export class UserNotificationException extends RuntimeException {

	private static readonly serialVersionUID : number = -6612891372229214720;


	/**
	 *
	 * Erzeugt eine Beschreibung des Fehlers, die dem User hilft die Ursache des Fehlers zu beheben.
	 *
	 * @param pFehlermeldung Eine Beschreibung des Fehlers, die dem User hilft die Ursache des Fehlers zu beheben.
	 */
	public constructor(pFehlermeldung : string) {
		super(pFehlermeldung);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.lang.Throwable', 'java.lang.RuntimeException', 'de.nrw.schule.svws.core.exceptions.UserNotificationException', 'java.lang.Exception', 'java.io.Serializable'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_exceptions_UserNotificationException(obj : unknown) : UserNotificationException {
	return obj as UserNotificationException;
}
