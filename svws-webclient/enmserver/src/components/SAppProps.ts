import type { TabData } from "@ui/ui/nav/TabData";

export interface AppProps {
	setApp: (value: TabData) => Promise<void>;
	app: TabData;
	selectedChild: TabData;
	apps: TabData[];
	appsHidden: boolean[];
}
