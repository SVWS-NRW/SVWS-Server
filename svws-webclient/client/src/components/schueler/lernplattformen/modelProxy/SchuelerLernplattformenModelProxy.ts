import { ModelProxy } from "@ui";
import type { SchuelerLernplattform } from "@core";

export class SchuelerLernplattformenModelProxy extends ModelProxy<SchuelerLernplattform> {

	constructor(
		data: () => SchuelerLernplattform,
		patch?: (data: Partial<SchuelerLernplattform>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof SchuelerLernplattform> = ["einwilligungAbgefragt", "einwilligungNutzung"];
		super({ data, patch, listOfAutopatchProps });
		this.validate();
	}
}
