import { ValidatorSlfs10SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung } from '../../../asd/validate/schueler/ValidatorSlfs10SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlfsSchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idFoerderschwerpunkt1           die ID des ersten Förderschwerpunkts
	 * @param hatSchwerbehinderungsNachweis   Schwerbehinderungsnachweis vorhanden
	 * @param kontext                         der Kontext des Validators
	 */
	public constructor(idFoerderschwerpunkt1: Supplier<number | null>, hatSchwerbehinderungsNachweis: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSlfs10SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung(idFoerderschwerpunkt1, hatSchwerbehinderungsNachweis, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlfsSchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlfsSchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlfsSchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung>('de.svws_nrw.asd.validate.schueler.ValidatorSlfsSchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlfsSchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung(obj: unknown): ValidatorSlfsSchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung {
	return obj as ValidatorSlfsSchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung;
}
