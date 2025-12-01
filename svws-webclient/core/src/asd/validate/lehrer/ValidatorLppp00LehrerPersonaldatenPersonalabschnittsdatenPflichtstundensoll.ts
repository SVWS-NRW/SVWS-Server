import { ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { ValidatorLppp03LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLppp03LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll extends Validator {

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
		this._validatoren.add(new ValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(daten, kontext));
		this._validatoren.add(new ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(daten, kontext));
		this._validatoren.add(new ValidatorLppp03LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(daten, kontext));
	}

	protected pruefe(): boolean {
		const pflichtstundensoll: number | null = this.daten.pflichtstundensoll;
		if (pflichtstundensoll === null) {
			this.addFehler(0, "Kein Wert im Feld 'pflichtstundensoll'.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll>('de.svws_nrw.asd.validate.lehrer.ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(obj: unknown): ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll {
	return obj as ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll;
}
