import { ValidatorSlfzSchuelerLernabschnittsdatenFoerderschwerpunktZwei } from '../../../asd/validate/schueler/ValidatorSlfzSchuelerLernabschnittsdatenFoerderschwerpunktZwei';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorSlfeSchuelerLernabschnittsdatenFoerderschwerpunktEins } from '../../../asd/validate/schueler/ValidatorSlfeSchuelerLernabschnittsdatenFoerderschwerpunktEins';

export class ValidatorSlfSchuelerLernabschnittsdatenFoerderschwerpunkt extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idFoerderschwerpunkt1   die ID des ersten Förderschwerpunktes
	 * @param idFoerderschwerpunkt2   die ID des zweiten Förderschwerpunktes
	 * @param kontext                 der Kontext des Validators
	 */
	public constructor(idFoerderschwerpunkt1: Supplier<number | null>, idFoerderschwerpunkt2: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSlfeSchuelerLernabschnittsdatenFoerderschwerpunktEins(idFoerderschwerpunkt1, idFoerderschwerpunkt2, kontext));
		this._validatoren.add(new ValidatorSlfzSchuelerLernabschnittsdatenFoerderschwerpunktZwei(idFoerderschwerpunkt1, idFoerderschwerpunkt2, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlfSchuelerLernabschnittsdatenFoerderschwerpunkt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlfSchuelerLernabschnittsdatenFoerderschwerpunkt', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlfSchuelerLernabschnittsdatenFoerderschwerpunkt>('de.svws_nrw.asd.validate.schueler.ValidatorSlfSchuelerLernabschnittsdatenFoerderschwerpunkt');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlfSchuelerLernabschnittsdatenFoerderschwerpunkt(obj: unknown): ValidatorSlfSchuelerLernabschnittsdatenFoerderschwerpunkt {
	return obj as ValidatorSlfSchuelerLernabschnittsdatenFoerderschwerpunkt;
}
