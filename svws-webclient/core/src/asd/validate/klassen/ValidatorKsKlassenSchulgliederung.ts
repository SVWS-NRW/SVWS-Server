import { ValidatorKs00KlassenSchulgliederung } from '../../../asd/validate/klassen/ValidatorKs00KlassenSchulgliederung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKsKlassenSchulgliederung extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchulgliederung   SchulgliederungID
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(idSchulgliederung: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorKs00KlassenSchulgliederung(idSchulgliederung, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKsKlassenSchulgliederung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKsKlassenSchulgliederung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKsKlassenSchulgliederung>('de.svws_nrw.asd.validate.klassen.ValidatorKsKlassenSchulgliederung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKsKlassenSchulgliederung(obj: unknown): ValidatorKsKlassenSchulgliederung {
	return obj as ValidatorKsKlassenSchulgliederung;
}
