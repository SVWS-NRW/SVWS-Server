import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorKob01KlassenOrganisationsformBerufsbildend } from '../../../asd/validate/klassen/ValidatorKob01KlassenOrganisationsformBerufsbildend';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKob00KlassenOrganisationsformBerufsbildend extends Validator {

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
		this._validatoren.add(new ValidatorKob01KlassenOrganisationsformBerufsbildend(this.getNotNullSupplierLong(idOrgaform), kontext));
	}

	protected pruefe(): boolean {
		const idOrgaform: number | null = this._idOrgaform.get();
		if (idOrgaform === null) {
			this.addFehler(0, ValidatorKob00KlassenOrganisationsformBerufsbildend.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKob00KlassenOrganisationsformBerufsbildend';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKob00KlassenOrganisationsformBerufsbildend', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKob00KlassenOrganisationsformBerufsbildend>('de.svws_nrw.asd.validate.klassen.ValidatorKob00KlassenOrganisationsformBerufsbildend');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKob00KlassenOrganisationsformBerufsbildend(obj: unknown): ValidatorKob00KlassenOrganisationsformBerufsbildend {
	return obj as ValidatorKob00KlassenOrganisationsformBerufsbildend;
}
