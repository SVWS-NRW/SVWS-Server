import type { VermerkartenListeManager } from "@ui";
import { ValidatorNumberRange } from "@ui";
import { ModelProxy, ValidatorInputRequired, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { VermerkartEintrag } from "@core";
import { StringPattern } from "../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";


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
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}

}
