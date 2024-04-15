<template>
	<div class="page--content">
		<svws-ui-content-card title="Raumliste aus Kurs42 importieren">
			<div class="flex flex-col gap-2 mb-4 lg:mb-8">
				<p>Laden Sie die Datei Raeume.txt von Kurs 42 hier hoch, um den Raum-Katalog um die Räume in dieser Datei zu ergänzen. </p>
				<p><b>Wichtig: </b>Die Zeichenkodierung muss UTF-8 ohne BOM sein. </p>
			</div>
			<svws-ui-input-wrapper>
				<input type="file" accept=".txt,.csv" @change="import_file" :disabled="loading">
				<svws-ui-spinner :spinning="loading" />
			</svws-ui-input-wrapper>
			<log-box :logs="logs" :status="status" />
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import { type List } from '@core';
	import type { SchuleDatenaustauschKurs42RaeumeProps } from './SSchuleDatenaustauschKurs42RaeumeProps';

	const props = defineProps<SchuleDatenaustauschKurs42RaeumeProps>();

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);
	const logs = ref<List<string|null>>();

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
		const result = await props.setGostKurs42RaeumeTxt(formData);
		logs.value = result.log;
		status.value = result.success;
		loading.value = false;
	}

</script>
