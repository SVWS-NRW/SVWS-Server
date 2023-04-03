import { RuntimeException } from '../../java/lang/RuntimeException';

export class UserNotificationException extends RuntimeException {

	private static readonly serialVersionUID : number = -6612891372229214720;


	/**
	 * Erzeugt eine Beschreibung des Fehlers, die dem User hilft die Ursache des Fehlers zu beheben.
	 *
	 * @param pFehlermeldung Eine Beschreibung des Fehlers, die dem User hilft die Ursache des Fehlers zu beheben.
	 */
	public constructor(pFehlermeldung : string) {
		super(pFehlermeldung);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.lang.Throwable', 'java.lang.RuntimeException', 'java.lang.Exception', 'de.svws_nrw.core.exceptions.UserNotificationException', 'java.io.Serializable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_exceptions_UserNotificationException(obj : unknown) : UserNotificationException {
	return obj as UserNotificationException;
}
