import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";

export interface SchuleDatenaustauschLaufbahnplanungProps {
	setGostLupoImportMDBFuerJahrgang: (formData: FormData, mode: 'none' | 'schueler' | 'all') => Promise<SimpleOperationResponse>;
}