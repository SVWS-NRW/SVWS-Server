import type { Schuljahresabschnitt, SimpleOperationResponse } from "@core";

export interface SchuleDatenaustauschUntisStundenplanProps {
	importUntisStundenplanGPU001: (formData: FormData, ignoreMissing: boolean) => Promise<SimpleOperationResponse>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
}