import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { GostJahrgangsdaten } from "@core/core/data/gost/GostJahrgangsdaten";
import { GostKlausurenAlleKlausurdaten } from "@core/core/data/gost/klausuren/GostKlausurenAlleKlausurdaten";
import { GostKlausurenHalbjahresdaten } from "@core/core/data/gost/klausuren/GostKlausurenHalbjahresdaten";
import type { GostKlausurraum } from "@core/core/data/gost/klausuren/GostKlausurraum";
import type { GostKlausurraumRich } from "@core/core/data/gost/klausuren/GostKlausurraumRich";
import { GostKlausurtermin } from "@core/core/data/gost/klausuren/GostKlausurtermin";
import type { GostKlausurterminblockungDaten } from "@core/core/data/gost/klausuren/GostKlausurterminblockungDaten";
import type { GostKlausurvorgabe } from "@core/core/data/gost/klausuren/GostKlausurvorgabe";
import { GostKursklausur } from "@core/core/data/gost/klausuren/GostKursklausur";
import type { GostNachschreibterminblockungKonfiguration } from "@core/core/data/gost/klausuren/GostNachschreibterminblockungKonfiguration";
import { GostSchuelerklausur } from "@core/core/data/gost/klausuren/GostSchuelerklausur";
import { GostSchuelerklausurtermin } from "@core/core/data/gost/klausuren/GostSchuelerklausurtermin";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import { GostFaecherManager } from "@core/core/utils/gost/GostFaecherManager";
import { GostKlausurplanManager } from "@core/core/utils/gost/klausuren/GostKlausurplanManager";
import { ListUtils } from "@core/core/utils/ListUtils";
import { StundenplanManager } from "@core/core/utils/stundenplan/StundenplanManager";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { GostKlausurplanungState } from "@ui/states/GostKlausurplanungState";
import { CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX } from "@ui/states/GostKlausurplanungState";
import { StateManager } from "@ui/ui/StateManager";

import { ApiStatus } from "~/components/ApiStatus";
import { api } from "~/router/Api";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";

interface GostKlausurplanungReactiveState {
	// Daten nur abhängig von dem Abiturjahrgang
	abiturjahr: number | undefined;
	abschnitt: Schuljahresabschnitt | undefined;
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	halbjahr: GostHalbjahr;
	manager: GostKlausurplanManager;
	kalenderdatum: string | undefined;
	termin: GostKlausurtermin | undefined;
}

const defaultState = <GostKlausurplanungReactiveState> {
	abiturjahr: undefined,
	abschnitt: undefined,
	jahrgangsdaten: undefined,
	halbjahr: GostHalbjahr.EF1,
	manager: new GostKlausurplanManager(),
	kalenderdatum: undefined,
	termin: undefined,
};


export class GostKlausurplanungStateImpl extends StateManager<GostKlausurplanungReactiveState> implements GostKlausurplanungState {

	private readonly vorgabenSchreibsperre = new ApiStatus();

	public constructor() {
		super(defaultState);
	}

	public get abschnitt(): Schuljahresabschnitt | undefined {
		return this._state.value.abschnitt;
	}

	public get abschnittOrException(): Schuljahresabschnitt {
		if (this._state.value.abschnitt === undefined) {
			throw new DeveloperNotificationException("Für die Klausurplanung wurde kein passender Schuljahresabschnitt gefunden.");
		}
		return this._state.value.abschnitt;
	}

	public get abiturjahr(): number {
		if (this._state.value.abiturjahr === undefined) {
			throw new DeveloperNotificationException("Es wurde noch kein Abiturjahrgang geladen.");
		}
		return this._state.value.abiturjahr;
	}

