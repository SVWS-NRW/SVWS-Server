import type { LehrerLehrbefaehigungAnerkennungKatalogEintrag } from "@core/asd/data/lehrer/LehrerLehrbefaehigungAnerkennungKatalogEintrag";
import type { LehrerLehrbefaehigungEintrag } from "@core/asd/data/lehrer/LehrerLehrbefaehigungEintrag";
import type { LehrerLehrbefaehigungKatalogEintrag } from "@core/asd/data/lehrer/LehrerLehrbefaehigungKatalogEintrag";
import { LehrerLehrbefaehigung } from "@core/asd/types/lehrer/LehrerLehrbefaehigung";
import { LehrerLehrbefaehigungAnerkennung } from "@core/asd/types/lehrer/LehrerLehrbefaehigungAnerkennung";
import { ModelProxy } from "@ui/model/ModelProxy";
import { computed } from "vue";

/**
 * Der spezielle ModelProxy für LehrerLehrbefaehigungEintrag
 */
export class LehrerLehrbefaehigungEintragModelProxy extends ModelProxy<LehrerLehrbefaehigungEintrag> {

	constructor(data: () => LehrerLehrbefaehigungEintrag, patch?: (data: Partial<LehrerLehrbefaehigungEintrag>) => Promise<boolean>) {
		const listOfAutopatchProps: Iterable<keyof LehrerLehrbefaehigungEintrag> = ["idAnerkennungsgrund"];
		super({ data, patch, listOfAutopatchProps });

		this.validate();
	}

	anerkennung = computed<LehrerLehrbefaehigungAnerkennungKatalogEintrag | null>({
		get: () => LehrerLehrbefaehigungAnerkennung.data().getEintragByID(this.proxy.idAnerkennungsgrund ?? -1),
		set: (v: LehrerLehrbefaehigungAnerkennungKatalogEintrag | null) => this.proxy.idAnerkennungsgrund = v?.id ?? null,
	});

	lehrbefaehigung = computed<LehrerLehrbefaehigungKatalogEintrag | null>(
		() => LehrerLehrbefaehigung.data().getEintragByID(this.proxy.idLehrbefaehigung)
	);
}
