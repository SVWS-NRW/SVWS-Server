import { computed } from "vue";
import { ModelProxy, ValidatorStringMatchesPattern, StringPattern } from "@ui";
import type { SchuelerVermerke, VermerkartEintrag } from "@core";

export class SchuelerVermerkeModelProxy extends ModelProxy<SchuelerVermerke> {

	private readonly _vermerkartenById: () => Map<number, VermerkartEintrag>;

	constructor(
		data: () => SchuelerVermerke,
		vermerkartenById: () => Map<number, VermerkartEintrag>,
		patch?: (data: Partial<SchuelerVermerke>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof SchuelerVermerke> = ["idVermerkart"];
		super({ data, patch, listOfAutopatchProps });
		this._vermerkartenById = vermerkartenById;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		// Bemerkung
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bemerkung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bemerkung');
	}

	vermerkart = computed<VermerkartEintrag | null>({
		get: () => this._vermerkartenById().get(this.proxy.idVermerkart ?? -1) ?? null,
		set: (value: VermerkartEintrag | null) => this.proxy.idVermerkart = value?.id ?? null,
	});


}
