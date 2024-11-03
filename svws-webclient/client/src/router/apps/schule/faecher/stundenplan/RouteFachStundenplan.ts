import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import type { DeveloperNotificationException} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchuleFaecher, type RouteSchuleFaecher } from "~/router/apps/schule/faecher/RouteSchuleFaecher";
import { routeFachStundenplanDaten } from "~/router/apps/schule/faecher/stundenplan/RouteFachStundenplanDaten";
import { RouteDataFachStundenplan } from "~/router/apps/schule/faecher/stundenplan/RouteDataFachStundenplan";

import type { StundenplanAuswahlProps } from "@comp";
import { ConfigElement } from "~/components/Config";
import { api } from "~/router/Api";

const SFachStundenplan = () => import("~/components/schule/faecher/stundenplan/SFachStundenplan.vue");

export class RouteFachStundenplan extends RouteNode<RouteDataFachStundenplan, RouteSchuleFaecher> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN ], "schule.faecher.stundenplan", "stundenplan", SFachStundenplan, new RouteDataFachStundenplan());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
			routeFachStundenplanDaten,
		];
		super.defaultChild = routeFachStundenplanDaten;
		api.config.addElements([
			new ConfigElement("schule.faecher.stundenplan.ganzerStundenplan", "user", "true"),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (isEntering)
				await routeFachStundenplan.data.ladeListe();
			// Prüfe, ob ein Fach ausgewählt ist. Wenn nicht dann wechsele in die Fach-Route zurück.
			if (id === undefined)
				return routeSchuleFaecher.getRoute();
			// Prüfe, ob diese Route das Ziel ist. Wenn dies der fall ist, dann muss ggf. noch ein Stundenplan geladen werden
			if (to.name === this.name)
				// Und wähle dann einen Eintrag aus der Stundenplanliste aus, wenn diese nicht leer ist
				if (routeFachStundenplan.data.mapStundenplaene.size !== 0) {
					const [idStundenplan] = routeFachStundenplan.data.mapStundenplaene.keys();
					return routeFachStundenplanDaten.getRoute({ id, idStundenplan, wochentyp: 0 });
				}
		} catch (error) {
			return routeError.getErrorRoute(error as DeveloperNotificationException);
		}
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return {
			idStundenplan: (this.data.hatAuswahl === true) ? this.data.auswahl.id : undefined,
			wochentyp: this.data.wochentyp,
			kw: (this.data.kalenderwoche === undefined) ? undefined : this.data.kalenderwoche.jahr + "." + this.data.kalenderwoche.kw,
		};
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

