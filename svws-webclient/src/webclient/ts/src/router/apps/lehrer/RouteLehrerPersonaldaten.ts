import { LehrerListeEintrag, LehrerPersonaldaten } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteLehrer } from "~/router/apps/RouteLehrer";
import { routeLogin } from "~/router/RouteLogin";



export class RouteDataLehrerPersonaldaten {
	item: LehrerListeEintrag | undefined = undefined;
	_personaldaten: LehrerPersonaldaten | undefined = undefined;

	get personaldaten(): LehrerPersonaldaten {
		if (this._personaldaten === undefined)
			throw new Error("Unerwarteter Fehler: Lehrerstammdaten nicht initialisiert");
		return this._personaldaten;
	}

	set personaldaten(value: LehrerPersonaldaten | undefined) {
		this._personaldaten = value;
	}

	patch = async (data : Partial<LehrerPersonaldaten>) => {
		if (this.item === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await routeLogin.data.api.patchLehrerPersonaldaten(data, routeLogin.data.schema, this.item.id);
	}

}

const SLehrerPersonaldaten = () => import("~/components/lehrer/personaldaten/SLehrerPersonaldaten.vue");

export class RouteLehrerPersonaldaten extends RouteNode<RouteDataLehrerPersonaldaten, RouteLehrer> {

	public constructor() {
		super("lehrer_personaldaten", "personaldaten", SLehrerPersonaldaten, new RouteDataLehrerPersonaldaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Personaldaten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const tmp = parseInt(to_params.id as string);
			await this.onSelect(this.parent!.liste.liste.find(s => s.id === tmp));
		}
	}

	protected async onSelect(item?: LehrerListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.personaldaten = undefined;
		} else {
			this.data.item = item;
			this.data.personaldaten = await routeLogin.data.api.getLehrerPersonaldaten(routeLogin.data.schema, item.id)
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			patch: this.data.patch,
			personaldaten: this.data.personaldaten
		};
	}

}

export const routeLehrerPersonaldaten = new RouteLehrerPersonaldaten();
