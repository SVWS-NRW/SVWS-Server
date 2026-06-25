import { ValidatorKob02KlassenOrganisationsformBerufsbildend } from '../../../asd/validate/klassen/ValidatorKob02KlassenOrganisationsformBerufsbildend';
import { BerufskollegOrganisationsformen } from '../../../asd/types/schule/BerufskollegOrganisationsformen';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKob01KlassenOrganisationsformBerufsbildend extends Validator {

	/**
	 * Orgaform
	 */
	private readonly _idOrgaform: Supplier<number>;

	private static readonly FEHLERTEXT: string = "Organisationsform der Klasse: Das Feld 'Organisationsform' muss zulässig sein.";


	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Organisationsform im Katalog.
	 *
	 * @param idOrgaform   ID Orgaform
	 * @param kontext      der Kontext des Validators
	 */
	public constructor(idOrgaform: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idOrgaform = idOrgaform;
		this._validatoren.add(new ValidatorKob02KlassenOrganisationsformBerufsbildend(idOrgaform, kontext));
	}

	protected pruefe(): boolean {
		const idOrgaform: number = this._idOrgaform.get();
		const oForm: BerufskollegOrganisationsformen | null = BerufskollegOrganisationsformen.data().getWertByIDOrNull(idOrgaform);
		if (oForm === null) {
			this.addFehler(0, ValidatorKob01KlassenOrganisationsformBerufsbildend.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKob01KlassenOrganisationsformBerufsbildend';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKob01KlassenOrganisationsformBerufsbildend', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKob01KlassenOrganisationsformBerufsbildend>('de.svws_nrw.asd.validate.klassen.ValidatorKob01KlassenOrganisationsformBerufsbildend');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKob01KlassenOrganisationsformBerufsbildend(obj: unknown): ValidatorKob01KlassenOrganisationsformBerufsbildend {
	return obj as ValidatorKob01KlassenOrganisationsformBerufsbildend;
}
