<template>
	<tr
		class="cursor-pointer border border-[#7f7f7f]/20 px-2 text-left"
		:class="{
			'bg-red-400': schiene.anzahlKollisionen
		}"
	>
		<td class="bg-slate-100 px-2">
			{{ schiene.name }} <br />
			{{ schiene.kurse.size() }} Kurse <br />
			{{ anzahl_schueler }} Schüler
		</td>
		<s-kurs-schueler-schiene-kurs
			v-for="k of getSchieneKurseArray(schiene.kurse)"
			:key="k.id"
			:kurs="k"
			:schueler="selected"
		/>
	</tr>
</template>

<script setup lang="ts">
	import {
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
		computed(() => {
			return app.dataKursblockungsergebnis.manager;
		});

	const anzahl_schueler: ComputedRef<number> = computed(() => {
		return manager.value?.getAnzahlSchuelerSchiene(props.schiene.id) || 0;
	});

	function getSchieneKurseArray(
		kurse: Vector<GostBlockungsergebnisKurs>
	): GostBlockungsergebnisKurs[] {
		return kurse.toArray(new Array<GostBlockungsergebnisKurs>());
	}
</script>
