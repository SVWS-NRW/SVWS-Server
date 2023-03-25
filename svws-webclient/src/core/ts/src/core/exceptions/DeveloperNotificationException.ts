import { RuntimeException } from '../../java/lang/RuntimeException';

export class DeveloperNotificationException extends RuntimeException {

	private static readonly serialVersionUID : number = 9106453927748350030;


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
		return ['java.lang.Throwable', 'de.svws_nrw.core.exceptions.DeveloperNotificationException', 'java.lang.RuntimeException', 'java.lang.Exception', 'java.io.Serializable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_exceptions_DeveloperNotificationException(obj : unknown) : DeveloperNotificationException {
	return obj as DeveloperNotificationException;
}
