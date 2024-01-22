<template>
	<div class="page--content page--content--full">
		<svws-ui-content-card title="Blockung aus Kurs42 hochladen">
			<div class="flex flex-col gap-2 mb-16 lg:mb-20">
				<p><i-ri-information-line class="inline align-text-top" /> Der Import besteht aus den folgenden Text-Dateien, die aus Kurs42 exportiert werden müssen: </p>
				<ul class="list-disc list-inside">
					<li> Blockung.txt </li>
					<li> Schueler.txt </li>
					<li> Faecher.txt </li>
					<li> Kurse.txt </li>
					<li> Schienen.txt </li>
					<li> Blockplan.txt </li>
					<li> Fachwahlen.txt </li>
				</ul>
				<p> Diese Text-Dateien müssen für den Import im Hauptverzeichnis einer zip-Datei vorliegen. </p>
				<p>
					Ein Import kann nur erfolgreich sein, wenn die Daten aus Kurs42 gut zu den Daten der Schild-Datenbank passen!<br>
					Ist dies nicht der Fall, so schlägt der Import fehl.
				</p>
				<p>
					<b>Wichtig:</b> Wurde die Kurs42-Datei quartalsweise verwaltet, so ist in der Datei "Blockung.txt" der Eintrag Abschnitt
					entsprechend auf das Halbjahr zu korrigieren, für welches die Blockung importiert werden soll.
				</p>
			</div>
			<svws-ui-input-wrapper>
				<input type="file" accept=".zip" @change="import_file" :disabled="loading">
				<svws-ui-spinner :spinning="loading" />
			</svws-ui-input-wrapper>
			<log-box :logs="logs" :status="status" />
		</svws-ui-content-card>
	</div>
	<Teleport to=".svws-schule-datenauschtausch-header-target" v-if="isMounted">
		<span class="inline-block mr-3">Import aus Kurs42</span>
		<br>
		<span class="opacity-50"><i-ri-download2-line /></span>
	</Teleport>
</template>

<script setup lang="ts">

	import { ref, onMounted } from 'vue';
	import type { List } from '@core';
	import type { SchuleDatenaustauschKurs42Props } from './SSchuleDatenaustauschKurs42Props';

	const props = defineProps<SchuleDatenaustauschKurs42Props>();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

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
		const result = await props.setGostKurs42ImportZip(formData);
		logs.value = result.log;
		status.value = result.success;
		loading.value = false;
	}

</script>
