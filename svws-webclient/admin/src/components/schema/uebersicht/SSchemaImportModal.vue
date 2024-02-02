<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big">
		<template #modalTitle>Schema wiederherstellen</template>
		<template #modalContent>
			<div class="flex flex-col items-center gap-3">
				<div class="flex flex-row"> <i-ri-alert-line class="text-red-500 mr-1" /> Bei der Wiederherstellung eines Schemas werden alle aktuell in diesem Schema hinterlegten Daten gelöscht.</div>
				<svws-ui-spacing />
				<div class="flex flex-col gap-3 w-128 text-left">
					<div class="flex flex-col gap-3 w-128 text-left">
						<div><b>Quell-Datenbank: </b> SQLite-Datei auswählen (Endung .sqlite)</div>
						<input type="file" @change="onFileChanged" :disabled="loading" accept=".sqlite">
					</div>
				</div>
				<svws-ui-spacing />
			</div>
		</template>
		<template #modalActions>
			<template v-if="status === undefined">
				<svws-ui-button type="secondary" @click="showModal().value = false" :disabled="loading"> Abbrechen </svws-ui-button>
				<svws-ui-button type="secondary" @click="add" :disabled="!file || loading"> <svws-ui-spinner :spinning="loading" /> Wiederherstellen </svws-ui-button>
			</template>
			<template v-else>
				<svws-ui-button type="secondary" @click="close"> Schließen </svws-ui-button>
			</template>
		</template>
		<template #modalLogs>
			<log-box :logs="logs" :status="status" />
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { List, SimpleOperationResponse } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		restoreSchema:  (formData: FormData) => Promise<SimpleOperationResponse>;
	}>();

	const file = ref<File | null>(null);
	const logs = ref<List<string|null>>();
	const status = ref<boolean>();

	const loading = ref<boolean>(false);

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const openModal = () => {
		showModal().value = true;
	}

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target && target.files) {
			file.value = target.files[0];
		}
	}

	async function add() {
		if (file.value === null)
			return;
		loading.value = true;
		const formData = new FormData();
		formData.append("database", file.value);
		const result = await props.restoreSchema(formData);
		logs.value = result.log;
		status.value = result.success;
		file.value = null;
		loading.value = false;
	}

	function close() {
		showModal().value = false;
		logs.value = undefined;
		status.value = undefined;
	}
</script>
