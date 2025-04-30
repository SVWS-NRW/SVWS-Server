import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchuelerAbiturLeistungsuebersicht } from "~/router/apps/schueler/abitur/RouteSchuelerAbiturLeistungsuebersicht";
import { RouteDataSchuelerAbitur } from "~/router/apps/schueler/abitur/RouteDataSchuelerAbitur";

import type { SchuelerAbiturProps } from "~/components/schueler/abitur/SchuelerAbiturProps";
import type { TabData } from "@ui";
import { schulformenGymOb } from "~/router/RouteHelper";
import { api } from "~/router/Api";

const SchuelerAbitur = () => import("~/components/schueler/abitur/SchuelerAbitur.vue");


export class RouteSchuelerAbitur extends RouteNode<RouteDataSchuelerAbitur, RouteSchueler> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
		], "schueler.abitur", "abitur", SchuelerAbitur, new RouteDataSchuelerAbitur());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Abitur";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
		super.children = [
			routeSchuelerAbiturLeistungsuebersicht,
		];
		super.defaultChild = routeSchuelerAbiturLeistungsuebersicht;
	}

	protected checkHidden(params?: RouteParams) {
		try {
			const { id } = (params !== undefined) ? RouteNode.getIntParams(params, ["id"]) : {id: undefined};
			if (!routeSchueler.data.manager.hasDaten())
				return false;
			const auswahl = routeSchueler.data.manager.auswahl();
			if (((auswahl.abiturjahrgang !== null) && routeSchueler.data.manager.abiturjahrgaenge.get(auswahl.abiturjahrgang))
				&& (api.benutzerHatKompetenz(BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN)
					|| (api.benutzerHatKompetenz(BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN) && api.benutzerKompetenzenAbiturjahrgaenge.has(auswahl.abiturjahrgang)))
				&& ((auswahl.jahrgang === 'Q1') || (auswahl.jahrgang === 'Q2')))
				return false;
			return routeSchueler.getRouteDefaultChild({ id });
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, [ "id" ]);
			if (id === undefined)
				throw new DeveloperNotificationException("Fehler: Keine Schüler-ID in der URL angegeben.");
			const schueler = routeSchueler.data.manager.liste.get(id);
			if (schueler === null)
				return routeSchueler.getRoute({ id });
			try {
				await this.data.setSchueler(schueler, isEntering);
			} catch(error) {
				return routeSchueler.getRoute({ id });
			}
			if (to === this)
				return this.getRouteView(this.data.view);
			if (!to.name.startsWith(this.data.view.name))
				for (const child of this.children)
					if (to.name.startsWith(child.name))
						this.data.setView(child, this.children);
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		this.data.reset();
	}

	public getProps(to: RouteLocationNormalized): SchuelerAbiturProps {
		return {
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
		};
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute(this.getRouteView(node));
		this.data.setView(node, this.children);
	}

}

export const routeSchuelerAbitur = new RouteSchuelerAbitur();

