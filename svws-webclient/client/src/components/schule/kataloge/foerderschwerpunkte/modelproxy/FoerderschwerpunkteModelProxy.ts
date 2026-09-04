import type { FoerderschwerpunktKatalogEintrag } from "@core/asd/data/schule/FoerderschwerpunktKatalogEintrag";
import { Foerderschwerpunkt } from "@core/asd/types/schule/Foerderschwerpunkt";
import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { FoerderschwerpunkteListeManager } from "@ui/ui/manager/kataloge/FoerderschwerpunkteListeManager";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";
import { computed } from "vue";

/**
 * ModelProxy für Foerderschwerpunkte
 */
export class FoerderschwerpunkteModelProxy extends ModelProxy<FoerderschwerpunktEintrag> {

	private readonly manager: () => FoerderschwerpunkteListeManager;
	private readonly schuljahr: number;

	/**
	 * ModelProxy für Foerderschwerpunkte
	 *
	 * @param data 			Lambda für den Zugriff auf die Original-Daten
	 * @param manager 		Manager
	 * @param schuljahr		schuljahr
	 * @param patch 		Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => FoerderschwerpunktEintrag,
		manager: () => FoerderschwerpunkteListeManager,
		schuljahr: number,
		patch?: (data: Partial<FoerderschwerpunktEintrag>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof FoerderschwerpunktEintrag> = ['istSichtbar', 'kuerzelStatistik'];
		super({ data, patch, listOfAutopatchProps });
		this.manager = manager;
		this.schuljahr = schuljahr;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addBlockingValidator(new ValidatorStringIsUniqueInList(() => this.proxy, (data: FoerderschwerpunktEintrag) => data.id, (data: FoerderschwerpunktEintrag) => data.kuerzel, () => this.manager().liste.list(), false), 'kuerzel');
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.kuerzel, null, 50), 'kuerzel');
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.kuerzel), 'kuerzel');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.kuerzel, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'kuerzel');
		// asd
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.kuerzelStatistik), "kuerzelStatistik");
		// sortierung
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.sortierung), "sortierung");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}

	foerderschwerpunkt = computed<FoerderschwerpunktKatalogEintrag | null>({
		get: () => Foerderschwerpunkt.data().getEintragBySchuljahrUndSchluessel(this.schuljahr, this.proxy.kuerzelStatistik),
		set: (v: FoerderschwerpunktKatalogEintrag | null) => this.proxy.kuerzelStatistik = v?.kuerzel ?? '',
	});

}
