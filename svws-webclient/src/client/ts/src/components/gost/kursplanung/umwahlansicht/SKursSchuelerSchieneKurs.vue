<template>
	<svws-ui-drag-data :key="kurs.id" tag="td" :data="{ id: kurs.id, fachID: kurs.fachID, kursart: kurs.kursart }"
		class="select-none text-center" :class="{ 'cursor-move border-2 border-green-700': is_draggable, 'bg-yellow-200': is_drop_zone }"
		:draggable="is_draggable" @drag-start="drag_started" @drag-end="drag_ended" :style="{ 'background-color': bgColor }">
		<svws-ui-drop-data @drop="drop_aendere_kurszuordnung($event, kurs.id)" v-slot="{active}">
			<div :class="{'bg-green-400': active && is_drop_zone}">
				<span>{{ kurs_name }}</span>
				<br>
				<span class="text-sm" title="Schriftlich/Insgesamt im Kurs">{{ schueler_schriftlich }}/{{ kurs.schueler.size() }}</span>
				<br>
				<span v-if="(allowRegeln && fach_gewaehlt && !blockung_aktiv)">
					<svws-ui-icon class="cursor-pointer" @click.stop="verbieten_regel_toggle">
						<i-ri-forbid-fill v-if="verbieten_regel" class="inline-block text-red-400" />
						<i-ri-forbid-line v-if="!verbieten_regel && !fixier_regel" class="inline-block" />
					</svws-ui-icon>
					<svws-ui-icon class="cursor-pointer" @click.stop="fixieren_regel_toggle">
						<i-ri-pushpin-fill v-if="fixier_regel" class="inline-block text-red-400" />
						<i-ri-pushpin-line v-if="!verbieten_regel && !fixier_regel" class="inline-block" />
					</svws-ui-icon>
				</span>
				<span v-else>
					<svws-ui-icon> <i-ri-forbid-fill v-if="verbieten_regel" class="inline-block text-red-400" /> </svws-ui-icon>
					<svws-ui-icon> <i-ri-pushpin-fill v-if="fixier_regel" class="inline-block text-red-400" /> </svws-ui-icon>
				</span>
			</div>
		</svws-ui-drop-data>
	</svws-ui-drag-data>
</template>

