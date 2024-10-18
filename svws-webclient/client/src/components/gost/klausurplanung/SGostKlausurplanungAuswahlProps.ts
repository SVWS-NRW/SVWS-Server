import type { GostHalbjahr, GostKlausurplanManager } from "@core";

export interface GostKlausurplanungAuswahlProps {
	kMan: () => GostKlausurplanManager;
	gotoHalbjahr: (value: GostHalbjahr) => Promise<void>;
	halbjahr: GostHalbjahr;
}