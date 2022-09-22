<template>
	<table v-if="daten">
		<thead>
			<tr>
				<td>Fachbezeichnug</td>
				<td>Lehrer</td>
				<td>Note</td>
			</tr>
		</thead>
		<tbody>
			<s-leistungsdaten-fach
				v-for="(fach, i) of leistungsdaten"
				:key="i"
				:fach="fach"
			/>
		</tbody>
	</table>
</template>

<script setup lang="ts">
	import {
		SchuelerLeistungsdaten,
		SchuelerLernabschnittsdaten,
		Vector
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const data = computed(() => {
		return {
			data: null
		};
	});

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	const daten: ComputedRef<SchuelerLernabschnittsdaten> = computed(() => {
		return (
			app.dataSchuelerAbschnittsdaten.daten ||
			new SchuelerLernabschnittsdaten()
		);
	});

	const leistungsdaten: ComputedRef<Array<SchuelerLeistungsdaten>> = computed(
		() => {
			let datenArray = new Array<SchuelerLeistungsdaten>();
			if (daten.value) {
				datenArray =
					daten.value.leistungsdaten.toArray(
						new Array<SchuelerLeistungsdaten>()
					) || [];
			}
			return datenArray;
		}
	);

	const leistungsdatenV: ComputedRef<Vector<SchuelerLeistungsdaten>> =
		computed<Vector<SchuelerLeistungsdaten>>(() => {
			let dataVector: Vector<SchuelerLeistungsdaten> =
				new Vector<SchuelerLeistungsdaten>();
			if (daten.value) {
				dataVector =
					daten.value.leistungsdaten ||
					new Vector<SchuelerLeistungsdaten>();
			}
			return dataVector;
		});

	const visible: ComputedRef<boolean> = computed<boolean>(() => {
		//return this.$app.leistungsdaten.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});
</script>
