import { computed } from "vue";

import type { SchuelerTelefon } from "@core/core/data/schueler/SchuelerTelefon";
import type { Telefonart } from "@core/core/data/schule/Telefonart";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { StringPattern, ValidatorStringMatchesPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class SchuelerTelefonnummerModelProxy extends ModelProxy<SchuelerTelefon> {

	private readonly telefonarten: () => KatalogState<Telefonart>;

	public constructor(
		data: () => SchuelerTelefon,
		telefonarten: () => KatalogState<Telefonart>,
		patch?: (data: Partial<SchuelerTelefon>) => Promise<boolean>
	) {
		super({ data, patch, listOfAutopatchProps: [] });
		this.telefonarten = telefonarten;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren(): void {
		// Telefonnummer
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.telefonnummer), "telefonnummer");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.telefonnummer, null, 20), "telefonnummer");
		this.addBlockingValidator(
			new ValidatorStringMatchesPattern(() => this.proxy.telefonnummer, StringPattern.IS_PHONE_NUMBER),
			"telefonnummer"
		);

		// Telefonart
		this.addBlockingValidator(new ValidatorInputRequired(() => this.telefonart.value), "idTelefonArt");

		// Bemerkung
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bemerkung, null, 50), "bemerkung");
	}

	public telefonart = computed<Telefonart | null>({
		get: () => this.telefonarten().byId.get(this.proxy.idTelefonArt) ?? null,
		set: (telefonart: Telefonart | null) => this.proxy.idTelefonArt = telefonart?.id ?? -1,
	});

}
