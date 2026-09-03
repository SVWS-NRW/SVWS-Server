import { computed } from "vue";
import type { LehrerLehrbefaehigungAnerkennungKatalogEintrag, LehrerLehrbefaehigungEintrag, LehrerLehrbefaehigungKatalogEintrag } from "@core";
import { LehrerLehrbefaehigung, LehrerLehrbefaehigungAnerkennung } from "@core";
import { ModelProxy } from "@ui";

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
