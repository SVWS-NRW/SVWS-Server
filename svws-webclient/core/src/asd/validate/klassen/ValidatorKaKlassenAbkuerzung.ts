import { ValidatorKa00KlassenAbkuerzung } from '../../../asd/validate/klassen/ValidatorKa00KlassenAbkuerzung';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKaKlassenAbkuerzung extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param kuerzel             Kürzel der Klasse
	 * @param listKuerzel         Liste der Klassenkürzel
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(kuerzel: Supplier<string | null>, listKuerzel: Supplier<List<string> | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorKa00KlassenAbkuerzung(kuerzel, listKuerzel, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKaKlassenAbkuerzung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKaKlassenAbkuerzung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKaKlassenAbkuerzung>('de.svws_nrw.asd.validate.klassen.ValidatorKaKlassenAbkuerzung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKaKlassenAbkuerzung(obj: unknown): ValidatorKaKlassenAbkuerzung {
	return obj as ValidatorKaKlassenAbkuerzung;
}
