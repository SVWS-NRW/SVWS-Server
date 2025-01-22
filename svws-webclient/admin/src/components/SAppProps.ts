import type { TabData } from "@ui/ui/nav/TabData";
import type { ApiStatus } from "./ApiStatus";

export interface AppProps {
	username: string;
	isServerAdmin: boolean;
	logout: () => Promise<void>;
	setApp: (value: TabData) => Promise<void>;
	app: TabData;
	apps: TabData[];
	appsHidden: boolean[];
	apiStatus: ApiStatus;
}
