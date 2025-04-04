import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import type { KlassenStundenplanProps } from "~/components/klassen/stundenplan/SKlassenStundenplanProps";
import { DeveloperNotificationException} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeKlassen, type RouteKlassen } from "~/router/apps/klassen/RouteKlassen";
import { RouteDataKlassenStundenplan } from "~/router/apps/klassen/stundenplan/RouteDataKlassenStundenplan";
import { ConfigElement } from "../../../../../../ui/src/utils/Config";
import { api } from "~/router/Api";

const SKlassenStundenplan = () => import("~/components/klassen/stundenplan/SKlassenStundenplan.vue");

export class RouteKlassenStundenplan extends RouteNode<RouteDataKlassenStundenplan, RouteKlassen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN ], "klassen.stundenplan", "stundenplan/:idStundenplan(\\d+)?/:wochentyp(\\d+)?/:kw(\\d+\\.\\d+)?", SKlassenStundenplan, new RouteDataKlassenStundenplan());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [];
		api.config.addElements([
			new ConfigElement("klasse.stundenplan.ganzerStundenplan", "user", "true"),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id: idKlasse, idStundenplan, wochentyp } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id", "idStundenplan", "wochentyp"]);
			const { kw: kwString } = RouteNode.getStringParams(to_params, ["kw"]);
			if (idSchuljahresabschnitt === undefined)
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
			let kwjahr = undefined;
			let kw = undefined;
			if ((kwString !== undefined) && (kwString !== "") && (wochentyp === undefined)) {
				const tmpKW = kwString.split(".");
				if (tmpKW.length !== 2)
					throw new DeveloperNotificationException("Die Angabe der Kalenderwoche muss die Form 'Jahr.KW' haben.");
				kwjahr = parseInt(tmpKW[0]);
				kw = parseInt(tmpKW[1]);
			}
			// Prüfe, ob ein Schüler ausgewählt ist. Wenn nicht dann wechsele in die Schüler-Route zurück.
			if (idKlasse === undefined)
				return routeKlassen.getRoute();
			// Lade die Stundenplandaten neu, wenn die ID des Schuljahresabschnittes sich ändert (das passiert beim Laden der Route automatisch)
			if (await this.data.ladeListe(idKlasse))
				return this.getRoute();
			// Aktualisiere / Lade ggf. den Stundenplan ...
			if (idStundenplan !== undefined)
				await routeKlassenStundenplan.data.setEintrag(idKlasse, idStundenplan, wochentyp ?? 0, kwjahr, kw);
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return {
			idStundenplan: (this.data.hatAuswahl === true) ? this.data.auswahl.id : undefined,
			wochentyp: this.data.wochentyp,
			kw: (this.data.kalenderwoche === undefined) ? undefined : this.data.kalenderwoche.jahr + "." + this.data.kalenderwoche.kw,
		};
	}

	public getProps(to: RouteLocationNormalized): KlassenStundenplanProps {
		return {
			apiStatus: api.status,
			getPDF: this.data.getPDF,
			id: routeKlassen.data.manager.daten().id,
			ignoreEmpty: this.data.ganzerStundenplan,
			stundenplan: () => (this.data.mapStundenplaene.size === 0) ? undefined : this.data.auswahl,
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

