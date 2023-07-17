<template>
	<div role="row" class="data-table__tr" :style="{ 'background-color': bgColor }">
		<template v-if="allowRegeln">
			<div role="cell" class="data-table__td data-table__td__align-center cursor-pointer hover:text-black"
				:class="{'text-black' : kursdetail_anzeige, 'text-black/50' : !kursdetail_anzeige}" @click="toggle_kursdetail_anzeige"
				title="Kursdetails anzeigen">
				<div class="inline-block">
					<i-ri-arrow-up-s-line v-if="kursdetail_anzeige" class="relative top-0.5" />
					<i-ri-arrow-down-s-line v-else class="relative top-0.5" />
				</div>
			</div>
		</template>
		<div role="cell" class="data-table__td">
			<div class="flex flex-grow items-center">
				<template v-if="kurs.id === edit_name">
					<span class="flex-shrink-0">{{ kursbezeichnung }}<span class="opacity-50">–</span></span>
					<svws-ui-text-input :model-value="tmp_name" @update:model-value="tmp_name=String($event)" focus headless @blur="edit_name=undefined" @keyup.enter="setSuffix" />
				</template>
				<template v-else>
					<span class="underline decoration-dotted decoration-black/50 hover:decoration-solid underline-offset-2 cursor-text" @click="edit_name = kurs.id">
						{{ kursbezeichnung }}
					</span>
				</template>
			</div>
		</div>
		<div role="cell" class="data-table__td" :no-padding="allowRegeln">
			<template v-if="allowRegeln">
				<svws-ui-multi-select :model-value="kurslehrer" @update:model-value="setKurslehrer($event as LehrerListeEintrag | undefined)" autocomplete :item-filter="lehrer_filter" removable headless
					:items="mapLehrer" :item-text="(l: LehrerListeEintrag)=> `${l.kuerzel}`" />
			</template>
			<template v-else>
				<span :class="{'opacity-50': !kurslehrer?.kuerzel}">{{ kurslehrer?.kuerzel || '—' }}</span>
			</template>
		</div>
		<div role="cell" class="data-table__td data-table__td__align-center">
			<svws-ui-checkbox v-if="allowRegeln" headless circle bw :model-value="kurs.istKoopKurs" @update:model-value="setKoop(Boolean($event))" />
			<span v-else class="icon inline-block">
				<i-ri-check-fill v-if="kurs.istKoopKurs" />
				<i-ri-close-line v-else class="opacity-25" />
			</span>
		</div>
		<template v-if="setze_kursdifferenz && kurs_blockungsergebnis">
			<div role="cell" class="data-table__td data-table__td__align-center blockung--kursdifferenz cursor-pointer group relative" @click="toggle_active_fachwahl" :class="{'border-b-transparent': kursOhneBorder}">
				{{ kursdifferenz[2] }}
				<i-ri-filter-line class="invisible absolute right-0 group-hover:visible opacity-25" />
			</div>
			<div role="cell" class="data-table__td data-table__td__align-center blockung--kursdifferenz" @click="toggle_active_fachwahl" :class="{'border-b-transparent': kursOhneBorder}">
				{{ kursdifferenz[1] }}
			</div>
		</template>
		<template v-else>
			<div role="cell" class="data-table__td data-table__td__align-center" :class="{'border-b-transparent': kursOhneBorder}" />
			<div role="cell" class="data-table__td data-table__td__align-center" :class="{'border-b-transparent': kursOhneBorder}" />
		</template>
		<!-- Es folgen die einzelnen Tabellenzellen für die Schienen der Blockung -->
		<template v-for="(schiene) in getErgebnismanager().getMengeAllerSchienen()" :key="schiene.id">
			<!-- Ggf. wird das Element in der Zelle für Drag & Drop dargestellt ... -->
			<svws-ui-drop-data v-if="!getDatenmanager().daten().istAktiv" tag="div" role="cell" v-slot="{ active }"
				class="data-table__td data-table__td__no-padding data-table__td__align-center"
				:class="{
					'bg-white/50': istDraggedKursInAndererSchiene(kurs, schiene).value,
					'bg-white text-black/25': istDraggedKursInSchiene(kurs, schiene).value,
					'data-table__td__disabled': istKursVerbotenInSchiene(kurs, schiene).value,
				}"
				:style="{'background-color': istKursVerbotenInSchiene(kurs, schiene).value ? bgColor : ''}"
				:drop-allowed="isKursDropZone(kurs, schiene).value" @drop="istKursDropAendereKursschiene($event, kurs, schiene)">
				<!-- Ist der Kurs der aktuellen Schiene zugeordnet, so ist er draggable ... -->
				<svws-ui-drag-data v-if="istZugeordnetKursSchiene(kurs, schiene).value" tag="div" :draggable="true" :key="kurs.id" :data="{kurs, schiene}"
					class="select-none w-full h-full rounded flex items-center justify-center relative group text-black"
					:class="{
						'bg-light text-primary font-bold': istKursAusgewaehlt(kurs).value,
						'bg-light/75': !istKursAusgewaehlt(kurs).value,
						'p-0.5': !active && !isKursDropZone(kurs, schiene).value,
						'p-0': active || isKursDropZone(kurs, schiene).value,
					}"
					@drag-start="dragKursStarted" @drag-end="dragKursEnded" @click="toggleKursAusgewaehlt(kurs)">
					{{ getErgebnismanager().getKursE(kurs.id).schueler.size() }}
					<span class="group-hover:bg-white rounded w-3 absolute top-1/2 transform -translate-y-1/2 left-0">
						<i-ri-draggable class="w-5 -ml-1 text-black opacity-40 group-hover:opacity-100 group-hover:text-black" />
					</span>
					<div class="icon cursor-pointer group absolute right-0.5 text-sm" @click.stop="toggleRegelFixiereKursInSchiene(kurs, schiene)">
						<i-ri-pushpin-fill v-if="istKursFixiertInSchiene(kurs, schiene).value" class="inline-block group-hover:opacity-75" />
						<i-ri-pushpin-line v-if="allowRegeln && !istKursFixiertInSchiene(kurs, schiene).value" class="inline-block opacity-25 group-hover:opacity-100" />
					</div>
				</svws-ui-drag-data>
				<!-- ... ansonsten ist er nicht draggable -->
				<div v-else class="cursor-pointer w-full h-full flex items-center justify-center relative group" @click="toggleRegelSperreKursInSchiene(kurs, schiene)"
					:style="{'background-color': istKursVerbotenInSchiene(kurs, schiene).value ? bgColor : ''}"
					:class="{
						'bg-white': active && isKursDropZone(kurs, schiene).value,
						'data-table__td__disabled': istKursVerbotenInSchiene(kurs, schiene).value
					}">
					&NonBreakingSpace;
					<template v-if="active">
						<div v-if="active && isKursDropZone(kurs, schiene).value" class="absolute inset-1 border-2 border-dashed border-black/25" />
					</template>
					<div v-if="istKursGesperrtInSchiene(kurs, schiene).value" class="icon"> <i-ri-lock2-line class="inline-block !opacity-100" /> </div>
					<div v-if="allowRegeln && !istKursGesperrtInSchiene(kurs, schiene).value" class="icon"> <i-ri-lock2-line class="inline-block !opacity-0 group-hover:!opacity-25" /> </div>
				</div>
			</svws-ui-drop-data>
			<!-- ... oder das Element in der Zelle ist nicht für Drag & Drop gedacht -->
			<div role="cell" v-else class="data-table__td data-table__td__align-center data-table__td__no-padding p-0.5">
				<div v-if="istZugeordnetKursSchiene(kurs, schiene).value" @click="toggleKursAusgewaehlt(kurs)"
					class="cursor-pointer w-full h-full rounded flex items-center justify-center relative group"
					:class="{
						'bg-light text-primary font-bold border border-black/50': istKursAusgewaehlt(kurs).value,
						'bg-white/50 border border-black/25': !istKursAusgewaehlt(kurs).value,
					}">
					{{ getErgebnismanager().getKursE(kurs.id).schueler.size() }}
					<div class="icon absolute right-1" v-if="istKursFixiertInSchiene(kurs, schiene).value"> <i-ri-pushpin-fill class="inline-block" /> </div>
					<div v-if="istKursGesperrtInSchiene(kurs, schiene).value" class="icon"> <i-ri-lock2-line class="inline-block" /> </div>
				</div>
			</div>
		</template>
	</div>
	<!-- Wenn Kurs-Details angewählt sind, erscheint die zusätzliche Zeile -->
	<s-gost-kursplanung-kursansicht-kurs-details v-if="kursdetail_anzeige" :bg-color="bgColor" :anzahl-spalten="6 + anzahlSchienen"
		:kurs="kurs" :kurse-mit-kursart="kurseMitKursart" :get-datenmanager="getDatenmanager" :map-lehrer="mapLehrer" :add-regel="addRegel"
		:add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer"
		:add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :split-kurs="splitKurs" :combine-kurs="combineKurs" />
	<s-gost-kursplanung-kursansicht-modal-regel-kurse :get-datenmanager="getDatenmanager" :add-regel="addRegel" ref="modal_regel_kurse" />
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungSchiene, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, GostBlockungsergebnisSchiene, LehrerListeEintrag, List} from "@core";
	import type { ComputedRef, Ref } from "vue";
	import type { GostKursplanungSchuelerFilter } from "../GostKursplanungSchuelerFilter";
	import { GostBlockungRegel, GostKursart, GostKursblockungRegelTyp } from "@core";
	import { computed, ref } from "vue";
	import { lehrer_filter } from "~/helfer";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
		updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<boolean>;
		patchKurs: (data: Partial<GostBlockungKurs>, kurs_id: number) => Promise<void>;
		addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		removeKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs) => Promise<void>;
		splitKurs: (kurs: GostBlockungKurs) => Promise<void>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		hatErgebnis: boolean;
		kurs: GostBlockungKurs;
		bgColor: string;
		mapLehrer: Map<number, LehrerListeEintrag>;
		schuelerFilter: GostKursplanungSchuelerFilter | undefined;
		allowRegeln: boolean;
	}>();

	const modal_regel_kurse = ref();

	const edit_name: Ref<number | undefined> = ref(undefined);
	const tmp_name = ref(props.kurs.suffix);
	const kursdetail_anzeige: Ref<boolean> = ref(false)

	const drag_data = ref<{ kurs: GostBlockungKurs | undefined; schiene: GostBlockungSchiene | undefined }>({schiene: undefined, kurs: undefined})


	async function setKoop(value: boolean) {
		await props.patchKurs({ istKoopKurs: value }, props.kurs.id);
	}

	async function setSuffix() {
		await props.patchKurs({ suffix: tmp_name.value }, props.kurs.id);
		tmp_name.value = props.kurs.suffix;
		edit_name.value = undefined;
	}

	const kursbezeichnung: ComputedRef<string> = computed(() => props.getDatenmanager().kursGetName(props.kurs.id));

	function toggle_active_fachwahl() {
		if (props.schuelerFilter === undefined)
			return;
		if (props.schuelerFilter.fach.value !== props.kurs.fach_id) {
			props.schuelerFilter.kursart.value = GostKursart.fromID(props.kurs.kursart);
			props.schuelerFilter.fach.value = props.kurs.fach_id;
		}
		else
			props.schuelerFilter.reset();
	}

	const kurslehrer: ComputedRef<LehrerListeEintrag | undefined> = computed(() => {
		const liste = props.getDatenmanager().kursGetLehrkraefteSortiert(props.kurs.id);
		return liste.size() > 0 ? props.mapLehrer.get(liste.get(0).id) : undefined;
	});

	async function setKurslehrer(value: LehrerListeEintrag | undefined) {
		if (value !== undefined) {
			const lehrer = await props.addKursLehrer(props.kurs.id, value.id);
			if (lehrer === undefined)
				throw new Error("Fehler beim Anlegen des Kurslehrers");
			await add_lehrer_regel();
		} else {
			await remove_kurslehrer();
		}
	}

	async function remove_kurslehrer() {
		if (!props.kurs || !kurslehrer.value)
			return;
		await props.removeKursLehrer(props.kurs.id, kurslehrer.value.id);
	}

	const lehrer_regel: ComputedRef<GostBlockungRegel | undefined> = computed(()=> {
		const regel_typ = GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN;
		const regeln = props.getDatenmanager().regelGetListeOfTyp(regel_typ);
		if (regeln.isEmpty())
			return undefined;
		return regeln.get(0);
	})

	async function add_lehrer_regel() {
		if (lehrer_regel.value !== undefined)
			return;
		const r = new GostBlockungRegel();
		const regel_typ = GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN
		r.typ = regel_typ.typ;
		await props.addRegel(r);
	}

	const anzahlSchienen: ComputedRef<number> = computed(() => props.getDatenmanager().schieneGetAnzahl());

	const kurs_blockungsergebnis: ComputedRef<GostBlockungsergebnisKurs | undefined> = computed(() =>
		props.hatErgebnis ? props.getErgebnismanager().getKursE(props.kurs.id) : undefined
	);

	const kurseMitKursart: ComputedRef<List<GostBlockungsergebnisKurs>> = computed(() => {
		const fachart = GostKursart.getFachartID(props.kurs.fach_id, props.kurs.kursart);
		return props.getErgebnismanager().getOfFachartKursmenge(fachart);
	});

	const filtered_by_kursart: ComputedRef<List<GostBlockungsergebnisKurs>> = computed(() => {
		const fachart_id = GostKursart.getFachartID(props.kurs.fach_id, props.kurs.kursart);
		return props.getErgebnismanager().getOfFachartKursmenge(fachart_id);
	})

	const setze_kursdifferenz: ComputedRef<boolean> = computed(() => filtered_by_kursart.value.get(0) === kurs_blockungsergebnis.value);

	const kursdifferenz: ComputedRef<[number, number, number]> = computed(() => {
		if (filtered_by_kursart.value.isEmpty())
			return [-1,-1, -1];
		const fachart_id = GostKursart.getFachartID(props.kurs.fach_id, props.kurs.kursart);
		const wahlen = props.getDatenmanager().fachwahlGetListeOfFachart(fachart_id).size() || 0;
		const kdiff = props.getErgebnismanager().getOfFachartKursdifferenz(fachart_id);
		return [filtered_by_kursart.value.size(), kdiff, wahlen];
	});

	const toggle_kursdetail_anzeige = () => kursdetail_anzeige.value = !kursdetail_anzeige.value

	const kursOhneBorder = computed(() => {
		return kurseMitKursart.value.size() > 1 && !kursbezeichnung.value.endsWith(kurseMitKursart.value.size().toString()) && !kursdetail_anzeige.value;
	})


	const istZugeordnetKursSchiene = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) : ComputedRef<boolean> => computed(() => {
		return props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(kurs.id, schiene.id);
	})

	const istKursFixiertInSchiene = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) : ComputedRef<boolean> => computed(() => {
		return props.getDatenmanager().kursGetHatFixierungInSchiene(kurs.id, schiene.id);
	})

	const istKursAusgewaehlt = (kurs: GostBlockungKurs) : ComputedRef<boolean> => computed(() => {
		const k = props.getErgebnismanager().getKursE(kurs.id);
		const filter_kurs_id = props.schuelerFilter?.kurs?.value?.id;
		return (k !== undefined) && (k.id === filter_kurs_id);
	});

	function toggleKursAusgewaehlt(kurs : GostBlockungKurs) {
		if (props.schuelerFilter === undefined)
			return;
		if (props.schuelerFilter.kurs.value?.id !== kurs.id)
			props.schuelerFilter.kurs.value = kurs;
		else
			props.schuelerFilter.reset();
	}

	function dragKursStarted(e: DragEvent) {
		const transfer = e.dataTransfer;
		const data = JSON.parse(transfer?.getData('text/plain') || "") as { kurs: GostBlockungKurs; schiene: GostBlockungSchiene } | undefined;
		if (!data)
			return;
		drag_data.value = data;
	}

	function dragKursEnded() {
		drag_data.value = {kurs: undefined, schiene: undefined};
	}

	const istDraggedKursInAndererSchiene = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) : ComputedRef<boolean> => computed(() => {
		return (drag_data.value.kurs !== undefined) && (drag_data.value.schiene !== undefined) && (drag_data.value.kurs.id === kurs.id) && (drag_data.value.schiene.id !== schiene.id);
	});

	const istDraggedKursInSchiene = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) : ComputedRef<boolean> => computed(() => {
		return (drag_data.value.kurs !== undefined) && (drag_data.value.schiene !== undefined) && (drag_data.value.kurs.id === kurs.id) && (drag_data.value.schiene.id === schiene.id);
	});

	const isKursDropZone = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) : ComputedRef<boolean> => computed(() => {
		if (drag_data.value.kurs === undefined || drag_data.value.schiene === undefined)
			return false;
		if ((drag_data.value.kurs.id === kurs.id) && (istZugeordnetKursSchiene(kurs, schiene).value))
			return false;
		return true;
	});

	async function istKursDropAendereKursschiene(data: {kurs: GostBlockungsergebnisKurs; schiene: GostBlockungSchiene}, kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) {
		if (!data.kurs || !data.schiene)
			return;
		if (data.kurs.id !== kurs.id) {
			modal_regel_kurse.value.openModal(data.kurs.id, kurs.id);
			return;
		}
		if ((data.kurs.id === kurs.id) && (!istZugeordnetKursSchiene(kurs, schiene).value) ) {
			// Entferne potentielle Fixierung beim Verschieben.
			if (props.allowRegeln && props.getDatenmanager().kursGetHatFixierungInSchiene(data.kurs.id, schiene.id)) {
				const regel = props.getDatenmanager().kursGetRegelFixierungInSchiene(kurs.id, schiene.id);
				await props.removeRegel(regel.id);
			}
			await props.updateKursSchienenZuordnung(data.kurs.id, data.schiene.id, schiene.id);
		}
	}

	const istKursGesperrtInSchiene = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) : ComputedRef<boolean> => computed(() => {
		return props.getDatenmanager().kursGetHatSperrungInSchiene(kurs.id, schiene.id);
	})

	async function toggleRegelSperreKursInSchiene(kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) {
		if (!props.allowRegeln)
			return;
		if (props.getDatenmanager().kursGetHatSperrungInSchiene(kurs.id, schiene.id)) {
			const regel = props.getDatenmanager().kursGetRegelGesperrtInSchiene(kurs.id, schiene.id);
			await props.removeRegel(regel.id);
		} else {
			const regel = new GostBlockungRegel();
			regel.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
			regel.parameter.add(kurs.id);
			regel.parameter.add(props.getErgebnismanager().getSchieneG(schiene.id).nummer);
			await props.addRegel(regel);
		}
	}

	const istKursVerbotenInSchiene = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) : ComputedRef<boolean> => computed(() => {
		return props.getDatenmanager().kursGetIstVerbotenInSchiene(kurs.id, schiene.id);
	})

	async function toggleRegelFixiereKursInSchiene(kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) {
		if (!props.allowRegeln)
			return;
		const s = props.getErgebnismanager().getSchieneG(schiene.id);
		if (props.getDatenmanager().kursGetHatFixierungInSchiene(kurs.id, schiene.id)) {
			const regel = props.getDatenmanager().kursGetRegelFixierungInSchiene(kurs.id, schiene.id);
			await props.removeRegel(regel.id);
		} else {
			const regel = new GostBlockungRegel();
			regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
			regel.parameter.add(kurs.id);
			regel.parameter.add(s.nummer);
			await props.addRegel(regel);
		}
	}

</script>
