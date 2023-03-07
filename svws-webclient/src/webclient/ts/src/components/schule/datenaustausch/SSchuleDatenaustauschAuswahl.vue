<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a title="Schule" @click="returnToSchule">Schule</a>
				<span title="Benutzer">Datenaustausch</span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-data-table :items="[{Daten:'Laufbahnplanung'}, {Daten: 'Kurs 42'}]" :footer="false" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { SchuleDatenaustauschAuswahlProps } from './SSchuleDatenaustauschAuswahlProps';

	const props = defineProps<SchuleDatenaustauschAuswahlProps>();

</script>
