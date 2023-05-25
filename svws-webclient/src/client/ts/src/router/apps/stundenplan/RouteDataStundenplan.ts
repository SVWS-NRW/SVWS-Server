import type { StundenplanListeEintrag, Stundenplan, StundenplanRaum, StundenplanPausenaufsicht, StundenplanAufsichtsbereich, StundenplanPausenzeit} from "@svws-nrw/svws-core";
import type { RouteNode } from "~/router/RouteNode";
import { StundenplanManager, DeveloperNotificationException, ArrayList } from "@svws-nrw/svws-core";
import { useDebounceFn } from "@vueuse/core";
import { shallowRef } from "vue";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeStundenplan } from "../RouteStundenplan";
import { routeStundenplanDaten } from "./RouteStundenplanDaten";

interface RouteStateStundenplan {
	auswahl: StundenplanListeEintrag | undefined;
	mapKatalogeintraege: Map<number, StundenplanListeEintrag>;
	daten: Stundenplan | undefined;
	stundenplanManager: StundenplanManager | undefined;
	view: RouteNode<any, any>;
}
export class RouteDataStundenplan {

	private static _defaultState: RouteStateStundenplan = {
		auswahl: undefined,
		mapKatalogeintraege: new Map(),
		daten: undefined,
		stundenplanManager: undefined,
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

	patch = useDebounceFn((data: Partial<Stundenplan>)=> this.patchit(data), 100)

	patchit = (data : Partial<Stundenplan>) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.server.patchStundenplan(data, api.schema, this.auswahl.id).then(()=>{
			const daten = this.daten;
			this.setPatchedState({daten: Object.assign(daten, data)});
		}).catch((e) => console.log(e))
	}

	patchRaum = async (data : Partial<StundenplanRaum>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		delete data.id;
		await api.server.patchStundenplanRaum(data, api.schema, id);
		const raum = this.stundenplanManager.getRaum(id);
		this.stundenplanManager.patchRaum(Object.assign(raum, data));
		this.commit();
	}

	patchPausenzeit = async (data : Partial<StundenplanPausenaufsicht>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		delete data.id;
		await api.server.patchStundenplanPausenzeit(data, api.schema, id);
		const pausenzeit = this.stundenplanManager.getPausenzeit(id);
		this.stundenplanManager.patchPausenzeit(Object.assign(pausenzeit, data));
		this.commit();
	}

	patchAufsichtsbereich = async (data : Partial<StundenplanAufsichtsbereich>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		await api.server.patchStundenplanAufsichtsbereich(data, api.schema, id);
		const aufsichtsbereich = this.stundenplanManager.getAufsichtsbereich(id);
		this.stundenplanManager.patchAufsichtsbereich(Object.assign(aufsichtsbereich, data));
		this.commit();
	}

	addRaum = async (raum: Partial<StundenplanRaum>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		delete raum.id;
		const _raum = await api.server.addStundenplanRaum(raum, api.schema, id)
		this.stundenplanManager.addRaum(_raum);
		this.commit();
	}

	addPausenzeit = async (pausenzeit: Partial<StundenplanPausenzeit>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		delete pausenzeit.id;
		const _pausenzeit = await api.server.addStundenplanPausenzeit(pausenzeit, api.schema, id)
		this.stundenplanManager.addPausenzeit(_pausenzeit);
		this.commit();
	}

	addAufsichtsbereich = async (aufsichtsbereich: Partial<StundenplanAufsichtsbereich>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		delete aufsichtsbereich.id;
		const _aufsichtsbereich = await api.server.addStundenplanAufsichtsbereich(aufsichtsbereich, api.schema, id)
		this.stundenplanManager.addAufsichtsbereich(_aufsichtsbereich);
		this.commit();
	}

	removeRaeume = async (raeume: StundenplanRaum[]) => {
		for (const raum of raeume) {
			await api.server.deleteStundenplanRaum(api.schema, raum.id);
			this.stundenplanManager.removeRaum(raum.id);
		}
		this.commit();
	}

	removePausenzeiten = async (pausenzeiten: StundenplanPausenzeit[]) => {
		for (const pausenzeit of pausenzeiten) {
			await api.server.deleteStundenplanPausenzeit(api.schema, pausenzeit.id);
			this.stundenplanManager.removePausenzeit(pausenzeit.id);
		}
		this.commit();
	}

	removeAufsichtsbereiche = async (aufsichtsbereiche: StundenplanAufsichtsbereich[]) => {
		for (const aufsichtsbereich of aufsichtsbereiche) {
			await api.server.deleteStundenplanAufsichtsbereich(api.schema, aufsichtsbereich.id);
			this.stundenplanManager.removeAufsichtsbereich(aufsichtsbereich.id);
		}
		this.commit();
	}

	importRaeume = async (raeume: StundenplanRaum[]) => {}
	importPausenzeiten = async (pausenzeiten: StundenplanPausenzeit[]) => {}
	importAufsichtsbereiche = async (s: StundenplanAufsichtsbereich[]) => {}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, api.abschnitt.id)
		const mapKatalogeintraege = new Map<number, StundenplanListeEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl?: StundenplanListeEintrag) => {
		if (auswahl === undefined && this.mapKatalogeintraege.size > 0)
			auswahl = this.mapKatalogeintraege.values().next().value;
		if (auswahl === undefined)
			this.setPatchedState({ auswahl, daten: undefined, stundenplanManager: undefined });
		else {
			const daten = await api.server.getStundenplan(api.schema, auswahl.id);
			const stundenplanManager = new StundenplanManager(daten, new ArrayList(), new ArrayList(), null);
			this.setPatchedState({ auswahl, daten, stundenplanManager });
		}
	}

	gotoEintrag = async (eintrag: StundenplanListeEintrag) => await RouteManager.doRoute(routeStundenplan.getRoute(eintrag.id));

}
