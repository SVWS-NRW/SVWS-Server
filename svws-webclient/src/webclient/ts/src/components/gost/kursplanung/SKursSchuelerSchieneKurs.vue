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
			{{ kurs_name }}<span v-if="is_draggable">
					<svws-ui-icon class="cursor-pointer" @click="verbieten_regel_toggle" >
						<i-ri-forbid-fill v-if="verbieten_regel" class="inline-block"/>
						<i-ri-forbid-line v-if="!verbieten_regel && !fixier_regel" class="inline-block"/>
					</svws-ui-icon>
					<svws-ui-icon class="cursor-pointer" @click="fixieren_regel_toggle" >
						<i-ri-pushpin-fill v-if="fixier_regel" class="inline-block"/>
						<i-ri-pushpin-line  v-if="!verbieten_regel && !fixier_regel" class="inline-block"/>
					</svws-ui-icon>
			</span>
			<br />{{ kurs.schueler.size() }} -
			{{ kurs_original?.id }}/{{ kurs.id }}
		</drop-data>
	</drag-data>
</template>

<script setup lang="ts">
	import {
		GostBlockungKurs,
		GostBlockungRegel,
		GostBlockungsergebnisKurs,
		GostBlockungsergebnisManager,
		GostKursblockungRegelTyp,
		SchuelerListeEintrag,
		ZulaessigesFach
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps({
		kurs: {
			type: GostBlockungsergebnisKurs,
			required: true
		},
		schueler: {
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

	const is_draggable: ComputedRef<boolean> = computed(() => {
		return props.kurs.schueler
			.toArray(new Array<Number>())
			.some(s => s === props.schueler.id);
	});

	const is_drop_zone: ComputedRef<boolean> = computed(() => {
		const fachID = main.config.drag_and_drop_data?.fachID;
		const kursart = main.config.drag_and_drop_data?.kursart;
		if (!fachID || !kursart)
			return false;
		if (fachID !== props.kurs.fachID || kursart !== props.kurs.kursart?.valueOf())
      return false;
		const kursID = main.config.drag_and_drop_data?.id;
		if (kursID === props.kurs.id)
			return false;
		return true;
	});

	const kurs_original: ComputedRef<GostBlockungKurs | undefined> = computed(
		() => {
			return manager.value?.getKursG(props.kurs.id);
		}
	);

	const kurs_name: ComputedRef<String> = computed(()=>
		manager.value?.getOfKursName(props.kurs.id) || ""
	)

	const gostfach: ComputedRef<ZulaessigesFach | undefined> = computed(() => {
		if (!app.dataFaecher.manager) return
		let fach
		for (const f of app.dataFaecher.manager.values())
			if (f.id === kurs_original.value?.fach_id) {
				fach = f; break
			}
		return ZulaessigesFach.getByKuerzelASD(fach?.kuerzel || null);
	});

	const bgColor: ComputedRef<string> = computed(() => {
		if ((!is_draggable.value) || (!gostfach.value))
			return "";
		return gostfach.value.getHMTLFarbeRGB().valueOf();
	});

	function drop_aendere_kurszuordnung(kurs: any, id_kurs_neu: number) {
		const schuelerid = props.schueler.id;
		if (!schuelerid) 
			return;
		if (kurs.id === id_kurs_neu)
		  return;
		if (kurs.id) {
			app.dataKursblockungsergebnis.assignSchuelerKurs(
				schuelerid,
				kurs.id,
				true
			);
		}
		app.dataKursblockungsergebnis.assignSchuelerKurs(
			schuelerid,
			id_kurs_neu,
			false
		);
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
	
	const verbieten_regel: ComputedRef<GostBlockungRegel | undefined> = computed(() => {
		//TODO M
		const regeln = app.dataKursblockung.daten?.regeln.toArray(new Array<GostBlockungRegel>())
		const regel = regeln?.find(r => r.typ === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ
			&& r.parameter.get(0) === props.schueler.id
			&& r.parameter.get(1) === props.kurs.id)
		return regel
	})
	const fixier_regel: ComputedRef<GostBlockungRegel | undefined> = computed(() => {
		//TODO M
		const regeln = app.dataKursblockung.daten?.regeln.toArray(new Array<GostBlockungRegel>())
		const regel = regeln?.find(r => r.typ === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ
			&& r.parameter.get(0) === props.schueler.id
			&& r.parameter.get(1) === props.kurs.id)
		return regel
	})

	const fixieren_regel_toggle = () => fixier_regel.value ? fixieren_regel_entfernen() : fixieren_regel_hinzufuegen()
	const verbieten_regel_toggle = () => verbieten_regel.value ? verbieten_regel_entfernen() : verbieten_regel_hinzufuegen()

	const regel_speichern = async (regel: GostBlockungRegel) => {
		regel.parameter.set(0, props.schueler.id)
		regel.parameter.set(1, props.kurs.id)
		await app.dataKursblockung.patch_blockung_regel(regel)
		app.dataKursblockung.manager?.addRegel(regel)
	}
	const fixieren_regel_hinzufuegen = async () => {
		const regel = await app.dataKursblockung.add_blockung_regel(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ)
		if (!regel) return
		await regel_speichern(regel)
	}
	const fixieren_regel_entfernen = async () => {
		if (!fixier_regel.value) return
		await app.dataKursblockung.del_blockung_regel(fixier_regel.value.id)
	}
	const verbieten_regel_hinzufuegen = async () => {
		const regel = await app.dataKursblockung.add_blockung_regel(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ)
		if (!regel) return
		await regel_speichern(regel)
	}
	const verbieten_regel_entfernen = async () => {
		if (!verbieten_regel.value) return
		await app.dataKursblockung.del_blockung_regel(verbieten_regel.value.id)
	}
</script>
