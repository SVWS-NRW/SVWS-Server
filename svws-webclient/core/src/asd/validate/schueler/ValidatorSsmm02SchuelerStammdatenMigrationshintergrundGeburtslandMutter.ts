import { ValidatorSsmm03SchuelerStammdatenMigrationshintergrundGeburtslandMutter } from '../../../asd/validate/schueler/ValidatorSsmm03SchuelerStammdatenMigrationshintergrundGeburtslandMutter';
import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmm02SchuelerStammdatenMigrationshintergrundGeburtslandMutter extends Validator {

	/**
	 * Geburtsland der Mutter
	 */
	private readonly _idGeburtslandMutter: Supplier<number | null>;

	private static readonly FEHLERTEXT: string = "Geburtsland der Mutter: Der eingetragene Wert für das Feld 'Geburtsland Mutter' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandMutter        die ID des Geburtslandes der Mutter
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public constructor(idGeburtslandMutter: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idGeburtslandMutter = idGeburtslandMutter;
		this._validatoren.add(new ValidatorSsmm03SchuelerStammdatenMigrationshintergrundGeburtslandMutter(idGeburtslandMutter, hatMigrationshintergrund, kontext));
	}

	protected pruefe(): boolean {
		const idGeburtslandMutter: number | null = this._idGeburtslandMutter.get();
		if (idGeburtslandMutter === null) {
			return true;
		}
		const schuljahr: number = this.kontext().getSchuljahr();
		if (!Nationalitaeten.data().isGueltig(idGeburtslandMutter, schuljahr)) {
			this.addFehler(0, ValidatorSsmm02SchuelerStammdatenMigrationshintergrundGeburtslandMutter.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmm02SchuelerStammdatenMigrationshintergrundGeburtslandMutter';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsmm02SchuelerStammdatenMigrationshintergrundGeburtslandMutter', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmm02SchuelerStammdatenMigrationshintergrundGeburtslandMutter>('de.svws_nrw.asd.validate.schueler.ValidatorSsmm02SchuelerStammdatenMigrationshintergrundGeburtslandMutter');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmm02SchuelerStammdatenMigrationshintergrundGeburtslandMutter(obj: unknown): ValidatorSsmm02SchuelerStammdatenMigrationshintergrundGeburtslandMutter {
	return obj as ValidatorSsmm02SchuelerStammdatenMigrationshintergrundGeburtslandMutter;
}
