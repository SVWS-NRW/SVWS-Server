import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingFilterDefinition } from '../../../core/data/reporting/ReportingFilterDefinition';
import { ReportingFilterVerknuepfung } from '../../../core/types/reporting/ReportingFilterVerknuepfung';
import { ReportingFilterEintrag } from '../../../core/data/reporting/ReportingFilterEintrag';
import { ReportingFilterOperation } from '../../../core/types/reporting/ReportingFilterOperation';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Arrays } from '../../../java/util/Arrays';
import { ReportingFilterKriterium } from '../../../core/data/reporting/ReportingFilterKriterium';

export class ReportingFilterDefinitionFactory extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Erzeugt eine {@link ReportingFilterDefinition} für einen Reporting-Datentyp.
	 *
	 * @param bezeichnung    die Bezeichnung der Filterdefinition (UI-Text)
	 * @param typ            der Typname des zu filternden Reporting-Datentyps (z. B. {@code "ReportingFach"})
	 * @param kriterien      die Filterkriterien (Top-Level), die auf den Datentyp angewendet werden sollen
	 *
	 * @return die neu erzeugte Filterdefinition
	 */
	public static definition(bezeichnung: string, typ: string, ...kriterien: Array<ReportingFilterKriterium>): ReportingFilterDefinition {
		const d: ReportingFilterDefinition | null = new ReportingFilterDefinition();
		d.bezeichnung = bezeichnung;
		d.typ = typ;
		d.kriterien = new ArrayList(Arrays.asList(...kriterien));
		return d;
	}

	/**
	 * Methode zum Erzeugen der Liste von Filterdefinitionen
	 *
	 * @param definitionen die anzubietenden Filterdefinitionen
	 *
	 * @return eine veränderbare Liste mit den übergebenen Filterdefinitionen
	 */
	public static definitionen(...definitionen: Array<ReportingFilterDefinition>): List<ReportingFilterDefinition> {
		return new ArrayList<ReportingFilterDefinition>(Arrays.asList(...definitionen));
	}

	/**
	 * Liefert eine leere, veränderbare Liste für {@code filterdefinitionenOptionen}.
	 *
	 * @return leere Liste von Filterdefinitionen
	 */
	public static keineDefinitionen(): List<ReportingFilterDefinition> {
		return new ArrayList<ReportingFilterDefinition>();
	}

	/**
	 * Erzeugt ein Filterkriterium, das die übergebenen Einträge mit {@code AND} verknüpft.
	 *
	 * @param eintraege die zu verknüpfenden Filtereinträge
	 *
	 * @return ein {@link ReportingFilterKriterium} mit {@code AND}-Verknüpfung
	 */
	public static and(...eintraege: Array<ReportingFilterEintrag>): ReportingFilterKriterium {
		return ReportingFilterDefinitionFactory.kriterium(ReportingFilterVerknuepfung.AND, Arrays.asList(...eintraege), ArrayList.of(), false);
	}

	/**
	 * Erzeugt ein Filterkriterium, das die übergebenen Einträge mit {@code OR} verknüpft.
	 *
	 * @param eintraege die zu verknüpfenden Filtereinträge
	 *
	 * @return ein {@link ReportingFilterKriterium} mit {@code OR}-Verknüpfung
	 */
	public static or(...eintraege: Array<ReportingFilterEintrag>): ReportingFilterKriterium {
		return ReportingFilterDefinitionFactory.kriterium(ReportingFilterVerknuepfung.OR, Arrays.asList(...eintraege), ArrayList.of(), false);
	}

	/**
	 * Erzeugt ein Filterkriterium, das die übergebenen Unterkriterien mit {@code AND} verknüpft.
	 * (Dabei enthält das Kriterium selbst keine direkten Einträge.)
	 *
	 * @param unterkriterien die zu verknüpfenden Unterkriterien
	 *
	 * @return ein {@link ReportingFilterKriterium} mit {@code AND}-Verknüpfung über Unterkriterien
	 */
	public static andMany(...unterkriterien: Array<ReportingFilterKriterium>): ReportingFilterKriterium {
		return ReportingFilterDefinitionFactory.kriterium(ReportingFilterVerknuepfung.AND, ArrayList.of(), Arrays.asList(...unterkriterien), false);
	}

	/**
	 * Erzeugt ein Filterkriterium, das die übergebenen Unterkriterien mit {@code OR} verknüpft.
	 * (Dabei enthält das Kriterium selbst keine direkten Einträge.)
	 *
	 * @param unterkriterien die zu verknüpfenden Unterkriterien
	 *
	 * @return ein {@link ReportingFilterKriterium} mit {@code OR}-Verknüpfung über Unterkriterien
	 */
	public static orMany(...unterkriterien: Array<ReportingFilterKriterium>): ReportingFilterKriterium {
		return ReportingFilterDefinitionFactory.kriterium(ReportingFilterVerknuepfung.OR, ArrayList.of(), Arrays.asList(...unterkriterien), false);
	}

	/**
	 * Negiert das übergebene Kriterium (entspricht einem logischen {@code NOT} der gesamten Gruppe).
	 *
	 * @param kriterium
	 *        das zu negierende Kriterium
	 *
	 * @return das gleiche Kriterium-Objekt (fluent), mit gesetzter Negation
	 */
	public static not(kriterium: ReportingFilterKriterium): ReportingFilterKriterium {
		kriterium.nicht = true;
		return kriterium;
	}

	/**
	 * Erzeugt ein Filterkriterium mit frei konfigurierbarer Verknüpfung, Einträgen und Unterkriterien.
	 *
	 * @param verknuepfung   logische Verknüpfung der enthaltenen Einträge und Unterkriterien
	 * @param eintraege      Liste der konkreten Filtereinträge (Attributvergleiche)
	 * @param unterkriterien Liste der Unterkriterien (verschachtelte Gruppen)
	 * @param nicht          {@code true}, falls das Gesamtergebnis dieser Gruppe negiert werden soll
	 *
	 * @return ein neu erzeugtes {@link ReportingFilterKriterium}
	 */
	public static kriterium(verknuepfung: ReportingFilterVerknuepfung, eintraege: List<ReportingFilterEintrag>, unterkriterien: List<ReportingFilterKriterium>, nicht: boolean): ReportingFilterKriterium {
		const k: ReportingFilterKriterium | null = new ReportingFilterKriterium();
		k.verknuepfung = verknuepfung.getId();
		k.eintraege = new ArrayList(eintraege);
		k.unterkriterien = new ArrayList(unterkriterien);
		k.nicht = nicht;
		return k;
	}

	/**
	 * Erzeugt einen Filtereintrag für {@code attribut == wert}.
	 *
	 * @param attribut der Attributname, auf den gefiltert wird
	 * @param wert     der Vergleichswert
	 *
	 * @return ein {@link ReportingFilterEintrag} mit Operation {@code EQUAL}
	 */
	public static eq(attribut: string, wert: string): ReportingFilterEintrag {
		return ReportingFilterDefinitionFactory.eintrag(attribut, ReportingFilterOperation.EQUAL, wert);
	}

	/**
	 * Erzeugt einen Filtereintrag für {@code attribut != wert}.
	 *
	 * @param attribut der Attributname, auf den gefiltert wird
	 * @param wert     der Vergleichswert
	 *
	 * @return ein {@link ReportingFilterEintrag} mit Operation {@code NOT_EQUAL}
	 */
	public static notEq(attribut: string, wert: string): ReportingFilterEintrag {
		return ReportingFilterDefinitionFactory.eintrag(attribut, ReportingFilterOperation.NOT_EQUAL, wert);
	}

	/**
	 * Erzeugt einen Filtereintrag für {@code attribut IN (werte...)}.
	 *
	 * @param attribut der Attributname, auf den gefiltert wird
	 * @param werte    die Vergleichswerte
	 *
	 * @return ein {@link ReportingFilterEintrag} mit Operation {@code IN}
	 */
	public static in(attribut: string, ...werte: Array<string>): ReportingFilterEintrag {
		return ReportingFilterDefinitionFactory.eintrag(attribut, ReportingFilterOperation.IN, ...werte);
	}

	/**
	 * Erzeugt einen Filtereintrag für ein Attribut mit frei wählbarer Operation und Werten.
	 *
	 * @param attribut  der Attributname, auf den gefiltert wird
	 * @param operation die anzuwendende Filteroperation
	 * @param werte     die Vergleichswerte (als Liste, um z. B. {@code IN} zu unterstützen)
	 *
	 * @return ein {@link ReportingFilterEintrag}
	 */
	public static eintrag(attribut: string, operation: ReportingFilterOperation, ...werte: Array<string>): ReportingFilterEintrag {
		const eintrag: ReportingFilterEintrag | null = new ReportingFilterEintrag();
		eintrag.attribut = attribut;
		eintrag.operation = operation.getId();
		eintrag.werte = new ArrayList(Arrays.asList(...werte));
		return eintrag;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.reporting.ReportingFilterDefinitionFactory';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.reporting.ReportingFilterDefinitionFactory'].includes(name);
	}

	public static readonly class = new Class<ReportingFilterDefinitionFactory>('de.svws_nrw.core.utils.reporting.ReportingFilterDefinitionFactory');

}

export function cast_de_svws_nrw_core_utils_reporting_ReportingFilterDefinitionFactory(obj: unknown): ReportingFilterDefinitionFactory {
	return obj as ReportingFilterDefinitionFactory;
}
