<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToKataloge">Kataloge</a>
				<span>Religionen</span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-data-table :clicked="auswahl" @update:clicked="gotoEintrag" :items="mapKatalogeintraege.values()"
					:columns="cols" clickable :footer="true">
					<template #footerActions>
						<s-religionen-neu-modal v-slot="{ openModal }" :add-eintrag="addEintrag">
							<button @click="openModal()" class="flex h-10 w-10 items-center justify-center">
								<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
							</button>
						</s-religionen-neu-modal>
					</template>
				</svws-ui-data-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { DataTableColumn } from "@ui";
	import { ReligionenAuswahlProps } from "./SReligionenAuswahlPops";

	const props = defineProps<ReligionenAuswahlProps>();

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "text", label: "Bezeichnung", sortable: true, span: 2 }
	];
</script>
