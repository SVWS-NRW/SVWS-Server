<template>
	<tr
		:style="{
			'background-color': bgColor
		}"
	>
		<td>{{ fach_bezeichnung }}</td>
		<td>{{ lehrer_kuerzel }}</td>
		<td>
			<svws-ui-multi-select
				v-model="inputNote"
				title="Note"
				:items="inputKatalogNoten"
				:item-text="(item: Note) => item.kuerzel"
			/>
		</td>
		<td></td>
	</tr>
</template>

<script setup lang="ts">
	import {
		Note,
		SchuelerLeistungsdaten,
		ZulaessigesFach
	} from "@svws-nrw/svws-core-ts";
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
		if (appLehrer) {
			result =
				appLehrer.auswahl.liste
					.find(l => props.fach.lehrerID === l.id)
					?.kuerzel.toString() || "";
		}
		return result;
	});
	const fach_bezeichnung: ComputedRef<string | undefined> = computed<
		string | undefined
	>(() => {
		let bezeichnung: string | undefined = "";
		if (appFaecher) {
			bezeichnung = appFaecher.auswahl.liste
				.find(f => f.id === props.fach.fachID)
				?.bezeichnung?.toString();
		}
		return bezeichnung;
	});
	const inputKatalogNoten: ComputedRef<Note[]> = computed<Note[]>(() => {
		return Note.values();
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
				appFaecher.auswahl.liste.find(f => f.id === props.fach.fachID)
					?.kuerzel || null
			);
		}
		return null;
	});
	const bgColor: ComputedRef<string> = computed<string>(() => {
		const fachgruppe = zul_fach.value?.getFachgruppe();
		// FIXME: @hmt Hintergrund auch für null weiß?
		if (!fachgruppe) return "#ffffff";
		const rgb =
			(fachgruppe.farbe.getRed() << 16) |
			(fachgruppe.farbe.getGreen() << 8) |
			(fachgruppe.farbe.getBlue() << 0);
		return "#" + (0x1000000 + rgb).toString(16).slice(1);
	});
	const visible: ComputedRef<boolean> = computed<boolean>(() => {
		//return $app.leistungsdaten.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});
</script>
