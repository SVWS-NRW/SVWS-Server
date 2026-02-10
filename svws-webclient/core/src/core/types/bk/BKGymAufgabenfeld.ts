import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { JavaMap } from '../../../java/util/JavaMap';

export class BKGymAufgabenfeld extends JavaEnum<BKGymAufgabenfeld> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<BKGymAufgabenfeld> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, BKGymAufgabenfeld> = new Map<string, BKGymAufgabenfeld>();

	/**
	 * Aufgabenfeld sprachlich literarisch künstlerisch
	 */
	public static readonly SPRACHLICH_LITERARISCH_KUENSTLERISCH: BKGymAufgabenfeld = new BKGymAufgabenfeld("SPRACHLICH_LITERARISCH_KUENSTLERISCH", 0, "I", "Deutsch", "Englisch", "Französisch", "Griechisch", "Italienisch", "Kunst", "Literatur", "Musik", "Latein", "Niederländisch", "Russisch", "Spanisch");

	/**
	 * Aufgabenfeld gesellschaftswissenschaftlich
	 */
	public static readonly GESELLSCHAFTSWISSENSCHAFTLICH: BKGymAufgabenfeld = new BKGymAufgabenfeld("GESELLSCHAFTSWISSENSCHAFTLICH", 1, "II", "Arbeits- und Betriebslehre", "Außenhandel", "Betriebsorganisation", "Betriebswirtschaftslehre", "Betriebswirtschaftslehre mit Rechnungswesen", "Betriebswirtschaftslehre mit Rechnungswesen und Controlling", "Betriebswirtschaftslehre mit Rechnungswesen/Wirtschaftsrecht", "Didaktik und Methodik", "Erdkunde", "Erziehungswissenschaften", "Geschichte", "Gesellschaftslehre mit Geschichte", "Business Communication", "Marketing", "Organisationslehre", "Philosophie", "Politik/Geschichte", "Psychologie", "Rechtskunde", "Recht und Verwaltung", "Sozialpädagogik", "Soziologie", "Spezielle Betriebswirtschaftslehre", "Global Studies", "Volkswirtschaftslehre", "Volks- und Betriebswirtschaftslehre", "Wirtschaftsgeografie", "Wirtschaftslehre", "Wirtschaftslehre des Haushalts", "Wirtschaftsrecht");

	/**
	 * Aufgabenfeld mathematisch naturwissenschaftlich
	 */
	public static readonly MATHEMATISCH_NATURWISSENSCHAFTLICH: BKGymAufgabenfeld = new BKGymAufgabenfeld("MATHEMATISCH_NATURWISSENSCHAFTLICH", 2, "III", "Angewandte Informatik", "Anwendungsentwicklung", "Bautechnik", "Bauplanungstechnik,", "Betriebsinformatik", "Biochemie", "Biologie", "Biologietechnik", "Chemie", "Chemietechnik", "Datentechnik", "Datenverarbeitungstechnik", "Elektrotechnik", "Energietechnik", "Ernährungslehre", "Ernährung", "Gestaltungstechnik", "Gesundheit", "Grafik-Design", "Haushaltstechnik", "Holztechnik", "Informatik", "Ingenieurwissenschaften", "Konstruktions- und Fertigungstechnik", "Maschinenbautechnik", "Maschinentechnik", "Mathematik", "Nachrichtentechnik", "Physik", "Physikalische Chemie", "Physiktechnik", "Softwareentwicklung", "Technische Informatik", "Technische Kommunikation", "Technisches Zeichnen", "Textil- und Bekleidungstechnik", "Umweltschutztechnik", "Umwelttechnik", "Werkstofftechnik", "Wirtschaftsinformatik", "Wirtschaftsinformatik/Organisationslehre");

	/**
	 * Fächer ohne Aufgabenfeld
	 */
	public static readonly OHNE_AUFGABENFELD: BKGymAufgabenfeld = new BKGymAufgabenfeld("OHNE_AUFGABENFELD", 3, "0", "Religionslehre", "Sport", "Sport/Gesundheitsförderung");

	/**
	 * Naturwissenschaften
	 */
	public static readonly NATURWISSENSCHAFTEN: BKGymAufgabenfeld = new BKGymAufgabenfeld("NATURWISSENSCHAFTEN", 4, "NW", "Biologie", "Chemie", "Physik");

	/**
	 * Eine Map, welche dem zulässigen Fach sein Aufgabenfeld zuordnet.
	 */
	private static readonly _mapAufgabenfeldByFach: JavaMap<string, BKGymAufgabenfeld> = new HashMap<string, BKGymAufgabenfeld>();

	/**
	 * Eine Map, welche dem Kuerzel sein Aufgabenfeld zuordnet.
	 */
	private static readonly _mapAufgabenfeldByKuerzel: JavaMap<string, BKGymAufgabenfeld> = new HashMap<string, BKGymAufgabenfeld>();

	/**
	 * Das Kürzel für das Aufgabenfeld
	 */
	private readonly kuerzel: string;

	/**
	 * Eine Liste der Fächer dieses Aufgabenfeldes
	 */
	private readonly fachbezeichnungen: ArrayList<string> = new ArrayList<string>();

	/**
	 * Erstellt einen neues Aufgabenfeld mit den übergebenen Fächern
	 *
	 * @param kuerzel             Kürzel des Aufgabenfeldes
	 * @param fachbezeichnungen   die Fächer des Aufgabenfeldes
	 */
	private constructor(name: string, ordinal: number, kuerzel: string, ...fachbezeichnungen: Array<string>) {
		super(name, ordinal);
		BKGymAufgabenfeld.all_values_by_ordinal.push(this);
		BKGymAufgabenfeld.all_values_by_name.set(name, this);
		this.kuerzel = kuerzel;
		for (const fach of fachbezeichnungen)
			this.fachbezeichnungen.add(fach);
	}

	/**
	 * Initialisiert die Map von den Fächern auf das zugehörige Aufgabenfeld, wenn es noch nicht geschehen ist.
	 */
	private static initMapAufgabenfeldByFach(): void {
		if (BKGymAufgabenfeld._mapAufgabenfeldByFach.size() === 0)
			for (const feld of BKGymAufgabenfeld.values())
				for (const fachbezeichnung of feld.fachbezeichnungen)
					BKGymAufgabenfeld._mapAufgabenfeldByFach.put(fachbezeichnung, feld);
	}

	/**
	 * Initialisiert die Map von den Kürzeln auf das zugehörige Aufgabenfeld, wenn es noch nicht geschehen ist.
	 */
	private static initMapAufgabenfeldByKuerzel(): void {
		if (BKGymAufgabenfeld._mapAufgabenfeldByKuerzel.size() === 0)
			for (const feld of BKGymAufgabenfeld.values())
				BKGymAufgabenfeld._mapAufgabenfeldByKuerzel.put(feld.kuerzel, feld);
	}

	/**
	 * Gibt die Liste der Fächer des Aufgabenfeldes zurück.
	 *
	 * @return die Liste der Fächer des Aufgabenfeldes
	 */
	public getFaecher(): List<string> {
		return this.fachbezeichnungen;
	}

	/**
	 * Prüft, ob das Fach mit der übergebenen Fachbezeichnung zu diesem Aufgabenfeld gehört.
	 *
	 * @param fachbezeichnung   die Fachbezeichnung
	 *
	 * @return true, falls das Fach zu dem Aufgabenfeld gehört, sonst false
	 */
	public hatFachbezeichnung(fachbezeichnung: string | null): boolean {
		BKGymAufgabenfeld.initMapAufgabenfeldByFach();
		if (fachbezeichnung === null)
			return false;
		return BKGymAufgabenfeld._mapAufgabenfeldByFach.get(fachbezeichnung) as unknown === this as unknown;
	}

	/**
	 * Liefert zu einem Kürzel das zugehörige Aufgabenfeld oder null
	 *
	 * @param kuerzel   das Kürzel eines Aufgabenfeldes
	 *
	 * @return das Aufgabenfeld oder null
	 */
	public static getAufgabenfeldFromKuerzel(kuerzel: string): BKGymAufgabenfeld | null {
		BKGymAufgabenfeld.initMapAufgabenfeldByKuerzel();
		return BKGymAufgabenfeld._mapAufgabenfeldByKuerzel.get(kuerzel);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<BKGymAufgabenfeld> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): BKGymAufgabenfeld | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.bk.BKGymAufgabenfeld';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.bk.BKGymAufgabenfeld', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<BKGymAufgabenfeld>('de.svws_nrw.core.types.bk.BKGymAufgabenfeld');

}

export function cast_de_svws_nrw_core_types_bk_BKGymAufgabenfeld(obj: unknown): BKGymAufgabenfeld {
	return obj as BKGymAufgabenfeld;
}
