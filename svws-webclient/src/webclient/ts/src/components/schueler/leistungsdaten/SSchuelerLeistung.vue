<template>
	<tr :style="{ 'background-color': bgColor }">
		<td>{{ fach_bezeichnung }}</td>
		<td>{{ lehrer_kuerzel }}</td>
		<td>
			<svws-ui-multi-select title="Note" v-model="inputNote" :items="Note.values()" :item-text="(item: Note) => item.kuerzel" />
		</td>
		<td>
		</td>
	</tr>
</template>

<script setup lang="ts">

	import { Note, SchuelerLeistungsdaten, ZulaessigesFach } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps({
		fach: { type: Object as () => SchuelerLeistungsdaten, required: true }
	});

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	const appLehrer = main.apps.lehrer;
	const appFaecher = main.apps.faecher;

	const lehrer_kuerzel: ComputedRef<string> = computed<string>(() => {
		let result = "";
		if (appLehrer)
			result = appLehrer.auswahl.liste.find(l => props.fach.lehrerID === l.id)?.kuerzel.toString() || "";
		return result;
	});

	const fach_bezeichnung: ComputedRef<string | undefined> = computed<string | undefined>(() => {
		let bezeichnung: string | undefined = "";
		if (appFaecher)
			bezeichnung = appFaecher.auswahl.liste.find(f => f.id === props.fach.fachID)?.bezeichnung?.toString();
		return bezeichnung;
	});

	const inputNote = computed({
		get(): Note | undefined {
			const id = props.fach.note;
			return Note.fromKuerzel(id);
		},
		set(val: Note | undefined) {
			if (app && val?.kuerzel) {
				// app.dataSchuelerAbschnittsdaten.patch({ note: val?.kuerzel });
			}
		}
	});

	const zul_fach: ComputedRef<ZulaessigesFach | null> = computed(() => {
		if (appFaecher) {
			return ZulaessigesFach.getByKuerzelASD(
				appFaecher.auswahl.liste.find(f => f.id === props.fach.fachID)?.kuerzel || null
			);
		}
		return null;
	});

	const bgColor: ComputedRef<string> = computed<string>(() => {
		if (!zul_fach.value)
			return "#ffffff";
		return zul_fach.value.getHMTLFarbeRGB().valueOf();
	});

</script>
