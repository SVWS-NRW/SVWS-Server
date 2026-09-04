import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import type { EinschulungsartKatalogEintrag } from "@core/asd/data/schueler/EinschulungsartKatalogEintrag";
import type { SchuelerNeu } from "@core/asd/data/schueler/SchuelerNeu";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import { Geschlecht } from "@core/asd/types/Geschlecht";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { ReligionEintrag } from "@core/core/data/schule/ReligionEintrag";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { SchuelerNeuManager } from "@ui/ui/manager/schueler/SchuelerNeuManager";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";
import { computed } from "vue";

export class SchuelerNeuModelProxy extends ModelProxy<SchuelerNeu> {

	private readonly _manager: () => SchuelerNeuManager;

	/**
	 * ModelProxy für die Schüler-Neuanlage.
	 *
	 * @param data      Lambda für den Zugriff auf die Original-Daten
	 * @param manager   Lambda für den Zugriff auf den SchuelerNeuManager
	 */
	constructor(
		data: () => SchuelerNeu,
		manager: () => SchuelerNeuManager
	) {
		super({ data });
		this._manager = manager;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addValidator(new ValidatorInputRequired(() => this.schuljahresabschnitt.value), "idSchuljahresabschnitt");

		this.addValidator(new ValidatorInputRequired(() => this.proxy.idJahrgang), "idJahrgang");

		this.addValidator(new ValidatorInputRequired(() => this.proxy.nachname), "nachname");
		this.addValidator(new ValidatorStringLength(() => this.proxy.nachname, null, 120), "nachname");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.nachname, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "nachname");

		this.addValidator(new ValidatorInputRequired(() => this.proxy.vorname), "vorname");
		this.addValidator(new ValidatorStringLength(() => this.proxy.vorname, null, 80), "vorname");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.vorname, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "vorname");

		this.addValidator(new ValidatorInputRequired(() => this.geschlecht.value), "geschlecht");

		this.addValidator(new ValidatorStringLength(() => this.proxy.alleVornamen, null, 255), "alleVornamen");

		this.addValidator(new ValidatorInputRequired(() => this.proxy.geburtsdatum), "geburtsdatum");
		this.addValidator(new ValidatorNumberRange(() => this.getAgeInYears(), 4, 50), "geburtsdatum");

		this.addValidator(new ValidatorInputRequired(() => this.religion.value), "idReligion");

	}

	schuljahresabschnitt = computed<Schuljahresabschnitt | null>({
		get: () => this._manager().schuljahresabschnitteFilteredById.get(this.proxy.idSchuljahresabschnitt) ?? null,
		set: (value: Schuljahresabschnitt | null) => {
			this.proxy.idSchuljahresabschnitt = value?.id ?? -1;
			this.proxy.idJahrgang = null;
			this.proxy.idKlasse = null;
		},
	});

	jahrgang = computed<JahrgangsDaten | null>({
		get: () => this._manager().jahrgaengeById.get(this.proxy.idJahrgang ?? -1) ?? null,
		set: (value: JahrgangsDaten | null) => {
			this.proxy.idJahrgang = value?.id ?? null;
			this.proxy.idKlasse = null;
		},
	});

	klasse = computed<KlassenListeEintrag | null>({
		get: () => this._manager().getKlassenByIdFuerAbschnitt(this.proxy.idSchuljahresabschnitt).get(this.proxy.idKlasse ?? -1) ?? null,
		set: (value: KlassenListeEintrag | null) => this.proxy.idKlasse = value?.id ?? null,
	});

	anmeldedatum = computed<string | null>({
		get: () => this.proxy.anmeldedatum,
		set: (value: string | null) => {
			this.proxy.anmeldedatum = value;
			this.proxy.aufnahmedatum = null;
		},
	});

	aufnahmedatum = computed<string | null>({
		get: () => this.proxy.aufnahmedatum,
		set: (value: string | null) => {
			this.proxy.aufnahmedatum = value;
			this.proxy.beginnBildungsgang = null;
		},
	});

	einschulungsart = computed<EinschulungsartKatalogEintrag | null>({
		get: () => this._manager().einschulungsartenById.get(this.proxy.idGrundschuleEinschulungsart ?? -1) ?? null,
		set: (value: EinschulungsartKatalogEintrag | null) => this.proxy.idGrundschuleEinschulungsart = value?.id ?? null,
	});

	geschlecht = computed<Geschlecht | null>({
		get: () => Geschlecht.fromValue(this.proxy.geschlecht) ?? null,
		set: (value: Geschlecht | null) => this.proxy.geschlecht = value?.id ?? -1,
	});

	religion = computed<ReligionEintrag | null>({
		get: () => this._manager().religionenById.get(this.proxy.idReligion ?? -1) ?? null,
		set: (value: ReligionEintrag | null) => this.proxy.idReligion = value?.id ?? null,
	});

	klassen = computed<KlassenListeEintrag[]>(() => {
		const alleKlassenAbschnitt = this._manager().klassenByIdAbschnitt.get(this.proxy.idSchuljahresabschnitt) ?? [];
		const result: KlassenListeEintrag[] = [];
		for (const klasse of alleKlassenAbschnitt) {
			if (klasse.idJahrgang === this.proxy.idJahrgang) {
				result.push(klasse);
			}
		}
		return result;
	});

	private getAgeInYears(): number | null {
		const geb = this.proxy.geburtsdatum;
		if (geb === null || geb === "") {
			return null;
		}

		const today = new Date();
		const birth = new Date(geb);
		// Berechnen der Altersdifferenz anhand der Jahreszahlen
		const yearDifference = today.getFullYear() - birth.getFullYear();

		// Beispiel: Heute 18.05.2026, Geburtstag 20.08.2010
		// Geburtstag war noch nicht -> yearDifference (16) - 1 = 15 (korrektes Alter)
		if (this.isBirthdayUpcomingThisYear(today, birth)) {
			return yearDifference - 1;
		}

		// Beispiel: Heute 18.05.2026, Geburtstag 01.03.2010
		// Geburtstag war bereits -> yearDifference (16) ist korrekt
		return yearDifference;
	}

	private isBirthdayUpcomingThisYear(today: Date, birthDate: Date): boolean {
		// Geburtstag im aktullen Jahr nachgestellt für die Prüfung
		const birthdayThisYear = new Date(today.getFullYear(), birthDate.getMonth(), birthDate.getDate());

		// Hier wird geprüft, ob der Geburtstag in diese, Jahr noch nicht stattgefunden hat
		return today < birthdayThisYear;
	}
}
