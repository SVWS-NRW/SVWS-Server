import type { RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { AppMenuGroup } from "@ui/ui/nav/AppMenuGroup";
import { ViewType } from "@ui/ui/nav/ViewType";
import { ConfigElement } from "@ui/utils/Config";

import { Katalog } from "~/cache/Katalog";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchuelerAllgemeinesGruppenprozesse } from "~/router/apps/schueler/allgemeines/RouteSchuelerAllgemeinesGruppenprozesse";
import { routeSchuelerBetriebe } from "~/router/apps/schueler/betriebe/RouteSchuelerBetriebe";
import { routeSchuelerErziehungsberechtigte } from "~/router/apps/schueler/erziehungsberechtigte/RouteSchuelerErziehungsberechtigte";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";
import { routeSchuelerIndividualdatenGruppenprozesse } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdatenGruppenprozesse";
import { routeSchuelerKAoA } from "~/router/apps/schueler/kaoa/RouteSchuelerKAoA";
import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/laufbahnplanung/RouteSchuelerLaufbahnplanung";
import { routeSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";
import { routeSchuelerNeu } from "~/router/apps/schueler/neu/RouteSchuelerNeu";
import { routeSchuelerSchnelleingabe } from "~/router/apps/schueler/neu/RouteSchuelerSchnelleingabe";
import { RouteDataSchueler } from "~/router/apps/schueler/RouteDataSchueler";
import { routeSchuelerSchulbesuch } from "~/router/apps/schueler/schulbesuch/RouteSchuelerSchulbesuch";
import { routeSchuelerStundenplan } from "~/router/apps/schueler/stundenplan/RouteSchuelerStundenplan";
import { routeError } from "~/router/error/RouteError";
import { RouteNode } from "~/router/RouteNode";
import { RouteTabNode } from "~/router/RouteTabNode";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { beschaeftigungsartenStateImpl } from "~/states/kataloge/BeschaeftigungsartenStateImpl";
import { betriebeStateImpl } from "~/states/kataloge/BetriebeStateImpl";
import { entlassgruendeStateImpl } from "~/states/kataloge/EntlassgruendeStateImpl";
import { fahrschuelerartenStateImpl } from "~/states/kataloge/FahrschuelerartenStateImpl";
import { orteStateImpl } from "~/states/kataloge/OrteStateImpl";
import { useSchuelerAuswahlState } from "~/states/schueler/SchuelerAuswahlState";

import { routeSchuelerAbitur } from "./abitur/RouteSchuelerAbitur";
import { routeSchuelerSonstiges } from "./sonstiges/RouteSchuelerSonstiges";
import { routeSchuelerSprachen } from "./sprachen/RouteSchuelerSprachen";


const SSchuelerAuswahl = () => import("~/components/schueler/SSchuelerAuswahl.vue");
const SSchuelerApp = () => import("~/components/schueler/SSchuelerApp.vue");


export class RouteSchueler extends RouteTabNode<RouteDataSchueler, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "schueler", String.raw`schueler/:id(\d+)?`, SSchuelerApp, new RouteDataSchueler());
		super.setView("liste", SSchuelerAuswahl, (_route) => <any>{
			pendingStateManagerRegistry: () => this.data.pendingStateManagerRegistry,
		});
		super.mode = ServerMode.STABLE;
		super.text = "Schüler";
		super.children = [
			routeSchuelerIndividualdaten,
			routeSchuelerSonstiges,
			routeSchuelerErziehungsberechtigte,
			routeSchuelerBetriebe,
			routeSchuelerKAoA,
			routeSchuelerSchulbesuch,
			routeSchuelerLernabschnitte,
			routeSchuelerSprachen,
			routeSchuelerLaufbahnplanung,
			routeSchuelerAbitur,
			routeSchuelerStundenplan,
			routeSchuelerSchnelleingabe,
			routeSchuelerAllgemeinesGruppenprozesse,
			routeSchuelerIndividualdatenGruppenprozesse,
			routeSchuelerNeu,
		];
		super.defaultChild = routeSchuelerIndividualdaten;
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-group-line";
		configStateImpl.nonPersistentConfig.addElements([
			new ConfigElement(`${this.name}.auswahl.id`, "user", ""),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await Promise.all([orteStateImpl.init(), beschaeftigungsartenStateImpl.init(), betriebeStateImpl.init(),
				entlassgruendeStateImpl.init(), fahrschuelerartenStateImpl.init(),
				routeApp.cache.refreshKataloge(Katalog.ERZIEHERARTEN, Katalog.FOERDERSCHWERPUNKTE, Katalog.HALTESTELLEN, Katalog.KINDERGAERTEN, Katalog.JAHRGAENGE,
					Katalog.MERKMALE, Katalog.RELIGIONEN, Katalog.SCHULEN, Katalog.TELEFONARTEN, Katalog.VERMERKARTEN)]);
		}
		try {
			const { idSchuljahresabschnitt, id: paramId } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id"]);
			if (idSchuljahresabschnitt === undefined) {
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
			}
			let id = paramId;
			if ((paramId === undefined) && isEntering) {
				const lastId = Number.parseInt(configStateImpl.nonPersistentConfig.getValue(`${this.name}.auswahl.id`));
				if (!Number.isNaN(lastId)) {
					id = lastId;
				}
			}
			if (isEntering && to.hasOneOfTypes([ViewType.GRUPPENPROZESSE, ViewType.HINZUFUEGEN, ViewType.NEU])) {
				return this.getRouteView(this.data.view, { id: id ?? '' });
			}
			// Daten zum ausgewählten Schuljahresabschnitt und Schüler laden
			const schuelerAuswahlState = useSchuelerAuswahlState();
			const idNeu = await schuelerAuswahlState.init(idSchuljahresabschnitt, isEntering);
			if ((idNeu !== null) && (idNeu !== id)) {
				return this.data.defaultView.getRoute({ id: idNeu });
			}

			// Wenn einer der folgenden Routen Types aufgerufen wird, wird hier ein Redirect initiiert, sobald eine ID in der URL enthalten ist.
			if (to.hasOneOfTypes([ViewType.GRUPPENPROZESSE, ViewType.HINZUFUEGEN]) && (id !== undefined)) {
				return this.getRouteView(to, { id: '' });
			}

			if (to.hasType(ViewType.GRUPPENPROZESSE)) {
				await schuelerAuswahlState.gotoGruppenprozessView(false);
			} else if (to.hasType(ViewType.HINZUFUEGEN)) {
				await schuelerAuswahlState.gotoHinzufuegenView(false);
			} else if (to.hasType(ViewType.NEU)) {
				await schuelerAuswahlState.gotoSchnelleingabeView(false);
			} else {
				await schuelerAuswahlState.gotoDefaultView(id);
			}

			if (to.name === this.name) {
				return await this.doUpdateIfTarget(from);
			}
			if (!to.name.startsWith(this.data.view.name)) {
				for (const child of this.children) {
					if (to.name.startsWith(child.name)) {
						this.data.setView(child, this.children);
					}
				}
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		const schuelerAuswahlState = useSchuelerAuswahlState();
		if (schuelerAuswahlState.activeViewType !== ViewType.DEFAULT) {
			this._selectedChild.value = undefined;
		}
		this.data.reset();
		const { id } = RouteNode.getStringParams(from_params, ['id']);
		await configStateImpl.nonPersistentConfig.setValue(`${this.name}.auswahl.id`, id ?? "");
	}


	/**
	 * Fügt die ID zu der Route hinzu. Diese Methode kann überschrieben werden, wenn neben
	 * der ID noch weitere Parameter benötigt werden.
	 *
	 * @returns die Routing-Parameter mit der ID.
	 */
	public addRouteParamsFromState(): RouteParamsRawGeneric {
		const params = <RouteParamsRawGeneric>{};
		const schuelerAuswahlState = useSchuelerAuswahlState();
		if (!schuelerAuswahlState.isAvailable) {
			return params;
		}
		const id = schuelerAuswahlState.manager.auswahlID();
		if (id !== null) {
			params.id = id;
		}
		return params;
	}


	protected doUpdateIfTarget = async (from: RouteNode<any, any> | undefined) => {
		const schuelerAuswahlState = useSchuelerAuswahlState();
		if (!schuelerAuswahlState.manager.hasDaten()) {
			return;
		}
		if ((from !== undefined) && (/(\.|^)stundenplan/).test(from.name)) {
			return this.getRouteView(routeSchuelerStundenplan);
		}
		return this.getRouteSelectedChild();
	};

}

export const routeSchueler = new RouteSchueler();
