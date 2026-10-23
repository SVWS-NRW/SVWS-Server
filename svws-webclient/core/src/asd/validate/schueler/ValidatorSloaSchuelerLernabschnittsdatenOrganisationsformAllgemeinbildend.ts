import { NullPointerException } from '../../../java/lang/NullPointerException';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorSloa00SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend } from '../../../asd/validate/schueler/ValidatorSloa00SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend';

export class ValidatorSloaSchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend extends Validator {

	/**
	 * Die Organisationsform ID des Schuelers
	 */
	private readonly idSchuljahresabschnitt: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator für die Prüfung.
	 *
	 * @param idSchuljahresabschnitt    Schuljahresabschnitt ID
	 * @param idOrganisationsform       Organisationsform ID
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number | null>, idOrganisationsform: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
		const schuljahr: Supplier<number> = { get: () => {
			const sja: Schuljahresabschnitt | null = kontext.getSchuljahresabschnittByID(this.getNotNullSupplierLong(idSchuljahresabschnitt).get());
			if (sja === null) {
				throw new NullPointerException();
			}
			return sja.schuljahr;
		} };
		this._validatoren.add(new ValidatorSloa00SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend(schuljahr, idOrganisationsform, kontext));
	}

	protected pruefe(): boolean {
		const sja: Schuljahresabschnitt | null = this.kontext().getSchuljahresabschnittByID(this.getNotNullSupplierLong(this.idSchuljahresabschnitt).get());
		if (sja === null) {
			this.addFehler(0, "Dem Schüler ist kein Schuljahresabschnitt zugeordnet");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSloaSchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSloaSchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSloaSchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend>('de.svws_nrw.asd.validate.schueler.ValidatorSloaSchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSloaSchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend(obj: unknown): ValidatorSloaSchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend {
	return obj as ValidatorSloaSchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend;
}
