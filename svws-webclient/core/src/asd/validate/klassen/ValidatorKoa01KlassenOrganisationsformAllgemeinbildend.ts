import { ValidatorKoa02KlassenOrganisationsformAllgemeinbildend } from '../../../asd/validate/klassen/ValidatorKoa02KlassenOrganisationsformAllgemeinbildend';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { AllgemeinbildendOrganisationsformen } from '../../../asd/types/schule/AllgemeinbildendOrganisationsformen';

export class ValidatorKoa01KlassenOrganisationsformAllgemeinbildend extends Validator {

	private readonly _idAllgemeinbildendOrganisationsform: Supplier<number>;


	/**
	 * @param idAllgemeinbildendOrganisationsform	ID
	 * @param kontext								Kontext
	 */
	public constructor(idAllgemeinbildendOrganisationsform: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idAllgemeinbildendOrganisationsform = idAllgemeinbildendOrganisationsform;
		this._validatoren.add(new ValidatorKoa02KlassenOrganisationsformAllgemeinbildend(idAllgemeinbildendOrganisationsform, kontext));
	}

	protected pruefe(): boolean {
		const allgemeinbildendOrganisationsform: AllgemeinbildendOrganisationsformen | null = AllgemeinbildendOrganisationsformen.data().getWertByIDOrNull(this._idAllgemeinbildendOrganisationsform.get());
		if (allgemeinbildendOrganisationsform === null) {
			this.addFehler(0, "Organisationsform der Klasse: Das Feld 'Organisationsform' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKoa01KlassenOrganisationsformAllgemeinbildend';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKoa01KlassenOrganisationsformAllgemeinbildend', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKoa01KlassenOrganisationsformAllgemeinbildend>('de.svws_nrw.asd.validate.klassen.ValidatorKoa01KlassenOrganisationsformAllgemeinbildend');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKoa01KlassenOrganisationsformAllgemeinbildend(obj: unknown): ValidatorKoa01KlassenOrganisationsformAllgemeinbildend {
	return obj as ValidatorKoa01KlassenOrganisationsformAllgemeinbildend;
}
