import type { JahrgangsDaten, KlassenDaten } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { ValidatorInputRequired } from "@ui";

export class ValidatorSchuelerLernabschnittKlasseUndJahrgang extends BasicValidator {

	private readonly getKlasse: () => KlassenDaten | null;
	private readonly getJahrgang: () => JahrgangsDaten | null;

	constructor(getKlasse: () => KlassenDaten | null, getJahrgang: () => JahrgangsDaten | null) {
		super(ValidatorFehlerart.MUSS);
		this.getKlasse = getKlasse;
		this.getJahrgang = getJahrgang;
		this._validatoren.add(new ValidatorInputRequired(() => getKlasse()?.id));
	}

	protected pruefe(): boolean	{
		if ((this.getKlasse() === null) || (this.getJahrgang() === null)) {
			return true;
		}

		// Jahrgangsübergreifende Klasse
		if (this.istKlasseJahrgangsuebergreifend()) {
			return true;
		}

		if (this.getKlasse()?.idJahrgang !== this.getJahrgang()?.id) {
			this.addFehler(1, "Die Kombination aus Klasse und Jahrgang ist nicht gültig.");
		}

		return this.getFehler().isEmpty();
	}

	private istKlasseJahrgangsuebergreifend() {
		return this.getKlasse()?.idJahrgang === null;
	}
}
