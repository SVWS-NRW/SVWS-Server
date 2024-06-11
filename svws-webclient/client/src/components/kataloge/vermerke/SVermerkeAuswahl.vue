<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToKataloge"> Kataloge </a>
				<span> Vermerkarten </span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-table :clicked="auswahl" @update:clicked="gotoEintrag" :items="mapKatalogeintraege.values()" :columns="cols" clickable selectable v-model="selected">
					<template #cell(anzahlVermerke)="{ value, rowData }">
						<div class="inline-flex min-h-5">
							<div v-if="isRemovable(rowData)" class="inline-flex">
								<span class="icon i-ri-alert-line mx-0.5 mr-1" />
								<p>verwendet</p>
							</div>
							<p class="w-8"> {{ value }} </p>
						</div>
					</template>
					<template #actions>
						<svws-ui-button @click="doDeleteEintraege()" type="trash" :disabled="selected.length === 0" />
						<s-vermerke-neu-modal v-slot="{ openModal }" :add-eintrag="addEintrag" :vermerke="mapKatalogeintraege">
							<svws-ui-button type="icon" @click="openModal()">
								<span class="icon i-ri-add-line" />
							</svws-ui-button>
						</s-vermerke-neu-modal>
					</template>
				</svws-ui-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { VermerkeAuswahlProps } from "./SVermerkeAuswahlProps";
	import type { VermerkartEintrag } from "@core";
	import { ref } from "vue";

	const props = defineProps<VermerkeAuswahlProps>();
	const selected = ref<VermerkartEintrag[]>([]);

	const cols = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc", span: 2},
		{ key: "anzahlVermerke", label: "Anzahl", sortable: true, defaultSort: "asc", span: 1, align: "right"},
	];

	async function doDeleteEintraege() {
		const deleteCandidates = selected.value.filter(elem => (elem.anzahlVermerke === 0));
		if (deleteCandidates.length === 0) {
			throw Error("Alle ausgwählten Vermerke werden verwendet und können nicht gelöscht werden")
		} else {
			await props.deleteEintraege(deleteCandidates);
			selected.value = [];
		}
	}

	const isRemovable = (rowData: any) => {
		return selected.value.includes(rowData) && (rowData.anzahlVermerke > 0)
	}

</script>

<style lang="postcss" scoped>

</style>
