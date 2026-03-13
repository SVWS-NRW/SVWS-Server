import type { EinwilligungsartenListeManager } from "@ui";
import { ModelProxy, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { Einwilligungsart, EinwilligungsschluesselKatalogEintrag } from "@core";
import { Einwilligungsschluessel, PersonTyp } from "@core";
import { ValidatorEinwilligungsartBezeichnungIsUniqueInList } from "~/components/schule/kataloge/einwilligungsarten/modelproxy/validation/ValidatorEinwilligungsartBezeichnungIsUniqueInList";
import { computed } from "vue";
import { StringPattern } from "../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";


/**
 * ModelProxy für Einwilligungsarten
 */
export class EinwilligungsartModelProxy extends ModelProxy<Einwilligungsart> {

	private readonly schuljahr: number;
	private readonly manager: () => EinwilligungsartenListeManager;

	/**
	 * ModelProxy für Einwilligungsarten
	 *
	 * @param data 			Lambda für den Zugriff auf die Original-Daten
	 * @param manager 		Manager
	 * @param schuljahr 	Das aktuelle Schuljahr
	 * @param patch 		Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => Einwilligungsart,
		manager: () => EinwilligungsartenListeManager,
		schuljahr: number,
		patch?: (data: Partial<Einwilligungsart>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof Einwilligungsart> = ['schluessel', 'istSichtbar'];
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this.manager = manager;
		this.schuljahr = schuljahr;
		this.addValidatoren();
		this.validate();
	}


	private addValidatoren() {
		this.addValidator(new ValidatorEinwilligungsartBezeichnungIsUniqueInList(() => this.proxy.bezeichnung, this.manager), 'bezeichnung');
		this.addValidator(new ValidatorStringLength(() => this.proxy.bezeichnung, null, 250), 'bezeichnung');
		this.addValidator(new ValidatorInputRequired(() => this.proxy.bezeichnung), 'bezeichnung');
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnung');
		// sortierung
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}

	einwilligungsschluessel = computed<EinwilligungsschluesselKatalogEintrag | null>({
		get: () => Einwilligungsschluessel.data().getEintragBySchuljahrUndSchluessel(this.schuljahr, this.proxy.schluessel ?? ''),
		set: (v: EinwilligungsschluesselKatalogEintrag | null) => this.proxy.schluessel = v?.schluessel ?? null,
	});

	personTyp = computed<PersonTyp>({
		get: () => PersonTyp.getByID(this.proxy.idPersonTyp) ?? PersonTyp.SCHUELER,
		set: (value: PersonTyp) => this.proxy.idPersonTyp = value.id,
	});
}
