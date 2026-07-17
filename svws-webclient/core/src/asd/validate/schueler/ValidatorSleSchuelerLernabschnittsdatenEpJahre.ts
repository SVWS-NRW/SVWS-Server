import { ValidatorSle00SchuelerLernabschnittsdatenEpJahre } from '../../../asd/validate/schueler/ValidatorSle00SchuelerLernabschnittsdatenEpJahre';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSleSchuelerLernabschnittsdatenEpJahre extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idEpJahre   EP-JahreID
	 * @param kontext     der Kontext des Validators
	 */
	public constructor(idEpJahre: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSle00SchuelerLernabschnittsdatenEpJahre(idEpJahre, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSleSchuelerLernabschnittsdatenEpJahre';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSleSchuelerLernabschnittsdatenEpJahre', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSleSchuelerLernabschnittsdatenEpJahre>('de.svws_nrw.asd.validate.schueler.ValidatorSleSchuelerLernabschnittsdatenEpJahre');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSleSchuelerLernabschnittsdatenEpJahre(obj: unknown): ValidatorSleSchuelerLernabschnittsdatenEpJahre {
	return obj as ValidatorSleSchuelerLernabschnittsdatenEpJahre;
}
