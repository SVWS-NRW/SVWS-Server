<template>
	<div class="page--flex page--content--full m-8">
		<svws-ui-header>
			<div class="flex flex-col">
				<div class="inline-block"> Laufbahnplanung Oberstufe - Datei laden </div>
			</div>
		</svws-ui-header>
		<svws-ui-content-card title="Datei für die Laufbahnplanung laden">
			<div class="flex items-start gap-3">
				<input type="file" accept=".lp" @change="import_file" :disabled="loading">
				<svws-ui-spinner :spinning="loading" />
				<br> {{ status === false ? "Fehler beim Import" : status === true ? "Import erfolgreich" : "" }}
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { LadeDatenProps } from "./LadeDatenProps";

	const props = defineProps<LadeDatenProps>();

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);

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
		status.value = await props.importLaufbahnplanung(formData);
		loading.value = false;
	}

</script>
