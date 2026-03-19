import type { LeitungsfunktionenListeManager } from "@ui";
import { ModelProxy, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { Leitungsfunktion } from "@core";
import { StringPattern } from "../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";


/**
 * ModelProxy für Leitungsfunktionen
 */
export class LeitungsfunktionenModelProxy extends ModelProxy<Leitungsfunktion> {

	private readonly manager: () => LeitungsfunktionenListeManager;

	/**
	 * ModelProxy für Leitungsfunktionen
	 *
	 * @param data 			Lambda für den Zugriff auf die Original-Daten
	 * @param manager 		Manager
	 * @param patch 		Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => Leitungsfunktion,
		manager: () => LeitungsfunktionenListeManager,
		patch?: (data: Partial<Leitungsfunktion>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof Leitungsfunktion> = ['istSichtbar'];
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this.manager = manager;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addValidator(new ValidatorStringIsUniqueInList(() => this.proxy, (data: Leitungsfunktion) => data.id, (data: Leitungsfunktion) => data.bezeichnung, () => this.manager().liste.list(), false), 'bezeichnung');
		this.addValidator(new ValidatorStringLength(() => this.proxy.bezeichnung, null, 50), 'bezeichnung');
		this.addValidator(new ValidatorInputRequired(() => this.proxy.bezeichnung), 'bezeichnung');
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnung');
		// sortierung
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}


}
