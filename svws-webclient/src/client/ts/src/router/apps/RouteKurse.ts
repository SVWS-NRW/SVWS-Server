import { BenutzerKompetenz, JahrgangsListeEintrag, KursDaten, KursListeEintrag, LehrerListeEintrag, List, Schueler, Schulform } from "@svws-nrw/svws-core";
import { shallowRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { AuswahlChildData } from "~/components/AuswahlChildData";
import { KurseAppProps } from "~/components/kurse/SKurseAppProps";
import { KurseAuswahlProps } from "~/components/kurse/SKurseAuswahlProps";
import { routeKursDaten } from "~/router/apps/kurse/RouteKursDaten";
import { RouteNode } from "~/router/RouteNode";
import { api } from "../Api";
import { routeApp, RouteApp } from "../RouteApp";
import { RouteManager } from "../RouteManager";
import { routeSchueler } from "./RouteSchueler";

interface RouteStateKurse {
	auswahl: KursListeEintrag | undefined;
	daten: KursDaten | undefined;
	mapKatalogeintraege: Map<number, KursListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	view: RouteNode<any, any>;
}

export class RouteDataKurse {

	private static _defaultState: RouteStateKurse = {
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		mapLehrer: new Map(),
		mapJahrgaenge: new Map(),
		view: routeKursDaten,
	}
	private _state = shallowRef(RouteDataKurse._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKurse>) {
		this._state.value = Object.assign({ ... RouteDataKurse._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKurse>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKurse.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Religionen gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): KursListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, KursListeEintrag> {
		return this._state.value.mapKatalogeintraege;
	}

	get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}

	get mapJahrgaenge(): Map<number, JahrgangsListeEintrag> {
		return this._state.value.mapJahrgaenge;
	}

	get daten(): KursDaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getKurseFuerAbschnitt(api.schema, routeApp.data.aktAbschnitt.value.id);
		const mapKatalogeintraege = new Map();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		// Laden der Jahrgänge
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const mapJahrgaenge = new Map();
		for (const j of listJahrgaenge)
			mapJahrgaenge.set(j.id, j);
		// Laden des Lehrer-Katalogs
		const listLehrer = await api.server.getLehrer(api.schema);
		const mapLehrer = new Map();
		for (const l of listLehrer)
			mapLehrer.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege, mapLehrer, mapJahrgaenge })
	}

	setEintrag = async (auswahl: KursListeEintrag) => {
		const daten = await api.server.getKurs(api.schema, auswahl.id);
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: JahrgangsListeEintrag) => {
		await RouteManager.doRoute(routeKurse.getRoute(eintrag.id));
	}

	gotoSchueler = async (eintrag: Schueler) => {
		await RouteManager.doRoute(routeSchueler.getRoute(eintrag.id));
	}

	patch = async (data : Partial<KursDaten>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patchKursDaten", data);
		//await api.server.patchKursDaten(data, api.schema, this.item.id);
	}
}

const SKurseAuswahl = () => import("~/components/kurse/SKurseAuswahl.vue")
const SKurseApp = () => import("~/components/kurse/SKurseApp.vue")

export class RouteKurse extends RouteNode<RouteDataKurse, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kurse", "/kurse/:id(\\d+)?", SKurseApp, new RouteDataKurse());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kurse";
		super.setView("liste", SKurseAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKursDaten
		];
		super.defaultChild = routeKursDaten;
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
		let eintrag: KursListeEintrag | undefined;
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

	public getAuswahlProps(to: RouteLocationNormalized): KurseAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapKatalogeintraege: this.data.mapKatalogeintraege,
			mapJahrgaenge: this.data.mapJahrgaenge,
			mapLehrer: this.data.mapLehrer,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoEintrag: this.data.gotoEintrag,
		};
	}

	public getProps(to: RouteLocationNormalized): KurseAppProps {
		return {
			auswahl: this.data.auswahl,
			mapLehrer: this.data.mapLehrer,
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

export const routeKurse = new RouteKurse();
