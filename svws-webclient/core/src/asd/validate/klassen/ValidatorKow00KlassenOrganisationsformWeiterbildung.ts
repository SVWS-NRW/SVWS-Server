import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorKow01KlassenOrganisationsformWeiterbildung } from '../../../asd/validate/klassen/ValidatorKow01KlassenOrganisationsformWeiterbildung';

export class ValidatorKow00KlassenOrganisationsformWeiterbildung extends Validator {

	private readonly _idWeiterbildendOrganisationsform: Supplier<number | null>;


	/**
	 * @param idWeiterbildendOrganisationsform	ID
	 * @param kontext							Kontext
	 */
	public constructor(idWeiterbildendOrganisationsform: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idWeiterbildendOrganisationsform = idWeiterbildendOrganisationsform;
		this._validatoren.add(new ValidatorKow01KlassenOrganisationsformWeiterbildung(this.getNotNullSupplierLong(idWeiterbildendOrganisationsform), kontext));
	}

	protected pruefe(): boolean {
		if (null === this._idWeiterbildendOrganisationsform.get()) {
			this.addFehler(0, "Organisationsform der Klasse: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKow00KlassenOrganisationsformWeiterbildung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKow00KlassenOrganisationsformWeiterbildung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKow00KlassenOrganisationsformWeiterbildung>('de.svws_nrw.asd.validate.klassen.ValidatorKow00KlassenOrganisationsformWeiterbildung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKow00KlassenOrganisationsformWeiterbildung(obj: unknown): ValidatorKow00KlassenOrganisationsformWeiterbildung {
	return obj as ValidatorKow00KlassenOrganisationsformWeiterbildung;
}
