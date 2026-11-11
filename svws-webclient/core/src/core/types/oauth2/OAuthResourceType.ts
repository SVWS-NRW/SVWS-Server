import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';

export class OAuthResourceType extends JavaEnum<OAuthResourceType> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<OAuthResourceType> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, OAuthResourceType> = new Map<string, OAuthResourceType>();

	/**
	 * Der Signierdienst von IT.NRW
	 */
	public static readonly IT_NRW_SIGNIERDIENST: OAuthResourceType = new OAuthResourceType("IT_NRW_SIGNIERDIENST", 0, "SIGNIERDIENST");

	/**
	 * Der Abruf von Daten (z.B. erfolgreiche Bewerbungen) bei schulbewerbung.de
	 */
	public static readonly SCHULBEWERBUNG_ERFOLGREICHE_BEWERBUNGEN: OAuthResourceType = new OAuthResourceType("SCHULBEWERBUNG_ERFOLGREICHE_BEWERBUNGEN", 1, "ERFOLGREICHE_BEWERBUNGEN");

	private readonly dbValue: string;

	/**
	 * Erstellt einen neuen OAuth-Ressourcentyp.
	 *
	 * @param dbValue   der DB-Wert des Ressourcentyps
	 */
	private constructor(name: string, ordinal: number, dbValue: string) {
		super(name, ordinal);
		OAuthResourceType.all_values_by_ordinal.push(this);
		OAuthResourceType.all_values_by_name.set(name, this);
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
	 * Diese Methode ermittelt den OAuth-Ressourcentyp anhand des übergebenen DB-Wertes.
	 *
	 * @param dbValue   der DB-Wert des OAuth-Ressourcentyps
	 *
	 * @return der OAuth-Ressourcentyp
	 */
	public static getByDbValue(dbValue: string): OAuthResourceType {
		let _sevar_1398647371;
		const _seexpr_1398647371 = (dbValue);
		if (_seexpr_1398647371 === "SIGNIERDIENST") {
			_sevar_1398647371 = OAuthResourceType.IT_NRW_SIGNIERDIENST;
		} else if (_seexpr_1398647371 === "ERFOLGREICHE_BEWERBUNGEN") {
			_sevar_1398647371 = OAuthResourceType.SCHULBEWERBUNG_ERFOLGREICHE_BEWERBUNGEN;
		} else {
			throw new DeveloperNotificationException("Der OAuth-Ressourcentyp mit dem DB-Wert " + dbValue + " existiert nicht.");;
		}
		return _sevar_1398647371;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<OAuthResourceType> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): OAuthResourceType | null {
		const tmp = this.all_values_by_name.get(name);
		return tmp ?? null;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.oauth2.OAuthResourceType';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.oauth2.OAuthResourceType', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<OAuthResourceType>('de.svws_nrw.core.types.oauth2.OAuthResourceType');

}

export function cast_de_svws_nrw_core_types_oauth2_OAuthResourceType(obj: unknown): OAuthResourceType {
	return obj as OAuthResourceType;
}
