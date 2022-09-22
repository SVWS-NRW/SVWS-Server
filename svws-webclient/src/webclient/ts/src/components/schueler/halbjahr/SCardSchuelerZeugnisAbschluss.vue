<template>
	<svws-ui-content-card title="Zeugnis / Abschluss">
		<div class="input-wrapper">
			<svws-ui-text-input
				v-model="inputDatumKonferenz"
				type="date"
				placeholder="Konferenz-Datum"
			/>
			<svws-ui-text-input
				v-model="inputDatumZeugnis"
				type="date"
				placeholder="Zeugnis-Datum"
			/>

			<div class="input-wrapper-3-cols">
				<svws-ui-multi-select
					v-model="inputVersetzungsVermerk"
					title="Vers.-Vermerk"
					:items="inputVersetzungsVermerke"
					:item-filter="versetzungsVermerk_filter"
					:item-sort="versetzungsVermerk_sort"
					:item-text="i => `${i.versetzungsVermerk}`"
					autocomplete
				/>
				<svws-ui-multi-select
					v-model="inputAbschlussArt"
					title="Abschluss-Art"
					:items="inputAbschlussArt"
					:item-filter="abschlussArt_filter"
					:item-sort="abschlussArt_sort"
					:item-text="i => `${i.abschlussArt}`"
					autocomplete
				/>
				<svws-ui-multi-select
					v-model="inputFolgeklasse"
					title="Folgeklasse"
					:items="inputFolgeklassee"
					:item-filter="folgeklasse_filter"
					:item-sort="folgeklasse_sort"
					:item-text="i => `${i.folgeklasse}`"
					autocomplete
				/>
			</div>

			<svws-ui-multi-select
				v-model="inputAbschluss"
				title="Abschluss"
				:items="inputAbschluss"
				:item-filter="abschluss_filter"
				:item-sort="abschluss_sort"
				:item-text="i => `${i.abschluss}`"
				autocomplete
			/>
			<svws-ui-multi-select
				v-model="inputZeugnisArt"
				title="Zeugnis-Art"
				:items="inputZeugnisArte"
				:item-filter="zeugnisArt_filter"
				:item-sort="zeugnisArt_sort"
				:item-text="i => `${i.zeugnisArt}`"
				autocomplete
			/>

			<div class="col-span-2 flex gap-4">
				<svws-ui-button type="primary">
					Versetzungs-/Abschluss-Berechnung
				</svws-ui-button>

				<svws-ui-button type="secondary">
					Nachprüfungen
				</svws-ui-button>
			</div>

			<div class="col-span-2">
				<svws-ui-textarea-input
					v-model="inputZeugnisbemerkungen"
					placeholder="Zeugnisbemerkungen"
					resizeable="vertical"
				/>
			</div>
			<div class="col-span-2">
				<svws-ui-textarea-input
					v-model="inputArbeitsUndSozialverhalten"
					placeholder="Arbeits- und Sozialverhalten"
					resizeable="vertical"
				/>
			</div>
			<div class="col-span-2">
				<svws-ui-textarea-input
					v-model="inputAusserunterrichtlichesEngagement"
					placeholder="Außerunterrichtliches Engagement"
					resizeable="vertical"
				/>
			</div>
			<div class="col-span-2">
				<svws-ui-textarea-input
					v-model="inputBemerkungVersetzung"
					placeholder="Bemerkung Versetzung"
					resizeable="vertical"
				/>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
</script>
