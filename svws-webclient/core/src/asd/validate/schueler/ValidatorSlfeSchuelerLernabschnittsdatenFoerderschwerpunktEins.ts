import { ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins } from '../../../asd/validate/schueler/ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins';
import { ValidatorSlfe11SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung } from '../../../asd/validate/schueler/ValidatorSlfe11SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlfeSchuelerLernabschnittsdatenFoerderschwerpunktEins extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idFoerderschwerpunkt1           die ID des ersten Förderschwerpunkts
	 * @param idFoerderschwerpunkt2           die ID des zweiten Förderschwerpunkts
	 * @param hatSchwerbehinderungsNachweis   Schwerbehinderungsnachweis vorhanden
	 * @param kontext                         der Kontext des Validators
	 */
	public constructor(idFoerderschwerpunkt1: Supplier<number | null>, idFoerderschwerpunkt2: Supplier<number | null>, hatSchwerbehinderungsNachweis: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins(idFoerderschwerpunkt1, idFoerderschwerpunkt2, kontext));
		this._validatoren.add(new ValidatorSlfe11SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung(idFoerderschwerpunkt1, hatSchwerbehinderungsNachweis, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlfeSchuelerLernabschnittsdatenFoerderschwerpunktEins';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlfeSchuelerLernabschnittsdatenFoerderschwerpunktEins', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlfeSchuelerLernabschnittsdatenFoerderschwerpunktEins>('de.svws_nrw.asd.validate.schueler.ValidatorSlfeSchuelerLernabschnittsdatenFoerderschwerpunktEins');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlfeSchuelerLernabschnittsdatenFoerderschwerpunktEins(obj: unknown): ValidatorSlfeSchuelerLernabschnittsdatenFoerderschwerpunktEins {
	return obj as ValidatorSlfeSchuelerLernabschnittsdatenFoerderschwerpunktEins;
}
