import type { SchuleStammdaten, Schulform } from "@core";
import type { AuswahlChildData } from "./AuswahlChildData";
import type { ApiStatus } from "./ApiStatus";

export interface AppProps {
	schulform: Schulform;
	schuleStammdaten: SchuleStammdaten;
	username: string;
	logout: () => Promise<void>;
	setApp:  (value: AuswahlChildData) => Promise<void>;
	app: AuswahlChildData;
	apps: AuswahlChildData[];
	appsHidden: boolean[];
	apiStatus: ApiStatus;
}
