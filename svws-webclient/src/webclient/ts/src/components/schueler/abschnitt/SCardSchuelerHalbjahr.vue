<template>
	<svws-ui-content-card title="Allgemeine Angaben">
		<div class="input-wrapper">
			<svws-ui-multi-select title="Schuljahr" v-model="inputSchuljahr" :items="inputSchuljahre" :item-filter="schuljahr_filter" 
				:item-sort="schuljahr_sort" :item-text="i => `${i.schuljahr}`" autocomplete />
			<svws-ui-multi-select title="Abschnitt" v-model="inputAbschnitt" :items="inputAbschnitte" :item-filter="abschnitt_filter"
				:item-sort="abschnitt_sort" :item-text="i => `${i.abschnitt}`" autocomplete />

			<svws-ui-multi-select title="Klasse" v-model="inputKlasse" :items="inputKlasse" :item-filter="klasse_filter" 
				:item-sort="klasse_sort" :item-text="i => `${i.klasse}`" autocomplete />
			<svws-ui-multi-select title="Jahrgang" v-model="inputJahrgang" :items="inputJahrgange" :item-filter="jahrgang_filter"
				:item-sort="jahrgang_sort" :item-text="i => `${i.jahrgang}`" autocomplete />

			<svws-ui-text-input placeholder="Datum von" v-model="inputDatumVon" type="date" />
			<svws-ui-text-input placeholder="Datum bis" v-model="inputDatumBis" type="date" />

			<div class="input-wrapper-3-cols">
				<svws-ui-multi-select title="Klassenlehrer / Tutor" v-model="inputKlassenlehrer" :items="inputKlassenlehrer" :item-filter="klassenlehrer_filter"
					:item-sort="klassenlehrer_sort" :item-text="i => `${i.klassenlehrer}`" autocomplete />
				<svws-ui-multi-select title="Stellvertreter" v-model="inputStellvertreter" :items="inputStellvertreter" :item-filter="stellvertreter_filter"
					:item-sort="stellvertreter_sort" :item-text="i => `${i.stellvertreter}`" autocomplete />
				<svws-ui-multi-select title="Sonderpädagoge" v-model="inputPaedagoge" :items="inputPaedagogee" :item-filter="paedagoge_filter"
					:item-sort="paedagoge_sort" :item-text="i => `${i.paedagoge}`" autocomplete />
				<svws-ui-text-input placeholder="Maximale Fehlstunden" v-model="inputFehlstundenMax" type="number" />
				<svws-ui-text-input placeholder="Fehlstunden Gesamt" v-model="inputFehlstundenGesamt" type="number" />
				<svws-ui-text-input placeholder="Fehlstunden Gesamt" v-model="inputFehlstundenUnentschuldigt" type="number" />
			</div>

			<svws-ui-multi-select title="Schulgliederung" v-model="inputSchulgliederung" :items="inputSchulgliederung" :item-filter="schulgliederung_filter"
				:item-sort="schulgliederung_sort" :item-text="i => `${i.schulgliederung}`" autocomplete />
			<svws-ui-multi-select title="Prüfungsordnung" v-model="inputPruefungsordnung" :items="inputPruefungsordnung" :item-filter="pruefungsordnung_filter"
				:item-sort="pruefungsordnung_sort" :item-text="i => `${i.pruefungsordnung}`" autocomplete />
			<svws-ui-multi-select title="Organisationsform" v-model="inputOrganisationsform" :items="inputOrganisationsform" :item-filter="organisationsform_filter"
				:item-sort="organisationsform_sort" :item-text="i => `${i.organisationsform}`" autocomplete />
			<svws-ui-multi-select title="Prüfungsordnung" v-model="inputKlassenart" :items="inputKlassenart" :item-filter="klassenart_filter"
				:item-sort="klassenart_sort" :item-text="i => `${i.klassenart}`" autocomplete />

			<svws-ui-multi-select title="Förderschwerpunkt" v-model="inputFoerderschwerpunkt" :items="inputFoerderschwerpunkt" :item-filter="foerderschwerpunkt_filter"
				:item-sort="foerderschwerpunkt_sort" :item-text="i => `${i.foerderschwerpunkt}`" autocomplete />
			<svws-ui-multi-select title="Weiterer Förderschwerpunkt" v-model="inputWeitererFoerderschwerpunkt" :items="inputWeitererFoerderschwerpunkte" :item-filter="weitererFoerderschwerpunkt_filter" 
				:item-sort="weitererFoerderschwerpunkt_sort" :item-text="i => `${i.weitererFoerderschwerpunkt}`" autocomplete />

			<div class="col-span-2">
				<svws-ui-checkbox v-model="inputSchwerstbehinderung"> Schwerstbehinderung </svws-ui-checkbox>
			</div>

			<svws-ui-text-input placeholder="Lernbereichsnote Gesellschaftswissenschaft" v-model="inputLernbereichsnoteGesellschaftswissenschaft" type="text" />
			<svws-ui-text-input placeholder="Lernbereichsnote Naturwissenschaft" v-model="inputLernbereichsnoteNaturwissenschaft" type="text" />
			<div class="col-span-2">
				<svws-ui-text-input placeholder="mögliche Nachprüfungsfächer" v-model="inputNachpruefungsfaecher" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

</script>
