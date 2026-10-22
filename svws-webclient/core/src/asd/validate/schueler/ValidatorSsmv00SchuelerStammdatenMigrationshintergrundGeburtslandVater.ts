import { ValidatorSsmv01SchuelerStammdatenMigrationshintergrundGeburtslandVater } from '../../../asd/validate/schueler/ValidatorSsmv01SchuelerStammdatenMigrationshintergrundGeburtslandVater';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland } from '../../../asd/validate/schueler/ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland';

export class ValidatorSsmv00SchuelerStammdatenMigrationshintergrundGeburtslandVater extends Validator {

	/**
	 * Geburtsland des Vaters
	 */
	private readonly _idGeburtslandVater: Supplier<number | null>;

	/**
	 * Gibt an, ob ein Migrationshintergrund vorhanden ist
	 */
	private readonly _hatMigrationshintergrund: Supplier<boolean | null>;

	private static readonly FEHLERTEXT: string = "Geburtsland des Vaters: Wenn ein Migrationshintergrund vorhanden ist, muss das Feld 'Geburtsland Vater' besetzt sein.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandVater         die ID des Geburtslandes der Vater
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public constructor(idGeburtslandVater: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idGeburtslandVater = idGeburtslandVater;
		this._hatMigrationshintergrund = hatMigrationshintergrund;
	}

	protected pruefe(): boolean {
		const idGeburtslandVater: number | null = this._idGeburtslandVater.get();
		const hatMigrationshintergrundZwisch: boolean | null = this._hatMigrationshintergrund.get();
		const hatMigrationshintergrund: boolean = hatMigrationshintergrundZwisch === null ? false : hatMigrationshintergrundZwisch;
		this._validatoren.clear();
		if (hatMigrationshintergrund) {
			if (idGeburtslandVater === null) {
				this.addFehler(0, ValidatorSsmv00SchuelerStammdatenMigrationshintergrundGeburtslandVater.FEHLERTEXT);
				return false;
			}
			this._validatoren.add(new ValidatorSsmv01SchuelerStammdatenMigrationshintergrundGeburtslandVater(this.getNotNullSupplierLong(this._idGeburtslandVater), this.kontext()));
		} else {
			this._validatoren.add(new ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland(this.getNotNullSupplierLong(this._idGeburtslandVater), this._hatMigrationshintergrund, this.kontext()));
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmv00SchuelerStammdatenMigrationshintergrundGeburtslandVater';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsmv00SchuelerStammdatenMigrationshintergrundGeburtslandVater', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmv00SchuelerStammdatenMigrationshintergrundGeburtslandVater>('de.svws_nrw.asd.validate.schueler.ValidatorSsmv00SchuelerStammdatenMigrationshintergrundGeburtslandVater');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmv00SchuelerStammdatenMigrationshintergrundGeburtslandVater(obj: unknown): ValidatorSsmv00SchuelerStammdatenMigrationshintergrundGeburtslandVater {
	return obj as ValidatorSsmv00SchuelerStammdatenMigrationshintergrundGeburtslandVater;
}
