import { routeSchule } from "~/router/apps/RouteSchule";
import { routeSchuleBenutzer } from "~/router/apps/schule/RouteSchuleBenutzer";
import { routeSchuleBenutzergruppe } from "~/router/apps/schule/RouteSchuleBenutzergruppe";
import { routeSchuleDatenaustausch } from "./apps/schule/RouteSchuleDatenaustausch";
import { routeKataloge } from "~/router/apps/RouteKataloge";
import { routeKatalogFaecher } from "~/router/apps/RouteKatalogFaecher";
import { routeKatalogReligion } from "~/router/apps/RouteKatalogReligionen";
import { routeKatalogJahrgaenge } from "~/router/apps/RouteKatalogJahrgaenge";
import { routeKatalogFoerderschwerpunkte } from "./apps/RouteKatalogFoerderschwerpunkte";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { routeLehrer } from "~/router/apps/RouteLehrer";
import { routeKlassen } from "~/router/apps/RouteKlassen";
import { routeKurse } from "~/router/apps/RouteKurse";
import { routeGost } from "~/router/apps/RouteGost";
import { routeStatistik } from "~/router/apps/RouteStatistik";
import { routeLogin } from "./RouteLogin";
import { RouteNode } from "~/router/RouteNode";

import SApp from "~/components/SApp.vue";
import type { AppProps } from "~/components/SAppProps";
import type { RouteLocationRaw, RouteParams } from "vue-router";
import { type OrtKatalogEintrag, type OrtsteilKatalogEintrag, Schuljahresabschnitt, Schulform, BenutzerKompetenz } from "@svws-nrw/svws-core-ts";
import { computed, shallowRef, WritableComputedRef } from "vue";
import { api } from "./Api";
import { ConfigElement } from "~/components/Config";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import { RouteManager } from "./RouteManager";

interface RouteStateApp {
	idSchuljahresabschnitt: number,
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	view: RouteNode<any, any>;
}
export class RouteDataApp {

	private static _defaultState : RouteStateApp = {
		idSchuljahresabschnitt: -1,
		mapOrte: new Map(),
		mapOrtsteile: new Map(),
		view: routeSchueler
	}

	private _state = shallowRef<RouteStateApp>(RouteDataApp._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateApp>) {
		this._state.value = Object.assign({ ... RouteDataApp._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateApp>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	aktAbschnitt: WritableComputedRef<Schuljahresabschnitt> = computed({
		get: () => {
			let abschnitt = api.config.getObjectValue("app.akt_abschnitt", Schuljahresabschnitt.transpilerFromJSON);
			if (abschnitt === null) {
				void api.config.setObjectValue("app.akt_abschnitt", api.abschnitt, Schuljahresabschnitt.transpilerToJSON);
				abschnitt = api.abschnitt;
			}
			return abschnitt;
		},
		set: (abschnitt: Schuljahresabschnitt) => {
			void api.config.setObjectValue("app.akt_abschnitt", abschnitt, Schuljahresabschnitt.transpilerToJSON);
			// TODO was tun, wenn das akt Halbjahr neu gesetzt wurde?
		}
	})

	setAbschnitt = (abschnitt: Schuljahresabschnitt): void => {
		this.aktAbschnitt.value = abschnitt;
	}

	/**
	 * Setzt den Schuljahresabschnitt und triggert damit das Laden der Defaults für diesen Abschnitt
	 *
	 * @param {number} idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	public async setSchuljahresabschnitt(idSchuljahresabschnitt?: number) {
		if (idSchuljahresabschnitt === undefined) {
			this._state.value = RouteDataApp._defaultState;
			return;
		}
		// Lade den Katalog der Orte
		const orte = await api.server.getOrte(api.schema);
		const mapOrte = new Map();
		for (const o of orte)
			mapOrte.set(o.id, o);
		// Lade den Katalog der Ortsteile
		const ortsteile = await api.server.getOrtsteile(api.schema);
		const mapOrtsteile = new Map();
		for (const o of ortsteile)
			mapOrtsteile.set(o.id, o);
		this.setPatchedDefaultState({
			idSchuljahresabschnitt: idSchuljahresabschnitt,
			mapOrte,
			mapOrtsteile,
			view: this._state.value.view,
		});
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeApp.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese gewählte Ansicht wird nicht unterstützt.");
	}

	public get mapOrte() {
		return this._state.value.mapOrte;
	}

	public get mapOrtsteile() {
		return this._state.value.mapOrtsteile;
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

}
export class RouteApp extends RouteNode<RouteDataApp, any> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "app", "/", SApp, new RouteDataApp());
		super.propHandler = (route) => this.getProps();
		super.text = "SVWS-Client";
		super.children = [
			routeSchule,
			routeSchuleBenutzer,
			routeSchuleBenutzergruppe,
			routeSchuleDatenaustausch,
			routeKataloge,
			routeKatalogFaecher,
			routeKatalogReligion,
			routeKatalogJahrgaenge,
			routeKatalogFoerderschwerpunkte,
			routeSchueler,
			routeLehrer,
			routeKlassen,
			routeKurse,
			routeGost,
			routeStatistik
		];
		super.menu = [
			routeSchule,
			routeKataloge,
			routeSchueler,
			routeLehrer,
			routeKlassen,
			routeKurse,
			routeGost,
			routeStatistik
		];
		super.defaultChild = routeSchueler;
		api.config.addElements([
			new ConfigElement("app.akt_abschnitt", "user", "")
		]);
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<any> {
		return this.getRoute();
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.data.setSchuljahresabschnitt(this.data.aktAbschnitt.value.id);
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		let cur: RouteNode<unknown, any> = to;
		while (cur.parent !== this)
		  cur = cur.parent;
		if (cur !== this.data.view)
			await this.data.setView(cur);
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		await this.data.setSchuljahresabschnitt();
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.defaultChild!.name };
	}

	public getProps(): AppProps {
		return {
			logout: routeLogin.logout,
			username: api.username,
			schulform: api.schulform,
			schuleStammdaten: api.schuleStammdaten,
			// Props für die Navigation
			setApp: this.setApp,
			app: this.getApp(),
			apps: this.getApps(),
			appsHidden: this.children_hidden().value,
		};
	}
	private getApp(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getApps(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of super.menu)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setApp = async (value: AuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: {  } });
		await this.data.setView(node);
	}

}

export const routeApp = new RouteApp();
