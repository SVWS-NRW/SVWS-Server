import { BerufskollegOrganisationsformen } from '../../../asd/types/schule/BerufskollegOrganisationsformen';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKob02KlassenOrganisationsformBerufsbildend extends Validator {

	/**
	 * Orgaform
	 */
	private readonly _idOrgaform: Supplier<number | null>;

	private static readonly FEHLERTEXT: string = "Organisationsform der Klasse: Der eingetragene Wert für das Feld 'Organisationsform' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Organisationsform im Katalog.
	 *
	 * @param idOrgaform   ID Orgaform
	 * @param kontext      der Kontext des Validators
	 */
	public constructor(idOrgaform: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idOrgaform = idOrgaform;
	}

	protected pruefe(): boolean {
		if (!BerufskollegOrganisationsformen.data().isGueltig(this._idOrgaform.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, ValidatorKob02KlassenOrganisationsformBerufsbildend.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKob02KlassenOrganisationsformBerufsbildend';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKob02KlassenOrganisationsformBerufsbildend', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKob02KlassenOrganisationsformBerufsbildend>('de.svws_nrw.asd.validate.klassen.ValidatorKob02KlassenOrganisationsformBerufsbildend');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKob02KlassenOrganisationsformBerufsbildend(obj: unknown): ValidatorKob02KlassenOrganisationsformBerufsbildend {
	return obj as ValidatorKob02KlassenOrganisationsformBerufsbildend;
}
