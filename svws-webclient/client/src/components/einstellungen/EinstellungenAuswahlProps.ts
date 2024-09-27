import type { AuswahlChildData } from "../AuswahlChildData";

export interface EinstellungenAuswahlProps {
	setChild: (value: AuswahlChildData) => Promise<void>;
	child: AuswahlChildData;
	children: AuswahlChildData[];
	childrenHidden: boolean[];
}
