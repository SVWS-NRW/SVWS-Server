import { Religion } from '../../../asd/types/schule/Religion';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIka02IntKatalogKonfessionenAsdKatalog extends Validator {

	/**
	 * Die Katalog-ID.
	 */
	private readonly _idKatalog: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idKatalog   die Katalog-ID der Fachrichtung
	 * @param kontext     der Kontext des Validators
	 */
	public constructor(idKatalog: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKatalog = idKatalog;
	}

	protected pruefe(): boolean {
		if (!Religion.data().isGueltig(this._idKatalog.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, "Konfession des Schülers: Der eingetragene Wert für das Feld 'Konfession ASD-Kürzel' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIka02IntKatalogKonfessionenAsdKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.intKataloge.ValidatorIka02IntKatalogKonfessionenAsdKatalog', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIka02IntKatalogKonfessionenAsdKatalog>('de.svws_nrw.asd.validate.intKataloge.ValidatorIka02IntKatalogKonfessionenAsdKatalog');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIka02IntKatalogKonfessionenAsdKatalog(obj: unknown): ValidatorIka02IntKatalogKonfessionenAsdKatalog {
	return obj as ValidatorIka02IntKatalogKonfessionenAsdKatalog;
}
