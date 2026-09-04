import type { LehrerAbgangsgrundKatalogEintrag } from "@core/asd/data/lehrer/LehrerAbgangsgrundKatalogEintrag";
import type { LehrerPersonaldaten } from "@core/asd/data/lehrer/LehrerPersonaldaten";
import type { LehrerZugangsgrundKatalogEintrag } from "@core/asd/data/lehrer/LehrerZugangsgrundKatalogEintrag";
import { LehrerAbgangsgrund } from "@core/asd/types/lehrer/LehrerAbgangsgrund";
import { LehrerZugangsgrund } from "@core/asd/types/lehrer/LehrerZugangsgrund";
import type { ValidatorKontext } from "@core/asd/validate/ValidatorKontext";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { LehrerListeManager } from "@ui/ui/manager/lehrer/LehrerListeManager";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";
import { computed } from "vue";
import { schuleStateImpl } from "~/states/SchuleStateImpl";

/**
 * Der spezielle ModelProxy für die Lehrerpersonaldaten
 */
export class LehrerPersonaldatenModelProxy extends ModelProxy<LehrerPersonaldaten> {

	protected readonly manager: () => LehrerListeManager;

	/**
	 * Erstellt einen ModelProxy für das Core-DTO LehrerIndividualdaten.
	 *
	 * @param data               Zugriff auf die "Original"-Daten
	 * @param validatorKontext   der Validator-Kontext für die Nutzung in den ASD-Validatoren
	 * @param manager            Manager der Lehrerliste
	 * @param patch              ggf. die Methode zum Patchen der einzelnen Attribute, sofern das automatische Patchen
	 *                           bei Änderungen gewünscht ist
	 */
	constructor(data: () => LehrerPersonaldaten, validatorKontext: () => ValidatorKontext, manager: () => LehrerListeManager,
		patch?: (data: Partial<LehrerPersonaldaten>) => Promise<boolean>) {
		super({ data, patch, listOfAutopatchProps: ["zugangsgrund", "abgangsgrund"] });
		this.manager = manager;
		this.addValidatoren(validatorKontext);
		this.validate();
	}

	private addValidatoren(validatorKontext: () => ValidatorKontext) {
		// Identnummer
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.identNrTeil1, null, 10), "identNrTeil1");
		this.addBlockingValidator(
			new ValidatorStringMatchesPattern(() => this.proxy.identNrTeil1, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES),
			"identNrTeil1"
		);

		// Seriennummer
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.identNrTeil2SerNr, null, 5), "identNrTeil2SerNr");
		this.addBlockingValidator(
			new ValidatorStringMatchesPattern(() => this.proxy.identNrTeil2SerNr, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES),
			"identNrTeil2SerNr"
		);

		// Vergütungsschlüssel
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.lbvVerguetungsschluessel, null, 1), "lbvVerguetungsschluessel");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(
			() => this.proxy.lbvVerguetungsschluessel,
			StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES
		), "lbvVerguetungsschluessel"
		);

		// PA-Nummer
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.personalaktennummer, null, 20), "personalaktennummer");
		this.addBlockingValidator(
			new ValidatorStringMatchesPattern(() => this.proxy.personalaktennummer, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES),
			"personalaktennummer"
		);

		// LBV-Personalnummer
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.lbvPersonalnummer, null, 15), "lbvPersonalnummer");
		this.addBlockingValidator(
			new ValidatorStringMatchesPattern(() => this.proxy.lbvPersonalnummer, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES),
			"lbvPersonalnummer"
		);
	}

	zugangsgrund = computed<LehrerZugangsgrundKatalogEintrag | null>({
		get: () => {
			const wert = LehrerZugangsgrund.data().getWertByKuerzel(this.proxy.zugangsgrund ?? '');
			if (wert === null) {
				return null;
			}
			return LehrerZugangsgrund.data().getEintragBySchuljahrUndWert(schuleStateImpl.schuljahr, wert);
		},
		set: (value) => this.proxy.zugangsgrund = value?.kuerzel ?? null,
	});

	abgangsgrund = computed<LehrerAbgangsgrundKatalogEintrag | null>({
		get: () => {
			const wert = LehrerAbgangsgrund.data().getWertByKuerzel(this.proxy.abgangsgrund ?? '');
			if (wert === null) {
				return null;
			}
			return LehrerAbgangsgrund.data().getEintragBySchuljahrUndWert(schuleStateImpl.schuljahr, wert);
		},
		set: (value) => this.proxy.abgangsgrund = value?.kuerzel ?? null,
	});

}
