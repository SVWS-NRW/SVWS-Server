<template>
	<template v-if="listeDerKurse.isEmpty() && fachwahlenAnzahl !== 0 && allowRegeln">
		<div role="row" class="data-table__tr data-table__tbody__tr data-table__tr__disabled-light" :style="{ 'background-color': bgColor }" :key="kursart.id">
			<div role="cell" class="data-table__td" />
			<div role="cell" class="data-table__td text-black/50">
				<span title="Fach">{{ fachwahlen.kuerzel }}</span><span class="opacity-50">-</span><span title="Kursart">{{ kursart.kuerzel }}</span>
			</div>
			<div role="cell" class="data-table__td data-table__td__align-left">
				<span class="opacity-25">—</span>
			</div>
			<div role="cell" class="data-table__td data-table__td__align-center">
				<span class="opacity-25">—</span>
			</div>
			<div role="cell" class="data-table__td data-table__td__align-center cursor-pointer group relative text-black/50" @click="toggleSchuelerFilterFachwahl(fachwahlen.id, kursart)">
				{{ fachwahlenAnzahl }}
			</div>
			<div role="cell" class="data-table__td data-table__td__align-center">
				<span class="opacity-25">—</span>
			</div>
			<div role="cell" class="data-table__td data-table__td__align-center" :style="{'gridColumn': 'span ' + getDatenmanager().schieneGetListe().size()}">
				<svws-ui-button type="transparent" size="small" @click="add_kurs(kursart)" title="Kurs anlegen" class="text-black">
					<span class="inline-flex items-center text-button -mr-0.5">
						<i-ri-book2-line />
						<i-ri-add-line class="-ml-1 text-sm" />
					</span>
					Kurs anlegen
				</svws-ui-button>
			</div>
		</div>
	</template>
	<template v-else>
		<template v-for="kurs in listeDerKurse" :key="kurs.id">
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
							<span class="flex-shrink-0">{{ kursbezeichnung(kurs).value }}<span class="opacity-50">–</span></span>
							<svws-ui-text-input :model-value="tmp_name" @update:model-value="tmp_name=String($event)" focus headless @blur="edit_name=undefined" @keyup.enter="setSuffix(kurs)" />
						</template>
						<template v-else>
							<span class="underline decoration-dotted decoration-black/50 hover:decoration-solid underline-offset-2 cursor-text" @click="editKursSuffix(kurs)">
								{{ kursbezeichnung(kurs).value }}
							</span>
						</template>
					</div>
				</div>
				<div role="cell" class="data-table__td" :no-padding="allowRegeln">
					<template v-if="allowRegeln">
						<svws-ui-multi-select :model-value="kurslehrer(kurs).value" @update:model-value="setKurslehrer(kurs, $event as LehrerListeEintrag | undefined)" autocomplete :item-filter="lehrer_filter" removable headless
							:items="mapLehrer" :item-text="(l: LehrerListeEintrag)=> `${l.kuerzel}`" />
					</template>
					<template v-else>
						<span :class="{'opacity-50': !kurslehrer(kurs).value?.kuerzel}">{{ kurslehrer(kurs).value?.kuerzel || '—' }}</span>
					</template>
				</div>
				<div role="cell" class="data-table__td data-table__td__align-center">
					<svws-ui-checkbox v-if="allowRegeln" headless circle bw :model-value="kurs.istKoopKurs" @update:model-value="setKoop(kurs, Boolean($event))" />
					<span v-else class="icon inline-block">
						<i-ri-check-fill v-if="kurs.istKoopKurs" />
						<i-ri-close-line v-else class="opacity-25" />
					</span>
				</div>
				<template v-if="setze_kursdifferenz(kurs).value && kurs_blockungsergebnis(kurs).value">
					<div role="cell" class="data-table__td data-table__td__align-center blockung--kursdifferenz cursor-pointer group relative" @click="toggle_active_fachwahl(kurs)" :class="{'border-b-transparent': kursOhneBorder(kurs).value}">
						{{ kursdifferenz(kurs).value[2] }}
						<i-ri-filter-line class="absolute right-0" :class="schuelerFilter?.fach === kurs.fach_id && schuelerFilter?.kursart?.id === kurs.kursart ? 'text-black' : 'invisible group-hover:visible opacity-25'" />
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center blockung--kursdifferenz" :class="{'border-b-transparent': kursOhneBorder(kurs).value}">
						{{ kursdifferenz(kurs).value[1] }}
					</div>
				</template>
				<template v-else>
					<div role="cell" class="data-table__td data-table__td__align-center cursor-pointer" :class="{'border-b-transparent': kursOhneBorder(kurs).value}" @click="toggle_active_fachwahl(kurs)" />
					<div role="cell" class="data-table__td data-table__td__align-center" :class="{'border-b-transparent': kursOhneBorder(kurs).value}" />
				</template>
				<!-- Es folgen die einzelnen Tabellenzellen für die Schienen der Blockung -->
				<template v-for="(schiene) in getErgebnismanager().getMengeAllerSchienen()" :key="schiene.id">
					<!-- Ggf. wird das Element in der Zelle für Drag & Drop dargestellt ... -->
					<div role="cell" v-if="!getDatenmanager().daten().istAktiv"
						class="data-table__td data-table__td__no-padding data-table__td__align-center p-0.5"
						:class="{
							'bg-white/50': istDraggedKursInAndererSchiene(kurs, schiene).value,
							'bg-white text-black/25': istDraggedKursInSchiene(kurs, schiene).value,
							'data-table__td__disabled': istKursVerbotenInSchiene(kurs, schiene).value,
						}"
						@dragover="if (isKursDropZone(kurs, schiene).value) $event.preventDefault();"
						@drop="onDropKursSchiene({kurs, schiene, fachId: fachwahlen.id})">
						<!-- Ist der Kurs der aktuellen Schiene zugeordnet, so ist er draggable, es sei denn, er ist fixiert ... -->
						<div v-if="istZugeordnetKursSchiene(kurs, schiene).value" :draggable="!istKursFixiertInSchiene(kurs, schiene).value" :key="kurs.id"
							class="select-none w-full h-full rounded-sm flex items-center justify-center relative group text-black cursor-grab"
							:class="{
								'cursor-grabbing': dragDataKursSchiene() !== undefined,
								'bg-light text-primary font-bold': istKursAusgewaehlt(kurs).value,
								'bg-light/75': !istKursAusgewaehlt(kurs).value,
								'p-0.5': dragDataKursSchiene() === undefined && !isKursDropZone(kurs, schiene).value,
								'p-0': dragDataKursSchiene() !== undefined || isKursDropZone(kurs, schiene).value,
							}"
							@dragstart.stop="onDragKursSchiene({kurs, schiene, fachId: fachwahlen.id})" @dragend="onDragKursSchiene(undefined)" @click="toggleKursAusgewaehlt(kurs)">
							{{ getErgebnismanager().getOfKursAnzahlSchuelerNichtExtern(kurs.id) }} {{ getErgebnismanager().getOfKursAnzahlSchuelerExterne(kurs.id)>0 ? `+${getErgebnismanager().getOfKursAnzahlSchuelerExterne(kurs.id)}e`:'' }} {{ getErgebnismanager().getOfKursAnzahlSchuelerDummy(kurs.id)>0 ? `+${getErgebnismanager().getOfKursAnzahlSchuelerDummy(kurs.id)}d`:'' }}
							<span class="group-hover:bg-white rounded-sm w-3 absolute top-1/2 transform -translate-y-1/2 left-0" v-if="!istKursFixiertInSchiene(kurs, schiene).value">
								<i-ri-draggable class="w-4 -ml-0.5 text-black opacity-40 group-hover:opacity-100 group-hover:text-black" />
							</span>
							<div class="icon cursor-pointer group absolute right-0.5 text-sm" @click.stop="toggleRegelFixiereKursInSchiene(kurs, schiene)">
								<i-ri-pushpin-fill v-if="istKursFixiertInSchiene(kurs, schiene).value" class="inline-block group-hover:opacity-75" />
								<i-ri-pushpin-line v-if="allowRegeln && !istKursFixiertInSchiene(kurs, schiene).value" class="inline-block opacity-25 group-hover:opacity-100" />
							</div>
						</div>
						<!-- ... ansonsten ist er nicht draggable -->
						<div v-else class="cursor-pointer w-full h-full flex items-center justify-center relative group" @click="toggleRegelSperreKursInSchiene(kurs, schiene)"
							:style="{'background-color': istKursVerbotenInSchiene(kurs, schiene).value ? bgColor : ''}"
							:class="{ 'data-table__td__disabled': istKursVerbotenInSchiene(kurs, schiene).value }">
							&NonBreakingSpace;
							<template v-if="dragDataKursSchiene() !== undefined">
								<div v-if="(dragDataKursSchiene() !== undefined) && (dragDataKursSchiene()?.kurs.id === kurs.id) && isKursDropZone(kurs, schiene).value" class="absolute inset-0.5 border-2 border-dashed border-black/25" />
							</template>
							<div v-if="(dragDataKursSchiene() === undefined) && istKursGesperrtInSchiene(kurs, schiene).value" class="icon"> <i-ri-lock-2-line class="inline-block !opacity-100" /> </div>
							<div v-if="allowRegeln && (dragDataKursSchiene() === undefined) && !istKursGesperrtInSchiene(kurs, schiene).value" class="icon"> <i-ri-lock-2-line class="inline-block !opacity-0 group-hover:!opacity-25" /> </div>
						</div>
					</div>
					<!-- ... oder das Element in der Zelle ist nicht für Drag & Drop gedacht -->
					<div role="cell" v-else class="data-table__td data-table__td__align-center data-table__td__no-padding p-0.5">
						<div v-if="istZugeordnetKursSchiene(kurs, schiene).value" @click="toggleKursAusgewaehlt(kurs)"
							class="cursor-pointer w-full h-full rounded flex items-center justify-center relative group"
							:class="{
								'bg-light text-primary font-bold border border-black/50': istKursAusgewaehlt(kurs).value,
								'bg-white/50 border border-black/25': !istKursAusgewaehlt(kurs).value,
							}">
							{{ getErgebnismanager().getOfKursAnzahlSchuelerNichtExtern(kurs.id) }} {{ getErgebnismanager().getOfKursAnzahlSchuelerExterne(kurs.id)>0 ? `+${getErgebnismanager().getOfKursAnzahlSchuelerExterne(kurs.id)}e`:'' }} {{ getErgebnismanager().getOfKursAnzahlSchuelerDummy(kurs.id)>0 ? `+${getErgebnismanager().getOfKursAnzahlSchuelerDummy(kurs.id)}d`:'' }}
							<div class="icon absolute right-1" v-if="istKursFixiertInSchiene(kurs, schiene).value"> <i-ri-pushpin-fill class="inline-block" /> </div>
							<div v-if="(dragDataKursSchiene() === undefined) && istKursGesperrtInSchiene(kurs, schiene).value" class="icon"> <i-ri-lock-2-line class="inline-block" /> </div>
						</div>
					</div>
				</template>
			</div>
			<!-- Wenn Kurs-Details angewählt sind, erscheint die zusätzliche Zeile -->
			<s-gost-kursplanung-kursansicht-kurs-details v-if="kursdetail_anzeige" :bg-color="bgColor" :anzahl-spalten="6 + anzahlSchienen"
				:kurs="kurs" :kurse-mit-kursart="kurseMitKursart(kurs).value" :get-datenmanager="getDatenmanager" :map-lehrer="mapLehrer" :add-regel="addRegel"
				:add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer"
				:add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :split-kurs="splitKurs" :combine-kurs="combineKurs" />
		</template>
	</template>
