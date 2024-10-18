import { RuntimeException } from '../../java/lang/RuntimeException';
import { Class } from '../../java/lang/Class';

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

	/**
	 * Überprüft, ob eine Bedingung erfüllt ist und wirft in diesem Fall eine UserNotificationException.
	 *
	 * @param pBeschreibung Die Beschreibung der Bedingung.
	 * @param pErfuellt     Falls TRUE, wird eine UserNotificationException geworfen.
	 */
	public static ifTrue(pBeschreibung : string, pErfuellt : boolean) : void {
		if (pErfuellt)
			throw new UserNotificationException(pBeschreibung)
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.exceptions.UserNotificationException';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.lang.Throwable', 'java.lang.RuntimeException', 'java.lang.Exception', 'de.svws_nrw.core.exceptions.UserNotificationException', 'java.io.Serializable'].includes(name);
	}

	public static class = new Class<UserNotificationException>('de.svws_nrw.core.exceptions.UserNotificationException');

}

export function cast_de_svws_nrw_core_exceptions_UserNotificationException(obj : unknown) : UserNotificationException {
	return obj as UserNotificationException;
}
