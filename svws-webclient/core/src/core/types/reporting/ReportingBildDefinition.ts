import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class ReportingBildDefinition extends JavaEnum<ReportingBildDefinition> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<ReportingBildDefinition> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, ReportingBildDefinition> = new Map<string, ReportingBildDefinition>();

	/**
	 * Definition für SchILD-NRW-Schullogo
	 */
	public static readonly SCHULLOGO_SCHILD: ReportingBildDefinition = new ReportingBildDefinition("SCHULLOGO_SCHILD", 0, "SCHULLOGO_SCHILD", "SchILD-NRW-Schullogo", "Das Schullogo, welches aus der SchILD-NRW Datenbank übernommen wurde.", 45, 45, ArrayList.of());

	/**
	 * Definition für quadratisches Schullogo
	 */
	public static readonly SCHULLOGO_QUADRATISCH: ReportingBildDefinition = new ReportingBildDefinition("SCHULLOGO_QUADRATISCH", 1, "SCHULLOGO_QUADRATISCH", "Quadratisches Schullogo", "Das Schullogo in einer quadratischen Abmessung.", 40, 40, ArrayList.of());

	/**
	 * Definition für quadratisches Schulträgerlogo
	 */
	public static readonly SCHULTRAEGERLOGO_QUADRATISCH: ReportingBildDefinition = new ReportingBildDefinition("SCHULTRAEGERLOGO_QUADRATISCH", 2, "SCHULTRAEGERLOGO_QUADRATISCH", "Quadratisches Schulträgerlogo", "Das Schulträgerlogo in einer quadratischen Abmessung.", 40, 40, ArrayList.of());

	/**
	 * Definition für DIN5008-Briefkopf
	 */
	public static readonly DIN5008_BRIEFKOPF: ReportingBildDefinition = new ReportingBildDefinition("DIN5008_BRIEFKOPF", 3, "DIN5008_BRIEFKOPF", "DIN5008-Briefkopf", "Vollständiger Briefkopf für Anschreiben nach DIN5008", 190, 45, ArrayList.of());

	/**
	 * Die Kennung für die Persistierung in der DB. Diese Kennung muss eindeutig über alle Definitionen hinweg sein.
	 */
	private readonly kennung: string | null;

	/**
	 * Die Bezeichnung der Bilddefinition, z. B. zur Anzeige in Listen oder Auswahldialogen.
	 */
	private readonly bezeichnung: string | null;

	/**
	 * Die Beschreibung der Bilddefinition, z. B. zur Erklärung des Bildinhalts oder des Verwendungszwecks.
	 */
	private readonly beschreibung: string | null;

	/**
	 * Die geforderte Breite des Bildes in mm.
	 */
	private readonly breite: number;

	/**
	 * Die geforderte Höhe des Bildes in mm.
	 */
	private readonly hoehe: number;

	/**
	 * Die Schulformen, für die die Bilddefinition gültig ist. Eine leere Liste der Schulformen wird interpretiert als für alle Schulformen gültig.
	 */
	private readonly schulformen: List<Schulform>;

	/**
	 * Erzeugt eine neue Bilddefinition.
	 *
	 * @param kennung      Die Kennung für die Persistierung in der DB. Diese Kennung muss eindeutig über alle Definitionen hinweg sein.
	 * @param bezeichnung  Die Bezeichnung der Bilddefinition, z. B. zur Anzeige in Listen oder Auswahldialogen.
	 * @param beschreibung Die Beschreibung der Bilddefinition, z. B. zur Erklärung des Bildinhalts oder des Verwendungszwecks.
	 * @param breite       Die geforderte Breite des Bildes in mm.
	 * @param hoehe        Die geforderte Höhe des Bildes in mm.
	 * @param schulformen  Die Schulformen, für die die Bilddefinition gültig ist. Eine leere Liste der Schulformen wird interpretiert als für alle Schulformen gültig.
	 */
	private constructor(name: string, ordinal: number, kennung: string | null, bezeichnung: string | null, beschreibung: string | null, breite: number, hoehe: number, schulformen: List<Schulform> | null) {
		super(name, ordinal);
		ReportingBildDefinition.all_values_by_ordinal.push(this);
		ReportingBildDefinition.all_values_by_name.set(name, this);
		this.kennung = kennung;
		this.bezeichnung = bezeichnung;
		this.beschreibung = beschreibung;
		this.breite = breite;
		this.hoehe = hoehe;
		this.schulformen = (schulformen !== null) ? schulformen : new ArrayList();
	}

	/**
	 * Diese Methode ermittelt die Bilddefinition anhand der übergebenen Kennung.
	 *
	 * @param kennung   die Kennung der Bilddefinition für die DB.
	 *
	 * @return die Bilddefinition oder {@code null}, falls die Kennung ungültig ist
	 */
	public static getByKennung(kennung: string | null): ReportingBildDefinition | null {
		if (kennung === null) {
			return null;
		}
		for (const bildDefinition of ReportingBildDefinition.values()) {
			if (JavaObject.equalsTranspiler(bildDefinition.getKennung(), (kennung))) {
				return bildDefinition;
			}
		}
		return null;
	}

	/**
	 * Diese Methode ermittelt die Bilddefinitionen, die für die übergebene Schulform gültig sind.
	 *
	 * @param schulform  die Schulform, für die dei zulässigen Bilddefinitionen gesucht werden sollen.
	 *
	 * @return die Bilddefinitionen oder {@code null}, falls die Schulform nicht unterstützt wird.
	 */
	public static getBySchulform(schulform: Schulform | null): List<ReportingBildDefinition> {
		if (schulform === null) {
			return new ArrayList();
		}
		const bildDefinitionen: List<ReportingBildDefinition> | null = new ArrayList<ReportingBildDefinition>();
		for (const bildDefinition of ReportingBildDefinition.values()) {
			if (ReportingBildDefinition.isSchulformGueltig(schulform, bildDefinition.getSchulformen())) {
				bildDefinitionen.add(bildDefinition);
			}
		}
		return bildDefinitionen;
	}

	/**
	 * Diese Methode ermittelt die Bilddefinition, die für die übergebene Kennung und Schulform gültig ist.
	 * Wenn keine gültige Bilddefinition für die Kennung und Schulform gefunden wird, wird {@code Optional.empty()} zurückgegeben.
	 *
	 * @param kennung    die Kennung der Bilddefinition für die DB.
	 * @param schulform   die Schulform, für die dei zulässigen Bilddefinitionen gesucht werden sollen.
	 *
	 * @return die Bilddefinition oder {@code Optional.empty()}, falls die Kennung ungültig ist oder die Schulform nicht unterstützt wird. Wenn keine
	 * Schulform angegeben wird, wird die Bilddefinition für die Kennung ohne Schulform-Filterung zurückgegeben.
	 */
	public static getByKennungAndSchulform(kennung: string | null, schulform: Schulform | null): ReportingBildDefinition | null {
		const bildDefinition = ReportingBildDefinition.getByKennung(kennung);
		return ((bildDefinition !== null) && ReportingBildDefinition.isSchulformGueltig(schulform, bildDefinition.getSchulformen())) ? bildDefinition : null;
	}

	private static isSchulformGueltig(schulform: Schulform | null, schulformen: List<Schulform> | null): boolean {
		return (schulform === null) || (schulformen === null) || schulformen.isEmpty() || schulformen.contains(schulform);
	}

	/**
	 * Gibt die DB-Kennung der Bilddefinition zurück. Diese Kennung dient als eindeutiger technischer Schlüssel
	 * zur Persistierung und zum Wiederauffinden der Definition in der Datenbank.
	 *
	 * @return die DB-Kennung der Bilddefinition
	 */
	public getKennung(): string | null {
		return this.kennung;
	}

	/**
	 * Gibt die Bezeichnung der Bilddefinition zurück. Die Bezeichnung ist für die fachliche oder
	 * benutzerfreundliche Anzeige des Bildes vorgesehen.
	 *
	 * @return die Bezeichnung der Bilddefinition
	 */
	public getBezeichnung(): string | null {
		return this.bezeichnung;
	}

	/**
	 * Gibt die Beschreibung der Bilddefinition zurück. Die Beschreibung erläutert den fachlichen Zweck
	 * oder die Verwendung des Bildes innerhalb des Reportings.
	 *
	 * @return die Beschreibung der Bilddefinition
	 */
	public getBeschreibung(): string | null {
		return this.beschreibung;
	}

	/**
	 * Gibt die Breite des Bildes in Millimetern zurück.
	 *
	 * @return die Breite des Bildes in mm
	 */
	public getBreite(): number {
		return this.breite;
	}

	/**
	 * Gibt die Höhe des Bildes in Millimetern zurück.
	 *
	 * @return die Höhe des Bildes in mm
	 */
	public getHoehe(): number {
		return this.hoehe;
	}

	/**
	 * Gibt die Schulformen zurück, für die das Bild gültig ist. Ist die zurückgegebene Liste leer,
	 * so gilt die Bilddefinition für alle Schulformen.
	 *
	 * @return die Schulformen, für die die Bilddefinition gültig ist
	 */
	public getSchulformen(): List<Schulform> | null {
		return this.schulformen;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<ReportingBildDefinition> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): ReportingBildDefinition | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.ReportingBildDefinition';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.ReportingBildDefinition', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<ReportingBildDefinition>('de.svws_nrw.core.types.reporting.ReportingBildDefinition');

}

export function cast_de_svws_nrw_core_types_reporting_ReportingBildDefinition(obj: unknown): ReportingBildDefinition {
	return obj as ReportingBildDefinition;
}
