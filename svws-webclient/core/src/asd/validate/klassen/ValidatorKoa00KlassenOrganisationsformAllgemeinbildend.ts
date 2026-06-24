import { ValidatorKoa01KlassenOrganisationsformAllgemeinbildend } from '../../../asd/validate/klassen/ValidatorKoa01KlassenOrganisationsformAllgemeinbildend';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKoa00KlassenOrganisationsformAllgemeinbildend extends Validator {

	private readonly _idAllgemeinbildendOrganisationsform: Supplier<number | null>;


	/**
	 * @param idAllgemeinbildendOrganisationsform	ID
	 * @param kontext								Kontext
	 */
	public constructor(idAllgemeinbildendOrganisationsform: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idAllgemeinbildendOrganisationsform = idAllgemeinbildendOrganisationsform;
		this._validatoren.add(new ValidatorKoa01KlassenOrganisationsformAllgemeinbildend(this.getNotNullSupplierLong(idAllgemeinbildendOrganisationsform), kontext));
	}

	protected pruefe(): boolean {
		if (null === this._idAllgemeinbildendOrganisationsform.get()) {
			this.addFehler(0, "Organisationsform der Klasse: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKoa00KlassenOrganisationsformAllgemeinbildend';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKoa00KlassenOrganisationsformAllgemeinbildend', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKoa00KlassenOrganisationsformAllgemeinbildend>('de.svws_nrw.asd.validate.klassen.ValidatorKoa00KlassenOrganisationsformAllgemeinbildend');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKoa00KlassenOrganisationsformAllgemeinbildend(obj: unknown): ValidatorKoa00KlassenOrganisationsformAllgemeinbildend {
	return obj as ValidatorKoa00KlassenOrganisationsformAllgemeinbildend;
}
