import type { HaltestellenListeManager } from "@ui";
import { ModelProxy, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { Haltestelle } from "@core";
import { StringPattern } from "../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";


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
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this.manager = manager;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addValidator(new ValidatorStringIsUniqueInList(() => this.proxy, (data: Haltestelle) => data.id, (data: Haltestelle) => data.bezeichnung, () => this.manager().liste.list(), false), 'bezeichnung');
		this.addValidator(new ValidatorStringLength(() => this.proxy.bezeichnung, null, 30), 'bezeichnung');
		this.addValidator(new ValidatorInputRequired(() => this.proxy.bezeichnung), 'bezeichnung');
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnung');
		// sortierung
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}
}
