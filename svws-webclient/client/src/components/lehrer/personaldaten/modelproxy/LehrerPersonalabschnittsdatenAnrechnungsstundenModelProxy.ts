import type { LehrerPersonalabschnittsdatenAnrechnungsstunden } from "@core/asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden";
import { ModelProxy } from "@ui/model/ModelProxy";

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
