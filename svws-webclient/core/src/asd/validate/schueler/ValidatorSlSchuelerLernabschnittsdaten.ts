import { ValidatorSleSchuelerLernabschnittsdatenEpJahre } from '../../../asd/validate/schueler/ValidatorSleSchuelerLernabschnittsdatenEpJahre';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorSlkSchuelerLernabschnittsdatenKlassenart } from '../../../asd/validate/schueler/ValidatorSlkSchuelerLernabschnittsdatenKlassenart';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlSchuelerLernabschnittsdaten extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idKlassenart  die Klassenart ID
	 * @param idEpJahre     die EPJahre ID
	 * @param kontext       der Kontext des Validators
	 */
	public constructor(idKlassenart: Supplier<number | null>, idEpJahre: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSlkSchuelerLernabschnittsdatenKlassenart(idKlassenart, kontext));
		this._validatoren.add(new ValidatorSleSchuelerLernabschnittsdatenEpJahre(idEpJahre, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlSchuelerLernabschnittsdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlSchuelerLernabschnittsdaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlSchuelerLernabschnittsdaten>('de.svws_nrw.asd.validate.schueler.ValidatorSlSchuelerLernabschnittsdaten');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlSchuelerLernabschnittsdaten(obj: unknown): ValidatorSlSchuelerLernabschnittsdaten {
	return obj as ValidatorSlSchuelerLernabschnittsdaten;
}
