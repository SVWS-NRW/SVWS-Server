import type { Lernplattform, List } from "@core";

export interface SchuleDatenaustauschLernplattformenProps {
	lernplattformen: List<Lernplattform>,
	export: (lernplattform: Lernplattform, datenformat: string) => Promise<Blob | null>,
}

