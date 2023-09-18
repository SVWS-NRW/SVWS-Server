import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { StundenplanAufsichtsbereich} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKataloge } from "~/router/apps/kataloge/RouteKataloge";
import { routeKatalogAufsichtsbereichDaten } from "~/router/apps/kataloge/aufsichtsbereich/RouteKatalogAufsichtsbereichDaten";

import type { AufsichtsbereicheAuswahlProps } from "~/components/kataloge/aufsichtsbereiche/SAufsichtsbereicheAuswahlProps";
import type { AufsichtsbereicheAppProps } from "~/components/kataloge/aufsichtsbereiche/SAufsichtsbereicheAppProps";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

interface RouteStateKatalogAufsichtsbereiche {
	auswahl: StundenplanAufsichtsbereich | undefined;
	daten: StundenplanAufsichtsbereich | undefined;
	mapKatalogeintraege: Map<number, StundenplanAufsichtsbereich>;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogAufsichtsbereiche {

	private static _defaultState: RouteStateKatalogAufsichtsbereiche = {
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		view: routeKatalogAufsichtsbereichDaten,
	}
	private _state = shallowRef(RouteDataKatalogAufsichtsbereiche._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogAufsichtsbereiche>) {
		this._state.value = Object.assign({ ... RouteDataKatalogAufsichtsbereiche._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogAufsichtsbereiche>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogAufsichtsbereiche.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Räume gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): StundenplanAufsichtsbereich | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, StundenplanAufsichtsbereich> {
		return this._state.value.mapKatalogeintraege;
	}

	get daten(): StundenplanAufsichtsbereich {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Raumdaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getAufsichtsbereiche(api.schema);
		const mapKatalogeintraege = new Map<number, StundenplanAufsichtsbereich>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl: StundenplanAufsichtsbereich) => {
		const daten = this.mapKatalogeintraege.get(auswahl.id);
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: StundenplanAufsichtsbereich) => {
		await RouteManager.doRoute(routeKatalogAufsichtsbereiche.getRoute(eintrag.id));
	}

	addEintrag = async (eintrag: Partial<StundenplanAufsichtsbereich>) => {
		delete eintrag.id;
		const raum = await api.server.addAufsichtsbereich(eintrag, api.schema);
		const mapKatalogeintraege = this.mapKatalogeintraege;
		mapKatalogeintraege.set(raum.id, raum);
		this.setPatchedState({mapKatalogeintraege});
		await this.gotoEintrag(raum);
	}

	deleteEintraege = async (eintraege: StundenplanAufsichtsbereich[]) => {
		const mapKatalogeintraege = this.mapKatalogeintraege;
		for (const eintrag of eintraege) {
			const raum = await api.server.deleteAufsichtsbereich(api.schema, eintrag.id);
			mapKatalogeintraege.delete(raum.id);
		}
		let auswahl;
		if (this.auswahl && mapKatalogeintraege.get(this.auswahl.id) === undefined)
			auswahl = mapKatalogeintraege.values().next().value;
		this.setPatchedState({mapKatalogeintraege, auswahl});
	}

	patch = async (eintrag : Partial<StundenplanAufsichtsbereich>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchAufsichtsbereich(eintrag, api.schema, this.auswahl.id);
		const auswahl = this.auswahl;
		this.setPatchedState({auswahl: Object.assign(auswahl, eintrag)});
	}
}

const SAufsichtsbereicheAuswahl = () => import("~/components/kataloge/aufsichtsbereiche/SAufsichtsbereicheAuswahl.vue")
const SAufsichtsbereicheApp = () => import("~/components/kataloge/aufsichtsbereiche/SAufsichtsbereicheApp.vue")

export class RouteKatalogAufsichtsbereiche extends RouteNode<RouteDataKatalogAufsichtsbereiche, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.aufsichtsbereiche", "/kataloge/aufsichtsbereiche/:id(\\d+)?", SAufsichtsbereicheApp, new RouteDataKatalogAufsichtsbereiche());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Aufsichtsbereiche";
		super.setView("liste", SAufsichtsbereicheAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogAufsichtsbereichDaten
		];
		super.defaultChild = routeKatalogAufsichtsbereichDaten;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.data.mapKatalogeintraege.size < 1)
			return;
		let eintrag: StundenplanAufsichtsbereich | undefined;
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

	public getAuswahlProps(to: RouteLocationNormalized): AufsichtsbereicheAuswahlProps {
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

	public getProps(to: RouteLocationNormalized): AufsichtsbereicheAppProps {
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

export const routeKatalogAufsichtsbereiche = new RouteKatalogAufsichtsbereiche();
