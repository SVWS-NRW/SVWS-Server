<template>
	<template v-if="!blockungAktiv">
		<svws-ui-drop-data v-for="(schiene) in getErgebnismanager().getMengeAllerSchienen()" :key="schiene.id"
			v-slot="{ active }"
			class="data-table__td data-table__td__no-padding data-table__td__align-center"
			:class="{'bg-white/50': drag_data.kurs?.id === kurs.id && drag_data.schiene?.id !== schiene.id, 'schiene-gesperrt': schiene_gesperrt(schiene), 'bg-white text-black/25': drag_data.kurs?.id === kurs.id && drag_data.schiene?.id === schiene.id}"
			tag="div"
			role="cell"
			:drop-allowed="is_drop_zone(schiene)"
			@drop="drop_aendere_kursschiene($event, schiene)">
			<svws-ui-drag-data v-if="kurs_schiene_zugeordnet(schiene)"
				:key="kurs.id"
				tag="div"
				:data="{kurs, schiene}"
				class="select-none w-full h-full rounded flex items-center justify-center relative group"
				:draggable="true"
				:class="{'schiene-gesperrt': schiene_gesperrt(schiene), 'bg-light text-primary font-bold': selected_kurs, 'bg-white/50': !selected_kurs, 'p-0.5': !active && !is_drop_zone(schiene), 'p-0': active || is_drop_zone(schiene)}"
				@drag-start="drag_started"
				@drag-end="drag_ended"
				@click="toggle_active_kurs">
				{{ kurs_blockungsergebnis?.schueler.size() }}
				<span class="group-hover:bg-white rounded w-3 absolute top-0.5 left-0.5">
					<i-ri-draggable class="w-5 -ml-1 text-black opacity-25 group-hover:opacity-50 group-hover:text-black" />
				</span>
				<svws-ui-icon class="cursor-pointer group absolute right-1" @click.stop="fixieren_regel_toggle(schiene)">
					<i-ri-pushpin-fill v-if="istFixiert(schiene)" class="inline-block group-hover:opacity-75" />
					<i-ri-pushpin-line v-if="!istFixiert(schiene) && allowRegeln" class="inline-block opacity-25 group-hover:opacity-100" />
				</svws-ui-icon>
			</svws-ui-drag-data>
			<template v-else>
				<div class="cursor-pointer w-full h-full flex items-center justify-center relative group" @click="sperren_regel_toggle(schiene)"
					:class="{'bg-white': active && is_drop_zone(schiene),
						'schiene-gesperrt': schiene_gesperrt(schiene)}">
					<div v-if="active && is_drop_zone(schiene)" class="absolute inset-1 border-2 border-dashed border-black/25" />
					<svws-ui-icon>
						<i-ri-prohibited-line v-if="sperr_regeln.find(r=>r.parameter.get(1) === ermittel_parent_schiene(schiene).nummer)" class="inline-block" />
						<i-ri-prohibited-line v-if="allowRegeln && !sperr_regeln.find(r=>r.parameter.get(1) === ermittel_parent_schiene(schiene).nummer)" class="inline-block opacity-0 group-hover:opacity-25" />
					</svws-ui-icon>
				</div>
			</template>
		</svws-ui-drop-data>
	</template>
	<template v-else>
		<div role="cell" v-for="schiene in getErgebnismanager().getMengeAllerSchienen()" :key="schiene.id" class="data-table__td data-table__td__align-center data-table__td__no-padding p-0.5">
			<div v-if="kurs_schiene_zugeordnet(schiene)"
				class="cursor-pointer w-full h-full rounded flex items-center justify-center relative group"
				:class="{'bg-light text-primary font-bold': selected_kurs, 'bg-white/50': !selected_kurs}"
				@click="toggle_active_kurs">
				{{ kurs_blockungsergebnis?.schueler.size() }}
				<svws-ui-icon class="absolute right-1" v-if="istFixiert(schiene)">
					<i-ri-pushpin-fill class="inline-block" />
				</svws-ui-icon>
				<svws-ui-icon v-if="sperr_regeln.find(r=>r.parameter.get(1) === ermittel_parent_schiene(schiene).nummer)">
					<i-ri-prohibited-line class="inline-block" />
				</svws-ui-icon>
			</div>
		</div>
	</template>
	<s-gost-kursplanung-kursansicht-modal-regel-kurse v-model="isModalOpen_KurseZusammen" :get-datenmanager="getDatenmanager"
		:kurs1-id="kurs1?.id" :kurs2-id="kurs.id" :add-regel="addRegel" />
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungSchiene, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, GostBlockungsergebnisSchiene, List } from "@svws-nrw/svws-core";
	import { GostBlockungRegel, GostKursblockungRegelTyp } from "@svws-nrw/svws-core";
	import type { ComputedRef, Ref } from "vue";
	import { computed, ref } from "vue";
	import type { GostKursplanungSchuelerFilter } from "../GostKursplanungSchuelerFilter";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
		updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<boolean>;
		blockungAktiv: boolean;
		allowRegeln: boolean;
		kurs: GostBlockungKurs;
		bgColor: string;
		schuelerFilter: GostKursplanungSchuelerFilter | undefined;
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

	const drag_data: Ref<{kurs: GostBlockungKurs | undefined; schiene: GostBlockungSchiene | undefined}> = ref({schiene: undefined, kurs: undefined})

	function drag_started(e: DragEvent) {
		const transfer = e.dataTransfer;
		const data = JSON.parse(transfer?.getData('text/plain') || "") as { kurs: GostBlockungKurs; schiene: GostBlockungSchiene } | undefined;
		if (!data)
			return;
		drag_data.value.kurs = data.kurs;
		drag_data.value.schiene = data.schiene;
	}

	function drag_ended() {
		drag_data.value.kurs = undefined;
		drag_data.value.schiene = undefined;
	}

	function is_drop_zone(schiene: GostBlockungsergebnisSchiene): boolean{
		return drag_data.value?.schiene?.id !== schiene.id && drag_data.value.kurs?.id === props.kurs.id && drag_data.value.schiene?.id !== schiene.id
	}

	const isModalOpen_KurseZusammen: Ref<boolean> = ref(false);

	let kurs1: GostBlockungsergebnisKurs | undefined = undefined;

	async function drop_aendere_kursschiene(drag_data: {kurs: GostBlockungsergebnisKurs; schiene: GostBlockungSchiene}, schiene: GostBlockungsergebnisSchiene) {
		if (!drag_data.kurs || !drag_data.schiene || kurs_blockungsergebnis.value === undefined)
			return
		if ((drag_data.kurs.id !== kurs_blockungsergebnis.value.id) && kurs_schiene_zugeordnet(schiene)) {
			kurs1 = drag_data.kurs;
			isModalOpen_KurseZusammen.value = true;
			return;
		}
		if (drag_data.kurs.id === kurs_blockungsergebnis.value.id && schiene.id !== drag_data.schiene.id) {
			if (fixier_regeln.value && props.allowRegeln) {
				const s = props.getErgebnismanager().getSchieneG(schiene.id);
				await fixieren_regel_entfernen(s);
			}
			await props.updateKursSchienenZuordnung(drag_data.kurs.id, drag_data.schiene.id, schiene.id);
		}
	}

	function kurs_schiene_zugeordnet(schiene: GostBlockungsergebnisSchiene): boolean {
		return props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(props.kurs.id, schiene.id);
	}


	// Regeln

	const regeln: ComputedRef<List<GostBlockungRegel>> = computed(() => props.getDatenmanager().getMengeOfRegeln());


	// Regeln zum Sperren

	const sperr_regeln: ComputedRef<GostBlockungRegel[]> = computed(() => {
		const arr = []
		for (const regel of regeln.value)
			if (regel.typ === GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ && regel.parameter.get(0) === props.kurs.id)
				arr.push(regel)
		return arr;
	})

	const ermittel_parent_schiene = (ergebnis_schiene: GostBlockungsergebnisSchiene): GostBlockungSchiene => {
		const schiene =	props.getErgebnismanager().getSchieneG(ergebnis_schiene.id)
		if (!schiene)
			throw new Error(`Schiene mit der ID: ${ergebnis_schiene.id} fehlt in der Definition`);
		return schiene;
	}

	const schiene_gesperrt = (schiene: GostBlockungsergebnisSchiene): boolean => {
		for (const regel of regeln.value) {
			const { nummer } = ermittel_parent_schiene(schiene)
			if (regel.typ === GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ
				&& ((regel.parameter.get(0) !== props.kurs.kursart && (nummer >= regel.parameter.get(1) && nummer <= regel.parameter.get(2)))
					|| (regel.parameter.get(0) === props.kurs.kursart && (nummer < regel.parameter.get(1) || nummer > regel.parameter.get(2)))))
				return true;
			else if (regel.typ === GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ
				&& regel.parameter.get(0) === props.kurs.kursart
				&& (nummer >= regel.parameter.get(1) && nummer <= regel.parameter.get(2)))
				return true;
		}
		return false;
	}


	async function sperren_regel_toggle(schiene: GostBlockungsergebnisSchiene) {
		if (!props.allowRegeln)
			return;
		const { nummer } = ermittel_parent_schiene(schiene);
		if (sperr_regeln.value.find(r => r.parameter.get(1) === nummer))
			await sperren_regel_entfernen(nummer);
		else
			await sperren_regel_hinzufuegen(nummer);
	}

	async function sperren_regel_hinzufuegen(nummer: number) {
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
		regel.parameter.add(props.kurs.id);
		regel.parameter.add(nummer);
		await props.addRegel(regel);
	}

	async function sperren_regel_entfernen(nummer: number) {
		if (!sperr_regeln.value.length)
			return
		const regel = sperr_regeln.value.find(r=>r.parameter.get(1) === nummer);
		if (!regel)
			return
		await props.removeRegel(regel.id);
	}


	// Regeln zum Fixieren

	function istFixiert(schiene: GostBlockungsergebnisSchiene) {
		const s = props.getErgebnismanager().getSchieneG(schiene.id);
		return fixier_regeln.value.some(r=>r.parameter.get(1) === s.nummer);
	}

	const fixier_regeln: ComputedRef<GostBlockungRegel[]> = computed(() => {
		const arr = []
		for (const regel of regeln.value)
			if (regel.typ === GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ && regel.parameter.get(0) === props.kurs.id)
				arr.push(regel);
		return arr;
	});

	async function fixieren_regel_toggle(schiene: GostBlockungsergebnisSchiene) {
		if (!props.allowRegeln)
			return;
		const s = props.getErgebnismanager().getSchieneG(schiene.id);
		if (fixier_regeln.value.some(r=>r.parameter.get(1) === s.nummer))
			await fixieren_regel_entfernen(s);
		else
			await fixieren_regel_hinzufuegen(s);
	}

	async function fixieren_regel_hinzufuegen(schiene: GostBlockungSchiene) {
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		const kurs = kurs_blockungsergebnis.value;
		regel.parameter.add(kurs.id);
		regel.parameter.add(schiene.nummer);
		await props.addRegel(regel);
	}

	async function fixieren_regel_entfernen(schiene: GostBlockungSchiene) {
		for (const regel of fixier_regeln.value)
			if (regel.parameter.get(1) === schiene.nummer)
				await props.removeRegel(regel.id);
	}


</script>

<style lang="postcss">
	.schiene-gesperrt {
		background-image: url("/images/table-cell--stripes.svg");
		background-size: auto 100%;
	}
</style>
