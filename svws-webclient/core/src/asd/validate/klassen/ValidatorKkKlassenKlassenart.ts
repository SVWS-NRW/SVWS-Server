import { ValidatorKk00KlassenKlassenart } from '../../../asd/validate/klassen/ValidatorKk00KlassenKlassenart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKkKlassenKlassenart extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idKlassenart   KlassenartID
	 * @param kontext        der Kontext des Validators
	 */
	public constructor(idKlassenart: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorKk00KlassenKlassenart(idKlassenart, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKkKlassenKlassenart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKkKlassenKlassenart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKkKlassenKlassenart>('de.svws_nrw.asd.validate.klassen.ValidatorKkKlassenKlassenart');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKkKlassenKlassenart(obj: unknown): ValidatorKkKlassenKlassenart {
	return obj as ValidatorKkKlassenKlassenart;
}
