<template>
	<tr
		class="cursor-pointer border border-[#7f7f7f]/20 px-2 text-left"
		:class="{
			'bg-red-400': schiene_hat_kollisionen
		}"
	>
		<td class="bg-slate-100 px-2">
			{{ schiene_g?.bezeichnung }} <br />
			{{ schiene.kurse.size() }} Kurse <br />
			{{ anzahl_schueler }} Schüler
		</td>
		<s-kurs-schueler-schiene-kurs
			v-for="kurs of getSchieneKurse"
			:key="kurs.id"
			:kurs="kurs"
			:schueler="selected"
		/>
	</tr>
</template>

<script setup lang="ts">
	import {
		GostBlockungSchiene,
		GostBlockungsergebnisKurs,
		GostBlockungsergebnisManager,
		GostBlockungsergebnisSchiene,
		SchuelerListeEintrag,
		Vector
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps({
		schiene: {
			type: GostBlockungsergebnisSchiene,
			required: true
		},
		selected: {
			type: SchuelerListeEintrag,
			required: true
		}
	});

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> =
		computed(() => app.dataKursblockungsergebnis.manager);

	const anzahl_schueler: ComputedRef<number> =
		computed(() => manager.value?.getOfSchieneAnzahlSchueler(props.schiene.id) || 0);

	const schiene_g: ComputedRef<GostBlockungSchiene | undefined> =
		computed(() => manager.value?.getSchieneG(props.schiene.id))

	const schiene_hat_kollisionen: ComputedRef<boolean> =
		computed(() => manager.value?.getOfSchieneHatKollision(props.schiene.id) || false)

	const getSchieneKurse: ComputedRef<Vector<GostBlockungsergebnisKurs>> =
		computed(()=> props.schiene.kurse)
</script>
