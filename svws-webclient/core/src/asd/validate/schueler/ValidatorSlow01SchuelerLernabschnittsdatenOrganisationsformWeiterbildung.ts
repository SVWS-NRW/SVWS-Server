import { WeiterbildungskollegOrganisationsformen } from '../../../asd/types/schule/WeiterbildungskollegOrganisationsformen';
import { ValidatorSlow02SchuelerLernabschnittsdatenOrganisationsformWeiterbildung } from '../../../asd/validate/schueler/ValidatorSlow02SchuelerLernabschnittsdatenOrganisationsformWeiterbildung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlow01SchuelerLernabschnittsdatenOrganisationsformWeiterbildung extends Validator {

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
		this._validatoren.add(new ValidatorSlow02SchuelerLernabschnittsdatenOrganisationsformWeiterbildung(schuljahr, idOrganisationsform, kontext));
	}

	protected pruefe(): boolean {
		const sa: WeiterbildungskollegOrganisationsformen | null = WeiterbildungskollegOrganisationsformen.data().getWertByIDOrNull(this.idOrganisationsform.get());
		if (sa === null) {
			this.addFehler(0, "Organisationsform des Schülers: Das Feld 'Organisationsform' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlow01SchuelerLernabschnittsdatenOrganisationsformWeiterbildung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlow01SchuelerLernabschnittsdatenOrganisationsformWeiterbildung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlow01SchuelerLernabschnittsdatenOrganisationsformWeiterbildung>('de.svws_nrw.asd.validate.schueler.ValidatorSlow01SchuelerLernabschnittsdatenOrganisationsformWeiterbildung');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlow01SchuelerLernabschnittsdatenOrganisationsformWeiterbildung(obj: unknown): ValidatorSlow01SchuelerLernabschnittsdatenOrganisationsformWeiterbildung {
	return obj as ValidatorSlow01SchuelerLernabschnittsdatenOrganisationsformWeiterbildung;
}
