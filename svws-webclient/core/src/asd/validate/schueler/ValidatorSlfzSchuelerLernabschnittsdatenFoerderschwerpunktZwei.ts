import { ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei } from '../../../asd/validate/schueler/ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlfzSchuelerLernabschnittsdatenFoerderschwerpunktZwei extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idFoerderschwerpunkt1   die ID des ersten Förderschwerpunkts
	 * @param idFoerderschwerpunkt2   die ID des weiteren Förderschwerpunkts
	 * @param kontext                 der Kontext des Validators
	 */
	public constructor(idFoerderschwerpunkt1: Supplier<number | null>, idFoerderschwerpunkt2: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei(idFoerderschwerpunkt1, idFoerderschwerpunkt2, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlfzSchuelerLernabschnittsdatenFoerderschwerpunktZwei';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlfzSchuelerLernabschnittsdatenFoerderschwerpunktZwei', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlfzSchuelerLernabschnittsdatenFoerderschwerpunktZwei>('de.svws_nrw.asd.validate.schueler.ValidatorSlfzSchuelerLernabschnittsdatenFoerderschwerpunktZwei');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlfzSchuelerLernabschnittsdatenFoerderschwerpunktZwei(obj: unknown): ValidatorSlfzSchuelerLernabschnittsdatenFoerderschwerpunktZwei {
	return obj as ValidatorSlfzSchuelerLernabschnittsdatenFoerderschwerpunktZwei;
}
