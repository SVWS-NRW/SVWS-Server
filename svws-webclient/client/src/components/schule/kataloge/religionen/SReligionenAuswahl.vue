<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Religionen</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #header />
		<template #content>
			<svws-ui-table :clicked @update:clicked="gotoEintrag" :items="religionListeManager().filtered()" :columns
				clickable selectable v-model="selected" :filter-open="true" scroll-into-view>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="filterNurSichtbare">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions>
					<svws-ui-button @click="doDeleteEintraege()" type="trash" :disabled="selected.length === 0" />
					<s-religionen-neu-modal v-slot="{ openModal }" :add-eintrag :schuljahr>
						<svws-ui-button type="icon" @click="openModal()" :has-focus="religionListeManager().filtered().isEmpty()">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
					</s-religionen-neu-modal>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { ReligionEintrag } from "@core";
	import type { ReligionenAuswahlProps } from "./SReligionenAuswahlPops";

	const props = defineProps<ReligionenAuswahlProps>();

	const schuljahr = computed<number>(() => props.religionListeManager().getSchuljahr());

	const selected = ref<ReligionEintrag[]>([]);

	const clicked = computed<ReligionEintrag | undefined>(() => {
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
