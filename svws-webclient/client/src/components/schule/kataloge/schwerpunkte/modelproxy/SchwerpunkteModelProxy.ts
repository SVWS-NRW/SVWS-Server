import type { SchuelerSchwerpunkt } from "@core/core/data/kataloge/SchuelerSchwerpunkt";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorSchwerpunktBezeichnung } from "~/components/schule/kataloge/schwerpunkte/modelproxy/validation/ValidatorSchwerpunktBezeichnung";

/**
 * ModelProxy für Schwerpunkte.
 */
export class SchwerpunkteModelProxy extends ModelProxy<SchuelerSchwerpunkt> {

	/**
	 * ModelProxy für Schwerpunkte
	 *
	 * @param data Lambda für den Zugriff auf die Original-Daten
	 * @param alleSchwerpunkte Lambda zur Liste aller Schwerpunkte
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => SchuelerSchwerpunkt,
		alleSchwerpunkte: () => Iterable<SchuelerSchwerpunkt>,
		patch?: (data: Partial<SchuelerSchwerpunkt>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof SchuelerSchwerpunkt> = ["istSichtbar"];
		super({ data, patch, listOfAutopatchProps });
		this.addValidatoren(alleSchwerpunkte);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<SchuelerSchwerpunkt>) {
		this.addBlockingValidator(new ValidatorSchwerpunktBezeichnung((): SchuelerSchwerpunkt => this.proxy, liste), "bezeichnung");
		this.addBlockingValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), 'sortierung');
		this.addBlockingValidator(new ValidatorNumberRange((): number => this.proxy.sortierung, 0, 32000), "sortierung");
	}
}
