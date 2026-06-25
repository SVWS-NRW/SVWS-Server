import { ValidatorKow02KlassenOrganisationsformWeiterbildung } from '../../../asd/validate/klassen/ValidatorKow02KlassenOrganisationsformWeiterbildung';
import { WeiterbildungskollegOrganisationsformen } from '../../../asd/types/schule/WeiterbildungskollegOrganisationsformen';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKow01KlassenOrganisationsformWeiterbildung extends Validator {

	private readonly _idWeiterbildendOrganisationsform: Supplier<number>;


	/**
	 * @param idWeiterbildendOrganisationsform	ID
	 * @param kontext							Kontext
	 */
	public constructor(idWeiterbildendOrganisationsform: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idWeiterbildendOrganisationsform = idWeiterbildendOrganisationsform;
		this._validatoren.add(new ValidatorKow02KlassenOrganisationsformWeiterbildung(idWeiterbildendOrganisationsform, kontext));
	}

	protected pruefe(): boolean {
		const idWeiterbildendOrganisationsform: WeiterbildungskollegOrganisationsformen | null = WeiterbildungskollegOrganisationsformen.data().getWertByIDOrNull(this._idWeiterbildendOrganisationsform.get());
		if (idWeiterbildendOrganisationsform === null) {
			this.addFehler(0, "Organisationsform der Klasse: Das Feld 'Organisationsform' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKow01KlassenOrganisationsformWeiterbildung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKow01KlassenOrganisationsformWeiterbildung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKow01KlassenOrganisationsformWeiterbildung>('de.svws_nrw.asd.validate.klassen.ValidatorKow01KlassenOrganisationsformWeiterbildung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKow01KlassenOrganisationsformWeiterbildung(obj: unknown): ValidatorKow01KlassenOrganisationsformWeiterbildung {
	return obj as ValidatorKow01KlassenOrganisationsformWeiterbildung;
}
