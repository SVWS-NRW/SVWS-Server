import type { AuswahlChildData } from "../AuswahlChildData";
import type { SchuleStammdaten } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface SchuleAuswahlProps {
	setChild: (value: AuswahlChildData) => Promise<void>;
	child: AuswahlChildData;
	children: AuswahlChildData[];
	childrenHidden: boolean[];
	schule: () => SchuleStammdaten;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
}
