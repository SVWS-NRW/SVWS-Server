<template>
	<div class="content-card overflow-auto h-full w-full">
		<div class="content-card--content">
			<div class="flex flex-col items-start gap-3">
				<div> <svws-ui-checkbox v-model="replaceSchueler">Laufbahndaten von Schüler ersetzen</svws-ui-checkbox> </div>
				<div> <svws-ui-checkbox v-model="replaceJahrgang" :disabled="!replaceSchueler">Jahrgangs-spezifische Daten ersetzen</svws-ui-checkbox> </div>
				<div>
					<input type="file" accept=".lup" @change="import_file" :disabled="loading">
					<svws-ui-spinner :spinning="loading" />
				</div>
			</div>
			<svws-ui-notification v-if="logs" :type="status === true ? 'success' : 'error'">
				<template #stack>
					<log-box :logs="logs" :status="status" />
				</template>
			</svws-ui-notification>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { List, SimpleOperationResponse } from "@core";

	const props = defineProps<{
		setGostLupoImportMDBFuerJahrgang: (formData: FormData, mode: 'none' | 'schueler' | 'all') => Promise<SimpleOperationResponse>;
	}>();

	const mode = ref<'none' | 'schueler' | 'all'>('none');

	const replaceSchueler = computed<boolean>({
		get: () => (mode.value !== 'none'),
		set: (value) => mode.value = value ? 'schueler' : 'none'
	});

	const replaceJahrgang = computed<boolean>({
		get: () => (mode.value === 'all'),
		set: (value) => mode.value = value ? 'all' : 'schueler'
	});

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
		const result = await props.setGostLupoImportMDBFuerJahrgang(formData, mode.value);
		logs.value = result.log;
		status.value = result.success;
		loading.value = false;
	}

	defineExpose({
		status
	});

</script>
