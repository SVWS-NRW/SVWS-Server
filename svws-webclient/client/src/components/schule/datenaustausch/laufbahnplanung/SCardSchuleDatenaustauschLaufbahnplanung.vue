<template>
	<div class="content-card overflow-auto h-full w-full">
		<div class="content-card--content">
			<div class="flex flex-col items-start gap-3">
				<div>
					<input type="file" accept=".lup" @change="import_file" :disabled="loading">
					<svws-ui-spinner :spinning="loading" />
				</div>
				<div v-if="log != null" class="mt-4">
					<div class="flex text-error mb-4">
						<div class="text-headline-md"> Log </div>
						<i-ri-alert-line v-if="(status === true)" class="ml-1" />
					</div>
					<pre>{{ log }}</pre>
				</div>
			</div>
		</div>
	</div>
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
