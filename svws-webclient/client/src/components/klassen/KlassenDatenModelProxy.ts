import { computed } from "vue";
import { AllgemeinbildendOrganisationsformen, ArrayList, BerufskollegOrganisationsformen, Jahrgaenge, JavaInteger, Klassenart, Schulgliederung,
	WeiterbildungskollegOrganisationsformen } from "@core";
import type { JahrgangsDaten, KlassenDaten, List, KlassenListeEintrag, KlassenDatenMinimal } from "@core";
import { ModelProxy, ValidatorInputRequired, ValidatorKlassenKuerzel, ValidatorNumberRange, ValidatorStringLength } from "@ui";
import type { KlassenListeManager } from "~/states/klassen/KlassenListeManager";
import { schuleStateImpl } from "~/states/SchuleStateImpl";

/**
 * Der spezielle ModelProxy für die Klassen-Daten
 */
export class KlassenDatenModelProxy extends ModelProxy<KlassenDaten> {

	protected manager: () => KlassenListeManager;

	constructor(
		data: () => KlassenDaten,
		vorhanden: () => Iterable<KlassenListeEintrag>,
		manager: () => KlassenListeManager,
		listOfAutopatchProps?: Iterable<keyof KlassenDaten>,
		patch?: (data: Partial<KlassenDaten>) => Promise<boolean>) {
		super({ data, patch, listOfAutopatchProps });
		this.manager = manager;

		this.addValidator(new ValidatorKlassenKuerzel(() => this.proxy.kuerzel ?? null, vorhanden), "kuerzel");
		this.addValidator(new ValidatorStringLength(() => this.proxy.beschreibung, 1, 150), "beschreibung");
		this.addBlockingValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), "sortierung");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, JavaInteger.MAX_VALUE), "sortierung");
		this.validate();
	}

	parallelitaet = computed<string | null>({
		get: () => this.proxy.parallelitaet ?? '---',
		set: (value) => this.proxy.parallelitaet = value,
	});

	schulgliederung = computed<Schulgliederung | null>({
		get: () => (this.proxy.idSchulgliederung === -1) ? null : Schulgliederung.data().getWertByIDOrNull(this.proxy.idSchulgliederung),
		set: (value) => this.proxy.idSchulgliederung = value?.daten(schuleStateImpl.schuljahr)?.id ?? -1,
	});
	schulgliederungen = computed(() => Schulgliederung.getBySchuljahrAndSchulform(schuleStateImpl.schuljahr, this.manager().schulform()));

	klassenart = computed<Klassenart | null>({
		get: () => ((this.proxy.idKlassenart === null) || (this.proxy.idKlassenart === -1)) ? null : Klassenart.data().getWertByIDOrNull(this.proxy.idKlassenart),
		set: (value) => this.proxy.idKlassenart = value?.daten(schuleStateImpl.schuljahr)?.id ?? -1,
	});

	klassenarten = computed(() => Klassenart.getBySchuljahrAndSchulform(schuleStateImpl.schuljahr, this.manager().schulform()));

	organisationsformAllgemeinbildend = computed<AllgemeinbildendOrganisationsformen | null>({
		get: () => {
			const id = this.proxy.idAllgemeinbildendOrganisationsform;
			return (id === null) ? null : AllgemeinbildendOrganisationsformen.data().getWertByIDOrNull(id);
		},
		set: (value) => this.proxy.idAllgemeinbildendOrganisationsform = value?.daten(schuleStateImpl.schuljahr)?.id ?? null,
	});
	organisationsformenAllgemeinbildend = computed(() => AllgemeinbildendOrganisationsformen.values());

	organisationsformBerufsbildend = computed<BerufskollegOrganisationsformen | null>({
		get: () => {
			const id = this.proxy.idBerufsbildendOrganisationsform;
			return (id === null) ? null : BerufskollegOrganisationsformen.data().getWertByIDOrNull(id);
		},
		set: (value) => this.proxy.idBerufsbildendOrganisationsform = value?.daten(schuleStateImpl.schuljahr)?.id ?? null,
	});
	organisationsformenBerufsbildend = computed(() => BerufskollegOrganisationsformen.values());

	organisationsformWeiterbildend = computed<WeiterbildungskollegOrganisationsformen | null>({
		get: () => {
			const id = this.proxy.idWeiterbildungOrganisationsform;
			return (id === null) ? null : WeiterbildungskollegOrganisationsformen.data().getWertByIDOrNull(id);
		},
		set: (value) => this.proxy.idWeiterbildungOrganisationsform = value?.daten(schuleStateImpl.schuljahr)?.id ?? null,
	});
	organisationsformenWeiterbildend = computed(() => WeiterbildungskollegOrganisationsformen.values());

	jahrgang = computed<JahrgangsDaten | null>({
		get: () => {
			const id = this.proxy.idJahrgang;
			return (id === null) ? null : this.manager().jahrgaenge.get(id);
		},
		set: (value) => (this.proxy.idJahrgang = value?.id ?? null),
	});
	jahrgaenge = computed<List<JahrgangsDaten>>(() => {
		const result = new ArrayList<JahrgangsDaten>();
		for (const jg of this.manager().jahrgaenge.list()) {
			if (jg.kuerzel !== "E3") { // Das dritte Jahr der Schuleingangsphase sollte nicht für einen Jahrgang einer Klasse verwendet werden, da es Schüler-spezifisch ist
				result.add(jg);
			}
		}
		return result;
	});

	vorgaengerklasse = computed<KlassenDatenMinimal | null>({
		get: () => {
			const id = this.proxy.idVorgaengerklasse;
			return (id === null) ? null : this.manager().klassenByIdVorabschnitt.get(id) ?? null;
		},
		set: (value) => this.proxy.idVorgaengerklasse = value?.id ?? null,
	});

	folgeklasse = computed<KlassenDatenMinimal | null>({
		get: () => {
			const id = this.proxy.idFolgeklasse;
			return (id === null) ? null : this.manager().klassenByIdFolgeAbschnitt.get(id) ?? null;
		},
		set: (value) => this.proxy.idFolgeklasse = value?.id ?? null,
	});

	kuerzelVorgaengerklasse = computed<string | null>(() => this.proxy.kuerzelVorgaengerklasse ?? '&nbsp;');
	kuerzelFolgeklasse = computed<string | null>(() => this.proxy.kuerzelFolgeklasse ?? '&nbsp;');

	listeFolgeklassen = computed<List<KlassenDatenMinimal>>(() => {
		const result = new ArrayList<KlassenDatenMinimal>();
		const idJahrgang = this.proxy.idJahrgang;
		if (idJahrgang === null) {
			for (const kl of this.manager().klassenByIdFolgeAbschnitt.values()) {
				result.add(kl);
			}
			return result;
		}
		const jg = this.manager().jahrgaenge.get(idJahrgang);
		if (jg === null) {
			return result;
		}
		const tmpJg = (jg.idJahrgang === null) ? null : Jahrgaenge.data().getWertByIDOrNull(jg.idJahrgang);
		if (tmpJg === null) {
			return result;
		}
		let schulgliederung: Schulgliederung | null;
		if (jg.idSchulgliederung === null) {
			schulgliederung = Schulgliederung.getDefault(this.manager().schulform());
		} else {
			schulgliederung = Schulgliederung.data().getWertByIDOrNull(jg.idSchulgliederung);
		}
		for (const kl of this.manager().klassenByIdFolgeAbschnitt.values()) {
			if (kl.idJahrgang === null) {
				result.add(kl); // Jahrgangunabhängige Klassen können als Vorgängerklassen vorkommen
			} else {
				const jgKl = this.manager().jahrgaenge.get(kl.idJahrgang);
				const tmpJgKl = (jgKl === null) || (jgKl.idJahrgang === null) ? null : Jahrgaenge.data().getWertByIDOrNull(jgKl.idJahrgang);
				if (tmpJgKl === null) {
					continue;
				}
				if (tmpJgKl.isNachfolgerVon(schuleStateImpl.schuljahr, tmpJg, this.manager().schulform(), schulgliederung)) {
					result.add(kl);
				}
			}
		}
		return result;
	});

	listeVorgaengerklassen = computed<List<KlassenDatenMinimal>>(() => {
		const result = new ArrayList<KlassenDatenMinimal>();
		const idJahrgang = this.proxy.idJahrgang;
		if (idJahrgang === null) {
			for (const kl of this.manager().klassenByIdVorabschnitt.values()) {
				result.add(kl);
			}
			return result;
		}
		const jg = this.manager().jahrgaenge.get(idJahrgang);
		if (jg === null) {
			return result;
		}
		const tmpJg = (jg.idJahrgang === null) ? null : Jahrgaenge.data().getWertByIDOrNull(jg.idJahrgang);
		if (tmpJg === null) {
			return result;
		}
		let schulgliederung: Schulgliederung | null;
		if (jg.idSchulgliederung === null) {
			schulgliederung = Schulgliederung.getDefault(this.manager().schulform());
		} else {
			schulgliederung = Schulgliederung.data().getWertByIDOrNull(jg.idSchulgliederung);
		}
		for (const kl of this.manager().klassenByIdVorabschnitt.values()) {
			if (kl.idJahrgang === null) {
				result.add(kl); // Jahrgangunabhängige Klassen können als Vorgängerklassen vorkommen
			} else {
				const jgKl = this.manager().jahrgaenge.get(kl.idJahrgang);
				const tmpJgKl = (jgKl === null) || (jgKl.idJahrgang === null) ? null : Jahrgaenge.data().getWertByIDOrNull(jgKl.idJahrgang);
				if (tmpJgKl === null) {
					continue;
				}
				if (tmpJgKl.isVorgaengerVon(schuleStateImpl.schuljahr, tmpJg, this.manager().schulform(), schulgliederung)) {
					result.add(kl);
				}
			}
		}
		return result;
	});
}
