import type { Betriebsart } from "@core/core/data/schule/Betriebsart";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorBetriebsartBezeichnung } from "~/components/schule/kataloge/betriebsarten/modelproxy/validation/ValidatorBetriebsartBezeichnung";

/**
 * ModelProxy für Betriebsarten.
 */
export class BetriebsartenModelProxy extends ModelProxy<Betriebsart> {

	/**
	 * ModelProxy für Betriebsarten
	 *
	 * @param data Lambda für den Zugriff auf die Original-Daten
	 * @param liste Lambda zur Liste aller Betriebsarten
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => Betriebsart,
		liste: () => Iterable<Betriebsart>,
		patch?: (data: Partial<Betriebsart>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof Betriebsart> = ["istSichtbar"];
		super({ data, patch, listOfAutopatchProps });
		this.addValidatoren(liste);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<Betriebsart>) {
		this.addBlockingValidator(new ValidatorBetriebsartBezeichnung(() => this.proxy, liste), "bezeichnung");
		this.addBlockingValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), 'sortierung');
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, JavaInteger.MAX_VALUE), "sortierung");
	}
}
