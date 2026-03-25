import type { ENMv1Klasse } from "@core";
import type { EnmKlassenleitungAuswahlListeManager, EnmManager } from "@ui";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface NotenmodulKlassenleitungAuswahlProps extends RouteAuswahlListProps<EnmKlassenleitungAuswahlListeManager> {
	enmManager: () => EnmManager;
	setAuswahlEinzel: (value: ENMv1Klasse | null) => void;
	auswahlEinzel: () => ENMv1Klasse | null;
	setAuswahlMehrfach: (value: Array<ENMv1Klasse>) => void;
	auswahlMehrfach: () => Array<ENMv1Klasse>;
}