	public async setAbiturjahr(abiturjahr: number | undefined): Promise<boolean> {
		const abiturjahrwechsel = (abiturjahr !== this._state.value.abiturjahr);
		if (!abiturjahrwechsel) {
			return false;
		}
		if (abiturjahr === undefined) {
			this._state.value = this._defaultState;
			return true;
		}
		try {
			api.status.start();
			// Lade die Daten für die Kursplanung, die nur vom Abiturjahrgang abhängen
			const jahrgangsdaten = await api.server.getGostAbiturjahrgang(api.schema, abiturjahr);
			const result: Partial<GostKlausurplanungReactiveState> = {
				abiturjahr: abiturjahr,
				jahrgangsdaten: jahrgangsdaten,
				halbjahr: this._state.value.halbjahr,
				abschnitt: this._state.value.abschnitt,
			};
			Object.assign(result, { manager: this._state.value.manager, kalenderdatum: this._state.value.kalenderdatum });
			// Setze den State neu
			this.setPatchedDefaultState(result);
		} finally {
			api.status.stop();
		}
		return abiturjahrwechsel;
	}

	public get jahrgangsdaten(): GostJahrgangsdaten {
		if (this._state.value.jahrgangsdaten === undefined) {
			throw new DeveloperNotificationException("Es wurde noch kein Abiturjahrgang geladen, so dass keine Jahrgangsdaten zur Verfügung stehen.");
		}
		return this._state.value.jahrgangsdaten;
	}

	public get halbjahr(): GostHalbjahr {
		return this._state.value.halbjahr;
	}

	public async setHalbjahr(halbjahr: GostHalbjahr, hjChanged: boolean): Promise<boolean> {
		const abiturjahr = this.abiturjahr;
		const manager = this.manager;
		const schuljahr = halbjahr.getSchuljahrFromAbiturjahr(abiturjahr);
		const abschnitt = (abiturjahr === -1) ? undefined : abschnittStateImpl.getBySchuljahrUndHalbjahr(schuljahr, halbjahr.halbjahr) ?? undefined;
		const vorlagenGeladen = manager.getFaecherManagerOrNull(-1) !== null;
		const fehlendeKlausurdaten = (abiturjahr === -1) ? new ArrayList<GostKlausurenHalbjahresdaten>() : manager.getMissingHjKlausurdata(abiturjahr, halbjahr.id);
		const stundenplaeneFehlen = (abschnitt !== undefined) && !manager.stundenplanManagerGeladenByAbschnitt(abschnitt.id);
		const fehlendatenFehlen = (abschnitt !== undefined) && !manager.hasFehlenddatenZuAbijahrUndHalbjahr(abiturjahr, halbjahr);

		if (!hjChanged && (halbjahr === this.halbjahr) && (abschnitt?.id === this.abschnitt?.id) && vorlagenGeladen
			&& fehlendeKlausurdaten.isEmpty() && !stundenplaeneFehlen && !fehlendatenFehlen) {
			return false;
		}

		const apiStatusGestartet = api.status.start();
		try {
			if (!vorlagenGeladen) {
				const vorlagen = await api.server.getGostKlausurenVorgabenJahrgang(api.schema, -1);
				const faecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, -1);
				const faecherManager = new GostFaecherManager(schuleStateImpl.abschnitt.schuljahr, faecher);
				manager.vorgabeAddAll(vorlagen);
				manager.setFaecherManager(-1, faecherManager);
			}

			if (!fehlendeKlausurdaten.isEmpty()) {
				const klausurdatenGzip = await api.server.getGostKlausurenAlleKlausurdatenGZip(fehlendeKlausurdaten, api.schema);
				const klausurdatenBlob = await new Response(klausurdatenGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
				const klausurdaten = GostKlausurenAlleKlausurdaten.transpilerFromJSON(await klausurdatenBlob.text());
				manager.addAllData(klausurdaten);
			}

			if (stundenplaeneFehlen) {
				const listStundenplaene = await api.server.getStundenplanlisteAktivFuerAbschnitt(api.schema, abschnitt.id);
				const listStundenplanManager = new ArrayList<StundenplanManager>();
				for (const stundenplan of listStundenplaene) {
					const stundenplandaten = await api.server.getStundenplan(api.schema, stundenplan.id);
					const unterrichte = await api.server.getStundenplanUnterrichte(api.schema, stundenplan.id);
					const pausenaufsichten = await api.server.getStundenplanPausenaufsichten(api.schema, stundenplan.id);
					const unterrichtsverteilung = await api.server.getStundenplanUnterrichtsverteilung(api.schema, stundenplan.id);
					const stundenplanmanager = new StundenplanManager(stundenplandaten, unterrichte, pausenaufsichten, unterrichtsverteilung);
					listStundenplanManager.add(stundenplanmanager);
				}
				manager.stundenplanManagerAddAllBySchuljahresabschnittsid(abschnitt.id, listStundenplanManager);
			}

			if (fehlendatenFehlen) {
				manager.setKlausurDataFehlend(await this.ladeFehlendData(abiturjahr, halbjahr));
			}

			this.setPatchedState({ halbjahr, abschnitt });
			return true;
		} finally {
			if (apiStatusGestartet) {
				api.status.stop();
			}
		}
	}

