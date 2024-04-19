import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";

import { routeSchema } from "../schema/RouteSchema";
import { RouteDataSchema } from "~/router/apps/schema/RouteDataSchema";

import type { SchemagruppeProps } from "~/components/schemagruppe/SSchemagruppeProps";
import type { SchemaAuswahlProps } from "~/components/schema/SSchemaAuswahlProps";
import { api } from "~/router/Api";

const SSchemaAuswahl = () => import("~/components/schema/SSchemaAuswahl.vue")
const SSchemagruppe = () => import("~/components/schemagruppe/SSchemagruppe.vue")


export class RouteSchemagruppe extends RouteNode<RouteDataSchema, RouteApp> {

	public constructor() {
		super("schemagruppe", "/schemagruppe", SSchemagruppe, new RouteDataSchema());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schemagruppe";
		super.setView("liste", SSchemaAuswahl, (route) => this.getAuswahlProps(route));
	}

	protected async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeSchema.data.auswahlGruppe.length === 0)
			return routeSchema.getRoute();
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name };
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchemaAuswahlProps {
		return {
			hasRootPrivileges: api.hasRootPrivileges,
			auswahl: routeSchema.data.auswahl,
			auswahlGruppe: routeSchema.data.auswahlGruppe,
			mapSchema: () => routeSchema.data.mapSchema,
			gotoSchema: routeSchema.data.gotoSchema,
			setAuswahlGruppe: routeSchema.data.setAuswahlGruppe,
			addSchema: routeSchema.data.addSchema,
			importSchema: routeSchema.data.importSchema,
			migrateSchema: routeSchema.data.migrateSchema,
			duplicateSchema: routeSchema.data.duplicateSchema,
			refresh: routeSchema.data.refresh,
			migrationQuellinformationen: () => routeSchema.data.migrationQuellinformationen.value,
			apiStatus: api.status,
			revision: routeSchema.data.revision,
		};
	}

	public getProps(to: RouteLocationNormalized): SchemagruppeProps {
		return {
			auswahlGruppe: routeSchema.data.auswahlGruppe,
			removeSchemata: routeSchema.data.removeSchemata,
			apiStatus: api.status,
		};
	}

}

export const routeSchemagruppe = new RouteSchemagruppe();
