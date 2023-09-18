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
				<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="mapKatalogeintraege().values()" :columns="cols" selectable v-model="selected">
					<template #cell(wochentag)="{ rowData }">
						{{ Wochentag.fromIDorException(rowData.wochentag).beschreibung }}
					</template>
					<template #cell(beginn)="{ rowData }">
						{{ rowData.beginn }}
					</template>
					<template #cell(ende)="{ rowData }">
						{{ rowData.ende }}
					</template>
					<template #actions>
						<svws-ui-button @click="doDeleteEintraege()" type="trash" :disabled="selected.length === 0" />
						<s-pausenzeit-neu-modal v-slot="{ openModal }" :add-pausenzeit="addEintrag">
							<svws-ui-button type="icon" @click="openModal()">
								<i-ri-add-line />
							</svws-ui-button>
						</s-pausenzeit-neu-modal>
					</template>
				</svws-ui-table>
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
