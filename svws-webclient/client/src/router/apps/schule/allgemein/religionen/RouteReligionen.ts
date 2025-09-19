import type { RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { ReligionenListeManager } from "@ui";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeReligionenDaten } from "~/router/apps/schule/allgemein/religionen/RouteReligionenDaten";
import { RouteDataReligionen } from "./RouteDataReligionen";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeReligionenGruppenprozesse } from "~/router/apps/schule/allgemein/religionen/RouteReligionenGruppenprozesse";
import { routeReligionenNeu } from "~/router/apps/schule/allgemein/religionen/RouteReligionenNeu";
import { routeApp} from "~/router/apps/RouteApp";
import type { ReligionenAuswahlProps } from "~/components/schule/allgemein/religionen/SReligionenAuswahlPops";

const SReligionenAuswahl = () => import("~/components/schule/allgemein/religionen/SReligionenAuswahl.vue");
const SReligionenApp = () => import("~/components/schule/allgemein/religionen/SReligionenApp.vue");

export class RouteReligionen extends RouteAuswahlNode<ReligionenListeManager, RouteDataReligionen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.religionen",
			"schule/religion/:id(\\d+)?", SReligionenApp, SReligionenAuswahl, new RouteDataReligionen());
		super.mode = ServerMode.STABLE;
		super.text = "Religionen";
		super.menugroup = RouteSchuleMenuGroup.ALLGEMEIN;
		super.children = [
			routeReligionenDaten,
			routeReligionenGruppenprozesse,
			routeReligionenNeu,
		];
		super.defaultChild = routeReligionenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
		super.getAuswahlListProps = (props) => (<ReligionenAuswahlProps> {
			...props,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
		})
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeReligionen = new RouteReligionen();
