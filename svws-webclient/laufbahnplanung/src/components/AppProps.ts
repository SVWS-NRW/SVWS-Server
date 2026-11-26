import type { TabData } from "@ui/ui/nav/TabManager";

export interface AppProps {
	setApp: (value: TabData) => Promise<void>;
	app: TabData;
	apps: TabData[];
	appsHidden: boolean[];
}
