import type { TabData } from "@ui";

export interface EinstellungenAuswahlProps {
	setChild: (value: TabData) => Promise<void>;
	child: TabData;
	children: TabData[];
	childrenHidden: boolean[];
}
