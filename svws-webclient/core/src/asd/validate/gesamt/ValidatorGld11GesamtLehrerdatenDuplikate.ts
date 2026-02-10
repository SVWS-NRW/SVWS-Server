import { HashMap } from '../../../java/util/HashMap';
import { LehrerStatistikGesamt } from '../../../asd/data/statistik/LehrerStatistikGesamt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Geschlecht } from '../../../asd/types/Geschlecht';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import type { JavaMap } from '../../../java/util/JavaMap';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorGld11GesamtLehrerdatenDuplikate extends Validator {

	private readonly listLehrer: Supplier<List<LehrerStatistikGesamt>>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listLehrer          die Liste aller Lehrerstammdaten
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(listLehrer: Supplier<List<LehrerStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this.listLehrer = listLehrer;
	}

	protected pruefe(): boolean {
		let success: boolean = true;
		const list: List<LehrerStatistikGesamt> = this.listLehrer.get();
		if (list.isEmpty())
			return success;
		const keys: JavaMap<string, LehrerStatistikGesamt> = new HashMap<string, LehrerStatistikGesamt>();
		for (const lehrer of list) {
			const geschlecht: Geschlecht | null = Geschlecht.fromValue(lehrer.geschlecht);
			const key: string = lehrer.nachname + "__" + lehrer.vorname + "__" + ((lehrer.geburtsdatum === null) ? "" : lehrer.geburtsdatum) + "__" + ((geschlecht === null) ? lehrer.geschlecht : geschlecht.kuerzel);
			const other: LehrerStatistikGesamt | null = keys.put(key, lehrer);
			if (other === null)
				continue;
			const fehlermeldung: string | null = "Lehrkäfte: Bei den IDs " + lehrer.id + " und " + other.id + " kommt die Kombination aus Nachname '" + lehrer.nachname + "', Vorname '" + lehrer.vorname + "', Geburtsdatum '" + lehrer.geburtsdatum + "' und Geschlecht '" + lehrer.geschlecht + "' mehrmals vor. Falls es sich hierbei um eine Person handelt, so fassen Sie die Datensätze bitte unter einer Lehrerabkürzung zusammen.";
			this.addFehler(2, fehlermeldung);
			success = false;
		}
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.gesamt.ValidatorGld11GesamtLehrerdatenDuplikate';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.gesamt.ValidatorGld11GesamtLehrerdatenDuplikate', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorGld11GesamtLehrerdatenDuplikate>('de.svws_nrw.asd.validate.gesamt.ValidatorGld11GesamtLehrerdatenDuplikate');

}

export function cast_de_svws_nrw_asd_validate_gesamt_ValidatorGld11GesamtLehrerdatenDuplikate(obj: unknown): ValidatorGld11GesamtLehrerdatenDuplikate {
	return obj as ValidatorGld11GesamtLehrerdatenDuplikate;
}
