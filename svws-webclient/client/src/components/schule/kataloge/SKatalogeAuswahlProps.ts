import type { TabData } from "@ui";
import type { AbschnittAuswahlDaten } from "@ui";

export interface KatalogeAuswahlProps {
	returnToSchule: () => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	setChild: (value: TabData) => Promise<void>;
	child: TabData;
	children: TabData[];
	childrenHidden: boolean[];
}