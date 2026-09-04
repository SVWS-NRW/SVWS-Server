import type { Beschaeftigungsart } from "@core/core/data/schule/Beschaeftigungsart";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
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
		super({ data, patch, listOfAutopatchProps });
		this.addValidatoren(liste);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<Beschaeftigungsart>) {
		this.addBlockingValidator(new ValidatorBeschaeftigungsartBezeichnung(() => this.proxy, liste), "bezeichnung");
		this.addBlockingValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), 'sortierung');
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, JavaInteger.MAX_VALUE), "sortierung");
	}
}
