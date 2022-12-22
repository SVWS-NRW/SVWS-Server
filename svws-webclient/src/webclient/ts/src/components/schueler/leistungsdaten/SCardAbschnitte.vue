<template>
	<table>
		<thead>
			<tr>
				<td>Abschnitt</td>
			</tr>
		</thead>
		<tbody>
			<tr v-for="r of rows" :key="r.id" @click="app.listAbschnitte.ausgewaehlt = r" >
				<td>
					{{ abschnitt(r)?.schuljahr }},
					{{ abschnitt(r)?.abschnitt }}. Halbjahr
				</td>
			</tr>
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import { SchuelerLernabschnittListeEintrag, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";
	import { computed } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	const appSchule = main.apps.schule;

	const rows = computed<SchuelerLernabschnittListeEintrag[]>(() => {
		return app.listAbschnitte.liste;
	});

	const schule_abschnitte = computed<Array<Schuljahresabschnitt>>((): Array<Schuljahresabschnitt> => {
			return appSchule.schuleStammdaten.daten?.abschnitte.toArray(new Array<Schuljahresabschnitt>()) || [];
	});

	const abschnitt = (a: SchuelerLernabschnittListeEintrag): Schuljahresabschnitt | undefined => {
		return schule_abschnitte.value.find(s => s.id === a.schuljahresabschnitt);
	};

	const visible = computed<boolean>(() => {
		//TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
