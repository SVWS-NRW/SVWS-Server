import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { AllgemeinbildendOrganisationsformen } from '../../../asd/types/schule/AllgemeinbildendOrganisationsformen';

export class ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend extends Validator {

	/**
	 * Das aktuelle Schuljahr des Schülers
	 */
	private readonly schuljahr: Supplier<number>;

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
		this.schuljahr = schuljahr;
		this.idOrganisationsform = idOrganisationsform;
	}

	protected pruefe(): boolean {
		if (!AllgemeinbildendOrganisationsformen.data().isGueltig(this.idOrganisationsform.get(), this.schuljahr.get())) {
			this.addFehler(0, "Organisationsform des Schülers: Der eingetragene Wert für das Feld 'Organisationsform' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend>('de.svws_nrw.asd.validate.schueler.ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend(obj: unknown): ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend {
	return obj as ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend;
}
