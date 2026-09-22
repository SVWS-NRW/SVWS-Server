import { ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins } from '../../../asd/validate/schueler/ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Foerderschwerpunkt } from '../../../asd/types/schule/Foerderschwerpunkt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins extends Validator {

	private readonly _idFoerderschwerpunkt1: Supplier<number | null>;


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
		this._validatoren.add(new ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins(idFoerderschwerpunkt1, idFoerderschwerpunkt2, kontext));
	}

	protected pruefe(): boolean {
		const idFoerderschwerpunkt1: number | null = this._idFoerderschwerpunkt1.get();
		if (idFoerderschwerpunkt1 === null) {
			return true;
		}
		if (Foerderschwerpunkt.data().getWertByIDOrNull(idFoerderschwerpunkt1) === null) {
			this.addFehler(0, "Förderschwerpunkt des Schülers: Das Feld 'Förderschwerpunkt' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins>('de.svws_nrw.asd.validate.schueler.ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins(obj: unknown): ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins {
	return obj as ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins;
}
