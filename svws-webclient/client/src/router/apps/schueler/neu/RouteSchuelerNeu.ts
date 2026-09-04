import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import type { RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import type { SchuelerNeuProps } from "~/components/schueler/neuanlage/SchuelerNeuProps";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerNeu } from "~/router/apps/schueler/neu/RouteDataSchuelerNeu";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

const SchuelerNeu = () => import("~/components/schueler/neuanlage/SchuelerNeu.vue");

export class RouteSchuelerNeu extends RouteNode<RouteDataSchuelerNeu, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN], "schueler.neu", "neu", SchuelerNeu, new RouteDataSchuelerNeu());
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schüler Neu";
		super.setCheckpoint = true;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeDaten();
	}

	public getProps(to: RouteLocationNormalized): SchuelerNeuProps {
		return {
			manager: () => this.data.manager,
			gotoDefaultView: routeSchueler.data.gotoDefaultView,
			add: routeSchueler.data.add,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
			gotToSchnelleingabe: this.data.goToSchnelleingabe,
		};
	}

}

export const routeSchuelerNeu = new RouteSchuelerNeu();
