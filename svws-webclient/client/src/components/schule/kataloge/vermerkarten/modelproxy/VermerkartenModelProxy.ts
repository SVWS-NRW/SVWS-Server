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
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });

		this.manager = manager;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addValidator(new ValidatorStringIsUniqueInList(() => this.proxy, (data: VermerkartEintrag) => data.id, (data: VermerkartEintrag) => data.bezeichnung, () => this.manager().liste.list(), false), 'bezeichnung');
		this.addValidator(new ValidatorStringLength(() => this.proxy.bezeichnung, null, 30), 'bezeichnung');
		this.addValidator(new ValidatorInputRequired(() => this.proxy.bezeichnung), 'bezeichnung');
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnung');
		// sortierung
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}
}
