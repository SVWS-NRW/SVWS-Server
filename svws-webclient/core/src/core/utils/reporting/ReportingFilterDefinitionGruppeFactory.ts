import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingFilterDefinitionFactory } from '../../../core/utils/reporting/ReportingFilterDefinitionFactory';
import { ReportingFilterDefinition } from '../../../core/data/reporting/ReportingFilterDefinition';
import { ReportingFilterDefinitionGruppe } from '../../../core/data/reporting/ReportingFilterDefinitionGruppe';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Arrays } from '../../../java/util/Arrays';

export class ReportingFilterDefinitionGruppeFactory extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Erzeugt eine {@link ReportingFilterDefinitionGruppe} mit den übergebenen Filterdefinitionen.
	 *
	 * @param bezeichnung    die Bezeichnung der Gruppe (UI-Text)
	 * @param typ            der Typname des zu filternden Reporting-Datentyps (z. B. {@code "ReportingSchueler"})
	 * @param uiIstSichtbar  gibt an, ob die Gruppe in der UI sichtbar sein soll
	 * @param definitionen   die der Gruppe zugeordneten, aktiven Filterdefinitionen
	 *
	 * @return die neu erzeugte Gruppe mit gesetzten {@code filterDefinitionen}
	 */
	public static gruppe(bezeichnung: string, typ: string, uiIstSichtbar: boolean, ...definitionen: Array<ReportingFilterDefinition>): ReportingFilterDefinitionGruppe {
		const gruppe: ReportingFilterDefinitionGruppe | null = new ReportingFilterDefinitionGruppe();
		gruppe.bezeichnung = bezeichnung;
		gruppe.typ = typ;
		gruppe.uiIstSichtbar = uiIstSichtbar;
		gruppe.filterDefinitionen.addAll(Arrays.asList(...definitionen));
		return gruppe;
	}

	/**
	 * Erzeugt eine {@link ReportingFilterDefinitionGruppe} aus einer Liste von IDs. Die IDs werden in einen
	 * einzelnen {@code IN}-Filtereintrag auf dem Attribut {@code "id"} überführt und in eine entsprechende
	 * Filterdefinition mit AND-Verknüpfung verpackt.
	 *
	 * @param bezeichnung    die Bezeichnung der Gruppe (UI-Text)
	 * @param typ            der Typname des zu filternden Reporting-Datentyps (z. B. {@code "ReportingSchueler"})
	 * @param uiIstSichtbar  gibt an, ob die Gruppe in der UI sichtbar sein soll
	 * @param idsListe       die IDs, die in einem {@code IN}-Filter auf {@code id} verwendet werden
	 *
	 * @return die neu erzeugte Gruppe mit einer einzelnen Filterdefinition für {@code id IN (idsListe)}
	 */
	public static gruppeAusIds(bezeichnung: string, typ: string, uiIstSichtbar: boolean, idsListe: List<number>): ReportingFilterDefinitionGruppe {
		const werte: Array<string> = Array(idsListe.size()).fill(null);
		for (let i: number = 0; i < idsListe.size(); i++) {
			werte[i] = "" + idsListe.get(i);
		}
		const definition: ReportingFilterDefinition = ReportingFilterDefinitionFactory.definition(bezeichnung, typ, ReportingFilterDefinitionFactory.and(ReportingFilterDefinitionFactory.in("id", ...werte)));
		return ReportingFilterDefinitionGruppeFactory.gruppe(bezeichnung, typ, uiIstSichtbar, definition);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.reporting.ReportingFilterDefinitionGruppeFactory';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.reporting.ReportingFilterDefinitionGruppeFactory'].includes(name);
	}

	public static readonly class = new Class<ReportingFilterDefinitionGruppeFactory>('de.svws_nrw.core.utils.reporting.ReportingFilterDefinitionGruppeFactory');

}

export function cast_de_svws_nrw_core_utils_reporting_ReportingFilterDefinitionGruppeFactory(obj: unknown): ReportingFilterDefinitionGruppeFactory {
	return obj as ReportingFilterDefinitionGruppeFactory;
}
