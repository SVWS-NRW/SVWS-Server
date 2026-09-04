import type { Lernplattform } from "@core/core/data/schule/Lernplattform";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { LernplattformListeManager } from "@ui/ui/manager/kataloge/LernplattformListeManager";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";


/**
 * ModelProxy für Lernplattformen
 */
export class LernplattformenModelProxy extends ModelProxy<Lernplattform> {

	private readonly manager: () => LernplattformListeManager;

	/**
	 * ModelProxy für Lernplattformen
	 *
	 * @param data 			Lambda für den Zugriff auf die Original-Daten
	 * @param manager 		Manager
	 * @param patch 		Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => Lernplattform,
		manager: () => LernplattformListeManager,
		patch?: (data: Partial<Lernplattform>) => Promise<boolean>
	) {
		super({ data, patch });
		this.manager = manager;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addBlockingValidator(new ValidatorStringIsUniqueInList(() => this.proxy, (data: Lernplattform) => data.id, (data: Lernplattform) => data.bezeichnung, () => this.manager().liste.list(), false), 'bezeichnung');
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bezeichnung, null, 255), 'bezeichnung');
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.bezeichnung), 'bezeichnung');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnung');
	}
}
