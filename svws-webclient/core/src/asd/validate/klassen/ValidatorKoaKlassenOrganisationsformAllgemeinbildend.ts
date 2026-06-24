import { ValidatorKoa00KlassenOrganisationsformAllgemeinbildend } from '../../../asd/validate/klassen/ValidatorKoa00KlassenOrganisationsformAllgemeinbildend';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKoaKlassenOrganisationsformAllgemeinbildend extends Validator {


	/**
	 * @param idAllgemeinbildendOrganisationsform	ID
	 * @param kontext								Kontext
	 */
	public constructor(idAllgemeinbildendOrganisationsform: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorKoa00KlassenOrganisationsformAllgemeinbildend(idAllgemeinbildendOrganisationsform, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKoaKlassenOrganisationsformAllgemeinbildend';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKoaKlassenOrganisationsformAllgemeinbildend', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKoaKlassenOrganisationsformAllgemeinbildend>('de.svws_nrw.asd.validate.klassen.ValidatorKoaKlassenOrganisationsformAllgemeinbildend');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKoaKlassenOrganisationsformAllgemeinbildend(obj: unknown): ValidatorKoaKlassenOrganisationsformAllgemeinbildend {
	return obj as ValidatorKoaKlassenOrganisationsformAllgemeinbildend;
}
