import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { AuswahlChildData } from "~/components/AuswahlChildData";
import { SchuelerAppProps } from "~/components/schueler/SSchuelerAppProps";
import { SchuelerAuswahlProps } from "~/components/schueler/SSchuelerAuswahlProps";
import { routeSchuelerAbschnitt } from "~/router/apps/schueler/RouteSchuelerAbschnitt";
import { routeSchuelerAdressen } from "~/router/apps/schueler/RouteSchuelerAdressen";
import { routeSchuelerErziehungsberechtigte } from "~/router/apps/schueler/RouteSchuelerErziehungsberechtigte";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/RouteSchuelerIndividualdaten";
import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/RouteSchuelerLaufbahnplanung";
import { routeSchuelerLeistungen } from "~/router/apps/schueler/RouteSchuelerLeistungen";
import { routeSchuelerSchulbesuch } from "~/router/apps/schueler/RouteSchuelerSchulbesuch";
import { routeSchuelerStundenplan } from "~/router/apps/schueler/RouteSchuelerStundenplan";
import { routeApp, RouteApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";
import { RouteDataSchueler } from "./schueler/RouteDataSchueler";

const SSchuelerAuswahl = () => import("~/components/schueler/SSchuelerAuswahl.vue")
const SSchuelerApp = () => import("~/components/schueler/SSchuelerApp.vue")

export class RouteSchueler extends RouteNode<RouteDataSchueler, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler", "/schueler/:id(\\d+)?", SSchuelerApp, new RouteDataSchueler());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schüler";
		super.setView("liste", SSchuelerAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeSchuelerIndividualdaten,
			routeSchuelerErziehungsberechtigte,
			routeSchuelerAdressen,
			routeSchuelerSchulbesuch,
			routeSchuelerAbschnitt,
			routeSchuelerLeistungen,
			routeSchuelerLaufbahnplanung,
			routeSchuelerStundenplan
		];
		super.defaultChild = routeSchuelerIndividualdaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.data.setSchuljahresabschnitt(routeApp.data.aktAbschnitt.value.id);
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		const eintrag = (id !== undefined) ? this.data.mapSchueler.get(id) : undefined;
		await this.data.setSchueler(eintrag);
		if (!this.data.hatStammdaten) {
			if (to.name === this.name)
				return;
			return this.getRoute(undefined);
		}
		if (to.name === this.name)
			return this.getChildRoute(this.data.stammdaten.id);
		if (!to.name.startsWith(this.data.view.name))
			for (const child of this.children)
				if (to.name.startsWith(child.name))
					await this.data.setView(child);
	}


	public getRoute(id?: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getChildRoute(id: number | undefined) : RouteLocationRaw {
		const redirect_name: string = (routeSchueler.selectedChild === undefined) ? routeSchuelerIndividualdaten.name : routeSchueler.selectedChild.name;
		return { name: redirect_name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchuelerAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			auswahlGruppe: this.data.auswahlGruppe,
			filter: this.data.filter,
			mapSchueler: this.data.mapSchueler,
			mapKlassen: this.data.mapKlassen,
			mapJahrgaenge: this.data.mapJahrgaenge,
			mapKurse: this.data.mapKurse,
			schulgliederungen: api.schulgliederungen,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoSchueler: this.data.gotoSchueler,
			setFilter: this.data.setFilter,
			setAuswahlGruppe: this.data.setAuswahlGruppe,
		};
	}

	public getProps(to: RouteLocationNormalized): SchuelerAppProps {
		return {
			auswahl: this.data.auswahl,
			stammdaten: this.data.auswahl === undefined ? undefined : this.data.stammdaten,
			mapKlassen: this.data.mapKlassen,
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

	private getTabs(): AuswahlChildData[] {		const result: AuswahlChildData[] = [];
		for (const c of this.children)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setTab = async (value: AuswahlChildData) => {		if (value.name === this.data.view.name)
		return;
	const node = RouteNode.getNodeByName(value.name);
	if (node === undefined)
		throw new Error("Unbekannte Route");
	await RouteManager.doRoute({ name: value.name, params: { id: this.data.auswahl?.id } });
	await this.data.setView(node);
	}

}

export const routeSchueler = new RouteSchueler();
