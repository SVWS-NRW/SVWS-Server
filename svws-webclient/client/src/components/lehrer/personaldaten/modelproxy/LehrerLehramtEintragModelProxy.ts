import type { LehrerLehramtEintrag } from "@core";
import { ModelProxy } from "@ui";

/**
 * Der spezielle ModelProxy für LehrerLehramtEintrag
 */
export class LehrerLehramtEintragModelProxy extends ModelProxy<LehrerLehramtEintrag> {

	constructor(data: () => LehrerLehramtEintrag) {

		super({ data });

		this.validate();
	}

}
