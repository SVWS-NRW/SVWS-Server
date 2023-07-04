<template>
	<svws-ui-drop-data v-slot="{ active }" v-if="!blockungAktiv"
		class="data-table__td data-table__td__no-padding data-table__td__align-center"
		:class="{
			'bg-white/50': modelValue.kurs?.id === kurs.id && modelValue.schiene?.id !== schiene.id,
			'bg-white text-black/25': modelValue.kurs?.id === kurs.id && modelValue.schiene?.id === schiene.id,
			'data-table__td__disabled': schiene_verboten,
		}"
		:style="{'background-color': schiene_verboten ? bgColorNichtMoeglich : ''}"
		tag="div"
		role="cell"
		:drop-allowed="is_drop_zone"
		@drop="drop_aendere_kursschiene($event, schiene)">
		<svws-ui-drag-data v-if="kurs_schiene_zugeordnet"
			:key="kurs.id"
			tag="div"
			:data="{kurs, schiene}"
			class="select-none w-full h-full rounded flex items-center justify-center relative group text-black"
			:draggable="true"
			:class="{
				'bg-light text-primary font-bold': selected_kurs,
				'bg-light/75': !selected_kurs,
				'p-0.5': !active && !is_drop_zone,
				'p-0': active || is_drop_zone,
			}"
			@drag-start="drag_started"
			@drag-end="drag_ended"
			@click="toggle_active_kurs">
			{{ kurs_blockungsergebnis?.schueler.size() }}
			<span class="group-hover:bg-white rounded w-3 absolute top-1/2 transform -translate-y-1/2 left-0">
				<i-ri-draggable class="w-5 -ml-1 text-black opacity-40 group-hover:opacity-100 group-hover:text-black" />
			</span>
			<div class="icon cursor-pointer group absolute right-0.5 text-sm" @click.stop="fixieren_regel_toggle">
				<i-ri-pushpin-fill v-if="istFixiert" class="inline-block group-hover:opacity-75" />
				<i-ri-pushpin-line v-if="allowRegeln && !istFixiert" class="inline-block opacity-25 group-hover:opacity-100" />
			</div>
		</svws-ui-drag-data>
		<div v-else class="cursor-pointer w-full h-full flex items-center justify-center relative group" @click="sperren_regel_toggle"
			:style="{'background-color': schiene_verboten ? bgColorNichtMoeglich : ''}"
			:class="{'bg-white': active && is_drop_zone, 'data-table__td__disabled': schiene_verboten}">
			&NonBreakingSpace;
			<div v-if="active && is_drop_zone" class="absolute inset-1 border-2 border-dashed border-black/25" />
			<div v-if="istGesperrt" class="icon"> <i-ri-lock2-line class="inline-block !opacity-100" /> </div>
			<div v-if="allowRegeln && !istGesperrt" class="icon"> <i-ri-lock2-line class="inline-block !opacity-0 group-hover:!opacity-25" /> </div>
		</div>
	</svws-ui-drop-data>
	<div role="cell" v-else class="data-table__td data-table__td__align-center data-table__td__no-padding p-0.5">
		<div v-if="kurs_schiene_zugeordnet"
			class="cursor-pointer w-full h-full rounded flex items-center justify-center relative group"
			:class="{'bg-light text-primary font-bold border border-black/50': selected_kurs, 'bg-white/50 border border-black/25': !selected_kurs}"
			@click="toggle_active_kurs">
			{{ kurs_blockungsergebnis?.schueler.size() }}
			<div class="icon absolute right-1" v-if="istFixiert"> <i-ri-pushpin-fill class="inline-block" /> </div>
			<div v-if="istGesperrt" class="icon"> <i-ri-lock2-line class="inline-block" /> </div>
		</div>
	</div>
	<s-gost-kursplanung-kursansicht-modal-regel-kurse v-model="isModalOpen_KurseZusammen" :get-datenmanager="getDatenmanager"
		:kurs1-id="kurs1?.id" :kurs2-id="kurs.id" :add-regel="addRegel" />
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungSchiene, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, GostBlockungsergebnisSchiene, List} from "@core";
	import type { ComputedRef, Ref } from "vue";
	import type { GostKursplanungSchuelerFilter } from "../GostKursplanungSchuelerFilter";
	import { GostBlockungRegel, GostKursblockungRegelTyp } from "@core";
	import { computed, ref } from "vue";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
		updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<boolean>;
		blockungAktiv: boolean;
		allowRegeln: boolean;
		kurs: GostBlockungKurs;
		schuelerFilter: GostKursplanungSchuelerFilter | undefined;
		schiene: GostBlockungsergebnisSchiene;
		modelValue: {kurs: GostBlockungKurs | undefined; schiene: GostBlockungSchiene | undefined};
		bgColorNichtMoeglich: string;
	}>();

	const emit = defineEmits<{
		"update:modelValue": [drag_data: {kurs: GostBlockungKurs | undefined; schiene: GostBlockungSchiene | undefined}];
	}>();

	const kurs_blockungsergebnis: ComputedRef<GostBlockungsergebnisKurs> = computed(() => props.getErgebnismanager().getKursE(props.kurs.id));

	const selected_kurs: ComputedRef<boolean> = computed(() => {
		const filter_kurs_id = props.schuelerFilter?.kurs?.value?.id;
		return (kurs_blockungsergebnis.value !== undefined) && (kurs_blockungsergebnis.value?.id === filter_kurs_id)
	});

	function toggle_active_kurs() {
		if (props.schuelerFilter === undefined)
			return;
		if (props.schuelerFilter.kurs.value?.id !== props.kurs.id)
			props.schuelerFilter.kurs.value = props.kurs;
		else
			props.schuelerFilter.reset();
	}

	// Drag & Drop

	function drag_started(e: DragEvent) {
		const transfer = e.dataTransfer;
		const data = JSON.parse(transfer?.getData('text/plain') || "") as { kurs: GostBlockungKurs; schiene: GostBlockungSchiene } | undefined;
		if (!data)
			return;
		emit("update:modelValue", data);
	}

	function drag_ended() {
		emit("update:modelValue", {kurs: undefined, schiene: undefined});
	}

	const is_drop_zone = computed(() => {
		if (!props.modelValue.kurs || !props.modelValue.schiene)
			return false;
		if ( (props.modelValue.kurs.id === props.kurs.id) && (kurs_schiene_zugeordnet.value) )
			return false;
		return true;
	});

	const isModalOpen_KurseZusammen: Ref<boolean> = ref(false);

	let kurs1: GostBlockungsergebnisKurs | undefined = undefined;

	async function drop_aendere_kursschiene(drag_data: {kurs: GostBlockungsergebnisKurs; schiene: GostBlockungSchiene}, schiene: GostBlockungsergebnisSchiene) {
		if (!drag_data.kurs || !drag_data.schiene || kurs_blockungsergebnis.value === undefined)
			return;

		if (drag_data.kurs.id !== kurs_blockungsergebnis.value.id) {
			kurs1 = drag_data.kurs;
			isModalOpen_KurseZusammen.value = true;
			return;
		}

		if ( (drag_data.kurs.id === kurs_blockungsergebnis.value.id) && (!kurs_schiene_zugeordnet.value) ) {
			// Entferne potentielle Fixierung beim Verschieben.
			if (props.allowRegeln && props.getDatenmanager().kursGetHatFixierungInSchiene(drag_data.kurs.id, schiene.id)) {
				const s = props.getErgebnismanager().getSchieneG(schiene.id);
				await fixieren_regel_entfernen(s);
			}

			await props.updateKursSchienenZuordnung(drag_data.kurs.id, drag_data.schiene.id, schiene.id);
		}
	}

	const kurs_schiene_zugeordnet = computed(() =>
		props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(props.kurs.id, props.schiene.id));

	// Regeln zum Sperren

	const istGesperrt = computed(() => {
		return props.getDatenmanager().kursGetHatSperrungInSchiene(props.kurs.id, props.schiene.id);
	})

	const schiene_verboten = computed(()=>{
		return props.getDatenmanager().kursGetIstVerbotenInSchiene(props.kurs.id, props.schiene.id);
	})

	async function sperren_regel_toggle() {
		if (!props.allowRegeln)
			return;
		const s = props.getErgebnismanager().getSchieneG(props.schiene.id);
		if (istGesperrt.value)
			await sperren_regel_entfernen(s.id);
		else
			await sperren_regel_hinzufuegen(s);
	}

	async function sperren_regel_hinzufuegen(schiene: GostBlockungSchiene) {
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
		regel.parameter.add(props.kurs.id);
		regel.parameter.add(schiene.nummer);
		await props.addRegel(regel);
	}

	async function sperren_regel_entfernen(nummer: number) {
		const regel = props.getDatenmanager().kursGetRegelGesperrtInSchiene(props.kurs.id, props.schiene.id);
		await props.removeRegel(regel.id);
	}

	// Regeln zum Fixieren

	const istFixiert = computed(() => {
		return props.getDatenmanager().kursGetHatFixierungInSchiene(props.kurs.id, props.schiene.id);
	})

	async function fixieren_regel_toggle() {
		if (!props.allowRegeln)
			return;
		const s = props.getErgebnismanager().getSchieneG(props.schiene.id);
		if (istFixiert.value)
			await fixieren_regel_entfernen(s);
		else
			await fixieren_regel_hinzufuegen(s);
	}

	async function fixieren_regel_hinzufuegen(schiene: GostBlockungSchiene) {
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regel.parameter.add(props.kurs.id);
		regel.parameter.add(schiene.nummer);
		await props.addRegel(regel);
	}

	async function fixieren_regel_entfernen(schiene: GostBlockungSchiene) {
		const regel = props.getDatenmanager().kursGetRegelFixierungInSchiene(props.kurs.id, props.schiene.id);
		await props.removeRegel(regel.id);
	}

</script>
