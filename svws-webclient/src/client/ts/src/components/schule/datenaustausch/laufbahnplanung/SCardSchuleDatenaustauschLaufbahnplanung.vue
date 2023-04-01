<template>
	<svws-ui-content-card title="Lupo-Datei für die Laufbahnplanung">
		<div class="content-wrapper">
			<input type="file" accept=".lup" @change="import_file" :disabled="loading">
			<svws-ui-spinner :spinning="loading" />
			<br>{{
				status === false
					? "Fehler beim Upload"
					: status === true
						? "Upload erfolgreich"
						: ""
			}}
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { ref } from "vue";

	const props = defineProps<{
		setGostLupoImportMDBFuerJahrgang: (formData: FormData) => Promise<boolean>;
	}>();

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
		status.value = await props.setGostLupoImportMDBFuerJahrgang(formData);
		loading.value = false;
	}

	defineExpose({
		status
	});
</script>