</template>

<script setup lang="ts">

	import { ref, type ComputedRef, computed } from "vue";
	import type { GostBlockungKurs, LehrerListeEintrag, GostBlockungsergebnisKurs, List, GostBlockungsergebnisSchiene } from "@core";
	import { ZulaessigesFach , GostBlockungRegel, GostKursart, GostKursblockungRegelTyp} from "@core";
	import { lehrer_filter } from "~/helfer";
	import type { SGostKursplanungKursansichtFachwahlProps } from "./SGostKursplanungKursansichtFachwahlProps";

	const props = defineProps<SGostKursplanungKursansichtFachwahlProps>();

	const bgColor: ComputedRef<string> = computed(() => ZulaessigesFach.getByKuerzelASD(props.fachwahlen.kuerzelStatistik).getHMTLFarbeRGBA(1.0));

	function toggleSchuelerFilterFachwahl(idFach: number, kursart: GostKursart) {
		if (props.schuelerFilter === undefined)
			return;
		if (props.schuelerFilter.fach !== idFach) {
			props.schuelerFilter.kursart = kursart;
			props.schuelerFilter.fach = idFach;
		} else {
			props.schuelerFilter.reset();
		}
	}

	async function add_kurs(art: GostKursart) {
		await props.addKurs(props.fachwahlen.id, art.id);
	}

	const edit_name = ref<number | undefined>(undefined);
	const tmp_name = ref("");
	const kursdetail_anzeige = ref<boolean>(false);

	const listeDerKurse : ComputedRef<List<GostBlockungKurs>> = computed(() => {
		return props.getDatenmanager().kursGetListeByFachUndKursart(props.fachwahlen.id, props.kursart.id);
	});

	function editKursSuffix(kurs: GostBlockungKurs) {
		edit_name.value = kurs.id;
		tmp_name.value = kurs.suffix;
	}

	async function setKoop(kurs: GostBlockungKurs, value: boolean) {
		await props.patchKurs({ istKoopKurs: value }, kurs.id);
	}

	async function setSuffix(kurs: GostBlockungKurs) {
		await props.patchKurs({ suffix: tmp_name.value }, kurs.id);
		tmp_name.value = kurs.suffix;
		edit_name.value = undefined;
	}

	const kursbezeichnung = (kurs: GostBlockungKurs) : ComputedRef<string> => computed(() => {
		return props.getDatenmanager().kursGetName(kurs.id)
	});

	function toggle_active_fachwahl(kurs: GostBlockungKurs) {
		if (props.schuelerFilter === undefined)
			return;
		if (props.schuelerFilter.fach !== kurs.fach_id || props.schuelerFilter.kursart?.id !== kurs.kursart) {
			props.schuelerFilter.kursart = GostKursart.fromID(kurs.kursart);
			props.schuelerFilter.fach = kurs.fach_id;
		}
		else
			props.schuelerFilter.reset();
	}

	const kurslehrer = (kurs: GostBlockungKurs) : ComputedRef<LehrerListeEintrag | undefined> => computed(() => {
		const liste = props.getDatenmanager().kursGetLehrkraefteSortiert(kurs.id);
		return liste.size() > 0 ? props.mapLehrer.get(liste.get(0).id) : undefined;
	});

	async function setKurslehrer(kurs: GostBlockungKurs, value: LehrerListeEintrag | undefined) {
		if (value !== undefined) {
			const lehrer = await props.addKursLehrer(kurs.id, value.id);
			if (lehrer === undefined)
				throw new Error("Fehler beim Anlegen des Kurslehrers");
			await add_lehrer_regel();
		} else {
			await remove_kurslehrer(kurs);
		}
	}

	async function remove_kurslehrer(kurs : GostBlockungKurs) {
		const lehrer = kurslehrer(kurs).value;
		if (lehrer === undefined)
			return;
		await props.removeKursLehrer(kurs.id, lehrer.id);
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

	const kurs_blockungsergebnis = (kurs: GostBlockungKurs) : ComputedRef<GostBlockungsergebnisKurs | undefined> => computed(() => {
		return props.hatErgebnis ? props.getErgebnismanager().getKursE(kurs.id) : undefined;
	});

	const kurseMitKursart = (kurs: GostBlockungKurs) : ComputedRef<List<GostBlockungsergebnisKurs>> => computed(() => {
		const fachart = GostKursart.getFachartID(kurs.fach_id, kurs.kursart);
		return props.getErgebnismanager().getOfFachartKursmenge(fachart);
	});

	const filtered_by_kursart = (kurs: GostBlockungKurs) : ComputedRef<List<GostBlockungsergebnisKurs>> => computed(() => {
		const fachart_id = GostKursart.getFachartID(kurs.fach_id, kurs.kursart);
		return props.getErgebnismanager().getOfFachartKursmenge(fachart_id);
	})

	const setze_kursdifferenz = (kurs: GostBlockungKurs) : ComputedRef<boolean> => computed(() => {
		return filtered_by_kursart(kurs).value.get(0) === kurs_blockungsergebnis(kurs).value;
	});

	const kursdifferenz = (kurs: GostBlockungKurs) : ComputedRef<[number, number, number]> => computed(() => {
		if (filtered_by_kursart(kurs).value.isEmpty())
			return [-1,-1, -1];
		const fachart_id = GostKursart.getFachartID(kurs.fach_id, kurs.kursart);
		const wahlen = props.getDatenmanager().fachwahlGetListeOfFachart(fachart_id).size() || 0;
		const kdiff = props.getErgebnismanager().getOfFachartKursdifferenz(fachart_id);
		return [filtered_by_kursart(kurs).value.size(), kdiff, wahlen];
	});

	const toggle_kursdetail_anzeige = () => kursdetail_anzeige.value = !kursdetail_anzeige.value

	const kursOhneBorder = (kurs: GostBlockungKurs) : ComputedRef<boolean> => computed(() => {
		return kurseMitKursart(kurs).value.size() > 1 && !kursbezeichnung(kurs).value.endsWith(kurseMitKursart(kurs).value.size().toString()) && !kursdetail_anzeige.value;
	})


	const istZugeordnetKursSchiene = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) : ComputedRef<boolean> => computed(() => {
		return props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(kurs.id, schiene.id);
	})

	const istKursFixiertInSchiene = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) : ComputedRef<boolean> => computed(() => {
		return props.getDatenmanager().kursGetHatFixierungInSchiene(kurs.id, schiene.id);
	})

	const istKursAusgewaehlt = (kurs: GostBlockungKurs) : ComputedRef<boolean> => computed(() => {
		const k = props.getErgebnismanager().getKursE(kurs.id);
		const filter_kurs_id = props.schuelerFilter?.kurs?.id;
		return (k !== undefined) && (k.id === filter_kurs_id);
	});

	function toggleKursAusgewaehlt(kurs : GostBlockungKurs) {
		if (props.schuelerFilter === undefined)
			return;
		if (props.schuelerFilter.kurs?.id !== kurs.id)
			props.schuelerFilter.kurs = kurs;
		else
			props.schuelerFilter.reset();
	}

	const istDraggedKursInAndererSchiene = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) : ComputedRef<boolean> => computed(() => {
		const dragData = props.dragDataKursSchiene();
		return (dragData !== undefined) && (dragData.kurs.id === kurs.id) && (dragData.schiene.id !== schiene.id);
	});

	const istDraggedKursInSchiene = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) : ComputedRef<boolean> => computed(() => {
		const dragData = props.dragDataKursSchiene();
		return (dragData !== undefined) && (dragData.kurs.id === kurs.id) && (dragData.schiene.id === schiene.id);
	});

	const isKursDropZone = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) : ComputedRef<boolean> => computed(() => {
		const dragData = props.dragDataKursSchiene();
		if (dragData === undefined)
			return false;
		if ((istZugeordnetKursSchiene(kurs, schiene).value) && ((dragData.kurs.id === kurs.id) || (dragData.fachId !== props.fachwahlen.id)))
			return false;
		if (!props.allowRegeln && (dragData.kurs.id !== kurs.id))
			return false;
		if (props.getDatenmanager().kursGetIstVerbotenInSchiene(kurs.id, schiene.id))
			return false;
		if (props.getDatenmanager().kursGetIstVerbotenInSchiene(kurs.id, dragData.schiene.id))
			return false;
		return true;
	});

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
