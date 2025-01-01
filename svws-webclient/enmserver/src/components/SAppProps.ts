import type { TabData, TabManager } from "@ui";
import type { ApiStatus } from "./ApiStatus";

export interface AppProps {
	username: string;
	logout: () => Promise<void>;
	setApp: (value: TabData) => Promise<void>;
	app: TabData;
	selectedChild: TabData;
	apps: TabData[];
	appsHidden: boolean[];
	apiStatus: ApiStatus;
}
