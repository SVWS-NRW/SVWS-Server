import { WeiterbildungskollegOrganisationsformen } from '../../../asd/types/schule/WeiterbildungskollegOrganisationsformen';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlow02SchuelerLernabschnittsdatenOrganisationsformWeiterbildung extends Validator {

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
		if (!WeiterbildungskollegOrganisationsformen.data().isGueltig(this.idOrganisationsform.get(), this.schuljahr.get())) {
			this.addFehler(0, "Organisationsform des Schülers: Der eingetragene Wert für das Feld 'Organisationsform' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlow02SchuelerLernabschnittsdatenOrganisationsformWeiterbildung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlow02SchuelerLernabschnittsdatenOrganisationsformWeiterbildung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlow02SchuelerLernabschnittsdatenOrganisationsformWeiterbildung>('de.svws_nrw.asd.validate.schueler.ValidatorSlow02SchuelerLernabschnittsdatenOrganisationsformWeiterbildung');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlow02SchuelerLernabschnittsdatenOrganisationsformWeiterbildung(obj: unknown): ValidatorSlow02SchuelerLernabschnittsdatenOrganisationsformWeiterbildung {
	return obj as ValidatorSlow02SchuelerLernabschnittsdatenOrganisationsformWeiterbildung;
}
