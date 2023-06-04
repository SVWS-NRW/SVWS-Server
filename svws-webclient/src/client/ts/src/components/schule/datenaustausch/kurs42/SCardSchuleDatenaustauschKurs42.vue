<template>
	<svws-ui-content-card title="Blockung aus Kurs42 hochladen">
		<div class="flex flex-col gap-2 mb-16 lg:mb-20">
			<p><i-ri-information-line class="inline align-text-top" /> Der Import besteht aus den folgenden Text-Dateien, die aus Kurs 42 exportiert werden müssen: </p>
			<ul class="list-disc list-inside">
				<li> Schueler.txt </li>
				<li> Faecher.txt </li>
				<li> Kurse.txt </li>
				<li> Schienen.txt </li>
				<li> Blockplan.txt </li>
				<li> Fachwahlen.txt </li>
			</ul>
			<p> Diese Text-Dateien müssen für den Import im Hauptverzeichnis einer zip-Datei vorliegen. </p>
			<p>
				Ein Import kann nur erfolgreich sein, wenn die Daten aus Kurs 42 gut zu den Daten der Schild-Datenbank passen!<br>
				Ist dies nicht der Fall, so schlägt der Import fehl.
			</p>
		</div>
		<svws-ui-input-wrapper>
			<input type="file" accept=".zip" @change="import_file" :disabled="loading">
			<svws-ui-spinner :spinning="loading" />
			<br>{{
				status === false
					? "Fehler beim Upload"
					: status === true
						? "Upload erfolgreich"
						: ""
			}}
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { ref } from "vue";

	const props = defineProps<{
		setGostKurs42ImportZip: (formData: FormData) => Promise<boolean>;
	}>();

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);

	async function import_file(event: Event) {
		const target = event.target as HTMLInputElement;
		if (!target.files?.length)
			return;
		const file = target.files.item(0);
		if (!file)
			return;
		loading.value = true;
		const formData = new FormData();
		formData.append("data", file);
		status.value = await props.setGostKurs42ImportZip(formData);
		loading.value = false;
	}

	defineExpose({
		status
	});
</script>
