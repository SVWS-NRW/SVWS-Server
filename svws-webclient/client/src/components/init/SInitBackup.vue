<template>
	<svws-ui-action-button title="Backup" description="Daten werden aus einem Backup wiederhergestellt" :action-function="doImport" action-label="Import starten" icon="i-ri-device-recover-line">
		<div class="flex flex-col gap-2 text-left">
			<span class="font-bold text-button">SQLite-Datenbank hochladen</span>
			<input type="file" @change="onFileChanged" :disabled="loading" accept=".sqlite">
			<svws-ui-spinner :spinning="loading" />
			<div class="font-bold text-sm">
				{{
					status === false
						? "Fehler beim Upload"
						: status === true
							? "Upload erfolgreich"
							: ""
				}}
			</div>
		</div>
	</svws-ui-action-button>
</template>

<script setup lang="ts">

	import { ref, shallowRef } from "vue";

	const props = defineProps<{
		importSQLite: (data: FormData) => Promise<boolean>;
	}>();

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);

	const file = shallowRef<File | null>(null);

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if ((target !== null) && (target.files !== null) && (target.files.length > 0))
			file.value = target.files[0];
	}

	async function doImport() {
		if (!file.value)
			return;
		loading.value = true;
		const formData = new FormData();
		formData.append("database", file.value);
		status.value = await props.importSQLite(formData);
		loading.value = false;
	}

</script>
