import { ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart } from '../../../asd/validate/schueler/ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart';
import { SchuelerSchulbesuchsdaten } from '../../../asd/data/schueler/SchuelerSchulbesuchsdaten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart extends Validator {

	private readonly _schulbesuchsdaten: Supplier<SchuelerSchulbesuchsdaten>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param schulbesuchsdaten ein Supplier für die Schulbesuchsdaten
	 * @param kontext           der Kontext des Validators
	 */
	public constructor(schulbesuchsdaten: Supplier<SchuelerSchulbesuchsdaten>, kontext: ValidatorKontext) {
		super(kontext);
		this._schulbesuchsdaten = schulbesuchsdaten;
		this._validatoren.add(new ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart(schulbesuchsdaten, kontext));
	}

	protected pruefe(): boolean {
		const daten: SchuelerSchulbesuchsdaten | null = this._schulbesuchsdaten.get();
		if ((daten === null) || (daten.idEinschulungsartGrundschule === null) || (daten.idEinschulungsartGrundschule < 0)) {
			this.addFehler(0, "Einschulungsart des Schülers: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart>('de.svws_nrw.asd.validate.schueler.ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart(obj: unknown): ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart {
	return obj as ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart;
}
