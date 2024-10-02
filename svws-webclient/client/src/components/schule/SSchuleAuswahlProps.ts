import type { TabData } from "@ui";
import type { SchuleStammdaten } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface SchuleAuswahlProps {
	setChild: (value: TabData) => Promise<void>;
	child: TabData;
	children: TabData[];
	childrenHidden: boolean[];
	schule: () => SchuleStammdaten;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
}
