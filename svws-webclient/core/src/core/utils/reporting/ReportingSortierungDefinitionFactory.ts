import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { Class, cast_java_lang_Class } from '../../../java/lang/Class';
import { Arrays } from '../../../java/util/Arrays';
import { ReportingSortierungDefinition } from '../../../core/data/reporting/ReportingSortierungDefinition';

export class ReportingSortierungDefinitionFactory extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Erzeugt eine {@link ReportingSortierungDefinition}.
	 *
	 * @param bezeichnung                die Bezeichnung der Sortierdefinition (UI-Text)
	 * @param typ                        der Typname des zu sortierenden Reporting-Datentyps (z. B. {@code "ReportingSchueler"})
	 * @param verwendeStandardsortierung {@code true}, falls die Standardsortierung für diesen Typ verwendet werden soll; sonst {@code false}
	 * @param attribute                  die Sortierattribute für eine benutzerdefinierte Sortierung (z. B. {@code "Nachname"} oder {@code "-Geburtsdatum"} für absteigend)
	 *
	 * @return die neu erzeugte Sortierdefinition
	 */
	public static definition(bezeichnung: string, typ: string, verwendeStandardsortierung: boolean, attribute: List<string>) : ReportingSortierungDefinition;

	/**
	 * Erzeugt eine {@link ReportingSortierungDefinition}.
	 *
	 * @param bezeichnung                die Bezeichnung der Sortierdefinition (UI-Text)
	 * @param typ                        die Klasse des zu sortierenden Reporting-Datentyps
	 * @param verwendeStandardsortierung {@code true}, falls die Standardsortierung für diesen Typ verwendet werden soll; sonst {@code false}
	 * @param attribute                  die Sortierattribute für eine benutzerdefinierte Sortierung (z. B. {@code "Nachname"} oder {@code "-Geburtsdatum"} für absteigend)
	 *
	 * @return die neu erzeugte Sortierdefinition
	 */
	public static definition(bezeichnung: string, typ: Class<any>, verwendeStandardsortierung: boolean, attribute: List<string>) : ReportingSortierungDefinition;

	/**
	 * Implementation for method overloads of 'definition'
	 */
	public static definition(__param0: string, __param1: Class<any> | string, __param2: boolean, __param3: List<string>): ReportingSortierungDefinition {
		if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && typeof __param2 === "boolean") && ((__param3 !== undefined) && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('java.util.List'))) || (__param3 === null))) {
			const bezeichnung: string = __param0;
			const typ: string = __param1;
			const verwendeStandardsortierung: boolean = __param2 as boolean;
			const attribute: List<string> = cast_java_util_List(__param3);
			const d: ReportingSortierungDefinition | null = new ReportingSortierungDefinition();
			d.bezeichnung = bezeichnung;
			d.typ = typ;
			d.verwendeStandardsortierung = verwendeStandardsortierung;
			d.attribute = new ArrayList(attribute);
			return d;
		} else if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('java.lang.Class'))) || (__param1 === null)) && ((__param2 !== undefined) && typeof __param2 === "boolean") && ((__param3 !== undefined) && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('java.util.List'))) || (__param3 === null))) {
			const bezeichnung: string = __param0;
			const typ: Class<any> = cast_java_lang_Class(__param1);
			const verwendeStandardsortierung: boolean = __param2 as boolean;
			const attribute: List<string> = cast_java_util_List(__param3);
			return ReportingSortierungDefinitionFactory.definition(bezeichnung, typ.getSimpleName(), verwendeStandardsortierung, attribute);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Erzeugt eine {@link ReportingSortierungDefinition}, die die Standardsortierung des Typs verwendet.
	 *
	 * @param bezeichnung    die Bezeichnung der Sortierdefinition (UI-Text)
	 * @param typ            die Klasse des zu sortierenden Reporting-Datentyps
	 *
	 * @return die neu erzeugte Sortierdefinition mit aktivierter Standardsortierung
	 */
	public static standard(bezeichnung: string, typ: string) : ReportingSortierungDefinition;

	/**
	 * Erzeugt eine {@link ReportingSortierungDefinition}, die die Standardsortierung des Typs verwendet.
	 *
	 * @param bezeichnung    die Bezeichnung der Sortierdefinition (UI-Text)
	 * @param typ            die Klasse des zu sortierenden Reporting-Datentyps
	 *
	 * @return die neu erzeugte Sortierdefinition mit aktivierter Standardsortierung
	 */
	public static standard(bezeichnung: string, typ: Class<any>) : ReportingSortierungDefinition;

	/**
	 * Implementation for method overloads of 'standard'
	 */
	public static standard(__param0: string, __param1: Class<any> | string): ReportingSortierungDefinition {
		if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && (typeof __param1 === "string"))) {
			const bezeichnung: string = __param0;
			const typ: string = __param1;
			return ReportingSortierungDefinitionFactory.definition(bezeichnung, typ, true, ArrayList.of());
		} else if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('java.lang.Class'))) || (__param1 === null))) {
			const bezeichnung: string = __param0;
			const typ: Class<any> = cast_java_lang_Class(__param1);
			return ReportingSortierungDefinitionFactory.definition(bezeichnung, typ, true, ArrayList.of());
		} else throw new Error('invalid method overload');
	}

	/**
	 * Erzeugt eine {@link ReportingSortierungDefinition} mit benutzerdefinierten Sortierattributen.
	 *
	 * @param bezeichnung    die Bezeichnung der Sortierdefinition (UI-Text)
	 * @param typ            der Typname des zu sortierenden Reporting-Datentyps (z. B. {@code "ReportingSchueler"})
	 * @param typBezeichnung die Bezeichnung des Typs, die auch in der UI verwendet werden kann, z. B. 'Schülersortierung'
	 * @param attribute      die Sortierattribute (z. B. {@code "Nachname"} oder {@code "-Geburtsdatum"} für absteigend)
	 *
	 * @return die neu erzeugte Sortierdefinition mit benutzerdefinierten Attributen
	 */
	public static benutzerdefiniert(bezeichnung: string, typ: string, typBezeichnung: string, attribute: List<string>): ReportingSortierungDefinition {
		return ReportingSortierungDefinitionFactory.definition(bezeichnung, typ, false, attribute);
	}

	/**
	 * Methode zum Erzeugen der Liste von Sortierdefinitionen.
	 *
	 * @param definitionen die anzubietenden Sortierdefinitionen
	 *
	 * @return eine veränderbare Liste mit den übergebenen Sortierdefinitionen
	 */
	public static definitionen(...definitionen: Array<ReportingSortierungDefinition>): List<ReportingSortierungDefinition> {
		return new ArrayList<ReportingSortierungDefinition>(Arrays.asList(...definitionen));
	}

	/**
	 * Liefert eine leere, veränderbare Liste für {@code sortierungDefinitionenOptionen}.
	 *
	 * @return leere Liste von Sortierdefinitionen
	 */
	public static keineDefinitionen(): List<ReportingSortierungDefinition> {
		return new ArrayList<ReportingSortierungDefinition>();
	}

	/**
	 * Normalisiert ein Sortierattribut auf aufsteigende Sortierung (entfernt ggf. ein führendes {@code '-'}).
	 *
	 * @param attribut der Attributname (mit oder ohne führendes {@code '-'})
	 *
	 * @return der Attributname für aufsteigende Sortierung (ohne führendes {@code '-'})
	 */
	public static asc(attribut: string): string {
		return attribut.startsWith("-") ? attribut.substring(1) : attribut;
	}

	/**
	 * Normalisiert ein Sortierattribut auf absteigende Sortierung (stellt ein führendes {@code '-'} sicher).
	 *
	 * @param attribut der Attributname (mit oder ohne führendes {@code '-'})
	 *
	 * @return der Attributname für absteigende Sortierung (mit führendem {@code '-'})
	 */
	public static desc(attribut: string): string {
		const clean: string | null = attribut.startsWith("-") ? attribut.substring(1) : attribut;
		return "-" + clean;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.reporting.ReportingSortierungDefinitionFactory';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.reporting.ReportingSortierungDefinitionFactory'].includes(name);
	}

	public static readonly class = new Class<ReportingSortierungDefinitionFactory>('de.svws_nrw.core.utils.reporting.ReportingSortierungDefinitionFactory');

}

export function cast_de_svws_nrw_core_utils_reporting_ReportingSortierungDefinitionFactory(obj: unknown): ReportingSortierungDefinitionFactory {
	return obj as ReportingSortierungDefinitionFactory;
}
