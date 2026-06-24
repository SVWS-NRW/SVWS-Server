import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { AllgemeinbildendOrganisationsformen } from '../../../asd/types/schule/AllgemeinbildendOrganisationsformen';

export class ValidatorKoa02KlassenOrganisationsformAllgemeinbildend extends Validator {

	private readonly _idAllgemeinbildendOrganisationsform: Supplier<number>;


	/**
	 * @param idAllgemeinbildendOrganisationsform 	ID
	 * @param kontext								Kontext
	 */
	public constructor(idAllgemeinbildendOrganisationsform: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idAllgemeinbildendOrganisationsform = idAllgemeinbildendOrganisationsform;
	}

	protected pruefe(): boolean {
		if (!AllgemeinbildendOrganisationsformen.data().isGueltig(this._idAllgemeinbildendOrganisationsform.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, "Organisationsform der Klasse: Der eingetragene Wert für das Feld 'Organisationsform' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKoa02KlassenOrganisationsformAllgemeinbildend';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKoa02KlassenOrganisationsformAllgemeinbildend', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKoa02KlassenOrganisationsformAllgemeinbildend>('de.svws_nrw.asd.validate.klassen.ValidatorKoa02KlassenOrganisationsformAllgemeinbildend');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKoa02KlassenOrganisationsformAllgemeinbildend(obj: unknown): ValidatorKoa02KlassenOrganisationsformAllgemeinbildend {
	return obj as ValidatorKoa02KlassenOrganisationsformAllgemeinbildend;
}
