import type { JahrgaengeListeManager, JahrgangsDaten, Schulform } from "@core";

export interface JahrgangDatenProps {
	schuljahr: number;
	schulform: Schulform;
	manager: () => JahrgaengeListeManager;
	patch: (data : Partial<JahrgangsDaten>) => Promise<void>;
}
