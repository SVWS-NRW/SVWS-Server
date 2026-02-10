import { computed } from "vue";
import { AllgemeinbildendOrganisationsformen, ArrayList, BerufskollegOrganisationsformen, Jahrgaenge, Klassenart, Schulgliederung, WeiterbildungskollegOrganisationsformen, type JahrgangsDaten, type KlassenDaten, type List } from "@core";
import { ModelProxy, ValidatorKlassenKuerzel, ValidatorStringLength, type KlassenListeManager } from "@ui";

/**
 * Der spezielle ModelProxy für die Klassen-Daten
 */
export class KlassenModelProxy extends ModelProxy<KlassenDaten> {

	protected manager: () => KlassenListeManager;
	protected mapKlassenVorigerAbschnitt: () => Map<number, KlassenDaten>;
	protected mapKlassenFolgenderAbschnitt: () => Map<number, KlassenDaten>;

	/**
	 * Erstellt einen validierenden Proxy für das Core-DTO KlassenDaten.
	 *
	 * @param data               ein Lambda für den Zugriff auf die "Original"-Daten
	 * @param vorhanden          die vorhandenen Klassen
	 * @param patchMethod        ggf. die Methode zum Patchen der einzelnen Attribute, sofern das automatische Patchen
	 *                           bei Änderungen gewünscht ist
	 */
	constructor(data: () => KlassenDaten, vorhanden: () => Iterable<KlassenDaten>, manager: () => KlassenListeManager,
		mapKlassenVorigerAbschnitt: () => Map<number, KlassenDaten>, mapKlassenFolgenderAbschnitt: () => Map<number, KlassenDaten>,
		listOfAutopatchProps?: Iterable<keyof KlassenDaten>, patch?: (data: Partial<KlassenDaten>) => Promise<boolean>) {
		super({ data, patch, listOfAutopatchProps });
		this.manager = manager;
		this.mapKlassenVorigerAbschnitt = mapKlassenVorigerAbschnitt;
		this.mapKlassenFolgenderAbschnitt = mapKlassenFolgenderAbschnitt;
		this.addValidator(new ValidatorKlassenKuerzel(() => this.proxy.kuerzel ?? null, vorhanden), "kuerzel");
		this.addValidator(new ValidatorStringLength(() => this.proxy.beschreibung, 150, 1), "beschreibung");
		this.validate();
	}

	parallelitaet = computed<string | null>({
		get: () => this.proxy.parallelitaet ?? '---',
		set: (value) => this.proxy.parallelitaet = value,
	});

	schulgliederung = computed<Schulgliederung | null>({
		get: () => (this.proxy.idSchulgliederung === -1) ? null : Schulgliederung.data().getWertByID(this.proxy.idSchulgliederung),
		set: (value) => this.proxy.idSchulgliederung = value?.daten(this.manager().getSchuljahr())?.id ?? -1,
	});
	schulgliederungen = computed(() => Schulgliederung.getBySchuljahrAndSchulform(this.manager().getSchuljahr(), this.manager().schulform()));

	klassenart = computed<Klassenart | null>({
		get: () => (this.proxy.idKlassenart === -1) ? null : Klassenart.data().getWertByID(this.proxy.idKlassenart),
		set: (value) => this.proxy.idKlassenart = value?.daten(this.manager().getSchuljahr())?.id ?? -1,
	});
	klassenarten = computed(() => Klassenart.getBySchuljahrAndSchulform(this.manager().getSchuljahr(), this.manager().schulform()));

	organisationsformAllgemeinbildend = computed<AllgemeinbildendOrganisationsformen | null>({
		get: () => {
			const id = this.proxy.idAllgemeinbildendOrganisationsform;
			return (id === null) ? null : AllgemeinbildendOrganisationsformen.data().getWertByID(id);
		},
		set: (value) => this.proxy.idAllgemeinbildendOrganisationsform = value?.daten(this.manager().getSchuljahr())?.id ?? null,
	});
	organisationsformenAllgemeinbildend = computed(() => AllgemeinbildendOrganisationsformen.values());

	organisationsformBerufsbildend = computed<BerufskollegOrganisationsformen | null>({
		get: () => {
			const id = this.proxy.idBerufsbildendOrganisationsform;
			return (id === null) ? null : BerufskollegOrganisationsformen.data().getWertByID(id);
		},
		set: (value) => this.proxy.idBerufsbildendOrganisationsform = value?.daten(this.manager().getSchuljahr())?.id ?? null,
	});
	organisationsformenBerufsbildend = computed(() => BerufskollegOrganisationsformen.values());

