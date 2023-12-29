import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchuelerLernabschnittAllgemein } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnittAllgemein";
import { routeSchuelerLernabschnittLeistungen } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnittLeistungen";
import { routeSchuelerLernabschnittVersetzungAbschluss } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnittVersetzungAbschluss";
import { routeSchuelerLernabschnittKonferenz } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnittKonferenz";
import { routeSchuelerLernabschnittZeugnisdruck } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnittZeugnisdruck";
import { routeSchuelerLernabschnittNachpruefung } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnittNachpruefung";
import { RouteDataSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteDataSchuelerLernabschnitte";

import type { SchuelerLernabschnitteProps, SchuelerLernabschnitteAuswahlChildData } from "~/components/schueler/lernabschnitte/SSchuelerLernabschnitteProps";
import { routeSchuelerLernabschnittGostKlausuren } from "./RouteSchuelerLernabschnittGostKlausuren";

const SSchuelerLernabschnitte = () => import("~/components/schueler/lernabschnitte/SSchuelerLernabschnitte.vue");


export class RouteSchuelerLernabschnitte extends RouteNode<RouteDataSchuelerLernabschnitte, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.lernabschnitt", "lernabschnitt/:abschnitt(\\d+)?/:wechselNr(\\d+)?", SSchuelerLernabschnitte, new RouteDataSchuelerLernabschnitte());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lernabschnitte";
		super.children = [
			routeSchuelerLernabschnittAllgemein,
			routeSchuelerLernabschnittLeistungen,
			routeSchuelerLernabschnittVersetzungAbschluss,
			routeSchuelerLernabschnittKonferenz,
			routeSchuelerLernabschnittZeugnisdruck,
			routeSchuelerLernabschnittNachpruefung,
			routeSchuelerLernabschnittGostKlausuren,
		];
		super.defaultChild = routeSchuelerLernabschnittLeistungen;
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array || to_params.abschnitt instanceof Array || to_params.wechselNr instanceof Array)
			return routeError.getRoute(new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		if (to_params.id === undefined)
			return routeError.getRoute(new Error("Fehler: Keine Schüler-ID in der URL angegeben."));
		const id = parseInt(to_params.id);
		if (this.data.idSchueler !== id)
			await this.data.setSchueler(id);
		if (to_params.abschnitt !== undefined) {
			const idSchuljahresabschnitt = parseInt(to_params.abschnitt);
			const wechselNr = (to_params.wechselNr === undefined) ? 0 : parseInt(to_params.wechselNr);
			await routeSchuelerLernabschnitte.data.setLernabschnitt(idSchuljahresabschnitt, wechselNr);
		}
		if ((to === this) && (this.data.hatAuswahl))
			return this.getChildRoute(id, this.data.auswahl.schuljahresabschnitt, this.data.auswahl.wechselNr);
	}

	public getChildRoute(id: number, abschnitt: number | undefined, wechselNr: number | undefined) : RouteLocationRaw {
		return { name: this.data.view.name, params: { id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getRoute(id: number, abschnitt: number | undefined, wechselNr: number | undefined) : RouteLocationRaw {
		return { name: this.name, params: { id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnitteProps {
		return {
			lernabschnitt: routeSchuelerLernabschnitte.data.auswahl,
			lernabschnitte: routeSchuelerLernabschnitte.data.listAbschnitte,
			gotoLernabschnitt: routeSchuelerLernabschnitte.data.gotoLernabschnitt,
			setChild: this.setChild,
			child: this.getChild(),
			children: this.getChildData(),
		};
	}

	private getChild(): SchuelerLernabschnitteAuswahlChildData {
		return this.data.view;
	}

	private getChildData(): SchuelerLernabschnitteAuswahlChildData[] {
		const result: SchuelerLernabschnitteAuswahlChildData[] = [];
		for (const c of this.children)
			result.push(c);
		return result;
	}

	private setChild = async (value: SchuelerLernabschnitteAuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: {
			id: routeSchueler.data.schuelerListeManager.daten().id,
			abschnitt: this.data.auswahl.schuljahresabschnitt,
			wechselNr: this.data.auswahl.wechselNr
		} });
		this.data.setView(node, this.children);
	}

}

export const routeSchuelerLernabschnitte = new RouteSchuelerLernabschnitte();

