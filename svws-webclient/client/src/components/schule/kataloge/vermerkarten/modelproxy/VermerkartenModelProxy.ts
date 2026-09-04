import type { VermerkartEintrag } from "@core/core/data/schule/VermerkartEintrag";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { VermerkartenListeManager } from "@ui/ui/manager/kataloge/VermerkartenListeManager";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";


/**
 * ModelProxy für Vermerkarten
 */
export class VermerkartenModelProxy extends ModelProxy<VermerkartEintrag> {

	private readonly manager: () => VermerkartenListeManager;

	/**
	 * ModelProxy für Vermerkarten
	 *
	 * @param data 			Lambda für den Zugriff auf die Original-Daten
	 * @param manager 		Manager
	 * @param patch 		Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => VermerkartEintrag,
		manager: () => VermerkartenListeManager,
		patch?: (data: Partial<VermerkartEintrag>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof VermerkartEintrag> = ['istSichtbar'];
		super({ data, patch, listOfAutopatchProps });

		this.manager = manager;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addBlockingValidator(new ValidatorStringIsUniqueInList(() => this.proxy, (data: VermerkartEintrag) => data.id, (data: VermerkartEintrag) => data.bezeichnung, () => this.manager().liste.list(), false), 'bezeichnung');
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bezeichnung, null, 30), 'bezeichnung');
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.bezeichnung), 'bezeichnung');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnung');
		// sortierung
		this.addBlockingValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), "sortierung");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}

}
