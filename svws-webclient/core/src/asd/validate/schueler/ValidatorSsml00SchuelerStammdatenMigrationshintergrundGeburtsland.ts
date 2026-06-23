import { ValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland } from '../../../asd/validate/schueler/ValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland } from '../../../asd/validate/schueler/ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland';

export class ValidatorSsml00SchuelerStammdatenMigrationshintergrundGeburtsland extends Validator {

	/**
	 * Geburtsland
	 */
	private readonly _idGeburtsland: Supplier<number | null>;

	/**
	 * hat Migrationshintergrund
	 */
	private readonly _hatMigrationshintergrund: Supplier<boolean | null>;

	private static readonly FEHLERTEXT: string = "Geburtsland des Schülers: Wenn ein Migrationshintergrund vorhanden ist, muss das Feld 'Geburtsland' besetzt sein.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtsland             die ID des Geburtslands
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idGeburtsland: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idGeburtsland = idGeburtsland;
		this._hatMigrationshintergrund = hatMigrationshintergrund;
		this._validatoren.add(new ValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland(this.getNotNullSupplierLong(idGeburtsland), kontext));
		this._validatoren.add(new ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland(this.getNotNullSupplierLong(idGeburtsland), hatMigrationshintergrund, kontext));
	}

	protected pruefe(): boolean {
		const idGeburtsland: number | null = this._idGeburtsland.get();
		const hatMigrationshintergrundZwisch: boolean | null = this._hatMigrationshintergrund.get();
		const hatMigrationshintergrund: boolean = hatMigrationshintergrundZwisch === null ? false : hatMigrationshintergrundZwisch;
		if (hatMigrationshintergrund) {
			if (idGeburtsland === null) {
				this.addFehler(0, ValidatorSsml00SchuelerStammdatenMigrationshintergrundGeburtsland.FEHLERTEXT);
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsml00SchuelerStammdatenMigrationshintergrundGeburtsland';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsml00SchuelerStammdatenMigrationshintergrundGeburtsland', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsml00SchuelerStammdatenMigrationshintergrundGeburtsland>('de.svws_nrw.asd.validate.schueler.ValidatorSsml00SchuelerStammdatenMigrationshintergrundGeburtsland');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsml00SchuelerStammdatenMigrationshintergrundGeburtsland(obj: unknown): ValidatorSsml00SchuelerStammdatenMigrationshintergrundGeburtsland {
	return obj as ValidatorSsml00SchuelerStammdatenMigrationshintergrundGeburtsland;
}
