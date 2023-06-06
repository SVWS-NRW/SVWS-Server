import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { ZeitrasterAuswahlProps } from "~/components/kataloge/zeitraster/SZeitrasterAuswahlProps";
import type { StundenplanZeitraster } from "@core";
import type { ZeitrasterAppProps } from "~/components/kataloge/zeitraster/SZeitrasterAppProps";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { RouteApp } from "~/router/RouteApp";
import { BenutzerKompetenz, Schulform } from "@core";
import { shallowRef } from "vue";
import { routeApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";
import { routeKataloge } from "./RouteKataloge";
import { routeKatalogZeitrasterDaten } from "./zeitraster/RouteKatalogZeitrasterDaten";


interface RouteStateKatalogZeitraster {
	auswahl: StundenplanZeitraster | undefined;
	daten: StundenplanZeitraster | undefined;
	mapKatalogeintraege: Map<number, StundenplanZeitraster>;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogZeitraster {

	private static _defaultState: RouteStateKatalogZeitraster = {
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		view: routeKatalogZeitrasterDaten,
	}
	private _state = shallowRef(RouteDataKatalogZeitraster._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogZeitraster>) {
		this._state.value = Object.assign({ ... RouteDataKatalogZeitraster._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogZeitraster>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogZeitraster.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Räume gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): StundenplanZeitraster | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, StundenplanZeitraster> {
		return this._state.value.mapKatalogeintraege;
	}

	get daten(): StundenplanZeitraster {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Raumdaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getZeitraster(api.schema);
		const mapKatalogeintraege = new Map<number, StundenplanZeitraster>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl: StundenplanZeitraster) => {
		const daten = this.mapKatalogeintraege.get(auswahl.id);
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: StundenplanZeitraster) => {
		await RouteManager.doRoute(routeKatalogZeitraster.getRoute(eintrag.id));
	}

	addEintrag = async (eintrag: Partial<StundenplanZeitraster>) => {
		delete eintrag.id;
		const raum = await api.server.addZeitrasterEintrag(eintrag, api.schema)
		const mapKatalogeintraege = this.mapKatalogeintraege;
		mapKatalogeintraege.set(raum.id, raum);
		this.setPatchedState({mapKatalogeintraege});
		await this.gotoEintrag(raum);
	}

	deleteEintraege = async (eintraege: StundenplanZeitraster[]) => {
		const mapKatalogeintraege = this.mapKatalogeintraege;
		for (const eintrag of eintraege) {
			const raum = await api.server.deleteZeitrasterEintrag(api.schema, eintrag.id);
			mapKatalogeintraege.delete(raum.id);
		}
		let auswahl;
		if (this.auswahl && mapKatalogeintraege.get(this.auswahl.id) === undefined)
			auswahl = mapKatalogeintraege.values().next().value;
		this.setPatchedState({mapKatalogeintraege, auswahl});
	}

	patch = async (eintrag : Partial<StundenplanZeitraster>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchZeitrasterEintrag(eintrag, api.schema, this.auswahl.id);
		const auswahl = this.auswahl;
		this.setPatchedState({auswahl: Object.assign(auswahl, eintrag)});
	}
}

const SZeitrasterAuswahl = () => import("~/components/kataloge/zeitraster/SZeitrasterAuswahl.vue")
const SZeitrasterApp = () => import("~/components/kataloge/zeitraster/SZeitrasterApp.vue")

export class RouteKatalogZeitraster extends RouteNode<RouteDataKatalogZeitraster, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.zeitraster", "/kataloge/zeitraster/:id(\\d+)?", SZeitrasterApp, new RouteDataKatalogZeitraster());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeitraster";
		super.setView("liste", SZeitrasterAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogZeitrasterDaten
		];
		super.defaultChild = routeKatalogZeitrasterDaten;
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
		let eintrag: StundenplanZeitraster | undefined;
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

	public getAuswahlProps(to: RouteLocationNormalized): ZeitrasterAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapKatalogeintraege: () => this.data.mapKatalogeintraege,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoEintrag: this.data.gotoEintrag,
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			returnToKataloge: routeKataloge.returnToKataloge
		};
	}

	public getProps(to: RouteLocationNormalized): ZeitrasterAppProps {
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

export const routeKatalogZeitraster = new RouteKatalogZeitraster();
