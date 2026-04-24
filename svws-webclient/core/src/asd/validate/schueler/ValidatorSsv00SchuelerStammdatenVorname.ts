import { ValidatorSsv10SchuelerStammdatenVorname } from '../../../asd/validate/schueler/ValidatorSsv10SchuelerStammdatenVorname';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsv00SchuelerStammdatenVorname extends Validator {

	/**
	 * Der Schueler-Vorname
	 */
	private readonly vornameSupplier: Supplier<string | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param vornameSupplier   der Vorname des Schuelers
	 * @param kontext           der Kontext der Schule
	 */
	public constructor(vornameSupplier: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.vornameSupplier = vornameSupplier;
		this._validatoren.add(new ValidatorSsv10SchuelerStammdatenVorname(this.getNotNullSupplier(vornameSupplier), kontext));
	}

	protected pruefe(): boolean {
		const vorname: string | null = this.vornameSupplier.get();
		if (vorname === null || JavaString.isEmpty(vorname)) {
			this.addFehler(0, "Rufname des Schülers: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsv00SchuelerStammdatenVorname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsv00SchuelerStammdatenVorname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsv00SchuelerStammdatenVorname>('de.svws_nrw.asd.validate.schueler.ValidatorSsv00SchuelerStammdatenVorname');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsv00SchuelerStammdatenVorname(obj: unknown): ValidatorSsv00SchuelerStammdatenVorname {
	return obj as ValidatorSsv00SchuelerStammdatenVorname;
}
