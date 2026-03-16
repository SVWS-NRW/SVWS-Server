import { ModelProxy, ValidatorInputRequired, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { BetriebeAnsprechpartner } from "@core";
import { StringPattern } from "../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

export class BetriebeAnsprechpartnerModelProxy extends ModelProxy<BetriebeAnsprechpartner> {


	constructor(
		data: () => BetriebeAnsprechpartner,
		patch?: (data: Partial<BetriebeAnsprechpartner>) => Promise<boolean>
	) {
		super({ data, patch, checkValidBeforePatch: true });
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addValidator(new ValidatorStringLength(() => this.proxy.anrede, null, 10), "anrede");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.anrede, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "anrede");

		this.addValidator(new ValidatorStringLength(() => this.proxy.rufname, null, 80), "rufname");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.rufname, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "rufname");

		this.addValidator(new ValidatorStringLength(() => this.proxy.name, null, 120), "name");
		this.addValidator(new ValidatorInputRequired(() => this.proxy.name), "name");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.name, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "name");

		this.addValidator(new ValidatorStringLength(() => this.proxy.telefon, null, 20), "telefon");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.telefon, StringPattern.IS_PHONE_NUMBER), "telefon");

		this.addValidator(new ValidatorStringLength(() => this.proxy.eMail, null, 100), "eMail");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.eMail, StringPattern.IS_EMAIL), "eMail");
	}
}
