import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import { DeveloperNotificationException} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchuleFaecher, type RouteSchuleFaecher } from "~/router/apps/schule/schulbezogen/faecher/RouteSchuleFaecher";
import { RouteDataFachStundenplan } from "~/router/apps/schule/schulbezogen/faecher/stundenplan/RouteDataFachStundenplan";
import { api } from "~/router/Api";
import type { FachStundenplanProps } from "~/components/schule/schulbezogen/faecher/stundenplan/SFachStundenplanProps";
import { ConfigElement } from "@ui";

const SFachStundenplan = () => import("~/components/schule/schulbezogen/faecher/stundenplan/SFachStundenplan.vue");

export class RouteFachStundenplan extends RouteNode<RouteDataFachStundenplan, RouteSchuleFaecher> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN ], "schule.faecher.stundenplan", "stundenplan/:idStundenplan(\\d+)?/:wochentyp(\\d+)?/:kw(\\d+\\.\\d+)?", SFachStundenplan, new RouteDataFachStundenplan());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [];
		api.config.addElements([
			new ConfigElement("schule.faecher.stundenplan.ganzerStundenplan", "user", "true"),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id: idFach, idStundenplan, wochentyp } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id", "idStundenplan", "wochentyp"]);
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
			if (idFach === undefined)
				return routeSchuleFaecher.getRoute();
			// Lade die Stundenplandaten neu, wenn die ID des Schuljahresabschnittes sich ändert (das passiert beim Laden der Route automatisch)
			if (await this.data.ladeListe())
				return this.getRoute();
			// Setze den Stundenplan ...
			await routeFachStundenplan.data.setEintrag(idFach, idStundenplan, wochentyp ?? 0, kwjahr, kw);
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		this.data.reset();
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return {
			idStundenplan: this.data.hatAuswahl ? this.data.auswahl.id : undefined,
			wochentyp: this.data.wochentyp,
			kw: (this.data.kalenderwoche === undefined) ? undefined : this.data.kalenderwoche.jahr + "." + this.data.kalenderwoche.kw,
		};
	}

	public getProps(to: RouteLocationNormalized): FachStundenplanProps {
		return {
			apiStatus: api.status,
			getPDF: this.data.getPDF,
			id: routeSchuleFaecher.data.manager.daten().id,
			ignoreEmpty: this.data.ganzerStundenplan,
			stundenplan: () => (this.data.mapStundenplaene.size === 0 || !this.data.hasManager) ? undefined : this.data.auswahl,
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

