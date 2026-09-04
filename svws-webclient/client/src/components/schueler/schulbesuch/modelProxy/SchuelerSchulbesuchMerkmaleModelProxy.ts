import type { SchuelerSchulbesuchMerkmal } from "@core/asd/data/schueler/SchuelerSchulbesuchMerkmal";
import type { Merkmal } from "@core/core/data/schule/Merkmal";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { SchuelerSchulbesuchManager } from "@ui/ui/manager/schueler/SchuelerSchulbesuchManager";
import { computed } from "vue";

export class SchuelerSchulbesuchMerkmaleModelProxy extends ModelProxy<SchuelerSchulbesuchMerkmal> {

	private readonly manager: () => SchuelerSchulbesuchManager;


	constructor(
		data: () => SchuelerSchulbesuchMerkmal,
		manager: () => SchuelerSchulbesuchManager) {
		super({ data });
		this.manager = manager;
	}

	merkmal = computed<Merkmal | null>({
		get: () => this.manager().merkmaleById.get(this.proxy.idMerkmal ?? -1) ?? null,
		set: (v: Merkmal | null) => this.proxy.idMerkmal = v?.id ?? null,
	});
}
