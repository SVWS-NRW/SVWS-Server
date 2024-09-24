<template>
	<svws-ui-action-button title="Backup wiederherstellen" description="Daten werden aus einem Backup wiederhergestellt" icon="i-ri-device-recover-line" :action-function action-label="Wiederherstellen" :action-disabled="!file || loadingFunction().value" :is-loading="loadingFunction().value" :is-active>
		<div class="flex flex-col gap-2">
			<div class="font-bold text-button">Quell-Datenbank: SQLite-Datenbank (.sqlite) hochladen</div>
			<input type="file" @change="onFileChanged" :disabled="loadingFunction().value" accept=".sqlite">
		</div>
	</svws-ui-action-button>
</template>

<script setup lang="ts">

	import { type ShallowRef, ref } from "vue";
	import type { SimpleOperationResponse } from "../../../../../core/src/core/data/SimpleOperationResponse";
	import type { List } from "../../../../../core/src/java/util/List";

	const props = defineProps<{
		restoreSchema: (formData: FormData) => Promise<SimpleOperationResponse>;
		logsFunction: () => ShallowRef<List<string | null> | undefined>;
		statusFunction: () => ShallowRef<boolean | undefined>;
		loadingFunction: () => ShallowRef<boolean>;
		isActive: boolean;
	}>();

	const file = ref<File | null>(null);

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target.files) {
			file.value = target.files[0];
		}
		clear();
	}

	async function actionFunction() {
		if (file.value === null)
			return;
		props.loadingFunction().value = true;
		const formData = new FormData();
		formData.append("database", file.value);
		const result = await props.restoreSchema(formData);
		props.logsFunction().value = result.log;
		props.statusFunction().value = result.success;
		file.value = null;
		props.loadingFunction().value = false;
	}

	function clear() {
		props.loadingFunction().value = false;
		props.logsFunction().value = undefined;
		props.statusFunction().value = undefined;
	}

</script>
