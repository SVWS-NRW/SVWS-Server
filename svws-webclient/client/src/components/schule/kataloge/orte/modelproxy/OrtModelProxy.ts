import { ModelProxy, StringPattern, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { LaenderKatalogEintrag, OrtKatalogEintrag } from "@core";
import { Laender } from "@core";
import { ValidatorOrtPlz } from "~/components/schule/kataloge/orte/modelproxy/validation/ValidatorOrtPlz";
import { ValidatorOrtOrtsname } from "~/components/schule/kataloge/orte/modelproxy/validation/ValidatorOrtOrtsname";
import { ValidatorOrtPlzOrtsnameUnique } from "~/components/schule/kataloge/orte/modelproxy/validation/ValidatorOrtPlzOrtsnameUnique";
import { computed } from "vue";

export class OrtModelProxy extends ModelProxy<OrtKatalogEintrag> {
	constructor(
		data: () => OrtKatalogEintrag,
		alleOrte: () => Iterable<OrtKatalogEintrag>,
		patch?: (data: Partial<OrtKatalogEintrag>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof OrtKatalogEintrag> = ['istSichtbar', 'idBundesland'];
		super({ data, patch, listOfAutopatchProps });

		this.addValidatoren(alleOrte);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<OrtKatalogEintrag>) {
		this.addBlockingValidator(new ValidatorOrtPlzOrtsnameUnique(() => this.proxy, liste), "ortsname", "plz");
		this.addBlockingValidator(new ValidatorOrtPlz(() => this.proxy), "plz");
		this.addBlockingValidator(new ValidatorOrtOrtsname(() => this.proxy), "ortsname");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.kreis, null, 3), "kreis");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.kreis, StringPattern.NO_WHITESPACES), "kreis");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.sortierung), "sortierung");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}

	bundesland = computed<LaenderKatalogEintrag | null>({
		get: () => Laender.data().getEintragByID(this.proxy.idBundesland ?? -1) ?? null,
		set: (v: LaenderKatalogEintrag | null) => this.proxy.idBundesland = v?.id ?? null,
	});

}
