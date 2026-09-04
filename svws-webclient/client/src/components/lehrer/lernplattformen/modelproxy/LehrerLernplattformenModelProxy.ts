import { ModelProxy } from "@ui";
import type { LehrerLernplattform } from "@core";

export class LehrerLernplattformenModelProxy extends ModelProxy<LehrerLernplattform> {

	constructor(
		data: () => LehrerLernplattform,
		patch?: (data: Partial<LehrerLernplattform>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof LehrerLernplattform> = ["einwilligungAbgefragt", "einwilligungNutzung"];
		super({ data, patch, listOfAutopatchProps });
		this.validate();
	}
}
