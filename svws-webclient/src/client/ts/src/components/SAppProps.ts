import { SchuleStammdaten, Schulform } from "@svws-nrw/svws-core";
import { AuswahlChildData } from "./AuswahlChildData";

export interface AppProps {
	schulform: Schulform;
	schuleStammdaten: SchuleStammdaten;
	username: string;
	logout: () => Promise<void>;
	setApp:  (value: AuswahlChildData) => Promise<void>;
	app: AuswahlChildData;
	apps: AuswahlChildData[];
	appsHidden: boolean[];
}
