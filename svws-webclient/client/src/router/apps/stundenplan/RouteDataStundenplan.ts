import type { StundenplanListeEintrag, StundenplanRaum, StundenplanPausenaufsicht, StundenplanAufsichtsbereich, StundenplanPausenzeit, List, Raum, Stundenplan, JahrgangsListeEintrag, StundenplanUnterricht, LehrerListeEintrag, StundenplanZeitraster} from "@core";
import type { RouteNode } from "~/router/RouteNode";

import { StundenplanManager, DeveloperNotificationException, ArrayList, StundenplanJahrgang } from "@core";

import { shallowRef } from "vue";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";

import { routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import { routeStundenplanDaten } from "./RouteStundenplanDaten";

interface RouteStateStundenplan {
	auswahl: StundenplanListeEintrag | undefined;
	mapKatalogeintraege: Map<number, StundenplanListeEintrag>;
	daten: Stundenplan | undefined;
	stundenplanManager: StundenplanManager | undefined;
	listRaeume: List<Raum>;
	listPausenzeiten: List<StundenplanPausenzeit>;
	listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
	listJahrgaenge: List<JahrgangsListeEintrag>;
	listLehrer: List<LehrerListeEintrag>;
	view: RouteNode<any, any>;
}
export class RouteDataStundenplan {

	private static _defaultState: RouteStateStundenplan = {
		auswahl: undefined,
		mapKatalogeintraege: new Map(),
		daten: undefined,
		stundenplanManager: undefined,
		listRaeume: new ArrayList(),
		listPausenzeiten: new ArrayList(),
		listAufsichtsbereiche: new ArrayList(),
		listJahrgaenge: new ArrayList(),
		listLehrer: new ArrayList(),
		view: routeStundenplanDaten,
	}
	private _state = shallowRef(RouteDataStundenplan._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateStundenplan>) {
		this._state.value = Object.assign({ ... RouteDataStundenplan._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateStundenplan>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeStundenplan.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Stundenpläne gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): StundenplanListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapKatalogeintraege;
	}

	get daten(): Stundenplan {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Stundenplandaten nicht initialisiert");
		return this._state.value.daten;
	}

	get stundenplanManager(): StundenplanManager {
		if (this._state.value.stundenplanManager === undefined)
			throw new Error("Unerwarteter Fehler: Stundenplandaten nicht initialisiert");
		return this._state.value.stundenplanManager;
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

	get listJahrgaenge(): List<JahrgangsListeEintrag> {
		return this._state.value.listJahrgaenge;
	}

	get listLehrer(): List<LehrerListeEintrag> {
		return this._state.value.listLehrer;
	}

	patch = async (data : Partial<Stundenplan>) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		await api.server.patchStundenplan(data, api.schema, this.auswahl.id);
		const daten = this.daten;
		if (this.auswahl) {
			if (data.bezeichnungStundenplan)
				this.auswahl.bezeichnung = data.bezeichnungStundenplan;
			if (data.gueltigAb)
				this.auswahl.gueltigAb = data.gueltigAb;
			if (data.gueltigBis)
				this.auswahl.gueltigBis = data.gueltigBis;
			this.mapKatalogeintraege.set(this.auswahl.id, this.auswahl);
		}
		this.setPatchedState({daten: Object.assign(daten, data), auswahl: this.auswahl, mapKatalogeintraege: this.mapKatalogeintraege});
		api.status.stop();
	}

	addRaum = async (raum: Partial<StundenplanRaum>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		delete raum.id;
		api.status.start();
		const _raum = await api.server.addStundenplanRaum(raum, api.schema, id)
		this.stundenplanManager.raumAdd(_raum);
		this.commit();
		api.status.stop();
	}

	patchRaum = async (data : Partial<StundenplanRaum>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		delete data.id;
		api.status.start();
		await api.server.patchStundenplanRaum(data, api.schema, id);
		const raum = this.stundenplanManager.raumGetByIdOrException(id);
		this.stundenplanManager.raumPatchAttributes(Object.assign(raum, data));
		this.commit();
		api.status.stop();
	}

	removeRaeume = async (raeume: Iterable<StundenplanRaum>) => {
		api.status.start();
		const list = new ArrayList<StundenplanRaum>()
		for (const raum of raeume) {
			await api.server.deleteStundenplanRaum(api.schema, raum.id);
			list.add(raum);
		}
		this.stundenplanManager.raumRemoveAll(list);
		this.commit();
		api.status.stop();
	}

	importRaeume = async (raeume: StundenplanRaum[]) => {}

	addPausenzeit = async (pausenzeit: Partial<StundenplanPausenzeit>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		delete pausenzeit.id;
		api.status.start();
		const _pausenzeit = await api.server.addStundenplanPausenzeit(pausenzeit, api.schema, id)
		this.stundenplanManager.pausenzeitAdd(_pausenzeit);
		this.commit();
		api.status.stop();
	}

	patchPausenzeit = async (data : Partial<StundenplanPausenzeit>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		delete data.id;
		api.status.start();
		await api.server.patchStundenplanPausenzeit(data, api.schema, id);
		const pausenzeit = this.stundenplanManager.pausenzeitGetByIdOrException(id);
		this.stundenplanManager.pausenzeitPatchAttributes(Object.assign(pausenzeit, data));
		this.commit();
		api.status.stop();
	}

	removePausenzeiten = async (pausenzeiten: Iterable<StundenplanPausenzeit>) => {
		api.status.start();
		const list = new ArrayList<StundenplanPausenzeit>()
		for (const pausenzeit of pausenzeiten) {
			await api.server.deleteStundenplanPausenzeit(api.schema, pausenzeit.id);
			list.add(pausenzeit);
		}
		this.stundenplanManager.pausenzeitRemoveAll(list);
		this.commit();
		api.status.stop();
	}

	importPausenzeiten = async (pausenzeiten: StundenplanPausenzeit[]) => {}

	addAufsichtUndBereich = async (data: Partial<StundenplanPausenaufsicht>) => {
		api.status.start();
		const pausenaufsicht = await api.server.addStundenplanPausenaufsicht(data, api.schema);
		this.stundenplanManager.pausenaufsichtAdd(pausenaufsicht);
		this.commit();
		api.status.stop();
	}

	addAufsichtsbereich = async (aufsichtsbereich: Partial<StundenplanAufsichtsbereich>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		delete aufsichtsbereich.id;
		api.status.start();
		const _aufsichtsbereich = await api.server.addStundenplanAufsichtsbereich(aufsichtsbereich, api.schema, id)
		this.stundenplanManager.aufsichtsbereichAdd(_aufsichtsbereich);
		this.commit();
		api.status.stop();
	}

	patchAufsichtsbereich = async (data : Partial<StundenplanAufsichtsbereich>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		await api.server.patchStundenplanAufsichtsbereich(data, api.schema, id);
		const aufsichtsbereich = this.stundenplanManager.aufsichtsbereichGetByIdOrException(id);
		this.stundenplanManager.aufsichtsbereichPatchAttributes(Object.assign(aufsichtsbereich, data));
		this.commit();
		api.status.stop();
	}

	removeAufsichtsbereiche = async (aufsichtsbereiche: Iterable<StundenplanAufsichtsbereich>) => {
		api.status.start();
		const list = new ArrayList<StundenplanAufsichtsbereich>()
		for (const aufsichtsbereich of aufsichtsbereiche) {
			await api.server.deleteStundenplanAufsichtsbereich(api.schema, aufsichtsbereich.id);
			list.add(aufsichtsbereich);
		}
		this.stundenplanManager.aufsichtsbereichRemoveAll(list);
		this.commit();
		api.status.stop();
	}

	importAufsichtsbereiche = async (s: StundenplanAufsichtsbereich[]) => {}

	addZeitraster = async (zeitraster: Iterable<Partial<StundenplanZeitraster>>) => {
		api.status.start();
		const list = new ArrayList<StundenplanZeitraster>();
		for (const z of zeitraster) {
			delete z.id;
			const item = await api.server.addZeitrasterEintrag(z, api.schema);
			list.add(item);
		}
		this.stundenplanManager.zeitrasterAddAll(list);
		this.commit();
		api.status.stop();
	}

	patchZeitraster = async (zeitraster : Iterable<StundenplanZeitraster>) => {
		api.status.start();
		const list = new ArrayList<StundenplanZeitraster>();
		for (const z of zeitraster) {
			await api.server.patchStundenplanZeitrasterEintrag(z, api.schema, z.id);
			this.stundenplanManager.zeitrasterPatchAttributes(z);
			list.add(z);
		}
		//TODO ergänzen
		// this.stundenplanManager.zeitrasterPatchAttributesAll(list);
		this.commit();
		api.status.stop();
	}

	removeZeitraster = async (multi: Iterable<StundenplanZeitraster>) => {
		api.status.start();
		const list = new ArrayList<StundenplanZeitraster>()
		for (const zeitraster of multi) {
			await api.server.deleteStundenplanZeitrasterEintrag(api.schema, zeitraster.id);
			list.add(zeitraster);
		}
		this.stundenplanManager.zeitrasterRemoveAll(list);
		this.commit();
		api.status.stop();
	}

	importZeitraster = async () => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const listKatalogeintraege: List<Partial<StundenplanZeitraster>> = await api.server.getZeitraster(api.schema);
		for (const item of listKatalogeintraege) {
			delete item.id;
			await api.server.addStundenplanZeitrasterEintrag(item, api.schema, id);
		}
		api.status.stop();
		await this.setEintrag(this.auswahl);
	}

	addUnterrichtKlasse = async (data: Iterable<Partial<StundenplanUnterricht>>) => {
		api.status.start();
		const list = new ArrayList<StundenplanUnterricht>();
		for (const datum of data) {
			const unterricht = await api.server.addStundenplanUnterricht(datum, api.schema);
			list.add(unterricht);
		}
		this.stundenplanManager.unterrichtAddAll(list);
		this.commit();
		api.status.stop();
	}

	patchUnterricht = async (data: Iterable<StundenplanUnterricht>, zeitraster: StundenplanZeitraster) => {
		api.status.start();
		const list = new ArrayList<StundenplanUnterricht>();
		for (const datum of data) {
			if (datum.idZeitraster !== zeitraster.id) {
				await api.server.patchStundenplanUnterricht({ idZeitraster: zeitraster.id }, api.schema, datum.id);
				datum.idZeitraster = zeitraster.id;
				list.add(datum);
			}
		}
		this.stundenplanManager.unterrichtPatchAttributesAll(list);
		this.commit();
		api.status.stop();
	}

	removeUnterrichtKlasse = async (unterrichte: Iterable<StundenplanUnterricht>) => {
		api.status.start();
		const list = new ArrayList<StundenplanUnterricht>()
		for (const unterricht of unterrichte) {
			await api.server.deleteStundenplanUnterricht(api.schema, unterricht.id);
			list.add(unterricht);
		}
		this.stundenplanManager.unterrichtRemoveAll(list);
		this.commit();
		api.status.stop();
	}

	addJahrgang = async (id: number) => {
		api.status.start();
		const jahrgang = new StundenplanJahrgang();
		jahrgang.id = id;
		this.stundenplanManager.jahrgangAdd(jahrgang);
		this.commit();
		api.status.stop();
	}

	removeJahrgang = async (id: number) => {
		api.status.start();
		this.stundenplanManager.jahrgangRemoveById(id);
		this.commit();
		api.status.stop();
	}

	public async ladeListe() {
		api.status.start();
		const listKatalogeintraege = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, api.abschnitt.id)
		const mapKatalogeintraege = new Map<number, StundenplanListeEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		const listRaeume = await api.server.getRaeume(api.schema);
		const listPausenzeiten = await api.server.getPausenzeiten(api.schema);
		const listAufsichtsbereiche = await api.server.getAufsichtsbereiche(api.schema);
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const listLehrer = await api.server.getLehrer(api.schema);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege, listRaeume, listPausenzeiten, listAufsichtsbereiche, listJahrgaenge, listLehrer })
		api.status.stop();
	}

	setEintrag = async (auswahl?: StundenplanListeEintrag) => {
		api.status.start();
		if (auswahl === undefined && this.mapKatalogeintraege.size > 0)
			auswahl = this.mapKatalogeintraege.values().next().value;
		if (auswahl === undefined)
			this.setPatchedState({ auswahl, daten: undefined, stundenplanManager: undefined });
		else {
			const daten = await api.server.getStundenplan(api.schema, auswahl.id);
			const unterrichtsdaten = await api.server.getStundenplanUnterrichte(api.schema, auswahl.id);
			const pausenaufsichten = await api.server.getStundenplanPausenaufsichten(api.schema, auswahl.id);
			const unterrichtsverteilung = await api.server.getStundenplanUnterrichtsverteilung(api.schema, auswahl.id);
			const stundenplanManager = new StundenplanManager(daten, unterrichtsdaten, pausenaufsichten, unterrichtsverteilung);
			this.setPatchedState({ auswahl, daten, stundenplanManager });
		}
		api.status.stop();
	}

	addEintrag = async () => {
		api.status.start();
		const eintrag = await api.server.addStundenplan(api.schema, api.abschnitt.id);
		this.mapKatalogeintraege.set(eintrag.id, eintrag)
		await this.setEintrag(eintrag);
		api.status.stop();
	}

	removeEintraege = async (eintraege: StundenplanListeEintrag[]) => {
		api.status.start();
		for (const eintrag of eintraege) {
			if (eintrag.id === this.auswahl?.id)
				this._state.value.auswahl = undefined;
			await api.server.deleteStundenplan(api.schema, eintrag.id);
			this.mapKatalogeintraege.delete(eintrag.id);
		}
		this.commit();
		api.status.stop();
	}

	gotoEintrag = async (eintrag: StundenplanListeEintrag) => await RouteManager.doRoute(routeStundenplan.getRoute(eintrag.id));

}
