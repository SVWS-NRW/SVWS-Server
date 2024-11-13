<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span> Vermerkarten </span>
		</template>
		<template #abschnitt>
			<!-- <abschnitt-auswahl :daten="schuljahresabschnittsauswahl" /> -->
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-table clickable :clicked="clickedEintrag" @update:clicked="gotoEintrag" :items="props.vermerkartenManager().liste.list()" :columns selectable
					:model-value="[...props.vermerkartenManager().liste.auswahl()]"
					@update:model-value="items => setAuswahl(items)" scroll-into-view enable-focus-switching>
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
						<svws-ui-button @click="doDeleteEintraege()" type="trash" :disabled="[...props.vermerkartenManager().liste.auswahl()].length === 0 || deleteCandidates.length === 0" />
						<s-vermerke-neu-modal v-slot="{ openModal }" :add-eintrag :vermerkarten-manager>
							<svws-ui-button type="icon" @click="openModal()" :has-focus="props.vermerkartenManager().liste.size() === 0">
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

	import { computed } from "vue";
	import type { VermerkeAuswahlProps } from "./SVermerkeAuswahlProps";
	import type { VermerkartEintrag } from "@core";
	import type { DataTableColumn } from "@ui";

	const props = defineProps<VermerkeAuswahlProps>();

	const columns : DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc", span: 2},
		{ key: "anzahlVermerke", label: "Anzahl", sortable: true, defaultSort: "asc", span: 1, align: "right"},
	];

	async function setAuswahl(items : VermerkartEintrag[] ) {
		props.vermerkartenManager().liste.auswahlClear();
		for (const item of items) {
			props.vermerkartenManager().liste.auswahlAdd(item);
		}

		if (props.vermerkartenManager().auswahlID() !== null)
			await props.commit();
	}

	const deleteCandidates = computed(() => [... props.vermerkartenManager().liste.auswahl()].filter(elem => elem.anzahlVermerke === 0));

	async function doDeleteEintraege() {
		await props.deleteEintraege(deleteCandidates.value);
		props.vermerkartenManager().liste.auswahlClear();
	}

	const clickedEintrag = computed(() => {
		return props.vermerkartenManager().liste.auswahlExists() ? null : props.vermerkartenManager().hasDaten() ? props.vermerkartenManager().auswahl() : null;
	})

	function isRemovable(rowData: VermerkartEintrag) {
		return [... props.vermerkartenManager().liste.auswahl()].includes(rowData) && (rowData.anzahlVermerke > 0);
	}

</script>
