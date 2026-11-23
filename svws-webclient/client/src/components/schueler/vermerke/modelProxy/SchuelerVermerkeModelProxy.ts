import { computed } from "vue";

import type { SchuelerVermerke } from "@core/core/data/schueler/SchuelerVermerke";
import type { VermerkartEintrag } from "@core/core/data/schule/VermerkartEintrag";
import { ModelProxy } from "@ui/model/ModelProxy";
import { useVermerkartenState } from "@ui/states/kataloge/VermerkartenState";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { StringPattern, ValidatorStringMatchesPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class SchuelerVermerkeModelProxy extends ModelProxy<SchuelerVermerke> {

	private readonly _vermerkartenState = useVermerkartenState();

	constructor(
		data: () => SchuelerVermerke,
		autopatch: boolean,
		patch?: (data: Partial<SchuelerVermerke>) => Promise<boolean>,
		listOfDisabledPropValidations: Iterable<keyof SchuelerVermerke> = []
	) {
		const listOfAutopatchProps: Iterable<keyof SchuelerVermerke> = autopatch ? ["idVermerkart"] : [];
		super({ data, patch, listOfAutopatchProps, listOfDisabledPropValidations });
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		// Vermerkart
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idVermerkart), 'idVermerkart');

		// Bemerkung
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bemerkung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bemerkung');
	}

	vermerkart = computed<VermerkartEintrag | null>({
		get: () => this._vermerkartenState.vermerkarten.byId.get(this.proxy.idVermerkart ?? -1) ?? null,
		set: (value: VermerkartEintrag | null) => this.proxy.idVermerkart = value?.id ?? null,
	});


}
