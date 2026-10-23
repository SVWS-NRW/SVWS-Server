import { ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend } from '../../../asd/validate/schueler/ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { AllgemeinbildendOrganisationsformen } from '../../../asd/types/schule/AllgemeinbildendOrganisationsformen';

export class ValidatorSloa01SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend extends Validator {

	/**
	 * Die Organisationsform ID des Schuelers
	 */
	private readonly idOrganisationsform: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator für die Prüfung.
	 *
	 * @param schuljahr              Schuljahr
	 * @param idOrganisationsform    Organisationsform ID
	 * @param kontext                der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, idOrganisationsform: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this.idOrganisationsform = idOrganisationsform;
		this._validatoren.add(new ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend(schuljahr, idOrganisationsform, kontext));
	}

	protected pruefe(): boolean {
		const sa: AllgemeinbildendOrganisationsformen | null = AllgemeinbildendOrganisationsformen.data().getWertByIDOrNull(this.idOrganisationsform.get());
		if (sa === null) {
			this.addFehler(0, "Organisationsform des Schülers: Das Feld 'Organisationsform' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSloa01SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSloa01SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSloa01SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend>('de.svws_nrw.asd.validate.schueler.ValidatorSloa01SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSloa01SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend(obj: unknown): ValidatorSloa01SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend {
	return obj as ValidatorSloa01SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend;
}
