import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchema, type RouteSchema } from "~/router/apps/schema/RouteSchema";

import type { SchemaUebersichtProps } from "~/components/schema/uebersicht/SSchemaUebersichtProps";

const SSchemaUebersicht = () => import("~/components/schema/uebersicht/SSchemaUebersicht.vue");


export class RouteSchemaUebersicht extends RouteNode<unknown, RouteSchema> {

	public constructor() {
		super("schema.uebersicht", "uebersicht", SSchemaUebersicht);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Übersicht";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): SchemaUebersichtProps {
		return {
			data: () => routeSchema.data.auswahl,
			backupSchema: routeSchema.data.backupSchema,
			restoreSchema: routeSchema.data.restoreSchema,
			migrateSchema: routeSchema.data.migrateSchema,
			upgradeSchema: routeSchema.data.upgradeSchema,
			revision: routeSchema.data.revision,
		};
	}

}

export const routeSchemaUebersicht = new RouteSchemaUebersicht();

