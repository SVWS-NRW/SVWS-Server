import { ModelProxy, ValidatorNumberRange } from "@ui";
import type { Betriebsart } from "@core";
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
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this.addValidatoren(liste);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<Betriebsart>) {
		this.addValidator(new ValidatorBetriebsartBezeichnung(() => this.proxy, liste), "bezeichnung");
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}
}
