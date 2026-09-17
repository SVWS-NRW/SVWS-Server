import { ValidatorSlfz10SchuelerLernabschnittsdatenFoerderschwerpunktZwei } from '../../../asd/validate/schueler/ValidatorSlfz10SchuelerLernabschnittsdatenFoerderschwerpunktZwei';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Foerderschwerpunkt } from '../../../asd/types/schule/Foerderschwerpunkt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei extends Validator {

	private readonly _idFoerderschwerpunkt1: Supplier<number | null>;

	private readonly _idFoerderschwerpunkt2: Supplier<number | null>;

	private static readonly FEHLERTEXT: string = "Weiterer Förderschwerpunkt des Schülers: Das Feld 'Weiterer Förderschwerpunkt' muss zulässig sein.";


	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Zulässigkeit des weiteren Förderschwerpunkts.
	 *
	 * @param idFoerderschwerpunkt1   die ID des ersten Förderschwerpunkts
	 * @param idFoerderschwerpunkt2   die ID des weiteren Förderschwerpunkts
	 * @param kontext                 der Kontext des Validators
	 */
	public constructor(idFoerderschwerpunkt1: Supplier<number | null>, idFoerderschwerpunkt2: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idFoerderschwerpunkt1 = idFoerderschwerpunkt1;
		this._idFoerderschwerpunkt2 = idFoerderschwerpunkt2;
		this._validatoren.add(new ValidatorSlfz10SchuelerLernabschnittsdatenFoerderschwerpunktZwei(idFoerderschwerpunkt1, idFoerderschwerpunkt2, kontext));
	}

	protected pruefe(): boolean {
		const idFoerderschwerpunkt2: number | null = this._idFoerderschwerpunkt2.get();
		if (idFoerderschwerpunkt2 === null) {
			return true;
		}
		if (Foerderschwerpunkt.data().getWertByIDOrNull(idFoerderschwerpunkt2) === null) {
			this.addFehler(0, ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei>('de.svws_nrw.asd.validate.schueler.ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei(obj: unknown): ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei {
	return obj as ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei;
}
