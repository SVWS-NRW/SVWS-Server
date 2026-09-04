import type { Telefonart } from "@core/core/data/schule/Telefonart";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorTelefonartBezeichnung } from "~/components/schule/kataloge/telefonarten/modelproxy/validation/ValidatorTelefonartBezeichnung";

/**
 * ModelProxy für Telefonarten.
 */
export class TelefonartenModelProxy extends ModelProxy<Telefonart> {

	/**
	 * ModelProxy für Telefonarten
	 *
	 * @param data Lambda für den Zugriff auf die Original-Daten
	 * @param alleTelefonarten Lambda zur Liste aller Telefonarten
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => Telefonart,
		alleTelefonarten: () => Iterable<Telefonart>,
		patch?: (data: Partial<Telefonart>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof Telefonart> = ["istSichtbar"];
		super({ data, patch, listOfAutopatchProps });
		this.addValidatoren(alleTelefonarten);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<Telefonart>) {
		this.addBlockingValidator(new ValidatorTelefonartBezeichnung((): Telefonart => this.proxy, liste), "bezeichnung");
		this.addBlockingValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), "sortierung");
		this.addBlockingValidator(new ValidatorNumberRange((): number => this.proxy.sortierung, 0, 32000), "sortierung");
	}
}
