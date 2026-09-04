import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";
import type { Wiedervorlage } from "~/components/wiedervorlage/Wiedervorlage";

/**
 * ModelProxy für Wiedervorlage.
 */
export class WiedervorlageModelProxy extends ModelProxy<Wiedervorlage> {

	constructor(data: () => Wiedervorlage) {
		super({ data });
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addValidator(new ValidatorInputRequired(() => this.proxy.bemerkung), 'bemerkung');
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.bemerkung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bemerkung');
		this.addValidator(new ValidatorInputRequired(() => this.proxy.tsWiedervorlage), 'tsWiedervorlage');
	}
}
