import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

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

import type { SchuelerLernabschnitteProps } from "~/components/schueler/lernabschnitte/SSchuelerLernabschnitteProps";
import { routeSchuelerLernabschnittGostKlausuren } from "./RouteSchuelerLernabschnittGostKlausuren";
import { routeApp } from "../../RouteApp";
import type { TabData } from "@ui";

const SSchuelerLernabschnitte = () => import("~/components/schueler/lernabschnitte/SSchuelerLernabschnitte.vue");


export class RouteSchuelerLernabschnitte extends RouteNode<RouteDataSchuelerLernabschnitte, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN ], "schueler.lernabschnitt", "lernabschnitt/:abschnitt(\\d+)?/:wechselNr(\\d+)?", SSchuelerLernabschnitte, new RouteDataSchuelerLernabschnitte());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lernabschnitte";
		super.children = [
			routeSchuelerLernabschnittAllgemein,
			routeSchuelerLernabschnittLeistungen,
			routeSchuelerLernabschnittGostKlausuren,
			routeSchuelerLernabschnittVersetzungAbschluss,
			routeSchuelerLernabschnittKonferenz,
			routeSchuelerLernabschnittZeugnisdruck,
			routeSchuelerLernabschnittNachpruefung,
		];
		super.defaultChild = routeSchuelerLernabschnittLeistungen;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id, abschnitt: idSchuljahresabschnitt, wechselNr } = RouteNode.getIntParams(to_params, ["id", "abschnitt", "wechselNr"]);
			if (id === undefined)
				throw new DeveloperNotificationException("Fehler: Keine Schüler-ID in der URL angegeben.");
			await this.data.setSchueler(id, isEntering);
			if (idSchuljahresabschnitt !== undefined) {
				await routeSchuelerLernabschnitte.data.setLernabschnitt(idSchuljahresabschnitt, wechselNr ?? 0);
			}
			if ((to === this) && (this.data.hatAuswahl))
				return this.getChildRoute(id, this.data.auswahl.schuljahresabschnitt, this.data.auswahl.wechselNr);
			if (!to.name.startsWith(this.data.view.name))
				for (const child of this.children)
					if (to.name.startsWith(child.name))
						this.data.setView(child, this.children);
		} catch (e) {
			return routeError.getRoute(e as DeveloperNotificationException);
		}
	}

	public getChildRoute(id: number, abschnitt: number | undefined, wechselNr: number | undefined) : RouteLocationRaw {
		return { name: this.data.view.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getRoute(id: number, abschnitt: number | undefined, wechselNr: number | undefined) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnitteProps {
		return {
			lernabschnitt: routeSchuelerLernabschnitte.data.auswahl,
			lernabschnitte: routeSchuelerLernabschnitte.data.listAbschnitte,
			gotoLernabschnitt: routeSchuelerLernabschnitte.data.gotoLernabschnitt,
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
		};
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: {
			id: routeSchueler.data.schuelerListeManager.daten().id,
			abschnitt: this.data.auswahl.schuljahresabschnitt,
			wechselNr: this.data.auswahl.wechselNr
		} });
		this.data.setView(node, this.children);
	}

}

export const routeSchuelerLernabschnitte = new RouteSchuelerLernabschnitte();

