import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchule } from "~/router/apps/schule/RouteSchule";
import { routeSchuleFachDaten } from "~/router/apps/schule/faecher/RouteSchuleFachDaten";

import type { TabData } from "@ui";
import type { FaecherAppProps } from "~/components/schule/faecher/SFaecherAppProps";
import type { FaecherAuswahlProps } from "~/components/schule/faecher/SFaecherAuswahlProps";
import { RouteDataSchuleFaecher } from "./RouteDataSchuleFaecher";
import { routeError } from "~/router/error/RouteError";
import { routeFachStundenplan } from "./stundenplan/RouteFachStundenplan";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";

const SSchuleAuswahl = () => import("~/components/schule/SSchuleAuswahl.vue")
const SFaecherAuswahl = () => import("~/components/schule/faecher/SFaecherAuswahl.vue")
const SFaecherApp = () => import("~/components/schule/faecher/SFaecherApp.vue")

export class RouteSchuleFaecher extends RouteNode<RouteDataSchuleFaecher, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.faecher", "schule/faecher/:id(\\d+)?", SFaecherApp, new RouteDataSchuleFaecher());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fächer";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.setView("liste", SFaecherAuswahl, (route) => this.getAuswahlProps(route));
		super.setView("submenu", SSchuleAuswahl, (route) => routeSchule.getAuswahlProps(route));
		super.children = [
			routeSchuleFachDaten,
			routeFachStundenplan,
		];
		super.defaultChild = routeSchuleFachDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id } = RouteNode.getIntParams(to_params, ["id", "idSchuljahresabschnitt"]);
			if (idSchuljahresabschnitt === undefined)
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
			if (this.data.idSchuljahresabschnitt !== idSchuljahresabschnitt) {
				const neueID = await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt);
				if (id !== undefined) {
					if (neueID === null)
						return this.getRoute(id);
					const params = { ... to_params};
					params.id = String(neueID);
					const locationRaw : RouteLocationRaw = {};
					locationRaw.name = to.name;
					locationRaw.params = params;
					return locationRaw;
				}
			}
			const eintrag = (id !== undefined) ? this.data.fachListeManager.liste.get(id) : null;
			await this.data.setEintrag(eintrag);
			if (!this.data.fachListeManager.hasDaten()) {
				if (id === undefined) {
					const listFiltered = this.data.fachListeManager.filtered();
					if (listFiltered.isEmpty())
						return;
					return this.getRoute(this.data.fachListeManager.filtered().get(0).id);
				}
				return this.getRoute();
			}
			if (to.name === this.name)
				return this.getChildRoute(this.data.fachListeManager.daten().id, from);
			if (!to.name.startsWith(this.data.view.name))
				for (const child of this.children)
					if (to.name.startsWith(child.name))
						this.data.setView(child, this.children);
		} catch (e) {
			return routeError.getRoute(e as DeveloperNotificationException);
		}
	}

	public getRoute(id?: number) : RouteLocationRaw {
		if (id === undefined)
			return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getChildRoute(id: number | undefined, from?: RouteNode<any, any>) : RouteLocationRaw {
		if (from !== undefined && (/(\.|^)stundenplan/).test(from.name))
			return { name: routeFachStundenplan.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id } };
		const redirect_name: string = (this.selectedChild === undefined) ? routeSchuleFachDaten.name : this.selectedChild.name;
		return { name: redirect_name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}


	public getAuswahlProps(to: RouteLocationNormalized): FaecherAuswahlProps {
		return {
			fachListeManager: () => this.data.fachListeManager,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			gotoEintrag: this.data.gotoEintrag,
			setFilter: this.data.setFilter,
			setzeDefaultSortierungSekII: this.data.setzeDefaultSortierungSekII,
		};
	}

	public getProps(to: RouteLocationNormalized): FaecherAppProps {
		return {
			fachListeManager: () => this.data.fachListeManager,
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
		};
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.fachListeManager.auswahlID() } });
		this.data.setView(node, this.children);
	}
}

export const routeSchuleFaecher = new RouteSchuleFaecher();
