<template>
	<template v-if="getDatenmanager().kursGetListeByFachUndKursart(fachwahlen.id, kursart.id).isEmpty() && fachwahlenAnzahl !== 0 && allowRegeln">
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
					Kurs anlegen <i-ri-add-circle-line class="-mr-0.5" />
				</svws-ui-button>
			</div>
		</div>
	</template>
	<template v-else>
		<s-gost-kursplanung-kursansicht-kurs v-for="kurs in getDatenmanager().kursGetListeByFachUndKursart(fachwahlen.id, kursart.id)" :key="kurs.id" :kurs="kurs" :bg-color="bgColor"
			:map-lehrer="mapLehrer" :allow-regeln="allowRegeln" :schueler-filter="schuelerFilter" :get-datenmanager="getDatenmanager"
			:hat-ergebnis="hatErgebnis" :get-ergebnismanager="getErgebnismanager"
			:add-regel="addRegel" :remove-regel="removeRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
			:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer"
			:add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :split-kurs="splitKurs" :combine-kurs="combineKurs" />
	</template>
</template>

<script setup lang="ts">

	import { type ComputedRef, computed } from "vue";
	import type { GostBlockungKurs, GostStatistikFachwahl, LehrerListeEintrag, GostBlockungRegel, GostFaecherManager, GostBlockungKursLehrer,
		GostBlockungsergebnisManager, GostBlockungsdatenManager, GostBlockungsergebnisKurs , GostKursart} from "@core";
	import type { GostKursplanungSchuelerFilter } from "../GostKursplanungSchuelerFilter";
	import type { Config } from "~/components/Config";
	import { ZulaessigesFach } from "@core";

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
		config: Config;
		hatErgebnis: boolean;
		schuelerFilter: GostKursplanungSchuelerFilter | undefined;
		fachwahlen: GostStatistikFachwahl;
		faecherManager: GostFaecherManager;
		fachwahlenAnzahl: number;
		kursart: GostKursart;
		mapLehrer: Map<number, LehrerListeEintrag>;
		allowRegeln: boolean;
	}>();

	const bgColor: ComputedRef<string> = computed(() => ZulaessigesFach.getByKuerzelASD(props.fachwahlen.kuerzelStatistik).getHMTLFarbeRGBA(1.0));

	function toggleSchuelerFilterFachwahl(idFach: number, kursart: GostKursart) {
		if (props.schuelerFilter === undefined)
			return;
		if (props.schuelerFilter.fach.value !== idFach) {
			props.schuelerFilter.kursart.value = kursart;
			props.schuelerFilter.fach.value = idFach;
		} else {
			props.schuelerFilter.reset();
		}
	}

	async function add_kurs(art: GostKursart) {
		await props.addKurs(props.fachwahlen.id, art.id);
	}

</script>
