import type { BenutzerListeEintrag } from "@core";
import { ArrayList } from "@core";
import { ModelProxy, ValidatorStringEquals, ValidatorStringHasNoWhitespaces, ValidatorStringNotEmpty, ValidatorStringNotIn } from "@ui";

/**
 * Der spezielle ModelProxy für die Lehrerstammdaten
 */
export class BenutzerModelProxy extends ModelProxy<{ anzeigename: string, name: string, passwort1: string, passwort2: string }> {

	/**
	 * Erstellt einen ModelProxy für das Core-DTO LehrerIndividualdaten.
	 *
	 * @param data               ein Lambda für den Zugriff auf die "Original"-Daten
	 * @param validatorKontext   der Validator-Kontext für die Nutzung in den ASD-Validatoren
	 * @param patchMethod        ggf. die Methode zum Patchen der einzelnen Attribute, sofern das automatische Patchen
	 *                           bei Änderungen gewünscht ist
	 */
	constructor(data: () => { anzeigename: string, name: string, passwort1: string, passwort2: string }, liste: () => Iterable<BenutzerListeEintrag>) {
		super({ data });
		const namen = new ArrayList<string>();
		for (const i of liste()) {
			namen.add(i.name);
		}
		this.addValidator(new ValidatorStringNotIn(() => this.proxy.name, namen, true), "name");
		this.addValidator(new ValidatorStringHasNoWhitespaces(() => this.proxy.name), "name");
		this.addValidator(new ValidatorStringNotEmpty(() => this.proxy.name), "name");
		this.addValidator(new ValidatorStringEquals(() => this.proxy.passwort2, () => this.proxy.passwort1), "passwort2");
		this.validate();
	}

}
