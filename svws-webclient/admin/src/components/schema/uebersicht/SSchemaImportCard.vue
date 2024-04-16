<template>
	<svws-ui-content-card class="ml-4 mt-4 mb-4">
		<div class="flex flex-col gap-2 mb-5">
			<div class="font-bold text-button">Quell-Datenbank: SQLite-Datenbank (.sqlite) hochladen</div>
			<input type="file" @change="onFileChanged" :disabled="loading().value" accept=".sqlite">
		</div>
		<svws-ui-button @click="add" :disabled="!file || loading().value"> <svws-ui-spinner :spinning="loading().value" /> Wiederherstellen </svws-ui-button>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, SimpleOperationResponse } from "@core";
	import { type ShallowRef, ref } from "vue";

	const props = defineProps<{
		restoreSchema:  (formData: FormData) => Promise<SimpleOperationResponse>;
		logs: () => ShallowRef<List<string | null> | undefined>;
		status: () => ShallowRef<boolean | undefined>;
		loading: () => ShallowRef<boolean>;
	}>();

	const file = ref<File | null>(null);

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
		props.loading().value = true;
		const formData = new FormData();
		formData.append("database", file.value);
		const result = await props.restoreSchema(formData);
		props.logs().value = result.log;
		props.status().value = result.success;
		file.value = null;
		props.loading().value = false;
	}

	function clear() {
		props.loading().value = false;
		props.logs().value = undefined;
		props.status().value = undefined;
	}

</script>
