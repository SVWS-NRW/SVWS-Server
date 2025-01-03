import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";

import { routeSchema } from "../schema/RouteSchema";

import type { SchemaNeuProps } from "~/components/schemaneu/SSchemaNeuProps";
import type { SchemaAuswahlProps } from "~/components/schema/SSchemaAuswahlProps";
import { api } from "~/router/Api";
import { ServerMode } from "@core/core/types/ServerMode";

const SSchemaAuswahl = () => import("~/components/schema/SSchemaAuswahl.vue")
const SSchemaNeu = () => import("~/components/schemaneu/SSchemaNeu.vue")


export class RouteSchemaNeu extends RouteNode<unknown, RouteApp> {

	public constructor() {
		super("schemaneu", "/schemaneu", SSchemaNeu);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "SchemaNeu";
		super.setView("liste", SSchemaAuswahl, (route) => this.getAuswahlProps(route));
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name };
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchemaAuswahlProps {
		return routeSchema.getAuswahlProps(to);
	}

	public getProps(to: RouteLocationNormalized): SchemaNeuProps {
		return {
			apiStatus: api.status,
			apiUsername: api.username,
			migrationQuellinformationen: () => routeSchema.data.migrationQuellinformationen.value,
			addSchema: routeSchema.data.addSchema,
			importSchema: routeSchema.data.importSchema,
			migrateSchema: routeSchema.data.migrateSchema,
			duplicateSchema: routeSchema.data.duplicateSchema,
			schema: routeSchema.data.auswahl?.name,
		};
	}

}

export const routeSchemaNeu = new RouteSchemaNeu();
