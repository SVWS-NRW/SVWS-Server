import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { BenutzerprofilAuswahlProps } from "~/components/benutzerprofil/SBenutzerprofilAuswahlProps";
import type { BenutzerprofilAppProps } from "~/components/benutzerprofil/SBenutzerprofilAppProps";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { RouteApp} from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { RouteDataBenutzerprofil } from "~/router/apps/benutzerprofil/RouteDataBenutzerprofil";
import { routeBenutzerprofilDaten } from "./daten/RouteBenutzerprofilDaten";
import { routeBenutzerprofilConfig } from "./config/RouteBenutzerprofilConfig";

const SBenutzerprofilAuswahl = () => import("~/components/benutzerprofil/SBenutzerprofilAuswahl.vue")
const SBenutzerprofilApp = () => import("~/components/benutzerprofil/SBenutzerprofilApp.vue")

export class RouteBenutzerprofil extends RouteNode<RouteDataBenutzerprofil, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "benutzerprofil", "/benutzerprofil", SBenutzerprofilApp, new RouteDataBenutzerprofil());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzerprofil";
		super.setView("liste", SBenutzerprofilAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeBenutzerprofilDaten,
			routeBenutzerprofilConfig,
		];
		super.menu = [
			routeBenutzerprofilDaten,
			routeBenutzerprofilConfig,
		];
		super.defaultChild = routeBenutzerprofilDaten;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.defaultChild!.name };
	}

	public getAuswahlProps(to: RouteLocationNormalized): BenutzerprofilAuswahlProps {
		return {
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			setChild: this.setChild,
			child: this.getChild(),
			children: this.getChildData(),
			childrenHidden: this.children_hidden().value,
		};
	}

	public getProps(to: RouteLocationNormalized): BenutzerprofilAppProps {
		return {
			benutzer: this.data.benutzer,
			// Props für die Navigation
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsHidden: this.children_hidden().value,
		};
	}

	private getChild(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getChildData(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of this.menu)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setChild = async (value: AuswahlChildData) => {
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: {} });
		await this.data.setView(node);
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

export const routeBenutzerprofil = new RouteBenutzerprofil();
