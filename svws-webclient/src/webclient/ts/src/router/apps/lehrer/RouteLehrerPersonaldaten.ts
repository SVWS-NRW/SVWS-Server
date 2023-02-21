import { LehrerListeEintrag, LehrerPersonaldaten } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeLehrer, RouteLehrer } from "~/router/apps/RouteLehrer";
import { routeLogin } from "~/router/RouteLogin";
import { LehrerPersonaldatenProps } from "~/components/lehrer/personaldaten/SLehrerPersonaldatenProps";



export class RouteDataLehrerPersonaldaten {

	item: LehrerListeEintrag | undefined = undefined;
	_personaldaten: LehrerPersonaldaten | undefined = undefined;

	public async onSelect(item?: LehrerListeEintrag) {
		if (item === this.item)
			return;
		if (item === undefined) {
			this.item = undefined;
			this.personaldaten = undefined;
		} else {
			this.item = item;
			this.personaldaten = await routeLogin.data.api.getLehrerPersonaldaten(routeLogin.data.schema, item.id)
		}
	}

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
		await this.data.onSelect(routeLehrer.data.auswahl.value);
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): LehrerPersonaldatenProps {
		return {
			patch: this.data.patch,
			personaldaten: this.data.personaldaten
		};
	}

}

export const routeLehrerPersonaldaten = new RouteLehrerPersonaldaten();
