import type { BetriebeAnsprechpartner } from "@core/core/data/schule/BetriebeAnsprechpartner";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class BetriebeAnsprechpartnerModelProxy extends ModelProxy<BetriebeAnsprechpartner> {


	constructor(
		data: () => BetriebeAnsprechpartner,
		patch?: (data: Partial<BetriebeAnsprechpartner>) => Promise<boolean>
	) {
		super({ data, patch });
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.anrede, null, 10), "anrede");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.anrede, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "anrede");

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.rufname, null, 80), "rufname");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.rufname, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "rufname");

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.name, null, 120), "name");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.name), "name");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.name, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "name");

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.telefon, null, 20), "telefon");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.telefon, StringPattern.IS_PHONE_NUMBER), "telefon");

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.eMail, null, 100), "eMail");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.eMail, StringPattern.IS_EMAIL), "eMail");
	}
}
