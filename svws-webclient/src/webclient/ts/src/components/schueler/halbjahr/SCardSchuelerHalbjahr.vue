<template>
	<svws-ui-content-card title="Allgemeine Angaben">
		<div class="input-wrapper">
			<svws-ui-multi-select
				v-model="inputSchuljahr"
				title="Schuljahr"
				:items="inputSchuljahre"
				:item-filter="schuljahr_filter"
				:item-sort="schuljahr_sort"
				:item-text="i => `${i.schuljahr}`"
				autocomplete
			/>
			<svws-ui-multi-select
				v-model="inputAbschnitt"
				title="Abschnitt"
				:items="inputAbschnitte"
				:item-filter="abschnitt_filter"
				:item-sort="abschnitt_sort"
				:item-text="i => `${i.abschnitt}`"
				autocomplete
			/>

			<svws-ui-multi-select
				v-model="inputKlasse"
				title="Klasse"
				:items="inputKlasse"
				:item-filter="klasse_filter"
				:item-sort="klasse_sort"
				:item-text="i => `${i.klasse}`"
				autocomplete
			/>
			<svws-ui-multi-select
				v-model="inputJahrgang"
				title="Jahrgang"
				:items="inputJahrgange"
				:item-filter="jahrgang_filter"
				:item-sort="jahrgang_sort"
				:item-text="i => `${i.jahrgang}`"
				autocomplete
			/>

			<svws-ui-text-input
				v-model="inputDatumVon"
				type="date"
				placeholder="Datum von"
			/>
			<svws-ui-text-input
				v-model="inputDatumBis"
				type="date"
				placeholder="Datum bis"
			/>

			<div class="input-wrapper-3-cols">
				<svws-ui-multi-select
					v-model="inputKlassenlehrer"
					title="Klassenlehrer / Tutor"
					:items="inputKlassenlehrer"
					:item-filter="klassenlehrer_filter"
					:item-sort="klassenlehrer_sort"
					:item-text="i => `${i.klassenlehrer}`"
					autocomplete
				/>
				<svws-ui-multi-select
					v-model="inputStellvertreter"
					title="Stellvertreter"
					:items="inputStellvertreter"
					:item-filter="stellvertreter_filter"
					:item-sort="stellvertreter_sort"
					:item-text="i => `${i.stellvertreter}`"
					autocomplete
				/>
				<svws-ui-multi-select
					v-model="inputPaedagoge"
					title="Sonderpädagoge"
					:items="inputPaedagogee"
					:item-filter="paedagoge_filter"
					:item-sort="paedagoge_sort"
					:item-text="i => `${i.paedagoge}`"
					autocomplete
				/>

				<svws-ui-text-input
					v-model="inputFehlstundenMax"
					type="number"
					placeholder="Maximale Fehlstunden"
				/>
				<svws-ui-text-input
					v-model="inputFehlstundenGesamt"
					type="number"
					placeholder="Fehlstunden Gesamt"
				/>
				<svws-ui-text-input
					v-model="inputFehlstundenUnentschuldigt"
					type="number"
					placeholder="Unentschuldigte Fehlstunden"
				/>
			</div>

			<svws-ui-multi-select
				v-model="inputSchulgliederung"
				title="Schulgliederung"
				:items="inputSchulgliederung"
				:item-filter="schulgliederung_filter"
				:item-sort="schulgliederung_sort"
				:item-text="i => `${i.schulgliederung}`"
				autocomplete
			/>
			<svws-ui-multi-select
				v-model="inputPruefungsordnung"
				title="Prüfungsordnung"
				:items="inputPruefungsordnung"
				:item-filter="pruefungsordnung_filter"
				:item-sort="pruefungsordnung_sort"
				:item-text="i => `${i.pruefungsordnung}`"
				autocomplete
			/>

			<svws-ui-multi-select
				v-model="inputOrganisationsform"
				title="Organisationsform"
				:items="inputOrganisationsform"
				:item-filter="organisationsform_filter"
				:item-sort="organisationsform_sort"
				:item-text="i => `${i.organisationsform}`"
				autocomplete
			/>
			<svws-ui-multi-select
				v-model="inputKlassenart"
				title="Prüfungsordnung"
				:items="inputKlassenart"
				:item-filter="klassenart_filter"
				:item-sort="klassenart_sort"
				:item-text="i => `${i.klassenart}`"
				autocomplete
			/>

			<svws-ui-multi-select
				v-model="inputFoerderschwerpunkt"
				title="Förderschwerpunkt"
				:items="inputFoerderschwerpunkt"
				:item-filter="foerderschwerpunkt_filter"
				:item-sort="foerderschwerpunkt_sort"
				:item-text="i => `${i.foerderschwerpunkt}`"
				autocomplete
			/>
			<svws-ui-multi-select
				v-model="inputWeitererFoerderschwerpunkt"
				title="Weiterer Förderschwerpunkt"
				:items="inputWeitererFoerderschwerpunkte"
				:item-filter="weitererFoerderschwerpunkt_filter"
				:item-sort="weitererFoerderschwerpunkt_sort"
				:item-text="i => `${i.weitererFoerderschwerpunkt}`"
				autocomplete
			/>

			<div class="col-span-2">
				<svws-ui-checkbox v-model="inputSchwerstbehinderung"
					>Schwerstbehinderung</svws-ui-checkbox
				>
			</div>

			<svws-ui-text-input
				v-model="inputLernbereichsnoteGesellschaftswissenschaft"
				type="text"
				placeholder="Lernbereichsnote Gesellschaftswissenschaft"
			/>
			<svws-ui-text-input
				v-model="inputLernbereichsnoteNaturwissenschaft"
				type="text"
				placeholder="Lernbereichsnote Naturwissenschaft"
			/>
			<div class="col-span-2">
				<svws-ui-text-input
					v-model="inputNachpruefungsfaecher"
					type="text"
					placeholder="mögliche Nachprüfungsfächer"
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
