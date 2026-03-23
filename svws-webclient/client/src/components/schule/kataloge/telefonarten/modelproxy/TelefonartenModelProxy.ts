import { ModelProxy, ValidatorInputRequired, ValidatorNumberRange } from "@ui";
import type { Telefonart } from "@core";
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
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this.addValidatoren(alleTelefonarten);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<Telefonart>) {
		this.addValidator(new ValidatorTelefonartBezeichnung((): Telefonart => this.proxy, liste), "bezeichnung");
		this.addValidator(new ValidatorNumberRange((): number => this.proxy.sortierung, 0, 32000), "sortierung");
		this.addValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), "sortierung");

	}
}
