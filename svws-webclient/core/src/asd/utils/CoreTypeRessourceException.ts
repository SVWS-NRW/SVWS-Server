import { RuntimeException } from '../../java/lang/RuntimeException';
import { Throwable } from '../../java/lang/Throwable';
import { Class } from '../../java/lang/Class';

export class CoreTypeRessourceException extends RuntimeException {

	private static readonly serialVersionUID: number = 878123458912431;


	/**
	 * Erzeugt eine neue Exception mit der übergebenen Nachricht und dem übergebenen Grund.
	 *
	 * @param message   die Nachricht
	 * @param cause     der Grund
	 */
	public constructor(message: string | null, cause: Throwable | null) {
		super(message, cause);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.utils.CoreTypeRessourceException';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['java.lang.Throwable', 'java.lang.RuntimeException', 'de.svws_nrw.asd.utils.CoreTypeRessourceException', 'java.lang.Exception', 'java.io.Serializable'].includes(name);
	}

	public static readonly class = new Class<CoreTypeRessourceException>('de.svws_nrw.asd.utils.CoreTypeRessourceException');

}

export function cast_de_svws_nrw_asd_utils_CoreTypeRessourceException(obj: unknown): CoreTypeRessourceException {
	return obj as CoreTypeRessourceException;
}
