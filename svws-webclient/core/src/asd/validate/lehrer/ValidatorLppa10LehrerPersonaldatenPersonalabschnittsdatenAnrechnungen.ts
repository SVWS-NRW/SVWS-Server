import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { LehrerAnrechnungsgrund } from '../../../asd/types/lehrer/LehrerAnrechnungsgrund';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/**
	 * Die Liste der Anrechnungsstunden.
	 */
	private readonly anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>;

	/**
	 * Die Liste der Lehrämter der Lehrkraft.
	 */
	private readonly lehraemter: Supplier<List<LehrerLehramtEintrag>>;


	/**
	 * Erstellt einen neuen Validator für die Exklusivitätsprüfung des Anrechnungsgrundes 935.
	 *
	 * @param anrechnungen die Liste der Anrechnungsstunden
	 * @param lehraemter   die Liste der Lehrämter
	 * @param kontext      der Kontext des Validators
	 */
	public constructor(anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, lehraemter: Supplier<List<LehrerLehramtEintrag>>, kontext: ValidatorKontext) {
		super(kontext);
		this.anrechnungen = anrechnungen;
		this.lehraemter = lehraemter;
	}

	protected pruefe(): boolean {
		const listeLehraemter: List<LehrerLehramtEintrag> | null = this.lehraemter.get();
		const listeAnrechnungen: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null = this.anrechnungen.get();
		if (listeLehraemter === null || listeAnrechnungen === null)
			return true;
		const grund935: LehrerAnrechnungsgrund | null = LehrerAnrechnungsgrund.data().getWertByBezeichner("ID_935");
		for (const lehramtEintrag of listeLehraemter)
			if (LehrerLehramt.ID_70 as unknown === LehrerLehramt.data().getWertByIDOrNull(lehramtEintrag.idKatalogLehramt) as unknown)
				for (const anrechnung of listeAnrechnungen) {
					if (anrechnung.idGrund === null)
						continue;
					const grund: LehrerAnrechnungsgrund | null = LehrerAnrechnungsgrund.data().getWertByIDOrNull(anrechnung.idGrund);
					if (grund as unknown !== grund935 as unknown) {
						this.addFehler(0, "Für das Lehramt 'Schulverwaltungsassistent/-in' darf nur der Anrechnungsgrund '935 - Schulverwaltungsassistenz' eingetragen sein.");
						return false;
					}
				}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen>('de.svws_nrw.asd.validate.lehrer.ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(obj: unknown): ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {
	return obj as ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen;
}
