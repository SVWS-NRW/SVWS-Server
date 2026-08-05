import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import type { RouteLocationNormalized, RouteParams } from "vue-router";
import { RouteManager } from "~/router/RouteManager";
import type { RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { type LehrerIndividualdatenGruppenprozesseProps } from "~/components/lehrer/individualdaten/LehrerIndividualdatenGruppenprozesseProps";

const LehrerIndividualdatenGruppenprozesse = () => import("~/components/lehrer/individualdaten/LehrerIndividualdatenGruppenprozesse.vue");

export class RouteLehrerIndividualdatenGruppenprozesse extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "lehrer.gruppenprozesse.daten", "gruppenprozesse/daten", LehrerIndividualdatenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.propHandler = (props) => this.getProps(props);
		super.mode = ServerMode.DEV;
		super.text = "Individualdaten";
		super.setCheckpoint = true;
	}

	protected async leaveBefore(from: RouteNode<any, any>, from_params: RouteParams): Promise<any> {
		routeLehrer.data.pendingStateManager.resetPendingState();
		routeLehrer.data.pendingStateManagerRegistry.removeAllPendingStateManager();
	}

	public getProps(_: RouteLocationNormalized): LehrerIndividualdatenGruppenprozesseProps {
		return {
			lehrerListeManager: () => routeLehrer.data.manager,
			autofocus: routeLehrer.data.autofocus,
			patchMultiple: () => routeLehrer.data.patchMultiple(this.data.pendingStateManager),
			pendingStateManager: () => routeLehrer.data.pendingStateManager,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}

}

export const routeLehrerIndividualdatenGruppenprozesse = new RouteLehrerIndividualdatenGruppenprozesse();

