import type { AuswahlChildData } from "../AuswahlChildData";
import type { AbschnittAuswahlDaten } from "@comp";

export interface KatalogeAuswahlProps {
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	setChild: (value: AuswahlChildData) => Promise<void>;
	child: AuswahlChildData;
	children: AuswahlChildData[];
	childrenHidden: boolean[];
}