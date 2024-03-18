<template>
	<svws-ui-content-card class="mt-4 mb-20">
		<div class="flex flex-col gap-2 mb-5">
			<div class="font-bold text-button">Quell-Datenbank: SQLite-Datenbank (.sqlite) hochladen</div>
			<input type="file" @change="onFileChanged" :disabled="loading" accept=".sqlite">
		</div>
		<div :class="{'mt-12': status === undefined, 'mt-5 mb-12': status !== undefined}">
			<template v-if="status === undefined">
				<div v-if="file" class="text-base leading-none -mt-3 mb-3 text-error"> <span class="icon i-ri-alert-line inline-block relative icon-error top-0.5 mr-0.5" />Bei der Wiederherstellung eines Schemas werden alle aktuell in diesem Schema hinterlegten Daten gelöscht.</div>
				<div class="flex gap-1 items-start">
					<svws-ui-button @click="add" :disabled="!file || loading"> <svws-ui-spinner :spinning="loading" /> Wiederherstellen </svws-ui-button>
					<svws-ui-button type="secondary" @click="setCurrentAction('')" :disabled="loading"> Abbrechen </svws-ui-button>
				</div>
			</template>
			<template v-else>
				<svws-ui-button type="secondary" @click="close"> Schließen </svws-ui-button>
			</template>
		</div>
		<log-box :logs="logs" :status="status">
			<template #button>
				<svws-ui-button v-if="status !== undefined" type="transparent" @click="clear" title="Verwerfe das Ergebnis des letzten Importversuchs"> Log verwerfen </svws-ui-button>
			</template>
		</log-box>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, SimpleOperationResponse } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		restoreSchema:  (formData: FormData) => Promise<SimpleOperationResponse>;
		setCurrentAction: (action: string) => void;
	}>();

	const file = ref<File | null>(null);
	const logs = ref<List<string|null>>();
	const status = ref<boolean>();

	const loading = ref<boolean>(false);

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target && target.files) {
			file.value = target.files[0];
		}
		clear();
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

	function clear() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	function close() {
		props.setCurrentAction('');
		clear();
	}

</script>
