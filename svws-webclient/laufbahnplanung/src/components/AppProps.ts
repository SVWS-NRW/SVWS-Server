import type { AuswahlChildData } from "./AuswahlChildData";

export interface AppProps {
	setApp:  (value: AuswahlChildData) => Promise<void>;
	app: AuswahlChildData;
	apps: AuswahlChildData[];
	appsHidden: boolean[];
}
