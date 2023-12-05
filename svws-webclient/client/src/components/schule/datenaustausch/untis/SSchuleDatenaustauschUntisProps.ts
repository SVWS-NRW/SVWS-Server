import type { Schuljahresabschnitt, SimpleOperationResponse } from "@core";

export interface SchuleDatenaustauschUntisProps {
	setUntisImportGPU: (formData: FormData) => Promise<SimpleOperationResponse>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
}