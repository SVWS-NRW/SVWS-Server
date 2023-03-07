export interface SchuleDatenaustauschAppProps {
	setGostLupoImportMDBFuerJahrgang: (formData: FormData) => Promise<boolean>;
	setGostKurs42ImportZip: (formData: FormData) => Promise<boolean>;
}