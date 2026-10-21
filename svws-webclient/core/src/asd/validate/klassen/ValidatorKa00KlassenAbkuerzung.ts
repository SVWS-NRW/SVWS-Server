import { ValidatorKa10KlassenAbkuerzung } from '../../../asd/validate/klassen/ValidatorKa10KlassenAbkuerzung';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { ValidatorKa11KlassenAbkuerzung } from '../../../asd/validate/klassen/ValidatorKa11KlassenAbkuerzung';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKa00KlassenAbkuerzung extends Validator {

	/**
	 * das Kürzel der Klasse
	 */
	private readonly kuerzel: Supplier<string | null>;

	/**
	 * eine Liste der Kürzel der Klassen der Schule
	 */
	private readonly listKuerzel: Supplier<List<string> | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param kuerzel     das Kürzel der Klasse
	 * @param listKuerzel eine Liste der Kürzel der Klassen der Schule
	 * @param kontext     der Kontext des Validators
	 */
	public constructor(kuerzel: Supplier<string | null>, listKuerzel: Supplier<List<string> | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.kuerzel = kuerzel;
		this.listKuerzel = listKuerzel;
		this._validatoren.add(new ValidatorKa10KlassenAbkuerzung(this.getNotNullSupplier(kuerzel), listKuerzel, kontext));
		this._validatoren.add(new ValidatorKa11KlassenAbkuerzung(this.getNotNullSupplier(kuerzel), kontext));
	}

	protected pruefe(): boolean {
		const klassenkuerzel: string | null = this.kuerzel.get();
		if ((klassenkuerzel === null) || (JavaString.isEmpty(klassenkuerzel))) {
			this.addFehler(0, "Kürzel der Klasse: Der eingetragene Wert darf nicht leer sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKa00KlassenAbkuerzung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKa00KlassenAbkuerzung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKa00KlassenAbkuerzung>('de.svws_nrw.asd.validate.klassen.ValidatorKa00KlassenAbkuerzung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKa00KlassenAbkuerzung(obj: unknown): ValidatorKa00KlassenAbkuerzung {
	return obj as ValidatorKa00KlassenAbkuerzung;
}
