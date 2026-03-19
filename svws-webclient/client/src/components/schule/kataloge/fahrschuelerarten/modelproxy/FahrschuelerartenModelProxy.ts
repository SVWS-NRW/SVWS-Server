import type { FahrschuelerartenListeManager } from "@ui";
import { ModelProxy, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { Fahrschuelerart } from "@core";
import { StringPattern } from "../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";


/**
 * ModelProxy für Fahrschülerarten
 */
export class FahrschuelerartenModelProxy extends ModelProxy<Fahrschuelerart> {

	private readonly manager: () => FahrschuelerartenListeManager;

	/**
	 * ModelProxy für Fahrschülerarten
	 *
	 * @param data 			Lambda für den Zugriff auf die Original-Daten
	 * @param manager 		Manager
	 * @param patch 		Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => Fahrschuelerart,
		manager: () => FahrschuelerartenListeManager,
		patch?: (data: Partial<Fahrschuelerart>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof Fahrschuelerart> = ['istSichtbar'];
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this.manager = manager;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addValidator(new ValidatorStringIsUniqueInList(() => this.proxy, (data: Fahrschuelerart) => data.id, (data: Fahrschuelerart) => data.bezeichnung, () => this.manager().liste.list(), false), 'bezeichnung');
		this.addValidator(new ValidatorStringLength(() => this.proxy.bezeichnung, null, 30), 'bezeichnung');
		this.addValidator(new ValidatorInputRequired(() => this.proxy.bezeichnung), 'bezeichnung');
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnung');
		// sortierung
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}
}
