import type { RouteLocationRaw, RouteParams } from "vue-router";
import type { DBSchemaListeEintrag} from "@svws-nrw/svws-core";
import type { Ref} from "vue";
import type { LoginProps } from "~/components/SLoginProps";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { RouteNode } from "~/router/RouteNode";
import { api } from "./Api";
import { RouteManager } from "./RouteManager";
import { routeInit } from "./RouteInit";
import { ref } from "vue";
import SLogin from "~/components/SLogin.vue";

export class RouteLogin extends RouteNode<unknown, any> {

	protected defaultChildNode = undefined;

	// Der Pfad, zu welchem weitergeleitet wird
	public routepath = "/";
	public redirect = '';
	protected schema: Ref<string | null> = ref(null);

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "login", "/login/:schemaname?", SLogin);
		super.propHandler = (route) => this.getProps();
		super.text = "Login";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name };
	}

	public login = async (schema: string, username: string, password: string): Promise<void> => {
		await api.login(schema, username, password);
		if (api.authenticated && await api.init())
			await RouteManager.doRoute(this.routepath);
		else if (api.authenticated && api.benutzerIstAdmin)
			await RouteManager.doRoute(routeInit.name);
	}

	public logout = async () => {
		this.routepath = "/";
		this.schema.value = api.schema;
		await RouteManager.doRoute({ name: this.name });
		await api.logout();
	}

	public setSchema = async (schema: DBSchemaListeEintrag) => {
		this.schema.value = schema.name;
	}

	public getProps(): LoginProps {
		return {
			setHostname: api.setHostname,
			setSchema: this.setSchema,
			login: this.login,
			connectTo: api.connectTo,
			authenticated: api.authenticated,
			hostname: api.hostname,
			schema: this.schema.value,
		}
	}

}

export const routeLogin = new RouteLogin();
