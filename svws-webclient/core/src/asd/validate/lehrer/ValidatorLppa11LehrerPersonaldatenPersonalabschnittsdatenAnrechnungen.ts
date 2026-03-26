import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { LehrerAnrechnungsgrund } from '../../../asd/types/lehrer/LehrerAnrechnungsgrund';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { JavaMath } from '../../../java/lang/JavaMath';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/**
	 * Die Liste der Anrechnungsstunden.
	 */
	private readonly anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>;

	/**
	 * Die Liste der Lehrämter der Lehrkraft.
	 */
	private readonly lehraemter: Supplier<List<LehrerLehramtEintrag>>;

	/**
	 * Das Pflichtstundensoll der Lehrkraft.
	 */
	private readonly pflichtstundensoll: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator für den Pflichtstundensoll-Abgleich bei Schulverwaltungsassistenten.
	 *
	 * @param anrechnungen       die Liste der Anrechnungsstunden
	 * @param lehraemter         die Liste der Lehrämter
	 * @param pflichtstundensoll das Pflichtstundensoll
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, lehraemter: Supplier<List<LehrerLehramtEintrag>>, pflichtstundensoll: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.anrechnungen = anrechnungen;
		this.lehraemter = lehraemter;
		this.pflichtstundensoll = pflichtstundensoll;
	}

	protected pruefe(): boolean {
		const listeLehraemter: List<LehrerLehramtEintrag> | null = this.lehraemter.get();
		const listeAnrechnungen: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null = this.anrechnungen.get();
		const soll: number | null = this.pflichtstundensoll.get();
		if (listeLehraemter === null || listeAnrechnungen === null || soll === null) {
			return true;
		}
		let hatLehramt70: boolean = false;
		for (const lehramtEintrag of listeLehraemter) {
			const idKatalog: number = lehramtEintrag.idKatalogLehramt;
			if (LehrerLehramt.ID_70 as unknown === LehrerLehramt.data().getWertByIDOrNull(idKatalog) as unknown) {
				hatLehramt70 = true;
				break;
			}
		}
		if (!hatLehramt70) {
			return true;
		}
		const grund935: LehrerAnrechnungsgrund | null = LehrerAnrechnungsgrund.data().getWertByBezeichner("ID_935");
		let summe935: number = 0;
		let hatAnrechnung935: boolean = false;
		for (const anrechnung of listeAnrechnungen) {
			if (anrechnung.idGrund !== null) {
				const grund: LehrerAnrechnungsgrund | null = LehrerAnrechnungsgrund.data().getWertByIDOrNull(anrechnung.idGrund);
				if (grund as unknown === grund935 as unknown) {
					hatAnrechnung935 = true;
					summe935 += anrechnung.anzahl;
				}
			}
		}
		if (hatAnrechnung935 && (Math.abs(summe935 - soll) > 0.001)) {
			this.addFehler(0, "Für das Lehramt 'Schulverwaltungsassistent/-in' muss die Anzahl der Anrechungsstunden bei dem Anrechnungsgrund '935 - Schulverwaltungsassistenz' dem Pflichtstundensoll entsprechen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen>('de.svws_nrw.asd.validate.lehrer.ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(obj: unknown): ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {
	return obj as ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen;
}
