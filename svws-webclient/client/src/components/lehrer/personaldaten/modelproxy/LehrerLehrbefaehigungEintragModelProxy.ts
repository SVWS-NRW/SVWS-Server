import type { LehrerLehrbefaehigungEintrag } from "@core";
import { ModelProxy } from "@ui";

/**
 * Der spezielle ModelProxy für LehrerLehrbefaehigungEintrag
 */
export class LehrerLehrbefaehigungEintragModelProxy extends ModelProxy<LehrerLehrbefaehigungEintrag> {

	constructor(data: () => LehrerLehrbefaehigungEintrag) {

		super({ data });

		this.validate();
	}

}
