import { ValidatorLpp01LehrerPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLpp01LehrerPersonalabschnittsdatenPflichtstundensoll';
import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { ValidatorLpp02LehrerPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLpp02LehrerPersonalabschnittsdatenPflichtstundensoll';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLpp03LehrerPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLpp03LehrerPersonalabschnittsdatenPflichtstundensoll';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpp00LehrerPersonalabschnittsdatenPflichtstundensoll extends Validator {

	/**
	 * Die Lehrer-Personalabschnittsdaten
	 */
	private readonly daten: LehrerPersonalabschnittsdaten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: LehrerPersonalabschnittsdaten, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorLpp01LehrerPersonalabschnittsdatenPflichtstundensoll(daten, kontext));
		this._validatoren.add(new ValidatorLpp02LehrerPersonalabschnittsdatenPflichtstundensoll(daten, kontext));
		this._validatoren.add(new ValidatorLpp03LehrerPersonalabschnittsdatenPflichtstundensoll(daten, kontext));
	}

	protected pruefe(): boolean {
		let success: boolean = true;
		const pflichtstundensoll: number | null = this.daten.pflichtstundensoll;
		success = this.exec(0, { getAsBoolean: () => pflichtstundensoll === null }, "Kein Wert im Feld 'pflichtstundensoll'.");
		if (!success)
			return false;
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpp00LehrerPersonalabschnittsdatenPflichtstundensoll';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLpp00LehrerPersonalabschnittsdatenPflichtstundensoll', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLpp00LehrerPersonalabschnittsdatenPflichtstundensoll>('de.svws_nrw.asd.validate.lehrer.ValidatorLpp00LehrerPersonalabschnittsdatenPflichtstundensoll');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpp00LehrerPersonalabschnittsdatenPflichtstundensoll(obj: unknown): ValidatorLpp00LehrerPersonalabschnittsdatenPflichtstundensoll {
	return obj as ValidatorLpp00LehrerPersonalabschnittsdatenPflichtstundensoll;
}
