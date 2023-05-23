<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal" size="small">
		<template #modalTitle>Laufbahnplanungsdaten importieren</template>
		<template #modalDescription>
			Bitte eine Datei auswählen, die importiert werden soll.
			<br><input type="file" accept=".lp" @change="import_file" :disabled="loading">
			<svws-ui-spinner :spinning="loading" />
			<br>{{
				status === false
					? "Fehler beim Upload"
					: status === true
						? "Upload erfolgreich"
						: ""
			}}
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal()">{{ status === true ? 'Schließen':'Abbrechen' }}</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { ref } from 'vue';

	const props = defineProps<{
		importLaufbahnplanung: (data: FormData) => Promise<boolean>;
	}>();

	const modal = ref();
	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);

	async function import_file(event: Event) {
		const target = event.target as HTMLInputElement;
		if (!target.files?.length)
			return;
		const file = target.files.item(0);
		if (!file)
			return;
		loading.value = true;
		const formData = new FormData();
		formData.append("data", file);
		status.value = await props.importLaufbahnplanung(formData);
		loading.value = false;
		if (status.value === true)
			modal.value.closeModal();
	}

	const openModal = () => {
		modal.value.openModal();
	}
</script>