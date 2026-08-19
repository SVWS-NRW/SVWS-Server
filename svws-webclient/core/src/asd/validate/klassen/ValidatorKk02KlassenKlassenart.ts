import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { Klassenart } from '../../../asd/types/klassen/Klassenart';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKk02KlassenKlassenart extends Validator {

	private readonly _idKlassenart: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idKlassenart  KlassenartID
	 * @param kontext       der Kontext des Validators
	 */
	public constructor(idKlassenart: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKlassenart = idKlassenart;
	}

	protected pruefe(): boolean {
		if (!Klassenart.data().isGueltig(this._idKlassenart.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, "Art der Klasse: Der eingetragene Wert für das Feld 'Klassenart' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKk02KlassenKlassenart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKk02KlassenKlassenart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKk02KlassenKlassenart>('de.svws_nrw.asd.validate.klassen.ValidatorKk02KlassenKlassenart');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKk02KlassenKlassenart(obj: unknown): ValidatorKk02KlassenKlassenart {
	return obj as ValidatorKk02KlassenKlassenart;
}
