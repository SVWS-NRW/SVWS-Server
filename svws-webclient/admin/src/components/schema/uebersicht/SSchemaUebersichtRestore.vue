<template>
	<ui-card icon="i-ri-device-recover-line" title="Backup wiederherstellen" subtitle="Daten werden aus einem Backup wiederhergestellt" :is-open @update:is-open="(open) => emit('opened', open)">
		<div class="space-y-4">
			<div class="font-bold text-button">Quell-Datenbank: SQLite-Datenbank (.sqlite) hochladen</div>
			<input type="file" @change="onFileChanged" :disabled="loading" accept=".sqlite">
		</div>
		<template #buttonFooterLeft>
			<svws-ui-button :disabled="!file || loading" :is-loading="loading" title="Wiederherstellen" @click="actionFunction" class="mt-4 w-fit">
				<svws-ui-spinner v-if="loading" spinning />
				<span v-else class="icon i-ri-play-line" />
				Wiederherstellen
			</svws-ui-button>
		</template>
	</ui-card>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
	import type { List } from "@core/java/util/List";

	const props = defineProps<{
		restoreSchema: (formData: FormData) => Promise<SimpleOperationResponse>;
		setLogs: (value: List<string | null> | undefined) => void;
		setStatus: (value: boolean | undefined) => void;
		loading: boolean;
		setLoading: (value: boolean) => void;
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
		props.setLoading(true);
		const formData = new FormData();
		formData.append("database", file.value);
		const result = await props.restoreSchema(formData);
		props.setLogs(result.log);
		props.setStatus(result.success);
		file.value = null;
		props.setLoading(false);
	}

	function clear() {
		props.setLoading(false);
		props.setLogs(undefined);
		props.setStatus(undefined);
	}

</script>
