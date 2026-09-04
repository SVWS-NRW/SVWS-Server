import type { LehrerStammdaten } from "@core/asd/data/lehrer/LehrerStammdaten";
import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorLehrerIndividualdatenKuerzel extends BasicValidator {

	private readonly lehrer: () => LehrerStammdaten;
	private readonly lehrerListe: () => Iterable<LehrerListeEintrag>;

	constructor(lehrer: () => LehrerStammdaten, lehrerListe: () => Iterable<LehrerListeEintrag>) {
		super(ValidatorFehlerart.MUSS);
		this.lehrerListe = lehrerListe;
		this.lehrer = lehrer;
		this._validatoren.add(new ValidatorStringLength(() => lehrer().kuerzel, null, 10));
		this._validatoren.add(new ValidatorInputRequired(() => lehrer().kuerzel));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => lehrer().kuerzel, StringPattern.NO_WHITESPACES));
	}

	protected pruefe(): boolean {
		for (const lehrer of this.lehrerListe()) {
			const sameID = lehrer.id === this.lehrer().id;
			const sameKuerzel = lehrer.kuerzel.toLocaleLowerCase() === this.lehrer().kuerzel.toLocaleLowerCase();
			if (!sameID && sameKuerzel) {
				this.addFehler(0, "Der Wert für das Kürzel ist bereits vergeben");
				return false;
			}
		}
		return true;
	}
}
