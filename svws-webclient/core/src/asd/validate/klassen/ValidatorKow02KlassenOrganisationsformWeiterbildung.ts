import { WeiterbildungskollegOrganisationsformen } from '../../../asd/types/schule/WeiterbildungskollegOrganisationsformen';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKow02KlassenOrganisationsformWeiterbildung extends Validator {

	private readonly _idWeiterbildendOrganisationsform: Supplier<number>;


	/**
	 * @param idWeiterbildendOrganisationsform 	ID
	 * @param kontext							Kontext
	 */
	public constructor(idWeiterbildendOrganisationsform: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idWeiterbildendOrganisationsform = idWeiterbildendOrganisationsform;
	}

	protected pruefe(): boolean {
		if (!WeiterbildungskollegOrganisationsformen.data().isGueltig(this._idWeiterbildendOrganisationsform.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, "Organisationsform der Klasse: Der eingetragene Wert für das Feld 'Organisationsform' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKow02KlassenOrganisationsformWeiterbildung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKow02KlassenOrganisationsformWeiterbildung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKow02KlassenOrganisationsformWeiterbildung>('de.svws_nrw.asd.validate.klassen.ValidatorKow02KlassenOrganisationsformWeiterbildung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKow02KlassenOrganisationsformWeiterbildung(obj: unknown): ValidatorKow02KlassenOrganisationsformWeiterbildung {
	return obj as ValidatorKow02KlassenOrganisationsformWeiterbildung;
}
