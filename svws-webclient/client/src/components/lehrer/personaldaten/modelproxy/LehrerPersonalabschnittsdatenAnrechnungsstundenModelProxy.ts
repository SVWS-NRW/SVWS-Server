import type { LehrerPersonalabschnittsdatenAnrechnungsstunden } from "@core";
import { ModelProxy } from "@ui";

/**
 * Der spezielle ModelProxy für die LehrerPersonalabschnittsdatenAnrechnungsstunden
 */
export class LehrerPersonalabschnittsdatenAnrechnungsstundenModelProxy extends ModelProxy<LehrerPersonalabschnittsdatenAnrechnungsstunden> {

	constructor(data: () => LehrerPersonalabschnittsdatenAnrechnungsstunden, patch: (data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>) => Promise<boolean>) {

		const listOfAutopatchProps: Iterable<keyof LehrerPersonalabschnittsdatenAnrechnungsstunden> = [];
		super({ data, patch, listOfAutopatchProps });

		this.validate();
	}

}
