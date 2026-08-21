import { ArrayList, Arrays, type List, type Logo, type ApiFile } from "@core";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { api } from "~/router/Api";

interface RouteStateSchuleLogoverwaltung extends RouteStateInterface {
	logos: List<Logo>;
}

const defaultState = <RouteStateSchuleLogoverwaltung>{
	logos: new ArrayList(),
};

export class RouteDataSchuleLogoverwaltung extends RouteData<RouteStateSchuleLogoverwaltung> {

	public constructor() {
		super(defaultState);
	}

	public async ladeDaten() {
		const logos = await api.server.getLogos(api.schema);
		this.setPatchedState({ logos });
	}

	get logos(): List<Logo> {
		return this._state.value.logos;
	}

	patchLogo = async (data: Partial<Logo>, id: number): Promise<boolean> => {
		await api.server.patchLogo(data, api.schema, id);
		const logos = this.logos;
		for (const tmpLogo of logos) {
			if (tmpLogo.id === id) {
				Object.assign(tmpLogo, data);
				break;
			}
		}

		this.setPatchedDefaultState({ logos });
		return true;
	};

	addLogo = async (data: Partial<Logo>): Promise<Logo> => {
		const { id, ...partialData } = data;
		const logo = await api.server.addLogo(partialData, api.schema);
		const logos = this.logos;
		logos.add(logo);
		this.setPatchedDefaultState({ logos });
		return logo;
	};

	deleteLogo = async (logosToDelete: Logo[]): Promise<void> => {
		const logoIds = Arrays.asList(logosToDelete.map(logo => logo.id));
		await api.server.deleteLogos(logoIds, api.schema);

		const logos = this.logos;
		for (const id of logoIds) {
			const logo = (logos.toArray() as Logo[]).find(l => l.id === id);
			if (logo !== undefined) {
				logos.remove(logo);
			}
		}

		this.setPatchedDefaultState({ logos });
	};

	zipLogos = async (logosToZip: List<number>): Promise<ApiFile> => {
		return api.server.getLogosAsZip(logosToZip, api.schema);
	};

}
