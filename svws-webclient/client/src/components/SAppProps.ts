import type { SchuleStammdaten, Schulform } from "@core";
import type { AuswahlChildData } from "./AuswahlChildData";
import type { ApiStatus } from "./ApiStatus";

export interface AppProps {
	schulform: Schulform;
	schuleStammdaten: SchuleStammdaten;
	username: string;
	schemaname: string;  // Der Name des DB-Schemas
	logout: () => Promise<void>;
	setApp:  (value: AuswahlChildData) => Promise<void>;
	benutzerprofilApp: AuswahlChildData;
	app: AuswahlChildData;
	apps: AuswahlChildData[];
	appsHidden: boolean[];
	apiStatus: ApiStatus;
	backticks: () => boolean;
}