	public get manager(): GostKlausurplanManager {
		return this._state.value.manager;
	}

	getConfigValue = (key: string) => configStateImpl.config.getValue(CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX + key);
	getConfigNumberValue = (key: string) => configStateImpl.config.getNumberValue(CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX + key);

	setConfigValue = async (key: string, value: string | number) => {
		if (typeof value === 'number') {
			await configStateImpl.config.setNumberValue(CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX + key, value);
		} else {
			await configStateImpl.config.setValue(CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX + key, value);
		}
		this.commit();
	};

	setRaumTermin = (termin: GostKlausurtermin | null) => {
		if ((termin !== null) && (this.selectedTermin?.equals(termin) !== true)) {
			this.setSelectedTermin(termin);
		}
	};

	public get quartal(): 0 | 1 | 2 {
		const value = Number.parseInt(this.getConfigValue("quartal"));
		return (value === 1 || value === 2) ? value : 0;
	}

	setQuartal = (value: 0 | 1 | 2) => {
		if (this.quartal !== value) {
			void this.setConfigValue("quartal", value.toString());
		}
	};

	public get kwWarnLimit(): number {
		return this.getConfigNumberValue("kwWarnLimit");
	}

	setKwWarnLimit = (value: number | null) => {
		if (value === null) {
			return;
		}
		if (value > this.kwErrorLimit) {
			void this.setConfigValue("kwErrorLimit", value);
		}
		void this.setConfigValue("kwWarnLimit", value);
	};

	public get kwErrorLimit(): number {
		return this.getConfigNumberValue("kwErrorLimit");
	}

	setKwErrorLimit = (value: number | null) => {
		if (value === null) {
			return;
		}
		if (value < this.kwWarnLimit) {
			void this.setConfigValue("kwWarnLimit", value);
		}
		void this.setConfigValue("kwErrorLimit", value);
	};

