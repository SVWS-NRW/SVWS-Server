import { ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
import { ValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll extends Validator {

	/**
	 * Das Pflichtstundensoll
	 */
	private readonly pflichtstundensoll: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll    das Pflichtstundensoll
	 * @param einsatzstatus    		Der Einsatzstatus
	 * @param beschaeftigungsart    Die Beschaeftigungsart
	 * @param kontext   			der Kontext des Validators
	 */
	public constructor(pflichtstundensoll: Supplier<number | null>, einsatzstatus: Supplier<string | null>, beschaeftigungsart: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.pflichtstundensoll = pflichtstundensoll;
		this._validatoren.add(new ValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensoll, kontext));
		this._validatoren.add(new ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensoll, einsatzstatus, kontext));
		this._validatoren.add(new ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensoll, einsatzstatus, beschaeftigungsart, kontext));
	}

	protected pruefe(): boolean {
		const pflichtstundensoll: number | null = this.pflichtstundensoll.get();
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
