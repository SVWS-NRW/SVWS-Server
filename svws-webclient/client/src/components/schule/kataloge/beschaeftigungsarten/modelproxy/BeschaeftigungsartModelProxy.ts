import { ModelProxy, ValidatorNumberRange } from "@ui";
import type { Beschaeftigungsart } from "@core";
import { ValidatorBeschaeftigungsartBezeichnung } from "~/components/schule/kataloge/beschaeftigungsarten/modelproxy/ValidatorBeschaeftigungsartBezeichnung";

export class BeschaeftigungsartModelProxy extends ModelProxy<Beschaeftigungsart> {

	/**
	 * ModelProxy für Beschaeftigungsarten
	 *
	 * @param data Lambda für den Zugriff auf die Original-Daten
	 * @param liste Lambda zur Liste aller Betriebsarten
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => Beschaeftigungsart,
		liste: () => Iterable<Beschaeftigungsart>,
		patch?: (data: Partial<Beschaeftigungsart>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof Beschaeftigungsart> = ["istSichtbar"];
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this.addValidatoren(liste);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<Beschaeftigungsart>) {
		this.addValidator(new ValidatorBeschaeftigungsartBezeichnung(() => this.proxy, liste), "bezeichnung");
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}
}
