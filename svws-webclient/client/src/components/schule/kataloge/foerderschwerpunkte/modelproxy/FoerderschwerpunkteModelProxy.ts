import type { FoerderschwerpunkteListeManager } from "@ui";
import { ModelProxy, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { FoerderschwerpunktEintrag, FoerderschwerpunktKatalogEintrag } from "@core";
import { Foerderschwerpunkt } from "@core";
import { StringPattern } from "../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";
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
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this.manager = manager;
		this.schuljahr = schuljahr;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addValidator(new ValidatorStringIsUniqueInList(() => this.proxy, (data: FoerderschwerpunktEintrag) => data.id, (data: FoerderschwerpunktEintrag) => data.kuerzel, () => this.manager().liste.list(), false), 'kuerzel');
		this.addValidator(new ValidatorStringLength(() => this.proxy.kuerzel, null, 50), 'kuerzel');
		this.addValidator(new ValidatorInputRequired(() => this.proxy.kuerzel), 'kuerzel');
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.kuerzel, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'kuerzel');
		// asd
		this.addValidator(new ValidatorInputRequired(() => this.proxy.kuerzelStatistik), "kuerzelStatistik");
		// sortierung
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}

	foerderschwerpunkt = computed<FoerderschwerpunktKatalogEintrag | null>({
		get: () => Foerderschwerpunkt.data().getEintragBySchuljahrUndSchluessel(this.schuljahr, this.proxy.kuerzelStatistik),
		set: (v: FoerderschwerpunktKatalogEintrag | null) => this.proxy.kuerzelStatistik = v?.kuerzel ?? '',
	});

}
