import { ValidatorNumberRange } from "@ui";
import { ModelProxy } from "@ui";
import type { KatalogEntlassgrund } from "@core";
import { ValidatorEntlassgrundBezeichnung } from "~/components/schule/kataloge/entlassgruende/modelproxy/validation/ValidatorEntlassgrundBezeichnung";

/**
 * ModelProxy für Entlassgründe
 */
export class EntlassgruendeModelProxy extends ModelProxy<KatalogEntlassgrund> {

	/**
	 * ModelProxy für Entlassgründe
	 *
	 * @param data Lambda für den Zugriff auf Original-Daten
	 * @param liste Lambda zur Liste aller Entlassgründe
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => KatalogEntlassgrund,
		alleEntlassgruende: () => Iterable<KatalogEntlassgrund>,
		patch?: (data: Partial<KatalogEntlassgrund>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof KatalogEntlassgrund> = ["istSichtbar"];
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this.addValidatoren(alleEntlassgruende);
		this.validate();

	}

	private addValidatoren(liste: () => Iterable<KatalogEntlassgrund>) {
		this.addValidator(new ValidatorEntlassgrundBezeichnung(() => this.proxy, liste), "bezeichnung");
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}
}
