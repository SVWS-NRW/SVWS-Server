import type { ENMKlasse } from "@core";
import type { EnmKlassenleitungAuswahlListeManager, EnmManager } from "@ui";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface NotenmodulKlassenleitungAuswahlProps extends RouteAuswahlListProps<EnmKlassenleitungAuswahlListeManager> {
	enmManager: () => EnmManager;
	setAuswahlEinzel: (value: ENMKlasse | null) => void;
	auswahlEinzel: () => ENMKlasse | null;
	setAuswahlMehrfach: (value: Array<ENMKlasse>) => void;
	auswahlMehrfach: () => Array<ENMKlasse>;
}