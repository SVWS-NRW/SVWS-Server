<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToKataloge">Kataloge</a>
				<span title="Pausenzeiten">Pausenzeiten</span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-data-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="mapKatalogeintraege().values()" :columns="cols" selectable v-model="selected">
					<template #cell(wochentag)="{ rowData }">
						{{ Wochentag.fromIDorException(rowData.wochentag).beschreibung }}
					</template>
					<template #cell(beginn)="{ rowData }">
						{{ rowData.beginn }}
					</template>
					<template #cell(ende)="{ rowData }">
						{{ rowData.ende }}
					</template>
					<template #footerActions>
						<div v-if="selected.length > 0" class="flex items-center justify-end pr-1 h-full">
							<svws-ui-button @click="doDeleteEintraege()" type="trash" class="cursor-pointer"
								:disabled="selected.length === 0" />
						</div>
						<s-pausenzeit-neu-modal v-slot="{ openModal }" :add-pausenzeit="addEintrag">
							<svws-ui-button type="icon" @click="openModal()">
								<i-ri-add-line />
							</svws-ui-button>
						</s-pausenzeit-neu-modal>
					</template>
				</svws-ui-data-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { PausenzeitenAuswahlProps } from "./SPausenzeitenAuswahlProps";
	import type { StundenplanPausenzeit } from "@core";
	import { Wochentag } from "@core";
	import { ref } from "vue";

	const props = defineProps<PausenzeitenAuswahlProps>();
	const selected = ref<StundenplanPausenzeit[]>([]);

	const cols = [
		{key: 'wochentag', label: 'Wochentag', span: 2}, {key: 'beginn', label: 'Beginn', span: 1}, {key: 'ende', label: 'Ende', span: 1}
	]

	async function doDeleteEintraege() {
		await props.deleteEintraege(selected.value);
		selected.value = [];
	}

</script>
