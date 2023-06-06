import type { Raum} from "@core";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { RouteApp } from "~/router/RouteApp";
import type { RaeumeAuswahlProps } from "~/components/kataloge/raeume/SRaeumeAuswahlProps";
import type { RaeumeAppProps } from "~/components/kataloge/raeume/SRaeumeAppProps";
import { BenutzerKompetenz, Schulform } from "@core";
import { shallowRef } from "vue";
import { routeApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";
import { routeKataloge } from "./RouteKataloge";
import { routeKatalogRaumDaten } from "./raum/RouteKatalogRaumDaten";


interface RouteStateKatalogRaeume {
	auswahl: Raum | undefined;
	daten: Raum | undefined;
	mapKatalogeintraege: Map<number, Raum>;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogRaeume {

	private static _defaultState: RouteStateKatalogRaeume = {
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		view: routeKatalogRaumDaten,
	}
	private _state = shallowRef(RouteDataKatalogRaeume._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogRaeume>) {
		this._state.value = Object.assign({ ... RouteDataKatalogRaeume._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogRaeume>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogRaeume.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Räume gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): Raum | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, Raum> {
		return this._state.value.mapKatalogeintraege;
	}

	get daten(): Raum {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Raumdaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getRaeume(api.schema);
		const mapKatalogeintraege = new Map<number, Raum>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl: Raum) => {
		const daten = this.mapKatalogeintraege.get(auswahl.id);
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: Raum) => {
		await RouteManager.doRoute(routeKatalogRaeume.getRoute(eintrag.id));
	}

	addEintrag = async (eintrag: Partial<Raum>) => {
		delete eintrag.id;
		const raum = await api.server.addRaum(eintrag, api.schema);
		const mapKatalogeintraege = this.mapKatalogeintraege;
		mapKatalogeintraege.set(raum.id, raum);
		this.setPatchedState({mapKatalogeintraege});
		await this.gotoEintrag(raum);
	}

	deleteEintraege = async (eintraege: Raum[]) => {
		const mapKatalogeintraege = this.mapKatalogeintraege;
		for (const eintrag of eintraege) {
			const raum = await api.server.deleteRaum(api.schema, eintrag.id);
			mapKatalogeintraege.delete(raum.id);
		}
		let auswahl;
		if (this.auswahl && mapKatalogeintraege.get(this.auswahl.id) === undefined)
			auswahl = mapKatalogeintraege.values().next().value;
		this.setPatchedState({mapKatalogeintraege, auswahl});
	}

	patch = async (eintrag : Partial<Raum>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchRaum(eintrag, api.schema, this.auswahl.id);
		const auswahl = this.auswahl;
		this.setPatchedState({auswahl: Object.assign(auswahl, eintrag)});
	}
}

const SRaeumeAuswahl = () => import("~/components/kataloge/raeume/SRaeumeAuswahl.vue")
const SRaeumeApp = () => import("~/components/kataloge/raeume/SRaeumeApp.vue")

export class RouteKatalogRaeume extends RouteNode<RouteDataKatalogRaeume, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.raeume", "/kataloge/raeume/:id(\\d+)?", SRaeumeApp, new RouteDataKatalogRaeume());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Räume";
		super.setView("liste", SRaeumeAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogRaumDaten
		];
		super.defaultChild = routeKatalogRaumDaten;
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
		let eintrag: Raum | undefined;
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

	public getAuswahlProps(to: RouteLocationNormalized): RaeumeAuswahlProps {
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

	public getProps(to: RouteLocationNormalized): RaeumeAppProps {
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

export const routeKatalogRaeume = new RouteKatalogRaeume();
