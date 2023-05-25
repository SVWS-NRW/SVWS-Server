import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { StundenplanAuswahlProps } from "~/components/stundenplan/SStundenplanAuswahlProps";
import type { RouteApp } from "~/router/RouteApp";
import type { StundenplanAppProps } from "~/components/stundenplan/SStundenplanAppProps";
import { DeveloperNotificationException, Stundenplan, StundenplanAufsichtsbereich, StundenplanListeEintrag, StundenplanPausenaufsicht, StundenplanPausenzeit, StundenplanRaum } from "@svws-nrw/svws-core";
import { ArrayList, StundenplanManager } from "@svws-nrw/svws-core";
import { shallowRef } from "vue";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { routeApp } from "~/router/RouteApp";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";
import { RouteNode } from "../RouteNode";
import { routeStundenplanDaten } from "./stundenplan/RouteStundenplanDaten";
import { routeStundenplanUnterricht } from "./stundenplan/RouteStundenplanUnterricht";
import { routeStundenplanPausenaufsicht } from "./stundenplan/RouteStundenplanPausenaufsicht";
import { useDebounceFn } from "@vueuse/core";

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
		// TODO Bei Anpassungen von nachname, vorname -> routeSchueler: Schülerliste aktualisieren...
	}

	patchRaum = async (data : Partial<StundenplanRaum>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		await api.server.patchStundenplanRaum(data, api.schema, id);
		const raum = this.stundenplanManager.getRaum(id);
		this.stundenplanManager.patchRaum(Object.assign(raum, data));
		this.commit();
	}

	patchPausenzeit = async (data : Partial<StundenplanPausenaufsicht>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		await api.server.patchStundenplanPausenzeit(data, api.schema, id);
		const pausenzeit = this.stundenplanManager.getPausenzeit(id);
		this.stundenplanManager.patchPausenzeit(Object.assign(pausenzeit, data));
		this.commit();
	}

	patchAufsichtsbereich = async (data : Partial<StundenplanAufsichtsbereich>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		//await api.server.patchStundenplanAufsichtsbereich(data, api.schema, id);
		const aufsichtsbereich = this.stundenplanManager.getAufsichtsbereich(id);
		this.stundenplanManager.patchAufsichtsbereich(Object.assign(aufsichtsbereich, data));
		this.commit();
	}

	addRaum = async () => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		const raum = await api.server.addStundenplanRaum(new StundenplanRaum(), api.schema, id)
		this.stundenplanManager.addRaum(raum);
		this.commit();
	}

	addPausenzeit = async () => {
		//const pausenzeit = await api.server.
		// this.stundenplanManager.addPausenzeit(pausenzeit);
		this.commit();
	}

	addAufsichtsbereich = async () => {
		//const aufsichtsbereich = await api.server.
		// this.stundenplanManager.addAufsichtsbereich(aufsichtsbereich);
		this.commit();
	}

	removeRaeume = async (raeume: StundenplanRaum[]) => {
		for (const raum of raeume) {
			this.stundenplanManager.removeRaum(raum.id);
		}
		this.commit();
	}

	removePausenzeiten = async (pausenzeiten: StundenplanPausenzeit[]) => {
		for (const pausenzeit of pausenzeiten) {

			this.stundenplanManager.removePausenzeit(pausenzeit.id);
		}
		this.commit();
	}

	removeAufsichtsbereiche = async (aufsichtsbereiche: StundenplanAufsichtsbereich[]) => {
		for (const aufsichtsbereich of aufsichtsbereiche) {

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

const SStundenplanAuswahl = () => import("~/components/stundenplan/SStundenplanAuswahl.vue")
const SStundenplanApp = () => import("~/components/stundenplan/SStundenplanApp.vue")

export class RouteStundenplan extends RouteNode<RouteDataStundenplan, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan", "/stundenplan/:id(\\d+)?", SStundenplanApp, new RouteDataStundenplan());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.setView("liste", SStundenplanAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeStundenplanDaten,
			routeStundenplanUnterricht,
			routeStundenplanPausenaufsicht,
		];
		super.defaultChild = routeStundenplanDaten;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.id === undefined) {
			await this.data.ladeListe();
		} else {
			const id = parseInt(to_params.id);
			const eintrag = this.data.mapKatalogeintraege.get(id);
			if (eintrag === undefined && this.data.auswahl !== undefined) {
				await this.data.ladeListe();
				return this.getRoute(this.data.auswahl.id);
			}
			else if (eintrag)
				await this.data.setEintrag(eintrag);
		}
		if (to.name === this.name && this.data.auswahl !== undefined)
			return this.getRoute(this.data.auswahl.id);
	}

	public getRoute(id?: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id }};
	}

	public getChildRoute(id: number | undefined) : RouteLocationRaw {
		const redirect_name = (routeStundenplan.selectedChild === undefined) ? routeStundenplanDaten.name : routeStundenplan.selectedChild.name;
		return { name: redirect_name, params: { id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): StundenplanAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapKatalogeintraege: this.data.mapKatalogeintraege,
			gotoEintrag: this.data.gotoEintrag,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
		};
	}

	public getProps(to: RouteLocationNormalized): StundenplanAppProps {
		return {
			data: () => this.data.daten,
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsHidden: this.children_hidden().value,
		};
	}

	private getTab(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getTabs(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of this.children)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setTab = async (value: AuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { } });
		await this.data.setView(node);
	}
}

export const routeStundenplan = new RouteStundenplan();
