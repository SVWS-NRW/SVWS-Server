import type { AuswahlChildData } from "./AuswahlChildData";
import type { ApiStatus } from "./ApiStatus";

export interface AppProps {
	username: string;
	logout: () => Promise<void>;
	setApp:  (value: AuswahlChildData) => Promise<void>;
	app: AuswahlChildData;
	apps: AuswahlChildData[];
	appsHidden: boolean[];
	apiStatus: ApiStatus;
}
