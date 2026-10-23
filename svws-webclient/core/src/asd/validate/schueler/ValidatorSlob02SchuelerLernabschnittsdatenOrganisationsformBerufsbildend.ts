import { BerufskollegOrganisationsformen } from '../../../asd/types/schule/BerufskollegOrganisationsformen';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend extends Validator {

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
		if (!BerufskollegOrganisationsformen.data().isGueltig(this.idOrganisationsform.get(), this.schuljahr.get())) {
			this.addFehler(0, "Organisationsform des Schülers: Der eingetragene Wert für das Feld 'Organisationsform' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend>('de.svws_nrw.asd.validate.schueler.ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend(obj: unknown): ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend {
	return obj as ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend;
}
