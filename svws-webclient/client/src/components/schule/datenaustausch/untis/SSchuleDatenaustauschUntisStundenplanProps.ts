import type { Schuljahresabschnitt, SimpleOperationResponse } from "@core";

export interface SchuleDatenaustauschUntisStundenplanProps {
	setUntisImportGPU: (formData: FormData) => Promise<SimpleOperationResponse>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
}