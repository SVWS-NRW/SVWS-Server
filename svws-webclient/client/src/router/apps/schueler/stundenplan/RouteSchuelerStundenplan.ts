import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { StundenplanAuswahlProps } from "@comp";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchuelerStundenplanDaten } from "~/router/apps/schueler/stundenplan/RouteSchuelerStundenplanDaten";
import { RouteDataSchuelerStundenplan } from "~/router/apps/schueler/stundenplan/RouteDataSchuelerStundenplan";
import { ConfigElement } from "~/components/Config";
import { api } from "~/router/Api";
import { routeApp } from "../../RouteApp";


const SSchuelerStundenplan = () => import("~/components/schueler/stundenplan/SSchuelerStundenplan.vue");

export class RouteSchuelerStundenplan extends RouteNode<RouteDataSchuelerStundenplan, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN ], "schueler.stundenplan", "stundenplan", SSchuelerStundenplan, new RouteDataSchuelerStundenplan());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
			routeSchuelerStundenplanDaten
		];
		super.defaultChild = routeSchuelerStundenplanDaten;
		api.config.addElements([
			new ConfigElement("schueler.stundenplan.ganzerStundenplan", "user", "true"),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await routeSchuelerStundenplan.data.ladeListe();
		if (to_params.id instanceof Array || to_params.idStundenplan instanceof Array)
			return routeError.getRoute(new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		// Prüfe, ob ein Schüler ausgewählt ist. Wenn nicht dann wechsele in die Schüler-Route zurück.
		const idSchueler = to_params.id === undefined ? undefined : parseInt(to_params.id);
		if (idSchueler === undefined)
			return routeSchueler.getRoute();
		// Prüfe, ob diese Route das Ziel ist. Wenn dies der fall ist, dann muss ggf. noch ein Stundenplan geladen werden
		if (to.name === this.name) {
			// Und wähle dann einen Eintrag aus der Stundenplanliste aus, wenn diese nicht leer ist
			if (routeSchuelerStundenplan.data.mapStundenplaene.size !== 0) {
				const stundenplan = routeSchuelerStundenplan.data.mapStundenplaene.entries().next().value;
				if (stundenplan !== undefined)
					return routeSchuelerStundenplanDaten.getRoute(idSchueler, stundenplan.id, 0);
			}
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: id, wochentyp: 0 }};
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

export const routeSchuelerStundenplan = new RouteSchuelerStundenplan();

