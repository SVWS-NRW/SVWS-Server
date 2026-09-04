import type { LehrerFachrichtungAnerkennungKatalogEintrag } from "@core/asd/data/lehrer/LehrerFachrichtungAnerkennungKatalogEintrag";
import type { LehrerFachrichtungEintrag } from "@core/asd/data/lehrer/LehrerFachrichtungEintrag";
import type { LehrerFachrichtungKatalogEintrag } from "@core/asd/data/lehrer/LehrerFachrichtungKatalogEintrag";
import { LehrerFachrichtung } from "@core/asd/types/lehrer/LehrerFachrichtung";
import { LehrerFachrichtungAnerkennung } from "@core/asd/types/lehrer/LehrerFachrichtungAnerkennung";
import { ModelProxy } from "@ui/model/ModelProxy";
import { computed } from "vue";

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
