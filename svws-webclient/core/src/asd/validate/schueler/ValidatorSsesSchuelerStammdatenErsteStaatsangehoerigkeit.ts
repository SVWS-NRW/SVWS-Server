import { NullPointerException } from '../../../java/lang/NullPointerException';
import { ValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit } from '../../../asd/validate/schueler/ValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit extends Validator {

	private readonly _idSchuljahresabschnitt: Supplier<number | null>;

	private readonly _kontextNeu: ValidatorKontext;


	/**
	 * Erstellt einen neuen Validator für die Prüfung der Staatsangehörigkeit.
	 *
	 * @param idSchuljahresabschnitt   Schuljahresabschnitt ID
	 * @param idStaatsangehoerigkeit   StaatsangehörigkeitID
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number | null>, idStaatsangehoerigkeit: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchuljahresabschnitt = idSchuljahresabschnitt;
		this._kontextNeu = kontext;
		const schuljahr: Supplier<number> = { get: () => {
			const sja: Schuljahresabschnitt | null = this._kontextNeu.getSchuljahresabschnittByID(this.getNotNullSupplierLong(idSchuljahresabschnitt).get());
			if (sja === null) {
				throw new NullPointerException();
			}
			return sja.schuljahr;
		} };
		this._validatoren.add(new ValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit(schuljahr, this.getNotNullSupplierLong(idStaatsangehoerigkeit), kontext));
	}

	protected pruefe(): boolean {
		const sja: Schuljahresabschnitt | null = this._kontextNeu.getSchuljahresabschnittByID(this.getNotNullSupplierLong(this._idSchuljahresabschnitt).get());
		if (sja === null) {
			this.addFehler(0, "Dem Schüler ist kein Schuljahresabschnitt zugeordnet");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit>('de.svws_nrw.asd.validate.schueler.ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit(obj: unknown): ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit {
	return obj as ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit;
}
