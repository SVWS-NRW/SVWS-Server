import type { GostHalbjahr } from "@core";

export interface GostKlausurplanungAuswahlChildData {
	name: string,
	text: string,
}

export interface GostKlausurplanungAuswahlProps {
	gotoHalbjahr: (value: GostHalbjahr) => Promise<void>;
	halbjahr: GostHalbjahr;
	setChild: (value: GostKlausurplanungAuswahlChildData) => Promise<void>;
	child: GostKlausurplanungAuswahlChildData;
	children: GostKlausurplanungAuswahlChildData[];
	childrenHidden: boolean[];
}