import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';

export class OAuth2ServerTyp extends JavaEnum<OAuth2ServerTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<OAuth2ServerTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, OAuth2ServerTyp> = new Map<string, OAuth2ServerTyp>();

	/**
	 * Web Noten Manager
	 */
	public static readonly WENOM : OAuth2ServerTyp = new OAuth2ServerTyp("WENOM", 0, 1);

	/**
	 * Schüler Online
	 */
	public static readonly SCHUELER_ONLINE : OAuth2ServerTyp = new OAuth2ServerTyp("SCHUELER_ONLINE", 1, 2);

	/**
	 * Die ID des Server-Typs
	 */
	private readonly id : number;

	/**
	 * Erstellt einen neuen OAuth2-Servertyp
	 *
	 * @param id   die ID des OAuth2-Server-Typs
	 */
	private constructor(name : string, ordinal : number, id : number) {
		super(name, ordinal);
		OAuth2ServerTyp.all_values_by_ordinal.push(this);
		OAuth2ServerTyp.all_values_by_name.set(name, this);
		this.id = id;
	}

	/**
	 * Gibt die ID dieses OAuth Servers wieder
	 *
	 * @return die ID
	 */
	public getId() : number {
		return this.id;
	}

	/**
	 * Diese Methode ermittelt den OAuth2-Servertyp anhand der übergebenen ID.
	 *
	 * @param id   die ID des OAuth2-Servertyps
	 *
	 * @return der OAuth2-Servertyp
	 */
	public static getByID(id : number) : OAuth2ServerTyp | null {
		for (const s of OAuth2ServerTyp.values())
			if (s.id === id)
				return s;
		return null;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<OAuth2ServerTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : OAuth2ServerTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.oauth2.OAuth2ServerTyp';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.oauth2.OAuth2ServerTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<OAuth2ServerTyp>('de.svws_nrw.core.types.oauth2.OAuth2ServerTyp');

}

export function cast_de_svws_nrw_core_types_oauth2_OAuth2ServerTyp(obj : unknown) : OAuth2ServerTyp {
	return obj as OAuth2ServerTyp;
}
