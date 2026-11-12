import SApp from "@wenom/components/SApp.vue";
import type { AppProps } from "@wenom/components/SAppProps";
import { RouteDataApp } from "@wenom/router/apps/RouteDataApp";
import { routeLeistungen } from "@wenom/router/apps/RouteLeistungen";
import { routeError } from "@wenom/router/error/RouteError";
import { RouteManager } from "@wenom/router/RouteManager";
import { RouteNode } from "@wenom/router/RouteNode";
import { RoutingStatus } from "@wenom/router/RoutingStatus";
import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { ENMLehrerFunktion } from "@core/core/data/enm/ENMLehrerFunktion";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import type { TabData } from "@ui/ui/nav/TabData";

import { routeAnkreuzkompetenzen } from "./RouteAnkreuzkompetenzen";
import { routeKlassenleitung } from "./RouteKlassenleitung";
import { routeKonferenzuebersicht } from "./RouteKonferenzuebersicht";
import { routeSchuelerfaecher } from "./RouteSchuelerfaecher";
import { routeTeilleistungen } from "./RouteTeilleistungen";


export class RouteApp extends RouteNode<RouteDataApp, any> {

	/** Die Knoten, welche im Haupt-Menu zur Verfügung gestellt werden */
	private readonly _menuMain: RouteNode<any, any>[];

	public constructor() {
		super(Schulform.values(), "app", "/", SApp, new RouteDataApp());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "ENM-Client";
		this._menuMain = [
			routeLeistungen,
			routeTeilleistungen,
			routeAnkreuzkompetenzen,
			routeKlassenleitung,
			routeSchuelerfaecher,
			routeKonferenzuebersicht,
		];
		super.children = [
			...this._menuMain,
		];
		super.menu = this._menuMain;
		super.defaultChild = routeLeistungen;
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		try {
			// Lade die ENM-Daten
			if (isEntering) {
				await this.data.ladeDaten();
			}
			// Prüfe, ob dies die Ziel-Route ist. Wenn ja, dann leite zum Default-Child um
			if (to.name === this.name) {
				return this.defaultChild!.getRoute();
			}
			this.data.setAuswahlKlassen([]);
			// Prüfe, ob die View aktualisiert werden muss
			let cur: RouteNode<any, any> = to;
			while (cur.parent !== this) {
				cur = cur.parent;
			}
			if (cur !== this.data.view) {
				this.data.setView(cur, this.children);
			}
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		// Entferne die geladenen ENM-Daten wieder
		this.data.entferneDaten();
	}

	public getProps(): AppProps {
		return {
			enmManager: () => routeApp.data.manager,
			setAnsicht: (lf) => routeApp.data.setAnsicht(lf),
			optionsAuswahlAnsicht: () => routeApp.data.optionsAuswahlAnsicht,
			auswahlAnsicht: () => routeApp.data.auswahlAnsicht,

			// Props für die Navigation
			setApp: this.setApp,
			app: this.getApp(),
			selectedChild: this.getSelectedChild(),
			apps: this.getApps(),
			appsHidden: this.children_hidden().value,
		};
	}

	private getSelectedChild(): TabData {
		const child = this.selectedChild!;
		return { name: child.name, text: child.text, hide: false };
	}

	private getApp(): TabData {
		return { name: this.data.view.name, text: this.data.view.text, hide: !this.data.view.hasView('liste') };
	}

	private getApps(): TabData[] {
		const result: TabData[] = [];
		for (const c of super.menu) {
			if (c.hatSchulform() && (c.hidden() === false)) {
				result.push({ name: c.name, text: c.text });
			}
		}
		return result;
	}

	private readonly setApp = async (value: TabData) => {
		if (value.name === routeKlassenleitung.name) {
			if (!this.data.optionsAuswahlAnsicht.has(ENMLehrerFunktion.KLASSENLEITUNG)) {
				return;
			}
			if (this.data.auswahlAnsicht !== ENMLehrerFunktion.KLASSENLEITUNG) {
				await this.data.setAnsicht(ENMLehrerFunktion.KLASSENLEITUNG);
			}
		}
		if ((value.name === routeSchuelerfaecher.name)
			&& (this.data.auswahlAnsicht !== ENMLehrerFunktion.KLASSENLEITUNG)
			&& (this.data.auswahlAnsicht !== ENMLehrerFunktion.ABTEILUNGSLEITUNG)) {
			const zielAnsicht = this.data.optionsAuswahlAnsicht.has(ENMLehrerFunktion.KLASSENLEITUNG)
				? ENMLehrerFunktion.KLASSENLEITUNG
				: ENMLehrerFunktion.ABTEILUNGSLEITUNG;
			if (this.data.optionsAuswahlAnsicht.has(zielAnsicht)) {
				await this.data.setAnsicht(zielAnsicht);
			}
		}
		if ((value.name === routeKonferenzuebersicht.name)
			&& (this.data.auswahlAnsicht !== ENMLehrerFunktion.KLASSENLEITUNG)
			&& (this.data.auswahlAnsicht !== ENMLehrerFunktion.ABTEILUNGSLEITUNG)) {
			const zielAnsicht = this.data.optionsAuswahlAnsicht.has(ENMLehrerFunktion.KLASSENLEITUNG)
				? ENMLehrerFunktion.KLASSENLEITUNG
				: ENMLehrerFunktion.ABTEILUNGSLEITUNG;
			if (this.data.optionsAuswahlAnsicht.has(zielAnsicht)) {
				await this.data.setAnsicht(zielAnsicht);
			}
		}
		if (value.name === this.data.view.name) {
			return;
		}
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined) {
			throw new DeveloperNotificationException("Unbekannte Route");
		}
		const result = await RouteManager.doRoute(node.getRoute());
		if (result === RoutingStatus.SUCCESS) {
			this.data.setView(node, this.children);
		}
	};

}

export const routeApp = new RouteApp();
