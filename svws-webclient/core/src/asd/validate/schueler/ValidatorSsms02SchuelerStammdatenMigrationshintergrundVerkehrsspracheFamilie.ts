import { ValidatorSsms03SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie } from '../../../asd/validate/schueler/ValidatorSsms03SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie';
import { Verkehrssprache } from '../../../asd/types/schule/Verkehrssprache';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie extends Validator {

	/**
	 * Die ID der Verkehrssprache der Familie des Schülers
	 */
	private readonly _idVerkehrsspracheFamilie: Supplier<number | null>;

	private static readonly FEHLERTEXT: string = "Verkehrssprache: Der eingetragene Wert für das Feld 'Verkehrssprache' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idVerkehrsspracheFamilie  die ID der Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idVerkehrsspracheFamilie: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idVerkehrsspracheFamilie = idVerkehrsspracheFamilie;
		this._validatoren.add(new ValidatorSsms03SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(idVerkehrsspracheFamilie, hatMigrationshintergrund, kontext));
	}

	protected pruefe(): boolean {
		const idVerkehrsspracheFamilie: number | null = this._idVerkehrsspracheFamilie.get();
		if (idVerkehrsspracheFamilie === null) {
			return true;
		}
		const schuljahr: number = this.kontext().getSchuljahr();
		if (!Verkehrssprache.data().isGueltig(idVerkehrsspracheFamilie, schuljahr)) {
			this.addFehler(0, ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie>('de.svws_nrw.asd.validate.schueler.ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(obj: unknown): ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie {
	return obj as ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie;
}
