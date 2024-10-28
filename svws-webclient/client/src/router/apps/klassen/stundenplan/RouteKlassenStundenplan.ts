import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { DeveloperNotificationException} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeKlassen, type RouteKlassen } from "~/router/apps/klassen/RouteKlassen";
import { routeKlassenStundenplanDaten } from "~/router/apps/klassen/stundenplan/RouteKlassenStundenplanDaten";
import { RouteDataKlassenStundenplan } from "~/router/apps/klassen/stundenplan/RouteDataKlassenStundenplan";

import { type StundenplanAuswahlProps } from "@comp";
import { ConfigElement } from "~/components/Config";
import { api } from "~/router/Api";
import { routeApp } from "../../RouteApp";

const SKlassenStundenplan = () => import("~/components/klassen/stundenplan/SKlassenStundenplan.vue");

export class RouteKlassenStundenplan extends RouteNode<RouteDataKlassenStundenplan, RouteKlassen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN ], "klassen.stundenplan", "stundenplan", SKlassenStundenplan, new RouteDataKlassenStundenplan());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
			routeKlassenStundenplanDaten
		];
		super.defaultChild = routeKlassenStundenplanDaten;
		api.config.addElements([
			new ConfigElement("klasse.stundenplan.ganzerStundenplan", "user", "true"),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			if (isEntering)
				await routeKlassenStundenplan.data.ladeListe();
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (id === undefined)
				return routeKlassen.getRoute(undefined);
			// Prüfe, ob diese Route als aktuelle View für die Tab-Bar gesetzt ist
			if (routeKlassen.data.view !== this)
				routeKlassen.data.setView(this, routeKlassen.children);
			// Prüfe, ob diese Route das Ziel ist. Wenn dies der fall ist, dann muss ggf. noch ein Stundenplan geladen werden
			if (to.name === this.name) {
			// Und wähle dann einen Eintrag aus der Stundenplanliste aus, wenn diese nicht leer ist
				if (routeKlassenStundenplan.data.mapStundenplaene.size !== 0) {
					const [idStundenplan] = routeKlassenStundenplan.data.mapStundenplaene.keys();
					return routeKlassenStundenplanDaten.getRoute(id, idStundenplan, 0);
				}
			}
		} catch (e) {
			return routeError.getRoute(e as DeveloperNotificationException);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		let redirect: RouteNode<any, any> = (this.selectedChild === undefined) ? this.defaultChild! : this.selectedChild;
		if (redirect.hidden({ idSchuljahresabschnitt: String(routeApp.data.idSchuljahresabschnitt), id: String(id) }) !== false)
			redirect = this.defaultChild!;
		return redirect.getRoute(id);
	}

	public getProps(to: RouteLocationNormalized): StundenplanAuswahlProps {
		return {
			stundenplan: this.data.mapStundenplaene.size === 0 ? undefined : this.data.auswahl,
			mapStundenplaene: this.data.mapStundenplaene,
			gotoStundenplan: this.data.gotoStundenplan,
			gotoWochentyp: this.data.gotoWochentyp,
			gotoKalenderwoche: this.data.gotoKalenderwoche,
			manager: () => this.data.manager,
			wochentyp: () => this.data.wochentyp,
			kalenderwoche: () => this.data.kalenderwoche,
			ganzerStundenplan: () => this.data.ganzerStundenplan,
			setGanzerStundenplan: this.data.setGanzerStundenplan,
		};
	}

}

export const routeKlassenStundenplan = new RouteKlassenStundenplan();

