import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { DateManager } from '../../../asd/validate/DateManager';
import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpl11LehrerPersonaldatenLehramt extends Validator {

	/**
	 * Die Lehrämter
	 */
	private readonly lehraemter: Supplier<List<LehrerLehramtEintrag>>;

	/**
	 * Das Geburtsdatum des Lehrers
	 */
	private readonly geburtsdatum: Supplier<DateManager | null>;

	private regulaereLehraemter: JavaSet<LehrerLehramt> = java_util_Set_of(LehrerLehramt.ID_00, LehrerLehramt.ID_01, LehrerLehramt.ID_02, LehrerLehramt.ID_04, LehrerLehramt.ID_08, LehrerLehramt.ID_09, LehrerLehramt.ID_10, LehrerLehramt.ID_11, LehrerLehramt.ID_12, LehrerLehramt.ID_14, LehrerLehramt.ID_15, LehrerLehramt.ID_16, LehrerLehramt.ID_17, LehrerLehramt.ID_19, LehrerLehramt.ID_20, LehrerLehramt.ID_21, LehrerLehramt.ID_24, LehrerLehramt.ID_25, LehrerLehramt.ID_27, LehrerLehramt.ID_29, LehrerLehramt.ID_30, LehrerLehramt.ID_31, LehrerLehramt.ID_35, LehrerLehramt.ID_40, LehrerLehramt.ID_50, LehrerLehramt.ID_51, LehrerLehramt.ID_52, LehrerLehramt.ID_53, LehrerLehramt.ID_54, LehrerLehramt.ID_55, LehrerLehramt.ID_96);


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehraemter   			die Lehrämter, die geprüft werden sollen
	 * @param geburtsdatum          das Geburtsdatum des Lehrers
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(lehraemter: Supplier<List<LehrerLehramtEintrag>>, geburtsdatum: Supplier<DateManager | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.lehraemter = lehraemter;
		this.geburtsdatum = geburtsdatum;
	}

	protected pruefe(): boolean {
		const datum: DateManager | null = this.geburtsdatum.get();
		if (datum === null)
			return true;
		if (datum.getJahr() >= 2003 && datum.getJahr() <= 2006) {
			for (const lehrerLehramtEintrag2 of this.lehraemter.get()) {
				const lehrerLehramt2: LehrerLehramt | null = LehrerLehramt.data().getWertByIDOrNull(lehrerLehramtEintrag2.idKatalogLehramt);
				if (lehrerLehramt2 === null)
					continue;
				if (this.regulaereLehraemter.contains(lehrerLehramt2)) {
					try {
						this.addFehler(3, "Für das Lehramt '" + LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag2.idKatalogLehramt).text + "' ist die Lehrkraft sehr jung. Wenn das Alter der Lehrkraft korrekt ist, sollte das eingetragene Lehramt überprüft werden. Bitte verwenden Sie die 'regulären' Lehrämter nur dann, wenn eine entsprechende abgeschlossene Ausbildung vorliegt. Wenn es sich um einen Studierenden handelt, der neben seinem Studium als Lehrkraft tätig ist, verwenden sie bitte das Lehramt 'Studierende'. Ansonsten tragen Sie bitte das Lehramt 'Sonstiges' ein. ");
					} catch(e : any) {
						this.addFehler(3, "Für das Lehramt mit der ID '" + lehrerLehramtEintrag2.idKatalogLehramt + "' ist die Lehrkraft sehr jung. Wenn das Alter der Lehrkraft korrekt ist, sollte das eingetragene Lehramt überprüft werden. Bitte verwenden Sie die 'regulären' Lehrämter nur dann, wenn eine entsprechende abgeschlossene Ausbildung vorliegt. Wenn es sich um einen Studierenden handelt, der neben seinem Studium als Lehrkraft tätig ist, verwenden sie bitte das Lehramt 'Studierende'. Ansonsten tragen Sie bitte das Lehramt 'Sonstiges' ein. ");
					}
					return false;
				}
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpl11LehrerPersonaldatenLehramt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpl11LehrerPersonaldatenLehramt', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpl11LehrerPersonaldatenLehramt>('de.svws_nrw.asd.validate.lehrer.ValidatorLpl11LehrerPersonaldatenLehramt');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpl11LehrerPersonaldatenLehramt(obj: unknown): ValidatorLpl11LehrerPersonaldatenLehramt {
	return obj as ValidatorLpl11LehrerPersonaldatenLehramt;
}
