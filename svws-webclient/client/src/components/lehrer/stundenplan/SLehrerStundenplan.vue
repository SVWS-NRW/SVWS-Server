<template>
	<Teleport defer to=".svws-ui-header--actions">
		<svws-ui-button-select type="secondary" :dropdown-actions>
			<template #icon> <svws-ui-spinner spinning v-if="apiStatus.pending" /> <span class="icon i-ri-printer-line" v-else /> </template>
		</svws-ui-button-select>
		<svws-ui-modal-hilfe> <hilfe-lehrer-stundenplan /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-flex-col overflow-x-auto">
		<template v-if="stundenplan() === undefined">
			<div class="flex flex-col gap-2 justify-center items-center min-h-full w-full grow text-headline-md text-ui-contrast-50 text-center">
				<span class="icon-xxl i-ri-calendar-event-line" />
				<span>Derzeit liegt kein Stundenplan<br>für diesen Lernabschnitt vor.</span>
			</div>
		</template>
		<template v-else>
			<stundenplan-auswahl :stundenplan="stundenplan()" :map-stundenplaene :goto-stundenplan :goto-wochentyp :goto-kalenderwoche :manager :wochentyp
				:kalenderwoche :ganzer-stundenplan :set-ganzer-stundenplan autofocus />
			<stundenplan-lehrer :id :ignore-empty :manager :wochentyp :kalenderwoche />
		</template>
	</div>
</template>

<script setup lang="ts">

	import type { LehrerStundenplanProps, DownloadPDFTypen } from './SLehrerStundenplanProps';

	const props = defineProps<LehrerStundenplanProps>();

	const dropdownActions = [
		{ text: "Stundenplan", action: () => downloadPDF("Stundenplan"), default: true },
		{ text: "Stundenplan mit Pausenaufsichten", action: () => downloadPDF("Stundenplan mit Pausenaufsichten") },
		{ text: "Stundenplan mit Pausenzeiten", action: () => downloadPDF("Stundenplan mit Pausenzeiten") },
	];

	async function downloadPDF(title: DownloadPDFTypen) {
		const { data, name } = await props.getPDF(title);
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

</script>
