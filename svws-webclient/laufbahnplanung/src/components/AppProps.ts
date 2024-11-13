import type { TabData } from "../../../ui/src/components/App/TabData";

export interface AppProps {
	setApp: (value: TabData) => Promise<void>;
	app: TabData;
	apps: TabData[];
	appsHidden: boolean[];
	enableFocusSwitching: boolean;
}