<script setup lang="ts">
	import { GostBlockungKurs, GostBlockungRegel, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, GostKursblockungRegelTyp,
		List, SchuelerListeEintrag, ZulaessigesFach } from "@svws-nrw/svws-core";
	import { computed, ComputedRef } from "vue";
	import { ApiStatus } from "~/components/ApiStatus";

	type DndData = { id: number, fachID: number, kursart: number };

	const props = defineProps<{
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
		updateKursSchuelerZuordnung: (idSchueler: number, idKursNeu: number, idKursAlt: number) => Promise<boolean>;
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		kurs: GostBlockungsergebnisKurs;
		schueler: SchuelerListeEintrag;
		apiStatus: ApiStatus;
		allowRegeln: boolean;
		dragAndDropData?: DndData;
	}>();

	const emit = defineEmits<{
		(e: 'dnd', data: DndData | undefined): void;
	}>()

	const fach_gewaehlt: ComputedRef<boolean> = computed(() =>
		props.getErgebnismanager().getOfSchuelerHatFachwahl(props.schueler.id, props.kurs.fachID, props.kurs.kursart)
	);

	const is_draggable: ComputedRef<boolean> = computed(() => {
		if (props.apiStatus.pending || blockung_aktiv.value)
			return false;
		for (const s of props.kurs.schueler)
			if (s === props.schueler.id)
				return true;
		return false;
	});

	const is_drop_zone: ComputedRef<boolean> = computed(() => {
		if (!props.dragAndDropData)
			return false
		const { id, fachID, kursart } = props.dragAndDropData;
		if (id === props.kurs.id)
			return false;
		if ((fachID !== props.kurs.fachID) || (kursart !== props.kurs.kursart))
			return false;
		return true;
	});

	const kurs_original: ComputedRef<GostBlockungKurs | undefined> = computed(() => props.getErgebnismanager().getKursG(props.kurs.id));

	const kurs_name: ComputedRef<String> = computed(() => props.getErgebnismanager().getOfKursName(props.kurs.id))

	const schueler_schriftlich: ComputedRef<number> = computed(() => props.getErgebnismanager().getOfKursAnzahlSchuelerSchriftlich(props.kurs.id));

	const gostfach: ComputedRef<ZulaessigesFach | undefined> = computed(() => {
		let fach
		for (const f of props.getDatenmanager().faecherManager().values()) {
			if (f.id === kurs_original.value?.fach_id) {
				fach = f;
				break;
			}
		}
		return ZulaessigesFach.getByKuerzelASD(fach?.kuerzel || null);
	});

	const bgColor: ComputedRef<string> = computed(() => {
		if (gostfach.value === undefined)
			return "";
		if (props.getErgebnismanager().getOfSchuelerOfKursIstZugeordnet(props.schueler.id, props.kurs.id))
			return gostfach.value.getHMTLFarbeRGB();
		return "";
	});

	const blockung_aktiv: ComputedRef<boolean> = computed(() => props.getDatenmanager().daten().istAktiv);

	const regeln: ComputedRef<List<GostBlockungRegel>> = computed(()=> props.getDatenmanager().getMengeOfRegeln());

	// const fixier_regel: ComputedRef<boolean> = computed(() => props.getDatenmanager().getOfSchuelerOfKursIstFixiert(props.schueler.id, props.kurs.id))
	const fixier_regel: ComputedRef<GostBlockungRegel | undefined> = computed(() => {
		for (const regel of regeln.value)
			if (regel.typ === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ
				&& regel.parameter.get(0) === props.schueler.id
				&& regel.parameter.get(1) === props.kurs.id)
				return regel;
		return undefined;
	})

	// const verbieten_regel: ComputedRef<boolean> = computed(() => props.getDatenmanager().getOfSchuelerOfKursIstGesperrt(props.schueler.id, props.kurs.id))
	const verbieten_regel: ComputedRef<GostBlockungRegel | undefined> = computed(() => {
		for (const regel of regeln.value)
			if (regel.typ === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ
				&& regel.parameter.get(0) === props.schueler.id
				&& regel.parameter.get(1) === props.kurs.id)
				return regel;
		return undefined;
	})

	const fixieren_regel_toggle = () => fixier_regel.value ? fixieren_regel_entfernen() : fixieren_regel_hinzufuegen();

	const verbieten_regel_toggle = () => verbieten_regel.value ? verbieten_regel_entfernen() : verbieten_regel_hinzufuegen();

	const regel_speichern = async (regel: GostBlockungRegel) => {
		regel.parameter.add(props.schueler.id);
		regel.parameter.add(props.kurs.id);
		await props.addRegel(regel);
	}

	const fixieren_regel_hinzufuegen = async () => {
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		await regel_speichern(regel);
	}

	const fixieren_regel_entfernen = async () => {
		if (!fixier_regel.value)
			return
		await props.removeRegel(fixier_regel.value.id);
	}

	const verbieten_regel_hinzufuegen = async () => {
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ;
		await regel_speichern(regel);
	}

	const verbieten_regel_entfernen = async () => {
		if (verbieten_regel.value === undefined)
			return
		await props.removeRegel(verbieten_regel.value.id);
	}

	function drag_started(e: DragEvent) {
		const transfer = e.dataTransfer;
		const data = JSON.parse(transfer?.getData('text/plain') || "") as DndData;
		if (!data)
			return;
		emit('dnd', data);
	}

	function drag_ended() {
		emit('dnd', undefined);
	}

	async function drop_aendere_kurszuordnung(kurs: any, id_kurs_neu: number) {
		if (!is_drop_zone.value)
			return;
		await props.updateKursSchuelerZuordnung(props.schueler.id, id_kurs_neu, kurs.id);
		drag_ended();
	}

</script>
