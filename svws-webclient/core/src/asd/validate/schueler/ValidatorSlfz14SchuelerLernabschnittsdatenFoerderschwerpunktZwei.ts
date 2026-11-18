import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Foerderschwerpunkt } from '../../../asd/types/schule/Foerderschwerpunkt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlfz14SchuelerLernabschnittsdatenFoerderschwerpunktZwei extends Validator {

	private readonly _idFoerderschwerpunkt1: Supplier<number | null>;

	private readonly _idFoerderschwerpunkt2: Supplier<number | null>;

	private static readonly FEHLERTEXT: string = "Förderschwerpunkt des Schülers: Der angegebene Förderschwerpunkt 'Klinikschule' ist nicht in Kombination mit anderen Förderschwerpunkten zulässig.";


	/**
	 * Erstellt einen neuen Validator zur Überprüfung des Förderschwerpunkts Klinikschule (KR)
	 * in Kombination mit einem weiteren Förderschwerpunkt.
	 *
	 * @param idFoerderschwerpunkt1   die Katalog-ID des ersten Förderschwerpunkts
	 * @param idFoerderschwerpunkt2   die Katalog-ID des weiteren Förderschwerpunkts
	 * @param kontext                 der Kontext des Validators
	 */
	public constructor(idFoerderschwerpunkt1: Supplier<number | null>, idFoerderschwerpunkt2: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idFoerderschwerpunkt1 = idFoerderschwerpunkt1;
		this._idFoerderschwerpunkt2 = idFoerderschwerpunkt2;
	}

	protected pruefe(): boolean {
		const id1: number | null = this._idFoerderschwerpunkt1.get();
		const id2: number | null = this._idFoerderschwerpunkt2.get();
		if ((id1 === null) || (id2 === null)) {
			return true;
		}
		const fs1: Foerderschwerpunkt | null = Foerderschwerpunkt.data().getWertByIDOrNull(id1);
		if (fs1 as unknown === Foerderschwerpunkt.KR as unknown) {
			this.addFehler(0, ValidatorSlfz14SchuelerLernabschnittsdatenFoerderschwerpunktZwei.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlfz14SchuelerLernabschnittsdatenFoerderschwerpunktZwei';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlfz14SchuelerLernabschnittsdatenFoerderschwerpunktZwei', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlfz14SchuelerLernabschnittsdatenFoerderschwerpunktZwei>('de.svws_nrw.asd.validate.schueler.ValidatorSlfz14SchuelerLernabschnittsdatenFoerderschwerpunktZwei');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlfz14SchuelerLernabschnittsdatenFoerderschwerpunktZwei(obj: unknown): ValidatorSlfz14SchuelerLernabschnittsdatenFoerderschwerpunktZwei {
	return obj as ValidatorSlfz14SchuelerLernabschnittsdatenFoerderschwerpunktZwei;
}
