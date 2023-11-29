<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big">
		<template #modalTitle>Schema wiederherstellen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-input-wrapper :grid="2">
					Achtung, bei der Wiederherstellung eines Schemas werden alle aktuell in diesem Schema hinterlegten Daten gelöscht.
					<div class="flex gap-3">
						SQLite-Datei auswählen:
						<input type="file" @change="onFileChanged" :disabled="loading">
					</div>
					<template v-if="loading">
						<div class="flex">
							<svws-ui-spinner :spinning="true" /> Das Schema wird importiert…
						</div>
					</template>
				</svws-ui-input-wrapper>
			</div>
		</template>
		<template #modalActions>
			<template v-if="status !== true">
				<svws-ui-button type="secondary" @click="showModal().value = false" :disabled="loading"> Abbrechen </svws-ui-button>
				<svws-ui-button type="secondary" @click="add" :disabled="!file || loading"> Wiederherstellen </svws-ui-button>
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
