import { NullPointerException } from '../../../java/lang/NullPointerException';
import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit } from '../../../asd/validate/schueler/ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit extends Validator {

	private readonly _idSchuljahresabschnitt: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt    Schuljahresabschnitt ID
	 * @param idStaatsangehoerigkeit2   Staatsangehörigkeit2ID
	 * @param idStaatsangehoerigkeit    StaatsangehörigkeitID
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number | null>, idStaatsangehoerigkeit2: Supplier<number | null>, idStaatsangehoerigkeit: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchuljahresabschnitt = idSchuljahresabschnitt;
		const schuljahr: Supplier<number> = { get: () => {
			const sja: Schuljahresabschnitt | null = kontext.getSchuljahresabschnittByID(this.getNotNullSupplierLong(idSchuljahresabschnitt).get());
			if (sja === null) {
				throw new NullPointerException();
			}
			return sja.schuljahr;
		} };
		this._validatoren.add(new ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit(schuljahr, idStaatsangehoerigkeit2, idStaatsangehoerigkeit, kontext));
	}

	protected pruefe(): boolean {
		const sja: Schuljahresabschnitt | null = this.kontext().getSchuljahresabschnittByID(this.getNotNullSupplierLong(this._idSchuljahresabschnitt).get());
		if (sja === null) {
			this.addFehler(0, "Dem Schüler ist kein Schuljahresabschnitt zugeordnet");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit>('de.svws_nrw.asd.validate.schueler.ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit(obj: unknown): ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit {
	return obj as ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit;
}
