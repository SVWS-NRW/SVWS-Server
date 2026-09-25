import { computed } from "vue";

import type { SchuelerVermerke } from "@core/core/data/schueler/SchuelerVermerke";
import type { VermerkartEintrag } from "@core/core/data/schule/VermerkartEintrag";
import { ModelProxy } from "@ui/model/ModelProxy";
import { useVermerkartenState } from "@ui/states/kataloge/VermerkartenState";
import { StringPattern, ValidatorStringMatchesPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class SchuelerVermerkeModelProxy extends ModelProxy<SchuelerVermerke> {

	private readonly _vermerkartenState = useVermerkartenState();

	constructor(
		data: () => SchuelerVermerke,
		patch?: (data: Partial<SchuelerVermerke>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof SchuelerVermerke> = ["idVermerkart"];
		super({ data, patch, listOfAutopatchProps });
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		// Bemerkung
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bemerkung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bemerkung');
	}

	vermerkart = computed<VermerkartEintrag | null>({
		get: () => this._vermerkartenState.vermerkarten.byId.get(this.proxy.idVermerkart ?? -1) ?? null,
		set: (value: VermerkartEintrag | null) => this.proxy.idVermerkart = value?.id ?? null,
	});


}
