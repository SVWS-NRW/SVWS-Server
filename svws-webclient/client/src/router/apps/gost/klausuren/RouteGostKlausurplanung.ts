import type { RouteLocationAsRelativeGeneric, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import { gostKlausurplanungStateImpl } from "~/states/GostKlausurplanungStateImpl";

import { BenutzerKompetenz, DeveloperNotificationException, GostHalbjahr, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { schulformenGymOb } from "~/router/RouteHelper";
import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost } from "~/router/apps/gost/RouteGost";

import { routeGostKlausurplanungVorgaben } from "~/router/apps/gost/klausuren/RouteGostKlausurplanungVorgaben";
import { routeGostKlausurplanungSchienen } from "~/router/apps/gost/klausuren/RouteGostKlausurplanungSchienen";
import { routeGostKlausurplanungKalender } from "~/router/apps/gost/klausuren/RouteGostKlausurplanungKalender";
import { routeGostKlausurplanungRaumzeit } from "~/router/apps/gost/klausuren/RouteGostKlausurplanungRaumzeit";
import { routeGostKlausurplanungDetailAnsicht } from "~/router/apps/gost/klausuren/RouteGostKlausurplanungDetailAnsicht";
import { routeGostKlausurplanungNachschreiber } from "~/router/apps/gost/klausuren/RouteGostKlausurplanungNachschreiber";
import { routeGostKlausurplanungNachschreibAnsicht } from "~/router/apps/gost/klausuren/RouteGostKlausurplanungNachschreibAnsicht";

import { RouteDataGostKlausurplanung } from "~/router/apps/gost/klausuren/RouteDataGostKlausurplanung";

import { routeError } from "~/router/error/RouteError";
import { ConfigElement, CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX } from "@ui";
import { api } from "~/router/Api";
import type { GostKlausurplanungProps } from "~/components/gost/klausuren/SGostKlausurplanungProps";
import { routeGostKlausurplanungProbleme } from "./RouteGostKlausurplanungProbleme";
import type { TabData } from "@ui";
import { CONFIG_KEY_GOST_KLAUSURPLAN_VORGABENTOIGNORE } from "~/components/gost/klausuren/SGostKlausurplanungVorgabenIgnoreManager";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { configStateImpl } from "~/states/ConfigStateImpl";

const SGostKlausurplanung = () => import("~/components/gost/klausuren/SGostKlausurplanung.vue");
const SGostKlausurplanungAuswahl = () => import("~/components/gost/klausuren/SGostKlausurplanungAuswahl.vue");

export function checkHiddenKlausurplanungStundenplan(params?: RouteParams): false | RouteLocationRaw {
	const abschnitt = gostKlausurplanungStateImpl.abschnitt;
	if ((abschnitt === undefined) || !gostKlausurplanungStateImpl.manager.stundenplanManagerGeladenAndExistsByAbschnitt(abschnitt.id)) {
		return { name: routeGostKlausurplanung.defaultChild!.name, params };
	}
	return false;
}

export class RouteGostKlausurplanung extends RouteNode<RouteDataGostKlausurplanung, RouteGost> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
		], "gost.klausurplanung", "klausurplanung/:halbjahr([0-5])?", SGostKlausurplanung, new RouteDataGostKlausurplanung());
		super.mode = ServerMode.STABLE;
		super.propHandler = () => this.getProps();
		super.setView("gost_child_auswahl", SGostKlausurplanungAuswahl, () => ({ gotoHalbjahr: this.data.gotoHalbjahr }));
		super.text = "Klausurplanung";
		super.children = [
			routeGostKlausurplanungVorgaben,
			routeGostKlausurplanungSchienen,
			routeGostKlausurplanungKalender,
			routeGostKlausurplanungRaumzeit,
			routeGostKlausurplanungDetailAnsicht,
			routeGostKlausurplanungNachschreiber,
			routeGostKlausurplanungNachschreibAnsicht,
			routeGostKlausurplanungProbleme,
		];
		super.defaultChild = routeGostKlausurplanungVorgaben;
		configStateImpl.config.addElements([
			new ConfigElement(CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX + "quartal", "user", "0"),
			new ConfigElement(CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX + "zeigeAlleJahrgaenge", "user", "false"),
			new ConfigElement(CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX + "kwWarnLimit", "user", "3"),
			new ConfigElement(CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX + "kwErrorLimit", "user", "4"),
			new ConfigElement(CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX + "raumblockung_regel_optimiere_blocke_in_moeglichst_wenig_raeume", "user", "true"),
			new ConfigElement(CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX + "raumblockung_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume", "user", "true"),
			new ConfigElement(CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX + "raumblockung_regel_forciere_selbe_kursklausur_im_selben_raum", "user", "true"),
			new ConfigElement(CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX + "raumblockung_regel_forciere_selbe_klausurdauer_pro_raum", "user", "false"),
			new ConfigElement(CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX + "raumblockung_regel_forciere_selben_klausurstart_pro_raum", "user", "true"),
			new ConfigElement(CONFIG_KEY_GOST_KLAUSURPLAN_VORGABENTOIGNORE, "user", "[]"),
		]);
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		};
	}

	public checkHidden(params?: RouteParams) {
		try {
			const { abiturjahr } = params ? RouteNode.getIntParams(params, ["abiturjahr"]) : { abiturjahr: undefined };
			if (abiturjahr === undefined) {
				return routeGost.getRouteDefaultChild({ abiturjahr: -1 });
			}
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async beforeEach(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams): Promise<boolean | void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr } = RouteNode.getIntParams(to_params, ["abiturjahr"]);
			if ((abiturjahr === undefined)) {
				return routeGost.getRouteDefaultChild({ abiturjahr: -1 });
			}
			return true;
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr, halbjahr: halbjahrId, idtermin } = RouteNode.getIntParams(to_params, ["abiturjahr", "halbjahr", "idtermin"]);
			const { datum } = RouteNode.getStringParams(to_params, ["datum"]);
			// Prüfe das Abiturjahr
			if (abiturjahr === undefined) {
				throw new DeveloperNotificationException("Fehler: Das Abiturjahr darf an dieser Stelle nicht undefined sein.");
			}
			// Füge ggf. die Konfiguration für die Routen-Parameter zur Config hinzu
			const routeParamsKey = this.data.getParamsKey(abiturjahr);
			if (!configStateImpl.config.hasElement(routeParamsKey)) {
				configStateImpl.config.addElement(new ConfigElement(routeParamsKey, "user", ""));
			}
			// Aktualisiere das Abiturjahr
			const abiturjahrwechsel = await gostKlausurplanungStateImpl.setAbiturjahr(abiturjahr);
			if ((abiturjahr === -1) && (this.data.view !== routeGostKlausurplanungVorgaben)) {
				this.data.setView(routeGostKlausurplanungVorgaben, this.children);
			}
			// Prüfe, ob ggf. Routing-Parameter für den Abiturjahrgang wiederhergestellt werden sollen...
			if (isEntering) {
				const route = await this.getRouteFromStoredParams(abiturjahr, halbjahrId);
				if (route !== undefined) {
					return route;
				}
			}
			// Aktualisiere das Halbjahr
			let halbjahr = GostHalbjahr.fromID(halbjahrId ?? null);
			if (abiturjahrwechsel || (halbjahr === null)) {
				let hj = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(abiturjahr, abschnittStateImpl.auswahl.schuljahr, abschnittStateImpl.auswahl.abschnitt);
				// In zwei Fällen existiert Halbjahr, z.B. weil der Abiturjahrgang abgeschlossen ist oder noch in der Sek I ist.
				hj ??= (abiturjahr < abschnittStateImpl.auswahl.schuljahr + abschnittStateImpl.auswahl.abschnitt) ? GostHalbjahr.Q22 : GostHalbjahr.EF1;
				halbjahr = hj;
			}
			const changedHalbjahr: boolean = await gostKlausurplanungStateImpl.setHalbjahr(halbjahr, abiturjahrwechsel);
			this.updateViewByRoute(to);
			if (changedHalbjahr || (to.name === this.name)) {
				return this.getRouteForCurrentView(halbjahr, datum, idtermin);
			}
		} catch (e) {
			this.data.reset();
			gostKlausurplanungStateImpl.reset();
			return await routeError.getErrorRoute(e instanceof Error ? e : new DeveloperNotificationException("Unbekannter Fehler beim Laden der Klausurplanungsdaten."));
		}
	}

	private async getRouteFromStoredParams(abiturjahr: number, halbjahrId: number | undefined): Promise<RouteLocationRaw | undefined> {
		const params = this.data.getParams(abiturjahr);
		if (params === undefined) {
			return undefined;
		}
		const { view } = RouteNode.getStringParams(params, ["view"]);
		delete params.view;
		const { halbjahr: storedHalbjahrId } = RouteNode.getIntParams(params, ["halbjahr"]);
		if ((view === this.data.view.name) && ((view !== this._defaultChild!.name) || (storedHalbjahrId === halbjahrId))) {
			return undefined;
		}
		const route = RouteNode.getNodeByName(view) ?? this._defaultChild!;
		this.data.setView(route, this.children);
		const halbjahrRestored = GostHalbjahr.fromID(storedHalbjahrId ?? null);
		if (halbjahrRestored !== null) {
			await gostKlausurplanungStateImpl.setHalbjahr(halbjahrRestored, true);
		}
		return { name: route.name, params };
	}

	private updateViewByRoute(to: RouteNode<any, any>): void {
		if (to.name.startsWith(this.data.view.name)) {
			return;
		}
		for (const child of this.children) {
			if (to.name.startsWith(child.name)) {
				this.data.setView(child, this.children);
				return;
			}
		}
	}

	private getRouteForCurrentView(halbjahr: GostHalbjahr, datum: string | undefined, idtermin: number | undefined): RouteLocationRaw {
		if ((this.data.view.name === routeGostKlausurplanungRaumzeit.name) && (idtermin !== undefined)) {
			return this.data.view.getRoute({ halbjahr: halbjahr.id, idtermin });
		}
		if (this.data.view.name === routeGostKlausurplanungKalender.name) {
			return this.data.view.getRoute({ halbjahr: halbjahr.id, datum, idtermin });
		}
		return this.data.view.getRoute({ halbjahr: halbjahr.id });
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		const { abiturjahr } = RouteNode.getIntParams(from_params, ["abiturjahr"]);
		if (abiturjahr !== undefined) {
			this.data.setParams(abiturjahr, from_params);
		}
		this.data.reset();
		gostKlausurplanungStateImpl.reset();
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { halbjahr: gostKlausurplanungStateImpl.halbjahr.id };
	}

	public getProps(): GostKlausurplanungProps {
		return {
			apiStatus: api.status,
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
		};
	}

	protected checkTabVisibility(tab: TabData) {
		if (gostKlausurplanungStateImpl.abiturjahr === -1) {
			return (tab.name === routeGostKlausurplanungVorgaben.name);
		}
		return true;
	}

	private readonly setTab = async (value: TabData) => {
		if (value.name === this.data.view.name) {
			return;
		}
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined) {
			throw new DeveloperNotificationException("Unbekannte Route");
		}
		const nodeRoute = node.getRoute() as RouteLocationAsRelativeGeneric;
		if ((nodeRoute.params !== undefined)) {
			delete nodeRoute.params.idtermin;
		}
		await RouteManager.doRoute(nodeRoute);
		this.data.setView(node, this.children);
	};

}

export const routeGostKlausurplanung = new RouteGostKlausurplanung();
