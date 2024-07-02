import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeKatalogRaeume, type RouteKatalogRaeume } from "~/router/apps/kataloge/raum/RouteKatalogRaeume";
import { routeRaumStundenplanDaten } from "~/router/apps/kataloge/raum/stundenplan/RouteRaumStundenplanDaten";
import { RouteDataRaumStundenplan } from "~/router/apps/kataloge/raum/stundenplan/RouteDataRaumStundenplan";

import type { StundenplanAuswahlProps } from "@comp";
import { ConfigElement } from "~/components/Config";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";

const SRaumStundenplan = () => import("~/components/kataloge/raeume/stundenplan/SRaumStundenplan.vue");

export class RouteRaumStundenplan extends RouteNode<RouteDataRaumStundenplan, RouteKatalogRaeume> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.raeume.stundenplan", "stundenplan", SRaumStundenplan, new RouteDataRaumStundenplan());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
			routeRaumStundenplanDaten,
		];
		super.defaultChild = routeRaumStundenplanDaten;
		api.config.addElements([
			new ConfigElement("kataloge.raeume.stundenplan.ganzerStundenplan", "user", "true"),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await routeRaumStundenplan.data.ladeListe();
		if (to_params.id instanceof Array || to_params.idStundenplan instanceof Array)
			return routeError.getRoute(new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		// Prüfe, ob ein Lehrer ausgewählt ist. Wenn nicht dann wechsele in die Lehrer-Route zurück.
		const idRaum = to_params.id === undefined ? undefined : parseInt(to_params.id);
		if (idRaum === undefined)
			return routeKatalogRaeume.getRoute(undefined);
		// Prüfe, ob diese Route das Ziel ist. Wenn dies der fall ist, dann muss ggf. noch ein Stundenplan geladen werden
		if (to.name === this.name) {
			// Und wähle dann einen Eintrag aus der Stundenplanliste aus, wenn diese nicht leer ist
			if (routeRaumStundenplan.data.mapStundenplaene.size !== 0) {
				const stundenplan = routeRaumStundenplan.data.mapStundenplaene.entries().next().value;
				if (stundenplan !== undefined)
					return routeRaumStundenplanDaten.getRoute(idRaum, stundenplan.id, 0);
			}
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id, wochentyp: 0 }};
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

export const routeRaumStundenplan = new RouteRaumStundenplan();

