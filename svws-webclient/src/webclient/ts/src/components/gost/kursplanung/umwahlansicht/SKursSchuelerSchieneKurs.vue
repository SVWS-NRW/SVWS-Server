<template>
	<drag-data
		:key="kurs.id"
		tag="td"
		:data="{ id: kurs.id, fachID: kurs.fachID, kursart: kurs.kursart?.valueOf() }"
		class="select-none"
		:class="{ 'cursor-move border-2 border-green-700': is_draggable, 'bg-yellow-200': is_drop_zone }"
		:draggable="is_draggable && !pending"
		:style="{ 'background-color': bgColor }"
		@drag-start="drag_started"
		@drag-end="drag_ended"
	>
		<drop-data @drop="drop_aendere_kurszuordnung($event, kurs.id)" v-slot="{active}" >
			<span :class="{'bg-red-400': active && is_drop_zone}">{{ kurs_name }}</span>
			<span v-if="allow_regeln">
					<svws-ui-icon class="cursor-pointer" @click.stop="verbieten_regel_toggle" >
						<i-ri-forbid-fill v-if="verbieten_regel" class="inline-block text-red-400"/>
						<i-ri-forbid-line v-if="!verbieten_regel && !fixier_regel" class="inline-block"/>
					</svws-ui-icon>
					<svws-ui-icon class="cursor-pointer" @click.stop="fixieren_regel_toggle" >
						<i-ri-pushpin-fill v-if="fixier_regel" class="inline-block text-red-400"/>
						<i-ri-pushpin-line v-if="!verbieten_regel && !fixier_regel" class="inline-block"/>
					</svws-ui-icon>
			</span>
			<span v-else>
					<svws-ui-icon> <i-ri-forbid-fill v-if="verbieten_regel" class="inline-block text-red-400"/> </svws-ui-icon>
					<svws-ui-icon> <i-ri-pushpin-fill v-if="fixier_regel" class="inline-block text-red-400"/> </svws-ui-icon>
			</span>
			<br />{{schueler_schriftlich}}/{{ kurs.schueler.size() }}
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
		List,
		SchuelerListeEintrag,
		Vector,
		ZulaessigesFach
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";

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

	const drag_data: WritableComputedRef<{ id: number, fachID: number, kursart: number }|undefined> =
		computed({
			get(): { id: number, fachID: number, kursart: number } {
				return main.config.drag_and_drop_data;
			},
			set(value: { id: number, fachID: number, kursart: number }|undefined) {
				main.config.drag_and_drop_data = value
			}});

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> =
		computed(() => app.dataKursblockung.ergebnismanager);

	const allow_regeln: ComputedRef<boolean> =
		computed(()=> app.blockungsergebnisauswahl.liste.length === 1)

	const is_draggable: ComputedRef<boolean> =
		computed(() => {
			for (const s of props.kurs.schueler)
				if (s === props.schueler.id)
					return true;
			return false;
		});

	const pending: ComputedRef<boolean> =
		computed(()=> app.dataKursblockungsergebnis.pending);

	const is_drop_zone: ComputedRef<boolean> =
		computed(() => {
			if (!drag_data.value) return false
			const { id, fachID, kursart } = drag_data.value;
			if (id === props.kurs.id)
				return false;
			if (fachID !== props.kurs.fachID || kursart !== props.kurs.kursart)
				return false;
			return true;
		});

	const kurs_original: ComputedRef<GostBlockungKurs | undefined> =
		computed( () => manager.value?.getKursG(props.kurs.id));

	const kurs_name: ComputedRef<String> =
		computed(()=> manager.value?.getOfKursName(props.kurs.id) || "")

	const schueler_schriftlich: ComputedRef<number> =
		computed(()=> manager.value?.getOfKursAnzahlSchuelerSchriftlich(props.kurs.id) || 0);

	const gostfach: ComputedRef<ZulaessigesFach | undefined> =
		computed(() => {
			if (app.dataKursblockung.datenmanager === undefined) return
			let fach
			for (const f of app.dataKursblockung.datenmanager.faecherManager().values())
				if (f.id === kurs_original.value?.fach_id) {
					fach = f; break
				}
			return ZulaessigesFach.getByKuerzelASD(fach?.kuerzel || null);
		});

	const bgColor: ComputedRef<string> =
		computed(() => {
			if ((!is_draggable.value) || (!gostfach.value))
				return "";
			return gostfach.value.getHMTLFarbeRGB().valueOf();
		});
	
	const regeln: ComputedRef<List<GostBlockungRegel>> =
		computed(()=> app.dataKursblockung.datenmanager?.getMengeOfRegeln() || new Vector<GostBlockungRegel>())

	const verbieten_regel: ComputedRef<GostBlockungRegel | undefined> =
		computed(() => {
			for (const regel of regeln.value)
				if (regel.typ === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ
						&& regel.parameter.get(0) === props.schueler.id
						&& regel.parameter.get(1) === props.kurs.id)
					return regel;
		})
	
	const fixier_regel: ComputedRef<GostBlockungRegel | undefined> =
		computed(() => {
			for (const regel of regeln.value)
				if (regel.typ === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ
						&& regel.parameter.get(0) === props.schueler.id
						&& regel.parameter.get(1) === props.kurs.id)
					return regel
		})

	const fixieren_regel_toggle = () => fixier_regel.value ? fixieren_regel_entfernen() : fixieren_regel_hinzufuegen()
	const verbieten_regel_toggle = () => verbieten_regel.value ? verbieten_regel_entfernen() : verbieten_regel_hinzufuegen()

	const regel_speichern = async (regel: GostBlockungRegel) => {
		regel.parameter.set(0, props.schueler.id)
		regel.parameter.set(1, props.kurs.id)
		await app.dataKursblockung.patch_blockung_regel(regel)
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

	function drag_started(e: DragEvent) {
		const transfer = e.dataTransfer;
		const data = JSON.parse(transfer?.getData('text/plain') || "") as { id: number, fachID: number, kursart: number } | undefined;
		if (!data) return;
		drag_data.value = data
	}
	function drag_ended() {
		drag_data.value = undefined;
	}

	async function drop_aendere_kurszuordnung(kurs: any, id_kurs_neu: number) {
		if (!is_drop_zone.value) 
			return;
		await app.dataKursblockungsergebnis.assignSchuelerKurs(props.schueler.id, id_kurs_neu, kurs.id);
		drag_ended();
	}
</script>
