import { JavaEnum } from '../../java/lang/JavaEnum';
import { HashMap } from '../../java/util/HashMap';

export class SchuelerStatus extends JavaEnum<SchuelerStatus> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<SchuelerStatus> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, SchuelerStatus> = new Map<string, SchuelerStatus>();

	/**
	 * Status Neuaufnahme mit dem Wert 0
	 */
	public static readonly NEUAUFNAHME : SchuelerStatus = new SchuelerStatus("NEUAUFNAHME", 0, 0, "Neuaufnahme", null, null);

	/**
	 * Status Warteliste mit dem Wert 1
	 */
	public static readonly WARTELISTE : SchuelerStatus = new SchuelerStatus("WARTELISTE", 1, 1, "Warteliste", null, null);

	/**
	 * Status Aktiv mit dem Wert 2
	 */
	public static readonly AKTIV : SchuelerStatus = new SchuelerStatus("AKTIV", 2, 2, "Aktiv", null, null);

	/**
	 * Status Beurlaubt mit dem Wert 3
	 */
	public static readonly BEURLAUBT : SchuelerStatus = new SchuelerStatus("BEURLAUBT", 3, 3, "Beurlaubt", null, null);

	/**
	 * Status Extern mit dem Wert 6
	 */
	public static readonly EXTERN : SchuelerStatus = new SchuelerStatus("EXTERN", 4, 6, "Extern", null, null);

	/**
	 * Status Abschluss mit dem Wert 8
	 */
	public static readonly ABSCHLUSS : SchuelerStatus = new SchuelerStatus("ABSCHLUSS", 5, 8, "Abschluss", null, null);

	/**
	 * Status Abgänger mit dem Wert 9
	 */
	public static readonly ABGANG : SchuelerStatus = new SchuelerStatus("ABGANG", 6, 9, "Abgang", null, null);

	/**
	 * Status Abgänger mit dem Wert 10
	 */
	public static readonly EHEMALIGE : SchuelerStatus = new SchuelerStatus("EHEMALIGE", 7, 10, "Ehemalige", null, null);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Die Zuordnung des Schüler-Status zu der ID
	 */
	private static readonly _mapID : HashMap<number, SchuelerStatus> = new HashMap();

	/**
	 * Die Zuordnung des Schüler-Status zu der ID
	 */
	private static readonly _mapBezeichnungen : HashMap<string, SchuelerStatus> = new HashMap();

	/**
	 * Die ID des Schüler Status, welche auch in der SVWS-Datenbank genutzt wird.
	 */
	public readonly id : number;

	/**
	 * Die textuelle Bezeichnung des Schülerstatus.
	 */
	public readonly bezeichnung : string;

	/**
	 * Gibt an, in welchem Schuljahr der Schülerstatus eingeführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public readonly gueltigVon : number | null;

	/**
	 * Gibt an, bis zu welchem Schuljahr der Schülerstatus gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public readonly gueltigBis : number | null;

	/**
	 * Erzeugt einen neuen Schüler-Status in der Aufzählung.
	 *
	 * @param id            die ID des Schüler Status, welche auch in der SVWS-Datenbank genutzt wird
	 * @param bezeichnung   die textuelle Bezeichnung des Schülerstatus
	 * @param gueltigVon    gibt an, in welchem Schuljahr der Schülerstatus eingeführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 * @param gueltigBis    gibt an, bis zu welchem Schuljahr der Schülerstatus gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	private constructor(name : string, ordinal : number, id : number, bezeichnung : string, gueltigVon : number | null, gueltigBis : number | null) {
		super(name, ordinal);
		SchuelerStatus.all_values_by_ordinal.push(this);
		SchuelerStatus.all_values_by_name.set(name, this);
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

	/**
	 * Gibt eine Map von den IDs der Schüler-Status auf die zugehörigen Schüler-Status
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Schüler-Status auf die zugehörigen Schüler-Status
	 */
	private static getMapID() : HashMap<number, SchuelerStatus> {
		if (SchuelerStatus._mapID.size() === 0)
			for (const p of SchuelerStatus.values())
				SchuelerStatus._mapID.put(p.id, p);
		return SchuelerStatus._mapID;
	}

	/**
	 * Gibt eine Map von den Bezeichnungen der Schüler-Status auf die zugehörigen Schüler-Status
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Bezeichnungen der Schüler-Status auf die zugehörigen Schüler-Status
	 */
	private static getMapBezeichnungen() : HashMap<string, SchuelerStatus> {
		if (SchuelerStatus._mapBezeichnungen.size() === 0)
			for (const p of SchuelerStatus.values())
				SchuelerStatus._mapBezeichnungen.put(p.bezeichnung.toUpperCase(), p);
		return SchuelerStatus._mapBezeichnungen;
	}

	/**
	 * Gibt den Schülerstatus anhand der ID zurück.
	 *
	 * @param status	die id des Schülerstatus
	 *
	 * @return der Schülerstatus oder null, falls die ID ungültig ist
	 */
	public static fromID(status : number) : SchuelerStatus | null {
		return SchuelerStatus.getMapID().get(status);
	}

	/**
	 * Ermittelt den Schülerstatus anhand der Bezeichnung.
	 *
	 * @param value	  die Bezeichnung des Schülerstatus
	 *
	 * @return der Schülerstatus oder null, falls die Bezeichnung ungültig ist
	 */
	public static fromBezeichnung(value : string | null) : SchuelerStatus | null {
		if (value === null)
			return null;
		return SchuelerStatus.getMapBezeichnungen().get(value.toUpperCase());
	}

	/**
	 * Prüft, ob die übergebene ID für einen gültigen Schülerstatus
	 * steht oder nicht
	 *
	 * @param id   die zu prüfende ID
	 *
	 * @return true, falls die ID gültig ist.
	 */
	public static isValidID(id : number | null) : boolean {
		if (id === null)
			return false;
		for (const status of SchuelerStatus.values())
			if (status.id === id)
				return true;
		return false;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<SchuelerStatus> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : SchuelerStatus | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.SchuelerStatus', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_SchuelerStatus(obj : unknown) : SchuelerStatus {
	return obj as SchuelerStatus;
}
