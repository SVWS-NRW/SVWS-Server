import type { TabData } from "@ui/ui/nav/TabData";

export interface AppProps {
	logout: () => Promise<void>;
	setApp: (value: TabData) => Promise<void>;
	app: TabData;
	selectedChild: TabData;
	apps: TabData[];
	appsHidden: boolean[];
}
