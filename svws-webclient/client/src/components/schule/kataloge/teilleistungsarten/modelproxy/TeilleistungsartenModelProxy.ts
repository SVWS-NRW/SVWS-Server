import type { Teilleistungsart } from "@core/core/data/kataloge/Teilleistungsart";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorTeilleistungsartBezeichnung } from "~/components/schule/kataloge/teilleistungsarten/modelproxy/validation/ValidatorBezeichnung";

/**
 * ModelProxy für Teilleistungsarten.
 */
export class TeilleistungsartenModelProxy extends ModelProxy<Teilleistungsart> {

	/**
	 * ModelProxy für Teilleistungsarten.
	 *
	 * @param data Lambda für den Zugriff auf die Original-Daten
	 * @param liste Lambda zur Liste aller Teilleistungsarten
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => Teilleistungsart,
		liste: () => Iterable<Teilleistungsart>,
		patch?: (data: Partial<Teilleistungsart>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof Teilleistungsart> = ["istSichtbar"];

		super({ data, patch, listOfAutopatchProps });

		this.addValidatoren(liste);
		this.validate();
	}


	private addValidatoren(liste: () => Iterable<Teilleistungsart>) {
		this.addBlockingValidator(new ValidatorTeilleistungsartBezeichnung(() => this.proxy, liste), "bezeichnung");
		this.addBlockingValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), "sortierung");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}
}
