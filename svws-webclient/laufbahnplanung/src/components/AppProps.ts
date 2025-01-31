import type { TabData } from "@ui/ui/nav/TabData";

export interface AppProps {
	setApp: (value: TabData) => Promise<void>;
	app: TabData;
	apps: TabData[];
	appsHidden: boolean[];
}
