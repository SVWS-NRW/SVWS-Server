import type { ReligionEintrag} from "@svws-nrw/svws-core";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { ReligionenAppProps } from "~/components/kataloge/religionen/SReligionenAppProps";
import type { ReligionenAuswahlProps } from "~/components/kataloge/religionen/SReligionenAuswahlPops";
import { routeKatalogReligionDaten } from "~/router/apps/religion/RouteKatalogReligionDaten";
import type { RouteApp } from "~/router/RouteApp";
import { routeApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";
import { routeKataloge } from "./RouteKataloge";

interface RouteStateKatalogeReligionen {
	auswahl: ReligionEintrag | undefined;
	mapKatalogeintraege: Map<number, ReligionEintrag>;
	view: RouteNode<any, any>;
}
export class RouteDataKatalogReligionen {

	private static _defaultState: RouteStateKatalogeReligionen = {
		auswahl: undefined,
		mapKatalogeintraege: new Map(),
		view: routeKatalogReligionDaten,
	}
	private _state = shallowRef(RouteDataKatalogReligionen._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogeReligionen>) {
		this._state.value = Object.assign({ ... RouteDataKatalogReligionen._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogeReligionen>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogReligion.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Religionen gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): ReligionEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, ReligionEintrag> {
		return this._state.value.mapKatalogeintraege;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getReligionen(api.schema);
		const mapKatalogeintraege = new Map<number, ReligionEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = (eintrag: ReligionEintrag) => {
		this.setPatchedState({
			auswahl: this.mapKatalogeintraege.get(eintrag.id)
		})
	}

	gotoEintrag = async (eintrag: ReligionEintrag) => {
		await RouteManager.doRoute(routeKatalogReligion.getRoute(eintrag.id));
	}

	addEintrag = async (eintrag: ReligionEintrag) => {
		const res = await api.server.createReligion(eintrag, api.schema);
		const mapKatalogeintraege = this.mapKatalogeintraege;
		mapKatalogeintraege.set(res.id, res);
		this.setPatchedState({ mapKatalogeintraege });
		await RouteManager.doRoute({ name: routeKatalogReligionDaten.name, params: { id: res.id } });
	}

	patch = async (data : Partial<ReligionEintrag>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchReligion(data, api.schema, this.auswahl.id)
	}
}
const SReligionenAuswahl = () => import("~/components/kataloge/religionen/SReligionenAuswahl.vue")
const SReligionenApp = () => import("~/components/kataloge/religionen/SReligionenApp.vue")

export class RouteKatalogReligionen extends RouteNode<RouteDataKatalogReligionen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.religionen", "/kataloge/religion/:id(\\d+)?", SReligionenApp, new RouteDataKatalogReligionen());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Religionen";
		super.setView("liste", SReligionenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogReligionDaten
		];
		super.defaultChild = routeKatalogReligionDaten;
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
				this.data.setEintrag(eintrag);
		}
		if (to.name === this.name && this.data.auswahl !== undefined)
			return this.getRoute(this.data.auswahl.id);
	}

	public getRoute(id: number|undefined) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): ReligionenAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapKatalogeintraege: this.data.mapKatalogeintraege,
			addEintrag: this.data.addEintrag,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoEintrag: this.data.gotoEintrag,
			returnToKataloge: routeKataloge.returnToKataloge
		};
	}

	public getProps(to: RouteLocationNormalized): ReligionenAppProps {
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

export const routeKatalogReligion = new RouteKatalogReligionen();
