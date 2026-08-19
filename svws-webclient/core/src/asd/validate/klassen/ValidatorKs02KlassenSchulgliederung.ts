import { Schulgliederung } from '../../../asd/types/schule/Schulgliederung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKs02KlassenSchulgliederung extends Validator {

	private readonly _idSchulgliederung: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idSchulgliederung   SchulgliederungID
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(idSchulgliederung: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchulgliederung = idSchulgliederung;
	}

	protected pruefe(): boolean {
		if (!Schulgliederung.data().isGueltig(this._idSchulgliederung.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, "Schulgliederung der Klasse: Der eingetragene Wert für das Feld 'Schulgliederung' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKs02KlassenSchulgliederung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKs02KlassenSchulgliederung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKs02KlassenSchulgliederung>('de.svws_nrw.asd.validate.klassen.ValidatorKs02KlassenSchulgliederung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKs02KlassenSchulgliederung(obj: unknown): ValidatorKs02KlassenSchulgliederung {
	return obj as ValidatorKs02KlassenSchulgliederung;
}
