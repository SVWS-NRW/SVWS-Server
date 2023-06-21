import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { ZeitrasterAuswahlProps } from "~/components/kataloge/zeitraster/SZeitrasterAuswahlProps";
import type { List, StundenplanZeitraster } from "@core";
import type { ZeitrasterAppProps } from "~/components/kataloge/zeitraster/SZeitrasterAppProps";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { RouteApp } from "~/router/RouteApp";
import { ArrayList, BenutzerKompetenz, Schulform } from "@core";
import { routeKatalogZeitrasterDaten } from "./zeitraster/RouteKatalogZeitrasterDaten";
import { shallowRef, toRaw } from "vue";
import { routeKataloge } from "./RouteKataloge";
import { RouteManager } from "../RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/RouteApp";
import { api } from "../Api";


interface RouteStateKatalogZeitraster {
	listKatalogeintraege: List<StundenplanZeitraster>;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogZeitraster {

	private static _defaultState: RouteStateKatalogZeitraster = {
		listKatalogeintraege: new ArrayList(),
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

	get listKatalogeintraege(): List<StundenplanZeitraster> {
		return this._state.value.listKatalogeintraege;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getZeitraster(api.schema);
		this.setPatchedDefaultState({ listKatalogeintraege })
	}

	patchZeitraster = async (data : StundenplanZeitraster, multi: StundenplanZeitraster[]) => {
		for (const zeitraster of multi)
			await api.server.patchZeitrasterEintrag(Object.assign(toRaw(zeitraster), {unterrichtstunde: data.unterrichtstunde, stundenbeginn: data.stundenbeginn, stundenende: data.stundenende}), api.schema, zeitraster.id);
		this.commit();
	}

	addZeitraster = async (item: Partial<StundenplanZeitraster>, tage: number[]) => {
		for (const tag of tage) {
			delete item.id;
			item.wochentag = tag;
			const _item = await api.server.addZeitrasterEintrag(item, api.schema);
			this.listKatalogeintraege.add(_item);
		}
		this.commit();
	}
	removeZeitraster = async (multi: StundenplanZeitraster[]) => {
		for (const zeitraster of multi) {
			await api.server.deleteZeitrasterEintrag(api.schema, zeitraster.id);
			this.listKatalogeintraege.remove(toRaw(zeitraster));
		}
		this.commit();
	}

	patch = async (eintrag : StundenplanZeitraster) => {
		await api.server.patchZeitrasterEintrag(eintrag, api.schema, eintrag.id);
	}
}

const SZeitrasterAuswahl = () => import("~/components/kataloge/zeitraster/SZeitrasterAuswahl.vue")
const SZeitrasterApp = () => import("~/components/kataloge/zeitraster/SZeitrasterApp.vue")

export class RouteKatalogZeitraster extends RouteNode<RouteDataKatalogZeitraster, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.zeitraster", "/kataloge/zeitraster", SZeitrasterApp, new RouteDataKatalogZeitraster());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeitraster";
		super.setView("liste", SZeitrasterAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogZeitrasterDaten
		];
		super.defaultChild = routeKatalogZeitrasterDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return this.getRoute();
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		await this.data.ladeListe();
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.defaultChild!.name};
	}

	public getAuswahlProps(to: RouteLocationNormalized): ZeitrasterAuswahlProps {
		return {
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			returnToKataloge: routeKataloge.returnToKataloge
		};
	}

	public getProps(to: RouteLocationNormalized): ZeitrasterAppProps {
		return {
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
		await RouteManager.doRoute({ name: value.name });
		await this.data.setView(node);
	}
}

export const routeKatalogZeitraster = new RouteKatalogZeitraster();
