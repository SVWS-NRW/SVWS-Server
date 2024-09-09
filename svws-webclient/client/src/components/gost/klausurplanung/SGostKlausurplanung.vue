<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-button-select v-if="!kMan().terminGetMengeAsList().isEmpty()" type="secondary" :dropdown-actions="dropdownList">
			<template #icon> <svws-ui-spinner spinning v-if="apiStatus.pending" /> <span class="icon-sm i-ri-printer-line" v-else /> </template>
		</svws-ui-button-select>
	</Teleport>
	<router-view />
</template>

<script setup lang="ts">

	import { RouterView } from "vue-router";
	import type { DownloadPDFTypen } from "./DownloadPDFTypen";
	import type { GostKlausurplanungProps } from "./SGostKlausurplanungProps";
	import { onMounted, ref } from "vue";

	const props = defineProps<GostKlausurplanungProps>();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const dropdownList = [
		{ text: "Klausurplan (Kurse)", action: () => downloadPDF("Klausurplan (Kurse)")},
		{ text: "Klausurplan (Kurse und Nachschreiber)", action: () => downloadPDF("Klausurplan (Kurse und Nachschreiber)") },
		{ text: "Klausurplan (detailliert)", action: () => downloadPDF("Klausurplan (detailliert)"), default: true },
		{ text: "Schüler-Klausurplan (gesamt)", action: () => downloadPDF("Schüler-Klausurplan (gesamt)") },
		{ text: "Schüler-Klausurplan (einzeln)", action: () => downloadPDF("Schüler-Klausurplan (einzeln)") },
		{ text: "Klausurplan alle Jgst. (Kurse)", action: () => downloadPDF("Klausurplan alle Jgst. (Kurse)") },
		{ text: "Klausurplan alle Jgst. (Kurse und Nachschreiber)", action: () => downloadPDF("Klausurplan alle Jgst. (Kurse und Nachschreiber)") },
		{ text: "Klausurplan alle Jgst. (detailliert)", action: () => downloadPDF("Klausurplan alle Jgst. (detailliert)") },
		{ text: "Schüler-Klausurplan alle Jgst. (gesamt)", action: () => downloadPDF("Schüler-Klausurplan alle Jgst. (gesamt)") },
		{ text: "Schüler-Klausurplan alle Jgst. (einzeln)", action: () => downloadPDF("Schüler-Klausurplan alle Jgst. (einzeln)") },
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
