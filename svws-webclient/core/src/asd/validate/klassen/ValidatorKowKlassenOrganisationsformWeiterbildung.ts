import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorKow00KlassenOrganisationsformWeiterbildung } from '../../../asd/validate/klassen/ValidatorKow00KlassenOrganisationsformWeiterbildung';

export class ValidatorKowKlassenOrganisationsformWeiterbildung extends Validator {


	/**
	 * @param idWeiterbildendOrganisationsform	ID
	 * @param kontext							Kontext
	 */
	public constructor(idWeiterbildendOrganisationsform: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorKow00KlassenOrganisationsformWeiterbildung(idWeiterbildendOrganisationsform, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKowKlassenOrganisationsformWeiterbildung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKowKlassenOrganisationsformWeiterbildung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKowKlassenOrganisationsformWeiterbildung>('de.svws_nrw.asd.validate.klassen.ValidatorKowKlassenOrganisationsformWeiterbildung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKowKlassenOrganisationsformWeiterbildung(obj: unknown): ValidatorKowKlassenOrganisationsformWeiterbildung {
	return obj as ValidatorKowKlassenOrganisationsformWeiterbildung;
}
