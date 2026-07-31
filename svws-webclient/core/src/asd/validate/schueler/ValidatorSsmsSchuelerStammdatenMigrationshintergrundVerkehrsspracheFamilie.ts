import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie } from '../../../asd/validate/schueler/ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie';

export class ValidatorSsmsSchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idVerkehrsspracheFamilie    die Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  Gibt an, ob ein Migrationshintergrund vorhanden ist
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idVerkehrsspracheFamilie: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(idVerkehrsspracheFamilie, hatMigrationshintergrund, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmsSchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsmsSchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmsSchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie>('de.svws_nrw.asd.validate.schueler.ValidatorSsmsSchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmsSchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(obj: unknown): ValidatorSsmsSchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie {
	return obj as ValidatorSsmsSchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie;
}
