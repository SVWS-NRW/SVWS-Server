import { ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination } from '../../../asd/validate/lehrer/ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpppkLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll     das Pflichtstundensoll des Lehrers
	 * @param idEinsatzstatus        die Katalog-ID des Einsatzstatus des Lehrers
	 * @param idBeschaeftigungsart   die Katalog-ID der Beschäftigungsart des Lehrers
	 * @param kontext                der Kontext des Validators
	 */
	public constructor(pflichtstundensoll: Supplier<number | null>, idEinsatzstatus: Supplier<number | null>, idBeschaeftigungsart: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination(pflichtstundensoll, idEinsatzstatus, idBeschaeftigungsart, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpppkLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLpppkLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpppkLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination>('de.svws_nrw.asd.validate.lehrer.ValidatorLpppkLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpppkLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination(obj: unknown): ValidatorLpppkLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination {
	return obj as ValidatorLpppkLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination;
}
