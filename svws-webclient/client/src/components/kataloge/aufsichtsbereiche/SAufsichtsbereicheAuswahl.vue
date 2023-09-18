<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToKataloge">Kataloge</a>
				<span title="Aufsichtsbereiche">Aufsichtsbereiche</span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="mapKatalogeintraege().values()" :columns="cols" selectable v-model="selected">
					<template #actions>
						<svws-ui-button @click="doDeleteEintraege()" type="trash" :disabled="selected.length === 0" />
						<s-aufsichtsbereich-neu-modal v-slot="{ openModal }" :add-aufsichtsbereich="addEintrag">
							<svws-ui-button type="icon" @click="openModal()">
								<i-ri-add-line />
							</svws-ui-button>
						</s-aufsichtsbereich-neu-modal>
					</template>
				</svws-ui-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { AufsichtsbereicheAuswahlProps } from "./SAufsichtsbereicheAuswahlProps";
	import type { Aufsichtsbereich } from "@core";
	import { ref } from "vue";

	const props = defineProps<AufsichtsbereicheAuswahlProps>();
	const selected = ref<Aufsichtsbereich[]>([]);

	const cols = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc', span: 0.5 },
		{ key: "beschreibung", label: "Beschreibung", sortable: true },
	];

	async function doDeleteEintraege() {
		await props.deleteEintraege(selected.value);
		selected.value = [];
	}

</script>
