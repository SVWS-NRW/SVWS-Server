/**
 *  Modelproxy für Orte
 */
import { ModelProxy, StringPattern, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { OrtKatalogEintrag } from "@core";
import { ValidatorOrtPlz } from "~/components/schule/kataloge/orte/modelproxy/validation/ValidatorOrtPlz";
import { ValidatorOrtOrtsname } from "~/components/schule/kataloge/orte/modelproxy/validation/ValidatorOrtOrtsname";
import { ValidatorOrtPlzOrtsnameUnique } from "~/components/schule/kataloge/orte/modelproxy/validation/ValidatorOrtPlzOrtsnameUnique";

export class OrtModelProxy extends ModelProxy<OrtKatalogEintrag> {
	constructor(
		data: () => OrtKatalogEintrag,
		alleOrte: () => Iterable<OrtKatalogEintrag>,
		patch?: (data: Partial<OrtKatalogEintrag>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof OrtKatalogEintrag> = ['sortierung', 'istSichtbar'];
		super({ data, patch, listOfAutopatchProps });

		this.addValidatoren(alleOrte);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<OrtKatalogEintrag>) {
		this.addBlockingValidator(new ValidatorOrtPlzOrtsnameUnique(() => this.proxy, liste), "ortsname", "plz");
		this.addBlockingValidator(new ValidatorOrtPlz(() => this.proxy), "plz");
		this.addBlockingValidator(new ValidatorOrtOrtsname(() => this.proxy), "ortsname");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.kreis, null, 3), "kreis");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.kreis, StringPattern.NO_WHITESPACES), "kreis");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.kuerzelBundesland, null, 2), "kuerzelBundesland");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.kuerzelBundesland, StringPattern.NO_WHITESPACES), "kuerzelBundesland");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.sortierung), "sortierung");
	}
}
