import type { ValidatorKontext, LehrerStammdaten } from "@core";
import { ValidatorLsdLehrerStammdatenGeburtsdatum, ValidatorLsnLehrerStammdatenNachname, ValidatorLsvLehrerStammdatenVorname } from "@core";
import { ModelProxy } from "@ui";

/**
 * Der spezielle ModelProxy für die Lehrerstammdaten
 */
export class LehrerIndividualdatenModelProxy extends ModelProxy<LehrerStammdaten> {

	/**
	 * Erstellt einen ModelProxy für das Core-DTO LehrerIndividualdaten.
	 *
	 * @param data               ein Lambda für den Zugriff auf die "Original"-Daten
	 * @param validatorKontext   der Validator-Kontext für die Nutzung in den ASD-Validatoren
	 * @param patchMethod        ggf. die Methode zum Patchen der einzelnen Attribute, sofern das automatische Patchen
	 *                           bei Änderungen gewünscht ist
	 */
	constructor(data: () => LehrerStammdaten, validatorKontext: () => ValidatorKontext, patchMethod?: (data: Partial<LehrerStammdaten>) => Promise<boolean>) {
		super({ data: data, patch: patchMethod });
		this.addValidator(new ValidatorLsnLehrerStammdatenNachname({ get: () => this.proxy.nachname }, validatorKontext()), "nachname");
		this.addValidator(new ValidatorLsvLehrerStammdatenVorname({ get: () => this.proxy.vorname }, validatorKontext()), "vorname");
		this.addValidator(new ValidatorLsdLehrerStammdatenGeburtsdatum({ get: () => this.proxy.geburtsdatum }, validatorKontext()), "geburtsdatum");
		this.validate();
	}

}
