import { JavaObject } from '../../../java/lang/JavaObject';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins extends Validator {

	private readonly _idFoerderschwerpunkt1: Supplier<number | null>;

	private readonly _idFoerderschwerpunkt2: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idFoerderschwerpunkt1   die ID des ersten Förderschwerpunkts
	 * @param idFoerderschwerpunkt2   die ID des zweiten Förderschwerpunkts
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
		if ((idFoerderschwerpunkt1 === null) || (idFoerderschwerpunkt2 === null)) {
			return true;
		}
		if (JavaObject.equalsTranspiler(idFoerderschwerpunkt1, (idFoerderschwerpunkt2))) {
			this.addFehler(0, "Förderschwerpunkt des Schülers: Der erste und der zweite Förderschwerpunkt dürfen nicht identisch sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins>('de.svws_nrw.asd.validate.schueler.ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins(obj: unknown): ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins {
	return obj as ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins;
}
