import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination extends Validator {

	private readonly _geburtsland: Supplier<string>;

	private readonly _geburtslandMutter: Supplier<string>;

	private readonly _geburtslandVater: Supplier<string>;

	private readonly _verkehrsspracheFamilie: Supplier<string>;

	/**
	 * hat Migrationshintergrund
	 */
	private readonly _hatMigrationshintergrund: Supplier<boolean | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param geburtsland               das Geburtsland
	 * @param geburtslandMutter         das Geburtsland der Mutter
	 * @param geburtslandVater          das Geburtsland des Vaters
	 * @param verkehrsspracheFamilie    die Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(geburtsland: Supplier<string>, geburtslandMutter: Supplier<string>, geburtslandVater: Supplier<string>, verkehrsspracheFamilie: Supplier<string>, hatMigrationshintergrund: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._geburtsland = geburtsland;
		this._geburtslandMutter = geburtslandMutter;
		this._geburtslandVater = geburtslandVater;
		this._verkehrsspracheFamilie = verkehrsspracheFamilie;
		this._hatMigrationshintergrund = hatMigrationshintergrund;
	}

	protected pruefe(): boolean {
		const hatMigrationshintergrundZwisch: boolean | null = this._hatMigrationshintergrund.get();
		const hatMigrationshintergrund: boolean = hatMigrationshintergrundZwisch === null ? false : hatMigrationshintergrundZwisch;
		if (hatMigrationshintergrund) {
			if (JavaString.equalsIgnoreCase("DEU", this._geburtsland.get()) && JavaString.equalsIgnoreCase("DEU", this._geburtslandMutter.get()) && JavaString.equalsIgnoreCase("DEU", this._geburtslandVater.get()) && JavaString.equalsIgnoreCase("de", this._verkehrsspracheFamilie.get())) {
				this.addFehler(0, "Migrationshintergrund des Schülers: Wenn ein Migrationshintergrund vorhanden ist, dürfen die Felder 'Geburtsland', 'Geburtsland Mutter', 'Geburtsland Vater' und 'Verkehrssprache' nicht allesamt mit Deutschland bzw. Deutsch belegt sein.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination>('de.svws_nrw.asd.validate.schueler.ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination(obj: unknown): ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination {
	return obj as ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination;
}
