import { ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll    das Pflichtstundensoll
	 * @param einsatzstatus    		der Einsatzstatus
	 * @param beschaeftigungsart    Die Beschaeftigungsart
	 * @param kontext   			der Kontext des Validators
	 */
	public constructor(pflichtstundensoll: Supplier<number | null>, einsatzstatus: Supplier<string | null>, beschaeftigungsart: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensoll, einsatzstatus, beschaeftigungsart, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll>('de.svws_nrw.asd.validate.lehrer.ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(obj: unknown): ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll {
	return obj as ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll;
}
