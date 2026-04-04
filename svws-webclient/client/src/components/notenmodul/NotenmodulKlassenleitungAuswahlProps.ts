import type { ENMv2Klasse } from "@core";
import type { EnmKlassenleitungAuswahlListeManager, EnmManager } from "@ui";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface NotenmodulKlassenleitungAuswahlProps extends RouteAuswahlListProps<EnmKlassenleitungAuswahlListeManager> {
	enmManager: () => EnmManager;
	setAuswahlEinzel: (value: ENMv2Klasse | null) => void;
	auswahlEinzel: () => ENMv2Klasse | null;
	setAuswahlMehrfach: (value: Array<ENMv2Klasse>) => void;
	auswahlMehrfach: () => Array<ENMv2Klasse>;
}