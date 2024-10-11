<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Fächer</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #header />
		<template #content>
			<svws-ui-table :clicked="fachListeManager().auswahl()" clickable @update:clicked="gotoEintrag" :items="fachListeManager().filtered()" :columns :filter-open="true" scroll-into-view>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="filterNurSichtbare">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions>
					<template v-if="fachListeManager().schulform().daten(schuljahr)?.hatGymOb ?? false">
						<s-faecher-auswahl-sortierung-sek-i-i-modal v-slot="{ openModal }" :setze-default-sortierung-sek-i-i :set-filter>
							<svws-ui-button type="secondary" @click="openModal">Standardsortierung Sek II anwenden …</svws-ui-button>
						</s-faecher-auswahl-sortierung-sek-i-i-modal>
					</template>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { FaecherAuswahlProps } from "./SFaecherAuswahlProps";

	const props = defineProps<FaecherAuswahlProps>();

	const schuljahr = computed(() => props.schuljahresabschnittsauswahl().aktuell.schuljahr)

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 }
	];

	const filterNurSichtbare = computed<boolean>({
		get: () => props.fachListeManager().filterNurSichtbar(),
		set: (value) => {
			props.fachListeManager().setFilterNurSichtbar(value);
			void props.setFilter();
		}
	});

</script>
