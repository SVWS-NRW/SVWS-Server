<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToKataloge">Kataloge</a>
				<span>Religionen</span>
			</nav>
		</template>
		<template #abschnitt>
			<span class="text-base font-bold opacity-50 select-none">{{ aktAbschnitt.schuljahr + "." + aktAbschnitt.abschnitt }}</span>
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-table :clicked="auswahl" @update:clicked="gotoEintrag" :items="mapKatalogeintraege.values()" :columns="cols" clickable selectable v-model="selected">
					<template #actions>
						<svws-ui-button @click="doDeleteEintraege()" type="trash" :disabled="selected.length === 0" />
						<s-religionen-neu-modal v-slot="{ openModal }" :add-eintrag="addEintrag">
							<svws-ui-button type="icon" @click="openModal()">
								<span class="icon i-ri-add-line" />
							</svws-ui-button>
						</s-religionen-neu-modal>
					</template>
				</svws-ui-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { ReligionEintrag } from "@core";
	import type { ReligionenAuswahlProps } from "./SReligionenAuswahlPops";
	import { ref } from "vue";

	const props = defineProps<ReligionenAuswahlProps>();
	const selected = ref<ReligionEintrag[]>([]);

	const cols = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "text", label: "Bezeichnung", sortable: true, span: 2 }
	];

	async function doDeleteEintraege() {
		await props.deleteEintraege(selected.value);
		selected.value = [];
	}
</script>
