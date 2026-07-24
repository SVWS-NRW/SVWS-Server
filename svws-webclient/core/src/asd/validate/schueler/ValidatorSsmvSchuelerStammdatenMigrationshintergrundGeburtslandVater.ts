import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorSsmv00SchuelerStammdatenMigrationshintergrundGeburtslandVater } from '../../../asd/validate/schueler/ValidatorSsmv00SchuelerStammdatenMigrationshintergrundGeburtslandVater';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmvSchuelerStammdatenMigrationshintergrundGeburtslandVater extends Validator {

	/**
	 * Geburtsland des Vaters
	 */
	private readonly _idGeburtslandVater: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandVater        die ID des Geburtslandes der Vater
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public constructor(idGeburtslandVater: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean>, kontext: ValidatorKontext) {
		super(kontext);
		this._idGeburtslandVater = idGeburtslandVater;
		this._validatoren.add(new ValidatorSsmv00SchuelerStammdatenMigrationshintergrundGeburtslandVater(this._idGeburtslandVater, hatMigrationshintergrund, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmvSchuelerStammdatenMigrationshintergrundGeburtslandVater';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSsmvSchuelerStammdatenMigrationshintergrundGeburtslandVater', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmvSchuelerStammdatenMigrationshintergrundGeburtslandVater>('de.svws_nrw.asd.validate.schueler.ValidatorSsmvSchuelerStammdatenMigrationshintergrundGeburtslandVater');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmvSchuelerStammdatenMigrationshintergrundGeburtslandVater(obj: unknown): ValidatorSsmvSchuelerStammdatenMigrationshintergrundGeburtslandVater {
	return obj as ValidatorSsmvSchuelerStammdatenMigrationshintergrundGeburtslandVater;
}
