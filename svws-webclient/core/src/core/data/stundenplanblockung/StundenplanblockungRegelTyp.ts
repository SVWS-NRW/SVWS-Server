import { JavaEnum } from '../../../java/lang/JavaEnum';
import { StundenplanblockungRegel, cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanblockungRegel } from '../../../core/data/stundenplanblockung/StundenplanblockungRegel';
import { StundenplanblockungRegelParameterTyp } from '../../../core/data/stundenplanblockung/StundenplanblockungRegelParameterTyp';
import { HashMap } from '../../../java/util/HashMap';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import type { Collection } from '../../../java/util/Collection';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Collections } from '../../../java/util/Collections';
import { Arrays } from '../../../java/util/Arrays';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class StundenplanblockungRegelTyp extends JavaEnum<StundenplanblockungRegelTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<StundenplanblockungRegelTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, StundenplanblockungRegelTyp> = new Map<string, StundenplanblockungRegelTyp>();

	/**
	 *  Eine Regel ist nicht definiert.
	 */
	public static readonly UNDEFINIERT : StundenplanblockungRegelTyp = new StundenplanblockungRegelTyp("UNDEFINIERT", 0, 0, "Undefiniert", Collections.emptyList());

	/**
	 *  Definiert, wie viele Tage in der Woche Unterricht stattfindet.
	 */
	public static readonly SCHULE_TAGE_PRO_WOCHE : StundenplanblockungRegelTyp = new StundenplanblockungRegelTyp("SCHULE_TAGE_PRO_WOCHE", 1, 1, "Schule: Unterrichtstage pro Woche:", Arrays.asList(StundenplanblockungRegelParameterTyp.WERT_INTEGER));

	/**
	 *  Definiert, wie viele Stunden es pro Tag maximal gibt.
	 */
	public static readonly SCHULE_LETZTE_STUNDE : StundenplanblockungRegelTyp = new StundenplanblockungRegelTyp("SCHULE_LETZTE_STUNDE", 2, 2, "Schule: Letzte mögliche Stunde:", Arrays.asList(StundenplanblockungRegelParameterTyp.WERT_INTEGER));

	/**
	 *  Definiert, wie viele Springstunden eine Lehrkraft pro Woche maximal haben darf.
	 *  Dieser Wert kann pro Lehrkraft mit einer Regel individuell überschrieben werden.
	 */
	public static readonly SCHULE_LEHRKRAFT_MAX_SPRING_PRO_WOCHE : StundenplanblockungRegelTyp = new StundenplanblockungRegelTyp("SCHULE_LEHRKRAFT_MAX_SPRING_PRO_WOCHE", 3, 3, "Schule: Pro Lehrkraft pro Woche max. Springstunden:", Arrays.asList(StundenplanblockungRegelParameterTyp.WERT_INTEGER));

	/**
	 *  Definiert, wie viele Springstunden eine Lehrkraft pro Tag maximal haben darf.
	 *  Dieser Wert kann pro Lehrkraft mit einer Regel individuell überschrieben werden.
	 */
	public static readonly SCHULE_LEHRKRAFT_MAX_SPRING_PRO_TAG : StundenplanblockungRegelTyp = new StundenplanblockungRegelTyp("SCHULE_LEHRKRAFT_MAX_SPRING_PRO_TAG", 4, 4, "Schule: Pro Lehrkraft pro Tag max. Springstunden:", Arrays.asList(StundenplanblockungRegelParameterTyp.WERT_INTEGER));

	/**
	 *  Definiert, wie viel Präsenz (Unterricht + Springstunden) eine Lehrkraft pro Tag maximal haben darf.
	 *  Dieser Wert kann pro Lehrkraft mit einer Regel individuell überschrieben werden.
	 */
	public static readonly SCHULE_LEHRKRAFT_MAX_PRAESENZ_PRO_TAG : StundenplanblockungRegelTyp = new StundenplanblockungRegelTyp("SCHULE_LEHRKRAFT_MAX_PRAESENZ_PRO_TAG", 5, 5, "Schule: Pro Lehrkraft pro Tag max. Präsenz:", Arrays.asList(StundenplanblockungRegelParameterTyp.WERT_INTEGER));

	/**
	 *  Definiert, ob in bestimmten Stunden Einzelstunden verboten sind.
	 */
	public static readonly SCHULE_LERNGRUPPEN_KEINE_EINZELSTUNDE_IN_STUNDE : StundenplanblockungRegelTyp = new StundenplanblockungRegelTyp("SCHULE_LERNGRUPPEN_KEINE_EINZELSTUNDE_IN_STUNDE", 6, 6, "Schule: Lerngruppen haben keine Enzelstunde in Stunde:", Arrays.asList(StundenplanblockungRegelParameterTyp.WERT_INTEGER));

	/**
	 *  Definiert, ob eine Doppelstunde (und mehr) diese und die nächste Stunde überschreiten darf.
	 */
	public static readonly SCHULE_LERNGRUPPEN_KEINE_STUNDENUEBERGAENGE_IN_STUNDE_UND_DARAUFFOLGEND : StundenplanblockungRegelTyp = new StundenplanblockungRegelTyp("SCHULE_LERNGRUPPEN_KEINE_STUNDENUEBERGAENGE_IN_STUNDE_UND_DARAUFFOLGEND", 7, 7, "Schule: Lerngruppen haben keinen Übergang von dieser zur darauffolgenden Stunde:", Arrays.asList(StundenplanblockungRegelParameterTyp.WERT_INTEGER));

	/**
	 * Der ID des Typs der Regel.
	 */
	public readonly id : number;

	/**
	 * Die Bezeichnung des Regel-Typs.
	 */
	public readonly bezeichnung : string | null;

	/**
	 * Die Typen der Regel-Parameter
	 */
	private readonly paramTypes : List<StundenplanblockungRegelParameterTyp>;

	/**
	 * Mapping von "Typ --> GostKursblockungRegelTyp".
	 */
	private static readonly _map_id_regel : HashMap<number, StundenplanblockungRegelTyp> = new HashMap<number, StundenplanblockungRegelTyp>();

	/**
	 * Erstellt einen neuen Regel-Typ mit der angegeben ID.
	 *
	 * @param id            die ID des Regel-Typs
	 * @param bezeichnung   die textuelle Bezeichnung für diesen Regel-Typ
	 * @param paramTypes    die Typen der Parameter für diesen Regel-Typ
	 */
	private constructor(name : string, ordinal : number, id : number, bezeichnung : string, paramTypes : List<StundenplanblockungRegelParameterTyp>) {
		super(name, ordinal);
		StundenplanblockungRegelTyp.all_values_by_ordinal.push(this);
		StundenplanblockungRegelTyp.all_values_by_name.set(name, this);
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.paramTypes = paramTypes;
	}

	/**
	 * Liefert die Map. Falls diese leer ist, wird sie vorher gefüllt.
	 *
	 * @return Liefert die Map. Falls diese leer ist, wird sie vorher gefüllt.
	 */
	private static getMap() : HashMap<number, StundenplanblockungRegelTyp> {
		if (StundenplanblockungRegelTyp._map_id_regel.isEmpty())
			for (const typ of StundenplanblockungRegelTyp.values())
				if (StundenplanblockungRegelTyp._map_id_regel.put(typ.id, typ) !== null)
					throw new NullPointerException("StundenplanblockungRegelTyp.id=" + typ.id + " doppelt!")
		return StundenplanblockungRegelTyp._map_id_regel;
	}

	/**
	 * Liefert die Menge aller existierender Regeln.
	 *
	 * @return Die Menge aller existierender Regeln.
	 */
	public static getCollection() : Collection<StundenplanblockungRegelTyp> {
		return StundenplanblockungRegelTyp.getMap().values();
	}

	/**
	 * Ermittelt den Regel-Typ anhand seiner ID und gibt diesen zurück.
	 *
	 * @param id   die ID des Regel-Typs
	 * @return der Regel-Typ
	 */
	public static fromTyp(id : number | null) : StundenplanblockungRegelTyp {
		if (id === null)
			return StundenplanblockungRegelTyp.UNDEFINIERT;
		const gostTyp : StundenplanblockungRegelTyp | null = StundenplanblockungRegelTyp.getMap().get(id);
		if (gostTyp === null)
			return StundenplanblockungRegelTyp.UNDEFINIERT;
		return gostTyp;
	}

	/**
	 * Ermittelt den Regel-Typ anhand des Regel-Objektes.
	 *
	 * @param pRegel Das Regel-Objekt.
	 * @return der Regel-Typ
	 */
	public static fromRegel(pRegel : StundenplanblockungRegel) : StundenplanblockungRegelTyp {
		return StundenplanblockungRegelTyp.fromTyp(pRegel.typ);
	}

	/**
	 * Gibt die Anzahl der Parameter für diesen Regel-Type zurück.
	 *
	 * @return die Anzahl der Parameter für diesen Regel-Type zurück.
	 */
	public getParamCount() : number {
		return this.paramTypes.size();
	}

	/**
	 * Gibt den i-ten Parameter-Typ der Regel zurück.
	 *
	 * @param i   der Index des Parameters
	 *
	 * @return der Parameter-Typ
	 *
	 * @throws IllegalArgumentException falls der angegebene Index ungültig ist
	 */
	public getParamType(i : number) : StundenplanblockungRegelParameterTyp {
		if ((i < 0) || (i >= this.paramTypes.size()))
			throw new IllegalArgumentException("Ein Parameter mit dem Index i existiert nicht für den Regel-Typ " + this.name()!)
		return this.paramTypes.get(i);
	}

	/**
	 * Prüft, ob der Regeltyp einen Parameter von dem angegebenen
	 * Parametertyp hat.
	 *
	 * @param paramType   der Parametertyp
	 *
	 * @return true, falls die Regel einen solchen Parametertyp hat und ansonsten false
	 */
	public hasParamType(paramType : StundenplanblockungRegelParameterTyp | null) : boolean {
		for (const cur of this.paramTypes)
			if (paramType as unknown === cur as unknown)
				return true;
		return false;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<StundenplanblockungRegelTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : StundenplanblockungRegelTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungRegelTyp';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungRegelTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<StundenplanblockungRegelTyp>('de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungRegelTyp');

}

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanblockungRegelTyp(obj : unknown) : StundenplanblockungRegelTyp {
	return obj as StundenplanblockungRegelTyp;
}
