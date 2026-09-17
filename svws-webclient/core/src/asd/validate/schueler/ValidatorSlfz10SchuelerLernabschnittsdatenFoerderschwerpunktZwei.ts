import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlfz10SchuelerLernabschnittsdatenFoerderschwerpunktZwei extends Validator {

	private readonly _idFoerderschwerpunkt1: Supplier<number | null>;

	private readonly _idFoerderschwerpunkt2: Supplier<number | null>;

	private static readonly FEHLERTEXT: string = "Weiterer Förderschwerpunkt des Schülers: Das Feld 'Weiterer Förderschwerpunkt' darf nicht befüllt sein, wenn das Feld 'Förderschwerpunkt' leer ist.";


	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Abhängigkeit vom ersten Förderschwerpunkt.
	 *
	 * @param idFoerderschwerpunkt1   die ID des ersten Förderschwerpunkts
	 * @param idFoerderschwerpunkt2   die ID des weiteren Förderschwerpunkts
	 * @param kontext                 der Kontext des Validators
	 */
	public constructor(idFoerderschwerpunkt1: Supplier<number | null>, idFoerderschwerpunkt2: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idFoerderschwerpunkt1 = idFoerderschwerpunkt1;
		this._idFoerderschwerpunkt2 = idFoerderschwerpunkt2;
	}

	protected pruefe(): boolean {
		const idFoerderschwerpunkt1: number | null = this._idFoerderschwerpunkt1.get();
		const idFoerderschwerpunkt2: number | null = this._idFoerderschwerpunkt2.get();
		if ((idFoerderschwerpunkt1 === null) && (idFoerderschwerpunkt2 !== null)) {
			this.addFehler(0, ValidatorSlfz10SchuelerLernabschnittsdatenFoerderschwerpunktZwei.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlfz10SchuelerLernabschnittsdatenFoerderschwerpunktZwei';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlfz10SchuelerLernabschnittsdatenFoerderschwerpunktZwei', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlfz10SchuelerLernabschnittsdatenFoerderschwerpunktZwei>('de.svws_nrw.asd.validate.schueler.ValidatorSlfz10SchuelerLernabschnittsdatenFoerderschwerpunktZwei');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlfz10SchuelerLernabschnittsdatenFoerderschwerpunktZwei(obj: unknown): ValidatorSlfz10SchuelerLernabschnittsdatenFoerderschwerpunktZwei {
	return obj as ValidatorSlfz10SchuelerLernabschnittsdatenFoerderschwerpunktZwei;
}