	private async ladeFehlendData(abiturjahr: number, halbjahr: GostHalbjahr): Promise<GostKlausurenHalbjahresdaten> {
		const fehlendDataGzip = await api.server.getGostKlausurenKlausurdatenIssuesGZip(api.schema, abiturjahr, halbjahr.id);
		const fehlendDataBlob = await new Response(fehlendDataGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
		return GostKlausurenHalbjahresdaten.transpilerFromJSON(await fehlendDataBlob.text());
	}

	reloadFehlendData = async () => {
		if ((this.abiturjahr !== -1) && (this.abschnitt !== undefined)) {
			const manager = this.manager;
			manager.setKlausurDataFehlend(await this.ladeFehlendData(this.abiturjahr, this.halbjahr));
		}
	};

	public get kalenderdatum(): string | undefined {
		return this._state.value.kalenderdatum;
	}

	public get kalenderdatumOrException(): string {
		if (this._state.value.kalenderdatum === undefined) {
			throw new DeveloperNotificationException("Es wurde kein Kalenderdatum für die Klausurplanung gesetzt.");
		}
		return this._state.value.kalenderdatum;
	}

	setKalenderdatum = (value: string | undefined) => {
		if (this._state.value.kalenderdatum !== value) {
			this.setPatchedState({ kalenderdatum: value });
		}
	};

	public get selectedTermin(): GostKlausurtermin | undefined {
		return this._state.value.termin;
	}

	setSelectedTermin = (value: GostKlausurtermin | undefined) => {
		if (this._state.value.termin !== value) {
			this.setPatchedState({ termin: value });
		}
	};

	get zeigeAlleJahrgaenge(): boolean {
		return this.getConfigValue("zeigeAlleJahrgaenge") === 'true';
	}

	setZeigeAlleJahrgaenge = (value: boolean) => {
		void this.setConfigValue('zeigeAlleJahrgaenge', value ? "true" : "false");
	};

	get zeigeBetroffeneUnterrichte(): boolean {
		return this.getConfigValue('zeigeBetroffeneUnterrichte') === 'true';
	}

	setZeigeBetroffeneUnterrichte = (value: boolean) => {
		void this.setConfigValue('zeigeBetroffeneUnterrichte', value ? 'true' : 'false');
	};

	get betroffeneUnterrichteAnwesenheitsschwelle(): number {
		const value = this.getConfigNumberValue('betroffeneUnterrichteAnwesenheitsschwelle');
		return Number.isFinite(value) ? value : 50;
	}

	setBetroffeneUnterrichteAnwesenheitsschwelle = (value: number | null) => {
		if (value === null) {
			return;
		}
		void this.setConfigValue('betroffeneUnterrichteAnwesenheitsschwelle', Math.max(0, Math.min(100, value)));
	};

	erzeugeKlausurtermin = async (quartal: number, ht: boolean): Promise<GostKlausurtermin> => {
		api.status.start();
		try {
			const terminNeu: Partial<GostKlausurtermin> = new GostKlausurtermin();
			terminNeu.idSchuljahresabschnitt = this.abschnittOrException.id;
			terminNeu.abiturjahrgang = this.abiturjahr;
			terminNeu.halbjahr = this.halbjahr.id;
			terminNeu.quartal = quartal;
			terminNeu.istHaupttermin = ht;
			delete terminNeu.id;
			const termin = await api.server.createGostKlausurenKlausurtermin(terminNeu, api.schema);
			this.manager.terminAdd(termin);
			this.commit();
			return termin;
		} finally {
			api.status.stop();
		}
	};

	loescheKlausurtermine = async (termine: List<GostKlausurtermin>) => {
		api.status.start();
		try {
			const terminIds = new ArrayList<number>();
			for (const termin of termine) {
				terminIds.add(termin.id);
			}
			await api.server.deleteGostKlausurenKlausurtermine(terminIds, api.schema);
			this.manager.terminRemoveAll(termine);
			this.commit();
		} finally {
			api.status.stop();
		}
	};

	loescheKursklausuren = async (klausuren: List<GostKursklausur> | GostKursklausur[]) => {
		api.status.start();
		try {
			const klausurIds = new ArrayList<number>();
			for (const klausur of klausuren) {
				klausurIds.add(klausur.id);
			}
			await api.server.deleteGostKlausurenKursklausuren(klausurIds, api.schema);
			const klausListe = new ArrayList<GostKursklausur>();
			if (Array.isArray(klausuren)) {
				for (const klausur of klausuren) {
					klausListe.add(klausur);
				}
			} else {
				klausListe.addAll(klausuren);
			}
			this.manager.kursklausurRemoveAll(klausListe);
			await this.reloadFehlendData();
			this.commit();
		} finally {
			api.status.stop();
		}
	};

	erzeugeSchuelerklausuren = async (klausuren: List<Partial<GostSchuelerklausur>>) => {
		api.status.start();
		try {
			const requestKlausuren = new ArrayList<Partial<GostSchuelerklausur>>();
			for (const klausur of klausuren) {
				const requestKlausur = { ...klausur };
				delete requestKlausur.id;
				requestKlausuren.add(requestKlausur);
			}
			const dtos = await api.server.createGostKlausurenSchuelerklausuren(requestKlausuren, api.schema);
			this.manager.addKlausurData(dtos);
			this.commit();
		} finally {
			api.status.stop();
		}
	};

	loescheSchuelerklausuren = async (klausuren: List<GostSchuelerklausur>) => {
		api.status.start();
		try {
			const ids = new ArrayList<number>();
			for (const klausur of klausuren) {
				ids.add(klausur.id);
			}
			await api.server.deleteGostKlausurenSchuelerklausuren(ids, api.schema);
			this.manager.schuelerklausurRemoveAll(klausuren);
			this.commit();
		} finally {
			api.status.stop();
		}
	};

	patchKlausur = async (klausur: GostKursklausur | GostSchuelerklausur | GostSchuelerklausurtermin, patch: Partial<GostKursklausur | GostSchuelerklausur | GostSchuelerklausurtermin>): Promise<void> => {
		try {
			api.status.start();
			patch.id = klausur.id;
			if (klausur instanceof GostKursklausur) {
				const result = await api.server.patchGostKlausurenKursklausur(patch, api.schema);
				if (result.kursklausurenPatched.isEmpty()) {
					throw new DeveloperNotificationException("Die gepatchte Kursklausur fehlt in der API-Response.");
				}
				this.manager.kursklausurPatchAttributesAndSetzeRaumZuSchuelerklausuren(result.kursklausurenPatched.getFirst(), result);
			} else if (klausur instanceof GostSchuelerklausurtermin) {
				const result = await api.server.patchGostKlausurenSchuelerklausurtermin(patch, api.schema);
				this.manager.schuelerklausurterminPatchAttributesAndSetzeRaumZuSchuelerklausuren(Object.assign(klausur, patch), result);
			} else if (klausur instanceof GostSchuelerklausur) {
				const result = await api.server.patchGostKlausurenSchuelerklausur(patch, api.schema);
				this.manager.schuelerklausurPatchAttributes(Object.assign(klausur, result));
			}
			this.commit();
		} finally {
			api.status.stop();
		}
	};

	patchSchuelerklausurtermine = async (klausuren: List<GostSchuelerklausurtermin>, patch: Partial<GostSchuelerklausurtermin>): Promise<void> => {
		if (klausuren.isEmpty()) {
			return;
		}
		try {
			api.status.start();
			const patches = new ArrayList<Partial<GostSchuelerklausurtermin>>();
			for (const klausur of klausuren) {
				patches.add({ id: klausur.id, ...patch });
			}
			const result = await api.server.patchGostKlausurenSchuelerklausurtermineMultiple(patches, api.schema);
			for (const klausur of klausuren) {
				this.manager.schuelerklausurterminPatchAttributes(Object.assign(klausur, patch));
			}
			this.manager.setzeRaumZuSchuelerklausuren(result);
			this.commit();
		} finally {
			api.status.stop();
		}
	};

	public get vorgabenSchreibvorgangLaeuft(): boolean {
		return this.vorgabenSchreibsperre.pending;
	}

	private async schreibeVorgaben(aktion: () => Promise<void>): Promise<void> {
		if (!this.vorgabenSchreibsperre.start()) {
			return;
		}
		const apiStatusGestartet = api.status.start();
		try {
			await aktion();
		} finally {
			if (apiStatusGestartet) {
				api.status.stop();
			}
			this.vorgabenSchreibsperre.stop();
		}
	}

	private async vorgabenCreateMultiple(vorgaben: List<GostKlausurvorgabe>) {
		const requests = new ArrayList<Partial<GostKlausurvorgabe>>();
		for (const vorgabe of vorgaben) {
			const { id, ...request } = vorgabe;
			requests.add(request);
		}
		const manager = this.manager;
		const neueVorgaben = await api.server.createGostKlausurenVorgabenMultiple(requests, api.schema);
		manager.vorgabeAddAll(neueVorgaben);
	}

	erzeugeDefaultKlausurvorgaben = async (quartal: number, abiturjahr: number) => {
		await this.schreibeVorgaben(async () => {
			const vorgaben = this.manager.vorgabeErzeugeFehlendeStandardVorlagen(this.halbjahr, quartal, abiturjahr);
			if (vorgaben.isEmpty()) {
				return;
			}
			await this.vorgabenCreateMultiple(vorgaben);
			this.commit();
		});
	};

	erzeugeKlausurvorgabe = async (vorgabe: Partial<GostKlausurvorgabe>) => {
		await this.schreibeVorgaben(async () => {
			try {
				const request: Partial<GostKlausurvorgabe> = {
					...vorgabe,
					abiturjahrgang: this.abiturjahr,
					halbjahr: this.halbjahr.id,
				};
				delete request.id;
				const neueVorgabe = await api.server.createGostKlausurenVorgabe(request, api.schema);
				this.manager.vorgabeAdd(neueVorgabe);
				await this.reloadFehlendData();
			} finally {
				this.commit();
			}
		});
	};

	patchKlausurvorgabe = async (vorgabe: Partial<GostKlausurvorgabe>, id: number) => {
		vorgabe.id = id;
		await this.patchKlausurvorgaben(ListUtils.create1(vorgabe));
	};

	patchKlausurvorgaben = async (vorgaben: List<Partial<GostKlausurvorgabe>>) => {
		if (vorgaben.isEmpty()) {
			return;
		}
		await this.schreibeVorgaben(async () => {
			const result = await api.server.patchGostKlausurenVorgabenMultiple(vorgaben, api.schema);
			for (const vorgabe of result.vorgabenPatched) {
				this.manager.vorgabePatchAttributes(vorgabe);
			}
			this.manager.setzeRaumZuSchuelerklausuren(result);
			this.commit();
		});
	};

	loescheKlausurvorgaben = async (vorgaben: List<GostKlausurvorgabe>) => {
		await this.schreibeVorgaben(async () => {
			const vorgabeIds = new ArrayList<number>();
			for (const vorgabe of vorgaben) {
				vorgabeIds.add(vorgabe.id);
			}
			await api.server.deleteGostKlausurenVorgabenMultiple(vorgabeIds, api.schema);
			this.manager.vorgabeRemoveAll(vorgaben);
			vorgaben.clear();
			await this.reloadFehlendData();
			this.commit();
		});
	};

	erzeugeKursklausurenAusVorgaben = async (quartal: number) => {
		api.status.start();
		try {
			const result = await api.server.createGostKlausurenKursklausurenJahrgangHalbjahrQuartal(api.schema, this.abiturjahr, this.halbjahr.id, quartal);
			this.manager.addKlausurData(result);
			this.commit();
			return result;
		} finally {
			api.status.stop();
		}
	};

	patchKlausurtermin = async (id: number, termin: Partial<GostKlausurtermin>) => {
		api.status.start();
		try {
			const oldTtermin = this.manager.terminGetByIdOrException(id);
			termin.id = id;
			const raumDataChanged = await api.server.patchGostKlausurenKlausurtermin(termin, api.schema);
			this.manager.terminPatchAttributesAndSetzeRaumZuSchuelerklausuren(raumDataChanged.terminPatched ?? Object.assign(oldTtermin, termin), raumDataChanged);
			this.commit();
		} finally {
			api.status.stop();
		}
	};

	erzeugeVorgabenAusVorlage = async (quartal: number) => {
		await this.schreibeVorgaben(async () => {
			const { manager, abiturjahr, halbjahr } = this;
			const vorlagen = manager.vorgabeGetMengeByHalbjahrAndQuartal(-1, halbjahr, quartal);
			const zielvorgaben = manager.vorgabeGetMengeByHalbjahrAndQuartal(abiturjahr, halbjahr, quartal);
			const kopien = GostKlausurplanManager.vorgabeKopiereFehlendeVorlagen(abiturjahr, halbjahr, quartal, vorlagen, zielvorgaben);
			if (kopien.isEmpty()) {
				return;
			}
			try {
				await this.vorgabenCreateMultiple(kopien);
				await this.reloadFehlendData();
			} finally {
				this.commit();
			}
		});
	};

	createKlausurraum = async (raum: Partial<GostKlausurraum>) => {
		api.status.start();
		try {
			const neuerRaum = await api.server.createGostKlausurenRaum(raum, api.schema);
			this.manager.raumAdd(neuerRaum);
			this.commit();
		} finally {
			api.status.stop();
		}
	};

	loescheKlausurraum = async (id: number): Promise<boolean> => {
		api.status.start();
		try {
			await api.server.deleteGostKlausurenRaum(api.schema, id);
			this.manager.raumRemoveById(id);
			this.commit();
			return true;
		} finally {
			api.status.stop();
		}
	};

	patchKlausurraum = async (id: number, raum: Partial<GostKlausurraum>): Promise<boolean> => {
		api.status.start();
		try {
			const oldRaum: GostKlausurraum = this.manager.raumGetByIdOrException(id);
			raum.id = id;
			await api.server.patchGostKlausurenRaum(raum, api.schema);
			this.manager.raumPatchAttributes(Object.assign(oldRaum, raum));
			this.commit();
			return true;
		} finally {
			api.status.stop();
		}
	};

	setzeRaumZuSchuelerklausuren = async (rRaeume: List<GostKlausurraumRich>, deleteFromRaeume: boolean): Promise<void> => {
		if (rRaeume.isEmpty()) {
			return;
		}
		api.status.start();
		try {
			let patchResponseData;
			if (deleteFromRaeume) {
				const ids = new ArrayList<number>();
				for (const raum of rRaeume) {
					ids.addAll(raum.idsSchuelerklausurtermine);
				}
				patchResponseData = await api.server.loescheGostSchuelerklausurtermineAusRaum(ids, api.schema);
			} else {
				patchResponseData = await api.server.setzeGostSchuelerklausurtermineZuRaum(rRaeume, api.schema);
			}
			this.manager.setzeRaumZuSchuelerklausuren(patchResponseData);
			this.commit();
		} finally {
			api.status.stop();
		}
	};

	ersetzeRaumzuweisungenFuerSchuelerklausurtermine = async (rRaeume: List<GostKlausurraumRich>): Promise<void> => {
		if (rRaeume.isEmpty()) {
			return;
		}
		api.status.start();
		try {
			const patchResponseData = await api.server.ersetzeGostSchuelerklausurtermineZuRaum(rRaeume, api.schema);
			this.manager.setzeRaumZuSchuelerklausuren(patchResponseData);
			this.commit();
		} finally {
			api.status.stop();
		}
	};

	blockenKursklausuren = async (blockungDaten: GostKlausurterminblockungDaten) => {
		api.status.start();
		try {
			const blockung = await api.server.blockenGostKursklausuren(blockungDaten, api.schema);
			this.manager.terminAddAll(blockung.termine);
			this.manager.kursklausurMengePatchAttributes(blockung.kursklausuren);
			this.commit();
		} finally {
			api.status.stop();
		}
	};

	blockenNachschreiber = async (config: GostNachschreibterminblockungKonfiguration) => {
		api.status.start();
		try {
			const blockungDaten = await api.server.blockenGostSchuelerklausurtermine(config, api.schema);
			this.manager.terminAddAll(blockungDaten.termine);
			for (const skt of blockungDaten.schuelerklausurtermine) {
				this.manager.schuelerklausurterminPatchAttributes(skt);
			}
			this.commit();
		} finally {
			api.status.stop();
		}
	};

	createSchuelerklausurtermin = async (skt: Partial<GostSchuelerklausurtermin>) => {
		api.status.start();
		try {
			const request = { ...skt };
			delete request.id;
			delete request.folgeNr;
			const result = await api.server.createGostKlausurenSchuelerklausurtermin(request, api.schema);
			this.manager.setzeRaumZuSchuelerklausuren(result);
			this.manager.schuelerklausurterminAddAll(result.schuelerklausurterminePatched);
			this.commit();
		} finally {
			api.status.stop();
		}
	};

	public async init(): Promise<void> {
		if (this._state.value.abiturjahr !== undefined) {
			return;
		}
		this.setPatchedDefaultState({ manager: new GostKlausurplanManager() });
	}

}

export const gostKlausurplanungStateImpl = new GostKlausurplanungStateImpl();
