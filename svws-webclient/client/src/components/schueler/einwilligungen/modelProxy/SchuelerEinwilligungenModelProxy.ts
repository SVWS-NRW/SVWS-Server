import type { SchuelerEinwilligung } from "@core/core/data/schueler/SchuelerEinwilligung";
import { ModelProxy } from "@ui/model/ModelProxy";
import { computed } from "vue";

export class SchuelerEinwilligungenModelProxy extends ModelProxy<SchuelerEinwilligung> {

	constructor(
		data: () => SchuelerEinwilligung,
		patch?: (data: Partial<SchuelerEinwilligung>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof SchuelerEinwilligung> = ["abgefragt", "status"];
		super({ data, patch, listOfAutopatchProps });
		this.validate();
	}

	currentStatus = computed({
		get: () => this.proxy.status,
		set: (status: boolean) => {
			this.proxy.status = status;
			if ((status) && (!this.proxy.abgefragt)) {
				this.proxy.abgefragt = true;
			}
		},
	});

	currentAbgefragt = computed({
		get: () => this.proxy.abgefragt,
		set: (abgefragt: boolean) => {
			this.proxy.abgefragt = abgefragt;
		},
	});
}
