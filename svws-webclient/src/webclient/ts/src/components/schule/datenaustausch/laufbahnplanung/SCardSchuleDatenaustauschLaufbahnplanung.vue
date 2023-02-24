<template>
	<svws-ui-content-card title="Laufbahnplanung">
		<div class="content-wrapper">
			Eine Lupo Datei importieren
			<br><input type="file" accept=".lup" @change="import_file">
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

	async function import_file(event: Event) {
		const target = event.target as HTMLInputElement;
		if (!target.files?.length) return;
		const file = target.files.item(0);
		if (!file) return;
		const formData = new FormData();
		formData.append("data", file);
		status.value = await props.setGostLupoImportMDBFuerJahrgang(formData)
	}

	defineExpose({
		status
	});
</script>
