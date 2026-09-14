import { computed } from "vue";

import type { LehrerEinwilligung } from "@core/core/data/lehrer/LehrerEinwilligung";
import { ModelProxy } from "@ui/model/ModelProxy";



export class LehrerEinwilligungenModelProxy extends ModelProxy<LehrerEinwilligung> {

	constructor(
		data: () => LehrerEinwilligung,
		patch?: (data: Partial<LehrerEinwilligung>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof LehrerEinwilligung> = ["istAbgefragt", "istZugestimmt"];
		super({ data, patch, listOfAutopatchProps });
		this.validate();
	}

	currentAbgefragt = computed({
		get: () => this.proxy.istAbgefragt,
		set: (abgefragt: boolean) => {
			this.proxy.istAbgefragt = abgefragt;
			if ((!abgefragt) && (this.proxy.istZugestimmt)) {
				this.proxy.istZugestimmt = false;
			}
		},
	});

	currentZugestimmt = computed({
		get: () => this.proxy.istZugestimmt,
		set: (zugestimmt: boolean) => {
			this.proxy.istZugestimmt = zugestimmt;
			if ((zugestimmt) && (!this.proxy.istAbgefragt)) {
				this.proxy.istAbgefragt = true;
			}
		},
	});
}
