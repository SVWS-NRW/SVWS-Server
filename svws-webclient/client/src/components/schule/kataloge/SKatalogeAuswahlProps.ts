import type { AuswahlChildData } from "../../AuswahlChildData";
import type { AbschnittAuswahlDaten } from "@comp";

export interface KatalogeAuswahlProps {
	returnToSchule: () => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	setChild: (value: AuswahlChildData) => Promise<void>;
	child: AuswahlChildData;
	children: AuswahlChildData[];
	childrenHidden: boolean[];
}