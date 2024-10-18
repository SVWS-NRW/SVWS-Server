<template>
	<div class="page--content">
		<svws-ui-content-card title="Raumliste aus Untis importieren">
			<div class="flex flex-col gap-2 mb-4 lg:mb-8">
				<p>
					Laden Sie die Datei
					<svws-ui-tooltip>
						<span class="font-bold flex flex-row gap-1">GPU005.txt: <span class="icon i-ri-information-line mt-0.5" /></span>
						<template #content>
							Die CSV-Datei muss als Textkodierung UTF-8 ohne BOM verwenden. Als Trennzeichen wird das Semikolon verwendet und für die textbegrenzung doppelte Anführungszeichen (")
						</template>
					</svws-ui-tooltip>
					von Untis hier hoch, um den Raum-Katalog um die Räume in dieser Datei zu ergänzen.
				</p>
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
	import type { SchuleDatenaustauschUntisRaeumeProps } from './SSchuleDatenaustauschUntisRaeumeProps';

	const props = defineProps<SchuleDatenaustauschUntisRaeumeProps>();

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);
	const logs = ref<List<string|null>>();

	async function import_file(event: Event) {
		const target = event.target as HTMLInputElement;
		if ((target.files === null) || (target.files.length === 0))
			return;
		const file = target.files.item(0);
		if (!file)
			return;
		status.value = undefined;
		loading.value = true;
		const formData = new FormData();
		formData.append("data", file);
		const result = await props.importUntisRaeumeGPU005(formData);
		logs.value = result.log;
		status.value = result.success;
		loading.value = false;
	}

</script>
