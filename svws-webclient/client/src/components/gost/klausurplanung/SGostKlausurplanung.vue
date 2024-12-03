<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-button-select v-if="!kMan().terminGetMengeAsList().isEmpty()" type="secondary" :dropdown-actions="dropdownList">
			<template #icon> <svws-ui-spinner spinning v-if="apiStatus.pending" /> <span class="icon-sm i-ri-printer-line" v-else /> </template>
		</svws-ui-button-select>
	</Teleport>
	<Teleport to=".svws-sub-nav-target" defer>
		<nav class="svws-ui-secondary-tabs">
			<svws-ui-tab-bar :tab-manager secondary enable-focus-switching>
				<template #badge="{ tab }">
					<template v-if="(tab.name === 'gost.klausurplanung.probleme') && kMan().hasFehlenddatenZuAbijahrUndHalbjahr(props.jahrgangsdaten!.abiturjahr, halbjahr)">
						<div class="font-bold text-white bg-error rounded-full shadow h-5 ml-1 -mt-3 px-1.5 pt-0.5" v-if="numErrors">{{ numErrors }}</div>
						<div class="font-bold text-black bg-yellow-200 rounded-full shadow h-5 ml-1 -mt-3 px-1.5 pt-0.5" v-if="numWarnings">{{ numWarnings }}</div>
					</template>
				</template>
			</svws-ui-tab-bar>
		</nav>
		<svws-ui-sub-nav />
	</Teleport>
	<router-view />
</template>

<script setup lang="ts">

	import { RouterView } from "vue-router";
	import type { DownloadPDFTypen } from "./DownloadPDFTypen";
	import type { GostKlausurplanungProps } from "./SGostKlausurplanungProps";
	import { computed, onMounted, ref } from "vue";

	const props = defineProps<GostKlausurplanungProps>();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const numErrors = computed<number>(() => props.kMan().planungsfehlerGetAnzahlByHalbjahrAndQuartal(props.jahrgangsdaten!.abiturjahr, props.halbjahr, props.quartalsauswahl.value));
	const numWarnings = computed<number>(() => props.kMan().planungshinweiseGetAnzahlByHalbjahrAndQuartal(props.jahrgangsdaten!.abiturjahr, props.halbjahr, props.quartalsauswahl.value));

	const dropdownList = [
		{ text: "Klausurplan (Kurse)", action: () => downloadPDF("Klausurplan (Kurse)")},
		{ text: "Klausurplan (Nachschreiber)", action: () => downloadPDF("Klausurplan (Nachschreiber)") },
		{ text: "Klausurplan (Kurse und Nachschreiber)", action: () => downloadPDF("Klausurplan (Kurse und Nachschreiber)") },
		{ text: "Klausurplan (detailliert)", action: () => downloadPDF("Klausurplan (detailliert)"), default: true },
		{ text: "Schüler-Klausurplan (gesamt)", action: () => downloadPDF("Schüler-Klausurplan (gesamt)") },
		{ text: "Schüler-Klausurplan (einzeln)", action: () => downloadPDF("Schüler-Klausurplan (einzeln)") },
		{ text: "Klausurplan alle Jgst. (Kurse)", action: () => downloadPDF("Klausurplan alle Jgst. (Kurse)") },
		{ text: "Klausurplan alle Jgst. (Nachschreiber)", action: () => downloadPDF("Klausurplan alle Jgst. (Nachschreiber)") },
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
