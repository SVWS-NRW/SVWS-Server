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
		{ text: "Klausurplan akutelle Jgst.", action: () => downloadPDF("Klausurplan aktuelle Jgst."), default: true },
		{ text: "Klausurplan alle Jgst.", action: () => downloadPDF("Klausurplan alle Jgst.") },
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
