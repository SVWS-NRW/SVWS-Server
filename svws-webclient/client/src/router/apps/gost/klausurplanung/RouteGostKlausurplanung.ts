import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, GostHalbjahr, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
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

import type { GostKlausurplanungAuswahlChildData, GostKlausurplanungAuswahlProps } from "~/components/gost/klausurplanung/SGostKlausurplanungAuswahlProps";
import { routeError } from "~/router/error/RouteError";


const SGostKlausurplanung = () => import("~/components/gost/klausurplanung/SGostKlausurplanung.vue");
const SGostKlausurplanungAuswahl = () => import("~/components/gost/klausurplanung/SGostKlausurplanungAuswahl.vue");

export class RouteGostKlausurplanung extends RouteNode<RouteDataGostKlausurplanung, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung", "klausurplanung/:halbjahr([0-5])?", SGostKlausurplanung, new RouteDataGostKlausurplanung());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getNoProps(route);
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
		];
		super.defaultChild = routeGostKlausurplanungVorgaben;
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		const abiturjahr = params?.abiturjahr === undefined ? null : Number(params.abiturjahr);
		if (abiturjahr === null)
			return { name: routeGost.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: abiturjahr }};
		return false;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : Number(to_params.abiturjahr);
		if ((abiturjahr === undefined))
			return routeGost.defaultChild!.getRoute(-1);
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			// Prüfe die Parameter zunächst allgemein
			if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array)
				throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
			const abiturjahr = !to_params.abiturjahr ? undefined : parseInt(to_params.abiturjahr);
			const halbjahr = !to_params.halbjahr ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
			// Prüfe das Abiturjahr
			if (abiturjahr === undefined)
				throw new DeveloperNotificationException("Fehler: Das Abiturjahr darf an dieser Stelle nicht undefined sein.");
			// Aktualisiere das Halbjahr
			if (halbjahr === undefined) {
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
			if (changedHalbjahr || (to.name === this.name))
				return this.data.view.getRoute(abiturjahr, halbjahr.id);
		} catch(e: unknown) {
			return routeError.getRoute(e instanceof Error ? e : new DeveloperNotificationException("Unbekannter Fehler beim Laden der Klausurplanungsdaten."));
		}
	}

	public getRoute(abiturjahr: number, halbjahr?: number) : RouteLocationRaw {
		if (halbjahr === undefined)
			return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: abiturjahr }};
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): GostKlausurplanungAuswahlProps {
		return {
			gotoHalbjahr: this.data.gotoHalbjahr,
			halbjahr: this.data.halbjahr,
			setChild: this.setChild,
			child: this.getChild(),
			children: this.getChildData(),
			childrenHidden: this.children_hidden().value,
		};
	}

	private getChild(): GostKlausurplanungAuswahlChildData {
		return this.data.view;
	}

	private getChildData(): GostKlausurplanungAuswahlChildData[] {
		const result: GostKlausurplanungAuswahlChildData[] = [];
		if (this.data.abiturjahr === -1) {
			result.push(routeGostKlausurplanungVorgaben);
			// result.push(routeGostKlausurplanungKalender);
			return result;
		}
		for (const c of this.children)
			result.push(c);
		return result;
	}

	private setChild = async (value: GostKlausurplanungAuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: this.data.abiturjahr, halbjahr: this.data.halbjahr.id } });
		this.data.setView(node, this.children);
	}

}

export const routeGostKlausurplanung = new RouteGostKlausurplanung();
