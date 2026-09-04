import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { configStateImpl } from "./ConfigStateImpl";
import { benutzerStateImpl } from "./BenutzerStateImpl";
import type { ApiFile } from "@core/api/BaseApi";
import { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import { GostBelegpruefungErgebnis } from "@core/core/abschluss/gost/GostBelegpruefungErgebnis";
import { GostBelegpruefungsArt } from "@core/core/abschluss/gost/GostBelegpruefungsArt";
import { HashMap2D } from "@core/core/adt/map/HashMap2D";
import type { Abiturdaten } from "@core/core/data/gost/Abiturdaten";
import type { GostBeratungslehrer } from "@core/core/data/gost/GostBeratungslehrer";
import type { GostBlockungListeneintrag } from "@core/core/data/gost/GostBlockungListeneintrag";
import type { GostBlockungsergebnis } from "@core/core/data/gost/GostBlockungsergebnis";
import { GostJahrgang } from "@core/core/data/gost/GostJahrgang";
import { GostJahrgangsdaten } from "@core/core/data/gost/GostJahrgangsdaten";
import { GostLaufbahnplanungBeratungsdaten } from "@core/core/data/gost/GostLaufbahnplanungBeratungsdaten";
import type { GostSchuelerFachwahl } from "@core/core/data/gost/GostSchuelerFachwahl";
import { GostSchuelerGKLWahl } from "@core/core/data/gost/GostSchuelerGKLWahl";
import type { GostLaufbahnplanungExportV2 } from "@core/core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerTyp } from "@core/core/types/benutzer/BenutzerTyp";
import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import { GostFaecherManager } from "@core/core/utils/gost/GostFaecherManager";
import { ArrayList } from "@core/java/util/ArrayList";
import { HashMap } from "@core/java/util/HashMap";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { List } from "@core/java/util/List";
import type { GostKlausurvorgabeEintrag, GostLaufbahnplanungState, GostBelegpruefungsModus } from "@ui/states/GostLaufbahnplanungState";
import { StateManager } from "@ui/ui/StateManager";


interface GostLaufbahnplanungReactiveState {
	mode: 'schueler' | 'abiturjahrgang' | undefined;
	auswahlSchueler: SchuelerListeEintrag | undefined;
	auswahlAbiturjahrgang: number | undefined;
	abiturdaten: Abiturdaten | undefined;
	abiturdatenManager: AbiturdatenManager | undefined;
	faecherManager: GostFaecherManager;
	gostBelegpruefungErgebnis: GostBelegpruefungErgebnis;
	gostJahrgang: GostJahrgang;
	gostJahrgangsdaten: GostJahrgangsdaten;
	gostLaufbahnBeratungsdaten: GostLaufbahnplanungBeratungsdaten;
	mapKlausurvorgaben: JavaMap<number, GostKlausurvorgabeEintrag>;
	gklMoeglich: HashMap2D<number, GostHalbjahr, List<GostKlausurvorgabeEintrag>>,
	gklWahlen: GostSchuelerGKLWahl,
	listeLehrer: List<LehrerListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	zwischenspeicher: GostLaufbahnplanungExportV2 | undefined;
}

/**
 * Der Zustand der Laufbahnplanung der Gymnasialen Oberstufe
 */
export class GostLaufbahnplanungStateImpl extends StateManager<GostLaufbahnplanungReactiveState> implements GostLaufbahnplanungState {

	public constructor() {
		super({
			mode: undefined,
			auswahlSchueler: undefined,
			auswahlAbiturjahrgang: undefined,
			abiturdaten: undefined,
			abiturdatenManager: undefined,
			faecherManager: new GostFaecherManager(-1),
			gostBelegpruefungErgebnis: new GostBelegpruefungErgebnis(),
			gostJahrgang: new GostJahrgang(),
			gostJahrgangsdaten: new GostJahrgangsdaten(),
			gostLaufbahnBeratungsdaten: new GostLaufbahnplanungBeratungsdaten(),
			mapKlausurvorgaben: new HashMap<number, GostKlausurvorgabeEintrag>(),
			gklMoeglich: new HashMap2D<number, GostHalbjahr, List<GostKlausurvorgabeEintrag>>(),
			gklWahlen: new GostSchuelerGKLWahl(),
			listeLehrer: new ArrayList<LehrerListeEintrag>(),
			mapLehrer: new Map<number, LehrerListeEintrag>(),
			zwischenspeicher: undefined,
		});
	}


	public async clear() {
		this.setPatchedDefaultState({});
	}

	public get valid(): boolean {
		return (this.mode !== undefined) && (this._state.value.abiturdatenManager !== undefined)
			&& (((this.mode === 'schueler') && (this._state.value.auswahlSchueler !== undefined))
				|| ((this.mode === 'abiturjahrgang') && (this._state.value.auswahlAbiturjahrgang !== undefined)));
	}

	public get mode(): 'schueler' | 'abiturjahrgang' | undefined {
		return this._state.value.mode;
	}

	public get schueler(): SchuelerListeEintrag {
		if (this.mode !== 'schueler') {
			throw new DeveloperNotificationException("Die Laufbahnplanung wurde für die Vorlagen bezüglich der Abiturjahrgänge initialisiert. Es stehen keine Schüler-spezifischen Informationen zur Verfügung.");
		}
		if (this._state.value.auswahlSchueler === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zur Laufbahnplanung abgerufen oder eingegeben werden.");
		}
		return this._state.value.auswahlSchueler;
	}

	public get schuelerOrNull(): SchuelerListeEintrag | null {
		return this._state.value.auswahlSchueler ?? null;
	}

	private get auswahlAbiturjahrgang(): number {
		if (this._state.value.auswahlAbiturjahrgang === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Abiturjahrgang nicht initialisiert");
		}
		return this._state.value.auswahlAbiturjahrgang;
	}

	public get id(): number | undefined {
		const { typ, typID } = benutzerStateImpl.benutzerdaten;
		return BenutzerTyp.getByID(typ) === BenutzerTyp.LEHRER ? typID : undefined;
	}

	public get listeLehrer(): List<LehrerListeEintrag> {
		return this._state.value.listeLehrer;
	}

	public get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}

	public get gostJahrgangsdaten(): GostJahrgangsdaten {
		return this._state.value.gostJahrgangsdaten;
	}

	public get beratungslehrer(): List<GostBeratungslehrer> {
		return new ArrayList(this._state.value.gostJahrgangsdaten.beratungslehrer);
	}

	public get gostLaufbahnBeratungsdaten(): GostLaufbahnplanungBeratungsdaten {
		return this._state.value.gostLaufbahnBeratungsdaten;
	}

	public get gostBelegpruefungErgebnis(): GostBelegpruefungErgebnis {
		return this._state.value.gostBelegpruefungErgebnis;
	}

	public get abiturdatenManager(): AbiturdatenManager {
		if (this._state.value.abiturdatenManager === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Abiturdaten-Manager nicht initialisiert");
		}
		return this._state.value.abiturdatenManager;
	}

	public get gostBelegpruefungsArt(): GostBelegpruefungsModus {
		const s = configStateImpl.config.getValue("app.gost.belegpruefungsart");
		if ((s === 'ef1') || (s === 'gesamt') || (s === 'auto')) {
			return s;
		}
		void configStateImpl.config.setValue("app.gost.belegpruefungsart", 'auto');
		throw new DeveloperNotificationException("Es wurde eine fehlerhafte Belegpruefungsart als Standardauswahl hinterlegt");
	}

	public async setGostBelegpruefungsArt(gostBelegpruefungsArt: GostBelegpruefungsModus) {
		await configStateImpl.config.setValue("app.gost.belegpruefungsart", gostBelegpruefungsArt);
		await this.setGostBelegpruefungErgebnis();
	}

	public async exportLaufbahnplanung(): Promise<ApiFile> {
		if (this.mode !== 'schueler') {
			throw new DeveloperNotificationException("Der Export von Schüler-Laufbahnplanungen steht nur in der Schüleransicht zur Verfügung.");
		}
		return await api.server.exportGostSchuelerLaufbahnplanung(api.schema, this.schueler.id);
	}


	public async importLaufbahnplanung(data: FormData): Promise<void> {
		await api.call(async (data: FormData) => {
			if (this.mode !== 'schueler') {
				throw new DeveloperNotificationException("Der Import von Schüler-Laufbahnplanungen steht nur in der Schüleransicht zur Verfügung.");
			}
			await api.server.importGostSchuelerLaufbahnplanung(data, api.schema, this.schueler.id);
			const abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.schueler.id);
			const gklWahlen = await api.server.getGostSchuelerGKLWahl(api.schema, this.schueler.id);
			const abiturdatenManager = this.createAbiturdatenmanager(abiturdaten);
			if (abiturdatenManager === undefined) {
				return;
			}
			const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
			this.setPatchedState({ abiturdaten, gklWahlen, abiturdatenManager, gostBelegpruefungErgebnis });
		})(data);
	}


	public async setWahl(idFach: number, wahl: GostSchuelerFachwahl) {
		let abiturdaten;
		if (this.mode === 'schueler') {
			await api.server.patchGostSchuelerFachwahl(wahl, api.schema, this.schueler.id, idFach);
			abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.schueler.id);
		} else if (this.mode === 'abiturjahrgang') {
			await api.server.patchGostAbiturjahrgangFachwahl(wahl, api.schema, this.auswahlAbiturjahrgang, idFach);
			abiturdaten = await api.server.getGostAbiturjahrgangLaufbahnplanung(api.schema, this.auswahlAbiturjahrgang);
		}
		this._state.value.abiturdaten = abiturdaten;
		await this.setGostBelegpruefungErgebnis();
	}


	public getKlausurvorgabe(id: number | null): GostKlausurvorgabeEintrag | null {
		if (id === null) {
			return null;
		}
		return this._state.value.mapKlausurvorgaben.get(id);
	}


	public istGKLMoeglich(idFach: number, halbjahr: GostHalbjahr): List<GostKlausurvorgabeEintrag> {
		return this._state.value.gklMoeglich.getOrException(idFach, halbjahr);
	}


	private pruefeGKLWahl(idVorgabe: number | null, idFach: number, halbjahr: GostHalbjahr): boolean {
		if (idVorgabe === null) {
			return false;
		}
		const vorgabe = this.getKlausurvorgabe(idVorgabe);
		if ((vorgabe !== null) && (vorgabe.fach.id === idFach) && (vorgabe.halbjahr === halbjahr)) {
			return true;
		}
		return false;
	}


	public istGKLGewaehlt(idFach: number, halbjahr: GostHalbjahr): boolean {
		const result = ((halbjahr.istEinfuehrungsphase()
				&& (this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeEF_Sprachen, idFach, halbjahr)
					|| this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeEF_GW, idFach, halbjahr)
					|| this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeEF_NW, idFach, halbjahr)))
			|| (halbjahr.istQualifikationsphase()
				&& (this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeQ_Sprachen, idFach, halbjahr)
					|| this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeQ_GW, idFach, halbjahr)
					|| this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeQ_NW, idFach, halbjahr))));
		return result;
	}


	public get gklWahlen(): GostSchuelerGKLWahl {
		return this._state.value.gklWahlen;
	}


	public async patchGKLWahlen(patch: Partial<GostSchuelerGKLWahl>) {
		const neu = Object.assign(new GostSchuelerGKLWahl(), this._state.value.gklWahlen, patch);
		await api.server.putGostSchuelerGKLWahl(neu, api.schema);
		this.setPatchedState({ gklWahlen: neu });
	}


	public async patchBeratungsdaten(data: Partial<GostLaufbahnplanungBeratungsdaten>) {
		if (this.mode !== 'schueler') {
			throw new DeveloperNotificationException("Anpassungen an Beratungsdaten sind nur in der Schüleransicht möglich.");
		}
		await api.server.patchGostSchuelerLaufbahnplanungBeratungsdaten(data, api.schema, this.schueler.id);
		const gostLaufbahnBeratungsdaten = this.gostLaufbahnBeratungsdaten;
		this.setPatchedState({ gostLaufbahnBeratungsdaten: Object.assign(gostLaufbahnBeratungsdaten, data) });
	}


	public get hatZwischenspeicher(): boolean {
		return (this.zwischenspeicher !== undefined);
	}


	public async saveLaufbahnplanung(): Promise<void> {
		if (this.mode !== 'schueler') {
			throw new DeveloperNotificationException("Das Zwischenspeichern der aktuellen Planungsdaten ist nur in der Schüleransicht möglich.");
		}
		const zwischenspeicher = await api.server.exportGostSchuelerLaufbahnplanungsdaten(api.schema, this.schueler.id);
		this.setPatchedState({ zwischenspeicher });
	}

	public async restoreLaufbahnplanung(): Promise<void> {
		await api.call(async (): Promise<void> => {
			if (this.mode !== 'schueler') {
				throw new DeveloperNotificationException("Das Zwischenspeichern der aktuellen Planungsdaten ist nur in der Schüleransicht möglich.");
			}
			if (this._state.value.zwischenspeicher === undefined) {
				return;
			}
			await api.server.importGostSchuelerLaufbahnplanungsdatenV2(this._state.value.zwischenspeicher, api.schema, this.schueler.id);
			const abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.schueler.id);
			const abiturdatenManager = this.createAbiturdatenmanager(abiturdaten);
			if (abiturdatenManager === undefined) {
				return;
			}
			const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
			this.setPatchedState({ zwischenspeicher: undefined, abiturdaten, abiturdatenManager, gostBelegpruefungErgebnis });
		})();
	}

	public async resetFachwahlen(forceDelete: boolean) {
		await api.call(async (forceDelete: boolean) => {
			if (this.mode === 'schueler') {
				if (forceDelete) {
					await api.server.deleteGostSchuelerFachwahlen(api.schema, this.schueler.id);
				} else {
					await api.server.resetGostSchuelerFachwahlen(api.schema, this.schueler.id);
				}
				const abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.schueler.id);
				this._state.value.abiturdaten = abiturdaten;
				const gklWahlen = await api.server.getGostSchuelerGKLWahl(api.schema, this.schueler.id);
				this._state.value.gklWahlen = gklWahlen;
				await this.setGostBelegpruefungErgebnis();
			} else if (this.mode === 'abiturjahrgang') {
				await api.server.resetGostAbiturjahrgangFachwahlen(api.schema, this.auswahlAbiturjahrgang);
				const abiturdaten = await api.server.getGostAbiturjahrgangLaufbahnplanung(api.schema, this.auswahlAbiturjahrgang);
				this._state.value.abiturdaten = abiturdaten;
				await this.setGostBelegpruefungErgebnis();
			}
		})(forceDelete);
	}

	private async gotoKursplanungMode(halbjahr: GostHalbjahr, idschueler: number | null): Promise<void> {
		// Bestimme die Liste der Blockungen
		const blockungsliste = await api.server.getGostAbiturjahrgangBlockungsliste(api.schema, this.gostJahrgangsdaten.abiturjahr, halbjahr.id);
		if (blockungsliste.isEmpty()) {
			return;
		}
		// Bestimme die aktive Blockung, falls gesetzt, sonst nehme das erste in der Liste
		let blockungseintrag: GostBlockungListeneintrag | undefined = undefined;
		for (const e of blockungsliste) {
			if (e.istAktiv) {
				blockungseintrag = e;
				break;
			}
		}
		blockungseintrag ??= blockungsliste.get(0);
		// Bestimme die Daten der Blockung mit der Ergebnisliste
		const blockungsdaten = await api.server.getGostBlockung(api.schema, blockungseintrag.id);
		if (blockungsdaten.ergebnisse.isEmpty()) {
			return;
		}
		// Bestimme das aktive Ergebnis, falls gesetzt, sonst nehme das erste in der Liste
		let ergebnis: GostBlockungsergebnis | undefined = undefined;
		for (const e of blockungsdaten.ergebnisse) {
			if (e.istAktiv) {
				ergebnis = e;
				break;
			}
		}
		ergebnis ??= blockungsdaten.ergebnisse.get(0);
		let route;
		if (idschueler === null) {
			route = RouteNode.getNodeByName("gost.kursplanung")!.getRoute({
				abiturjahr: this.gostJahrgangsdaten.abiturjahr,
				halbjahr: halbjahr.id,
				idblockung: blockungsdaten.id,
				idergebnis: ergebnis.id,
			});
		} else {
			route = RouteNode.getNodeByName("gost.kursplanung.schueler")!.getRoute({
				abiturjahr: this.gostJahrgangsdaten.abiturjahr,
				halbjahr: halbjahr.id,
				idblockung: blockungsdaten.id,
				idergebnis: ergebnis.id,
				idschueler,
			});
		}
		await RouteManager.doRoute(route);
	}

	public async gotoKursplanung(halbjahr: GostHalbjahr): Promise<void> {
		await api.call(async (halbjahr: GostHalbjahr): Promise<void> => {
			if (this.mode === 'schueler') {
				await this.gotoKursplanungMode(halbjahr, this.schueler.id);
			} else if (this.mode === 'abiturjahrgang') {
				await this.gotoKursplanungMode(halbjahr, null);
			}
		})(halbjahr);
	}


	private createAbiturdatenmanager(daten?: Abiturdaten): AbiturdatenManager | undefined {
		const abiturdaten = daten || this._state.value.abiturdaten;
		if (abiturdaten === undefined) {
			return;
		}
		const art = this.gostBelegpruefungsArt;
		if (art === 'ef1') {
			return new AbiturdatenManager(abiturdaten, this._state.value.gostJahrgangsdaten, this._state.value.faecherManager, GostBelegpruefungsArt.EF1);
		}
		if (art === 'gesamt') {
			return new AbiturdatenManager(abiturdaten, this._state.value.gostJahrgangsdaten, this._state.value.faecherManager, GostBelegpruefungsArt.GESAMT);
		}
		const abiturdatenManager = new AbiturdatenManager(abiturdaten, this._state.value.gostJahrgangsdaten, this._state.value.faecherManager, GostBelegpruefungsArt.GESAMT);
		if (abiturdatenManager.pruefeBelegungExistiert(abiturdatenManager.getFachbelegungen(), GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)) {
			return abiturdatenManager;
		}
		return new AbiturdatenManager(abiturdaten, this._state.value.gostJahrgangsdaten, this._state.value.faecherManager, GostBelegpruefungsArt.EF1);
	}

	private async setGostBelegpruefungErgebnis() {
		const abiturdatenManager = this.createAbiturdatenmanager();
		if (abiturdatenManager === undefined) {
			return;
		}
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({ abiturdatenManager, gostBelegpruefungErgebnis });
	}

	public get zwischenspeicher(): GostLaufbahnplanungExportV2 | undefined {
		return this._state.value.zwischenspeicher;
	}

	public get faechermanager(): GostFaecherManager {
		return this._state.value.faecherManager;
	}

	public set faecherManager(faecherManager: GostFaecherManager | undefined) {
		this.setPatchedState({ faecherManager });
	}

	public async addBeratungslehrer(id: number) {
		if (this.mode !== 'abiturjahrgang') {
			throw new DeveloperNotificationException("Das Hinzufügen eines Beratungslehrers ist nur in der Ansicht der Laufbahnplanung für den Abiturjahrgang möglich.");
		}
		api.status.start();
		const lehrer = await api.server.addGostAbiturjahrgangBeratungslehrer(id, api.schema, this.gostJahrgangsdaten.abiturjahr);
		this._state.value.gostJahrgangsdaten.beratungslehrer.add(lehrer);
		this.setPatchedState({ gostJahrgangsdaten: this._state.value.gostJahrgangsdaten });
		api.status.stop();
	}

	public async removeBeratungslehrer(eintraege: GostBeratungslehrer[]) {
		if (this.mode !== 'abiturjahrgang') {
			throw new DeveloperNotificationException("Das Entfernen eines Beratungslehrers ist nur in der Ansicht der Laufbahnplanung für den Abiturjahrgang möglich.");
		}
		api.status.start();
		for (const eintrag of eintraege) {
			await api.server.removeGostAbiturjahrgangBeratungslehrer(eintrag.id, api.schema, this.gostJahrgangsdaten.abiturjahr);
			for (let i = 0; i < this.gostJahrgangsdaten.beratungslehrer.size() ; i++) {
				const b = this.gostJahrgangsdaten.beratungslehrer.get(i);
				if (b.id === eintrag.id) {
					this.gostJahrgangsdaten.beratungslehrer.removeElementAt(i);
				}
			}
		}
		this.setPatchedState({ gostJahrgangsdaten: this._state.value.gostJahrgangsdaten });
		api.status.stop();
	}


	private async ladeGKL(abiturjahr: number, faecherManager: GostFaecherManager): Promise<{
		gklMoeglich: HashMap2D<number, GostHalbjahr, List<GostKlausurvorgabeEintrag>>,
		mapKlausurvorgaben: JavaMap<number, GostKlausurvorgabeEintrag>
	}> {
		const vorgaben = await api.server.getGostKlausurenVorgabenJahrgang(api.schema, abiturjahr);

		const gklMoeglich = new HashMap2D<number, GostHalbjahr, List<GostKlausurvorgabeEintrag>>();
		for (const fach of faecherManager.faecher()) {
			for (const halbjahr of GostHalbjahr.values()) {
				gklMoeglich.put(fach.id, halbjahr, new ArrayList<GostKlausurvorgabeEintrag>());
			}
		}

		const mapKlausurvorgaben = new HashMap<number, GostKlausurvorgabeEintrag>();
		for (const vorgabe of vorgaben) {
			const halbjahr = GostHalbjahr.fromIDorException(vorgabe.halbjahr);
			const fach = faecherManager.get(vorgabe.idFach);
			if (fach === null) {
				continue;
			}
			const eintrag = { fach, halbjahr, vorgabe };
			mapKlausurvorgaben.put(vorgabe.id, eintrag);
			if ((!vorgabe.istGklMoeglich) || !gklMoeglich.containsKey1(vorgabe.idFach)) {
				continue;
			}
			gklMoeglich.getOrException(vorgabe.idFach, halbjahr).add(eintrag);
		}
		return { gklMoeglich, mapKlausurvorgaben };
	}


	public async ladeSchuelerDaten(auswahlSchueler: SchuelerListeEintrag | null) {
		if ((this._state.value.mode === 'schueler') && (auswahlSchueler === this._state.value.auswahlSchueler)) {
			return;
		}
		if (auswahlSchueler === null) {
			this.setPatchedDefaultState({});
		} else {
			const gostJahrgang = new GostJahrgang();
			if (auswahlSchueler.abiturjahrgang !== null) {
				gostJahrgang.abiturjahr = auswahlSchueler.abiturjahrgang;
			}
			gostJahrgang.jahrgang = auswahlSchueler.jahrgang;
			try {
				const abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, auswahlSchueler.id);
				const gostJahrgangsdaten = await api.server.getGostAbiturjahrgang(api.schema, gostJahrgang.abiturjahr);
				const gostLaufbahnBeratungsdaten = await api.server.getGostSchuelerLaufbahnplanungBeratungsdaten(api.schema, auswahlSchueler.id);
				const listGostFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, gostJahrgang.abiturjahr);
				const faecherManager = new GostFaecherManager(abiturdaten.schuljahrAbitur, listGostFaecher);
				const listFachkombinationen	= await api.server.getGostAbiturjahrgangFachkombinationen(api.schema, gostJahrgang.abiturjahr);
				faecherManager.addFachkombinationenAll(listFachkombinationen);
				const { gklMoeglich, mapKlausurvorgaben } = await this.ladeGKL(gostJahrgang.abiturjahr, faecherManager);
				const gklWahlen = await api.server.getGostSchuelerGKLWahl(api.schema, auswahlSchueler.id);
				const listeLehrer = await api.server.getLehrer(api.schema);
				const mapLehrer = new Map<number, LehrerListeEintrag>();
				for (const l of listeLehrer) {
					mapLehrer.set(l.id, l);
				}
				const mode = 'schueler';
				this.setPatchedState({ mode, auswahlSchueler, abiturdaten, gostJahrgang, gostJahrgangsdaten, gostLaufbahnBeratungsdaten, faecherManager,
					mapKlausurvorgaben, gklMoeglich, gklWahlen, listeLehrer, mapLehrer, zwischenspeicher: undefined });
				await this.setGostBelegpruefungErgebnis();
			} catch {
				throw new DeveloperNotificationException("Die Laufbahndaten konnten nicht eingeholt werden, sind für diesen Schüler Laufbahndaten möglich?");
			}
		}
	}

	public async ladeAbijahrgangsDaten(auswahlAbiturjahrgang: number) {
		if ((this._state.value.mode === 'abiturjahrgang') && (auswahlAbiturjahrgang === this._state.value.auswahlAbiturjahrgang)) {
			return;
		} else {
			const gostJahrgang = new GostJahrgang();
			gostJahrgang.abiturjahr = auswahlAbiturjahrgang;
			gostJahrgang.jahrgang = GostHalbjahr.EF1.jahrgang; // Gehe bei der Vorlage von einer Planung ab EF.1 ohne vorhandene/vergangene Daten aus
			try {
				const abiturdaten = await api.server.getGostAbiturjahrgangLaufbahnplanung(api.schema, auswahlAbiturjahrgang);
				const gostJahrgangsdaten = await api.server.getGostAbiturjahrgang(api.schema, gostJahrgang.abiturjahr);
				const listGostFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, gostJahrgang.abiturjahr);
				const faecherManager = new GostFaecherManager(gostJahrgang.abiturjahr - 1, listGostFaecher);
				const listFachkombinationen	= await api.server.getGostAbiturjahrgangFachkombinationen(api.schema, gostJahrgang.abiturjahr);
				faecherManager.addFachkombinationenAll(listFachkombinationen);
				const { gklMoeglich, mapKlausurvorgaben } = await this.ladeGKL(gostJahrgang.abiturjahr, faecherManager);
				const listeLehrer = await api.server.getLehrer(api.schema);
				const mapLehrer = new Map<number, LehrerListeEintrag>();
				for (const l of listeLehrer) {
					mapLehrer.set(l.id, l);
				}
				const mode = 'abiturjahrgang';
				this.setPatchedState({ mode, auswahlAbiturjahrgang, abiturdaten, gostJahrgang, gostJahrgangsdaten, faecherManager,
					mapKlausurvorgaben, gklMoeglich, listeLehrer, mapLehrer });
				await this.setGostBelegpruefungErgebnis();
			} catch {
				throw new DeveloperNotificationException("Die Laufbahndaten konnten nicht eingeholt werden, sind für diesen Abiturjahrgang Laufbahndaten möglich?");
			}
		}
	}

}

export const gostLaufbahnplanungStateImpl = new GostLaufbahnplanungStateImpl();
