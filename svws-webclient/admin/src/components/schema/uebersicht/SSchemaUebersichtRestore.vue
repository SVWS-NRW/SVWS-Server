<template>
	<ui-card icon="i-ri-device-recover-line" title="Backup wiederherstellen" subtitle="Daten werden aus einem Backup wiederhergestellt" :is-open @update:is-open="(open) => emit('opened', open)">
		<div class="flex flex-col gap-2 mt-2">
			<div class="font-bold text-button">Quell-Datenbank: SQLite-Datenbank (.sqlite) hochladen</div>
			<input type="file" @change="onFileChanged" :disabled="loadingFunction().value" accept=".sqlite">
			<svws-ui-button :disabled="!file || loadingFunction().value" :is-loading="loadingFunction().value" title="Wiederherstellen" @click="actionFunction" class="mt-4 w-fit">
				<svws-ui-spinner v-if="loadingFunction().value" spinning />
				<span v-else class="icon i-ri-play-line" />
				Wiederherstellen
			</svws-ui-button>
		</div>
	</ui-card>
</template>

<script setup lang="ts">

	import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
	import type { List } from "@core/java/util/List";
	import { type ShallowRef, ref } from "vue";

	const props = defineProps<{
		restoreSchema: (formData: FormData) => Promise<SimpleOperationResponse>;
		logsFunction: () => ShallowRef<List<string | null> | undefined>;
		statusFunction: () => ShallowRef<boolean | undefined>;
		loadingFunction: () => ShallowRef<boolean>;
		isOpen: boolean;
	}>();

	const emit = defineEmits<{
		'opened': [value: boolean];
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
