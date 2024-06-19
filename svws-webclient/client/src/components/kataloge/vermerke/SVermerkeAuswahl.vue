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
						<svws-ui-button @click="doDeleteEintraege()" type="trash" :disabled="selected.length === 0 || deleteCandidates.length === 0" />
						<s-vermerke-neu-modal v-slot="{ openModal }" :add-eintrag :map-katalogeintraege>
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
	import type { DataTableColumn } from "@ui";
	import { computed, ref } from "vue";

	const props = defineProps<VermerkeAuswahlProps>();
	const selected = ref<VermerkartEintrag[]>([]);

	const cols: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc", span: 2},
		{ key: "anzahlVermerke", label: "Anzahl", sortable: true, defaultSort: "asc", span: 1, align: "right"},
	];

	const deleteCandidates = computed(() => selected.value.filter(elem => (elem.anzahlVermerke === 0)));

	async function doDeleteEintraege() {
		await props.deleteEintraege(deleteCandidates.value);
		selected.value = [];
	}

	function isRemovable(rowData: any) {
		return selected.value.includes(rowData) && (rowData.anzahlVermerke > 0);
	}

</script>

<style lang="postcss" scoped>

</style>
