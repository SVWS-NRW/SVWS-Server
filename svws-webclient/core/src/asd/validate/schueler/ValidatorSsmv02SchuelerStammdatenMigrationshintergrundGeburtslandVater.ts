import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater } from '../../../asd/validate/schueler/ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater extends Validator {

	/**
	 * Geburtsland des Vaters
	 */
	private readonly _idGeburtslandVater: Supplier<number | null>;

	private static readonly FEHLERTEXT: string = "Geburtsland des Vaters: Der eingetragene Wert für das Feld 'Geburtsland Vater' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandVater        die ID des Geburtslandes des Vaters
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public constructor(idGeburtslandVater: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idGeburtslandVater = idGeburtslandVater;
		this._validatoren.add(new ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater(idGeburtslandVater, hatMigrationshintergrund, kontext));
	}

	protected pruefe(): boolean {
		const idGeburtslandVater: number | null = this._idGeburtslandVater.get();
		if (idGeburtslandVater === null) {
			return true;
		}
		const schuljahr: number = this.kontext().getSchuljahr();
		if (!Nationalitaeten.data().isGueltig(idGeburtslandVater, schuljahr)) {
			this.addFehler(0, ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater>('de.svws_nrw.asd.validate.schueler.ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater(obj: unknown): ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater {
	return obj as ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater;
}
