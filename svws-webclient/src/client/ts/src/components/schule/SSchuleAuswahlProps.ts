import type { AuswahlChildData } from "../AuswahlChildData";

export interface SchuleAuswahlProps {
	setChild: (value: AuswahlChildData) => Promise<void>;
	child: AuswahlChildData;
	children: AuswahlChildData[];
	childrenHidden: boolean[];
}