import { computed } from "vue";

import type { SchuelerSchulbesuchMerkmal } from "@core/asd/data/schueler/SchuelerSchulbesuchMerkmal";
import type { Merkmal } from "@core/core/data/schule/Merkmal";
import { ModelProxy } from "@ui/model/ModelProxy";
import { useMerkmaleState } from "@ui/states/kataloge/MerkmaleState";

export class SchuelerSchulbesuchMerkmaleModelProxy extends ModelProxy<SchuelerSchulbesuchMerkmal> {

	private readonly merkmaleState = useMerkmaleState();

	constructor(data: () => SchuelerSchulbesuchMerkmal) {
		super({ data });
	}

	merkmal = computed<Merkmal | null>({
		get: () => this.merkmaleState.merkmale.byId.get(this.proxy.idMerkmal ?? -1) ?? null,
		set: (v: Merkmal | null) => this.proxy.idMerkmal = v?.id ?? null,
	});
}
