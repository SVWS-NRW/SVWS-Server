import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';

export class OAuthServiceDomain extends JavaEnum<OAuthServiceDomain> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<OAuthServiceDomain> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, OAuthServiceDomain> = new Map<string, OAuthServiceDomain>();

	/**
	 * Web Noten Manager
	 */
	public static readonly WENOM: OAuthServiceDomain = new OAuthServiceDomain("WENOM", 0, 1, "WENOM");

	/**
	 * Schüler Online
	 */
	public static readonly SCHUELER_ONLINE: OAuthServiceDomain = new OAuthServiceDomain("SCHUELER_ONLINE", 1, 2, "SCHUELER_ONLINE");

	private readonly id: number;

	private readonly dbValue: string | null;

	/**
	 * Erstellt eine neue OAuth Service Domäne
	 *
	 * @param id   die ID des OAuth-Domäne
	 * @param dbValue der DB-Wert der OAuth-Domäne
	 */
	private constructor(name: string, ordinal: number, id: number, dbValue: string | null) {
		super(name, ordinal);
		OAuthServiceDomain.all_values_by_ordinal.push(this);
		OAuthServiceDomain.all_values_by_name.set(name, this);
		this.id = id;
		this.dbValue = dbValue;
	}

	/**
	 * Gibt die ID der OAuth Domäne wieder
	 *
	 * @return die ID
	 */
	public getId(): number {
		return this.id;
	}

	/**
	 * Gibt den DB-Wert des Enums zurück
	 * @return den DB-Wert
	 */
	public getDbValue(): string | null {
		return this.dbValue;
	}

	/**
	 * Diese Methode ermittelt den OAuth-Domäne anhand der übergebenen ID.
	 *
	 * @param id   die ID des OAuth-Domäne
	 *
	 * @return die OAuth Domaine
	 */
	public static getByID(id: number): OAuthServiceDomain | null {
		for (const s of OAuthServiceDomain.values()) {
			if (s.id === id) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Diese Methode ermittelt die OAuth Domäne anhand des übergebenen Database Wertes.
	 *
	 * @param dbValue   DB Wert der OAuth Domaine
	 *
	 * @return die OAuth Domaine
	 */
	public static getByDbValue(dbValue: string | null): OAuthServiceDomain | null {
		let _sevar_1342810125 : any;
		const _seexpr_1342810125 = (dbValue);
		if (_seexpr_1342810125 === "WENOM") {
			_sevar_1342810125 = OAuthServiceDomain.WENOM;
		} else if (_seexpr_1342810125 === "SCHUELER_ONLINE") {
			_sevar_1342810125 = OAuthServiceDomain.SCHUELER_ONLINE;
		} else {
			_sevar_1342810125 = null;
		}
		return _sevar_1342810125;
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
		return (!tmp) ? null : tmp;
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
