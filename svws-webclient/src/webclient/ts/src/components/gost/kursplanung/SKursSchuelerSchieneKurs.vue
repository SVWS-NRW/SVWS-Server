<template>
	<drag-data
		:key="kurs.id"
		tag="td"
		type="kurs"
		:data="{
			id: kurs.id,
			fachID: kurs.fachID,
			kursart: kurs.kursart?.valueOf()
		}"
		class="select-none"
		:class="{
			'cursor-move border-2 border-green-700': is_draggable,
			'bg-yellow-200': is_drop_zone
		}"
		:draggable="is_draggable"
		:style="{
			'background-color': bgColor
		}"
	>
		<drop-data
			type="kurs"
			@drop="drop_aendere_kurszuordnung($event, kurs.id)"
			@drag-over="drag_over($event, kurs)"
		>
			{{ kurs.name }}<br />{{ kurs.schueler.size() }} -
			{{ kurs_original?.id }}/{{ kurs.id }}
		</drop-data>
	</drag-data>
</template>

<script setup lang="ts">
	import {
		GostBlockungKurs,
		GostBlockungsergebnisKurs,
		GostBlockungsergebnisManager,
		GostBlockungsergebnisSchuelerzuordnung,
		SchuelerListeEintrag,
		ZulaessigesFach
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps({
		kurs: {
			type: GostBlockungsergebnisKurs,
			required: true
		},
		selected: {
			type: SchuelerListeEintrag,
			required: true
		}
	});

	const drag_id = ref("");

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> =
		computed(() => {
			return app.dataKursblockungsergebnis.manager;
		});

	const is_draggable: ComputedRef<boolean> = computed(() => {
		return props.kurs.schueler
			.toArray(new Array<GostBlockungsergebnisSchuelerzuordnung>())
			.some(s => s.id === props.selected.id);
	});

	const is_drop_zone: ComputedRef<boolean> = computed(() => {
		const fachID = main.config.drag_and_drop_data?.fachID;
		const kursart = main.config.drag_and_drop_data?.kursart;
		if (!fachID || !kursart) {
			return false;
		}
		return (
			fachID === props.kurs.fachID &&
			kursart === props.kurs.kursart?.valueOf()
		);
	});

	const kurs_original: ComputedRef<GostBlockungKurs | undefined> = computed(
		() => {
			return manager.value?.getKurs(props.kurs.id);
		}
	);

	const gostfach: ComputedRef<ZulaessigesFach | undefined> = computed(() => {
		if (!app.dataFaecher.daten) return
		let fach
		for (const f of app.dataFaecher.daten)
			if (f.id === kurs_original.value?.fach_id) {
				fach = f; break
			}
		return ZulaessigesFach.getByKuerzelASD(fach?.kuerzel || null);
	});

	const bgColor: ComputedRef<string> = computed(() => {
		if (!is_draggable.value) return "";
		const fachgruppe = gostfach.value?.getFachgruppe();
		if (!fachgruppe) return "#ffffff";
		const rgb =
			(fachgruppe.farbe.getRed() << 16) |
			(fachgruppe.farbe.getGreen() << 8) |
			(fachgruppe.farbe.getBlue() << 0);
		return "#" + (0x1000000 + rgb).toString(16).slice(1);
	});

	function drop_aendere_kurszuordnung(kurs: any, id_kurs_neu: number) {
		const schuelerid = props.selected?.id;
		if (!schuelerid) return;
		if (kurs.id) {
			app.dataKursblockungsergebnis.assignSchuelerKurs(
				schuelerid,
				kurs.id,
				true
			);
			app.dataKursblockungsergebnis.assignSchuelerKurs(
				schuelerid,
				id_kurs_neu,
				false
			);
		}
	}

	function drag_over(event: DragEvent, kurs: GostBlockungsergebnisKurs) {
		const transfer = event.dataTransfer;
		if (!transfer) return;
		const data = main.config.drag_and_drop_data;
		if (
			!data ||
			kurs.fachID != data.fachID ||
			kurs.kursart?.valueOf() != data.kursart
		)
			return;
		event.preventDefault();
	}
</script>
