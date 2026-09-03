<template>
	<div class="page page-grid-cards">
		<VermerkartenNotify class="col-span-full font-bold text-center border rounded-sm p-2 text-ui-caution" />
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="2"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						@change="model.patch"
						:max-len="30" required />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@change="model.patch"
						:min="0" :max="32000"
						:readonly
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
		<svws-ui-content-card class="w-full" :title="'Alle Schüler mit der Vermerkart `' + manager().auswahl().bezeichnung + '`'"
			v-if="manager().schuelerVermerkartZusammenfassungen.size() > 0">
			<svws-ui-table class="w-full" :columns :items="manager().schuelerVermerkartZusammenfassungen">
				<template #cell(status)="{ value }: { value: number}">
					<span :class="{'opacity-25': value === 2}">{{ value }}</span>
				</template>
				<template #header(linkToSchueler)>
					<span class="icon i-ri-group-line" />
				</template>
				<template #cell(linkToSchueler)="{ rowData }">
					<button type="button" @click.stop="gotoSchueler(rowData)" class="button button--icon" title="Schüler ansehen">
						<span class="icon i-ri-link" />
					</button>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { useBenutzerState, type DataTableColumn } from "@ui";
	import type { VermerkartenDatenProps } from "./VermerkartenDatenProps";
	import { VermerkartenModelProxy } from "~/components/schule/kataloge/vermerkarten/modelproxy/VermerkartenModelProxy";
	import { BenutzerKompetenz } from "@core";
	import { computed } from "vue";

	const props = defineProps<VermerkartenDatenProps>();
	const benutzerState = useBenutzerState();

	const model = new VermerkartenModelProxy(() => props.manager().daten(), props.manager, props.patch);
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzAdd.value);

	const columns: DataTableColumn[] = [
		{ key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center" },
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Rufname", sortable: true },
		{ key: "anzahlVermerke", label: "Anzahl", fixedWidth: 8.75, sortable: true, span: 0.5 },
	];

</script>
