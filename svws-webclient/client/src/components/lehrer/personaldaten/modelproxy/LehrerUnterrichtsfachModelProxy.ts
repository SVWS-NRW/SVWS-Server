import { computed } from "vue";

import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { LehrerUnterrichtsfach } from "@core/core/data/lehrer/LehrerUnterrichtsfach";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { StringPattern, ValidatorStringMatchesPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

/**
 * Der spezielle ModelProxy für LehrerUnterrichtsfach
 */
export class LehrerUnterrichtsfachModelProxy extends ModelProxy<LehrerUnterrichtsfach> {

	private readonly faecherById: Map<number, FachDaten>;

	constructor(data: () => LehrerUnterrichtsfach, faecherById: Map<number, FachDaten>, patch?: (data: Partial<LehrerUnterrichtsfach>) => Promise<boolean>) {
		const listOfAutopatchProps: Iterable<keyof LehrerUnterrichtsfach> = ["istSek1", "istSek2"];
		super({ data, patch, listOfAutopatchProps });

		this.faecherById = faecherById;

		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addBlockingValidator(new ValidatorInputRequired(() => (this.proxy.idFach >= 0) ? this.proxy.idFach : null), "idFach");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bemerkung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "bemerkung");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bemerkung, null, 255), "bemerkung");
	}

	unterrichtsfach = computed<FachDaten | null>({
		get: () => this.faecherById.get(this.proxy.idFach) ?? null,
		set: (v: FachDaten | null) => this.proxy.idFach = v?.id ?? -1,
	});

}
