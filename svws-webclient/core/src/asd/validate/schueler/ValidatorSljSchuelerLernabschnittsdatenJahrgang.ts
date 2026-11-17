import { ValidatorSlj00SchuelerLernabschnittsdatenJahrgang } from '../../../asd/validate/schueler/ValidatorSlj00SchuelerLernabschnittsdatenJahrgang';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSljSchuelerLernabschnittsdatenJahrgang extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param jahrgangID         die ID des Jahrgangs des Schülers
	 * @param idListJahrgaenge   die Liste aller Jahrgänge der Schule
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(jahrgangID: Supplier<number | null>, idListJahrgaenge: Supplier<List<number>>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSlj00SchuelerLernabschnittsdatenJahrgang(jahrgangID, idListJahrgaenge, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSljSchuelerLernabschnittsdatenJahrgang';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSljSchuelerLernabschnittsdatenJahrgang', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSljSchuelerLernabschnittsdatenJahrgang>('de.svws_nrw.asd.validate.schueler.ValidatorSljSchuelerLernabschnittsdatenJahrgang');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSljSchuelerLernabschnittsdatenJahrgang(obj: unknown): ValidatorSljSchuelerLernabschnittsdatenJahrgang {
	return obj as ValidatorSljSchuelerLernabschnittsdatenJahrgang;
}
