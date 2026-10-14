import type { ENMLehrerFunktion } from "@core/core/data/enm/ENMLehrerFunktion";
import type { EnmManager } from "@ui/components/enm/EnmManager";
import type { TabData } from "@ui/ui/nav/TabData";

export interface AppProps {
	enmManager: () => EnmManager;
	setAnsicht: (auswahlAnsicht: ENMLehrerFunktion) => Promise<void>;
	optionsAuswahlAnsicht: () => Set<ENMLehrerFunktion>;
	auswahlAnsicht: () => ENMLehrerFunktion;

	setApp: (value: TabData) => Promise<void>;
	app: TabData;
	selectedChild: TabData;
	apps: TabData[];
	appsHidden: boolean[];
}
