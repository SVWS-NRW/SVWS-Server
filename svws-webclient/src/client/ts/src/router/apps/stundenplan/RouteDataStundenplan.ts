import type { StundenplanListeEintrag, StundenplanRaum, StundenplanPausenaufsicht, StundenplanAufsichtsbereich, StundenplanPausenzeit, List, Raum, Stundenplan, JahrgangsListeEintrag} from "@core";
import { Wochentag } from "@core";
import { StundenplanZeitraster } from "@core";
import type { RouteNode } from "~/router/RouteNode";

import { StundenplanManager, DeveloperNotificationException, ArrayList, StundenplanJahrgang } from "@core";

import { shallowRef } from "vue";
import { useDebounceFn } from "@vueuse/core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";

import { routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import { routeStundenplanDaten } from "./RouteStundenplanDaten";
import { arrayBuffer } from "stream/consumers";

interface RouteStateStundenplan {
	auswahl: StundenplanListeEintrag | undefined;
	mapKatalogeintraege: Map<number, StundenplanListeEintrag>;
	daten: Stundenplan | undefined;
	stundenplanManager: StundenplanManager | undefined;
	listRaeume: List<Raum>;
	listPausenzeiten: List<StundenplanPausenzeit>;
	listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
	listJahrgaenge: List<JahrgangsListeEintrag>;
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

	patch = useDebounceFn((data: Partial<Stundenplan>)=> this.patchit(data), 100)

	patchit = (data : Partial<Stundenplan>) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.server.patchStundenplan(data, api.schema, this.auswahl.id).then(()=>{
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
		}).catch((e) => console.log(e))
	}

	patchRaum = async (data : Partial<StundenplanRaum>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		delete data.id;
		await api.server.patchStundenplanRaum(data, api.schema, id);
		const raum = this.stundenplanManager.raumGetByIdOrException(id);
		this.stundenplanManager.raumPatchAttributes(Object.assign(raum, data));
		this.commit();
	}

	patchPausenzeit = async (data : Partial<StundenplanPausenaufsicht>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		delete data.id;
		await api.server.patchStundenplanPausenzeit(data, api.schema, id);
		const pausenzeit = this.stundenplanManager.pausenzeitGetByIdOrException(id);
		this.stundenplanManager.pausenzeitPatchAttributes(Object.assign(pausenzeit, data));
		this.commit();
	}

	patchAufsichtsbereich = async (data : Partial<StundenplanAufsichtsbereich>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		await api.server.patchStundenplanAufsichtsbereich(data, api.schema, id);
		const aufsichtsbereich = this.stundenplanManager.aufsichtsbereichGetByIdOrException(id);
		this.stundenplanManager.aufsichtsbereichPatchAttributes(Object.assign(aufsichtsbereich, data));
		this.commit();
	}

	patchZeitraster = async (data : Partial<StundenplanZeitraster>, zeitraster: StundenplanZeitraster) => {
		Object.assign(zeitraster, data)
		await api.server.patchStundenplanZeitrasterEintrag(zeitraster, api.schema, zeitraster.id);
		this.stundenplanManager.zeitrasterPatchAttributes(zeitraster);
		this.commit();
	}

	addRaum = async (raum: Partial<StundenplanRaum>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		delete raum.id;
		const _raum = await api.server.addStundenplanRaum(raum, api.schema, id)
		this.stundenplanManager.raumAdd(_raum);
		this.commit();
	}

	addPausenzeit = async (pausenzeit: Partial<StundenplanPausenzeit>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		delete pausenzeit.id;
		const _pausenzeit = await api.server.addStundenplanPausenzeit(pausenzeit, api.schema, id)
		this.stundenplanManager.pausenzeitAdd(_pausenzeit);
		this.commit();
	}

	addAufsichtsbereich = async (aufsichtsbereich: Partial<StundenplanAufsichtsbereich>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		delete aufsichtsbereich.id;
		const _aufsichtsbereich = await api.server.addStundenplanAufsichtsbereich(aufsichtsbereich, api.schema, id)
		this.stundenplanManager.aufsichtsbereichAdd(_aufsichtsbereich);
		this.commit();
	}

	addZeitraster = async (wochentag: Wochentag | undefined, stunde : number | undefined) => {
		const idStundenplan = this._state.value.auswahl?.id;
		if (idStundenplan === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		if ((wochentag === undefined) && (stunde === undefined)) {
			return;
		} else if ((wochentag === undefined) && (stunde !== undefined)) {
			// Füge Zeile im Stundenplan hinzu
			console.log("Stunde hinten anfügen: ", stunde);
			for (const wochentag of this.stundenplanManager.zeitrasterGetWochentageAlsEnumRange()) {
				const z = this.stundenplanManager.zeitrasterGetByWochentagAndStundeOrNull(wochentag.id, stunde);
				if (z !== null) {
					const zeitraster: Partial<StundenplanZeitraster> = new StundenplanZeitraster();
					delete zeitraster.id;
					zeitraster.wochentag = wochentag.id;
					zeitraster.unterrichtstunde = stunde + 1;
					zeitraster.stundenbeginn = (z.stundenende ?? 0) + 5;
					zeitraster.stundenende = zeitraster.stundenbeginn + ((z.stundenende ?? 0) - (z.stundenbeginn ?? 0));
					const _item = await api.server.addStundenplanZeitrasterEintrag(zeitraster, api.schema, idStundenplan)
					this.stundenplanManager.zeitrasterAdd(_item);
				}
			}
		} else if ((wochentag !== undefined) && (stunde === undefined)) {
			// Füge Spalte im Stundenplan hinzu
			console.log("Wochentag hinten hinzufügen: ", wochentag.kuerzel);
			const ersteStunde = this.stundenplanManager.zeitrasterGetStundeMin();
			const letzterTag = this.stundenplanManager.zeitrasterGetWochentagMax();
			const zeitraster: Partial<StundenplanZeitraster> = new StundenplanZeitraster();
			delete zeitraster.id;
			zeitraster.wochentag = Wochentag.fromIDorException(letzterTag + 1).id;
			zeitraster.unterrichtstunde = ersteStunde;
			zeitraster.stundenbeginn = this.stundenplanManager.zeitrasterGetMinutenMinDerStunde(ersteStunde);
			zeitraster.stundenende = this.stundenplanManager.zeitrasterGetMinutenMaxDerStunde(ersteStunde);
			const _item = await api.server.addStundenplanZeitrasterEintrag(zeitraster, api.schema, idStundenplan)
			this.stundenplanManager.zeitrasterAdd(_item);
		} else if ((wochentag !== undefined) && (stunde !== undefined)) {
			// Füge Zelle im Stundenplan hinzu
			console.log("Zeitrastereintrag ergänzen", wochentag.kuerzel, stunde);
		}
		this.commit();
	}

	removeRaeume = async (raeume: StundenplanRaum[]) => {
		for (const raum of raeume) {
			await api.server.deleteStundenplanRaum(api.schema, raum.id);
			this.stundenplanManager.raumRemoveById(raum.id);
		}
		this.commit();
	}

	removePausenzeiten = async (pausenzeiten: StundenplanPausenzeit[]) => {
		for (const pausenzeit of pausenzeiten) {
			await api.server.deleteStundenplanPausenzeit(api.schema, pausenzeit.id);
			this.stundenplanManager.pausenzeitRemoveById(pausenzeit.id);
		}
		this.commit();
	}

	removeAufsichtsbereiche = async (aufsichtsbereiche: StundenplanAufsichtsbereich[]) => {
		for (const aufsichtsbereich of aufsichtsbereiche) {
			await api.server.deleteStundenplanAufsichtsbereich(api.schema, aufsichtsbereich.id);
			this.stundenplanManager.aufsichtsbereichRemoveById(aufsichtsbereich.id);
		}
		this.commit();
	}

	removeZeitraster = async (multi: StundenplanZeitraster[]) => {
		for (const zeitraster of multi) {
			await api.server.deleteStundenplanZeitrasterEintrag(api.schema, zeitraster.id);
			this.stundenplanManager.zeitrasterRemoveById(zeitraster.id);
		}
		this.commit();
	}

	importRaeume = async (raeume: StundenplanRaum[]) => {}
	importPausenzeiten = async (pausenzeiten: StundenplanPausenzeit[]) => {}
	importAufsichtsbereiche = async (s: StundenplanAufsichtsbereich[]) => {}
	importZeitraster = async () => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		const listKatalogeintraege: List<Partial<StundenplanZeitraster>> = await api.server.getZeitraster(api.schema);
		for (const item of listKatalogeintraege) {
			delete item.id;
			await api.server.addStundenplanZeitrasterEintrag(item, api.schema, id);
		}
		await this.setEintrag(this.auswahl);
	}

	addJahrgang = async (id: number) => {
		const jahrgang = new StundenplanJahrgang();
		jahrgang.id = id;
		this.stundenplanManager.jahrgangAdd(jahrgang);
		this.commit();
	}

	removeJahrgang = async (id: number) => {
		this.stundenplanManager.jahrgangRemoveById(id);
		this.commit();
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, api.abschnitt.id)
		const mapKatalogeintraege = new Map<number, StundenplanListeEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		const listRaeume = await api.server.getRaeume(api.schema);
		const listPausenzeiten = await api.server.getPausenzeiten(api.schema);
		const listAufsichtsbereiche = await api.server.getAufsichtsbereiche(api.schema);
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege, listRaeume, listPausenzeiten, listAufsichtsbereiche, listJahrgaenge })
	}

	setEintrag = async (auswahl?: StundenplanListeEintrag) => {
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
	}

	addEintrag = async () => {
		const eintrag = await api.server.addStundenplan(api.schema, api.abschnitt.id);
		this.mapKatalogeintraege.set(eintrag.id, eintrag)
		await this.setEintrag(eintrag);
	}

	removeEintraege = async (eintraege: StundenplanListeEintrag[]) => {
		for (const eintrag of eintraege) {
			if (eintrag.id === this.auswahl?.id)
				this._state.value.auswahl = undefined;
			await api.server.deleteStundenplan(api.schema, eintrag.id);
			this.mapKatalogeintraege.delete(eintrag.id);
		}
		this.commit();
	}

	gotoEintrag = async (eintrag: StundenplanListeEintrag) => await RouteManager.doRoute(routeStundenplan.getRoute(eintrag.id));

}
