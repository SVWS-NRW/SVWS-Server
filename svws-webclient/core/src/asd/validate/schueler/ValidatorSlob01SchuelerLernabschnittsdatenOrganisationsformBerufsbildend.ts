import { BerufskollegOrganisationsformen } from '../../../asd/types/schule/BerufskollegOrganisationsformen';
import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend } from '../../../asd/validate/schueler/ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlob01SchuelerLernabschnittsdatenOrganisationsformBerufsbildend extends Validator {

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
		this._validatoren.add(new ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend(schuljahr, idOrganisationsform, kontext));
	}

	protected pruefe(): boolean {
		const sa: BerufskollegOrganisationsformen | null = BerufskollegOrganisationsformen.data().getWertByIDOrNull(this.idOrganisationsform.get());
		if (sa === null) {
			this.addFehler(0, "Organisationsform des Schülers: Das Feld 'Organisationsform' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlob01SchuelerLernabschnittsdatenOrganisationsformBerufsbildend';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSlob01SchuelerLernabschnittsdatenOrganisationsformBerufsbildend', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlob01SchuelerLernabschnittsdatenOrganisationsformBerufsbildend>('de.svws_nrw.asd.validate.schueler.ValidatorSlob01SchuelerLernabschnittsdatenOrganisationsformBerufsbildend');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlob01SchuelerLernabschnittsdatenOrganisationsformBerufsbildend(obj: unknown): ValidatorSlob01SchuelerLernabschnittsdatenOrganisationsformBerufsbildend {
	return obj as ValidatorSlob01SchuelerLernabschnittsdatenOrganisationsformBerufsbildend;
}
