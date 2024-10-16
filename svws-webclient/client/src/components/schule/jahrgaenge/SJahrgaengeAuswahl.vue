<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Jahrgänge</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-table :clicked="auswahl()" clickable @update:clicked="gotoEintrag" :items="mapKatalogeintraege().values()" :columns scroll-into-view>
					<template #cell(bezeichnung)="{ value, rowData }">
						<div class="flex flex-row w-full">
							<div class="flex-grow text-ellipsis overflow-hidden whitespace-nowrap">
								<svws-ui-tooltip :title="value">
									{{ value }}
								</svws-ui-tooltip>
							</div>
							<div v-if="rowData.anzahlRestabschnitte === null" class="flex-none text-error">
								<svws-ui-tooltip title="Der Jahrgang hat keine Anzahl für die Restabschnitte definiert.">
									<span class="icon i-ri-alert-line" />
								</svws-ui-tooltip>
							</div>
						</div>
					</template>
					<!-- TODO: Beim Implementieren des '+'-Buttons zum Hinfügen eines Eintrags die property hasFocus auf die svws-ui-button-Komponente setzen. true, wenn Liste leer, sonst false (z.B. :hasFocus="rowsFiltered.length === 0") -->
				</svws-ui-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { JahrgaengeAuswahlProps } from "./SJahrgaengeAuswahlProps";

	const props = defineProps<JahrgaengeAuswahlProps>();

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 }
	];

</script>
