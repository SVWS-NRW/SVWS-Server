import { ValidatorKl10KlassenKlassenleitung } from '../../../asd/validate/klassen/ValidatorKl10KlassenKlassenleitung';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKlKlassenKlassenleitung extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param klassenLeitungen   Klassenleitungen
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(klassenLeitungen: Supplier<List<number>>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorKl10KlassenKlassenleitung(klassenLeitungen, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKlKlassenKlassenleitung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKlKlassenKlassenleitung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKlKlassenKlassenleitung>('de.svws_nrw.asd.validate.klassen.ValidatorKlKlassenKlassenleitung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKlKlassenKlassenleitung(obj: unknown): ValidatorKlKlassenKlassenleitung {
	return obj as ValidatorKlKlassenKlassenleitung;
}
