import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { HaltestellenListeManager } from "@ui/ui/manager/kataloge/HaltestellenListeManager";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";


/**
 * ModelProxy für Haltestellen
 */
export class HaltestellenModelProxy extends ModelProxy<Haltestelle> {


	private readonly manager: () => HaltestellenListeManager;

	/**
	 * ModelProxy für Haltestellen
	 *
	 * @param data 			Lambda für den Zugriff auf die Original-Daten
	 * @param manager 		Manager
	 * @param patch 		Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => Haltestelle,
		manager: () => HaltestellenListeManager,
		patch?: (data: Partial<Haltestelle>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof Haltestelle> = ['istSichtbar'];
		super({ data, patch, listOfAutopatchProps });
		this.manager = manager;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addBlockingValidator(new ValidatorStringIsUniqueInList(() => this.proxy, (data: Haltestelle) => data.id, (data: Haltestelle) => data.bezeichnung, () => this.manager().liste.list(), false), 'bezeichnung');
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bezeichnung, null, 30), 'bezeichnung');
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.bezeichnung), 'bezeichnung');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnung');
		// sortierung
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.sortierung), "sortierung");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}
}
