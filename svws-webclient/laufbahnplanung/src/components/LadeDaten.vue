<template>
	<ui-login-layout :version :githash application="Laufbahnplanung Oberstufe">
		<template #logo>
			<img src="/images/Wappenzeichen_NRW_bw.svg" alt="Logo NRW" class="h-14">
		</template>
		<template #main>
			<div class="w-full text-left mb-4">Datei öffnen:</div>
			<input type="file" accept=".lp" @change="import_file" :disabled="loading" class="w-full">
			<svws-ui-spinner :spinning="loading" />
			<div v-if="typeof status === 'string'" class="mt-2 text-sm text-left border-ui-danger text-ui-ondanger rounded-md bg-ui-danger p-1 border-2">
				{{ status }}
			</div>
			<div class="mt-2 text-sm text-left border-ui-warning text-ui-onwarning rounded-md bg-ui-warning p-1 border-2">
				Bitte beachten Sie, dass keinerlei personenbezogenen Daten
				auf unsere Server geladen werden. Die Daten verbleiben auf Ihrem Endgerät.
			</div>
		</template>
	</ui-login-layout>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { LadeDatenProps } from "./LadeDatenProps";
	import { version } from '../../version';
	import { githash } from '../../githash';
	import { UserNotificationException } from "@core/index";

	const props = defineProps<LadeDatenProps>();

	const status = ref<string | null | undefined>(undefined);
	const loading = ref<boolean>(false);

	async function import_file(event: Event) {
		const target = event.target as HTMLInputElement;
		if ((target.files === null) || (target.files.length === 0))
			return;
		const file = target.files.item(0);
		if (!file)
			return;
		status.value = undefined;
		loading.value = true;
		const formData = new FormData();
		formData.append("data", file);
		try {
			await props.importLaufbahnplanung(formData);
		} catch (e) {
			if (e instanceof UserNotificationException)
				status.value = e.message;
			else
				status.value = "Es gab einen Fehler beim Import der Laufbahnplanungsdaten";
		}
		loading.value = false;
	}

</script>
