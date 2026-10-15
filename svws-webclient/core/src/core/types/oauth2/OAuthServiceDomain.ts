import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';

export class OAuthServiceDomain extends JavaEnum<OAuthServiceDomain> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<OAuthServiceDomain> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, OAuthServiceDomain> = new Map<string, OAuthServiceDomain>();

	/**
	 *  Web Noten Manager
	 */
	public static readonly WENOM: OAuthServiceDomain = new OAuthServiceDomain("WENOM", 0, "WENOM");

	/**
	 *  Schnittstelle schulbewerbung.de
	 */
	public static readonly SCHULBEWERBUNG: OAuthServiceDomain = new OAuthServiceDomain("SCHULBEWERBUNG", 1, "SCHULBEWERBUNG");

	/**
	 *  IT NRW
	 */
	public static readonly IT_NRW: OAuthServiceDomain = new OAuthServiceDomain("IT_NRW", 2, "IT_NRW");

	private readonly dbValue: string | null;

	/**
	 * Erstellt eine neue OAuth Service Domäne
	 *
	 * @param dbValue der DB-Wert der OAuth-Domäne
	 */
	private constructor(name: string, ordinal: number, dbValue: string | null) {
		super(name, ordinal);
		OAuthServiceDomain.all_values_by_ordinal.push(this);
		OAuthServiceDomain.all_values_by_name.set(name, this);
		this.dbValue = dbValue;
	}

	/**
	 * Gibt den DB-Wert des Enums zurück
	 *
	 * @return den DB-Wert
	 */
	public getDbValue(): string | null {
		return this.dbValue;
	}

	/**
	 * Diese Methode ermittelt die OAuth Domäne anhand des übergebenen Database Wertes.
	 *
	 * @param dbValue DB Wert der OAuth Domaine
	 * @return die OAuth Domaine
	 */
	public static getByDbValue(dbValue: string | null): OAuthServiceDomain | null {
		let _sevar_39850074;
		const _seexpr_39850074 = (dbValue);
		if (_seexpr_39850074 === "WENOM") {
			_sevar_39850074 = OAuthServiceDomain.WENOM;
		} else if (_seexpr_39850074 === "SCHULBEWERBUNG") {
			_sevar_39850074 = OAuthServiceDomain.SCHULBEWERBUNG;
		} else if (_seexpr_39850074 === "IT_NRW") {
			_sevar_39850074 = OAuthServiceDomain.IT_NRW;
		} else {
			_sevar_39850074 = null;
		}
		return _sevar_39850074;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<OAuthServiceDomain> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): OAuthServiceDomain | null {
		const tmp = this.all_values_by_name.get(name);
		return tmp ?? null;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.oauth2.OAuthServiceDomain';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.oauth2.OAuthServiceDomain', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<OAuthServiceDomain>('de.svws_nrw.core.types.oauth2.OAuthServiceDomain');

}

export function cast_de_svws_nrw_core_types_oauth2_OAuthServiceDomain(obj: unknown): OAuthServiceDomain {
	return obj as OAuthServiceDomain;
}
