import { computed } from "vue";
import type { LehrerFachrichtungAnerkennungKatalogEintrag, LehrerFachrichtungEintrag, LehrerFachrichtungKatalogEintrag } from "@core";
import { LehrerFachrichtung, LehrerFachrichtungAnerkennung } from "@core";
import { ModelProxy } from "@ui";

/**
 * Der spezielle ModelProxy für LehrerFachrichtungEintrag
 */
export class LehrerFachrichtungEintragModelProxy extends ModelProxy<LehrerFachrichtungEintrag> {

	constructor(data: () => LehrerFachrichtungEintrag, patch?: (data: Partial<LehrerFachrichtungEintrag>) => Promise<boolean>) {

		const listOfAutopatchProps: Iterable<keyof LehrerFachrichtungEintrag> = ["idAnerkennungsgrund"];
		super({ data, patch, listOfAutopatchProps });

		this.validate();
	}

	anerkennung = computed<LehrerFachrichtungAnerkennungKatalogEintrag | null>({
		get: () => LehrerFachrichtungAnerkennung.data().getEintragByID(this.proxy.idAnerkennungsgrund ?? -1),
		set: (v: LehrerFachrichtungAnerkennungKatalogEintrag | null) => this.proxy.idAnerkennungsgrund = v?.id ?? null,
	});

	fachrichtung = computed<LehrerFachrichtungKatalogEintrag | null>(
		() => LehrerFachrichtung.data().getEintragByID(this.proxy.idFachrichtung)
	);

}
