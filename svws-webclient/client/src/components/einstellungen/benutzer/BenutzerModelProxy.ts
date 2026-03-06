import type { BenutzerListeEintrag } from "@core";
import { ArrayList } from "@core";
import { ModelProxy, ValidatorStringEquals, ValidatorStringIsUniqueInList, ValidatorStringMatchesPattern, ValidatorInputRequired } from "@ui";
import { StringPattern } from "../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

type ModelProxyData = { anzeigename: string, name: string, passwort1: string, passwort2: string };
/**
 * Der spezielle ModelProxy für die Lehrerstammdaten
 */
export class BenutzerModelProxy extends ModelProxy<ModelProxyData> {

	/**
	 * Erstellt einen ModelProxy für das Core-DTO LehrerIndividualdaten.
	 *
	 * @param data              ein Lambda für den Zugriff auf die "Original"-Daten
	 * @param liste        		Liste der Benutzereinträge
	 */
	constructor(data: () => ModelProxyData, liste: () => Iterable<BenutzerListeEintrag>) {
		super({ data });
		//
		const namen = new ArrayList<ModelProxyData>();
		for (const i of liste()) {
			namen.add({ anzeigename: "", name: i.name, passwort1: "", passwort2: "" });
		}
		//
		this.addValidator(new ValidatorStringIsUniqueInList<ModelProxyData>(() => this.proxy, (data) => data, (data) => data.name, () => namen, false), "name");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.name, StringPattern.NO_WHITESPACES), "name");
		this.addValidator(new ValidatorInputRequired<string>(() => this.proxy.name), "name");
		this.addValidator(new ValidatorStringEquals(() => this.proxy.passwort2, () => this.proxy.passwort1), "passwort2");
		this.validate();
	}

}
