<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Blockung aus Kurs42 importieren">
			<div class="flex flex-col gap-2 mb-4 lg:mb-8">
				<p><span class="icon i-ri-information-line inline align-text-top" /> Der Import besteht aus den folgenden Text-Dateien, die aus Kurs42 exportiert werden müssen: </p>
				<ul class="list-disc list-inside">
					<li> Blockung.txt </li>
					<li> Schueler.txt </li>
					<li> Faecher.txt </li>
					<li> Kurse.txt </li>
					<li> Schienen.txt </li>
					<li> Blockplan.txt </li>
					<li> Fachwahlen.txt </li>
				</ul>
				<p> Diese Text-Dateien müssen für den Import im Hauptverzeichnis einer zip-Datei vorliegen. Die Zeichenkodierung muss UTF-8 ohne BOM sein. </p>
				<p>
					Ein Import kann nur erfolgreich sein, wenn die Daten aus Kurs42 gut zu den Daten der Schild-Datenbank passen!<br>
					Ist dies nicht der Fall, so schlägt der Import fehl.
				</p>
				<p>
					<b>Wichtig:</b> Wurde die Kurs42-Datei quartalsweise verwaltet, so ist in der Datei "Blockung.txt" der Eintrag Abschnitt
					entsprechend auf das Halbjahr zu korrigieren, für welches die Blockung importiert werden soll.
				</p>
				<p>
					<b>Wichtig:</b> Die Datei Schueler.txt muss die korrekten Datenbank-IDs beinhalten. Sind diese fehlerhaft, so kommt es ggf. zu fehlerhaften
					oder auch fehlenden Zuordnungen, die den Import unbrauchbar machen.
				</p>
				<p>
					<b>Wichtig:</b> Die Blockung wird anschließend nur korrekt dargestellt, wenn die Laufbahnplanungsdaten im Client bereits vorhanden sind, weil die
					Fachwahlen nicht aus Kurs 42 importiert werden. GGf. muss zuvor ein Jahrgangsbezogener Import aus LuPO erfolgen.
				</p>
			</div>
			<svws-ui-input-wrapper>
				<input class="contentFocusField" type="file" accept=".zip" @change="import_file" :disabled="loading">
				<svws-ui-spinner :spinning="loading" />
			</svws-ui-input-wrapper>
			<log-box :logs="logs" :status="status" />
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import type { List } from '@core';
	import type { SchuleDatenaustauschKurs42BlockungProps } from './SSchuleDatenaustauschKurs42BlockungProps';

	const props = defineProps<SchuleDatenaustauschKurs42BlockungProps>();

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);
	const logs = ref<List<string|null>>();

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
		const result = await props.setGostKurs42ImportZip(formData);
		logs.value = result.log;
		status.value = result.success;
		loading.value = false;
	}

</script>
