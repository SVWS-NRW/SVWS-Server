import { JavaObject } from '../../java/lang/JavaObject';
import { RuntimeException } from '../../java/lang/RuntimeException';
import { Throwable, cast_java_lang_Throwable } from '../../java/lang/Throwable';
import { Class } from '../../java/lang/Class';

export class ValidatorException extends RuntimeException {

	private static readonly serialVersionUID : number = 4841861661278456913;


	/**
	 * Erstellt eine neue Validator-Exception
	 */
	public constructor();

	/**
	 * Erstellt eine neue Validator-Exception mit der übergebenen Nachricht.
	 *
	 * @param message   die Nachricht der Exception (siehe {@link #getMessage()})
	 */
	public constructor(message : string | null);

	/**
	 * Erstellt eine neue Validator-Exception mit der übergebenen Nachricht und
	 * dem übergebenen Grund.
	 *
	 * @param message   die Nachricht der Exception (siehe {@link #getMessage()})
	 * @param cause     der Grund für die Exception oder null
	 */
	public constructor(message : string | null, cause : Throwable | null);

	/**
	 * Erstellt eine neue Validator-Exception mit dem übergebenen Grund.
	 *
	 * @param cause   der Grund für die Exception oder null
	 */
	public constructor(cause : Throwable | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : Throwable | null | string, __param1? : Throwable | null) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && (typeof __param0 === "string") || (__param0 === null)) && (__param1 === undefined)) {
			const message : string | null = __param0;
			// empty method body
		} else if (((__param0 !== undefined) && (typeof __param0 === "string") || (__param0 === null)) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('java.lang.Throwable'))) || (__param1 === null))) {
			const message : string | null = __param0;
			const cause : Throwable | null = cast_java_lang_Throwable(__param1);
			// empty method body
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.lang.Throwable'))) || (__param0 === null)) && (__param1 === undefined)) {
			const cause : Throwable | null = cast_java_lang_Throwable(__param0);
			// empty method body
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.ValidatorException';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.lang.Throwable', 'de.svws_nrw.asd.validate.ValidatorException', 'java.lang.RuntimeException', 'java.lang.Exception', 'java.io.Serializable'].includes(name);
	}

	public static class = new Class<ValidatorException>('de.svws_nrw.asd.validate.ValidatorException');

}

export function cast_de_svws_nrw_asd_validate_ValidatorException(obj : unknown) : ValidatorException {
	return obj as ValidatorException;
}
