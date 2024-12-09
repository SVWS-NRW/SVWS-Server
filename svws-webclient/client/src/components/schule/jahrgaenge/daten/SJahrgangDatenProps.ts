import type { JahrgangListeManager, JahrgangsDaten, Schulform } from "@core";

export interface JahrgangDatenProps {
	schuljahr: number;
	schulform: Schulform;
	jahrgangListeManager: () => JahrgangListeManager;
	patch: (data : Partial<JahrgangsDaten>) => Promise<void>;
}