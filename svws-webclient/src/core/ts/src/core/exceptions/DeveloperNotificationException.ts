import { RuntimeException } from '../../java/lang/RuntimeException';

export class DeveloperNotificationException extends RuntimeException {

	private static readonly serialVersionUID : number = 9106453927748350030;


	/**
	 * Erzeugt eine Beschreibung des Fehlers, die dem User hilft die Ursache des Fehlers zu beheben.
	 *
	 * @param pFehlermeldung Eine Beschreibung des Fehlers, die dem User hilft die Ursache des Fehlers zu beheben.
	 */
	public constructor(pFehlermeldung : string) {
		super(pFehlermeldung);
	}

	/**
	 * Überprüft, ob eine Bedingung erfüllt ist und wirft in diesem Fall eine DeveloperNotificationException.
	 *
	 * @param pBeschreibung Die Beschreibung der Bedingung.
	 * @param pErfuellt     Falls TRUE, wird eine DeveloperNotificationException geworfen.
	 */
	public static check(pBeschreibung : string, pErfuellt : boolean) : void {
		if (pErfuellt)
			throw new DeveloperNotificationException(pBeschreibung)
	}

	/**
	 * Überprüft, ob eine Bedingung erfüllt ist und wirft in diesem Fall eine DeveloperNotificationException.
	 * Andernfalls wird der Parameter t zurückgegeben.
	 *
	 * @param pFehlermeldung Die Beschreibung der Bedingung.
	 * @param pErgebnis Der Rückgabewert, falls es keinen Fehler gibt.
	 * @param <T> Der Typ von pErgebnis.
	 * @return Liefert pErgebnis, falls es keinen Fehler gibt.
	 */
	public static checkNull<T>(pFehlermeldung : string, pErgebnis : T | null) : T {
		if (pErgebnis === null)
			throw new DeveloperNotificationException(pFehlermeldung)
		return pErgebnis;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.lang.Throwable', 'de.svws_nrw.core.exceptions.DeveloperNotificationException', 'java.lang.RuntimeException', 'java.lang.Exception', 'java.io.Serializable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_exceptions_DeveloperNotificationException(obj : unknown) : DeveloperNotificationException {
	return obj as DeveloperNotificationException;
}
