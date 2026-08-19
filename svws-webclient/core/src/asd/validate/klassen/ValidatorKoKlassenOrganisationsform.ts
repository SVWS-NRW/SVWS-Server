import { ValidatorKoaKlassenOrganisationsformAllgemeinbildend } from '../../../asd/validate/klassen/ValidatorKoaKlassenOrganisationsformAllgemeinbildend';
import { ValidatorKowKlassenOrganisationsformWeiterbildung } from '../../../asd/validate/klassen/ValidatorKowKlassenOrganisationsformWeiterbildung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorKobKlassenOrganisationsformBerufsbildend } from '../../../asd/validate/klassen/ValidatorKobKlassenOrganisationsformBerufsbildend';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKoKlassenOrganisationsform extends Validator {


	/**
	 * @param idAllgemeinbildendOrganisationsform	ID Orgaform A-Schulen
	 * @param idWeiterbildendOrganisationsform	    ID Orgaform WBK-Schulen
	 * @param idBerufsbildendOrganisationsform	    ID Orgaform BK-Schulen
	 * @param kontext							    Kontext
	 */
	public constructor(idAllgemeinbildendOrganisationsform: Supplier<number | null>, idWeiterbildendOrganisationsform: Supplier<number | null>, idBerufsbildendOrganisationsform: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorKoaKlassenOrganisationsformAllgemeinbildend(idAllgemeinbildendOrganisationsform, kontext));
		this._validatoren.add(new ValidatorKowKlassenOrganisationsformWeiterbildung(idWeiterbildendOrganisationsform, kontext));
		this._validatoren.add(new ValidatorKobKlassenOrganisationsformBerufsbildend(idBerufsbildendOrganisationsform, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKoKlassenOrganisationsform';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKoKlassenOrganisationsform', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKoKlassenOrganisationsform>('de.svws_nrw.asd.validate.klassen.ValidatorKoKlassenOrganisationsform');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKoKlassenOrganisationsform(obj: unknown): ValidatorKoKlassenOrganisationsform {
	return obj as ValidatorKoKlassenOrganisationsform;
}
