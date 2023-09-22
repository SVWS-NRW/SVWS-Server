<template>
	<svws-ui-content-card title="LuPO-Datei für die Laufbahnplanung hochladen">
		<div class="flex items-start gap-3">
			<input type="file" accept=".lup" @change="import_file" :disabled="loading">
			<svws-ui-spinner :spinning="loading" />
			<br> {{ status === false ? "Fehler beim Import" : status === true ? "Import erfolgreich" : "" }}
		</div>
	</svws-ui-content-card>
	<svws-ui-content-card v-if="log != null" title="Log">
		<pre class="flex flex-col">
			{{ log }}
		</pre>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import { type List, type SimpleOperationResponse } from "@core";

	const props = defineProps<{
		setGostLupoImportMDBFuerJahrgang: (formData: FormData) => Promise<SimpleOperationResponse>;
	}>();

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);
	const log = ref<string | null>(null);

	function log2str(log: List<string | null>) : string {
		let result = "";
		for (const s of log)
			if (s !== null)
				result += s + "\n";
		return result;
	}

	async function import_file(event: Event) {
		const target = event.target as HTMLInputElement;
		if (!target.files?.length)
			return;
		const file = target.files.item(0);
		if (!file)
			return;
		status.value = undefined;
		loading.value = true;
		const formData = new FormData();
		formData.append("data", file);
		const result = await props.setGostLupoImportMDBFuerJahrgang(formData);
		log.value = log2str(result.log);
		status.value = result.success;
		loading.value = false;
	}

	defineExpose({
		status
	});
</script>
