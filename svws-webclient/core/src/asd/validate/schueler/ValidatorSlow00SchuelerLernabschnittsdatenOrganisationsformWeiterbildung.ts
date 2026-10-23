import { ValidatorSlow01SchuelerLernabschnittsdatenOrganisationsformWeiterbildung } from '../../../asd/validate/schueler/ValidatorSlow01SchuelerLernabschnittsdatenOrganisationsformWeiterbildung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlow00SchuelerLernabschnittsdatenOrganisationsformWeiterbildung extends Validator {

	/**
	 * Die Organisationsform ID des Schuelers
	 */
	private readonly idOrganisationsform: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator für die Prüfung.
	 *
	 * @param schuljahr              Schuljahr
	 * @param idOrganisationsform    Organisationsform ID
	 * @param kontext                der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, idOrganisationsform: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.idOrganisationsform = idOrganisationsform;
		this._validatoren.add(new ValidatorSlow01SchuelerLernabschnittsdatenOrganisationsformWeiterbildung(schuljahr, this.getNotNullSupplierLong(idOrganisationsform), kontext));
	}

	protected pruefe(): boolean {
		const idOrganisationsformLong: number | null = this.idOrganisationsform.get();
		if (idOrganisationsformLong === null) {
			this.addFehler(0, "Organisationsform des Schülers: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlow00SchuelerLernabschnittsdatenOrganisationsformWeiterbildung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlow00SchuelerLernabschnittsdatenOrganisationsformWeiterbildung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlow00SchuelerLernabschnittsdatenOrganisationsformWeiterbildung>('de.svws_nrw.asd.validate.schueler.ValidatorSlow00SchuelerLernabschnittsdatenOrganisationsformWeiterbildung');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlow00SchuelerLernabschnittsdatenOrganisationsformWeiterbildung(obj: unknown): ValidatorSlow00SchuelerLernabschnittsdatenOrganisationsformWeiterbildung {
	return obj as ValidatorSlow00SchuelerLernabschnittsdatenOrganisationsformWeiterbildung;
}
