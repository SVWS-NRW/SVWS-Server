import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlen";
import { routeGostFaecher } from "~/router/apps/gost/faecher/RouteGostFaecher";
import { routeGostBeratung } from "~/router/apps/gost/beratung/RouteGostBeratung";
import { routeGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import { routeGostKursplanung } from "~/router/apps/gost/kursplanung/RouteGostKursplanung";
import { routeGostLaufbahnfehler } from "~/router/apps/gost/laufbahnfehler/RouteGostLaufbahnfehler";

import { RouteDataGost } from "~/router/apps/gost/RouteDataGost";

import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { GostAppProps } from "~/components/gost/SGostAppProps";
import type { GostAuswahlProps } from "~/components/gost/SGostAuswahlProps";

const SGostAuswahl = () => import("~/components/gost/SGostAuswahl.vue")
const SGostApp = () => import("~/components/gost/SGostApp.vue")

export class RouteGost extends RouteNode<RouteDataGost, RouteApp> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost", "gost/:abiturjahr(-?\\d+)?", SGostApp, new RouteDataGost());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Oberstufe";
		super.setView("liste", SGostAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeGostFaecher,
			routeGostBeratung,
			routeGostLaufbahnfehler,
			routeGostFachwahlen,
			routeGostKursplanung,
			routeGostKlausurplanung
		];
		super.defaultChild = routeGostFaecher;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		return this.getRoute();
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.setSchuljahresabschnitt(routeApp.data.aktAbschnitt.value.id);
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.abiturjahr instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		let cur: RouteNode<unknown, any> = to;
		while (cur.parent !== this)
			cur = cur.parent;
		if (cur !== this.data.view)
			this.data.setView(cur, this.children);
		const abiturjahr = !to_params.abiturjahr ? undefined : parseInt(to_params.abiturjahr);
		if (abiturjahr === undefined)
			return this.getRoute();
		const eintrag = this.data.mapAbiturjahrgaenge.get(abiturjahr);
		await this.data.setAbiturjahrgang(eintrag);
		if (this.name !== to.name)
			return;
		const redirect: RouteNode<unknown, any> = (this.selectedChild === undefined) ? this.defaultChild! : this.selectedChild;
		if (redirect.hidden({ abiturjahr: String(abiturjahr) }))
			return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: String(abiturjahr) }};
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		routeGost.data.params = from_params;
	}

	public getRoute(abiturjahr? : number | null) : RouteLocationRaw {
		let redirect: RouteNode<unknown, any> = (this.selectedChild === undefined) ? this.defaultChild! : this.selectedChild;
		if (redirect.hidden({ abiturjahr: String(abiturjahr || -1) }))
			redirect = this.defaultChild!;
		return { name: redirect.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: abiturjahr || -1 }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): GostAuswahlProps {
		return {
			serverMode: api.mode,
			auswahl: this.data.auswahl,
			jahrgangsdaten: () => this.data.auswahl === undefined ? undefined : this.data.jahrgangsdaten,
			mapAbiturjahrgaenge: () => this.data.mapAbiturjahrgaenge,
			mapJahrgaengeOhneAbiJahrgang: () => this.data.mapJahrgaengeOhneAbiJahrgang,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			apiStatus: api.status,
			setAbschnitt: routeApp.data.setAbschnitt,
			addAbiturjahrgang: this.data.addAbiturjahrgang,
			gotoAbiturjahrgang: this.data.gotoAbiturjahrgang,
			getAbiturjahrFuerJahrgang: this.data.getAbiturjahrFuerJahrgang,
			removeAbiturjahrgang: this.data.removeAbiturjahrgang,
		};
	}

	public getProps(to: RouteLocationNormalized): GostAppProps {
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
		let list = super.menu;
		if (list.length < 1)
			list = super.children;
		for (const c of list)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setTab = async (value: AuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: this.data.auswahl?.abiturjahr || -1 } });
		this.data.setView(node, this.children);
	}

}

export const routeGost = new RouteGost();
