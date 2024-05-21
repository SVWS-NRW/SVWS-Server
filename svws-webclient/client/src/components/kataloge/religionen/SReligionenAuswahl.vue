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
			<svws-ui-table :clicked="auswahl" @update:clicked="gotoEintrag" :items="religionListeManager().filtered()" :columns="columns"
				clickable selectable v-model="selected" :filter-open="true">
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="filterNurSichtbare">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions>
					<svws-ui-button @click="doDeleteEintraege()" type="trash" :disabled="selected.length === 0" />
					<s-religionen-neu-modal v-slot="{ openModal }" :add-eintrag="addEintrag">
						<svws-ui-button type="icon" @click="openModal()">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
					</s-religionen-neu-modal>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { ReligionEintrag } from "@core";
	import type { ReligionenAuswahlProps } from "./SReligionenAuswahlPops";
	import { computed, ref } from "vue";

	const props = defineProps<ReligionenAuswahlProps>();
	const selected = ref<ReligionEintrag[]>([]);

	const auswahl = computed<ReligionEintrag | undefined>(() => {
		const manager = props.religionListeManager();
		return manager.hasDaten() ? manager.auswahl() : undefined;
	});

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "text", label: "Bezeichnung", sortable: true, span: 3 }
	];

	const filterNurSichtbare = computed<boolean>({
		get: () => props.religionListeManager().filterNurSichtbar(),
		set: (value) => {
			props.religionListeManager().setFilterNurSichtbar(value);
			void props.setFilter();
		}
	});

	async function doDeleteEintraege() {
		await props.deleteEintraege(selected.value);
		selected.value = [];
	}

</script>
