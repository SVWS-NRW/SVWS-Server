<template>
	<div class="svws-ui-tr -mt-px border-y border-black/10  shadow-inner select-none text-uistatic" :style="{ 'background-color': bgColor }">
		<div class=" pr-3 pl-7 pt-3 pb-4 flex justify-between" :style="{'gridColumn': 'span ' + getDatenmanager().schieneGetListe().size()+5}">
			<div class="flex items-center gap-2">
				<span class="text-sm font-bold">Kurs:</span>
				<svws-ui-button type="icon" @click="removeKurse([kurs.id])" title="Kurs entfernen" class="ml-1" :disabled="apiStatus.pending">
					<span class="icon-uistatic icon i-ri-delete-bin-line" />
				</svws-ui-button>
				<svws-ui-button type="secondary" @click="addKurs(kurs.fach_id, kurs.kursart)" title="Kurs hinzufügen" :disabled="apiStatus.pending"><div class="text-uistatic">Hinzufügen</div></svws-ui-button>
				<svws-ui-button type="secondary" @click="splitKurs(kurs)" :disabled="apiStatus.pending"><div class="text-uistatic">Aufteilen</div></svws-ui-button>
				<div v-if="andereKurse.size > 0" class="w-52">
					<svws-ui-select :model-value="undefined" @update:model-value="kurs2 => (kurs2 !== undefined && kurs2 !== null) && combineKurs(kurs, kurs2)"
						title="Zusammenlegen mit" class="" headless :items="andereKurse" :item-text="i => getDatenmanager().kursGetName(i.id)" :disabled="apiStatus.pending" />
				</div>
				<span class="text-sm font-bold">Externe Schüler:</span>
				<div class="w-24">
					<svws-ui-input-number placeholder="externe Schüler" :model-value="getErgebnismanager().getOfKursAnzahlSchuelerDummy(kurs.id)" @update:model-value="updateExterne" :min="0" :max="50" headless :disabled="apiStatus.pending" />
				</div>
			</div>
			<s-gost-kursplanung-kursansicht-modal-zusatzkraefte :kurs :map-lehrer :get-datenmanager :add-lehrer-regel :add-kurs-lehrer :remove-kurs-lehrer />
			<div class="flex items-center gap-2">
				<span class="text-sm font-bold">Schienen:</span>
				<div class="flex gap-1">
					<svws-ui-button type="icon" @click="removeSchieneKurs(kurs)" size="small" :disabled="(kurs.anzahlSchienen <= 1) || (apiStatus.pending)">
						<span class="icon-uistatic icon-sm i-ri-subtract-line" />
					</svws-ui-button>
					<div class="mx-1">{{ kurs.anzahlSchienen }}</div>
					<svws-ui-button type="icon" @click="addSchieneKurs(kurs)" size="small" :disabled="apiStatus.pending || (kurs.anzahlSchienen >= getDatenmanager().schieneGetAnzahl())">
						<span class="icon-uistatic icon-sm i-ri-add-line" />
					</svws-ui-button>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegelUpdate, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, LehrerListeEintrag } from "@core";
	import type { ApiStatus } from '~/components/ApiStatus';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
		addLehrerRegel: () => Promise<void>;
		removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		removeKurse: (ids: Iterable<number>) => Promise<void>;
		combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs | undefined | null) => Promise<void>;
		splitKurs: (kurs: GostBlockungKurs) => Promise<void>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		anzahlSpalten: number;
		kurs: GostBlockungKurs;
		fachart: number;
		mapLehrer: Map<number, LehrerListeEintrag>;
		bgColor: string;
		apiStatus: ApiStatus;
	}>();

	const andereKurse = computed<Map<number, GostBlockungsergebnisKurs>>(() => {
		const result = new Map<number, GostBlockungsergebnisKurs>();
		for (const k of props.getErgebnismanager().getOfFachartKursmenge(props.fachart))
			if (k.id !== props.kurs.id)
				result.set(k.id, k);
		return result;
	});

	async function updateExterne(anzahl: number | null) {
		if (props.apiStatus.pending)
			return;
		const curr = props.getErgebnismanager().getOfKursAnzahlSchuelerDummy(props.kurs.id);
		if (curr === anzahl || (anzahl === null) || anzahl < 0)
			return;
		const update = props.getErgebnismanager().regelupdateCreate_09_KURS_MIT_DUMMY_SUS_AUFFUELLEN(props.kurs.id, anzahl);
		await props.regelnUpdate(update);
	}

</script>
