<template>
	<svws-ui-table-row :style="{ 'background-color': bgColor }">
		<template v-if="allowRegeln">
			<svws-ui-table-cell align="center" class="cursor-pointer hover:text-black" :class="{'text-black' : kursdetail_anzeige, 'text-black/50' : !kursdetail_anzeige}" @click="toggle_kursdetail_anzeige" title="Kursdetails anzeigen">
				<div class="inline-block">
					<i-ri-arrow-up-s-line v-if="kursdetail_anzeige" class="relative top-0.5" />
					<i-ri-arrow-down-s-line v-else class="relative top-0.5" />
				</div>
			</svws-ui-table-cell>
		</template>
		<svws-ui-table-cell>
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
		</svws-ui-table-cell>
		<svws-ui-table-cell :no-padding="allowRegeln">
			<template v-if="allowRegeln">
				<svws-ui-multi-select :model-value="kurslehrer" @update:model-value="setKurslehrer($event as LehrerListeEintrag | undefined)" autocomplete :item-filter="lehrer_filter" removable headless
					:items="mapLehrer" :item-text="(l: LehrerListeEintrag)=> `${l.kuerzel}`" />
			</template>
			<template v-else>
				<span :class="{'opacity-50': !kurslehrer?.kuerzel}">{{ kurslehrer?.kuerzel || '—' }}</span>
			</template>
		</svws-ui-table-cell>
		<svws-ui-table-cell align="center">
			<svws-ui-checkbox headless circle bw v-if="allowRegeln" :model-value="kurs.istKoopKurs" @update:model-value="setKoop(Boolean($event))" />
			<svws-ui-icon v-else class="inline-block">
				<i-ri-check-fill v-if="kurs.istKoopKurs" />
				<i-ri-close-line v-else class="opacity-25" />
			</svws-ui-icon>
		</svws-ui-table-cell>
		<template v-if="setze_kursdifferenz && kurs_blockungsergebnis">
			<svws-ui-table-cell align="center" class="blockung--kursdifferenz cursor-pointer group relative" @click="toggle_active_fachwahl" :class="{'border-b-transparent': kursOhneBorder}">
				{{ kursdifferenz[2] }}
				<i-ri-filter-line class="invisible absolute right-0 group-hover:visible opacity-25" />
			</svws-ui-table-cell>
			<svws-ui-table-cell align="center" class="blockung--kursdifferenz" @click="toggle_active_fachwahl" :class="{'border-b-transparent': kursOhneBorder}">
				{{ kursdifferenz[1] }}
			</svws-ui-table-cell>
		</template>
		<template v-else>
			<svws-ui-table-cell align="center" :class="{'border-b-transparent': kursOhneBorder}" />
			<svws-ui-table-cell align="center" :class="{'border-b-transparent': kursOhneBorder}" />
		</template>
		<s-gost-kursplanung-kursansicht-kurs-schienen v-for="(schiene) in getErgebnismanager().getMengeAllerSchienen()" :key="schiene.id" :schiene="schiene"
			:blockung-aktiv="blockung_aktiv" :allow-regeln="allowRegeln" :kurs="kurs" :bg-color-nicht-moeglich="bgColor"
			:get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager" :schueler-filter="schuelerFilter"
			:add-regel="addRegel" :remove-regel="removeRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
			v-model="drag_data" />
	</svws-ui-table-row>
	<!--Wenn Kursdtails angewählt sind, erscheint die zusätzliche Zeile-->
	<s-gost-kursplanung-kursansicht-kurs-details v-if="kursdetail_anzeige" :bg-color="bgColor" :anzahl-spalten="6 + anzahlSchienen"
		:kurs="kurs" :kurse-mit-kursart="kurseMitKursart" :get-datenmanager="getDatenmanager" :map-lehrer="mapLehrer" :add-regel="addRegel"
		:add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer"
		:add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :split-kurs="splitKurs" :combine-kurs="combineKurs" />
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungSchiene, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, LehrerListeEintrag, List} from "@core";
	import type { ComputedRef, Ref } from "vue";
	import type { GostKursplanungSchuelerFilter } from "../GostKursplanungSchuelerFilter";
	import { GostBlockungRegel, GostKursart, GostKursblockungRegelTyp, ZulaessigesFach } from "@core";
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

	const edit_name: Ref<number | undefined> = ref(undefined);
	const tmp_name = ref(props.kurs.suffix);
	const kursdetail_anzeige: Ref<boolean> = ref(false)

	const drag_data: Ref<{kurs: GostBlockungKurs | undefined; schiene: GostBlockungSchiene | undefined}> = ref({schiene: undefined, kurs: undefined})

	async function setKoop(value: boolean) {
		await props.patchKurs({ istKoopKurs: value }, props.kurs.id);
	}

	async function setSuffix() {
		await props.patchKurs({ suffix: tmp_name.value }, props.kurs.id);
		tmp_name.value = props.kurs.suffix;
		edit_name.value = undefined;
	}

	const kursbezeichnung: ComputedRef<string> = computed(() => props.getDatenmanager().kursGetName(props.kurs.id));

	const kuerzel = computed(()=> props.getErgebnismanager().getFach(props.kurs.fach_id).kuerzel);

	/*const bgColorNichtMoeglich: ComputedRef<string> = computed(() =>
		`color-mix(in srgb, ${ZulaessigesFach.getByKuerzelASD(kuerzel.value).getHMTLFarbeRGB()}, rgb(255,255,255)`);*/

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
		const regel_typ = GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN
		const regeln = props.getDatenmanager().regelGetListe()
		if (!regeln)
			return undefined;
		for (const r of regeln)
			if (r.typ === regel_typ.typ)
				return r;
		return undefined;
	})

	async function add_lehrer_regel() {
		if (lehrer_regel.value !== undefined)
			return;
		const r = new GostBlockungRegel();
		const regel_typ = GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN
		r.typ = regel_typ.typ;
		r.parameter.add(1);
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
		const wahlen = props.getDatenmanager().getOfFachartMengeFachwahlen(fachart_id).size() || 0;
		const kdiff = props.getErgebnismanager().getOfFachartKursdifferenz(fachart_id);
		return [filtered_by_kursart.value.size(), kdiff, wahlen];
	});

	const blockung_aktiv: ComputedRef<boolean> = computed(() => props.getDatenmanager().daten().istAktiv);

	const toggle_kursdetail_anzeige = () => kursdetail_anzeige.value = !kursdetail_anzeige.value

	const kursOhneBorder = computed(() => {
		return kurseMitKursart.value.size() > 1 && !kursbezeichnung.value.endsWith(kurseMitKursart.value.size().toString()) && !kursdetail_anzeige.value;
	})

</script>
