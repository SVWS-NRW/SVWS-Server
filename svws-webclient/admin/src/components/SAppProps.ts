import type { TabData } from "../../../ui/src/components/App/TabData";
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
	enableFocusSwitching: boolean;
}
