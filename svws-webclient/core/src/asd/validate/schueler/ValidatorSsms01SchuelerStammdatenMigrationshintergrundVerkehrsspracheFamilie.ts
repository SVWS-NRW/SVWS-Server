import { ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie } from '../../../asd/validate/schueler/ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie';
import { Verkehrssprache } from '../../../asd/types/schule/Verkehrssprache';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie extends Validator {

	/**
	 * Die ID der Verkehrssprache der Familie des Schülers
	 */
	private readonly _idVerkehrsspracheFamilie: Supplier<number | null>;

	/**
	 * Gibt an, ob ein Migrationshintergrund vorhanden ist
	 */
	private readonly _hatMigrationshintergrund: Supplier<boolean | null>;

	private static readonly FEHLERTEXT: string = "Verkehrssprache: Das Feld muss zulässig sein.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idVerkehrsspracheFamilie  die Katalog-ID der Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idVerkehrsspracheFamilie: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idVerkehrsspracheFamilie = idVerkehrsspracheFamilie;
		this._hatMigrationshintergrund = hatMigrationshintergrund;
		this._validatoren.add(new ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(idVerkehrsspracheFamilie, hatMigrationshintergrund, kontext));
	}

	protected pruefe(): boolean {
		const verkehrsspracheFamilie: number | null = this._idVerkehrsspracheFamilie.get();
		if (verkehrsspracheFamilie === null) {
			return true;
		}
		if (Verkehrssprache.data().getWertByIDOrNull(verkehrsspracheFamilie) === null) {
			this.addFehler(0, ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie>('de.svws_nrw.asd.validate.schueler.ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(obj: unknown): ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie {
	return obj as ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie;
}
