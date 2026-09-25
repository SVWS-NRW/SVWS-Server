import { SchuelerSchulbesuchsdaten } from '../../../asd/data/schueler/SchuelerSchulbesuchsdaten';
import { Einschulungsart } from '../../../asd/types/schueler/Einschulungsart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart extends Validator {

	private readonly _schulbesuchsdaten: Supplier<SchuelerSchulbesuchsdaten>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param schulbesuchsdaten   ein Supplier für die Schulbesuchsdaten des Schülers
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(schulbesuchsdaten: Supplier<SchuelerSchulbesuchsdaten>, kontext: ValidatorKontext) {
		super(kontext);
		this._schulbesuchsdaten = schulbesuchsdaten;
	}

	protected pruefe(): boolean {
		const daten: SchuelerSchulbesuchsdaten | null = this._schulbesuchsdaten.get();
		if ((daten === null) || (daten.idEinschulungsartGrundschule === null) || (daten.idEinschulungsartGrundschule < 0)) {
			return true;
		}
		const art: Einschulungsart | null = Einschulungsart.data().getWertByIDOrNull(daten.idEinschulungsartGrundschule);
		if (art === null) {
			return true;
		}
		if (art.daten(this.kontext().getSchuljahr()) === null) {
			this.addFehler(0, "Einschulungsart des Schülers: Der eingetragene Wert für das Feld 'Einschulungsart' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart>('de.svws_nrw.asd.validate.schueler.ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart(obj: unknown): ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart {
	return obj as ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart;
}
