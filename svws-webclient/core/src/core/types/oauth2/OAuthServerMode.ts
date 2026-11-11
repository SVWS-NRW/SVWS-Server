import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { ServerMode } from '../../../core/types/ServerMode';

export class OAuthServerMode extends JavaEnum<OAuthServerMode> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<OAuthServerMode> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, OAuthServerMode> = new Map<string, OAuthServerMode>();

	/**
	 * Die Testumgebung der externen Systeme. Wird von den Server-Modi BETA, ALPHA und DEV genutzt.
	 */
	public static readonly DEV: OAuthServerMode = new OAuthServerMode("DEV", 0, "DEVELOPMENT");

	/**
	 * Die Produktivumgebung der externen Systeme. Wird vom Server-Modus STABLE genutzt.
	 */
	public static readonly PROD: OAuthServerMode = new OAuthServerMode("PROD", 1, "PRODUCTION");

	private readonly dbValue: string;

	/**
	 * Erstellt einen neuen OAuth-Server-Modus.
	 *
	 * @param dbValue   der DB-Wert des Modus
	 */
	private constructor(name: string, ordinal: number, dbValue: string) {
		super(name, ordinal);
		OAuthServerMode.all_values_by_ordinal.push(this);
		OAuthServerMode.all_values_by_name.set(name, this);
		this.dbValue = dbValue;
	}

	/**
	 * Gibt den DB-Wert des Enums zurück.
	 *
	 * @return den DB-Wert
	 */
	public getDbValue(): string {
		return this.dbValue;
	}

	/**
	 * Diese Methode ermittelt den OAuth-Server-Modus anhand des übergebenen DB-Wertes.
	 *
	 * @param dbValue   der DB-Wert des OAuth-Server-Modus
	 *
	 * @return der OAuth-Server-Modus
	 */
	public static getByDbValue(dbValue: string): OAuthServerMode {
		let _sevar_843551382;
		const _seexpr_843551382 = (dbValue);
		if (_seexpr_843551382 === "DEVELOPMENT") {
			_sevar_843551382 = OAuthServerMode.DEV;
		} else if (_seexpr_843551382 === "PRODUCTION") {
			_sevar_843551382 = OAuthServerMode.PROD;
		} else {
			throw new DeveloperNotificationException("Der OAuth-Server-Modus mit dem DB-Wert " + dbValue + " existiert nicht.");;
		}
		return _sevar_843551382;
	}

	/**
	 * Bildet den Modus, in welchem der Server betrieben wird, auf den OAuth-Server-Modus ab:
	 * BETA, ALPHA und DEV werden auf DEV abgebildet, STABLE und null auf PROD. Der Rückfall
	 * auf PROD ist Absicht: Ein Server im Zweifel nutzt nie die Test-Systeme.
	 *
	 * @param serverMode   der Modus, in welchem der Server betrieben wird
	 *
	 * @return der zugehörige OAuth-Server-Modus
	 */
	public static getByServerMode(serverMode: ServerMode | null): OAuthServerMode | null {
		if ((serverMode as unknown === ServerMode.BETA as unknown) || (serverMode as unknown === ServerMode.ALPHA as unknown) || (serverMode as unknown === ServerMode.DEV as unknown)) {
			return OAuthServerMode.DEV;
		}
		return OAuthServerMode.PROD;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<OAuthServerMode> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): OAuthServerMode | null {
		const tmp = this.all_values_by_name.get(name);
		return tmp ?? null;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.oauth2.OAuthServerMode';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.oauth2.OAuthServerMode', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<OAuthServerMode>('de.svws_nrw.core.types.oauth2.OAuthServerMode');

}

export function cast_de_svws_nrw_core_types_oauth2_OAuthServerMode(obj: unknown): OAuthServerMode {
	return obj as OAuthServerMode;
}
