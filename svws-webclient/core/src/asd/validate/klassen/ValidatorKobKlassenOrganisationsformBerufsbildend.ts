import { ValidatorKob00KlassenOrganisationsformBerufsbildend } from '../../../asd/validate/klassen/ValidatorKob00KlassenOrganisationsformBerufsbildend';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKobKlassenOrganisationsformBerufsbildend extends Validator {

	/**
	 * Orgaform
	 */
	private readonly _idOrgaform: Supplier<number | null>;

	private static readonly FEHLERTEXT: string = "Organisationsform der Klasse: Kein Wert vorhanden.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idOrgaform  die Organisationsform
	 * @param kontext     der Kontext des Validators
	 */
	public constructor(idOrgaform: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idOrgaform = idOrgaform;
		this._validatoren.add(new ValidatorKob00KlassenOrganisationsformBerufsbildend(this.getNotNullSupplierLong(idOrgaform), kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKobKlassenOrganisationsformBerufsbildend';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKobKlassenOrganisationsformBerufsbildend', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKobKlassenOrganisationsformBerufsbildend>('de.svws_nrw.asd.validate.klassen.ValidatorKobKlassenOrganisationsformBerufsbildend');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKobKlassenOrganisationsformBerufsbildend(obj: unknown): ValidatorKobKlassenOrganisationsformBerufsbildend {
	return obj as ValidatorKobKlassenOrganisationsformBerufsbildend;
}
