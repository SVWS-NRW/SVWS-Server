import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, GostHalbjahr, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { schulformenGymOb } from "~/router/RouteHelper";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";
import { routeGost, type RouteGost } from "~/router/apps/gost/RouteGost";

import { routeGostKlausurplanungVorgaben } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungVorgaben";
import { routeGostKlausurplanungSchienen } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungSchienen";
import { routeGostKlausurplanungKalender } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungKalender";
import { routeGostKlausurplanungRaumzeit } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungRaumzeit";
import { routeGostKlausurplanungDetailAnsicht } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungDetailAnsicht";
import { routeGostKlausurplanungNachschreiber } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungNachschreiber";
import { routeGostKlausurplanungNachschreibAnsicht } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungNachschreibAnsicht";

import { RouteDataGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteDataGostKlausurplanung";

import type { GostKlausurplanungAuswahlProps } from "~/components/gost/klausurplanung/SGostKlausurplanungAuswahlProps";
import { routeError } from "~/router/error/RouteError";
import { ConfigElement } from "~/components/Config";
import { api } from "~/router/Api";
import type { GostKlausurplanungProps } from "~/components/gost/klausurplanung/SGostKlausurplanungProps";
import { routeGostKlausurplanungProbleme } from "./RouteGostKlausurplanungProbleme";
import type { TabData } from "@ui";


const SGostKlausurplanung = () => import("~/components/gost/klausurplanung/SGostKlausurplanung.vue");
const SGostKlausurplanungAuswahl = () => import("~/components/gost/klausurplanung/SGostKlausurplanungAuswahl.vue");

export class RouteGostKlausurplanung extends RouteNode<RouteDataGostKlausurplanung, RouteGost> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN
		], "gost.klausurplanung", "klausurplanung/:halbjahr([0-5])?", SGostKlausurplanung, new RouteDataGostKlausurplanung());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.setView("gost_child_auswahl", SGostKlausurplanungAuswahl, (route) => this.getAuswahlProps(route));
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
		api.config.addElements([
			new ConfigElement("gost.klausurplan.quartal", "user", "0"),
			new ConfigElement("gost.klausurplan.zeigeAlleJahrgaenge", "user", "false"),
			new ConfigElement("gost.klausurplan.raumblockung_regel_optimiere_blocke_in_moeglichst_wenig_raeume", "user", "true"),
			new ConfigElement("gost.klausurplan.raumblockung_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume", "user", "true"),
			new ConfigElement("gost.klausurplan.raumblockung_regel_forciere_selbe_kursklausur_im_selben_raum", "user", "true"),
			new ConfigElement("gost.klausurplan.raumblockung_regel_forciere_selbe_klausurdauer_pro_raum", "user", "false"),
			new ConfigElement("gost.klausurplan.raumblockung_regel_forciere_selben_klausurstart_pro_raum", "user", "true"),
		]);
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		try {
			const { abiturjahr } = params ? RouteNode.getIntParams(params, ["abiturjahr"]) : { abiturjahr: null };
			if ((abiturjahr === null))
				return { name: routeGost.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr }};
			return false;
		} catch (e) {
			return routeError.getRoute(e as DeveloperNotificationException);
		}
	}

	public async beforeEach(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr } = RouteNode.getIntParams(to_params, ["abiturjahr"]);
			if ((abiturjahr === undefined))
				return routeGost.defaultChild!.getRoute(-1);
			return true;
		} catch (e) {
			return routeError.getRoute(e as DeveloperNotificationException);
		}
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr, halbjahr: halbjahrId, idtermin, kw } = RouteNode.getIntParams(to_params, ["abiturjahr", "halbjahr", "idtermin", "kw"]);
			const halbjahr = GostHalbjahr.fromID(halbjahrId ?? null);
			// Prüfe das Abiturjahr
			if (abiturjahr === undefined)
				throw new DeveloperNotificationException("Fehler: Das Abiturjahr darf an dieser Stelle nicht undefined sein.");
			// Aktualisiere das Halbjahr
			if (halbjahr === null) {
				let hj = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(abiturjahr, routeApp.data.aktAbschnitt.value.schuljahr, routeApp.data.aktAbschnitt.value.abschnitt);
				if (hj === null) // In zwei Fällen existiert Halbjahr, z.B. weil der Abiturjahrgang abgeschlossen ist oder noch in der Sek I ist.
					hj = (abiturjahr < routeApp.data.aktAbschnitt.value.schuljahr + routeApp.data.aktAbschnitt.value.abschnitt) ? GostHalbjahr.Q22 : GostHalbjahr.EF1;
				return this.getRoute(abiturjahr, hj.id);
			}
			const hjChanged = !this.data.hatAbiturjahr || (this.data.abiturjahr !== abiturjahr);
			await this.data.setAbiturjahr(abiturjahr);
			const changedHalbjahr: boolean = await this.data.setHalbjahr(halbjahr, hjChanged);
			if (!to.name.startsWith(this.data.view.name))
				for (const child of this.children)
					if (to.name.startsWith(child.name))
						this.data.setView(child, this.children);
			if (changedHalbjahr || (to.name === this.name)) {
				if (this.data.view.name === "gost.klausurplanung.raumzeit" && idtermin !== undefined)
					return this.data.view.getRoute(abiturjahr, halbjahr.id, idtermin);
				if (this.data.view.name === "gost.klausurplanung.kalender")
					return this.data.view.getRoute(abiturjahr, halbjahr.id, kw, idtermin);
				return this.data.view.getRoute(abiturjahr, halbjahr.id);
			}
		} catch(e) {
			return routeError.getRoute(e instanceof Error ? e : new DeveloperNotificationException("Unbekannter Fehler beim Laden der Klausurplanungsdaten."));
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		this.data.reset(); // Hiermit geht auch die Information verloren, welches die letzte ausgewählte Sub-Route war. Dies könnte ggf. über das Sichern der view vermieden werden
	}

	public getRoute(abiturjahr: number, halbjahr?: number) : RouteLocationRaw {
		if (halbjahr === undefined)
			return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: abiturjahr }};
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): GostKlausurplanungAuswahlProps {
		return {
			kMan: () => routeGostKlausurplanung.data.manager,
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
			gotoHalbjahr: this.data.gotoHalbjahr,
			halbjahr: this.data.halbjahr,
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
		};
	}

	protected checkTabVisibility(tab: TabData) {
		if (this.data.abiturjahr === -1)
			return (tab.name === routeGostKlausurplanungVorgaben.name);
		return true;
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: this.data.abiturjahr, halbjahr: this.data.halbjahr.id } });
		this.data.setView(node, this.children);
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungProps {
		return {
			apiStatus: api.status,
			kMan: () => routeGostKlausurplanung.data.manager,
			getPDF: this.data.getPDF,
		}
	}

}

export const routeGostKlausurplanung = new RouteGostKlausurplanung();
