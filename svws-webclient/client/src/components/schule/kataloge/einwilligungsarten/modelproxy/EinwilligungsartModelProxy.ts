import type { EinwilligungsschluesselKatalogEintrag } from "@core/asd/data/schule/EinwilligungsschluesselKatalogEintrag";
import { Einwilligungsschluessel } from "@core/asd/types/schule/Einwilligungsschluessel";
import type { Einwilligungsart } from "@core/core/data/schule/Einwilligungsart";
import { PersonTyp } from "@core/core/types/schule/PersonTyp";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { EinwilligungsartenListeManager } from "@ui/ui/manager/kataloge/EinwilligungsartenListeManager";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";
import { computed } from "vue";
import { ValidatorEinwilligungsartBezeichnungIsUniqueInList } from "./validation/ValidatorEinwilligungsartBezeichnungIsUniqueInList";


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
		super({ data, patch, listOfAutopatchProps });
		this.manager = manager;
		this.schuljahr = schuljahr;
		this.addValidatoren();
		this.validate();
	}


	private addValidatoren() {
		this.addBlockingValidator(new ValidatorEinwilligungsartBezeichnungIsUniqueInList(() => this.proxy.bezeichnung, this.manager), 'bezeichnung');
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bezeichnung, null, 250), 'bezeichnung');
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.bezeichnung), 'bezeichnung');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnung');
		// sortierung
		this.addBlockingValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), 'sortierung');
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, JavaInteger.MAX_VALUE), "sortierung");
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
