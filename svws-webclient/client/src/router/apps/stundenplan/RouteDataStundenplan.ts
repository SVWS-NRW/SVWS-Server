import type { List, Raum, JahrgangsDaten, LehrerListeEintrag, StundenplanPausenaufsichtBereichUpdate, StundenplanKalenderwochenzuordnung, SimpleOperationResponse, StundenplanListeEintrag, ApiFile, ReportingParameter} from "@core";
import { Stundenplan} from "@core";
import { StundenplanUnterrichtListeManager } from "@core";
import { StundenplanManager } from "@core";
import { StundenplanListeManager} from "@core";
import { StundenplanKonfiguration} from "@core";
import { StundenplanPausenaufsicht, Wochentag, StundenplanRaum, StundenplanAufsichtsbereich, StundenplanPausenzeit, StundenplanUnterricht, StundenplanZeitraster, DeveloperNotificationException, ArrayList, StundenplanJahrgang, UserNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";

import { routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import { routeStundenplanDaten } from "./RouteStundenplanDaten";
import { routeStundenplanNeu } from "~/router/apps/stundenplan/RouteStundenplanNeu";
import { routeKatalogPausenzeiten } from "./kataloge/RouteKatalogPausenzeiten";
import { routeStundenplanGruppenprozesse } from "./RouteStundenplanGruppenprozesse";

import { routeKatalogAufsichtsbereiche } from "./kataloge/RouteKatalogAufsichtsbereiche";
import { routeKatalogRaeume } from "./kataloge/RouteKatalogRaeume";
import { routeApp } from "../RouteApp";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { ViewType } from "@ui";

interface RouteStateStundenplan extends RouteStateAuswahlInterface<StundenplanListeManager> {
	stundenplanUnterrichtListeManager: StundenplanUnterrichtListeManager | undefined;
	listRaeume: List<Raum>;
	listPausenzeiten: List<StundenplanPausenzeit>;
	listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
	listJahrgaenge: List<JahrgangsDaten>;
	listLehrer: List<LehrerListeEintrag>;
	selected: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined;
}

const defaultState: RouteStateStundenplan = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	stundenplanUnterrichtListeManager: undefined,
	listRaeume: new ArrayList(),
	listPausenzeiten: new ArrayList(),
	listAufsichtsbereiche: new ArrayList(),
	listJahrgaenge: new ArrayList(),
	listLehrer: new ArrayList(),
	selected: undefined,
	view: routeStundenplanDaten,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataStundenplan extends RouteDataAuswahl<StundenplanListeManager, RouteStateStundenplan> {

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateStundenplan>> {
		const schuljahresabschnitt = api.mapAbschnitte.value.get(idSchuljahresabschnitt);
		if (schuljahresabschnitt === undefined)
			throw new DeveloperNotificationException('Es ist kein gültiger Schuljahresabschnitt ausgewählt');
		// Lade die Kataloge und erstelle den Manager
		const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const manager = new StundenplanListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, listStundenplaene, true);
		const auswahl = (listStundenplaene.size() > 0) ? listStundenplaene.get(0) : manager.getStundenplanVorlage();
		manager.setDaten(await this.ladeDaten(auswahl));
		const katalogDaten = await this.ladeVorlagenInternal();
		return { manager, ...katalogDaten };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public async ladeDaten(auswahl: StundenplanListeEintrag | null): Promise<StundenplanManager | null> {
		if (auswahl === null)
			return null;
		else if(auswahl.id < 0) {
			const stundenplan = new Stundenplan();
			stundenplan.id = -1;
			stundenplan.raeume = this.listRaeume;
			stundenplan.pausenzeiten = this.listPausenzeiten;
			stundenplan.aufsichtsbereiche = this.listAufsichtsbereiche;
			stundenplan.gueltigAb = "1979-07-10";
			stundenplan.gueltigBis = "2050-09-29";
			const stundenplanManager = new StundenplanManager(stundenplan, new ArrayList(), new ArrayList(), null);
			stundenplanManager.stundenplanKonfigSet(this.settingsDefaults);
			return stundenplanManager;
		}
		const daten = await api.server.getStundenplan(api.schema, auswahl.id);
		const unterrichtsdaten = await api.server.getStundenplanUnterrichte(api.schema, auswahl.id);
		const pausenaufsichten = await api.server.getStundenplanPausenaufsichten(api.schema, auswahl.id);
		const unterrichtsverteilung = await api.server.getStundenplanUnterrichtsverteilung(api.schema, auswahl.id);
		const stundenplanManager = new StundenplanManager(daten, unterrichtsdaten, pausenaufsichten, unterrichtsverteilung);
		stundenplanManager.stundenplanKonfigSet(this.settingsDefaults);
		const stundenplanUnterrichtListeManager = new StundenplanUnterrichtListeManager(api.schulform, stundenplanManager, api.schuleStammdaten.abschnitte, daten.idSchuljahresabschnitt);
		if (this._state.value.stundenplanUnterrichtListeManager !== undefined)
			stundenplanUnterrichtListeManager.useFilter(this._state.value.stundenplanUnterrichtListeManager);
		this.setPatchedState({ stundenplanUnterrichtListeManager }, false);
		return stundenplanManager;
	}

	public async reloadVorlagen(): Promise<void> {
		const katalogDaten = await this.ladeVorlagenInternal();
		this.setPatchedState(katalogDaten);
	}

	private async ladeVorlagenInternal(): Promise<{listRaeume: List<Raum>;
		listPausenzeiten: List<StundenplanPausenzeit>;
		listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
		listJahrgaenge: List<JahrgangsDaten>;
		listLehrer: List<LehrerListeEintrag>}> {
		const listRaeume = await api.server.getRaeume(api.schema);
		const listPausenzeiten = await api.server.getPausenzeiten(api.schema);
		const listAufsichtsbereiche = await api.server.getAufsichtsbereiche(api.schema);
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const listLehrer = await api.server.getLehrer(api.schema);
		return { listRaeume, listPausenzeiten, listAufsichtsbereiche, listJahrgaenge, listLehrer };
	}

	protected async doPatch(data: Partial<any>, id: number): Promise<void> {
		await api.server.patchStundenplan(data, api.schema, this.manager.auswahl().id);
		const daten = this.manager.daten();
		Object.assign(daten, data);
		if (data.wochenTypModell !== undefined)
			this.manager.daten().stundenplanSetWochenTypModell(data.wochenTypModell);
		if (data.bezeichnungStundenplan !== undefined)
			this.manager.auswahl().bezeichnung = data.bezeichnungStundenplan;
		if (data.gueltigAb !== undefined)
			this.manager.auswahl().gueltigAb = data.gueltigAb;
		if (data.gueltigBis !== undefined)
			this.manager.auswahl().gueltigBis = data.gueltigBis;
		if (data.aktiv !== undefined)
			this.manager.auswahl().aktiv = data.aktiv;
		if (!this.manager.daten().kalenderwochenzuordnungGetMengeUngueltige().isEmpty()) {
			const ids = new ArrayList<number>();
			for (const z of this.manager.daten().kalenderwochenzuordnungGetMengeUngueltige())
				ids.add(z.id);
			const res = await api.server.deleteStundenplanKalenderwochenzuordnungen(ids, api.schema, this.manager.auswahl().id);
			this.manager.daten().kalenderwochenzuordnungRemoveAll(res);
		}
	}
	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteStundenplaene(ids, api.schema);
	}
	protected deleteMessage(id: number, stundenplan: StundenplanListeEintrag): string {
		return `Stundenplan ${stundenplan.bezeichnung} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	public constructor() {
		super(defaultState, routeStundenplanGruppenprozesse, routeStundenplanNeu);
	}

	get stundenplanUnterrichtListeManager(): StundenplanUnterrichtListeManager {
		if (this._state.value.stundenplanUnterrichtListeManager === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: stundenplanUnterrichtListeManager nicht initialisiert");
		return this._state.value.stundenplanUnterrichtListeManager;
	}

	get listRaeume(): List<Raum> {
		return this._state.value.listRaeume;
	}

	get listPausenzeiten(): List<StundenplanPausenzeit> {
		return this._state.value.listPausenzeiten;
	}

	get listAufsichtsbereiche(): List<StundenplanAufsichtsbereich> {
		return this._state.value.listAufsichtsbereiche;
	}

	get listJahrgaenge(): List<JahrgangsDaten> {
		return this._state.value.listJahrgaenge;
	}

	get listLehrer(): List<LehrerListeEintrag> {
		return this._state.value.listLehrer;
	}

	get selected(): Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined {
		return this._state.value.selected;
	}

	get settingsDefaults(): StundenplanKonfiguration {
		const json = api.config.getValue("stundenplan.settings.defaults");
		return StundenplanKonfiguration.transpilerFromJSON(json);
	}

	setSettingsDefaults = async (value: StundenplanKonfiguration) => {
		const json = StundenplanKonfiguration.transpilerToJSON(value);
		await api.config.setValue('stundenplan.settings.defaults', json);
	}

	public setSelection = (selected: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined) => {
		this.setPatchedState({selected});
	}

	add = async (partial: Partial<Stundenplan>): Promise<void> => {
		const neu = await api.server.addStundenplan({ ...partial, idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(neu.id);
	}


	deleteKalenderwochenzuordnungen = async () => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		if (!this.manager.daten().kalenderwochenzuordnungGetMengeUngueltige().isEmpty()) {
			const ids = new ArrayList<number>();
			for (const z of this.manager.daten().kalenderwochenzuordnungGetMengeUngueltige())
				ids.add(z.id);
			const res = await api.server.deleteStundenplanKalenderwochenzuordnungen(ids, api.schema, this.manager.auswahl().id);
			this.manager.daten().kalenderwochenzuordnungRemoveAll(res);
		}
		api.status.stop();
	}

	patchKalenderwochenzuordnungen = async (data: List<StundenplanKalenderwochenzuordnung>) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const listHinzuzufuegen = new ArrayList<Partial<StundenplanKalenderwochenzuordnung>>();
		const listPatch = new ArrayList<StundenplanKalenderwochenzuordnung>();
		for (const e of data)
			if (e.id < 0) {
				const ee = e.clone() as Partial<StundenplanKalenderwochenzuordnung>;
				delete ee.id;
				listHinzuzufuegen.add(ee);
			} else
				listPatch.add(e);
		if (!listPatch.isEmpty()) {
			await api.server.patchStundenplanKalenderwochenzuordnungen(listPatch, api.schema, this.manager.auswahl().id);
			this.manager.daten().kalenderwochenzuordnungPatchAll(listPatch);
		}
		if (!listHinzuzufuegen.isEmpty()) {
			const resHinzufuegen = await api.server.addStundenplanKalenderwochenzuordnungen(listHinzuzufuegen, api.schema, this.manager.auswahl().id);
			this.manager.daten().kalenderwochenzuordnungAddAll(resHinzufuegen);
		}
		this.commit();
		api.status.stop();
	}

	addRaum = async (raum: Partial<StundenplanRaum>) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		if ((raum.kuerzel === undefined) || this.manager.daten().raumExistsByKuerzel(raum.kuerzel))
			throw new UserNotificationException('Ein Raum mit diesem Kürzel existiert bereits');
		api.status.start();
		delete raum.id;
		const _raum = await api.server.addStundenplanRaum(raum, api.schema, this.manager.auswahl().id);
		this.manager.daten().raumAdd(_raum);
		this.commit();
		api.status.stop();
	}

	patchRaum = async (data: Partial<StundenplanRaum>, id: number) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		if ((data.groesse !== undefined) && (data.groesse < 1))
			throw new UserNotificationException("Ein Raum muss mindestens eine Größe von 1 haben.");
		api.status.start();
		const raum = this.manager.daten().raumGetByIdOrException(id);
		// setze das Kürzel auf das bisherige, damit kein Fehler geworfen wird.
		if (data.kuerzel === undefined)
			data.kuerzel = raum.kuerzel;
		delete data.id;
		await api.server.patchStundenplanRaum(data, api.schema, id);
		this.manager.daten().raumPatchAttributes(Object.assign(raum, data));
		this.commit();
		api.status.stop();
	}

	removeRaeume = async (raeume: Iterable<StundenplanRaum>) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const listID = new ArrayList<number>();
		for (const raum of raeume)
			listID.add(raum.id);
		const list = await api.server.deleteStundenplanRaeume(listID, api.schema, this.manager.auswahl().id);
		this.manager.daten().raumRemoveAll(list);
		if (this.selected instanceof StundenplanRaum && list.contains(this.selected))
			this._state.value.selected = undefined;
		this.commit();
		api.status.stop();
	}

	raeumeSyncToVorlage = async (raeume: Iterable<StundenplanRaum>) => {
		const neu = new ArrayList<Partial<Raum>>();
		outer: for (const r of raeume) {
			const { id, ...partial} = r;
			for (const kr of this.listRaeume)
				if (kr.kuerzel === r.kuerzel) {
					await api.server.patchRaum({ beschreibung: partial.beschreibung, groesse: partial.groesse }, api.schema, kr.id);
					Object.assign(kr, partial);
					break outer;
				}
			neu.add(partial);
		}
		const liste = await api.server.addRaeume(neu, api.schema);
		this.listRaeume.addAll(liste);
		this.commit();
	}

	raeumeSyncToStundenplan = async (raeume: Iterable<Raum>) => {
		const stundenplanId = this.manager.auswahlID();
		if (stundenplanId === null)
			return;
		const neu = new ArrayList<Partial<Raum>>();
		for (const r of raeume) {
			const { id, ...partial} = r;
			const kr = this.manager.daten().raumGetByKuerzelOrNull(r.kuerzel);
			if (kr !== null) {
				await api.server.patchStundenplanRaum({ beschreibung: partial.beschreibung, groesse: partial.groesse }, api.schema, kr.id);
				Object.assign(kr, partial);
				this.manager.daten().raumPatchAttributes(kr);
				break;
			}
			neu.add(partial);
		}
		const liste = await api.server.addStundenplanRaeume(neu, api.schema, stundenplanId);
		this.manager.daten().raumAddAll(liste);
		this.commit();
	}

	addPausenzeiten = async (pausenzeiten: Iterable<Partial<StundenplanPausenzeit>>) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const list = new ArrayList<Partial<StundenplanPausenzeit>>();
		for (const p of pausenzeiten) {
			if ((p.wochentag !== undefined) && (p.beginn !== undefined) && (p.ende !== undefined)) {
				delete p.id;
				list.add(p);
			}
		}
		const _pausenzeiten = await api.server.addStundenplanPausenzeiten(list, api.schema, this.manager.auswahl().id)
		this.manager.daten().pausenzeitAddAll(_pausenzeiten);
		this.commit();
		api.status.stop();
	}

	patchPausenzeit = async (pausenzeit : Partial<StundenplanPausenzeit>, id: number) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		await api.server.patchStundenplanPausenzeit(pausenzeit, api.schema, id);
		const _pausenzeit = this.manager.daten().pausenzeitGetByIdOrException(id);
		this.manager.daten().pausenzeitPatchAttributes(Object.assign(_pausenzeit, pausenzeit));
		this.commit();
		api.status.stop();
	}

	removePausenzeiten = async (pausenzeiten: Iterable<StundenplanPausenzeit>) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const listID = new ArrayList<number>();
		for (const pausenzeit of pausenzeiten)
			listID.add(pausenzeit.id);
		if (!listID.isEmpty()) {
			const list = await api.server.deleteStundenplanPausenzeiten(listID, api.schema, this.manager.auswahl().id);
			this.manager.daten().pausenzeitRemoveAll(list);
			if (this.selected instanceof StundenplanPausenzeit && list.contains(this.selected))
				this._state.value.selected = undefined;
			this.commit();
		}
		api.status.stop();
	}

	pausenzeitenSyncToVorlage = async (pausenzeiten: Iterable<StundenplanPausenzeit>) => {
		const neu = new ArrayList<Partial<StundenplanPausenzeit>>();
		for (const { id, ...partial } of pausenzeiten)
			neu.add(partial);
		const liste = await api.server.addPausenzeiten(neu, api.schema);
		this.listPausenzeiten.addAll(liste);
		this.commit();
	}

	pausenzeitenSyncToStundenplan = async (pausenzeiten: Iterable<StundenplanPausenzeit>) => {
		const stundenplanId = this.manager.auswahlID();
		if (stundenplanId === null)
			return;
		const neu = new ArrayList<Partial<StundenplanPausenzeit>>();
		for (const { id, ...partial} of pausenzeiten)
			neu.add(partial);
		const liste = await api.server.addStundenplanPausenzeiten(neu, api.schema, stundenplanId);
		this.manager.daten().pausenzeitAddAll(liste);
		this.commit();
	}

	updateAufsichtBereich = async (update: StundenplanPausenaufsichtBereichUpdate, idPausenzeit?: number, idLehrer?: number) => {
		if (update.listEntfernen.isEmpty() && update.listHinzuzufuegen.isEmpty())
			return;
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		/** Der folgende Teil ist falsch, weil von einer einzigen Pausenzeit und einem einzigen Lehrer ausgegangen wird. Daher ist auch nur ein Element in der List zulässig. */
		const listAdd = new ArrayList<Partial<StundenplanPausenaufsicht>>();
		for (const aufsichtsbereich of update.listHinzuzufuegen)
			if ((aufsichtsbereich.idPausenaufsicht < 0) && (idPausenzeit !== undefined) && (idLehrer !== undefined)) {
				const aufsichtNeu: Partial<StundenplanPausenaufsicht> = new StundenplanPausenaufsicht();
				aufsichtNeu.idPausenzeit = idPausenzeit;
				aufsichtNeu.idLehrer = idLehrer;
				delete aufsichtNeu.id;
				listAdd.add(aufsichtNeu);
				// Abbrechen, damit nicht zu viele gleiche angelegt werden.
				break;
			}
		if (!listAdd.isEmpty()) {
			const res = await api.server.addStundenplanPausenaufsichten(listAdd, api.schema, this.manager.auswahl().id);
			this.manager.daten().pausenaufsichtAddAll(res);
			if (res.size() > 0) {
				const aufsichtErstellt = res.get(0);
				for (const aufsichtsbereich of update.listHinzuzufuegen)
					if (aufsichtsbereich.idPausenaufsicht < 0)
						aufsichtsbereich.idPausenaufsicht = aufsichtErstellt.id;
			}
		}
		const res = await api.server.updateStundenplanPausenaufsichtenBereiche(update, api.schema, this.manager.auswahl().id);
		this.manager.daten().pausenaufsichtbereichRemoveAll(update.listEntfernen);
		this.manager.daten().pausenaufsichtbereichAddAll(res);
		const listRemove = new ArrayList<number>();
		for (const aufsicht of this.manager.daten().pausenaufsichtGetMengeAsList())
			if (aufsicht.bereiche.isEmpty())
				listRemove.add(aufsicht.id);
		await api.server.deleteStundenplanPausenaufsichten(listRemove, api.schema, this.manager.auswahl().id);
		this.manager.daten().pausenaufsichtRemoveAllById(listRemove);
		api.status.stop();
	}

	addAufsicht = async (data: Partial<StundenplanPausenaufsicht>) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const pausenaufsicht = await api.server.addStundenplanPausenaufsicht(data, api.schema, this.manager.auswahl().id);
		this.manager.daten().pausenaufsichtAdd(pausenaufsicht);
		this.commit();
		api.status.stop();
	}

	removeAufsicht = async (aufsichtID: number) => {
		api.status.start();
		const pausenaufsicht = await api.server.deleteStundenplanPausenaufsicht(api.schema, aufsichtID);
		this.manager.daten().pausenaufsichtRemoveById(pausenaufsicht.id);
		this.commit();
		api.status.stop();
	}

	patchAufsicht = async (aufsicht: Partial<StundenplanPausenaufsicht>, id: number) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const _aufsicht = this.manager.daten().pausenaufsichtGetByIdOrException(id);
		await api.server.patchStundenplanPausenaufsicht(aufsicht, api.schema, this.manager.auswahl().id, id);
		this.manager.daten().pausenaufsichtPatchAttributes(Object.assign(_aufsicht, aufsicht));
		this.commit();
		api.status.stop();
	}

	addAufsichtsbereich = async (aufsichtsbereich: Partial<StundenplanAufsichtsbereich>) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		if ((aufsichtsbereich.kuerzel === undefined) || this.manager.daten().aufsichtsbereichExistsByKuerzel(aufsichtsbereich.kuerzel))
			throw new UserNotificationException('Eine Aufsichtsbereich mit diesem Kürzel existiert bereits');
		api.status.start();
		delete aufsichtsbereich.id;
		const _aufsichtsbereich = await api.server.addStundenplanAufsichtsbereich(aufsichtsbereich, api.schema, this.manager.auswahl().id)
		this.manager.daten().aufsichtsbereichAdd(_aufsichtsbereich);
		this.commit();
		api.status.stop();
	}

	patchAufsichtsbereich = async (aufsichtsbereich : Partial<StundenplanAufsichtsbereich>, id: number) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const _aufsichtsbereich = this.manager.daten().aufsichtsbereichGetByIdOrException(id);
		if (aufsichtsbereich.kuerzel === undefined)
			aufsichtsbereich.kuerzel = _aufsichtsbereich.kuerzel;
		await api.server.patchStundenplanAufsichtsbereich(aufsichtsbereich, api.schema, id);
		this.manager.daten().aufsichtsbereichPatchAttributes(Object.assign(_aufsichtsbereich, aufsichtsbereich));
		this.commit();
		api.status.stop();
	}

	removeAufsichtsbereiche = async (aufsichtsbereiche: Iterable<StundenplanAufsichtsbereich>) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const listID = new ArrayList<number>();
		for (const aufsichtsbereich of aufsichtsbereiche)
			listID.add(aufsichtsbereich.id);
		const list = await api.server.deleteStundenplanAufsichtsbereiche(listID, api.schema, this.manager.auswahl().id);
		this.manager.daten().aufsichtsbereichRemoveAll(list);
		if (this.selected instanceof StundenplanAufsichtsbereich && list.contains(this.selected))
			this._state.value.selected = undefined;
		this.commit();
		api.status.stop();
	}

	aufsichtsbereicheSyncToVorlage = async (aufsichtsbereiche: Iterable<StundenplanAufsichtsbereich>) => {
		const neu = new ArrayList<Partial<StundenplanAufsichtsbereich>>();
		outer: for (const a of aufsichtsbereiche) {
			const { id, ...partial} = a;
			for (const ka of this.listAufsichtsbereiche)
				if (ka.kuerzel === a.kuerzel) {
					await api.server.patchAufsichtsbereich({ beschreibung: partial.beschreibung }, api.schema, ka.id);
					Object.assign(ka, partial);
					break outer;
				}
			neu.add(partial);
		}
		const liste = await api.server.addAufsichtsbereiche(neu, api.schema);
		this.listAufsichtsbereiche.addAll(liste);
		this.commit();
	}

	aufsichtsbereicheSyncToStundenplan = async (aufsichtsbereiche: Iterable<StundenplanAufsichtsbereich>) => {
		const stundenplanId = this.manager.auswahlID();
		if (stundenplanId === null)
			return;
		const neu = new ArrayList<Partial<StundenplanAufsichtsbereich>>();
		for (const a of aufsichtsbereiche) {
			const { id, ...partial} = a;
			const ka = this.manager.daten().aufsichtsbereichGetByKuerzelOrNull(a.kuerzel);
			if (ka !== null) {
				await api.server.patchStundenplanAufsichtsbereich({ beschreibung: partial.beschreibung }, api.schema, ka.id);
				Object.assign(ka, partial);
				this.manager.daten().aufsichtsbereichPatchAttributes(ka);
				break;
			}
			neu.add(partial);
		}
		const liste = await api.server.addStundenplanAufsichtsbereiche(neu, api.schema, stundenplanId);
		this.manager.daten().aufsichtsbereichAddAll(liste);
		this.commit();
	}

	addZeitraster = async (zeitraster: Iterable<Partial<StundenplanZeitraster>>) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const list = new ArrayList<Partial<StundenplanZeitraster>>();
		for (const z of zeitraster) {
			delete z.id;
			list.add(z);
		}
		const res = await api.server.addStundenplanZeitrasterEintraege(list, api.schema, this.manager.auswahl().id);
		this.manager.daten().zeitrasterAddAll(res);
		this.commit();
		api.status.stop();
	}

	patchZeitraster = async (zeitraster : Iterable<StundenplanZeitraster>) => {
		api.status.start();
		const list: List<StundenplanZeitraster> = new ArrayList();
		for (const z of zeitraster)
			list.add(z);
		await api.server.patchStundenplanZeitrasterEintraege(list, api.schema);
		this.manager.daten().zeitrasterPatchAttributesAll(list);
		this.commit();
		api.status.stop();
	}

	removeZeitraster = async (multi: Iterable<StundenplanZeitraster>) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const listID = new ArrayList<number>()
		for (const zeitraster of multi)
			listID.add(zeitraster.id);
		if (!listID.isEmpty()) {
			const list = await api.server.deleteStundenplanZeitrasterEintraege(listID, api.schema, this.manager.auswahl().id);
			this.manager.daten().zeitrasterRemoveAll(list);
			if ((this.selected instanceof StundenplanZeitraster && list.contains(this.selected))
				|| (typeof this.selected === 'number')
				|| (this.selected instanceof Wochentag))
				this._state.value.selected = undefined;
			this.commit();
		}
		api.status.stop();
	}

	importZeitraster = async () => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const listKatalogeintraege: List<Partial<StundenplanZeitraster>> = await api.server.getZeitraster(api.schema);
		for (const item of listKatalogeintraege)
			delete item.id;
		const zeitraster = await api.server.addStundenplanZeitrasterEintraege(listKatalogeintraege, api.schema, this.manager.auswahl().id);
		// kein Aufruf an den Manager notwendig, da wir die Route nun neu laden
		this.manager.daten().zeitrasterAddAll(zeitraster);
		this.commit();
		api.status.stop();
	}

	addUnterrichte = async (data: Iterable<Partial<StundenplanUnterricht>>) => {
		api.status.start();
		const list = new ArrayList<Partial<StundenplanUnterricht>>();
		for (const datum of data)
			list.add(datum);
		const unterrichte = await api.server.addStundenplanUnterrichte(list, api.schema);
		this.manager.daten().unterrichtAddAll(unterrichte);
		this.commit();
		api.status.stop();
	}

	patchUnterrichte = async (data: Iterable<StundenplanUnterricht>, zeitraster?: StundenplanZeitraster, wochentyp?: number) => {
		api.status.start();
		const list: List<StundenplanUnterricht> = new ArrayList();
		if ((zeitraster !== undefined) && (wochentyp !== undefined))
			loop: for (const datum of data) {
				if (!this.manager.daten().unterrichtIstVerschiebenErlaubtNach(datum, zeitraster, wochentyp))
					continue loop;
				datum.idZeitraster = zeitraster.id;
				datum.wochentyp = wochentyp;
				list.add(datum);
			}
		else
			for (const datum of data)
				list.add(datum);
		await api.server.patchStundenplanUnterrichte(list, api.schema);
		this.manager.daten().unterrichtPatchAttributesAll(list);
		this.commit();
		api.status.stop();
	}

	removeUnterrichte = async (unterrichte: Iterable<StundenplanUnterricht>) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const listID = new ArrayList<number>();
		for (const item of unterrichte)
			listID.add(item.id);
		const list = await api.server.deleteStundenplanUnterrichte(listID, api.schema, this.manager.auswahl().id);
		this.manager.daten().unterrichtRemoveAll(list);
		if ((this.selected instanceof StundenplanUnterricht) && listID.contains(this.selected.id))
			this._state.value.selected = undefined;
		this.commit();
		api.status.stop();
	}

	mergeUnterrichte = async (list: Iterable<List<StundenplanUnterricht>>) => {
		api.status.start();
		const listRemove = new ArrayList<StundenplanUnterricht>();
		const listAdd = new ArrayList<Partial<StundenplanUnterricht>>();
		for (const unterricht of list) {
			if (unterricht.isEmpty())
				continue;
			const add = <Partial<StundenplanUnterricht>>unterricht.get(0).clone();
			delete add.id;
			add.wochentyp = 0;
			listAdd.add(add);
			listRemove.addAll(unterricht);
		}
		await this.removeUnterrichte(listRemove);
		await this.addUnterrichte(listAdd);
		api.status.stop();
	}

	addJahrgang = async (id: number) => {
		api.status.start();
		const jahrgang = new StundenplanJahrgang();
		for (const j of this.listJahrgaenge)
			if (j.id === id) {
				jahrgang.kuerzel = j.kuerzel ?? j.kuerzelStatistik ?? '';
				jahrgang.id = j.id;
			}
		this.manager.daten().jahrgangAdd(jahrgang);
		this.commit();
		api.status.stop();
	}

	removeJahrgang = async (id: number) => {
		api.status.start();
		this.manager.daten().jahrgangRemoveById(id);
		this.commit();
		api.status.stop();
	}

	gotoKatalog = async (katalog: 'raeume'|'aufsichtsbereiche'|'pausenzeiten') => {
		switch (katalog) {
			case 'aufsichtsbereiche':
				return await RouteManager.doRoute(routeKatalogAufsichtsbereiche.getRoute({id: -1}));
			case 'pausenzeiten':
				return await RouteManager.doRoute(routeKatalogPausenzeiten.getRoute({id: -1}));
			case 'raeume':
				return await RouteManager.doRoute(routeKatalogRaeume.getRoute({id: -1}));
		}
	}

	// setFilter = async () => {
	// 	this.stundenplanUnterrichtListeManager.filterInvalidateCache();
	// 	this.setPatchedState({ stundenplanUnterrichtListeManager: this.stundenplanUnterrichtListeManager });
	// }

	get ganzerStundenplanRaum(): boolean {
		return api.config.getValue("stundenplan.raeume.ganzerStundenplan") === 'true';
	}

	setGanzerStundenplanRaum = async (value: boolean) => {
		await api.config.setValue("stundenplan.raeume.ganzerStundenplan", value ? "true" : "false");
	}

	gotoEintrag = async (eintrag?: StundenplanListeEintrag) => await RouteManager.doRoute(routeStundenplan.getRoute({ id: eintrag?.id }));

	getPDF = api.call(async (reportingParameter: ReportingParameter, idRaum: number): Promise<ApiFile> => {
		const id = this._state.value.manager?.auswahlID() ?? null;
		if (id === null)
			throw new DeveloperNotificationException("Es ist kein gültiger Stundenplan ausgewählt.");
		reportingParameter.idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		reportingParameter.idsHauptdaten.add(id);
		reportingParameter.idsDetaildaten.add(idRaum);
		return await api.server.pdfReport(reportingParameter, api.schema);
	})
}
