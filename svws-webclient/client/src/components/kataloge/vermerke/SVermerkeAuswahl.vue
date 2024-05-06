<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToKataloge">Kataloge</a>
				<span>Vermerkarten </span>
			</nav>
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-table :clicked="auswahl" @update:clicked="gotoEintrag" :items="mapKatalogeintraege.values()" :columns="cols" clickable selectable v-model="selected">
					<template #actions>
						<svws-ui-button @click="doDeleteEintraege()" type="trash" :disabled="selected.length === 0" />
						<s-vermerke-neu-modal v-slot="{ openModal }" :add-eintrag="addEintrag">
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
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc"}
	];

	async function doDeleteEintraege() {
		await props.deleteEintraege(selected.value);
		selected.value = [];
	}
</script>
