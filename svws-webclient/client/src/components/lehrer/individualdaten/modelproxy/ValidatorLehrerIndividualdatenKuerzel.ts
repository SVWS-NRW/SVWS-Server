import { StringPattern, ValidatorInputRequired, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { LehrerListeEintrag, LehrerStammdaten } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";

export class ValidatorLehrerIndividualdatenKuerzel extends BasicValidator {

	private readonly lehrerListe: () => Iterable<LehrerListeEintrag>;
	private readonly currentLehrer: () => LehrerStammdaten;

	constructor(lehrer: () => LehrerStammdaten, lehrerListe: () => Iterable<LehrerListeEintrag>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringLength(() => lehrer().kuerzel, null, 10));
		this._validatoren.add(new ValidatorInputRequired(() => lehrer().kuerzel));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => lehrer().kuerzel, StringPattern.NO_WHITESPACES));
		this.lehrerListe = lehrerListe;
		this.currentLehrer = lehrer;
	}

	protected pruefe(): boolean {
		for (const lehrer of this.lehrerListe()) {
			const sameID = lehrer.id === this.currentLehrer().id;
			const sameKuerzel = lehrer.kuerzel.toLocaleLowerCase() === this.currentLehrer().kuerzel.toLocaleLowerCase();
			if (!sameID && sameKuerzel) {
				this.addFehler(0, "Der Wert für das Kürzel ist bereits vergeben");
				return false;
			}
		}
		return true;
	}
}