	organisationsformWeiterbildend = computed<WeiterbildungskollegOrganisationsformen | null>({
		get: () => {
			const id = this.proxy.idWeiterbildungOrganisationsform;
			return (id === null) ? null : WeiterbildungskollegOrganisationsformen.data().getWertByID(id);
		},
		set: (value) => this.proxy.idWeiterbildungOrganisationsform = value?.daten(this.manager().getSchuljahr())?.id ?? null,
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

	vorgaengerklasse = computed<KlassenDaten | null>({
		get: () => {
			const id = this.proxy.idVorgaengerklasse;
			return (id === null) ? null : this.mapKlassenVorigerAbschnitt().get(id) ?? null;
		},
		set: (value) => this.proxy.idVorgaengerklasse = value?.id ?? null,
	});

	folgeklasse = computed<KlassenDaten | null>({
		get: () => {
			const id = this.proxy.idFolgeklasse;
			return (id === null) ? null : this.mapKlassenFolgenderAbschnitt().get(id) ?? null;
		},
		set: (value) => this.proxy.idFolgeklasse = value?.id ?? null,
	});

	kuerzelVorgaengerklasse = computed<string | null>(() => this.proxy.kuerzelVorgaengerklasse ?? '&nbsp;');
	kuerzelFolgeklasse = computed<string | null>(() => this.proxy.kuerzelFolgeklasse ?? '&nbsp;');

	listeFolgeklassen = computed<List<KlassenDaten>>(() => {
		const result = new ArrayList<KlassenDaten>();
		const idJahrgang = this.proxy.idJahrgang;
		if (idJahrgang === null) {
			for (const kl of this.mapKlassenFolgenderAbschnitt().values()) {
				result.add(kl);
			}
			return result;
		}
		const jg = this.manager().jahrgaenge.get(idJahrgang);
		if (jg === null) {
			return result;
		}
		const tmpJg = (jg.kuerzelStatistik === null) ? null : Jahrgaenge.data().getWertBySchluessel(jg.kuerzelStatistik);
		if (tmpJg === null) {
			return result;
		}
		let schulgliederung: Schulgliederung | null = null;
		if (jg.kuerzelSchulgliederung === null) {
			schulgliederung = Schulgliederung.getDefault(this.manager().schulform());
		} else {
			schulgliederung = Schulgliederung.data().getWertBySchluessel(jg.kuerzelSchulgliederung);
		}
		for (const kl of this.mapKlassenFolgenderAbschnitt().values()) {
			if (kl.idJahrgang === null) {
				result.add(kl); // Jahrgangunabhängige Klassen können als Vorgängerklassen vorkommen
			} else {
				const jgKl = this.manager().jahrgaenge.get(kl.idJahrgang);
				const tmpJgKl = (jgKl === null) || (jgKl.kuerzelStatistik === null) ? null : Jahrgaenge.data().getWertBySchluessel(jgKl.kuerzelStatistik);
				if (tmpJgKl === null) {
					continue;
				}
				if (tmpJgKl.isNachfolgerVon(this.manager().getSchuljahr(), tmpJg, this.manager().schulform(), schulgliederung)) {
					result.add(kl);
				}
			}
		}
		return result;
	});

	listeVorgaengerklassen = computed<List<KlassenDaten>>(() => {
		const result = new ArrayList<KlassenDaten>();
		const idJahrgang = this.proxy.idJahrgang;
		if (idJahrgang === null) {
			for (const kl of this.mapKlassenVorigerAbschnitt().values()) {
				result.add(kl);
			}
			return result;
		}
		const jg = this.manager().jahrgaenge.get(idJahrgang);
		if (jg === null) {
			return result;
		}
		const tmpJg = (jg.kuerzelStatistik === null) ? null : Jahrgaenge.data().getWertBySchluessel(jg.kuerzelStatistik);
		if (tmpJg === null) {
			return result;
		}
		let schulgliederung: Schulgliederung | null = null;
		if (jg.kuerzelSchulgliederung === null) {
			schulgliederung = Schulgliederung.getDefault(this.manager().schulform());
		} else {
			schulgliederung = Schulgliederung.data().getWertBySchluessel(jg.kuerzelSchulgliederung);
		}
		for (const kl of this.mapKlassenVorigerAbschnitt().values()) {
			if (kl.idJahrgang === null) {
				result.add(kl); // Jahrgangunabhängige Klassen können als Vorgängerklassen vorkommen
			} else {
				const jgKl = this.manager().jahrgaenge.get(kl.idJahrgang);
				const tmpJgKl = (jgKl === null) || (jgKl.kuerzelStatistik === null) ? null : Jahrgaenge.data().getWertBySchluessel(jgKl.kuerzelStatistik);
				if (tmpJgKl === null) {
					continue;
				}
				if (tmpJgKl.isVorgaengerVon(this.manager().getSchuljahr(), tmpJg, this.manager().schulform(), schulgliederung)) {
					result.add(kl);
				}
			}
		}
		return result;
	});
}
