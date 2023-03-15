import { BenutzerKompetenz, FachDaten, FaecherListeEintrag, List, Schulform, Vector } from "@svws-nrw/svws-core";
import { ShallowRef, shallowRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { AuswahlChildData } from "~/components/AuswahlChildData";
import { FaecherAppProps } from "~/components/kataloge/faecher/SFaecherAppProps";
import { FaecherAuswahlProps } from "~/components/kataloge/faecher/SFaecherAuswahlProps";
import { routeKatalogFachDaten } from "~/router/apps/faecher/RouteKatalogFachDaten";
import { routeApp, RouteApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";
import { routeKataloge } from "./RouteKataloge";

interface RouteStateKatalogFaecher {
	auswahl: FaecherListeEintrag | undefined;
	daten: FachDaten | undefined;
	mapKatalogeintraege: Map<number, FaecherListeEintrag>;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogFaecher {

	private static _defaultState: RouteStateKatalogFaecher = {
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		view: routeKatalogFachDaten,
	}
	private _state = shallowRef(RouteDataKatalogFaecher._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogFaecher>) {
		this._state.value = Object.assign({ ... RouteDataKatalogFaecher._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogFaecher>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogFaecher.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Fächer gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): FaecherListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, FaecherListeEintrag> {
		return this._state.value.mapKatalogeintraege;
	}

	get daten(): FachDaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getFaecher(api.schema);
		const mapKatalogeintraege = new Map<number, FaecherListeEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl: FaecherListeEintrag) => {
		const daten = await api.server.getFach(api.schema, auswahl.id)
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: FaecherListeEintrag) => {
		await RouteManager.doRoute(routeKatalogFaecher.getRoute(eintrag.id));
	}

	patch = async (data : Partial<FachDaten>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patchFachDaten", data);
		//await api.server.patchFachDaten(data, api.schema, this.item.id);
	}
}
export class RouteDataKatalogFaecherx {
	auswahl: ShallowRef<FaecherListeEintrag | undefined> = shallowRef(undefined);
	listFaecher: List<FaecherListeEintrag> = new Vector();
	mapFaecher: Map<number, FaecherListeEintrag> = new Map();

	public async ladeListe() {
		this.listFaecher = await api.server.getFaecher(api.schema);
		const mapKurse = new Map<number, FaecherListeEintrag>();
		for (const l of this.listFaecher)
			mapKurse.set(l.id, l);
		this.mapFaecher = mapKurse;
	}

	public async onSelect(item?: FaecherListeEintrag) {
		if (item === this.auswahl.value)
			return;
		if (item === undefined) {
			this.auswahl.value = undefined;
		} else {
			this.auswahl.value = item;
		}
	}

	setFach = async (value: FaecherListeEintrag | undefined) => {
		if (value === undefined || value === null) {
			await RouteManager.doRoute({ name: routeKatalogFaecher.name, params: { } });
			return;
		}
		const redirect_name: string = (routeKatalogFaecher.selectedChild === undefined) ? routeKatalogFachDaten.name : routeKatalogFaecher.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}}

const SFaecherAuswahl = () => import("~/components/kataloge/faecher/SFaecherAuswahl.vue")
const SFaecherApp = () => import("~/components/kataloge/faecher/SFaecherApp.vue")

export class RouteKatalogFaecher extends RouteNode<RouteDataKatalogFaecher, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "faecher", "/kataloge/faecher/:id(\\d+)?", SFaecherApp, new RouteDataKatalogFaecher());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fächer";
		super.setView("liste", SFaecherAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogFachDaten
		];
		super.defaultChild = routeKatalogFachDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.data.mapKatalogeintraege.size < 1)
			return;
		let eintrag: FaecherListeEintrag | undefined;
		if (!to_params.id && this.data.auswahl)
			return this.getRoute(this.data.auswahl.id);
		if (!to_params.id) {
			eintrag = this.data.mapKatalogeintraege.get(0);
			return this.getRoute(eintrag?.id);
		}
		else {
			const id = parseInt(to_params.id);
			eintrag = this.data.mapKatalogeintraege.get(id);
			if (eintrag === undefined) {
				return;
			}
		}
		if (eintrag !== undefined)
			await this.data.setEintrag(eintrag);
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): FaecherAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapKatalogeintraege: this.data.mapKatalogeintraege,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoEintrag: this.data.gotoEintrag,
			returnToKataloge: routeKataloge.returnToKataloge
		};
	}

	public getProps(to: RouteLocationNormalized): FaecherAppProps {
		return {
			auswahl: this.data.auswahl,
			// Props für die Navigation
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
		for (const c of super.children)
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
		await RouteManager.doRoute({ name: value.name, params: { id: this.data.auswahl?.id } });
		await this.data.setView(node);
	}
}

export const routeKatalogFaecher = new RouteKatalogFaecher();
