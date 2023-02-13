import { routeSchule } from "~/router/apps/RouteSchule";
import { routeSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";
import { routeSchuleBenutzergruppe } from "~/router/apps/RouteSchuleBenutzergruppe";
import { routeKataloge } from "~/router/apps/RouteKataloge";
import { routeKatalogFaecher } from "~/router/apps/RouteKatalogFaecher";
import { routeKatalogReligion } from "~/router/apps/RouteKatalogReligion";
import { routeKatalogJahrgaenge } from "~/router/apps/RouteKatalogJahrgaenge";
import { routeKatalogFoerderschwerpunkte } from "./apps/RouteKatalogFoerderschwerpunkte";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { routeLehrer } from "~/router/apps/RouteLehrer";
import { routeKlassen } from "~/router/apps/RouteKlassen";
import { routeKurse } from "~/router/apps/RouteKurse";
import { routeGost } from "~/router/apps/RouteGost";
import { routeStatistik } from "~/router/apps/RouteStatistik";
import { RouteNode } from "~/router/RouteNode";

import SApp from "~/components/SApp.vue";
import type { RouteLocationRaw, RouteParams } from "vue-router";
import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
import { type List, OrtKatalogEintrag, type OrtsteilKatalogEintrag, Vector, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";
import { routeLogin } from "./RouteLogin";
import { ApiLoadingStatus } from "~/apps/core/ApiLoadingStatus.class";
import { BaseList } from "~/apps/BaseList";
import { computed, Ref, ref, WritableComputedRef } from "vue";
import { UserConfigKeys } from "~/utils/userconfig/keys";

export class RouteDataApp {
	schule: DataSchuleStammdaten = new DataSchuleStammdaten();
	orte: List<OrtKatalogEintrag> = new Vector();
	mapOrte: Map<number, OrtKatalogEintrag> = new Map();
	ortsteile: List<OrtsteilKatalogEintrag> = new Vector();
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag> = new Map();
	apiLoadingStatus: ApiLoadingStatus = new ApiLoadingStatus();

	public user_config: Ref<Map<keyof UserConfigKeys, UserConfigKeys[keyof UserConfigKeys]>> = ref(new Map());
	public drag_and_drop_data: Ref<any> = ref(undefined);

	aktAbschnitt: WritableComputedRef<Schuljahresabschnitt> = computed({

		/**
	 * Der aktuell ausgewählte Abschnitt
	 *
	 * @returns {Schuljahresabschnitt | undefined}
	 */
		get: () => {
			let abschnitt = this.user_config.value.get('app.akt_abschnitt') as Schuljahresabschnitt | undefined;
			if (abschnitt === undefined && this.schule.daten !== undefined) {
				const id = this.schule.daten.idSchuljahresabschnitt;
				for (const a of this.schule.daten.abschnitte) {
					if (a.id === id) {
						this.user_config.value.set('app.akt_abschnitt', a);
						abschnitt = a;
						break;
					}
				}
			}
			if (abschnitt === undefined)
				throw new Error("Es fehlt ein aktueller Abschnitt in den Schuldaten");
			return abschnitt;
		},
		/**
	 * Setzt den aktuellen Schuljahresabschnitt
	 *
	 * @param {Schuljahresabschnitt} abschnitt
	 */
		set: (abschnitt: Schuljahresabschnitt) => {
			this.user_config.value.set('app.akt_abschnitt', abschnitt);
			// TODO was tun, wenn das akt Halbjahr neu gesetzt wurde?
			// const lists = BaseList.all
			// for (const l of lists) {
			// 	void l.update_list()
			// }
		}
	})

	public setAbschnitt(abschnitt: Schuljahresabschnitt) {
		this.aktAbschnitt.value = abschnitt;
	}
}
export class RouteApp extends RouteNode<RouteDataApp, any> {

	public constructor() {
		super("app", "/", SApp, new RouteDataApp());
		super.propHandler = (route) => this.getProps();
		super.text = "SVWS-Client";
		super.children = [
			routeSchule,
			routeSchuleBenutzer,
			routeSchuleBenutzergruppe,
			routeKataloge,
			routeKatalogFaecher,
			routeKatalogReligion,
			routeKatalogJahrgaenge,
			routeKatalogFoerderschwerpunkte,
			routeSchueler,
			routeLehrer,
			routeKlassen,
			routeKurse,
			routeGost,
			routeStatistik
		];
		super.menu = [
			routeSchule,
			routeKataloge,
			routeSchueler,
			routeLehrer,
			routeKlassen,
			routeKurse,
			routeGost,
			routeStatistik
		];
		super.defaultChild = routeSchueler;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<any> {
		return this.getRoute();
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.data.schule.select(true);    // TODO Kein Data-Objekt, sondern Handhabung des aktuellen Abschnitts, etc. in dieser Route (RouteDataApp-Objekt)
		// Lade den Katalog der Orte
		this.data.orte = await routeLogin.data.api.getOrte(routeLogin.data.schema);
		this.data.mapOrte = new Map();
		for (const o of this.data.orte)
			this.data.mapOrte.set(o.id, o);
		// Lade den Katalog der Ortsteile
		this.data.ortsteile = await routeLogin.data.api.getOrtsteile(routeLogin.data.schema);
		this.data.mapOrtsteile = new Map();
		for (const o of this.data.ortsteile)
			this.data.mapOrtsteile.set(o.id, o);
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		await this.data.schule.unselect();
		this.data.orte = new Vector<OrtKatalogEintrag>();
		this.data.mapOrte = new Map();
		this.data.ortsteile = new Vector<OrtsteilKatalogEintrag>();
		this.data.mapOrtsteile = new Map();
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.defaultChild!.name };
	}

	public getProps(): Record<string, any> {
		return {
			username: routeLogin.data.username,
			schule: this.data.schule,
			orte: this.data.orte,
			ortsteile: this.data.ortsteile,
			aktAbschnitt: this.data.aktAbschnitt,
		};
	}
}

export const routeApp = new RouteApp();
