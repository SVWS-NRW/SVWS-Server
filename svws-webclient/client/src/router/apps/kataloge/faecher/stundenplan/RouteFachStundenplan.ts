import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeKatalogFaecher, type RouteKatalogFaecher } from "~/router/apps/kataloge/faecher/RouteKatalogFaecher";
import { routeFachStundenplanDaten } from "~/router/apps/kataloge/faecher/stundenplan/RouteFachStundenplanDaten";
import { RouteDataFachStundenplan } from "~/router/apps/kataloge/faecher/stundenplan/RouteDataFachStundenplan";

import type { StundenplanAuswahlProps } from "@comp";
import { ConfigElement } from "~/components/Config";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";

const SFachStundenplan = () => import("~/components/kataloge/faecher/stundenplan/SFachStundenplan.vue");

export class RouteFachStundenplan extends RouteNode<RouteDataFachStundenplan, RouteKatalogFaecher> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.faecher.stundenplan", "stundenplan", SFachStundenplan, new RouteDataFachStundenplan());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
			routeFachStundenplanDaten,
		];
		super.defaultChild = routeFachStundenplanDaten;
		api.config.addElements([
			new ConfigElement("kataloge.faecher.stundenplan.ganzerStundenplan", "user", "true"),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await routeFachStundenplan.data.ladeListe();
		if (to_params.id instanceof Array || to_params.idStundenplan instanceof Array)
			return routeError.getRoute(new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		// Prüfe, ob ein Lehrer ausgewählt ist. Wenn nicht dann wechsele in die Lehrer-Route zurück.
		const idFach = to_params.id === undefined ? undefined : parseInt(to_params.id);
		if (idFach === undefined)
			return routeKatalogFaecher.getRoute(undefined);
		// Prüfe, ob diese Route das Ziel ist. Wenn dies der fall ist, dann muss ggf. noch ein Stundenplan geladen werden
		if (to.name === this.name) {
			// Und wähle dann einen Eintrag aus der Stundenplanliste aus, wenn diese nicht leer ist
			if (routeFachStundenplan.data.mapStundenplaene.size !== 0) {
				const stundenplan = routeFachStundenplan.data.mapStundenplaene.entries().next().value;
				if (stundenplan !== undefined)
					return routeFachStundenplanDaten.getRoute(idFach, stundenplan.id, 0);
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

export const routeFachStundenplan = new RouteFachStundenplan();

