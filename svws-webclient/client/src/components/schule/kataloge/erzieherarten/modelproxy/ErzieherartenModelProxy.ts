import type { ErzieherartListeManager } from "@ui";
import { ModelProxy, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { Erzieherart } from "@core";
import { StringPattern } from "../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

/**
 * ModelProxy für Erzieherarten
 */
export class ErzieherartenModelProxy extends ModelProxy<Erzieherart> {

	private readonly manager: () => ErzieherartListeManager;

	/**
	 * ModelProxy für Erzieherarten
	 *
	 * @param data 			Lambda für den Zugriff auf die Original-Daten
	 * @param manager 		Manager
	 * @param patch 		Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => Erzieherart,
		manager: () => ErzieherartListeManager,
		patch?: (data: Partial<Erzieherart>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof Erzieherart> = ['istSichtbar'];
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this.manager = manager;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addValidator(new ValidatorStringIsUniqueInList(() => this.proxy, (data: Erzieherart) => data.id, (data: Erzieherart) => data.bezeichnung, () => this.manager().liste.list(), false), 'bezeichnung');
		this.addValidator(new ValidatorStringLength(() => this.proxy.bezeichnung, null, 30), 'bezeichnung');
		this.addValidator(new ValidatorInputRequired(() => this.proxy.bezeichnung), 'bezeichnung');
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnung');
		// sortierung
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}

}
