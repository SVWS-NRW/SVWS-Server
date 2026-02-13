import type { LehrerFachrichtungEintrag } from "@core";
import { ModelProxy } from "@ui";

/**
 * Der spezielle ModelProxy für LehrerFachrichtungEintrag
 */
export class LehrerFachrichtungEintragModelProxy extends ModelProxy<LehrerFachrichtungEintrag> {

	constructor(data: () => LehrerFachrichtungEintrag) {

		super({ data });

		this.validate();
	}

}
