import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { JahrgangsDaten, JahrgangsListeEintrag} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKataloge } from "~/router/apps/kataloge/RouteKataloge";
import { routeKatalogJahrgaengeDaten } from "~/router/apps/kataloge/jahrgaenge/RouteKatalogJahrgaengeDaten";

import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { JahrgaengeAppProps } from "~/components/kataloge/jahrgaenge/SJahrgaengeAppProps";
import type { JahrgaengeAuswahlProps } from "~/components/kataloge/jahrgaenge/SJahrgaengeAuswahlProps";

interface RouteStateKatalogJahrgaenge {
	auswahl: JahrgangsListeEintrag | undefined;
	daten: JahrgangsDaten | undefined;
	mapKatalogeintraege: Map<number, JahrgangsListeEintrag>;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogJahrgaenge {

	private static _defaultState: RouteStateKatalogJahrgaenge = {
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		view: routeKatalogJahrgaengeDaten,
	}
	private _state = shallowRef(RouteDataKatalogJahrgaenge._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogJahrgaenge>) {
		this._state.value = Object.assign({ ... RouteDataKatalogJahrgaenge._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogJahrgaenge>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogJahrgaenge.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Religionen gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): JahrgangsListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, JahrgangsListeEintrag> {
		return this._state.value.mapKatalogeintraege;
	}

	get daten(): JahrgangsDaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getJahrgaenge(api.schema);
		const mapKatalogeintraege = new Map<number, JahrgangsListeEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl: JahrgangsListeEintrag) => {
		const daten = await api.server.getJahrgang(api.schema, auswahl.id)
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: JahrgangsListeEintrag) => {
		await RouteManager.doRoute(routeKatalogJahrgaenge.getRoute(eintrag.id));
	}

	patch = async (data : Partial<JahrgangsDaten>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patchJahrgangDaten", data);
		//await api.server.patchJahrgangDaten(data, api.schema, this.item.id);
	}
}
const SJahrgaengeAuswahl = () => import("~/components/kataloge/jahrgaenge/SJahrgaengeAuswahl.vue")
const SJahrgaengeApp = () => import("~/components/kataloge/jahrgaenge/SJahrgaengeApp.vue")

export class RouteKatalogJahrgaenge extends RouteNode<RouteDataKatalogJahrgaenge, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.jahrgaenge", "/kataloge/jahrgaenge/:id(\\d+)?", SJahrgaengeApp, new RouteDataKatalogJahrgaenge());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgänge";
		super.setView("liste", SJahrgaengeAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogJahrgaengeDaten
		];
		super.defaultChild = routeKatalogJahrgaengeDaten;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.data.mapKatalogeintraege.size < 1)
			return;
		let eintrag: JahrgangsListeEintrag | undefined;
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

	public getAuswahlProps(to: RouteLocationNormalized): JahrgaengeAuswahlProps {
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

	public getProps(to: RouteLocationNormalized): JahrgaengeAppProps {
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

export const routeKatalogJahrgaenge = new RouteKatalogJahrgaenge();
