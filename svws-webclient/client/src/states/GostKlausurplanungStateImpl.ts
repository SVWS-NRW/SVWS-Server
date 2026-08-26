
import type {
	GostJahrgangsdaten,
	GostKlausurvorgabe,
	GostKlausurraum,
	Schuljahresabschnitt,
	GostKlausurterminblockungDaten,
	GostNachschreibterminblockungKonfiguration,
	List,
	GostKlausurraumRich,
} from "@core";
import { ListUtils, GostKlausurtermin, ArrayList, StundenplanManager, GostFaecherManager, GostHalbjahr, GostKlausurplanManager, DeveloperNotificationException, GostSchuelerklausurtermin, GostKlausurenAlleKlausurdaten, GostKlausurenHalbjahresdaten, GostKursklausur, GostSchuelerklausur } from "@core";
import type { GostKlausurplanungState } from "@ui";
import { CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX, StateManager } from "@ui";

import { api } from "~/router/Api";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { configStateImpl } from "~/states/ConfigStateImpl";

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
		if (this._state.value.abiturjahr === undefined) {
			throw new DeveloperNotificationException("Es kann kein Halbjahr ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		}
		if (!hjChanged && (halbjahr === this._state.value.halbjahr)) {
			return false;
		}
		try {
			api.status.start();
			const result: Partial<GostKlausurplanungReactiveState> = {
				abschnitt: undefined,
				halbjahr: halbjahr,
			};

			if (!this.manager.isVorgabenInitialized()) {
				const listKlausurvorgaben = await api.server.getGostKlausurenVorgabenJahrgang(api.schema, -1);
				this.manager.vorgabeAddAll(listKlausurvorgaben);
				const listFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, -1);
				const faecherManager = new GostFaecherManager(schuleStateImpl.abschnitt.schuljahr, listFaecher);
				this.manager.setFaecherManager(-1, faecherManager);
			}
			if (this._state.value.abiturjahr === -1) {
				this.setPatchedState(result);
				return true;
			}
			const schuljahr = halbjahr.getSchuljahrFromAbiturjahr(this._state.value.abiturjahr);
			const abschnitt = abschnittStateImpl.getBySchuljahrUndHalbjahr(schuljahr, halbjahr.halbjahr);
			if (abschnitt !== null) {
				Object.assign(result, { abschnitt });
			}
			this.setPatchedState(result);
			const missingKlausurData = this.manager.getMissingHjKlausurdata(this.abiturjahr, halbjahr.id);
			if (!missingKlausurData.isEmpty()) {
				const klausurdatenGzip = await api.server.getGostKlausurenAlleKlausurdatenGZip(missingKlausurData, api.schema);
				const klausurdatenBlob = await new Response(klausurdatenGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
				const klausurdaten = GostKlausurenAlleKlausurdaten.transpilerFromJSON(await klausurdatenBlob.text());
				this.manager.addAllData(klausurdaten);
				this.setPatchedState(result);
			}
			if (abschnitt === null) {
				return true;
			}
			if (!this.manager.stundenplanManagerGeladenByAbschnitt(abschnitt.id)) {
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
				this.manager.stundenplanManagerAddAllBySchuljahresabschnittsid(abschnitt.id, listStundenplanManager);
			}
			this.setPatchedState(result);
			if (!this.manager.hasFehlenddatenZuAbijahrUndHalbjahr(this.abiturjahr, this._state.value.halbjahr)) {
				await this.reloadFehlendData();
				this.commit();
			}
			return true;
		} finally {
			api.status.stop();
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

	reloadFehlendData = async () => {
		if ((this.abiturjahr !== -1) && (this._state.value.abschnitt !== undefined)) {
			const fehlendDataGzip = await api.server.getGostKlausurenKlausurdatenIssuesGZip(api.schema, this.abiturjahr, this._state.value.halbjahr.id);
			const fehlendDataBlob = await new Response(fehlendDataGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
			const fehlendData = GostKlausurenHalbjahresdaten.transpilerFromJSON(await fehlendDataBlob.text());
			this.manager.setKlausurDataFehlend(fehlendData);
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

	erzeugeKlausurtermin = async (quartal: number, ht: boolean): Promise<GostKlausurtermin> => {
		api.status.start();
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
		api.status.stop();
		return termin;
	};

	loescheKlausurtermine = async (termine: List<GostKlausurtermin>) => {
		api.status.start();
		const terminIds = new ArrayList<number>();
		for (const termin of termine) {
			terminIds.add(termin.id);
		}
		await api.server.deleteGostKlausurenKlausurtermine(terminIds, api.schema);
		this.manager.terminRemoveAll(termine);
		this.commit();
		api.status.stop();
	};

	loescheKursklausuren = async (klausuren: List<GostKursklausur> | GostKursklausur[]) => {
		api.status.start();
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
		api.status.stop();
	};

	erzeugeSchuelerklausuren = async (klausuren: List<Partial<GostSchuelerklausur>>) => {
		api.status.start();
		for (const klausur of klausuren) {
			delete klausur.id;
		}
		const dtos = await api.server.createGostKlausurenSchuelerklausuren(klausuren, api.schema);
		this.manager.addKlausurData(dtos);
		this.commit();
		api.status.stop();
	};

	loescheSchuelerklausuren = async (klausuren: List<GostSchuelerklausur>) => {
		api.status.start();
		const ids = new ArrayList<number>();
		for (const klausur of klausuren) {
			ids.add(klausur.id);
		}
		await api.server.deleteGostKlausurenSchuelerklausuren(ids, api.schema);
		this.manager.schuelerklausurRemoveAll(klausuren);
		this.commit();
		api.status.stop();
	};

	patchKlausur = async (klausur: GostKursklausur | GostSchuelerklausur | GostSchuelerklausurtermin, patch: Partial<GostKursklausur | GostSchuelerklausur | GostSchuelerklausurtermin>): Promise<void> => {
		try {
			api.status.start();
			patch.id = klausur.id;
			if (klausur instanceof GostKursklausur) {
				const result = await api.server.patchGostKlausurenKursklausur(patch, api.schema);
				if (result.kursklausurPatched === null) {
					throw new DeveloperNotificationException("Die gepatchte Kursklausur fehlt in der API-Response.");
				}
				this.manager.kursklausurPatchAttributesAndSetzeRaumZuSchuelerklausuren(result.kursklausurPatched, result);
			} else if (klausur instanceof GostSchuelerklausurtermin) {
				const result = await api.server.patchGostKlausurenSchuelerklausurtermin(patch, api.schema);
				this.manager.schuelerklausurterminPatchAttributesAndSetzeRaumZuSchuelerklausuren(Object.assign(klausur, patch), result);
			} else if (klausur instanceof GostSchuelerklausur) {
				const result = await api.server.patchGostKlausurenSchuelerklausur(patch, api.schema);
				this.manager.schuelerklausurPatchAttributes(Object.assign(klausur, result));
			}
		} finally {
			this.commit();
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
				this.manager.schuelerklausurterminPatchAttributesAndSetzeRaumZuSchuelerklausuren(Object.assign(klausur, patch), result);
			}
		} finally {
			this.commit();
			api.status.stop();
		}
	};

	erzeugeDefaultKlausurvorgaben = async (quartal: number) => {
		api.status.start();
		const neueVorgaben = await api.server.createMissingGostKlausurenVorgabenVorlagen(api.schema, this.halbjahr.id, quartal);
		this.manager.vorgabeAddAll(neueVorgaben);
		this.commit();
		api.status.stop();
	};

	erzeugeKlausurvorgabe = async (vorgabe: Partial<GostKlausurvorgabe>) => {
		api.status.start();
		delete vorgabe.id;
		vorgabe.abiturjahrgang = this.abiturjahr;
		vorgabe.halbjahr = this.halbjahr.id;
		try {
			const neueVorgabe = await api.server.createGostKlausurenVorgabe(vorgabe, api.schema);
			this.manager.vorgabeAdd(neueVorgabe);
			await this.reloadFehlendData();
		} finally {
			this.commit();
			api.status.stop();
		}
	};

	patchKlausurvorgabe = async (vorgabe: Partial<GostKlausurvorgabe>, id: number) => {
		vorgabe.id = id;
		await this.patchKlausurvorgaben(ListUtils.create1(vorgabe));
	};

	patchKlausurvorgaben = async (vorgaben: List<Partial<GostKlausurvorgabe>>) => {
		if (vorgaben.isEmpty()) {
			return;
		}
		api.status.start();
		try {
			await api.server.patchGostKlausurenVorgabenMultiple(vorgaben, api.schema);
			for (const vorgabe of vorgaben) {
				this.manager.vorgabePatchAttributes(Object.assign(this.manager.vorgabeGetByIdOrException(vorgabe.id!), vorgabe));
			}
			this.commit();
		} finally {
			api.status.stop();
		}
	};

	loescheKlausurvorgaben = async (vorgaben: List<GostKlausurvorgabe>) => {
		api.status.start();
		const vorgabeIds = new ArrayList<number>();
		for (const vorgabe of vorgaben) {
			vorgabeIds.add(vorgabe.id);
		}
		await api.server.deleteGostKlausurenVorgabenMultiple(vorgabeIds, api.schema);
		this.manager.vorgabeRemoveAll(vorgaben);
		vorgaben.clear();
		await this.reloadFehlendData();
		this.commit();
		api.status.stop();
	};

	erzeugeKursklausurenAusVorgaben = async (quartal: number) => {
		api.status.start();
		try {
			const result = await api.server.createGostKlausurenKursklausurenJahrgangHalbjahrQuartal(api.schema, this.abiturjahr, this.halbjahr.id, quartal);
			this.manager.addKlausurData(result);
			return result;
		} finally {
			this.commit();
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
		} finally {
			this.commit();
			api.status.stop();
		}
	};

	erzeugeVorgabenAusVorlage = async (quartal: number) => {
		api.status.start();
		try {
			const listKlausurvorgaben = await api.server.copyGostKlausurenVorgabenVorlagenToJahrgang(api.schema, this.abiturjahr, this.halbjahr.id, quartal);
			this.manager.vorgabeAddAll(listKlausurvorgaben);
			await this.reloadFehlendData();
		} finally {
			this.commit();
			api.status.stop();
		}
	};

	createKlausurraum = async (raum: Partial<GostKlausurraum>) => {
		api.status.start();
		const neuerRaum = await api.server.createGostKlausurenRaum(raum, api.schema);
		this.manager.raumAdd(neuerRaum);
		this.commit();
		api.status.stop();
	};

	loescheKlausurraum = async (id: number): Promise<boolean> => {
		api.status.start();
		await api.server.deleteGostKlausurenRaum(api.schema, id);
		this.manager.raumRemoveById(id);
		this.commit();
		api.status.stop();
		return true;
	};

	patchKlausurraum = async (id: number, raum: Partial<GostKlausurraum>): Promise<boolean> => {
		api.status.start();
		const oldRaum: GostKlausurraum = this.manager.raumGetByIdOrException(id);
		raum.id = id;
		await api.server.patchGostKlausurenRaum(raum, api.schema);
		this.manager.raumPatchAttributes(Object.assign(oldRaum, raum));
		this.commit();
		api.status.stop();
		return true;
	};

	setzeRaumZuSchuelerklausuren = async (rRaeume: List<GostKlausurraumRich>, deleteFromRaeume: boolean): Promise<void> => {
		if (rRaeume.isEmpty()) {
			return;
		}
		api.status.start();
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
		api.status.stop();
	};

	blockenKursklausuren = async (blockungDaten: GostKlausurterminblockungDaten) => {
		api.status.start();
		const blockung = await api.server.blockenGostKursklausuren(blockungDaten, api.schema);
		this.manager.terminAddAll(blockung.termine);
		this.manager.kursklausurMengePatchAttributes(blockung.kursklausuren);
		this.commit();
		api.status.stop();
	};

	blockenNachschreiber = async (config: GostNachschreibterminblockungKonfiguration) => {
		api.status.start();
		const blockungDaten = await api.server.blockenGostSchuelerklausurtermine(config, api.schema);
		this.manager.terminAddAll(blockungDaten.termine);
		for (const skt of blockungDaten.schuelerklausurtermine) {
			this.manager.schuelerklausurterminPatchAttributes(skt);
		}
		this.commit();
		api.status.stop();
	};

	createSchuelerklausurtermin = async (skt: Partial<GostSchuelerklausurtermin>) => {
		api.status.start();
		delete skt.id;
		delete skt.folgeNr;
		const skNeu = await api.server.createGostKlausurenSchuelerklausurtermin(skt, api.schema);
		this.manager.schuelerklausurterminAdd(skNeu);
		this.commit();
		api.status.stop();
	};

}

export const gostKlausurplanungStateImpl = new GostKlausurplanungStateImpl();
