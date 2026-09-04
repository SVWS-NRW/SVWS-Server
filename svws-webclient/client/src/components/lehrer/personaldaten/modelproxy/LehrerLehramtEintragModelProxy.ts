import type { LehrerLehramtAnerkennungKatalogEintrag } from "@core/asd/data/lehrer/LehrerLehramtAnerkennungKatalogEintrag";
import type { LehrerLehramtEintrag } from "@core/asd/data/lehrer/LehrerLehramtEintrag";
import type { LehrerLehramtKatalogEintrag } from "@core/asd/data/lehrer/LehrerLehramtKatalogEintrag";
import { LehrerLehramt } from "@core/asd/types/lehrer/LehrerLehramt";
import { LehrerLehramtAnerkennung } from "@core/asd/types/lehrer/LehrerLehramtAnerkennung";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { computed } from "vue";

/**
 * Der spezielle ModelProxy für LehrerLehramtEintrag
 */
export class LehrerLehramtEintragModelProxy extends ModelProxy<LehrerLehramtEintrag> {

	constructor(data: () => LehrerLehramtEintrag, patch?: (data: Partial<LehrerLehramtEintrag>) => Promise<boolean>) {
		const listOfAutopatchProps: Iterable<keyof LehrerLehramtEintrag> = ["idAnerkennungsgrund"];
		super({ data, patch, listOfAutopatchProps });

		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addBlockingValidator(new ValidatorInputRequired(() => (this.proxy.idKatalogLehramt === 0) ? null : this.proxy.idKatalogLehramt), "idKatalogLehramt");
	}

	lehramt = computed<LehrerLehramtKatalogEintrag | null>({
		get: () => LehrerLehramt.data().getEintragByID(this.proxy.idKatalogLehramt),
		set: (v: LehrerLehramtKatalogEintrag | null) => this.proxy.idKatalogLehramt = v?.id ?? 0,
	});

	anerkennung = computed<LehrerLehramtAnerkennungKatalogEintrag | null>({
		get: () => LehrerLehramtAnerkennung.data().getEintragByID(this.proxy.idAnerkennungsgrund ?? -1),
		set: (v: LehrerLehramtAnerkennungKatalogEintrag | null) => this.proxy.idAnerkennungsgrund = v?.id ?? null,
	});

}
