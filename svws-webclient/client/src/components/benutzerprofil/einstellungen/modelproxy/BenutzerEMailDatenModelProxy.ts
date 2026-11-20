import type { BenutzerEMailDaten } from "@core/core/data/benutzer/BenutzerEMailDaten";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { StringPattern, ValidatorStringMatchesPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

import { ValidatorBenutzerEMailDatenPasswort } from "./ValidatorBenutzerEMailDatenPasswort";

/**
 * Der spezielle ModelProxy für die E-Mail-Benutzerdaten
 */
export class BenutzerEMailDatenModelProxy extends ModelProxy<BenutzerEMailDaten> {

	/**
	 * Erstellt einen ModelProxy für das Core-DTO BenutzerEMailDaten.
	 *
	 * @param data    ein Lambda für den Zugriff auf die "Original"-Daten
	 * @param patch   die Methode zum Patchen der Daten
	 */
	constructor(data: () => BenutzerEMailDaten, patch: (data: Partial<BenutzerEMailDaten>) => Promise<boolean>) {
		super({ data, patch });
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		// Name
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.name, null, 255), "name");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.name, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "name");

		// E-Mail-Adresse
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.address, null, 100), "address");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.address, StringPattern.IS_EMAIL), "address");

		// SMTP-Username
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.usernameSMTP, null, 255), "usernameSMTP");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.usernameSMTP, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "usernameSMTP");

		// SMTP-Passwort
		this.addValidator(new ValidatorBenutzerEMailDatenPasswort(() => this.proxy.passwordSMTP, () => this.proxy.usernameSMTP), "passwordSMTP", "usernameSMTP");

		// Signatur
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.signatur, null, 2047), "signatur");
	}

}
