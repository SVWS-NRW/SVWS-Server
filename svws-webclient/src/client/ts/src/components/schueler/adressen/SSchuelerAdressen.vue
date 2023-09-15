<template>
	<div class="page--content">
		<div class="col-span-full">
			<template v-if="(betriebsStammdaten !== undefined) && (betrieb !== undefined)">
				<s-card-schueler-beschaeftigung :list-schuelerbetriebe="listSchuelerbetriebe" :map-beschaeftigungsarten="mapBeschaeftigungsarten"
					:map-lehrer="mapLehrer" :map-betriebe="mapBetriebe" :map-ansprechpartner="mapAnsprechpartner" 
					:patch-schueler-betriebsdaten="patchSchuelerBetriebsdaten" :set-schueler-betrieb="setSchuelerBetrieb" />
			</template>
			<template v-else>
				<svws-ui-content-card title="Beschäftigungen">
					<svws-ui-data-table :items="[]" :columns="cols" no-data-html="Noch kein Schülerbetrieb vorhanden.">
						<template #header(Anschreiben)>
							<svws-ui-tooltip>
								<i-ri-mail-send-line />
								<template #content>
									Erhält Anschreiben
								</template>
							</svws-ui-tooltip>
						</template>
					</svws-ui-data-table>
				</svws-ui-content-card>
			</template>
			<s-card-schueler-add-adresse-modal :id-schueler="idSchueler" :map-beschaeftigungsarten="mapBeschaeftigungsarten"
				:map-lehrer="mapLehrer" :map-betriebe="mapBetriebe" :map-ansprechpartner="mapAnsprechpartner"
				:create-schueler-betriebsdaten="createSchuelerBetriebsdaten" v-slot="{ openModal }">
				<svws-ui-button @click="openModal()" class="mt-4">Betrieb hinzufügen</svws-ui-button>
			</s-card-schueler-add-adresse-modal>
			<template v-if="(betriebsStammdaten !== undefined) && (betrieb !== undefined)">
				<s-card-schueler-adresse :betriebs-stammdaten="betriebsStammdaten" :betrieb="betrieb" :map-orte="mapOrte"
					:map-lehrer="mapLehrer" :map-ansprechpartner="mapAnsprechpartner" :create-ansprechpartner="createAnsprechpartner"
					:patch-schueler-betriebsdaten="patchSchuelerBetriebsdaten" :patch-betrieb="patchBetrieb" :patch-ansprechpartner="patchAnsprechpartner" />
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { DataTableColumn } from "@ui";
	import type { SchuelerAdressenProps } from "./SSChuelerAdressenProps";

	defineProps<SchuelerAdressenProps>();

	const cols: DataTableColumn[] = [
		{ key: "Betrieb", label: "Betrieb"},
		{ key: "Ausbilder", label: "Ausbilder"},
		{ key: "Beschäftigungsart", label: "Beschäftigungsart"},
		{ key: "Beginn", label: "Beginn", span: 0.5},
		{ key: "Ende", label: "Ende", span: 0.5},
		{ key: "Praktikum", label: "Praktikum", span: 0.25, tooltip: 'Praktikum', align: "center"},
		{ key: "Betreuungslehrer", label: "Betreuungslehrer"},
		{ key: "Ansprechpartner", label: "Ansprechpartner"},
		{ key: "Anschreiben", label: "Anschreiben", tooltip: "Betrieb erhält Anschreiben", fixedWidth: 3, align: "center"}
	];
</script>
